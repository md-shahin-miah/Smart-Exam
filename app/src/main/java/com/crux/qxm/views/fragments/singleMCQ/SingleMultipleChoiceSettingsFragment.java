package com.crux.qxm.views.fragments.singleMCQ;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.QxmCategoryAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.db.sampleModels.SingleMCQSettingsModel;
import com.crux.qxm.di.singleMultipleChoiceSettingsFragmentFeature.DaggerSingleMultipleChoiceSettingsFragmentComponent;
import com.crux.qxm.di.singleMultipleChoiceSettingsFragmentFeature.SingleMultipleChoiceSettingsFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmCategoryHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PARTICIPANTS;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTEST_END;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.MCQ_EXPIRATION_DATE_TIME;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_MODEL_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMultipleChoiceSettingsFragment extends Fragment implements QxmCategoryAdapter.CategorySelectListener, Animation.AnimationListener {

    //region Fragment-Properties
    @Inject
    Retrofit retrofit;

    private static final String TAG = "SingleMultipleChoiceSet";
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    private SingleMCQModel singleMCQModel;
    //private SingleMCQSettings questionSettings;

    private int categorySelected = 0;

    private String dateString, amPM;
    private Date chooseDate;

    // Animation
    private Animation animSlideUp;
    private Animation animSideDown;


    //endregion

    //region BindViewsWithButterKnife
    @BindView(R.id.rvMCQCategory)
    RecyclerView rvMCQCategory;

    @BindView(R.id.rlMCQExpiration)
    RelativeLayout rlMCQExpiration;

    @BindView(R.id.switchSMCExpiration)
    SwitchCompat switchSMCExpiration;

    @BindView(R.id.tvSMCQExpirationTime)
    AppCompatTextView tvSMCQExpirationTime;


    //region ContestMode

    @BindView(R.id.switchContestMode)
    SwitchCompat switchContestMode;

    @BindView(R.id.rlContestMode)
    RelativeLayout rlContestMode;

    @BindView(R.id.llContestModeSettings)
    LinearLayoutCompat llContestModeSettings;


    @BindView(R.id.checkBoxContestEndSpecificDate)
    AppCompatCheckBox checkBoxContestEndSpecificDate;

    @BindView(R.id.etNumberOfWinners)
    AppCompatEditText etNumberOfWinners;

    @BindView(R.id.checkboxRaffleDraw)
    AppCompatCheckBox checkboxRaffleDraw;

    //region RadioGroup-ParticipationExpiration
    @BindView(R.id.rgParticipationExpiration)
    RadioGroup rgParticipationExpiration;
    @BindView(R.id.radioParticipationAllow)
    RadioButton radioParticipationAllow;
    @BindView(R.id.radioParticipationDeny)
    RadioButton radioParticipationDeny;
    //endregion

    @BindView(R.id.rgCorrectAnswerVisibility)
    RadioGroup rgCorrectAnswerVisibility;

    //endregion

    @BindView(R.id.radioCorrectAnsVisibleContestEnds)
    RadioButton radioCorrectAnsVisibleContestEnds;

    @BindView(R.id.radioCorrectAnsVisibleImmediately)
    RadioButton radioCorrectAnsVisibleImmediately;

    @BindView(R.id.rgLeaderBoardPublish)
    RadioGroup rgLeaderBoardPublish;

    @BindView(R.id.radioLeaderBoardPublishContinue)
    RadioButton radioLeaderBoardPublishContinue;
    @BindView(R.id.radioLeaderBoardPublishContestEnds)
    RadioButton radioLeaderBoardPublishContestEnds;

    //region Leaderboard
    @BindView(R.id.rgLeaderBoardPrivacy)
    RadioGroup rgLeaderBoardPrivacy;

    @BindView(R.id.radioLeaderBoardPrivacyParticipants)
    RadioButton radioLeaderBoardPrivacyParticipants;
    @BindView(R.id.radioLeaderBoardPrivacyPublic)
    RadioButton radioLeaderBoardPrivacyPublic;
    //endregion

    @BindView(R.id.viewOverlay)
    View viewOverlay;

    @BindView(R.id.btnPublish)
    AppCompatButton btnPublish;
    //endregion

    //region Fragment-Constructor
    public SingleMultipleChoiceSettingsFragment() {
        // Required empty public constructor
    }
    //endregion

    public static SingleMultipleChoiceSettingsFragment newInstance(SingleMCQModel singleMCQModel) {

        Bundle args = new Bundle();
        args.putParcelable(SINGLE_MCQ_MODEL_KEY, singleMCQModel);
        SingleMultipleChoiceSettingsFragment fragment = new SingleMultipleChoiceSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //region Override-onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_multiple_choice_settings, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
    //endregion

    //region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        singleMCQModel = getArguments() != null ? getArguments().getParcelable(SINGLE_MCQ_MODEL_KEY) : null;

        if (singleMCQModel != null) {
            Gson gson = new Gson();
            String singleMCQModelJSON = gson.toJson(singleMCQModel);
            Log.d(TAG, "SingleMCQModel Json: " + singleMCQModelJSON);
        }

        setUpDagger2(view.getContext());

        init(view);

        populateDataIntoRecyclerView();

        initializeClickListeners();
    }

    //endregion

    //region SetUpDagger2

    private void setUpDagger2(Context context) {
        SingleMultipleChoiceSettingsFragmentComponent singleMultipleChoiceSettingsFragmentComponent =
                DaggerSingleMultipleChoiceSettingsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        singleMultipleChoiceSettingsFragmentComponent
                .injectSingleMultipleChoiceSettingsFragmentFeature(SingleMultipleChoiceSettingsFragment.this);
    }
    //endregion

    //region Init
    private void init(View view) {
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());


        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplicationContext(),
                R.anim.slide_up);
        animSideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.slide_down);

        // set animation listener
        animSlideUp.setAnimationListener(this);
        animSideDown.setAnimationListener(this);


        // Get Default QxmAppNotificationSettings
        if (singleMCQModel.getSingleMCQSettings() == null)
            singleMCQModel.setSingleMCQSettings(SingleMCQSettingsModel.getDefaultQuestionSettings());

        viewOverlay.bringToFront();
        //TODO contest mode is disabled in  beta release
        viewChangeWithContestMode();
        disableEnableControls(singleMCQModel.getSingleMCQSettings().isContestModeEnabled(), llContestModeSettings);

    }

    private void viewChangeWithContestMode() {

        if (switchContestMode.isChecked()) {
            //contest mode on
            viewOverlay.setVisibility(View.GONE);
            rlMCQExpiration.setVisibility(View.GONE);
            radioCorrectAnsVisibleContestEnds.setEnabled(true);
            radioCorrectAnsVisibleContestEnds.setAlpha(1.0f);
            radioLeaderBoardPublishContestEnds.setEnabled(true);
            radioLeaderBoardPublishContestEnds.setAlpha(1.0f);

            // set Leaderboard publish time - when contest ends
            radioLeaderBoardPublishContestEnds.setEnabled(true);
            radioLeaderBoardPublishContestEnds.setChecked(true);
            // Disable Leaderboard publish time - continuous update
            radioLeaderBoardPublishContinue.setEnabled(false);


            if (singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate() != null) {
                if (QxmStringIntegerChecker.isLongInt(singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate())) {
                    checkBoxContestEndSpecificDate.setChecked(true);
                    long expTime = Long.parseLong(singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate());

                    Log.d(TAG, "getDate: " + TimeFormatString.getDate(expTime));
                    Log.d(TAG, "getTimeAndDate: " + TimeFormatString.getTimeAndDate(expTime));

                    checkBoxContestEndSpecificDate.setText(String.format("Contest ends at %s", TimeFormatString.getTimeAndDate(expTime)));
                } else {
                    checkBoxContestEndSpecificDate.setChecked(false);
                }
            } else {
                checkBoxContestEndSpecificDate.setChecked(false);
            }
        } else {
            //contest mode off
            viewOverlay.setVisibility(View.VISIBLE);
            rlMCQExpiration.setVisibility(View.VISIBLE);
            radioCorrectAnsVisibleContestEnds.setEnabled(false);
            radioCorrectAnsVisibleContestEnds.setAlpha(0.4f);
            radioCorrectAnsVisibleImmediately.setChecked(true);
            radioLeaderBoardPublishContestEnds.setEnabled(false);
            radioLeaderBoardPublishContestEnds.setAlpha(0.4f);
            radioLeaderBoardPublishContinue.setChecked(true);


            // set Leaderboard publish time - continuous
            radioLeaderBoardPublishContinue.setEnabled(true);
            radioLeaderBoardPublishContinue.setChecked(true);
            // Disable Leaderboard publish time - when contest ends
            radioLeaderBoardPublishContestEnds.setEnabled(false);

        }
    }

    private void disableEnableControls(boolean enable, LinearLayoutCompat vg) {

        if (enable) {
            vg.setVisibility(View.VISIBLE);
            vg.startAnimation(animSideDown);
        } else {
            vg.startAnimation(animSlideUp);
        }
    }

    //endregion

    //region InitializeClickListeners
    private void initializeClickListeners() {

        //disabled contest mode in beta release
        switchContestMode.setClickable(false);
        rlContestMode.setOnClickListener(v-> Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show());


        tvSMCQExpirationTime.setOnClickListener(v -> {
            chooseDateAndTime(context, MCQ_EXPIRATION_DATE_TIME);
        });

        switchSMCExpiration.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                chooseDateAndTime(context, MCQ_EXPIRATION_DATE_TIME);
            } else {

                tvSMCQExpirationTime.setVisibility(View.GONE);
                singleMCQModel.getSingleMCQSettings().setSingleMCQExpirationDate(null);
            }

        });

        switchContestMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                rlContestMode.setBackground(getResources().getDrawable(R.drawable.dialog_title_bar_background));

                viewChangeWithContestMode();
                disableEnableControls(true, llContestModeSettings);
                singleMCQModel.getSingleMCQSettings().setContestModeEnabled(true);

                Toast.makeText(context, "Contest mode activated", Toast.LENGTH_SHORT).show();
            } else {
                rlContestMode.setBackground(null);
                viewChangeWithContestMode();
                disableEnableControls(false, llContestModeSettings);

                singleMCQModel.getSingleMCQSettings().setContestModeEnabled(false);

                Toast.makeText(context, "Contest mode deactivated", Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "ContestModeOn: " + singleMCQModel.getSingleMCQSettings().isContestModeEnabled());

        });

        checkBoxContestEndSpecificDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chooseDateAndTime(context, CONTEST_END_CHOOSE_DATE_TIME);
                checkBoxContestEndSpecificDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                checkBoxContestEndSpecificDate.setText(getResources().getString(R.string.choose_date_and_time));
                checkBoxContestEndSpecificDate.setTextColor(getResources().getColor(R.color.primary_text_color));

            }

            Log.d(TAG, "ContestEndTimeType: " + singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate());

        });

        rgParticipationExpiration.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioParticipationAllow) {
                singleMCQModel.getSingleMCQSettings().setParticipateAfterContestEnd(true);
                Log.d(TAG, "ParticipationExpiration: allow participation after contest ends");
                radioParticipationAllow.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioParticipationDeny.setTextColor(getResources().getColor(R.color.primary_text_color));
            } else if (checkedId == R.id.radioParticipationDeny) {
                singleMCQModel.getSingleMCQSettings().setParticipateAfterContestEnd(false);
                Log.d(TAG, "ParticipationExpiration: deny participation after contest ends");
                radioParticipationAllow.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioParticipationDeny.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        rgCorrectAnswerVisibility.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioCorrectAnsVisibleContestEnds) {
                Log.d(TAG, "clickListeners: radioCorrectAnsVisibleContestEnds");
                singleMCQModel.getSingleMCQSettings().setCorrectAnswerVisibilityDate(CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS);
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioCorrectAnsVisibleImmediately) {
                Log.d(TAG, "clickListeners: radioCorrectAnsVisibleImmediately");
                singleMCQModel.getSingleMCQSettings().setCorrectAnswerVisibilityDate(CORRECT_ANSWER_VISIBILITY_IMMEDIATELY);
                radioCorrectAnsVisibleContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioCorrectAnsVisibleImmediately.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            Log.d(TAG, "Correct Answer Visibility: " + singleMCQModel.getSingleMCQSettings().getCorrectAnswerVisibilityDate());


        });

        // region Leaderboard
        rgLeaderBoardPublish.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioLeaderBoardPublishContinue:
                    singleMCQModel.getSingleMCQSettings().setLeaderboardPublishDate(LEADERBOARD_PUBLISH_TYPE_CONTINUE);
                    radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.colorPrimary));
                    radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.primary_text_color));

                    break;

                case R.id.radioLeaderBoardPublishContestEnds:

                    singleMCQModel.getSingleMCQSettings().setLeaderboardPublishDate(LEADERBOARD_PUBLISH_TYPE_CONTEST_END);
                    radioLeaderBoardPublishContinue.setTextColor(getResources().getColor(R.color.primary_text_color));
                    radioLeaderBoardPublishContestEnds.setTextColor(getResources().getColor(R.color.colorPrimary));

                    break;
            }
            Log.d(TAG, "LeaderBoardPublish: " + singleMCQModel.getSingleMCQSettings().getLeaderboardPublishDate());
        });

        //region Leaderboard-privacy
        rgLeaderBoardPrivacy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLeaderBoardPrivacyParticipants) {

                singleMCQModel.getSingleMCQSettings().setLeaderboardPrivacy(LEADERBOARD_PRIVACY_PARTICIPANTS);

                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.primary_text_color));

            } else if (checkedId == R.id.radioLeaderBoardPrivacyPublic) {

                singleMCQModel.getSingleMCQSettings().setLeaderboardPrivacy(LEADERBOARD_PRIVACY_PUBLIC);

                radioLeaderBoardPrivacyParticipants.setTextColor(getResources().getColor(R.color.primary_text_color));
                radioLeaderBoardPrivacyPublic.setTextColor(getResources().getColor(R.color.colorPrimary));

            }

            Log.d(TAG, "LeaderBoardPrivacy: " + singleMCQModel.getSingleMCQSettings().getLeaderboardPrivacy());
        });
        //endregion

        // region RaffleDraw
        checkboxRaffleDraw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            singleMCQModel.getSingleMCQSettings().setRaffleDrawEnabled(isChecked);
            Log.d(TAG, "setRaffleDrawEnabled: " + singleMCQModel.getSingleMCQSettings().isRaffleDrawEnabled());
        });

        // endregion

        // endregion

        btnPublish.setOnClickListener(v -> {
            Gson gson = new Gson();
            Log.d(TAG, "SingleMCQSettings: " + gson.toJson(singleMCQModel.getSingleMCQSettings()));

            if (validateSingleMCQSettings()) {
                Log.d(TAG, "initializeClickListeners: Single MCQ is OK");
                startSingleMultipleChoicePostNetworkCall();

            }
        });

    }
    //endregion

    //region validateSingleMCQSettings
    private boolean validateSingleMCQSettings() {

        if (switchContestMode.isChecked()) {
            if (singleMCQModel.getSingleMCQSettings().getQuestionCategory().size() == 0) {
                Toast.makeText(context, "Please select a category for the Question.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (QxmStringIntegerChecker.isLongInt(singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate())) {
                Long contestExpTime = Long.parseLong(singleMCQModel.getSingleMCQSettings().getSingleMCQExpirationDate());
                Long currentTime = Calendar.getInstance().getTimeInMillis();

                if (contestExpTime < currentTime) {
                    Toast.makeText(context, "Please specify a valid Contest End Time.", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(context, "Please specify Contest End Time", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!TextUtils.isEmpty(etNumberOfWinners.getText())) {
                int numberOfWinner = Integer.parseInt(etNumberOfWinners.getText().toString());
                if (numberOfWinner <= 0) {
                    Toast.makeText(context, "Number of winner must be a positive number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (numberOfWinner > 1000) {
                    Toast.makeText(context, "Maximum number of winner 1000 winners are allowed.", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(context, "Please specify the total number of the contest winner.", Toast.LENGTH_SHORT).show();
                return false;
            }


        } else {

            if (singleMCQModel.getSingleMCQSettings().getQuestionCategory().size() == 0) {
                Toast.makeText(context, "Please select a category for the Question.", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return true;
    }
    //endregion

    //region chooseDateAndTime
    private void chooseDateAndTime(Context context, String chooseDateFor) {

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

                case MCQ_EXPIRATION_DATE_TIME:
                    singleMCQModel.getSingleMCQSettings().setSingleMCQExpirationDate(String.valueOf(calendar.getTimeInMillis()));
                    tvSMCQExpirationTime.setText(String.format("Your Single MCQ will be expired on %s", expireDateString));
                    tvSMCQExpirationTime.setVisibility(View.VISIBLE);
                    break;
                case CONTEST_END_CHOOSE_DATE_TIME:
                    Log.d(TAG, "onTimeSet: CONTEST_END_CHOOSE_DATE_TIME:" + expireDateString);
                    singleMCQModel.getSingleMCQSettings().setSingleMCQExpirationDate(String.valueOf(calendar.getTimeInMillis()));
                    checkBoxContestEndSpecificDate.setText(String.format("Contest ends on %s", expireDateString));
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
            String expireDateMessage = "Your Qxm will be expired on: " + dateString;

            Log.d(TAG, "onDateSet: " + expireDateMessage);

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }

    //endregion

    // region PopulateDataIntoRecyclerView

    private void populateDataIntoRecyclerView() {

        ArrayList<String> qxmCategoryList = QxmCategoryHelper.getQxmCategoryList(context);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                3, GridLayoutManager.HORIZONTAL, false);

        rvMCQCategory.setLayoutManager(gridLayoutManager);
        rvMCQCategory.setHasFixedSize(true);
        rvMCQCategory.setNestedScrollingEnabled(false);

        QxmCategoryAdapter qxmCategoryAdapter = new QxmCategoryAdapter(context, qxmCategoryList, singleMCQModel.getSingleMCQSettings().getQuestionCategory(), this);
        rvMCQCategory.setAdapter(qxmCategoryAdapter);

    }

    // endregion

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

    @Override
    public void onCategorySelected(RelativeLayout interestMainContent, TextView categoryTV, int position) {
        String selectedOrDeselectedCategory = categoryTV.getText().toString().trim();

        int maxCategorySelection = 1;

        if (interestMainContent.getBackground() != null) {
            singleMCQModel.getSingleMCQSettings().getQuestionCategory().remove(selectedOrDeselectedCategory);
            interestMainContent.setBackgroundResource(0);

            if (categorySelected != 0) categorySelected--;

        } else if (categorySelected < maxCategorySelection) {
            singleMCQModel.getSingleMCQSettings().getQuestionCategory().add(selectedOrDeselectedCategory);

            interestMainContent.setBackground(getResources().getDrawable(R.drawable.background_qxm_category_grid_item));
            categorySelected++;

        } else {
            Toast.makeText(context, "You can only select one category for a the Single Multiple Choice Question.", Toast.LENGTH_SHORT).show();
        }
    }

    //region startSingleMultipleChoicePostNetworkCall

    private void startSingleMultipleChoicePostNetworkCall() {

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(getContext());
        qxmProgressDialog.showProgressDialog("Posting Multiple Choice", "Multiple choice is being created, please wait...", false);

        Call<QxmApiResponse> createSingleMultipleChoiceNetworkCall = apiService.createSingleMultipleChoice(token.getToken(), token.getUserId(), singleMCQModel);

        createSingleMultipleChoiceNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse: createPollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                qxmProgressDialog.closeProgressDialog();

                if (response.code() == 201) {

                    Toast.makeText(getContext(), "Question posted successfully!", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.loadHomeFragment();

                } else if (response.code() == 400) {
                    Toast.makeText(context, R.string.the_minimum_interval_time_between_post_message, Toast.LENGTH_SHORT).show();

                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {

                    Log.d(TAG, "onResponse: createPollNetworkCall failed");
                    Toast.makeText(getContext(), "Network error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(getContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //endregion

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


}
