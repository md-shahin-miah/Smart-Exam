package com.crux.qxm.views.fragments.group;


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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.group.InviteMemberToGroupAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.addMemberToGroup.AddMemberList;
import com.crux.qxm.db.models.group.addMemberToGroup.Follower;
import com.crux.qxm.db.models.group.addMemberToGroup.Following;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.groupJoinRequestSentFragmentFeature.DaggerGroupJoinRequestSentFragmentComponent;
import com.crux.qxm.di.groupJoinRequestSentFragmentFeature.GroupJoinRequestSentFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.GROUP_ID_KEY;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteMembersToGroupFragment extends Fragment {

    private static final String TAG = "GroupJoinRequestSentFra";
    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    // region BindViewWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvDone)
    AppCompatTextView tvDone;
    @BindView(R.id.svAddMember)
    SearchView svAddMember;
    @BindView(R.id.rvSuggestedUser)
    RecyclerView rvSuggestedUser;
    @BindView(R.id.searchProgressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;
    //endregion

    private String groupId;
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;

    //region RxJava
    private QxmToken token;
    private AddMemberList memberList;

    //endregion
    private List<Object> suggestedMemberList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private InviteMemberToGroupAdapter inviteMemberToGroupAdapter;
    private List<String> selectedUserIds;

    private CompositeDisposable compositeDisposable;
    private PublishSubject<String> publishSubject;
    private String userId;
    private String userToken;


    // region searchView onQueryTextListener
    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {

            Log.d(TAG, "onQueryTextSubmit: " + s);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            Log.d(TAG, "onQueryTextChange: " + s);
            Log.d(TAG, "onQueryTextChange queryLength :" + s.length());
            tvNotFound.setVisibility(View.GONE);

            if (s.length() >= 1) {

                searchProgressBar.setVisibility(View.VISIBLE);
                rvSuggestedUser.setVisibility(View.GONE);
                instantSearchSuggestedUser(s);

            } else {

                compositeDisposable.clear();
                getSuggestedAddMemberListNetworkCall();
                Log.d(TAG, "onQueryTextChange: query length is less than 1");
            }
            return false;
        }
    };

    public InviteMembersToGroupFragment() {
        // Required empty public constructor
    }

    public static InviteMembersToGroupFragment newInstance(String groupId) {

        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);

        InviteMembersToGroupFragment fragment = new InviteMembersToGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_members_to_group, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

        if (memberList == null)
            getSuggestedAddMemberListNetworkCall();


    }

    private void setUpDagger2(Context context) {
        GroupJoinRequestSentFragmentComponent groupJoinRequestSentFragmentComponent =
                DaggerGroupJoinRequestSentFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        groupJoinRequestSentFragmentComponent.injectGroupJoinRequestSentFragmentFeature(InviteMembersToGroupFragment.this);
    }

    private void init(View view) {
        groupId = getArguments() != null ? getArguments().getString(GROUP_ID_KEY) : null;
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (suggestedMemberList == null)
            suggestedMemberList = new ArrayList<>();

        if (selectedUserIds == null)
            selectedUserIds = new ArrayList<>();


        inviteMemberToGroupAdapter = new InviteMemberToGroupAdapter(context, suggestedMemberList, selectedUserIds, qxmFragmentTransactionHelper, picasso, apiService, groupId, token.getToken(), token.getUserId());

        rvSuggestedUser.setAdapter(inviteMemberToGroupAdapter);
        rvSuggestedUser.setLayoutManager(new LinearLayoutManager(context));
        rvSuggestedUser.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        //rxJava objects
        compositeDisposable = new CompositeDisposable();
        publishSubject = PublishSubject.create();

        setupSearchView();
        getUserIdAndToken();


    }

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        tvDone.setOnClickListener(v -> {
            Log.d(TAG, "selectedUserIds: " + selectedUserIds.toString());

            addMembersToGroupNetworkCall(groupId, selectedUserIds);

            Toast.makeText(context, selectedUserIds.toString(), Toast.LENGTH_SHORT).show();
        });
    }

    private void addMembersToGroupNetworkCall(String groupId, List<String> selectedUserIds) {

        Call<QxmApiResponse> addMembersToGroup = apiService.addMembersToGroup(token.getToken(), groupId, token.getUserId(), selectedUserIds);

        addMembersToGroup.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: addMembersToGroupNetworkCall ");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 201) {
                    Toast.makeText(context, "Member added successfully", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).onBackPressed();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: addMembersToGroupNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSuggestedAddMemberListNetworkCall() {
        Call<AddMemberList> getSuggestedAddMemberList = apiService.getSuggestedAddMemberList(token.getToken(), token.getUserId(), groupId);

        getSuggestedAddMemberList.enqueue(new Callback<AddMemberList>() {
            @Override
            public void onResponse(@NonNull Call<AddMemberList> call, @NonNull Response<AddMemberList> response) {
                Log.d(TAG, "onResponse: getSuggestedAddMemberListNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {

                        memberList = response.body();
                        suggestedMemberList.addAll(memberList.getFollowing());

                        for (Follower follower : memberList.getFollower()) {
                            boolean duplicate = false;
                            for (Following following : memberList.getFollowing()) {
                                if (follower.getUserId().equals(following.getUserId())) {
                                    duplicate = true;
                                    break;
                                }
                            }
                            if (!duplicate) suggestedMemberList.add(follower);
                        }

                        rvSuggestedUser.setVisibility(View.VISIBLE);
                        searchProgressBar.setVisibility(View.GONE);
                        inviteMemberToGroupAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "onResponse: getSuggestedAddMemberListNetworkCall failed");
                        Log.d(TAG, "onResponse: response body is null");
                        Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: getSuggestedAddMemberListNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddMemberList> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getSuggestedAddMemberListNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // endregion

    //region instantSearchSuggestedUser
    private void instantSearchSuggestedUser(String query) {

        DisposableObserver<AddMemberList> userSearchObserver = getSearchAddMemberObserver();

        compositeDisposable.add(
                publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return !s.trim().isEmpty();

                            }
                        })
                        .switchMapSingle(new Function<String, SingleSource<AddMemberList>>() {
                            @Override
                            public SingleSource<AddMemberList> apply(String s) throws Exception {
                                return apiService.getSearchedSuggestedAddMemberList(userToken, userId, groupId, s)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        }).subscribeWith(userSearchObserver));

        publishSubject.onNext(query);
    }
    //endregion

    private void setupSearchView() {
        svAddMember.setOnQueryTextListener(onQueryTextListener);
        svAddMember.setIconifiedByDefault(false);  // Do not iconify the widget; expand it by default
        svAddMember.setIconified(false);
        svAddMember.setQueryRefinementEnabled(true); // an arrow button will be added which allow user to refine the query from history

        svAddMember.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Toast.makeText(context, "Closed Search view", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //region getSearchAddMemberObserver (it will observe the api call for suggested member search)
    private DisposableObserver<AddMemberList> getSearchAddMemberObserver() {

        Log.d(TAG, "getSearchAddMemberObserver: Called");

        return new DisposableObserver<AddMemberList>() {
            @Override
            public void onNext(AddMemberList addMemberList) {

                Log.d(TAG, "onNext suggested user: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSuggestedUser.setVisibility(View.VISIBLE);
                suggestedMemberList.clear();

                Log.d(TAG, "onNext addMemberList: "+addMemberList);


                if (addMemberList.getFollower() != null)
                    suggestedMemberList.addAll(addMemberList.getFollower());
                if (addMemberList.getFollowing() != null)
                    suggestedMemberList.addAll(addMemberList.getFollowing());

                if (suggestedMemberList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(getResources().getString(R.string.no_qxm_found));
                } else tvNotFound.setVisibility(View.GONE);

                inviteMemberToGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                Log.d(TAG, "onError suggested add member search :" + e.getMessage());
                Toast.makeText(getApplicationContext(), R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                searchProgressBar.setVisibility(View.GONE);
                rvSuggestedUser.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete suggested member search: called");
            }
        };

    }
    //endregion

    //region get userId and token
    public void getUserIdAndToken() {
        userId = token.getUserId();
        userToken = token.getToken();
    }
    //endregion

    //region Override-OnStart

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

    // region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // don't need observe anymore if activity is destroyed
    }
    //endregion
}
