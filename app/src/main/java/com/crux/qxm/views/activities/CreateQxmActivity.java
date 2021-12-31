package com.crux.qxm.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.utils.userSessionTrackerHelper.UserActivitySessionTrackerHelper;
import com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment;
import com.crux.qxm.views.fragments.createQxmActivityFragments.EditQxmFragment;
import com.crux.qxm.views.fragments.createQxmActivityFragments.QxmSettingsFragment;
import com.facebook.FacebookSdk;

import java.util.Calendar;

import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_EDIT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_MODEL_KEY;

public class CreateQxmActivity extends AppCompatActivity {

    private static final String TAG = "CreateQxmActivity";
    private long startTime = 0;
    private UserActivitySessionTrackerHelper activitySessionTrackerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_create_qxm);

        activitySessionTrackerHelper = new UserActivitySessionTrackerHelper();

        Intent intent = getIntent();

        if (intent != null) {
            QxmDraft qxmDraft = intent.getParcelableExtra(QXM_DRAFT_KEY);
            QxmModel qxmModel = intent.getParcelableExtra(QXM_MODEL_KEY);
            boolean qxmEdit = intent.getBooleanExtra(QXM_EDIT_KEY, false);
            if (qxmDraft != null && !qxmEdit) {
                Log.d(TAG, "Intent parcelable extra: " + qxmDraft.toString());
                loadFragment(CreateQxmFragment.newInstance(qxmDraft));

            }else if(qxmModel != null){
                Log.d(TAG, "Intent parcelable extra: "+ qxmModel.toString());
                loadFragment(CreateQxmFragment.newInstance(qxmModel));
            } else if(qxmDraft != null){
                Log.d(TAG, "load edit qxm fragment");
                loadFragment(EditQxmFragment.newInstance(qxmDraft));
            } else {
                loadFragment(new CreateQxmFragment());
            }
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }
    // endregion


    @Override
    protected void onStart() {
        super.onStart();
        startTime = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "onStart: startTime: " + startTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
        long endTime = Calendar.getInstance().getTimeInMillis();
        activitySessionTrackerHelper.saveCreateQxmActivitySession(startTime, endTime);
        Log.d(TAG, "onStop: activitySession: " + activitySessionTrackerHelper.getActivitySessionTracker().toString());
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);
        if (fragment instanceof CreateQxmFragment) {

            // Showing user that if he/she goes back, his/her created questions will be discarded.
            // and he/she can save the created questions as draft if wishes

            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage(R.string.alert_dialog_message_create_qxm_fragment_on_back_press_warning);
            builder.setTitle("Warning !");

            builder.setPositiveButton("OK", (dialogInterface, i) -> {

                fragmentManager.popBackStack();
                finish();

            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });

            builder.setNeutralButton("Save as draft", (dialogInterface, i) -> (
                    (CreateQxmFragment) fragment).onBackPressed());


            dialog = builder.create();
            dialog.show();

        }else if(fragment instanceof EditQxmFragment){
            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage(R.string.alert_dialog_message_qxm_edit_fragment_on_back_press_warning);
            builder.setTitle(R.string.alert_dialog_title_warning);

            builder.setPositiveButton("OK", (dialogInterface, i) -> {

                fragmentManager.popBackStack();
                finish();

            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });


            dialog = builder.create();
            dialog.show();
        }
        else if (fragment instanceof QxmSettingsFragment) {

            ((QxmSettingsFragment)fragment).onBackPressed();
        }

        else
            super.onBackPressed();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: Called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
