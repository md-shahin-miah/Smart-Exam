package com.crux.qxm.adapter.following;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.following.FollowingUser;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FOLLOWING_NOTIFICATION_FREQUENTLY;
import static com.crux.qxm.utils.StaticValues.FOLLOWING_NOTIFICATION_NONE;
import static com.crux.qxm.utils.StaticValues.FOLLOWING_NOTIFICATION_OCCASIONALLY;
import static com.crux.qxm.utils.StaticValues.FOLLOWING_NOTIFICATION_OFF;

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.ViewHolder> {

    private static final String TAG = "FollowingListAdapter";
    private Context context;
    private Picasso picasso;
    private List<FollowingUser> followingUserList;
    private String token;
    private QxmApiService apiService;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public FollowingListAdapter(Context context, FragmentActivity fragmentActivity, Picasso picasso, List<FollowingUser> followingUserList, String token, QxmApiService apiService) {
        this.context = context;
        this.picasso = picasso;
        this.followingUserList = followingUserList;
        this.token = token;
        this.apiService = apiService;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View followingUserListView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_following_list_layout, parent, false
        );
        return new FollowingListAdapter.ViewHolder(followingUserListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowingUser user = followingUserList.get(position);

        Log.d(TAG, "onBindViewHolder: User = " + user.toString());

        // region LoadUserProfileImage
        if (!TextUtils.isEmpty(user.getFollowing().getModifiedProfileImage())) {
            Log.d(TAG, "onBindViewHolder: loading userImage: " + user.getFollowing().getModifiedProfileImage());
            picasso.load(user.getFollowing().getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        } else {
            Log.d(TAG, "onBindViewHolder: userImage null. loading default image.");
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        }
        // endregion

        // region SetUserFullName
        if (!TextUtils.isEmpty(user.getFollowing().getFullName())) {
            holder.tvUserFullName.setText(user.getFollowing().getFullName());
        }else
            holder.tvUserFullName.setText("");
        // endregion

        // region ViewUserProfile--gotoProfileFragment
        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getFollowing().getUserId(),user.getFollowing().getFullName()));


        // endregion

        // region SetFollowingNotificationOfTheUser

        if (!TextUtils.isEmpty(user.getNotification())) {
            //String notificationStatus = user.getNotification().toLowerCase();

            switch (user.getNotification()) {
                case FOLLOWING_NOTIFICATION_OCCASIONALLY:
                    holder.ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                            R.drawable.ic_following_notification
                    ));
                    break;
                case FOLLOWING_NOTIFICATION_FREQUENTLY:
                    holder.ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                            R.drawable.ic_following_notification_frequently
                    ));
                    break;
                case FOLLOWING_NOTIFICATION_NONE:
                case FOLLOWING_NOTIFICATION_OFF:
                    holder.ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                            R.drawable.ic_following_notification_off
                    ));
                    break;
            }

        }

        // endregion

        // region SetClickListenerTOIvFollowingNotification

        holder.ivFollowingNotification.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(followingUserList.get(position).getFollowingId())
                    && !TextUtils.isEmpty(followingUserList.get(position).getFollowing().getFullName())
                    && !TextUtils.isEmpty(followingUserList.get(position).getNotification())) {

                showSetNotificationTypeDialog(v.getRootView(), holder.ivFollowingNotification, followingUserList.get(position));
            } else {
                Toast.makeText(context, "Changing notification settings is not available now, please try again after sometime.", Toast.LENGTH_SHORT).show();
            }

        });

        // endregion
    }


    // region showSetNotificationTypeDialog

    private void showSetNotificationTypeDialog(View rootView, AppCompatImageView ivFollowingNotification, FollowingUser followingUser) {
        String followingId = followingUser.getFollowingId();
        String userFullName = followingUser.getFollowing().getFullName();
        String notification = followingUser.getNotification();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_notification_type_selection, (ViewGroup) rootView, false);
        AppCompatTextView tvDialogTitle = view.findViewById(R.id.tvUserFullName);
        RadioGroup radioGroup = view.findViewById(R.id.rgNotification);

        RadioButton radioNotifyAll = view.findViewById(R.id.radioNotifyAll);
        RadioButton radioNotifyOccasionally = view.findViewById(R.id.radioNotifyOccasionally);
        RadioButton radioNotifyNone = view.findViewById(R.id.radioNotifyNone);

        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);
        AppCompatButton btnOk = view.findViewById(R.id.btnOk);

        tvDialogTitle.setText(String.format("Notifications from %s", userFullName));

        switch (notification) {
            case FOLLOWING_NOTIFICATION_FREQUENTLY:
                radioNotifyAll.setChecked(true);
                break;

            case FOLLOWING_NOTIFICATION_OCCASIONALLY:
                radioNotifyOccasionally.setChecked(true);
                break;

            case FOLLOWING_NOTIFICATION_NONE:
                 FOLLOWING_NOTIFICATION_OFF:
                radioNotifyNone.setChecked(true);
                break;

        }

        Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioNotifyAll:
                    Log.d(TAG, "checkedId: radioNotifyAll");
                    break;

                case R.id.radioNotifyOccasionally:
                    Log.d(TAG, "checkedId: radioNotifyOccasionally");
                    break;

                case R.id.radioNotifyNone:
                    Log.d(TAG, "checkedId: radioNotifyNone");
                    break;
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnOk.setOnClickListener(v -> {

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radioNotifyAll:
                    followingUser.setNotification(FOLLOWING_NOTIFICATION_FREQUENTLY);
                    changeFollowingUserNotificationSettingsNetworkCall(followingId, FOLLOWING_NOTIFICATION_FREQUENTLY);
                    updateNotificationSettingsInUI(FOLLOWING_NOTIFICATION_FREQUENTLY, ivFollowingNotification);
                    Toast.makeText(context, "You will get all notification from this user.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.radioNotifyOccasionally:
                    followingUser.setNotification(FOLLOWING_NOTIFICATION_OCCASIONALLY);
                    changeFollowingUserNotificationSettingsNetworkCall(followingId, FOLLOWING_NOTIFICATION_OCCASIONALLY);
                    updateNotificationSettingsInUI(FOLLOWING_NOTIFICATION_OCCASIONALLY, ivFollowingNotification);
                    Toast.makeText(context, "You will get notification occasionally from this user.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.radioNotifyNone:
                    followingUser.setNotification(FOLLOWING_NOTIFICATION_NONE);
                    changeFollowingUserNotificationSettingsNetworkCall(followingId, FOLLOWING_NOTIFICATION_NONE);
                    updateNotificationSettingsInUI(FOLLOWING_NOTIFICATION_NONE, ivFollowingNotification);
                    Toast.makeText(context, "You will not get any notification from this user.", Toast.LENGTH_SHORT).show();
                    break;
            }
            dialog.dismiss();
        });

        dialog.show();

    }

    // endregion

    private void changeFollowingUserNotificationSettingsNetworkCall(String followingId, String notification) {
        Call<QxmApiResponse> changeSpecificUserFollowingNotificationSettings =
                apiService.changeSpecificUserFollowingNotificationSettings(token, followingId, notification);

        changeSpecificUserFollowingNotificationSettings.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: success");
                    Toast.makeText(context, "Notification settings changed.", Toast.LENGTH_SHORT).show();

                }else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                }  else {
                    Log.d(TAG, "onResponse: response code error");
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: LocalizedMessage" + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: StackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, "Network error. Please  try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNotificationSettingsInUI(String notification, AppCompatImageView ivFollowingNotification) {
        switch (notification) {
            case FOLLOWING_NOTIFICATION_OCCASIONALLY:
                ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.ic_following_notification
                ));
                break;
            case FOLLOWING_NOTIFICATION_FREQUENTLY:
                ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.ic_following_notification_frequently
                ));
                break;
            case FOLLOWING_NOTIFICATION_NONE:
                ivFollowingNotification.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.ic_following_notification_off
                ));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return followingUserList.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        followingUserList.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView ivUserImage;
        public AppCompatTextView tvUserFullName;
        public AppCompatImageView ivFollowingNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvUserFullName = itemView.findViewById(R.id.tvUserFullName);
            ivFollowingNotification = itemView.findViewById(R.id.ivFollwoingNotification);
        }
    }
}
