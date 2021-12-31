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
import com.crux.qxm.db.models.notification.qxmNotification.QxmNotification;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;

public class QxmNotificationAdapter extends RecyclerView.Adapter<QxmNotificationAdapter.ViewHolder> {

    // region Properties
    private static final String TAG = "QxmNotificationAdapter";

    private Context context;
    // Todo:: change the model..
    private List<QxmNotification> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private String userId;

    // endregion

    // region Constructor-QxmNotificationAdapter

    public QxmNotificationAdapter(Context context, List<QxmNotification> items, FragmentActivity fragmentActivity, Picasso picasso, String userId) {
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

        return new QxmNotificationAdapter.ViewHolder(qxmNotificationSingleItemView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QxmNotification qxmNotificationData = items.get(position);

        Log.d(TAG, "onBindViewHolder: EvaluateNotifications: " + qxmNotificationData.toString());

        // region load user profile image
        if (!TextUtils.isEmpty(qxmNotificationData.getQxmCreator().getModifiedProfileImage()))
            picasso.load(qxmNotificationData.getQxmCreator().getModifiedProfileImage()).into(holder.ivUserImage);

        // endregion

        // region SetMainText
        String mainText = String.format("<b>%s</b> submission of <b>%s</b> qxm is <b>pending for evaluation</b>",
                qxmNotificationData.getQxmCreator().getFullName(),
                qxmNotificationData.getQuestionSet().getQuestionSetTitle());

        holder.tvMainText.setText(Html.fromHtml(mainText));
        // endregion

        // region show thumbnail image view if feed contains thumbnail

        if (!TextUtils.isEmpty(qxmNotificationData.getQuestionSet().getQuestionSetThumbnail())) {
            holder.ivQxmThumbnail.setVisibility(View.VISIBLE);
            picasso.load(qxmNotificationData.getQuestionSet().getQuestionSetThumbnail()).into(holder.ivQxmThumbnail);
        } else {
            holder.ivQxmThumbnail.setVisibility(View.GONE);
        }

        // endregion

        // region Show Notification Time Ago

        // Todo:: change with notification time
        long enrolledAt = Long.parseLong(qxmNotificationData.getCreatedAt());
        String feedPostTimeAgo = TimeAgo.using(enrolledAt);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvNotificationTime.setText(feedPostTimeAgo);

        // endregion

        // region FeedThumbnail-OnClick

        holder.ivQxmThumbnail.setOnClickListener(v -> {
            String qxmId = items.get(position).getId();
            Log.d(TAG, "qxmId: " + qxmId);
            if(items.get(position).getQxmCreator().getId().equals(userId)){
                // load MyQxmDetailsFragment

                Bundle args = new Bundle();
                args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
                qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);

            }else{
                // load QuestionOverviewFragment
                
                Bundle args = new Bundle();
                args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
            }
        });

        // endregion

        //region goto user profile

        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                qxmNotificationData.getQxmCreator().getId(), qxmNotificationData.getQxmCreator().getFullName())
        );

        //endregion

        // region feed menu item onclick

        holder.ivFeedOptions.setOnClickListener(v -> {

            //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();

            PopupMenu popup = new PopupMenu(fragmentActivity, v, Gravity.END);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_notification, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {


                switch (item.getItemId()) {

                    case R.id.action_delete:
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);
                        // Todo:: hide qxm notification api & implementation
                        //hideSingleQxmNotificationNetworkCall();
                        break;

                }

                return false;
            });
            popup.show();

        });

        // endregion

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
