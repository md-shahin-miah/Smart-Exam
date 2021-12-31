package com.crux.qxm.views.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.Onboarding.OnboardingActivity;
import com.crux.qxm.R;
import com.crux.qxm.utils.userSessionTrackerHelper.UserActivitySessionTrackerHelper;
import com.crux.qxm.views.fragments.loginActivityFragments.LauncherFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.LoginFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.ProfilePicUploadFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.UserInterestAndPreferableLanguageInputFragment;

import java.util.Calendar;

import static com.crux.qxm.utils.StaticValues.isfirsttimeentered;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "MyMainActivity";
    private long startTime = 0;
    private UserActivitySessionTrackerHelper activitySessionTrackerHelper;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);



        shareprefget();

        Log.d(TAG, "onCreate: pager"+isfirsttimeentered);

        init();
        loadLoginFragment();
        //loadLauncherFragment();
    }

    private void init() {
        activitySessionTrackerHelper = new UserActivitySessionTrackerHelper();
        // AppEventsLogger.newLogger(getApplicationContext());
    }


    // region LoadLoginFragment
    private void loadLauncherFragment() {
        // initializing with login fragment
        LauncherFragment launcherFragment = new LauncherFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, launcherFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadLoginFragment
    private void loadLoginFragment() {
        // initializing with login fragment
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // endregion



    @Override
    protected void onStart() {
        super.onStart();
        startTime = Calendar.getInstance().getTimeInMillis();
        shareprefget();
        if (isfirsttimeentered==1){
            startActivity( new Intent(LoginActivity.this, OnboardingActivity.class));
            finish();
        }
        Log.d(TAG, "onStart: startTime: " + startTime);
    }

    @Override
    protected void onStop() {
        super.onStop();

        long endTime = Calendar.getInstance().getTimeInMillis();
        activitySessionTrackerHelper.saveLoginActivitySession(startTime, endTime);
        if (activitySessionTrackerHelper.getActivitySessionTracker() != null)
            Log.d(TAG, "onStop: activitySession: " + activitySessionTrackerHelper.getActivitySessionTracker().toString());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof LoginFragment || fragment instanceof ProfilePicUploadFragment
                || fragment instanceof UserInterestAndPreferableLanguageInputFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
  public void   shareprefget(){
      SharedPreferences pref = getApplicationContext().getSharedPreferences("isfirsttimeentered", MODE_PRIVATE);
      isfirsttimeentered=pref.getInt("val", 1);

    }
}