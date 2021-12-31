package com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment;


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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.MyQxmSingleQxmEnrolledListAdapter;
import com.crux.qxm.db.models.evaluation.Participator;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.myQxmSingleQxmEnrolledListFeature.DaggerMyQxmSingleQxmEnrolledFragmentComponent;
import com.crux.qxm.di.myQxmSingleQxmEnrolledListFeature.MyQxmSingleQxmEnrolledFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.ENROLLED_LIST_FRAGMENT_BUNDLE_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQxmSingleQxmEnrolledListFragment extends Fragment {

    private static final String TAG = "MyQxmSingleQxmEnrolledF";

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;
    private Realm realm;
    private RealmService realmService;
    private QxmToken token;
    ArrayList<Participator> enrolledUserList;
    private Context context;
    @BindView(R.id.rvEnrolled)
    RecyclerView rvEnrolled;

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    ArrayList<String> bundleDataList;
    MyQxmSingleQxmEnrolledListAdapter myQxmSingleQxmEnrolledListAdapter;
    QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public MyQxmSingleQxmEnrolledListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        assert getArguments() != null;
        bundleDataList = getArguments().getStringArrayList(ENROLLED_LIST_FRAGMENT_BUNDLE_DATA);

        if(bundleDataList != null){

            Log.d(TAG, "onCreate: "+bundleDataList);
        }else {

            Toast.makeText(context, "Can not load data,try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_qxm_single_qxm_enrolled, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDagger2(context);
        init();

        if (enrolledUserList != null)
            loadPreviousData();
        else
            getEnrolledListAndLoadView();


        // pop this fragment when press back arrow
        ivBackArrow.setOnClickListener(v->Objects.requireNonNull(getActivity()).onBackPressed());
    }

    private void init() {
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
    }

    //region setup dagger
    private void setUpDagger2(Context context) {
        MyQxmSingleQxmEnrolledFragmentComponent myQxmSingleQxmEnrolledFragmentComponent =
                DaggerMyQxmSingleQxmEnrolledFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        myQxmSingleQxmEnrolledFragmentComponent.injectMyQxmSingleQxmEnrolledFragmentFeature(MyQxmSingleQxmEnrolledListFragment.this);

    }
    //endregion

    //region  load pending evaluation list network call
    public void getEnrolledListAndLoadView()  {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Loading enrolled list", String.format("%s is loading. Please wait... ", bundleDataList.get(1)), false);

        Call<ArrayList<Participator>> getEnrolledList = apiService.getAcceptedEnrolledUserList(token.getToken(),bundleDataList.get(0));

        getEnrolledList.enqueue(new Callback<ArrayList<Participator>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Participator>> call, @NonNull Response<ArrayList<Participator>> response) {

                Log.d(TAG, "onResponse: getEnrolledList network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                enrolledUserList = response.body();
                dialog.closeProgressDialog();

                if (enrolledUserList != null){

                    myQxmSingleQxmEnrolledListAdapter = new MyQxmSingleQxmEnrolledListAdapter(context, enrolledUserList, picasso, participator -> qxmFragmentTransactionHelper.loadUserProfileFragment(participator.getId(),participator.getFullName()));
                    rvEnrolled.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvEnrolled.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
                    rvEnrolled.setItemAnimator(new DefaultItemAnimator());
                    rvEnrolled.setNestedScrollingEnabled(false);
                    rvEnrolled.setAdapter(myQxmSingleQxmEnrolledListAdapter);


                }else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Participator>> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }
    //endregion

    //region load previous data

    private void loadPreviousData(){

        myQxmSingleQxmEnrolledListAdapter = new MyQxmSingleQxmEnrolledListAdapter(context, enrolledUserList, picasso, participator -> qxmFragmentTransactionHelper.loadUserProfileFragment(participator.getId(),participator.getFullName()));
        rvEnrolled.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEnrolled.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        rvEnrolled.setItemAnimator(new DefaultItemAnimator());
        rvEnrolled.setNestedScrollingEnabled(false);
        rvEnrolled.setAdapter(myQxmSingleQxmEnrolledListAdapter);


    }

    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


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

        //hiding bottom navigation view when destroy this fragment
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    // endregion

}
