package com.crux.qxm.adapter.myQxm.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.feed.FeedDataInterested;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAddQxmToList;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;


public class ListFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ListFeedAdapter";
    public static String qxmId;
    private final int NORMAL_FEED = 1;
    private final int INTEREST_BASED_FEED = 2;
    public AppCompatDialog dialog;
    private Context context;
    private List<Object> items;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private String userId;
    private String token;
    private String listId;
    private QxmApiService apiService;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private ListFeedAdapterListener listFeedAdapterListener;


    public ListFeedAdapter(Context context, List<Object> items, FragmentActivity fragmentActivity, Picasso picasso, String userId, String token, String listId, QxmApiService apiService, ListFeedAdapterListener listFeedAdapterListener) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.token = token;
        this.userId = userId;
        this.listId = listId;
        this.apiService = apiService;
        this.listFeedAdapterListener = listFeedAdapterListener;
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

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int igniteCount1;
        int shareCount;
        switch (holder.getItemViewType()) {

            case NORMAL_FEED:

                NormalFeedViewHolder normalFeedViewHolder = (NormalFeedViewHolder) holder;

                FeedData feedDataNormal = (FeedData) items.get(position);

                Log.d(TAG, "onBindViewHolder: Normal Feed: " + feedDataNormal.toString());

                if (feedDataNormal.getFeedCreatorImageURL() != null)
                    picasso.load(feedDataNormal.getFeedCreatorImageURL()).into(normalFeedViewHolder.ivUserImage);
                else
                    normalFeedViewHolder.ivUserImage.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.ic_user_default)
                    );

                normalFeedViewHolder.ivUserImage.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(feedDataNormal.getFeedCreatorId(), feedDataNormal.getFeedCreatorName()));

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

                // TODO add youtube video player
                if (!TextUtils.isEmpty(feedDataNormal.getFeedYoutubeLinkURL())) {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.VISIBLE);
                } else {
                    normalFeedViewHolder.ivYoutubeButton.setVisibility(View.GONE);
                }


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
                long feedCreationTime = Long.parseLong(feedDataNormal.getFeedCreationTime());
                String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
                Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
                normalFeedViewHolder.tvFeedCreationTime.setText(feedPostTimeAgo);
                normalFeedViewHolder.tvFeedDescription.setText(feedDataNormal.getFeedDescription());
                SetlinkClickable.setLinkclickEvent(normalFeedViewHolder.tvFeedDescription, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                        intent.putExtra(YOUTUBE_LINK_KEY, url);
                        context.startActivity(intent);
                    }
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

                    String qxmId = feedDataNormal.getFeedQxmId();
                    Log.d("FeedData", feedDataNormal.toString());
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                });

                normalFeedViewHolder.flItemContainer.setOnClickListener(v -> listFeedAdapterListener
                        .onItemSelected(feedDataNormal.getFeedQxmId()));

                normalFeedViewHolder.tvFeedDescription.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId())
                );

                normalFeedViewHolder.tvFeedCreationTime.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId())
                );

                normalFeedViewHolder.ivFeedPrivacy.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId())
                );

                normalFeedViewHolder.flItemContainer.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId()));

                normalFeedViewHolder.tvFeedCreationTime.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId()));

                normalFeedViewHolder.rlFeedEventsInfo.setOnClickListener(v ->
                        listFeedAdapterListener.onItemSelected(feedDataNormal.getFeedQxmId()));

                normalFeedViewHolder.tvFeedCreator.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadUserProfileFragment(
                                feedDataNormal.getFeedCreatorId(),
                                feedDataNormal.getFeedName()
                        ));

                // endregion

                // region feed menu item onclick

                normalFeedViewHolder.ivFeedOptions.setOnClickListener(v -> {

                    //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();

                    PopupMenu popup = new PopupMenu(fragmentActivity, v, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_list_feed, popup.getMenu());

                    popup.setOnMenuItemClickListener(item -> {

                        switch (item.getItemId()) {

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

                            case R.id.action_remove_from_list:
                                deleteQxmInListNetworkCall(token, listId, feedDataNormal.getFeedQxmId(), holder);

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

                    normalFeedViewHolder.tvParticipate.setText(R.string.see_details);
                    normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));
                    normalFeedViewHolder.ivParticipate.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_file_document));

                } else {

                    // If I Participated in this Qxm, then update the UI to blue color otherwise gray
                    Log.d(TAG, "feedDataNormal: isParticipated - " + feedDataNormal.isParticipated());
                    if (feedDataNormal.isParticipated()) {
                        normalFeedViewHolder.tvParticipate.setText(context.getString(R.string.participated));
                        normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                        normalFeedViewHolder.ivParticipate
                                .setColorFilter(ContextCompat.getColor(context, R.color.light_blue_400),
                                        android.graphics.PorterDuff.Mode.SRC_ATOP);

                    } else {
                        normalFeedViewHolder.tvParticipate.setText(context.getString(R.string.participate));
                        normalFeedViewHolder.tvParticipate.setTextColor(context.getResources().getColor(R.color.grey));
                        normalFeedViewHolder.ivParticipate.setColorFilter(context.getResources().getColor(R.color.grey));
                    }
                }

                //endregion

                break;
            case INTEREST_BASED_FEED:
                InterestedFeedViewHolder interestedFeedViewHolder = (InterestedFeedViewHolder) holder;
                FeedDataInterested feedDataInterested = (FeedDataInterested) items.get(position);
                Log.d(TAG, "onBindViewHolder: INTEREST_BASED_FEED: " + feedDataInterested.toString());
                FeedItemsInterestedAdapter feedItemsInterestedAdapter = new FeedItemsInterestedAdapter(context, feedDataInterested.getFeedDataListInterested());
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                interestedFeedViewHolder.interestedQxmRV.setLayoutManager(horizontalLayoutManager);
                interestedFeedViewHolder.interestedQxmRV.setAdapter(feedItemsInterestedAdapter);
        }
    }

    private void deleteQxmInListNetworkCall(String token, String listId, String feedQxmId, RecyclerView.ViewHolder holder) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog("Remove Qxm", "The Qxm is being removed, please wait...", false);

        Log.d(TAG, "deleteQxmInListNetworkCall: token: " + token);
        Call<QxmApiResponse> deleteQxmInList = apiService.deleteQxmInList(token, listId, feedQxmId);

        deleteQxmInList.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                progressDialog.closeProgressDialog();
                if (response.code() == 201) {
                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);
                    if (items.size() == 0)
                        listFeedAdapterListener.listFeedAdapterEmpty();

                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: Feed network call failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteQxmInListNetworkCall");
                Log.d(TAG, "onFailure: getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                progressDialog.closeProgressDialog();

                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // region igniteQxmNetworkCall
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


    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof FeedData)
            return NORMAL_FEED;
        else if (items.get(position) instanceof FeedDataInterested)
            return INTEREST_BASED_FEED;

        return -1;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // region Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }

    // endregion

    // region normal feed view holder

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
        AppCompatImageView ivParticipate;
        AppCompatTextView tvParticipate;
//        AppCompatTextView tvFeedThumbnail;


        NormalFeedViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "NormalFeedViewHolder: ");
                FeedData feedData = (FeedData) (items.get(getAdapterPosition()));
                listFeedAdapterListener.onItemSelected(feedData.getFeedQxmId());
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
            ivParticipate = itemView.findViewById(R.id.ivParticipate);
            tvParticipate = itemView.findViewById(R.id.tvParticipate);
//            tvFeedThumbnail = itemView.findViewById(R.id.tvFeedThumbnail);


        }
    }
    // endregion

    public interface ListFeedAdapterListener {
        void onItemSelected(String qxmId);

        void listFeedAdapterEmpty();
    }

    // region interest based feed view holder
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
