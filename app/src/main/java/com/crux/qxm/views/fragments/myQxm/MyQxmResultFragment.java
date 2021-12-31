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
import com.crux.qxm.adapter.myQxm.myQxmResult.MyQxmResultAdapter;
import com.crux.qxm.db.models.myQxmResult.MyQxmResult;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmPerformanceFragmentFeature.DaggerMyQxmPerformanceFragmentComponent;
import com.crux.qxm.di.myQxmPerformanceFragmentFeature.MyQxmPerformanceFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmResultFragment extends Fragment implements MyQxmResultAdapter.MyQxmResultAdapterListener {

    private static final String TAG = "MyQxmResultFragment";
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private MyQxmResultAdapter myQxmResultAdapter;
    //    private List<FullResult> fullResultList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private List<MyQxmResult> myQxmResultList;


    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVPerformance)
    RecyclerView RVPerformance;
    @BindView(R.id.noInternetView)
    View noInternetView;

    AppCompatTextView tvRetry;

    public MyQxmResultFragment() {
        // Required empty public constructor
    }


    public static MyQxmResultFragment newInstance() {

        Bundle args = new Bundle();

        MyQxmResultFragment fragment = new MyQxmResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_result, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        context = view.getContext();

        setUpDagger2();

        init();

        noInternetFunctionality();

        if (myQxmResultList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getMyQxmPerformanceListNetworkCall();
        }
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        MyQxmPerformanceFragmentComponent myQxmPerformanceFragmentComponent =
                DaggerMyQxmPerformanceFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmPerformanceFragmentComponent.injectMyQxmPerformanceFragmentFeature(MyQxmResultFragment.this);
    }

    // endregion

    // region Init

    private void init() {

        realmService = new RealmService(Realm.getDefaultInstance());
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (myQxmResultList == null) {
            myQxmResultList = new ArrayList<>();
        }

//        if(fullResultList == null)
//            fullResultList = new ArrayList<>();

        myQxmResultAdapter = new MyQxmResultAdapter(context, picasso, getActivity(), myQxmResultList, this);

        RVPerformance.setAdapter(myQxmResultAdapter);
        RVPerformance.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myQxmResultAdapter.clear();
            getMyQxmPerformanceListNetworkCall();

        });

    }

    private void getMyQxmPerformanceListNetworkCall() {

        Call<List<MyQxmResult>> getMyQxmResultNetworkCall = apiService.getMyQxmResult(token.getToken(), token.getUserId());

        getMyQxmResultNetworkCall.enqueue(new Callback<List<MyQxmResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyQxmResult>> call, @NonNull Response<List<MyQxmResult>> response) {
                Log.d(TAG, "onResponse: getMyQxmPerformanceListNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: success");

                    if (response.body() != null) {

                        myQxmResultList.addAll(response.body());

                        if (myQxmResultList.size() == 0) {
                            tvFeedEmptyMessage.setVisibility(View.VISIBLE);
                            RVPerformance.setVisibility(View.GONE);
                        } else {
                            tvFeedEmptyMessage.setVisibility(View.GONE);
                            RVPerformance.setVisibility(View.VISIBLE);
                        }

                        myQxmResultAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "onResponse: response body is null");
                        Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {

                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<MyQxmResult>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getMyQxmPerformanceListNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });


        /*if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        // for testing purpose
        tvFeedEmptyMessage.setVisibility(View.VISIBLE);
        RVPerformance.setVisibility(View.GONE);*/
    }

    @Override
    public void onItemSelected(MyQxmResult qxmResult) {
        if (qxmResult.getFeedViewType().equals(StaticValues.FEED_VIEW_TYPE_QXM))
            qxmFragmentTransactionHelper.loadFullResultFragment(qxmResult.getQxmId());
        else if (qxmResult.getFeedViewType().equals(StaticValues.FEED_VIEW_TYPE_SINGLE_MCQ))
            qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(qxmResult.getQxmId());
    }

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            myQxmResultAdapter.clear();
            getMyQxmPerformanceListNetworkCall();
        });
    }

    //endregion


}
