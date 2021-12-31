package com.crux.qxm.views.fragments.search;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.search.SearchedGroupAdapter;
import com.crux.qxm.adapter.search.SearchedListAdapter;
import com.crux.qxm.adapter.search.SearchedPollAdapter;
import com.crux.qxm.adapter.search.SearchedQxmAdapter;
import com.crux.qxm.adapter.search.SearchedSingleMCQAdapter;
import com.crux.qxm.adapter.search.SearchedUserAdapter;
import com.crux.qxm.db.models.search.group.SearchedGroup;
import com.crux.qxm.db.models.search.group.SearchedGroupContainer;
import com.crux.qxm.db.models.search.list.SearchedList;
import com.crux.qxm.db.models.search.poll.SearchedPoll;
import com.crux.qxm.db.models.search.poll.SearchedPollContainer;
import com.crux.qxm.db.models.search.qxm.SearchedQxm;
import com.crux.qxm.db.models.search.qxm.SearchedQxmContainer;
import com.crux.qxm.db.models.search.singleMCQ.SearchedSingleMcq;
import com.crux.qxm.db.models.search.singleMCQ.SearchedSingleMcqContainer;
import com.crux.qxm.db.models.search.user.SearchedUser;
import com.crux.qxm.db.models.search.user.SearchedUserContainer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.searchFragmentFeature.DaggerSearchFragmentComponent;
import com.crux.qxm.di.searchFragmentFeature.SearchFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.KeyboardChecker;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmKeyboardHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.ToastHelper;
import com.crux.qxm.utils.UrlSegmentValidator;
import com.crux.qxm.utils.deepLinkingHelper.IdValidator;
import com.crux.qxm.utils.eventBus.Events;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.SEARCH_GROUP;
import static com.crux.qxm.utils.StaticValues.SEARCH_LIST;
import static com.crux.qxm.utils.StaticValues.SEARCH_POLL;
import static com.crux.qxm.utils.StaticValues.SEARCH_QXM;
import static com.crux.qxm.utils.StaticValues.SEARCH_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.SEARCH_USER;
import static com.crux.qxm.utils.StaticValues.URI_GROUP;
import static com.crux.qxm.utils.StaticValues.URI_POLL;
import static com.crux.qxm.utils.StaticValues.URI_QXM;
import static com.crux.qxm.utils.StaticValues.URI_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.URI_USER;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchableFragment extends Fragment implements SearchedUserAdapter.UserAdapterListener, SearchedQxmAdapter.QxmAdapterListener, SearchedGroupAdapter.GroupAdapterListener, SearchedSingleMCQAdapter.SingleMcqAdapterListener, SearchedPollAdapter.SinglePollAdapterListener, SearchedListAdapter.SearchedListAdapterListener {

    private static final String TAG = "SearchableFragment";
    private static boolean isUrlBasedSearchTriggered = false;

    @Inject
    Picasso picasso;

    @Inject
    Retrofit retrofit;
    @BindView(R.id.rvSearchedContent)
    RecyclerView rvSearchedContent;
    @BindView(R.id.cvSearchForContainer)
    CardView cvSearchForContainer;
    @BindView(R.id.cvRecentSearchContainer)
    CardView cvRecentSearchContainer;
    @BindView(R.id.flLoader)
    FrameLayout flLoader;
    @BindView(R.id.searchProgressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.llcSearchPeople)
    LinearLayoutCompat llcSearchUser;
    @BindView(R.id.llcSearchPost)
    LinearLayoutCompat llcSearchPost;
    @BindView(R.id.llcSearchGroup)
    LinearLayoutCompat llcSearchGroup;
    @BindView(R.id.llcSearchSingleMCQ)
    LinearLayoutCompat llcSearchSingleMCQ;
    @BindView(R.id.llcSearchPoll)
    LinearLayoutCompat llcSearchPoll;
    @BindView(R.id.llcSearchList)
    LinearLayoutCompat llcSearchList;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvNotFound)
    AppCompatTextView tvNotFound;
    private QxmApiService apiService;
    private QxmToken token;
    private String searchFor = SEARCH_USER;
    private Context context;
    private CompositeDisposable compositeDisposable;
    private PublishSubject<String> publishSubject;

    //adapters
    private SearchedUserAdapter searchedUserAdapter;
    private SearchedQxmAdapter searchedQxmAdapter;
    private SearchedGroupAdapter searchedGroupAdapter;
    private SearchedSingleMCQAdapter searchedSingleMCQAdapter;
    private SearchedPollAdapter searchedPollAdapter;
    private SearchedListAdapter searchedListAdapter;

    // array list
    private List<SearchedUser> searchedUserList;
    private List<SearchedQxm> searchedQxmList;
    private List<SearchedGroup> searchedGroupList;
    private List<SearchedSingleMcq> searchedSingleMcqList;
    private List<SearchedPoll> searchedPollList;
    private List<SearchedList> searchedListList;

    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmKeyboardHelper qxmKeyboardHelper;

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
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                if (URLUtil.isValidUrl(s) && !isUrlBasedSearchTriggered) {

                    //region urlBasedSearch
                    Uri uri = Uri.parse(s);
                    List<String> pathSegments = uri.getPathSegments();
                    Log.d(TAG, "onQueryTextChange url pathSegments : " + pathSegments);

                    if (pathSegments.size() >= 2) {

                        String contentType = pathSegments.get(0);
                        String contentID  = pathSegments.get(1);

                        boolean isValidSegmentCategory = UrlSegmentValidator.isValidSegmentCategory(contentType);
                        boolean isValidContentID = IdValidator.isValidMongoDBObjectId(contentID);

                        if (isValidSegmentCategory && isValidContentID) {

                            isUrlBasedSearchTriggered = true;
                            switch (contentType) {
                                case URI_USER:
                                    qxmFragmentTransactionHelper.loadUserProfileFragment(contentID, "Loading User Profile...");
                                    break;
                                case URI_QXM:
                                    Bundle args = new Bundle();
                                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, contentID);
                                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                                    break;
                                case URI_GROUP:
                                    qxmFragmentTransactionHelper.loadViewQxmGroupFragment(contentID, "Loading Group Info...");
                                    break;
                                case URI_SINGLE_MCQ:
                                    qxmFragmentTransactionHelper.loadSingleMCQOverviewFragment(contentID);
                                    break;
                                case URI_POLL:
                                    qxmFragmentTransactionHelper.loadPollOverviewFragment(contentID);
                                    break;

                            }
                        }else{
                            ToastHelper.displayToast(getApplicationContext(),getString(R.string.invalid_url));
                            searchProgressBar.setVisibility(View.GONE);
                        }

                    } else ToastHelper.displayToast(getApplicationContext(),getString(R.string.invalid_url));
                    //endregion
                }

                else {

                    if(isUrlBasedSearchTriggered) isUrlBasedSearchTriggered = false;

                    else {
                        switch (Objects.requireNonNull(searchView.getQueryHint()).toString()) {

                            case SEARCH_USER:
                                instantSearchUser(s);
                                break;
                            case SEARCH_QXM:
                                instantSearchQxm(s);
                                break;
                            case SEARCH_GROUP:
                                instantSearchGroup(s);
                                break;
                            case SEARCH_SINGLE_MCQ:
                                instantSearchSingleMcq(s);
                                break;
                            case SEARCH_POLL:
                                instantSearchPoll(s);
                                break;
                            case SEARCH_LIST:
                                instantSearchList(s);
                                break;

                        }

                    }

                }


            } else {

                compositeDisposable.clear();
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
            }
            return false;
        }
    };
    private StaticValues.SearchCategory searchCategory;

    public SearchableFragment() {
        // Required empty public constructor
    }

    public static SearchableFragment newInstance() {
        return new SearchableFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");

        // registering event bus
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searchable, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");

        setUpDagger2(getContext());
        init();
        getUserIdAndToken();
        setupSearchView();
        setCorrectQueryHintAndAdapter();

        //checking if keyboard is open or not
        KeyboardChecker.isKeyboardOpen(view);

        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getFragmentManager() != null) {
                Log.d(TAG, "popped Searchable Fragment");

                qxmKeyboardHelper.closeKeyboard();

                getFragmentManager().popBackStack();

            }
        });

    }
    //endregion

    // region setup dagger2
    private void setUpDagger2(Context context) {

        SearchFragmentComponent searchFragmentComponent =
                DaggerSearchFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        searchFragmentComponent.injectSearchFragmentFeature(SearchableFragment.this);

    }
    //endregion

    //region init
    public void init() {

        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        apiService = retrofit.create(QxmApiService.class);

        searchedUserList = new ArrayList<>();
        searchedQxmList = new ArrayList<>();
        searchedGroupList = new ArrayList<>();
        searchedSingleMcqList = new ArrayList<>();
        searchedPollList = new ArrayList<>();
        searchedListList = new ArrayList<>();

        searchedUserAdapter = new SearchedUserAdapter(getContext(), searchedUserList, picasso, this);
        searchedQxmAdapter = new SearchedQxmAdapter(getContext(), searchedQxmList, this);
        searchedGroupAdapter = new SearchedGroupAdapter(getContext(), searchedGroupList, picasso, this);
        searchedSingleMCQAdapter = new SearchedSingleMCQAdapter(getContext(), searchedSingleMcqList, this);
        searchedPollAdapter = new SearchedPollAdapter(getContext(), searchedPollList, this);
        searchedListAdapter = new SearchedListAdapter(getContext(), searchedListList, this);

        rvSearchedContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchedContent.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        rvSearchedContent.setItemAnimator(new DefaultItemAnimator());
        rvSearchedContent.setNestedScrollingEnabled(false);
        rvSearchedContent.setAdapter(searchedUserAdapter);

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        qxmKeyboardHelper = new QxmKeyboardHelper(getActivity());

        //rxJava objects
        compositeDisposable = new CompositeDisposable();
        publishSubject = PublishSubject.create();

        // passing empty string fetches all the user
        // publishSubject.onNext("");

    }
    //endregion

    //region get userId and token
    public void getUserIdAndToken() {
        userId = token.getUserId();
        userToken = token.getToken();
    }
    //endregion

    //region setupSearchView
    private void setupSearchView() {
        Log.d(TAG, "setupSearchView: searchFor: " + searchFor);
        setQueryHintAdapter(searchFor);
        searchView.setQueryHint(searchFor);
        searchView.setIconifiedByDefault(false);  // Do not iconify the widget; expand it by default
        searchView.setIconified(false);
        searchView.setQueryRefinementEnabled(true); // an arrow button will be added which allow user to refine the query from history
        searchView.setOnQueryTextListener(onQueryTextListener);


    }
    //endregion

    //region getUserSearchObserver(it will observe the api call for user search)
    private DisposableObserver<SearchedUserContainer> getUserSearchObserver() {

        return new DisposableObserver<SearchedUserContainer>() {
            @Override
            public void onNext(SearchedUserContainer searchedUserContainer) {

                Log.d(TAG, "onNext userSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                searchedUserList.clear();
                searchedUserList.addAll(searchedUserContainer.getMeFollowingUserList());
                searchedUserList.addAll(searchedUserContainer.getGeneralUserList());

                if (searchedUserList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(getResources().getString(R.string.no_user_found));
                } else tvNotFound.setVisibility(View.GONE);

                searchedUserAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError user search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete user search called");
            }
        };

    }
    //endregion

    //region getQxmSearchObserver(it will observe the api call for qxm search)
    private DisposableObserver<SearchedQxmContainer> getQxmSearchObserver() {

        return new DisposableObserver<SearchedQxmContainer>() {
            @Override
            public void onNext(SearchedQxmContainer searchedQxmContainer) {

                Log.d(TAG, "onNext qxmSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                searchedQxmList.clear();
                searchedQxmList.addAll(searchedQxmContainer.getFollowerQxmList());
                searchedQxmList.addAll(searchedQxmContainer.getOtherQxmList());

                if (searchedQxmList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(getResources().getString(R.string.no_qxm_found));
                } else tvNotFound.setVisibility(View.GONE);

                searchedQxmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError qxm search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete qxm search: called");
            }
        };

    }
    //endregion

    //region getSingleMcqSearchObserver(it will observe the api call for qxm search)
    private DisposableObserver<SearchedSingleMcqContainer> getSingleMcqSearchObserver() {

        return new DisposableObserver<SearchedSingleMcqContainer>() {
            @Override
            public void onNext(SearchedSingleMcqContainer searchedSingleMcqContainer) {

                Log.d(TAG, "onNext singleMcqSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                Log.d(TAG, "onNext singleMcqSearch: " + searchedSingleMcqContainer);
                searchedSingleMcqList.clear();
                searchedSingleMcqList.addAll(searchedSingleMcqContainer.getSearchedFollowerSingleMcq());
                searchedSingleMcqList.addAll(searchedSingleMcqContainer.getSearchedOthersSingleMcq());

                if (searchedSingleMcqList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(R.string.no_single_mcq_found_with_this_keyword);
                } else tvNotFound.setVisibility(View.GONE);

                searchedSingleMCQAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError singleMcq search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.no_internet_found, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete single mcq search: called");
            }
        };

    }
    //endregion

    //region getGroupSearchObserver(it will observe the api call for group search)
    private DisposableObserver<SearchedGroupContainer> getGroupSearchObserver() {

        return new DisposableObserver<SearchedGroupContainer>() {
            @Override
            public void onNext(SearchedGroupContainer searchedGroupContainer) {

                Log.d(TAG, "onNext groupSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                searchedGroupList.clear();
                searchedGroupList.addAll(searchedGroupContainer.getMyGroupList());
                searchedGroupList.addAll(searchedGroupContainer.getOtherGroupList());

                if (searchedGroupList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(getResources().getString(R.string.no_group_found));
                } else tvNotFound.setVisibility(View.GONE);

                searchedGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError group search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete group search: called");
            }
        };
    }
    //endregion

    //region getPollSearchObserver(it will observe the api call for poll search)
    private DisposableObserver<SearchedPollContainer> getPollSearchObserver() {

        return new DisposableObserver<SearchedPollContainer>() {
            @Override
            public void onNext(SearchedPollContainer searchedPollContainer) {

                Log.d(TAG, "onNext groupSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                Log.d(TAG, "onNext searchedPollContainer: " + searchedPollContainer);

                searchedPollList.clear();
                searchedPollList.addAll(searchedPollContainer.getFollowersPoll());
                searchedPollList.addAll(searchedPollContainer.getOthersPoll());

                if (searchedPollList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(R.string.no_poll_found_with_this_keyword);
                } else tvNotFound.setVisibility(View.GONE);

                searchedPollAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError poll search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.no_internet_found, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete poll search: called");
            }
        };
    }
    //endregion

    //region getListSearchObserver(it will observe the api call for list search)
    private DisposableObserver<List<SearchedList>> getListSearchObserver() {

        return new DisposableObserver<List<SearchedList>>() {
            @Override
            public void onNext(List<SearchedList> searchedListListFromNetwork) {

                Log.d(TAG, "onNext groupSearch: Called");
                searchProgressBar.setVisibility(View.GONE);
                rvSearchedContent.setVisibility(View.VISIBLE);
                cvSearchForContainer.setVisibility(View.GONE);
                cvRecentSearchContainer.setVisibility(View.GONE);

                Log.d(TAG, "onNext searched list: " + searchedListListFromNetwork);

                searchedListList.clear();
                searchedListList.addAll(searchedListListFromNetwork);


                if (searchedListList.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(R.string.no_list_found_with_this_keyword);
                } else tvNotFound.setVisibility(View.GONE);

                searchedListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

                searchProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError list search :" + e.getMessage());
                rvSearchedContent.setVisibility(View.GONE);
                cvSearchForContainer.setVisibility(View.VISIBLE);
                cvRecentSearchContainer.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.no_internet_found, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete list search: called");
            }
        };
    }
    //endregion

    // region network call with rxJava for USER search
    private void instantSearchUser(String query) {

        DisposableObserver<SearchedUserContainer> userSearchObserver = getUserSearchObserver();

        compositeDisposable.add(
                publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return !s.trim().isEmpty();

                            }
                        })
                        .switchMapSingle(new Function<String, SingleSource<SearchedUserContainer>>() {
                            @Override
                            public SingleSource<SearchedUserContainer> apply(String s) throws Exception {
                                return apiService.getSearchedUser(userToken, userId, s)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        }).subscribeWith(userSearchObserver));

        publishSubject.onNext(query);
    }
    //endregion

    // region network call with rxJava for QXM search
    private void instantSearchQxm(String query) {

        DisposableObserver<SearchedQxmContainer> qxmSearchObserver = getQxmSearchObserver();

        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.trim().isEmpty();

                    }
                })
                .switchMapSingle(new Function<String, SingleSource<SearchedQxmContainer>>() {
                    @Override
                    public SingleSource<SearchedQxmContainer> apply(String s) throws Exception {
                        return apiService.getSearchedQxm(userToken, userId, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                    }
                }).subscribeWith(qxmSearchObserver));

        publishSubject.onNext(query);

    }
    //endregion

    //region network call  with rx java for GROUP search
    private void instantSearchGroup(String query) {

        DisposableObserver<SearchedGroupContainer> groupSearchObserver = getGroupSearchObserver();

        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.trim().isEmpty();
                    }
                })
                .switchMapSingle(new Function<String, SingleSource<SearchedGroupContainer>>() {
                    @Override
                    public SingleSource<SearchedGroupContainer> apply(String s) throws Exception {
                        return apiService.getSearchedGroup(userToken, userId, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribeWith(groupSearchObserver));

        publishSubject.onNext(query);
    }
    //endregion

    // region network call with rxJava for Single Mcq search
    private void instantSearchSingleMcq(String query) {

        DisposableObserver<SearchedSingleMcqContainer> singleMcqSearchObserver = getSingleMcqSearchObserver();

        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.trim().isEmpty();

                    }
                })
                .switchMapSingle(new Function<String, SingleSource<SearchedSingleMcqContainer>>() {
                    @Override
                    public SingleSource<SearchedSingleMcqContainer> apply(String s) throws Exception {
                        return apiService.getSearchedSingleMCQ(userToken, userId, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                    }
                }).subscribeWith(singleMcqSearchObserver));

        publishSubject.onNext(query);

    }
    //endregion

    // region network call with rxJava for poll search
    private void instantSearchPoll(String query) {

        DisposableObserver<SearchedPollContainer> pollSearchObserver = getPollSearchObserver();

        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.trim().isEmpty();

                    }
                })
                .switchMapSingle(new Function<String, SingleSource<SearchedPollContainer>>() {
                    @Override
                    public SingleSource<SearchedPollContainer> apply(String s) throws Exception {
                        return apiService.getSearchedPoll(userToken, userId, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                    }
                }).subscribeWith(pollSearchObserver));

        publishSubject.onNext(query);

    }
    //endregion

    // region network call with rxJava for list search
    private void instantSearchList(String query) {

        DisposableObserver<List<SearchedList>> listSearchObserver = getListSearchObserver();

        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.trim().isEmpty();

                    }
                })
                .switchMapSingle(new Function<String, SingleSource<List<SearchedList>>>() {
                    @Override
                    public SingleSource<List<SearchedList>> apply(String s) throws Exception {
                        return apiService.getSearchedList(userToken, userId, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                    }
                }).subscribeWith(listSearchObserver));

        publishSubject.onNext(query);

    }

    //region search view query hint change and set correct adapter
    private void setCorrectQueryHintAndAdapter() {

        llcSearchUser.setOnClickListener(v -> setQueryHintAdapter(SEARCH_USER));

        llcSearchPost.setOnClickListener(v -> setQueryHintAdapter(SEARCH_QXM));

        llcSearchGroup.setOnClickListener(v -> setQueryHintAdapter(SEARCH_GROUP));

        llcSearchSingleMCQ.setOnClickListener(v -> setQueryHintAdapter(SEARCH_SINGLE_MCQ));

        llcSearchPoll.setOnClickListener(v -> setQueryHintAdapter(SEARCH_POLL));

        llcSearchList.setOnClickListener(v -> setQueryHintAdapter(SEARCH_LIST));
    }
    //endregion

    private void setQueryHintAdapter(String setQueryHintAdapterFor) {
        switch (setQueryHintAdapterFor) {
            case SEARCH_USER:
                searchFor = SEARCH_USER;
                searchView.setQueryHint(SEARCH_USER);
                rvSearchedContent.setAdapter(searchedUserAdapter);
                searchCategory = StaticValues.SearchCategory.USER;
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.white));

                break;
            case SEARCH_QXM:
                searchFor = SEARCH_QXM;
                searchView.setQueryHint(SEARCH_QXM);
                rvSearchedContent.setAdapter(searchedQxmAdapter);
                searchCategory = StaticValues.SearchCategory.QXM;
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchList.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case SEARCH_GROUP:
                searchFor = SEARCH_GROUP;
                searchView.setQueryHint(SEARCH_GROUP);
                rvSearchedContent.setAdapter(searchedGroupAdapter);
                searchCategory = StaticValues.SearchCategory.GROUP;
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchList.setBackgroundColor(getResources().getColor(R.color.white));

                break;
            case SEARCH_SINGLE_MCQ:
                searchFor = SEARCH_SINGLE_MCQ;
                searchView.setQueryHint(SEARCH_SINGLE_MCQ);
                rvSearchedContent.setAdapter(searchedSingleMCQAdapter);
                searchCategory = StaticValues.SearchCategory.SingleMCQ;
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchList.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case SEARCH_POLL:
                searchFor = SEARCH_POLL;
                searchView.setQueryHint(SEARCH_POLL);
                rvSearchedContent.setAdapter(searchedPollAdapter);
                searchCategory = StaticValues.SearchCategory.Poll;
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchList.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case SEARCH_LIST:
                searchFor = SEARCH_LIST;
                searchView.setQueryHint(SEARCH_LIST);
                rvSearchedContent.setAdapter(searchedListAdapter);
                searchCategory = StaticValues.SearchCategory.List;
                llcSearchList.setBackgroundColor(getResources().getColor(R.color.layout_background_dark));
                llcSearchPoll.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchUser.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchPost.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchGroup.setBackgroundColor(getResources().getColor(R.color.white));
                llcSearchSingleMCQ.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    //region every adapter main view click listener (implemented using interface)
    @Override
    public void onUserSelected(SearchedUser searchedUser) {
        qxmKeyboardHelper.closeKeyboard();
        qxmFragmentTransactionHelper.loadUserProfileFragment(searchedUser.getId(), searchedUser.getFullName());
    }

    @Override
    public void onQxmSelected(SearchedQxm searchedQxm) {
        qxmKeyboardHelper.closeKeyboard();
        String qxmId = searchedQxm.getQxmId();
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
    }

    @Override
    public void onGroupSelected(SearchedGroup searchedGroup) {
        qxmKeyboardHelper.closeKeyboard();
        qxmFragmentTransactionHelper.loadViewQxmGroupFragment(searchedGroup.getGroupId(), searchedGroup.getSearchedGroupInfo().getGroupName());
    }

    @Override
    public void onSingleMCQSelected(SearchedSingleMcq searchedSingleMcq) {

        qxmFragmentTransactionHelper.loadSingleMCQOverviewFragment(searchedSingleMcq.getFeedQxmId());

    }

    @Override
    public void onPollSelected(SearchedPoll searchedPoll) {

        qxmFragmentTransactionHelper.loadPollOverviewFragment(searchedPoll.getId());
    }

    //endregion

    @Override
    public void onListSelected(SearchedList searchedList) {

        qxmFragmentTransactionHelper.loadSingleListQxmFragment(searchedList.getId(), searchedList.getListName());

    }
    //endregion

    // region receiving keyboard visibility event message
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onKeyboardShowingEvent(Events.KeyboardVisibilityEvent keyboardVisibilityEvent) {

//        Log.d(TAG, "onKeyboardShowingEvent: " + keyboardVisibilityEvent.isKeyBoardOpen());

        BottomNavigationViewEx bottomNavigationViewEx = (Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation_view));
        //hiding add question button when keyboard is open
        if (keyboardVisibilityEvent.isKeyBoardOpen()) {

            bottomNavigationViewEx.setVisibility(View.GONE);

        } else {

            bottomNavigationViewEx.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    //region onStop
    @Override
    public void onStop() {
        super.onStop();

        // unregistering event bus
        EventBus.getDefault().unregister(this);
    }
    //endregion

    //region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // don't need observe anymore if activity is destroyed
        Log.d(TAG, "onDestroy: Is compositeDisposable cleared : " + compositeDisposable.isDisposed());
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }


    //endregion
}
