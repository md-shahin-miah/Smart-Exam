package com.crux.qxm.views.fragments.questionOverview;


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
import android.widget.RelativeLayout;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.questionOverviewFragmentFeature.DaggerQuestionOverviewFragmentComponent;
import com.crux.qxm.di.questionOverviewFragmentFeature.QuestionOverviewFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAddQxmToList;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.utils.qxmDialogs.QxmReportDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
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

import static android.view.View.GONE;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_BY_DATE;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.PARTICIPATED_LIST_FRAGMENT_BUNDLE_DATA;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_AFTER_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_ENDED;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_NOT_STARTED;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_STOPPED;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_LEADERBOARD_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.REPORT_QXM;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.USER_BASIC_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_QXM;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionOverViewFragment extends Fragment {

    // region Fragment-Properties

    private static final String TAG = "QuestionOverViewFrag";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    // region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrowQuesOverView)
    AppCompatImageView ivBackArrowQuesOverView;
    @BindView(R.id.nsvRootView)
    NestedScrollView nsvRootView;
    @BindView(R.id.ivQxmAuthorImage)
    AppCompatImageView ivQxmAuthorImage;
    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;
    @BindView(R.id.tvQxmDescription)
    AppCompatTextView tvQxmDescription;
    @BindView(R.id.tvQxmAuthor)
    AppCompatTextView tvQxmAuthor;
    @BindView(R.id.tvQxmCreationTime)
    AppCompatTextView tvQxmCreationTime;
    @BindView(R.id.ivQxmPrivacy)
    AppCompatImageView ivQxmPrivacy;
    @BindView(R.id.ivQuestionOverviewOptions)
    AppCompatImageView ivQuestionOverviewOptions;

    // endregion
    @BindView(R.id.ivQxmThumbnail)
    AppCompatImageView ivQxmThumbnail;
    @BindView(R.id.btnSeeQuestion)
    AppCompatButton btnSeeQuestion;
    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;
    @BindView(R.id.btnParticipateNow)
    AppCompatButton btnParticipateNow;
    @BindView(R.id.tvMessageParticipated)
    AppCompatTextView tvMessageParticipated;
    @BindView(R.id.llLastSubmissionDateContainer)
    LinearLayoutCompat llLastSubmissionDateContainer;
    @BindView(R.id.tvLastSubmissionDate)
    AppCompatTextView tvLastSubmissionDate;
    @BindView(R.id.tvResultPublishDate)
    AppCompatTextView tvResultPublishDate;
    @BindView(R.id.tvQxmExpirationDate)
    AppCompatTextView tvQxmExpirationDate;
    @BindView(R.id.tvTitleExamRule)
    AppCompatTextView tvTitleExamRule;
    @BindView(R.id.llExamRuleOne)
    LinearLayoutCompat llExamRuleOne;
    @BindView(R.id.llExamRuleTwo)
    LinearLayoutCompat llExamRuleTwo;
    @BindView(R.id.tvQuestionCategory)
    AppCompatTextView tvQuestionCategory;
    @BindView(R.id.llEnrollCountContainer)
    LinearLayoutCompat llEnrollCountContainer;
    @BindView(R.id.tvEnrollCount)
    AppCompatTextView tvEnrollCount;
    @BindView(R.id.tvParticipantCount)
    AppCompatTextView tvParticipantCount;
    @BindView(R.id.tvViewAllEnrolled)
    AppCompatTextView tvViewAllEnrolled;
    @BindView(R.id.tvViewAllParticipants)
    AppCompatTextView tvViewAllParticipants;
    @BindView(R.id.noInternetView)
    View noInternetView;
    @BindView(R.id.tvRuleForRaffleDraw)
    AppCompatTextView tvRuleForRaffleDraw;
    @BindView(R.id.tvRuleForNegativeMarks)
    AppCompatTextView tvRuleForNegativeMarks;
    @BindView(R.id.tvExamRuleNumberingNegativeMarks)
    AppCompatTextView tvExamRuleNumberingNegativeMarks;
    @BindView(R.id.tvExamRuleNumberingRaffleDraw)
    AppCompatTextView tvExamRuleNumberingRaffleDraw;

    @BindView(R.id.rlSeeLeaderboard)
    RelativeLayout rlSeeLeaderboard;
    @BindView(R.id.ivSeeLeaderboard)
    AppCompatImageView ivSeeLeaderboard;
    @BindView(R.id.tvSeeLeaderboard)
    AppCompatTextView tvSeeLeaderboard;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;


    private Context context;
    private FeedData feedData;
    private String qxmId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmModel qxmModel; // --> qxmModel will be used for get ExamQuestionSheet
    private String viewFor;
    private QxmToken token;
    private UserBasic user;


    // endregion

    // region Fragment-Constructor

    public QuestionOverViewFragment() {
        // Required empty public constructor
    }

    // endregion

    // region Override-OnCreate

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        qxmId = getArguments().getString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW);
        Log.d(TAG, "onCreate- QxmId: " + qxmId);
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_overview, container, false);
        context = view.getContext();

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(context);
        if (qxmId != null) {
            init();
            noInternetFunctionality();
            if (feedData == null || !TextUtils.isEmpty(viewFor)) {
                viewFor = "";
                questionOverViewNetworkCall();
            } else {
                nsvRootView.setVisibility(View.VISIBLE);
                setViewValues();
                clickListeners();
            }
        }
        swipeRL.setOnRefreshListener(() -> {
            swipeRL.setRefreshing(true);
            questionOverViewNetworkCall();
        });

    }

    // endregion

    // region ClickListeners

    private void clickListeners() {

        ivBackArrowQuesOverView.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getActivity() != null) {
                Log.d(TAG, "onBackPressed calling from question overview fragment");
                getActivity().onBackPressed();
            }
        });

        ivQuestionOverviewOptions.setOnClickListener(v ->
                showQuestionOverViewOptionMenu(v, getActivity())
        );

        if (feedData.getQuestionVisibility().equals(QUESTION_VISIBILITY_AFTER_PARTICIPATION)
                && !feedData.isParticipated()) {
            btnSeeQuestion.setVisibility(GONE);

        } else {
            btnSeeQuestion.setVisibility(View.VISIBLE);
            btnSeeQuestion.setOnClickListener(v ->
                    getQuestionSheetAndLoadViewNetworkCall(feedData.getFeedQxmId(), VIEW_FOR_SEE_QUESTION));

        }

        ivQxmThumbnail.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(feedData.getFeedYoutubeLinkURL())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, feedData.getFeedYoutubeLinkURL());
                context.startActivity(intent);
            }
        });

        ivYoutubeButton.setOnClickListener(v -> {

            Intent intent = new Intent(context, YouTubePlayBackActivity.class);
            intent.putExtra(YOUTUBE_LINK_KEY, feedData.getFeedYoutubeLinkURL());
            context.startActivity(intent);
        });


        btnParticipateNow.setOnClickListener(v -> {

            Log.d(TAG, "Participate/Result button clicked: " + feedData.isParticipated());
            if (feedData.getFeedCreatorId().equals(token.getUserId())) {
                if (feedData.isEnrollEnabled()) {

                    tvMessageParticipated.setText(getString(R.string.you_can_not_enroll_in_your_own_qxm));
                    tvMessageParticipated.setTextColor(getResources().getColor(R.color.warning));
                    Toast.makeText(context, getString(R.string.you_can_not_enroll_in_your_own_qxm), Toast.LENGTH_SHORT).show();
                } else {
                    tvMessageParticipated.setText(getString(R.string.you_can_not_participate_in_your_own_qxm));
                    tvMessageParticipated.setTextColor(getResources().getColor(R.color.warning));
                    Toast.makeText(context, getString(R.string.you_can_not_participate_in_your_own_qxm), Toast.LENGTH_SHORT).show();
                }
            } else if (feedData.isParticipated()) {
                // See result
                qxmFragmentTransactionHelper.loadFullResultFragment(qxmId);

            } else if (feedData.isEnrollPending()) {

                Toast.makeText(context, R.string.your_enroll_request_is_pending, Toast.LENGTH_SHORT).show();

            } else if (
                    feedData.isEnrollEnabled()
                            && feedData.isEnrollAccepted()
                            && !feedData.isEnrollPending()
                            && !feedData.getQxmCurrentStatus().equals(QXM_CURRENT_STATUS_NOT_STARTED)) {
                // enroll enabled+requestAccepted
                getQuestionSheetAndLoadViewNetworkCall(feedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_QXM);

            } else if (feedData.isEnrollEnabled()
                    && !feedData.isEnrollPending()
                    && !feedData.isEnrollAccepted()) {
                // pending false, accept false = no enroll request sent, So
                // SEND ENROLL REQUEST.
                sendEnrollRequestNetworkCall();
            } else { // participate
                // Qxm Manual Start check
                switch (feedData.getQxmCurrentStatus()) {
                    case QXM_CURRENT_STATUS_NOT_STARTED:
                        Toast.makeText(context, R.string.toast_message_qxm_status_not_started, Toast.LENGTH_SHORT).show();
                        break;
                    case QXM_CURRENT_STATUS_ENDED:
                        Toast.makeText(context, R.string.toast_message_qxm_status_expired, Toast.LENGTH_SHORT).show();
                        break;
                    case QXM_CURRENT_STATUS_STOPPED:
                        Toast.makeText(context, R.string.toast_message_qxm_status_stopped, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        viewFor = VIEW_FOR_PARTICIPATE_QXM;
                        getQuestionSheetAndLoadViewNetworkCall(feedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_QXM);
                        break;
                }
            }
        });

    }

    private void showQuestionOverViewOptionMenu(View v, FragmentActivity fragmentActivity) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_question_overview, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
                    qxmShareContentToGroup.shareToGroup
                            (feedData.getFeedQxmId(), SHARED_CATEGORY_QXM);

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(feedData.getFeedQxmId()));

                    break;

                case R.id.action_add_to_list:
                    QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                    addQxmToList.addQxmToList(feedData.getFeedQxmId(), feedData.getFeedName());

                    break;

                case R.id.action_report_qxm:

                    QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token.getToken(), token.getUserId(), feedData.getFeedQxmId());
                    qxmReportDialog.showReportDialog(REPORT_QXM);
                    break;
            }


            return false;
        });
        popup.show();
    }

    // endregion

    // region SendEnrollRequestNetworkCall

    private void sendEnrollRequestNetworkCall() {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Enroll Request", String.format("Sending enroll request of %s. Please wait...", feedData.getFeedName()), false);

        Log.d(TAG, "sendEnrollRequestNetworkCall: UserId = " + token.getUserId());
        Call<QxmApiResponse> sendEnrollRequest = apiService.sendEnrollRequest(token.getToken(), token.getUserId(), feedData.getFeedQxmId());

        sendEnrollRequest.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: sendEnrollRequest Network Call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                //hiding noInternetView
                noInternetView.setVisibility(GONE);

                if (response.code() == 201) {
                    feedData.setEnrollPending(true);
                    btnParticipateNow.setText(R.string.enroll_request_pending);
                    Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
                    dialog.closeProgressDialog();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, "Request not sent. please try again.", Toast.LENGTH_SHORT).show();
                    dialog.closeProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        QuestionOverviewFragmentComponent questionOverviewFragmentComponent =
                DaggerQuestionOverviewFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        questionOverviewFragmentComponent.injectQuestionOverviewFragmentFeature(QuestionOverViewFragment.this);
    }

    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            viewFor = "";
            questionOverViewNetworkCall();
        });
    }

    //endregion

    // region Init

    private void init() {
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();
        user = realmService.getSavedUser();
    }

    // endregion

    // region SetViewValues

    private void setViewValues() {

        Log.d(TAG, "setValuesInViews: feedData: " + feedData);

        if (isLeaderboardCanBeShown() && QxmStringIntegerChecker.isInt(feedData.getFeedParticipantsQuantity())
                && Integer.parseInt(feedData.getFeedParticipantsQuantity()) > 0) {

            rlSeeLeaderboard.setVisibility(View.VISIBLE);
            tvSeeLeaderboard.setOnClickListener(v -> {
                // GOTO Leaderboard Fragment
                if (qxmId != null) {
                    loadLeaderboardFragment(qxmId);
                }

            });
            ivSeeLeaderboard.setOnClickListener(v -> {
                // GOTO Leaderboard Fragment
                //is me participated check kora lagbe....and user object oi onujaye pathate hobe
                if (qxmId != null) {
                    loadLeaderboardFragment(qxmId);

                }
            });

        } else {
            rlSeeLeaderboard.setVisibility(GONE);
        }


        //region Set Enroll Status in Button
        if (feedData.isEnrollEnabled() && !feedData.getFeedCreatorId().equals(token.getUserId())) {

            if (!feedData.isEnrollPending() && !feedData.isEnrollAccepted()) {
                // no enroll request sent. So
                // Show ENROLL NOW
                btnParticipateNow.setText(getString(R.string.enroll_now));

            } else if (feedData.isEnrollPending() && !feedData.isEnrollAccepted()) {
                // enroll request sent. So,
                // Show ENROLL REQUEST PENDING
                btnParticipateNow.setText(R.string.enroll_request_pending);
            } else if (feedData.isEnrollAccepted()) {
                // show participate button
                btnParticipateNow.setText(getString(R.string.participate_now));
            } else {
                // show Enroll Button
                btnParticipateNow.setText(getString(R.string.enroll_now));

            }
        } else if (feedData.isEnrollEnabled() && feedData.getFeedCreatorId().equals(token.getUserId())) {
            // show Enroll Button
            btnParticipateNow.setText(getString(R.string.enroll_now));

            tvMessageParticipated.setText(getString(R.string.this_is_your_own_qxm));
            tvMessageParticipated.setVisibility(View.VISIBLE);

        } else if (feedData.getFeedCreatorId().equals(token.getUserId())) {
            tvMessageParticipated.setText(getString(R.string.this_is_your_own_qxm));
            tvMessageParticipated.setVisibility(View.VISIBLE);
        }
        //endregion

        //region Have I participated
        if (feedData.isParticipated()) {
            btnParticipateNow.setText(getString(R.string.see_result_caps_word));
            if (feedData.getCorrectAnswerVisibilityDate().equals(CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES)) {
                long examExpireMillis = Long.parseLong(feedData.getQxmExpirationDate());
                if (System.currentTimeMillis() >= examExpireMillis) {
                    btnParticipateNow.setEnabled(true);
                    btnParticipateNow.setAlpha(1f);
                } else {
                    btnParticipateNow.setEnabled(false);
                    btnParticipateNow.setAlpha(0.5f);
                }
            }
            tvMessageParticipated.setVisibility(View.VISIBLE);
        } else if (feedData.getQxmCurrentStatus().equals(QXM_CURRENT_STATUS_NOT_STARTED)) {

            tvMessageParticipated.setText(getString(R.string.qxm_start_manual_message));
            tvMessageParticipated.setTextColor(context.getResources().getColor(R.color.warning));
            tvMessageParticipated.setVisibility(View.VISIBLE);
        } else if (QxmStringIntegerChecker.isLongInt(feedData.getQxmStartScheduleDate())) {

            long examStartMillis = Long.parseLong(feedData.getQxmStartScheduleDate());
            if (examStartMillis >= System.currentTimeMillis()) {
                String qxmStartTime = String.format("Qxm will be started on %s", TimeFormatString.getTimeAndDate(
                        Long.parseLong(feedData.getQxmStartScheduleDate())));
                tvMessageParticipated.setText(qxmStartTime);
                tvMessageParticipated.setTextColor(context.getResources().getColor(R.color.warning));
                tvMessageParticipated.setVisibility(View.VISIBLE);
                btnParticipateNow.setEnabled(false);
                btnParticipateNow.setAlpha(0.4f);
            } else if (examStartMillis < System.currentTimeMillis()) {
                tvMessageParticipated.setVisibility(GONE);
                btnParticipateNow.setEnabled(true);
                btnParticipateNow.setAlpha(1f);
            }

        } else {
            tvMessageParticipated.setVisibility(GONE);
        }
        //endregion

        if (feedData.getFeedCreatorImageURL() != null && !feedData.getFeedCreatorImageURL().isEmpty()) {
            Log.d(TAG, "setViewValues: author image" + feedData.getFeedCreatorImageURL());
            picasso.load(feedData.getFeedCreatorImageURL())
                    .placeholder(R.drawable.ic_user_default)
                    .error(R.drawable.ic_user_default)
                    .into(ivQxmAuthorImage);
        }
        tvQxmTitle.setText(feedData.getFeedName());
        tvQxmAuthor.setText(feedData.getFeedCreatorName());
        tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(Long.parseLong(feedData.getFeedCreationTime())));

        if (feedData.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
            ivQxmPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
        else
            ivQxmPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));


        if (!TextUtils.isEmpty(feedData.getFeedThumbnailURL()))
            picasso.load(feedData.getFeedThumbnailURL()).into(ivQxmThumbnail);
        else ivQxmThumbnail.setVisibility(GONE);


        if (!TextUtils.isEmpty(feedData.getFeedYoutubeLinkURL()))
            ivYoutubeButton.setVisibility(View.VISIBLE);
        else
            ivYoutubeButton.setVisibility(GONE);

        if (!TextUtils.isEmpty(feedData.getFeedDescription())) {
            tvQxmDescription.setText(feedData.getFeedDescription());
            SetlinkClickable.setLinkclickEvent(tvQxmDescription, new HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, url);
                    context.startActivity(intent);
                }
            });

        } else {
            tvQxmDescription.setVisibility(GONE);
        }
        if (feedData.getNegativePoints() != null) {

            if (!feedData.getNegativePoints().isEmpty()) {

                if (Double.parseDouble(feedData.getNegativePoints()) > 0) {

                    tvTitleExamRule.setVisibility(View.VISIBLE);
                    llExamRuleTwo.setVisibility(View.VISIBLE);
                    tvExamRuleNumberingNegativeMarks.setText("1");
                    String negativeMarksMsg = "In multiple choice question for each wrong answer there will be " + feedData.getNegativePoints() + " marks deducted.";
                    tvRuleForNegativeMarks.setText(negativeMarksMsg);
                }
            }
        }

