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
import com.crux.qxm.adapter.myQxm.group.GroupPendingRequestListAdapter;
import com.crux.qxm.db.models.group.groupPendingRequest.GroupPendingRequestListItem;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.groupPendingRequestListFragmentFeature.DaggerGroupPendingRequestListFragmentComponent;
import com.crux.qxm.di.groupPendingRequestListFragmentFeature.GroupPendingRequestListFragmentComponent;
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
public class GroupPendingRequestListFragment extends Fragment {

    private static final String TAG = "GroupSentInvitationList";

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private String groupId;
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;
    private RealmService realmService;
    private GroupPendingRequestListAdapter groupPendingRequestListAdapter;
    private List<GroupPendingRequestListItem> groupPendingRequestListItemList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvGroupInvitations)
    RecyclerView rvGroupInvitations;

    public GroupPendingRequestListFragment() {
        // Required empty public constructor
    }

    public static GroupPendingRequestListFragment newInstance(String groupId) {

        Bundle args = new Bundle();
        args.putString(GROUP_ID_KEY, groupId);

        GroupPendingRequestListFragment fragment = new GroupPendingRequestListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_pending_request_list, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

        if(groupPendingRequestListItemList.size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            getGroupInvitationListNetworkCall(token.getToken(), groupId);
        }


    }

    private void setUpDagger2(Context context) {

        GroupPendingRequestListFragmentComponent groupPendingRequestListFragmentComponent =
                DaggerGroupPendingRequestListFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        groupPendingRequestListFragmentComponent.injectGroupPendingRequestListFragmentFeature(GroupPendingRequestListFragment.this);
    }

    private void init(View view) {

        groupId = getArguments() != null ? getArguments().getString(GROUP_ID_KEY) : null;
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        groupPendingRequestListItemList = new ArrayList<>();

        groupPendingRequestListAdapter = new GroupPendingRequestListAdapter(context, groupPendingRequestListItemList,
                qxmFragmentTransactionHelper, picasso, apiService, token.getToken(), token.getUserId(), groupId);
        rvGroupInvitations.setAdapter(groupPendingRequestListAdapter);
        rvGroupInvitations.setLayoutManager(new LinearLayoutManager(context));
        rvGroupInvitations.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            groupPendingRequestListItemList.clear();
            groupPendingRequestListAdapter.notifyDataSetChanged();
            getGroupInvitationListNetworkCall(token.getToken(), groupId);
        });
    }

    private void initializeClickListeners() {

        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

    private void getGroupInvitationListNetworkCall(String token, String groupId) {
        Call<List<GroupPendingRequestListItem>> getGroupInvitationList = apiService.getGroupPendingRequestList(token, groupId);

        getGroupInvitationList.enqueue(new Callback<List<GroupPendingRequestListItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<GroupPendingRequestListItem>> call, @NonNull Response<List<GroupPendingRequestListItem>> response) {
                Log.d(TAG, "StatusCode: " + response.code());

                swipeRefreshLayout.setRefreshing(false);

                if(response.code() == 200){

                    if(response.body() != null){
                        groupPendingRequestListItemList.addAll(response.body());
                        groupPendingRequestListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(context, "You have no pending request.", Toast.LENGTH_SHORT).show();
                    }

                }else if(response.code() == 403){
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }
                else {
                    Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GroupPendingRequestListItem>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getGroupInvitationListNetworkCall");
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
