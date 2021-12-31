package com.crux.qxm.views.fragments.myQxm;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
import com.crux.qxm.adapter.myQxm.MyQxmAdapter;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmInitialFragmentFeature.DaggerMyQxmInitialFragmentComponent;
import com.crux.qxm.di.myQxmInitialFragmentFeature.MyQxmInitialFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.recyclerViewPaginationHelper.EndlessRecyclerViewScrollListener;
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

import static android.view.View.GONE;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS;
import static com.crux.qxm.utils.StaticValues.FILTER_ALL_QXMS;
import static com.crux.qxm.utils.StaticValues.FILTER_PRIVATE_QXMS;
import static com.crux.qxm.utils.StaticValues.FILTER_PUBLIC_QXMS;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.SINGLE_QXM_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY;
import static com.crux.qxm.utils.StaticValues.isMyQxmFeedRefreshingNeeded;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmInitialFragment extends Fragment implements MyQxmAdapter.MyQxmAdapterListener {

    private static final String TAG = "MyQxmInitialFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private QxmToken token;
    private MyQxmAdapter myQxmAdapter;
    private List<FeedData> myQxmFeedData;
    private List<FeedData> filteredFeedData;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private PopupMenu filterPopupMenu;
    private String filterType;

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    @BindView(R.id.rlFilterContainer)
    RelativeLayout rlFilterContainer;
    @BindView(R.id.tvFilterTitle)
    AppCompatTextView tvFilterTitle;
    @BindView(R.id.ivFilter)
    AppCompatImageView ivFilter;
    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVFeed)
    RecyclerView RVFeed;
    @BindView(R.id.noInternetView)
    View noInternetView;

    AppCompatTextView tvRetry;

    public MyQxmInitialFragment() {
        // Required empty public constructor
    }

    // region onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_initial, container, false);

        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment

        return view;

    }
    // endregion

    // region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        context = view.getContext();
        setUpDagger2(context);

        init();

        noInternetFunctionality();

        initializeClickListeners();

        if (myQxmFeedData.size() == 0) {
            myQxmAdapter.clear();
            swipeRefreshLayout.setRefreshing(true);
            getMyQxmFeedNetworkCall(0);
        }

    }

    private void initializeClickListeners() {
        ivFilter.setOnClickListener(v -> filterPopupMenu.show());

        filterPopupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.filter_all_qxm:
                    filterType = FILTER_ALL_QXMS;
                    tvFilterTitle.setText(getResources().getString(R.string.all_qxms));
                    myQxmAdapter.clear();
                    getMyQxmFeedNetworkCall(0);

                    /*filteredFeedData.clear();
                    filteredFeedData.addAll(myQxmFeedData);
                    myQxmAdapter.notifyDataSetChanged();*/
                    Log.d(TAG, "Filter: All Qxm ");
                    break;

                case R.id.filter_public_qxm:
                    filterType = FILTER_PUBLIC_QXMS;
                    tvFilterTitle.setText(getResources().getString(R.string.public_qxms));
                    myQxmAdapter.clear();
                    getMyQxmFeedNetworkCall(0);

                    /*filteredFeedData.clear();

                    for (int i = 0; i < myQxmFeedData.size(); i++) {
                        if (myQxmFeedData.get(i).getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
                            filteredFeedData.add(myQxmFeedData.get(i));
                        }
                    }

                    myQxmAdapter.notifyDataSetChanged();*/

                    Log.d(TAG, "Filter: Public Qxm ");
                    break;

                case R.id.filter_private_qxm:
                    filterType = FILTER_PRIVATE_QXMS;
                    tvFilterTitle.setText(getResources().getString(R.string.private_qxms));

                    myQxmAdapter.clear();
                    getMyQxmFeedNetworkCall(0);

                    /*filteredFeedData.clear();

                    for (int i = 0; i < myQxmFeedData.size(); i++) {
                        if (myQxmFeedData.get(i).getFeedPrivacy().equals(QXM_PRIVACY_PRIVATE)) {
                            filteredFeedData.add(myQxmFeedData.get(i));
                        }
                    }

                    myQxmAdapter.notifyDataSetChanged();*/

                    Log.d(TAG, "Filter: Private Qxm ");

                    break;
            }

            StaticValues.CURRENT_FILTER = filterType;
            Log.d(TAG, "initializeClickListeners: current filter: " + StaticValues.CURRENT_FILTER);

            return false;
        });
    }

    // endregion

    // region setup dagger2
    private void setUpDagger2(Context context) {

        MyQxmInitialFragmentComponent myQxmInitialFragmentComponent =
                DaggerMyQxmInitialFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmInitialFragmentComponent.injectMyQxmInitialFragmentFeature(MyQxmInitialFragment.this);

    }
    //endregion

    // region Init

    private void init() {

        if (myQxmFeedData == null)
            myQxmFeedData = new ArrayList<>();

        if (filteredFeedData == null)
            filteredFeedData = new ArrayList<>();

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        filterType = StaticValues.CURRENT_FILTER;
        if(filterType.equals(FILTER_PRIVATE_QXMS)){
            tvFilterTitle.setText(getString(R.string.private_qxms));
        }

        filterPopupMenu = new PopupMenu(context, ivFilter);
        filterPopupMenu.getMenuInflater().inflate(R.menu.menu_my_qxm_filter, filterPopupMenu.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filterPopupMenu.setGravity(Gravity.END);
        }

        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getContext());
