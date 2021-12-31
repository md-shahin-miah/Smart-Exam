package com.crux.qxm.views.fragments.acitivityLog;


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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.activityLog.ActivityLogDateWiseContainerAdapter;
import com.crux.qxm.db.models.activityLog.ActivitiesItem;
import com.crux.qxm.db.models.activityLog.ActivityLog;
import com.crux.qxm.db.models.activityLog.ActivityLogDateContainer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.activityLogFragmentFeature.ActivityLogFragmentComponent;
import com.crux.qxm.di.activityLogFragmentFeature.DaggerActivityLogFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
public class ActivityLogFragment extends Fragment {


    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "ActivityLogFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;

    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    private ActivityLogDateWiseContainerAdapter dateWiseContainerAdapter;
    private List<ActivityLogDateContainer> activityLogContainerList;
    private List<ActivitiesItem> activitiesItemList;
    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVActivityLog)
    RecyclerView RVActivityLog;
    // endregion

    // region Fragment-Constructor
    public ActivityLogFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Fragment-NewInstance

    public static ActivityLogFragment newInstance() {

        Bundle args = new Bundle();

        ActivityLogFragment fragment = new ActivityLogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_log, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
    // endregion

    // region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setUpDagger2(view.getContext());

        init(view);

        if (activitiesItemList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getActivityLogListNetworkCall();
        }

        initializeClickListeners();


    }
    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        ActivityLogFragmentComponent activityLogFragmentComponent =
                DaggerActivityLogFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        activityLogFragmentComponent.injectActivityLogFragmentFeature(ActivityLogFragment.this);
    }

    // endregion

    // region Init

    private void init(View view) {
        context = view.getContext();

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if(activityLogContainerList == null) activityLogContainerList = new ArrayList<>();

        if (activitiesItemList == null) activitiesItemList = new ArrayList<>();

        dateWiseContainerAdapter = new ActivityLogDateWiseContainerAdapter(context, qxmFragmentTransactionHelper, activityLogContainerList, picasso, apiService, token);
        RVActivityLog.setAdapter(dateWiseContainerAdapter);
        RVActivityLog.setLayoutManager(new LinearLayoutManager(context));
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            dateWiseContainerAdapter.clear();
            activitiesItemList.clear();
            getActivityLogListNetworkCall();
        });
    }

    // endregion

    // region getActivityLogListNetworkCall

    private void getActivityLogListNetworkCall() {

        Call<ActivityLog> getUserActivityLog = apiService.getUserActivityLog(token.getToken(), token.getUserId());

        getUserActivityLog.enqueue(new Callback<ActivityLog>() {
            @Override
            public void onResponse(@NonNull Call<ActivityLog> call, @NonNull Response<ActivityLog> response) {
                Log.d(TAG, "onResponse: getActivityLogListNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());


                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: success");

                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: response.body = " + response.body().toString());

                        activitiesItemList.addAll(response.body().getActivities());

                        // do date wise container work...
                        populateActivityItemListInDateWise();

                        dateWiseContainerAdapter.notifyDataSetChanged();

                    }

                } else if (response.code() == 403) {
                    Log.d(TAG, "onResponse: user session expired");
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: getActivityLogListNetworkCall failed. ");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                }

                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ActivityLog> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getActivityLogListNetworkCall");
                Log.d(TAG, "onFailure: Error Message: " + t.getMessage());
                Log.d(TAG, "onFailure: Error Stacktrace: " + Arrays.toString(t.getStackTrace()));

                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Network error. Please try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateActivityItemListInDateWise() {
        Calendar calendar = Calendar.getInstance();

        String todayDate = TimeFormatString.getDate(calendar.getTimeInMillis());

        calendar.add(Calendar.DATE, -1);
        String yesterdayDate = TimeFormatString.getDate(calendar.getTimeInMillis());

        String previousLogDate = "";


        ActivityLogDateContainer containerToday= new ActivityLogDateContainer();
        containerToday.setDate("Today");
        containerToday.setActivitiesItemList(new ArrayList<>());
        activityLogContainerList.add(0, containerToday);

        ActivityLogDateContainer containerYesterday= new ActivityLogDateContainer();
        containerYesterday.setDate("Yesterday");
        containerYesterday.setActivitiesItemList(new ArrayList<>());
        activityLogContainerList.add(1, containerYesterday);



        for (int i = 0, j = 1; i < activitiesItemList.size(); i++) {
            if (QxmStringIntegerChecker.isLongInt(activitiesItemList.get(i).getTime())) {

                String logDate = TimeFormatString.getDate(Long.parseLong(
                        activitiesItemList.get(i).getTime()));

                if (logDate.equals(todayDate)) {
                    activityLogContainerList.get(0).getActivitiesItemList().add(activitiesItemList.get(i));
                } else if (logDate.equals(yesterdayDate)) {
                    activityLogContainerList.get(1).getActivitiesItemList().add(activitiesItemList.get(i));
                } else {
                    if (logDate.equals(previousLogDate)) {
                        activityLogContainerList.get(j).getActivitiesItemList().add(activitiesItemList.get(i));
                    } else {
                        j++;
                        previousLogDate = logDate;
                        ActivityLogDateContainer container = new ActivityLogDateContainer();
                        container.setDate(logDate);
                        container.setActivitiesItemList(new ArrayList<>());
                        container.getActivitiesItemList().add(0, activitiesItemList.get(i));

                        activityLogContainerList.add(j, container);
                    }
                }

            }
        }
        // Remove Today if today activity list is empty
        if(activityLogContainerList.get(0).getActivitiesItemList().size() == 0){

            // Remove Yesterday if yesterday activity list is empty
            if(activityLogContainerList.get(1).getActivitiesItemList().size() == 0)
                activityLogContainerList.remove(1);

            activityLogContainerList.remove(0);
        }

        // Remove Yesterday if yesterday activity list is empty
        if(activityLogContainerList.size() > 1 && activityLogContainerList.get(1).getActivitiesItemList().size() == 0)
            activityLogContainerList.remove(1);

        Log.d(TAG, "populateActivityItemListInDateWise: ContainerListSize = " + activityLogContainerList.size());
        Log.d(TAG, "populateActivityItemListInDateWise: ContainerList = " + activityLogContainerList.toString());

    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    // endregion

    // region Override-OnDestroyView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    // endregion


}
