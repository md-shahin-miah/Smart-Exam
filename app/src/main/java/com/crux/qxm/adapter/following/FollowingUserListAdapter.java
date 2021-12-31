package com.crux.qxm.adapter.following;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.following.FollowingUser;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingUserListAdapter extends RecyclerView.Adapter<FollowingUserListAdapter.ViewHolder> {
    private static final String TAG = "FollowingUserListAdapte";
    private Context context;
    private Picasso picasso;
    private List<FollowingUser> followingUserList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public FollowingUserListAdapter(Context context, Picasso picasso, FragmentActivity fragmentActivity, List<FollowingUser> followingUserList) {
        this.context = context;
        this.picasso = picasso;
        this.followingUserList = followingUserList;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View followingUserListView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_following_user_single_item_layout, parent, false
        );

        return new FollowingUserListAdapter.ViewHolder(followingUserListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowingUser user = followingUserList.get(position);

        Log.d(TAG, "onBindViewHolder: User = " + user.toString());

        // region LoadUserProfileImage
        if (!TextUtils.isEmpty(user.getFollowing().getModifiedProfileImage())) {
            picasso.load(user.getFollowing().getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        }
        // endregion

        // region ProfileImageOnClick-gotoUserProfile

        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getFollowing().getUserId(),user.getFollowing().getFullName()));

        // endregion

        // region SetOnlineIndicator
        if (user.isOnline()) {
            holder.viewOnlineIndicator.setBackground(context.getResources().getDrawable(
                    R.drawable.background_following_user_online
            ));
        } else {
            holder.viewOnlineIndicator.setBackground(context.getResources().getDrawable(
                    R.drawable.background_following_user_offline
            ));
        }
        // endregion

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + followingUserList.size());
        return followingUserList.size();
    }

    // region FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivUserImage;
        View viewOnlineIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            viewOnlineIndicator = itemView.findViewById(R.id.viewOnlineIndicator);
        }
    }

    // endregion

    // region Clear - clean all elements of the recycler

    public void clear() {
        followingUserList.clear();
        notifyDataSetChanged();
    }

    // endregion


}
