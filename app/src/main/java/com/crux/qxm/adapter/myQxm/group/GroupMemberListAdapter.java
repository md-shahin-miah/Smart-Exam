package com.crux.qxm.adapter.myQxm.group;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.groupMemberList.AdminItem;
import com.crux.qxm.db.models.group.groupMemberList.MembersItem;
import com.crux.qxm.db.models.group.groupMemberList.ModeratorItem;
import com.crux.qxm.db.models.group.groupMemberList.NewAdmin;
import com.crux.qxm.db.models.group.groupMemberList.NewMod;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_ADMIN;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_MODERATOR;
import static com.crux.qxm.utils.StaticValues.isMyQxmGroupFragmentRefreshNeeded;
import static com.crux.qxm.utils.StaticValues.isViewQxmGroupFragmentRefreshNeeded;

public class GroupMemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "GroupMemberListAdapter";
    private static final int VIEW_TYPE_ADMIN = 1;
    private static final int VIEW_TYPE_MODERATOR = 2;
    private static final int VIEW_TYPE_MEMBER = 3;

    private Context context;
    private List<Object> membersItemList;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private RealmService realmService;
    private QxmApiService apiService;
    private QxmToken token;
    private String groupId;
    private String myStatus;

    public GroupMemberListAdapter(Context context, List<Object> membersItemList, Picasso picasso, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, RealmService realmService, QxmApiService apiService, QxmToken token, String groupId, String myStatus) {
        this.context = context;
        this.membersItemList = membersItemList;
        this.picasso = picasso;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.realmService = realmService;
        this.apiService = apiService;
        this.token = token;
        this.groupId = groupId;
        this.myStatus = myStatus;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        switch (viewType) {
            case VIEW_TYPE_ADMIN:
                View groupAdminListView = inflater.inflate(R.layout.profile_single_group_admin, parent, false);
                return new AdminViewHolder(groupAdminListView);
            case VIEW_TYPE_MODERATOR:
                View groupModeratorListView = inflater.inflate(R.layout.profile_single_group_moderator, parent, false);
                return new ModeratorViewHolder(groupModeratorListView);
            case VIEW_TYPE_MEMBER:
                View groupMemberListView = inflater.inflate(R.layout.profile_single_group_member, parent, false);
                return new MemberViewHolder(groupMemberListView);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {

            case VIEW_TYPE_ADMIN:

                Log.d(TAG, "onBindViewHolder: VIEW_TYPE_ADMIN");
                AdminViewHolder adminViewHolder = (AdminViewHolder) holder;
                AdminItem admin = (AdminItem) membersItemList.get(position);

                if (!TextUtils.isEmpty(admin.getNewAdmin().getFullName()))
                    adminViewHolder.tvUserFullName.setText(admin.getNewAdmin().getFullName());
                if (!TextUtils.isEmpty(admin.getNewAdmin().getModifiedProfileImage()))
                    picasso.load(admin.getNewAdmin().getModifiedProfileImage()).into(adminViewHolder.ivUserImage);

                adminViewHolder.tvMemberStatus.setText(context.getString(R.string.admin));



                adminViewHolder.rlSingleGroupMember.setOnLongClickListener(v -> {


                    if(myStatus.equals(MEMBER_STATUS_ADMIN)){

                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_group_admin_actions_on_admin, popup.getMenu());

                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {

                                case R.id.action_remove_admin:
                                    removeAdminNetworkCall(groupId, admin.getNewAdmin().getId(), admin, adminViewHolder.tvMemberStatus);
                                    break;

                                case R.id.action_make_moderator:
                                    makeModeratorNetworkCall(admin.getNewAdmin().getId(), admin, adminViewHolder.tvMemberStatus);
                                    break;
                            }

                            return false;
                        });

                        popup.show();


                    }

                    return false;
                });


                break;
            case VIEW_TYPE_MODERATOR:

                Log.d(TAG, "onBindViewHolder: VIEW_TYPE_MODERATOR");

                ModeratorViewHolder moderatorViewHolder = (ModeratorViewHolder) holder;

                ModeratorItem moderator = (ModeratorItem) membersItemList.get(position);

                if (!TextUtils.isEmpty(moderator.getNewMod().getFullName()))
                    moderatorViewHolder.tvUserFullName.setText(moderator.getNewMod().getFullName());
                if (!TextUtils.isEmpty(moderator.getNewMod().getModifiedProfileImage()))
                    picasso.load(moderator.getNewMod().getModifiedProfileImage()).into(moderatorViewHolder.ivUserImage);

                moderatorViewHolder.tvMemberStatus.setText(context.getString(R.string.moderator));

                

                moderatorViewHolder.rlSingleGroupMember.setOnLongClickListener(v -> {

                    if(myStatus.equals(MEMBER_STATUS_ADMIN)){
                        
                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_group_admin_actions_on_moderator, popup.getMenu());

                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {

                                case R.id.action_make_admin:

                                    makeAdminNetworkCall(moderator.getNewMod().getId(), moderator, moderatorViewHolder.tvMemberStatus);

                                    break;

                                case R.id.action_remove_moderator:
                                    removeModeratorNetworkCall(moderator.getNewMod().getId(), moderator, moderatorViewHolder.tvMemberStatus);
                                    break;
                            }

                            return false;
                        });
                        popup.show();



                    }else if(myStatus.equals(MEMBER_STATUS_MODERATOR)){

                        Toast.makeText(context, "Moderator can not do any action on another moderator!", Toast.LENGTH_SHORT).show();
                        

                    }
                    
                    return false;
                });

                break;
            case VIEW_TYPE_MEMBER:

                Log.d(TAG, "onBindViewHolder: VIEW_TYPE_MEMBER");

                MemberViewHolder memberViewHolder = (MemberViewHolder) holder;

                MembersItem member = (MembersItem) membersItemList.get(position);

                if (!TextUtils.isEmpty(member.getFullName()))
                    memberViewHolder.tvUserFullName.setText(member.getFullName());
                if (!TextUtils.isEmpty(member.getModifiedProfileImage()))
                    picasso.load(member.getModifiedProfileImage()).into(memberViewHolder.ivUserImage);

                memberViewHolder.tvMemberStatus.setText(context.getString(R.string.member));


                memberViewHolder.rlSingleGroupMember.setOnLongClickListener(v -> {

                    if(myStatus.equals(MEMBER_STATUS_ADMIN)){
                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_group_admin_actions_on_member, popup.getMenu());

                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {

                                case R.id.action_make_admin:

                                    makeAdminNetworkCall(member.getId(), member, memberViewHolder.tvMemberStatus);
                                    break;

                                case R.id.action_make_moderator:
                                    makeModeratorNetworkCall(member.getId(), member, memberViewHolder.tvMemberStatus);
                                    break;

                                case R.id.action_remove_member:
                                    removeMemberNetworkCall(member.getId());
                                    break;
                            }

                            return false;
                        });
                        popup.show();


                    }else if(myStatus.equals(MEMBER_STATUS_MODERATOR)){
                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_group_moderator_actions_on_member, popup.getMenu());

                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {

                                case R.id.action_remove_member:
                                    removeMemberNetworkCall(member.getId());
                                    break;
                            }

                            return false;
                        });
                        popup.show();
                    }

                    return false;
                });



                break;
        }


    }

    private void removeAdminNetworkCall(String groupId, String adminToBeDeletedId, AdminItem admin, AppCompatTextView tvMemberStatus) {
        Call<QxmApiResponse> deleteAdminNetworkCall = apiService.deleteAdmin(token.getToken(), groupId, adminToBeDeletedId);

        deleteAdminNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteAdminNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if(response.code() == 201){
                    tvMemberStatus.setText(context.getResources().getString(R.string.member));
                    moveUserToMember(admin);
                    Toast.makeText(context, context.getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                }else if(response.code() == 403){
                    Toast.makeText(context, context.getResources().getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveUserToMember(AdminItem admin) {
        MembersItem member = new MembersItem();

        member.setId(admin.getNewAdmin().getId());
        member.setFullName(admin.getNewAdmin().getFullName());
        member.setProfileImage(admin.getNewAdmin().getProfileImage());

        for (Object object : membersItemList) {
            if(object instanceof AdminItem && ((AdminItem) object).getNewAdmin().getId().equals(admin.getNewAdmin().getId())){
                membersItemList.remove(object);
                break;
            }
        }
        membersItemList.add(member);
        notifyDataSetChanged();
    }

    private void removeModeratorNetworkCall(String moderatorToBeDeletedId, ModeratorItem moderator, AppCompatTextView tvMemberStatus) {
        Call<QxmApiResponse> deleteModerator = apiService.deleteModerator(token.getToken(), groupId, moderatorToBeDeletedId);

        deleteModerator.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: removeModeratorNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if(response.code() == 201){
                    tvMemberStatus.setText(context.getResources().getString(R.string.member));
                    moveUserToMember(moderator);
                    Toast.makeText(context, "Moderator removed successfully", Toast.LENGTH_SHORT).show();


                }else if(response.code() == 403){
                    Toast.makeText(context, context.getResources().getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveUserToMember(ModeratorItem moderator) {
        MembersItem member = new MembersItem();

        member.setId(moderator.getNewMod().getId());
        member.setFullName(moderator.getNewMod().getFullName());
        member.setProfileImage(moderator.getNewMod().getProfileImage());

        for (Object object : membersItemList) {
            if(object instanceof ModeratorItem && ((ModeratorItem) object).getNewMod().getId().equals(moderator.getNewMod().getId())){
                membersItemList.remove(object);
                break;
            }
        }
        membersItemList.add(member);
        notifyDataSetChanged();
    }


    private void makeAdminNetworkCall(String userId, Object  memberItem, AppCompatTextView tvMemberStatus) {


        Call<QxmApiResponse> addAdminToGroup = apiService.addAdminToGroup(token.getToken(),userId, groupId,  token.getUserId());

        addAdminToGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                if (response.code() == 201) {
                    tvMemberStatus.setText(context.getResources().getString(R.string.admin));

                    moveUserToAdminList(memberItem);

                    Toast.makeText(context, "User added as admin.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void moveUserToAdminList(Object memberItem) {
        if(memberItem instanceof MembersItem){
            MembersItem item = (MembersItem) memberItem;
            AdminItem adminItem = new AdminItem();
            NewAdmin newAdmin = new NewAdmin();

            newAdmin.setId(item.getId());
            newAdmin.setFullName(item.getFullName());
            newAdmin.setProfileImage(item.getProfileImage());
            adminItem.setNewAdmin(newAdmin);

            for (Object object : membersItemList) {
                if(object instanceof MembersItem && ((MembersItem) object).getId().equals(item.getId())){
                    membersItemList.remove(object);
                    break;
                }
            }
            membersItemList.add(adminItem);
            notifyDataSetChanged();

        }else if(memberItem instanceof ModeratorItem){

            ModeratorItem item = (ModeratorItem) memberItem;
            AdminItem adminItem = new AdminItem();
            NewAdmin newAdmin = new NewAdmin();

            newAdmin.setId(item.getNewMod().getId());
            newAdmin.setFullName(item.getNewMod().getFullName());
            newAdmin.setProfileImage(item.getNewMod().getProfileImage());
            adminItem.setNewAdmin(newAdmin);

            for (Object object : membersItemList) {
                if(object instanceof ModeratorItem && ((ModeratorItem) object).getNewMod().getId().equals(item.getNewMod().getId())){
                    membersItemList.remove(object);
                    break;
                }
            }
            membersItemList.add(adminItem);
            notifyDataSetChanged();
        }
    }

    private void makeModeratorNetworkCall(String userId, Object memberItem, AppCompatTextView tvMemberStatus) {

        Call<QxmApiResponse> addModeratorToGroup = apiService.addModeratorToGroup(token.getToken(), userId, groupId, token.getUserId());

        addModeratorToGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                if (response.code() == 201) {
                    tvMemberStatus.setText(context.getResources().getString(R.string.moderator));
                    moveUserToModList(memberItem);
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveUserToModList(Object memberItem) {
        if(memberItem instanceof MembersItem){
            MembersItem item = (MembersItem) memberItem;
            ModeratorItem moderatorItem = new ModeratorItem();
            NewMod newMod = new NewMod();

            newMod.setId(item.getId());
            newMod.setFullName(item.getFullName());
            newMod.setProfileImage(item.getProfileImage());
            moderatorItem.setNewMod(newMod);

            for (Object object : membersItemList) {
                if(object instanceof MembersItem && ((MembersItem) object).getId().equals(item.getId())){
                    membersItemList.remove(object);
                    break;
                }
            }
            membersItemList.add(moderatorItem);
            notifyDataSetChanged();

        }else if(memberItem instanceof AdminItem){

            AdminItem item = (AdminItem) memberItem;
            ModeratorItem moderatorItem = new ModeratorItem();
            NewMod newMod = new NewMod();

            newMod.setId(item.getNewAdmin().getId());
            newMod.setFullName(item.getNewAdmin().getFullName());
            newMod.setProfileImage(item.getNewAdmin().getProfileImage());
            moderatorItem.setNewMod(newMod);

            for (Object object : membersItemList) {
                if(object instanceof AdminItem && ((AdminItem) object).getNewAdmin().getId().equals(item.getNewAdmin().getId())){
                    membersItemList.remove(object);
                    break;
                }
            }
            membersItemList.add(moderatorItem);
            notifyDataSetChanged();
        }
    }

    private void removeMemberNetworkCall(String memberId) {
        Call<QxmApiResponse> deleteAGroupMember = apiService.deleteAGroupMember(token.getToken(), groupId, memberId);

        deleteAGroupMember.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                if (response.code() == 201) {
                    isMyQxmGroupFragmentRefreshNeeded = true;
                    isViewQxmGroupFragmentRefreshNeeded = true;
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return membersItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (membersItemList.get(position) instanceof AdminItem)
            return VIEW_TYPE_ADMIN;
        else if (membersItemList.get(position) instanceof ModeratorItem)
            return VIEW_TYPE_MODERATOR;
        else if(membersItemList.get(position) instanceof MembersItem)
            return VIEW_TYPE_MEMBER;
        else
            return -1;
    }

    // Clean all elements of the recycler view
    public void clear() {

        membersItemList.clear();
        notifyDataSetChanged();
    }

    //region AdminViewHolder
    class AdminViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlSingleGroupMember)
        RelativeLayout rlSingleGroupMember;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.tvMemberStatus)
        AppCompatTextView tvMemberStatus;

        AdminViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    //endregion

    //region ModeratorViewHolder
    class ModeratorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlSingleGroupMember)
        RelativeLayout rlSingleGroupMember;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.tvMemberStatus)
        AppCompatTextView tvMemberStatus;

        ModeratorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
    //endregion

    //region MemberViewHolder

    class MemberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlSingleGroupMember)
        RelativeLayout rlSingleGroupMember;
        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;
        @BindView(R.id.tvMemberStatus)
        AppCompatTextView tvMemberStatus;

        MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
    //endregion
}
