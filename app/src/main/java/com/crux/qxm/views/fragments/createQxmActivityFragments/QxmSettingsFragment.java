package com.crux.qxm.views.fragments.createQxmActivityFragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.QxmCategoryAdapter;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.crux.qxm.db.models.questions.QuestionSettings;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.db.sampleModels.QxmSettingsModel;
import com.crux.qxm.di.qxmSettingsFragmentFeature.DaggerQxmSettingsFragmentComponent;
import com.crux.qxm.di.qxmSettingsFragmentFeature.QxmSettingsFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.OnBackPressed;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmCategoryHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.CONTEST_END_CHOOSE_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.CONTEST_END_WITH_EXAM_DURATION;
import static com.crux.qxm.utils.StaticValues.CONTEST_START_CHOOSE_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.CONTEST_START_INSTANTLY;
import static com.crux.qxm.utils.StaticValues.CONTEST_START_MANUAL;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.EVALUATE_AUTOMATIC;
import static com.crux.qxm.utils.StaticValues.EVALUATE_MANUAL;
import static com.crux.qxm.utils.StaticValues.EVALUATE_SEMI_AUTO;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PARTICIPANTS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_BY_DATE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SET;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SETTINGS;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_DRAFTED;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_PUBLISHED;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_AFTER_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_BEFORE_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_NOT_STARTED;
import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_EXPIRATION_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_START_INSTANTLY;
import static com.crux.qxm.utils.StaticValues.QXM_START_MANUAL;
import static com.crux.qxm.utils.StaticValues.QXM_START_SCHEDULE_CHOOSE_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.QXM_TIME_TYPE_NO_LIMIT;

/**
 * A simple {@link Fragment} subclass.
 */
public class QxmSettingsFragment extends Fragment implements QxmCategoryAdapter.CategorySelectListener, Animation.AnimationListener,
        OnBackPressed {

    private static final String TAG = "QxmSettingsFragment";

    @Inject
    Retrofit retrofit;
    private QxmApiService apiService;

    private RealmService realmService;

    private QuestionSet questionSet;
    private QuestionSettings questionSettings;
    private QxmToken token;
    private QxmDraft qxmDraft;

    private Context context;
    private QxmProgressDialog progressDialog;

    private boolean hasFillInTheBlanksOrShortAnswer = false;
    private double maxPoints = 0;

    private int categorySelected = 0;
    private int examHour;
    private int examMin;
    private int examSec;

    private String dateString, amPM;
    private Date chooseDate;

    private ArrayAdapter<String> priorityOneAdapter;
    private ArrayAdapter<String> priorityTwoAdapter;
    private ArrayAdapter<String> priorityThreeAdapter;

    // Animation
    private Animation animSlideUp;
    private Animation animSideDown;

    // region BindViewsWithButterKnife

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;

    @BindView(R.id.rvQxmCategory)
    RecyclerView rvQxmCategory;

    @BindView(R.id.spinnerExamTimeType)
    AppCompatSpinner spinnerExamTimeType;

    @BindView(R.id.view_examTimeTop)
    View view_examTimeTop;
    @BindView(R.id.view_examTimeBottom)
    View getView_examTimeBottom;

    @BindView(R.id.llExamTimeSelector)
    LinearLayoutCompat llExamTimeSelector;
    @BindView(R.id.llExamTimeValue)
    LinearLayoutCompat llExamTimeValue;


    @BindView(R.id.spinnerTimeHour)
    AppCompatSpinner spinnerExamTimeHour;

    @BindView(R.id.spinnerTimeMin)
    AppCompatSpinner spinnerExamTimeMin;

    @BindView(R.id.spinnerTimeSeconds)
    AppCompatSpinner spinnerExamTimeSec;

    @BindView(R.id.spinnerPrivacyType)
    AppCompatSpinner spinnerPrivacyType;

    @BindView(R.id.switchQxmEnroll)
    SwitchCompat switchQxmEnroll;

    @BindView(R.id.ivEnrollInfo)
    AppCompatImageView ivEnrollInfo;

    @BindView(R.id.tvEnrollHint)
    AppCompatTextView tvEnrollHint;

    @BindView(R.id.spinnerPriorityOne)
    AppCompatSpinner spinnerPriorityOne;

    @BindView(R.id.spinnerPriorityTwo)
    AppCompatSpinner spinnerPriorityTwo;

    @BindView(R.id.spinnerPriorityThree)
    AppCompatSpinner spinnerPriorityThree;

    @BindView(R.id.tvExamTime)
    AppCompatTextView tvExamTime;

    @BindView(R.id.rgQxmVisibility)
    RadioGroup rgQxmVisibility;

    @BindView(R.id.radioQxmVisibilityAfterParticipation)
    RadioButton radioQxmVisibilityAfterParticipation;
    @BindView(R.id.radioQxmVisibilityBeforeParticipation)
    RadioButton radioQxmVisibilityBeforeParticipation;

    @BindView(R.id.tvTitleQxmStartSchedule)
    AppCompatTextView tvTitleQxmStartSchedule;

    @BindView(R.id.rgQxmStartSchedule)
    RadioGroup rgQxmStartSchedule;

    @BindView(R.id.radioQxmStartInstantly)
    RadioButton radioQxmStartInstantly;
    @BindView(R.id.radioQxmStartChooseDate)
    RadioButton radioQxmStartChooseDate;
    @BindView(R.id.radioQxmStartManual)
    RadioButton radioQxmStartManual;

    @BindView(R.id.rgQxmEvaluation)
    RadioGroup rgQxmEvaluation;

    @BindView(R.id.radioQxmEvaluateAutomatic)
    RadioButton radioQxmEvaluateAutomatic;
    @BindView(R.id.radioQxmEvaluateManual)
    RadioButton radioQxmEvaluateManual;
    @BindView(R.id.radioQxmEvaluateSemiAutomatic)
    RadioButton radioQxmEvaluateSemiAuto;

    @BindView(R.id.tvEvaluationTypeAutomaticDisableTip)
    AppCompatTextView tvEvaluationTypeAutomaticDisableTip;

    @BindView(R.id.tvEvaluationTypeAutomaticTip)
    AppCompatTextView tvEvaluationTypeAutomaticTip;

    @BindView(R.id.switchQxmExpiration)
    SwitchCompat switchQxmExpiration;
    @BindView(R.id.tvLeaderBoardPublishQxmExpiresTips)
    AppCompatTextView tvLeaderBoardPublishQxmExpiresTips;

    @BindView(R.id.rlQxmExpiration)
    RelativeLayout rlQxmExpiration;

    @BindView(R.id.tvQxmExpirationTime)
    AppCompatTextView tvQxmExpirationTime;

    @BindView(R.id.checkboxNegativeMarking)
    AppCompatCheckBox checkboxNegativeMarking;

    @BindView(R.id.tvNegativeMarking)
    AppCompatTextView tvNegativeMarking;
    @BindView(R.id.etNegativeMarking)
    AppCompatEditText etNegativeMarking;

    @BindView(R.id.rlContestMode)
    RelativeLayout rlContestMode;
    @BindView(R.id.switchContestMode)
    SwitchCompat switchContestMode;
    @BindView(R.id.llContestModeSettings)
    LinearLayoutCompat llContestModeSettings;

    @BindView(R.id.etNumberOfWinners)
    AppCompatEditText etNumberOfWinners;

    @BindView(R.id.viewOverlay)
    View viewOverlay;

    @BindView(R.id.tvParticipationExpiration)
    AppCompatTextView tvParticipationExpiration;


    @BindView(R.id.rgContestStart)
    RadioGroup rgContestStart;

    @BindView(R.id.radioContestStartImmediately)
    RadioButton radioContestStartImmediately;
    @BindView(R.id.radioContestStartManual)
    RadioButton radioContestStartManual;
    @BindView(R.id.radioContestStartSpecificDate)
    RadioButton radioContestStartSpecificDate;


    @BindView(R.id.rgContestEndTime)
    RadioGroup rgContestEndTime;

    @BindView(R.id.radioContestEndWithExamEnds)
    RadioButton radioContestEndWithExamEnds;
    @BindView(R.id.radioContestEndSpecificDate)
    RadioButton radioContestEndSpecificDate;


    @BindView(R.id.rgParticipationExpiration)
    RadioGroup rgParticipationExpiration;

    @BindView(R.id.radioParticipationAllow)
    RadioButton radioParticipationAllow;
    @BindView(R.id.radioParticipationDeny)
    RadioButton radioParticipationDeny;


    @BindView(R.id.rgCorrectAnswerVisibility)
    RadioGroup rgCorrectAnswerVisibility;

    @BindView(R.id.radioCorrectAnsVisibleByDate)
    RadioButton radioCorrectAnsVisibleByDate;
    @BindView(R.id.radioCorrectAnsVisibleContestEnds)
    RadioButton radioCorrectAnsVisibleContestEnds;
    @BindView(R.id.radioCorrectAnsVisibleImmediately)
    RadioButton radioCorrectAnsVisibleImmediately;
    @BindView(R.id.radioCorrectAnsVisibleAfterQxmExpire)
    RadioButton radioCorrectAnsVisibleAfterQxmExpire;


    @BindView(R.id.rgLeaderBoardPublish)
    RadioGroup rgLeaderBoardPublish;

    @BindView(R.id.radioLeaderBoardPublishContinue)
    RadioButton radioLeaderBoardPublishContinue;
    @BindView(R.id.radioLeaderBoardPublishQxmExpires)
    RadioButton radioLeaderBoardPublishQxmExpires;
    @BindView(R.id.radioLeaderBoardPublishContestEnds)
    RadioButton radioLeaderBoardPublishContestEnds;
    @BindView(R.id.radioLeaderBoardPublishSpecificDate)
    RadioButton radioLeaderBoardPublishSpecificDate;


    @BindView(R.id.rgLeaderBoardPrivacy)
    RadioGroup rgLeaderBoardPrivacy;

    @BindView(R.id.radioLeaderBoardPrivacyParticipants)
    RadioButton radioLeaderBoardPrivacyParticipants;
    @BindView(R.id.radioLeaderBoardPrivacyPublic)
    RadioButton radioLeaderBoardPrivacyPublic;

    @BindView(R.id.btnSaveAsDraft)
    AppCompatButton btnSaveAsDraft;

    @BindView(R.id.btnQxmPublish)
    AppCompatButton btnQxmPublish;
    private String[] priorities;

    // endregion

    public QxmSettingsFragment() {
        // Required empty public constructor
    }

    // region Override - onCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qxm_settings, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override - onViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null && arguments.getParcelable(PARCELABLE_QUESTION_SET) != null) {
            questionSet = arguments.getParcelable(PARCELABLE_QUESTION_SET);

            if (arguments.getParcelable(PARCELABLE_QUESTION_SETTINGS) != null) {
                questionSettings = arguments.getParcelable(PARCELABLE_QUESTION_SETTINGS);
                qxmDraft = getArguments().getParcelable(QXM_DRAFT_KEY);
                if (questionSettings != null && qxmDraft != null) {
                    Log.d(TAG, "onViewCreated: QuestionSettings: " + questionSettings.toString());
                    Log.d(TAG, "onViewCreated: QxmDraftId: " + qxmDraft.toString());
                    categorySelected = questionSettings.getQuestionCategory().size();
                }
            }
        }

        if (questionSet != null)
            isShortAnswerAddedInQuestionSet(questionSet);

