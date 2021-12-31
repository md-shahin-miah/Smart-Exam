package com.crux.qxm.views.fragments.following;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.following.FollowingUserListAdapter;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.following.FollowingUser;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.followingFragmentFeature.DaggerFollowingFragmentComponent;
import com.crux.qxm.di.followingFragmentFeature.FollowingFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.recyclerViewPaginationHelper.EndlessRecyclerViewScrollListener;
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
import static com.crux.qxm.utils.StaticValues.SINGLE_QXM_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_SINGLE_MCQ;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    // region FollowingFragment-Properties

    private static final String TAG = "FollowingFragment";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    Parcelable recyclerViewState;
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private List<FollowingUser> followingUserList;
    private List<Object> feedModelList;
    private FollowingUserListAdapter followingUserListAdapter;
    private FeedAdapter feedAdapter;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    private LinearLayoutManager rvLayoutManager;

    // endregion

    // region BindViewsWithButterKnife

    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.tvFollowingUserListEmptyMsg)
    AppCompatTextView tvFollowingUserListEmptyMsg;
    @BindView(R.id.llFollowingUserList)
    LinearLayoutCompat llFollowingUserList;
    @BindView(R.id.rvFollowingUserList)
    RecyclerView rvFollowingUserList;
    @BindView(R.id.tvAll)
    AppCompatTextView tvAll;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVFeed)
    RecyclerView RVFeed;
    @BindView(R.id.noInternetView)
    View noInternetView;

    AppCompatTextView tvRetry;


    // endregion

    // region Fragment-EmptyConstructor
    public FollowingFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();

        setUpDagger2(context);

        init();

        noInternetFunctionality();

        setClickListeners();


        if (feedModelList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getFeedItemListNetworkCall(0);
        }

        if (followingUserList.size() == 0) {
//            swipeRefreshLayout.setRefreshing(true);
            getFollowingUserListNetworkCall();
        }

        //region goto user profile


        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(token.getUserId(), user.getFullName()));

        //endregion
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        FollowingFragmentComponent followingFragmentComponent =
                DaggerFollowingFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        followingFragmentComponent.injectFollowingFragmentFeature(FollowingFragment.this);
    }

    // endregion

    // region Init

    private void init() {

        apiService = retrofit.create(QxmApiService.class);

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);

        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        if (followingUserList == null) followingUserList = new ArrayList<>();

        if (feedModelList == null) feedModelList = new ArrayList<>();

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        setUserImageIntoView();


        initializeFollowingUserListAdapter();

        initializeFeedAdapter();

        feedAdapter = new FeedAdapter(context, feedModelList, getActivity(), picasso, user.getUserId(), token.getToken(), apiService, this);
        rvLayoutManager = new LinearLayoutManager(context);
        RVFeed.setAdapter(feedAdapter);
        RVFeed.setLayoutManager(rvLayoutManager);
