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
import com.crux.qxm.adapter.notification.AllNotificationAdapter;
import com.crux.qxm.db.models.notification.allNotification.AllNotification;
import com.crux.qxm.db.models.notification.allNotification.NotificationItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.notificationAllNotificationFragmentFeature.AllNotificationFragmentComponent;
import com.crux.qxm.di.notificationAllNotificationFragmentFeature.DaggerAllNotificationFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class AllNotificationFragment extends Fragment {

    // region Fragment-Properties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "QxmNotificationFragment";
    private QxmApiService apiService;
    private Context context;
    private Realm realm;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;

    private AllNotificationAdapter allNotificationAdapter;
    private List<NotificationItem> notificationItems;

    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.tvEmptyMessage)
    AppCompatTextView tvEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVAllNotification)
    RecyclerView RVAllNotification;
    @BindView(R.id.noInternetView)
    View noInternetView;

    // endregion

    public AllNotificationFragment() {
        // Required empty public constructor
    }


    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_notification, container, false);

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

        if (notificationItems.size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            getQxmNotificationNetworkCall();
        }
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        AllNotificationFragmentComponent allNotificationFragmentComponent =
                DaggerAllNotificationFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        allNotificationFragmentComponent.injectAllNotificationFragmentFeature(AllNotificationFragment.this);
    }

    // endregion

    // region Init
    private void init() {

        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        if (notificationItems == null)
            notificationItems = new ArrayList<>();

        allNotificationAdapter = new AllNotificationAdapter(context, notificationItems, getActivity(), picasso, token.getUserId());
        RVAllNotification.setAdapter(allNotificationAdapter);
        RVAllNotification.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            allNotificationAdapter.clear();
            getQxmNotificationNetworkCall();
        });
    }

    private void getQxmNotificationNetworkCall() {
        Call<AllNotification> getMyQxmFeed = apiService.getAllNotificationList(token.getToken(), token.getUserId());
        getMyQxmFeed.enqueue(new Callback<AllNotification>() {
            @Override
            public void onResponse(@NonNull Call<AllNotification> call, @NonNull Response<AllNotification> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");
                    AllNotification allNotification = response.body();
                    if (allNotification != null){
                        if(allNotification.getAll().size() == 0){
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                        }else{
                            tvEmptyMessage.setVisibility(View.GONE);

                            Collections.reverse(allNotification.getAll());
                            notificationItems.addAll(allNotification.getAll());
                        }
                    }

                    // passing feed to the recycler view adapter
                    allNotificationAdapter.notifyDataSetChanged();

                    Log.d(TAG, "MyQxmFeed: " + notificationItems.toString());

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
            public void onFailure(@NonNull Call<AllNotification> call, @NonNull Throwable t) {
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
            allNotificationAdapter.clear();
            getQxmNotificationNetworkCall();
        });
    }

    //endregion
}