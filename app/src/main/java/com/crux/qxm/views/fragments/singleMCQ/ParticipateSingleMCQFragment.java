package com.crux.qxm.views.fragments.singleMCQ;

import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.participationSingleMCQ.SingleMCQExamQuestionSheetAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.singleMCQ.ParticipateSingleMCQModel;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.participateSingleMCQFragmentFeature.DaggerParticipateSingleMCQFragmentComponent;
import com.crux.qxm.di.participateSingleMCQFragmentFeature.ParticipateSingleMCQFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.RecyclerViewItemDecoration;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
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
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_ID_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;
import static com.crux.qxm.utils.StaticValues.isFeedSingleMCQItemClicked;

public class ParticipateSingleMCQFragment extends Fragment {


    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "ParticipateQxmFragment";
    private String singleMCQId;
    private Realm realm;
    private String viewFor;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private Context context;
    private SingleMCQModel singleMCQModel;
    private int i = 0;
    private long examStartTime;
    private double totalPoints;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region BindViewsWithButterKnife

    // region SeeQuestion

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    // endregion
    @BindView(R.id.tvToolbarTitle)
    AppCompatTextView tvToolbarTitle;

    @BindView(R.id.rlQxmSetInfo)
    RelativeLayout rlQxmSetInfo;

    @BindView(R.id.tvQxmTitle)
    AppCompatTextView tvQxmTitle;
    @BindView(R.id.ivQuestionImage)
    AppCompatImageView ivQuestionImage;
    @BindView(R.id.tvQxmDescription)
    AppCompatTextView tvQxmDescription;
    @BindView(R.id.rvParticipateQxm)
    RecyclerView rvParticipateQxm;
    @BindView(R.id.btnSubmitAnswer)
    AppCompatButton btnSubmitAnswer;
    @BindView(R.id.ivYoutubeButton)
    AppCompatImageView ivYoutubeButton;

    // endregion

    // region Fragment-constructor

    public ParticipateSingleMCQFragment() {
    }

    // endregion

    // region ParticipateQxmFragment-newInstance

