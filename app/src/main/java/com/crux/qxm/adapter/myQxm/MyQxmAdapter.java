package com.crux.qxm.adapter.myQxm;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.FeedAdapter;
import com.crux.qxm.adapter.singleMCQAdapter.SingleMCQAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.deleteQxmNetworkCall.DeleteQxmNetworkCall;
import com.crux.qxm.networkLayer.editQxmNetworkCall.EditQxmNetworkCall;
import com.crux.qxm.networkLayer.editSingleMCQNetworkCall.EditSingleMCQNetworkCall;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAddQxmToList;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_QXM;
import static com.crux.qxm.utils.StaticValues.FEED_VIEW_TYPE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isMyQxmFeedSingleMCQItemClicked;

public class MyQxmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region MyQxmAdapter-Properties
    private static final String TAG = "MyQxmAdapter";
    public static String qxmId;
    private final int NORMAL_FEED = 1;
    private final int SINGLE_MCQ_FEED = 2;
    private final int NORMAL_FEED_WITH_LONG_NAME = 3;
    private Context context;
    private List<FeedData> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    private MyQxmAdapterListener myQxmAdapterListener;
    //endregion

    //region MyQxmAdapter-Constructor
    public MyQxmAdapter(Context context, List<FeedData> items, FragmentActivity fragmentActivity, Picasso picasso, QxmApiService apiService, QxmToken token, MyQxmAdapterListener myQxmAdapterListener) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
        this.apiService = apiService;
        this.token = token;
        this.myQxmAdapterListener = myQxmAdapterListener;
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

            case NORMAL_FEED_WITH_LONG_NAME:
                Log.d(TAG, "onCreateViewHolder: NORMAL_FEED_WITH_LONG_NAME");
                View normalFeedWithLongNameView = layoutInflater.inflate(R.layout.feed_single_item_for_long_name, parent, false);
                viewHolder = new MyQxmAdapter.ViewHolder(normalFeedWithLongNameView);
                return viewHolder;
            case SINGLE_MCQ_FEED:
                View singleMCQFeedView = layoutInflater.inflate(R.layout.feed_single_mcq_single_item, parent, false);
                viewHolder = new MyQxmAdapter.SingleMCQFeedViewHolder(singleMCQFeedView);
                return viewHolder;

            default:
                View normalFeedView = layoutInflater.inflate(R.layout.feed_single_item, parent, false);
                viewHolder = new MyQxmAdapter.ViewHolder(normalFeedView);
                return viewHolder;
        }


    }
    //endregion

    //region Override-onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            //region NORMAL_FEED
            case NORMAL_FEED_WITH_LONG_NAME:
            case NORMAL_FEED:

                ViewHolder normalFeedViewHolder = (ViewHolder) holder;

                FeedData myQxmFeedData = items.get(position);

                Log.d(TAG, "onBindViewHolder: Normal Feed: " + myQxmFeedData.toString());

                //changing text of participate button

                if (myQxmFeedData.getFeedCreatorId().equals(token.getUserId())) {

                    normalFeedViewHolder.tvParticipate.setText(R.string.see_details);
                    normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));
                    normalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_file_document));

                }


                if (!TextUtils.isEmpty(myQxmFeedData.getFeedCreatorImageURL()))
                    picasso.load(myQxmFeedData.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(normalFeedViewHolder.ivUserImage);
                else
                    normalFeedViewHolder.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_default));

                // show contest text view if contest mode is on
                if (myQxmFeedData.isContestModeOn())
                    normalFeedViewHolder.CVContest.setVisibility(View.VISIBLE);
                else normalFeedViewHolder.CVContest.setVisibility(View.GONE);

                // set qxm privacy icon -> public or private
                if (myQxmFeedData.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
                } else {
                    normalFeedViewHolder.ivFeedPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));
                }
                // show thumbnail image view if feed contains thumbnail
                if (!TextUtils.isEmpty(myQxmFeedData.getFeedThumbnailURL())) {

                    normalFeedViewHolder.flFeedThumbnailContainer.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(myQxmFeedData.getFeedThumbnailURL())) {

                        normalFeedViewHolder.ivFeedThumbnail.setVisibility(View.VISIBLE);
//                normalFeedViewHolder.tvFeedThumbnail.setVisibility(View.GONE);
                        picasso.load(myQxmFeedData.getFeedThumbnailURL()).into(normalFeedViewHolder.ivFeedThumbnail);
                    }

                } else {

                    //normalFeedViewHolder.flFeedThumbnailContainer.setVisibility(View.GONE);
                    normalFeedViewHolder.ivFeedThumbnail.setVisibility(View.GONE);
//            normalFeedViewHolder.tvFeedThumbnail.setVisibility(View.VISIBLE);
//            normalFeedViewHolder.tvFeedThumbnail.setText(myQxmFeedData.getFeedName());
                }

                if (!TextUtils.isEmpty(myQxmFeedData.getFeedYoutubeLinkURL())) {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.VISIBLE);
                } else {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.GONE);
                }

                normalFeedViewHolder.tvFeedName.setText(myQxmFeedData.getFeedName());
                normalFeedViewHolder.tvFeedCreator.setText(myQxmFeedData.getFeedCreatorName());

                if (myQxmFeedData.getFeedParticipantsQuantity() != null && !myQxmFeedData.getFeedParticipantsQuantity().equals("0")) {

                    if (myQxmFeedData.getFeedParticipantsQuantity().equals("1"))
                        normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", myQxmFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participant)));
                    else
                        normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("%s %s", myQxmFeedData.getFeedParticipantsQuantity(), context.getResources().getString(R.string.participants)));

                    normalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

                } else {
                    normalFeedViewHolder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
                    normalFeedViewHolder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
                }

                // show feed item time ago
                long feedCreationTime = Long.parseLong(myQxmFeedData.getFeedCreationTime());
                String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                normalFeedViewHolder.tvFeedCreationTime.setText(feedPostTimeAgo);

                normalFeedViewHolder.tvFeedDescription.setText(myQxmFeedData.getFeedDescription());

                SetlinkClickable.setLinkclickEvent(normalFeedViewHolder.tvFeedDescription, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, url);
                        context.startActivity(intent);
                    }
                });


                //region IgniteCount
                int igniteCount1;

                if (TextUtils.isEmpty(myQxmFeedData.getFeedIgniteQuantity())) {
                    igniteCount1 = 0;
                    myQxmFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                } else {
                    igniteCount1 = Integer.parseInt(myQxmFeedData.getFeedIgniteQuantity());
                    myQxmFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
                }

                if (!myQxmFeedData.getFeedIgniteQuantity().equals("0")) {
                    normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

                } else {
                    normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }

                normalFeedViewHolder.tvIgniteQuantity.setText(myQxmFeedData.getFeedIgniteQuantity());
                //endregion

                // region ShareCount
                int shareCount;
                if (TextUtils.isEmpty(myQxmFeedData.getFeedShareQuantity())) {
                    shareCount = 0;
                    myQxmFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                } else {
                    shareCount = Integer.parseInt(myQxmFeedData.getFeedShareQuantity());
                    myQxmFeedData.setFeedShareQuantity(String.valueOf(shareCount));
                }
                normalFeedViewHolder.tvShareQuantity.setText(myQxmFeedData.getFeedShareQuantity());
                // endregion


                if (myQxmFeedData.isEditedFlag()) {
                    normalFeedViewHolder.tvEdited.setVisibility(View.VISIBLE);
                } else {
                    normalFeedViewHolder.tvEdited.setVisibility(View.GONE);
                }

                // region feed main content onclick

                normalFeedViewHolder.llSingleFeedItem.setOnClickListener(v -> myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));

                normalFeedViewHolder.tvFeedName.setOnClickListener(v -> myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));


                normalFeedViewHolder.flItemContainer.setOnClickListener(v -> myQxmAdapterListener.onQxmSelected(
                        myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()
                ));

                normalFeedViewHolder.tvFeedDescription.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName())
                );

                normalFeedViewHolder.tvFeedCreationTime.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName())
                );

                normalFeedViewHolder.ivFeedPrivacy.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName())
                );

                normalFeedViewHolder.flItemContainer.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));

                normalFeedViewHolder.tvFeedCreationTime.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));

                normalFeedViewHolder.rlFeedEventsInfo.setOnClickListener(v ->
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));

                normalFeedViewHolder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        myQxmFeedData.getFeedCreatorId(), myQxmFeedData.getFeedCreatorName()
                ));

                normalFeedViewHolder.tvFeedCreator.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        myQxmFeedData.getFeedCreatorId(), myQxmFeedData.getFeedCreatorName()
                ));

                normalFeedViewHolder.ivFeedThumbnail.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(myQxmFeedData.getFeedYoutubeLinkURL())) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, myQxmFeedData.getFeedYoutubeLinkURL());
                        context.startActivity(intent);
                    } else {
                        myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName());
                    }
                });

                normalFeedViewHolder.ivYoutubeButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, myQxmFeedData.getFeedYoutubeLinkURL());
                    context.startActivity(intent);
                });

                // endregion

                // region feed menu item onclick

                normalFeedViewHolder.ivFeedOptions.setOnClickListener(v -> {

                    //Toast.makeText(context, "Clicked Normal feed Three    dot", Toast.LENGTH_SHORT).show();

                    PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_myqxm_feed, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {

                        switch (item.getItemId()) {

                            case R.id.action_add_to_list:
                                QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                                addQxmToList.addQxmToList(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName());

                                break;

                            case R.id.action_edit:

                                EditQxmNetworkCall.editQxmNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                                        fragmentActivity, token.getToken(), token.getUserId(), myQxmFeedData.getFeedQxmId());

                                break;

                            case R.id.action_share_to_group:

                                QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                                        (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
                                qxmShareContentToGroup.shareToGroup
                                        (myQxmFeedData.getFeedQxmId(), SHARED_CATEGORY_QXM);

                                break;

                            case R.id.action_share_link:
                                QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                                QxmUrlHelper urlHelper = new QxmUrlHelper();
                                qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(myQxmFeedData.getFeedQxmId()));

                                break;


                            case R.id.action_delete:
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
                                                    token.getToken(),
                                                    token.getUserId(),
                                                    myQxmFeedData.getFeedQxmId(),
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
                // TODO give action based on network call, now everything in this region is just for test

                // at first checking if user already clicked on ignite
                if (myQxmFeedData.isIgnitedClicked()) {
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

                        // ignite network call
                        igniteQxmNetworkCall(token.getUserId(), myQxmFeedData.getFeedQxmId());
                        int igniteCount = Integer.parseInt(myQxmFeedData.getFeedIgniteQuantity()) + 1;
                        normalFeedViewHolder.tvIgniteQuantity.setText(String.valueOf(igniteCount));
                        normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);
                        myQxmFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));

                    } else {
                        String ignite = "Ignite";
                        normalFeedViewHolder.tvIgnite.setText(ignite);
                        normalFeedViewHolder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                        normalFeedViewHolder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                        igniteQxmNetworkCall(token.getUserId(), myQxmFeedData.getFeedQxmId());
                        int igniteCount = Integer.parseInt(myQxmFeedData.getFeedIgniteQuantity()) - 1;
                        normalFeedViewHolder.tvIgniteQuantity.setText(String.valueOf(igniteCount));
                        myQxmFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));

                        if (igniteCount <= 0) {

                            normalFeedViewHolder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                            normalFeedViewHolder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                        }
                    }

                });


                // TODO we will decide what to do later
                normalFeedViewHolder.llcShare.setOnClickListener(v -> {
                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
                    if (!normalFeedViewHolder.tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                        qxmShareContentToGroup.shareToGroup
                                (myQxmFeedData.getFeedQxmId(), SHARED_CATEGORY_QXM);

                    } else {

                        qxmShareContentToGroup.shareToGroup
                                (myQxmFeedData.getFeedQxmId(), SHARED_CATEGORY_QXM);

                        if (qxmShareContentToGroup.sharedSuccess) {
                            qxmShareContentToGroup.sharedSuccess = false;
                            normalFeedViewHolder.tvShare.setText(context.getString(R.string.shared));
                            normalFeedViewHolder.tvShare.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            normalFeedViewHolder.ivShare.setImageResource(R.drawable.ic_share_active);
                        }
                    }
                });

                normalFeedViewHolder.llParticipate.setOnClickListener(v -> myQxmAdapterListener.onQxmSelected(myQxmFeedData.getFeedQxmId(), myQxmFeedData.getFeedName()));
                //endregion

                break;

            //endregion

            case SINGLE_MCQ_FEED:

                //region SINGLE_MCQ_FEED

                Log.d(TAG, "onBindViewHolder: SINGLE MCQ FEED CALLED...");

                SingleMCQFeedViewHolder singleMCQFeedViewHolder = (SingleMCQFeedViewHolder) holder;

                FeedData singleMCQFeedData = items.get(position);


                singleMCQFeedViewHolder.tvParticipate.setText(R.string.see_details);
                singleMCQFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));
                singleMCQFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.ic_file_document));


                Log.d(TAG, "onBindViewHolder: Single MCQ Feed: " + singleMCQFeedData.toString());

                singleMCQFeedViewHolder.tvSingleMCQFeedTitle.setText(singleMCQFeedData.getFeedName());


                // Poll creator image
                if (!TextUtils.isEmpty(singleMCQFeedData.getFeedCreatorImageURL())) {
                    picasso.load(singleMCQFeedData.getFeedCreatorImageURL())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(singleMCQFeedViewHolder.ivUserImage);
                } else {
                    singleMCQFeedViewHolder.ivUserImage.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_user_default));
                }

                // Goto Poll Creator Profile
                singleMCQFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                singleMCQFeedData.getFeedCreatorId(),
                                singleMCQFeedData.getFeedCreatorName())
                );

                // poll creator name
                singleMCQFeedViewHolder.tvSingleMCQCreatorFullName.setText(singleMCQFeedData.getFeedCreatorName());

                // region SingleMCQ-body


                // SingleMCQ title
                Log.d(TAG, "onBindViewHolder: poll title: " + singleMCQFeedData.getFeedName());

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
                        //Log.d(TAG, "onInterceptTouchEvent: MotionEvent" + e.toString());

                        return true;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        Log.d(TAG, "onTouchEvent: " + e.toString());
                        if (e.getAction() == MotionEvent.ACTION_UP && !isMyQxmFeedSingleMCQItemClicked) {
                            Log.d(TAG, "onInterceptTouchEvent: MotionEvent.ACTION_UP");
                            Log.d(TAG, "onInterceptTouchEvent: isMyQxmFeedSingleMCQItemClicked");
                            isMyQxmFeedSingleMCQItemClicked = true;
                            myQxmAdapterListener.onSingleMCQItemSelected(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName());
                        }

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        //Log.d(TAG, "onRequestDisallowInterceptTouchEvent: " + disallowIntercept);
                    }
                });


                // endregion


                // endregion

                //region If the Single Qxm is created by me then hide the participate button.
