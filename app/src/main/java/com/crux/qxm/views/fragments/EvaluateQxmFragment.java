package com.crux.qxm.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.evaluateQxm.EvaluateAnswerSheetAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.evaluation.EvaluationDetails;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.evaluateQxmFragmentFeature.DaggerEvaluateQxmFragmentComponent;
import com.crux.qxm.di.evaluateQxmFragmentFeature.EvaluateQxmFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.RecyclerViewItemDecoration;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.PARTICIPATION_ID_KEY;
import static com.crux.qxm.utils.StaticValues.PARTICIPATOR_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;
import static com.crux.qxm.utils.StaticValues.QXM_TIME_TYPE_NO_LIMIT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluateQxmFragment extends Fragment {

    // region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrowEvaluateQxm)
    AppCompatImageView ivBackArrowEvaluateQxm;
    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;
    @BindView(R.id.tvParticipantName)
    AppCompatTextView tvParticipantName;
    @BindView(R.id.tvParticipationDate)
    AppCompatTextView tvParticipationDate;
    @BindView(R.id.tvUserExperience)
    AppCompatTextView tvUserExperience;
    @BindView(R.id.tvAnsweredPoints)
    AppCompatTextView tvAnsweredPoints;
    @BindView(R.id.tvTimeTaken)
    AppCompatTextView tvTimeTaken;

    @BindView(R.id.tvTotalTime)
    AppCompatTextView tvTotalTime;
    @BindView(R.id.tvTotalPoints)
    AppCompatTextView tvTotalPoints;
    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;
    @BindView(R.id.ivQuestionImage)
    AppCompatImageView ivQuestionImage;
    @BindView(R.id.tvQxmDescription)
    AppCompatTextView tvQxmDescription;
    @BindView(R.id.rvEvaluateQxm)
    RecyclerView rvEvaluateQxm;
    @BindView(R.id.btnSubmitEvaluation)
    AppCompatButton btnSubmitEvaluation;
    // endregion

    // region EvaluateQxmFragmentClassProperties

    private static final String TAG = "EvaluateQxmFragment";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;
    private Context context;
    private String participationId;
    private EvaluationDetails evaluationDetails;
    private QxmToken token;
    private double totalPoints;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private RealmService realmService;

    // endregion

    // region  Constructor

    public EvaluateQxmFragment() {
        // Required empty public constructor
    }

    // endregion

    // region NewInstance

    public static EvaluateQxmFragment newInstance(String participationId, String participatorName) {

        Bundle args = new Bundle();
        args.putString(PARTICIPATION_ID_KEY, participationId);
        args.putString(PARTICIPATOR_NAME_KEY, participatorName);
//        args.putParcelable(EVALUATION_DETAILS_KEY, evaluationDetails);

        EvaluateQxmFragment fragment = new EvaluateQxmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluate_qxm, container, false);

        ButterKnife.bind(this, view);

        context = view.getContext();

        setUpDagger2(context);

        return view;
    }

    // endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {

        EvaluateQxmFragmentComponent evaluateQxmFragmentComponent =
                DaggerEvaluateQxmFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        evaluateQxmFragmentComponent.injectEvaluateQxmFragmentFeature(EvaluateQxmFragment.this);

    }
    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        participationId = getArguments() != null ? getArguments().getString(PARTICIPATION_ID_KEY) : null;
        String participatorName = getArguments() != null ? getArguments().getString(PARTICIPATOR_NAME_KEY) : null;
        init();
        initializeClickListeners();

        if (!TextUtils.isEmpty(participationId)) {

            getQuestionSheetForEvaluationNetworkCall(participationId, participatorName);

        } else {
            Log.d(TAG, "onViewCreated: Participation id is null");
            Toast.makeText(context, "Something goes wrong! Please, try again later.", Toast.LENGTH_SHORT).show();
        }


    }

    // region LoadDataIntoViews

    private void loadDataInViews() {

        loadParticipantsDetails();

        if (evaluationDetails.getQuestionSettings() != null) {
            if (!TextUtils.isEmpty(evaluationDetails.getQuestionSettings().getExamDuration())
                    && !evaluationDetails.getQuestionSettings().getExamDuration().equals(QXM_TIME_TYPE_NO_LIMIT)) {

                String regex = "[0-9]+";
                if (evaluationDetails.getQuestionSettings().getExamDuration().matches(regex)) {
                    long examDuration = Long.parseLong(evaluationDetails.getQuestionSettings().getExamDuration());
                    tvTotalTime.setText(String.format("Total Time: %s",
                            TimeFormatString.getDurationInHourMinSec(examDuration)));
                } else {
                    tvTotalTime.setText(String.format("Total Time: %s", evaluationDetails.getQuestionSettings().getExamDuration()));
                }

            } else {
                tvTotalTime.setText(getString(R.string.total_time_unlimited));
            }
        } else {
            tvTotalTime.setText(getString(R.string.total_time_unlimited));
        }

        // region setTotalPoints
        totalPoints = 0;

        for (Question question : evaluationDetails.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (!TextUtils.isEmpty(question.getMultipleChoice().getPoints()))
                        totalPoints += Double.parseDouble(question.getMultipleChoice().getPoints());
                    break;
                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (!TextUtils.isEmpty(question.getFillInTheBlanks().getPoints()))
                        totalPoints += Double.parseDouble(question.getFillInTheBlanks().getPoints());
                    break;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (!TextUtils.isEmpty(question.getShortAnswer().getPoints()))
                        totalPoints += Double.parseDouble(question.getShortAnswer().getPoints());
                    break;
            }
        }

        tvTotalPoints.setText(String.format("Total Points: %s", totalPoints));
        //endregion

        tvQxmTitle.setText(evaluationDetails.getQuestionSet().getQuestionSetTitle());

        tvQxmDescription.setText(evaluationDetails.getQuestionSet().getQuestionSetDescription());
        SetlinkClickable.setLinkclickEvent(tvQxmDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });


        if (!TextUtils.isEmpty(evaluationDetails.getQuestionSet().getQuestionSetThumbnail())) {
            picasso.load(evaluationDetails.getQuestionSet().getQuestionSetThumbnail()).into(ivQuestionImage);
            ivQuestionImage.setVisibility(View.VISIBLE);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }


    }

    // endregion

    // endregion

    // region Init

    private void init() {

        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());


    }

    // endregion

    // region InitAdapter

    private void initAdapter() {
        EvaluateAnswerSheetAdapter evaluateAnswerSheetAdapter = new EvaluateAnswerSheetAdapter(context, picasso, evaluationDetails.getQuestionSet().getQuestions());
        rvEvaluateQxm.setLayoutManager(new LinearLayoutManager(context));
        rvEvaluateQxm.setAdapter(evaluateAnswerSheetAdapter);
        rvEvaluateQxm.addItemDecoration(new RecyclerViewItemDecoration(5, 5, 5, 5));
        rvEvaluateQxm.setNestedScrollingEnabled(false);

        rvEvaluateQxm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    btnSubmitEvaluation.setVisibility(View.GONE);
                } else {
                    btnSubmitEvaluation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // endregion

    // region initializeClickListeners

    private void initializeClickListeners() {


        btnSubmitEvaluation.setOnClickListener(v -> {

            calculateAndSaveResultSummary();
            submitEvaluationNetworkCall();

        });

        ivBackArrowEvaluateQxm.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
    }


    // endregion

    // region LoadParticipantsDetails

    private void loadParticipantsDetails() {

        if (!TextUtils.isEmpty(evaluationDetails.getParticipator().getModifiedProfileImage()))
            picasso.load(evaluationDetails.getParticipator().getModifiedProfileImage()).into(ivUserImage);
        tvParticipantName.setText(evaluationDetails.getParticipator().getFullName());

        if (!TextUtils.isEmpty(evaluationDetails.getUserPerformance().getExperienceLevel()))
            tvUserExperience.setText(evaluationDetails.getUserPerformance().getExperienceLevel());
        else
            tvUserExperience.setText("1");

        tvParticipationDate.setText(TimeFormatString.getTimeAndDate(
                Long.parseLong(evaluationDetails.getUserPerformance().getParticipatedDate())
        ));
        tvAnsweredPoints.setText(evaluationDetails.getUserPerformance().getAnsweredPoints());

        tvTimeTaken.setText(TimeFormatString.getDurationInHourMinSec(
                Long.parseLong(evaluationDetails.getUserPerformance().getTimeTakenToAnswer())
        ));
    }

    // endregion

    // region CalculateAndSaveResultSummary

    private void calculateAndSaveResultSummary() {
        double achievedPoints = 0;
        int totalMCQ = 0;
        int correctMCQ = 0;
        int totalFillInTheBlanks = 0;
        int correctFillInTheBlanks = 0;
        int totalShortAnswer = 0;
        int correctShortAnswer = 0;

        for (Question question : evaluationDetails.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (!TextUtils.isEmpty(question.getMultipleChoice().getPoints()) && question.getMultipleChoice().isCorrect()) {
                        correctMCQ++;
                        achievedPoints += Double.parseDouble(question.getMultipleChoice().getPoints());
                    }
                    totalMCQ++;

                    break;
                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (!TextUtils.isEmpty(question.getFillInTheBlanks().getAchievedPoints()) && question.getFillInTheBlanks().isAnswerPartiallyCorrect()) {
                        achievedPoints += Double.parseDouble(question.getFillInTheBlanks().getAchievedPoints());
                        correctFillInTheBlanks++;


                    } else if (!TextUtils.isEmpty(question.getFillInTheBlanks().getPoints()) && question.getFillInTheBlanks().isCorrect()) {
                        achievedPoints += Double.parseDouble(question.getFillInTheBlanks().getPoints());
                        correctFillInTheBlanks++;
                    }
                    totalFillInTheBlanks++;
                    break;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (!TextUtils.isEmpty(question.getShortAnswer().getPoints()) && question.getShortAnswer().isCorrect()) {
                        achievedPoints += Double.parseDouble(question.getShortAnswer().getGivenPoints());
                        correctShortAnswer++;
                    }
                    totalShortAnswer++;
                    break;
            }
        }

        if (evaluationDetails.getUserPerformance().getAchievePoints() != null
                && Double.parseDouble(evaluationDetails.getUserPerformance().getAchievePoints()) > 0.0f) {
            double negativePoint = evaluationDetails.getQuestionSettings().getNegativePoints(); // ForEachQuestion
            double totalNegativePoints = (totalMCQ - correctMCQ) * negativePoint;
            totalNegativePoints += ((totalFillInTheBlanks - correctFillInTheBlanks) * negativePoint);
            totalNegativePoints += ((totalShortAnswer - correctShortAnswer) * negativePoint);

            achievedPoints -= totalNegativePoints;
        }

        Log.d(TAG, "AchievedPoints: " + achievedPoints);
        evaluationDetails.setMarks(String.valueOf(achievedPoints));
        evaluationDetails.getUserPerformance().setAchievePoints(String.format("%s out of %s", achievedPoints, totalPoints));

        if (achievedPoints > 0.0f) {
            evaluationDetails.getUserPerformance().setAchievePointsInPercentage(
                    String.format(Locale.ENGLISH, "%.2f%%", ((achievedPoints / totalPoints) * 100))
            );
        } else {
            evaluationDetails.getUserPerformance().setAchievePointsInPercentage(getString(R.string.zero_percentage));
        }

        Log.d(TAG, String.format("MultipleChoiceQuestion: %s out of %s", correctMCQ, totalMCQ));
        evaluationDetails.getUserPerformance().setTotalCorrectMCQ(String.format("%s out of %s", correctMCQ, totalMCQ));

        Log.d(TAG, String.format("FillInTheBlanks: %s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));
        evaluationDetails.getUserPerformance().setTotalCorrectFillInTheBlanks(String.format("%s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));

        Log.d(TAG, String.format("ShortAnswer: %s out of %s", correctShortAnswer, totalShortAnswer));
        evaluationDetails.getUserPerformance().setTotalCorrectShortAnswer(String.format("%s out of %s", correctShortAnswer, totalShortAnswer));


    }

    // endregion

    // region getQuestionSheetForEvaluationNetworkCall
    public void getQuestionSheetForEvaluationNetworkCall(String participationId, String participatorName) {

        Log.d(TAG, "getQuestionSheetForEvaluationNetworkCall: participationId = " + participationId);
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Qxm Evaluation", String.format("%s's answer sheet is loading for evaluation. ", participatorName), false);

        Call<EvaluationDetails> getExamQuestionSheet = apiService.getEvaluationDetails(token.getToken(), participationId);
        getExamQuestionSheet.enqueue(new Callback<EvaluationDetails>() {
            @Override
            public void onResponse(@NonNull Call<EvaluationDetails> call, @NonNull Response<EvaluationDetails> response) {
                Log.d(TAG, "onResponse: getQuestionSheetAndLoadViewNetworkCall network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());


                dialog.closeProgressDialog();

                if (response.code() == 200) {
                    if (response.body() != null) {
                        evaluationDetails = new EvaluationDetails();
                        evaluationDetails.setId(response.body().getId());
                        evaluationDetails.setQuestionSet(response.body().getQuestionSet());
                        evaluationDetails.setQuestionSettings(response.body().getQuestionSettings());
                        evaluationDetails.setParticipator(response.body().getParticipator());
                        evaluationDetails.setParticipatedAt(response.body().getParticipatedAt());
                        evaluationDetails.setParticipatedQxm(response.body().getParticipatedQxm());
                        evaluationDetails.setUserPerformance(response.body().getUserPerformance());

                        Gson gson = new Gson();
                        Log.d(TAG, "onResponse: " + gson.toJson(evaluationDetails));


                        if (evaluationDetails != null) {
                            // init
                            initAdapter();
                            loadDataInViews();

                        } else
                            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EvaluationDetails> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: ", t);

                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // endregion

    // region SubmitEvaluationNetworkCall

    private void submitEvaluationNetworkCall() {
        Log.d(TAG, "Token: " + token.getToken());
        Log.d(TAG, "UserId: " + token.getUserId());
        Log.d(TAG, "ParticipationId: " + participationId);
        Log.d(TAG, "qxmId: " + evaluationDetails.getId());
        Gson gson = new Gson();
        Log.d(TAG, "QxmModel->QuestionSet: " + gson.toJson(evaluationDetails));


        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Submit Evaluation", "The evaluated answer is being submitted. Please wait...", false);


        Call<QxmApiResponse> submitEvaluatedAnswer = apiService.submitEvaluatedAnswer(token.getToken(), participationId, evaluationDetails);

        submitEvaluatedAnswer.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse: submitEvaluatedAnswer Network Call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    Toast.makeText(context, "The evaluated answer has been submitted successfully.", Toast.LENGTH_SHORT).show();
                    dialog.closeProgressDialog();

                    Objects.requireNonNull(getActivity()).onBackPressed();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);

                } else {
                    Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    dialog.closeProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }
}
