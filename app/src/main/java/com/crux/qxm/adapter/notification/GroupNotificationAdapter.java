package com.crux.qxm.adapter.notification;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.notification.groupNotification.GroupNotificationItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.GROUP_NOTIFICATION_STATUS_INVITE_REQUEST;
import static com.crux.qxm.utils.StaticValues.GROUP_NOTIFICATION_STATUS_JOIN_REQUEST;
import static com.crux.qxm.utils.StaticValues.GROUP_NOTIFICATION_STATUS_JOIN_REQUEST_ACCEPTED;
import static com.crux.qxm.utils.StaticValues.isMyQxmGroupFragmentRefreshNeeded;
import static com.crux.qxm.utils.StaticValues.isViewQxmGroupFragmentRefreshNeeded;

public class GroupNotificationAdapter extends RecyclerView.Adapter<GroupNotificationAdapter.ViewHolder> {

    // region Properties

    private static final String TAG = "GroupNotificationAdapte";
    private Context context;
    // Todo:: change the model..
    private List<GroupNotificationItem> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    // endregion

    // region Constructor-GroupNotificationAdapter

    public GroupNotificationAdapter(Context context, List<GroupNotificationItem> items, FragmentActivity fragmentActivity, Picasso picasso, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-OnCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View resultNotificationAdapterSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_notification_group_single_item, parent, false);

        return new GroupNotificationAdapter.ViewHolder(resultNotificationAdapterSingleItemView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupNotificationItem groupNotificationItem = items.get(position);

        Log.d(TAG, "onBindViewHolder: EvaluateNotifications: " + groupNotificationItem.toString());

        switch (groupNotificationItem.getStatus()) {
            case GROUP_NOTIFICATION_STATUS_INVITE_REQUEST: {
                // region SetMainText
                String mainText = String.format("<b>%s</b> has sent you an invitation request to join <b>%s</b> Group.",
                        groupNotificationItem.getUser().getFullName(),
                        groupNotificationItem.getGroup().getGroupInfo().getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));

                // Invisible the Accept-Reject button
                visibleAcceptRejectButtons(holder);

                break;
            }
            case GROUP_NOTIFICATION_STATUS_JOIN_REQUEST: {
                // region SetMainText
                String mainText = String.format("<b>%s</b> has sent a join request in <b>%s</b> Group.",
                        groupNotificationItem.getUser().getFullName(),
                        groupNotificationItem.getGroup().getGroupInfo().getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));

                // Invisible the Accept-Reject button
                visibleAcceptRejectButtons(holder);

                // region tvAcceptRequest-OnClick

                holder.tvAcceptRequest.setOnClickListener(v -> {

                    // Adding User To Group
                    acceptGroupJoinRequestNetworkCall(token.getToken(),
                            groupNotificationItem.getGroup().getId(),
                            groupNotificationItem.getUser().getId(),
                            token.getUserId());

                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);

                });

                // endregion

                // region tvRejectRequest-OnClick
                holder.tvRejectRequest.setOnClickListener(v -> {

                    //Reject User Group Join Request
                    rejectGroupJoinRequestNetworkCall(token.getToken(),
                            groupNotificationItem.getGroup().getId(),
                            groupNotificationItem.getUser().getId(),
                            token.getUserId());

                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);
                });

                // endregion


                break;
            }
            case GROUP_NOTIFICATION_STATUS_JOIN_REQUEST_ACCEPTED: {

                // region SetMainText
                String mainText = String.format("<b>%s</b> accepted your join request in <b>%s</b> Group.",
                        groupNotificationItem.getUser().getFullName(),
                        groupNotificationItem.getGroup().getGroupInfo().getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));

                // Invisible the Accept-Reject button
                goneAcceptRejectButtons(holder);
                break;
            }
        }

        // endregion

        // region show thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(groupNotificationItem.getGroup().getGroupInfo().getGroupImage())) {
            holder.ivGroupImage.setVisibility(View.VISIBLE);
            picasso.load(groupNotificationItem.getGroup().getGroupInfo().getGroupImage())
                    .into(holder.ivGroupImage);
        } else {
            holder.ivGroupImage.setVisibility(View.GONE);
        }

        // endregion

        // region Show Notification Time Ago
        // Todo:: change with notification time
        /*long enrolledAt = Long.parseLong(groupNotificationItem.getCreatedAt());
        String feedPostTimeAgo = TimeAgo.using(enrolledAt);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvNotificationTime.setText(feedPostTimeAgo);*/

        // endregion

        // region FeedThumbnail-OnClick

        holder.ivGroupImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadViewQxmGroupFragment(
                        groupNotificationItem.getGroup().getId(),
                        groupNotificationItem.getGroup().getGroupInfo().getGroupName()
                ));

        // endregion


        // region feed menu item onclick

        holder.ivFeedOptions.setOnClickListener(v -> {

            //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();

            PopupMenu popup = new PopupMenu(fragmentActivity, v, Gravity.END);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_notification, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {


                switch (item.getItemId()) {

                    case R.id.action_delete:
                        // Todo:: delete notification api.
                        items.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);

                        break;

                }

                return false;
            });
            popup.show();

        });

        // endregion
    }

    private void rejectGroupJoinRequestNetworkCall(String token, String groupId, String userId, String adminId) {
        Call<QxmApiResponse> rejectGroupJoinRequestNetworkCall = apiService.rejectGroupJoinRequest(token, groupId, userId, adminId);

        rejectGroupJoinRequestNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: rejectGroupJoinRequestNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getResources().getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: rejectGroupJoinRequestNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptGroupJoinRequestNetworkCall(String token, String groupId, String invitedUserId, String userId) {
        Call<QxmApiResponse> inviteAcceptFromAdmin = apiService.acceptGroupJoinRequest(token, groupId, invitedUserId, userId);

        inviteAcceptFromAdmin.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    isMyQxmGroupFragmentRefreshNeeded = true;
                    isViewQxmGroupFragmentRefreshNeeded = true;

                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: inviteAcceptFromAdminNetworkCall failed");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goneAcceptRejectButtons(ViewHolder holder) {
        holder.tvAcceptRequest.setVisibility(View.GONE);
        holder.tvRejectRequest.setVisibility(View.GONE);
    }

    private void visibleAcceptRejectButtons(ViewHolder holder) {
        holder.tvAcceptRequest.setVisibility(View.VISIBLE);
        holder.tvRejectRequest.setVisibility(View.VISIBLE);
    }

    // endregion

    // region GetItemCount

    @Override
    public int getItemCount() {
        return items.size();
    }

    // endregion

    //region Clear
    public void clear() {
        // Clean all elements of the recycler
        items.clear();
        notifyDataSetChanged();
    }
    // endregion

    // region FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvGroupName;
        AppCompatTextView tvMainText;
        AppCompatImageView ivGroupImage;
        AppCompatTextView tvNotificationTime;
        AppCompatTextView tvAcceptRequest;
        AppCompatTextView tvRejectRequest;
        AppCompatImageView ivFeedOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            ivGroupImage = itemView.findViewById(R.id.ivGroupImage);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            tvAcceptRequest = itemView.findViewById(R.id.tvAcceptRequest);
            tvRejectRequest = itemView.findViewById(R.id.tvRejectRequest);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
        }
    }

    // endregion

}
