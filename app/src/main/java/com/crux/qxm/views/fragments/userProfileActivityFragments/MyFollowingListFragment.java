package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.MyFollowingAdapter;
import com.crux.qxm.db.models.profile.User;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myFollowingListFragmentFeature.DaggerMyFollowingListFragmentComponent;
import com.crux.qxm.di.myFollowingListFragmentFeature.MyFollowingListFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.USER_ID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFollowingListFragment extends Fragment {


    private static final String TAG = "MyFollowingListFragment";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvToolbarMsg)
    AppCompatTextView tvToolbarMsg;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.rvFollowingList)
    RecyclerView rvFollowingList;
    @BindView(R.id.progressbar)
    ProgressBar listFeedProgressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private List<User> followingList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private MyFollowingAdapter myFollowingAdapter;
    private String userId;


    // region Fragment-NewInstance
    public static MyFollowingListFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(USER_ID_KEY,userId);
        MyFollowingListFragment fragment = new MyFollowingListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    public MyFollowingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_following_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userId = getArguments() != null ? getArguments().getString(USER_ID_KEY) : null;

        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        setUpDagger2(context);
        init();
        viewOnClickListener();

        if (followingList.size() == 0){
            swipeRL.setRefreshing(true);
            getMyFollowingListNetworkCall();
        }
    }

    // region SetupDagger2
    private void setUpDagger2(Context context) {

        MyFollowingListFragmentComponent myFollowingListFragmentComponent =
                DaggerMyFollowingListFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myFollowingListFragmentComponent.injectMyFollowingListFeature(MyFollowingListFragment.this);
    }
    //endregion

    // region Init
    private void init() {


        if (followingList == null)
            followingList = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        // going to user profile fragment through interface call
        myFollowingAdapter = new MyFollowingAdapter(getContext(), followingList, picasso, following -> {

            qxmFragmentTransactionHelper.loadUserProfileFragment(following.getId(),following.getFullName());

        });
        rvFollowingList.setAdapter(myFollowingAdapter);
        rvFollowingList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowingList.setItemAnimator(new DefaultItemAnimator());
        rvFollowingList.addItemDecoration( new DividerItemDecoration(Objects.requireNonNull(getContext()),DividerItemDecoration.VERTICAL));
        swipeRL.setOnRefreshListener(() -> {
            myFollowingAdapter.clear();
            getMyFollowingListNetworkCall();

        });

        //tvToolbarMsg.setText(list_title);

    }
    // endregion

    //region view onclickListener
    private void viewOnClickListener(){

        // going previous fragment when click on back press
        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getFragmentManager() != null) {
                Log.d(TAG, "pop SingleListQxmFragment fragment");
                getFragmentManager().popBackStack();
            }
        });

        // search icon click action
        ivSearch.setOnClickListener(v-> qxmFragmentTransactionHelper.loadSearchFragmentFragment());

    }
    //endregion

    // region getMyFollowingListNetworkCall

    private void getMyFollowingListNetworkCall() {

        Call<List<User>> getFollowerList = apiService.getFollowingListSimple(token.getToken(),userId);
        getFollowerList.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    List<User> followingListFromApi = response.body();
                    followingList.clear();
                    followingList.addAll(followingListFromApi);

                    if(followingList.size()==0) {
                        tvNotFound.setVisibility(View.VISIBLE);
                        tvNotFound.setText(R.string.you_have_not_followed_any_user_yet);
                    }
                    else tvNotFound.setVisibility(View.GONE);

                    myFollowingAdapter.notifyDataSetChanged();


                    swipeRL.setRefreshing(false);

                }else {
                    swipeRL.setRefreshing(false);
                    Log.d(TAG, "onResponse: Feed network call failed");

                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t){

                Log.d(TAG, "onFailure: getFollowerList");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRL.setRefreshing(false);
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    // endregion

    // region Override-OnDestroyView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    // endregion

}