//                    if (singleMCQFeedData.getFeedCreatorId().equals(userId)) {
//                        singleMCQFeedViewHolder.viewParticipateBtnSeparator.setVisibility(View.GONE);
//                        singleMCQFeedViewHolder.llParticipate.setVisibility(View.GONE);
//                    } else {
//                        singleMCQFeedViewHolder.viewParticipateBtnSeparator.setVisibility(View.VISIBLE);
//                        singleMCQFeedViewHolder.llParticipate.setVisibility(View.VISIBLE);
//                    }
                //endregion


                //region Set Participate Quantity to view
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

                singleMCQFeedViewHolder.llSingleFeedItem.setOnClickListener(v ->
                        myQxmAdapterListener.onSingleMCQItemSelected(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName()));


                singleMCQFeedViewHolder.llParticipate.setOnClickListener(v -> myQxmAdapterListener.onSingleMCQItemSelected(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName()));

                // show feed item time ago
                if (QxmStringIntegerChecker.isLongInt(singleMCQFeedData.getFeedCreationTime())) {

                    long feedCreationTime1 = Long.parseLong(singleMCQFeedData.getFeedCreationTime());
                    String feedPostTimeAgo1 = TimeAgo.using(feedCreationTime1);
                    Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo1);
                    singleMCQFeedViewHolder.tvSingleMCQCreationTime.setText(feedPostTimeAgo1);
                } else {
                    Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
                    //pollFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
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


                // region feed menu item onclick

                singleMCQFeedViewHolder.ivSingleMCQFeedOptions.setOnClickListener(v ->
                        loadSingleMCQFeedMenuForSingleMCQCreator(v, holder, position, singleMCQFeedData)
                );

                // endregion

                singleMCQFeedViewHolder.ivSingleMCQThumbnail.setOnClickListener(v -> {

                    if (!TextUtils.isEmpty(singleMCQFeedData.getFeedYoutubeLinkURL())) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, singleMCQFeedData.getFeedYoutubeLinkURL());
                        context.startActivity(intent);
                    } else {
                        Log.d(TAG, "onBindViewHolder: Image thumbnail.");
                        myQxmAdapterListener.onSingleMCQItemSelected(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName());
                    }

                });

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

                        igniteSingleMCQNetworkCall(singleMCQFeedData.getFeedQxmId());

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

                        igniteSingleMCQNetworkCall(singleMCQFeedData.getFeedPollId());
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

                break;

            // endregion

        }


    }

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        return items.size();
    }
    //endregion

    //region Override-getItemViewType
    @Override
    public int getItemViewType(int position) {

        FeedData feedData = items.get(position);
        if (feedData.getFeedViewType() == null || feedData.getFeedViewType().equals(FEED_VIEW_TYPE_QXM)) {
            if (feedData.getFeedCreatorName().length() < 22) {
                Log.d(TAG, "getItemViewType: NORMAL_FEED");
                return NORMAL_FEED;
            } else {
                Log.d(TAG, "getItemViewType: NORMAL_FEED_WITH_LONG_NAME ");
                return NORMAL_FEED_WITH_LONG_NAME;
            }
        } else if (feedData.getFeedViewType().equals(FEED_VIEW_TYPE_SINGLE_MCQ)) {
            Log.d(TAG, "getItemViewType: SINGLE_MCQ_FEED ");
            return SINGLE_MCQ_FEED;
        }

        return NORMAL_FEED;

    }
    //endregion

    // region myQxm feed view holder

    //region clear
    // Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }

    // endregion

    //region ignite network call
    private void igniteQxmNetworkCall(String userId, String qxmId) {
        Log.d(TAG, String.format("igniteQxmNetworkCall: userId= %s, qxmId= %s", userId, qxmId));

        Call<QxmApiResponse> igniteQxm = apiService.igniteQxm(token.getToken(), userId, qxmId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onResponse: Response body is null");
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
                Log.d(TAG, "onFailure: : getFaiMessage" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region igniteSingleMCQNetworkCall
    private void igniteSingleMCQNetworkCall(String singleMCQId) {
        Call<QxmApiResponse> igniteQxm = apiService.singleMCQIgnite(token.getToken(), token.getUserId(), singleMCQId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: igniteSingleMCQNetworkCall");
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
                Log.d(TAG, "onFailure: igniteSingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region SingleMCQFeedViewHolder

    //region loadSingleMCQFeedMenuForSingleMCQCreator
    private void loadSingleMCQFeedMenuForSingleMCQCreator(View v, RecyclerView.ViewHolder holder, int position, FeedData singleMCQFeedData) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_my_qxm_single_mcq, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.action_share_to_group:
                    Toast.makeText(context, context.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                    /*QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
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
                    /*QxmAddQxmToList addQxmToList = new QxmAddQxmToList(fragmentActivity, context, apiService, token.getUserId(), token.getToken());
                    addQxmToList.addQxmToList(singleMCQFeedData.getFeedQxmId(), singleMCQFeedData.getFeedName());*/

                    break;

                case R.id.action_edit:

                    EditSingleMCQNetworkCall.getSingleMCQForEditNetworkCall(apiService, context, qxmFragmentTransactionHelper,
                            token.getToken(), singleMCQFeedData.getFeedQxmId());

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
                                deleteSingleMCQNetworkCall(token.getToken(), token.getUserId(), singleMCQFeedData.getFeedQxmId());
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

    //region deleteSingleMCQNetworkCall
    private void deleteSingleMCQNetworkCall(String token, String userId, String singleMCQId) {

        Call<QxmApiResponse> deletePoll = apiService.deleteSingleMCQ(token, userId, singleMCQId);

        deletePoll.enqueue(new Callback<QxmApiResponse>() {
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
    //endregion

    //region interface-MyQxmAdapterListener
    public interface MyQxmAdapterListener {
        void onQxmSelected(String qxmId, String qxmTitle);

        void onSingleMCQItemSelected(String singleMCQId, String singleMCQTitle);

    }
    //endregion

    // region Class-ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flItemContainer;
        LinearLayoutCompat llSingleFeedItem;
        FrameLayout flFeedThumbnailContainer;
        CardView CVContest;
        AppCompatImageView ivFeedPrivacy;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatTextView tvFeedName;
        AppCompatTextView tvFeedCreator;
        AppCompatTextView tvFeedCreationTime;
        AppCompatImageView ivFeedThumbnail;
        AppCompatImageView ivYoutubeButton;
        AppCompatTextView tvFeedDescription;
        RelativeLayout rlFeedEventsInfo;
        AppCompatTextView tvParticipantsQuantity;
        AppCompatImageView ivIgniteQuantity;
        AppCompatTextView tvIgniteQuantity;
        AppCompatTextView tvShareQuantity;
        LinearLayoutCompat llcIgnite;
        LinearLayoutCompat llcShare;
        AppCompatImageView ivIgnite;
        AppCompatImageView ivShare;
        AppCompatTextView tvIgnite;
        AppCompatTextView tvShare;
        LinearLayoutCompat llParticipate;
        AppCompatImageView ivParticipate;
        AppCompatTextView tvParticipate;
        AppCompatTextView tvEdited;
//        AppCompatTextView tvFeedThumbnail;


        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                myQxmAdapterListener.onQxmSelected(items.get(getAdapterPosition()).getFeedQxmId(), items.get(getAdapterPosition()).getFeedName());
                Log.d(TAG, "holder: " + items.get(getAdapterPosition()).getFeedQxmId());
            });
            flItemContainer = itemView.findViewById(R.id.flItemContainer);
            llSingleFeedItem = itemView.findViewById(R.id.llSingleFeedItem);
            flFeedThumbnailContainer = itemView.findViewById(R.id.FLFeedThumbnailContainer);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivFeedPrivacy = itemView.findViewById(R.id.ivFeedPrivacy);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            tvFeedName = itemView.findViewById(R.id.tvFeedName);
            tvFeedCreator = itemView.findViewById(R.id.tvFeedCreator);
            tvFeedCreationTime = itemView.findViewById(R.id.tvFeedCreationTime);
            ivFeedThumbnail = itemView.findViewById(R.id.ivFeedThumbnail);
            ivYoutubeButton = itemView.findViewById(R.id.ivYoutubeButton);
            tvFeedDescription = itemView.findViewById(R.id.tvFeedDescription);
            rlFeedEventsInfo = itemView.findViewById(R.id.rlFeedEventsInfo);
            tvParticipantsQuantity = itemView.findViewById(R.id.tvParticipantsQuantity);
            ivIgniteQuantity = itemView.findViewById(R.id.ivIgniteQuantity);
            tvIgniteQuantity = itemView.findViewById(R.id.tvIgniteQuantity);
            tvShareQuantity = itemView.findViewById(R.id.tvShareQuantity);
            llcIgnite = itemView.findViewById(R.id.llcIgnite);
            llcShare = itemView.findViewById(R.id.llcShare);
            ivIgnite = itemView.findViewById(R.id.ivIgnite);
            ivShare = itemView.findViewById(R.id.ivShare);
            tvIgnite = itemView.findViewById(R.id.tvIgnite);
            tvShare = itemView.findViewById(R.id.tvShare);
            llParticipate = itemView.findViewById(R.id.llParticipate);
            ivParticipate = itemView.findViewById(R.id.ivParticipate);
            tvParticipate = itemView.findViewById(R.id.tvParticipate);
            tvEdited = itemView.findViewById(R.id.tvEdited);
//          tvFeedThumbnail = itemView.findViewById(R.id.tvFeedThumbnail);


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


        SingleMCQFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "SingleMCQFeedViewHolder: clicked");
                FeedData feedData = items.get(getAdapterPosition());

                myQxmAdapterListener.onSingleMCQItemSelected(feedData.getFeedQxmId(), feedData.getFeedName());
                Log.d(TAG, "SingleMCQFeedViewHolder: singleMCQId: " + feedData.getFeedQxmId());
            });
        }
    }
    //endregion




}
