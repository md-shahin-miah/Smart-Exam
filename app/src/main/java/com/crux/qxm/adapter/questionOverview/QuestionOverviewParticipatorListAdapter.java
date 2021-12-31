package com.crux.qxm.adapter.questionOverview;

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
import com.crux.qxm.db.models.evaluation.Participator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionOverviewParticipatorListAdapter extends RecyclerView.Adapter<QuestionOverviewParticipatorListAdapter.ViewHolder> {

    private static final String TAG = "QuestionOverviewPartici";
    private ArrayList<Participator> participatorArrayList;
    private Picasso picasso;
    private Context context;
    private OnParticipantClickListener onParticipantClickListener;

    public QuestionOverviewParticipatorListAdapter(Context context, ArrayList<Participator> participatorArrayList, Picasso picasso, OnParticipantClickListener onParticipantClickListener) {
        this.context = context;
        this.participatorArrayList = participatorArrayList;
        this.picasso = picasso;
        this.onParticipantClickListener = onParticipantClickListener;
    }

    @NonNull
    @Override
    public QuestionOverviewParticipatorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View questionOverviewParticipantsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_row, parent, false);
        return new ViewHolder(questionOverviewParticipantsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Participator participator = participatorArrayList.get(position);
        Log.d(TAG, "participator: " + participator);

        if (!TextUtils.isEmpty(participator.getModifiedProfileImage()))
            picasso.load(participator.getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        else
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        holder.tvUserFullName.setText(participator.getFullName());

        holder.ivUserImage.setOnClickListener(v -> onParticipantClickListener.onParticipantImageClick(participator));

    }

    @Override
    public int getItemCount() {
        return participatorArrayList.size();
    }

    //region FollowingViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // do anything with user data in implementation
            itemView.setOnClickListener(v ->
                    onParticipantClickListener.onParticipantClick(participatorArrayList.get(getAdapterPosition())));
        }
    }
    //endregion

    //region OnParticipantClick Listener

    public interface OnParticipantClickListener {
        void onParticipantClick(Participator participator);

        void onParticipantImageClick(Participator participator);
    }
    //endregion
}
