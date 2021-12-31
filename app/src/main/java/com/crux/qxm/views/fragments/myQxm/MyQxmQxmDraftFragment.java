package com.crux.qxm.views.fragments.myQxm;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.qxmDraft.QxmDraftAdapter;
import com.crux.qxm.db.models.draft.DraftedQxm;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmQBankFragmentFeature.DaggerMyQxmQBankFragmentComponent;
import com.crux.qxm.di.myQxmQBankFragmentFeature.MyQxmQBankFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.views.activities.CreateQxmActivity;
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

import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_MODEL_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmQxmDraftFragment extends Fragment implements QxmDraftAdapter.QxmDraftListener {

    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "MyQxmQxmDraftFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;

    private QxmToken token;

    private QxmDraftAdapter qxmDraftAdapter;
    private List<Object> qxmDraftList;
    private boolean onStopCalled;
    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.tvFeedEmptyMessage)
    AppCompatTextView tvFeedEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.RVQxmDraft)
    RecyclerView RVQxmDraft;

    // endregion

    // region Fragment-Constructor
    public MyQxmQxmDraftFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qxm_qxm_draft, container, false);

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

        if (qxmDraftList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getQxmDraftsFromLocalDB();
            getQxmDraftsNetworkCall();
        }
        initializeClickListeners();
    }

    // endregion

    //region Override-OnStop
    @Override
    public void onStop() {
        super.onStop();
        onStopCalled = true;
        Log.d(TAG, "onStop: called");
    }
    //endregion

    //region Override-OnStart
    @Override
    public void onStart() {
        super.onStart();
        if (onStopCalled) {
            onStopCalled = false;
            qxmDraftAdapter.clear();
            getQxmDraftsFromLocalDB();
            getQxmDraftsNetworkCall();
        }
    }
    //endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        MyQxmQBankFragmentComponent myQxmQBankFragmentComponent =
                DaggerMyQxmQBankFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmQBankFragmentComponent.injectMyQxmQBankFragmentFeature(MyQxmQxmDraftFragment.this);
    }

    // endregion

    // region Init

    private void init(View view) {
        context = view.getContext();

        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);

        if (qxmDraftList == null) qxmDraftList = new ArrayList<>();

        qxmDraftAdapter = new QxmDraftAdapter(context, qxmDraftList, realmService, picasso, getActivity(), apiService, token, this);
        RVQxmDraft.setAdapter(qxmDraftAdapter);
        RVQxmDraft.setLayoutManager(new LinearLayoutManager(context));

        onStopCalled = false;
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            qxmDraftAdapter.clear();
            getQxmDraftsFromLocalDB();
            getQxmDraftsNetworkCall();
        });
    }

    // endregion

    // region getQxmDraftsNetworkCall

    private void getQxmDraftsNetworkCall() {

        Log.d(TAG, "getQxmDraftsNetworkCall: token: " + token.toString());
        Call<List<DraftedQxm>> getMyQxmDrafts = apiService.getDraftedQxm(token.getToken(), token.getUserId());

        getMyQxmDrafts.enqueue(new Callback<List<DraftedQxm>>() {
            @Override
            public void onResponse(@NonNull Call<List<DraftedQxm>> call, @NonNull Response<List<DraftedQxm>> response) {

                Log.d(TAG, "onResponse: response code: " + response.code());
                Log.d(TAG, "onResponse: response body: " + response.body());

                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: getQxmDraftsNetworkCall success");
                    if (response.body() != null) {
                        qxmDraftList.addAll(response.body());
                        if (qxmDraftList.size() == 0) {
                            onQxmDraftListEmptyListener();
                        }
                        qxmDraftAdapter.notifyDataSetChanged();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_could_not_load_drafted_qxm_message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DraftedQxm>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onFailure: getQxmDraftsNetworkCall\n" +
                        "StackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, R.string.network_call_failure_could_not_load_drafted_qxm_message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // endregion

    //region getQxmDraftsFromLocalDB
    private void getQxmDraftsFromLocalDB() {

        List<QxmDraft> qxmDraftList = realmService.getQxmDrafts();
        Log.d(TAG, "getQxmDraftsFromLocalDB: qxmDraftList size = " + qxmDraftList.size());
        Log.d(TAG, "getQxmDraftsFromLocalDB: QxmDraftList: " + qxmDraftList.toString());
        if (qxmDraftList.size() != 0) {
            RVQxmDraft.setVisibility(View.VISIBLE);
            tvFeedEmptyMessage.setVisibility(View.GONE);
            qxmDraftAdapter.addAll(qxmDraftList);
            qxmDraftAdapter.notifyDataSetChanged();

        }
        //if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }
    //endregion

    //region Override-onItemSelected
    @Override
    public void onItemSelected(QxmDraft qxmDraft) {
        Intent intent = new Intent(getActivity(), CreateQxmActivity.class);
        intent.putExtra(QXM_DRAFT_KEY, qxmDraft);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }

    @Override
    public void onItemSelected(QxmModel qxmModel) {
        Intent intent = new Intent(getActivity(), CreateQxmActivity.class);
        intent.putExtra(QXM_MODEL_KEY, qxmModel);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }
    //endregion

    //region Override-onQxmDraftListEmptyListener
    @Override
    public void onQxmDraftListEmptyListener() {
        RVQxmDraft.setVisibility(View.GONE);
        tvFeedEmptyMessage.setVisibility(View.VISIBLE);
    }
    //endregion

}
