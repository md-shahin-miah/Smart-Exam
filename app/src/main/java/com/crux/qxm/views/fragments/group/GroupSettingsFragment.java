package com.crux.qxm.views.fragments.group;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.GroupSettings;
import com.crux.qxm.db.models.group.groupSettings.GroupSettingsContainer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.groupSettingsFeature.DaggerGroupSettingsFragmentComponent;
import com.crux.qxm.di.groupSettingsFeature.GroupSettingsFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.FREQUENTLY;
import static com.crux.qxm.utils.StaticValues.GROUP_ID_KEY;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_ADMIN;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_MODERATOR;
import static com.crux.qxm.utils.StaticValues.OCCASIONALLY;
import static com.crux.qxm.utils.StaticValues.OFF;
import static com.crux.qxm.utils.StaticValues.PRIVACY_PUBLIC;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSettingsFragment extends Fragment {

    // region Fragment-Properties

    private static final String TAG = "GroupSettingsFragment";

    @Inject
    Retrofit retrofit;

    private String groupId;
    private String memberStatus;
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private GroupSettingsContainer settingsContainer;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.ivSave)
    AppCompatImageView ivSave;

    @BindView(R.id.rlJoinRequestNeeded)
    RelativeLayout rlJoinRequestNeeded;
    @BindView(R.id.rlMemberCanAddOther)
    RelativeLayout rlMemberCanAddOther;
    @BindView(R.id.rlMembersAreVisibleToOther)
    RelativeLayout rlMembersAreVisibleToOther;
    @BindView(R.id.rlGroupPrivacy)
    RelativeLayout rlGroupPrivacy;

    @BindView(R.id.switchJoinRequestNeeded)
    SwitchCompat switchJoinRequestNeeded;
    @BindView(R.id.switchMemberCanAddOther)
    SwitchCompat switchMemberCanAddOther;
    @BindView(R.id.switchMemberVisibleToOther)
    SwitchCompat switchMemberVisibleToOther;

    @BindView(R.id.spinnerNotificationFrequency)
    AppCompatSpinner spinnerNotificationFrequency;
    @BindView(R.id.spinnerGroupPrivacy)
    AppCompatSpinner spinnerGroupPrivacy;
    @BindView(R.id.btnLeaveGroup)
    AppCompatButton btnLeaveGroup;
    @BindView(R.id.btnDeleteGroup)
    AppCompatButton btnDeleteGroup;

    // endregion

    // region Fragment-Constructor
    public GroupSettingsFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Fragment-newInstance

    public static GroupSettingsFragment newInstance(String groupId, String memberStatus) {
        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);
        args.putString(MEMBER_STATUS, memberStatus);

        GroupSettingsFragment fragment = new GroupSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_settings, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
    // endregion

    // region Override-OnViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        groupId = getArguments() != null ? getArguments().getString(GROUP_ID_KEY) : null;
        memberStatus = getArguments() != null ? getArguments().getString(MEMBER_STATUS) : null;

        context = view.getContext();

        setUpDagger2(context);

        init();

        setUserTypeAccessLevelViews(memberStatus);

        if (settingsContainer == null)
            getGroupSettingsNetworkCall(groupId);
    }

    private void setUserTypeAccessLevelViews(String memberStatus) {

        switch(memberStatus){

            case MEMBER_STATUS_ADMIN:


                break;

            case MEMBER_STATUS_MODERATOR:
                rlJoinRequestNeeded.setVisibility(View.GONE);
                rlMemberCanAddOther.setVisibility(View.GONE);
                rlMembersAreVisibleToOther.setVisibility(View.GONE);
                rlGroupPrivacy.setVisibility(View.GONE);
                btnDeleteGroup.setVisibility(View.GONE);
                break;

            default:
                rlJoinRequestNeeded.setVisibility(View.GONE);
                rlMemberCanAddOther.setVisibility(View.GONE);
                rlMembersAreVisibleToOther.setVisibility(View.GONE);
                rlGroupPrivacy.setVisibility(View.GONE);
                btnDeleteGroup.setVisibility(View.GONE);
                break;
        }

    }

    private void getGroupSettingsNetworkCall(String groupId) {
        Call<GroupSettingsContainer> getGroupSettings = apiService.getGroupSettings(token.getToken(), groupId);

        getGroupSettings.enqueue(new Callback<GroupSettingsContainer>() {
            @Override
            public void onResponse(@NonNull Call<GroupSettingsContainer> call, @NonNull Response<GroupSettingsContainer> response) {
                Log.d(TAG, "onResponse: getGroupSettingsNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {

                        settingsContainer = response.body();
                        Log.d(TAG, "onResponse: settingsContainer: " + settingsContainer.toString());
                        loadDataInToViews(settingsContainer.getGroupSettings());
                    } else {
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<GroupSettingsContainer> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getGroupSettingsNetworkCall");
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // region LoadDataInToViews

    private void loadDataInToViews(GroupSettings groupSettings) {

        if (groupSettings.isJoinRequestNeeded()) switchJoinRequestNeeded.setChecked(true);
        else switchJoinRequestNeeded.setChecked(false);

        if (groupSettings.isMemberCanAddOther()) switchMemberCanAddOther.setChecked(true);
        else switchMemberCanAddOther.setChecked(false);

        if (groupSettings.isMemberVisibleToOther()) switchMemberVisibleToOther.setChecked(true);
        else switchMemberVisibleToOther.setChecked(false);

        switch (groupSettings.getNotificationFrequency().toLowerCase()) {
            case FREQUENTLY:
                spinnerNotificationFrequency.setSelection(0);
                break;

            case OCCASIONALLY:
                spinnerNotificationFrequency.setSelection(1);
                break;

            case OFF:
                spinnerNotificationFrequency.setSelection(2);
                break;
        }

        if (groupSettings.getPrivacy().toLowerCase().equals(PRIVACY_PUBLIC))
            spinnerGroupPrivacy.setSelection(0);
        else
            spinnerGroupPrivacy.setSelection(1);

    }
    // endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {
        GroupSettingsFragmentComponent groupSettingsFragmentComponent =
                DaggerGroupSettingsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        groupSettingsFragmentComponent.injectGroupSettingsFragmentFeature(GroupSettingsFragment.this);
    }
    // endregion

    // region Init

    private void init() {
        apiService = retrofit.create(QxmApiService.class);

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        initializeClickListeners();
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        ivSave.setOnClickListener(v -> {
            saveGroupSettingsNetworkCall();
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        btnLeaveGroup.setOnClickListener(v -> {
            QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);
            boolean leaveForever = false;

            qxmAlertDialogBuilder.setMessage("Are you sure, you really want to leave this group!");
            qxmAlertDialogBuilder.setTitle("Leave Group");
            qxmAlertDialogBuilder.setIcon(R.drawable.ic_logout_variant_grey);

            qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton("LEAVE", (dialog, which) ->
                    leaveGroupNetworkCall(token.getToken(), groupId, token.getUserId(), leaveForever));

            qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton("NO", null);

            qxmAlertDialogBuilder.getAlertDialogBuilder().show();

        });

        btnDeleteGroup.setOnClickListener(v -> {

            QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);

            qxmAlertDialogBuilder.setMessage("Are you sure, you really want to delete this group!");
            qxmAlertDialogBuilder.setTitle("Delete Group");
            qxmAlertDialogBuilder.setIcon(R.drawable.ic_delete_black);

            qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton("DELETE", (dialog, which) ->
                    deleteGroupNetworkCall(token.getToken(), token.getUserId(), groupId));

            qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton("NO", null);

            qxmAlertDialogBuilder.getAlertDialogBuilder().show();
        });
    }


    private void leaveGroupNetworkCall(String token, String groupId, String userId, boolean leaveForever) {
        Call<QxmApiResponse> leaveGroup = apiService.leaveGroup(token, groupId, userId, leaveForever);

        leaveGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: leaveGroupNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if(response.code() == 201){
                    //viewPagerPosition = 3 (Group)
                    qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(3);
                }else if(response.code() == 400){
                    Toast.makeText(context, "You can not leave this group right now. First you have to make some one admin and try again.", Toast.LENGTH_LONG).show();
                }else if(response.code() == 403){
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }
                else{
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: leaveGroupNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteGroupNetworkCall(String token, String adminId, String groupId) {

        Call<QxmApiResponse> deleteGroup = apiService.deleteGroup(token, groupId, adminId);

        deleteGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteGroupNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if(response.code() == 201){
                    Toast.makeText(context, "Group deleted successfully", Toast.LENGTH_SHORT).show();
                    //viewPagerPosition = 3 (Group)
                    qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(3);

                }else if(response.code() == 403){
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }else{
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteGroupNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    // region SaveGroupSettingsNetworkCall

    private void saveGroupSettingsNetworkCall() {

        settingsContainer.setGroupSettings(getSettingsFromUI());

        Log.d(TAG, "saveGroupSettingsNetworkCall: GroupSettings: " + settingsContainer.toString());

        Log.d(TAG, "saveGroupSettingsNetworkCall: GroupId: " + groupId);

        Call<QxmApiResponse> updateGroupSettings = apiService.updateGroupSettings(token.getToken(), groupId, token.getUserId(), settingsContainer);

        updateGroupSettings.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: Update Group GroupSettingsContainer network call");
                Log.d(TAG, "onResponse: response code: " + response.code());
                Log.d(TAG, "onResponse: response body" + response.body());

                if (response.code() == 201) {
                    Toast.makeText(context, "Group settings updated successfully.", Toast.LENGTH_SHORT).show();
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Update Group GroupSettingsContainer network call");
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // endregion

    // region GetSettingsFromUI

    private GroupSettings getSettingsFromUI() {
        GroupSettings groupSettings = new GroupSettings();

        groupSettings.setJoinRequestNeeded(switchJoinRequestNeeded.isChecked());
        groupSettings.setMemberCanAddOther(switchMemberCanAddOther.isChecked());
        groupSettings.setMemberVisibleToOther(switchMemberVisibleToOther.isChecked());
        groupSettings.setNotificationFrequency(spinnerNotificationFrequency.getSelectedItem().toString());
        groupSettings.setPrivacy(spinnerGroupPrivacy.getSelectedItem().toString());

        return groupSettings;
    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    // endregion

    // region Override-OnDestroyView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    // endregion
}
