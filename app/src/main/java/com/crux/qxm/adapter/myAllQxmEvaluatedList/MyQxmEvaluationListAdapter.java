package com.crux.qxm.adapter.myAllQxmEvaluatedList;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.crux.qxm.R;
import com.crux.qxm.db.models.evaluation.EvaluationPendingQxm;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.PENDING_EVALUATION_LIST_FRAGMENT_BUNDLE_DATA;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;

public class MyQxmEvaluationListAdapter extends RecyclerView.Adapter<MyQxmEvaluationListAdapter.ViewHolder> {

    // region Adapter-Properties
    private static final String TAG = "MyQxmEvaluationListAdap";
    private List<EvaluationPendingQxm> evaluationPendingQxmList;
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    // endregion

    // region Adapter-Constructor
    public MyQxmEvaluationListAdapter(List<EvaluationPendingQxm> evaluationPendingQxmList, Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper) {
        this.evaluationPendingQxmList = evaluationPendingQxmList;
        this.context = context;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }
    // endregion

    // region Override-OnCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myQxmEvaluationListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_my_qxm_evaluation_list_single_item, parent, false);

        return new ViewHolder(myQxmEvaluationListItem);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EvaluationPendingQxm pendingQxm = evaluationPendingQxmList.get(position);

        // Qxm Title
        holder.tvQxmTitle.setText(pendingQxm.getFeedTitle());

        // Qxm CreationTime
        if (QxmStringIntegerChecker.isLongInt(pendingQxm.getFeedCreationTime())) {
            long feedCreationTime = Long.parseLong(pendingQxm.getFeedCreationTime());
            String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
            Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
            holder.tvQxmCreationTime.setText(feedPostTimeAgo);

        } else {
            Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
            // holder.tvQxmCreationTime.setVisibility(View.GONE);
        }

        if (pendingQxm.getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC))
            holder.ivQxmPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
        else
            holder.ivQxmPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));

        holder.tvPendingEvaluationCount.setText(String.valueOf(pendingQxm.getFeedPendingEvaluationQuantity()));

        holder.tvEvaluatedTotalCount.setText(String.valueOf(pendingQxm.getFeedParticipantsQuantity()));

        holder.tvQxmCategory.setText(
                String.format("Category: %s", QxmArrayListToStringProcessor.getStringFromArrayList((ArrayList<String>) pendingQxm.getFeedCategory()))
        );

        holder.tvSeePendingEvaluationList.setOnClickListener(v ->
                loadQxmPendingEvaluatedList(pendingQxm.getFeedQxmId(), pendingQxm.getFeedTitle())
        );

        holder.llPendingEvaluation.setOnClickListener(v ->
                loadQxmPendingEvaluatedList(pendingQxm.getFeedQxmId(), pendingQxm.getFeedTitle())
        );

        holder.tvSeeEvaluatedList.setOnClickListener(v ->
                //Todo:: type needed - Qxm or SingleMCQ,
                qxmFragmentTransactionHelper.loadQuestionOverviewParticipantListFragment(pendingQxm.getFeedQxmId(), "")
        );

        holder.llEvaluated.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadQuestionOverviewParticipantListFragment(pendingQxm.getFeedQxmId(), "")
        );
    }

    // endregion

    // region loadQxmPendingEvaluatedList

    private void loadQxmPendingEvaluatedList(String qxmId, String qxmTitle) {
        // passing qxm id in bundle
        Bundle bundle = new Bundle();

        //readying bundle data
        ArrayList<String> bundleDataList = new ArrayList<>();
        bundleDataList.add(qxmId);
        bundleDataList.add(qxmTitle);
        bundle.putStringArrayList(PENDING_EVALUATION_LIST_FRAGMENT_BUNDLE_DATA, bundleDataList);

        qxmFragmentTransactionHelper.loadMyQxmSingleQxmPendingEvaluationListFragment(bundle);
    }

    // endregion

    // region Override-GetItemCount

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCountSize: " + evaluationPendingQxmList.size());
        return evaluationPendingQxmList.size();
    }

    // endregion

    // region Clear

    public void clear() {
        evaluationPendingQxmList.clear();
        notifyDataSetChanged();
    }

    // endregion

    // region Adapter-FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvQxmTitle)
        AppCompatTextView tvQxmTitle;
        @BindView(R.id.tvQxmCreationTime)
        AppCompatTextView tvQxmCreationTime;
        @BindView(R.id.ivQxmPrivacy)
        AppCompatImageView ivQxmPrivacy;
        @BindView(R.id.tvPendingEvaluationCount)
        AppCompatTextView tvPendingEvaluationCount;
        @BindView(R.id.tvSeePendingEvaluationList)
        AppCompatTextView tvSeePendingEvaluationList;
        @BindView(R.id.rlPendingEvaluation)
        RelativeLayout llPendingEvaluation;
        @BindView(R.id.tvEvaluatedTotalCount)
        AppCompatTextView tvEvaluatedTotalCount;
        @BindView(R.id.tvSeeEvaluatedList)
        AppCompatTextView tvSeeEvaluatedList;
        @BindView(R.id.rlEvaluated)
        RelativeLayout llEvaluated;
        @BindView(R.id.tvQxmCategory)
        AppCompatTextView tvQxmCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // endregion
}
