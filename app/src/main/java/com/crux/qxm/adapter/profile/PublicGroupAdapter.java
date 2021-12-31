package com.crux.qxm.adapter.profile;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.search.group.SearchedGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublicGroupAdapter extends RecyclerView.Adapter<PublicGroupAdapter.ViewHolder> {
    private static final String TAG = "PublicGroupAdapter";
    private Context context;
    private Picasso picasso;
    private List<SearchedGroup> groupList;
    private PublicGroupAdapterListener publicGroupAdapterListener;

    public PublicGroupAdapter(Context context, List<SearchedGroup> groupList, FragmentActivity fragmentActivity, Picasso picasso, PublicGroupAdapterListener publicGroupAdapterListener) {
        this.context = context;
        this.groupList = groupList;
        this.picasso = picasso;
        this.publicGroupAdapterListener = publicGroupAdapterListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myGroupSingleItemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.profile_single_public_group_item, parent, false
        );

        return new PublicGroupAdapter.ViewHolder(myGroupSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SearchedGroup groupData = groupList.get(position);

        Log.d(TAG, "onBindViewHolder: GroupData = " + groupData.toString());

        if (!TextUtils.isEmpty(groupData.getSearchedGroupInfo().getGroupThumbnail()))
            picasso.load(groupData.getSearchedGroupInfo().getGroupThumbnail()).into(holder.ivGroupThumbnail);

        if (!TextUtils.isEmpty(groupData.getSearchedGroupInfo().getGroupName()))
            holder.tvGroupName.setText(groupData.getSearchedGroupInfo().getGroupName());
        else
            holder.tvGroupName.setText("");
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }


    //region FollowingViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivGroupThumbnail)
        AppCompatImageView ivGroupThumbnail;
        @BindView(R.id.tvGroupName)
        AppCompatTextView tvGroupName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            ivGroupThumbnail = itemView.findViewById(R.id.ivGroupThumbnail);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);

            itemView.setOnClickListener(v->{
                publicGroupAdapterListener.onGroupSelected(groupList.get(getAdapterPosition()));
            });
        }
    }
    //endregion

    //region PublicGroupAdapterListener
    public interface PublicGroupAdapterListener{

        void onGroupSelected(SearchedGroup searchedGroup);
    }
    //endregion

    //region clear
    // Clean all elements of the recycler
    public void clear() {
        groupList.clear();
        notifyDataSetChanged();
    }
    //endregion
}
