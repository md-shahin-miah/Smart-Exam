package com.crux.qxm.views.fragments.qxmResult;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.di.resultSummaryFragmentFeature.DaggerResultSummaryFragmentComponent;
import com.crux.qxm.di.resultSummaryFragmentFeature.ResultSummaryFragmentComponent;
import com.crux.qxm.utils.TimeFormatString;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.FULL_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.PROFILE_IMAGE_KEY;
import static com.crux.qxm.utils.StaticValues.USER_PERFORMANCE_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultSummaryFragment extends Fragment {

    // region BindViewsWithButterKnife

    @BindView(R.id.tvParticipantName)
    AppCompatTextView tvParticipantName;

    @BindView(R.id.tvParticipationDate)
    AppCompatTextView tvParticipationDate;

    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;

    @BindView(R.id.tvUserExperience)
    AppCompatTextView tvUserExperience;

    @BindView(R.id.tvAnsweredPoints)
    AppCompatTextView tvAnsweredPoints;

    @BindView(R.id.tvTimeTaken)
    AppCompatTextView tvTimeTaken;

    @BindView(R.id.tvPosition)
    AppCompatTextView tvPosition;

    @BindView(R.id.tvAchievedPointsInPercentage)
    AppCompatTextView tvAchievedPointsInPercentage;

    @BindView(R.id.tvAchievedPoints)
    AppCompatTextView tvAchievedPoints;

    @BindView(R.id.tvTotalCorrectMCQ)
    AppCompatTextView tvTotalCorrectMCQ;

    @BindView(R.id.tvTotalCorrectFillInTheBlanks)
    AppCompatTextView tvTotalCorrectFillInTheBlanks;

    @BindView(R.id.tvTotalCorrectShortAnswer)
    AppCompatTextView tvTotalCorrectShortAnswer;

    @BindView(R.id.btnReviewAgain)
    AppCompatButton btnReviewAgain;

    @BindView(R.id.btnDone)
    AppCompatButton btnDone;

    // endregion

    // region ResultSummaryFragment-Properties

    @Inject
    Picasso picasso;
    private static final String TAG = "ResultSummaryFragment";
    private String fullName;
    private String profileImage;
    private UserPerformance userPerformance;
    private Context context;

    // endregion

    // region Fragment-Constructor

    public ResultSummaryFragment() {
        // Required empty public constructor
    }

    // endregion

    // region newInstance

    public static ResultSummaryFragment newInstance(String fullName, String profileImage, UserPerformance userPerformance) {

        Bundle args = new Bundle();
        args.putString(FULL_NAME_KEY, fullName);
        args.putString(PROFILE_IMAGE_KEY, profileImage);
        args.putParcelable(USER_PERFORMANCE_KEY, userPerformance);
        ResultSummaryFragment fragment = new ResultSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-OnCreatedView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_summary, container, false);

        ButterKnife.bind(this, view);

        context = view.getContext();

        setUpDagger2(context);

        return view;
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        ResultSummaryFragmentComponent resultSummaryFragmentComponent =
                DaggerResultSummaryFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        resultSummaryFragmentComponent.injectResultSummaryFragment(ResultSummaryFragment.this);
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = getArguments() != null ? getArguments().getString(FULL_NAME_KEY) : null;
        profileImage = getArguments() != null ? getArguments().getString(PROFILE_IMAGE_KEY) : null;
        userPerformance = getArguments() != null ? getArguments().getParcelable(USER_PERFORMANCE_KEY) : null;

        if (userPerformance != null) {
            Gson gson = new Gson();
            Log.d(TAG, "onViewCreated: userPerformance JSON: " + gson.toJson(userPerformance));

            init();
        }
    }

    // endregion

    // region Init

    private void init() {

        loadDataInParticipantsDetailsViews();

        loadDataInObservationViews();

        loadDataInResultSummaryViews();
    }


    // endregion

    // region LoadDataInParticipantsDetailsViews

    private void loadDataInParticipantsDetailsViews() {
        if (!TextUtils.isEmpty(fullName))
            tvParticipantName.setText(fullName);
        if (!TextUtils.isEmpty(profileImage))
            picasso.load(profileImage).into(ivUserImage);

        tvParticipationDate.setText(TimeFormatString.getTimeAndDate(
                Long.parseLong(userPerformance.getParticipatedDate())
        ));
        tvUserExperience.setText(userPerformance.getExperienceLevel());
    }

    // endregion

    // region LoadDataInObservationViews

    private void loadDataInObservationViews() {
        tvAnsweredPoints.setText(userPerformance.getAnsweredPoints());
        tvTimeTaken.setText(TimeFormatString.getDurationInHourMinSec(
                Long.parseLong(userPerformance.getTimeTakenToAnswer())
        ));
    }

    // endregion

    // region LoadDataInResultSummaryViews

    private void loadDataInResultSummaryViews() {
        tvPosition.setText(userPerformance.getRankPosition());
        tvAchievedPoints.setText(userPerformance.getAchievePoints());
        tvTotalCorrectMCQ.setText(userPerformance.getTotalCorrectMCQ());
        tvTotalCorrectFillInTheBlanks.setText(userPerformance.getTotalCorrectFillInTheBlanks());
        tvTotalCorrectShortAnswer.setText(userPerformance.getTotalCorrectShortAnswer());
    }

    // endregion
}
