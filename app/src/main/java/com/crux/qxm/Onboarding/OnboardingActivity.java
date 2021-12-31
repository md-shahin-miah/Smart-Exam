package com.crux.qxm.Onboarding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.crux.qxm.R;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.views.activities.HomeActivity;
import com.crux.qxm.views.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.isfirsttimeentered;

public class OnboardingActivity extends AppCompatActivity {

    private static final String TAG = "OnboardingActivity";

    OnboardingAdapter onboardingAdapter;
    LinearLayout layoutOnbordingindicators;
    MaterialButton ButtonOnBoardingAction, ButtonOnBoardingActionprevious;
    TextView skipText;

//    String intentget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        intentget = getIntent().getExtras().getString("key");
//
//        Log.d(TAG, "onCreate: getintent"+intentget);

        skipText=findViewById(R.id.skip_text);

        layoutOnbordingindicators = findViewById(R.id.LayoutOnBoardingIndicator);

        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimary)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.onboarding_color)); //status bar or the time bar at the top
        }

        ButtonOnBoardingAction = findViewById(R.id.ButtonOnBoardingAction);
        ButtonOnBoardingActionprevious = findViewById(R.id.ButtonOnBoardingActionPrevious);
        setonbordingitems();

        ViewPager2 OnbordingViewPager = findViewById(R.id.OnboardingViewPager);
        OnbordingViewPager.setAdapter(onboardingAdapter);

        setUpOnIndicators();
        setcurrentBordingIndicator(0);

        OnbordingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setcurrentBordingIndicator(position);
            }
        });


        ButtonOnBoardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OnbordingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    OnbordingViewPager.setCurrentItem(OnbordingViewPager.getCurrentItem() + 1);
                } else {

                    shareprefset();
                    startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        ButtonOnBoardingActionprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (OnbordingViewPager.getCurrentItem() + 1 >onboardingAdapter.getItemCount()) {
                    OnbordingViewPager.setCurrentItem(OnbordingViewPager.getCurrentItem()- 1);
//                } else {
//                    LoginActivity.isfirsttimeentered = 2;
//                    startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
//                    finish();
//                }
            }
        });

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareprefset();
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void shareprefset() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("isfirsttimeentered", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("val",2);
        editor.apply();

    }


    public void setonbordingitems() {


        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem onboardingItem0 = new OnboardingItem();
        onboardingItem0.setTitle("Cartoon 1");
        onboardingItem0.setDescription(" this is the desc and details of this image ");
        onboardingItem0.setImage(R.drawable.im1);


        OnboardingItem onboardingItem1 = new OnboardingItem();
        onboardingItem1.setTitle("Cartoon 2");
        onboardingItem1.setDescription(" this is the desc and details of this image ");
        onboardingItem1.setImage(R.drawable.im2);

        OnboardingItem onboardingItem2 = new OnboardingItem();
        onboardingItem2.setTitle("Cartoon 3");
        onboardingItem2.setDescription(" this is the desc and details of this image ");
        onboardingItem2.setImage(R.drawable.im3);


        OnboardingItem onboardingItem3 = new OnboardingItem();
        onboardingItem3.setTitle("Cartoon 4");
        onboardingItem3.setDescription(" this is the desc and details of this image ");
        onboardingItem3.setImage(R.drawable.im4);

        OnboardingItem onboardingItem4 = new OnboardingItem();
        onboardingItem4.setTitle("Cartoon 4");
        onboardingItem4.setDescription(" this is the desc and details of this image ");
        onboardingItem4.setImage(R.drawable.im5);

        OnboardingItem onboardingItem5 = new OnboardingItem();
        onboardingItem5.setTitle("Cartoon 4");
        onboardingItem5.setDescription(" this is the desc and details of this image ");
        onboardingItem5.setImage(R.drawable.im6);

        onboardingItems.add(onboardingItem0);
        onboardingItems.add(onboardingItem1);
        onboardingItems.add(onboardingItem2);
        onboardingItems.add(onboardingItem3);
        onboardingItems.add(onboardingItem4);
        onboardingItems.add(onboardingItem5);


        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setUpOnIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onbording_indicator_inactiove
            ));
            indicators[i].setLayoutParams(layoutparams);
            layoutOnbordingindicators.addView(indicators[i]);
        }
    }


    @SuppressLint("SetTextI18n")
    private void setcurrentBordingIndicator(int index) {

        int childcount = layoutOnbordingindicators.getChildCount();
        for (int i = 0; i < childcount; i++) {

            ImageView imageView = (ImageView) layoutOnbordingindicators.getChildAt(i);

            if (i == index) {

                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboardingindicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onbording_indicator_inactiove)
                );

            }

        }
        if (index == onboardingAdapter.getItemCount() - 1) {
            ButtonOnBoardingAction.setText("Start");
        } else {
            ButtonOnBoardingAction.setText("Next");
        }

        if (index == 0) {
            ButtonOnBoardingActionprevious.setVisibility(View.INVISIBLE);

        } else {
            ButtonOnBoardingActionprevious.setVisibility(View.VISIBLE);
            ButtonOnBoardingActionprevious.setText("Previous");
        }
    }
}