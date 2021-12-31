package com.crux.qxm.adapter.myQxm.myPoll;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.FeedPollOptionAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.editPollNetworkCall.GetPollDataNetworkCall;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_POLL;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

public class MyPollAdapter extends RecyclerView.Adapter<MyPollAdapter.ViewHolder> {
    private static final String TAG = "MyPollAdapter";

    private Context context;
    private List<FeedData> items;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private String userId;
    private String token;

    public MyPollAdapter(Context context, List<FeedData> items, Picasso picasso, FragmentActivity fragmentActivity, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, QxmApiService apiService, String userId, String token) {
        this.context = context;
        this.items = items;
        this.picasso = picasso;
        this.fragmentActivity = fragmentActivity;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View pollSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_poll_single_item, parent, false);

        return new MyPollAdapter.ViewHolder(pollSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int igniteCount1;
        int shareCount;

        FeedData pollFeedData = items.get(position);

        Log.d(TAG, "onBindViewHolder: Poll Feed: " + pollFeedData.toString());

        holder.tvFeedPollTitle.setText(pollFeedData.getFeedName());

        // Poll creator image
        if (!TextUtils.isEmpty(pollFeedData.getFeedCreatorImageURL())) {
            picasso.load(pollFeedData.getFeedCreatorImageURL())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_user_default));
        }

        // Goto Poll Creator Profile
        holder.ivUserImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        pollFeedData.getFeedCreatorId(),
                        pollFeedData.getFeedCreatorName())
        );

        // poll creator name
        holder.tvPollCreatorName.setText(pollFeedData.getFeedCreatorName());


        // region poll-body


        // poll title
        Log.d(TAG, "onBindViewHolder: poll title: " + pollFeedData.getFeedName());
        holder.tvPollTitle.setText(pollFeedData.getFeedName());
        SetlinkClickable.setLinkclickEvent(holder.tvPollTitle, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });


        // region show poll thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(pollFeedData.getFeedThumbnailURL())) {
            holder.ivPollThumbnail.setVisibility(View.VISIBLE);
            picasso.load(pollFeedData.getFeedThumbnailURL()).into(holder.ivPollThumbnail);
        } else {
            holder.ivPollThumbnail.setVisibility(View.GONE);
        }
        // endregion

        // region poll options


        FeedPollOptionAdapter feedPollOptionAdapter = new FeedPollOptionAdapter(context, pollFeedData.getFeedPollId(), apiService, userId, token, pollFeedData, holder.tvParticipantsQuantity, qxmFragmentTransactionHelper);
        holder.rvPollOption.setLayoutManager(new LinearLayoutManager(context));
        holder.rvPollOption.setAdapter(feedPollOptionAdapter);
        holder.rvPollOption.setNestedScrollingEnabled(false);


        // endregion


        // endregion

        if (pollFeedData.getPollParticipatorQuantity() != null && !pollFeedData.getPollParticipatorQuantity().equals("0")) {

            if (pollFeedData.getPollParticipatorQuantity().equals("1"))
                holder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participant)));
            else
                holder.tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participants)));

            holder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

        } else {
            holder.tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
            holder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
        }


        // show feed item time ago
        if (QxmStringIntegerChecker.isLongInt(pollFeedData.getFeedCreationTime())) {

            long feedCreationTime = Long.parseLong(pollFeedData.getFeedCreationTime());
            String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
            Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
            holder.tvPollCreationTime.setText(feedPostTimeAgo);
        } else {
            Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    pollFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
        }

        // region PollFeedAdapterItemClickListeners

        holder.tvPollCreatorName.setOnClickListener(v ->
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
            holder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
            holder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

        } else {
            holder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
            holder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
        }
        holder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

        // endregion

        // region ShareCount

        if (TextUtils.isEmpty(pollFeedData.getFeedShareQuantity())) {
            shareCount = 0;
            pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
        } else {
            shareCount = Integer.parseInt(pollFeedData.getFeedShareQuantity());
            pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
        }

        holder.tvShareQuantity.setText(pollFeedData.getFeedShareQuantity());

        // endregion


        // region feed menu item onclick

        holder.ivPollFeedOptions.setOnClickListener(v -> {

            loadPollFeedMenuForPollCreator(v, holder, position, pollFeedData);


        });

        // endregion

        // region ignite and share

        // region at first checking if user already clicked on ignite
        if (pollFeedData.isIgnitedClicked()) {

            String ignited = "Ignited";
            holder.tvIgnite.setText(ignited);
            holder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
            holder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);
        }
        // endregion

        //region PollIgnite
        holder.llcIgnite.setOnClickListener(v -> {

            if (!holder.tvIgnite.getText().equals("Ignited")) {
                String ignited = "Ignited";
                holder.tvIgnite.setText(ignited);
                holder.tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                holder.ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                ignitePollNetworkCall(userId, pollFeedData.getFeedPollId());

                int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
                igniteCount++;
                pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                holder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());
                holder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                holder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

            } else {
                String ignite = "Ignite";
                holder.tvIgnite.setText(ignite);
                holder.tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                holder.ivIgnite.setImageResource(R.drawable.ic_ignite);

                ignitePollNetworkCall(userId, pollFeedData.getFeedPollId());
                int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());

                if (igniteCount > 0)
                    igniteCount--;
                else
                    igniteCount = 0;

                pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                holder.tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

                if (igniteCount <= 0) {

                    holder.tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    holder.ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }
            }

        });
        //endregion

        //region PollShare
        holder.llcShare.setOnClickListener(v -> {
            QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                    (fragmentActivity, apiService, context, picasso, userId, token);

            if (!holder.tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                qxmShareContentToGroup.shareToGroup
                        (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

            } else {

                qxmShareContentToGroup.shareToGroup
                        (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                if (qxmShareContentToGroup.sharedSuccess) {
                    qxmShareContentToGroup.sharedSuccess = false;
                    holder.tvShare.setText(context.getString(R.string.shared));
                    holder.tvShare.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    holder.ivShare.setImageResource(R.drawable.ic_share_active);
                }
            }
        });
        //endregion

        //endregion

        // endregion
    }

    // region loadPollFeedMenuForPollCreator
    private void loadPollFeedMenuForPollCreator(View v, RecyclerView.ViewHolder holder, int position, FeedData pollFeedData) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_my_qxm_my_poll, popup.getMenu());
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


    @Override
    public int getItemCount() {
        return items.size();
    }

    // region Clear
    public void clear() {
        // Clean all elements of the recycler
        items.clear();
        notifyDataSetChanged();
    }
    // endregion


    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
