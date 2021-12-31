package com.crux.qxm.views.fragments.group;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.Group;
import com.crux.qxm.db.models.group.GroupSettings;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.createGroupFragmentFeature.CreateGroupFragmentComponent;
import com.crux.qxm.di.createGroupFragmentFeature.DaggerCreateGroupFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_GROUP_IMAGE;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends Fragment {

    // region Fragment-Properties

    private static final String TAG = "CreateGroupFragment";

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private QxmApiService apiService;
    private Context context;
    private UserBasic user;
    private QxmToken token;

    private Group group;

    // image saving file
    File saveFile;
    // Uri that holds image uri
    Uri savedImageUri;
    // this string will be used to save image name to pass storage sytem
    String imageNameToPass = "";
    ImageProcessor imageProcessorForGroupImage;
    public static int GROUP_IMAGE_UPLOAD_REQUEST = 123;
    public static String onActivityCall = ON_ACTIVITY_CALL_FOR_GROUP_IMAGE;
    public static String GROUP_IMAGE_UPLOADED_URL;

    // endregion

    // region BindViewWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.btnCreateGroup)
    AppCompatButton btnCreateGroup;
    @BindView(R.id.ivGroupImage)
    AppCompatImageView ivGroupImage;
    @BindView(R.id.etGroupName)
    AppCompatEditText etGroupName;
    @BindView(R.id.etGroupDescription)
    AppCompatEditText etGroupDescription;

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

    // endregion

    // region Fragment-Constructor
    public CreateGroupFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();

        setUpDagger2();

        init();

        initializeClickListeners();

    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        CreateGroupFragmentComponent createGroupFragmentComponent =
                DaggerCreateGroupFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        createGroupFragmentComponent.injectCreateGroupFragmentFeature(CreateGroupFragment.this);
    }

    // endregion

    // region Init

    private void init() {

        apiService = retrofit.create(QxmApiService.class);

        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_GROUP_IMAGE;

        group = new Group(new GroupSettings());

    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {

        ivGroupImage.setOnClickListener(v -> {

            StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_GROUP_IMAGE;
            imageProcessorForGroupImage = new ImageProcessor(context, CreateGroupFragment.this, TAG, apiService, token, ivGroupImage);
            imageProcessorForGroupImage.pickImageFromGallery();
        });

        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        btnCreateGroup.setOnClickListener(v -> {

            if (storeGroupDetailsIntoModel()) {
                createGroupNetworkCall();
            } else {
                Log.d(TAG, "initializeClickListeners: validation failed");
            }

        });
    }

    // endregion

    // region StoreGroupDetailsIntoModel

    private boolean storeGroupDetailsIntoModel() {

        if (!TextUtils.isEmpty(etGroupName.getText().toString().trim())) {
            group.setGroupName(etGroupName.getText().toString().trim());
        } else {
            Toast.makeText(context, "Enter group name", Toast.LENGTH_SHORT).show();
            etGroupName.requestFocus();
            return false;
        }

        if (!TextUtils.isEmpty(etGroupDescription.getText().toString().trim())) {
            group.setGroupDescription(etGroupDescription.getText().toString().trim());
        } else {
            Toast.makeText(context, "Enter group description", Toast.LENGTH_SHORT).show();
            etGroupDescription.requestFocus();
            return false;
        }

        if (!TextUtils.isEmpty(GROUP_IMAGE_UPLOADED_URL))
            group.setGroupImageUrl(GROUP_IMAGE_UPLOADED_URL);
        group.getGroupSettings().setJoinRequestNeeded(switchJoinRequestNeeded.isChecked());
        group.getGroupSettings().setMemberCanAddOther(switchMemberCanAddOther.isChecked());
        group.getGroupSettings().setMemberVisibleToOther(switchMemberVisibleToOther.isChecked());
        group.getGroupSettings().setNotificationFrequency(spinnerNotificationFrequency.getSelectedItem().toString());
        group.getGroupSettings().setPrivacy(spinnerGroupPrivacy.getSelectedItem().toString());

        Log.d(TAG, "storeGroupDetailsIntoModel: Group: " + group.toString());

        return true;
    }

    // endregion

    // region CreateGroupNetworkCall

    private void createGroupNetworkCall() {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(getResources().getString(R.string.title_create_group), getResources().getString(R.string.message_create_group), false);

        Gson gson = new Gson();

        Log.d(TAG, "createGroupNetworkCall: JSON: " + gson.toJson(group));

        Call<QxmApiResponse> createGroup = apiService.createGroup(token.getToken(), token.getUserId(), group);

        createGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: Create Group callback.");
                Log.d(TAG, "onResponse: Response code: " + response.code());
                Log.d(TAG, "onResponse: Response body: " + response.body());

                progressDialog.closeProgressDialog();

                if (response.code() == 201) {
                    Toast.makeText(context, "Group successfully created.", Toast.LENGTH_SHORT).show();
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout();

                } else {
                    Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Create Group callback");
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: " + Arrays.toString(t.getStackTrace()));
                progressDialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // endregion


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Called");

        // passing image process result for feedback
        if (imageProcessorForGroupImage != null) {

            Log.d(TAG, "onActivityResult: imageProcessorForGroupImage");
            imageProcessorForGroupImage.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: called");
        if(onActivityCall.equals(ON_ACTIVITY_CALL_FOR_GROUP_IMAGE)){

            if(imageProcessorForGroupImage!=null){

                Log.d(TAG, "onRequestPermissionsResult: not null");
                imageProcessorForGroupImage.onRequestPermissionsResult(requestCode,permissions,grantResults);
            }
        }



    }


    //region receiving event after uploading qSet thumbnail

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEventQSetThumbnail(Events.OnPhotoUploadMessage onPhotoUploadMessageGroupImageThumbnail) {

        Log.d(TAG, "onPhotoUploadEventQSetThumbnail: " + onPhotoUploadMessageGroupImageThumbnail);

        String message = onPhotoUploadMessageGroupImageThumbnail.getMessage();
        String groupImageThumbnailURL = onPhotoUploadMessageGroupImageThumbnail.getSentImageURL();


        switch (message) {

            case STARTING_PHOTO_UPLOAD:
                //changing upload button text
                /*tvAddDeleteThumbnail.setText(R.string.uploading_image);*/

                //block going to next fragment
                /*ivDoneCreatingQuestion.setClickable(false);
                ivDoneCreatingQuestion.setFocusableInTouchMode(false);
                ivDoneCreatingQuestion.setAlpha(0.5f);*/

                break;

            case IMAGE_UPLOAD_SUCCESS:

                // setting url in a variable
                CreateGroupFragment.GROUP_IMAGE_UPLOADED_URL = groupImageThumbnailURL;
                //changing upload button text
                /*tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);*/

                // release going to next fragment
                //block going to next fragment
                /*ivDoneCreatingQuestion.setClickable(true);
                ivDoneCreatingQuestion.setAlpha(1.0f);*/

                Toast.makeText(context, R.string.toast_message_image_uploaded_successfully, Toast.LENGTH_SHORT).show();
                break;

            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                //changing upload button text
                /*tvAddDeleteThumbnail.setText(R.string.add_thumbnail);*/

                // release going to next fragment
                //block going to next fragment
                /*ivDoneCreatingQuestion.setClickable(true);
                ivDoneCreatingQuestion.setAlpha(1.0f);*/


                Toast.makeText(context, R.string.toast_message_could_not_upload_image_try_again_later, Toast.LENGTH_SHORT).show();
                break;


        }

    }

    //endregion

    // region Override-OnStart
    @Override
    public void onStart() {
        super.onStart();

        // registering event bus
        if (!EventBus.getDefault().isRegistered(this)) {
            // registering event bus
            EventBus.getDefault().register(this);
        }

        //hiding bottom navigation view when start this fragment
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

    }
    // endregion

    // region Override-OnStop

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Called");

        if (imageProcessorForGroupImage != null) {
            imageProcessorForGroupImage.onStop();
        }
    }

    // endregion

    //region onDestroy

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregistering event bus
        EventBus.getDefault().unregister(this);

        //hiding bottom navigation view when destroy this fragment
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    //endregion
}
