package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.group.GroupData;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.ViewHolder> {
    private static final String TAG = "MyGroupAdapter";
    private final String userId;
    private Context context;
    private Picasso picasso;
    private List<GroupData> groupList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;


    public MyGroupAdapter(Context context, List<GroupData> groupList, FragmentActivity fragmentActivity, Picasso picasso, String userId) {
        this.context = context;
        this.groupList = groupList;
        this.picasso = picasso;
        this.userId = userId;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myGroupSingleItemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_my_group_list_single_item_layout, parent, false
        );

        return new MyGroupAdapter.ViewHolder(myGroupSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroupData groupData = groupList.get(position);

        Log.d(TAG, "onBindViewHolder: GroupData = " + groupData.toString());
        Log.d(TAG, "onBindViewHolder: entered here");

        if (!TextUtils.isEmpty(groupData.getGroupImageUrl()))
            picasso.load(groupData.getModifiedGroupImageURL()).into(holder.ivGroupImage);

        if (!TextUtils.isEmpty(groupData.getGroupName()))
            holder.tvGroupName.setText(groupData.getGroupName());
        else
            holder.tvGroupName.setText("");

        if (!TextUtils.isEmpty(groupData.getMemberCount())) {
            if (Integer.parseInt(groupData.getMemberCount()) > 1)
                holder.tvMemberCount.setText(String.format("%s Members", groupData.getMemberCount()));
            else
                holder.tvMemberCount.setText(String.format("%s Member", groupData.getMemberCount()));
        } else
            holder.tvMemberCount.setText(R.string.zero_member);

        holder.ivGroupImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadViewQxmGroupFragment(groupData.getGroupId(), groupData.getGroupName()));

        holder.rlMyGroup.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadViewQxmGroupFragment(groupData.getGroupId(), groupData.getGroupName()));
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMyGroup;
        AppCompatImageView ivGroupImage;
        AppCompatTextView tvGroupName;
        AppCompatTextView tvMemberCount;


        public ViewHolder(View itemView) {
            super(itemView);

            rlMyGroup = itemView.findViewById(R.id.rlMyGroup);
            ivGroupImage = itemView.findViewById(R.id.ivGroupImage);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvMemberCount = itemView.findViewById(R.id.tvMemberCount);
        }
    }

    // Clean all elements of the recycler
    public void clear() {

        groupList.clear();
        notifyDataSetChanged();

    }
}