//        myQxmAdapter = new MyQxmAdapter(getContext(), myQxmFeedData, getActivity(), picasso,apiService,token, this);
        myQxmAdapter = new MyQxmAdapter(getContext(), filteredFeedData, getActivity(), picasso, apiService, token, this);
        RVFeed.setAdapter(myQxmAdapter);
        RVFeed.setLayoutManager(rvLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myQxmAdapter.clear();
            getMyQxmFeedNetworkCall(0);

        });


        //region recycler view scroll listener

        RVFeed.addOnScrollListener(new EndlessRecyclerViewScrollListener(rvLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.d(TAG, "onLoadMore page: "+page);

                getMyQxmFeedNetworkCall(page);

            }
        });

        //endregion

    }

    // endregion

    // region GetFeedItemListNetworkCall

    private void getMyQxmFeedNetworkCall(int paginationIndex) {

        Call<List<FeedData>> getMyQxmFeed = apiService.getMyQxmFeed(token.getToken(), token.getUserId(),paginationIndex);

        getMyQxmFeed.enqueue(new Callback<List<FeedData>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedData>> call, @NonNull Response<List<FeedData>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());
                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");
                    List<FeedData> myQxmFeed = response.body();
                    myQxmFeedData.clear();
                    myQxmFeedData.addAll(myQxmFeed);

                    Log.d(TAG, "onResponse: filtered feed data : " + filteredFeedData.size());
                    Log.d(TAG, "onResponse: myQxmFeedData : " + myQxmFeedData.size());


                    if (myQxmFeedData.size() == 0 && paginationIndex ==  0){
                        rlFilterContainer.setVisibility(View.GONE);
                        tvFeedEmptyMessage.setVisibility(View.VISIBLE);
                    }
                    else{
                        rlFilterContainer.setVisibility(View.VISIBLE);
                        tvFeedEmptyMessage.setVisibility(View.GONE);

                        // region for filter-all
                        switch (filterType){
                            case FILTER_ALL_QXMS:
                                filteredFeedData.addAll(myQxmFeedData);
                                break;

                            case FILTER_PUBLIC_QXMS:

                                for (int i = 0; i < myQxmFeedData.size(); i++) {
                                    if (myQxmFeedData.get(i).getFeedPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
                                        filteredFeedData.add(myQxmFeedData.get(i));
                                    }
                                }

                                if(filteredFeedData.size() == 0){
                                    Toast.makeText(context, "You have not created any public Qxm yet.", Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case FILTER_PRIVATE_QXMS:

                                for (int i = 0; i < myQxmFeedData.size(); i++) {
                                    if (myQxmFeedData.get(i).getFeedPrivacy().equals(QXM_PRIVACY_PRIVATE)) {
                                        filteredFeedData.add(myQxmFeedData.get(i));
                                    }
                                }

                                if(filteredFeedData.size() == 0){
                                    Toast.makeText(context, "You have not created any private Qxm yet.", Toast.LENGTH_SHORT).show();
                                }

                                break;
                        }

                        // endregion
                    }

                    // passing feed to the recycler view adapter
                    myQxmAdapter.notifyDataSetChanged();

                    Log.d(TAG, "MyQxmFeed: " + myQxmFeedData.toString());

                    swipeRefreshLayout.setRefreshing(false);

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {

                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse: Feed network call failed");

                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedData>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getFeedItem");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                //hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
            }
        });


    }


    // endregion

    //region NoInternetFunctionality

    private void noInternetFunctionality() {

        tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            myQxmAdapter.clear();
            getMyQxmFeedNetworkCall(0);
        });
    }

    //endregion

    @Override
    public void onQxmSelected(String qxmId, String qxmTitle) {

        Log.d("QxmID", qxmId);
        //Log.d("FeedDataToPass",feedDataToPass.toString());
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxmId);
        args.putString(SINGLE_QXM_DETAILS_TITLE_KEY, qxmTitle);

        // going to qxm details fragment with qxmid
        qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);
    }

    @Override
    public void onSingleMCQItemSelected(String singleMCQId, String singleMCQTitle) {
        Log.d("SingleMCQID", singleMCQId);
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS, singleMCQId);
        args.putString(SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY, singleMCQTitle);

        //going to single MCQ details fragment with singleMCQId
        qxmFragmentTransactionHelper.loadSingleMCQDetailsFragment(args);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isMyQxmFeedRefreshingNeeded){
            isMyQxmFeedRefreshingNeeded = false;
            swipeRefreshLayout.setRefreshing(true);
            myQxmAdapter.clear();
            getMyQxmFeedNetworkCall(0);
        }
    }
}