    public static ParticipateSingleMCQFragment newInstance(SingleMCQModel singleMCQModel, String viewFor) {

        Bundle args = new Bundle();
        args.putParcelable(QXM_MODEL_KEY, singleMCQModel);
        args.putString(VIEW_FOR_KEY, viewFor);

        ParticipateSingleMCQFragment fragment = new ParticipateSingleMCQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ParticipateSingleMCQFragment newInstance(String singleMCQId, String viewFor) {

        Bundle args = new Bundle();
        args.putString(SINGLE_MCQ_ID_KEY, singleMCQId);
        args.putString(VIEW_FOR_KEY, viewFor);

        ParticipateSingleMCQFragment fragment = new ParticipateSingleMCQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // endregion

    // region Override-onCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_mcq_participate_qxm, container, false);

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
        singleMCQModel = getArguments() != null ? getArguments().getParcelable(QXM_MODEL_KEY) : null;
        singleMCQId = getArguments() != null ? getArguments().getString(SINGLE_MCQ_ID_KEY) : null;
        viewFor = getArguments() != null ? getArguments().getString(VIEW_FOR_KEY) : null;

        init();

        setViewFor();

        if (singleMCQModel != null && viewFor != null) {
            Log.d(TAG, "onViewCreated: " + singleMCQModel.toString());
            setDataIntoViews();
            initAdapter();
        } else {
            if(token != null){
                Log.d(TAG, "onViewCreated: loading...");
                getSingleMCQNetworkCall(token.getToken(), singleMCQId);
            }
            else{
                Log.d(TAG, "onViewCreated: user not logged in");
                Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                qxmFragmentTransactionHelper.logout();
            }
            Log.d(TAG, "onViewCreated: singleMCQModel.getQuestionSet() & singleMCQModel.getQuestionSettings() must not be null.");
        }

    }

    private void setViewFor() {

        if (viewFor.equals(VIEW_FOR_SEE_QUESTION)) {
            setFragmentViewAsSeeQuestionPerspective();
        } else {
            setFragmentViewAsParticipateQxmPerspective();
        }

    }

    private void getSingleMCQNetworkCall(String token, String singleMCQId) {
        Call<SingleMCQModel> getSingleMCQModel = apiService.getSingleMCQModel(token, singleMCQId);

        getSingleMCQModel.enqueue(new Callback<SingleMCQModel>() {
            @Override
            public void onResponse(@NonNull Call<SingleMCQModel> call, @NonNull Response<SingleMCQModel> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200) {
                    singleMCQModel = response.body();
                    if (response.body() != null) {
                        totalPoints = Double.parseDouble(response.body().getMultipleChoice().getPoints());
                    }else{
                        totalPoints = 5.0f;
                    }
                    setDataIntoViews();
                    initAdapter();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: getSingleMCQ Network Call failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SingleMCQModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getSingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {
        ParticipateSingleMCQFragmentComponent participateSingleMCQFragmentComponent =
                DaggerParticipateSingleMCQFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        participateSingleMCQFragmentComponent.injectParticipateSingleMCQFragment(ParticipateSingleMCQFragment.this);
    }
    // endregion

    // region Init
    private void init() {



        apiService = retrofit.create(QxmApiService.class);
        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        ivBackArrow.setOnClickListener(v -> {
            Log.d(TAG, "Back arrow pressed: ");
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        btnSubmitAnswer.setOnClickListener(v -> processAnswersAndSubmit());
    }

    // endregion


    // region SetFragmentAsSeeQuestionPerspective

    private void setFragmentViewAsSeeQuestionPerspective() {
        btnSubmitAnswer.setVisibility(View.GONE);

        tvToolbarTitle.setText(R.string.single_mcq_overview);

        RelativeLayout.LayoutParams layoutParamsForTime = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsForTime.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        layoutParamsForTime.setMargins(0, 60, 0, 0);

        RelativeLayout.LayoutParams layoutParamsForPoints = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsForPoints.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        layoutParamsForPoints.setMargins(0, 60, 0, 0);
    }

    // endregion

    // region setFragmentViewAsParticipateQxmPerspective

    private void setFragmentViewAsParticipateQxmPerspective() {
        examStartTime = System.currentTimeMillis();
    }

    // endregion


    // region LoadQuestionSetThumbnail

    private void setDataIntoViews() {
        tvQxmTitle.setText(singleMCQModel.getMultipleChoice().getQuestionTitle());
        tvQxmDescription.setText(singleMCQModel.getMultipleChoice().getDescription());
        SetlinkClickable.setLinkclickEvent(tvQxmDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(singleMCQModel.getMultipleChoice().getImage())) {
            picasso.load(singleMCQModel.getMultipleChoice().getImage()).into(ivQuestionImage);
            ivQuestionImage.setVisibility(View.VISIBLE);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(singleMCQModel.getMultipleChoice().getYoutubeVideoLink())){
            ivYoutubeButton.setVisibility(View.VISIBLE);
        }else {
            ivYoutubeButton.setVisibility(View.GONE);
        }

       ivQuestionImage.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(singleMCQModel.getMultipleChoice().getYoutubeVideoLink())) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY,singleMCQModel.getMultipleChoice().getYoutubeVideoLink());
                context.startActivity(intent);
            }
        });

    }

    // endregion

    // region ProcessAnswersAndSubmit

    private void processAnswersAndSubmit() {
        initializeUserPerformanceInQuestionSet();
        saveTimeTakenByParticipantsToAnswer();
        saveParticipationDate();
        saveTotalAnsweredPoints();

        examineUserAnswers();
        calculateAndSaveResultSummary();
        submitAnswerToBackend();
    }

    private void examineUserAnswers() {

        for (Question question : generateQuestionListFromSingleMCQ()) {
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
        if (singleMCQModel.getUserPerformance() == null) {
            singleMCQModel.setUserPerformance(new UserPerformance());
        }
    }

    // endregion

    // region SaveTimeTakenByParticipantsToAnswer

    private void saveTimeTakenByParticipantsToAnswer() {
        long timeTakenToAnswer = System.currentTimeMillis() - examStartTime;
        singleMCQModel.getUserPerformance().setTimeTakenToAnswer(String.valueOf(timeTakenToAnswer));
        Log.d(TAG, "saveTimeTakenByParticipantsToAnswer: " + TimeFormatString.getDurationInHourMinSec(timeTakenToAnswer));
    }

    // endregion

    // region SaveParticipationDate

    private void saveParticipationDate() {
        singleMCQModel.getUserPerformance()
                .setParticipatedDate(
                        String.valueOf(Calendar.getInstance().getTimeInMillis())
                );

        Log.d(TAG, "savedParticipationDate: " + TimeFormatString.getTimeAndDate(Long.parseLong(
                singleMCQModel.getUserPerformance().getParticipatedDate()
        )));
    }

    // endregion

    // region SaveTotalAnsweredPoints

    private void saveTotalAnsweredPoints() {
        double totalAnsweredPoints = 0;

        for (Question question : generateQuestionListFromSingleMCQ()) {
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
                        for (String participantsAnswer : question.getFillInTheBlanks().getParticipantsAnswers()) {
                            if (!TextUtils.isEmpty(participantsAnswer)) {
                                totalAnsweredPoints += Double.parseDouble(question.getFillInTheBlanks().getPoints());
                            }
                        }
                    }
                    break;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (!TextUtils.isEmpty(question.getShortAnswer().getPoints()))
                        totalAnsweredPoints += Double.parseDouble(question.getShortAnswer().getPoints());
                    break;
            }
        }

        Log.d(TAG, "saveTotalAnsweredPoints: " + totalAnsweredPoints);
        singleMCQModel.getUserPerformance().setAnsweredPoints(String.valueOf(totalAnsweredPoints));
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

        for (Question question : generateQuestionListFromSingleMCQ()) {
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

        Log.d(TAG, "AchievedPoints: " + achievedPoints);
        singleMCQModel.setMarks(String.valueOf(achievedPoints));
        singleMCQModel.getUserPerformance().setAchievePoints(String.format("%s out of %s", String.valueOf(achievedPoints), String.valueOf(totalPoints)));

        if(achievedPoints > 0.0f){

            singleMCQModel.getUserPerformance().setAchievePointsInPercentage(
                    String.format(Locale.ENGLISH, "%.2f%%", ((achievedPoints / totalPoints) * 100))
            );
        }else{
            singleMCQModel.getUserPerformance().setAchievePointsInPercentage(getString(R.string.zero_percentage));
        }


        Log.d(TAG, String.format("MultipleChoiceQuestion: %s out of %s", correctMCQ, totalMCQ));
        singleMCQModel.getUserPerformance().setTotalCorrectMCQ(String.format("%s out of %s", correctMCQ, totalMCQ));

        Log.d(TAG, String.format("FillInTheBlanks: %s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));
        singleMCQModel.getUserPerformance().setTotalCorrectFillInTheBlanks(String.format("%s out of %s", correctFillInTheBlanks, totalFillInTheBlanks));

        Log.d(TAG, String.format("ShortAnswer: %s out of %s", correctShortAnswer, totalShortAnswer));
        singleMCQModel.getUserPerformance().setTotalCorrectShortAnswer(String.format("%s out of %s", correctShortAnswer, totalShortAnswer));


    }

    // endregion

    // region SubmitAnswerToBackend
    private void submitAnswerToBackend() {

        QxmProgressDialog dialog = new QxmProgressDialog(context);

        dialog.showProgressDialog("Submit Answer", "Your answers are being submitted.", false);

        for (Question question : generateQuestionListFromSingleMCQ()) {

            Log.d(TAG, "submitAnswerToBackend: " + question.getMultipleChoice().getCorrectAnswers().toString());
        }

        ParticipateSingleMCQModel participateSingleMCQModel = new ParticipateSingleMCQModel();
        participateSingleMCQModel.setId(singleMCQModel.getId());
        participateSingleMCQModel.setMarks(singleMCQModel.getMarks());
        participateSingleMCQModel.setMultipleChoice(singleMCQModel.getMultipleChoice());
        participateSingleMCQModel.setSingleMCQSettings(singleMCQModel.getSingleMCQSettings());
        participateSingleMCQModel.setUserPerformance(singleMCQModel.getUserPerformance());

        Call<QxmApiResponse> submitAnswer = apiService.submitSingleMCQAnswer(token.getToken(), token.getUserId(), singleMCQModel.getId(), participateSingleMCQModel);

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

                    qxmFragmentTransactionHelper.loadFullResultSingleMCQFragment(singleMCQModel.getId());


                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);

                } else {
                    Log.d(TAG, "onResponse: SubmitAnswerNetworkCall -> failed");
                    Toast.makeText(context, "Your answer submission failed. Please, try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: SubmitAnswerNetworkCall");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                dialog.closeProgressDialog();
                Toast.makeText(context, "Your answer submission failed. Please, try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    // region InitAdapter

    private void initAdapter() {

        SingleMCQExamQuestionSheetAdapter examQuestionSheetAdapter = new SingleMCQExamQuestionSheetAdapter(context, picasso, generateQuestionListFromSingleMCQ(), viewFor);
        rvParticipateQxm.setLayoutManager(new LinearLayoutManager(context));
        rvParticipateQxm.setAdapter(examQuestionSheetAdapter);
        rvParticipateQxm.addItemDecoration(new RecyclerViewItemDecoration(5, 5, 5, 5));
        rvParticipateQxm.setNestedScrollingEnabled(false);

    }

    // endregion


    //region generate question

    private List<Question> generateQuestionListFromSingleMCQ() {

        List<Question> questionList = new ArrayList<>();
        Question question = new Question();
        question.setQuestionType(QUESTION_TYPE_MULTIPLE_CHOICE);
        question.setMultipleChoice(singleMCQModel.getMultipleChoice());
        questionList.add(question);
        return questionList;
    }

    //end region

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }

        // The following boolean flag variable is used to
        // restrict from occurring multiple Single MCQ touch events (for solving Fragment Overlay)
        isFeedSingleMCQItemClicked = false;
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