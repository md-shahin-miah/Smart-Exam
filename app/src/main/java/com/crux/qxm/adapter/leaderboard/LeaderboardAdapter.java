package com.crux.qxm.adapter.leaderboard;

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
import com.crux.qxm.db.models.leaderboard.LeaderBoardItem;
import com.crux.qxm.db.models.leaderboard.Participator;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private static final String TAG = "LeaderboardAdapter";

    private List<LeaderBoardItem> leaderBoardItemList;
    private Context context;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private String qxmId;
    private LeaderboardItemClickListener leaderboardItemClickListener;


    public LeaderboardAdapter(List<LeaderBoardItem> leaderBoardItemList, Context context, Picasso picasso, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, String qxmId, LeaderboardItemClickListener leaderboardItemClickListener) {
        this.leaderBoardItemList = leaderBoardItemList;
        this.context = context;
        this.picasso = picasso;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.qxmId = qxmId;
        this.leaderboardItemClickListener = leaderboardItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View leaderViewListsItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_specific_qxm_leaderboard_single_item, parent, false);

        return new LeaderboardAdapter.ViewHolder(leaderViewListsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderBoardItem boardItem = leaderBoardItemList.get(position);

        Log.d(TAG, "onBindViewHolder: leaderboardItem: " + boardItem.toString());

        // user image
        if (!TextUtils.isEmpty(boardItem.getParticipator().getModifiedProfileImage())) {
            picasso.load(boardItem.getParticipator().getModifiedProfileImage()).error(context.getResources().getDrawable(R.drawable.ic_user_default)).into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_default));
        }

        // user full name with leaderboard position
        holder.tvUserFullName.setText(String.format("#%s %s", (position+1) , boardItem.getParticipator().getFullName()));

        // user achieve point
        holder.tvAchievedPoints.setText(boardItem.getUserPerformance().getAchievePoints());

        holder.tvAchievedPointsInPercentage.setText(boardItem.getUserPerformance().getAchievePointsInPercentage());

        holder.tvTimeTaken.setText(TimeFormatString.getDurationInHourMinSec(Long.parseLong(
                        boardItem.getUserPerformance().getTimeTakenToAnswer()
                ))
        );

        // click listeners
        holder.ivUserImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        boardItem.getParticipator().getId(),
                        boardItem.getParticipator().getFullName()
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
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.tvAchievedPoints)
        AppCompatTextView tvAchievedPoints;
        @BindView(R.id.tvAchievedPointsInPercentage)
        AppCompatTextView tvAchievedPointsInPercentage;
        @BindView(R.id.tvTimeTaken)
        AppCompatTextView tvTimeTaken;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {

                Participator participator = leaderBoardItemList.get(getAdapterPosition()).getParticipator();
                UserBasic userBasic = new UserBasic(participator.getId(), participator.getFullName(), participator.getModifiedProfileImage());

                leaderboardItemClickListener.onItemClickListener(qxmId, userBasic);

            });
            ButterKnife.bind(this, itemView);
        }
    }

    public interface LeaderboardItemClickListener{
        void onItemClickListener(String qxmId, UserBasic userBasic);
    }
}
