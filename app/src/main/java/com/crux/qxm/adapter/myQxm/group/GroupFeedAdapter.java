package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.FeedPollOptionAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.feed.FeedDataCauseOfNotInterested;
import com.crux.qxm.db.models.feed.FeedDataInterested;
import com.crux.qxm.db.models.feed.FeedDataNotInterested;
import com.crux.qxm.db.models.feed.HiddenFeedData;
import com.crux.qxm.db.models.feed.feedDataWithShared.FeedInGroupItem;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_POLL;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_QXM;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_ADMIN;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.REPORT_POLL;
import static com.crux.qxm.utils.StaticValues.REPORT_QXM;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_POLL;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;


public class GroupFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //region GroupFeedAdapter-Properties
    private static final String TAG = "GroupFeedAdapter";
    public static String qxmId;
    private final int NORMAL_FEED = 1;
    private final int INTEREST_BASED_FEED = 2;
    private final int NOT_INTERESTED = 3;
    private final int CAUSE_OF_NOT_INTERESTED = 4;
    private final int POLL_FEED = 5;
    private final int HIDE_POLL = 6;
    private final int SHARED_NORMAL_FEED = 7;
    private final int SHARED_NORMAL_FEED_WITH_LONG_NAME = 8;
    public AppCompatDialog dialog;
    private Context context;
    private List<Object> items;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private String userId;
    private String token;
    private QxmApiService apiService;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private GroupFeedAdapterListener groupFeedAdapterListener;
    private String groupId;
    private String memberStatus;
    //endregion

    //region GroupFeedAdapter-Constructor
    public GroupFeedAdapter(Context context, String groupId, List<Object> items, FragmentActivity fragmentActivity, Picasso picasso, String userId, String token, QxmApiService apiService, GroupFeedAdapterListener groupFeedAdapterListener) {
        this.context = context;
        this.groupId = groupId;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.userId = userId;
        this.token = token;
        this.apiService = apiService;
        this.groupFeedAdapterListener = groupFeedAdapterListener;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }
    //endregion

    //region Override-onCreateViewHolder
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

            case SHARED_NORMAL_FEED:
                View sharedNormalFeedView = layoutInflater.inflate(R.layout.feed_single_item_with_shared_ui, parent, false);
                viewHolder = new SharedNormalFeedViewHolder(sharedNormalFeedView);
                return viewHolder;

            case SHARED_NORMAL_FEED_WITH_LONG_NAME:
                View sharedNormalFeedWithLongNameView = layoutInflater.inflate(R.layout.feed_single_item_with_shared_ui_for_long_name, parent, false);
                viewHolder = new SharedNormalFeedViewHolder(sharedNormalFeedWithLongNameView);
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

            case HIDE_POLL:
                View hidePollView = layoutInflater.inflate(R.layout.feed_single_item_hide_feed_item, parent, false);
                viewHolder = new HidePollViewHolder(hidePollView);
                return viewHolder;

            default:
                normalFeedView = layoutInflater.inflate(R.layout.feed_single_item, parent, false);
                viewHolder = new NormalFeedViewHolder(normalFeedView);
                return viewHolder;

        }

    }
    //endregion

    //region Override-onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int igniteCount1;
        int shareCount;
        switch (holder.getItemViewType()) {
            case SHARED_NORMAL_FEED_WITH_LONG_NAME:
            case SHARED_NORMAL_FEED:


                // region SHARED_NORMAL_FEED

                SharedNormalFeedViewHolder sharedNormalFeedViewHolder = (SharedNormalFeedViewHolder) holder;

                FeedData feedDataSharedNormal = (FeedData) items.get(position);

                Log.d(TAG, "onBindViewHolder: SHARED NORMAL FEED: " + feedDataSharedNormal.toString());

                List<FeedInGroupItem> feedInGroupItemList = new ArrayList<>();

                for (FeedInGroupItem item : feedDataSharedNormal.getFeedInGroup()) {
                    if(item.getGroupId().equals(groupId)){
                        feedInGroupItemList.add(item);
                    }
                }

                FeedInGroupItem feedInGroupItem = feedInGroupItemList.get(feedInGroupItemList.size() - 1);


                picasso.load(feedInGroupItem.getAddedBy().getModifiedProfileImage()).error(context.getResources().getDrawable(
                        R.drawable.ic_user_default)).into(sharedNormalFeedViewHolder.ivSharedUserImage);


                String sharedByFullName = String.format("<font color='%s'>%s</font><font color='%s'> shared</font>",
                        context.getResources().getString(R.string.text_primary_color),
                        feedInGroupItem.getAddedBy().getFullName(),
                        context.getResources().getString(R.string.text_secondary_color));

                sharedNormalFeedViewHolder.tvSharedBy.setText(Html.fromHtml(sharedByFullName), TextView.BufferType.SPANNABLE);

                //region SharingAQxmTime-tvSharingTime
                Collections.reverse(feedInGroupItemList);
                StringBuilder sharingTime  = new StringBuilder();

                for(int i =0; i < feedInGroupItemList.size(); i++ ){
                    if (QxmStringIntegerChecker.isLongInt(feedInGroupItemList.get(0).getAddedBy().getSharingTime())) {

                        long feedSharingTime = Long.parseLong(feedInGroupItem.getAddedBy().getSharingTime());

                        if(i != 0) sharingTime.append(", ");
                        sharingTime.append(TimeAgo.using(feedSharingTime));

                    }
                }

                if(!TextUtils.isEmpty(sharingTime.toString())){
                    sharedNormalFeedViewHolder.tvSharingTime.setText(sharingTime.toString());
                }else{
                    sharedNormalFeedViewHolder.tvSharingTime.setVisibility(View.GONE);
                }

                /*if (QxmStringIntegerChecker.isLongInt(feedInGroupItem.getAddedBy().getSharingTime())) {

                    long feedSharingTime = Long.parseLong(feedInGroupItem.getAddedBy().getSharingTime());
                    String feedSharingTimeAgo = TimeAgo.using(feedSharingTime);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedSharingTimeAgo);
                    sharedNormalFeedViewHolder.tvSharingTime.setText(feedSharingTimeAgo);
                } else {
                    sharedNormalFeedViewHolder.tvSharingTime.setVisibility(View.GONE);
                }*/
                //endregion


                if (!TextUtils.isEmpty(feedDataSharedNormal.getFeedCreatorImageURL())) {

                    picasso.load(feedDataSharedNormal.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(sharedNormalFeedViewHolder.ivUserImage);

                } else {
                    sharedNormalFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                sharedNormalFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                feedDataSharedNormal.getFeedCreatorId(),
                                feedDataSharedNormal.getFeedCreatorName())
                );

                // show contest text view if contest mode is on
                if (feedDataSharedNormal.isContestModeOn())
                    sharedNormalFeedViewHolder.CVContest.setVisibility(View.VISIBLE);
                else sharedNormalFeedViewHolder.CVContest.setVisibility(View.GONE);

                // show thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(feedDataSharedNormal.getFeedThumbnailURL())) {
                    sharedNormalFeedViewHolder.ivFeedThumbnail.setVisibility(View.VISIBLE);
//                    sharedNormalFeedViewHolder.tvFeedThumbnail.setVisibility(View.GONE);
                    picasso.load(feedDataSharedNormal.getFeedThumbnailURL()).into(sharedNormalFeedViewHolder.ivFeedThumbnail);
                } else {
                    //sharedNormalFeedViewHolder.flFeedThumbnailContainer.setVisibility(View.GONE);
                    sharedNormalFeedViewHolder.ivFeedThumbnail.setVisibility(View.GONE);
//                    sharedNormalFeedViewHolder.tvFeedThumbnail.setVisibility(View.VISIBLE);
//                    sharedNormalFeedViewHolder.tvFeedThumbnail.setText(feedDataSharedNormal.getFeedName());
                }

                if (!TextUtils.isEmpty(feedDataSharedNormal.getFeedYoutubeLinkURL())) {
                    sharedNormalFeedViewHolder.ivYoutubeButton.setVisibility(View.VISIBLE);
                } else {
                    sharedNormalFeedViewHolder.ivYoutubeButton.setVisibility(View.GONE);
                }

                //show privacy icon based on privacy
                if (feedDataSharedNormal.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
                    sharedNormalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
                else
                    sharedNormalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));

                sharedNormalFeedViewHolder.tvFeedName.setText(feedDataSharedNormal.getFeedName());
                sharedNormalFeedViewHolder.tvFeedCreator.setText(feedDataSharedNormal.getFeedCreatorName());
                if (feedDataSharedNormal.getFeedParticipantsQuantity() != null) {
                    sharedNormalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", feedDataSharedNormal.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));
                    if (feedDataSharedNormal.getFeedParticipantsQuantity().equals("0")) {
                        sharedNormalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                    }
                } else {
                    sharedNormalFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participants)));
                }

                // show feed item time ago
                if (QxmStringIntegerChecker.isLongInt(feedDataSharedNormal.getFeedCreationTime())) {

                    long feedCreationTime = Long.parseLong(feedDataSharedNormal.getFeedCreationTime());
                    String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                    sharedNormalFeedViewHolder.tvFeedCreationTime.setText(feedPostTimeAgo);
                } else {
                    Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    sharedNormalFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
                }
                sharedNormalFeedViewHolder.tvFeedDescription.setText(feedDataSharedNormal.getFeedDescription());

                SetlinkClickable.setLinkclickEvent(sharedNormalFeedViewHolder.tvFeedDescription, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, url);
                        context.startActivity(intent);
                    }
                });


                sharedNormalFeedViewHolder.tvFeedCreator.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(feedDataSharedNormal.getFeedCreatorId(),
                                feedDataSharedNormal.getFeedCreatorName()));

                sharedNormalFeedViewHolder.ivYoutubeButton.setOnClickListener(v -> {

                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, feedDataSharedNormal.getFeedYoutubeLinkURL());
                    context.startActivity(intent);


                });


                // IgniteCount
                if (TextUtils.isEmpty(feedDataSharedNormal.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    feedDataSharedNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(feedDataSharedNormal.getFeedIgniteQuantity());
                    feedDataSharedNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!feedDataSharedNormal.getFeedIgniteQuantity().equals("0")) {
                    sharedNormalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.icon_color));
                    sharedNormalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                }
                sharedNormalFeedViewHolder.tvIgniteQuantity.setText(feedDataSharedNormal.getFeedIgniteQuantity());

                // ShareCount
                if (TextUtils.isEmpty(feedDataSharedNormal.getFeedShareQuantity())) {
                    shareCount = 0;
                    feedDataSharedNormal.setFeedShareQuantity(String.valueOf(shareCount));
                } else {
                    shareCount = Integer.parseInt(feedDataSharedNormal.getFeedShareQuantity());
                    feedDataSharedNormal.setFeedShareQuantity(String.valueOf(shareCount));
                }
                sharedNormalFeedViewHolder.tvShareQuantity.setText(feedDataSharedNormal.getFeedShareQuantity());


                // region feed main content onclick

                sharedNormalFeedViewHolder.llSingleFeedItem.setOnClickListener(v -> {
                   /* Toast.makeText(context, "Clicked Normal feed Main Content", Toast.LENGTH_SHORT).show();


                    String positionn = String.valueOf(position);
                    Log.d("Position",positionn);

                    feedDataToPass = feedDataSharedNormal;
                    qxmId = feedDataSharedNormal.getFeedQxmId();

                    Log.d("FeedData",feedDataSharedNormal.toString());
                    //Log.d("FeedDataToPass",feedDataToPass.toString());

                    Bundle args = new Bundle();
                    args.putParcelable(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, feedDataSharedNormal);
                    QuestionOverViewFragment questionOverViewFragment =  new QuestionOverViewFragment();
                    questionOverViewFragment.setArguments(args);


                    fragmentManager = fragmentActivity.getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.frame_container,questionOverViewFragment,"questionOverViewFragment");
                    fragmentTransaction.addToBackStack("questionOverViewFragment");
                    fragmentTransaction.commit();*/
                });

                sharedNormalFeedViewHolder.tvFeedName.setOnClickListener(v -> {

                    String qxmId = feedDataSharedNormal.getFeedQxmId();
                    Log.d("FeedData", feedDataSharedNormal.toString());
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                });

                sharedNormalFeedViewHolder.ivFeedThumbnail.setOnClickListener(v -> {

                    if (!TextUtils.isEmpty(feedDataSharedNormal.getFeedYoutubeLinkURL())) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, feedDataSharedNormal.getFeedYoutubeLinkURL());
                        context.startActivity(intent);
                    } else {
                        String qxmId = feedDataSharedNormal.getFeedQxmId();
                        Log.d("FeedData", feedDataSharedNormal.toString());
                        Bundle args = new Bundle();
                        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

                        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    }

                });



                /*sharedNormalFeedViewHolder.tvFeedThumbnail.setOnClickListener(v -> {

                    String qxmId = feedDataSharedNormal.getFeedQxmId();
                    Log.d("FeedData", feedDataSharedNormal.toString());
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);


                });*/

                // endregion

                // region feed menu item onclick

                sharedNormalFeedViewHolder.ivFeedOptions.setOnClickListener(v -> {


                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    if (!TextUtils.isEmpty(memberStatus) && memberStatus.equals(MEMBER_STATUS_ADMIN)) {
                        Log.d(TAG, "onBindViewHolder: memberStatus: " + memberStatus);
                        inflater.inflate(R.menu.menu_group_feed_admin, popup.getMenu());

                    } else {
                        Log.d(TAG, "onBindViewHolder: memberStatus: " + memberStatus);
                        inflater.inflate(R.menu.menu_group_feed_member, popup.getMenu());
                    }

                    popup.setOnMenuItemClickListener(item -> {

                        switch (item.getItemId()) {

                            //region Action_Not_Interested
                            /*case R.id.action_not_interested:

                                FeedDataNotInterested notInterested = new FeedDataNotInterested();
                                FeedDataCauseOfNotInterested causeOfNotInterested = new FeedDataCauseOfNotInterested();
                                causeOfNotInterested.setFeedId(feedDataSharedNormal.getFeedId());
                                causeOfNotInterested.setFeedCreatorId(feedDataSharedNormal.getFeedCreatorId());
                                causeOfNotInterested.setFeedQxmId(feedDataSharedNormal.getFeedQxmId());
                                causeOfNotInterested.setNotInterestedCause("");
                                causeOfNotInterested.setFeedData(feedDataSharedNormal);

                                notInterested.setCauseOfNotInterested(causeOfNotInterested);

                                items.set(position, notInterested);
                                notifyItemChanged(holder.getAdapterPosition());

                                notInterestedNetworkCall(token, feedDataSharedNormal.getFeedQxmId(), userId);

                                break;*/
                            //endregion

                            case R.id.action_share_to_group:

                                QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                                        (fragmentActivity, apiService, context, picasso, userId, token);
                                qxmShareContentToGroup.shareToGroup
                                        (feedDataSharedNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                                break;

                            case R.id.action_share_link:
                                QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                                QxmUrlHelper urlHelper = new QxmUrlHelper();
                                qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(feedDataSharedNormal.getFeedQxmId()));

                                break;

                            case R.id.action_add_to_list:
                                QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, userId, token);
                                addQxmToList.addQxmToList(feedDataSharedNormal.getFeedQxmId(), feedDataSharedNormal.getFeedName());

                                break;

                            case R.id.action_report_qxm:

                                QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token, userId, feedDataSharedNormal.getFeedQxmId());
                                qxmReportDialog.showReportDialog(REPORT_QXM);
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
                                            deleteQxmFromGroupFeedNetworkCall(token, groupId, feedDataSharedNormal.getFeedId());
                                        }
                                );

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
                if (feedDataSharedNormal.isIgnitedClicked()) {

                    String ignited = "Ignited";
                    sharedNormalFeedViewHolder.tvIgnite.setText(ignited);
                    sharedNormalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    sharedNormalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }

                sharedNormalFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!sharedNormalFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        sharedNormalFeedViewHolder.tvIgnite.setText(ignited);
                        sharedNormalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        sharedNormalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        igniteQxmNetworkCall(token, userId, feedDataSharedNormal.getFeedQxmId());
                        int igniteCount = Integer.parseInt(feedDataSharedNormal.getFeedIgniteQuantity());
                        igniteCount++;
                        feedDataSharedNormal.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        sharedNormalFeedViewHolder.tvIgniteQuantity.setText(feedDataSharedNormal.getFeedIgniteQuantity());
                        sharedNormalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        sharedNormalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                    } else {
                        String ignite = "Ignite";
                        sharedNormalFeedViewHolder.tvIgnite.setText(ignite);
                        sharedNormalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                        sharedNormalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                        igniteQxmNetworkCall(token, userId, feedDataSharedNormal.getFeedQxmId());
                        int igniteCount = Integer.parseInt(feedDataSharedNormal.getFeedIgniteQuantity());

                        if (igniteCount > 0)
                            igniteCount--;
                        else
                            igniteCount = 0;

                        feedDataSharedNormal.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        sharedNormalFeedViewHolder.tvIgniteQuantity.setText(feedDataSharedNormal.getFeedIgniteQuantity());
                        sharedNormalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);

                        if (igniteCount <= 0) {

                            sharedNormalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                            sharedNormalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                        }
                    }

                });


                sharedNormalFeedViewHolder.llcShare.setOnClickListener(v -> {
                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    if (!sharedNormalFeedViewHolder.tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                        qxmShareContentToGroup.shareToGroup
                                (feedDataSharedNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                    } else {

                        qxmShareContentToGroup.shareToGroup
                                (feedDataSharedNormal.getFeedQxmId(), SHARED_CATEGORY_QXM);

                        if (qxmShareContentToGroup.sharedSuccess) {
                            qxmShareContentToGroup.sharedSuccess = false;
                            sharedNormalFeedViewHolder.tvShare.setText(context.getString(R.string.shared));
                            sharedNormalFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            sharedNormalFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }


                    }


                });

                if (feedDataSharedNormal.getFeedCreatorId().equals(userId)) {
                    sharedNormalFeedViewHolder.tvParticipate.setText(R.string.see_details);
                    sharedNormalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_file_document));

                } else {
                    sharedNormalFeedViewHolder.tvParticipate.setText(R.string.participate);
                    sharedNormalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_participate_hand));
                }

                sharedNormalFeedViewHolder.llParticipate.setOnClickListener(v ->
                        groupFeedAdapterListener.onItemSelected(feedDataSharedNormal.getFeedCreatorId(), feedDataSharedNormal.getFeedQxmId())
                );

                //endregion


                // endregion

                break;
            case NORMAL_FEED:

                // region NORMAL_FEED

                NormalFeedViewHolder normalFeedViewHolder = (NormalFeedViewHolder) holder;

                FeedData feedDataNormal = (FeedData) items.get(position);

                Log.d(TAG, "onBindViewHolder: Normal Feed: " + feedDataNormal.toString());

                if (!TextUtils.isEmpty(feedDataNormal.getFeedCreatorImageURL())) {

                    picasso.load(feedDataNormal.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(normalFeedViewHolder.ivUserImage);

                } else {
                    normalFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                normalFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                feedDataNormal.getFeedCreatorId(),
                                feedDataNormal.getFeedCreatorName())
                );

                // show contest text view if contest mode is on
                if (feedDataNormal.isContestModeOn())
                    normalFeedViewHolder.CVContest.setVisibility(View.VISIBLE);
                else normalFeedViewHolder.CVContest.setVisibility(View.GONE);

                // show thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(feedDataNormal.getFeedThumbnailURL())) {
                    normalFeedViewHolder.ivFeedThumbnail.setVisibility(View.VISIBLE);
