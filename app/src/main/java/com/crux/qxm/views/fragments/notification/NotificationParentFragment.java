package com.crux.qxm.views.fragments.notification;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.crux.qxm.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.VIEW_PAGER_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationParentFragment extends Fragment {

    private static final String TAG = "NotificationParentFragm";

    @BindView(R.id.viewPagerNotification)
    ViewPager viewPagerNotification;
    @BindView(R.id.tabLayoutNotification)
    TabLayout tabLayoutNotification;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    public NotificationParentFragment() {
        // Required empty public constructor
    }

    public static NotificationParentFragment newInstance(int viewPagerPosition) {

        Bundle args = new Bundle();
        args.putInt(VIEW_PAGER_POSITION, viewPagerPosition);

        NotificationParentFragment fragment = new NotificationParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_parent, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int viewPagerPosition = getArguments() != null ? getArguments().getInt(VIEW_PAGER_POSITION) : 0;

        init();

        // get view pager's specific fragment
        viewPagerNotification.setCurrentItem(viewPagerPosition);
    }

    // endregion

    // region Init
    private void init() {
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPagerNotification.setAdapter(sectionPagerAdapter);
        tabLayoutNotification.setupWithViewPager(viewPagerNotification);
        initializeCLickListeners();
    }

    // endregion
    private void initializeCLickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });
    }

    // region SectionPagerAdapter
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d(TAG, "getItem: 0");
                    return new AllNotificationFragment();
                case 1:
                    Log.d(TAG, "getItem: 1");
                    return new QxmNotificationFragment();
                case 2:
                    Log.d(TAG, "getItem: 2");
                    return new PrivateQxmNotificationFragment();
                case 3:
                    Log.d(TAG, "getItem: 3");
                    return new EvaluationNotificationFragment();
                case 4:
                    Log.d(TAG, "getItem: 4");
                    return new ResultNotificationFragment();
                case 5:
                    Log.d(TAG, "getItem: 5");
                    return new GroupNotificationFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "ALL";
                case 1:
                    return "QXM";
                case 2:
                    return "PRIVATE QXM";
                case 3:
                    return "EVALUATION";
                case 4:
                    return "RESULT";
                case 5:
                    return "GROUP";

            }

            return null;
        }
    }
    //endregion

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

}
