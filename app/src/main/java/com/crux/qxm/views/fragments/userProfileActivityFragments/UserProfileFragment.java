package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.users.UserInfo;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.userProfileFragmentFeature.DaggerUserProfileFragmentComponent;
import com.crux.qxm.di.userProfileFragmentFeature.UserProfileFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmCopyLinkToClipboard;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.utils.recyclerViewPaginationHelper.EndlessRecyclerViewScrollListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE;
import static com.crux.qxm.utils.StaticValues.SINGLE_QXM_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.TAG_EDIT_PROFILE_FRAGMENT;
import static com.crux.qxm.utils.StaticValues.USER_FULL_NAME;
import static com.crux.qxm.utils.StaticValues.USER_ID_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.isUserProfileFragmentRefreshNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    // region Fragment-Properties
    private static final String TAG = "UserProfileFragment";
    Realm realm;
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    QxmApiService apiService;
    Parcelable recyclerViewState;
    private QxmToken token;
    private RealmService realmService;
    private Context context;
    private QxmProgressDialog progressDialog;
    private String userId;
    private String userFullName;
    private UserInfo user;
    private UserBasic userBasic;
    private List<Object> feedModelList;
    private FeedAdapter feedAdapter;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private ImageProcessor imageProcessorForProfilePicture;

    private LinearLayoutManager rvLayoutManager;

    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.llOtherOptions)
    LinearLayoutCompat llOtherOptions;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;

    @BindView(R.id.rlUserProfileHolder)
    RelativeLayout rlUserProfileHolder;

    @BindView(R.id.iv_profile_pic)
    AppCompatImageView ivProfilePic;

    @BindView(R.id.llChangeImage)
    LinearLayoutCompat llChangeImage;

    @BindView(R.id.llProfileSettings)
    LinearLayoutCompat llProfileSettings;

    @BindView(R.id.llActivity)
    LinearLayoutCompat llActivity;

    @BindView(R.id.llMyQxm)
    LinearLayoutCompat llMyQxm;

    @BindView(R.id.llFollowUser)
    LinearLayoutCompat llFollowUser;

    @BindView(R.id.ivUserFollow)
    AppCompatImageView ivUserFollow;

    @BindView(R.id.tvFollow)
    AppCompatTextView tvFollow;

    @BindView(R.id.tvUserFullName)
    AppCompatTextView tvUserFullName;

    @BindView(R.id.tvUserStatus)
    AppCompatTextView tvUserStatus;

    @BindView(R.id.tvUserFollowers)
    AppCompatTextView tvUserFollowers;

    @BindView(R.id.tvUserFollowings)
    AppCompatTextView tvUserFollowings;

    @BindView(R.id.tvUserGroups)
    AppCompatTextView tvUserGroups;

    @BindView(R.id.tvUserQxms)
    AppCompatTextView tvUserQxms;

    @BindView(R.id.tvUserLocation)
    AppCompatTextView tvUserLocation;

    @BindView(R.id.tvUserEducation)
    AppCompatTextView tvUserEducation;

    @BindView(R.id.tvUserOccupation)
    AppCompatTextView tvUserOccupation;

    @BindView(R.id.llLocation)
    LinearLayoutCompat llLocation;

    @BindView(R.id.llStudy)
    LinearLayoutCompat llStudy;

    @BindView(R.id.llOccupation)
    LinearLayoutCompat llOccupation;

    @BindView(R.id.tvDetails)
    AppCompatTextView tvProfileDetails;
    @BindView(R.id.rvUserProfileFeed)
    RecyclerView RVFeed;

    @BindView(R.id.llcFollowers)
    LinearLayoutCompat llcFollowers;
    @BindView(R.id.llcFollowing)
    LinearLayoutCompat llcFollowing;
    @BindView(R.id.llcGroups)
    LinearLayoutCompat llcGroups;
    @BindView(R.id.llcQxms)
    LinearLayoutCompat llcQxms;
    @BindView(R.id.tvChangeImage)
    AppCompatTextView tvChangeImage;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;
    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.noInternetView)
    View noInternetView;


    // endregion

    // region Fragment-Constructor

    public UserProfileFragment() {
        // Required empty public constructor
    }

    // endregion

    // region Fragment-NewInstance

    public static UserProfileFragment newInstance(String userId, String userFullName) {

        Bundle args = new Bundle();
        args.putString(USER_ID_KEY, userId);
        args.putString(USER_FULL_NAME, userFullName);
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        userId = getArguments() != null ? getArguments().getString(USER_ID_KEY) : null;
        userFullName = getArguments() != null ? getArguments().getString(USER_FULL_NAME) : null;

        context = view.getContext();

        setUpDagger2(context);
        init();
        noInternetFunctionality();
        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);


        Log.d(TAG, "onViewCreated: myUserId: " + token.getUserId() + ", OtherUserId: " + userId);
        if (!userId.equals(token.getUserId())) {

            rlUserProfileHolder.setVisibility(GONE);
            llChangeImage.setVisibility(GONE);
            llMyQxm.setVisibility(View.GONE);
            llProfileSettings.setVisibility(GONE);
            llActivity.setVisibility(GONE);
            llFollowUser.setVisibility(View.VISIBLE);
        } else {
            rlUserProfileHolder.setVisibility(GONE);
            llChangeImage.setVisibility(View.VISIBLE);
            llMyQxm.setVisibility(View.VISIBLE);
            llProfileSettings.setVisibility(View.VISIBLE);
            llActivity.setVisibility(View.VISIBLE);
            llFollowUser.setVisibility(GONE);
        }

        // if there is no userInfo then fetch them again
        if (user == null) {

            rlUserProfileHolder.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            getUserInfoNetworkCall(0);

        } else {

            setValuesInViews();

        }

        setUpClickListeners();


    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        UserProfileFragmentComponent userProfileFragmentComponent =
                DaggerUserProfileFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent()).build();

        userProfileFragmentComponent.injectUserProfileFragmentFeature(UserProfileFragment.this);
    }

    // endregion

    // region Init

    private void init() {


        Log.d(TAG, "init: Called");
        //Initialize DB
        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        userBasic = realmService.getSavedUser();
        Log.d(TAG, "init saved user: "+userBasic);

        // GetTokenFromDB
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        progressDialog = new QxmProgressDialog(context);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (feedModelList == null)
            feedModelList = new ArrayList<>();

        rvLayoutManager = new LinearLayoutManager(getContext());
        feedAdapter = new FeedAdapter(getContext(), feedModelList, getActivity(), picasso, token.getUserId(), token.getToken(), apiService, this);
        RVFeed.setAdapter(feedAdapter);
        RVFeed.setLayoutManager(rvLayoutManager);
        //RVFeed.setNestedScrollingEnabled(false);

        // setting user fullName from parcelable data
        tvToolbarTitle.setText(userFullName);

        //region recycler view scroll listener

        RVFeed.addOnScrollListener(new EndlessRecyclerViewScrollListener(rvLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.d(TAG, "onLoadMore page: " + page);
                getUserInfoNetworkCall(page);

            }
        });

        //endregion

    }

    //  endregion

    // region GetUserInfo

    private void getUserInfoNetworkCall(int paginationIndex) {

        Log.d(TAG, "getUserInfoNetworkCall: Token: " + token.toString());

        Call<UserInfo> getProfileInfo = apiService.getUserProfileInfo(token.getToken(), userId, paginationIndex);

        getProfileInfo.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setVisibility(GONE);
                }

                if (response.code() == 200) {

                    user = response.body();

                    Log.d(TAG, "Feed: " + feedModelList.toString());

                    if (user.getFeedDataList().isEmpty() && paginationIndex == 0) {

                        if (user.getFeedDataList() == null || user.getFeedDataList().size() == 0){
                            if(user.getProfileData().getUserId().equals(token.getUserId())){
                                tvFeedEmptyMessage.setText(context.getString(R.string.profile_empty_feed_message));
                            }else{
                                tvFeedEmptyMessage.setText(String.format("%s has not posted anything yet!", user.getProfileData().getFullName()));
                            }
                            tvFeedEmptyMessage.setVisibility(View.VISIBLE);
                        }
                        else{
                            tvFeedEmptyMessage.setVisibility(GONE);
                        }


                    } else {

                        tvFeedEmptyMessage.setVisibility(GONE);
                        List<FeedData> feedItemList = user.getFeedDataList();
                        feedModelList.addAll(feedItemList);
                        // passing feed to the recycler view adapter
                        feedAdapter.notifyDataSetChanged();

                    }

                    setValuesInViews();


                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getUserProfileInfo");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setVisibility(GONE);
                }

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context)){

                    RVFeed.setVisibility(GONE);
                    noInternetView.setVisibility(View.VISIBLE);
                }

                else{

                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    noInternetView.setVisibility(GONE);
                    RVFeed.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    // endregion

    // region GetOtherUserProfileInfoNetworkCall

    private void getOtherUserProfileInfoNetworkCall() {

        progressDialog.showProgressDialog("User Info", "Loading...", false);

        Log.d(TAG, "getUserInfoNetworkCall: Token: " + token.toString());
        Log.d(TAG, "getOtherUserProfileInfoNetworkCall: OtherUserId= " + userId + ", myUserId= " + token.getUserId());
        Call<UserInfo> getProfileInfo = apiService.getOtherUserProfileInfo(token.getToken(), userId, token.getUserId());

        getProfileInfo.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200) {

                    user = response.body();

                    progressDialog.closeProgressDialog();


                    setValuesInViews();

                    List<FeedData> feedItemList = user.getFeedDataList();
                    feedModelList.addAll(feedItemList);
                    // passing feed to the recycler view adapter

                    feedAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Feed: " + feedModelList.toString());


                } else {
                    progressDialog.closeProgressDialog();
                    Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getUserProfileInfo");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                progressDialog.closeProgressDialog();
                Toast.makeText(getContext(), "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    // endregion

    // region SetUpClickListeners

    private void setUpClickListeners() {

        ivBackArrow.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        llFollowUser.setOnClickListener(v -> {
            if (!user.getProfileData().getAmIFollowingThisUser()) {
                sentFollowingRequestNetworkCall();
            } else {
                unFollowThisUserNetworkCall();
            }
        });

        tvProfileDetails.setOnClickListener(v -> {
            if (userId.equals(token.getUserId())) {
                qxmFragmentTransactionHelper.loadEditProfileFragment();
            } else {
                Toast.makeText(context, "Not available.", Toast.LENGTH_SHORT).show();
            }
        });

        llcFollowers.setOnClickListener(v -> qxmFragmentTransactionHelper.loadFollowerListFragment(userId));

        llcFollowing.setOnClickListener(v -> qxmFragmentTransactionHelper.loadMyFollowingListFragment(userId));

        llcGroups.setOnClickListener(v -> qxmFragmentTransactionHelper.loadPublicGroupFragment());

        llcQxms.setOnClickListener(v -> {

            // if qxm is list is not empty, go to first qxm position
            if (!feedModelList.isEmpty()) {

                Log.d(TAG, "setUpClickListeners llcQxms: Called");
                //RVFeed.smoothScrollToPosition(0);

            } else
                Toast.makeText(context, "You haven't created any qxm yet!", Toast.LENGTH_SHORT).show();

        });

        llActivity.setOnClickListener(v -> qxmFragmentTransactionHelper.loadActivityLogFragment());


        llChangeImage.setOnClickListener(v -> initializeProfilePhotoChangingProcess());

        llProfileSettings.setOnClickListener(v -> qxmFragmentTransactionHelper.loadProfileSettingsFragment());

        llOtherOptions.setOnClickListener(v -> showUserProfileBottomSheetView());

    }

    // region ShowGroupAdminBottomSheetView

    private void showUserProfileBottomSheetView() {

        QxmUrlHelper urlHelper = new QxmUrlHelper();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_user_profile_other_options, (ViewGroup) getView(), false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        AppCompatTextView tvCopyLink = bottomSheetView.findViewById(R.id.tvCopyLink);

        tvCopyLink.setOnClickListener(v -> {
            QxmCopyLinkToClipboard qxmCopyLinkToClipboard = new QxmCopyLinkToClipboard(getActivity());

            if (qxmCopyLinkToClipboard.copyToClipboard(urlHelper.getUserProfileUrl(userId))) {
                bottomSheetDialog.dismiss();
                Toast.makeText(context, "Link copied to clipboard.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    // endregion


    // region UnFollowThisUserNetworkCall
    private void unFollowThisUserNetworkCall() {
        if (!userId.equals(token.getUserId())) {
            QxmProgressDialog progressDialog = new QxmProgressDialog(context);

            progressDialog.showProgressDialog("Unfollow User",
                    String.format("Unfollowing %s, please wait...", user.getProfileData().getFullName()),
                    false);

            Call<QxmApiResponse> sentFollowingRequest = apiService.unfollowUser(token.getToken(), token.getUserId(), userId);

            sentFollowingRequest.enqueue(new Callback<QxmApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                    Log.d(TAG, "onResponse: response code: " + response.code());
                    Log.d(TAG, "onResponse: response body: " + response.body());
                    progressDialog.closeProgressDialog();
                    if (response.code() == 201) {
                        user.getProfileData().setAmIFollowingThisUser(false);
                        ivUserFollow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_user));
                        tvFollow.setText(R.string.follow);
                        Toast.makeText(context, "User unfollowed successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "User unfollowed unsuccessful. Please try after sometime.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: ErrorMessage: " + t.getLocalizedMessage());
                    Log.d(TAG, "onFailure: Stacktrace: " + Arrays.toString(t.getStackTrace()));
                    progressDialog.closeProgressDialog();
                    Toast.makeText(context, "Network error. Please try again after sometime.", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Log.d(TAG, "sentUnFollowNetworkCall not done, cause: same user id.");
        }

    }

    // endregion


    // endregion

    // region SentFollowingRequestNetworkCall

    private void sentFollowingRequestNetworkCall() {
        if (!userId.equals(token.getUserId())) {
            QxmProgressDialog progressDialog = new QxmProgressDialog(context);

            progressDialog.showProgressDialog("Following Request",
                    String.format("Adding %s to your following list, please wait...", user.getProfileData().getFullName()),
                    false);

            Call<QxmApiResponse> sentFollowingRequest = apiService.followUser(token.getToken(), token.getUserId(), userId);

            sentFollowingRequest.enqueue(new Callback<QxmApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                    Log.d(TAG, "onResponse: response code: " + response.code());
                    Log.d(TAG, "onResponse: response body: " + response.body());
                    progressDialog.closeProgressDialog();
                    if (response.code() == 201) {

                        // if followed successfully, then change the text of tvFollow
                        ivUserFollow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_follower_blue));
                        tvFollow.setText(R.string.followed);

                        user.getProfileData().setAmIFollowingThisUser(true);

                        Toast.makeText(context, "User added to your following list.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "The process of User Follow is failed. Please try after sometime.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: ErrorMessage: " + t.getLocalizedMessage());
                    Log.d(TAG, "onFailure: Stacktrace: " + Arrays.toString(t.getStackTrace()));
                    progressDialog.closeProgressDialog();
                    Toast.makeText(context, "Network error. Please try again after sometime.", Toast.LENGTH_SHORT).show();

                }
            });


        } else {
            Log.d(TAG, "sentFollowingRequestNetworkCall: same user id.");
        }


    }

    // endregion

    // region SetValuesInViews

    private void setValuesInViews() {

        if (user != null) {
            userFullName = user.getProfileData().getFullName();

            tvToolbarTitle.setText(userFullName);

            rlUserProfileHolder.setVisibility(View.VISIBLE);

            Log.d(TAG, "setValuesInViews:  " + user.toString());

            if (user.getProfileData().getAmIFollowingThisUser()) {
                tvFollow.setText(context.getResources().getString(R.string.followed));
            } else {
                tvFollow.setText(context.getResources().getString(R.string.follow));
                ivUserFollow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_user));
            }

            if (!TextUtils.isEmpty(user.getProfileData().getUserProfileUrl()))
                picasso.load(user.getProfileData().getUserProfileUrl()).into(ivProfilePic);
            if (!TextUtils.isEmpty(user.getProfileData().getFullName()))
                tvUserFullName.setText(user.getProfileData().getFullName());
            if (!TextUtils.isEmpty(user.getProfileData().getUserStatus()))
                tvUserStatus.setText(user.getProfileData().getUserStatus());
            if (!TextUtils.isEmpty(user.getProfileData().getFollowerCount()))
                tvUserFollowers.setText(user.getProfileData().getFollowerCount());
            if (!TextUtils.isEmpty(user.getProfileData().getFollowingCount()))
                tvUserFollowings.setText(user.getProfileData().getFollowingCount());
            if (!TextUtils.isEmpty(user.getProfileData().getGroupsCount()))
                tvUserGroups.setText(user.getProfileData().getGroupsCount());
            if (!TextUtils.isEmpty(user.getProfileData().getQxmCount()))
                tvUserQxms.setText(user.getProfileData().getQxmCount());

            // userLocation
            if (user.getProfileData().getAddress() != null) {

                if (!TextUtils.isEmpty(user.getProfileData().getAddress().getUserCity())) {
                    if (!TextUtils.isEmpty(user.getProfileData().getAddress().getUserCountry())) {
                        tvUserLocation.setText(String.format("%s, %s", user.getProfileData().getAddress().getUserCity(), user.getProfileData().getAddress().getUserCountry()));
                    } else {
                        tvUserLocation.setText(user.getProfileData().getAddress().getUserCity());
                    }
                } else if (!TextUtils.isEmpty(user.getProfileData().getAddress().getUserCountry())) {
                    tvUserLocation.setText(user.getProfileData().getAddress().getUserCountry());
                } else {
                    llLocation.setVisibility(View.GONE);
                }
            } else {
                llLocation.setVisibility(View.GONE);
            }

            //userEducation
            if (user.getProfileData().getInstitution() != null) {
                if (!TextUtils.isEmpty(user.getProfileData().getInstitution().getInstituteName())) {
                    tvUserEducation.setText(user.getProfileData().getInstitution().getInstituteName());
                } else {
                    llStudy.setVisibility(View.GONE);
                }
            } else {
                llStudy.setVisibility(View.GONE);
            }

            // userOccupation
            if (user.getProfileData().getOccupation() != null) {

                if (!TextUtils.isEmpty(user.getProfileData().getOccupation().getDesignation())) {
                    if (!TextUtils.isEmpty(user.getProfileData().getOccupation().getOrganization())) {
                        tvUserOccupation.setText(String.format("%s at %s", user.getProfileData().getOccupation().getDesignation(), user.getProfileData().getOccupation().getOrganization()));
                    } else {
                        tvUserOccupation.setText(user.getProfileData().getOccupation().getDesignation());
                    }
                } else if (!TextUtils.isEmpty(user.getProfileData().getOccupation().getOrganization())) {
                    tvUserOccupation.setText(user.getProfileData().getOccupation().getOrganization());
                } else {
                    llOccupation.setVisibility(View.GONE);
                }
            } else {
                llOccupation.setVisibility(View.GONE);
            }

        } else {
            rlUserProfileHolder.setVisibility(View.GONE);
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    // endregion

    // region GotoEditProfileFragment

    private void gotoEditProfileFragment() {

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        EditUserProfileFragment editUserProfileFragment = new EditUserProfileFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, editUserProfileFragment, TAG_EDIT_PROFILE_FRAGMENT)
                .addToBackStack(TAG_EDIT_PROFILE_FRAGMENT)
                .commit();
    }

    // endregion

    //region initializeProfilePhotoChangingProcess
    private void initializeProfilePhotoChangingProcess() {

        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE;
        imageProcessorForProfilePicture = new ImageProcessor(context, UserProfileFragment.this, TAG, apiService, token, userBasic, ivProfilePic, tvChangeImage);
        imageProcessorForProfilePicture.pickImageFromGallery();


    }
    //endregion

    //region receiving event after uploading user image

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEvent(Events.OnPhotoUploadMessage onPhotoUploadMessage) {


        String sentImageURL = onPhotoUploadMessage.getSentImageURL();
        String localImageName = onPhotoUploadMessage.getLocalImageName();
        String message = onPhotoUploadMessage.getMessage();

        Log.d(TAG, "onPhotoUploadEvent sentImageURL: " + sentImageURL);
        Log.d(TAG, "onPhotoUploadEvent Message: " + message);
        Log.d(TAG, "onPhotoUploadEvent: " + onPhotoUploadMessage);

        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent STARTING_PHOTO_UPLOAD: Called");
                tvUploadingImage.setVisibility(View.VISIBLE);
                break;

            case IMAGE_UPLOAD_SUCCESS:

                Log.d(TAG, "onPhotoUploadEvent IMAGE_UPLOAD_SUCCESS: Called");
                tvUploadingImage.setVisibility(View.GONE);

                // upload success, save new image url in realm database
                Log.d(TAG, "execute onPhotoUploadMessage: " + onPhotoUploadMessage);
                realmService.setUserProfileImage(userBasic, sentImageURL);
                Toast.makeText(context, R.string.photo_uploaded_successfully, Toast.LENGTH_SHORT).show();
                break;


            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent ERROR_OCCURRED_DURING_PHOTO_UPLOAD: Called");
                Toast.makeText(context, R.string.toast_message_could_not_upload_image_try_again_later, Toast.LENGTH_SHORT).show();
                tvUploadingImage.setVisibility(View.GONE);

                break;


        }


    }

    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG, "onActivityResult: Called");

        // passing image process result for feedback
        imageProcessorForProfilePicture.onActivityResult(requestCode, resultCode, data);

    }


    //endregion


    //region Override-onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: Called");

        if (StaticValues.onActivityCall.equals(ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE)) {

            if (imageProcessorForProfilePicture != null) {

                Log.d(TAG, "onRequestPermissionsResult: not null");
                imageProcessorForProfilePicture.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    //endregion


    // region Override-OnResume

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerViewState != null) {
            Log.d(TAG, "onResume: retain recyclerViewState");
            RVFeed.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    // endregion

    // region Override-OnPause
    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = RVFeed.getLayoutManager().onSaveInstanceState();
    }

    // endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (isUserProfileFragmentRefreshNeeded) {
            isUserProfileFragmentRefreshNeeded = false;
            swipeRefreshLayout.setRefreshing(true);
            feedAdapter.clear();
            getUserInfoNetworkCall(0);
        }
        else if (user != null) {
            swipeRefreshLayout.setVisibility(View.GONE);
            if (user.getFeedDataList() == null || user.getFeedDataList().size() == 0){
                if(user.getProfileData().getUserId().equals(userId)){
                    tvFeedEmptyMessage.setText(context.getString(R.string.profile_empty_feed_message));
                }else{
                    tvFeedEmptyMessage.setText(String.format("%s has not posted anything yet!", user.getProfileData().getFullName()));
                }
                tvFeedEmptyMessage.setVisibility(View.VISIBLE);
            }
            else{
                tvFeedEmptyMessage.setVisibility(View.GONE);
            }
        }


        if (!EventBus.getDefault().isRegistered(this)) {
            // registering event bus
            EventBus.getDefault().register(this);
        }

        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

        setUpClickListeners();
    }

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            RVFeed.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            feedAdapter.clear();
            getUserInfoNetworkCall(0);
        });
    }

    //endregion

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
                qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQId, VIEW_FOR_PARTICIPATE_SINGLE_MCQ);
            }
        }else{

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

    //region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregistering event bus
        EventBus.getDefault().unregister(this);
    }
    //endregion
}



