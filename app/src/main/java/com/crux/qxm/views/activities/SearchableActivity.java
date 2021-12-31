//package com.crux.qxm.views.activities;
//
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.SearchRecentSuggestions;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.appcompat.widget.LinearLayoutCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.SearchView;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.crux.qxm.App;
//import com.crux.qxm.R;
//import com.crux.qxm.adapter.search.SearchedGroupAdapter;
//import com.crux.qxm.adapter.search.SearchedQxmAdapter;
//import com.crux.qxm.adapter.search.SearchedUserAdapter;
//import com.crux.qxm.db.models.search.group.SearchedGroup;
//import com.crux.qxm.db.models.search.group.SearchedGroupContainer;
//import com.crux.qxm.db.models.search.qxm.SearchedQxm;
//import com.crux.qxm.db.models.search.qxm.SearchedQxmContainer;
//import com.crux.qxm.db.models.search.user.SearchedUser;
//import com.crux.qxm.db.models.search.user.SearchedUserContainer;
//import com.crux.qxm.db.realmModels.apiToken.QxmToken;
//import com.crux.qxm.db.realmModels.user.UserBasic;
//import com.crux.qxm.db.realmService.RealmService;
//import com.crux.qxm.di.searchedActivityFeature.DaggerSearchableActivityComponent;
//import com.crux.qxm.di.searchedActivityFeature.SearchableActivityComponent;
//import com.crux.qxm.networkLayer.QxmApiService;
//import com.crux.qxm.utils.QxmFragmentTransactionHelper;
//import com.crux.qxm.utils.contentProvider.SearchSuggestionProvider;
//import com.crux.qxm.views.fragments.userProfileActivityFragments.UserProfileFragment;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import io.reactivex.SingleSource;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.functions.Function;
//import io.reactivex.functions.Predicate;
//import io.reactivex.observers.DisposableObserver;
//import io.reactivex.schedulers.Schedulers;
//import io.reactivex.subjects.PublishSubject;
//import io.realm.Realm;
//import retrofit2.Retrofit;
//
//import static com.crux.qxm.utils.StaticValues.SEARCH_GROUP;
//import static com.crux.qxm.utils.StaticValues.SEARCH_QXM;
//import static com.crux.qxm.utils.StaticValues.SEARCH_USER;
//
//public class SearchableActivity extends AppCompatActivity implements SearchedUserAdapter.UserAdapterListener {
//
//    private static final String TAG = "SearchableActivity";
//
//
//    @Inject
//    Picasso picasso;
//
//    @Inject
//    Retrofit retrofit;
//
//    QxmApiService apiService;
//    Realm realm;
//    RealmService realmService;
//    UserBasic user;
//    QxmToken token;
//
//    @BindView(R.id.rvSearchedContent)
//    RecyclerView rvSearchedContent;
//    @BindView(R.id.cvSearchForContainer)
//    CardView cvSearchForContainer;
//    @BindView(R.id.cvRecentSearchContainer)
//    CardView cvRecentSearchContainer;
//    @BindView(R.id.flLoader)
//    FrameLayout flLoader;
//    @BindView(R.id.searchProgressBar)
//    ProgressBar searchProgressBar;
//
//    @BindView(R.id.llcSearchPeople)
//    LinearLayoutCompat llcSearchUser;
//    @BindView(R.id.llcSearchPost)
//    LinearLayoutCompat llcSearchPost;
//    @BindView(R.id.llcSearchGroup)
//    LinearLayoutCompat llcSearchGroup;
//
//    SearchView searchView;
//
//    private Context context;
//    private CompositeDisposable compositeDisposable;
//    private PublishSubject<String> publishSubject;
//    private SearchedUserAdapter searchedUserAdapter;
//    private SearchedQxmAdapter searchedQxmAdapter;
//    private SearchedGroupAdapter searchedGroupAdapter;
//    private List<SearchedUser> searchedUserList;
//    private List<SearchedQxm> searchedQxmList;
//    private List<SearchedGroup> searchedGroupList;
//    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
//
//    private String userId;
//    private String userToken;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_searchable);
//        ButterKnife.bind(this);
//
//        setUpDagger2(context);
//        init();
//        getUserIdAndToken();
//        setCorrectQueryHintAndAdapter();
//
//    }
//
//    // region searchView onQueryTextListener
//    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String s) {
//
//            Log.d(TAG, "onQueryTextSubmit: " + s);
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String s) {
//
//            Log.d(TAG, "onQueryTextChange: " + s);
//            if (s.length() >= 1) {
//
//                searchProgressBar.setVisibility(View.VISIBLE);
//                rvSearchedContent.setVisibility(View.GONE);
//                cvSearchForContainer.setVisibility(View.GONE);
//                cvRecentSearchContainer.setVisibility(View.GONE);
//
//                switch (Objects.requireNonNull(searchView.getQueryHint()).toString()) {
//
//                    case SEARCH_USER:
//                        instantSearchUser(s);
//                        break;
//                    case SEARCH_QXM:
//                        instantSearchQxm(s);
//                        break;
//                    case SEARCH_GROUP:
//                        instantSearchGroup(s);
//                        break;
//                }
//
//            } else {
//
//                searchProgressBar.setVisibility(View.GONE);
//                rvSearchedContent.setVisibility(View.GONE);
//                cvSearchForContainer.setVisibility(View.VISIBLE);
//                cvRecentSearchContainer.setVisibility(View.VISIBLE);
//            }
//            return false;
//        }
//    };
//    //endregion
//
//    //region init
//    public void init() {
//
//        realm = Realm.getDefaultInstance();
//        realmService = new RealmService(realm);
//        user = realmService.getSavedUser();
//        token = realmService.getApiToken();
//        apiService = retrofit.create(QxmApiService.class);
//
//        searchedUserList = new ArrayList<>();
//        searchedQxmList = new ArrayList<>();
//        searchedGroupList = new ArrayList<>();
//
//        searchedUserAdapter = new SearchedUserAdapter(this, searchedUserList, picasso, this);
//        searchedQxmAdapter = new SearchedQxmAdapter(this, searchedQxmList);
//        searchedGroupAdapter = new SearchedGroupAdapter(this,searchedGroupList,picasso);
//
//        rvSearchedContent.setLayoutManager(new LinearLayoutManager(this));
//        rvSearchedContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        rvSearchedContent.setItemAnimator(new DefaultItemAnimator());
//        rvSearchedContent.setNestedScrollingEnabled(false);
//        rvSearchedContent.setAdapter(searchedUserAdapter);
//
//        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(this);
//
//
//        //rxJava objects
//        compositeDisposable = new CompositeDisposable();
//        publishSubject = PublishSubject.create();
//
//        // passing empty string fetches all the user
//        // publishSubject.onNext("");
//
//    }
//    //endregion
//
//    //region get userId and token
//    public void getUserIdAndToken() {
//        userId = token.getUserId();
//        userToken = token.getToken();
//    }
//    //endregion
//
//    // region setup dagger2
//    private void setUpDagger2(Context context) {
//
//        SearchableActivityComponent searchableActivityComponent =
//                DaggerSearchableActivityComponent
//                        .builder()
//                        .appComponent(App.get(this).getAppComponent())
//                        .build();
//
//        searchableActivityComponent.injectSearchableActivityFeature(SearchableActivity.this);
//
//    }
//    //endregion
//
//    //region onNewIntent
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        handleIntent(intent);
//    }
//    //endregion
//
//    //region onCreateOptionsMenu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//
//        searchView.setQueryHint(SEARCH_USER);
//
//        searchView.setIconifiedByDefault(false);  // Do not iconify the widget; expand it by default
//
//        searchView.setIconified(false);
//
//        searchView.setOnQueryTextListener(onQueryTextListener);
//
//        searchView.setQueryRefinementEnabled(true); // an arrow button will be added which allow user to refine the query from history
//
//        if (searchManager != null) {
//            searchView.setSearchableInfo(
//                    searchManager.getSearchableInfo(getComponentName()));
//
//        } else Toast.makeText(this, "SearchManager is null !", Toast.LENGTH_SHORT).show();
//
//        return true;
//
//    }
//    //endregion
//
//    // region onOptionsItemSelected
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            //deleting previous search history
//            case R.id.delete:
//                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                        SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
//                suggestions.clearHistory();
//                Toast.makeText(this, "Deleted all search history successfully", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    //endregion
//
//    //region handleIntent
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            Log.d(TAG, "handleIntent: " + query);
//            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
//
//            //Saving the recent search query in content provider
//            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
//            suggestions.saveRecentQuery(query, null);
//        }
//    }
//    //endregion
//
//    //region getUserSearchObserver(it will observe the api call for user search)
//    private DisposableObserver<SearchedUserContainer> getUserSearchObserver() {
//
//        return new DisposableObserver<SearchedUserContainer>() {
//            @Override
//            public void onNext(SearchedUserContainer searchedUserContainer) {
//
//                Log.d(TAG, "onNext userSearch: Called");
//                searchProgressBar.setVisibility(View.GONE);
//                rvSearchedContent.setVisibility(View.VISIBLE);
//                cvSearchForContainer.setVisibility(View.GONE);
//                cvRecentSearchContainer.setVisibility(View.GONE);
//
//                searchedUserList.clear();
//                searchedUserList.addAll(searchedUserContainer.getMeFollowingUserList());
//                searchedUserList.addAll(searchedUserContainer.getGeneralUserList());
//                searchedUserAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//                searchProgressBar.setVisibility(View.GONE);
//                Log.d(TAG, "onError user search :" + e.getMessage());
//                rvSearchedContent.setVisibility(View.GONE);
//                cvSearchForContainer.setVisibility(View.VISIBLE);
//                cvRecentSearchContainer.setVisibility(View.VISIBLE);
//                Toast.makeText(getApplicationContext(), "Something went wrong during user search :(", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete user search called");
//            }
//        };
//
//    }
//    //endregion
//
//    //region getQxmSearchObserver(it will observe the api call for qxm search
//    private DisposableObserver<SearchedQxmContainer> getQxmSearchObserver() {
//
//        return new DisposableObserver<SearchedQxmContainer>() {
//            @Override
//            public void onNext(SearchedQxmContainer searchedQxmContainer) {
//
//                Log.d(TAG, "onNext qxmSearch: Called");
//                searchProgressBar.setVisibility(View.GONE);
//                rvSearchedContent.setVisibility(View.VISIBLE);
//                cvSearchForContainer.setVisibility(View.GONE);
//                cvRecentSearchContainer.setVisibility(View.GONE);
//
//                searchedQxmList.clear();
//                searchedQxmList.addAll(searchedQxmContainer.getFollowerQxmList());
//                searchedQxmList.addAll(searchedQxmContainer.getOtherQxmList());
//                searchedQxmAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//                searchProgressBar.setVisibility(View.GONE);
//                Log.d(TAG, "onError qxm search :" + e.getMessage());
//                rvSearchedContent.setVisibility(View.GONE);
//                cvSearchForContainer.setVisibility(View.VISIBLE);
//                cvRecentSearchContainer.setVisibility(View.VISIBLE);
//                Toast.makeText(getApplicationContext(), "Something went wrong during qxm search :(", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete qxm search: called");
//            }
//        };
//
//    }
//    //endregion
//
//    //region getGroupSearchObserver(it will observe the api call for group search
//    private DisposableObserver<SearchedGroupContainer> getGroupSearchObserver(){
//
//        return new DisposableObserver<SearchedGroupContainer>() {
//            @Override
//            public void onNext(SearchedGroupContainer searchedGroupContainer) {
//
//                Log.d(TAG, "onNext groupSearch: Called");
//                searchProgressBar.setVisibility(View.GONE);
//                rvSearchedContent.setVisibility(View.VISIBLE);
//                cvSearchForContainer.setVisibility(View.GONE);
//                cvRecentSearchContainer.setVisibility(View.GONE);
//
//                searchedGroupList.clear();
//                searchedGroupList.addAll(searchedGroupContainer.getMyGroupList());
//                searchedGroupList.addAll(searchedGroupContainer.getOtherGroupList());
//                searchedGroupAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//                searchProgressBar.setVisibility(View.GONE);
//                Log.d(TAG, "onError group search :" + e.getMessage());
//                rvSearchedContent.setVisibility(View.GONE);
//                cvSearchForContainer.setVisibility(View.VISIBLE);
//                cvRecentSearchContainer.setVisibility(View.VISIBLE);
//                Toast.makeText(getApplicationContext(), "Something went wrong during group search :(", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete group search: called");
//            }
//        };
//    }
//    //endregion
//
//    // region network call with rxJava for USER search
//    private void instantSearchUser(String query) {
//
//        DisposableObserver<SearchedUserContainer> userSearchObserver = getUserSearchObserver();
//
//        compositeDisposable.add(
//                publishSubject.debounce(300, TimeUnit.MILLISECONDS)
//                        .distinctUntilChanged()
//                        .filter(new Predicate<String>() {
//                            @Override
//                            public boolean test(String s) throws Exception {
//                                return !s.trim().isEmpty();
//
//                            }
//                        })
//                        .switchMapSingle(new Function<String, SingleSource<SearchedUserContainer>>() {
//                            @Override
//                            public SingleSource<SearchedUserContainer> apply(String s) throws Exception {
//                                return apiService.getSearchedUser(userToken, userId, s)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread());
//                            }
//                        }).subscribeWith(userSearchObserver));
//
//        publishSubject.onNext(query);
//    }
//    //endregion
//
//    // region network call with rxJava for QXM search
//    private void instantSearchQxm(String query) {
//
//        DisposableObserver<SearchedQxmContainer> qxmSearchObserver = getQxmSearchObserver();
//
//        compositeDisposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
//                .distinctUntilChanged()
//                .filter(new Predicate<String>() {
//                    @Override
//                    public boolean test(String s) throws Exception {
//                        return !s.trim().isEmpty();
//
//                    }
//                })
//                .switchMapSingle(new Function<String, SingleSource<SearchedQxmContainer>>() {
//                    @Override
//                    public SingleSource<SearchedQxmContainer> apply(String s) throws Exception {
//                        return apiService.getSearchedQxm(userToken, userId, s)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread());
//
//                    }
//                }).subscribeWith(qxmSearchObserver));
//
//        publishSubject.onNext(query);
//
//    }
//    //endregion
//
//    //region network call  with rx java for GROUP search
//    private void instantSearchGroup(String query){
//
//        DisposableObserver<SearchedGroupContainer> groupSearchObserver = getGroupSearchObserver();
//
//        compositeDisposable.add(publishSubject.debounce(300,TimeUnit.MILLISECONDS)
//        .distinctUntilChanged()
//        .filter(new Predicate<String>() {
//            @Override
//            public boolean test(String s) throws Exception {
//                return !s.trim().isEmpty();
//            }
//        })
//        .switchMapSingle(new Function<String, SingleSource<SearchedGroupContainer>>() {
//            @Override
//            public SingleSource<SearchedGroupContainer> apply(String s) throws Exception {
//                return apiService.getSearchedGroup(userToken,userId,s)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        }).subscribeWith(groupSearchObserver));
//
//        publishSubject.onNext(query);
//    }
//    //endregion
//
//    //region onDestroy
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        compositeDisposable.clear(); // don't need observe anymore if activity is destroyed
//        Log.d(TAG, "onDestroy: Is compositeDisposable cleared : " + compositeDisposable.isDisposed());
//    }
//    //endregion
//
//    //region search view query hint change and set correct adapter
//    private void setCorrectQueryHintAndAdapter() {
//
//        llcSearchUser.setOnClickListener(v -> {
//
//            searchView.setQueryHint(SEARCH_USER);
//            rvSearchedContent.setAdapter(searchedUserAdapter);
//        });
//
//        llcSearchPost.setOnClickListener(v -> {
//
//            searchView.setQueryHint(SEARCH_QXM);
//            rvSearchedContent.setAdapter(searchedQxmAdapter);
//        });
//
//        llcSearchGroup.setOnClickListener(v -> {
//
//            searchView.setQueryHint(SEARCH_GROUP);
//            rvSearchedContent.setAdapter(searchedGroupAdapter);
//        });
//    }
//    //endregion
//
//    //region every adapter main view click listener (implemented using interface)
//    @Override
//    public void onFollowingSelected(SearchedUser searchedUser) {
//        //qxmFragmentTransactionHelper.loadUserProfileFragment(searchedUser.getId());
//
//        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(searchedUser.getId());
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_container,userProfileFragment,userProfileFragment.getClass().getSimpleName());
//        fragmentTransaction.commit();
//    }
//
//    //endregion
//
//}
