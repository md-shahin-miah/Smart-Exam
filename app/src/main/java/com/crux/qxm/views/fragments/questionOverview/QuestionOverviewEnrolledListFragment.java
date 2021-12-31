package com.crux.qxm.views.fragments.questionOverview;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questionOverview.QuestionOverviewEnrolledListAdapter;
import com.crux.qxm.db.models.evaluation.Participator;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.questionOverviewEnrolledListFragmentFeature.DaggerQuestionOverviewEnrolledListFragmentComponent;
import com.crux.qxm.di.questionOverviewEnrolledListFragmentFeature.QuestionOverviewEnrolledListFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionOverviewEnrolledListFragment extends Fragment {

    private static final String TAG = "QuestionOverviewPartici";

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    ArrayList<Participator> enrolledUserArrayList = new ArrayList<>();
    @BindView(R.id.rvEnrolled)
    RecyclerView rvEnrolled;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.searchProgressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;
    BottomNavigationViewEx bottomNavigationViewEx;

    QuestionOverviewEnrolledListAdapter questionOverviewEnrolledListAdapter;
    QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    private Context context;
    private String qxmId;
    private String userToken;

    // rxJava
    private CompositeDisposable compositeDisposable;
    private PublishSubject<String> publishSubject;


    public QuestionOverviewEnrolledListFragment() {
        // Required empty public constructor
    }
    //endregion

    //region newInstance
    public static QuestionOverviewEnrolledListFragment newInstance(String qxmId) {

        Bundle args = new Bundle();
        args.putString(QXM_ID_KEY, qxmId);
        QuestionOverviewEnrolledListFragment fragment = new QuestionOverviewEnrolledListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_overview_enrolled_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //getting qxmId from bundle
        qxmId = getArguments() != null ? getArguments().getString(QXM_ID_KEY) : null;
        context = view.getContext();

        setUpDagger2(getContext());
        init();
        initializeAdapter();
        clickListener();



        if (enrolledUserArrayList != null) {
            if (enrolledUserArrayList.size() == 0)
                getAllEnrolledListNetworkCall();
        }

        //region searchView onQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(TAG, "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d(TAG, "onQueryTextChange: " + newText);
                Log.d(TAG, "onQueryTextChange queryLength :" + newText.length());

                if (newText.length() >= 1) {

                    // showing search is in progress
                    tvNotFound.setVisibility(View.GONE);
                    swipeRL.setVisibility(View.GONE);
                    searchProgressBar.setVisibility(View.VISIBLE);
                    instantSearchParticipator(newText);

                } else {

                    compositeDisposable.clear();
                    tvNotFound.setVisibility(View.GONE);
                    searchProgressBar.setVisibility(View.GONE);
                    swipeRL.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        //endregion
    }

    //region init
    private void init() {
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        token = realmService.getApiToken();
        userToken = token.getToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        bottomNavigationViewEx = (Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view));

        //rxJava objects
        publishSubject = PublishSubject.create();
        compositeDisposable = new CompositeDisposable();

    }
    //endregion

    //region setup dagger
    private void setUpDagger2(Context context) {

        QuestionOverviewEnrolledListFragmentComponent questionOverviewEnrolledListFragmentComponent =
                DaggerQuestionOverviewEnrolledListFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        questionOverviewEnrolledListFragmentComponent.injectQuestionOverviewEnrolledListFragmentFeature(QuestionOverviewEnrolledListFragment.this);

    }
    //endregion

    //region initialize adapter

    private void initializeAdapter() {


        questionOverviewEnrolledListAdapter = new QuestionOverviewEnrolledListAdapter(getContext(), enrolledUserArrayList, picasso, participator -> {

            // goto user profile on click any single row of participator list
            qxmFragmentTransactionHelper.loadUserProfileFragment(participator.getId(), participator.getFullName());

        });
        rvEnrolled.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEnrolled.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        rvEnrolled.setItemAnimator(new DefaultItemAnimator());
        rvEnrolled.setNestedScrollingEnabled(false);
        rvEnrolled.setAdapter(questionOverviewEnrolledListAdapter);

        swipeRL.setOnRefreshListener(() -> {

            // if search view is open ans user typed something
            // then during swipe refresh search for the qurey
            // otherwise get updated participator list

            if(Objects.requireNonNull(searchView.getQuery()).toString().length()>=1){

                instantSearchParticipator(searchView.getQuery().toString());
            }else {

                enrolledUserArrayList.clear();
                getUpdatedEnrolledList();
            }


        });

    }

    //endregion

    //region getAllEnrolledListNetworkCall

    private void getAllEnrolledListNetworkCall() {

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(getContext());
        qxmProgressDialog.showProgressDialog("Loading Enrolled List", "Enrolled list is being loaded,please wait...", false);

        Call<ArrayList<Participator>> getAllParticipantList = apiService.getAcceptedEnrolledUserList(token.getToken(), qxmId);
        getAllParticipantList.enqueue(new Callback<ArrayList<Participator>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Participator>> call, @NonNull Response<ArrayList<Participator>> response) {

                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());


                if (response.code() == 200 || response.code() == 201) {

                    enrolledUserArrayList.clear();
                    enrolledUserArrayList.addAll(response.body());
                    questionOverviewEnrolledListAdapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(context, Objects.requireNonNull(getContext()).getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Participator>> call, @NonNull Throwable t) {

                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "onFailure: getFeedItem");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, Objects.requireNonNull(getContext()).getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();
            }
        });


    }

    //endregion

    //region getUpdatedEnrolledList
    private void getUpdatedEnrolledList() {

        Call<ArrayList<Participator>> getAllParticipantList = apiService.getAcceptedEnrolledUserList( token.getToken(), qxmId);
        getAllParticipantList.enqueue(new Callback<ArrayList<Participator>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Participator>> call, @NonNull Response<ArrayList<Participator>> response) {

                swipeRL.setRefreshing(false);
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());


                if (response.code() == 200 || response.code() == 201) {

                    enrolledUserArrayList.clear();
                    enrolledUserArrayList.addAll(response.body());
                    questionOverviewEnrolledListAdapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(context, Objects.requireNonNull(getContext()).getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Participator>> call, @NonNull Throwable t) {

                swipeRL.setRefreshing(false);
                Log.d(TAG, "onFailure: getFeedItem");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, Objects.requireNonNull(getContext()).getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //endregion

    //region clickListener
    private void clickListener() {

        ivBackArrow.setOnClickListener(v -> {

            // transaction to QuestionOverviewFragment
            // if user in searching mode,then take him/her in normal toolbar condition
            if (tvToolbarTitle.getVisibility() == View.GONE || ivSearch.getVisibility() == View.GONE) {

                tvToolbarTitle.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
            }
            // otherwise user is in normal position pop the fragment on back press
            else if (getActivity() != null) {
                Log.d(TAG, "onBackPressed calling from QuestionOverViewParticipantListFragment");
                getActivity().onBackPressed();
            }

        });


        ivSearch.setOnClickListener(v -> {

            tvToolbarTitle.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            showSoftKeyboard(searchView);

        });


    }
    //endregion

    //region getEnrolledUserSearchObserver (it will observe the api call for the participator)

    private DisposableObserver<List<Participator>> getEnrolledUserSearchObserver() {

        return new DisposableObserver<List<Participator>>() {

            @Override
            public void onNext(List<Participator> enrolledUserArrayListFromApi) {

                Log.d(TAG, "onNext: Called");
                Log.d(TAG, "onNext Participator :" + enrolledUserArrayListFromApi);

                searchProgressBar.setVisibility(View.GONE);
                swipeRL.setVisibility(View.VISIBLE);


                enrolledUserArrayList.clear();
                enrolledUserArrayList.addAll(enrolledUserArrayListFromApi);
                questionOverviewEnrolledListAdapter.notifyDataSetChanged();

                if(enrolledUserArrayList.isEmpty()){

                    swipeRL.setRefreshing(false);
                    swipeRL.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(getResources().getString(R.string.no_participator_found));
                }else {

                    tvNotFound.setVisibility(View.GONE);
                    swipeRL.setVisibility(View.VISIBLE);
                    swipeRL.setRefreshing(false);

                }

            }

            @Override
            public void onError(Throwable e) {

                Log.d(TAG, "onError: Called");
                Log.d(TAG, "onError: " + e.getMessage());

                searchProgressBar.setVisibility(View.GONE);
                swipeRL.setVisibility(View.GONE);
                swipeRL.setRefreshing(false);
                Toast.makeText(context, getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

                Log.d(TAG, "onComplete: Called");
            }
        };
    }

    //endregion

    //region network call with rxJava for participator search

    private void instantSearchParticipator(String query) {


        DisposableObserver<List<Participator>> participatorSearchObserver = getEnrolledUserSearchObserver();


        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)

                .distinctUntilChanged()
                .filter(s -> !s.trim().isEmpty())
                .switchMapSingle((Function<String, SingleSource<List<Participator>>>) s -> apiService.getParticipatorListInASpecificQxm(userToken, qxmId, s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())).subscribeWith(participatorSearchObserver));

        publishSubject.onNext(query);
    }

    //endregion

    //region onStart

    @Override
    public void onStart() {
        super.onStart();

        //hiding bottom navigation view when start this fragment
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    //endregion


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

    //region onDestroy
    @Override
    public void onDestroy() {

        super.onDestroy();
        compositeDisposable.clear();
    }
    //endregion

    //region onStop

    @Override
    public void onStop() {
        super.onStop();
    }

    //endregion

    //region showSoftKeyboard
    public void showSoftKeyboard(View view){

            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            view.requestFocusFromTouch();
            Log.d(TAG, "showSoftKeyboard: Open");
            InputMethodManager imm =(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }


    }
    //endregion

}
