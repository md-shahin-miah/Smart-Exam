package com.crux.qxm.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.participationQxm.ExamQuestionSheetAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.participateQxmFragmentFeature.DaggerParticipateQxmFragmentComponent;
import com.crux.qxm.di.participateQxmFragmentFeature.ParticipateQxmFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmTotalPointsCalculator;
import com.crux.qxm.utils.QxmdpHelper;
import com.crux.qxm.utils.RecyclerViewItemDecoration;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;
import static com.crux.qxm.utils.StaticValues.QXM_MODEL_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_TIME_TYPE_NO_LIMIT;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_PARTICIPATE_QXM;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

public class ParticipateQxmFragment extends Fragment {

    // region BindViewsWithButterKnife

    private static final String TAG = "ParticipateQxmFragment";
    // region SeeQuestion
    @BindView(R.id.toolbar_participation_qxm_fragment)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    /*@BindView(R.id.view_TopMargin)
    View view_TopMargin;*/

    // endregion
    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;
    @BindView(R.id.rlExamTimeCountdown)
    RelativeLayout rlExamTimeCountdown;
    @BindView(R.id.rlQxmSetInfo)
    RelativeLayout rlQxmSetInfo;
    @BindView(R.id.tvTimeRemaining)
    AppCompatTextView tvTimeRemaining;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvPercentage)
    AppCompatTextView tvPercentage;
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
    @BindView(R.id.rvParticipateQxm)
    RecyclerView rvParticipateQxm;
    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;
    @BindView(R.id.cvSubmitAnswer)
    CardView cvSubmitAnswer;
    @BindView(R.id.nsvQuestionHolder)
    NestedScrollView nsvQuestionHolder;


    // endregion

    // region Fragment-Properties
    @BindView(R.id.btnSubmitAnswer)
    AppCompatButton btnSubmitAnswer;
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    Realm realm;
    String viewFor;
    CountDownTimer timer;
    private RealmService realmService;
    private QxmApiService apiService;
    private Context context;
    private QxmModel qxmModel;
    private int i = 0;
    private int progress = 0;
    private long examStartTime;
    private double totalPoints;

    // endregion

    // region Fragment-constructor

    public ParticipateQxmFragment() {
    }

    // endregion

    // region ParticipateQxmFragment-newInstance

    public static ParticipateQxmFragment newInstance(QxmModel qxmModel, String viewFor) {

        Bundle args = new Bundle();
        args.putParcelable(QXM_MODEL_KEY, qxmModel);
        args.putString(VIEW_FOR_KEY, viewFor);

        ParticipateQxmFragment fragment = new ParticipateQxmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-onCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_participate_qxm, container, false);

        ButterKnife.bind(this, view);

        context = view.getContext();

        setUpDagger2(context);

        return view;
    }

    // endregion

    // region Override-onViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qxmModel = getArguments() != null ? getArguments().getParcelable(QXM_MODEL_KEY) : null;
        viewFor = getArguments() != null ? getArguments().getString(VIEW_FOR_KEY) : null;

        if (qxmModel != null && viewFor != null) {
            Log.d(TAG, "onViewCreated: " + qxmModel.toString());
            init();
            initAdapter();
        } else {
            Log.d(TAG, "onViewCreated: qxmModel.getQuestionSet() & qxmModel.getQuestionSettings() must not be null.");
        }

        processInitialAnswersAndSubmit();
    }
    // endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {
        ParticipateQxmFragmentComponent participateQxmFragmentComponent =
                DaggerParticipateQxmFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        participateQxmFragmentComponent.injectParticipateQxmFragment(ParticipateQxmFragment.this);
    }
    // endregion

    // region Init
    private void init() {
        hideBottomNavigationView();

        if (viewFor.equals(VIEW_FOR_SEE_QUESTION)) {
            setFragmentViewAsSeeQuestionPerspective();
        } else {
            setFragmentViewAsParticipateQxmPerspective();
        }

        apiService = retrofit.create(QxmApiService.class);
        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);

        setTotalTimeInView();

        setTotalPointsInView();

        tvQxmTitle.setText(qxmModel.getQuestionSet().getQuestionSetTitle());
        if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetDescription())){
            tvQxmDescription.setText(qxmModel.getQuestionSet().getQuestionSetDescription());
            SetlinkClickable.setLinkclickEvent(tvQxmDescription, new HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, url);
                    context.startActivity(intent);
                }
            });
        }

        else
            tvQxmDescription.setVisibility(View.GONE);

        loadQuestionSetThumbnail();

        btnSubmitAnswer.setOnClickListener(v -> {

            btnSubmitAnswer.setEnabled(false);


            if (validateAnswerSheet()) {
                if (timer != null)
                    timer.cancel();
                processAnswersAndSubmit();
            } else {
                btnSubmitAnswer.setEnabled(true);
                Toast.makeText(context, R.string.please_answer_required_question_message, Toast.LENGTH_SHORT).show();
            }


        });

        if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetYoutubeLink())) {
            ivYoutubeButton.setVisibility(View.VISIBLE);
        } else {
            ivYoutubeButton.setVisibility(View.GONE);
        }

        ivQuestionImage.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetYoutubeLink())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, qxmModel.getQuestionSet().getQuestionSetYoutubeLink());
                context.startActivity(intent);
            }
        });
    }

    private boolean validateAnswerSheet() {

        for (Question question : qxmModel.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (question.getMultipleChoice().getRequired() && question.getMultipleChoice().getParticipantsAnswers().size() == 0)
                        return false;

                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (question.getFillInTheBlanks().getRequired() && question.getFillInTheBlanks().getParticipantsAnswers().size() == 0)
                        return false;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (question.getShortAnswer().getRequired() && TextUtils.isEmpty(question.getShortAnswer().getParticipantsAnswer()))
                        return false;
            }
        }

        return true;
    }

    // endregion

    // region HideBottomNavigationView

    private void hideBottomNavigationView() {
        if (getActivity() != null)
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
    }

    // endregion

    // region SetFragmentAsSeeQuestionPerspective

    private void setFragmentViewAsSeeQuestionPerspective() {
        rlExamTimeCountdown.setVisibility(View.GONE);
        cvSubmitAnswer.setVisibility(View.GONE);
        btnSubmitAnswer.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        appbarLayout.setVisibility(View.VISIBLE);

        ivBackArrow.setOnClickListener(v -> {
            Log.d(TAG, "Back arrow pressed: ");
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
    }

    // endregion

    // region setFragmentViewAsParticipateQxmPerspective

    private void setFragmentViewAsParticipateQxmPerspective() {
        toolbar.setVisibility(View.GONE);
        appbarLayout.setVisibility(View.GONE);
        examStartTime = System.currentTimeMillis();

        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT);

        layoutParams.setMargins(0, QxmdpHelper.getDp(context, 20), 0, 0);


        nsvQuestionHolder.setLayoutParams(layoutParams);

    }

    // endregion

    // region SetTotalTimeInView

    private void setTotalTimeInView() {
        String regex = "[0-9]+";

        if (!TextUtils.isEmpty(qxmModel.getQuestionSettings().getExamDuration())
                && !qxmModel.getQuestionSettings().getExamDuration().equals(QXM_TIME_TYPE_NO_LIMIT)) {

            if (qxmModel.getQuestionSettings().getExamDuration().matches(regex)) {
                long examDuration = Long.parseLong(qxmModel.getQuestionSettings().getExamDuration());

                if (viewFor.equals(VIEW_FOR_PARTICIPATE_QXM))
                    startCountdown(examDuration);

                tvTotalTime.setText(String.format("Total Time: %s",
                        TimeFormatString.getDurationInHourMinSec(examDuration)));
                rlExamTimeCountdown.setVisibility(View.VISIBLE);
            }

        } else {
            rlExamTimeCountdown.setVisibility(View.GONE);
            tvTotalTime.setText(getString(R.string.total_time_unlimited));
        }
    }

    // endregion

    // region setTotalPoints

    private void setTotalPointsInView() {

        /*double totalPoints = 0;

        for (Question question : qxmModel.getQuestionSet().getQuestions()) {
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
        }*/

        totalPoints = Double.parseDouble(QxmTotalPointsCalculator.getTotalPoints(qxmModel.getQuestionSet().getQuestions()));
        tvTotalPoints.setText(String.format("Total Points: %s", totalPoints));
    }

    // endregion

    // region LoadQuestionSetThumbnail

    private void loadQuestionSetThumbnail() {
        if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetThumbnail())) {
            picasso.load(qxmModel.getQuestionSet().getQuestionSetThumbnail()).into(ivQuestionImage);
            ivQuestionImage.setVisibility(View.VISIBLE);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }
    }

    // endregion

    // region ProcessAnswersAndSubmit

    private void processAnswersAndSubmit() {
        initializeUserPerformanceInQuestionSet();
        saveTimeTakenByParticipantsToAnswer();
        saveParticipationDate();
        saveTotalAnsweredPoints();
        saveNegativeMarksIfExists();
        if (qxmModel.getQuestionSettings().getEvaluationType().equals(StaticValues.EVALUATE_AUTOMATIC)) {
            examineUserAnswers();
            calculateAndSaveResultSummary();
        }
        submitAnswerToBackend();
    }

    private void processInitialAnswersAndSubmit() {
        initializeUserPerformanceInQuestionSet();
        saveTimeTakenByParticipantsToAnswer();
        saveParticipationDate();
        saveTotalAnsweredPoints();
        saveNegativeMarksIfExists();
        if (qxmModel.getQuestionSettings().getEvaluationType().equals(StaticValues.EVALUATE_AUTOMATIC)) {
            examineUserAnswers();
            calculateAndSaveResultSummary();
        }
        submitInitialAnswerToBackend();
    }

    private void saveNegativeMarksIfExists() {

        String negativePointInString = String.valueOf(qxmModel.getQuestionSettings().getNegativePoints());
        qxmModel.getUserPerformance().setNegativePoints(negativePointInString);
    }

    private void examineUserAnswers() {
        for (Question question : qxmModel.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:

                    if (question.getMultipleChoice().getParticipantsAnswers().containsAll(question.getMultipleChoice().getCorrectAnswers())
                            && question.getMultipleChoice().getParticipantsAnswers().size() == question.getMultipleChoice().getCorrectAnswers().size())
                        question.getMultipleChoice().setCorrect(true);
                    else
                        question.getMultipleChoice().setCorrect(false);

                    break;

                case QUESTION_TYPE_FILL_IN_THE_BLANKS:

                    ArrayList<String> participantsAnswers = new ArrayList<>();
                    ArrayList<String> correctAnswers = new ArrayList<>();

                    for (String participantsAns : question.getFillInTheBlanks().getParticipantsAnswers()) {
                        participantsAnswers.add(participantsAns.toLowerCase());
                    }

                    for (String correctAns : question.getFillInTheBlanks().getCorrectAnswers()) {
                        correctAnswers.add(correctAns.toLowerCase());
                    }

                    if (participantsAnswers.containsAll(correctAnswers))
                        question.getFillInTheBlanks().setCorrect(true);
                    else
                        question.getMultipleChoice().setCorrect(false);

                    break;

            }
        }
    }

    // endregion

    // region CreateUserPerformanceInQuestionSet

    private void initializeUserPerformanceInQuestionSet() {
        if (qxmModel.getUserPerformance() == null) {
            qxmModel.setUserPerformance(new UserPerformance());
        }
    }

    // endregion

    // region SaveTimeTakenByParticipantsToAnswer

    private void saveTimeTakenByParticipantsToAnswer() {
        long timeTakenToAnswer = System.currentTimeMillis() - examStartTime;
        qxmModel.getUserPerformance().setTimeTakenToAnswer(String.valueOf(timeTakenToAnswer));
        qxmModel.setTimeTaken(timeTakenToAnswer);
        Log.d(TAG, "saveTimeTakenByParticipantsToAnswer: " + TimeFormatString.getDurationInHourMinSec(timeTakenToAnswer));
    }

    // endregion

    // region SaveParticipationDate

    private void saveParticipationDate() {
        qxmModel.getUserPerformance()
                .setParticipatedDate(
                        String.valueOf(Calendar.getInstance().getTimeInMillis())
                );

        Log.d(TAG, "savedParticipationDate: " + TimeFormatString.getTimeAndDate(Long.parseLong(
                qxmModel.getUserPerformance().getParticipatedDate()
        )));
    }

    // endregion

    // region SaveTotalAnsweredPoints

    private void saveTotalAnsweredPoints() {
        double totalAnsweredPoints = 0;

        for (Question question : qxmModel.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (question.getMultipleChoice().getParticipantsAnswers().size() > 0)
                        if (!TextUtils.isEmpty(question.getMultipleChoice().getPoints()))
                            totalAnsweredPoints += Double.parseDouble(question.getMultipleChoice().getPoints());
                        else
                            Log.d(TAG, "saveTotalAnsweredPoints: points is empty or null");
                    break;

                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (question.getFillInTheBlanks().getParticipantsAnswers().size() > 0) {
                        totalAnsweredPoints += Double.parseDouble(question.getFillInTheBlanks().getPoints());
                    }
                    break;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (!TextUtils.isEmpty(question.getShortAnswer().getPoints()))
                        totalAnsweredPoints += Double.parseDouble(question.getShortAnswer().getPoints());
                    break;
            }
        }

        Log.d(TAG, "saveTotalAnsweredPoints: " + totalAnsweredPoints);
        qxmModel.getUserPerformance().

                setAnsweredPoints(String.valueOf(totalAnsweredPoints));
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

        for (Question question : qxmModel.getQuestionSet().getQuestions()) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (!TextUtils.isEmpty(question.getMultipleChoice().getPoints()) && question.getMultipleChoice().isCorrect()) {
                        correctMCQ++;
                        achievedPoints += Double.parseDouble(question.getMultipleChoice().getPoints());
                    }
                    totalMCQ++;

                    break;
                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (!TextUtils.isEmpty(question.getFillInTheBlanks().getPoints()) && question.getFillInTheBlanks().isCorrect()) {
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

        if (qxmModel.getQuestionSettings().getNegativePoints() > 0.0f) {
            double negativePoint = qxmModel.getQuestionSettings().getNegativePoints(); // ForEachQuestion
            double totalNegativePoints = (totalMCQ - correctMCQ) * negativePoint;
            totalNegativePoints += ((totalFillInTheBlanks - correctFillInTheBlanks) * negativePoint);

            achievedPoints -= totalNegativePoints;
        }

        Log.d(TAG, "AchievedPoints: " + achievedPoints);
        qxmModel.setMarks(String.valueOf(achievedPoints));
        qxmModel.getUserPerformance().setAchievePoints(String.format("%s out of %s", achievedPoints, totalPoints));

        if (achievedPoints > 0.0f) {

            qxmModel.getUserPerformance().setAchievePointsInPercentage(
                    String.format(Locale.ENGLISH, "%.2f%%", ((achievedPoints / totalPoints) * 100))
            );
        } else {
            qxmModel.getUserPerformance().setAchievePointsInPercentage(getString(R.string.zero_percentage));
        }


        Log.d(TAG, String.format("MultipleChoiceQuestion: %s out of %s", correctMCQ, totalMCQ));
        qxmModel.getUserPerformance().setTotalCorrectMCQ(String.format("%s out of %s", correctMCQ, totalMCQ));

        Log.d(TAG, String.format("FillInTheBlanks: %s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));
        qxmModel.getUserPerformance().setTotalCorrectFillInTheBlanks(String.format("%s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));

        Log.d(TAG, String.format("ShortAnswer: %s out of %s", correctShortAnswer, totalShortAnswer));
        qxmModel.getUserPerformance().setTotalCorrectShortAnswer(String.format("%s out of %s", correctShortAnswer, totalShortAnswer));


    }

    // endregion

    // region SubmitAnswerToBackend
    private void submitAnswerToBackend() {

        QxmProgressDialog dialog = new QxmProgressDialog(context);

        dialog.showProgressDialog("Submit Answer", "Your answers are being submitted.", false);

        QxmToken token = realmService.getApiToken();

        for (Question question : qxmModel.getQuestionSet().getQuestions()) {

            Log.d(TAG, "submitAnswerToBackend: " + question.getMultipleChoice().getCorrectAnswers().toString());
        }

        Gson gson = new Gson();

        Log.d(TAG, "submitAnswerToBackend: userId: " + token.getUserId() + ", qxmId: " + qxmModel.getId());
        Log.d(TAG, "submitAnswerToBackend: " + gson.toJson(qxmModel));

        Call<QxmApiResponse> submitAnswer = apiService.submitAnswer(token.getToken(), token.getUserId(), qxmModel.getId(), qxmModel);

        submitAnswer.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                dialog.closeProgressDialog();

                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: SubmitAnswerNetworkCall -> Success");

                    StaticValues.isHomeFeedRefreshingNeeded = true;
                    StaticValues.isUserProfileFragmentRefreshNeeded = true;

                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(context, "Your answer is submitted successfully.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);

                } else {
                    btnSubmitAnswer.setEnabled(true);
                    Log.d(TAG, "onResponse: SubmitAnswerNetworkCall -> failed");
                    Toast.makeText(context, "Your answer submission failed. Please, try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: SubmitAnswerNetworkCall");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                btnSubmitAnswer.setEnabled(true);
                dialog.closeProgressDialog();
                Toast.makeText(context, "Your answer submission failed. Please, try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region initial answer submit
    private void submitInitialAnswerToBackend() {

        QxmToken token = realmService.getApiToken();
        Call<QxmApiResponse> initialQxmParticipation = apiService.initialQxmParticipation(token.getToken(), token.getUserId(), qxmModel.getId(), qxmModel);

        initialQxmParticipation.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: initial participation success");

                    // StaticValues.isHomeFeedRefreshingNeeded = true;
                    // StaticValues.isUserProfileFragmentRefreshNeeded = true;

                    // if (getActivity() != null)
                    //     getActivity().getSupportFragmentManager().popBackStack();
                    // Toast.makeText(context, "Your answer is submitted successfully.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);

                } else {
                    btnSubmitAnswer.setEnabled(true);
                    Log.d(TAG, "onResponse: initial participation -> failed");
                    // Toast.makeText(context, "Your answer submission failed. Please, try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: SubmitAnswerNetworkCall");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                btnSubmitAnswer.setEnabled(true);

            }
        });
    }
    //endregion

    // region InitAdapter

    private void initAdapter() {
        ExamQuestionSheetAdapter examQuestionSheetAdapter = new ExamQuestionSheetAdapter(context, picasso, qxmModel.getQuestionSet().getQuestions(), viewFor);
        rvParticipateQxm.setLayoutManager(new LinearLayoutManager(context));
        rvParticipateQxm.setAdapter(examQuestionSheetAdapter);
        rvParticipateQxm.addItemDecoration(new RecyclerViewItemDecoration(5, 5, 5, 5));
        rvParticipateQxm.setNestedScrollingEnabled(false);

        /*if (viewFor.equals(VIEW_FOR_PARTICIPATE_QXM)) {
            rvParticipateQxm.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                Log.d(TAG, String.format("onScrolled: dx: %d, dy: %d", dx, dy) );
                    if (dy > 0) {
                        btnSubmitAnswer.setVisibility(View.GONE);
                    } else {
                        btnSubmitAnswer.setVisibility(View.VISIBLE);
                    }
                }
            });

        }*/


    }

    // endregion

    // region StartExamTimeCountdown

    private void startCountdown(long remainingTime) {
        timer = new CountDownTimer(remainingTime, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                String strDays, strHours, strMin, strSec;
                String time;

                int sec = (int) seconds % 60;
                int min = (int) minutes % 60;
                int hour = (int) hours % 24;
                int day = (int) days;

                if (sec < 10) strSec = "0" + sec;
                else strSec = "" + sec;

                if (min < 10) strMin = "0" + min;
                else strMin = "" + min;

                if (hour < 10) strHours = "0" + hour;
                else strHours = "" + hour;

                if (day < 10) strDays = "0" + day;
                else strDays = "" + day;


                if (day != 0) {
                    time = strDays + "d " + strHours + "h " + strMin + "m " + strSec + "s";
                } else if (hour != 0) {
                    time = strHours + "hr " + strMin + "min " + strSec + "sec";
                } else if (min != 0) {
                    time = strMin + "min " + strSec + "sec";
                } else {
                    time = strSec + "sec";
                }

                tvTimeRemaining.setText(time);
                progress = (int) (i * 100 / (remainingTime / 1000));
                tvPercentage.setText(String.format("%s%%", progress));
                progressBar.setProgress(progress);
                i++;
            }

            public void onFinish() {
                tvTimeRemaining.setText(context.getString(R.string.time_is_up));
                tvPercentage.setText(context.getString(R.string.percentage_100));
                progressBar.setProgress(100);
                processAnswersAndSubmit();
            }
        }.start();
    }

    // endregion


    //region Overrided method onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }

    }
    //endregion
}