//        if (!feedData.isContestModeOn()) {
//            tvTitleExamRule.setVisibility(View.GONE);
//            llExamRuleOne.setVisibility(View.GONE);
//            llExamRuleTwo.setVisibility(View.GONE);
//        } else {
//            tvTitleExamRule.setVisibility(View.VISIBLE);
//            llExamRuleOne.setVisibility(View.VISIBLE);
//            llExamRuleTwo.setVisibility(View.VISIBLE);
//
//        }


        if (feedData.isContestModeOn()) {

            llLastSubmissionDateContainer.setVisibility(View.VISIBLE);

            if (QxmStringIntegerChecker.isLongInt(feedData.getQxmExpirationDate())) {
                tvLastSubmissionDate.setText(TimeFormatString.getDate(Long.parseLong(feedData.getQxmExpirationDate())));
            } else {
                tvLastSubmissionDate.setText(getString(R.string.not_defined));
            }
        } else {
            llLastSubmissionDateContainer.setVisibility(GONE);
        }

        if (QxmStringIntegerChecker.isNumber(feedData.getCorrectAnswerVisibilityDate())) {

            if (isEvaluationAutomaic(feedData.getEvaluationType()))
                tvResultPublishDate.setText(
                        TimeFormatString.getTimeAndDate(Long.parseLong(feedData.getCorrectAnswerVisibilityDate())));
            else tvResultPublishDate.setText(
                    TimeFormatString.getTimeAndDate(Long.parseLong(feedData.getCorrectAnswerVisibilityDate())) +
                            " " + getString(R.string.after_evaluation));

        } else {
            if (!TextUtils.isEmpty(feedData.getCorrectAnswerVisibilityDate())) {

                switch (feedData.getCorrectAnswerVisibilityDate()) {

                    case CORRECT_ANSWER_VISIBILITY_BY_DATE:

                        Log.d(TAG, "setViewValues correct answer visibility: " + feedData.getCorrectAnswerVisibilityDate());
                        tvResultPublishDate.setText(
                                TimeFormatString.getTimeAndDate(Long.parseLong(feedData.getCorrectAnswerVisibilityDate())));
                        break;
                    case CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES:
                        if (isEvaluationAutomaic(feedData.getEvaluationType()))
                            tvResultPublishDate.setText(R.string.after_qxm_expires);
                        else
                            tvResultPublishDate.setText(R.string.after_qxm_expires_and_evaluation_done);
                        break;
                    case CORRECT_ANSWER_VISIBILITY_IMMEDIATELY:
                        if (isEvaluationAutomaic(feedData.getEvaluationType()))
                            tvResultPublishDate.setText(R.string.immediately_after_participation);
                        else
                            tvResultPublishDate.setText(R.string.immediately_after_participation_and_evaluation_done);
                        break;
                    case CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS:
                        if (isEvaluationAutomaic(feedData.getEvaluationType()))
                            tvResultPublishDate.setText(R.string.after_contest_ends);
                        else
                            tvResultPublishDate.setText(R.string.after_contest_ends_and_evaluation_done);
                        break;
                }
            } else {
                tvResultPublishDate.setText(getString(R.string.not_defined));
            }
        }
        if (!TextUtils.isEmpty(feedData.getQxmExpirationDate())) {
            String regex = "[0-9]+";
            if (feedData.getQxmExpirationDate().matches(regex)) {
                long expireDate = Long.parseLong(feedData.getQxmExpirationDate());
                tvQxmExpirationDate.setText(TimeFormatString.getTimeAndDate(expireDate));
            } else if (feedData.getQxmExpirationDate().equals("exam_duration")) {
                tvQxmExpirationDate.setText(getString(R.string.question_expired_after_participation));
            }
        } else {
            tvQxmExpirationDate.setText(getString(R.string.not_defined));
        }

        //setting category
        tvQuestionCategory.setText(QxmArrayListToStringProcessor.getStringFromArrayList(
                feedData.getFeedCategory()
        ));

        if (feedData.isEnrollEnabled())
            tvEnrollCount.setText(feedData.getFeedEnrollAcceptedQuantity());
        else
            llEnrollCountContainer.setVisibility(GONE);

        tvParticipantCount.setText(feedData.getFeedParticipantsQuantity());

        // hiding view all text view based on enroll and participant count
        if (feedData.getFeedEnrollAcceptedQuantity().equals("0"))
            tvViewAllEnrolled.setVisibility(GONE);
        else tvViewAllEnrolled.setVisibility(View.VISIBLE);

        if (feedData.getFeedParticipantsQuantity().equals("0"))
            tvViewAllParticipants.setVisibility(GONE);
        else tvViewAllParticipants.setVisibility(View.VISIBLE);

        // view all enrolled and participants
        tvViewAllEnrolled.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadQuestionOverviewEnrolledListFragment(qxmId));


        tvViewAllParticipants.setOnClickListener(view -> {

            // passing qxm id in bundle
            Bundle bundle = new Bundle();

            //readying bundle data
            ArrayList<String> bundleDataList = new ArrayList<>();
            bundleDataList.add(feedData.getFeedQxmId());
            bundleDataList.add(feedData.getFeedName());
            bundle.putStringArrayList(PARTICIPATED_LIST_FRAGMENT_BUNDLE_DATA, bundleDataList);

            // load destination fragment
            qxmFragmentTransactionHelper.loadMyQxmSingleQxmParticipatorListFragment(bundle);
        });

        // Check if the Qxm Expired or not
        // TODO:: optimize code...based on Qxm Expiration
        if (isQxmExpired()) {
            // The Qxm has expired!
            if (!feedData.isParticipated()) {

                btnParticipateNow.setEnabled(false);
                btnParticipateNow.setAlpha(0.4f);
                tvMessageParticipated.setVisibility(View.VISIBLE);
                tvMessageParticipated.setText(context.getString(R.string.the_qxm_has_been_expired));
                tvMessageParticipated.setTextColor(context.getResources().getColor(R.color.wrong_answer));
            }
        }

    }

    private boolean isQxmExpired() {

        if (QxmStringIntegerChecker.isLongInt(feedData.getQxmExpirationDate())) {
            long expirationDate = Long.parseLong(feedData.getQxmExpirationDate());
            long currentDate = Calendar.getInstance().getTimeInMillis();
            return currentDate > expirationDate;
        }

        return false;
    }

    private void loadLeaderboardFragment(String qxmId) {
        Bundle bundle = new Bundle();
        bundle.putString(QXM_ID_KEY, qxmId);
        if (feedData.isParticipated())
            bundle.putParcelable(USER_BASIC_KEY, user);
        bundle.putString(QXM_QUESTION_SET_TITLE, feedData.getFeedName());
        bundle.putString(QXM_CREATOR_NAME_KEY, feedData.getFeedCreatorName());
        bundle.putString(QXM_CREATION_TIME_KEY, feedData.getFeedCreationTime());
        bundle.putString(QXM_PRIVACY_KEY, feedData.getFeedPrivacy());
        bundle.putString(QXM_CREATOR_ID_KEY, feedData.getFeedCreatorId());
        bundle.putString(QXM_LEADERBOARD_PRIVACY_KEY, feedData.getLeaderboardPrivacy());
        qxmFragmentTransactionHelper.loadLeaderboardFragment(bundle);
    }

    private boolean isLeaderboardCanBeShown() {
        // If user already participated in the qxm then he/she can see the leaderboard from Full Result Fragment
        if (feedData.isParticipated())
            return false;
        else if (feedData.getLeaderboardPrivacy() != null && feedData.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PUBLIC)) {
            // Leaderboard Privacy=PUBLIC, PublishType=Continue **means show leaderboard always

            if (feedData.getLeaderboardPublishDate() != null) {

                if (feedData.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTINUE))
                    return true;
                else if (feedData.isContestModeOn() && feedData.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTEST_END)) {
                    // If contest mode enabled
                    if (QxmStringIntegerChecker.isLongInt(feedData.getQxmExpirationDate())) {
                        long qxmExpirationDate = Long.parseLong(feedData.getQxmExpirationDate());
                        long currentDate = Calendar.getInstance().getTimeInMillis();
                        return currentDate > qxmExpirationDate;
                    }
                } else if (QxmStringIntegerChecker.isLongInt(feedData.getLeaderboardPublishDate())) {
                    long leaderboardPublishDate = Long.parseLong(feedData.getLeaderboardPublishDate());
                    long currentDate = Calendar.getInstance().getTimeInMillis();
                    return currentDate > leaderboardPublishDate;
                }
            }

        }

        return false;
    }

    // endregion

    // region GetQuestionSheetAndLoadView

    private void getQuestionSheetAndLoadViewNetworkCall(String qxmId, String questionSheetFor) {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        if (questionSheetFor.equals(VIEW_FOR_SEE_QUESTION))
            dialog.showProgressDialog("Loading Question Set", String.format("%s is loading. Please wait... ", feedData.getFeedName()), false);
        else
            dialog.showProgressDialog("Prepare Exam Sheet", String.format("%s is loading. Please wait... ", feedData.getFeedName()), false);

        Call<QxmModel> getExamQuestionSheet = apiService.getExamQuestionSheet(token.getToken(), qxmId);

        getExamQuestionSheet.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                Log.d(TAG, "onResponse: getQuestionSheetAndLoadViewNetworkCall network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                noInternetView.setVisibility(GONE);

                dialog.closeProgressDialog();

                if (response.code() == 200) {
                    qxmModel = response.body();
                    if (qxmModel != null) {
                        Log.d(TAG, "onResponse: qxmModel: " + qxmModel.toString());
                        qxmFragmentTransactionHelper.loadParticipateQxmFragment(qxmModel, questionSheetFor);
                    } else
                        Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else if (response.code() == 409) {
                    Toast.makeText(context, "You have already participated.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);

            }
        });
    }

    // endregion


    // region QuestionOverViewNetworkCall

    private void questionOverViewNetworkCall() {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(getString(R.string.progress_dialog_title_question_overview_loading), getString(R.string.progress_dialog_message_question_overview_loading), false);

        Call<FeedData> questionOverviewCall = apiService.questionOverview(token.getToken(), qxmId, token.getUserId());

        questionOverviewCall.enqueue(new Callback<FeedData>() {
            @Override
            public void onResponse(@NonNull Call<FeedData> call, @NonNull Response<FeedData> response) {

                Log.d(TAG, "onResponse: questionOverview network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);

                //hiding noInternetView
                noInternetView.setVisibility(GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {

                        feedData = response.body();


                        //make visible main layout(root view)
                        nsvRootView.setVisibility(View.VISIBLE);

                        setViewValues();
                        clickListeners();

                    } else {

                        //hiding noInternetView
                        Log.d(TAG, "onResponse: questionOverview network call " + " Response body is null");
                    }
                } else {

                    //hiding noInternetView
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<FeedData> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);

                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });

    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(GONE);
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

    private boolean isEvaluationAutomaic(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s.contains("automatic");
        }
        return false;
    }




}
