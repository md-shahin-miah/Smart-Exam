package com.crux.qxm.views.fragments.group;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.group.GroupFeedAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.groupFeed.Group;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.viewQxmGroupFragmentFeature.DaggerViewQxmGroupFragmentComponent;
import com.crux.qxm.di.viewQxmGroupFragmentFeature.ViewQxmGroupFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.QxmCopyLinkToClipboard;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.utils.qxmDialogs.QxmReportDialog;
import com.crux.qxm.utils.qxmDialogs.QxmShowQRCodeDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.GROUP_ID_KEY;
import static com.crux.qxm.utils.StaticValues.GROUP_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.GROUP_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_ADMIN;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_MEMBER;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_MODERATOR;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_NOT_MEMBER;
import static com.crux.qxm.utils.StaticValues.MEMBER_STATUS_REQUEST_PENDING;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE;
import static com.crux.qxm.utils.StaticValues.QR_CODE_FOR_GROUP;
import static com.crux.qxm.utils.StaticValues.REPORT_GROUP;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isViewQxmGroupFragmentRefreshNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewQxmGroupFragment extends Fragment implements GroupFeedAdapter.GroupFeedAdapterListener {

    // region ViewQxmGroupFragment-Properties

    private static final String TAG = "ViewQxmGroupFragment";

    private String groupId;

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    private QxmApiService apiService;

    private Context context;
    private UserBasic user;
    private QxmToken token;
    private Group groupData;
    // feed
    private List<Object> feedModelList;
    private GroupFeedAdapter groupFeedAdapter;
    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;
    @BindView(R.id.llViewQxmGroupFragment)
    LinearLayoutCompat llViewQxmGroupFragment;
    @BindView(R.id.ivGroupImage)
    AppCompatImageView ivGroupImage;
    @BindView(R.id.tvGroupName)
    AppCompatTextView tvGroupName;
    @BindView(R.id.llJoin)
    LinearLayoutCompat llJoin;
    @BindView(R.id.tvJoinGroup)
    AppCompatTextView tvJoinGroup;
    @BindView(R.id.llAddMember)
    LinearLayoutCompat llAddMember;
    @BindView(R.id.llPendingRequestList)
    LinearLayoutCompat llPendingRequestList;
    @BindView(R.id.llOtherOptions)
    LinearLayoutCompat llOtherOptions;
    @BindView(R.id.tvMemberStatus)
    AppCompatTextView tvMemberStatus;
    @BindView(R.id.llMemberCount)
    LinearLayoutCompat llMemberCount;
    @BindView(R.id.tvMemberCount)
    AppCompatTextView tvMemberCount;
    @BindView(R.id.tvQxmCount)
    AppCompatTextView tvQxmCount;
    @BindView(R.id.tvGroupDescription)
    AppCompatTextView tvGroupDescription;
    @BindView(R.id.tvNoFeedEmptyMessage)
    AppCompatTextView tvNoFeedEmptyMessage;
    @BindView(R.id.rvGroupFeed)
    RecyclerView rvGroupFeed;
    private String groupName;
    @BindView(R.id.llChangeImage)
    LinearLayoutCompat llChangeImage;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;
    @BindView(R.id.tvGroupPrivacy)
    AppCompatTextView tvGroupPrivacy;
    @BindView(R.id.ivGroupPrivacyIcon)
    AppCompatImageView ivGroupPrivacyIcon;

    private ImageProcessor imageProcessorForGroupImageChange;

    // endregion

    // region ViewQxmGroupFragment-Constructor

    public ViewQxmGroupFragment() {
        // Required empty public constructor
    }

    // endregion

    //region ViewQxmGroupFragment-NewInstance
    public static ViewQxmGroupFragment newInstance(String groupId, String groupName) {

        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);
        args.putString(GROUP_NAME_KEY, groupName);

        ViewQxmGroupFragment fragment = new ViewQxmGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_qxm_group, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        groupId = getArguments() != null ? getArguments().getString(GROUP_ID_KEY) : null;
        groupName = getArguments() != null ? getArguments().getString(GROUP_NAME_KEY) : null;

        Log.d(TAG, "onViewCreated: GroupId: " + groupId);
        Log.d(TAG, "onViewCreated: GroupName: " + groupName);

        if (!TextUtils.isEmpty(groupName)) tvToolbarTitle.setText(groupName);

        context = view.getContext();

        setUpDagger2();

        init();


        if (groupData == null || isViewQxmGroupFragmentRefreshNeeded) {
            isViewQxmGroupFragmentRefreshNeeded = false;
            llViewQxmGroupFragment.setVisibility(View.GONE);
            getSpecificGroupDataNetworkCall();
        } else {
            llViewQxmGroupFragment.setVisibility(View.VISIBLE);
            loadGroupDataIntoViews();
        }

        initializeClickListeners();

    }


    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        ViewQxmGroupFragmentComponent viewQxmGroupFragmentComponent =
                DaggerViewQxmGroupFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        viewQxmGroupFragmentComponent.injectViewQxmGroupFragmentFeature(ViewQxmGroupFragment.this);
    }

    // endregion

    // region Init
    private void init() {

        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);

        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        if (feedModelList == null)
            feedModelList = new ArrayList<>();

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        initializeAdapter();

    }
    // endregion

    //region GetSpecificGroupDataNetworkCall
    private void getSpecificGroupDataNetworkCall() {
//         groupData = GroupDataSampleModel.getSampleGroupData();
//        Log.d(TAG, "getSpecificGroupDataNetworkCall: " + groupData.toString());

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        if (!TextUtils.isEmpty(groupName))
            dialog.showProgressDialog("Group", String.format("Loading %s group information...", groupName), false);
        else
            dialog.showProgressDialog("Group", "Loading group information...", false);
        Call<Group> getSpecificGroupData = apiService.getSpecificGroupData(token.getToken(), groupId, token.getUserId());

        getSpecificGroupData.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                Log.d(TAG, "onResponse: getSpecificGroupDataNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        groupData = response.body();
                        groupFeedAdapter.setMemberStatus(groupData.getMyStatus());
                        loadGroupDataIntoViews();
                        if (!groupData.getMyStatus().equals("NotMember")) {
                            if (groupData.getGroupFeed().size() > 0) {
                                tvNoFeedEmptyMessage.setVisibility(View.GONE);
                                feedModelList.clear();
                                feedModelList.addAll(groupData.getGroupFeed());
                                groupFeedAdapter.notifyDataSetChanged();
                                rvGroupFeed.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: adapter updated..");
                            } else {
                                tvNoFeedEmptyMessage.setVisibility(View.VISIBLE);
                            }
                        } else rvGroupFeed.setVisibility(View.GONE);



                        llViewQxmGroupFragment.setVisibility(View.VISIBLE);

                    } else {
                        Log.d(TAG, "onResponse: Error: response body null");

                        Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: Error: Response code is not correct. please check the response code.");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

                dialog.closeProgressDialog();
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getSpecificGroupDataNetworkCall");
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region LoadGroupDataIntoViews
    private void loadGroupDataIntoViews() {
        tvToolbarTitle.setText(groupData.getGroupInfo().getGroupName());
        if (!TextUtils.isEmpty(groupData.getGroupInfo().getGroupImageUrl()))
            picasso.load(groupData.getGroupInfo().getModifiedGroupImageUrl()).into(ivGroupImage);

        tvGroupName.setText(groupData.getGroupInfo().getGroupName());
        tvMemberStatus.setText(groupData.getMyStatus());

        if (groupData.getGroupInfo().getGroupSettings().getPrivacy().equals(GROUP_PRIVACY_PUBLIC)) {
            tvGroupPrivacy.setText(getText(R.string.group_public_text));
            ivGroupPrivacyIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_public));
        } else {
            tvGroupPrivacy.setText(getText(R.string.group_private_text));
            ivGroupPrivacyIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_private));
        }
        tvGroupDescription.setText(groupData.getGroupInfo().getGroupDescription());
        SetlinkClickable.setLinkclickEvent(tvGroupDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });
        tvMemberCount.setText(groupData.getMemberCount());
        tvQxmCount.setText(groupData.getQxmCount());

        if (!TextUtils.isEmpty(groupData.getMyStatus())) {
            if (groupData.getMyStatus().equals(MEMBER_STATUS_ADMIN)
                    || groupData.getMyStatus().equals(MEMBER_STATUS_MODERATOR)
                    || groupData.getMyStatus().equals(MEMBER_STATUS_MEMBER)) {
                tvJoinGroup.setText(getString(R.string.invite_people));
            } else {
                tvJoinGroup.setText(getString(R.string.join));
            }
        }

        if ((groupData.getMyStatus().equals(MEMBER_STATUS_ADMIN)
                || groupData.getMyStatus().equals(MEMBER_STATUS_MODERATOR)
        ) && groupData.getGroupInfo().getGroupSettings().isIsMemberCanAddOther()) {
            llAddMember.setVisibility(View.VISIBLE);
            llJoin.setVisibility(View.GONE);
        } else {
            llAddMember.setVisibility(View.GONE);
            llJoin.setVisibility(View.VISIBLE);
        }

        //----- USER NOT A MEMBER -----//
        if (groupData.getMyStatus().equals(MEMBER_STATUS_REQUEST_PENDING)) {
            tvJoinGroup.setText(R.string.request_pending);
            llPendingRequestList.setVisibility(View.GONE);
        } else if (groupData.getMyStatus().equals(MEMBER_STATUS_NOT_MEMBER)) {
            llPendingRequestList.setVisibility(View.GONE);
        }

    }
    //endregion

    //region InitializeAdapter
    private void initializeAdapter() {
        groupFeedAdapter = new GroupFeedAdapter(context, groupId, feedModelList, getActivity(), picasso, user.getUserId(), token.getToken(), apiService, this);
        rvGroupFeed.setAdapter(groupFeedAdapter);
        rvGroupFeed.setLayoutManager(new LinearLayoutManager(context));
        rvGroupFeed.setNestedScrollingEnabled(false);
    }
    //endregion

    // region InitializeClickListeners
    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        //ivGroupImage.setOnClickListener(v -> Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show());

        llJoin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(groupData.getMyStatus())) {
                switch (groupData.getMyStatus()) {
                    case MEMBER_STATUS_ADMIN:

                    case MEMBER_STATUS_MODERATOR:
                        qxmFragmentTransactionHelper.loadGroupJoinRequestSentFragment(groupData.getId());
                        break;
                    case MEMBER_STATUS_MEMBER:
                        Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (groupData.getMyStatus().equals(MEMBER_STATUS_NOT_MEMBER)) {

                            sendJoinRequestToGroupNetworkCall(token.getToken(), token.getUserId(), groupId);
                            groupData.setMyStatus(MEMBER_STATUS_REQUEST_PENDING);
                            tvJoinGroup.setText(R.string.request_pending);
                        } else if (groupData.getMyStatus().equals(MEMBER_STATUS_REQUEST_PENDING)) {
                            cancelGroupJoinRequestNetworkCall(groupId);
                            groupData.setMyStatus(MEMBER_STATUS_NOT_MEMBER);
                            tvJoinGroup.setText(R.string.join);
                        }
                        break;
                }
            }

        });

        llAddMember.setOnClickListener(v -> qxmFragmentTransactionHelper.loadAddMembersToGroupFragment(groupId));

        llPendingRequestList.setOnClickListener(v -> qxmFragmentTransactionHelper.loadGroupPendingRequestListFragment(groupId));

        llOtherOptions.setOnClickListener(v -> showGroupBottomSheetView());

        llMemberCount.setOnClickListener(v -> qxmFragmentTransactionHelper.loadGroupMemberListFragment(groupData.getId(), groupData.getMyStatus()));

        llChangeImage.setOnClickListener(v -> initializeGroupPhotoChangingProcess());
    }
    // endregion

    //region SendJoinRequestToGroupNetworkCall
    private void sendJoinRequestToGroupNetworkCall(String token, String userId, String groupId) {


        Call<QxmApiResponse> sendJoinRequestToGroup = apiService.sendJoinRequestToGroup(token, userId, groupId);

        sendJoinRequestToGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: sendJoinRequestToGroupNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();

                } else {
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: sendJoinRequestToGroupNetworkCall");
                Log.d(TAG, "onFailure: getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region CancelGroupJoinRequestNetworkCall
    private void cancelGroupJoinRequestNetworkCall(String groupId) {
        Call<QxmApiResponse> cancelGroupJoinRequestNetworkCall = apiService.cancelGroupJoinRequest(token.getToken(), groupId, token.getUserId());

        cancelGroupJoinRequestNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: cancelGroupJoinRequestNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getResources().getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: cancelGroupJoinRequestNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region ShowGroupAdminBottomSheetView

    private void showGroupBottomSheetView() {

        QxmUrlHelper urlHelper = new QxmUrlHelper();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_group_other_options, (ViewGroup) getView(), false);
        bottomSheetDialog.setContentView(bottomSheetView);
        //bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        AppCompatTextView tvGenerateQRCode = bottomSheetView.findViewById(R.id.tvGenerateQRCode);
        AppCompatTextView tvCopyLink = bottomSheetView.findViewById(R.id.tvCopyLink);
        AppCompatTextView tvSettings = bottomSheetView.findViewById(R.id.tvSettings);
        AppCompatTextView tvReport = bottomSheetView.findViewById(R.id.tvReport);

        if (groupData != null && !TextUtils.isEmpty(groupData.getMyStatus())) {

            switch (groupData.getMyStatus()) {
                case MEMBER_STATUS_ADMIN:
                    tvReport.setVisibility(View.GONE);

                    break;
                case MEMBER_STATUS_MEMBER:
                    tvReport.setVisibility(View.VISIBLE);
                    break;
                case MEMBER_STATUS_MODERATOR:
                    tvReport.setVisibility(View.GONE);

                    break;
                default:
                    tvSettings.setVisibility(View.GONE);
                    break;
            }
        }

        tvGenerateQRCode.setOnClickListener(v -> {
            QxmShowQRCodeDialog qxmShowQRCodeDialog = new QxmShowQRCodeDialog(v.getContext(), getActivity());
            qxmShowQRCodeDialog.showGroupQRCode(QR_CODE_FOR_GROUP,
                    groupData.getGroupInfo().getGroupName(),
                    urlHelper.getGroupUrl(groupData.getId())
            );

            bottomSheetDialog.dismiss();
        });

        tvCopyLink.setOnClickListener(v -> {
            QxmCopyLinkToClipboard qxmCopyLinkToClipboard = new QxmCopyLinkToClipboard(getActivity());

            if (qxmCopyLinkToClipboard.copyToClipboard(urlHelper.getGroupUrl(groupData.getId()))) {
                bottomSheetDialog.dismiss();
                Toast.makeText(context, "Link copied to clipboard.", Toast.LENGTH_SHORT).show();
            }

        });

        tvSettings.setOnClickListener(v -> {
            qxmFragmentTransactionHelper.loadGroupSettingsFragment(groupId, groupData.getMyStatus());
            bottomSheetDialog.dismiss();

        });

        tvReport.setOnClickListener(v -> {
            QxmReportDialog groupReportDialog = new QxmReportDialog(getActivity(), v.getContext(), apiService, token.getToken(), token.getUserId(), groupId);
            groupReportDialog.showReportDialog(REPORT_GROUP);
            bottomSheetDialog.dismiss();
        });

    }

    // endregion

    //region initializeGroupPhotoChangingProcess
    private void initializeGroupPhotoChangingProcess() {

        Log.d(TAG, "initializeGroupPhotoChangingProcess: called");
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE;
        imageProcessorForGroupImageChange = new ImageProcessor(context, ViewQxmGroupFragment.this, TAG, apiService, token, ivGroupImage, groupData.getId());
        imageProcessorForGroupImageChange.pickImageFromGallery();


    }
    //endregion

    //region receiving event after uploading group image

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEvent(Events.OnPhotoUploadMessage onPhotoUploadMessage) {


        String message = onPhotoUploadMessage.getMessage();

        Log.d(TAG, "onPhotoUploadEvent Message: " + message);
        Log.d(TAG, "onPhotoUploadEvent: " + onPhotoUploadMessage);

        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent STARTING_PHOTO_UPLOAD: Called");
                tvUploadingImage.setVisibility(View.VISIBLE);
                break;

            case IMAGE_UPLOAD_SUCCESS:

                Log.d(TAG, "onPhotoUploadEvent IMAGE_UPLOAD_SUCCESS: Called");
                tvUploadingImage.setVisibility(View.GONE);

                // upload success, save new image url in realm database
                Log.d(TAG, "execute onPhotoUploadMessage: " + onPhotoUploadMessage);
                StaticValues.isMyQxmGroupFragmentRefreshNeeded = true;
                Toast.makeText(context, R.string.photo_uploaded_successfully, Toast.LENGTH_SHORT).show();
                break;


            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent ERROR_OCCURRED_DURING_PHOTO_UPLOAD: Called");
                Toast.makeText(context, R.string.toast_message_could_not_upload_image_try_again_later, Toast.LENGTH_SHORT).show();
                tvUploadingImage.setVisibility(View.GONE);

                break;


        }


    }

    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // passing image process result for feedback

        imageProcessorForGroupImageChange.onActivityResult(requestCode, resultCode, data);

    }


    //endregion

    //region Override-onStart
    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            // registering event bus
            EventBus.getDefault().register(this);
        }


        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

        if (groupData != null) {
            llViewQxmGroupFragment.setVisibility(View.VISIBLE);
            //loadGroupDataIntoViews();
        }
    }
    //endregion

    //region Override-onDestroyView
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unregistering event bus
        EventBus.getDefault().unregister(this);

        if (getActivity() != null) {

            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }
    //endregion

    // region Override-OnItemSelected (FeedAdapter)

    @Override
    public void onItemSelected(String userId, String qxmId) {
        Log.d("GroupFeedAdapterListen", TAG + " userId: " + userId);
        Log.d("GroupFeedAdapterListen", TAG + " qxmId: " + qxmId);
        if (token.getUserId().equals(userId)) {
            // goto SingleQxmDetailsFragment
            Log.d("GroupFeedAdapterListen", TAG + " onPollSelected: goto SingleQxmDetailsFragment");
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
            qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
        } else {
            Log.d("GroupFeedAdapterListen", TAG + " onPollSelected: goto QuestionOverview");
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
            qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
        }
    }

    // endregion


    @Override
    public void onDetach() {
        super.onDetach();
        llViewQxmGroupFragment.setVisibility(View.GONE);
    }
}
