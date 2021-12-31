package com.crux.qxm.views.fragments.notification;

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
import com.crux.qxm.adapter.notification.EvaluationNotificationAdapter;
import com.crux.qxm.db.models.notification.EvaluationNotification;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.notificationEvaluationNotificationFragmentFeature.DaggerEvaluationNotificationFragmentComponent;
import com.crux.qxm.di.notificationEvaluationNotificationFragmentFeature.EvaluationNotificationFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluationNotificationFragment extends Fragment {

    // region Fragment-Properties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "EvaluationNotificationF";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private QxmToken token;

    private EvaluationNotificationAdapter evaluationNotificationAdapter;
    private List<EvaluationNotification> evaluationNotificationList;

    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.tvEmptyMessage)
    AppCompatTextView tvEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVEvaluateQxmNotification)
    RecyclerView RVEvaluateQxmNotification;
    @BindView(R.id.noInternetView)
    View noInternetView;

    // endregion

    public EvaluationNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluation_notification, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        context = view.getContext();

        setUpDagger2(context);

        init();

        noInternetFunctionality();

        if (evaluationNotificationList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getEvaluationNotificationNetworkCall();
        }
    }

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        EvaluationNotificationFragmentComponent evaluationNotificationFragmentComponent =
                DaggerEvaluationNotificationFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();
        evaluationNotificationFragmentComponent.injectEvaluationNotificationFragmentFeature(EvaluationNotificationFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        if (evaluationNotificationList == null)
            evaluationNotificationList = new ArrayList<>();

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        evaluationNotificationAdapter = new EvaluationNotificationAdapter(getContext(), evaluationNotificationList, getActivity(), picasso);
        RVEvaluateQxmNotification.setAdapter(evaluationNotificationAdapter);
        RVEvaluateQxmNotification.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            evaluationNotificationAdapter.clear();
            getEvaluationNotificationNetworkCall();

        });
    }


    // endregion

    // region GetEnrollParticipatedFeedNetworkCall

    private void getEvaluationNotificationNetworkCall() {
        Call<List<EvaluationNotification>> getEvaluationNotificationList = apiService.getEvaluationNotificationList(token.getToken(), token.getUserId());

        getEvaluationNotificationList.enqueue(new Callback<List<EvaluationNotification>>() {
            @Override
            public void onResponse(@NonNull Call<List<EvaluationNotification>> call, @NonNull Response<List<EvaluationNotification>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: EvaluationNotificationNetworkCall network call success");
                    List<EvaluationNotification> notificationList = response.body();
                    if (notificationList != null){
                        if(notificationList.size() == 0){
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                        }else{
                            tvEmptyMessage.setVisibility(View.GONE);
                            evaluationNotificationList.addAll(notificationList);
                        }
                    }

                    // passing feed to the recycler view adapter
                    evaluationNotificationAdapter.notifyDataSetChanged();

                    Log.d(TAG, "MyQxmFeed: " + evaluationNotificationList.toString());

                    swipeRefreshLayout.setRefreshing(false);

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {

                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse: EvaluationNotificationNetworkCall network call failed");

                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EvaluationNotification>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: EvaluationNotificationNetworkCall ");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
            evaluationNotificationAdapter.clear();
            getEvaluationNotificationNetworkCall();
        });
    }

    //endregion
}
