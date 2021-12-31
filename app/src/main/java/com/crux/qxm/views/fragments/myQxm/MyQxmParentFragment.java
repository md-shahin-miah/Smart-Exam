package com.crux.qxm.views.fragments.myQxm;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.crux.qxm.di.myQxmParentFragmentFeature.DaggerMyQxmParentFragmentComponent;
import com.crux.qxm.di.myQxmParentFragmentFeature.MyQxmParentFragmentComponent;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.views.fragments.myQxm.list.MyQxmListFragment;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static com.crux.qxm.utils.StaticValues.VIEW_PAGER_POSITION;


public class MyQxmParentFragment extends Fragment {

    private static final String TAG = "MyQxmParentFragment";

    @Inject
    Picasso picasso;
    private UserBasic user;
    private Context context;
    private final int MY_QXM_PARENT_VIEWPAGER_SIZE = 6;

    @BindView(R.id.viewPagerMyQxm)
    ViewPager viewPagerMyQxm;
    @BindView(R.id.tabLayoutMyQxm)
    TabLayout tabLayoutMyQxm;

    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public static MyQxmParentFragment newInstance(int viewPagerPosition) {

        Bundle args = new Bundle();
        args.putInt(VIEW_PAGER_POSITION, viewPagerPosition);

        MyQxmParentFragment fragment = new MyQxmParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_parent, container, false);

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

        int viewPagerPosition = getArguments() != null ? getArguments().getInt(VIEW_PAGER_POSITION) : 0;

        context = view.getContext();
        setUpDagger2(context);

        // calling init method for initial setup
        init();

        //setting user image
        setUserImage();

        //region Click Listeners

        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());

        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(user.getUserId(), user.getFullName()));

        //endregion

        // get view pager's specific fragment
        viewPagerMyQxm.setCurrentItem(viewPagerPosition);

        setFabButtonVisibility();
    }

    // endregion

    // region setup dagger2

    private void setUpDagger2(Context context) {

        MyQxmParentFragmentComponent myQxmParentFragmentComponent =
                DaggerMyQxmParentFragmentComponent

                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmParentFragmentComponent.injectMyQxmParentFragmentFeature(MyQxmParentFragment.this);
    }

    //endregion

    // region Init

    public void init() {
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPagerMyQxm.setAdapter(sectionPagerAdapter);
        tabLayoutMyQxm.setupWithViewPager(viewPagerMyQxm);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
    }

    //endregion

    // region section pager adapter
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d(TAG, "getItem: 0");
                    return new MyQxmInitialFragment();
                case 1:
                    Log.d(TAG, "getItem: 1");
                    return new MyQxmResultFragment();
                case 2:
                    Log.d(TAG, "getItem: 2");
                    return new MyQxmListFragment();
                case 3:
                    Log.d(TAG, "getItem: 3");
                    return new MyQxmGroupFragment();
                case 4:
                    Log.d(TAG, "getItem: 4");
                    return new MyQxmQxmDraftFragment();
                case 5:
                    Log.d(TAG, "getItem: 5");
                    return new MyQxmMyPollFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return MY_QXM_PARENT_VIEWPAGER_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "QXM";
                case 1:
                    return "RESULT";
                case 2:
                    return "LIST";
                case 3:
                    return "GROUP";
                case 4:
                    return "Qxm Drafts";
                case 5:
                    return "My Polls";

            }
            return null;
        }
    }
    //endregion

    //region set user image
    public void setUserImage() {

        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        user = realmService.getSavedUser();

        if (user != null) {
            if (!TextUtils.isEmpty(user.getProfilePic())) {
                picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivUserImage);
            }
        }
    }
    //endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: Called");
        if (getActivity() != null) {
            ((BottomNavigationViewEx) getActivity().findViewById(R.id.bottom_navigation_view)).getMenu().getItem(3).setChecked(true);

        }


        if(viewPagerMyQxm.getCurrentItem()==2){
            getActivity().findViewById(R.id.fabCreateList).setVisibility(View.VISIBLE);
        }
        else if(viewPagerMyQxm.getCurrentItem()==3){
            getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.VISIBLE);
        }
    }

    // endregion


    @Override
    public void onStop() {
        super.onStop();
        //hiding fab icon during moving from the fragment
        if (getActivity() != null) {
            Log.d(TAG, "onPause: Called");
            getActivity().findViewById(R.id.fabCreateList).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.GONE);
        }

    }

    // region setFabButtonVisibility
    private void setFabButtonVisibility(){

        viewPagerMyQxm.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

             //   Log.d(TAG, "onPageScrolled Called: "+String.valueOf(position));

            }
            @Override
            public void onPageSelected(int position) {

                Log.d(TAG, "onPageSelected Called: "+String.valueOf(position));


                if(getActivity()!=null){

                    switch (position) {
                        case 2:
                            if(NetworkState.haveNetworkConnection(context)){
                                Log.d(TAG, "onPageSelected: create list fav icon visible");
                                getActivity().findViewById(R.id.fabCreateList).setVisibility(View.VISIBLE);
                                getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.GONE);
                            }

                            break;
                        case 3:
                            if(NetworkState.haveNetworkConnection(context)){
                                Log.d(TAG, "onPageSelected: create group fav icon visible");
                                getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.VISIBLE);
                                getActivity().findViewById(R.id.fabCreateList).setVisibility(View.GONE);
                            }
                            break;
                        default:

                            Log.d(TAG, "onPageSelected: ****GoneEverything****");
                            getActivity().findViewById(R.id.fabCreateList).setVisibility(View.GONE);
                            getActivity().findViewById(R.id.fabCreateGroup).setVisibility(View.GONE);
                            break;
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //endregion
}
