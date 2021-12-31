package com.crux.qxm.adapter.notification;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.notification.allNotification.NotificationItem;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_ENROLL_REQUEST;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_EVALUATION;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_FOLLOW;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_GROUP_INVITE_REQUEST_TO_USER;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_GROUP_JOIN_REQUEST;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_GROUP_JOIN_REQUEST_ACCEPTED;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_POLL_CREATED;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_QXM_CREATED;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_QXM_EDITED;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_RESULT;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_SINGLE_MCQ_CREATED;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_STATUS_SINGLE_MCQ_EDITED;

public class AllNotificationAdapter extends RecyclerView.Adapter<AllNotificationAdapter.ViewHolder> {


    // region Properties
    private static final String TAG = "QxmNotificationAdapter";

    private Context context;
    // Todo:: change the model..
    private List<NotificationItem> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private String userId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Constructor-QxmNotificationAdapter

    public AllNotificationAdapter(Context context, List<NotificationItem> items, FragmentActivity fragmentActivity, Picasso picasso, String userId) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.userId = userId;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-OnCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View qxmNotificationSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_notification_qxm_single_item, parent, false);

        return new AllNotificationAdapter.ViewHolder(qxmNotificationSingleItemView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notificationItem = items.get(position);

        Log.d(TAG, "onBindViewHolder: EvaluateNotifications: " + notificationItem.toString());

        // region load user profile image
        //TODO:: profile image
        if (!TextUtils.isEmpty(notificationItem.getUserImage()))
            picasso.load(notificationItem.getUserImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);

        else
            holder.ivUserImage.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.ic_user_default)
            );
        // endregion

        String mainText;
        switch (notificationItem.getStatus()) {

            case NOTIFICATION_STATUS_QXM_CREATED:

                if (notificationItem.getQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getQxmName(), 0, 100).append("...");
                    notificationItem.setQxmName(stringBuilder.toString());
                }


                // region SetMainText
                mainText = String.format("<b>%s</b> posted a Qxm, <b>%s</b>",
                        notificationItem.getQxmCreator(),
                        notificationItem.getQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getUserId(), notificationItem.getQxmCreator())
                );

                //endregion

                holder.itemView.setOnClickListener(v -> {
                    if (notificationItem.getUserId().equals(userId)) {
                        // load MyQxmDetailsFragment

                        Bundle args = new Bundle();
                        args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, notificationItem.getQxmId());
                        qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);

                    } else {
                        // load QuestionOverviewFragment

                        Bundle args = new Bundle();
                        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, notificationItem.getQxmId());
                        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    }
                });

                break;

            case NOTIFICATION_STATUS_POLL_CREATED:

                // region SetMainText
                mainText = String.format("<b>%s</b> posted a Poll, <b>%s</b>",
                        notificationItem.getPollCreator(),
                        notificationItem.getPollName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getUserId(), notificationItem.getPollCreator())
                );

                //endregion

                break;

            case NOTIFICATION_STATUS_SINGLE_MCQ_CREATED:


                if (notificationItem.getSingleMCQName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getSingleMCQName(), 0, 100).append("...");
                    notificationItem.setSingleMCQName(stringBuilder.toString());
                }


                // region SetMainText
                mainText = String.format("<b>%s</b> created a Single MCQ, <b>%s</b>",
                        notificationItem.getSingleMCQCreator(),
                        notificationItem.getSingleMCQName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getUserId(), notificationItem.getSingleMCQCreator())
                );

                //endregion


                break;

            case NOTIFICATION_STATUS_QXM_EDITED:

                if (notificationItem.getQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getQxmName(), 0, 100).append("...");
                    notificationItem.setQxmName(stringBuilder.toString());
                }

                // region SetMainText
                mainText = String.format("<b>%s</b> edited a Qxm, <b>%s</b>",
                        notificationItem.getQxmCreator(),
                        notificationItem.getQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getUserId(), notificationItem.getQxmCreator())
                );

                //endregion


                break;

            case NOTIFICATION_STATUS_SINGLE_MCQ_EDITED:

                if (notificationItem.getSingleQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getSingleQxmName(), 0, 100).append("...");
                    notificationItem.setSingleQxmName(stringBuilder.toString());
                }

                // region SetMainText
                mainText = String.format("<b>%s</b> edited a Single MCQ, <b>%s</b>",
                        notificationItem.getSingleQxmCreator(),
                        notificationItem.getSingleQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getUserId(), notificationItem.getSingleQxmName())
                );

                //endregion

                break;


            case NOTIFICATION_STATUS_FOLLOW:
                // region SetMainText
                mainText = String.format("<b>%s</b> following <b>you</b>",
                        notificationItem.getFollowerName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                if (!TextUtils.isEmpty(notificationItem.getProfileImage())) {
                    picasso.load(notificationItem.getProfileImage())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(holder.ivUserImage);
                }

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getFollowerId(), notificationItem.getFollowerName())
                );

                //endregion

                holder.itemView.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getFollowerId(), notificationItem.getFollowerName()));

                break;

            case NOTIFICATION_STATUS_ENROLL_REQUEST:

                if (notificationItem.getQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getQxmName(), 0, 100).append("...");
                    notificationItem.setQxmName(stringBuilder.toString());
                }

                // region SetMainText
                mainText = String.format("<b>%s</b> sent an Qxm enroll request of <b>%s</b>",
                        notificationItem.getQxmCreatorName(),
                        notificationItem.getQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));


                // endregion


                //region goto user profile
                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getQxmCreatorId(), notificationItem.getQxmCreatorName())
                );

                //endregion

                //region load QuestionOverviewFragment
                holder.itemView.setOnClickListener(v -> {

                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, notificationItem.getQxmId());
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);


                });
                //endregion

                break;

            case NOTIFICATION_STATUS_EVALUATION:

                if (notificationItem.getQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getQxmName(), 0, 100).append("...");
                    notificationItem.setQxmName(stringBuilder.toString());
                }

                // region SetMainText
                mainText = String.format("<b>%s</b> submission of <b>%s</b> is pending for evaluation",
                        notificationItem.getParticipatorName(),
                        notificationItem.getQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getParticipatorId(), notificationItem.getParticipatorName())
                );

                //endregion

                holder.itemView.setOnClickListener(v -> {

                    // load MyQxmDetailsFragment
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, notificationItem.getQxmId());
                    qxmFragmentTransactionHelper.loadFullResultFragment(notificationItem.getQxmId());

                });

                break;

            case NOTIFICATION_STATUS_RESULT:

                if (notificationItem.getQxmName().length() > 100) {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(notificationItem.getQxmName(), 0, 100).append("...");
                    notificationItem.setQxmName(stringBuilder.toString());
                }

                // region SetMainText
                mainText = String.format("<b>%s</b> qxm result has published",
                        notificationItem.getQxmName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getParticipatorId(), notificationItem.getParticipatorName())
                );

                //endregion

                //region load Qxm Result Fragment
                holder.itemView.setOnClickListener(v ->
                        qxmFragmentTransactionHelper.loadFullResultFragment(notificationItem.getQxmId())
                );
                //endregion

                break;

            case NOTIFICATION_STATUS_GROUP_JOIN_REQUEST:

                // region SetMainText
                mainText = String.format("<b>%s</b> sent a join request in <b>%s</b> group.",
                        notificationItem.getNewMemberName(),
                        notificationItem.getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                if (!TextUtils.isEmpty(notificationItem.getGroupImage())) {
                    picasso.load(notificationItem.getGroupImage())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(holder.ivUserImage);

                    Log.d(TAG, "onBindViewHolder: has group image");
                }

                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getNewMemberId(), notificationItem.getNewMemberName())
                );

                //endregion

                //region load View Group Fragment
                holder.itemView.setOnClickListener(v -> qxmFragmentTransactionHelper.loadViewQxmGroupFragment(
                        notificationItem.getGroupId(),
                        notificationItem.getGroupName()
                ));
                //endregion

                break;

            case NOTIFICATION_STATUS_GROUP_JOIN_REQUEST_ACCEPTED:
                // region SetMainText
                //Todo:: addedBy(Admin) name & Id needed
                mainText = String.format("<b>You</b> are added in <b>%s</b> group.",
                        notificationItem.getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getNewMemberId(), notificationItem.getNewMemberName())
                );

                //endregion

                //region load View Group Fragment
                holder.itemView.setOnClickListener(v -> qxmFragmentTransactionHelper.loadViewQxmGroupFragment(
                        notificationItem.getGroupId(),
                        notificationItem.getGroupName()
                ));
                //endregion

                break;

            case NOTIFICATION_STATUS_GROUP_INVITE_REQUEST_TO_USER:

                // region SetMainText
                mainText = String.format("<b>%s</b> sent an invitation for joining in <b>%s</b> group.",
                        notificationItem.getAdminName(),
                        notificationItem.getGroupName());

                holder.tvMainText.setText(Html.fromHtml(mainText));
                // endregion

                if (!TextUtils.isEmpty(notificationItem.getGroupImage())) {
                    picasso.load(notificationItem.getGroupImage())
                            .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                            .into(holder.ivUserImage);

                    Log.d(TAG, "onBindViewHolder: has group image");
                }

                //region goto user profile

                holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                        notificationItem.getAdminId(), notificationItem.getAdminName())
                );


                //endregion

                //region load View Group Fragment
                holder.itemView.setOnClickListener(v -> qxmFragmentTransactionHelper.loadViewQxmGroupFragment(
                        notificationItem.getGroupId(),
                        notificationItem.getGroupName()
                ));
                //endregion

                break;
        }


        // region show thumbnail image view if feed contains thumbnail

        /*if (!TextUtils.isEmpty(notificationItem.getQuestionSetThumbnail())) {
            holder.ivQxmThumbnail.setVisibility(View.VISIBLE);
            picasso.load(notificationItem.getQuestionSetThumbnail().into(holder.ivQxmThumbnail);
        } else {
            holder.ivQxmThumbnail.setVisibility(View.GONE);
        }*/

        holder.ivQxmThumbnail.setVisibility(View.GONE);

        // endregion

        // region Show Notification Time Ago

        // Todo:: change with notification time
        long enrolledAt = Long.parseLong(notificationItem.getTime());
        String feedPostTimeAgo = TimeAgo.using(enrolledAt);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvNotificationTime.setText(feedPostTimeAgo);

        // endregion

        // region FeedThumbnail-OnClick

        holder.ivQxmThumbnail.setOnClickListener(v -> {
            String qxmId = notificationItem.getQxmId();
            Log.d(TAG, "qxmId: " + qxmId);
            if (notificationItem.getUserId().equals(userId)) {
                // load MyQxmDetailsFragment

                Bundle args = new Bundle();
                args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
                qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);

            } else {
                // load QuestionOverviewFragment

                Bundle args = new Bundle();
                args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
            }

        });

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

                        // Todo:: hide notification api.
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

    // endregion


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // region GetItemCount
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: items.size = " + items.size());
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

        AppCompatImageView ivUserImage;
        AppCompatTextView tvMainText;
        AppCompatImageView ivQxmThumbnail;
        AppCompatTextView tvNotificationTime;
        AppCompatImageView ivFeedOptions;

        public ViewHolder(View itemView) {
            super(itemView);

            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            ivQxmThumbnail = itemView.findViewById(R.id.ivQxmThumbnail);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
        }
    }

    // endregion
}
