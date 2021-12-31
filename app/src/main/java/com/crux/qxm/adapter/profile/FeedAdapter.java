package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.FeedPollOptionAdapter;
import com.crux.qxm.adapter.singleMCQAdapter.SingleMCQAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.feed.FeedDataCauseOfNotInterested;
import com.crux.qxm.db.models.feed.FeedDataInterested;
import com.crux.qxm.db.models.feed.FeedDataNotInterested;
import com.crux.qxm.db.models.feed.HiddenFeedData;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.InternalURLSpan;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.deleteQxmNetworkCall.DeleteQxmNetworkCall;
import com.crux.qxm.networkLayer.editPollNetworkCall.GetPollDataNetworkCall;
import com.crux.qxm.networkLayer.editQxmNetworkCall.EditQxmNetworkCall;
import com.crux.qxm.networkLayer.editSingleMCQNetworkCall.EditSingleMCQNetworkCall;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAddQxmToList;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.qxmDialogs.QxmReportDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_POLL;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_QXM;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.REPORT_POLL;
import static com.crux.qxm.utils.StaticValues.REPORT_QXM;
import static com.crux.qxm.utils.StaticValues.REPORT_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_POLL;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isFeedSingleMCQItemClicked;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region FeedAdapter-Properties
    private static final String TAG = "FeedAdapter";
    public static String qxmId;
    private final int NORMAL_FEED = 1;
    private final int INTEREST_BASED_FEED = 2;
    private final int NOT_INTERESTED = 3;
    private final int CAUSE_OF_NOT_INTERESTED = 4;
    private final int POLL_FEED = 5;
    private final int HIDE_FEED_ITEM = 6;
    private final int NORMAL_FEED_WITH_LONG_NAME = 7;
    private final int SINGLE_MCQ_FEED = 8;
    public AppCompatDialog dialog;
    private Context context;
    private List<Object> items;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private String userId;
    private String token;
    private QxmApiService apiService;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private FeedAdapterListener feedAdapterListener;
    //endregion

    public FeedAdapter(Context context, List<Object> items, FragmentActivity fragmentActivity, Picasso picasso, String userId, String token, QxmApiService apiService, FeedAdapterListener feedAdapterListener) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.userId = userId;
        this.token = token;
        this.apiService = apiService;
        this.feedAdapterListener = feedAdapterListener;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflating layout for each view holder
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case NORMAL_FEED:
                View normalFeedView = layoutInflater.inflate(R.layout.feed_single_item, parent, false);
                viewHolder = new NormalFeedViewHolder(normalFeedView);
                return viewHolder;

            case NORMAL_FEED_WITH_LONG_NAME:
                Log.d(TAG, "onCreateViewHolder: NORMAL_FEED_WITH_LONG_NAME");
                View normalFeedWithLongNameView = layoutInflater.inflate(R.layout.feed_single_item_for_long_name, parent, false);
                viewHolder = new NormalFeedViewHolder(normalFeedWithLongNameView);
                return viewHolder;

            case POLL_FEED:
                View pollFeedView = layoutInflater.inflate(R.layout.feed_poll_single_item, parent, false);
                viewHolder = new PollFeedViewHolder(pollFeedView);
                return viewHolder;

            case INTEREST_BASED_FEED:
                View interestedFeedView = layoutInflater.inflate(R.layout.feed_layout_interest_based_qxm_container, parent, false);
                viewHolder = new InterestedFeedViewHolder(interestedFeedView);
                return viewHolder;

            case NOT_INTERESTED:
                View removedFeedItemView = layoutInflater.inflate(R.layout.feed_single_item_removed, parent, false);
                viewHolder = new NotInterestedItemViewHolder(removedFeedItemView);
                return viewHolder;

            case CAUSE_OF_NOT_INTERESTED:
                View tellUsWhyView = layoutInflater.inflate(R.layout.feed_single_item_tell_us_why, parent, false);
                viewHolder = new CauseOfNotInterestedItemViewHolder(tellUsWhyView);
                return viewHolder;

            case HIDE_FEED_ITEM:
                View hideFeedItemView = layoutInflater.inflate(R.layout.feed_single_item_hide_feed_item, parent, false);
                viewHolder = new HideFeedItemViewHolder(hideFeedItemView);
                return viewHolder;

            case SINGLE_MCQ_FEED:
                View singleMCQFeedView = layoutInflater.inflate(R.layout.feed_single_mcq_single_item, parent, false);
                viewHolder = new SingleMCQFeedViewHolder(singleMCQFeedView);
                return viewHolder;

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int igniteCount1;
        int shareCount;
        switch (holder.getItemViewType()) {

            case NORMAL_FEED_WITH_LONG_NAME:

            case NORMAL_FEED:

                // region NORMAL_FEED

                NormalFeedViewHolder normalFeedViewHolder = (NormalFeedViewHolder) holder;

                FeedData feedDataNormal = (FeedData) items.get(position);

                Log.d(TAG, "onBindViewHolder: Normal Feed: " + feedDataNormal.toString());

                Log.d(TAG, "onBindViewHolder: feed thumbnail: " + feedDataNormal.getFeedThumbnailURL());

                if (!TextUtils.isEmpty(feedDataNormal.getFeedCreatorImageURL())) {
                    picasso.load(feedDataNormal.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(normalFeedViewHolder.ivUserImage);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(feedDataNormal.getFeedCreatorImageURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(normalFeedViewHolder.ivUserImage);

                } else {
                    normalFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                normalFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                feedDataNormal.getFeedCreatorId(),
                                feedDataNormal.getFeedCreatorName())
                );

                if (feedDataNormal.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
                else
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));

                // show contest text view if contest mode is on
                if (feedDataNormal.isContestModeOn())
                    normalFeedViewHolder.CVContest.setVisibility(View.VISIBLE);
                else normalFeedViewHolder.CVContest.setVisibility(View.GONE);

                // show thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(feedDataNormal.getFeedThumbnailURL())) {
                    normalFeedViewHolder.ivFeedThumbnail.setVisibility(View.VISIBLE);
                    picasso.load(feedDataNormal.getFeedThumbnailURL()).into(normalFeedViewHolder.ivFeedThumbnail);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(feedDataNormal.getFeedThumbnailURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(normalFeedViewHolder.ivFeedThumbnail);
                } else {
                    //normalFeedViewHolder.flFeedThumbnailContainer.setVisibility(View.GONE);
                    normalFeedViewHolder.ivFeedThumbnail.setVisibility(View.GONE);
//                    normalFeedViewHolder.tvFeedThumbnail.setVisibility(View.VISIBLE);
//                    normalFeedViewHolder.tvFeedThumbnail.setText(feedDataNormal.getFeedName());
                }


                if (!TextUtils.isEmpty(feedDataNormal.getFeedYoutubeLinkURL())) {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.VISIBLE);
                } else {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.GONE);
                }

                normalFeedViewHolder.tvFeedName.setText(feedDataNormal.getFeedName());
                normalFeedViewHolder.tvFeedCreator.setText(feedDataNormal.getFeedCreatorName().trim());

                if (feedDataNormal.getFeedParticipantsQuantity() != null && !feedDataNormal.getFeedParticipantsQuantity().equals("0")) {

                    if (feedDataNormal.getFeedParticipantsQuantity().equals("1"))
                        normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", feedDataNormal.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participant)));
                    else
                        normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", feedDataNormal.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));

                    normalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                } else {
                    normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
                    normalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                }

                // show feed item time ago
                if (QxmStringIntegerChecker.isLongInt(feedDataNormal.getFeedCreationTime())) {

                    long feedCreationTime = Long.parseLong(feedDataNormal.getFeedCreationTime());
                    String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                    normalFeedViewHolder.tvFeedCreationTime.setText(feedPostTimeAgo);
                } else {
                    Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    normalFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
                }
                normalFeedViewHolder.tvFeedDescription.setText(feedDataNormal.getFeedDescription());


                SetlinkClickable.setLinkclickEvent(normalFeedViewHolder.tvFeedDescription, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, url);
                        context.startActivity(intent);
                    }
                });


                if (feedDataNormal.isEditedFlag()) {
                    normalFeedViewHolder.tvEdited.setVisibility(View.VISIBLE);
                } else {
                    normalFeedViewHolder.tvEdited.setVisibility(View.GONE);
                }

                // region feedAdapterListenerCalls

                normalFeedViewHolder.tvFeedCreator.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(feedDataNormal.getFeedCreatorId(),
                                feedDataNormal.getFeedCreatorName()));

                normalFeedViewHolder.ivYoutubeButton.setOnClickListener(v -> {

                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, feedDataNormal.getFeedYoutubeLinkURL());
                    context.startActivity(intent);


                });
                //endregion


                // IgniteCount
                if (TextUtils.isEmpty(feedDataNormal.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(feedDataNormal.getFeedIgniteQuantity());
                    feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!feedDataNormal.getFeedIgniteQuantity().equals("0")) {
                    normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                } else {
                    normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }
                normalFeedViewHolder.tvIgniteQuantity.setText(feedDataNormal.getFeedIgniteQuantity());

                // ShareCount
                if (TextUtils.isEmpty(feedDataNormal.getFeedShareQuantity())) {
                    shareCount = 0;
                    feedDataNormal.setFeedShareQuantity(String.valueOf(shareCount));
                } else {
                    shareCount = Integer.parseInt(feedDataNormal.getFeedShareQuantity());
                    feedDataNormal.setFeedShareQuantity(String.valueOf(shareCount));
                }
                normalFeedViewHolder.tvShareQuantity.setText(feedDataNormal.getFeedShareQuantity());


                // region feed main content onclick

//                normalFeedViewHolder.llSingleFeedItem.setOnClickListener(v -> {
//                   /* Toast.makeText(context, "Clicked Normal feed Main Content", Toast.LENGTH_SHORT).show();
//
//
//                    String positionn = String.valueOf(position);
//                    Log.d("Position",positionn);
//
//                    feedDataToPass = feedDataNormal;
//                    qxmId = feedDataNormal.getFeedQxmId();
//
//                    Log.d("FeedData",feedDataNormal.toString());
//                    //Log.d("FeedDataToPass",feedDataToPass.toString());
//
//                    Bundle args = new Bundle();
//                    args.putParcelable(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, feedDataNormal);
//                    QuestionOverViewFragment questionOverViewFragment =  new QuestionOverViewFragment();
//                    questionOverViewFragment.setArguments(args);
//
//
//                    fragmentManager = fragmentActivity.getSupportFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                    fragmentTransaction.replace(R.id.frame_container,questionOverViewFragment,"questionOverViewFragment");
//                    fragmentTransaction.addToBackStack("questionOverViewFragment");
//                    fragmentTransaction.commit();*/
//                });
//
//                normalFeedViewHolder.tvFeedName.setOnClickListener(v -> {
//
//                    String qxmId = feedDataNormal.getFeedQxmId();
//                    Log.d("FeedData", feedDataNormal.toString());
//                    Bundle args = new Bundle();
//                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
//                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
//                });
//
//                normalFeedViewHolder.ivFeedThumbnail.setOnClickListener(v -> {
//
//                    if (!TextUtils.isEmpty(feedDataNormal.getFeedYoutubeLinkURL())) {
//                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
//                        intent.putExtra(YOUTUBE_LINK_KEY, feedDataNormal.getFeedYoutubeLinkURL());
//                        context.startActivity(intent);
//                    } else {
//
//                        feedAdapterListener.onItemSelected(feedDataNormal.getFeedCreatorId(), feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName());
//                        /*String qxmId = feedDataNormal.getFeedQxmId();
//                        Log.d("FeedData", feedDataNormal.toString());
//                        Bundle args = new Bundle();
//                        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
//
//                        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);*/
//                    }
//
//                });
//


                /*normalFeedViewHolder.tvFeedThumbnail.setOnClickListener(v -> {

                    String qxmId = feedDataNormal.getFeedQxmId();
                    Log.d("FeedData", feedDataNormal.toString());
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);


                });*/

                // endregion

                // region feed menu item onclick

                normalFeedViewHolder.ivFeedOptions.setOnClickListener(v -> {

                    //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();


                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();

                    if (feedDataNormal.getFeedCreatorId().equals(userId)) {
                        inflater.inflate(R.menu.menu_feed_for_creator, popup.getMenu());
                    } else {
                        inflater.inflate(R.menu.menu_feed, popup.getMenu());
                    }


                    popup.setOnMenuItemClickListener(item -> {

                        switch (item.getItemId()) {

                            case R.id.action_not_interested:

                                FeedDataNotInterested notInterested = new FeedDataNotInterested();
                                FeedDataCauseOfNotInterested causeOfNotInterested = new FeedDataCauseOfNotInterested();
                                causeOfNotInterested.setFeedId(feedDataNormal.getFeedId());
                                causeOfNotInterested.setFeedCreatorId(feedDataNormal.getFeedCreatorId());
                                causeOfNotInterested.setFeedQxmId(feedDataNormal.getFeedQxmId());
                                causeOfNotInterested.setNotInterestedCause("");
                                causeOfNotInterested.setFeedData(feedDataNormal);

                                notInterested.setCauseOfNotInterested(causeOfNotInterested);

                                items.set(position, notInterested);
                                notifyItemChanged(holder.getAdapterPosition());

                                notInterestedNetworkCall(token, feedDataNormal.getFeedQxmId(), userId);

                                break;

                            case R.id.action_share_to_group:

                                QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                                        (fragmentActivity, apiService, context, picasso, userId, token);
                                qxmShareContentToGroup.shareToGroup
                                        (feedDataNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                                break;

                            case R.id.action_share_link:
                                QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                                QxmUrlHelper urlHelper = new QxmUrlHelper();
                                qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(feedDataNormal.getFeedQxmId()));

                                break;

                            case R.id.action_add_to_list:
                                QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, userId, token);
                                addQxmToList.addQxmToList(feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName());

                                break;

                            case R.id.action_report_qxm:

                                QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token, userId, feedDataNormal.getFeedQxmId());
                                qxmReportDialog.showReportDialog(REPORT_QXM);
                                break;

                            case R.id.action_edit:

                                EditQxmNetworkCall.editQxmNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                                        fragmentActivity, token, userId, feedDataNormal.getFeedQxmId());


                                break;

                            case R.id.action_delete:

                                // Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                                QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

                                qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);
                                qxmAlertDialogBuilder.setTitle(context.getString(R.string.alert_dialog_title_delete_qxm));
                                qxmAlertDialogBuilder.setMessage(context.getString(R.string.alert_dialog_message_delete_qxm));

                                qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton(R.string.delete_uppercase,
                                        (dialog, which) -> {
                                            DeleteQxmNetworkCall.deleteQxmNetworkCall(
                                                    apiService,
                                                    context,
                                                    qxmFragmentTransactionHelper,
                                                    token,
                                                    userId,
                                                    feedDataNormal.getFeedQxmId(),
                                                    () -> {
                                                        items.remove(position);
                                                        notifyItemRemoved(position);
                                                    }
                                            );
                                        });

                                qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton(R.string.no_uppercase, null);

                                qxmAlertDialogBuilder.getAlertDialogBuilder().show();


                                break;
                        }


                        return false;
                    });
                    popup.show();

                });

                // endregion

                // region ignite and share
                // at first checking if user already clicked on ignite
                if (feedDataNormal.isIgnitedClicked()) {

                    String ignited = "Ignited";
                    normalFeedViewHolder.tvIgnite.setText(ignited);
                    normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }

                normalFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!normalFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        normalFeedViewHolder.tvIgnite.setText(ignited);
                        normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        igniteQxmNetworkCall(token, userId, feedDataNormal.getFeedQxmId());
                        int igniteCount = Integer.parseInt(feedDataNormal.getFeedIgniteQuantity());
                        igniteCount++;
                        feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        normalFeedViewHolder.tvIgniteQuantity.setText(feedDataNormal.getFeedIgniteQuantity());
                        normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                    } else {
                        String ignite = "Ignite";
                        normalFeedViewHolder.tvIgnite.setText(ignite);
                        normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                        normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                        igniteQxmNetworkCall(token, userId, feedDataNormal.getFeedQxmId());
                        int igniteCount = Integer.parseInt(feedDataNormal.getFeedIgniteQuantity());

                        if (igniteCount > 0)
                            igniteCount--;
                        else
                            igniteCount = 0;

                        feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        normalFeedViewHolder.tvIgniteQuantity.setText(feedDataNormal.getFeedIgniteQuantity());

                        if (igniteCount <= 0) {

                            normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                            normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                        }
                    }

                });


                normalFeedViewHolder.llcShare.setOnClickListener(v -> {
                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    if (!normalFeedViewHolder.tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                        qxmShareContentToGroup.shareToGroup
                                (feedDataNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                    } else {

                        qxmShareContentToGroup.shareToGroup
                                (feedDataNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                        if (qxmShareContentToGroup.sharedSuccess) {
                            qxmShareContentToGroup.sharedSuccess = false;
                            normalFeedViewHolder.tvShare.setText(context.getString(R.string.shared));
                            normalFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                            normalFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }


                    }


                });

                if (feedDataNormal.getFeedCreatorId().equals(userId)) {
                    normalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_file_document));
                    normalFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));

                    normalFeedViewHolder.tvParticipate.setText(R.string.see_details);
                    normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));


                } else {

                    // If I Participated in this Qxm, then update the UI to blue color otherwise gray
                    Log.d(TAG, "feedDataNormal: isParticipated - " + feedDataNormal.isParticipated());
                    if (feedDataNormal.isParticipated()) {
                        normalFeedViewHolder.tvParticipate.setText(context.getString(R.string.participated));
                        normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                        normalFeedViewHolder.ivParticipate
                                .setColorFilter(ContextCompat.getColor(context, R.color.light_blue_400),
                                        android.graphics.PorterDuff.Mode.SRC_ATOP);
                        normalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.ic_file_document));

                        normalFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.light_blue_400));

                    } else {
                        normalFeedViewHolder.tvParticipate.setText(context.getString(R.string.participate));
                        normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));
                        normalFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));
                        normalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.ic_participate_hand));
                        normalFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));
                    }
                }

                normalFeedViewHolder.llParticipate.setOnClickListener(v ->
                        feedAdapterListener.onItemSelected(feedDataNormal.getFeedCreatorId(), feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName())
                );

                //endregion


                // endregion

                break;

            case POLL_FEED:

                // region POLL FEED

                Log.d(TAG, "onBindViewHolder: POLL FEED CALLED...");

                PollFeedViewHolder pollFeedViewHolder = (PollFeedViewHolder) holder;

                FeedData pollFeedData = (FeedData) items.get(position);

                Log.d(TAG, "onBindViewHolder: Poll Feed: " + pollFeedData.toString());

                pollFeedViewHolder.tvFeedPollTitle.setText(pollFeedData.getFeedName());

                // Poll creator image
                if (!TextUtils.isEmpty(pollFeedData.getFeedCreatorImageURL())) {
                    picasso.load(pollFeedData.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(pollFeedViewHolder.ivUserImage);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(pollFeedData.getFeedCreatorImageURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(pollFeedViewHolder.ivUserImage);

                } else {
                    pollFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                // Goto Poll Creator Profile
                pollFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                pollFeedData.getFeedCreatorId(),
                                pollFeedData.getFeedCreatorName())
                );

                // poll creator name
                pollFeedViewHolder.tvPollCreatorName.setText(pollFeedData.getFeedCreatorName());


                // region poll-body


                // poll title
                Log.d(TAG, "onBindViewHolder: poll title: " + pollFeedData.getFeedName());
                pollFeedViewHolder.tvPollTitle.setText(pollFeedData.getFeedName());

                // Todo:: for debugging -> singlePollOverview
                //region singlePollOverviewDebugging
                pollFeedViewHolder.tvPollTitle.setOnClickListener(v -> {
                    Log.d(TAG, "onBindViewHolder: singlePollOverview debugging..");
                    qxmFragmentTransactionHelper.loadPollOverviewFragment(pollFeedData.getFeedPollId());
                });

                //endregion


                // region show poll thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(pollFeedData.getFeedThumbnailURL())) {
                    pollFeedViewHolder.ivPollThumbnail.setVisibility(View.VISIBLE);
                    picasso.load(pollFeedData.getFeedThumbnailURL()).into(pollFeedViewHolder.ivPollThumbnail);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(pollFeedData.getFeedThumbnailURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(pollFeedViewHolder.ivPollThumbnail);

                } else {
                    pollFeedViewHolder.ivPollThumbnail.setVisibility(View.GONE);
                }
                // endregion

                // region poll options


                FeedPollOptionAdapter feedPollOptionAdapter = new FeedPollOptionAdapter(context, pollFeedData.getFeedPollId(), apiService, userId, token, pollFeedData, pollFeedViewHolder.tvParticipantsQuantity, qxmFragmentTransactionHelper);
                pollFeedViewHolder.rvPollOption.setLayoutManager(new LinearLayoutManager(context));
                pollFeedViewHolder.rvPollOption.setAdapter(feedPollOptionAdapter);
                pollFeedViewHolder.rvPollOption.setNestedScrollingEnabled(false);


                // endregion


                // endregion

                if (pollFeedData.getPollParticipatorQuantity() != null && !pollFeedData.getPollParticipatorQuantity().equals("0")) {

                    if (pollFeedData.getPollParticipatorQuantity().equals("1"))
                        pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participant)));
                    else
                        pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participants)));

                    pollFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                } else {
                    pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
                    pollFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                }


                // show feed item time ago
                if (QxmStringIntegerChecker.isLongInt(pollFeedData.getFeedCreationTime())) {

                    long feedCreationTime = Long.parseLong(pollFeedData.getFeedCreationTime());
                    String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                    pollFeedViewHolder.tvPollCreationTime.setText(feedPostTimeAgo);
                } else {
                    Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    pollFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
                }

                // region PollFeedAdapterItemClickListeners

                pollFeedViewHolder.tvPollCreatorName.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(pollFeedData.getFeedCreatorId(),
                                pollFeedData.getFeedCreatorName()));

                // endregion

                // region IgniteCount

                if (TextUtils.isEmpty(pollFeedData.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
                    pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!pollFeedData.getFeedIgniteQuantity().equals("0")) {
                    pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                } else {
                    pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }
                pollFeedViewHolder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

                // endregion

                // region ShareCount

                if (TextUtils.isEmpty(pollFeedData.getFeedShareQuantity())) {
                    shareCount = 0;
                    pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                } else {
                    shareCount = Integer.parseInt(pollFeedData.getFeedShareQuantity());
                    pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                }

                pollFeedViewHolder.tvShareQuantity.setText(pollFeedData.getFeedShareQuantity());

                // endregion


                // region feed menu item onclick

                pollFeedViewHolder.ivPollFeedOptions.setOnClickListener(v -> {

                    if (pollFeedData.getFeedCreatorId().equals(userId)) {
                        loadPollFeedMenuForPollCreator(v, holder, position, pollFeedData);
                    } else {
                        loadPollFeedMenuForNormalUser(v, holder, position, pollFeedData);
                    }


                });

                // endregion

                // region ignite and share

                // region at first checking if user already clicked on ignite
                if (pollFeedData.isIgnitedClicked()) {

                    String ignited = "Ignited";
                    pollFeedViewHolder.tvIgnite.setText(ignited);
                    pollFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    pollFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }
                // endregion

                //region PollIgnite
                pollFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!pollFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        pollFeedViewHolder.tvIgnite.setText(ignited);
                        pollFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        pollFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        ignitePollNetworkCall(userId, pollFeedData.getFeedPollId());

                        int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
                        igniteCount++;
                        pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        pollFeedViewHolder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());
                        pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                    } else {
                        String ignite = "Ignite";
                        pollFeedViewHolder.tvIgnite.setText(ignite);
                        pollFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                        pollFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                        ignitePollNetworkCall(userId, pollFeedData.getFeedPollId());
                        int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());

                        if (igniteCount > 0)
                            igniteCount--;
                        else
                            igniteCount = 0;

                        pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        pollFeedViewHolder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

                        if (igniteCount <= 0) {

                            pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                            pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                        }
                    }

                });
                //endregion

                //region PollShare
                pollFeedViewHolder.llcShare.setOnClickListener(v -> {
                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);

                    if (!pollFeedViewHolder.tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                        qxmShareContentToGroup.shareToGroup
                                (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    } else {

                        qxmShareContentToGroup.shareToGroup
                                (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                        if (qxmShareContentToGroup.sharedSuccess) {
                            qxmShareContentToGroup.sharedSuccess = false;
                            pollFeedViewHolder.tvShare.setText(context.getString(R.string.shared));
                            pollFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                            pollFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }
                    }
                });
                //endregion

                //endregion

                // endregion

                break;

            case SINGLE_MCQ_FEED:

                // region SINGLE_MCQ_FEED

                Log.d(TAG, "onBindViewHolder: SINGLE MCQ FEED CALLED...");

                SingleMCQFeedViewHolder singleMCQFeedViewHolder = (SingleMCQFeedViewHolder) holder;

                FeedData singleMCQFeedData = (FeedData) items.get(position);

               /* singleMCQFeedViewHolder.llSingleFeedItem.setOnClickListener(v ->
                        feedAdapterListener.onSingleMCQItemSelected(userId, singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.isParticipated())
                );*/

                Log.d(TAG, "onBindViewHolder: Single MCQ Feed: " + singleMCQFeedData.toString());

                // setting contest mode icon based on status
                if (singleMCQFeedData.isContestModeOn())
                    singleMCQFeedViewHolder.CVContest.setVisibility(View.VISIBLE);
                else singleMCQFeedViewHolder.CVContest.setVisibility(View.GONE);

                singleMCQFeedViewHolder.tvSingleMCQFeedTitle.setText(singleMCQFeedData.getFeedName());

                // Single MCQ creator image
                if (!TextUtils.isEmpty(singleMCQFeedData.getFeedCreatorImageURL())) {
                    picasso.load(singleMCQFeedData.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(singleMCQFeedViewHolder.ivUserImage);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(singleMCQFeedData.getFeedCreatorImageURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(singleMCQFeedViewHolder.ivUserImage);

                } else {
                    singleMCQFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                // Goto SingleMCQ Creator Profile
                singleMCQFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                singleMCQFeedData.getFeedCreatorId(),
                                singleMCQFeedData.getFeedCreatorName())
                );

                // creator name
                singleMCQFeedViewHolder.tvSingleMCQCreatorFullName.setText(singleMCQFeedData.getFeedCreatorName());


                // region SingleMCQ-body


                // SingleMCQ title
                Log.d(TAG, "onBindViewHolder: SingleMCQ title: " + singleMCQFeedData.getFeedName());

                if (!TextUtils.isEmpty(singleMCQFeedData.getFeedDescription())) {
                    singleMCQFeedViewHolder.tvSingleMCQTitle.setText(singleMCQFeedData.getFeedDescription());
                    SetlinkClickable.setLinkclickEvent(singleMCQFeedViewHolder.tvSingleMCQTitle, new HandleLinkClickInsideTextView() {
                        public void onLinkClicked(String url) {
                            Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                            intent.putExtra(YOUTUBE_LINK_KEY, url);
                            context.startActivity(intent);
                        }
                    });
                } else
                    singleMCQFeedViewHolder.tvSingleMCQTitle.setText(singleMCQFeedData.getFeedName());


                // region show SingleMCQ thumbnail image view if feed contains thumbnail

                Log.d(TAG, "onBindViewHolder singleMCQFeedData: " + singleMCQFeedData.getFeedThumbnailURL());
                if (!TextUtils.isEmpty(singleMCQFeedData.getFeedThumbnailURL())) {
                    singleMCQFeedViewHolder.ivSingleMCQThumbnail.setVisibility(View.VISIBLE);
                    picasso.load(singleMCQFeedData.getFeedThumbnailURL()).into(singleMCQFeedViewHolder.ivSingleMCQThumbnail);
                    // Glide
                    //         .with(fragmentActivity)
                    //         .load(singleMCQFeedData.getFeedThumbnailURL())
                    //         .thumbnail(0.1f)
                    //         .centerInside()
                    //         .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //         .transition(DrawableTransitionOptions.withCrossFade())
                    //         .into(singleMCQFeedViewHolder.ivSingleMCQThumbnail);

                } else {
                    singleMCQFeedViewHolder.ivSingleMCQThumbnail.setVisibility(View.GONE);
                }
                // endregion

                // region show youtube button if youtube video link is available
                if (!TextUtils.isEmpty(singleMCQFeedData.getFeedYoutubeLinkURL())) {
                    singleMCQFeedViewHolder.ivYoutubeButton.setVisibility(View.VISIBLE);
                } else {
                    singleMCQFeedViewHolder.ivYoutubeButton.setVisibility(View.GONE);
                }
                //endregion

                // region SingleMCQ options

                MultipleChoice multipleChoice = new MultipleChoice();
                multipleChoice.setOptions(singleMCQFeedData.getSingleMCQoptions());
                SingleMCQAdapter singleMCQAdapter = new SingleMCQAdapter(context, multipleChoice);

                singleMCQFeedViewHolder.rvSingleMCQOptions.setAdapter(singleMCQAdapter);
                singleMCQFeedViewHolder.rvSingleMCQOptions.setNestedScrollingEnabled(false);
                singleMCQFeedViewHolder.rvSingleMCQOptions.setLayoutManager(new LinearLayoutManager(context));

                singleMCQFeedViewHolder.rvSingleMCQOptions.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        //Log.d(TAG, "onInterceptTouchEvent: " + e.toString());

                        if (e.getAction() == MotionEvent.ACTION_UP) {
                            if (!isFeedSingleMCQItemClicked) {
                                isFeedSingleMCQItemClicked = true;

                                if (singleMCQFeedData.getFeedCreatorId().equals(userId)) {
                                    loadSingleMCQDetailsFragment(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName());
                                } else {

                                    if (singleMCQFeedData.isParticipated()) {
                                        qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQFeedData.getFeedQxmId());
                                    } else {
                                        qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQFeedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_SINGLE_MCQ);
                                    }
                                }
                            }
                        }


                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        //Log.d(TAG, "onTouchEvent: ");
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        //Log.d(TAG, "onRequestDisallowInterceptTouchEvent: ");
                    }
                });


                // endregion


                // endregion

                //region If the Single Qxm is created by me then hide the participate button.
                if (singleMCQFeedData.getFeedCreatorId().equals(userId)) {

                    singleMCQFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_file_document));
                    singleMCQFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));

                    singleMCQFeedViewHolder.tvParticipate.setText(context.getResources().getString(R.string.see_details));
                    singleMCQFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));

                    singleMCQFeedViewHolder.llParticipate.setOnClickListener(v ->
                            loadSingleMCQDetailsFragment(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName()));


                    singleMCQFeedViewHolder.rvSingleMCQOptions.setOnClickListener(v ->
                            loadSingleMCQDetailsFragment(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName()));

                    singleMCQFeedViewHolder.tvSingleMCQTitle.setOnClickListener(v ->
                            loadSingleMCQDetailsFragment(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName()));

                } else {

                    if (singleMCQFeedData.isParticipated()) {
                        singleMCQFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.ic_file_document));
                        singleMCQFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.light_blue_400));

                        singleMCQFeedViewHolder.tvParticipate.setText(context.getResources().getString(R.string.see_result));
                        singleMCQFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                        // I'v already participated.. show me the result on
                        singleMCQFeedViewHolder.llParticipate.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQFeedData.getFeedQxmId())
                        );

                        singleMCQFeedViewHolder.rvSingleMCQOptions.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQFeedData.getFeedQxmId()));

                        singleMCQFeedViewHolder.tvSingleMCQTitle.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQFeedData.getFeedQxmId()));

                    } else {
                        singleMCQFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.ic_participate_hand));
                        singleMCQFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));

                        singleMCQFeedViewHolder.tvParticipate.setText(context.getResources().getString(R.string.participate));
                        singleMCQFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));

                        // I'm not participate yet, so participate click listener
                        //TODO:: COMMENTED FOR DEBUGGING PURPOSE
                        /*singleMCQFeedViewHolder.llParticipate.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQFeedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_SINGLE_MCQ)
                        );*/

                        //region DEBUG
                        singleMCQFeedViewHolder.llParticipate.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadSingleMCQOverviewFragment(singleMCQFeedData.getFeedQxmId())
                        );
                        //endregion

                        singleMCQFeedViewHolder.rvSingleMCQOptions.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQFeedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_SINGLE_MCQ));

                        singleMCQFeedViewHolder.tvSingleMCQTitle.setOnClickListener(v ->
                                qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQFeedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_SINGLE_MCQ));
                    }
                }
                //endregion

                //region Set Single MCQ Participate Quantity to view
                if (singleMCQFeedData.getFeedParticipantsQuantity() != null && !singleMCQFeedData.getFeedParticipantsQuantity().equals("0")) {

                    if (singleMCQFeedData.getFeedParticipantsQuantity().equals("1"))
                        singleMCQFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", singleMCQFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participant)));
                    else
                        singleMCQFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", singleMCQFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));

                    singleMCQFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                } else {
                    singleMCQFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
                    singleMCQFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                }
                //endregion

                Log.d(TAG, "onBindViewHolder: SingleMCQ isParticipated: " + singleMCQFeedData.isParticipated());


                // show feed item time ago
                if (QxmStringIntegerChecker.isLongInt(singleMCQFeedData.getFeedCreationTime())) {

                    long feedCreationTime = Long.parseLong(singleMCQFeedData.getFeedCreationTime());
                    String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                    singleMCQFeedViewHolder.tvSingleMCQCreationTime.setText(feedPostTimeAgo);
                } else {
                    Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    pollFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
                }

                // region SingleMCQdAdapterItemClickListeners

                singleMCQFeedViewHolder.tvSingleMCQCreatorFullName.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(singleMCQFeedData.getFeedCreatorId(),
                                singleMCQFeedData.getFeedCreatorName()));

                // endregion

                // region IgniteCount

                if (TextUtils.isEmpty(singleMCQFeedData.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    singleMCQFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(singleMCQFeedData.getFeedIgniteQuantity());
                    singleMCQFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!singleMCQFeedData.getFeedIgniteQuantity().equals("0")) {
                    singleMCQFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    singleMCQFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                } else {
                    singleMCQFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    singleMCQFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }
                singleMCQFeedViewHolder.tvIgniteQuantity.setText(singleMCQFeedData.getFeedIgniteQuantity());

                // endregion

                // region ShareCount

                if (TextUtils.isEmpty(singleMCQFeedData.getFeedShareQuantity())) {
                    shareCount = 0;
                    singleMCQFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                } else {
                    shareCount = Integer.parseInt(singleMCQFeedData.getFeedShareQuantity());
                    singleMCQFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                }

                singleMCQFeedViewHolder.tvShareQuantity.setText(singleMCQFeedData.getFeedShareQuantity());

                // endregion

                // region single mcq thumbnail click listener
                // singleMCQFeedViewHolder.ivSingleMCQThumbnail.setOnClickListener(v -> {
                //
                //     if (!TextUtils.isEmpty(singleMCQFeedData.getFeedYoutubeLinkURL())) {
                //         Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                //         intent.putExtra(YOUTUBE_LINK_KEY, singleMCQFeedData.getFeedYoutubeLinkURL());
                //         context.startActivity(intent);
                //     } else if (singleMCQFeedData.getFeedCreatorId().equals(userId)) {
                //         loadSingleMCQDetailsFragment(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName());
                //     } else {
                //         if (singleMCQFeedData.isParticipated()) {
                //             qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQFeedData.getFeedQxmId());
                //         } else {
                //             qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQFeedData.getFeedQxmId(), VIEW_FOR_PARTICIPATE_SINGLE_MCQ);
                //         }
                //     }
                //
                // });
                //endregion


                // region feed menu item onclick

                singleMCQFeedViewHolder.ivSingleMCQFeedOptions.setOnClickListener(v -> {

                    if (singleMCQFeedData.getFeedCreatorId().equals(userId)) {
                        loadSingleMCQFeedMenuForSingleMCQCreator(v, holder, position, singleMCQFeedData);
                    } else {
                        loadSingleMCQFeedMenuForNormalUser(v, holder, position, singleMCQFeedData);
                    }
                });

                // endregion

                // region ignite and share

                // region at first checking if user already clicked on ignite
                if (singleMCQFeedData.isIgnitedClicked()) {

                    String ignited = "Ignited";
                    singleMCQFeedViewHolder.tvIgnite.setText(ignited);
                    singleMCQFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    singleMCQFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }
                // endregion

                //region PollIgnite
                singleMCQFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!singleMCQFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        singleMCQFeedViewHolder.tvIgnite.setText(ignited);
                        singleMCQFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        singleMCQFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        igniteSingleMCQNetworkCall(userId, singleMCQFeedData.getFeedQxmId());

                        int igniteCount = Integer.parseInt(singleMCQFeedData.getFeedIgniteQuantity());
                        igniteCount++;
                        singleMCQFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        singleMCQFeedViewHolder.tvIgniteQuantity.setText(singleMCQFeedData.getFeedIgniteQuantity());
                        singleMCQFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        singleMCQFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                    } else {
                        String ignite = "Ignite";
                        singleMCQFeedViewHolder.tvIgnite.setText(ignite);
                        singleMCQFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                        singleMCQFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                        igniteSingleMCQNetworkCall(userId, singleMCQFeedData.getFeedPollId());
                        int igniteCount = Integer.parseInt(singleMCQFeedData.getFeedIgniteQuantity());

                        if (igniteCount > 0)
                            igniteCount--;
                        else
                            igniteCount = 0;

                        singleMCQFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        singleMCQFeedViewHolder.tvIgniteQuantity.setText(singleMCQFeedData.getFeedIgniteQuantity());

                        if (igniteCount <= 0) {

                            singleMCQFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                            singleMCQFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                        }
                    }

                });
                //endregion

                //region SingleMCQShare
                singleMCQFeedViewHolder.llcShare.setOnClickListener(v -> {

                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareSingleMCQLink(urlHelper.getSingleMCQUrl(singleMCQFeedData.getFeedQxmId()));
                });

                //endregion

                //endregion

                // endregion

                break;

            case INTEREST_BASED_FEED:

                // region INTEREST_BASED_FEED

                InterestedFeedViewHolder interestedFeedViewHolder = (InterestedFeedViewHolder) holder;
                FeedDataInterested feedDataInterested = (FeedDataInterested) items.get(position);
                Log.d(TAG, "onBindViewHolder: INTEREST_BASED_FEED: " + feedDataInterested.toString());
                FeedItemsInterestedAdapter feedItemsInterestedAdapter = new FeedItemsInterestedAdapter(context, feedDataInterested.getFeedDataListInterested());
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                interestedFeedViewHolder.interestedQxmRV.setLayoutManager(horizontalLayoutManager);
                interestedFeedViewHolder.interestedQxmRV.setAdapter(feedItemsInterestedAdapter);

                // endregion

                break;
            case NOT_INTERESTED:

                // region NOT_INTERESTED

                Log.d(TAG, "onBindViewHolder: NOT_INTERESTED: " + holder.getAdapterPosition());

                NotInterestedItemViewHolder notInterestedItemViewHolder = (NotInterestedItemViewHolder) holder;
                FeedDataNotInterested feedDataNotInterested = (FeedDataNotInterested) items.get(position);

                notInterestedItemViewHolder.tvUndo.setOnClickListener(v -> {
                    items.set(position, feedDataNotInterested.getCauseOfNotInterested().getFeedData());
                    notifyItemChanged(holder.getAdapterPosition());
                });

                notInterestedItemViewHolder.tvTellUsWhy.setOnClickListener(v -> {
                    items.set(position, feedDataNotInterested.getCauseOfNotInterested());
                    notifyItemChanged(holder.getAdapterPosition());
                });

                // endregion

                break;
            case CAUSE_OF_NOT_INTERESTED:

                // region CAUSE_OF_NOT_INTERESTED

                Log.d(TAG, "onBindViewHolder: CAUSE_OF_NOT_INTERESTED: " + holder.getAdapterPosition());

                CauseOfNotInterestedItemViewHolder causeOfNotInterestedItemViewHolder =
                        (CauseOfNotInterestedItemViewHolder) holder;
                FeedDataCauseOfNotInterested causeOfNotInterested = (FeedDataCauseOfNotInterested) items.get(position);


                causeOfNotInterestedItemViewHolder.ivClose.setOnClickListener(v -> {
                    items.set(holder.getAdapterPosition(), new FeedDataNotInterested(causeOfNotInterested));
                    notifyItemChanged(holder.getAdapterPosition());
                });

                causeOfNotInterestedItemViewHolder.rgTellUsWhy.setOnCheckedChangeListener((group, checkedId) -> {

                    switch (checkedId) {
                        case R.id.rbAlreadyParticipated:
                            Log.d(TAG, "onBindViewHolder: " + context.getString(R.string.i_ve_already_participated_in_this_qxm));
                            causeOfNotInterested.setNotInterestedCause(context.getString(R.string.i_ve_already_participated_in_this_qxm));

                            //Todo:: "tell us why" network call
                            causeOfNotInterestedItemViewHolder.rbNotInterested.setChecked(false);
                            items.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);

                            break;

                        case R.id.rbDoNotLikeThisQxm:
                            Log.d(TAG, "onBindViewHolder: " + context.getString(R.string.i_don_t_like_this_qxm));
                            causeOfNotInterested.setNotInterestedCause(context.getString(R.string.i_don_t_like_this_qxm));

                            //Todo:: "tell us why" network call
                            causeOfNotInterestedItemViewHolder.rbDoNotLikeThisQxm.setChecked(false);
                            items.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);

                            break;

                        case R.id.rbNotInterested:
                            Log.d(TAG, "onBindViewHolder: " + context.getString(R.string.i_don_t_like_this_qxm));
                            causeOfNotInterested.setNotInterestedCause(context.getString(R.string.not_interested));

                            //Todo:: "tell us why" network call
                            causeOfNotInterestedItemViewHolder.rbNotInterested.setChecked(false);
                            items.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);

                            break;
                    }
                });

                // endregion

                break;

            case HIDE_FEED_ITEM:
                // region HIDE_FEED_ITEM

                Log.d(TAG, "onBindViewHolder: HIDE_FEED_ITEM: " + holder.getAdapterPosition());

                HideFeedItemViewHolder hideFeedItemViewHolder = (HideFeedItemViewHolder) holder;
                HiddenFeedData hiddenFeedData = (HiddenFeedData) items.get(position);

                hideFeedItemViewHolder.tvQxmRemoved.setText(hiddenFeedData.getFeedItemRemovedMessage());

                hideFeedItemViewHolder.tvUndo.setOnClickListener(v -> {
                    items.set(position, hiddenFeedData.getFeedData());
                    notifyItemChanged(holder.getAdapterPosition());
                });

                // endregion

                break;
        }
    }

    // region loadSingleMCQDetailsFragment
    private void loadSingleMCQDetailsFragment(String singleMCQId, String singleMCQTitle) {

        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS, singleMCQId);
        args.putString(SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY, singleMCQTitle);

        //going to single MCQ details fragment with singleMCQId

        qxmFragmentTransactionHelper.loadSingleMCQDetailsFragment(args);
    }
    // endregion

    // region loadPollFeedMenuForPollCreator
    private void loadPollFeedMenuForPollCreator(View v, RecyclerView.ViewHolder holder, int position, FeedData pollFeedData) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_poll_for_creator, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_hide_poll:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedItemRemovedMessage(context.getString(R.string.poll_removed));
                    hiddenFeedData.setFeedData(pollFeedData);
                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: poll hide network call... implementation
                    notInterestedNetworkCall(token, pollFeedData.getFeedPollId(), userId);

                    break;


                case R.id.action_share_to_group:

