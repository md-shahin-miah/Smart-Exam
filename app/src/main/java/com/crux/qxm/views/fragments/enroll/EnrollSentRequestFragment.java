package com.crux.qxm.views.fragments.enroll;


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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.enroll.EnrollSentRequestAdapter;
import com.crux.qxm.db.models.enroll.sentEnrollRequest.SentEnrollRequestData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.enrollSentRequestFragmentFeature.DaggerEnrollSentRequestFragmentComponent;
import com.crux.qxm.di.enrollSentRequestFragmentFeature.EnrollSentRequestFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
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

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnrollSentRequestFragment extends Fragment {

    // region Fragment-Properties

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "EnrollYouEnrolledFragme";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private QxmToken token;
    private EnrollSentRequestAdapter enrollSentRequestAdapter;
    private List<SentEnrollRequestData> sentEnrollRequestFeedData;
    private AppCompatTextView tvRetry;

    // endregion

    // region BindViewsWithButterKnife

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVEnrollSentRequestFeed)
    RecyclerView RVEnrollSentRequestFeed;
    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.noInternetView)
    View noInternetView;

    // endregion

    // region Fragment-Constructor
    public EnrollSentRequestFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enroll_sent_requests, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OvViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        context = view.getContext();

        setUpDagger2(context);

        init();
        noInternetFunctionality();

        if (sentEnrollRequestFeedData.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getEnrollParticipatedFeedNetworkCall();
        }

    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        EnrollSentRequestFragmentComponent enrollSentRequestFragmentComponent =
                DaggerEnrollSentRequestFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        enrollSentRequestFragmentComponent.injectEnrollSentRequestFragmentFeature(EnrollSentRequestFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        if (sentEnrollRequestFeedData == null)
            sentEnrollRequestFeedData = new ArrayList<>();

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        enrollSentRequestAdapter = new EnrollSentRequestAdapter(getContext(), sentEnrollRequestFeedData, getActivity(), picasso, apiService, token);
        RVEnrollSentRequestFeed.setAdapter(enrollSentRequestAdapter);
        RVEnrollSentRequestFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            enrollSentRequestAdapter.clear();
            getEnrollParticipatedFeedNetworkCall();

        });
    }


    // endregion

    // region GetEnrollParticipatedFeedNetworkCall

    private void getEnrollParticipatedFeedNetworkCall() {
        Call<List<SentEnrollRequestData>> getMyQxmFeed = apiService.getEnrollSentRequestsList(token.getToken(), token.getUserId());

        getMyQxmFeed.enqueue(new Callback<List<SentEnrollRequestData>>() {
            @Override
            public void onResponse(@NonNull Call<List<SentEnrollRequestData>> call, @NonNull Response<List<SentEnrollRequestData>> response) {

                Log.d(TAG, "StatusCode: " + response.code());
//                Log.d(TAG, "response body: " + response.body());
                //hiding noInternetView
                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");
                    List<SentEnrollRequestData> myQxmFeed = response.body();
                    if (myQxmFeed != null) {
                        if (myQxmFeed.size() == 0) {
                            tvFeedEmptyMessage.setVisibility(View.VISIBLE);
                        } else {
                            tvFeedEmptyMessage.setVisibility(View.GONE);
                            sentEnrollRequestFeedData.addAll(myQxmFeed);
                        }
                    }

                    // passing feed to the recycler view adapter
                    enrollSentRequestAdapter.notifyDataSetChanged();

                    Log.d(TAG, "MyQxmFeed: " + sentEnrollRequestFeedData.toString());

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
            public void onFailure(@NonNull Call<List<SentEnrollRequestData>> call, @NonNull Throwable t) {
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
            enrollSentRequestAdapter.clear();
            getEnrollParticipatedFeedNetworkCall();
        });
    }

    //endregion
}
