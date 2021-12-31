package com.crux.qxm.views.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.BuildConfig;
import com.crux.qxm.Onboarding.OnboardingActivity;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.homeFragmentFeature.DaggerHomeFragmentComponent;
import com.crux.qxm.di.homeFragmentFeature.HomeFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.DebugKeyHashGenerator;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.appUpdateAndMaintenanceHelper.AppUpdateDialog;
import com.crux.qxm.utils.appUpdateAndMaintenanceHelper.ServerMaintenanceDialog;
import com.crux.qxm.utils.recyclerViewPaginationHelper.EndlessRecyclerViewScrollListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.FILTER_PRIVATE_QXMS;
import static com.crux.qxm.utils.StaticValues.SINGLE_QXM_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.isHomeFeedRefreshingNeeded;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "HomeFragment";

    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private List<Object> feedModelList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private UserActivitySessionTracker activitySessionTracker;

    ActionBarDrawerToggle toggle;
    private AppCompatTextView tvGotoProfile;
    private LinearLayoutCompat nav_llNotification;
    private LinearLayoutCompat nav_llEvaluateQxm;
    private LinearLayoutCompat nav_llPerformance;
    private LinearLayoutCompat nav_llActivity;
    private LinearLayoutCompat nav_llFollowers;
    private LinearLayoutCompat nav_llGroups;
    private LinearLayoutCompat nav_llPrivateQxm;
    private LinearLayoutCompat nav_llMyEnrollment;
    //private LinearLayoutCompat nav_llcCreatePoll;

    private AppCompatTextView nav_tvSettings, nav_tvReportaBug, nav_tvAppintroBoarding;
    private AppCompatTextView nav_tvGiveFeedBack;
    private AppCompatTextView nav_tvShareApp;
    private AppCompatTextView nav_tvTermsAndPrivacyPolicy;
    private AppCompatTextView nav_tvHelpAndSupport;
    private AppCompatTextView nav_tvLogout;

    AppCompatTextView tvDrawerUserName;
    AppCompatImageView ivDrawerUserImage;

    private LinearLayoutManager rvLayoutManager;
    private FeedAdapter feedAdapter;
    Parcelable recyclerViewState;
    private AlertDialog apkUpdateAlertDialog;
    private AlertDialog serverStatusAlertDialog;

    @BindView(R.id.noInternetView)
    View noInternetView;
    @BindView(R.id.ivNotification)
    AppCompatImageView ivNotification;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.toolbar_home_fragment)
    Toolbar toolbar_home_fragment;
    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.RVFeed)
    RecyclerView RVFeed;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;

    //region newInstance
    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    // region Fragment-Constructor

    public HomeFragment() {
        // Required empty public constructor
    }


    // endregion

    // region onCreateView


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DebugKeyHashGenerator.printHashKey(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_with_drawer, container, false);

        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment

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

        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);

        // checkIfApkUpdatedApkAvailable();
        //new AppUpdateDialog(apkUpdateAlertDialog).checkIfApkUpdatedApkAvailable(this);
        // checkIfServerUnderMaintenance();


        setClickListeners();

        if (feedModelList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getFeedItemListNetworkCall(0);
        }

        // setting user profile info in respected views


        Log.d(TAG, "onViewCreated localUser :" + user);

        if (user != null) {

            if (user.getProfilePic() != null) {

                Log.d(TAG, "onViewCreated user local image: " + user.getProfilePic());
                picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivUserImage);
                picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivDrawerUserImage);
            }
            tvDrawerUserName.setText(user.getFullName());

        }
        // endregion


    }

    // endregion

    // region SetupDagger2

    private void setUpDagger2(Context context) {

        HomeFragmentComponent homeFragmentComponent =
                DaggerHomeFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        homeFragmentComponent.injectHomeFragmentFeature(HomeFragment.this);
    }

    //endregion

    // region Init

    private void init() {

        toggle = setupDrawerToggle();
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (feedModelList == null)
            feedModelList = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();

        Log.d(TAG, "init user: " + user.toString());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        activitySessionTracker = realmService.getUserActivitySessionTrackerData();

        if (getActivity() != null)
            feedAdapter = new FeedAdapter(context, feedModelList, getActivity(), picasso, token.getUserId(), token.getToken(), apiService, this);

        rvLayoutManager = new LinearLayoutManager(context);
        RVFeed.setAdapter(feedAdapter);
        RVFeed.setLayoutManager(rvLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            feedAdapter.clear();
            getFeedItemListNetworkCall(0);

        });

        //region recycler view scroll listener

        RVFeed.addOnScrollListener(new EndlessRecyclerViewScrollListener(rvLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.d(TAG, "onLoadMore page: " + page);

                getFeedItemListNetworkCall(page);

            }
        });

        //endregion


        // we are using header view as whole layout of drawer
        View navDrawerView = navView.getHeaderView(0);
        ivDrawerUserImage = navDrawerView.findViewById(R.id.ivDrawerUserImage);
        tvDrawerUserName = navDrawerView.findViewById(R.id.tvDrawerUserName);
        tvGotoProfile = navDrawerView.findViewById(R.id.tvGotoProfile);

        nav_llNotification = navDrawerView.findViewById(R.id.llNotification);
        nav_llEvaluateQxm = navDrawerView.findViewById(R.id.llEvaluation);
        nav_llPerformance = navDrawerView.findViewById(R.id.llPerformance);
        nav_llActivity = navDrawerView.findViewById(R.id.llActivity);
        nav_llFollowers = navDrawerView.findViewById(R.id.llFollowers);
        nav_llGroups = navDrawerView.findViewById(R.id.llGroups);
        nav_llPrivateQxm = navDrawerView.findViewById(R.id.llPrivateQxm);
        nav_llMyEnrollment = navDrawerView.findViewById(R.id.llMyEnrollment);

        nav_tvSettings = navDrawerView.findViewById(R.id.tvSettings);
        nav_tvReportaBug = navDrawerView.findViewById(R.id.tvReport_a_bug);
        nav_tvShareApp = navDrawerView.findViewById(R.id.tvShareApp);
        nav_tvGiveFeedBack = navDrawerView.findViewById(R.id.tvGiveFeedback);
        nav_tvTermsAndPrivacyPolicy = navDrawerView.findViewById(R.id.tvTermsAndPrivacyPolicy);
        nav_tvHelpAndSupport = navDrawerView.findViewById(R.id.tvHelpAndSupport);
        nav_tvAppintroBoarding = navDrawerView.findViewById(R.id.tvAppintroOnbording);
        nav_tvLogout = navDrawerView.findViewById(R.id.tvLogout);
        //nav_llcCreatePoll = navDrawerView.findViewById(R.id.llcCreatePoll);


    }

    // endregion

    // region setClickListeners

    private void setClickListeners() {
        // region nav drawer item click listener

        ivDrawerUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getUserId(), user.getFullName()));

        tvGotoProfile.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getUserId(), user.getFullName()));

        nav_llNotification.setOnClickListener(v -> qxmFragmentTransactionHelper.loadNotificationFragment());

        //nav_llcCreatePoll.setOnClickListener(v -> qxmFragmentTransactionHelper.loadCreatePollFragment());

        nav_llEvaluateQxm.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadMyAllQxmEvaluationListFragment()
        );

        nav_llPerformance.setOnClickListener(v -> qxmFragmentTransactionHelper
                .loadMyQxmParentFragmentsSpecificFragment(1));

        nav_llActivity.setOnClickListener(v -> qxmFragmentTransactionHelper.loadActivityLogFragment());

        nav_llFollowers.setOnClickListener(v -> qxmFragmentTransactionHelper.loadFollowerListFragment(user.getUserId()));

        nav_llGroups.setOnClickListener(v -> qxmFragmentTransactionHelper
                .loadMyQxmParentFragmentsSpecificFragment(3));

        nav_llPrivateQxm.setOnClickListener(v -> {
            // Todo:: goto MyPrivateQxmFragment (We will create this fragment in MVP2)
            StaticValues.CURRENT_FILTER = FILTER_PRIVATE_QXMS;
            qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(0);
        });

        nav_llMyEnrollment.setOnClickListener(v -> qxmFragmentTransactionHelper
                .loadEnrollParentFragmentsSpecificFragment(1));

        nav_tvSettings.setOnClickListener(v -> {
            // Todo:: create AppSettings
            // Todo:: goto AppSettings
            qxmFragmentTransactionHelper.loadQxmAppSettingsFragment();
        });
        nav_tvReportaBug.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "app.cruxbd@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            intent.putExtra(Intent.EXTRA_TEXT, "message");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));


        });


        nav_tvShareApp.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Get QXM App on: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share QXM Via"));
        });

        nav_tvGiveFeedBack.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadSendFeedbackFragment()
        );

        nav_tvTermsAndPrivacyPolicy.setOnClickListener(v -> {
            // Todo:: create App Terms&Condition and PrivacyPolicy
            // Todo:: goto AppPrivacyPolicyFragment
            // -----
            // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
            // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
            // and launch the desired Url with CustomTabsIntent.launchUrl()

           /* String url = "https://www.myqxm.com/";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(v.getContext(), Uri.parse(url));

            // Changes the background color for the omnibox. colorInt is an int
            // that specifies a Color.

            builder.setToolbarColor(Color.parseColor("#2196F3"));

            builder.build();*/

            qxmFragmentTransactionHelper.loadQxmTermsAndPrivacyFragment();


            // ----
        });

        nav_tvHelpAndSupport.setOnClickListener(v -> {
            String body = null;
            try {
                body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, "Help and support - catch exception ");
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.email_address_qxm_help_and_support)});
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.email_subject_help_and_support));
            intent.putExtra(Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));

            // Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
        });
        nav_tvAppintroBoarding.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OnboardingActivity.class);
            intent.putExtra("key", "appintro");
            getActivity().startActivity(intent);
            getActivity().finish();
        });


        nav_tvLogout.setOnClickListener(v -> qxmFragmentTransactionHelper.logout(realmService));

        // endregion

        // region homeFragment item click listener