//        RVFeed.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // following user list
            followingUserListAdapter.clear();
            getFollowingUserListNetworkCall();
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

    }

    // endregion

    // region SetUserImageIntoView
    private void setUserImageIntoView() {

        Log.d(TAG, "setUserImageIntoView: called");
        if (user != null) {

            Log.d(TAG, "setUserImageIntoView user: " + user);
            if (!TextUtils.isEmpty(user.getModifiedURLProfileImage())) {
                picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivUserImage);
            }
        }
    }
    // endregion

    // region GetFollowingUserList
    private void getFollowingUserListNetworkCall() {

        Call<List<FollowingUser>> getFollowingUserList = apiService.getFollowingUserList(token.getToken(), token.getUserId());

        getFollowingUserList.enqueue(new Callback<List<FollowingUser>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingUser>> call, @NonNull Response<List<FollowingUser>> response) {

                Log.d(TAG, "onResponse: response code = " + response.code());
                Log.d(TAG, "onResponse: response body = " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        followingUserList.addAll(response.body());

                        if (followingUserList.size() == 0) {
                            Log.d(TAG, "onResponse: following user list is empty.");
                            tvFollowingUserListEmptyMsg.setVisibility(View.VISIBLE);
                            llFollowingUserList.setVisibility(View.GONE);

                        } else {
                            Log.d(TAG, "onResponse: following user loaded.");
                            tvFollowingUserListEmptyMsg.setVisibility(View.GONE);
                            followingUserListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAG, "onResponse: response body is null");
//                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: following user not loaded.");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FollowingUser>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: StackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }
    // endregion

    // region InitializeFollowingUserListAdapter
    private void initializeFollowingUserListAdapter() {
        followingUserListAdapter = new FollowingUserListAdapter(context, picasso, getActivity(), followingUserList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvFollowingUserList.setLayoutManager(linearLayoutManager);
        rvFollowingUserList.setAdapter(followingUserListAdapter);
        rvFollowingUserList.setNestedScrollingEnabled(false);
    }

    // endregion

    // region InitializeFeedAdapter
    private void initializeFeedAdapter() {

    }
    // endregion

    // region GetFeedItemListNetworkCall
    private void getFeedItemListNetworkCall(int paginationIndex) {

        Log.d(TAG, "getFeedItemListNetworkCall: ");

        Call<List<FeedData>> getFeedItemList = apiService.getFollowingFeedData(token.getToken(), token.getUserId(), paginationIndex);

        getFeedItemList.enqueue(new Callback<List<FeedData>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedData>> call, @NonNull Response<List<FeedData>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());
                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    List<FeedData> feedItemList = response.body();

                    feedModelList.addAll(feedItemList);

                    // passing feed to the recycler view adapter

                    feedAdapter.notifyDataSetChanged();
                    followingUserListAdapter.notifyDataSetChanged();


//                    Log.d(TAG, "Feed: " + feedModelList.toString());

                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    swipeRefreshLayout.setRefreshing(false);
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
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });
    }
    // endregion

    // region SetClickListeners
    private void setClickListeners() {
        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());
        tvAll.setOnClickListener(v -> qxmFragmentTransactionHelper.loadMyFollowingListFragment(user.getUserId()));
    }
    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {
        tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            // following user list
            followingUserListAdapter.clear();
            getFollowingUserListNetworkCall();
            feedAdapter.clear();
            getFeedItemListNetworkCall(0);
        });
    }

    //endregion

    // region Override-OnResume
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: FollowingFragment");
        if (recyclerViewState != null) {
            Log.d(TAG, "onResume: retain recyclerViewState");
            RVFeed.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }
    // endregion

    // region Override-OnStart
    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: ");
            ((BottomNavigationViewEx) getActivity().findViewById(R.id.bottom_navigation_view)).getMenu().getItem(2).setChecked(true);
        }


    }
    // endregion

    // region Override-OnPause
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: FollowingFragment");
        recyclerViewState = RVFeed.getLayoutManager().onSaveInstanceState();
    }

    // endregion

    // region Override-OnItemSelected (FeedAdapter)

    /*@Override
    public void onPollSelected(String qxmId) {
        Log.d("GroupFeedAdapterListener", TAG + " qxmId: " + qxmId);
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
    }*/

    @Override
    public void onItemSelected(String userId, String qxmId, String qxmTitle) {
        Log.d("FeedAdapterListen", TAG + " userId: " + userId);
        Log.d("FeedAdapterListen", TAG + " qxmId: " + qxmId);
        if (token.getUserId().equals(userId)) {
            // goto SingleQxmDetailsFragment
            Log.d("FeedAdapterListen", TAG + " onPollSelected: goto SingleQxmDetailsFragment");
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
            args.putString(SINGLE_QXM_DETAILS_TITLE_KEY, qxmTitle);
            qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
        } else {
            Log.d("FeedAdapterListen", TAG + " onPollSelected: goto QuestionOverview");
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
}
