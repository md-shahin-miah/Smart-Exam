package com.crux.qxm.views.fragments.singleMCQ;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.leaderboardSingleMCQ.LeaderboardSingleMCQAdapter;
import com.crux.qxm.db.models.leaderboardSingleMCQ.SingleMCQLeaderboardItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.leaderboardSingleMCQFragmentFeature.DaggerLeaderboardSingleMCQFragmentComponent;
import com.crux.qxm.di.leaderboardSingleMCQFragmentFeature.LeaderboardSingleMCQFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
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

import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_CREATOR_ID;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_LEADER_BOARD_PRIVACY;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_LEADER_BOARD_PRIVACY_PARTICIPANTS;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardSingleMCQFragment extends Fragment {

    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "LeaderboardFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    //private List<LeaderBoardItem> leaderboardItemList;
    private List<SingleMCQLeaderboardItem> mcqLeaderboardItems;
    private LeaderboardSingleMCQAdapter leaderboardSingleMCQAdapter;

    // endregion

    // region BindViewsWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;
    @BindView(R.id.cvQuestionSet)
    CardView cvQuestionSet;
    @BindView(R.id.tvQxmCreator)
    AppCompatTextView tvQxmCreator;
    @BindView(R.id.tvQxmCreationTime)
    AppCompatTextView tvQxmCreationTime;
    @BindView(R.id.ivQxmPrivacy)
    AppCompatImageView ivQxmPrivacy;
    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;
    @BindView(R.id.tvMyPosition)
    AppCompatTextView tvMyPosition;
    @BindView(R.id.tvUserFullName)
    AppCompatTextView tvUserFullName;
    @BindView(R.id.tvAchievedPoints)
    AppCompatTextView tvAchievedPoints;
    @BindView(R.id.tvTimeTaken)
    AppCompatTextView tvTimeTaken;
    @BindView(R.id.rlLeaderboardContainer)
    RelativeLayout rlLeaderboardContainer;
    @BindView(R.id.tvLeaderboardEmptyMessage)
    AppCompatTextView tvLeaderboardEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvLeaderboard)
    RecyclerView rvLeaderboard;
    private String singleMCQId;
    private String qxmTitle;
    private String qxmCreatorName;
    private String qxmCreationTime;
    private String qxmPrivacy;
    private String leaderBoardPrivacy;
    private String singleQxmCreatorId;


    // endregion

    // region Fragment-NewInstance

    public static LeaderboardSingleMCQFragment newInstance(Bundle bundle) {

        LeaderboardSingleMCQFragment fragment = new LeaderboardSingleMCQFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // endregion

    // region Fragment-Constructor
    public LeaderboardSingleMCQFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_single_mcq, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");

        singleMCQId = getArguments() != null ? getArguments().getString(QXM_ID_KEY) : null;
        qxmTitle = getArguments() != null ? getArguments().getString(QXM_QUESTION_SET_TITLE) : null;
        qxmCreatorName = getArguments() != null ? getArguments().getString(QXM_CREATOR_NAME_KEY) : null;
        qxmCreationTime = getArguments() != null ? getArguments().getString(QXM_CREATION_TIME_KEY) : null;
        qxmPrivacy = getArguments() != null ? getArguments().getString(QXM_PRIVACY_KEY) : null;
        leaderBoardPrivacy = getArguments() != null ? getArguments().getString(SINGLE_MCQ_LEADER_BOARD_PRIVACY) : null;
        singleQxmCreatorId = getArguments() != null ? getArguments().getString(SINGLE_MCQ_CREATOR_ID) : null;
        context = view.getContext();

        setUpDagger2();

        init();

        if (mcqLeaderboardItems.size() == 0 && !TextUtils.isEmpty(singleMCQId)) {
            cvQuestionSet.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
            getLeaderboardOfSpecificQxmNetworkCall(singleMCQId, token.getUserId());
        }

        initializeClickListeners();

    }


    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        LeaderboardSingleMCQFragmentComponent leaderboardFragmentComponent =
                DaggerLeaderboardSingleMCQFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        leaderboardFragmentComponent.injectLeaderboardSingleMCQFragmentFeature(LeaderboardSingleMCQFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        Log.d(TAG, "init: ");
        realmService = new RealmService(Realm.getDefaultInstance());
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (mcqLeaderboardItems == null)
            mcqLeaderboardItems = new ArrayList<>();

        leaderboardSingleMCQAdapter = new LeaderboardSingleMCQAdapter(mcqLeaderboardItems, context, picasso, qxmFragmentTransactionHelper);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(context));
        rvLeaderboard.setAdapter(leaderboardSingleMCQAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mcqLeaderboardItems.clear();
            getLeaderboardOfSpecificQxmNetworkCall(singleMCQId, user.getUserId());
        });

    }

    // endregion

    // region GetLeaderboardOfSpecificQxmNetworkCall

    private void getLeaderboardOfSpecificQxmNetworkCall(String singleMCQId, String userId) {
        Log.d(TAG, String.format("getLeaderboardOfSpecificQxmNetworkCall: singleMCQId= %s, userId: %s", singleMCQId, userId));

        Call<List<SingleMCQLeaderboardItem>> getLeaderboardOfSingleMCQ = apiService.getLeaderboardOfSingleMCQ(token.getToken(), singleMCQId, userId);

        getLeaderboardOfSingleMCQ.enqueue(new Callback<List<SingleMCQLeaderboardItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<SingleMCQLeaderboardItem>> call, @NonNull Response<List<SingleMCQLeaderboardItem>> response) {
                Log.d(TAG, "onResponse: getLeaderboardOfSpecificQxmNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());


                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: success");

                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: response.body = " + response.body().toString());

                            //creator perspective
                            if(userId.equals(singleQxmCreatorId))
                                mcqLeaderboardItems.addAll(response.body());

                            // participator perspective
                            else{
                                // if leader board privacy is participant, only get user and pass it
                                if (!leaderBoardPrivacy.equals(SINGLE_MCQ_LEADER_BOARD_PRIVACY_PARTICIPANTS))
                                    mcqLeaderboardItems.addAll(response.body());
                                else mcqLeaderboardItems.addAll(filterMcqLeaderboardItems(userId,response.body()));
                            }
                            Log.d(TAG, "onResponse: mcqLeaderboardItems " + mcqLeaderboardItems.toString());
                            leaderboardSingleMCQAdapter.notifyDataSetChanged();
                            setLeaderboardValuesInViews();

                            cvQuestionSet.setVisibility(View.VISIBLE);
                        }

                    } else if (response.code() == 403) {
                        Log.d(TAG, "onResponse: user session expired");
                        Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                        qxmFragmentTransactionHelper.logout(realmService);
                    } else {
                        Log.d(TAG, "onResponse: getLeaderboardOfSpecificQxmNetworkCall failed. ");
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                    }
                    if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                }


            }

            @Override
            public void onFailure(@NonNull Call<List<SingleMCQLeaderboardItem>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getLeaderboardOfSpecificQxmNetworkCall");
                Log.d(TAG, "onFailure: Error Message: " + t.getMessage());
                Log.d(TAG, "onFailure: Error Stacktrace: " + Arrays.toString(t.getStackTrace()));
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Network error. Please try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setLeaderboardValuesInViews() {
        // qxm & qxm creator details [leaderboard header part]
        tvQxmTitle.setText(qxmTitle);

        tvQxmCreator.setText(qxmCreatorName);
        if (QxmStringIntegerChecker.isLongInt(qxmCreationTime))
            tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(
                    Long.parseLong(qxmCreationTime)
            ));
        if (qxmPrivacy.equals(QXM_PRIVACY_PRIVATE))
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_privacy_private));
        else
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_privacy_public));

        // my position & some result information

        picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivUserImage);

        if (mcqLeaderboardItems != null) {
            //tvMyPosition.setText(String.format("#%s", mcqLeaderboardItems.getMyPosition()));
            // TODO:: tvMyPosition only used in contest mode.
            tvMyPosition.setVisibility(View.GONE);

            tvUserFullName.setText(user.getFullName());

            /*tvAchievedPoints.setText(mcqLeaderboardItems.getMyInfo().getUserPerformance().getAchievePoints());

            if (QxmStringIntegerChecker.isLongInt(mcqLeaderboardItems.getMyInfo().getUserPerformance().getTimeTakenToAnswer())) {

                tvTimeTaken.setText(TimeFormatString.getDurationInHourMinSec(
                        Long.parseLong(
                                mcqLeaderboardItems.getMyInfo().getUserPerformance().getTimeTakenToAnswer()
                        )
                ));
            }*/
        }

    }


    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        tvQxmCreator.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });

        ivUserImage.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });

        tvUserFullName.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });
    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(qxmTitle) && !TextUtils.isEmpty(qxmCreatorName)
                && !TextUtils.isEmpty(qxmCreationTime) && !TextUtils.isEmpty(qxmPrivacy)
                && mcqLeaderboardItems != null) {
            setLeaderboardValuesInViews();
        }

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

    // filter user
    private List<SingleMCQLeaderboardItem> filterMcqLeaderboardItems(String criteria,
                                                                     List<SingleMCQLeaderboardItem> singleMCQLeaderboardItems) {
        List<SingleMCQLeaderboardItem> items = new ArrayList<>();
        for (SingleMCQLeaderboardItem singleMCQLeaderboardItem : singleMCQLeaderboardItems) {
            if (singleMCQLeaderboardItem.getParticipator().getId().equals(criteria))
                items.add(singleMCQLeaderboardItem);
        }
        return items;

    }

}
