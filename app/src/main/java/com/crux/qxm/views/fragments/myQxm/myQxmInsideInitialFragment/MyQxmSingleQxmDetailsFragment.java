package com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.singleQxmDetails.MyQxmSingleQxmQuestionSheetAdapter;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.myQxm.MyQxmDetails;
import com.crux.qxm.db.models.myQxm.SingleQxmSettings;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.MyQxmSingleQxmDetailsFragmentFeature.DaggerMyQxmSingleQxmDetailsFragmentComponent;
import com.crux.qxm.di.MyQxmSingleQxmDetailsFragmentFeature.MyQxmSingleQxmDetailsFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAddQxmToList;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmTotalPointsCalculator;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.CreateQxmActivity;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.EDIT_QXM_REQUEST;
import static com.crux.qxm.utils.StaticValues.ENROLLED_LIST_FRAGMENT_BUNDLE_DATA;
import static com.crux.qxm.utils.StaticValues.EVALUATE_AUTOMATIC;
import static com.crux.qxm.utils.StaticValues.EVALUATE_MANUAL;
import static com.crux.qxm.utils.StaticValues.EVALUATE_SEMI_AUTO;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.PENDING_EVALUATION_LIST_FRAGMENT_BUNDLE_DATA;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_AFTER_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_BEFORE_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QXM;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_NOT_STARTED;
import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_EDIT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.QXM_START_MANUAL;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.SINGLE_QXM_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isMyQxmSingleQxmDetailsRefreshingNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmSingleQxmDetailsFragment extends Fragment {

    //region Fragment-Properties
    private static final String TAG = "MyQxmSingleQxmDetailsFr";
    private FeedData myQxmFeedData;
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    //    private QxmModel qxmModel; // --> qxmModel will be used for get ExamQuestionSheet
    private QxmToken token;
    private String qxmTitle;
    private MyQxmDetails myQxmDetails;
    private List<Question> questionList;
    private MyQxmSingleQxmQuestionSheetAdapter examQuestionSheetAdapter;
    private String qxmId;


    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    //endregion

    //region BindViewWithButterKnife
    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;
    @BindView(R.id.nsvRootView)
    NestedScrollView nsvRootView;
    @BindView(R.id.tvQuestionTitle)
    AppCompatTextView tvQuestionTitle;
    @BindView(R.id.ivQuestionPrivacy)
    AppCompatImageView ivQuestionPrivacy;
    @BindView(R.id.tvQuestionPrivacy)
    AppCompatTextView tvQuestionPrivacy;
    @BindView(R.id.ivQuestionOptions)
    AppCompatImageView ivQuestionOptions;
    @BindView(R.id.ivQuestionThumbnail)
    AppCompatImageView ivQuestionThumbnail;
    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;
    @BindView(R.id.tvQuestionDescription)
    AppCompatTextView tvQuestionDescription;
    @BindView(R.id.tvQuestionCategory)
    AppCompatTextView tvQuestionCategory;
    @BindView(R.id.tvQuestionTotalTime)
    AppCompatTextView tvQuestionTotalTime;
    @BindView(R.id.tvTotalPoints)
    AppCompatTextView tvTotalPoints;
    @BindView(R.id.tvHoursAgo)
    AppCompatTextView tvHoursAgo;
    @BindView(R.id.tvMyQxmManualStartMessage)
    AppCompatTextView tvMyQxmManualStartMessage;
    @BindView(R.id.btnMyQxmManualStart)
    AppCompatButton btnMyQxmManualStart;
    @BindView(R.id.cvContest)
    CardView cvContest;
    @BindView(R.id.rvQuestionSheet)
    RecyclerView rvQuestionSheet;
    @BindView(R.id.llEnrollCountContainer)
    LinearLayoutCompat llEnrollCountContainer;
    @BindView(R.id.tvViewAllEnrolled)
    AppCompatTextView tvViewAllEnrolled;
    @BindView(R.id.llTotalParticipants)
    LinearLayoutCompat llTotalParticipants;
    @BindView(R.id.llTotalPendingEvaluation)
    LinearLayoutCompat llTotalPendingEvaluation;
    @BindView(R.id.tvViewAllParticipants)
    AppCompatTextView tvViewAllParticipants;
    @BindView(R.id.btViewAllPendingEvaluation)
    AppCompatButton btViewAllPendingEvaluation;
    @BindView(R.id.tvTotalEnrolled)
    AppCompatTextView tvTotalEnrolled;
    @BindView(R.id.tvTotalPendingTitle)
    AppCompatTextView tvTotalPendingTitle;
    @BindView(R.id.tvTotalReviewPending)
    AppCompatTextView tvTotalReviewPending;
    @BindView(R.id.tvTotalParticipants)
    AppCompatTextView tvTotalParticipants;
    @BindView(R.id.btnLeaderboard)
    AppCompatButton btnLeaderboard;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.noInternetView)
    View noInternetView;

    //Qxm Settings
    @BindView(R.id.tvQxmStartSchedule)
    AppCompatTextView tvQxmStartSchedule;

    @BindView(R.id.tvEnrollNeeded)
    AppCompatTextView tvEnrollNeeded;

    @BindView(R.id.tvEvaluationType)
    AppCompatTextView tvEvaluationType;

    @BindView(R.id.tvNegativePoints)
    AppCompatTextView tvNegativePoints;

    @BindView(R.id.tvQuestionVisibility)
    AppCompatTextView tvQuestionVisibility;

    @BindView(R.id.tvCorrectAnswerVisibility)
    AppCompatTextView tvCorrectAnswerVisibility;

    @BindView(R.id.llQxmExpirationDate)
    LinearLayoutCompat llQxmExpirationDate;

    @BindView(R.id.tvQxmExpirationDate)
    AppCompatTextView tvQxmExpirationDate;

    @BindView(R.id.tvContestModeEnabled)
    AppCompatTextView tvContestModeEnabled;

    @BindView(R.id.tvContestStartTime)
    AppCompatTextView tvContestStartTime;

    @BindView(R.id.tvContestEndTime)
    AppCompatTextView tvContestEndTime;

    @BindView(R.id.tvPriorityOne)
    AppCompatTextView tvPriorityOne;

    @BindView(R.id.tvPriorityTwo)
    AppCompatTextView tvPriorityTwo;

    @BindView(R.id.tvPriorityThree)
    AppCompatTextView tvPriorityThree;

    @BindView(R.id.tvNumberOfWinner)
    AppCompatTextView tvNumberOfWinner;

    @BindView(R.id.tvParticipationAfterContestEnd)
    AppCompatTextView tvParticipationAfterContestEnd;

    @BindView(R.id.llContestStartTime)
    LinearLayoutCompat llContestStartTime;

    @BindView(R.id.llContestEndTime)
    LinearLayoutCompat llContestEndTime;

    @BindView(R.id.llWinnerSelectionRule)
    LinearLayoutCompat llWinnerSelectionRule;

    @BindView(R.id.tvLeaderboardPublishTime)
    AppCompatTextView tvLeaderboardPublishTime;

    @BindView(R.id.tvLeaderboardPrivacy)
    AppCompatTextView tvLeaderboardPrivacy;

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;


    //endregion

    // region Fragment-Constructor
    public MyQxmSingleQxmDetailsFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreate

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        // receiving myQxmFeed data from MyQxmAdapter using parcelable
        assert getArguments() != null;
        qxmId = getArguments().getString(FEED_DATA_PASS_TO_MY_QXM_DETAILS);


        if (qxmId != null)
            Log.d(TAG, qxmId);
        else
            Toast.makeText(getContext(), "Data not loaded...", Toast.LENGTH_SHORT).show();
    }

    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_single_qxm_details, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");

        setUpDagger2(context);

        init();
        noInternetFunctionality();

        if (myQxmDetails == null) {
            myQxmDetailsNetworkCall(qxmId);

        } else {
            nsvRootView.setVisibility(View.VISIBLE);
            setValuesInViews();

            loadAdapterWithPreviousData();
        }

        viewsOnClick();

        swipeRL.setOnRefreshListener(() -> {
            swipeRL.setRefreshing(true);
            myQxmDetailsNetworkCall(qxmId);

        });
    }

    // endregion

    //region SetUpDagger2
    private void setUpDagger2(Context context) {
        MyQxmSingleQxmDetailsFragmentComponent myQxmSingleQxmDetailsFragmentComponent =
                DaggerMyQxmSingleQxmDetailsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmSingleQxmDetailsFragmentComponent.injectMyQxmSingleQxmDetailsFragmentFeature(MyQxmSingleQxmDetailsFragment.this);
    }
    //endregion

    //region NoInternetFunctionality
    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(View.GONE);
            myQxmDetailsNetworkCall(qxmId);
        });
    }
    //endregion

    // region Init
    public void init() {

        if (myQxmFeedData == null) {
            myQxmFeedData = new FeedData();
        }
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        // set toolbar
        qxmTitle = getArguments() != null ? getArguments().getString(SINGLE_QXM_DETAILS_TITLE_KEY) : null;
        if (!TextUtils.isEmpty(qxmTitle))
            tvToolbarTitle.setText(qxmTitle);

        Log.d(TAG, "init: " + token.getUserId());
    }
    // endregion

    // region Setting View Values

    public void setValuesInViews() {

        tvQuestionTitle.setText(myQxmFeedData.getFeedName());
        if (myQxmFeedData.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
            ivQuestionPrivacy.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_public));
        else
            ivQuestionPrivacy.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_private));
        tvQuestionPrivacy.setText(myQxmFeedData.getFeedPrivacy());

        if (myQxmFeedData.getFeedThumbnailURL() != null && !TextUtils.isEmpty(myQxmFeedData.getFeedThumbnailURL()))
            picasso.load(myQxmFeedData.getFeedThumbnailURL()).into(ivQuestionThumbnail);
        else ivQuestionThumbnail.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(myQxmFeedData.getFeedYoutubeLinkURL()))
            ivYoutubeButton.setVisibility(View.VISIBLE);
        else
            ivYoutubeButton.setVisibility(View.GONE);

        tvQuestionDescription.setText(myQxmFeedData.getFeedDescription());

        SetlinkClickable.setLinkclickEvent(tvQuestionDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });



        if (myQxmFeedData.isContestModeOn()) {
            cvContest.setVisibility(View.VISIBLE);
        } else {
            cvContest.setVisibility(View.GONE);
        }


        tvTotalEnrolled.setText(myQxmFeedData.getFeedEnrollAcceptedQuantity());

        if (!myQxmFeedData.isEnrollEnabled())
            llEnrollCountContainer.setVisibility(View.GONE);
            // hiding view all textView if enroll quantity is 0
        else if (myQxmFeedData.getFeedEnrollAcceptedQuantity().equals("0")) {
            tvViewAllEnrolled.setVisibility(View.GONE);
        }

        tvTotalParticipants.setText(myQxmFeedData.getFeedParticipantsQuantity());

        // hiding view all textView if participants quantity is 0
        if (myQxmFeedData.getFeedParticipantsQuantity().equals("0")) {
            tvViewAllParticipants.setVisibility(View.GONE);
        }

        tvTotalReviewPending.setText(myQxmFeedData.getFeedPendingEvaluationQuantity());

        // hiding view all textView if pending evaluation quantity is 0
        if (myQxmFeedData.getFeedPendingEvaluationQuantity().equals("0")) {
            btViewAllPendingEvaluation.setVisibility(View.GONE);
        } else {
            tvTotalPendingTitle.setTextColor(context.getResources().getColor(R.color.warning));
            tvTotalReviewPending.setTextColor(context.getResources().getColor(R.color.warning));
        }

        //setting category

        tvQuestionCategory.setText(QxmArrayListToStringProcessor.getStringFromArrayList(myQxmFeedData.getFeedCategory()));

        Log.d(TAG, "setValuesInViews: Question time: ");

        String regex = "[0-9]+";
        if (myQxmFeedData.getFeedExamDuration().matches(regex)) {
            tvQuestionTotalTime.setText(TimeFormatString.getDurationInHourMinSec(
                    Long.parseLong(myQxmFeedData.getFeedExamDuration())
            ));
        } else
            tvQuestionTotalTime.setText(getString(R.string.unlimited));
        // set total points
        tvTotalPoints.setText(QxmTotalPointsCalculator.getTotalPoints(myQxmDetails.getQuestionList()));

        // show feed item time ago
        long feedCreationTime = Long.parseLong(myQxmFeedData.getFeedCreationTime());
        String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        tvHoursAgo.setText(feedPostTimeAgo);

        if (!TextUtils.isEmpty(myQxmDetails.getQxmStartScheduleDate())
                && !TextUtils.isEmpty(myQxmDetails.getQxmCurrentStatus())) {
            Log.d(TAG, "QxmStartScheduleDate: " + myQxmDetails.getQxmStartScheduleDate());
            if (myQxmDetails.getQxmStartScheduleDate().equals(QXM_START_MANUAL)
                    && myQxmDetails.getQxmCurrentStatus().equals(QXM_CURRENT_STATUS_NOT_STARTED)) {
                tvMyQxmManualStartMessage.setVisibility(View.VISIBLE);
                btnMyQxmManualStart.setVisibility(View.VISIBLE);
                llTotalParticipants.setVisibility(View.GONE);
                llTotalPendingEvaluation.setVisibility(View.GONE);
                btnLeaderboard.setVisibility(View.GONE);
            } else {
                tvMyQxmManualStartMessage.setVisibility(View.GONE);
                btnMyQxmManualStart.setVisibility(View.GONE);
                llTotalParticipants.setVisibility(View.VISIBLE);
                llTotalPendingEvaluation.setVisibility(View.VISIBLE);
                if (QxmStringIntegerChecker.isInt(myQxmFeedData.getFeedParticipantsQuantity())
                        && Integer.parseInt(myQxmFeedData.getFeedParticipantsQuantity()) > 0)
                    btnLeaderboard.setVisibility(View.VISIBLE);
                else
                    btnLeaderboard.setVisibility(View.GONE);
            }
        }

        //region QxmSettings
        SingleQxmSettings settings = myQxmDetails.getSingleQxmSettings();
        //region qxmStartScheduleDate
        if (QxmStringIntegerChecker.isLongInt(settings.getQxmStartScheduleDate())) {

            tvQxmStartSchedule.setText(TimeFormatString.getTimeAndDate(Long.parseLong(settings.getQxmStartScheduleDate())));
        } else {
            tvQxmStartSchedule.setText(settings.getQxmStartScheduleDate());
        }
        //endregion

        //region enrollStatus
        if (settings.isEnrollStatus()) tvEnrollNeeded.setText(getString(R.string.yes));
        else tvEnrollNeeded.setText(getString(R.string.no));
        //endregion

        //region evaluationType
        switch (settings.getEvaluationType()) {
            case EVALUATE_AUTOMATIC:
                tvEvaluationType.setText(getString(R.string.automatic));
                break;
            case EVALUATE_MANUAL:
                tvEvaluationType.setText(getString(R.string.manual));
                break;
            case EVALUATE_SEMI_AUTO:
                tvEvaluationType.setText(getString(R.string.semi_automatic));
                break;

        }

        //endregion

        //region negativePoints
        if (settings.getNegativePoints() == 0)
            tvNegativePoints.setText(getString(R.string.no));
        else
            tvNegativePoints.setText(String.valueOf(settings.getNegativePoints()));

        // endregion

        //region questionVisibility
        switch (settings.getQuestionVisibility()) {
            case QUESTION_VISIBILITY_AFTER_PARTICIPATION:
                tvQuestionVisibility.setText(getString(R.string.after_participation));
                break;
            case QUESTION_VISIBILITY_BEFORE_PARTICIPATION:
                tvQuestionVisibility.setText(getString(R.string.before_participation));
                break;
        }
        //endregion

        //region correctAnswerVisibilityDate
        switch (settings.getCorrectAnswerVisibilityDate()) {
            case CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS:
                tvCorrectAnswerVisibility.setText(getString(R.string.after_contest_ends));
                break;
            case CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES:
                tvCorrectAnswerVisibility.setText(getString(R.string.after_qxm_expires));
                break;
            case CORRECT_ANSWER_VISIBILITY_IMMEDIATELY:
                tvCorrectAnswerVisibility.setText(getString(R.string.after_participation));
                break;
            default:
                if (QxmStringIntegerChecker.isLongInt(settings.getCorrectAnswerVisibilityDate())) {
                    tvCorrectAnswerVisibility.setText(TimeFormatString.getTimeAndDate(
                            Long.parseLong(settings.getCorrectAnswerVisibilityDate())
                    ));
                } else {
                    tvCorrectAnswerVisibility.setText(getString(R.string.after_participation));
                }
        }
        //endregion

        // region QxmExpiration
        if (QxmStringIntegerChecker.isLongInt(settings.getQxmExpirationDate()))
            tvQxmExpirationDate.setText(TimeFormatString.getTimeAndDate(
                    Long.parseLong(settings.getQxmExpirationDate())
            ));
        else
            tvQxmExpirationDate.setText(getString(R.string.never));

        // endregion

        //isContestModeEnabled
        if (settings.isContestModeEnabled()) {
            // if contest mode enabled then expiration date will be the contest end date..
            // so hide the qxmExpiration date
            llQxmExpirationDate.setVisibility(View.GONE);

            tvContestModeEnabled.setText(getString(R.string.yes));
            // Contest Start Time
            if (QxmStringIntegerChecker.isLongInt(settings.getQxmStartScheduleDate()))
                tvContestStartTime.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(settings.getQxmStartScheduleDate())
                ));
            else
                tvContestStartTime.setText("");

            // contest end time
            if (QxmStringIntegerChecker.isLongInt(settings.getQxmExpirationDate()))
                tvContestEndTime.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(settings.getQxmExpirationDate())
                ));
            else
                tvContestEndTime.setText("");

            // Number of winner
            tvNumberOfWinner.setText(String.valueOf(settings.getNumberOfWinners()));

            // winning priority
            if (settings.getWinnerSelectionPriorityRules().size() == 3) {

                tvPriorityOne.setText(String.format("Priority 1: %s", settings.getWinnerSelectionPriorityRules().get(0)));

                tvPriorityTwo.setText(String.format("Priority 2: %s", settings.getWinnerSelectionPriorityRules().get(1)));

                tvPriorityThree.setText(String.format("Priority 3: %s", settings.getWinnerSelectionPriorityRules().get(2)));
            }

            if (settings.isParticipateAfterContestEnd()) {
                tvParticipationAfterContestEnd.setText(getString(R.string.yes));
            } else {
                tvParticipationAfterContestEnd.setText(getString(R.string.no));
            }


        } else {
            llQxmExpirationDate.setVisibility(View.VISIBLE);
            tvContestModeEnabled.setText(getString(R.string.no));
            llContestStartTime.setVisibility(View.GONE);
            llContestEndTime.setVisibility(View.GONE);
            llWinnerSelectionRule.setVisibility(View.GONE);

        }

        //leaderboard publish date
        switch (settings.getLeaderboardPublishDate()) {
            case LEADERBOARD_PUBLISH_TYPE_CONTINUE:
                tvLeaderboardPublishTime.setText(getString(R.string.continuous_update));
                break;

            case LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES:
                tvLeaderboardPublishTime.setText(getString(R.string.after_qxm_expires));
                break;

            case LEADERBOARD_PUBLISH_TYPE_CONTEST_END:
                tvLeaderboardPublishTime.setText(getString(R.string.after_contest_ends));
                break;
            default:
                if (QxmStringIntegerChecker.isLongInt(settings.getLeaderboardPublishDate())) {
                    tvLeaderboardPublishTime.setText(
                            TimeFormatString.getTimeAndDate(Long.parseLong(
                                    settings.getLeaderboardPublishDate()
                            ))
                    );
                } else {
                    tvLeaderboardPublishTime.setText(getString(R.string.continuous_update));
                }

        }

        //leaderboard privacy
        if (settings.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PUBLIC))
            tvLeaderboardPrivacy.setText(getString(R.string.privacy_public));
        else tvLeaderboardPrivacy.setText(getString(R.string.participants_only));

        // endregion
    }


    //endregion

    //region View On Click Listener
    public void viewsOnClick() {

        ivQuestionOptions.setOnClickListener(v -> showSingleQxmDetailsOptionMenu(v, getActivity()));

        ivQuestionThumbnail.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(myQxmFeedData.getFeedYoutubeLinkURL())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, myQxmFeedData.getFeedYoutubeLinkURL());
                context.startActivity(intent);
            }
        });

        ivYoutubeButton.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(myQxmFeedData.getFeedYoutubeLinkURL())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, myQxmFeedData.getFeedYoutubeLinkURL());
                context.startActivity(intent);
            }

        });

        tvViewAllEnrolled.setOnClickListener(v -> {

            // passing qxm id in bundle
            Bundle bundle = new Bundle();

            //readying bundle data
            ArrayList<String> bundleDataList = new ArrayList<>();
            bundleDataList.add(myQxmFeedData.getFeedQxmId());
            bundleDataList.add(myQxmFeedData.getFeedName());
            bundle.putStringArrayList(ENROLLED_LIST_FRAGMENT_BUNDLE_DATA, bundleDataList);

            // loading destination fragment
            qxmFragmentTransactionHelper.loadMyQxmSingleQxmEnrolledListFragment(bundle);

        });

        tvViewAllParticipants.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadQuestionOverviewParticipantListFragment(myQxmFeedData.getFeedQxmId(), QXM));

        btViewAllPendingEvaluation.setOnClickListener(v -> {

            //Toast.makeText(context, "Clicked btTotalReviewPending", Toast.LENGTH_SHORT).show();

            // passing qxm id in bundle
            Bundle bundle = new Bundle();

            //readying bundle data
            ArrayList<String> bundleDataList = new ArrayList<>();
            bundleDataList.add(myQxmFeedData.getFeedQxmId());
            bundleDataList.add(myQxmFeedData.getFeedName());
            bundle.putStringArrayList(PENDING_EVALUATION_LIST_FRAGMENT_BUNDLE_DATA, bundleDataList);

            // load destination fragment
            qxmFragmentTransactionHelper.loadMyQxmSingleQxmPendingEvaluationListFragment(bundle);

            //getPendingEvaluationListAndLoadView();
        });

        btnMyQxmManualStart.setOnClickListener(v -> myQxmStartExamNowNetworkCall());

        btnLeaderboard.setOnClickListener(v -> loadLeaderboardFragment());

        // pop this fragment when press back arrow
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

    }

    private void loadLeaderboardFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(QXM_ID_KEY, qxmId);
        //bundle.putParcelable(USER_BASIC_KEY, user);
        bundle.putString(QXM_CREATOR_ID_KEY, myQxmFeedData.getFeedCreatorId());
        bundle.putString(QXM_QUESTION_SET_TITLE, myQxmFeedData.getFeedName());
        bundle.putString(QXM_CREATOR_NAME_KEY, myQxmFeedData.getFeedCreatorName());
        bundle.putString(QXM_CREATION_TIME_KEY, myQxmFeedData.getFeedCreationTime());
        bundle.putString(QXM_PRIVACY_KEY, myQxmFeedData.getFeedPrivacy());
        qxmFragmentTransactionHelper.loadLeaderboardFragment(bundle);
    }

    private void showSingleQxmDetailsOptionMenu(View v, FragmentActivity fragmentActivity) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_single_my_qxm_details, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_add_to_list:
                    QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                    addQxmToList.addQxmToList(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName());

                    break;

                case R.id.action_edit:

                    editQxmNetworkCall(token.getToken(), token.getUserId(), myQxmFeedData.getFeedQxmId());

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(myQxmFeedData.getFeedQxmId()));

                    break;

                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
                    qxmShareContentToGroup.shareToGroup
                            (myQxmFeedData.getFeedQxmId(), SHARED_CATEGORY_QXM);

                    break;

                case R.id.action_delete:

                    QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

                    qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);
                    qxmAlertDialogBuilder.setTitle(context.getString(R.string.alert_dialog_title_delete_qxm));
                    qxmAlertDialogBuilder.setMessage(context.getString(R.string.alert_dialog_message_delete_qxm));

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton(R.string.delete_uppercase,
                            (dialog, which) ->
                                    deleteQxmNetworkCall(token.getToken(), token.getUserId(), myQxmFeedData.getFeedQxmId())
                    );

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton(R.string.no_uppercase, null);

                    qxmAlertDialogBuilder.getAlertDialogBuilder().show();

                    break;

            }


            return false;
        });
        popup.show();
    }

    private void editQxmNetworkCall(String token, String userId, String qxmId) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(getString(R.string.alert_dialog_title_edit_qxm),
                getString(R.string.alert_dialog_message_edit_qxm),
                false);

        Call<QxmModel> editQxm = apiService.editQxm(token, userId, qxmId);

        editQxm.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                progressDialog.closeProgressDialog();
                Log.d(TAG, "onResponse: response.code = " + response.code());

                if (response.code() == 200) {

                    if (response.body() != null) {

                        QxmDraft qxmDraft = makeQxmDraftFromQxmModel(response.body());
                        Intent intent = new Intent(getActivity(), CreateQxmActivity.class);
                        intent.putExtra(QXM_DRAFT_KEY, qxmDraft);
                        intent.putExtra(QXM_EDIT_KEY, true);
                        // Objects.requireNonNull(getActivity()).startActivity(intent);

                        // Todo:: startActivityForResult should be removed when fix Qxm Duplicates issue when edit qxm
                        Objects.requireNonNull(getActivity()).startActivityForResult(intent, EDIT_QXM_REQUEST);
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();

                } else {
                    Log.d(TAG, "onResponse: editQxmNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: editQxmNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

                progressDialog.closeProgressDialog();

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void deleteQxmNetworkCall(String token, String userId, String qxmId) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(getString(R.string.alert_dialog_title_delete_qxm),
                getString(R.string.progress_dialog_message_delete_qxm),
                false);

        Call<QxmApiResponse> deleteQxm = apiService.deleteQxm(token, userId, qxmId);

        deleteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                progressDialog.closeProgressDialog();
                Log.d(TAG, "onResponse: response.code = " + response.code());

                if (response.code() == 201) {
                    qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(0);
                    Toast.makeText(context, "Qxm Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: deleteQxmNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteQxmNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

                progressDialog.closeProgressDialog();

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //endregion

    // region MyQxmStartExamNowNetworkCall

    private void myQxmStartExamNowNetworkCall() {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Starting Qxm", String.format("The %s qxm is being started. Please wait... ", myQxmFeedData.getFeedName()), false);

        Log.d(TAG, "myQxmStartExamNowNetworkCall: qxmId: " + myQxmFeedData.getFeedQxmId());
        Call<QxmApiResponse> startQxmNow = apiService.startQxmNow(token.getToken(), myQxmFeedData.getFeedQxmId());

        startQxmNow.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: response.code =" + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());
                dialog.closeProgressDialog();

                if (response.code() == 201) {
                    tvMyQxmManualStartMessage.setVisibility(View.GONE);
                    btnMyQxmManualStart.setVisibility(View.GONE);
                    llTotalParticipants.setVisibility(View.VISIBLE);
                    llTotalPendingEvaluation.setVisibility(View.VISIBLE);
                    Toast.makeText(context, String.format("%s is now available for participation.", myQxmFeedData.getFeedName()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: stackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // endregion

    //region LoadQuestionSheetNetworkCall
    public void myQxmDetailsNetworkCall(String qxmId) {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        if (!TextUtils.isEmpty(qxmTitle))
            dialog.showProgressDialog("Loading details", String.format("%s is loading. Please wait... ", qxmTitle), false);
        else
            dialog.showProgressDialog("Loading details", String.format("%s is loading. Please wait... ", "Qxm details"), false);

        Call<MyQxmDetails> getMyQxmDetails = apiService.myQxmDetails(token.getToken(), qxmId);

        getMyQxmDetails.enqueue(new Callback<MyQxmDetails>() {
            @Override
            public void onResponse(@NonNull Call<MyQxmDetails> call, @NonNull Response<MyQxmDetails> response) {
                Log.d(TAG, "onResponse: getMyQxmDetails network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);

                noInternetView.setVisibility(View.GONE);


                if (response.code() == 200) {
                    myQxmDetails = response.body();
                    if (myQxmDetails != null) {

                        if (myQxmDetails.getMyqxmFeedData() != null && myQxmDetails.getQuestionList() != null) {


                            myQxmFeedData = myQxmDetails.getMyqxmFeedData();
                            questionList = myQxmDetails.getQuestionList();

                            Log.d(TAG, "onResponse: " + myQxmFeedData);
                            Log.d(TAG, "onResponse: " + questionList);

                            // making layout (root view) visible at first
                            nsvRootView.setVisibility(View.VISIBLE);

                            setValuesInViews();

                            examQuestionSheetAdapter = new MyQxmSingleQxmQuestionSheetAdapter(context, picasso, questionList);
                            rvQuestionSheet.setLayoutManager(new LinearLayoutManager(context));
                            rvQuestionSheet.setNestedScrollingEnabled(false);
                            rvQuestionSheet.setAdapter(examQuestionSheetAdapter);

                        } else {

                            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: " + myQxmDetails);
                        }

                    } else {

                        Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyQxmDetails> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }
    //endregion

    // region load previous data

    private void loadAdapterWithPreviousData() {
        examQuestionSheetAdapter = new MyQxmSingleQxmQuestionSheetAdapter(context, picasso, questionList);
        rvQuestionSheet.setLayoutManager(new LinearLayoutManager(context));
        rvQuestionSheet.setNestedScrollingEnabled(false);
        rvQuestionSheet.setAdapter(examQuestionSheetAdapter);
    }

    //endregion

    //region QxmDraft from QxmModel
    private QxmDraft makeQxmDraftFromQxmModel(QxmModel qxmModel) {

        QxmDraft qxmDraft = new QxmDraft();

        qxmDraft.setId(qxmModel.getId());
        qxmDraft.setQuestionSetTitle(qxmModel.getQuestionSet().getQuestionSetTitle());
        qxmDraft.setQuestionSetDescription(qxmModel.getQuestionSet().getQuestionSetDescription());
        qxmDraft.setQuestionSetThumbnail(qxmModel.getQuestionSet().getQuestionSetThumbnail());
        qxmDraft.setYoutubeLink(qxmModel.getQuestionSet().getQuestionSetYoutubeLink());
        qxmDraft.setQuestionStatus(qxmModel.getQuestionSettings().getQuestionStatus());
        qxmDraft.setQuestionCategory(qxmModel.getQuestionSettings().getQuestionCategory());

        Gson gson = new Gson();
        String questionSetJson = gson.toJson(qxmModel);
        qxmDraft.setQuestionSetJson(questionSetJson);

        qxmDraft.setLastEditedAt(qxmModel.getQuestionSettings().getLastEditedAt());
        qxmDraft.setCreatedAt(qxmModel.getQuestionSettings().getCreatedAt());

        return qxmDraft;
    }
    //endregion


    @Override
    public void onStart() {
        super.onStart();
        if (isMyQxmSingleQxmDetailsRefreshingNeeded) {
            isMyQxmSingleQxmDetailsRefreshingNeeded = false;
            nsvRootView.setVisibility(View.GONE);
            myQxmDetails = new MyQxmDetails();
            myQxmFeedData = new FeedData();
            myQxmDetailsNetworkCall(qxmId);

        }
    }





}
