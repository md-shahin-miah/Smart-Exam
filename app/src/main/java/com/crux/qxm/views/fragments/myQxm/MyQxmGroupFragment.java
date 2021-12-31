package com.crux.qxm.views.fragments.myQxm;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.group.MyGroupAdapter;
import com.crux.qxm.db.models.group.GroupData;
import com.crux.qxm.db.models.group.groupFeed.MyGroup;
import com.crux.qxm.db.models.group.groupFeed.MyGroupFeed;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmGroupFragmentFeature.DaggerMyQxmGroupFragmentComponent;
import com.crux.qxm.di.myQxmGroupFragmentFeature.MyQxmGroupFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
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
import static com.crux.qxm.utils.StaticValues.isMyQxmGroupFragmentRefreshNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmGroupFragment extends Fragment {

    private static final String TAG = "MyQxmGroupFragment";

    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;
    @Inject
    Picasso picasso;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private List<GroupData> feedModelList;
    private MyGroupAdapter feedAdapter;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private boolean isComingFromGroupCreateFragment = false;
    FloatingActionButton fabCreateGroup;
    FloatingActionButton fabCreateList;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvNoGroupMessage)
    AppCompatTextView tvNoGroupMessage;
    @BindView(R.id.RVFeed)
    RecyclerView RVFeed;
    @BindView(R.id.noInternetView)
    View noInternetView;

    private AppCompatTextView tvRetry;


    public MyQxmGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_group, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        context = view.getContext();

        setUpDagger2();
        init();
        noInternetFunctionality();
        initializeFeedAdapter();

        if (feedModelList.size() == 0 || isMyQxmGroupFragmentRefreshNeeded) {
            isMyQxmGroupFragmentRefreshNeeded = false;
            feedAdapter.clear();
            swipeRefreshLayout.setRefreshing(true);
            getFeedItemListNetworkCall();
        } else if(isComingFromGroupCreateFragment){
            isComingFromGroupCreateFragment = false;
            feedAdapter.clear();
            swipeRefreshLayout.setRefreshing(true);
            getFeedItemListNetworkCall();
        }
    }

    private void setUpDagger2() {
        MyQxmGroupFragmentComponent myQxmGroupFragmentComponent =
                DaggerMyQxmGroupFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmGroupFragmentComponent.injectMyQxmGroupFragmentFeature(MyQxmGroupFragment.this);
    }

    private void init() {

        if (feedModelList == null)
            feedModelList = new ArrayList<>();

        realmService = new RealmService(Realm.getDefaultInstance());
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        //getting fab button from activity
        fabCreateGroup = Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateGroup);
        fabCreateList = Objects.requireNonNull(getActivity()).findViewById(R.id.fabCreateList);
        fabCreateGroup.setOnClickListener(view -> {
            isComingFromGroupCreateFragment = true;
            qxmFragmentTransactionHelper.loadCreateGroupFragment();
        });


    }

    private void initializeFeedAdapter() {
        feedAdapter = new MyGroupAdapter(getContext(), feedModelList, getActivity(), picasso, user.getUserId());
        RVFeed.setAdapter(feedAdapter);
        RVFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            feedAdapter.clear();
            getFeedItemListNetworkCall();

        });
    }

    private void getFeedItemListNetworkCall() {

        Call<MyGroupFeed> getMyGroupList = apiService.getMyGroupList(token.getToken(), token.getUserId());
        getMyGroupList.enqueue(new Callback<MyGroupFeed>() {
            @Override
            public void onResponse(@NonNull Call<MyGroupFeed> call, @NonNull Response<MyGroupFeed> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                // TODO:: remove this response code 201 checking from the below if condition
                if (response.code() == 200 || response.code() == 201) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    if (response.body() != null) {

                        List<MyGroup> myGroupList = Objects.requireNonNull(response.body()).getMyGroup();

                        if (myGroupList != null && myGroupList.size() > 0) {

                            tvNoGroupMessage.setVisibility(View.GONE);

                            for (int i = 0; i < myGroupList.size(); i++) {

                                GroupData groupData = new GroupData();
                                groupData.setGroupId(myGroupList.get(i).getGroup().getId());
                                groupData.setGroupName(myGroupList.get(i).getGroup().getGroupInfo().getGroupName());
                                groupData.setGroupImageUrl(myGroupList.get(i).getGroup().getGroupInfo().getGroupImageUrl());
                                groupData.setMyStatus(myGroupList.get(i).getStatus());
                                groupData.setMemberCount(myGroupList.get(i).getGroup().getMemberCount());
                                feedModelList.add(groupData);
                            }

                            // passing feed to the recycler view adapter
                            feedAdapter.notifyDataSetChanged();
                        } else {
                            tvNoGroupMessage.setVisibility(View.VISIBLE);
                        }

                        Log.d(TAG, "Feed: " + feedModelList.toString());
                    } else {
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<MyGroupFeed> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getMyGroupList");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });
    }

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            feedAdapter.clear();
            getFeedItemListNetworkCall();
        });
    }

    //endregion

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    //region onOnPause
    @Override
    public void onPause() {

        // fabCreateGroup might hide when this visible
        //so, make it hide
        fabCreateGroup.setVisibility(View.GONE);
        fabCreateList.setVisibility(View.GONE);
        super.onPause();
    }
    //endregion


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d(TAG, "setUserVisibleHint: Visible");

    }
}
