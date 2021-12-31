package com.crux.qxm.views.fragments.following;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.following.FollowingListAdapter;
import com.crux.qxm.db.models.following.FollowingUser;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.followingUserListFragmentFeature.DaggerFollowingUserListFragmentComponent;
import com.crux.qxm.di.followingUserListFragmentFeature.FollowingUserListFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingListFragment extends Fragment {

    // region FollowingListFragment-Properties
    private static final String TAG = "FollowingUserListFragme";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private Context context;
    private QxmApiService apiService;
    private Realm realm;
    private RealmService realmService;
    private QxmToken token;
    private List<FollowingUser> followingList;
    private FollowingListAdapter followingListAdapter;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.ivBackArrowQuesOverView)
    AppCompatImageView ivBackArrowQuesOverView;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.spinnerFollowingFilter)
    AppCompatSpinner spinnerFollowingFilter;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvFollowingList)
    RecyclerView rvFollowingList;

    // endregion

    // region Fragment-EmptyConstructor
    public FollowingListFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following_list, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        context = view.getContext();

        setUpDagger2();

        init();

        if (followingList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getFollowingListNetworkCall();
        }

        setClickListeners();

    }

    // endregion

    // region SetUpDagger2
    private void setUpDagger2() {
        FollowingUserListFragmentComponent followingUserListFragmentComponent =
                DaggerFollowingUserListFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        followingUserListFragmentComponent.injectFollowingUserListFragmentFeature(FollowingListFragment.this);
    }

    // endregion

    // region Init

    private void init() {

        apiService = retrofit.create(QxmApiService.class);

        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (followingList == null) followingList = new ArrayList<>();

        initializeFollowingUserListAdapter();

    }

    // endregion

    // region InitializeFollowingUserListAdapter

    private void initializeFollowingUserListAdapter() {
        followingListAdapter = new FollowingListAdapter(context, getActivity(), picasso, followingList, token.getToken(), apiService);
        rvFollowingList.setAdapter(followingListAdapter);
        rvFollowingList.setLayoutManager(new LinearLayoutManager(context));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            followingListAdapter.clear();
            getFollowingListNetworkCall();

        });
    }

    // endregion

    // region GetFollowingListNetworkCall

    private void getFollowingListNetworkCall() {
        Call<List<FollowingUser>> getFollowingUserList = apiService.getFollowingUserList(token.getToken(), token.getUserId());

        getFollowingUserList.enqueue(new Callback<List<FollowingUser>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingUser>> call, @NonNull Response<List<FollowingUser>> response) {

                Log.d(TAG, "onResponse: response code = " + response.code());
                Log.d(TAG, "onResponse: response body = " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        followingList.addAll(response.body());
                        Log.d(TAG, "onResponse: followingList : " + followingList.size());
                        followingListAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        Log.d(TAG, "onResponse: response body is null");
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: following user not loaded.");
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FollowingUser>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: StackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // endregion

    // region SetClickListeners

    private void setClickListeners() {
        ivBackArrowQuesOverView.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());

        spinnerFollowingFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] filterOptions = context.getResources().getStringArray(R.array.following_filter);

                Toast.makeText(context, "Filter option " + filterOptions[position] + " is selected. This feature is coming soon.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // endregion
}
