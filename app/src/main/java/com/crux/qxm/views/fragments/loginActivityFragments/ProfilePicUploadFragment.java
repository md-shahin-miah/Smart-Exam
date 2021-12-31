package com.crux.qxm.views.fragments.loginActivityFragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.response.UserProfileImageResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.profilePicUploadFragmentFeature.DaggerProfilePicUploadFragmentComponent;
import com.crux.qxm.di.profilePicUploadFragmentFeature.ProfilePicUploadFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.InputStreamToByteConverter;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePicUploadFragment extends Fragment {

    // region Fragment-Properties

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final String TAG = "UserProfilePicUploadFra";
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "user_profile_image";
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    //    private static final int REQUEST_SEND_IMAGE_TO_AWS = 120;
    //    private QxmProgressDialog progressDialog;
    @BindView(R.id.tvGreetings)
    TextView tvGreetings;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    @BindView(R.id.llcUploadImage)
    LinearLayoutCompat llcUploadImage;
    @BindView(R.id.tvUploadImage)
    AppCompatTextView tvUploadImage;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;
    @BindView(R.id.tvUploadedImage)
    AppCompatTextView tvUploadedImage;
    private QxmApiService apiService;
    private RealmService realmService;

    // endregion

    // region BindViewWithButterKnife
    private UserBasic user;
    private QxmToken token;
    private AlertDialog mAlertDialog;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    // image saving file
    private File saveFile;
    // Uri that holds image uri
    private Uri savedImageUri;
    // this string will be used to save image name to pass storage sytem
    private String imageNameToPass = "";

    // endregion

    // region Fragment-Constructor
    public ProfilePicUploadFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnViewCreated

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_pic_upload, container, false);


        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

