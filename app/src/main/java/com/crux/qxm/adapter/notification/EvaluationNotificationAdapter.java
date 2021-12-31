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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.notification.EvaluationNotification;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;

public class EvaluationNotificationAdapter extends RecyclerView.Adapter<EvaluationNotificationAdapter.ViewHolder> {
    // region Properties


    private Context context;
    private List<EvaluationNotification> items;
    // Todo:: change the model..
    private static final String TAG = "EvaluationNotificationA";
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Constructor-EvaluationNotificationAdapter

    public EvaluationNotificationAdapter(Context context, List<EvaluationNotification> items, FragmentActivity fragmentActivity, Picasso picasso) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-onCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View evaluationNotificationSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_notification_evaluation_single_item, parent, false);

        return new EvaluationNotificationAdapter.ViewHolder(evaluationNotificationSingleItemView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EvaluationNotification evaluationNotificationData = items.get(position);

        Log.d(TAG, "onBindViewHolder: EvaluateNotifications: " + evaluationNotificationData.toString());

        // region load user profile image
        if (!TextUtils.isEmpty(evaluationNotificationData.getParticipator().getModifiedProfileImage()))
            picasso.load(evaluationNotificationData.getParticipator().getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);

        else
            holder.ivUserImage.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.ic_user_default)
            );

        // endregion

        // region SetMainText
        String mainText = String.format("<b>%s</b> submission of <b>%s</b> qxm is <b>pending for evaluation</b>",
                evaluationNotificationData.getParticipator().getFullName(),
                evaluationNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetTitle());

        holder.tvMainText.setText(Html.fromHtml(mainText));
        // endregion

        // region show contest text view if contest mode is on
        if (evaluationNotificationData.getParticipatedQxm().getQxmSettings().isContestModeEnabled())
            holder.CVContest.setVisibility(View.VISIBLE);
        else holder.CVContest.setVisibility(View.GONE);

        // endregion

        // region show thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(evaluationNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetThumbnail())) {
            holder.ivQxmThumbnail.setVisibility(View.VISIBLE);
            picasso.load(evaluationNotificationData.getParticipatedQxm().getQuestionSet().getQuestionSetThumbnail()).into(holder.ivQxmThumbnail);
        } else {
            holder.ivQxmThumbnail.setVisibility(View.GONE);
        }

        // endregion

        // region Show Notification Time Ago
        // Todo:: change with notification time
        long enrolledAt = Long.parseLong(evaluationNotificationData.getParticipatedAt());
        String feedPostTimeAgo = TimeAgo.using(enrolledAt);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvNotificationTime.setText(feedPostTimeAgo);

        // endregion

        //region goto user profile

        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                evaluationNotificationData.getParticipator().getId(), evaluationNotificationData.getParticipator().getFullName())
        );

        //endregion

        // region FeedThumbnail-OnClick

        holder.ivQxmThumbnail.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, evaluationNotificationData.getParticipatedQxm().getId());

            qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
        });

        // endregion

        holder.tvEvaluate.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadEvaluateQxmFragment(
                        evaluationNotificationData.getId(),
                        evaluationNotificationData.getParticipator().getFullName())
        );

        holder.tvLater.setOnClickListener(v -> {

            items.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), items.size() - 1);
        });

        // region feed menu item onclick

        holder.ivFeedOptions.setOnClickListener(v -> {

            //Toast.makeText(context, "Clicked Normal feed Three dot", Toast.LENGTH_SHORT).show();

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
    // Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }

    // endregion

    // region Class-FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView CVContest;
        AppCompatImageView ivUserImage;
        AppCompatTextView tvMainText;
        AppCompatImageView ivQxmThumbnail;
        AppCompatTextView tvNotificationTime;
        AppCompatTextView tvEvaluate;
        AppCompatTextView tvLater;
        AppCompatImageView ivFeedOptions;


        public ViewHolder(View itemView) {
            super(itemView);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            ivQxmThumbnail = itemView.findViewById(R.id.ivQxmThumbnail);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            tvEvaluate = itemView.findViewById(R.id.tvEvaluate);
            tvLater = itemView.findViewById(R.id.tvLater);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
        }
    }

    // endregion
}
