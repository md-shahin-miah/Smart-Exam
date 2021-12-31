package com.crux.qxm.views.fragments.singleMCQ;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.adapter.singleMCQAdapter.SingleMCQAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.singleMCQOverviewFragmentFeature.DaggerSingleMCQOverviewFragmentComponent;
import com.crux.qxm.di.singleMCQOverviewFragmentFeature.SingleMCQOverviewFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmActionShareLink;
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

import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_BY_DATE;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.PARTICIPATED_LIST_FRAGMENT_BUNDLE_DATA;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.REPORT_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_CREATOR_ID;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_ID_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_LEADER_BOARD_PRIVACY;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_TIME_TYPE_NO_LIMIT;
import static com.crux.qxm.utils.StaticValues.USER_BASIC_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMCQOverviewFragment extends Fragment {

    private static final String TAG = "SingleMCQOverviewFragme";

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private Context context;
    private FeedData feedData;
    private String singleMCQId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    private UserBasic user;

    // region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.nsvRootView)
    NestedScrollView nsvRootView;
    @BindView(R.id.ivMCQAuthorImage)
    AppCompatImageView ivMCQAuthorImage;
    @BindView(R.id.tvMCQTitle)
    AppCompatTextView tvMCQTitle;
    @BindView(R.id.tvMCQDescription)
    AppCompatTextView tvMCQDescription;
    @BindView(R.id.tvMCQAuthor)
    AppCompatTextView tvMCQAuthor;
    @BindView(R.id.tvMCQCreationTime)
    AppCompatTextView tvMCQCreationTime;

    @BindView(R.id.ivMCQOverviewOptionsMenu)
    AppCompatImageView ivMCQOverviewOptionsMenu;


    @BindView(R.id.ivMCQThumbnail)
    AppCompatImageView ivMCQThumbnail;
    @BindView(R.id.rvSingleMCQOptions)
    RecyclerView rvSingleMCQOptions;

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
    @BindView(R.id.tvMCQExpirationDate)
    AppCompatTextView tvMCQExpirationDate;
    @BindView(R.id.tvTitleExamRule)
    AppCompatTextView tvTitleExamRule;
    @BindView(R.id.llExamRuleOne)
    LinearLayoutCompat llExamRuleOne;
    @BindView(R.id.llExamRuleTwo)
    LinearLayoutCompat llExamRuleTwo;
    @BindView(R.id.tvQuestionCategory)
    AppCompatTextView tvQuestionCategory;
    @BindView(R.id.tvParticipantCount)
    AppCompatTextView tvParticipantCount;
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

    // endregion

    public SingleMCQOverviewFragment() {
        // Required empty public constructor
    }

    public static SingleMCQOverviewFragment newInstance(String singleMCQId) {

        Bundle args = new Bundle();
        args.putString(SINGLE_MCQ_ID_KEY, singleMCQId);

        SingleMCQOverviewFragment fragment = new SingleMCQOverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_mcqoverview, container, false);

        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        singleMCQId = getArguments() != null ? getArguments().getString(SINGLE_MCQ_ID_KEY) : null;

        setUpDagger2(context);

        if (singleMCQId != null) {

            init();
            noInternetFunctionality();

            if (feedData == null) {
                singleMCQOverViewNetworkCall();
            } else {
                nsvRootView.setVisibility(View.VISIBLE);
                setValuesInViews();
                clickListeners();
            }
        }

        swipeRL.setOnRefreshListener(this::singleMCQOverViewNetworkCall);
    }

    private void setUpDagger2(Context context) {
        SingleMCQOverviewFragmentComponent singleMCQOverviewFragmentComponent =
                DaggerSingleMCQOverviewFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent()).build();

        singleMCQOverviewFragmentComponent.injectSingleMCQOverviewFragmentFeature(SingleMCQOverviewFragment.this);
    }

    private void init() {

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        token = realmService.getApiToken();
        user = realmService.getSavedUser();
        realmService.close();


    }

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> singleMCQOverViewNetworkCall());
    }

    private void singleMCQOverViewNetworkCall() {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(getString(R.string.progress_dialog_title_question_overview_loading), getString(R.string.progress_dialog_message_question_overview_loading), false);

        Call<FeedData> questionOverviewCall = apiService.getSingleMCQOverview(token.getToken(), singleMCQId, token.getUserId());

        questionOverviewCall.enqueue(new Callback<FeedData>() {
            @Override
            public void onResponse(@NonNull Call<FeedData> call, @NonNull Response<FeedData> response) {

                Log.d(TAG, "onResponse: questionOverview network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();
                swipeRL.setRefreshing(false);

                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {

                        feedData = response.body();


                        //make visible main layout(root view)
                        nsvRootView.setVisibility(View.VISIBLE);

                        setValuesInViews();
                        clickListeners();

                    } else {

                        //hiding noInternetView
                        Log.d(TAG, "onResponse: questionOverview network call " + " Response body is null");
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();

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

    //endregion

    // region SetViewValues
    private void setValuesInViews() {

        Log.d(TAG, "setValuesInViews: feedData: " + feedData);

        MultipleChoice multipleChoice = new MultipleChoice();
        multipleChoice.setOptions(feedData.getSingleMCQoptions());
        SingleMCQAdapter singleMCQAdapter = new SingleMCQAdapter(context, multipleChoice);

        rvSingleMCQOptions.setAdapter(singleMCQAdapter);
        rvSingleMCQOptions.setNestedScrollingEnabled(false);
        rvSingleMCQOptions.setLayoutManager(new LinearLayoutManager(context));

        if (isLeaderboardCanBeShown() && QxmStringIntegerChecker.isInt(feedData.getFeedParticipantsQuantity())
                && Integer.parseInt(feedData.getFeedParticipantsQuantity()) > 0) {

            rlSeeLeaderboard.setVisibility(View.VISIBLE);
            tvSeeLeaderboard.setOnClickListener(v -> {
                // GOTO Leaderboard Fragment
                if (singleMCQId != null) {
                    loadSingleMCQLeaderboardFragment(singleMCQId);
                }

            });
            ivSeeLeaderboard.setOnClickListener(v -> {
                // GOTO Leaderboard Fragment
                //is me participated check kora lagbe....and user object oi onujaye pathate hobe
                if (singleMCQId != null) {
                    loadSingleMCQLeaderboardFragment(singleMCQId);

                }
            });

        } else {
            rlSeeLeaderboard.setVisibility(View.GONE);
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
            tvMessageParticipated.setVisibility(View.VISIBLE);
        } else {
            tvMessageParticipated.setVisibility(View.GONE);
        }
        //endregion

        if (feedData.getFeedCreatorImageURL() != null && !feedData.getFeedCreatorImageURL().isEmpty())
            picasso.load(feedData.getFeedCreatorImageURL()).into(ivMCQAuthorImage);

        tvMCQTitle.setText(feedData.getFeedName());
        tvMCQAuthor.setText(feedData.getFeedCreatorName());
        tvMCQCreationTime.setText(TimeFormatString.getTimeAgo(Long.parseLong(feedData.getFeedCreationTime())));


        if (!TextUtils.isEmpty(feedData.getFeedThumbnailURL()))
            picasso.load(feedData.getFeedThumbnailURL()).into(ivMCQThumbnail);
        else ivMCQThumbnail.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(feedData.getFeedYoutubeLinkURL()))
            ivYoutubeButton.setVisibility(View.VISIBLE);
        else
            ivYoutubeButton.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(feedData.getFeedDescription())){
            tvMCQDescription.setText(feedData.getFeedDescription());
            SetlinkClickable.setLinkclickEvent(tvMCQDescription, new HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, url);
                    context.startActivity(intent);
                }
            });

        }

        else
            tvMCQDescription.setVisibility(View.GONE);

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
            llLastSubmissionDateContainer.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(feedData.getCorrectAnswerVisibilityDate())) {
            switch (feedData.getCorrectAnswerVisibilityDate()) {
                case CORRECT_ANSWER_VISIBILITY_BY_DATE:
                    tvResultPublishDate.setText(
                            TimeFormatString.getTimeAndDate(Long.parseLong(feedData.getCorrectAnswerVisibilityDate())));

                    break;
                case CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES:
                    tvResultPublishDate.setText(R.string.after_qxm_expires);
                    break;
                case CORRECT_ANSWER_VISIBILITY_IMMEDIATELY:
                    tvResultPublishDate.setText(R.string.immediately_after_participation);
                    break;
                case CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS:
                    tvResultPublishDate.setText(R.string.after_contest_ends);
                    break;
            }
        } else {
            tvResultPublishDate.setText(getString(R.string.not_defined));
        }

        if (!TextUtils.isEmpty(feedData.getQxmExpirationDate())) {
            String regex = "[0-9]+";
            if (feedData.getQxmExpirationDate().equals(SINGLE_MCQ_TIME_TYPE_NO_LIMIT)) {
                tvMCQExpirationDate.setText(context.getString(R.string.unlimited));
            } else if (feedData.getQxmExpirationDate().matches(regex)) {
                long expireDate = Long.parseLong(feedData.getQxmExpirationDate());
                tvMCQExpirationDate.setText(TimeFormatString.getTimeAndDate(expireDate));
            } else {
                tvMCQExpirationDate.setText(feedData.getQxmExpirationDate());
            }
        } else {
            tvMCQExpirationDate.setText(getString(R.string.not_defined));
        }

        //setting category
        tvQuestionCategory.setText(QxmArrayListToStringProcessor.getStringFromArrayList(
                feedData.getFeedCategory()
        ));


        tvParticipantCount.setText(feedData.getFeedParticipantsQuantity());


        if (feedData.getFeedParticipantsQuantity().equals("0"))
            tvViewAllParticipants.setVisibility(View.GONE);
        else tvViewAllParticipants.setVisibility(View.VISIBLE);


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
        if (isSingleMCQExpired()) {
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
    // endregion

    //region isSingleMCQExpired
    private boolean isSingleMCQExpired() {

        if (QxmStringIntegerChecker.isLongInt(feedData.getQxmExpirationDate())) {
            long expirationDate = Long.parseLong(feedData.getQxmExpirationDate());
            long currentDate = Calendar.getInstance().getTimeInMillis();
            return currentDate > expirationDate;
        }

        return false;
    }
    //endregion

    //region loadSingleMCQLeaderboardFragment
    private void loadSingleMCQLeaderboardFragment(String singleMCQId) {
        Bundle bundle = new Bundle();
        bundle.putString(QXM_ID_KEY, singleMCQId);
        bundle.putParcelable(USER_BASIC_KEY, user);
        bundle.putString(QXM_QUESTION_SET_TITLE, feedData.getFeedName());
        bundle.putString(QXM_CREATOR_NAME_KEY, feedData.getFeedCreatorName());
        bundle.putString(QXM_CREATION_TIME_KEY, feedData.getFeedCreationTime());
        bundle.putString(QXM_PRIVACY_KEY, feedData.getFeedPrivacy());
        bundle.putString(SINGLE_MCQ_LEADER_BOARD_PRIVACY, feedData.getLeaderboardPrivacy());
        bundle.putString(SINGLE_MCQ_CREATOR_ID, feedData.getFeedCreatorId());
        qxmFragmentTransactionHelper.loadLeaderboardSingleMCQFragment(bundle);
    }
    //endregion

    //region isLeaderboardCanBeShown
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
    //endregion

    // region ClickListeners
    private void clickListeners() {

        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getActivity() != null) {
                Log.d(TAG, "onBackPressed calling from question overview fragment");
                getActivity().onBackPressed();
            }
        });

        ivMCQOverviewOptionsMenu.setOnClickListener(v -> {
                    boolean isCreator = feedData.getFeedCreatorId().equals(token.getUserId());
                    showSingleMCQOverViewOptionsMenu(v, getActivity(), isCreator);
                }
        );


        ivMCQThumbnail.setOnClickListener(v -> {
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
            if (feedData.isParticipated())
                Toast.makeText(context, "Show Single MCQ Result if Result Published", Toast.LENGTH_SHORT).show();
            else if (!feedData.getFeedCreatorId().equals(token.getUserId())) {

                qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQId, VIEW_FOR_PARTICIPATE_SINGLE_MCQ);

            } else {
                Toast.makeText(context, "You cannot participate this MCQ, because you are the creator of this MCQ.", Toast.LENGTH_SHORT).show();
            }

        });

    }
    // endregion

    //region showSingleMCQOverViewOptionsMenu
    private void showSingleMCQOverViewOptionsMenu(View v, FragmentActivity fragmentActivity, boolean isCreator) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_single_mcq_overview, popup.getMenu());

        if (isCreator) popup.getMenu().findItem(R.id.action_report_qxm).setVisible(false);

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());

                    qxmShareContentToGroup.shareToGroup
                            (feedData.getFeedQxmId(), SHARED_CATEGORY_SINGLE_MCQ);

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getSingleMCQUrl(feedData.getFeedQxmId()));

                    break;

                case R.id.action_add_to_list:
                    // Todo:: Add Single MCQ to List not implemented
                    Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                    addQxmToList.addQxmToList(feedData.getFeedQxmId(), feedData.getFeedName());*/

                    break;

                case R.id.action_report_qxm:

                    QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token.getToken(), token.getUserId(), feedData.getFeedQxmId());
                    qxmReportDialog.showReportDialog(REPORT_SINGLE_MCQ);
                    break;
            }


            return false;
        });
        popup.show();
    }
    //endregion

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