//                    TODO COMING SOON
//                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
//                            (fragmentActivity, apiService, context, picasso, userId, token);
//                    qxmShareContentToGroup.shareToGroup
//                            (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    Toast.makeText(v.getContext(), v.getResources().getString(R.string.toast_coming_soon), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.sharePollLink(urlHelper.getPollUrl(pollFeedData.getFeedPollId()));

                    break;


                case R.id.action_edit:

                    GetPollDataNetworkCall.getPollDataNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                            token, pollFeedData.getFeedPollId());

                    break;

                case R.id.action_delete:

                    QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

                    qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);
                    qxmAlertDialogBuilder.setTitle(context.getString(R.string.alert_dialog_title_delete_poll));
                    qxmAlertDialogBuilder.setMessage(context.getString(R.string.alert_dialog_message_delete_poll));

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton(R.string.delete_uppercase,
                            (dialog, which) -> {
                                items.remove(position);
                                notifyItemRemoved(position);
                                deletePollNetworkCall(token, userId, pollFeedData.getFeedPollId());
                            }
                    );

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton(R.string.no_uppercase, null);

                    qxmAlertDialogBuilder.getAlertDialogBuilder().show();

                    break;
            }


            return false;
        });
        popup.show();

    }
    // endregion

    // region loadSingleMCQFeedMenuForSingleMCQCreator
    private void loadSingleMCQFeedMenuForSingleMCQCreator(View v, RecyclerView.ViewHolder holder, int position, FeedData singleMCQFeedData) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_single_mcq_for_creator, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_hide:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedItemRemovedMessage(context.getString(R.string.single_mcq_removed));
                    hiddenFeedData.setFeedData(singleMCQFeedData);
                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: single MCQ hide network call... implementation
                    notInterestedNetworkCall(token, singleMCQFeedData.getFeedPollId(), userId);

                    break;

                case R.id.action_share_to_group:
                    Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    qxmShareContentToGroup.shareToGroup
                            (singleMCQFeedData.getFeedQxmId(), SHARED_CATEGORY_SINGLE_MCQ);*/
                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getSingleMCQUrl(singleMCQFeedData.getFeedQxmId()));

                    break;

                case R.id.action_add_to_list:
                    Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, userId, token);
                    addQxmToList.addQxmToList(feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName());*/

                    break;


                case R.id.action_edit:

                    EditSingleMCQNetworkCall.getSingleMCQForEditNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                            token, singleMCQFeedData.getFeedQxmId());

                    break;

                case R.id.action_delete:

                    QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

                    qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);
                    qxmAlertDialogBuilder.setTitle(context.getString(R.string.alert_dialog_title_delete_single_mcq));
                    qxmAlertDialogBuilder.setMessage(context.getString(R.string.alert_dialog_message_delete_single_mcq));

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton(R.string.delete_uppercase,
                            (dialog, which) -> {
                                items.remove(position);
                                notifyItemRemoved(position);
                                deleteSingleMCQNetworkCall(token, userId, singleMCQFeedData.getFeedQxmId());
                            }
                    );

                    qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton(R.string.no_uppercase, null);

                    qxmAlertDialogBuilder.getAlertDialogBuilder().show();

                    break;
            }


            return false;
        });
        popup.show();

    }
    // endregion

    //region FeedPoolMenuForNormalUser
    private void loadPollFeedMenuForNormalUser(View v, RecyclerView.ViewHolder holder, int position, FeedData pollFeedData) {

        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_poll, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_hide_poll:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedItemRemovedMessage(context.getString(R.string.poll_removed));
                    hiddenFeedData.setFeedData(pollFeedData);

                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: poll hide network call... implementation
                    notInterestedNetworkCall(token, pollFeedData.getFeedPollId(), userId);

                    break;

                case R.id.action_share_to_group:

//                    TODO COMING SOON !
//                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
//                            (fragmentActivity, apiService, context, picasso, userId, token);
//                    qxmShareContentToGroup.shareToGroup
//                            (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.sharePollLink(urlHelper.getQxmUrl(pollFeedData.getFeedPollId()));

                    break;

                case R.id.action_report_poll:

                    QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token, userId, pollFeedData.getFeedPollId());
                    qxmReportDialog.showReportDialog(REPORT_POLL);
                    break;
            }


            return false;
        });
        popup.show();

    }
    //endregion

    // region loadSingleMCQFeedMenuForNormalUser
    private void loadSingleMCQFeedMenuForNormalUser(View v, RecyclerView.ViewHolder holder, int position, FeedData singleMCQFeedData) {

        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_single_mcq, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_hide:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedItemRemovedMessage(context.getString(R.string.single_mcq_removed));
                    hiddenFeedData.setFeedData(singleMCQFeedData);

                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: single MCQ hide network call... implementation
                    notInterestedNetworkCall(token, singleMCQFeedData.getFeedQxmId(), userId);

                    break;

                case R.id.action_share_to_group:
                    Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    qxmShareContentToGroup.shareToGroup
                            (singleMCQFeedData.getFeedQxmId(), SHARED_CATEGORY_SINGLE_MCQ);*/
                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.shareQxmLink(urlHelper.getSingleMCQUrl(singleMCQFeedData.getFeedQxmId()));

                    break;

                case R.id.action_add_to_list:
                    Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, userId, token);
                    addQxmToList.addQxmToList(feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName());*/

                    break;

                case R.id.action_edit:

                    EditSingleMCQNetworkCall.getSingleMCQForEditNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                            token, singleMCQFeedData.getFeedQxmId());

                    break;

                case R.id.action_report:

                    QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token, userId, singleMCQFeedData.getFeedQxmId());
                    qxmReportDialog.showReportDialog(REPORT_SINGLE_MCQ);
                    break;
            }


            return false;
        });
        popup.show();

    }
    // endregion

    // region NotInterestedNetworkCall

    private void notInterestedNetworkCall(String token, String qxmId, String userId) {
        Call<QxmApiResponse> notInterested = apiService.notInterested(token, qxmId, userId);

        notInterested.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: response code = " + response.code());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: notInterested success");
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: notInterestedNetworkCall");
                Log.d(TAG, "onFailure: error message: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: error stackTrace: " + Arrays.toString(t.getStackTrace()));
            }
        });
    }

    // endregion

    // region IgniteQxmNetworkCall
    private void igniteQxmNetworkCall(String token, String userId, String qxmId) {
        Log.d(TAG, String.format("igniteQxmNetworkCall: userId= %s, qxmId= %s", userId, qxmId));

        Call<QxmApiResponse> igniteQxm = apiService.igniteQxm(token, userId, qxmId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    /*if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getFeedItem");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region IgnitePollNetworkCall
    private void ignitePollNetworkCall(String userId, String pollId) {
        Log.d(TAG, String.format("igniteQxmNetworkCall: userId= %s, pollId= %s", userId, pollId));

        Call<QxmApiResponse> igniteQxm = apiService.pollIgnite(token, userId, pollId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: ignitePollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    /*if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ignitePollNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region igniteSingleMCQNetworkCall
    private void igniteSingleMCQNetworkCall(String userId, String singleMCQId) {
        Log.d(TAG, String.format("igniteSingleMCQNetworkCall: userId= %s, pollId= %s", userId, singleMCQId));

        Call<QxmApiResponse> igniteQxm = apiService.singleMCQIgnite(token, userId, singleMCQId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: igniteSingleMCQNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    /*if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: igniteSingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region DeletePollNetworkCall

    private void deletePollNetworkCall(String token, String userId, String pollId) {

        Call<QxmApiResponse> deletePoll = apiService.deletePoll(token, userId, pollId);

        deletePoll.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deletePollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    Toast.makeText(context, R.string.poll_deleted_successfully, Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deletePollNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // endregion

    private void deleteSingleMCQNetworkCall(String token, String userId, String singleMCQId) {

        Call<QxmApiResponse> deleteSingleMCQ = apiService.deleteSingleMCQ(token, userId, singleMCQId);

        deleteSingleMCQ.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteSingleMCQNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
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
                Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemViewType(int position) {


        if (items.get(position) instanceof FeedData) {

            Log.d(TAG, "getItemViewType: instance of feedData");

            FeedData feedData = (FeedData) items.get(position);


            if (feedData.getFeedViewType() == null || feedData.getFeedViewType().equals(FEED_VIEW_TYPE_QXM)) {
                if (feedData.getFeedCreatorName().length() < 22) {
                    Log.d(TAG, "getItemViewType: NORMAL_FEED");
                    return NORMAL_FEED;
                } else {
                    Log.d(TAG, "getItemViewType: NORMAL_FEED_WITH_LONG_NAME ");
                    return NORMAL_FEED_WITH_LONG_NAME;
                }
            }
            if (feedData.getFeedViewType().equals(FEED_VIEW_TYPE_POLL)) {
                Log.d(TAG, "getItemViewType: POLL_FEED ");
                return POLL_FEED;
            }
            if (feedData.getFeedViewType().equals(FEED_VIEW_TYPE_SINGLE_MCQ)) {
                Log.d(TAG, "getItemViewType: SINGLE_MCQ_FEED ");
                return SINGLE_MCQ_FEED;
            }

        } else if (items.get(position) instanceof FeedDataInterested)
            return INTEREST_BASED_FEED;
        else if (items.get(position) instanceof FeedDataNotInterested)
            return NOT_INTERESTED;
        else if (items.get(position) instanceof FeedDataCauseOfNotInterested)
            return CAUSE_OF_NOT_INTERESTED;
        else if (items.get(position) instanceof HiddenFeedData)
            return HIDE_FEED_ITEM;


        return NORMAL_FEED;

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "FeedAdapter getItemCount: " + items.size());
        return items.size();
    }


    // region Clear
    public void clear() {
        // Clean all elements of the recycler
        items.clear();
        notifyDataSetChanged();

    }

    // endregion

    // region interface-FeedAdapterListener
    public interface FeedAdapterListener {
        void onItemSelected(String userId, String qxmId, String qxmTitle);

        void onSingleMCQItemSelected(String userId, String singleMCQId, String singleMCQTitle, boolean isParticipated);
    }
    // endregion

    // region Class-NormalFeedViewHolder
    public class NormalFeedViewHolder extends RecyclerView.ViewHolder {

        FrameLayout flItemContainer;
        LinearLayoutCompat llSingleFeedItem;
        FrameLayout flFeedThumbnailContainer;
        CardView CVContest;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatTextView tvFeedName;
        AppCompatTextView tvFeedCreator;
        AppCompatTextView tvFeedCreationTime;
        AppCompatImageView ivFeedPrivacy;
        AppCompatImageView ivFeedThumbnail;
        AppCompatImageView ivYoutubeButton;
        AppCompatTextView tvFeedDescription;
        AppCompatTextView tvParticipantsQuantity;
        AppCompatImageView ivIgniteQuantity;
        AppCompatTextView tvIgniteQuantity;
        AppCompatTextView tvShareQuantity;
        RelativeLayout rlFeedEventsInfo;
        LinearLayoutCompat llcIgnite;
        LinearLayoutCompat llcShare;
        LinearLayoutCompat llParticipate;
        View viewParticipateBtnSeparator;
        AppCompatImageView ivParticipate;
        AppCompatImageView ivIgnite;
        AppCompatImageView ivShare;
        AppCompatTextView tvIgnite;
        AppCompatTextView tvShare;
        AppCompatTextView tvParticipate;
        AppCompatTextView tvEdited;
        AppCompatTextView tvFeedThumbnail;


        NormalFeedViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                Log.d(TAG, "NormalFeedViewHolder: ");
                FeedData feedData = (FeedData) (items.get(getAdapterPosition()));
                feedAdapterListener.onItemSelected(feedData.getFeedCreatorId(), feedData.getFeedQxmId(), feedData.getFeedName());
                Log.d(TAG, "NormalFeedViewHolder: " + feedData.getFeedQxmId());
            });
            flItemContainer = itemView.findViewById(R.id.flItemContainer);
            llSingleFeedItem = itemView.findViewById(R.id.llSingleFeedItem);
            flFeedThumbnailContainer = itemView.findViewById(R.id.FLFeedThumbnailContainer);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            tvFeedName = itemView.findViewById(R.id.tvFeedName);
            tvFeedCreator = itemView.findViewById(R.id.tvFeedCreator);
            tvFeedCreationTime = itemView.findViewById(R.id.tvFeedCreationTime);
            ivFeedPrivacy = itemView.findViewById(R.id.ivFeedPrivacy);
            ivFeedThumbnail = itemView.findViewById(R.id.ivFeedThumbnail);
            ivYoutubeButton = itemView.findViewById(R.id.ivYoutubeButton);
            tvFeedDescription = itemView.findViewById(R.id.tvFeedDescription);
            tvParticipantsQuantity = itemView.findViewById(R.id.tvParticipantsQuantity);
            ivIgniteQuantity = itemView.findViewById(R.id.ivIgniteQuantity);
            tvIgniteQuantity = itemView.findViewById(R.id.tvIgniteQuantity);
            tvShareQuantity = itemView.findViewById(R.id.tvShareQuantity);
            rlFeedEventsInfo = itemView.findViewById(R.id.rlFeedEventsInfo);
            llcIgnite = itemView.findViewById(R.id.llcIgnite);
            llcShare = itemView.findViewById(R.id.llcShare);
            llParticipate = itemView.findViewById(R.id.llParticipate);
            viewParticipateBtnSeparator = itemView.findViewById(R.id.viewParticipateBtnSeparator);
            ivParticipate = itemView.findViewById(R.id.ivParticipate);
            ivIgnite = itemView.findViewById(R.id.ivIgnite);
            ivShare = itemView.findViewById(R.id.ivShare);
            tvIgnite = itemView.findViewById(R.id.tvIgnite);
            tvShare = itemView.findViewById(R.id.tvShare);
            tvParticipate = itemView.findViewById(R.id.tvParticipate);
            tvEdited = itemView.findViewById(R.id.tvEdited);
//            tvFeedThumbnail = itemView.findViewById(R.id.tvFeedThumbnail);


        }
    }
    // endregion

    // region Class-PollFeedViewHolder

    class PollFeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llSingleFeedItem)
        LinearLayoutCompat llSingleFeedItem;
        @BindView(R.id.FLFeedThumbnailContainer)
        FrameLayout FLFeedThumbnailContainer;
        @BindView(R.id.tvFeedPollTitle)
        AppCompatTextView tvFeedPollTitle;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.ivPollFeedOptions)
        AppCompatImageView ivPollFeedOptions;
        @BindView(R.id.tvPollCreatorFullName)
        AppCompatTextView tvPollCreatorName;
        @BindView(R.id.tvPollType)
        AppCompatTextView tvPollType;
        @BindView(R.id.tvPollCreationTime)
        AppCompatTextView tvPollCreationTime;
        @BindView(R.id.ivPollPrivacy)
        AppCompatImageView ivPollPrivacy;
        @BindView(R.id.tvPollTitle)
        AppCompatTextView tvPollTitle;
        @BindView(R.id.ivPollThumbnail)
        AppCompatImageView ivPollThumbnail;
        @BindView(R.id.rvPollOption)
        RecyclerView rvPollOption;
        @BindView(R.id.ivYoutubeButton)
        AppCompatImageView ivYoutubeButton;
        @BindView(R.id.tvParticipantsQuantity)
        AppCompatTextView tvParticipantsQuantity;
        @BindView(R.id.ivIgniteQuantity)
        AppCompatImageView ivIgniteQuantity;
        @BindView(R.id.tvIgniteQuantity)
        AppCompatTextView tvIgniteQuantity;
        @BindView(R.id.tvShareQuantity)
        AppCompatTextView tvShareQuantity;
        @BindView(R.id.rlPollEventsInfo)
        RelativeLayout rlPollEventsInfo;
        @BindView(R.id.llcIgnite)
        LinearLayoutCompat llcIgnite;
        @BindView(R.id.llcShare)
        LinearLayoutCompat llcShare;
        @BindView(R.id.ivIgnite)
        AppCompatImageView ivIgnite;
        @BindView(R.id.ivShare)
        AppCompatImageView ivShare;
        @BindView(R.id.tvIgnite)
        AppCompatTextView tvIgnite;
        @BindView(R.id.tvShare)
        AppCompatTextView tvShare;


        PollFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // endregion

    // region Class-SingleMCQFeedViewHolder

    class SingleMCQFeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llSingleFeedItem)
        LinearLayoutCompat llSingleFeedItem;
        @BindView(R.id.FLFeedThumbnailContainer)
        FrameLayout FLFeedThumbnailContainer;
        @BindView(R.id.tvSingleMCQFeedTitle)
        AppCompatTextView tvSingleMCQFeedTitle;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.ivSingleMCQFeedOptions)
        AppCompatImageView ivSingleMCQFeedOptions;
        @BindView(R.id.tvSingleMCQCreatorFullName)
        AppCompatTextView tvSingleMCQCreatorFullName;
        @BindView(R.id.tvSingleMCQCreationTime)
        AppCompatTextView tvSingleMCQCreationTime;
        @BindView(R.id.ivSingleMCQPrivacy)
        AppCompatImageView ivSingleMCQPrivacy;
        @BindView(R.id.tvSingleMCQTitle)
        AppCompatTextView tvSingleMCQTitle;
        @BindView(R.id.ivSingleMCQThumbnail)
        AppCompatImageView ivSingleMCQThumbnail;
        @BindView(R.id.rvSingleMCQOptions)
        RecyclerView rvSingleMCQOptions;
        @BindView(R.id.ivYoutubeButton)
        AppCompatImageView ivYoutubeButton;
        @BindView(R.id.tvParticipantsQuantity)
        AppCompatTextView tvParticipantsQuantity;
        @BindView(R.id.ivIgniteQuantity)
        AppCompatImageView ivIgniteQuantity;
        @BindView(R.id.tvIgniteQuantity)
        AppCompatTextView tvIgniteQuantity;
        @BindView(R.id.tvShareQuantity)
        AppCompatTextView tvShareQuantity;
        @BindView(R.id.rlSingleMCQEventsInfo)
        RelativeLayout rlSingleMCQEventsInfo;
        @BindView(R.id.llcIgnite)
        LinearLayoutCompat llcIgnite;
        @BindView(R.id.llcShare)
        LinearLayoutCompat llcShare;
        @BindView(R.id.ivIgnite)
        AppCompatImageView ivIgnite;
        @BindView(R.id.ivShare)
        AppCompatImageView ivShare;
        @BindView(R.id.tvIgnite)
        AppCompatTextView tvIgnite;
        @BindView(R.id.tvShare)
        AppCompatTextView tvShare;
        @BindView(R.id.llParticipate)
        LinearLayoutCompat llParticipate;
        @BindView(R.id.ivParticipate)
        AppCompatImageView ivParticipate;
        @BindView(R.id.tvParticipate)
        AppCompatTextView tvParticipate;
        @BindView(R.id.viewParticipateBtnSeparator)
        View viewParticipateBtnSeparator;
        @BindView(R.id.CVContest)
        CardView CVContest;


        SingleMCQFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "SingleMCQFeedViewHolder: clicked");
                FeedData feedData = (FeedData) (items.get(getAdapterPosition()));
                feedAdapterListener.onSingleMCQItemSelected(feedData.getFeedCreatorId(), feedData.getFeedQxmId(), feedData.getFeedName(), feedData.isParticipated());
                Log.d(TAG, "SingleMCQFeedViewHolder: singleMCQId: " + feedData.getFeedQxmId());
            });
        }
    }

    // endregion

    // region Class-InterestedFeedViewHolder
    public class InterestedFeedViewHolder extends RecyclerView.ViewHolder {

        TextView interestedTV;
        RecyclerView interestedQxmRV;

        InterestedFeedViewHolder(View itemView) {
            super(itemView);
            interestedTV = itemView.findViewById(R.id.interestedTV);
            interestedQxmRV = itemView.findViewById(R.id.interestedQxmRV);

        }
    }

    // endregion

    // region Class-NotInterestedItemViewHolder

    class NotInterestedItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUndo)
        AppCompatTextView tvUndo;
        @BindView(R.id.tvTellUsWhy)
        AppCompatTextView tvTellUsWhy;


        NotInterestedItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // endregion

    // region class-CauseOfNotInterestedItemViewHolder

    class CauseOfNotInterestedItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivClose)
        AppCompatImageView ivClose;
        @BindView(R.id.rgTellUsWhy)
        RadioGroup rgTellUsWhy;
        @BindView(R.id.rbAlreadyParticipated)
        AppCompatRadioButton rbAlreadyParticipated;
        @BindView(R.id.rbDoNotLikeThisQxm)
        AppCompatRadioButton rbDoNotLikeThisQxm;
        @BindView(R.id.rbNotInterested)
        AppCompatRadioButton rbNotInterested;

        CauseOfNotInterestedItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // endregion

    //region Class-HideFeedItemViewHolder
    class HideFeedItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQxmRemoved)
        AppCompatTextView tvQxmRemoved;
        @BindView(R.id.tvUndo)
        AppCompatTextView tvUndo;

        HideFeedItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //endregion

    /* Within the RecyclerView.Adapter class */

    // region Class-Interest based feed item adapter
    public class FeedItemsInterestedAdapter extends RecyclerView.Adapter<FeedItemsInterestedAdapter.InterestedFeedViewHolder> {

        private Context context;
        private List<FeedData> feedDataListInterested;

        FeedItemsInterestedAdapter(Context context, List<FeedData> feedDataListInterested) {

            this.context = context;
            this.feedDataListInterested = feedDataListInterested;

        }

        @NonNull
        @Override
        public FeedItemsInterestedAdapter.InterestedFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View interestedFeedView = inflater.inflate(R.layout.feed_single_interest_based_qxm_row, parent, false);
            return new FeedItemsInterestedAdapter.InterestedFeedViewHolder(interestedFeedView);

        }

        @Override
        public void onBindViewHolder(@NonNull FeedItemsInterestedAdapter.InterestedFeedViewHolder holder, int position) {

            FeedData feedDataInterested = feedDataListInterested.get(position);

            // setting interested data
            //Picasso.with(context).load(feedDataInterested.getQxmCreatorImage()).transform(new CircleTransform()).into(holder.qxmMakerIV);
            holder.tvFeedName.setText(feedDataInterested.getFeedName());

            holder.tvParticipantQuantity.setText(String.format("%s Participants", feedDataInterested.getFeedParticipantsQuantity()));

            holder.RRMainContent.setOnClickListener(v -> Toast.makeText(context, "Clicked Main Content", Toast.LENGTH_SHORT).show());

            holder.tvOptions.setOnClickListener(v -> Toast.makeText(context, "Clicked three dot menu", Toast.LENGTH_SHORT).show());

        }

        @Override
        public int getItemCount() {
            return feedDataListInterested.size();
        }


        class InterestedFeedViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout RRMainContent;
            AppCompatImageView ivFeedCreator;
            AppCompatTextView tvFeedName;
            TextView tvFeedCreationTime;
            TextView tvParticipantQuantity;
            ImageView tvOptions;


            InterestedFeedViewHolder(View itemView) {
                super(itemView);
                RRMainContent = itemView.findViewById(R.id.RRMainContent);
                ivFeedCreator = itemView.findViewById(R.id.ivFeedCreator);
                tvFeedName = itemView.findViewById(R.id.tvFeedName);
                tvFeedCreationTime = itemView.findViewById(R.id.tvFeedCreationTime);
                tvParticipantQuantity = itemView.findViewById(R.id.tvParticipantQuantity);
                tvOptions = itemView.findViewById(R.id.tvOptions);

            }
        }
    }

    // endregion

// Add a list of items -- change to type used

//    public void addAll(FeedDataInterested feedDataInterested, FeedDataWithUserImage feedDataWithUserImage) {
//
//        items.add(feedDataInterested);
//        items.add(feedDataWithUserImage.getFeedDataArrayList());
//        notifyDataSetChanged();
//
//    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {

            String uri = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));

            containedUrls.add(uri);
        }

        return containedUrls;
    }


}
