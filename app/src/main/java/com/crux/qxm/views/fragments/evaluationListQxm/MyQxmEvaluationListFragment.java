package com.crux.qxm.views.fragments.evaluationListQxm;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myAllQxmEvaluatedList.MyQxmEvaluationListAdapter;
import com.crux.qxm.db.models.evaluation.EvaluationPendingQxm;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmEvaluationListFragmentFeature.DaggerMyQxmEvaluationListFragmentComponent;
import com.crux.qxm.di.myQxmEvaluationListFragmentFeature.MyQxmEvaluationListFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmEvaluationListFragment extends Fragment {

    // region Fragment-Properties
    @Inject
    Retrofit retrofit;

    private static final String TAG = "MyQxmEvaluationListFrag";
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private MyQxmEvaluationListAdapter myQxmEvaluationListAdapter;
    private List<EvaluationPendingQxm> evaluationPendingQxmList;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private boolean isFirstLoad = true;

    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvEvaluateQxm)
    RecyclerView rvQxmEvaluationList;

    // endregion

    // region Fragment-Constructor
    public MyQxmEvaluationListFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluation_list_of_qxm, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

        if (evaluationPendingQxmList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getMyPendingEvaluatedListNetworkCall();
        }

    }

    // endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {

        MyQxmEvaluationListFragmentComponent myQxmEvaluationListFragmentComponent =
                DaggerMyQxmEvaluationListFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmEvaluationListFragmentComponent.injectMyQxmEvaluationListFragmentFeature(
                MyQxmEvaluationListFragment.this
        );


    }
    // endregion

    // region Init
    private void init(View view) {

        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (evaluationPendingQxmList == null)
            evaluationPendingQxmList = new ArrayList<>();

        myQxmEvaluationListAdapter = new MyQxmEvaluationListAdapter(evaluationPendingQxmList, context, qxmFragmentTransactionHelper);
        rvQxmEvaluationList.setAdapter(myQxmEvaluationListAdapter);
        rvQxmEvaluationList.setLayoutManager(new LinearLayoutManager(context));

    }
    // endregion

    // region InitializeClickListeners
    private void initializeClickListeners() {

        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myQxmEvaluationListAdapter.clear();
            getMyPendingEvaluatedListNetworkCall();
        });
    }

    // endregion

    // region GetMyPendingEvaluatedListNetworkCall

    private void getMyPendingEvaluatedListNetworkCall() {
        Call<List<EvaluationPendingQxm>> getMyPendingEvaluatedList = apiService.getMyPendingEvaluatedList(token.getToken(), token.getUserId());

        getMyPendingEvaluatedList.enqueue(new Callback<List<EvaluationPendingQxm>>() {
            @Override
            public void onResponse(@NonNull Call<List<EvaluationPendingQxm>> call, @NonNull Response<List<EvaluationPendingQxm>> response) {
                Log.d(TAG, "onResponse: getMyPendingEvaluatedListNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: success");
                    if (response.body() != null) {
                        evaluationPendingQxmList.addAll(response.body());
                        myQxmEvaluationListAdapter.notifyDataSetChanged();
                    } else {
                        // Todo:: empty evaluation list msg show
                        Log.d(TAG, "onResponse: list is empty");

                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: failed getMyPendingEvaluatedListNetworkCall");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<EvaluationPendingQxm>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getMyPendingEvaluatedListNetworkCall");
                Log.d(TAG, "onFailure: error message: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: error stack trace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

        if(!isFirstLoad){

            swipeRefreshLayout.setRefreshing(true);
            myQxmEvaluationListAdapter.clear();
            getMyPendingEvaluatedListNetworkCall();
        }
    }

    // endregion

    // region Override-OnStop

    @Override
    public void onStop() {
        isFirstLoad = false;
        super.onStop();
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
