package com.crux.qxm.adapter.leaderboardSingleMCQ;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.leaderboardSingleMCQ.SingleMCQLeaderboardItem;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardSingleMCQAdapter extends RecyclerView.Adapter<LeaderboardSingleMCQAdapter.ViewHolder> {

    private static final String TAG = "LeaderboardAdapter";

    private List<SingleMCQLeaderboardItem> leaderBoardItemList;
    private Context context;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public LeaderboardSingleMCQAdapter(List<SingleMCQLeaderboardItem> mcqLeaderboardItemList, Context context, Picasso picasso, QxmFragmentTransactionHelper qxmFragmentTransactionHelper) {
        this.leaderBoardItemList = mcqLeaderboardItemList;
        this.context = context;
        this.picasso = picasso;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View leaderViewListsItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_single_mcq_leaderboard_single_item, parent, false);

        return new LeaderboardSingleMCQAdapter.ViewHolder(leaderViewListsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleMCQLeaderboardItem item = leaderBoardItemList.get(position);

        Log.d(TAG, "onBindViewHolder: leaderboardItem: " + item.toString());

        // user image
        if (!TextUtils.isEmpty(item.getParticipator().getModifiedProfileImage())) {
            picasso.load(item.getParticipator().getModifiedProfileImage()).error(context.getResources().getDrawable(R.drawable.ic_user_default)).into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_default));
        }

        // user full name with leader board position
        holder.tvUserFullName.setText(String.format(" %s", item.getParticipator().getFullName()));

        if(item.getQuestionSet().isIsCorrect()){
            Log.d(TAG, "onBindViewHolder: correct: " + item.getQuestionSet().isIsCorrect());
            holder.ivAnswerStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_marked_outline));
            holder.tvAnswerStatus.setText(context.getString(R.string.correct));
            holder.tvAnswerStatus.setTextColor(context.getResources().getColor(R.color.correct_answer));
        }else{
            Log.d(TAG, "onBindViewHolder: correct: " + item.getQuestionSet().isIsCorrect());
            holder.ivAnswerStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.close_box_outline));
            holder.tvAnswerStatus.setText(context.getString(R.string.wrong));
            holder.tvAnswerStatus.setTextColor(context.getResources().getColor(R.color.wrong_answer));
        }

        // click listeners
        holder.ivUserImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        item.getParticipator().getId(),
                        item.getParticipator().getFullName()
                ));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: leaderBoardItemList.size = " + leaderBoardItemList.size());
        return leaderBoardItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.ivAnswerStatus)
        AppCompatImageView ivAnswerStatus;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;

        @BindView(R.id.tvAnswerStatus)
        AppCompatTextView tvAnswerStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