//        progressDialog = new QxmProgressDialog(view.getContext());

        // for local DB testing

        init();
        loadDataIntoViews(view.getContext());
        initializeClickListeners();
    }

    // endregion

    // region Init

    private void init() {
        realmService = new RealmService(Realm.getDefaultInstance());
        apiService = retrofit.create(QxmApiService.class);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
    }

    // endregion

    // region LoadDataIntoViews

    private void loadDataIntoViews(Context context) {
        tvGreetings.setText(String.format("Hi, %s", user.getFullName()));

        picasso.load(user.getProfilePic())
                .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                .into(ivProfileImage);

        Log.d(TAG, String.format("onCreateView :: profilePicUrl: %s", user.getProfilePic()));
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        btnContinue.setOnClickListener(v -> {

            // save user sign up steps status

            realmService.setUserProfilePicUploadDone(true);
            qxmFragmentTransactionHelper.loadUserInterestAndPreferableLanguageInputFragment();
        });

        llcUploadImage.setOnClickListener(v -> pickImageFromGallery(v.getContext()));

        ivProfileImage.setOnClickListener(v -> pickImageFromGallery(v.getContext()));
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        ProfilePicUploadFragmentComponent profilePicUploadFragmentComponent =
                DaggerProfilePicUploadFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        profilePicUploadFragmentComponent.injectProfilePicUploadFragment(ProfilePicUploadFragment.this);

    }

    // endregion

    // region pickImageFromGallery
    // method for pic image from gallery
    private void pickImageFromGallery(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }
    //endregion

    // region startCropActivity
    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(Objects.requireNonNull(getContext()).getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(getContext(), this);
    }

    //endregion

    // region showImageAfterCrop
    public void showImageAfterCrop(Uri uri) {
        try {

            // getting image file from storage for preview
            //Bitmap myBitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            ivProfileImage.setImageBitmap(bitmap);


            //changePhotoIV.setVisibility(View.VISIBLE);
            //Drawable uploadThumbnailImage = getResources().getDrawable( R.drawable.ic_action_upload );
            //btnImageUpload.setCompoundDrawablesWithIntrinsicBounds( uploadThumbnailImage, null, null, null);
            //btnImageUpload.setText(getResources().getString(R.string.upload_image));

            uploadUserImage(uri);

        } catch (Exception e) {

            Log.d(TAG, "showImageAfterCrop profile image during signUp: "+e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(Objects.requireNonNull(uri.getPath())).getAbsolutePath(), options);

    }
    //endregion

    // region saveCroppedImage
    private void saveCroppedImage(Uri uri) {

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {

            if (uri != null && Objects.requireNonNull(uri.getScheme()).equals("file")) {
                try {
                    copyFileToLocalDisk(uri);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, uri.toString(), e);
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // endregion

    // region copyFileToLocalDisk
    private void copyFileToLocalDisk(Uri croppedFileUri) throws Exception {

        //String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), getApplicationContext().getResources().getString(R.string.qxm_local_resource_storage_directory_name));

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }


        // naming the photo
        String userId = token.getUserId();
        String filename = String.format("%s_%s", userId + "_", croppedFileUri.getLastPathSegment());
        imageNameToPass = filename;

        Log.d("FileName", filename);
        saveFile = new File(mediaStorageDir, filename);
        FileInputStream inStream = new FileInputStream(new File(Objects.requireNonNull(croppedFileUri.getPath())));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        Log.d(TAG, "copyFileToLocalDisk: Image is saved successfully");
    }

    //endregion

    // region uploadUserImage (network call)
    public void uploadUserImage(Uri uri) {

        try {

            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);

            if (inputStream != null) {

                btnContinue.setEnabled(false);
                btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background_disabled));

//                progressDialog.showProgressDialog("Profile Image Upload", "Uploading...", false);

                Log.d(TAG, "uploadUserImage: Token: " + token.toString());
                Log.d(TAG, "uploadUserImage: userId : " + token.getUserId());


                // naming the photo
                String userId = token.getUserId();
                imageNameToPass = String.format("%s_%s", userId + "_", uri.getLastPathSegment());


                // showing user image is uploading
                tvUploadImage.setVisibility(View.GONE);
                tvUploadingImage.setVisibility(View.VISIBLE);
                tvUploadedImage.setVisibility(View.GONE);

                byte[] imageBytes = InputStreamToByteConverter.getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", imageNameToPass, requestFile);
                Log.d(TAG, "uploadUserImage: token = " + token.getToken());


                Call<UserProfileImageResponse> imageUploadResponse = apiService.uploadProfileImage(token.getToken(), token.getUserId(), body);

                imageUploadResponse.enqueue(new Callback<UserProfileImageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserProfileImageResponse> call, @NonNull Response<UserProfileImageResponse> response) {

                        Log.d(TAG, "onResponse: success.");
                        Log.d(TAG, "StatusCode: " + response.code());
                        Log.d(TAG, "response body: " + response.body());

                        if (response.code() == 201) {
                            Log.d(TAG, "onResponse: Image upload success");
                            if (response.body() != null) {

                                UserBasic user = new UserBasic();
                                user.setProfilePic(response.body().getUserProfileImageURL());
                                realmService.setUserProfileImage(user,response.body().getUserProfileImageURL());

                                Log.d(TAG, "onResponse local user: "+user);

                                // showing user image is uploaded
                                tvUploadImage.setVisibility(View.GONE);
                                tvUploadingImage.setVisibility(View.GONE);
                                tvUploadedImage.setVisibility(View.VISIBLE);

                                Toast.makeText(getContext(), "Photo uploaded successfully. Tap on photo to change", Toast.LENGTH_SHORT).show();

                                // user will tap on on photo to change it
                                llcUploadImage.setClickable(false);
                                ivProfileImage.setClickable(true);

                                btnContinue.setEnabled(true);
                                btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));
//                                progressDialog.closeProgressDialog();

                            } else {
//                                progressDialog.closeProgressDialog();
                                btnContinue.setEnabled(true);
                                btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));
                                Toast.makeText(getContext(), "Image is not uploaded. Please try again.", Toast.LENGTH_SHORT).show();
                            }

                        } else if (response.code() == 403) {
                            Toast.makeText(getContext(), "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                            QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                            qxmFragmentTransactionHelper.logout(realmService);
                        } else {
//                            progressDialog.closeProgressDialog();
                            btnContinue.setEnabled(true);
                            btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));
                            Log.d(TAG, "onResponse: Image upload failed");
                            Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<UserProfileImageResponse> call, @NonNull Throwable t) {


//                        progressDialog.closeProgressDialog();
                        btnContinue.setEnabled(true);
                        btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));

                        // showing user to upload again
                        tvUploadImage.setVisibility(View.VISIBLE);
                        tvUploadingImage.setVisibility(View.GONE);
                        tvUploadedImage.setVisibility(View.GONE);


                        // user will tap on upload button to upload a new photo
                        llcUploadImage.setClickable(true);
                        ivProfileImage.setClickable(false);

                        Toast.makeText(getContext(), "Can not upload photo, try again later", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onFailure: unSuccessful");
                        Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                        Log.d(TAG, "Message: " + t.getMessage());
                    }
                });

            }

        } catch (FileNotFoundException e) {
//            progressDialog.closeProgressDialog();
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));
            e.printStackTrace();
        } catch (IOException e) {
//            progressDialog.closeProgressDialog();
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getResources().getDrawable(R.drawable.button_background));

            e.printStackTrace();
        }
    }
    //endregion

    // region requestPermission (permission access from android)

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    (dialog, which) -> this.requestPermissions(
                    new String[]{permission}, requestCode),
                    getString(R.string.label_ok),
                    getString(R.string.label_cancel)
            );
        } else {
            this.requestPermissions(new String[]{permission}, requestCode);
        }
    }

    //endregion

    // region onRequestPermissionsResult
    //Callback received when a permissions request has been completed.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:

                Log.d(TAG, "onRequestPermissionsResult: " + grantResults[0]);
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("OnRequestPermissionsRes", "Called");
                    pickImageFromGallery(getContext());
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    // endregion

    // region showAlertDialog
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, null);
        mAlertDialog = builder.show();
    }
    //endregion

    // region showIfPhotoUploaded

