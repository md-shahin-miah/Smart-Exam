package com.crux.qxm.adapter.myQxm;

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

public class MyQxmSingleQxmEnrolledListAdapter extends RecyclerView.Adapter<MyQxmSingleQxmEnrolledListAdapter.ViewHolder> {


    private static final String TAG = "MyQxmSingleQxmEnrolledA";

    private ArrayList<Participator> enrolledUserList;
    private Picasso picasso;
    private Context context;
    private OnEnrolledUserClickListener onEnrolledUserClickListener;

    public MyQxmSingleQxmEnrolledListAdapter(Context context, ArrayList<Participator> enrolledUserList, Picasso picasso, OnEnrolledUserClickListener onEnrolledUserClickListener) {

        this.enrolledUserList = enrolledUserList;
        this.picasso = picasso;
        this.context = context;
        this.onEnrolledUserClickListener = onEnrolledUserClickListener;

    }

    @NonNull
    @Override
    public MyQxmSingleQxmEnrolledListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myQxmSingleQxmPendingParticipantsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_single_qxm_enrolled_list_item, parent,false);
        return new ViewHolder(myQxmSingleQxmPendingParticipantsView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyQxmSingleQxmEnrolledListAdapter.ViewHolder holder, int position) {

        Participator participator = enrolledUserList.get(position);
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


    }

    @Override
    public int getItemCount() {
        return enrolledUserList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{

                onEnrolledUserClickListener.onEnrolledUser(enrolledUserList.get(getAdapterPosition()));
            });
        }


    } //region OnParticipantClick Listener

    public interface  OnEnrolledUserClickListener{
        void onEnrolledUser(Participator participator);
    }
    //endregion

}
