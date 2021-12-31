package com.crux.qxm.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.response.UploadedImageLink;
import com.crux.qxm.db.models.response.UserProfileImageResponse;
import com.crux.qxm.db.models.users.UserInfo;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.fragments.group.CreateGroupFragment;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED;
import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;
import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_GROUP_IMAGE;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_Q_SET;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION;
import static com.crux.qxm.utils.StaticValues.PHOTO_UPLOAD_REASON_QSET_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.PHOTO_UPLOAD_REASON_SINGLE_QUESTION_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.onActivityCall;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ImageProcessor {

    private static final int REQUEST_SEND_IMAGE_TO_AWS = 1200;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "image";
    private Context context;
    private Fragment fragment;
    private String TAG;
    private QxmApiService qxmApiService;
    private UserBasic userBasic;
    private UserInfo userInfo;
    private AppCompatImageView imageView;
    private AppCompatImageView removePhotoImageView;
    private AppCompatTextView textView;
    private int questionPosition;
    private AlertDialog mAlertDialog;
    private QxmToken token;
    private String id;

    // image saving file
    private File saveFile;
    // Uri that holds image uri
    private Uri savedImageUri;
    // this string will be used to save image name to pass storage system
    private String imageNameToPass = "";

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, UserBasic userBasic, AppCompatImageView imageView) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.userBasic = userBasic;
        this.imageView = imageView;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, QxmToken token, AppCompatImageView imageView) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.token = token;
        this.imageView = imageView;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, UserBasic userBasic, AppCompatImageView imageView, AppCompatTextView textView) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.userBasic = userBasic;
        this.imageView = imageView;
        this.textView = textView;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, QxmToken token, AppCompatImageView imageView, AppCompatTextView textView) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.token = token;
        this.imageView = imageView;
        this.textView = textView;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, QxmToken token, UserBasic userBasic, AppCompatImageView imageView, AppCompatTextView textView) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.token = token;
        this.userBasic = userBasic;
        this.imageView = imageView;
        this.textView = textView;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService qxmApiService, QxmToken token) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = qxmApiService;
        this.token = token;
    }

    public ImageProcessor(Context context, Fragment fragment, String TAG, QxmApiService apiService, QxmToken token, AppCompatImageView imageView, String id) {

        this.context = context;
        this.fragment = fragment;
        this.TAG = TAG;
        this.qxmApiService = apiService;
        this.token = token;
        this.imageView = imageView;
        this.id = id;

    }


    // region pickImageFromGallery
    // method for pic image from gallery
    public void pickImageFromGallery() {

        Log.d(TAG, "pickImageFromGallery: outside");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    context.getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);

            Log.d(TAG, "pickImageFromGallery: getPermission");
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            fragment.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
            Log.d(TAG, "pickImageFromGallery: choosingPhoto");
        }
    }
    //endregion

    // region startCropActivity
    private void startCropActivity(@NonNull Uri uri) {

        Log.d(TAG, "startCropActivity: Called");
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(context, fragment);
        Log.d(TAG, "startCropActivity: Called Later");
    }

    //endregion

    // region showImageAfterCrop
    private void showImageAfterCrop(Uri uri, AppCompatImageView imageView) {
        try {

            // getting image file from storage for preview
            Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);


            Log.d(TAG, "showImageAfterCrop: Called");
            imageView.setImageBitmap(myBitmap);

            // if user is coming from UserProfileFragment, then upload user profile picture
            // otherwise, upload
            switch (onActivityCall) {
                case ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE:
                    changeSingleProfilePhoto(qxmApiService, token, uri);
                    break;
                case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE:
                    changeGroupPhoto(qxmApiService, token, id, uri);
                    break;
                default:
                    uploadImage(qxmApiService, token, uri);
                    break;
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(Objects.requireNonNull(uri.getPath())).getAbsolutePath(), options);

    }
    //endregion

    // region saveCroppedImage
    private void saveCroppedImage(Uri uri) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    context.getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {

            if (uri != null && Objects.requireNonNull(uri.getScheme()).equals("file")) {
                try {
                    copyFileToLocalDisk(uri);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, uri.toString(), e);
                }
            } else {
                Toast.makeText(context, context.getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // endregion

    // region copyFileToLocalDisk
    private void copyFileToLocalDisk(Uri croppedFileUri) throws Exception {

        //String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.qxm_local_resource_storage_directory_name));

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }


        // naming the photo
        String imageId = UUID.randomUUID().toString();
        String filename = String.format("%s_%s", imageId, croppedFileUri.getLastPathSegment());
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

    // region uploadImage (network call)
    private void uploadImage(QxmApiService apiService, QxmToken token, Uri uri) {

        eventTrackingJustBeforeImageUpload();
        // region try-catch block
        try {
            // reading image from local disk as stream
            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {

                // naming the photo
                String imageId = UUID.randomUUID().toString();
                imageNameToPass = String.format("%s_%s", imageId, uri.getLastPathSegment());

                // converting stream to bytes and prepare for retrofit (RequestBody and MultipartBody)
                byte[] imageBytes = InputStreamToByteConverter.getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", imageNameToPass, requestFile);
                Log.d(TAG, "uploadImage: userId = " + token.getUserId());

                QxmProgressDialog progressDialog = new QxmProgressDialog(context);
                if (fragment instanceof CreateGroupFragment) {
                    progressDialog.showProgressDialog("Image Upload", "Image Uploading, please wait...", false);
                }

                // network call started
                Call<UploadedImageLink> imageUploadResponse = apiService.uploadThumbnailImage(token.getToken(), body);
                imageUploadResponse.enqueue(new Callback<UploadedImageLink>() {
                    @Override
                    public void onResponse(@NonNull Call<UploadedImageLink> call, @NonNull Response<UploadedImageLink> response) {

                        Log.d(TAG, "onResponse: success.");
                        Log.d(TAG, "StatusCode: " + response.code());
                        Log.d(TAG, "response body: " + response.body());

                        if (response.body() != null) {

                            if (!TextUtils.isEmpty(response.body().getImagelink())) {

                                switch (StaticValues.onActivityCall) {
                                    // update thumbnail image if user upload thumbnail
                                    case ON_ACTIVITY_CALL_FOR_Q_SET:

                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessageQSetThumbnail(imageNameToPass, response.body().getImagelink(), IMAGE_UPLOAD_SUCCESS, PHOTO_UPLOAD_REASON_QSET_THUMBNAIL));
                                        break;

                                    // otherwise it is a single question image
                                    // So, pass the event to save it in specific question
                                    case ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION:

                                        //sending photo upload success event
                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(imageNameToPass, response.body().getImagelink(), questionPosition, IMAGE_UPLOAD_SUCCESS));
                                        break;

                                    // Or may be it can be a group image
                                    case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE:
                                        CreateGroupFragment.GROUP_IMAGE_UPLOADED_URL = response.body().getImagelink();
                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(imageNameToPass,response.body().getImagelink()));
                                        Log.d(TAG, "onResponse: ON_ACTIVITY_CALL_FOR_GROUP_IMAGE= " + CreateGroupFragment.GROUP_IMAGE_UPLOADED_URL);
                                        break;

                                    // Or may a poll thumbnail image
                                    case ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL:

                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(imageNameToPass, response.body().getImagelink(), IMAGE_UPLOAD_SUCCESS));
                                        Log.d(TAG, "onResponse  ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL: Called");
                                        break;
                                    // Or may a poll thumbnail image
                                    case ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL:

                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(imageNameToPass, response.body().getImagelink(), IMAGE_UPLOAD_SUCCESS));
                                        Log.d(TAG, "onResponse  ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL: Called");
                                        break;
                                }
                            }

                        } else {

                            Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                        }

                        if (fragment instanceof CreateGroupFragment)
                            progressDialog.closeProgressDialog();

                    }

                    @Override
                    public void onFailure(@NonNull Call<UploadedImageLink> call, @NonNull Throwable t) {


                        eventTrackingIfPhotoUploadErrorOccurred();

                        Toast.makeText(context, "Can not upload photo, try again later", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: unSuccessful");
                        Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                        Log.d(TAG, "Message: " + t.getMessage());

                        if (fragment instanceof CreateGroupFragment)
                            progressDialog.closeProgressDialog();
                    }
                });

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }
    //endregion

    //region changeSingleProfilePhoto (network call)

    private void changeSingleProfilePhoto(QxmApiService apiService, QxmToken token, Uri uri) {

        eventTrackingJustBeforeImageUpload();
        // region try-catch block
        try {
            // reading image from local disk as stream
            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {


                // naming the photo
                String imageId = UUID.randomUUID().toString();
                imageNameToPass = String.format("%s_%s", imageId, uri.getLastPathSegment());


                // converting stream to bytes and prepare for retrofit (RequestBody and MultipartBody)
                byte[] imageBytes = InputStreamToByteConverter.getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", imageNameToPass, requestFile);
                Log.d(TAG, "uploadImage: userId = " + token.getUserId());

                // network call started
                Call<UserProfileImageResponse> imageUploadResponse = apiService.uploadProfileImage(token.getToken(), token.getUserId(), body);
                imageUploadResponse.enqueue(new Callback<UserProfileImageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserProfileImageResponse> call, @NonNull Response<UserProfileImageResponse> response) {

                        Log.d(TAG, "onResponse: success.");
                        Log.d(TAG, "StatusCode: " + response.code());
                        Log.d(TAG, "response body: " + response.body());

                        if (response.body() != null) {

                            if (!TextUtils.isEmpty(response.body().getUserProfileImageURL())) {

                                switch (StaticValues.onActivityCall) {

                                    case ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE:

                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(imageNameToPass, response.body().getUserProfileImageURL(), IMAGE_UPLOAD_SUCCESS));
                                        Log.d(TAG, "onResponse  ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE: Called");
                                        break;
                                }

                            }

                        } else {

                            Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<UserProfileImageResponse> call, @NonNull Throwable t) {


                        eventTrackingIfPhotoUploadErrorOccurred();
                        //Toast.makeText(context, "Can not upload photo, try again later", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: unSuccessful");
                        Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                        Log.d(TAG, "Message: " + t.getMessage());


                    }
                });

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region changeGroupPhoto (network call)

    private void changeGroupPhoto(QxmApiService apiService, QxmToken token, String groupId, Uri uri) {

        eventTrackingJustBeforeImageUpload();
        // region try-catch block
        try {
            // reading image from local disk as stream
            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {


                // naming the photo
                String imageId = UUID.randomUUID().toString();
                imageNameToPass = String.format("%s_%s", imageId, uri.getLastPathSegment());

                // converting stream to bytes and prepare for retrofit (RequestBody and MultipartBody)
                byte[] imageBytes = InputStreamToByteConverter.getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", imageNameToPass, requestFile);
                Log.d(TAG, "uploadImage: userId = " + token.getUserId());

                // network call started
                Call<QxmApiResponse> imageUploadResponse = apiService.changeGroupImage(token.getToken(), groupId, body);
                imageUploadResponse.enqueue(new Callback<QxmApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                        Log.d(TAG, "onResponse: success.");
                        Log.d(TAG, "StatusCode: " + response.code());
                        Log.d(TAG, "response body: " + response.body());

                        if (response.body() != null) {

                            if (response.code() == 201) {

                                switch (StaticValues.onActivityCall) {

                                    case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE:

                                        EventBus.getDefault().post(new Events.OnPhotoUploadMessage(IMAGE_UPLOAD_SUCCESS));
                                        Log.d(TAG, "onResponse  ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE: Called");
                                        break;
                                }

                            }

                        } else {

                            Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {


                        eventTrackingIfPhotoUploadErrorOccurred();
                        //Toast.makeText(context, "Can not upload photo, try again later", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: unSuccessful");
                        Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                        Log.d(TAG, "Message: " + t.getMessage());


                    }
                });

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(fragment.getActivity()), permission)) {
            showAlertDialog(context.getString(R.string.permission_title_rationale), rationale,
                    (dialog, which) -> {
                        fragment.requestPermissions(new String[]{permission}, requestCode);
                    },
                    context.getString(R.string.label_ok), null, context.getString(R.string.label_cancel));
        } else {
            fragment.requestPermissions(new String[]{permission}, requestCode);
        }
    }

    //endregion

    // region onRequestPermissionsResult
    //Callback received when a permissions request has been completed.


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:

                Log.d(TAG, "onRequestPermissionsResult: " + grantResults[0]);
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("OnRequestPermissionsRes", "Called");
                    pickImageFromGallery();
                }
                break;
        }
    }
    // endregion

    // region showAlertDialog
    private void showAlertDialog(@Nullable String title, @Nullable String message,
                                 @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                 @NonNull String positiveText,
                                 @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                 @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();


    }
    //endregion

    // region showIfPhotoUploaded