//    //showing user if photo uploaded or not
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getMessage(Events.OnPhotoUploadMessage oneThreadToAnotherThreadMessage){
//
//        Toast.makeText(getContext(), oneThreadToAnotherThreadMessage.getMessage(), Toast.LENGTH_SHORT).show();
//
//        if(oneThreadToAnotherThreadMessage.getMessage().equals("successUploadQPhoto")){
//
//            btnImageUpload.setText(getResources().getString(R.string.uploaded));
//        }else if(oneThreadToAnotherThreadMessage.getMessage().equals("failedUploadQphoto")){
//            btnImageUpload.setText(getResources().getString(R.string.failed_uloading));
//        }
//    }

    // endregion

    // region UCrop basisConfig

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    public UCrop basisConfig(@NonNull UCrop uCrop) {

        Log.d("BasicConfig", "Called");
        uCrop = uCrop.useSourceImageAspectRatio();
        uCrop = uCrop.withMaxResultSize(1500, 1500);
        return uCrop;
    }

    // endregion

    // region Ucrop advancedConfig

    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */

    public UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        Log.d("AdvancedConfig", "Called");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.withAspectRatio(1,1);
        options.setToolbarWidgetColor(getResources().getColor(R.color.white));
        options.setToolbarColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */

        return uCrop.withOptions(options);
    }

    // endregion

    // region handleCropResult
    public void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);

        // keeping this uri to another object because we are uploading photo from other place
        savedImageUri = resultUri;

        if (resultUri != null) {

            //saveCroppedImage(resultUri);
            showImageAfterCrop(resultUri);

        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }
    // endregion

    // region handleCropError

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            //Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(getContext(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    // region onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("OnActivityResult", "Called");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {

                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    Log.d("StartCropActivity", "Called");
                    startCropActivity(data.getData());
                    Log.d("DATA", String.valueOf(selectedUri));
                } else {

                    Toast.makeText(getContext(), R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Log.d("HandleCropResult", "Called");
                handleCropResult(data);
            }

            Log.d("RequestCode", String.valueOf(requestCode));
            Log.d("RequUCrop", String.valueOf(UCrop.REQUEST_CROP));
            Log.d("RequSelectPic", String.valueOf(REQUEST_SELECT_PICTURE));
        }
        if (resultCode == UCrop.RESULT_ERROR) {

            Log.d("HandleCropError", "Called");
            handleCropError(data);
        }
    }

    // endregion

    // region onStop
    @Override
    public void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
    //endregion

    // region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        // App.getRefWatcher(getActivity()).watch(this);
    }

    // endregion
}
