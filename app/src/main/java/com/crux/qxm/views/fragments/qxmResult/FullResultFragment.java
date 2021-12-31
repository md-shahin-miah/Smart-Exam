package com.crux.qxm.views.fragments.qxmResult;


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
import com.crux.qxm.adapter.fullResultQxm.FullResultQxmAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.result.FullResult;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.fullResultFragmentFeature.DaggerFullResultFragmentComponent;
import com.crux.qxm.di.fullResultFragmentFeature.FullResultFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.RecyclerViewItemDecoration;
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

import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PARTICIPANTS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_QXM_ID;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_LEADERBOARD_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.USER_BASIC_KEY;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullResultFragment extends Fragment {

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

    @BindView(R.id.llcNegativeMarks)
    LinearLayoutCompat llcNegativeMarks;
    @BindView(R.id.tvNegativeMarks)
    AppCompatTextView tvNegativeMarks;

    @BindView(R.id.tvCorrectAnswerVisibility)
    AppCompatTextView tvCorrectAnswerVisibility;

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;


    // endregion

    // region FullResultFragment-ClassProperties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;

    private static final String TAG = "FullResultFragment";

    private Context context;
    private UserBasic user;
    private QxmToken token;
    private FullResult fullResult;
    private FeedData feedData;
    private List<Question> questionList;
    private String qxmId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private double negativePoint;
    private boolean amICreator;


    // endregion

    // region FragmentConstructor

    public FullResultFragment() {
        // Required empty public constructor
    }

    // endregion

    // region NewInstance

    public static FullResultFragment newInstance(String qxmId) {
        Bundle args = new Bundle();
        args.putString(QXM_ID_KEY, qxmId);

        FullResultFragment fragment = new FullResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FullResultFragment newInstance(String qxmId, UserBasic userBasic) {
        Bundle args = new Bundle();
        args.putString(QXM_ID_KEY, qxmId);
        args.putParcelable(USER_BASIC_KEY, userBasic);

        FullResultFragment fragment = new FullResultFragment();
        fragment.setArguments(args);
        return fragment;

    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_result, container, false);

        ButterKnife.bind(this, view);

        context = view.getContext();

        setUpDagger2();

        return view;
    }

    // endregion

    // region SetUpDagger2
    private void setUpDagger2() {

        FullResultFragmentComponent fullResultFragmentComponent =
                DaggerFullResultFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        fullResultFragmentComponent.injectFullResultFragment(FullResultFragment.this);
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        qxmId = getArguments() != null ? getArguments().getString(NOTIFICATION_QXM_ID) : null;
        user = getArguments() != null ? getArguments().getParcelable(USER_BASIC_KEY) : null;

        init();

        noInternetFunctionality();

        if (fullResult == null && !TextUtils.isEmpty(qxmId)) {
            getFullResultNetworkCall(qxmId, user.getUserId(), token.getToken());
        } else if (feedData != null && questionList != null) {
            initAdapter();
            initializeUserPerformanceViews();
            initializeQxmInfo();
        }

        swipeRL.setOnRefreshListener(() -> {
            swipeRL.setRefreshing(true);
            getFullResultNetworkCall(qxmId, user.getUserId(), token.getToken());
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

        if (questionList == null)
            questionList = new ArrayList<>();

        /*if(!realmService.getSavedUser().getUserId().equals(user.getUserId()))
            btnLeaderboard.setVisibility(View.GONE);*/


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

        if (fullResult.getMyPosition() > 0)
            tvMyPosition.setText(String.format("My Position: %s", fullResult.getMyPosition()));
        else
            tvMyPosition.setText(String.format("My Position: %s", "1"));


        tvAchievedPoints.setText(fullResult.getUserPerformance().getAchievePoints());

        tvAchievedPointsInPercentage.setText(fullResult.getUserPerformance().getAchievePointsInPercentage());

        tvTotalCorrectMCQ.setText(fullResult.getUserPerformance().getTotalCorrectMCQ());
        tvTotalCorrectFillInTheBlanks.setText(fullResult.getUserPerformance().getTotalCorrectFillInTheBlanks());
        tvTotalCorrectShortAnswer.setText(fullResult.getUserPerformance().getTotalCorrectShortAnswer());


        if (fullResult.getUserPerformance().getNegativePoints() != null) {
            negativePoint = Double.parseDouble(fullResult.getUserPerformance().getNegativePoints());
            Log.d(TAG, "initializeUserPerformanceViews: negativePoint: " + negativePoint);
            if (Double.parseDouble(fullResult.getUserPerformance().getNegativePoints()) > 0) {
                llcNegativeMarks.setVisibility(View.VISIBLE);
                tvNegativeMarks.setText(fullResult.getUserPerformance().getNegativePoints());
            }

        }
    }

    // endregion

    private boolean isLeaderboardCanBeShown() {

        // Leaderboard Privacy=PUBLIC, PublishType=Continue **means show leaderboard always

        if (fullResult.getLeaderboardPrivacy() != null &&
                ((fullResult.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PUBLIC))
                        || fullResult.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PARTICIPANTS)
                )) {

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

        return false;
    }

    // region InitializeQxmInfo

    private void initializeQxmInfo() {

        if (!TextUtils.isEmpty(feedData.getFeedCreatorImageURL()))
            picasso.load(feedData.getFeedCreatorImageURL()).into(ivQxmCreatorImage);

        tvQxmTitle.setText(fullResult.getQuestionSet().getQuestionSetTitle());
        tvQxmCreator.setText(feedData.getFeedCreatorName());

        tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(
                Long.parseLong(feedData.getFeedCreationTime())
        ));

        if (!TextUtils.isEmpty(feedData.getFeedPrivacy())) {
            // TODO:: setQxmPrivacyPrivateImage
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_following));
        }


        if (!TextUtils.isEmpty(fullResult.getQuestionSet().getQuestionSetThumbnail()))
            picasso.load(fullResult.getQuestionSet().getQuestionSetThumbnail()).into(ivQuestionImage);
        else
            ivQuestionImage.setVisibility(View.GONE);

        if (TextUtils.isEmpty(fullResult.getQuestionSet().getQuestionSetYoutubeLink()))
            ivYoutubeButton.setVisibility(View.GONE);

        tvQxmDescription.setText(fullResult.getQuestionSet().getQuestionSetDescription());
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
                getFragmentManager().popBackStack();
            }
        });

        btnLeaderboard.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(qxmId)) {

                Log.d(TAG, "initializeClickListeners feedData " + fullResult.getFeedData());
                Log.d(TAG, "initializeClickListeners feedCreatorId: " + fullResult.getFeedData().getFeedCreatorId());
                Log.d(TAG, "initializeClickListeners leaderBoardPrivacy: " + fullResult.getFeedData().getLeaderboardPrivacy());

                Bundle bundle = new Bundle();
                bundle.putString(QXM_ID_KEY, qxmId);
                bundle.putParcelable(USER_BASIC_KEY, user);
                bundle.putString(QXM_QUESTION_SET_TITLE, fullResult.getQuestionSet().getQuestionSetTitle());
                bundle.putString(QXM_CREATOR_ID_KEY, fullResult.getFeedData().getFeedCreatorId());
                bundle.putString(QXM_CREATOR_NAME_KEY, fullResult.getFeedData().getFeedCreatorName());
                bundle.putString(QXM_CREATION_TIME_KEY, fullResult.getFeedData().getFeedCreationTime());
                bundle.putString(QXM_PRIVACY_KEY, fullResult.getFeedData().getFeedPrivacy());
                bundle.putString(QXM_LEADERBOARD_PRIVACY_KEY, fullResult.getLeaderboardPrivacy());
                qxmFragmentTransactionHelper.loadLeaderboardFragment(bundle);
            } else {
                Log.d(TAG, "loadLeaderboard -> QxmId is empty or null");
                Toast.makeText(context, "Try again..later.", Toast.LENGTH_SHORT).show();
            }
        });


        // region play youtube video if available
        ivQuestionImage.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(feedData.getFeedYoutubeLinkURL())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, feedData.getFeedYoutubeLinkURL());
                context.startActivity(intent);
            }

        });
        //endregion

    }

    // endregion

    // region InitAdapter

    private void initAdapter() {

        if (fullResult.getUserPerformance() != null && fullResult.getUserPerformance().getNegativePoints() != null)
            negativePoint = Double.parseDouble(fullResult.getUserPerformance().getNegativePoints());

        FullResultQxmAdapter resultQxmAdapter = new FullResultQxmAdapter(context, picasso, fullResult.getQuestionSet().getQuestions(), negativePoint, amICreator);

        rvFullResultQxmQuestions.setLayoutManager(new LinearLayoutManager(context));
        rvFullResultQxmQuestions.setAdapter(resultQxmAdapter);
        rvFullResultQxmQuestions.addItemDecoration(new RecyclerViewItemDecoration(5, 5, 5, 5));
        rvFullResultQxmQuestions.setNestedScrollingEnabled(false);

        if (QxmStringIntegerChecker.isNumber(fullResult.getCorrectAnswerVisibilityDate())) {

            long currentMillis = System.currentTimeMillis();
            long answerVisibilityMillis = Long.parseLong(fullResult.getCorrectAnswerVisibilityDate());
            if (answerVisibilityMillis <= currentMillis) {
                rvFullResultQxmQuestions.setVisibility(View.VISIBLE);
            } else {
                rvFullResultQxmQuestions.setVisibility(View.GONE);
                tvCorrectAnswerVisibility.setVisibility(View.VISIBLE);
                String correctAnswerTime =
                        getString(R.string.correct_answer_visibility_prefix) + " " +
                                TimeFormatString.getTimeAndDate(answerVisibilityMillis);
                tvCorrectAnswerVisibility.setText(correctAnswerTime);

            }
        } else {
            rvFullResultQxmQuestions.setVisibility(View.VISIBLE);
            tvCorrectAnswerVisibility.setVisibility(View.GONE);
        }
    }
    // endregion

    // region GetFullResultNetworkCall

    public void getFullResultNetworkCall(String qxmId, String userId, String token) {
        Log.d(TAG, "getFullResultAndLoadView: qxmId: " + qxmId + ", userBasic: " + userId + ", token: " + token);
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Qxm Result", "Loading your result. Please wait..", false);

        Call<FullResult> getQxmFullResult = apiService.getQxmFullResult(token, userId, qxmId);

        getQxmFullResult.enqueue(new Callback<FullResult>() {
            @Override
            public void onResponse(@NonNull Call<FullResult> call, @NonNull Response<FullResult> response) {

                Log.d(TAG, "onResponse: getFullResultAndLoadView network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {
                        fullResult = new FullResult();
                        fullResult = response.body();

//                        fullResult.setQuestionSet(response.body() != null ? response.body().getQuestionSet() : null);
//                        fullResult.setUserPerformance(response.body() != null ? response.body().getUserPerformance() : null);
//                        fullResult.setMarks(response.body() != null ? response.body().getMarks() : null);
//                        fullResult.setEvaluated(response.body() != null && response.body().isEvaluated());
//                        fullResult.setFeedData(response.body().getFeedData());

                        if ((fullResult.getQuestionSet() != null && fullResult.getUserPerformance() != null)) {

                            feedData = fullResult.getFeedData();

                            questionList = fullResult.getQuestionSet().getQuestions();

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
            public void onFailure(@NonNull Call<FullResult> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                swipeRL.setRefreshing(false);
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

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v ->
                getFullResultNetworkCall(qxmId, token.getUserId(), token.getToken()));
    }

    //endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (fullResult != null) {
            showOrHideLeaderboard();
            llFullResultContainer.setVisibility(View.VISIBLE);
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
}
