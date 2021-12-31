package com.crux.qxm.views.fragments.enroll;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.participationParentFragmentFeature.DaggerParticipationParentFragmentComponent;
import com.crux.qxm.di.participationParentFragmentFeature.ParticipationParentFragmentComponent;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static com.crux.qxm.utils.StaticValues.VIEW_PAGER_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipationParentFragment extends Fragment {

    @Inject
    Picasso picasso;
    private static final String TAG = "ParticipationParentFrag";
    SectionPagerAdapter sectionPagerAdapter;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.viewPagerEnroll)
    ViewPager viewPagerEnroll;
    @BindView(R.id.tabLayoutEnroll)
    TabLayout tabLayoutEnroll;


    public ParticipationParentFragment() {
        // Required empty public constructor
    }

    public static ParticipationParentFragment newInstance(int viewPagerPosition) {

        Bundle args = new Bundle();
        args.putInt(VIEW_PAGER_POSITION, viewPagerPosition);

        ParticipationParentFragment fragment = new ParticipationParentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_participation_parent, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int viewPagerPosition = getArguments() != null ? getArguments().getInt(VIEW_PAGER_POSITION) : 0;

        setUpDagger2(view.getContext());

        init();

        // get view pager's specific fragment
        viewPagerEnroll.setCurrentItem(viewPagerPosition);

    }

    private void setUpDagger2(Context context) {
        ParticipationParentFragmentComponent participationParentFragmentComponent =
                DaggerParticipationParentFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        participationParentFragmentComponent
                .injectParticipationParentFragmentFeature(ParticipationParentFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPagerEnroll.setAdapter(sectionPagerAdapter);
        tabLayoutEnroll.setupWithViewPager(viewPagerEnroll);
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        UserBasic user = realmService.getSavedUser();

        if (user.getModifiedURLProfileImage() != null && !user.getModifiedURLProfileImage().isEmpty())
            picasso.load(user.getModifiedURLProfileImage())
                    .error(getResources().getDrawable(R.drawable.ic_user_default))
                    .into(ivUserImage);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());

        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getUserId(), user.getFullName()));

    }

    // endregion

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
                    return new EnrollParticipatedFragment();
                case 1:
                    Log.d(TAG, "getItem: 1");
                    return new EnrollYouEnrolledFragment();
                case 2:
                    Log.d(TAG, "getItem: 2");
                    return new EnrollPeopleEnrollFragment();
                case 3:
                    Log.d(TAG, "getItem: 3");
                    return new EnrollSentRequestFragment();
                case 4:
                    Log.d(TAG, "getItem: 4");
                    return new EnrollReceivedRequestFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "My PARTICIPATION";
                case 1:
                    return "My ENROLLMENT";
                case 2:
                    return "PEOPLE ENROLLED";
                case 3:
                    return "SENT REQUESTS";
                case 4:
                    return "RECEIVE REQUESTS";

            }

            return null;
        }
    }
    //endregion

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: ");
            ((BottomNavigationViewEx) getActivity().findViewById(R.id.bottom_navigation_view)).getMenu().getItem(1).setChecked(true);
        }
    }
}
