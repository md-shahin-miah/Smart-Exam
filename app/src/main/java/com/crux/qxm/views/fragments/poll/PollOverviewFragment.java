package com.crux.qxm.views.fragments.poll;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.FeedPollOptionAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.pollOverviewFragmentFeature.DaggerPollOverviewFragmentComponent;
import com.crux.qxm.di.pollOverviewFragmentFeature.PollOverviewFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmActionShareLink;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmShareContentToGroup;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.QxmUrlHelper;
import com.crux.qxm.utils.qxmDialogs.QxmReportDialog;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static com.crux.qxm.utils.StaticValues.POLL_ID_KEY;
import static com.crux.qxm.utils.StaticValues.REPORT_POLL;
import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_POLL;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollOverviewFragment extends Fragment {

    //region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "PollOverviewFragment";
    private Context context;
    private FeedData pollFeedData;
    private String pollId;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    //endregion

    //region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.nsvRootView)
    NestedScrollView nsvRootView;
    @BindView(R.id.noInternetView)
    View noInternetView;
    @BindView(R.id.llSingleFeedItem)
    LinearLayoutCompat llSingleFeedItem;
    @BindView(R.id.FLFeedThumbnailContainer)
    FrameLayout FLFeedThumbnailContainer;
    @BindView(R.id.tvFeedPollTitle)
    AppCompatTextView tvFeedPollTitle;
    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;
    @BindView(R.id.ivPollFeedOptions)
    AppCompatImageView ivPollFeedOptions;
    @BindView(R.id.tvPollCreatorFullName)
    AppCompatTextView tvPollCreatorName;
    @BindView(R.id.tvPollType)
    AppCompatTextView tvPollType;
    @BindView(R.id.tvPollCreationTime)
    AppCompatTextView tvPollCreationTime;
    @BindView(R.id.ivPollPrivacy)
    AppCompatImageView ivPollPrivacy;
    @BindView(R.id.tvPollTitle)
    AppCompatTextView tvPollTitle;
    @BindView(R.id.ivPollThumbnail)
    AppCompatImageView ivPollThumbnail;
    @BindView(R.id.rvPollOption)
    RecyclerView rvPollOption;
    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;
    @BindView(R.id.tvParticipantsQuantity)
    AppCompatTextView tvParticipantsQuantity;
    @BindView(R.id.ivIgniteQuantity)
    AppCompatImageView ivIgniteQuantity;
    @BindView(R.id.tvIgniteQuantity)
    AppCompatTextView tvIgniteQuantity;
    @BindView(R.id.tvShareQuantity)
    AppCompatTextView tvShareQuantity;
    @BindView(R.id.rlPollEventsInfo)
    RelativeLayout rlPollEventsInfo;
    @BindView(R.id.llcIgnite)
    LinearLayoutCompat llcIgnite;
    @BindView(R.id.llcShare)
    LinearLayoutCompat llcShare;
    @BindView(R.id.ivIgnite)
    AppCompatImageView ivIgnite;
    @BindView(R.id.ivShare)
    AppCompatImageView ivShare;
    @BindView(R.id.tvIgnite)
    AppCompatTextView tvIgnite;
    @BindView(R.id.tvShare)
    AppCompatTextView tvShare;
    //endregion

    //region Fragment-constructor
    public PollOverviewFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Fragment-newInstance
    public static PollOverviewFragment newInstance(String pollId) {

        Bundle args = new Bundle();
        args.putString(POLL_ID_KEY, pollId);
        PollOverviewFragment fragment = new PollOverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Override-onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poll_overview, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
    //endregion

    //region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        pollId = getArguments() != null ? getArguments().getString(POLL_ID_KEY) : null;

        setUpDagger2(context);
        clickListeners();

        if (pollId != null) {
            init();
            noInternetFunctionality();

            if (pollFeedData == null) {
                swipeRefreshLayout.setRefreshing(true);
                singlePollOverviewNetworkCall();
            } else {
                setValuesInViews(pollFeedData);
            }

        }

    }
    //endregion

    //region setValuesInViews
    private void setValuesInViews(FeedData pollFeedData) {
        int igniteCount1;
        int shareCount;


        Log.d(TAG, "onBindViewHolder: Poll Feed: " + pollFeedData.toString());

        tvFeedPollTitle.setText(pollFeedData.getFeedName());

        // Poll creator image
        if (!TextUtils.isEmpty(pollFeedData.getFeedCreatorImageURL())) {
            picasso.load(pollFeedData.getFeedCreatorImageURL())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(ivUserImage);
        } else {
            ivUserImage.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_user_default));
        }

        // Goto Poll Creator Profile
        ivUserImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        pollFeedData.getFeedCreatorId(),
                        pollFeedData.getFeedCreatorName())
        );

        // poll creator name
        tvPollCreatorName.setText(pollFeedData.getFeedCreatorName());


        // region poll-body


        // poll title
        Log.d(TAG, "onBindViewHolder: poll title: " + pollFeedData.getFeedName());
        tvPollTitle.setText(pollFeedData.getFeedName());


        // region show poll thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(pollFeedData.getFeedThumbnailURL())) {
            ivPollThumbnail.setVisibility(View.VISIBLE);
            picasso.load(pollFeedData.getFeedThumbnailURL()).into(ivPollThumbnail);
        } else {
            ivPollThumbnail.setVisibility(View.GONE);
        }
        // endregion

        // region poll options


        FeedPollOptionAdapter feedPollOptionAdapter = new FeedPollOptionAdapter(context, pollFeedData.getFeedPollId(), apiService, token.getUserId(), token.getToken(), pollFeedData, tvParticipantsQuantity, qxmFragmentTransactionHelper);
        rvPollOption.setLayoutManager(new LinearLayoutManager(context));
        rvPollOption.setAdapter(feedPollOptionAdapter);
        rvPollOption.setNestedScrollingEnabled(false);


        // endregion


        // endregion

        if (pollFeedData.getPollParticipatorQuantity() != null && !pollFeedData.getPollParticipatorQuantity().equals("0")) {

            if (pollFeedData.getPollParticipatorQuantity().equals("1"))
                tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participant)));
            else
                tvParticipantsQuantity.setText(String.format("%s %s", pollFeedData.getPollParticipatorQuantity(), context.getResources().getString(R.string.participants)));

            tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));

        } else {
            tvParticipantsQuantity.setText(String.format("0 %s", context.getResources().getString(R.string.participant)));
            tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
        }


        // show feed item time ago
        if (QxmStringIntegerChecker.isLongInt(pollFeedData.getFeedCreationTime())) {

            long feedCreationTime = Long.parseLong(pollFeedData.getFeedCreationTime());
            String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
            Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
            tvPollCreationTime.setText(feedPostTimeAgo);
        } else {
            Log.d(TAG, "onBindViewHolder: FeedCreationTime is not long int");
//                    pollFeedViewHolder.tvFeedCreationTime.setVisibility(View.GONE);
        }

        // region PollFeedAdapterItemClickListeners

        tvPollCreatorName.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(pollFeedData.getFeedCreatorId(),
                        pollFeedData.getFeedCreatorName()));

        // endregion

        // region IgniteCount

        if (TextUtils.isEmpty(pollFeedData.getFeedIgniteQuantity())) {
            igniteCount1 = 0;
            pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
        } else {
            igniteCount1 = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
            pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount1));
        }

        if (!pollFeedData.getFeedIgniteQuantity().equals("0")) {
            tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
            ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

        } else {
            tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
            ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
        }
        tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

        // endregion

        // region ShareCount

        if (TextUtils.isEmpty(pollFeedData.getFeedShareQuantity())) {
            shareCount = 0;
            pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
        } else {
            shareCount = Integer.parseInt(pollFeedData.getFeedShareQuantity());
            pollFeedData.setFeedShareQuantity(String.valueOf(shareCount));
        }

        tvShareQuantity.setText(pollFeedData.getFeedShareQuantity());

        // endregion


        // region feed menu item onclick

        ivPollFeedOptions.setOnClickListener(v -> {
            boolean isCreator = pollFeedData.getFeedCreatorId().equals(token.getUserId());
            loadPollFeedMenu(getActivity(), v, pollFeedData, isCreator);


        });

        // endregion

        // region ignite and share

        // region at first checking if user already clicked on ignite
        if (pollFeedData.isIgnitedClicked()) {

            String ignited = "Ignited";
            tvIgnite.setText(ignited);
            tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
            ivIgnite.setImageResource(R.drawable.ic_ignite_active);
        }
        // endregion

        //region PollIgnite
        llcIgnite.setOnClickListener(v -> {

            if (!tvIgnite.getText().equals("Ignited")) {
                String ignited = "Ignited";
                tvIgnite.setText(ignited);
                tvIgnite.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                ivIgnite.setImageResource(R.drawable.ic_ignite_active);

                ignitePollNetworkCall(token.getUserId(), pollFeedData.getFeedPollId());

                int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());
                igniteCount++;
                pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());
                tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                ivIgniteQuantity.setImageResource(R.drawable.ic_ignite_active);

            } else {
                String ignite = "Ignite";
                tvIgnite.setText(ignite);
                tvIgnite.setTextColor(context.getResources().getColor(R.color.grey));
                ivIgnite.setImageResource(R.drawable.ic_ignite);

                ignitePollNetworkCall(token.getUserId(), pollFeedData.getFeedPollId());
                int igniteCount = Integer.parseInt(pollFeedData.getFeedIgniteQuantity());

                if (igniteCount > 0)
                    igniteCount--;
                else
                    igniteCount = 0;

                pollFeedData.setFeedIgniteQuantity(String.valueOf(igniteCount));
                tvIgniteQuantity.setText(pollFeedData.getFeedIgniteQuantity());

                if (igniteCount <= 0) {

                    tvIgniteQuantity.setTextColor(context.getResources().getColor(R.color.grey));
                    ivIgniteQuantity.setImageResource(R.drawable.ic_ignite);
                }
            }

        });
        //endregion

        //region PollShare
        llcShare.setOnClickListener(v -> {
            QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                    (getActivity(), apiService, context, picasso, token.getUserId(), token.getToken());

            if (!tvShare.getText().toString().equals(context.getString(R.string.shared))) {
                qxmShareContentToGroup.shareToGroup
                        (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

            } else {

                qxmShareContentToGroup.shareToGroup
                        (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                if (qxmShareContentToGroup.sharedSuccess) {
                    qxmShareContentToGroup.sharedSuccess = false;
                    tvShare.setText(context.getString(R.string.shared));
                    tvShare.setTextColor(context.getResources().getColor(R.color.light_blue_400));
                    ivShare.setImageResource(R.drawable.ic_share_active);
                }
            }
        });
        //endregion

        //endregion
    }
    //endregion

    //region loadPollFeedMenu
    private void loadPollFeedMenu(FragmentActivity fragmentActivity, View v, FeedData pollFeedData, boolean isCreator) {
        PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_poll_overview, popup.getMenu());

        if (isCreator) popup.getMenu().findItem(R.id.action_report_poll).setVisible(false);

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {


                case R.id.action_share_to_group:

                    QxmShareContentToGroup qxmShareContentToGroup = new QxmShareContentToGroup
                            (fragmentActivity, apiService, context, picasso, token.getUserId(), token.getToken());
                    qxmShareContentToGroup.shareToGroup
                            (pollFeedData.getFeedPollId(), SHARED_CATEGORY_POLL);

                    break;

                case R.id.action_share_link:
                    QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
                    QxmUrlHelper urlHelper = new QxmUrlHelper();
                    qxmActionShareLink.sharePollLink(urlHelper.getQxmUrl(pollFeedData.getFeedPollId()));

                    break;


                case R.id.action_report_poll:

                    QxmReportDialog qxmReportDialog = new QxmReportDialog(fragmentActivity, context, apiService, token.getToken(), token.getUserId(), pollFeedData.getFeedPollId());
                    qxmReportDialog.showReportDialog(REPORT_POLL);
                    break;
            }


            return false;
        });
        popup.show();

    }
    //endregion

    //region setUpDagger2
    private void setUpDagger2(Context context) {
        PollOverviewFragmentComponent pollOverviewFragmentComponent =
                DaggerPollOverviewFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent()).build();

        pollOverviewFragmentComponent.injectPollOverviewFragmentFeature(PollOverviewFragment.this);
    }
    //endregion

    //region init
    private void init() {
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        apiService = retrofit.create(QxmApiService.class);
        Realm realm = Realm.getDefaultInstance();
        RealmService realmService = new RealmService(realm);
        token = realmService.getApiToken();
        realmService.close();

    }
    //endregion

    //region noInternetFunctionality
    private void noInternetFunctionality() {

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            nsvRootView.setVisibility(GONE);
            noInternetView.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
                    singlePollOverviewNetworkCall();
                }
        );
    }
    //endregion

    //region singlePollOverviewNetworkCall
    private void singlePollOverviewNetworkCall() {



        Call<FeedData> getSinglePollOverviewData = apiService.getSinglePollOverviewData(token.getToken(), pollId, token.getUserId());

        getSinglePollOverviewData.enqueue(new Callback<FeedData>() {
            @Override
            public void onResponse(@NonNull Call<FeedData> call, @NonNull Response<FeedData> response) {

                Log.d(TAG, "onResponse: singlePollOverviewNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setVisibility(GONE);

                noInternetView.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (response.body() != null) {
                        pollFeedData = response.body();

                        nsvRootView.setVisibility(View.VISIBLE);
                        setValuesInViews(pollFeedData);

                    } else {
                        Log.d(TAG, "onResponse: response body is null");
                        Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<FeedData> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: singlePollOverviewNetworkCall");
                Log.d(TAG, "onFailure: stackTrace: " + Arrays.toString(t.getStackTrace()));
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setVisibility(GONE);
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                if (!NetworkState.haveNetworkConnection(context)) {
                    noInternetView.setVisibility(View.VISIBLE);
                    nsvRootView.setVisibility(View.GONE);
                }
            }
        });
    }
    //endregion

    //region clickListeners
    private void clickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            // transaction to HomeFragment
            if (getActivity() != null) {
                Log.d(TAG, "onBackPressed calling from question overview fragment");
                getActivity().onBackPressed();
            }
        });
    }
    //endregion

    // region IgnitePollNetworkCall
    private void ignitePollNetworkCall(String userId, String pollId) {
        Log.d(TAG, String.format("igniteQxmNetworkCall: userId= %s, pollId= %s", userId, pollId));

        Call<QxmApiResponse> igniteQxm = apiService.pollIgnite(token.getToken(), userId, pollId);
        igniteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: ignitePollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");
                    /*if (response.body() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ignitePollNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
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

    // endregion
}
