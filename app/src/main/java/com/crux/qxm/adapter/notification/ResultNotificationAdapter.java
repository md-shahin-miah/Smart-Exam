package com.crux.qxm.adapter.notification;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.notification.ResultNotification;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;

public class ResultNotificationAdapter extends RecyclerView.Adapter<ResultNotificationAdapter.ViewHolder> {

    // region Properties

    private static final String TAG = "ResultNotificationAdapt";
    private Context context;
    // Todo:: change the model..
    private List<ResultNotification> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Constructor-ResultNotificationAdapter

    public ResultNotificationAdapter(Context context, List<ResultNotification> items, FragmentActivity fragmentActivity, Picasso picasso) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-OnCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View resultNotificationAdapterSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_notification_result_single_item, parent, false);

        return new ResultNotificationAdapter.ViewHolder(resultNotificationAdapterSingleItemView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultNotification resultNotificationData = items.get(position);

        Log.d(TAG, "onBindViewHolder: EvaluateNotifications: " + resultNotificationData.toString());

        // region SetMainText
        // Todo:: group name
        String mainText = String.format("<b>%s</b> wants to join <b>%s</b>.",
                resultNotificationData.getParticipator().getFullName(),
                resultNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetTitle());

        holder.tvMainText.setText(Html.fromHtml(mainText));
        // endregion

        // region show contest text view if contest mode is on
        if (resultNotificationData.getParticipatedQxm().getQxmSettings().isContestModeEnabled())
            holder.CVContest.setVisibility(View.VISIBLE);
        else holder.CVContest.setVisibility(View.GONE);

        // endregion

        // region show thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(resultNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetThumbnail())) {
            holder.ivQxmThumbnail.setVisibility(View.VISIBLE);
            picasso.load(resultNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetThumbnail()).into(holder.ivQxmThumbnail);
        } else {
            holder.ivQxmThumbnail.setVisibility(View.GONE);
        }

        // endregion

        // region Show Notification Time Ago
        // Todo:: change with notification time
        long notificationTime = Long.parseLong(resultNotificationData.getParticipatedAt());
        String feedPostTimeAgo = TimeAgo.using(notificationTime);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvNotificationTime.setText(feedPostTimeAgo);

        // endregion

        // region FeedThumbnail-OnClick

        holder.ivQxmThumbnail.setOnClickListener(v -> {
            // Todo:: goto MyQxmDetailsFragment
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, resultNotificationData.getParticipatedQxm().getId());

            qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);

            // loadMyQxmDetailsFragment(args);

        });

        // endregion

        // region tvAcceptRequest-OnClick

        holder.tvSeeResult.setOnClickListener(v -> {
            qxmFragmentTransactionHelper.loadFullResultFragment(resultNotificationData.getParticipatedQxm().getId());
        });

        // endregion

        // region tvRejectRequest-OnClick
        holder.tvClose.setOnClickListener(v -> {
            // Todo: hide the notification item.

            items.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);
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

        CardView CVContest;
        AppCompatImageView ivQxmPrivacy;
        AppCompatTextView tvMainText;
        AppCompatImageView ivQxmThumbnail;
        AppCompatTextView tvNotificationTime;
        AppCompatTextView tvSeeResult;
        AppCompatTextView tvClose;
        AppCompatImageView ivFeedOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivQxmPrivacy = itemView                                                                                                                                                                                                   .findViewById(R.id.ivQxmPrivacy);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            ivQxmThumbnail = itemView.findViewById(R.id.ivQxmThumbnail);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            tvSeeResult = itemView.findViewById(R.id.tvSeeResult);
            tvClose = itemView.findViewById(R.id.tvClose);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
        }
    }

    // endregion


}
