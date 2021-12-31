package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.PublicGroupAdapter;
import com.crux.qxm.db.models.search.group.SearchedGroup;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.publicGroupFragmentFeature.DaggerPublicGroupFragmentComponent;
import com.crux.qxm.di.publicGroupFragmentFeature.PublicGroupFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicGroupFragment extends Fragment {


    private static final String TAG = "PublicGroupFragment";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private Context context;
    private Realm realm;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private QxmApiService apiService;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvToolbarMsg)
    AppCompatTextView tvToolbarMsg;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.rvGroupList)
    RecyclerView rvGroupList;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;

    private List<SearchedGroup> groupList;
    private PublicGroupAdapter publicGroupAdapter;

    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // region Fragment-NewInstance
    public static PublicGroupFragment newInstance() {
        Bundle args = new Bundle();
        PublicGroupFragment fragment = new PublicGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    public PublicGroupFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_public_group, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        setUpDagger2();
        init();
        viewOnClickListener();
        initializeFeedAdapter();
        if (groupList.size() == 0){
            swipeRL.setRefreshing(true);
            getUserPublicGroupNetworkCall();
        }


    }

    //region setUpDagger2
    private void setUpDagger2() {
        PublicGroupFragmentComponent publicGroupFragmentComponent =
                DaggerPublicGroupFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        publicGroupFragmentComponent.injectPublicFragmentFeature(PublicGroupFragment.this);
    }
    //endregion

    //region init
    private void init() {

        if (groupList == null)
            groupList = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();
        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

    }
    //endregion

    //region initializeFeedAdapter
    private void initializeFeedAdapter() {
        publicGroupAdapter = new PublicGroupAdapter(getContext(), groupList, getActivity(), picasso, searchedGroup ->
                qxmFragmentTransactionHelper.loadViewQxmGroupFragment(searchedGroup.getGroupId(),searchedGroup.getSearchedGroupInfo().getGroupName()));
        rvGroupList.setAdapter(publicGroupAdapter);
        rvGroupList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),DividerItemDecoration.VERTICAL));
        rvGroupList.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRL.setOnRefreshListener(() -> {
            publicGroupAdapter.clear();
            getUserPublicGroupNetworkCall();

        });
    }
    //endregion

    //region getUserPublicGroupNetworkCall
    private void getUserPublicGroupNetworkCall() {

        Call<List<SearchedGroup>> getUserPublicGroup = apiService.getUserPublicGroupList(token.getToken(), token.getUserId());


        getUserPublicGroup.enqueue(new Callback<List<SearchedGroup>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchedGroup>> call, @NonNull Response<List<SearchedGroup>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());


                if (response.code() == 200 ){

                    if(response.body()!= null){

                        groupList.clear();
                        groupList.addAll(response.body());
                        publicGroupAdapter.notifyDataSetChanged();
                        swipeRL.setRefreshing(false);

                        if(groupList.size()==0) {
                            tvNotFound.setVisibility(View.VISIBLE);
                            tvNotFound.setText(R.string.no_public_group_found);
                        }
                        else tvNotFound.setVisibility(View.GONE);

                    }
                    else Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(context, "Something went wrong during loading group list!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SearchedGroup>> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: getMyGroupList");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRL.setRefreshing(false);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                
            }
        });


    }

    //endregion

    //region view onclickListener
    private void viewOnClickListener(){

        // going previous fragment when click on back press
        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getFragmentManager() != null) {
                Log.d(TAG, "pop SingleListQxmFragment fragment");
                getFragmentManager().popBackStack();
            }
        });

        // search icon click action
        ivSearch.setOnClickListener(v->{

            Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
        });

    }
    //endregion
}
