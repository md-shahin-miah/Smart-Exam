package com.crux.qxm.views.fragments.singleMCQ;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.fullResultSingleMCQ.FullResultSingleMCQAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.singleMCQ.FullResultSingleMCQ;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.fullResultSingleMCQFragmentFeature.DaggerFullResultSingleMCQFragmentComponent;
import com.crux.qxm.di.fullResultSingleMCQFragmentFeature.FullResultSingleMCQFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PARTICIPANTS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_ID_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_LEADER_BOARD_PRIVACY;
import static com.crux.qxm.utils.StaticValues.USER_BASIC_KEY;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isFeedSingleMCQItemClicked;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullResultSingleMCQFragment extends Fragment {

    // region FullResultFragment-ClassProperties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;

    private static final String TAG = "FullResultSingleMCQFrag";

    private Context context;
    private UserBasic user;
    private QxmToken token;
    private FullResultSingleMCQ fullResult;
    private FeedData feedData;
    // private List<Question> questionList;
    private String singleMCQId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;

    // endregion

    // region ButterKnifeBindViews

    @BindView(R.id.llFullResultContainer)
    LinearLayoutCompat llFullResultContainer;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    @BindView(R.id.tvParticipantName)
    AppCompatTextView tvParticipantName;

    @BindView(R.id.tvParticipationDate)
    AppCompatTextView tvParticipationDate;

    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;

    @BindView(R.id.tvUserExperience)
    AppCompatTextView tvUserExperience;

    @BindView(R.id.tvTotalParticipants)
    AppCompatTextView tvTotalParticipants;

    @BindView(R.id.tvMyPosition)
    AppCompatTextView tvMyPosition;

    @BindView(R.id.tvAchievedPoints)
    AppCompatTextView tvAchievedPoints;

    @BindView(R.id.tvAchievedPointsInPercentage)
    AppCompatTextView tvAchievedPointsInPercentage;

    @BindView(R.id.tvTotalCorrectMCQ)
    AppCompatTextView tvTotalCorrectMCQ;

    @BindView(R.id.tvTotalCorrectFillInTheBlanks)
    AppCompatTextView tvTotalCorrectFillInTheBlanks;

    @BindView(R.id.tvTotalCorrectShortAnswer)
    AppCompatTextView tvTotalCorrectShortAnswer;

    @BindView(R.id.btnLeaderboard)
    AppCompatButton btnLeaderboard;

    @BindView(R.id.tvLeaderboardStatus)
    AppCompatTextView tvLeaderboardStatus;

    // qxm info
    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;

    @BindView(R.id.tvQxmCreator)
    AppCompatTextView tvQxmCreator;

    @BindView(R.id.tvQxmCreationTime)
    AppCompatTextView tvQxmCreationTime;

    @BindView(R.id.ivQxmPrivacy)
    AppCompatImageView ivQxmPrivacy;

    @BindView(R.id.ivQxmCreatorImage)
    AppCompatImageView ivQxmCreatorImage;

    @BindView(R.id.ivQuestionImage)
    AppCompatImageView ivQuestionImage;

    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;

    @BindView(R.id.tvQxmDescription)
    AppCompatTextView tvQxmDescription;

    @BindView(R.id.rvFullResultQxmQuestions)
    RecyclerView rvFullResultQxmQuestions;

    @BindView(R.id.noInternetView)
    View noInternetView;
    private boolean amICreator;
    private List<MultipleChoice> multipleChoiceList;
    private FullResultSingleMCQAdapter resultSingleMCQAdapter;


    // endregion

    // region FragmentConstructor

    public FullResultSingleMCQFragment() {
        // Required empty public constructor
    }

    // endregion

    // region NewInstance

    public static FullResultSingleMCQFragment newInstance(String singleMCQId, UserBasic userBasic) {
        Bundle args = new Bundle();
        args.putString(SINGLE_MCQ_ID_KEY, singleMCQId);
        args.putParcelable(USER_BASIC_KEY, userBasic);

        FullResultSingleMCQFragment fragment = new FullResultSingleMCQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FullResultSingleMCQFragment newInstance(String singleMCQId) {
        Bundle args = new Bundle();
        args.putString(SINGLE_MCQ_ID_KEY, singleMCQId);

        FullResultSingleMCQFragment fragment = new FullResultSingleMCQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_result_single_mcq, container, false);

        ButterKnife.bind(this, view);

        context = view.getContext();

        setUpDagger2();

        return view;
    }

    // endregion

    // region SetUpDagger2
    private void setUpDagger2() {

        FullResultSingleMCQFragmentComponent fullResultSingleMCQFragmentComponent =
                DaggerFullResultSingleMCQFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        fullResultSingleMCQFragmentComponent.injectFullResultSingleMCQFragmentFeature(FullResultSingleMCQFragment.this);
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        singleMCQId = getArguments() != null ? getArguments().getString(SINGLE_MCQ_ID_KEY) : null;
        user = getArguments().getParcelable(USER_BASIC_KEY);

        init();
        noInternetFunctionality();


        if (fullResult == null && !TextUtils.isEmpty(singleMCQId)) {
            if (user == null)
                getFullResultOfSingleMCQNetworkCall(singleMCQId, token.getUserId(), token.getToken());
            else
                getFullResultOfSingleMCQNetworkCall(singleMCQId, user.getUserId(), token.getToken());

        } else if (feedData != null && fullResult.getMultipleChoice() != null) {

            initializeUserPerformanceViews();
            initializeQxmInfo();
            initAdapter();
        }

        swipeRL.setOnRefreshListener(() -> {
            swipeRL.setRefreshing(true);
            if (user == null)
                getFullResultOfSingleMCQNetworkCall(singleMCQId, token.getUserId(), token.getToken());
            else
                getFullResultOfSingleMCQNetworkCall(singleMCQId, user.getUserId(), token.getToken());


        });
    }
    // endregion

    // region Init

    private void init() {
        RealmService realmService = new RealmService(Realm.getDefaultInstance());


        token = realmService.getApiToken();
        if (user == null) user = realmService.getSavedUser();

        Log.d(TAG, "init: token.UserId= " + token.getUserId());
        Log.d(TAG, "init: user.UserId= " + user.getUserId());

        amICreator = !token.getUserId().equals(user.getUserId());

        Log.d(TAG, "init: amICreator: " + amICreator);

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        initializeClickListeners();


    }

    // endregion

    // region InitializeUserPerformanceViews

    private void initializeUserPerformanceViews() {
        tvParticipantName.setText(user.getFullName());
        tvParticipationDate.setText(TimeFormatString.getTimeAndDate(
                Long.parseLong(fullResult.getUserPerformance().getParticipatedDate())
        ));

        if (!TextUtils.isEmpty(fullResult.getUserPerformance().getExperienceLevel())) {
            tvUserExperience.setText(fullResult.getUserPerformance().getExperienceLevel());
        } else {
            tvUserExperience.setText("1");
        }

        if (!TextUtils.isEmpty(user.getModifiedURLProfileImage())) {
            picasso.load(user.getModifiedURLProfileImage()).into(ivUserImage);
        }

        tvTotalParticipants.setText(String.format("Total Participants: %s", feedData.getFeedParticipantsQuantity()));

        if (!TextUtils.isEmpty(fullResult.getUserPerformance().getRankPosition())) {

            tvMyPosition.setText(fullResult.getUserPerformance().getRankPosition());
        } else {
            tvMyPosition.setVisibility(View.GONE);
            //tvMyPosition.setText(String.format("My Position: %s", "1"));
        }

        tvAchievedPoints.setText(fullResult.getUserPerformance().getAchievePoints());

        tvAchievedPointsInPercentage.setText(fullResult.getUserPerformance().getAchievePointsInPercentage());

        tvTotalCorrectMCQ.setText(fullResult.getUserPerformance().getTotalCorrectMCQ());
        tvTotalCorrectFillInTheBlanks.setText(fullResult.getUserPerformance().getTotalCorrectFillInTheBlanks());
        tvTotalCorrectShortAnswer.setText(fullResult.getUserPerformance().getTotalCorrectShortAnswer());

    }

    // endregion

    // region InitializeQxmInfo

    private void initializeQxmInfo() {

        if (!TextUtils.isEmpty(feedData.getFeedCreatorImageURL()))
            picasso.load(feedData.getFeedCreatorImageURL()).into(ivQxmCreatorImage);

        tvQxmTitle.setText(fullResult.getMultipleChoice().getQuestionTitle());
        tvQxmCreator.setText(feedData.getFeedCreatorName());

        tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(
                Long.parseLong(feedData.getFeedCreationTime())
        ));

        if (!TextUtils.isEmpty(feedData.getFeedPrivacy())) {
            // TODO:: setQxmPrivacyPrivateImage
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_following));
        }

        if (!TextUtils.isEmpty(fullResult.getMultipleChoice().getImage()))
            picasso.load(fullResult.getMultipleChoice().getImage()).into(ivQuestionImage);
        else
            ivQuestionImage.setVisibility(View.GONE);

        if (TextUtils.isEmpty(fullResult.getMultipleChoice().getYoutubeVideoLink()))
            ivYoutubeButton.setVisibility(View.GONE);

        tvQxmDescription.setText(fullResult.getMultipleChoice().getDescription());
        SetlinkClickable.setLinkclickEvent(tvQxmDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });

    }

    // endregion

    // region initializeClickListeners

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {

                Fragment fragment = getFragmentManager().findFragmentByTag(ParticipateSingleMCQFragment.class.getName());

                if (fragment != null)
                    getFragmentManager().popBackStack(ParticipateSingleMCQFragment.class.getName(), POP_BACK_STACK_INCLUSIVE);

                getFragmentManager().popBackStack();
            }
        });

        btnLeaderboard.setOnClickListener(v -> {
            //Todo:: working on it...
            // Toast.makeText(context, R.string.toast_message_we_are_working_on_it_it_will_back_soon, Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(singleMCQId)) {

                Bundle bundle = new Bundle();
                bundle.putString(QXM_ID_KEY, singleMCQId);
                bundle.putParcelable(USER_BASIC_KEY, user);
                bundle.putString(QXM_QUESTION_SET_TITLE, fullResult.getMultipleChoice().getQuestionTitle());
                bundle.putString(QXM_CREATOR_NAME_KEY, fullResult.getFeedData().getFeedCreatorName());
                bundle.putString(QXM_CREATION_TIME_KEY, fullResult.getFeedData().getFeedCreationTime());
                bundle.putString(QXM_PRIVACY_KEY, fullResult.getFeedData().getFeedPrivacy());
                bundle.putString(SINGLE_MCQ_LEADER_BOARD_PRIVACY,fullResult.getLeaderboardPrivacy());
                qxmFragmentTransactionHelper.loadLeaderboardSingleMCQFragment(bundle);
            } else {
                Log.d(TAG, "loadLeaderboard -> QxmId is empty or null");
                Toast.makeText(context, "Try again..later.", Toast.LENGTH_SHORT).show();
            }
        });

        ivQuestionImage.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(fullResult.getMultipleChoice().getYoutubeVideoLink())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, fullResult.getMultipleChoice().getYoutubeVideoLink());
                context.startActivity(intent);
            }

        });


    }

    // endregion

    // region InitAdapter

    private void initAdapter() {

        multipleChoiceList = new ArrayList<>();
        multipleChoiceList.add(fullResult.getMultipleChoice());
        resultSingleMCQAdapter = new FullResultSingleMCQAdapter(context, picasso, multipleChoiceList, amICreator);
        rvFullResultQxmQuestions.setLayoutManager(new LinearLayoutManager(context));
        rvFullResultQxmQuestions.setAdapter(resultSingleMCQAdapter);
        //rvFullResultQxmQuestions.addItemDecoration(new RecyclerViewItemDecoration(5, 5, 5, 5));
        rvFullResultQxmQuestions.setNestedScrollingEnabled(false);
    }
    // endregion

    // region GetFullResultNetworkCall

    public void getFullResultOfSingleMCQNetworkCall(String singleMCQId, String userId, String token) {

        Log.d(TAG, "getFullResultAndLoadView: singleMCQId: " + singleMCQId + ", userId: " + userId + ", token: " + token);
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("SingleMCQ Result", "Loading your result. Please wait..", false);

        Call<FullResultSingleMCQ> getQxmFullResult = apiService.getSingleMCQFullResult(token, userId, singleMCQId);

        getQxmFullResult.enqueue(new Callback<FullResultSingleMCQ>() {
            @Override
            public void onResponse(@NonNull Call<FullResultSingleMCQ> call, @NonNull Response<FullResultSingleMCQ> response) {
                Log.d(TAG, "onResponse: getFullResultAndLoadView network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);
                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {
                        fullResult = new FullResultSingleMCQ();
                        fullResult.setMultipleChoice(response.body().getMultipleChoice());
                        fullResult.setUserPerformance(response.body().getUserPerformance());
                        fullResult.setFeedData(response.body().getFeedData());
                        fullResult.setMarks(response.body().getMarks());
                        fullResult.setEvaluated(response.body().isEvaluated());
                        fullResult.setLeaderboardPrivacy(response.body().getLeaderboardPrivacy());
                        fullResult.setLeaderboardPublishDate(response.body().getLeaderboardPublishDate());

                        if ((fullResult.getMultipleChoice() != null && fullResult.getUserPerformance() != null)) {

                            feedData = fullResult.getFeedData();
                            showOrHideLeaderboard();
                            initAdapter();
                            initializeUserPerformanceViews();

                            initializeQxmInfo();

                            llFullResultContainer.setVisibility(View.VISIBLE);


                        } else {
                            Log.d(TAG, "fullResult.getQuestionSet() / fullResult.getUserPerformance() might be null");
                            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "response body is null");
                        Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 404) {

                    Toast.makeText(context, getString(R.string.result_not_published_yet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<FullResultSingleMCQ> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showOrHideLeaderboard() {
        if (isLeaderboardCanBeShown()) {
            btnLeaderboard.setVisibility(View.VISIBLE);
            tvLeaderboardStatus.setVisibility(View.GONE);
        } else {
            btnLeaderboard.setVisibility(View.GONE);
            tvLeaderboardStatus.setVisibility(View.VISIBLE);
        }
    }

    private boolean isLeaderboardCanBeShown() {

        // Leader board Privacy=PUBLIC, PublishType=Continue **means show leader board always
        if (fullResult.getLeaderboardPrivacy() != null && fullResult.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PUBLIC)) {

            if (fullResult.getLeaderboardPublishDate() != null) {

                if (fullResult.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTINUE))
                    return true;
                else if (fullResult.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTEST_END)
                        || fullResult.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES)) {
                    // If contest mode enabled
                    if (QxmStringIntegerChecker.isLongInt(fullResult.getQxmExpirationDate())) {
                        long qxmExpirationDate = Long.parseLong(fullResult.getQxmExpirationDate());
                        long currentDate = Calendar.getInstance().getTimeInMillis();
                        tvLeaderboardStatus.setText(String.format(getString(R.string.leaderboard_will_be_published_at_with_variable), TimeFormatString.getTimeAndDate(qxmExpirationDate)));
                        return currentDate > qxmExpirationDate;
                    }
                } else if (QxmStringIntegerChecker.isLongInt(fullResult.getLeaderboardPublishDate())) {
                    long leaderboardPublishDate = Long.parseLong(fullResult.getLeaderboardPublishDate());
                    long currentDate = Calendar.getInstance().getTimeInMillis();
                    tvLeaderboardStatus.setText(String.format(getString(R.string.leaderboard_will_be_published_at_with_variable), TimeFormatString.getTimeAndDate(leaderboardPublishDate)));
                    return currentDate > leaderboardPublishDate;
                }
            }

        }
        else return fullResult.getLeaderboardPrivacy() != null && fullResult.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PARTICIPANTS);
        return false;
    }

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v ->
                getFullResultOfSingleMCQNetworkCall(singleMCQId, token.getUserId(), token.getToken()));
    }

    //endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (fullResult != null)
            llFullResultContainer.setVisibility(View.VISIBLE);

        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

        // The following boolean flag variable is used to
        // restrict from occurring multiple Single MCQ touch events (for solving Fragment Overlay)
        isFeedSingleMCQItemClicked = false;
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
