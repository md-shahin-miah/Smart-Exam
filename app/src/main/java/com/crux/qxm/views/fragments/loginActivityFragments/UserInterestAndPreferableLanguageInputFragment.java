package com.crux.qxm.views.fragments.loginActivityFragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.QxmCategoryAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.userInterestAndPreferableLanguageInputFragmentFeature.DaggerUserInterestAndPreferableLanguageInputFragmentComponent;
import com.crux.qxm.di.userInterestAndPreferableLanguageInputFragmentFeature.UserInterestAndPreferableLanguageInputFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmCategoryHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmPreferableLanguagePickerDialog;
import com.crux.qxm.views.activities.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInterestAndPreferableLanguageInputFragment extends Fragment implements QxmCategoryAdapter.CategorySelectListener {

    // region Fragment-Properties
    private static final String TAG = "UserInterestAndPreferab";

    @Inject
    Retrofit retrofit;
    @Inject
    Picasso picasso;

    private Context context;
    private int categorySelected = 0;
    private ArrayList<String> userInterests = new ArrayList<>();
    private String usersBirthDayInMillis = "";

    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private ArrayList<String> userPreferredLanguages;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region BindViewWithButterKnife

    @BindView(R.id.rvQxmCategory)
    RecyclerView rvQxmCategory;
    @BindView(R.id.tvAddLanguage)
    AppCompatTextView tvAddLanguage;
    @BindView(R.id.tvChoosePreferableLanguageHint)
    AppCompatTextView tvChoosePreferableLanguageHint;
    @BindView(R.id.tvSelectedPreferableLanguages)
    AppCompatTextView tvSelectedPreferableLanguages;
    @BindView(R.id.rgUserGender)
    RadioGroup rgUserGender;
    @BindView(R.id.etUserDateOfBirth)
    AppCompatEditText etUserDateOfBirth;
    @BindView(R.id.rlMakeDobVisible)
    RelativeLayout rlMakeDobVisible;
    @BindView(R.id.checkboxMakeDobPublic)
    AppCompatCheckBox checkboxMakeDobPublic;
    @BindView(R.id.btnNext)
    AppCompatButton btnNext;

    // endregion

    // region Fragment-Constructor
    public UserInterestAndPreferableLanguageInputFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_interest_and_prefarable_language_input, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        UserInterestAndPreferableLanguageInputFragmentComponent userInterestAndPreferableLanguageInputFragmentComponent
                = DaggerUserInterestAndPreferableLanguageInputFragmentComponent.builder()
                .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                .build();

        userInterestAndPreferableLanguageInputFragmentComponent.injectUserInterestAndPreferableLanguageInputFragmentFeature(
                UserInterestAndPreferableLanguageInputFragment.this
        );
    }

    // endregion

    // region Init

    private void init(View view) {
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        userPreferredLanguages = new ArrayList<>();

        Log.d(TAG, "init: userPreferredLanguages size = " + userPreferredLanguages.size());

        populateDataIntoRecyclerView();

        btnNext.setOnClickListener(v -> {

            if (userInterests.size() < 3) {

                Toast.makeText(context, "Please select at least three interests, it will help us to generate your customized feed.", Toast.LENGTH_LONG).show();
            }
            else if(rgUserGender.getCheckedRadioButtonId() == -1){
                Toast.makeText(context, R.string.select_your_gender, Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(etUserDateOfBirth.getText())){
                Toast.makeText(context, "Provide your date of birth", Toast.LENGTH_SHORT).show();
            }
            else{

                Log.d(TAG, "init: necessary data for this fragment are provided provided ");
                sendUserInterestAndPreferableLanguageNetworkCall();
            }

            Log.d(TAG, "init: "+rgUserGender.getCheckedRadioButtonId());

        });
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {

        tvAddLanguage.setOnClickListener(v -> {

            Log.d(TAG, "tvAddLanguage Clicked: userPreferredLanguages size" + userPreferredLanguages.size());
            Log.d(TAG, "tvAddLanguage Clicked: userPreferredLanguages values" + userPreferredLanguages.toString());

            QxmPreferableLanguagePickerDialog qxmPreferableLanguagePickerDialog =
                    new QxmPreferableLanguagePickerDialog(v.getRootView(), getActivity(), context, userPreferredLanguages,
                            tvChoosePreferableLanguageHint,
                            tvSelectedPreferableLanguages);

            qxmPreferableLanguagePickerDialog.showPreferableLanguagePickerDialog();


        });

        etUserDateOfBirth.setOnClickListener(v->{

            showDatePicker();
        });

        rlMakeDobVisible.setOnClickListener(v->{

            if(!checkboxMakeDobPublic.isChecked())
                checkboxMakeDobPublic.setChecked(true);
            else
                checkboxMakeDobPublic.setChecked(false);

        });


    }

    // endregion

    // region SendUserInterestAndPreferableLanguageNetworkCall

    private void sendUserInterestAndPreferableLanguageNetworkCall() {



        if(userPreferredLanguages.size() == 0)
            userPreferredLanguages.add("English");

        // region user's gender and date of birth

        // gender
        String userGender = "";

        if((int)rgUserGender.getCheckedRadioButtonId() == 1){
            userGender = "Male";
        }else if((int)rgUserGender.getCheckedRadioButtonId() == 2){
            userGender = "Female";
        }
        else if((int)rgUserGender.getCheckedRadioButtonId() == 3){
            userGender = "Other";
        }

        Log.d(TAG, "Users Gender: "+userGender);

        //date of birth
        String userDateOfBirth = usersBirthDayInMillis;

        // whether date of birth is public or not
        String userDobPrivacy= "Private";

        if(checkboxMakeDobPublic.isChecked())
            userDobPrivacy = "Public";

        //endregion


        Call<QxmApiResponse> postUserInterestsAndPreferredLanguages = apiService.postUserInterestAndPreferredLanguages(
                token.getToken(), token.getUserId(), userInterests, userPreferredLanguages,userGender,userDateOfBirth,userDobPrivacy);

        postUserInterestsAndPreferredLanguages.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: postUserInterestsAndPreferredLanguages");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: success");

                    // save user sign up steps status
                    //realmService.saveUserSignUpProcessDone(false, true);
                    realmService.removeSavedSignUpStatus();

                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    Objects.requireNonNull(getActivity()).finish();

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: failed");
                    Toast.makeText(context, "Network request time out. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: postUserInterestsAndPreferredLanguages");
                Log.d(TAG, "onFailure: error localized message: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: error stack trace: " + Arrays.toString(t.getStackTrace()));

                Toast.makeText(context, "Network error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // endregion

    // region PopulateDataIntoRecyclerView

    private void populateDataIntoRecyclerView() {

        ArrayList<String> qxmCategoryList = QxmCategoryHelper.getQxmCategoryList(context);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,
                3, GridLayoutManager.HORIZONTAL, false);

        rvQxmCategory.setLayoutManager(gridLayoutManager);
        rvQxmCategory.setHasFixedSize(true);
        rvQxmCategory.setNestedScrollingEnabled(false);

        QxmCategoryAdapter qxmCategoryAdapter = new QxmCategoryAdapter(context, qxmCategoryList, userInterests, this);
        rvQxmCategory.setAdapter(qxmCategoryAdapter);

    }

    @Override
    public void onCategorySelected(RelativeLayout interestMainContent, TextView categoryTV, int position) {
        String selectedOrDeselectedCategory = categoryTV.getText().toString().trim();

        int maxCategorySelection = 27;
        if (interestMainContent.getBackground() != null) {
            userInterests.remove(selectedOrDeselectedCategory);
            interestMainContent.setBackgroundResource(0);

            if (categorySelected != 0) categorySelected--;

        } else if (categorySelected < maxCategorySelection) {
            userInterests.add(selectedOrDeselectedCategory);

            interestMainContent.setBackground(getResources().getDrawable(R.drawable.background_qxm_category_grid_item));
            categorySelected++;

        } else {
            Toast.makeText(context, "You have selected maximum number of Interests.", Toast.LENGTH_SHORT).show();
        }
    }

    // endregion

    //region showDatePicker
    private void showDatePicker(){

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            String dateString = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year);
            String dob = "Your selected date is: " + dateString;
            Log.d(TAG, "onDateSet: " + dob);
            usersBirthDayInMillis = String.valueOf(calendar.getTimeInMillis());
            Log.d(TAG, "showDatePicker selected Birth Days in millis: "+usersBirthDayInMillis);
            etUserDateOfBirth.setText(dateString);

        };

        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle("Select Date of Birth");
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

        Log.d(TAG, "showDatePicker: called");

    }
    //endregion

}
