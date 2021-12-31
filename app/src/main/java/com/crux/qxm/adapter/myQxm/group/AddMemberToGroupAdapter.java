package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import android.text.TextUtils;
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
import com.crux.qxm.db.models.group.addMemberToGroup.Follower;
import com.crux.qxm.db.models.group.addMemberToGroup.Following;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberToGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AddMemberToGroupAdapter";
    private final int VIEW_TYPE_FOLLOWING = 1;
    private final int VIEW_TYPE_FOLLOWER= 2;
    private Context context;
    private Picasso picasso;
    private List<Object> memberList;
    private List<String> selectedUserIds;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private String groupId;
    private String token;
    private String userId;


    public AddMemberToGroupAdapter(Context context, List<Object> memberList, List<String> selectedUserIds, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, Picasso picasso, QxmApiService apiService, String groupId, String token, String userId) {
        this.context = context;
        this.memberList = memberList;
        this.selectedUserIds = selectedUserIds;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.picasso = picasso;
        this.apiService = apiService;
        this.groupId = groupId;
        this.token = token;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View suggestedUserListView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_add_member_to_group_single_list_item, parent, false
        );

        if(viewType == VIEW_TYPE_FOLLOWING){
            return new FollowingViewHolder(suggestedUserListView);
        }else{
            return new FollowerViewHolder(suggestedUserListView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == VIEW_TYPE_FOLLOWING){

            FollowingViewHolder followingViewHolder = (FollowingViewHolder) holder;
            Following suggestedUser = (Following) memberList.get(position);

            Log.d(TAG, "FollowingViewHolder: suggestedUser = " + suggestedUser.toString());

            if (!TextUtils.isEmpty(suggestedUser.getModifiedProfileImage()))
                picasso.load(suggestedUser.getModifiedProfileImage()).into(followingViewHolder.ivUserImage);

            if (!TextUtils.isEmpty(suggestedUser.getFullName()))
                followingViewHolder.tvUsername.setText(suggestedUser.getFullName());
            else
                followingViewHolder.tvUsername.setText("");

            followingViewHolder.ivUserImage.setOnClickListener(v ->
                    qxmFragmentTransactionHelper.loadUserProfileFragment(
                            suggestedUser.getUserId(), suggestedUser.getFullName()
                    ));


            followingViewHolder.tvAddMemberToGroup.setOnClickListener(v -> {
                followingViewHolder.tvAddMemberToGroup.setText(R.string.adding_member);
                followingViewHolder.tvAddMemberToGroup.setEnabled(false);
                addMemberToGroupNetworkCall(token, groupId, userId, suggestedUser.getUserId(), followingViewHolder.tvAddMemberToGroup);
            });

        }else {

            FollowerViewHolder followerViewHolder = (FollowerViewHolder) holder;
            Follower suggestedUser = (Follower) memberList.get(position);

            Log.d(TAG, "FollowerViewHolder: suggestedUser = " + suggestedUser.toString());

            if (!TextUtils.isEmpty(suggestedUser.getModifiedProfileImage()))
                picasso.load(suggestedUser.getModifiedProfileImage()).into(followerViewHolder.ivUserImage);

            if (!TextUtils.isEmpty(suggestedUser.getFullName()))
                followerViewHolder.tvUsername.setText(suggestedUser.getFullName());
            else
                followerViewHolder.tvUsername.setText("");


            followerViewHolder.ivUserImage.setOnClickListener(v ->
                    qxmFragmentTransactionHelper.loadUserProfileFragment(
                            suggestedUser.getUserId(), suggestedUser.getFullName()
                    ));

            followerViewHolder.tvAddMemberToGroup.setOnClickListener(v -> {
                followerViewHolder.tvAddMemberToGroup.setText(R.string.adding_member);
                followerViewHolder.tvAddMemberToGroup.setEnabled(false);
                addMemberToGroupNetworkCall(token, groupId, userId, suggestedUser.getUserId(), followerViewHolder.tvAddMemberToGroup);
            });
        }

    }

    private void addMemberToGroupNetworkCall(String token, String groupId, String addByUserId, String newMemberUserId, AppCompatTextView tvSendJoinRequest) {

        Log.d(TAG, "addMemberToGroupNetworkCall: newMemberId " + newMemberUserId);
        Call<QxmApiResponse> addMemberNetworkCall = apiService.addMember(token, groupId, addByUserId, newMemberUserId);

        addMemberNetworkCall.enqueue(new Callback<QxmApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                if(response.code() == 201){
                    Log.d(TAG, "onResponse: success");
                    StaticValues.isViewQxmGroupFragmentRefreshNeeded = true;
                    tvSendJoinRequest.setText(R.string.member_added);
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                }else if(response.code() == 403){
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                }else {
                    Log.d(TAG, "onResponse: failed");
                    Toast.makeText(context, "Member could not be added. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: addMemberToGroupNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + memberList.size());
        return memberList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(memberList.get(position) instanceof Following)
            return VIEW_TYPE_FOLLOWING;
        else
            return VIEW_TYPE_FOLLOWER;
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvAddMemberToGroup;
        AppCompatImageView ivUserImage;
        AppCompatTextView tvUsername;


        public FollowingViewHolder(View itemView) {
            super(itemView);

            tvAddMemberToGroup = itemView.findViewById(R.id.tvAddMemberToGroup);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }

    public class FollowerViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvAddMemberToGroup;
        AppCompatImageView ivUserImage;
        AppCompatTextView tvUsername;


        public FollowerViewHolder(View itemView) {
            super(itemView);

            tvAddMemberToGroup = itemView.findViewById(R.id.tvAddMemberToGroup);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }

}
