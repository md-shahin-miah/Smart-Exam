package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.crux.qxm.adapter.profile.FollowerAdapter;
import com.crux.qxm.db.models.profile.User;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.followerListFragmentFeature.DaggerFollowerListFragmentComponent;
import com.crux.qxm.di.followerListFragmentFeature.FollowerListFragmentComponent;
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
public class FollowerListFragment extends Fragment {

    private static final String TAG = "FollowerListFragment";

    private QxmApiService apiService;
    private Context context;
    private QxmToken token;
    private List<User> followerList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private FollowerAdapter followerAdapter;
    private String userId;

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    // region BindViewsWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvToolbarMsg)
    AppCompatTextView tvToolbarMsg;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.rvFollowerList)
    RecyclerView rvFollowerList;
    @BindView(R.id.progressbar)
    ProgressBar listFeedProgressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;

    // endregion

    // region Fragment-NewInstance
    public static FollowerListFragment newInstance(String userId) {

        Bundle args = new Bundle();
        args.putString(USER_ID_KEY, userId);
        FollowerListFragment fragment = new FollowerListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Fragment-Constructor

    public FollowerListFragment() {
        // Required empty public constructor
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follower_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userId = getArguments() != null ? getArguments().getString(USER_ID_KEY) : null;

        context = getActivity();
        setUpDagger2(context);
        init();
        viewOnClickListener();

        if (followerList.size() == 0){
            swipeRL.setRefreshing(true);
            getFollowerListNetworkCall();
        }
    }

    // endregion

    // region SetupDagger2
    private void setUpDagger2(Context context) {

        FollowerListFragmentComponent followerListFragmentComponent =
                DaggerFollowerListFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        followerListFragmentComponent.injectFollowerListFragmentFeature(FollowerListFragment.this);
    }
    //endregion

    // region Init
    private void init() {


        if (followerList == null)
            followerList = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        // going to user profile fragment through interface call
        followerAdapter = new FollowerAdapter(getContext(), followerList, picasso, follower ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(follower.getId(), follower.getFullName()));
        rvFollowerList.setAdapter(followerAdapter);
        rvFollowerList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowerList.setItemAnimator(new DefaultItemAnimator());
        rvFollowerList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        swipeRL.setOnRefreshListener(() -> {
            followerAdapter.clear();
            getFollowerListNetworkCall();

        });

        //tvToolbarMsg.setText(list_title);

    }
    // endregion

    //region view onclickListener
    private void viewOnClickListener() {

        // going previous fragment when click on back press
        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getFragmentManager() != null) {
                Log.d(TAG, "pop SingleListQxmFragment fragment");
                getFragmentManager().popBackStack();
            }
        });

        // search icon click action
        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());

    }
    //endregion

    // region getFollowerListNetworkCall

    private void getFollowerListNetworkCall() {

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(context, "Request can not be perform. Try again after some time.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "getFollowerListNetworkCall: userId: " + userId);

            Call<List<User>> getFollowerList = apiService.getFollowerList(token.getToken(), userId);
            getFollowerList.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {

                    Log.d(TAG, "StatusCode: " + response.code());
                    Log.d(TAG, "response body: " + response.body());

                    if (response.code() == 200) {

                        Log.d(TAG, "onResponse: Feed network call success");

                        List<User> followerListFromApi = response.body();
                        followerList.clear();
                        followerList.addAll(followerListFromApi);

                        if (followerList.size() == 0) {
                            tvNotFound.setVisibility(View.VISIBLE);
                            tvNotFound.setText(R.string.you_have_not_followed_any_user_yet);
                        } else tvNotFound.setVisibility(View.GONE);

                        followerAdapter.notifyDataSetChanged();


                        swipeRL.setRefreshing(false);

                    } else {
                        swipeRL.setRefreshing(false);
                        Log.d(TAG, "onResponse: Feed network call failed");

                        Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {

                    Log.d(TAG, "onFailure: getFollowerList");
                    Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                    Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                    swipeRL.setRefreshing(false);
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            });
        }


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
