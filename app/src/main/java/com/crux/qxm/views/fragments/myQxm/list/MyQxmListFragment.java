package com.crux.qxm.views.fragments.myQxm.list;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.list.MyListAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmListFragmentFeature.DaggerMyQxmListFragmentComponent;
import com.crux.qxm.di.myQxmListFragmentFeature.MyQxmListFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmCreateList;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmListFragment extends Fragment implements QxmCreateList.QxmCreateListListeners {

    private static final String TAG = "MyQxmListFragment";

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    FloatingActionButton fabCreateList;
    FloatingActionButton fabCreateGroup;
    @BindView(R.id.tvNoListMessage)
    AppCompatTextView tvNoListMessage;
    @BindView(R.id.rvMyList)
    RecyclerView rvMyList;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.noInternetView)
    View noInternetView;

    private QxmApiService apiService;
    private Context context;
    private Realm realm;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private MyListAdapter myListAdapter;
    private List<com.crux.qxm.db.models.myQxm.list.List> myLists;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    AppCompatTextView tvRetry;


    public MyQxmListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        setUpDagger2();
        init();
        noInternetFunctionality();
        initializeListAdapter();
        if (myLists.isEmpty())
            getMyAllListNetworkCall();

    }

    // region setUpDagger2
    private void setUpDagger2() {
        MyQxmListFragmentComponent myQxmListFragmentComponent =
                DaggerMyQxmListFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmListFragmentComponent.injectMyQxmListFragmentFeature(MyQxmListFragment.this);
    }
    //endregion

    //region init
    private void init() {

        if (myLists == null)
            myLists = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();
        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        //getting fab button from activity
        fabCreateList = Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateList);
        fabCreateGroup = Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateGroup);
        fabCreateList.setOnClickListener(view -> {

            QxmCreateList createList = new QxmCreateList(view.getContext(), apiService, token.getUserId(), token.getToken(), this);
            createList.createListDialogShow();
        });

    }
    //endregion

    //region initializeListAdapter
    private void initializeListAdapter() {


        myListAdapter = new MyListAdapter(getContext(), myLists, token, new MyListAdapter.MyListAdapterListener() {

            @Override
            public void onMyListSelected(com.crux.qxm.db.models.myQxm.list.List list) {

                //going to Single List through click
                Log.d(TAG, "initializeListAdapter listId: " + list.getId());
                new QxmFragmentTransactionHelper(getActivity()).loadSingleListQxmFragment(list.getId(), list.getListSettings().getListName());

            }

            @Override
            public void onMyListLongClickListener(com.crux.qxm.db.models.myQxm.list.List list) {

                QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

                qxmAlertDialogBuilder.setMessage("Are you sure, you really want to delete this list!");
                qxmAlertDialogBuilder.setTitle("Delete List");
                qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);

                qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton("DELETE", (dialog, which) ->
                        deleteListNetworkCall(token.getToken(),token.getUserId(),list));

                qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton("NO", null);

                qxmAlertDialogBuilder.getAlertDialogBuilder().show();
            }
        });

        rvMyList.setAdapter(myListAdapter);
        rvMyList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        rvMyList.setItemAnimator(new DefaultItemAnimator());
        swipeRL.setOnRefreshListener(() -> {
            myListAdapter.clear();
            getMyAllListNetworkCall();

        });
    }
    //endregion

    //region getMyAllListNetworkCall
    private void getMyAllListNetworkCall() {

        Call<List<com.crux.qxm.db.models.myQxm.list.List>> getMyAllList = apiService.getAllList(token.getToken(), user.getUserId());

        getMyAllList.enqueue(new Callback<List<com.crux.qxm.db.models.myQxm.list.List>>() {
            @Override
            public void onResponse(@NonNull Call<List<com.crux.qxm.db.models.myQxm.list.List>> call, @NonNull Response<List<com.crux.qxm.db.models.myQxm.list.List>> response) {
                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {

                        List<com.crux.qxm.db.models.myQxm.list.List> myListsFromApi = response.body();
                        myLists.addAll(myListsFromApi);
                        Log.d(TAG, "onResponse AllLists: " + myListsFromApi);
                        myListAdapter.notifyDataSetChanged();
                        swipeRL.setRefreshing(false);

                        // if user has no list, show no list message
                        if (myLists.isEmpty())
                            tvNoListMessage.setVisibility(View.VISIBLE);
                        else tvNoListMessage.setVisibility(View.GONE);

                    } else {

                        Log.d(TAG, "onResponse getMyAllList : " + response.body());
                        Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                        swipeRL.setRefreshing(false);
                    }


                } else {

                    Log.d(TAG, "onResponse getMyAllList response code :" + response.code());
                    Toast.makeText(context, "Something went wrong,try again later!", Toast.LENGTH_SHORT).show();
                    swipeRL.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<com.crux.qxm.db.models.myQxm.list.List>> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure getMyAllList :" + t.getMessage());
                Toast.makeText(context, "Network error, connect with internet!", Toast.LENGTH_SHORT).show();
                swipeRL.setRefreshing(false);
                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context)){
                    Log.d(TAG, "onPageSelected: ****GoneEverything****");
                    Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateList).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.GONE);
                    noInternetView.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    //endregion


    //region delete my single list
    private void deleteListNetworkCall(String token,String userId,com.crux.qxm.db.models.myQxm.list.List list){

        Call<QxmApiResponse> deleteList = apiService.deleteSingleList(token,list.getId(),userId);

        deleteList.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteListNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if(response.code() == 201){
                    Toast.makeText(context, "List deleted successfully", Toast.LENGTH_SHORT).show();
                    //viewPagerPosition = 3 (Group)

                    myLists.remove(list);
                    myListAdapter.notifyDataSetChanged();

                }else if(response.code() == 403){
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }else{
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteListNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {

            Log.d(TAG, "onPageSelected: create list fav icon visible");
            Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateList).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.GONE);

            noInternetView.setVisibility(GONE);
            swipeRL.setRefreshing(true);
            myListAdapter.clear();
            getMyAllListNetworkCall();
        });
    }

    //endregion

    //region onOnPause
    @Override
    public void onPause() {

        // fabCreateList  might hide when this visible
        //so, make it hide
        fabCreateList.setVisibility(View.GONE);
        fabCreateGroup.setVisibility(View.GONE);
        super.onPause();
    }
    //endregion


    @Override
    public void onResume() {

        Log.d(TAG, "onResume: Called" + TAG);
        super.onResume();

        // fabCreateList might hide when this fragment
//        fabCreateList.setVisibility(View.VISIBLE);
//        fabCreateGroup.setVisibility(View.GONE);
    }

    @Override
    public void onListCreatedListener() {
        myListAdapter.clear();
        getMyAllListNetworkCall();
    }
}