//      ivNotification.setOnClickListener(v -> Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show());
//
        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());
//
//        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getUserId()));
        ivNotification.setOnClickListener(v -> qxmFragmentTransactionHelper.loadNotificationFragment());

        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(token.getUserId(), user.getFullName()));

        //endregion
    }

    // endregion

    // region GetFeedItemListNetworkCall

    private void getFeedItemListNetworkCall(int paginationIndex) {

        Log.d(TAG, "getFeedItemListNetworkCall token :" + token.getToken());
        Call<List<FeedData>> getFeedItemList = apiService.getFeedItemList(token.getToken(), token.getUserId(), paginationIndex);

        getFeedItemList.enqueue(new Callback<List<FeedData>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedData>> call, @NonNull Response<List<FeedData>> response) {

                swipeRefreshLayout.setRefreshing(false);

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    //hiding noInternetView
                    noInternetView.setVisibility(View.GONE);

                    List<FeedData> feedItemList = response.body();

                    feedModelList.addAll(feedItemList);

                    if (feedModelList.size() == 0) tvFeedEmptyMessage.setVisibility(View.VISIBLE);
                    else tvFeedEmptyMessage.setVisibility(GONE);
                    // passing feed to the recycler view adapter

                    feedAdapter.notifyDataSetChanged();


                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: Feed network call failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedData>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getFeedItem");
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

    // endregion

    // region SetupDrawerToggle

    private ActionBarDrawerToggle setupDrawerToggle() {

        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(getActivity(), drawer_layout, toolbar_home_fragment, R.string.drawer_open, R.string.drawer_close);

    }
    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            feedAdapter.clear();
            getFeedItemListNetworkCall(0);
        });
    }

    //endregion


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: HomeFragment");
        if (recyclerViewState != null) {
            Log.d(TAG, "onResume: retain recyclerViewState");
            RVFeed.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

        new AppUpdateDialog(apkUpdateAlertDialog).checkIfApkUpdatedApkAvailable(this);
        new ServerMaintenanceDialog(serverStatusAlertDialog).checkIfServerUnderMaintenance(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // checkIfApkUpdatedApkAvailable();
        // new AppUpdateDialog(apkUpdateAlertDialog).checkIfApkUpdatedApkAvailable(this);
        checkIfServerUnderMaintenance();

        if (isHomeFeedRefreshingNeeded) {
            isHomeFeedRefreshingNeeded = false;
            swipeRefreshLayout.setRefreshing(true);
            feedAdapter.clear();
            getFeedItemListNetworkCall(0);
        }

        if (getActivity() != null) {
            Log.d(TAG, "onStart: ");
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
            ((BottomNavigationViewEx) getActivity().findViewById(R.id.bottom_navigation_view)).getMenu().getItem(0).setChecked(true);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = RVFeed.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: HomeFragment");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: HomeFragment");
    }

    // region Override-OnItemSelected (FeedAdapter)

   /* @Override
    public void onPollSelected(String qxmId) {

        Log.d("GroupFeedAdapterListener", TAG + " qxmId: " + qxmId);
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
    }*/

    @Override
    public void onItemSelected(String userId, String qxmId, String qxmTitle) {
        Log.d("FeedAdapterListener", TAG + " userId: " + userId);
        Log.d("FeedAdapterListener", TAG + " qxmId: " + qxmId);
        if (token.getUserId().equals(userId)) {
            // goto SingleQxmDetailsFragment
            Log.d("FeedAdapterListener", TAG + " onPollSelected: goto SingleQxmDetailsFragment");
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
            args.putString(SINGLE_QXM_DETAILS_TITLE_KEY, qxmTitle);
            qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
        } else {
            Log.d("FeedAdapterListener", TAG + " onPollSelected: goto QuestionOverview");
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
            qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
        }
    }

    @Override
    public void onSingleMCQItemSelected(String userId, String singleMCQId, String singleMCQTitle, boolean isParticipated) {
        Log.d(TAG, "onSingleMCQItemSelected: " + userId + ", MCQId: " + singleMCQId + ", isParticipated: " + isParticipated);
        if (!token.getUserId().equals(userId)) {

            if (isParticipated) {
                // I'v already participated.. show me the result on
                qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQId);
            } else {
                // I'm not participate yet, so participate click listener
                //qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQId, VIEW_FOR_PARTICIPATE_SINGLE_MCQ);
                qxmFragmentTransactionHelper.loadSingleMCQOverviewFragment(singleMCQId);
            }
        } else {

            //Log.d("FeedDataToPass",feedDataToPass.toString());
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS, singleMCQId);
            args.putString(SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY, singleMCQTitle);


            //going to single MCQ details fragment with singleMCQId

            qxmFragmentTransactionHelper.loadSingleMCQDetailsFragment(args);


            Log.d(TAG, "onSingleMCQItemSelected: this is my Single MCQ");
        }

    }

    // endregion


    //region checkIfApkUpdatedApkAvailable
    private void checkIfApkUpdatedApkAvailable() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("version");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    if (apkUpdateAlertDialog != null)
                        apkUpdateAlertDialog.dismiss();

                    Log.d(TAG, "onDataChange dataSnapshot: " + dataSnapshot.toString());


                    String appVersion = (String) dataSnapshot.getValue();

                    try {
                        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        String version = pInfo.versionName;
                        long versionCode;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            versionCode = pInfo.getLongVersionCode();
                        } else versionCode = pInfo.versionCode;
                        if (appVersion != null && !appVersion.isEmpty()) {
                            long updatedVersionCode = Long.parseLong(appVersion);
                            if (updatedVersionCode > versionCode) {
                                if (apkUpdateAlertDialog != null)
                                    apkUpdateAlertDialog.dismiss();
                                generateUpdateDialog(context);
                            } else {
                                if (apkUpdateAlertDialog != null) {
                                    apkUpdateAlertDialog.dismiss();
                                }
                            }

                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled details: " + databaseError.getDetails());
                Log.d(TAG, "onCancelled message: " + databaseError.getMessage());
                Log.d(TAG, "onCancelled code: " + databaseError.getCode());
            }
        });
    }
    //endregion

    //region checkIfServerUnderMaintenance
    private void checkIfServerUnderMaintenance() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("server_status");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    if (serverStatusAlertDialog != null)
                        serverStatusAlertDialog.dismiss();

                    Log.d(TAG, "onDataChange dataSnapshot server_status : " + dataSnapshot.toString());

                    String serverStatus = (String) dataSnapshot.getValue();

                    if (serverStatus != null && !serverStatus.isEmpty()) {

                        generateServerStatusDialog(context, serverStatus);

                    } else {

                        if (serverStatusAlertDialog != null) {
                            serverStatusAlertDialog.dismiss();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled details: " + databaseError.getDetails());
                Log.d(TAG, "onCancelled message: " + databaseError.getMessage());
                Log.d(TAG, "onCancelled code: " + databaseError.getCode());
            }
        });
    }
    //endregion

    //region generateUpdateDialog
    private void generateUpdateDialog(Context context) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_updated_apk_available));

        //alertDialogBuilder.setMessage(context.getResources().getString(R.string.your_current_apk_version) +" "+ currentApkVersion+" " +"You must update to the latest version "+ latestApkVersion + " to continue the app.");
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.you_are_using_old_version));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Update", (dialogInterface, i) -> {

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context.getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }

            dialogInterface.dismiss();

        });


        if (!((AppCompatActivity) context).isFinishing()) {

            apkUpdateAlertDialog = alertDialogBuilder.create();
            apkUpdateAlertDialog.show();
        }


    }
    //endregion

    //region generateServerStatusDialog
    private void generateServerStatusDialog(Context context, String message) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_server_under_maintenance));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);

        if (!((AppCompatActivity) context).isFinishing()) {

            serverStatusAlertDialog = alertDialogBuilder.create();
            serverStatusAlertDialog.show();
        }


    }
    //endregion


}
