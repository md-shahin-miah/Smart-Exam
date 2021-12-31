package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.groupInvitations.PendingInviteRequestItem;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupInvitationListAdapter extends RecyclerView.Adapter<GroupInvitationListAdapter.ViewHolder> {
    private static final String TAG = "GroupInvitationListAdap";
    private Context context;
    private Picasso picasso;
    private List<PendingInviteRequestItem> pendingInviteRequest;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private String token;
    private String userId;
    private String groupId;


    public GroupInvitationListAdapter(Context context, List<PendingInviteRequestItem> pendingInviteRequest,
                                      QxmFragmentTransactionHelper qxmFragmentTransactionHelper, Picasso picasso,
                                      QxmApiService apiService, String token, String userId, String groupId) {
        this.context = context;
        this.pendingInviteRequest = pendingInviteRequest;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        this.userId = userId;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_group_sent_invitation_single_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvUserFullName.setText(pendingInviteRequest.get(position).getUserInvited().getFullName());
        picasso.load(pendingInviteRequest.get(position).getUserInvited().getModifiedProfileImage()).into(holder.ivUserImage);
        holder.tvInvitationStatus.setText(pendingInviteRequest.get(position).getStatus());

        holder.tvAccept.setOnClickListener(v -> {
            inviteAcceptFromAdminNetworkCall(token, groupId, pendingInviteRequest.get(position).getUserInvited().getId(), userId);
        });

        holder.tvCancel.setOnClickListener(v -> {
            // TODO:: cancel request api add & Implement.
                pendingInviteRequest.remove(position);
                notifyItemRemoved(position);
        });

    }

    private void inviteAcceptFromAdminNetworkCall(String token, String groupId, String invitedUserId, String userId) {

        Call<QxmApiResponse> inviteAcceptFromAdmin = apiService.inviteAcceptFromAdmin(token, groupId, invitedUserId, userId);

        inviteAcceptFromAdmin.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: inviteAcceptFromAdminNetworkCall failed");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingInviteRequest.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvInvitationStatus)
        AppCompatTextView tvInvitationStatus;
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
