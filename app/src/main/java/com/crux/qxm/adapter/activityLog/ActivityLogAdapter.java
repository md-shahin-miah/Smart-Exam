package com.crux.qxm.adapter.activityLog;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.activityLog.ActivitiesItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.ACTIVITY_CREATE_LIST;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_FOLLOW;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_GROUP_ACCEPT_JOIN_REQUEST;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_GROUP_ADD_MEMBER;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_GROUP_CREATE;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_GROUP_JOIN_REQUEST;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_ENROLL_REQUEST_ACCEPTED;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_ENROLL_REQUEST_SENT;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_EVALUATION_CONFIRM;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_IGNITE;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_IGNITE_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_QXM_CREATION;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_QXM_EDITED;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_QXM_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_SHARE_QXM_TO_GROUP;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_SINGLE_MCQ_CREATED;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_SINGLE_MCQ_EDITED;
import static com.crux.qxm.utils.StaticValues.ACTIVITY_SINGLE_MCQ_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private static final String TAG = "ActivityLogAdapter";
    private Context context;
    private List<ActivitiesItem> activityLogList;
    private Picasso picasso;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public ActivityLogAdapter(Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, List<ActivitiesItem> activityLogList, Picasso picasso, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.activityLogList = activityLogList;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View activitySingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_activity_log_single_item, parent, false);
        return new ViewHolder(activitySingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ActivitiesItem log = activityLogList.get(position);

        Log.d(TAG, "\n\nActivityLogItem: " + log.toString() + "\n\n");

        if (!TextUtils.isEmpty(log.getType())) {

            switch (log.getType()) {
                case ACTIVITY_FOLLOW:
                    Log.d(TAG, "ACTIVITY_FOLLOW");
                    // region ActivityFollow

                    if (!TextUtils.isEmpty(log.getFollowingThumbnail())) {
                        picasso.load(log.getFollowingThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_following
                        ));
                        Log.d(TAG, "onBindViewHolder: profile image is null");
                    }


                    if (!TextUtils.isEmpty(log.getFollowingName())) {
                        String text = String.format("<font color='%s'>%s</font> followed <font color='%s'>%s</font>",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getFollowingName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    holder.rlSingleLogContainer.setOnClickListener(v -> {

                    });

                    // endregion

                    break;
                case ACTIVITY_GROUP_JOIN_REQUEST:
                    Log.d(TAG, "ACTIVITY_GROUP_JOIN_REQUEST");
                    // region ActivityGroupJoinRequest

                    if (!TextUtils.isEmpty(log.getGroupThumbnail())) {
                        picasso.load(log.getGroupThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_group_small
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getGroupName())) {
                        String text = String.format("<font color='%s'>%s</font> sent join request to <font color='%s'>%s</font> group",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getGroupName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_GROUP_ACCEPT_JOIN_REQUEST:
                    Log.d(TAG, "ACTIVITY_GROUP_ACCEPT_JOIN_REQUEST");
                    // region ACTIVITY_GROUP_ACCEPT_JOIN_REQUEST

                    if (!TextUtils.isEmpty(log.getGroupThumbnail())) {
                        picasso.load(log.getGroupThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_group_small
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getGroupName())) {
                        String text = String.format("%s have accepted the group join request of <font color='%s'>%s</font> in <font color='%s'>%s</font> group",

                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getNewMemberName(),
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getGroupName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_GROUP_ADD_MEMBER:
                    Log.d(TAG, "ACTIVITY_GROUP_ADD_MEMBER");
                    // region ACTIVITY_GROUP_ADD_MEMBER

                    if (!TextUtils.isEmpty(log.getGroupThumbnail())) {
                        picasso.load(log.getGroupThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_group_small
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getGroupName())) {
                        String text = String.format("You have added <font color='%s'>%s</font> in <font color='%s'>%s</font> group",

                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getNewMemberName(),
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getGroupName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;
                case ACTIVITY_EVALUATION_CONFIRM:
                    Log.d(TAG, "ACTIVITY_EVALUATION_CONFIRM");
                    // region ActivityEvaluationConfirm

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have evaluated <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;
                case ACTIVITY_QXM_PARTICIPATION:
                case ACTIVITY_PARTICIPATION:
                    Log.d(TAG, "ACTIVITY_QXM_PARTICIPATION");
                    // region ActivityQxmParticipation

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have participated <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_SINGLE_MCQ_PARTICIPATION:
                    Log.d(TAG, "ACTIVITY_SINGLE_MCQ_PARTICIPATION");
                    // region ACTIVITY_SINGLE_MCQ_PARTICIPATION

                    if (!TextUtils.isEmpty(log.getSingleMcqThumbnail())) {
                        picasso.load(log.getSingleMcqThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getSingleMcqName())) {
                        String text = String.format("<font color='%s'>%s</font> have participated <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getSingleMcqName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;


                case ACTIVITY_QXM_CREATION:
                    Log.d(TAG, "ACTIVITY_QXM_CREATION");
                    // region ActivityQxmCreation

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                    R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have created <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_QXM_EDITED:
                    Log.d(TAG, "ACTIVITY_QXM_EDITED");
                    // region ActivityQxmCreation

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> edited <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_ENROLL_REQUEST_SENT:
                    Log.d(TAG, "ACTIVITY_ENROLL_REQUEST_SENT");
                    // region ActivityEnrollRequest

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have sent enroll request for <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_ENROLL_REQUEST_ACCEPTED:
                    Log.d(TAG, "ACTIVITY_ENROLL_REQUEST_ACCEPTED");
                    // region ActivityEnrollAccepted

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have accepted enroll request for <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_IGNITE:
                    Log.d(TAG, "ACTIVITY_IGNITE");
                    // region ActivityIgnite

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have ignited <font color='%s'>%s</font> qxm",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_IGNITE_SINGLE_MCQ:
                    Log.d(TAG, "ACTIVITY_IGNITE_SINGLE_MCQ");
                    // region ActivityIgnite

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have ignited <font color='%s'>%s</font> Single MCQ",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_GROUP_CREATE:
                    Log.d(TAG, "ACTIVITY_GROUP_CREATE");
                    // region ACTIVITY_GROUP_CREATE

                    if (!TextUtils.isEmpty(log.getGroupThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_group_small
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getGroupName())) {
                        String text = String.format("<font color='%s'>%s</font> have created <font color='%s'>%s</font> Group",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getGroupName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                case ACTIVITY_SHARE_QXM_TO_GROUP:

                    Log.d(TAG, "ACTIVITY_SHARE_QXM_TO_GROUP");
                    // region ACTIVITY_SHARE_QXM_TO_GROUP

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }
                    //"You have shared Qxm in test group mridul"
                    if (!TextUtils.isEmpty(log.getGroupName())) {
                        String text = String.format("<font color='%s'>%s</font> have shared <font color='%s'>%s</font> qxm in <font color='%s'>%s</font>",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getQxmName(),
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getGroupName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_SINGLE_MCQ_CREATED:
                    Log.d(TAG, "ACTIVITY_SINGLE_MCQ_CREATED");
                    // region ACTIVITY_SINGLE_MCQ_CREATION

                    if (!TextUtils.isEmpty(log.getSingleMCQThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.ic_qxm_feed
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getSingleMCQName())) {
                        String text = String.format("<font color='%s'>%s</font> have created <font color='%s'>%s</font> Single MCQ",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getSingleMCQName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_SINGLE_MCQ_EDITED:
                    Log.d(TAG, "ACTIVITY_SINGLE_MCQ_EDITED");
                    // region ACTIVITY_SINGLE_MCQ_EDITED

                    if (!TextUtils.isEmpty(log.getQxmThumbnail())) {
                        picasso.load(log.getQxmThumbnail()).into(holder.ivImage);
                    } else {
                        holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                                R.drawable.img_create_single_mcq
                        ));
                    }

                    if (!TextUtils.isEmpty(log.getSingleQxmName())) {
                        String text = String.format("<font color='%s'>%s</font> have edited <font color='%s'>%s</font> Single MCQ",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getSingleQxmName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                case ACTIVITY_CREATE_LIST:
                    Log.d(TAG, "ACTIVITY_CREATE_LIST");
                    // region ACTIVITY_CREATE_LIST

                    holder.ivImage.setImageDrawable(context.getResources().getDrawable(
                            android.R.drawable.ic_menu_agenda
                    ));

                    if (!TextUtils.isEmpty(log.getListName())) {
                        String text = String.format("<font color='%s'>%s</font> have created <font color='%s'>%s</font> List",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                "You",
                                context.getResources().getString(R.string.text_highlighter_blue),
                                log.getListName());
                        holder.tvActivityLog.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    }

                    // endregion

                    break;

                    default:
                        Log.d(TAG, "switch-case default called: not matched with any case.");
                        holder.itemView.setVisibility(View.GONE);
                        break;

            }

        }

        // region click listeners
        holder.ivImage.setOnClickListener(v -> {
            switch (log.getType()) {
                case ACTIVITY_FOLLOW:
                    qxmFragmentTransactionHelper.loadUserProfileFragment(log.getFollowingId(),
                            log.getFollowingName());
                    break;
                case ACTIVITY_GROUP_JOIN_REQUEST:
                    qxmFragmentTransactionHelper.loadViewQxmGroupFragment(log.getGroupId(),
                            log.getGroupName());
                    break;

                case ACTIVITY_EVALUATION_CONFIRM:
                case ACTIVITY_ENROLL_REQUEST_ACCEPTED:
                case ACTIVITY_QXM_CREATION: {
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, log.getQxmId());
                    qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
                    break;
                }

                case ACTIVITY_IGNITE:
                case ACTIVITY_ENROLL_REQUEST_SENT:
                case ACTIVITY_QXM_PARTICIPATION:
                case ACTIVITY_PARTICIPATION: {
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, log.getQxmId());
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    break;
                }

            }

        });


        holder.rlSingleLogContainer.setOnClickListener(v -> {
            switch (log.getType()) {
                case ACTIVITY_FOLLOW:
                    qxmFragmentTransactionHelper.loadUserProfileFragment(log.getFollowingId(),
                            log.getFollowingName());
                    break;
                case ACTIVITY_GROUP_JOIN_REQUEST:
                    qxmFragmentTransactionHelper.loadViewQxmGroupFragment(log.getGroupId(),
                            log.getGroupName());
                    break;

                case ACTIVITY_EVALUATION_CONFIRM:
                case ACTIVITY_ENROLL_REQUEST_ACCEPTED:
                case ACTIVITY_QXM_CREATION: {
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, log.getQxmId());
                    qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
                    break;
                }

                case ACTIVITY_IGNITE:
                case ACTIVITY_ENROLL_REQUEST_SENT:
                case ACTIVITY_QXM_PARTICIPATION: {
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, log.getQxmId());
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    break;
                }

            }

        });



        // endregion
    }

    @Override
    public int getItemCount() {
        return activityLogList.size();
    }

    public void clear() {
        activityLogList.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlSingleLogContainer)
        RelativeLayout rlSingleLogContainer;
        @BindView(R.id.ivImage)
        AppCompatImageView ivImage;
        @BindView(R.id.tvActivityLog)
        AppCompatTextView tvActivityLog;
        @BindView(R.id.ivActivityItemOptions)
        AppCompatImageView ivActivityItemOptions;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }



}
