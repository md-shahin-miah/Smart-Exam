package com.crux.qxm.views.fragments.group;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.crux.qxm.adapter.myQxm.group.AddMemberToGroupAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.group.addMemberToGroup.AddMemberList;
import com.crux.qxm.db.models.group.addMemberToGroup.Follower;
import com.crux.qxm.db.models.group.addMemberToGroup.Following;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.addMembersToGroupFragmentFeature.AddMembersToGroupFragmentComponent;
import com.crux.qxm.di.addMembersToGroupFragmentFeature.DaggerAddMembersToGroupFragmentComponent;
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

import static com.crux.qxm.utils.StaticValues.GROUP_ID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMembersToGroupFragment extends Fragment {

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private static final String TAG = "GroupJoinRequestSentFra";
    private String groupId;
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private AddMemberList memberList;
    private List<Object> suggestedMemberList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private AddMemberToGroupAdapter addMemberToGroupAdapter;
    private List<String> selectedUserIds;
    // region BindViewWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    @BindView(R.id.tvDone)
    AppCompatTextView tvDone;

    @BindView(R.id.svAddMember)
    SearchView svAddMember;

    @BindView(R.id.rvSuggestedUser)
    RecyclerView rvSuggestedUser;


    // endregion


    public AddMembersToGroupFragment() {
        // Required empty public constructor
    }

    public static AddMembersToGroupFragment newInstance(String groupId) {

        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);

        AddMembersToGroupFragment fragment = new AddMembersToGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_members_to_group, container, false);

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
        AddMembersToGroupFragmentComponent addMembersToGroupFragmentComponent =
                DaggerAddMembersToGroupFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        addMembersToGroupFragmentComponent.injectAddMembersToGroupFragmentFeature(AddMembersToGroupFragment.this);

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


        addMemberToGroupAdapter = new AddMemberToGroupAdapter(context, suggestedMemberList, selectedUserIds, qxmFragmentTransactionHelper, picasso, apiService, groupId, token.getToken(), token.getUserId());

        rvSuggestedUser.setAdapter(addMemberToGroupAdapter);
        rvSuggestedUser.setLayoutManager(new LinearLayoutManager(context));
        rvSuggestedUser.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

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

                        addMemberToGroupAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "onResponse: getSuggestedAddMemberListNetworkCall failed");
                        Log.d(TAG, "onResponse: response body is null");
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
}
