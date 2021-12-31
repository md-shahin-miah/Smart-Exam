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
import com.crux.qxm.adapter.myQxm.myPoll.MyPollAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myPollFragmentFeature.DaggerMyPollFragmentComponent;
import com.crux.qxm.di.myPollFragmentFeature.MyPollFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.recyclerViewPaginationHelper.EndlessRecyclerViewScrollListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

public class MyQxmMyPollFragment extends Fragment {

    //region Fragment-Properties
    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private static final String TAG = "MyQxmMyPollFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private QxmToken token;
    private List<FeedData> myPollList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private MyPollAdapter myPollAdapter;
    //endregion

    //region Fragment-ViewsBindWithButterKnife
    @BindView(R.id.tvMyPollEmptyMessage)
    AppCompatTextView tvMyPollEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvMyPoll)
    RecyclerView rvMyPoll;
    @BindView(R.id.noInternetView)
    View noInternetView;
    //endregion

    //region Fragment-constructor
    public MyQxmMyPollFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Override-onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_poll, container, false);

        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment

        return view;
    }
    //endregion

    //region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        setUpDagger2(context);
        init();
        noInternetFunctionality();

        if (myPollList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getMyPollNetworkCall(0);
        }
    }
    //endregion

    //region Method-setUpDagger2
    private void setUpDagger2(Context context) {
        MyPollFragmentComponent myPollFragmentComponent = DaggerMyPollFragmentComponent.builder()
                .appComponent((App.get((AppCompatActivity) context)).getAppComponent())
                .build();

        myPollFragmentComponent.injectMyPollFragmentFeature(MyQxmMyPollFragment.this);
    }
    //endregion

    //region Method-init
    private void init() {
        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (myPollList == null)
            myPollList = new ArrayList<>();

        myPollAdapter = new     MyPollAdapter(context, myPollList, picasso, getActivity(), qxmFragmentTransactionHelper, apiService, token.getUserId(), token.getToken());

        rvMyPoll.setAdapter(myPollAdapter);
        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(context);
        rvMyPoll.setLayoutManager(rvLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myPollAdapter.clear();
            getMyPollNetworkCall(0);
        });

        //region recycler view scroll listener

        rvMyPoll.addOnScrollListener(new EndlessRecyclerViewScrollListener(rvLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.d(TAG, "onLoadMore page: " + page);

                getMyPollNetworkCall(page);

            }
        });

        //endregion
    }
    //endregion

    //region Method-getMyPollNetworkCall
    private void getMyPollNetworkCall(int paginationIndex) {
        Log.d(TAG, "getMyPollNetworkCall: paginationIndex= " + paginationIndex);

        Call<List<FeedData>> getMyPoll = apiService.getMyPoll(token.getToken(), token.getUserId(), paginationIndex);

        getMyPoll.enqueue(new Callback<List<FeedData>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedData>> call, @NonNull Response<List<FeedData>> response) {
                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200 || response.code() == 201) {

                    Log.d(TAG, "onResponse: GetMyPoll network call success");

                    //hiding noInternetView
                    noInternetView.setVisibility(View.GONE);

                    List<FeedData> feedItemList = response.body();

                    myPollList.addAll(feedItemList);

                    if (myPollList.size() == 0) tvMyPollEmptyMessage.setVisibility(View.VISIBLE);
                    else tvMyPollEmptyMessage.setVisibility(GONE);
                    // passing feed to the recycler view adapter

                    myPollAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);


                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse: GetMyPoll network call failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedData>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: GetMyPollGetMyPoll");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);

                //showing noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //endregion

    //region NoInternetFunctionality
    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            myPollAdapter.clear();
            getMyPollNetworkCall(0);
        });
    }
    //endregion
}