//        if (questionSet == null) Log.d(TAG, "onCreateView: questionSet is nul");
//        else Log.d(TAG, "onCreateView: questionSet: " + questionSet.toString());

        context = view.getContext();

        setUpDagger2(context);

        init(view);

        populateDataIntoRecyclerView();

        if (!TextUtils.isEmpty(questionSettings.getQuestionStatus()) && questionSettings.getQuestionStatus().equals(QUESTION_STATUS_DRAFTED))
            loadDataInViews();

        clickListeners();
    }

    private void isShortAnswerAddedInQuestionSet(QuestionSet questionSet) {


        if (questionSet.getQuestions() != null && questionSet.getQuestions().size() > 0) {

            double points;
            for (Question question : questionSet.getQuestions()) {
                switch (question.getQuestionType()) {
                    case QUESTION_TYPE_SHORT_ANSWER:
                        hasFillInTheBlanksOrShortAnswer = true;
                        points = Double.parseDouble(question.getShortAnswer().getPoints());
                        if (maxPoints < points) maxPoints = points;

                        break;
                    case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                        hasFillInTheBlanksOrShortAnswer = true;
                        points = Double.parseDouble(question.getFillInTheBlanks().getPoints());
                        if (maxPoints < points) maxPoints = points;

                        break;
                    case QUESTION_TYPE_MULTIPLE_CHOICE:
                        points = Double.parseDouble(question.getMultipleChoice().getPoints());
                        if (maxPoints < points) maxPoints = points;
                        break;
                }

            }

        }

    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        QxmSettingsFragmentComponent qxmSettingsFragmentComponent =
                DaggerQxmSettingsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        qxmSettingsFragmentComponent.injectQxmSettingsFragmentFeature(QxmSettingsFragment.this);
    }

    // endregion

    // region Init

    private void init(View view) {

        progressDialog = new QxmProgressDialog(context);

        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());

        token = realmService.getApiToken();

        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplicationContext(),
                R.anim.slide_up);
        animSideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down);

        // set animation listener
        animSlideUp.setAnimationListener(this);
        animSideDown.setAnimationListener(this);


        // Get Default QxmAppNotificationSettings
        if (questionSettings == null)
            questionSettings = QxmSettingsModel.getDefaultQuestionSettings();

        //region Changed Settings based on the FillInTheBlanks and ShortAnswer existence in Question Set
        if (hasFillInTheBlanksOrShortAnswer) {
            questionSettings.setEvaluationType(EVALUATE_SEMI_AUTO);
            radioQxmEvaluateSemiAuto.setChecked(true);
            radioQxmEvaluateAutomatic.setEnabled(false);
            radioQxmEvaluateAutomatic.setAlpha(0.4f);
            tvEvaluationTypeAutomaticDisableTip.setVisibility(View.VISIBLE);
            tvEvaluationTypeAutomaticTip.setVisibility(View.GONE);

            radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioCorrectAnsVisibleImmediately.setText(R.string.show_result_after_evaluation);
            radioCorrectAnsVisibleImmediately.setChecked(true);
            radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));


        } else {
            radioQxmEvaluateAutomatic.setEnabled(true);
            radioQxmEvaluateAutomatic.setAlpha(1.0f);
            tvEvaluationTypeAutomaticDisableTip.setVisibility(View.GONE);
            tvEvaluationTypeAutomaticTip.setVisibility(View.VISIBLE);

        }
        //endregion

        Log.d(TAG, "init: switchQxmExpiration: " + switchQxmExpiration.isChecked());
        if (switchQxmExpiration.isChecked()) {
            Log.d(TAG, "init: switchQxmExpiration =  true ");
            radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.VISIBLE);
            radioLeaderBoardPublishQxmExpires.setVisibility(View.VISIBLE);
            radioLeaderBoardPublishContestEnds.setVisibility(View.VISIBLE);

        } else {
            Log.d(TAG, "init: switchQxmExpiration =  false ");
            radioCorrectAnsVisibleImmediately.setChecked(true);
            radioLeaderBoardPublishContestEnds.setVisibility(View.GONE);
            radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.GONE);
            radioLeaderBoardPublishQxmExpires.setVisibility(View.GONE);
        }

        viewOverlay.bringToFront();

        disableEnableControls(questionSettings.isContestModeEnabled(), llContestModeSettings);

        // region ExamTimeType
        ArrayAdapter<CharSequence> examTimeTypeAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.exam_time_type_list, android.R.layout.simple_spinner_item);
        examTimeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamTimeType.setAdapter(examTimeTypeAdapter);

        // draft - for prevent showing time picker dialog.
        if (questionSettings != null) {
            if (!questionSettings.getExamDuration().equals(QXM_TIME_TYPE_NO_LIMIT)) {
                spinnerExamTimeType.setSelection(1);
            }
        }

        // endregion

        // region ExamTime

        //ArrayList for ExamTime (Hour, Min+sec)
        ArrayList<String> examTimeHourList = new ArrayList<>();
        ArrayList<String> examTimeMinList = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            examTimeMinList.add(String.valueOf(i));
            if (i <= 12) examTimeHourList.add(String.valueOf(i));
        }

        //Hour Spinner
        ArrayAdapter<String> examTimeHourAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, examTimeHourList);


        examTimeHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamTimeHour.setAdapter(examTimeHourAdapter);

        //Minute Spinner
        ArrayAdapter<String> examTimeMinAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, examTimeMinList);
        examTimeMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamTimeMin.setAdapter(examTimeMinAdapter);

        //Seconds Spinner
        ArrayAdapter<String> examTimeSecAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, examTimeMinList);
        examTimeSecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamTimeSec.setAdapter(examTimeSecAdapter);


        spinnerExamTimeHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                examHour = Integer.parseInt(spinnerExamTimeHour.getSelectedItem().toString());

                String examTime = examHour + " hours "
                        + examMin + " minutes " + examSec + " seconds";

                tvExamTime.setText(examTime);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerExamTimeMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                examMin = Integer.parseInt(spinnerExamTimeMin.getSelectedItem().toString());

                String examTime = examHour + " hours "
                        + examMin + " minutes " + examSec + " seconds";

                tvExamTime.setText(examTime);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerExamTimeSec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                examSec = Integer.parseInt(spinnerExamTimeSec.getSelectedItem().toString());

                String examTime = examHour + " hours "
                        + examMin + " minutes " + examSec + " seconds";

                tvExamTime.setText(examTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // endregion

        // region SetTimeWithNumberPicker

        tvExamTime.setOnClickListener(v -> showExamTimePicker(v.getRootView(), v.getContext()));

        spinnerExamTimeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {


                    questionSettings.setExamDuration(QXM_TIME_TYPE_NO_LIMIT);

                    hideExamTimeViews();

                } else if (position == 1) {


//                    questionSettings.setExamDuration(QXM_TIME_TYPE_LIMITED);

                    showExamTimeViews();

                    showExamTimePicker(view.getRootView(), parent.getContext());
                }
                Log.d(TAG, "ExamTimeType: " + questionSettings.getExamDuration());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // endregion

        // region QxmPrivacyType
        ArrayAdapter<CharSequence> qxmPrivacyTypeAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.qxm_privacy_list, android.R.layout.simple_spinner_item);
        qxmPrivacyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrivacyType.setAdapter(qxmPrivacyTypeAdapter);

        spinnerPrivacyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    questionSettings.setQuestionPrivacy(QXM_PRIVACY_PUBLIC);
                } else {
                    questionSettings.setQuestionPrivacy(QXM_PRIVACY_PRIVATE);
                }
                Log.d(TAG, "onPollSelected: Question Privacy: " + questionSettings.getQuestionPrivacy());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // endregion

        // region RaffleDraw

        //RafflePriorityOne

        priorities = new String[]{
                "Marks",
                "Quick Submission",
                "Raffle Draw"
        };

        List<String> priorityList = new ArrayList<>(Arrays.asList(priorities));
        priorityList.add(0, "Priority One");

        priorityOneAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, priorityList) {
            @Override
            public boolean isEnabled(int position) {
                // disable the first item of spinner
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        priorityOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorityOne.setAdapter(priorityOneAdapter);

        //RafflePriorityTwo
        List<String> priorityTwoList = new ArrayList<>(Arrays.asList(priorities));
        priorityTwoList.add(0, "Priority Two");
        priorityTwoList.remove(1);// remove marks form priorityList

        priorityTwoAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, priorityTwoList) {
            @Override
            public boolean isEnabled(int position) {
                // disable the first item of spinner
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        priorityTwoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorityTwo.setAdapter(priorityTwoAdapter);

        //RafflePriorityThree
        List<String> priorityThreeList = new ArrayList<>(Arrays.asList(priorities));
        priorityThreeList.add(0, "Priority Three");
        priorityThreeList.remove(1); // remove marks from priorityList

        priorityThreeAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, priorityThreeList) {
            @Override
            public boolean isEnabled(int position) {
                // disable the first item of spinner
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        priorityThreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorityThree.setAdapter(priorityThreeAdapter);

        // endregion

        viewChangeWithContestMode();

    }

    private void viewChangeWithContestMode() {

        if (switchContestMode.isChecked()) {
            //contest mode on
            viewOverlay.setVisibility(View.GONE);
            rgQxmStartSchedule.setEnabled(false);
            rlQxmExpiration.setVisibility(View.GONE);
            radioLeaderBoardPublishQxmExpires.setVisibility(View.GONE);
            radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.GONE);

            // Hiding Qxm Start Schedule
            tvTitleQxmStartSchedule.setVisibility(View.GONE);
            rgQxmStartSchedule.setVisibility(View.GONE);

            // Contest EndTime
            if (questionSettings.getExamDuration().equals(QXM_TIME_TYPE_NO_LIMIT)) {

                radioContestEndWithExamEnds.setChecked(false);
                radioContestEndWithExamEnds.setEnabled(false);
                radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestEndWithExamEnds.setAlpha(0.4f);
                radioContestEndSpecificDate.setChecked(true);
            } else {
                radioContestEndWithExamEnds.setEnabled(true);
                radioContestEndWithExamEnds.setAlpha(1.0f);
            }


            // CONTEST
            tvParticipationExpiration.setVisibility(View.GONE);
            //  rgParticipationExpiration.setVisibility(View.VISIBLE);
            radioCorrectAnsVisibleContestEnds.setVisibility(View.VISIBLE);
            radioLeaderBoardPublishContestEnds.setVisibility(View.VISIBLE);

            /*if (questionSettings.getQxmExpirationDate()!= null) {
                if(QxmStringIntegerChecker.isLongInt(questionSettings.getQxmExpirationDate())){
                    radioContestEndSpecificDate.setChecked(true);
                    long expTime = Long.parseLong(questionSettings.getQxmExpirationDate());

                    Log.d(TAG, "getDate: " + TimeFormatString.getDate(expTime));
                    Log.d(TAG, "getTimeAndDate: " + TimeFormatString.getTimeAndDate(expTime));

                    radioContestEndSpecificDate.setText(String.format("Contest ends at %s", TimeFormatString.getTimeAndDate(expTime)));
                }else{
                    radioContestEndSpecificDate.setChecked(false);
                }
            }else{
                radioContestEndSpecificDate.setChecked(false);
            }*/


        } else {
            //contest mode off
            viewOverlay.setVisibility(View.VISIBLE);
            rgQxmStartSchedule.setEnabled(true);
            rlQxmExpiration.setVisibility(View.VISIBLE);
            radioLeaderBoardPublishQxmExpires.setVisibility(View.VISIBLE);

            if (switchQxmExpiration.isChecked()) {
                Log.d(TAG, "init: switchQxmExpiration =  true ");
                radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.VISIBLE);
                radioLeaderBoardPublishQxmExpires.setVisibility(View.VISIBLE);
                radioLeaderBoardPublishContestEnds.setVisibility(View.VISIBLE);

            } else {
                Log.d(TAG, "init: switchQxmExpiration =  false ");
                radioCorrectAnsVisibleImmediately.setChecked(true);
                radioLeaderBoardPublishContestEnds.setVisibility(View.GONE);
                radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.GONE);
                radioLeaderBoardPublishQxmExpires.setVisibility(View.GONE);
            }

            // Showing Qxm Start Schedule
            tvTitleQxmStartSchedule.setVisibility(View.VISIBLE);
            rgQxmStartSchedule.setVisibility(View.VISIBLE);


            // CONTEST
            tvParticipationExpiration.setVisibility(View.GONE);
            //  rgParticipationExpiration.setVisibility(View.GONE);
            radioCorrectAnsVisibleContestEnds.setVisibility(View.GONE);
            radioLeaderBoardPublishContestEnds.setVisibility(View.GONE);

            /*if (questionSettings.getQxmExpirationDate()!= null) {
                if(QxmStringIntegerChecker.isLongInt(questionSettings.getQxmExpirationDate())){
                    switchQxmExpiration.setChecked(true);
                    long expTime = Long.parseLong(questionSettings.getQxmExpirationDate());

                    Log.d(TAG, "getDate: " + TimeFormatString.getDate(expTime));
                    Log.d(TAG, "getTimeAndDate: " + TimeFormatString.getTimeAndDate(expTime));

                    tvQxmExpirationTime.setText(String.format("Contest ends at %s", TimeFormatString.getTimeAndDate(expTime)));

                }else{
                    switchQxmExpiration.setChecked(false);
                    tvQxmExpirationTime.setVisibility(View.GONE);
                }
            }else{
                switchQxmExpiration.setChecked(false);
                tvQxmExpirationTime.setVisibility(View.GONE);
            }*/
        }
    }

    // endregion

    // region ClickListeners

    private void clickListeners() {

        ivBackArrow.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        //region RadioButton date-time chooser
        radioQxmStartChooseDate.setOnClickListener(v -> {
//            if (radioQxmStartChooseDate.isChecked()) {
//                chooseDateAndTime(context, QXM_START_SCHEDULE_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_dialog_choose_qxm_start_date));
//            }
            chooseDateAndTime(context, QXM_START_SCHEDULE_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_dialog_choose_qxm_start_date));
        });
        //endregion

        //region Enroll
        switchQxmEnroll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            questionSettings.setEnrollEnabled(isChecked);
            Log.d(TAG, "Enroll onCheckedChanged: " + isChecked);
        });

        ivEnrollInfo.setOnClickListener(v -> {
            if (tvEnrollHint.getVisibility() == View.VISIBLE) {
                tvEnrollHint.setVisibility(View.GONE);
            } else {
                tvEnrollHint.setVisibility(View.VISIBLE);
            }
        });
        //endregion

        rgQxmVisibility.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioQxmVisibilityAfterParticipation) {
                Log.d(TAG, "QxmVisibility: " + "After Participation");
                questionSettings.setQuestionVisibility(QUESTION_VISIBILITY_AFTER_PARTICIPATION);

                radioQxmVisibilityAfterParticipation.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioQxmVisibilityBeforeParticipation.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioQxmVisibilityBeforeParticipation) {
                Log.d(TAG, "QxmVisibility: " + "Before Participation");
                questionSettings.setQuestionVisibility(QUESTION_VISIBILITY_BEFORE_PARTICIPATION);

                radioQxmVisibilityAfterParticipation.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmVisibilityBeforeParticipation.setTextColor(getResources().getColor(R.color.colorPrimary));
            }


        });

        rgQxmStartSchedule.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioQxmStartInstantly:
                    Log.d(TAG, "QxmStartSchedule: QxmStartInstantly");
                    questionSettings.setQxmStartScheduleDate(QXM_START_INSTANTLY);

                    radioQxmStartChooseDate.setText(getResources().getString(R.string.choose_date_and_time));

                    radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;
                case R.id.radioQxmStartChooseDate:
                    Log.d(TAG, "QxmStartSchedule: QxmStartByDate");

                    radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioQxmStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;
                case R.id.radioQxmStartManual:
                    Log.d(TAG, "QxmStartSchedule: QxmStartManually");
                    questionSettings.setQxmStartScheduleDate(QXM_START_MANUAL);
                    questionSettings.setQxmCurrentStatus(QXM_CURRENT_STATUS_NOT_STARTED);
                    radioQxmStartChooseDate.setText(getResources().getString(R.string.choose_date_and_time));

                    radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmStartManual.setTextColor(getResources().getColor(R.color.colorPrimary));

                    break;
            }
        });

        rgQxmEvaluation.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.radioQxmEvaluateAutomatic:
                    questionSettings.setEvaluationType(EVALUATE_AUTOMATIC);
                    radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;

                case R.id.radioQxmEvaluateManual:
                    questionSettings.setEvaluationType(EVALUATE_MANUAL);
                    radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;

                case R.id.radioQxmEvaluateSemiAutomatic:
                    questionSettings.setEvaluationType(EVALUATE_SEMI_AUTO);
                    radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.colorPrimary));

                    break;
            }

            Log.d(TAG, "QxmEvaluation: " + questionSettings.getEvaluationType());

        });

        switchQxmExpiration.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                // Visible correctAnswerVisible After QxmExpires
                radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.VISIBLE);
                radioLeaderBoardPublishQxmExpires.setVisibility(View.VISIBLE);
                tvLeaderBoardPublishQxmExpiresTips.setVisibility(View.VISIBLE);
                chooseDateAndTime(context, QXM_EXPIRATION_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_qxm_expiration_date));
            } else {
                if (radioLeaderBoardPublishQxmExpires.isChecked())
                    radioLeaderBoardPublishContinue.setChecked(true);

                if (radioCorrectAnsVisibleAfterQxmExpire.isChecked())
                    radioCorrectAnsVisibleImmediately.setChecked(true);

                radioCorrectAnsVisibleAfterQxmExpire.setVisibility(View.GONE);
                radioLeaderBoardPublishQxmExpires.setVisibility(View.GONE);
                tvLeaderBoardPublishQxmExpiresTips.setVisibility(View.GONE);
                tvQxmExpirationTime.setVisibility(View.GONE);
                questionSettings.setQxmExpirationDate(null);

            }


        });

        // this text is shown when qxm expires date is given
        // so allow user to change expire date by clicking on it when it is visible
        tvQxmExpirationTime.setOnClickListener(v -> {
            chooseDateAndTime(context, QXM_EXPIRATION_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_qxm_expiration_date));
        });

        checkboxNegativeMarking.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvNegativeMarking.setTextColor(getResources().getColor(R.color.primary_text_color));
                etNegativeMarking.setEnabled(true);
                Log.d(TAG, "NegativeMarkingEnabled: true");
            } else {
                tvNegativeMarking.setTextColor(getResources().getColor(R.color.secondary_text_color));
                etNegativeMarking.setEnabled(false);
                Log.d(TAG, "NegativeMarkingEnabled: false");
            }

        });

        // region ContestMode

        switchContestMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                rlContestMode.setBackground(getResources().getDrawable(R.drawable.dialog_title_bar_background));
                rgQxmStartSchedule.setEnabled(false);
                viewChangeWithContestMode();
                disableEnableControls(true, llContestModeSettings);

                questionSettings.setContestModeEnabled(true);

                Toast.makeText(context, "Contest mode activated", Toast.LENGTH_SHORT).show();
            } else {
                rlContestMode.setBackground(null);
                rgQxmStartSchedule.setEnabled(true);
                viewChangeWithContestMode();
                disableEnableControls(false, llContestModeSettings);

                questionSettings.setContestModeEnabled(false);

                Toast.makeText(context, "Contest mode deactivated", Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "ContestModeOn: " + questionSettings.isContestModeEnabled());

        });

        //region contest start

        // as we want click changes in this field, it is out of group
        radioContestStartSpecificDate.setOnClickListener(v -> {
            chooseDateAndTime(context, CONTEST_START_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_contest_start_time));
            radioContestStartImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioContestStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
        });
        rgContestStart.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.radioContestStartImmediately:
                    questionSettings.setQxmStartScheduleDate(CONTEST_START_INSTANTLY);
                    radioContestStartSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                    radioContestStartImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioContestStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                    break;
                // case R.id.radioContestStartSpecificDate:
                //     chooseDateAndTime(context, CONTEST_START_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_contest_start_time));
                //     radioContestStartImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                //     radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                //     radioContestStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                //     break;
                case R.id.radioContestStartManual:
                    questionSettings.setQxmStartScheduleDate(CONTEST_START_MANUAL);
                    radioContestStartSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                    radioContestStartImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioContestStartManual.setTextColor(getResources().getColor(R.color.colorPrimary));

                    break;
            }

            Log.d(TAG, "ContestStartTimeType: " + questionSettings.getQxmStartScheduleDate());
        });
        //endregion

        //region contest end
        radioContestEndSpecificDate.setOnClickListener(v -> {
            chooseDateAndTime(context, CONTEST_END_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_contest_end_time));
            radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioContestEndSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
        });

        rgContestEndTime.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioContestEndWithExamEnds) {

                questionSettings.setQxmExpirationDate(CONTEST_END_WITH_EXAM_DURATION);
                radioContestEndSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioContestEndSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                Log.d(TAG, "ContestEndTimeType: " + questionSettings.getQxmExpirationDate());

            }
            // else if (checkedId == R.id.radioContestEndSpecificDate) {
            //
            //     // chooseDateAndTime(context, CONTEST_END_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_title_choose_contest_end_time));
            //     // radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            //     // radioContestEndSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            //
            // }
        });
        //endregion

        //region participation expiration
        rgParticipationExpiration.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioParticipationAllow) {
                questionSettings.setParticipateAfterContestEnd(true);
                Log.d(TAG, "ParticipationExpiration: allow participation after contest ends");
                radioParticipationAllow.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioParticipationDeny.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (checkedId == R.id.radioParticipationDeny) {
                questionSettings.setParticipateAfterContestEnd(false);
                Log.d(TAG, "ParticipationExpiration: deny participation after contest ends");
                radioParticipationAllow.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioParticipationDeny.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        //endregion

        //region correct answer visibility
        // as we want support for changes in click, this radio button is out of radio group
        radioCorrectAnsVisibleByDate.setOnClickListener(v -> {
            chooseDateAndTime(context, CORRECT_ANSWER_VISIBILITY_DATE_TIME, getString(R.string.time_picker_dialog_choose_correct_answer_visibility_date));
            radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));
        });

        rgCorrectAnswerVisibility.setOnCheckedChangeListener((group, checkedId) -> {
            // if (checkedId == R.id.radioCorrectAnsVisibleByDate) {
            //     chooseDateAndTime(context, CORRECT_ANSWER_VISIBILITY_DATE_TIME, getString(R.string.time_picker_dialog_choose_correct_answer_visibility_date));
            //
            //     radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            //     radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            //     radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
            //     radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));
            // }
            if (checkedId == R.id.radioCorrectAnsVisibleContestEnds) {
                Log.d(TAG, "clickListeners: radioCorrectAnsVisibleContestEnds");
                questionSettings.setCorrectAnswerVisibilityDate(CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS);
                radioCorrectAnsVisibleByDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioCorrectAnsVisibleImmediately) {
                Log.d(TAG, "clickListeners: radioCorrectAnsVisibleImmediately");
                questionSettings.setCorrectAnswerVisibilityDate(CORRECT_ANSWER_VISIBILITY_IMMEDIATELY);
                radioCorrectAnsVisibleByDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioCorrectAnsVisibleAfterQxmExpire) {
                Log.d(TAG, "clickListeners: radioCorrectAnsVisibleAfterQxmExpire");
                questionSettings.setCorrectAnswerVisibilityDate(CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES);
                radioCorrectAnsVisibleByDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            Log.d(TAG, "Correct Answer Visibility: " + questionSettings.getCorrectAnswerVisibilityDate());


        });
        //endregion

        // region Leader board


        // as we want changes in click, it is out of radio group
        radioLeaderBoardPublishSpecificDate.setOnClickListener(v -> {
            chooseDateAndTime(context, LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_choose_leaderboard_publish_date));
            radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
        });

        rgLeaderBoardPublish.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioLeaderBoardPublishContinue:
                    questionSettings.setLeaderboardPublishDate(LEADERBOARD_PUBLISH_TYPE_CONTINUE);
                    radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                    radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;
                // case R.id.radioLeaderBoardPublishSpecificDate:
                // chooseDateAndTime(context, LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME, getString(R.string.time_picker_dialog_choose_leaderboard_publish_date));
                // radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                // radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                // radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                // radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                //
                // break;
                case R.id.radioLeaderBoardPublishQxmExpires:

                    questionSettings.setLeaderboardPublishDate(LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES);
                    radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));

                    radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;
                case R.id.radioLeaderBoardPublishContestEnds:

                    questionSettings.setLeaderboardPublishDate(LEADERBOARD_PUBLISH_TYPE_CONTEST_END);
                    radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));

                    radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));

                    break;
            }
            Log.d(TAG, "LeaderBoardPublish: " + questionSettings.getLeaderboardPublishDate());
        });

        rgLeaderBoardPrivacy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLeaderBoardPrivacyParticipants) {

                questionSettings.setLeaderboardPrivacy(LEADERBOARD_PRIVACY_PARTICIPANTS);

                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioLeaderBoardPrivacyPublic) {

                questionSettings.setLeaderboardPrivacy(LEADERBOARD_PRIVACY_PUBLIC);

                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.colorPrimary));

            }

            Log.d(TAG, "LeaderBoardPrivacy: " + questionSettings.getLeaderboardPrivacy());
        });


        // region RaffleDraw


        spinnerPriorityOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    //
                    questionSettings.getWinnerSelectionPriorityRules().set(0, priorities[position - 1]);
                }
                Log.d(TAG, "raffle priority: " + questionSettings.getWinnerSelectionPriorityRules().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPriorityOne.setSelection(1);
        spinnerPriorityOne.setEnabled(false);

        spinnerPriorityTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    questionSettings.getWinnerSelectionPriorityRules().set(1, priorities[position]);
                    if (position == 1) {
                        spinnerPriorityThree.setEnabled(true);
                        spinnerPriorityThree.setSelection(2);
                    } else if (position == 2) {
                        spinnerPriorityThree.setSelection(1);
                        spinnerPriorityThree.setEnabled(false);
                    }
                }
                Log.d(TAG, "raffle priority: " + questionSettings.getWinnerSelectionPriorityRules().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPriorityThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    questionSettings.getWinnerSelectionPriorityRules().set(2, priorities[position]);

                    if (position == 1)
                        spinnerPriorityTwo.setSelection(2);
                    else if (position == 2)
                        spinnerPriorityTwo.setSelection(1);
                }
                Log.d(TAG, "raffle priority: " + questionSettings.getWinnerSelectionPriorityRules().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // endregion

        // endregion

        btnSaveAsDraft.setOnClickListener(v -> {

            saveNegativeMarking();

            if (questionSet != null) {
                progressDialog.showProgressDialog("Draft QXM", "Qxm is being drafted...", false);
                questionSettings.setQuestionStatus(QUESTION_STATUS_DRAFTED);

                postQxmToBackEnd();

            } else {
                Toast.makeText(context, "Qxm is can not be publish. please try again.", Toast.LENGTH_SHORT).show();
            }

        });

        btnQxmPublish.setOnClickListener(v -> {

            if (validateQxmSettings()) {
                Log.d(TAG, "Qxm Publish: validation passed.");

                saveNegativeMarking();

                if (questionSet != null) {
                    progressDialog.showProgressDialog("Publish QXM", "Qxm is being published...", false);
                    questionSettings.setQuestionStatus(QUESTION_STATUS_PUBLISHED);
                    postQxmToBackEnd();

                } else {
                    Toast.makeText(context, "Qxm is can not be publish. please try again.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG, "Qxm Publish: validation failed.");
            }

        });

    }

    private boolean validateQxmSettings() {

        if (questionSettings.getQuestionCategory().size() == 0) {
            rvQxmCategory.requestFocus();
            Toast.makeText(context, "Please select a category for this QXM.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (questionSettings.isContestModeEnabled() && TextUtils.isEmpty(etNumberOfWinners.getText().toString())) {
            Toast.makeText(context, "Please enter the number of the winners.", Toast.LENGTH_SHORT).show();
            etNumberOfWinners.requestFocus();
            return false;
        } else if (QxmStringIntegerChecker.isLongInt(questionSettings.getQxmExpirationDate())) {
            long qxmExpTime = Long.parseLong(questionSettings.getQxmExpirationDate());
            if (qxmExpTime < Calendar.getInstance().getTimeInMillis()) {
                Toast.makeText(context, "Your given Qxm Expiration time is invalid. Please select a valid date.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (QxmStringIntegerChecker.isLongInt(questionSettings.getLeaderboardPublishDate())) {
            long leaderboardPublishDate = Long.parseLong(questionSettings.getLeaderboardPublishDate());
            if (leaderboardPublishDate < Calendar.getInstance().getTimeInMillis()) {
                Toast.makeText(context, "Leaderboard publish date is invalid. Please choose a valid date for publishing leaderboard.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (checkboxNegativeMarking.isChecked()) {
            String negativePoints = etNegativeMarking.getText().toString().trim();
            if (!TextUtils.isEmpty(negativePoints)) {
                double points = Double.parseDouble(negativePoints);
                if (points == 0.0) {

                    Toast.makeText(context, "Please input valid negative points", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (points > maxPoints) {
                    Toast.makeText(context, String.format("Negative point must be grater than zero and less than or equal to %s (your question's maximum marks).", maxPoints), Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "Please input negative points", Toast.LENGTH_SHORT).show();
                etNegativeMarking.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void saveQxmAsDraft() {

        realmService.saveQxmAsDraft(getQxmDraft());

        Toast.makeText(context, "This Exam has saved as a draft on your device.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "saveQxmAsDraft: This Exam has saved as a draft on your device is successful.");

    }

    private QxmDraft getQxmDraft() {

        if (!questionSettings.isContestModeEnabled()) {
            questionSettings.setNumberOfWinners(0);
            questionSettings.setRaffleDrawEnabled(false);
            questionSettings.setParticipateAfterContestEnd(false);
        }

        QxmModel qxmModel = new QxmModel(questionSet, questionSettings);

        Type type = new TypeToken<QxmModel>() {
        }.getType();
        Gson gson = new Gson();
        String qxmJson = gson.toJson(qxmModel, type);
        Log.d(TAG, "QXM JSON: " + qxmJson);

        if (qxmDraft == null)
            qxmDraft = new QxmDraft();


        qxmDraft.setQuestionSetTitle(qxmModel.getQuestionSet().getQuestionSetTitle());
        qxmDraft.setQuestionSetDescription(qxmModel.getQuestionSet().getQuestionSetDescription());
        qxmDraft.setQuestionCategory(qxmModel.getQuestionSettings().getQuestionCategory());
        qxmDraft.setQuestionStatus(qxmModel.getQuestionSettings().getQuestionStatus());

        qxmDraft.setQuestionCategories(QxmArrayListToStringProcessor.getStringFromArrayList(qxmModel.getQuestionSettings().getQuestionCategory()));

        qxmDraft.setLastEditedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        if (TextUtils.isEmpty(qxmDraft.getCreatedAt()))
            qxmDraft.setCreatedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        //QuestionSet Full JSON
        qxmDraft.setQuestionSetJson(qxmJson);

        return qxmDraft;
    }

    // endregion

    // region ShowExamTimePicker

    private void showExamTimePicker(View rootView, Context context) {

        AppCompatDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View alertView = layoutInflater.inflate(R.layout.number_picker, (ViewGroup) rootView, false);

        NumberPicker hour = alertView.findViewById(R.id.hourNumber);
        NumberPicker minute = alertView.findViewById(R.id.minuteNumber);
        NumberPicker sec = alertView.findViewById(R.id.secNumber);

        builder.setTitle("Set Exam Duration");
        builder.setView(alertView);

        hour.setMinValue(0);
        hour.setMaxValue(12);
        hour.setWrapSelectorWheel(true);
        if (examHour != 0)
            hour.setValue(examHour);
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setWrapSelectorWheel(true);
        if (examMin != 0)
            minute.setValue(examMin);
        sec.setMinValue(0);
        sec.setMaxValue(59);
        sec.setWrapSelectorWheel(true);
        if (examSec != 0)
            sec.setValue(examSec);

        hour.setOnValueChangedListener((numberPicker, i, i1) -> {
            examHour = i1;
            Log.d("examHour", String.valueOf(examHour));
        });

        minute.setOnValueChangedListener((numberPicker, i, i1) -> {
            examMin = i1;
            Log.d("examMin", String.valueOf(examMin));
        });
        sec.setOnValueChangedListener((numberPicker, i, i1) -> {
            examSec = i1;
            Log.d("examSec", String.valueOf(examSec));
        });

        builder.setPositiveButton("Ok", (dialog1, which) -> {


            String answerDeadLineMessage = examHour + " hours "
                    + examMin + " minutes " + examSec + " seconds";


            questionSettings.setExamDuration(String.valueOf(((examHour * 60 * 60) + (examMin * 60) + (examSec)) * 1000));

            tvExamTime.setText(answerDeadLineMessage);


            // adding info to settings model
//                additionalInfo.setAnsDeadline(answerDeadlineString);

        });

        builder.setNegativeButton("Cancel", (dialog12, which) -> {

            // set no time limit if Time is not set or previously set
            if (view_examTimeTop.getVisibility() != View.VISIBLE)
                spinnerExamTimeType.setSelection(0);

//                additionalInfo.setAnsDeadline("false");

        });

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    // endregion

    //region ShowExamTimeViews

    private void showExamTimeViews() {
        //view_examTimeTop.setVisibility(View.VISIBLE);
        //getView_examTimeBottom.setVisibility(View.VISIBLE);
        //llExamTimeSelector.setVisibility(View.VISIBLE);
        llExamTimeValue.setVisibility(View.VISIBLE);
    }

    // endregion

    // region HideExamTimeViews

    private void hideExamTimeViews() {
        view_examTimeTop.setVisibility(View.GONE);
        getView_examTimeBottom.setVisibility(View.GONE);
        llExamTimeSelector.setVisibility(View.GONE);
        llExamTimeValue.setVisibility(View.GONE);
    }

    // endregion

    // region ChooseDateAndTime

    private void chooseDateAndTime(Context context, String chooseDateFor, String timePickerDialogTitle) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> {

            calendar.setTime(chooseDate);
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            calendar.set(Calendar.SECOND, 0);

            chooseDate = calendar.getTime();

            if (selectedHour >= 12) {
                amPM = "PM";
                selectedHour = selectedHour % 12;
            } else {
                amPM = "AM";
            }

            String expireDateString = dateString + " at " + selectedHour + ":" + selectedMinute + " " + amPM;

            switch (chooseDateFor) {
                case QXM_START_SCHEDULE_CHOOSE_DATE_TIME:
                    Log.d(TAG, "onTimeSet: QXM_START_SCHEDULE_CHOOSE_DATE_TIME:" + expireDateString);
                    Log.d(TAG, "chooseDateAndTime: " + calendar.getTime());
                    questionSettings.setQxmStartScheduleDate(String.valueOf(calendar.getTimeInMillis()));
                    //radioQxmStartChooseDate.setText(getString(R.string.exam_start_schedule)+TimeFormatString
                    // .getTimeAndDate(calendar.getTimeInMillis()));
                    radioQxmStartChooseDate.setText(String.format("Exam start schedule: %s", TimeFormatString.getTimeAndDate(calendar.getTimeInMillis())));

                    break;
                case QXM_EXPIRATION_DATE_TIME:
                    questionSettings.setQxmExpirationDate(String.valueOf(calendar.getTimeInMillis()));
                    tvQxmExpirationTime.setText(String.format("Exam will be expired on %s", expireDateString));
                    tvQxmExpirationTime.setVisibility(View.VISIBLE);
                    break;
                case CONTEST_START_CHOOSE_DATE_TIME:
                    Log.d(TAG, "onTimeSet: CONTEST_START_CHOOSE_DATE_TIME:" + expireDateString);
                    questionSettings.setQxmStartScheduleDate(String.valueOf(calendar.getTimeInMillis()));
                    radioContestStartSpecificDate.setText(String.format("Contest starts on %s", expireDateString));

                    break;
                case CONTEST_END_CHOOSE_DATE_TIME:
                    Log.d(TAG, "onTimeSet: CONTEST_END_CHOOSE_DATE_TIME:" + expireDateString);
                    questionSettings.setQxmExpirationDate(String.valueOf(calendar.getTimeInMillis()));
                    radioContestEndSpecificDate.setText(String.format("Contest ends on %s", expireDateString));

                    break;
                case CORRECT_ANSWER_VISIBILITY_DATE_TIME:
                    Log.d(TAG, "onTimeSet: CORRECT_ANSWER_VISIBILITY_DATE_TIME:" + expireDateString);
                    questionSettings.setCorrectAnswerVisibilityDate(String.valueOf(calendar.getTimeInMillis()));
                    radioCorrectAnsVisibleByDate.setText(String.format("Answer visible on %s", expireDateString));

                    break;
                case LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME:
                    Log.d(TAG, "onTimeSet: LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME:" + expireDateString);
                    Log.d(TAG, "PublishType: " + LEADERBOARD_PUBLISH_TYPE_BY_DATE);
                    questionSettings.setLeaderboardPublishDate(String.valueOf(calendar.getTimeInMillis()));

                    radioLeaderBoardPublishSpecificDate.setText(String.format("The leaderboard will be published on %s", expireDateString));

                    break;
            }

        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.setOnCancelListener(dialog -> {

            Log.d(TAG, "onCancel: timePicker");

            // if user cancel setting date-time... change the radio button to another option not to choose date
        });

        mTimePicker.setCancelable(false);
        mTimePicker.show();

        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, monthOfYear, dayOfMonth);
            chooseDate = calendar1.getTime();

            dateString = dayOfMonth + "/" + monthOfYear + "/" + year;
            String expireDateMessage = "Exam will be expired on: " + dateString;

            Log.d(TAG, "onDateSet: " + expireDateMessage);

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle(timePickerDialogTitle);
        datePickerDialog.show();

    }

    // endregion

    // region SaveNegativeMarking

    private void saveNegativeMarking() {
        if (checkboxNegativeMarking.isChecked()) {
            String negativePoints = etNegativeMarking.getText().toString().trim();

            if (!TextUtils.isEmpty(negativePoints)) {
                double points = Double.parseDouble(negativePoints);
                if (points == 0.0)
                    Toast.makeText(context, "Please input valid negative points", Toast.LENGTH_SHORT).show();
                else if (points > maxPoints) {
                    Toast.makeText(context, String.format("Negative point must be grater than zero and less than or equal to %s (%s is your question's maximum marks).", maxPoints, maxPoints), Toast.LENGTH_LONG).show();
                }
                questionSettings.setNegativePoints(points);
            } else {
                Toast.makeText(context, "Please input negative points", Toast.LENGTH_SHORT).show();
                etNegativeMarking.requestFocus();
            }
        }
    }

    // endregion

    // region DisableEnableControls in ContestModeViews
    private void disableEnableControls(boolean enable, ViewGroup vg) {
        if (enable) {
            vg.setVisibility(View.VISIBLE);
            vg.startAnimation(animSideDown);
        } else {
            vg.startAnimation(animSlideUp);
        }

        //Todo:: comment this for testing purpose
        /*vg.setEnabled(enable); // the point that I was missing
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            child.setClickable(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }*/
    }


    // endregion

    // region PopulateDataIntoRecyclerView

    private void populateDataIntoRecyclerView() {

        ArrayList<String> qxmCategoryList = QxmCategoryHelper.getQxmCategoryList(context);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                3, GridLayoutManager.HORIZONTAL, false);

        rvQxmCategory.setLayoutManager(gridLayoutManager);
        rvQxmCategory.setHasFixedSize(true);
        rvQxmCategory.setNestedScrollingEnabled(false);

        QxmCategoryAdapter qxmCategoryAdapter = new QxmCategoryAdapter(context, qxmCategoryList, questionSettings.getQuestionCategory(), this);
        rvQxmCategory.setAdapter(qxmCategoryAdapter);

    }

    // endregion

    // region PostQxmTOBackend

    private void postQxmToBackEnd() {


        if (!questionSettings.isContestModeEnabled()) {
            questionSettings.setNumberOfWinners(0);
            questionSettings.setRaffleDrawEnabled(false);
            questionSettings.setParticipateAfterContestEnd(false);
        }


        QxmModel qxmModel = new QxmModel(questionSet, questionSettings);


        Gson gson = new Gson();
        Log.d(TAG, "postQxmToBackEnd: Token: " + token.getToken());
        Log.d(TAG, "postQxmToBackEnd: UserId:" + token.getUserId());
        Log.d(TAG, "QXM JSON: " + gson.toJson(qxmModel));

        Call<QxmModel> postQxm = apiService.postQxm(token.getToken(), token.getUserId(), qxmModel);

        postQxm.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                Log.d(TAG, "onResponse: postQxm network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: postQxm success");

                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: new Qxm created successfully");

                        //QxmModel qxm = response.body();
                        //   realmService.addQxm(qxm);
                        Toast.makeText(context, "Question posted successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.closeProgressDialog();
                        Intent intent = new Intent();

                        if (qxmModel.getQuestionSettings().getQuestionPrivacy().equals(QUESTION_STATUS_DRAFTED))
                            intent.putExtra(QXM_PRIVACY_KEY, QUESTION_STATUS_DRAFTED);
                        else
                            intent.putExtra(QXM_PRIVACY_KEY, QUESTION_STATUS_PUBLISHED);

                        {
                            if (getActivity() != null) {

                                // as this is our static value we are making this empty
                                CreateQxmFragment.questionThumbnailImage = "";
                                CreateQxmFragment.youtubeThumbnailURL = "";

                                getActivity().setResult(Activity.RESULT_OK, intent);
                                getActivity().finish();

                            }
                        }


                    } else {
                        progressDialog.closeProgressDialog();
                        Log.d(TAG, "onResponse: postQxm failed. Response body is null");
                        Toast.makeText(context, "Failed to post question", Toast.LENGTH_SHORT).show();

                    }
                } else if (response.code() == 400) {
                    progressDialog.closeProgressDialog();
                    Toast.makeText(context, R.string.the_minimum_interval_time_between_post_message, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    progressDialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: postQxm failed");

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure postQxm: network call.");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                progressDialog.closeProgressDialog();

                saveQxmAsDraft();

                Toast.makeText(context, "Failed to post question but saved as draft on your device.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // endregion

    // region Override - onInterestSeleced


    @Override
    public void onCategorySelected(RelativeLayout interestMainContent, TextView categoryTV, int position) {

        String selectedOrDeselectedCategory = categoryTV.getText().toString().trim();

        int maxCategorySelection = 1;

        if (interestMainContent.getBackground() != null) {
            questionSettings.getQuestionCategory().remove(selectedOrDeselectedCategory);
            interestMainContent.setBackgroundResource(0);

            if (categorySelected != 0) categorySelected--;

        } else if (categorySelected < maxCategorySelection) {
            questionSettings.getQuestionCategory().add(selectedOrDeselectedCategory);

            interestMainContent.setBackground(getResources().getDrawable(R.drawable.background_qxm_category_grid_item));
            categorySelected++;

        } else {
            Toast.makeText(context, "You can only select one category for a question.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation == animSlideUp) {
            llContestModeSettings.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    // endregion

    // region LoadDataInViews
    private void loadDataInViews() {
        Log.d(TAG, "loadDataInViews: QuestionSettings = " + questionSettings.toString());

        // region 1. Category Select
        // already done this part in category Adapter
        Log.d(TAG, "loadDataInViews: Category: " + questionSettings.getQuestionCategory().toString());

        // endregion

        // region 2. Qxm Time
        if (questionSettings.getExamDuration().equals(QXM_TIME_TYPE_NO_LIMIT)) {
            spinnerExamTimeType.setSelection(0);
            hideExamTimeViews();
        } else {
            spinnerExamTimeType.setSelection(1);
            if (QxmStringIntegerChecker.isLongInt(questionSettings.getExamDuration())) {
                tvExamTime.setText(TimeFormatString.getDurationInHourMinSecFullForm(
                        Long.parseLong(questionSettings.getExamDuration())
                ));
            }
            showExamTimeViews();
        }
        // endregion

        // region 3. Qxm Privacy
        if (questionSettings.getQuestionPrivacy().equals(LIST_PRIVACY_PUBLIC))
            spinnerPrivacyType.setSelection(0);
        else if (questionSettings.getQuestionPrivacy().equals(LIST_PRIVACY_PRIVATE))
            spinnerPrivacyType.setSelection(1);

        // endregion

        // region 4. Enroll

        if (questionSettings.isEnrollEnabled())
            switchQxmEnroll.setChecked(true);
        else switchQxmEnroll.setChecked(false);

        // endregion

        // region 5. Qxm will be visible

        if (questionSettings.getQuestionVisibility().equals(QUESTION_VISIBILITY_AFTER_PARTICIPATION)) {
            radioQxmVisibilityAfterParticipation.setChecked(true);
            radioQxmVisibilityAfterParticipation.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioQxmVisibilityBeforeParticipation.setTextColor(getResources().getColor(R.color.primary_text_color));

        } else if (questionSettings.getQuestionVisibility().equals(QUESTION_VISIBILITY_BEFORE_PARTICIPATION)) {

            radioQxmVisibilityAfterParticipation.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioQxmVisibilityBeforeParticipation.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioQxmVisibilityBeforeParticipation.setChecked(true);
        }

        // endregion

        // region 6. Qxm Start Schedule

        switch (questionSettings.getQxmStartScheduleDate()) {
            case QXM_START_INSTANTLY:

                radioQxmStartChooseDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioQxmStartInstantly.setChecked(true);
                radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                break;
            case QXM_START_MANUAL:
                questionSettings.setQxmCurrentStatus(QXM_CURRENT_STATUS_NOT_STARTED);
                radioQxmStartChooseDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioQxmStartManual.setChecked(true);
                radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmStartManual.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            default:

                radioQxmStartChooseDate.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(questionSettings.getQxmStartScheduleDate())
                ));
                radioQxmStartChooseDate.setChecked(true);
                radioQxmStartInstantly.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmStartChooseDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioQxmStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                break;
        }

        // endregion

        // region 7. Qxm Evaluation Type

        switch (questionSettings.getEvaluationType()) {
            case EVALUATE_AUTOMATIC:
                radioQxmEvaluateAutomatic.setChecked(true);
                radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.primary_text_color));
                break;
            case EVALUATE_MANUAL:
                radioQxmEvaluateManual.setChecked(true);
                radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.primary_text_color));
                break;
            case EVALUATE_SEMI_AUTO:
                radioQxmEvaluateSemiAuto.setChecked(true);
                radioQxmEvaluateAutomatic.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmEvaluateManual.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioQxmEvaluateSemiAuto.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }

        // endregion

        // region 8. Qxm Expiration
        if (QxmStringIntegerChecker.isLongInt(questionSettings.getQxmExpirationDate())) {
            Log.d(TAG, "loadDataInViews: ");
            switchQxmExpiration.setChecked(true);
            tvQxmExpirationTime.setText(String.format("Exam will be expired on %s", TimeFormatString.getTimeAndDate(
                    Long.parseLong(questionSettings.getQxmExpirationDate())
            )));
            tvQxmExpirationTime.setVisibility(View.VISIBLE);
        }

        // endregion

        // region 9. Negative Marking
        if (questionSettings.getNegativePoints() != 0.0) {
            checkboxNegativeMarking.setChecked(true);
            tvNegativeMarking.setTextColor(getResources().getColor(R.color.primary_text_color));
            etNegativeMarking.setEnabled(true);
            etNegativeMarking.setText(String.valueOf(questionSettings.getNegativePoints()));
        }
        // endregion

        // region 10. Contest Mode
        // TODO contest mode is disable in first beta release
        // if (questionSettings.isContestModeEnabled()) {
        //     switchContestMode.setChecked(true);
        //     rlContestMode.setBackground(getResources().getDrawable(R.drawable.dialog_title_bar_background));
        //     rgQxmStartSchedule.setEnabled(false);
        //     viewChangeWithContestMode();
        //     disableEnableControls(true, llContestModeSettings);
        //
        //     // Toast.makeText(context, "Contest mode activated", Toast.LENGTH_SHORT).show();
        //
        // } else {
        //     switchContestMode.setChecked(false);
        //     rlContestMode.setBackground(null);
        //     rgQxmStartSchedule.setEnabled(true);
        //     viewChangeWithContestMode();
        //     disableEnableControls(false, llContestModeSettings);
        //
        //     // Toast.makeText(context, "Contest mode deactivated", Toast.LENGTH_SHORT).show();
        // }


        // region 10.1 Contest Start Time
        if (!TextUtils.isEmpty(questionSettings.getQxmStartScheduleDate())) {
            if (questionSettings.getQxmStartScheduleDate().equals(CONTEST_START_INSTANTLY)) {
                radioContestStartImmediately.setChecked(true);
                radioContestStartSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));

                radioContestStartImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (questionSettings.getQxmStartScheduleDate().equals(CONTEST_START_MANUAL)) {
                radioContestStartManual.setChecked(true);
                radioContestStartSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));

                radioContestStartImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestStartManual.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (QxmStringIntegerChecker.isLongInt(questionSettings.getQxmStartScheduleDate())) {
                radioContestStartSpecificDate.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(questionSettings.getQxmStartScheduleDate())
                ));
                radioContestStartImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestStartSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioContestStartManual.setTextColor(getResources().getColor(R.color.primary_text_color));
            }
        }
        // endregion

        // region 10.2 Contest End Time
        if (!TextUtils.isEmpty(questionSettings.getQxmExpirationDate())) {

            if (questionSettings.getQxmExpirationDate().equals(CONTEST_END_WITH_EXAM_DURATION)) {
                radioContestEndWithExamEnds.setChecked(true);
                radioContestEndSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));

                radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioContestEndSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else {
                radioContestEndSpecificDate.setChecked(true);
                if (QxmStringIntegerChecker.isLongInt(questionSettings.getQxmExpirationDate())) {

                    radioContestEndSpecificDate.setText(String.format("Contest ends on %s", TimeFormatString
                            .getTimeAndDate(Long.parseLong(questionSettings.getQxmExpirationDate()))));
                }
                radioContestEndWithExamEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioContestEndSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }


        // endregion

        // region 10.3 Winner Selection Rule
        if (etNumberOfWinners != null)
            etNumberOfWinners.setText(String.valueOf(questionSettings.getNumberOfWinners()));

        int priorityOneIndex = priorityOneAdapter.getPosition(questionSettings.getWinnerSelectionPriorityRules().get(0));
        spinnerPriorityOne.setSelection(priorityOneIndex);

        int priorityTwoIndex = priorityTwoAdapter.getPosition(questionSettings.getWinnerSelectionPriorityRules().get(1));
        spinnerPriorityTwo.setSelection(priorityTwoIndex);

        int priorityThreeIndex = priorityThreeAdapter.getPosition(questionSettings.getWinnerSelectionPriorityRules().get(2));
        spinnerPriorityThree.setSelection(priorityThreeIndex);


        // endregion

        // region 10.4 Participation Expiration
        if (questionSettings.isParticipateAfterContestEnd()) {
            radioParticipationAllow.setChecked(true);
            radioParticipationAllow.setTextColor(getResources().getColor(R.color.colorPrimary));
            radioParticipationDeny.setTextColor(getResources().getColor(R.color.primary_text_color));
        } else {
            radioParticipationDeny.setChecked(true);
            radioParticipationAllow.setTextColor(getResources().getColor(R.color.primary_text_color));
            radioParticipationDeny.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        // endregion

        // endregion
        switchContestMode.setClickable(false);
        rlContestMode.setOnClickListener(v -> Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show());

        // region 11. Correct Answer Visibility with Marks
        if (!TextUtils.isEmpty(questionSettings.getCorrectAnswerVisibilityDate())) {
            if (questionSettings.getCorrectAnswerVisibilityDate().equals(CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS)) {
                radioCorrectAnsVisibleByDate.setText(getResources().getString(R.string.choose_date_and_time));

                radioCorrectAnsVisibleContestEnds.setChecked(true);

                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (questionSettings.getCorrectAnswerVisibilityDate().equals(CORRECT_ANSWER_VISIBILITY_IMMEDIATELY)) {
                radioCorrectAnsVisibleByDate.setText(getResources().getString(R.string.choose_date_and_time));

                radioCorrectAnsVisibleImmediately.setChecked(true);

                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (QxmStringIntegerChecker.isLongInt(questionSettings.getCorrectAnswerVisibilityDate())) {

                radioCorrectAnsVisibleByDate.setText(String.format("Answer visible on %s", TimeFormatString
                        .getTimeAndDate(Long.parseLong(questionSettings.getCorrectAnswerVisibilityDate()))));

                radioCorrectAnsVisibleByDate.setChecked(true);

                radioCorrectAnsVisibleByDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleAfterQxmExpire.setTextColor(getResources().getColor(R.color.primary_text_color));
            }
        }
        // endregion

        // region 12. Leaderboard Publish Time
        if (!TextUtils.isEmpty(questionSettings.getLeaderboardPublishDate())) {
            if (questionSettings.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTINUE)) {
                radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioLeaderBoardPublishContinue.setChecked(true);
                radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (questionSettings.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_CONTEST_END)) {
                radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioLeaderBoardPublishContestEnds.setChecked(true);
                radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));

            } else if (questionSettings.getLeaderboardPublishDate().equals(LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES)) {
                radioLeaderBoardPublishSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                radioLeaderBoardPublishQxmExpires.setChecked(true);
                radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (QxmStringIntegerChecker.isLongInt(questionSettings.getLeaderboardPublishDate())) {
                radioLeaderBoardPublishSpecificDate.setText(String.format("The leaderboard will be published on %s", TimeFormatString
                        .getTimeAndDate(Long.parseLong(questionSettings.getLeaderboardPublishDate()))));
                radioLeaderBoardPublishSpecificDate.setChecked(true);
                radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPublishQxmExpires.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
            }
        }
        // endregion

        // region 13. Leaderboard Privacy
        if (!TextUtils.isEmpty(questionSettings.getLeaderboardPrivacy())) {
            if (questionSettings.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PARTICIPANTS)) {
                radioLeaderBoardPrivacyParticipants.setChecked(true);
                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (questionSettings.getLeaderboardPrivacy().equals(LEADERBOARD_PRIVACY_PUBLIC)) {
                radioLeaderBoardPrivacyPublic.setChecked(true);
                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
        // endregion

        // region


    }

    @Override
    public void onBackPressed() {

        CreateQxmFragment createQxmFragment = CreateQxmFragment.newInstance(getQxmDraft());

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction
                .add(R.id.frame_container, createQxmFragment, CreateQxmFragment.class.getName())
                .addToBackStack(CreateQxmFragment.class.getName())
                .commit();
    }

    // endregion

}
