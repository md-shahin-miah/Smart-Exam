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
import com.crux.qxm.adapter.notification.PrivateQxmNotificationAdapter;
import com.crux.qxm.db.models.enroll.peopleEnrolled.PeopleEnrolledData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.notificationPrivateQxmNotificationFragmentFeature.DaggerPrivateQxmNotificationFragmentComponent;
import com.crux.qxm.di.notificationPrivateQxmNotificationFragmentFeature.PrivateQxmNotificationFragmentComponent;
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
public class PrivateQxmNotificationFragment extends Fragment {

    // region Fragment-Properties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "ResultNotificationFragm";
    private QxmApiService apiService;
    private Context context;
    private Realm realm;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;

    private PrivateQxmNotificationAdapter privateQxmNotificationAdapter;
    private List<PeopleEnrolledData> peopleEnrolledFeedData;

    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.tvEmptyMessage)
    AppCompatTextView tvEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVPrivateQxmNotification)
    RecyclerView RVPrivateQxmNotification;
    @BindView(R.id.noInternetView)
    View noInternetView;

    // endregion

    // region Fragment-Constructor
    public PrivateQxmNotificationFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_private_qxm_notification, container, false);

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

        setUpDagger2(context);

        init();

        noInternetFunctionality();

        if (peopleEnrolledFeedData.size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            getEnrollParticipatedFeedNetworkCall();
        }
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        PrivateQxmNotificationFragmentComponent privateQxmNotificationFragmentComponent =
                DaggerPrivateQxmNotificationFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();
        privateQxmNotificationFragmentComponent.injectPrivateQxmNotificationFragmentFeature(PrivateQxmNotificationFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        if (peopleEnrolledFeedData == null)
            peopleEnrolledFeedData = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        privateQxmNotificationAdapter = new PrivateQxmNotificationAdapter(context, peopleEnrolledFeedData, getActivity(), picasso);
        RVPrivateQxmNotification.setAdapter(privateQxmNotificationAdapter);
        RVPrivateQxmNotification.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            privateQxmNotificationAdapter.clear();
            getEnrollParticipatedFeedNetworkCall();

        });
    }

    // endregion

    // region GetEnrollParticipatedFeedNetworkCall

    private void getEnrollParticipatedFeedNetworkCall() {

        Call<List<PeopleEnrolledData>> getMyQxmFeed = apiService.getPeopleEnrolledList(token.getToken(), token.getUserId());
        getMyQxmFeed.enqueue(new Callback<List<PeopleEnrolledData>>() {
            @Override
            public void onResponse(@NonNull Call<List<PeopleEnrolledData>> call, @NonNull Response<List<PeopleEnrolledData>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");
                    List<PeopleEnrolledData> peopleEnrolledData = response.body();
                    if (peopleEnrolledData != null){

                        if(peopleEnrolledData.size() == 0){
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                        }else{
                            tvEmptyMessage.setVisibility(View.GONE);
                            peopleEnrolledFeedData.addAll(peopleEnrolledData);
                        }
                    }

                    // passing feed to the recycler view adapter
                    privateQxmNotificationAdapter.notifyDataSetChanged();

                    Log.d(TAG, "MyQxmFeed: " + peopleEnrolledFeedData.toString());

                    swipeRefreshLayout.setRefreshing(false);

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {

                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse: Feed network call failed");

                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PeopleEnrolledData>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getFeedItem");
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
            privateQxmNotificationAdapter.clear();
            getEnrollParticipatedFeedNetworkCall();
        });
    }

    //endregion
}
