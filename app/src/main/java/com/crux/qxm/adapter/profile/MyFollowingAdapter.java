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

public class MyFollowingAdapter extends RecyclerView.Adapter<MyFollowingAdapter.ViewHolder> {

    private Context context;
    private List<User> followingList;
    private Picasso picasso;
    private FollowingAdapterListener followingAdapterListener;


    public MyFollowingAdapter(Context context, List<User> followingList, Picasso picasso, FollowingAdapterListener followingAdapterListener){

        this.context = context;
        this.followingList = followingList;
        this.picasso = picasso;
        this.followingAdapterListener = followingAdapterListener;
    }


    @NonNull
    @Override
    public MyFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View followingListView = inflater.inflate(R.layout.profile_single_my_following,parent,false);
        return new MyFollowingAdapter.ViewHolder(followingListView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyFollowingAdapter.ViewHolder holder, int position) {

        if(!TextUtils.isEmpty(followingList.get(position).getFullName()))
            holder.tvUserFullName.setText(followingList.get(position).getFullName());
        else
            holder.tvUserFullName.setText("");
        if(!TextUtils.isEmpty(followingList.get(position).getModifiedProfileImage()))
            picasso.load(followingList.get(position).getModifiedProfileImage()).into(holder.ivUserImage);
        else
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_default));

    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    // Clean all elements of the recycler view
    public void clear() {

        followingList.clear();
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

                followingAdapterListener.onFollowingSelected(followingList.get(getAdapterPosition()));

            });

        }
    }
    //endregion

    // region UserAdapterListener
    public interface FollowingAdapterListener {
        void onFollowingSelected(User follower);
    }
    //endregion
}