//    //showing user if photo uploaded or not
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getMessage(Events.OnPhotoUploadMessage oneThreadToAnotherThreadMessage){
//
//        Toast.makeText(context, oneThreadToAnotherThreadMessage.getMessage(), Toast.LENGTH_SHORT).show();
//
//        if(oneThreadToAnotherThreadMessage.getMessage().equals("successUploadQPhoto")){
//
//            btnImageUpload.setText(getResources().context.getString(R.string.uploaded));
//        }else if(oneThreadToAnotherThreadMessage.getMessage().equals("failedUploadQphoto")){
//            btnImageUpload.setText(getResources().context.getString(R.string.failed_uloading));
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
    private UCrop basisConfig(@NonNull UCrop uCrop) {

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

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        Log.d("AdvancedConfig", "Called");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        // options.withAspectRatio(3,2);
        // options.setAspectRatioOptions(0,new AspectRatio("",3,2));
        options.setToolbarColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.colorPrimary));
        options.setToolbarWidgetColor(context.getResources().getColor(R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary));


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
    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);

        // keeping this uri to another object because we are uploading photo from other place
        savedImageUri = resultUri;

        if (resultUri != null) {

            //saveCroppedImage(resultUri);

            if (imageView != null)
                imageView.setVisibility(View.VISIBLE);

            showImageAfterCrop(resultUri, imageView);

        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }
    // endregion

    // region handleCropError

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {

        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            //Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    // region onActivityResult
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

                    Toast.makeText(context, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
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

        if (resultCode == RESULT_CANCELED) {

            if (textView != null) {
                textView.setClickable(true);
            }
        }
    }

    // endregion

    // region setting and getting image view

    public AppCompatImageView getImageView() {
        return imageView;
    }

    public void setImageView(AppCompatImageView imageView) {
        this.imageView = imageView;
    }

    //endregion


    //region clear single question imageView

    public AppCompatImageView getRemovePhotoImageView() {

        return this.removePhotoImageView;
    }

    public void setRemovePhotoImageView(AppCompatImageView imageView) {

        this.removePhotoImageView = imageView;
    }

    //endregion


    // region setting and getting question position

    public int getQuestionPosition() {
        return questionPosition;
    }

    public void setQuestionPosition(int questionPosition) {
        this.questionPosition = questionPosition;
    }

    //endregion

    // region onStop

    public void onStop() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    // region eventTrackingJustBeforeImageUpload
    private void eventTrackingJustBeforeImageUpload() {

        switch (StaticValues.onActivityCall) {

            case ON_ACTIVITY_CALL_FOR_Q_SET:
                //image can be a question set thumbnail
                EventBus.getDefault().post(new Events.OnPhotoUploadMessageQSetThumbnail(STARTING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION:
                // otherwise it is a single question image
                // so, pass the event to save it in specific question
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD, PHOTO_UPLOAD_REASON_SINGLE_QUESTION_THUMBNAIL));
                break;
            case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE:
                //image can be a group image thumbnail
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD));
            case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE:
                //image can be a group image thumbnail changing
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD));

                // or may be it can be a group image
                //TODO what to do just before group thumbnail photo upload
                break;
            case ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL:
                // or may a poll thumbnail image
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE:
                // or may a profile picture changing image event
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL:
                // or may be a single multiple choice question thumbnail
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(STARTING_PHOTO_UPLOAD));
                break;


        }
    }
    //endregion

    //region eventTrackingIfPhotoUploadErrorOccurred
    private void eventTrackingIfPhotoUploadErrorOccurred() {

        switch (StaticValues.onActivityCall) {

            case ON_ACTIVITY_CALL_FOR_Q_SET:
                // image can be a question set thumbnail image
                EventBus.getDefault().post(new Events.OnPhotoUploadMessageQSetThumbnail(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION:
                // otherwise it is a single question image
                // So, pass the event to save it in specific question
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE:
                // Or may be it can be a group image
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE:
                // Or may be it can be a group image change
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL:
                // Or may a poll thumbnail image
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE:
                // Or may a poll thumbnail image
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
            case ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL:
                // or may be a single multiple choice question thumbnail
                EventBus.getDefault().post(new Events.OnPhotoUploadMessage(ERROR_OCCURRED_DURING_PHOTO_UPLOAD));
                break;
        }


    }
    //endregion


}
