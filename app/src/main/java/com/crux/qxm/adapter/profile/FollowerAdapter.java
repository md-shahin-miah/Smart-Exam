package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.profile.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    private Context context;
    private List<User> followerList;
    private Picasso picasso;
    private FollowerAdapterListener followerAdapterListener;


    public FollowerAdapter(Context context, List<User> followerList, Picasso picasso, FollowerAdapterListener followerAdapterListener){

        this.context = context;
        this.followerList = followerList;
        this.picasso = picasso;
        this.followerAdapterListener = followerAdapterListener;
    }


    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View followerListView = inflater.inflate(R.layout.profile_single_follower,parent,false);
        return new FollowerAdapter.ViewHolder(followerListView);

    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {

        if(!TextUtils.isEmpty(followerList.get(position).getFullName()))
            holder.tvUserFullName.setText(followerList.get(position).getFullName());
        if(!TextUtils.isEmpty(followerList.get(position).getModifiedProfileImage()))
            picasso.load(followerList.get(position).getModifiedProfileImage()).into(holder.ivUserImage);

    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    // Clean all elements of the recycler view
    public void clear() {

        followerList.clear();
        notifyDataSetChanged();
    }


    //region viewHolder
    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{

                followerAdapterListener.onFollowerSelected(followerList.get(getAdapterPosition()));

            });

        }
    }
    //endregion

    // region UserAdapterListener
    public interface FollowerAdapterListener {
        void onFollowerSelected(User follower);
    }
    //endregion
}
