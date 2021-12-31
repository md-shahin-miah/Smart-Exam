package com.crux.qxm.views.fragments.leaderboard;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.leaderboard.LeaderboardAdapter;
import com.crux.qxm.db.models.leaderboard.LeaderBoardItem;
import com.crux.qxm.db.models.leaderboard.QxmLeaderboard;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.leaderboardFragmentFeature.DaggerLeaderboardFragmentComponent;
import com.crux.qxm.di.leaderboardFragmentFeature.LeaderboardFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
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

import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_CREATION_TIME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_CREATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_LEADERBOARD_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.QXM_QUESTION_SET_TITLE;
import static com.crux.qxm.utils.StaticValues.USER_BASIC_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment implements LeaderboardAdapter.LeaderboardItemClickListener {

    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "LeaderboardFragment";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private UserBasic user;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private List<LeaderBoardItem> leaderboardItemList;
    private QxmLeaderboard qxmLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;

    // endregion

    // region BindViewsWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;
    @BindView(R.id.cvQuestionSet)
    CardView cvQuestionSet;
    @BindView(R.id.tvQxmCreator)
    AppCompatTextView tvQxmCreator;
    @BindView(R.id.tvQxmCreationTime)
    AppCompatTextView tvQxmCreationTime;
    @BindView(R.id.ivQxmPrivacy)
    AppCompatImageView ivQxmPrivacy;
    @BindView(R.id.viewSeparator)
    View viewSeparator;
    @BindView(R.id.llMyInfoOnLeaderboardContainer)
    LinearLayoutCompat llMyInfoOnLeaderboardContainer;
    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;
    @BindView(R.id.tvTitleParticipator)
    AppCompatTextView tvTitleParticipator;
    @BindView(R.id.tvMyPosition)
    AppCompatTextView tvMyPosition;
    @BindView(R.id.tvUserFullName)
    AppCompatTextView tvUserFullName;
    @BindView(R.id.tvAchievedPoints)
    AppCompatTextView tvAchievedPoints;
    @BindView(R.id.tvTimeTaken)
    AppCompatTextView tvTimeTaken;
    @BindView(R.id.rlLeaderboardContainer)
    RelativeLayout rlLeaderboardContainer;
    @BindView(R.id.tvLeaderboardEmptyMessage)
    AppCompatTextView tvLeaderboardEmptyMessage;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvLeaderboard)
    RecyclerView rvLeaderboard;
    private String qxmId;
    private String qxmCreatorId;
    private String qxmTitle;
    private String qxmCreatorName;
    private String qxmCreationTime;
    private String qxmPrivacy;
    private String leaderBoardPrivacy;


    // endregion

    // region Fragment-NewInstance

    public static LeaderboardFragment newInstance(Bundle bundle) {

        LeaderboardFragment fragment = new LeaderboardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // endregion

    // region Fragment-Constructor
    public LeaderboardFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");
        qxmId = getArguments() != null ? getArguments().getString(QXM_ID_KEY) : null;
        user = getArguments() != null ? getArguments().getParcelable(USER_BASIC_KEY) : null;
        qxmCreatorId = getArguments() != null ? getArguments().getString(QXM_CREATOR_ID_KEY) : null;
        qxmTitle = getArguments() != null ? getArguments().getString(QXM_QUESTION_SET_TITLE) : null;
        qxmCreatorName = getArguments() != null ? getArguments().getString(QXM_CREATOR_NAME_KEY) : null;
        qxmCreationTime = getArguments() != null ? getArguments().getString(QXM_CREATION_TIME_KEY) : null;
        qxmPrivacy = getArguments() != null ? getArguments().getString(QXM_PRIVACY_KEY) : null;
        leaderBoardPrivacy = getArguments() != null ? getArguments().getString(QXM_LEADERBOARD_PRIVACY_KEY) : null;
        context = view.getContext();

        Log.d(TAG, "onViewCreated qxmCreatorId: " + qxmCreatorId);
        Log.d(TAG, "onViewCreated leaderBoardPrivacy: " + leaderBoardPrivacy);

        setUpDagger2();

        init();

        if (leaderboardItemList.size() == 0 && !TextUtils.isEmpty(qxmId)) {
            cvQuestionSet.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);

            if (user != null)
                getLeaderboardOfSpecificQxmNetworkCall(qxmId, user.getUserId());
            else
                getLeaderboardOfSpecificQxmNetworkCall(qxmId, token.getUserId());
        }

        initializeClickListeners();

    }


    // endregion

    // region SetUpDagger2

    private void setUpDagger2() {
        LeaderboardFragmentComponent leaderboardFragmentComponent =
                DaggerLeaderboardFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        leaderboardFragmentComponent.injectLeaderboardFragmentFeature(LeaderboardFragment.this);
    }

    // endregion

    // region Init

    private void init() {
        Log.d(TAG, "init: ");
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (leaderboardItemList == null)
            leaderboardItemList = new ArrayList<>();

        /*if (user != null && realmService.getSavedUser().getUserId().equals(user.getUserId()))
            tvTitleParticipator.setText(getString(R.string.you));
        else tvTitleParticipator.setText(getString(R.string.selected_participant));*/


        leaderboardAdapter = new LeaderboardAdapter(leaderboardItemList, context, picasso, qxmFragmentTransactionHelper, qxmId, this);

        rvLeaderboard.setLayoutManager(new LinearLayoutManager(context));
        rvLeaderboard.setAdapter(leaderboardAdapter);
        //rvLeaderboard.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            leaderboardItemList.clear();
            if (user != null)
                getLeaderboardOfSpecificQxmNetworkCall(qxmId, user.getUserId());
            else
                getLeaderboardOfSpecificQxmNetworkCall(qxmId, token.getUserId());
        });

    }

    // endregion

    // region GetLeaderboardOfSpecificQxmNetworkCall

    private void getLeaderboardOfSpecificQxmNetworkCall(String qxmId, String userId) {
        Log.d(TAG, String.format("getLeaderboardOfSpecificQxmNetworkCall: qxmId= %s, userId: %s", qxmId, userId));

        Call<QxmLeaderboard> getLeaderboardOfSpecificQxm = apiService.getLeaderboardOfSpecificQxm(token.getToken(), qxmId, userId);

        getLeaderboardOfSpecificQxm.enqueue(new Callback<QxmLeaderboard>() {
            @Override
            public void onResponse(@NonNull Call<QxmLeaderboard> call, @NonNull Response<QxmLeaderboard> response) {
                Log.d(TAG, "onResponse: getLeaderboardOfSpecificQxmNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());


                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: success");

                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: response.body = " + response.body().toString());
                            qxmLeaderboard = response.body();
                            leaderboardItemList.addAll(qxmLeaderboard.getLeaderBoard());
                            Log.d(TAG, "onResponse: leaderboardItemList = " + leaderboardItemList.toString());

                            Log.d(TAG, "onResponse qxmCreatorId: " + qxmCreatorId);
                            Log.d(TAG, "onResponse userId: " + userId);

                            if (qxmCreatorId.equals(userId)) {
                                leaderboardAdapter.notifyDataSetChanged();
                                setLeaderboardValuesInViews();
                                cvQuestionSet.setVisibility(View.VISIBLE);
                            } else {
                                if (!leaderBoardPrivacy.equals(LEADERBOARD_PRIVACY_PUBLIC)) {
                                    rvLeaderboard.setVisibility(View.GONE);
                                    setLeaderboardValuesInViews();
                                    cvQuestionSet.setVisibility(View.VISIBLE);
                                } else {
                                    rvLeaderboard.setVisibility(View.VISIBLE);
                                    leaderboardAdapter.notifyDataSetChanged();
                                    setLeaderboardValuesInViews();
                                    cvQuestionSet.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (response.code() == 403) {
                        Log.d(TAG, "onResponse: user session expired");
                        Toast.makeText(context, getString(R.string.expired_login_session), Toast.LENGTH_SHORT).show();
                        qxmFragmentTransactionHelper.logout(realmService);
                    } else if (response.code() == 401) {
                        //Log.d(TAG, "onResponse: " + qxmLeaderboard.toString());
                        Toast.makeText(context, getString(R.string.leader_board_not_published_yet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.toast_something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<QxmLeaderboard> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getLeaderboardOfSpecificQxmNetworkCall");
                Log.d(TAG, "onFailure: Error Message: " + t.getMessage());
                Log.d(TAG, "onFailure: Error Stacktrace: " + Arrays.toString(t.getStackTrace()));
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "Network error. Please try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setLeaderboardValuesInViews() {
        // qxm & qxm creator details [leaderboard header part]
        tvQxmTitle.setText(qxmTitle);
        tvQxmCreator.setText(qxmCreatorName);
        if (QxmStringIntegerChecker.isLongInt(qxmCreationTime))
            tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(
                    Long.parseLong(qxmCreationTime)
            ));
        if (qxmPrivacy.equals(QXM_PRIVACY_PRIVATE))
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_privacy_private));
        else
            ivQxmPrivacy.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_privacy_public));

        // my position & some result information

        if (user != null) {
            viewSeparator.setVisibility(View.VISIBLE);
            llMyInfoOnLeaderboardContainer.setVisibility(View.VISIBLE);

            if (realmService.getSavedUser().getUserId().equals(user.getUserId()))
                tvTitleParticipator.setText(getString(R.string.you));
            else tvTitleParticipator.setText(getString(R.string.selected_participant));

            if (user.getModifiedURLProfileImage() != null && !user.getModifiedURLProfileImage().isEmpty())
                picasso.load(user.getModifiedURLProfileImage()).error(getResources().getDrawable(R.drawable.ic_user_default)).into(ivUserImage);

            if (qxmLeaderboard != null) {
                tvMyPosition.setText(String.format("#%s", qxmLeaderboard.getMyPosition()));

                tvUserFullName.setText(user.getFullName());

                tvAchievedPoints.setText(qxmLeaderboard.getMyInfo().getUserPerformance().getAchievePoints());

                if (QxmStringIntegerChecker.isLongInt(qxmLeaderboard.getMyInfo().getUserPerformance().getTimeTakenToAnswer())) {

                    tvTimeTaken.setText(TimeFormatString.getDurationInHourMinSec(
                            Long.parseLong(
                                    qxmLeaderboard.getMyInfo().getUserPerformance().getTimeTakenToAnswer()
                            )
                    ));
                }
            }
        } else {
            viewSeparator.setVisibility(View.INVISIBLE);
            llMyInfoOnLeaderboardContainer.setVisibility(View.GONE);
        }

    }


    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        tvQxmCreator.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });

        ivUserImage.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });

        tvUserFullName.setOnClickListener(v -> {
//            qxmFragmentTransactionHelper.loadUserProfileFragment();
        });
    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(qxmTitle) && !TextUtils.isEmpty(qxmCreatorName)
                && !TextUtils.isEmpty(qxmCreationTime) && !TextUtils.isEmpty(qxmPrivacy)
                && qxmLeaderboard != null) {
            setLeaderboardValuesInViews();
        }

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

    @Override
    public void onItemClickListener(String qxmId, UserBasic userBasic) {
        Log.d(TAG, "onItemClickListener: ");
        if (qxmCreatorId != null && qxmCreatorId.equals(token.getUserId())) {
            Log.d(TAG, "onItemClickListener: qxmCreatorId is not null");
            qxmFragmentTransactionHelper.loadFullResultFragment(qxmId, userBasic);
        }
    }

    // endregion

}