//                    normalFeedViewHolder.tvFeedThumbnail.setVisibility(View.GONE);
                    picasso.load(feedDataNormal.getFeedThumbnailURL()).into(normalFeedViewHolder.ivFeedThumbnail);
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

                //show privacy icon based on privacy
                if (feedDataNormal.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
                else
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));

                normalFeedViewHolder.tvFeedName.setText(feedDataNormal.getFeedName());

                normalFeedViewHolder.tvFeedCreator.setText(feedDataNormal.getFeedCreatorName());
                if (feedDataNormal.getFeedParticipantsQuantity() != null) {
                    normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", feedDataNormal.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));
                    if (feedDataNormal.getFeedParticipantsQuantity().equals("0")) {
                        normalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                    }
                } else {
                    normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participants)));
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


                // feedAdapterListenerCalls

                normalFeedViewHolder.tvFeedCreator.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(feedDataNormal.getFeedCreatorId(),
                                feedDataNormal.getFeedCreatorName()));

                normalFeedViewHolder.ivYoutubeButton.setOnClickListener(v -> {

                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, feedDataNormal.getFeedYoutubeLinkURL());
                    context.startActivity(intent);


                });


                // IgniteCount
                if (TextUtils.isEmpty(feedDataNormal.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(feedDataNormal.getFeedIgniteQuantity());
                    feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!feedDataNormal.getFeedIgniteQuantity().equals("0")) {
                    normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.icon_color));
                    normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

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

                normalFeedViewHolder.tvFeedName.setOnClickListener(v -> {

                    String qxmId = feedDataNormal.getFeedQxmId();
                    Log.d("FeedData", feedDataNormal.toString());
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                });

                normalFeedViewHolder.ivFeedThumbnail.setOnClickListener(v -> {

                    if (!TextUtils.isEmpty(feedDataNormal.getFeedYoutubeLinkURL())) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, feedDataNormal.getFeedYoutubeLinkURL());
                        context.startActivity(intent);
                    } else {
                        String qxmId = feedDataNormal.getFeedQxmId();
                        Log.d("FeedData", feedDataNormal.toString());
                        Bundle args = new Bundle();
                        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

                        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    }

                });

                // endregion

                // region feed menu item onclick

                normalFeedViewHolder.ivFeedOptions.setOnClickListener(v -> {

                    //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();

                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_feed, popup.getMenu());
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
                    normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }

                normalFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!normalFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        normalFeedViewHolder.tvIgnite.setText(ignited);
                        normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        igniteQxmNetworkCall(token, userId, feedDataNormal.getFeedQxmId());
                        int igniteCount = Integer.parseInt(feedDataNormal.getFeedIgniteQuantity());
                        igniteCount++;
                        feedDataNormal.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        normalFeedViewHolder.tvIgniteQuantity.setText(feedDataNormal.getFeedIgniteQuantity());
                        normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
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
                        normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);

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
                                (feedDataNormal.getFeedQxmId(), feedDataNormal.getFeedName());

                        if (qxmShareContentToGroup.sharedSuccess) {
                            qxmShareContentToGroup.sharedSuccess = false;
                            normalFeedViewHolder.tvShare.setText(context.getString(R.string.shared));
                            normalFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            normalFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }


                    }


                });

                normalFeedViewHolder.llParticipate.setOnClickListener(v ->
                        groupFeedAdapterListener.onItemSelected(feedDataNormal.getFeedCreatorId(), feedDataNormal.getFeedQxmId())
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

                // Poll creator image
                if (!TextUtils.isEmpty(pollFeedData.getFeedCreatorImageURL())) {
                    picasso.load(pollFeedData.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(pollFeedViewHolder.ivUserImage);
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

                // Todo:: define poll type
                // pollFeedViewHolder.tvPollType.setText();

                // region poll-body


                // poll title
                Log.d(TAG, "onBindViewHolder: poll title: " + pollFeedData.getFeedName());
                pollFeedViewHolder.tvPollTitle.setText(pollFeedData.getFeedName());

                //show privacy icon based on privacy
                if (pollFeedData.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
                    pollFeedViewHolder.ivPollPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
                else
                    pollFeedViewHolder.ivPollPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));


                // region show poll thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(pollFeedData.getFeedThumbnailURL())) {
                    pollFeedViewHolder.ivPollThumbnail.setVisibility(View.VISIBLE);
                    picasso.load(pollFeedData.getFeedThumbnailURL()).into(pollFeedViewHolder.ivPollThumbnail);
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

                if (pollFeedData.getFeedParticipantsQuantity() != null) {
                    if (pollFeedData.getFeedParticipantsQuantity().equals("0")) {
                        pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));
                        pollFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                    } else {
                        pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));
                        pollFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                } else {
                    pollFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participants)));
                    pollFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
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
                    pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.icon_color));
                    pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

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

                // region feed main content onclick

                pollFeedViewHolder.tvPollTitle.setOnClickListener(v -> {

                });

                pollFeedViewHolder.ivPollThumbnail.setOnClickListener(v -> {

                });


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
                    pollFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    pollFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
                }
                // endregion

                //region PollIgnite
                pollFeedViewHolder.llcIgnite.setOnClickListener(v -> {

                    if (!pollFeedViewHolder.tvIgnite.getText().equals("Ignited")) {
                        String ignited = "Ignited";
                        pollFeedViewHolder.tvIgnite.setText(ignited);
                        pollFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        pollFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                        ignitePollNetworkCall(userId, pollFeedData.getFeedPollId());

                        int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
                        igniteCount++;
                        pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                        pollFeedViewHolder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());
                        pollFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
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
                        pollFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);

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
                            pollFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            pollFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }
                    }
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

            case HIDE_POLL:
                // region HIDE_POLL

                Log.d(TAG, "onBindViewHolder: HIDE_POLL: " + holder.getAdapterPosition());

                HidePollViewHolder hidePollViewHolder = (HidePollViewHolder) holder;
                HiddenFeedData hiddenFeedData = (HiddenFeedData) items.get(position);

                hidePollViewHolder.tvUndo.setOnClickListener(v -> {
                    items.set(position, hiddenFeedData.getFeedData());
                    notifyItemChanged(holder.getAdapterPosition());
                });

                // endregion

                break;
        }
    }
    //endregion

    //region DeleteQxmFromGroupFeedNetworkCall
    private void deleteQxmFromGroupFeedNetworkCall(String token, String groupId, String feedId) {

        Call<QxmApiResponse> deleteAQxmInGroup = apiService.deleteAQxmInGroup(token, groupId, feedId);

        deleteAQxmInGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteQxmFromGroupFeedNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    Toast.makeText(context, R.string.network_call_qxm_delete_successfull_message, Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

            }
        });

    }
    //endregion

    //region LoadPollFeedMenuForPollCreator
    private void loadPollFeedMenuForPollCreator(View v, RecyclerView.ViewHolder holder, int position, FeedData pollFeedData) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_poll_for_creator, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {


                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    qxmShareContentToGroup.shareToGroup
                            (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.sharePollLink(urlHelper.getQxmUrl(pollFeedData.getFeedPollId()));

                    break;

                case R.id.action_hide_poll:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedData(pollFeedData);
                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: poll hide network call... implementation
                    notInterestedNetworkCall(token, pollFeedData.getFeedPollId(), userId);

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
    //endregion

    //region FeedPoolMenuForNormalUser
    private void loadPollFeedMenuForNormalUser(View v, RecyclerView.ViewHolder holder, int position, FeedData pollFeedData) {

        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_feed_poll, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {


                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, userId, token);
                    qxmShareContentToGroup.shareToGroup
                            (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.sharePollLink(urlHelper.getQxmUrl(pollFeedData.getFeedPollId()));

                    break;

                case R.id.action_hide_poll:

                    HiddenFeedData hiddenFeedData = new HiddenFeedData();
                    hiddenFeedData.setFeedData(pollFeedData);
                    items.set(position, hiddenFeedData);
                    notifyItemChanged(holder.getAdapterPosition());
                    // TODO:: poll hide network call... implementation
                    notInterestedNetworkCall(token, pollFeedData.getFeedPollId(), userId);

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
                    if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
                    if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ignitePollNetworkcall");
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

    //region Override-getItemViewType
    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof FeedData) {

            FeedData feedData = (FeedData) items.get(position);

            if (feedData.getFeedViewType() == null || feedData.getFeedViewType().equals(FEED_VIEW_TYPE_QXM)) {

                if (feedData.getFeedInGroup() != null) {
                    if (feedData.getFeedInGroup().size() > 0) {
                        if (feedData.getFeedCreatorName().length() < 22)
                            return SHARED_NORMAL_FEED;
                        else
                            return SHARED_NORMAL_FEED_WITH_LONG_NAME;

                    }
                }
                return NORMAL_FEED;
            } else if (feedData.getFeedViewType().equals(FEED_VIEW_TYPE_POLL))
                return POLL_FEED;

        } else if (items.get(position) instanceof FeedDataInterested)
            return INTEREST_BASED_FEED;
        else if (items.get(position) instanceof FeedDataNotInterested)
            return NOT_INTERESTED;
        else if (items.get(position) instanceof FeedDataCauseOfNotInterested)
            return CAUSE_OF_NOT_INTERESTED;
        else if (items.get(position) instanceof HiddenFeedData)
            return HIDE_POLL;

        return NORMAL_FEED;

    }
    //endregion

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        return items.size();
    }
    //endregion

    // region Clear
    public void clear() {
        // Clean all elements of the recycler
        items.clear();
        notifyDataSetChanged();

    }

    // endregion

    //region SetMemberStatus
    public void setMemberStatus(String memberStatus) {
        Log.d(TAG, "setMemberStatus: " + memberStatus);
        this.memberStatus = memberStatus;
    }
    //endregion

    // region NormalFeedViewHolder

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
        AppCompatImageView ivIgnite;
        AppCompatImageView ivShare;
        AppCompatTextView tvIgnite;
        AppCompatTextView tvShare;
//        AppCompatTextView tvFeedThumbnail;


        NormalFeedViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                Log.d(TAG, "NormalFeedViewHolder: ");
                FeedData feedData = (FeedData) (items.get(getAdapterPosition()));
                groupFeedAdapterListener.onItemSelected(feedData.getFeedCreatorId(), feedData.getFeedQxmId());
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
            ivIgnite = itemView.findViewById(R.id.ivIgnite);
            ivShare = itemView.findViewById(R.id.ivShare);
            tvIgnite = itemView.findViewById(R.id.tvIgnite);
            tvShare = itemView.findViewById(R.id.tvShare);
//            tvFeedThumbnail = itemView.findViewById(R.id.tvFeedThumbnail);


        }
    }
    // endregion

    // region SharedNormalFeedViewHolder

    public class SharedNormalFeedViewHolder extends RecyclerView.ViewHolder {

        // shared user info
        AppCompatImageView ivSharedUserImage;
        AppCompatTextView tvSharedBy;
        AppCompatTextView tvSharingTime;

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
        AppCompatImageView ivParticipate;
        AppCompatImageView ivIgnite;
        AppCompatImageView ivShare;
        AppCompatTextView tvIgnite;
        AppCompatTextView tvShare;
        AppCompatTextView tvParticipate;
//        AppCompatTextView tvFeedThumbnail;

        SharedNormalFeedViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                Log.d(TAG, "NormalFeedViewHolder: ");
                FeedData feedData = (FeedData) (items.get(getAdapterPosition()));
                groupFeedAdapterListener.onItemSelected(feedData.getFeedCreatorId(), feedData.getFeedQxmId());
                Log.d(TAG, "NormalFeedViewHolder: " + feedData.getFeedQxmId());
            });

            ivSharedUserImage = itemView.findViewById(R.id.ivSharedUserImage);
            tvSharedBy = itemView.findViewById(R.id.tvSharedBy);
            tvSharingTime = itemView.findViewById(R.id.tvSharingTime);
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
            ivParticipate = itemView.findViewById(R.id.ivParticipate);
            ivIgnite = itemView.findViewById(R.id.ivIgnite);
            ivShare = itemView.findViewById(R.id.ivShare);
            tvIgnite = itemView.findViewById(R.id.tvIgnite);
            tvShare = itemView.findViewById(R.id.tvShare);
            tvParticipate = itemView.findViewById(R.id.tvParticipate);
//            tvFeedThumbnail = itemView.findViewById(R.id.tvFeedThumbnail);


        }
    }
    // endregion

    // region PollFeedViewHolder

    class PollFeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llSingleFeedItem)
        LinearLayoutCompat llSingleFeedItem;
        @BindView(R.id.FLFeedThumbnailContainer)
        FrameLayout FLFeedThumbnailContainer;
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

    //region Interface-GroupFeedAdapterListener
    public interface GroupFeedAdapterListener {
        void onItemSelected(String userId, String qxmId);

    }
    //endregion

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

    //region Class-HidePollViewHolder
    class HidePollViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUndo)
        AppCompatTextView tvUndo;

        HidePollViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //endregion

    /* Within the RecyclerView.Adapter class */

    // region interest based feed item adapter
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
}
