package com.crux.qxm.views.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crux.qxm.R;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();
        /*UserProfileFragment userProfileFragment = UserProfileFragment.newInstance()
        loadFragment();*/
    }

    // region Init

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // endregion

    // region LoadFragment

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_user_profile_activity, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    // endregion
}
