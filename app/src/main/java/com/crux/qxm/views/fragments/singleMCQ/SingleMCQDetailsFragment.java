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

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.singleMCQDetails.SingleMCQDetailsQuestionAdapter;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.models.singleMCQ.singleMCQDetails.SingleMCQDetails;
import com.crux.qxm.db.models.singleMCQ.singleMCQDetails.SingleMCQSettings;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.singleMCQDetailsFragmentFeature.DaggerSingleMCQDetailsFragmentComponent;
import com.crux.qxm.di.singleMCQDetailsFragmentFeature.SingleMCQDetailsFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.editSingleMCQNetworkCall.EditSingleMCQNetworkCall;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_CREATOR_ID;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_LEADER_BOARD_PRIVACY;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isFeedSingleMCQItemClicked;
import static com.crux.qxm.utils.StaticValues.isMyQxmFeedSingleMCQItemClicked;
import static com.crux.qxm.utils.StaticValues.isMyQxmSingleQxmDetailsRefreshingNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMCQDetailsFragment extends Fragment {

    //region Fragment-Properties
    private static final String TAG = "SingleMCQDetailsFragmen";
    private FeedData myQxmFeedData;
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    //    private QxmModel qxmModel; // --> qxmModel will be used for get ExamQuestionSheet
    private QxmToken token;
    private SingleMCQDetails singleMCQDetails;
    private MultipleChoice multipleChoice;
    private List<MultipleChoice> multipleChoiceList;
    private SingleMCQDetailsQuestionAdapter singleMCQDetailsQuestionAdapter;
    private String singleMCQId;
    private String singleMCQTitle;

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
    @BindView(R.id.tvHoursAgo)
    AppCompatTextView tvHoursAgo;
    @BindView(R.id.cvContest)
    CardView cvContest;
    @BindView(R.id.rvQuestionSheet)
    RecyclerView rvQuestionSheet;
    @BindView(R.id.llTotalParticipants)
    LinearLayoutCompat llTotalParticipants;

    @BindView(R.id.tvViewAllParticipants)
    AppCompatTextView tvViewAllParticipants;
    @BindView(R.id.tvTotalParticipants)
    AppCompatTextView tvTotalParticipants;
    @BindView(R.id.btnLeaderboard)
    AppCompatButton btnLeaderboard;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.noInternetView)
    View noInternetView;

    // Question Settings
    //Single MCQ Settings
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
    //endregion

    // region Fragment-Constructor
    public SingleMCQDetailsFragment() {
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
        singleMCQId = getArguments().getString(FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS);


        if (singleMCQId != null)
            Log.d(TAG, singleMCQId);
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
        View view = inflater.inflate(R.layout.fragment_single_mcq_details, container, false);
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

        if (singleMCQDetails == null) {
            getSingleMCQDetailsNetworkCall(singleMCQId);

        } else {
            nsvRootView.setVisibility(View.VISIBLE);
            setViewValues();

            loadAdapterWithPreviousData();
        }

        viewsOnClick();
    }

    // endregion

    //region SetUpDagger2
    private void setUpDagger2(Context context) {

        SingleMCQDetailsFragmentComponent singleMCQDetailsFragmentComponent =
                DaggerSingleMCQDetailsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent()).build();

        singleMCQDetailsFragmentComponent.injectSingleMCQDetailsFragmentFeature(SingleMCQDetailsFragment.this);

    }
    //endregion

    //region NoInternetFunctionality
    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(View.GONE);
            getSingleMCQDetailsNetworkCall(singleMCQId);
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
        if (multipleChoiceList == null)
            multipleChoiceList = new ArrayList<>();

        // set toolbar
        singleMCQTitle = getArguments() != null ? getArguments().getString(SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY) : null;
        if (!TextUtils.isEmpty(singleMCQTitle))
            tvToolbarTitle.setText(singleMCQTitle);

        Log.d(TAG, "init: " + token.getUserId());
    }
    // endregion

    // region Setting View Values

    public void setViewValues() {

        tvQuestionTitle.setText(myQxmFeedData.getFeedName());
        if (myQxmFeedData.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
            ivQuestionPrivacy.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_public));
        else
            ivQuestionPrivacy.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_private));
        tvQuestionPrivacy.setText(myQxmFeedData.getFeedPrivacy());

        if (!TextUtils.isEmpty(myQxmFeedData.getFeedThumbnailURL()))
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


        tvTotalParticipants.setText(myQxmFeedData.getFeedParticipantsQuantity());

        // hiding view all textView if participants quantity is 0
        if (myQxmFeedData.getFeedParticipantsQuantity().equals("0")) {
            tvViewAllParticipants.setVisibility(View.GONE);
        }

        //setting category

        tvQuestionCategory.setText(QxmArrayListToStringProcessor.getStringFromArrayList(myQxmFeedData.getFeedCategory()));

        Log.d(TAG, "setValuesInViews: Question time: ");

        // show feed item time ago
        long feedCreationTime = Long.parseLong(myQxmFeedData.getFeedCreationTime());
        String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        tvHoursAgo.setText(feedPostTimeAgo);


        llTotalParticipants.setVisibility(View.VISIBLE);

        if (QxmStringIntegerChecker.isInt(myQxmFeedData.getFeedParticipantsQuantity()) &&
                Integer.parseInt(myQxmFeedData.getFeedParticipantsQuantity()) > 0)
            btnLeaderboard.setVisibility(View.VISIBLE);
        else
            btnLeaderboard.setVisibility(View.GONE);

        //Settings

        //region Correct Answer Visibility
        SingleMCQSettings settings = singleMCQDetails.getSingleMCQSettings();
        if (QxmStringIntegerChecker.isLongInt(settings.getCorrectAnswerVisibilityDate()))
            tvCorrectAnswerVisibility.setText(TimeFormatString.getTimeAndDate(
                    Long.parseLong(settings.getCorrectAnswerVisibilityDate())
            ));
        else
            tvCorrectAnswerVisibility.setText(
                    settings.getCorrectAnswerVisibilityDate()
            );
        //endregion

        // region QxmExpiration
        if (QxmStringIntegerChecker.isLongInt(settings.getQxmExpirationDate()))
            tvQxmExpirationDate.setText(TimeFormatString.getTimeAndDate(
                    Long.parseLong(settings.getQxmExpirationDate())
            ));
        else
            tvQxmExpirationDate.setText(getString(R.string.never));

        if (settings.isContestModeEnabled()) {
            llQxmExpirationDate.setVisibility(View.GONE);
            tvContestModeEnabled.setText(getString(R.string.yes));

            // Contest Start Time
            if (QxmStringIntegerChecker.isLongInt(myQxmFeedData.getFeedCreationTime()))
                tvContestStartTime.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(myQxmFeedData.getFeedCreationTime())
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


        tvViewAllParticipants.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadQuestionOverviewParticipantListFragment(myQxmFeedData.getFeedQxmId(), SINGLE_MCQ));

        btnLeaderboard.setOnClickListener(v -> loadSingleMCQLeaderboardFragment());

        // pop this fragment when press back arrow
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

    }

    private void loadSingleMCQLeaderboardFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(QXM_ID_KEY, singleMCQId);
        bundle.putString(QXM_CREATOR_ID_KEY, myQxmFeedData.getFeedCreatorId());
        bundle.putString(QXM_QUESTION_SET_TITLE, myQxmFeedData.getFeedName());
        bundle.putString(QXM_CREATOR_NAME_KEY, myQxmFeedData.getFeedCreatorName());
        bundle.putString(QXM_CREATION_TIME_KEY, myQxmFeedData.getFeedCreationTime());
        bundle.putString(QXM_PRIVACY_KEY, myQxmFeedData.getFeedPrivacy());
        bundle.putString(SINGLE_MCQ_LEADER_BOARD_PRIVACY,myQxmFeedData.getLeaderboardPrivacy());
        bundle.putString(SINGLE_MCQ_CREATOR_ID,myQxmFeedData.getFeedCreatorId());
        qxmFragmentTransactionHelper.loadLeaderboardSingleMCQFragment(bundle);
    }

    private void showSingleQxmDetailsOptionMenu(View v, FragmentActivity fragmentActivity) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_single_mcq_details, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                /*case R.id.action_add_to_list:
                    QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                    addQxmToList.addQxmToList(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName());

                    break;*/

                case R.id.action_edit:

                    EditSingleMCQNetworkCall.getSingleMCQForEditNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                            token.getToken(), singleMCQDetails.getFeedData().getFeedQxmId());

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getSingleMCQUrl(myQxmFeedData.getFeedQxmId()));

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
                    qxmAlertDialogBuilder.setTitle(context.getString(R.string.alert_dialog_title_delete_single_mcq));
                    qxmAlertDialogBuilder.setMessage(context.getString(R.string.alert_dialog_message_delete_single_mcq));

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton(R.string.delete_uppercase,
                            (dialog, which) ->
                                    deleteSingleMCQNetworkCall(token.getToken(), token.getUserId(), myQxmFeedData.getFeedQxmId())
                    );

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton(R.string.no_uppercase, null);

                    qxmAlertDialogBuilder.getAlertDialogBuilder().show();

                    break;

            }


            return false;
        });
        popup.show();
    }

    //region deleteSingleMCQNetworkCall
    private void deleteSingleMCQNetworkCall(String token, String userId, String singleMCQId) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(getString(R.string.alert_dialog_title_delete_single_mcq),
                getString(R.string.progress_dialog_message_delete_single_mcq),
                false);

        Call<QxmApiResponse> deletePoll = apiService.deleteSingleMCQ(token, userId, singleMCQId);

        deletePoll.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteSingleMCQNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                progressDialog.closeProgressDialog();
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(0);
                    Toast.makeText(context, R.string.single_mcq_deleted_successfully, Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteSingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());


                progressDialog.closeProgressDialog();

                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    //endregion

    //endregion


    //region getSingleMCQDetailsNetworkCall
    public void getSingleMCQDetailsNetworkCall(String singleMCQId) {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        if (!TextUtils.isEmpty(singleMCQTitle))
            dialog.showProgressDialog("Loading details", String.format("%s is loading. Please wait... ", singleMCQTitle), false);
        else
            dialog.showProgressDialog("Loading details", String.format("%s is loading. Please wait... ", "Single MCQ Details"), false);
        Call<SingleMCQDetails> getMyQxmDetails = apiService.getSpecificSingleMCQ(token.getToken(), singleMCQId);

        getMyQxmDetails.enqueue(new Callback<SingleMCQDetails>() {
            @Override
            public void onResponse(@NonNull Call<SingleMCQDetails> call, @NonNull Response<SingleMCQDetails> response) {
                Log.d(TAG, "onResponse: getSingleMCQDetailsNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();

                noInternetView.setVisibility(View.GONE);


                if (response.code() == 200) {
                    singleMCQDetails = response.body();
                    if (singleMCQDetails != null) {

                        if (singleMCQDetails.getFeedData() != null) {

                            myQxmFeedData = singleMCQDetails.getFeedData();
                            myQxmFeedData.setLeaderboardPrivacy(singleMCQDetails.getSingleMCQSettings().getLeaderboardPrivacy());
                            //questionList = singleMCQDetails.getQuestionList();

                            Log.d(TAG, "onResponse: " + myQxmFeedData);

                            // making layout (root view) visible at first
                            nsvRootView.setVisibility(View.VISIBLE);

                            setViewValues();

                            multipleChoice = new MultipleChoice();

                            multipleChoice.setQuestionTitle(singleMCQDetails.getFeedData().getFeedName());
                            multipleChoice.setDescription(singleMCQDetails.getFeedData().getFeedDescription());
                            //multipleChoice.setYoutubeVideoLink(singleMCQDetails.getFeedData().getFeedYoutubeLinkURL());
                            //multipleChoice.setImage(singleMCQDetails.getFeedData().getFeedThumbnailURL());
                            multipleChoice.setOptions(singleMCQDetails.getFeedData().getSingleMCQoptions());
                            multipleChoice.setCorrectAnswers((ArrayList<String>) singleMCQDetails.getCorrectAns());

                            multipleChoiceList.add(multipleChoice);

                            singleMCQDetailsQuestionAdapter = new SingleMCQDetailsQuestionAdapter(context, picasso, multipleChoiceList);
                            rvQuestionSheet.setLayoutManager(new LinearLayoutManager(context));
                            rvQuestionSheet.setNestedScrollingEnabled(false);
                            rvQuestionSheet.setAdapter(singleMCQDetailsQuestionAdapter);

                        } else {

                            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: " + singleMCQDetails);
                        }

                    } else
                        Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SingleMCQDetails> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
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
        singleMCQDetailsQuestionAdapter = new SingleMCQDetailsQuestionAdapter(context, picasso, multipleChoiceList);
        rvQuestionSheet.setLayoutManager(new LinearLayoutManager(context));
        rvQuestionSheet.setNestedScrollingEnabled(false);
        rvQuestionSheet.setAdapter(singleMCQDetailsQuestionAdapter);
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
            singleMCQDetails = new SingleMCQDetails();
            myQxmFeedData = new FeedData();
            getSingleMCQDetailsNetworkCall(singleMCQId);

        }

        // The following boolean flag variable is used to
        // restrict from occurring multiple Single MCQ touch events (for solving Fragment Overlay)
        isFeedSingleMCQItemClicked = false;
        isMyQxmFeedSingleMCQItemClicked = false;
    }







}
