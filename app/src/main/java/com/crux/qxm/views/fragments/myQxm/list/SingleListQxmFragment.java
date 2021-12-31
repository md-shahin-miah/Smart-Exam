package com.crux.qxm.views.fragments.myQxm.list;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.list.ListFeedAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.singleListQxmFragmentFeature.DaggerSingleListQxmFragmentComponent;
import com.crux.qxm.di.singleListQxmFragmentFeature.SingleListQxmFragmentComponent;
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

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.LIST_ID;
import static com.crux.qxm.utils.StaticValues.LIST_TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleListQxmFragment extends Fragment implements ListFeedAdapter.ListFeedAdapterListener {


    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private static final String TAG = "SingleListQxmFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvToolbarMsg)
    AppCompatTextView tvToolbarMsg;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivFilter)
    AppCompatImageView ivFilter;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.listFeedProgressBar)
    ProgressBar listFeedProgressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;


    private List<Object> listFeedData;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private ListFeedAdapter listFeedAdapter;

    private String listId;
    private String list_title;

    public SingleListQxmFragment() {
        // Required empty public constructor
    }

    // region Fragment-NewInstance

    public static SingleListQxmFragment newInstance(String listId,String listTitle) {

        Bundle args = new Bundle();
        args.putString(LIST_ID, listId);
        args.putString(LIST_TITLE, listTitle);
        SingleListQxmFragment fragment = new SingleListQxmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_single_list_qxm, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listId = getArguments() != null ? getArguments().getString(LIST_ID) : null;
        Log.d(TAG, "onViewCreated: listId: " + listId);
        list_title = getArguments() != null ? getArguments().getString(LIST_TITLE) : null;

        context = getActivity();
        setUpDagger2(context);
        init();
        viewOnClickListener();

        if (listFeedData.size() == 0)
            getListFeedItemNetworkCall();



    }

    // region SetupDagger2
    private void setUpDagger2(Context context) {

        SingleListQxmFragmentComponent singleListQxmFragmentComponent =
                DaggerSingleListQxmFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        singleListQxmFragmentComponent.injectSingleListQxmFragmentFeature(SingleListQxmFragment.this);
    }
    //endregion

    // region Init
    private void init() {


        if (listFeedData == null)
            listFeedData = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        listFeedAdapter = new ListFeedAdapter(getContext(), listFeedData, getActivity(), picasso, token.getUserId(), token.getToken(), listId, apiService, this);
        rvFeed.setAdapter(listFeedAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRL.setOnRefreshListener(() -> {
            listFeedAdapter.clear();
            getListFeedItemNetworkCall();

        });

        tvToolbarMsg.setText(list_title);

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
        ivSearch.setOnClickListener(v-> Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show());

        // filter icon click action
        ivFilter.setOnClickListener(v-> Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show());

    }
    //endregion

    // region GetFeedItemListNetworkCall

    private void getListFeedItemNetworkCall() {

        Call<List<FeedData>> getFeedItemList = apiService.getListFeed(token.getToken(),listId, token.getUserId());

        getFeedItemList.enqueue(new Callback<List<FeedData>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedData>> call, @NonNull Response<List<FeedData>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    List<FeedData> feedItemList = response.body();
                    listFeedData.clear();
                    listFeedData.addAll(feedItemList);

                    if(listFeedData.size()==0) {
                        tvNotFound.setVisibility(View.VISIBLE);
                        tvNotFound.setText(R.string.no_qxm_found_in_this_list);
                    }
                    else {
                        tvNotFound.setVisibility(View.GONE);
                        rvFeed.setVisibility(View.VISIBLE);
                    }
                    // passing feed to the recycler view adapter

                    listFeedAdapter.notifyDataSetChanged();


//                    Log.d(TAG, "Feed: " + listFeedData.toString());

                    swipeRL.setRefreshing(false);

                }else {
                    swipeRL.setRefreshing(false);
                    Log.d(TAG, "onResponse: Feed network call failed");

                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedData>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getFeedItem");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRL.setRefreshing(false);
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemSelected(String qxmId) {
        Log.d("GroupFeedAdapterListen", TAG + " qxmId: " + qxmId);
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
    }

    @Override
    public void listFeedAdapterEmpty() {
        rvFeed.setVisibility(View.GONE);
        tvNotFound.setVisibility(View.VISIBLE);
        tvNotFound.setText(R.string.no_qxm_found_in_this_list);

    }

    // endregion

}
