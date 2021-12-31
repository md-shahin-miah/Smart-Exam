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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.group.GroupMemberListAdapter;
import com.crux.qxm.db.models.group.groupMemberList.GroupMembers;
import com.crux.qxm.db.models.group.groupMemberList.MembersItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.groupMemberListFragmentFeature.DaggerGroupMemberListFragmentComponent;
import com.crux.qxm.di.groupMemberListFragmentFeature.GroupMemberListFragmentComponent;
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
import static com.crux.qxm.utils.StaticValues.MY_STATUS_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMemberListFragment extends Fragment {

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private static final String TAG = "GroupMemberListFragment";
    private String groupId;
    private String myStatus;
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private List<Object> groupMemberList;
    private List<MembersItem> memberList;
    private GroupMemberListAdapter memberListAdapter;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvGroupMembers)
    RecyclerView rvGroupMembers;


    public GroupMemberListFragment() {
        // Required empty public constructor
    }

    public static GroupMemberListFragment newInstance(String groupId, String myStatus) {

        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);
        args.putString(MY_STATUS_KEY, myStatus);

        GroupMemberListFragment fragment = new GroupMemberListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_member_list, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

        if(groupMemberList.size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            getGroupMembersNetworkCall(groupId);
        }
    }

    private void setUpDagger2(Context context) {
        GroupMemberListFragmentComponent groupMemberListFragmentComponent =
                DaggerGroupMemberListFragmentComponent.builder()
                .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                .build();

        groupMemberListFragmentComponent.injectGroupMemberListFragmentFeature(GroupMemberListFragment.this);
    }

    private void init(View view) {
        groupId = getArguments() != null ? getArguments().getString(GROUP_ID_KEY) : null;
        myStatus = getArguments().getString(MY_STATUS_KEY);
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        groupMemberList = new ArrayList<>();
        memberList = new ArrayList<>();
        QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        memberListAdapter = new GroupMemberListAdapter(context, groupMemberList, picasso, qxmFragmentTransactionHelper, realmService, apiService, token, groupId, myStatus);

        rvGroupMembers.setAdapter(memberListAdapter);
        rvGroupMembers.setLayoutManager(new LinearLayoutManager(context));
        rvGroupMembers.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    }

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            memberListAdapter.clear();
            getGroupMembersNetworkCall(groupId);
        });
    }

    private void getGroupMembersNetworkCall(String groupId) {

        Call<GroupMembers> getFollowerList = apiService.getGroupMemberList(token.getToken(), groupId);

        getFollowerList.enqueue(new Callback<GroupMembers>() {
            @Override
            public void onResponse(@NonNull Call<GroupMembers> call, @NonNull Response<GroupMembers> response) {
                Log.d(TAG, "onResponse: getGroupMembersNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                swipeRefreshLayout.setRefreshing(false);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: getGroupMemberList network call success");

                    if(response.body() != null){
                        Log.d(TAG, "onResponse: response body is not null" );
                        groupMemberList.addAll(response.body().getAdmin());
                        groupMemberList.addAll(response.body().getModerator());
                        groupMemberList.addAll(response.body().getMembers());

                        if (groupMemberList.size() == 0) {
                            // todo:: show msg group has no member
                            Toast.makeText(context, "This group has no member", Toast.LENGTH_SHORT).show();

                        } else {
                            // todo:: gone msg group has no member
                            Log.d(TAG, "onResponse: memberListAdapter notifyDataSetChanged called");
                            memberListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.d(TAG, "onResponse: getGroupMembersNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<GroupMembers> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: getGroupMembersNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
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
