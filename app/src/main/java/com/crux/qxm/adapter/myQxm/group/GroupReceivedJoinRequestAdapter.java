package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupReceivedJoinRequestAdapter extends RecyclerView.Adapter<GroupReceivedJoinRequestAdapter.ViewHolder> {
    private static final String TAG = "InviteMemberToGroupAdapter";
    private final int VIEW_TYPE_FOLLOWING = 1;
    private final int VIEW_TYPE_FOLLOWER= 2;
    private Context context;
    private Picasso picasso;

    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;


    public GroupReceivedJoinRequestAdapter(Context context, List<Object> memberList, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, Picasso picasso) {
        this.context = context;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.picasso = picasso;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_group_received_join_requests_single_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvJoinRequestSentDate)
        AppCompatTextView tvJoinRequestSentDate;
        @BindView(R.id.tvAccept)
        AppCompatTextView tvAccept;
        @BindView(R.id.tvCancel)
        AppCompatTextView tvCancel;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


}
