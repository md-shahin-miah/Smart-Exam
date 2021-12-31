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

public class QuestionOverviewEnrolledListAdapter extends RecyclerView.Adapter<QuestionOverviewEnrolledListAdapter.ViewHolder> {

    private static final String TAG = "QuestionOverviewPartici";
    private ArrayList<Participator> enrolledUserArrayList;
    private Picasso picasso;
    private Context context;
    private OnEnrolledUserClickListener onEnrolledUserClickListener;

    public QuestionOverviewEnrolledListAdapter(Context context, ArrayList<Participator> enrolledUserArrayList, Picasso picasso, OnEnrolledUserClickListener onEnrolledUserClickListener){
        this.context = context;
        this.enrolledUserArrayList = enrolledUserArrayList;
        this.picasso = picasso;
        this.onEnrolledUserClickListener = onEnrolledUserClickListener;
    }

    @NonNull
    @Override
    public QuestionOverviewEnrolledListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View questionOverviewEnrolledView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_row,parent,false);
       return new ViewHolder(questionOverviewEnrolledView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Participator enrolledUser = enrolledUserArrayList.get(position);
        Log.d(TAG, "enrolledUser: "+enrolledUser);

        if(!TextUtils.isEmpty(enrolledUser.getModifiedProfileImage()))
            picasso.load(enrolledUser.getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        else
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        holder.tvUserFullName.setText(enrolledUser.getFullName());

    }

    @Override
    public int getItemCount() {
        return enrolledUserArrayList.size();
    }

    //region FollowingViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            // do anything with user data in implementation
            itemView.setOnClickListener(v-> {

                onEnrolledUserClickListener.onParticipantClick(enrolledUserArrayList.get(getAdapterPosition()));

            });
        }
    }
    //endregion

    //region OnParticipantClick Listener

    public interface  OnEnrolledUserClickListener{
        void onParticipantClick(Participator enrolledUser);
    }
    //endregion
}
