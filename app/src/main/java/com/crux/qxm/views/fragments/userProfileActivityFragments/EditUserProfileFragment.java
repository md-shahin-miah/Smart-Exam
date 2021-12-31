package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.InstitutionAdapter;
import com.crux.qxm.adapter.profile.InterestAdapter;
import com.crux.qxm.adapter.profile.OccupationAdapter;
import com.crux.qxm.adapter.profile.SocialAdapter;
import com.crux.qxm.adapter.profile.WebsiteAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.enroll.SocialAddress;
import com.crux.qxm.db.models.users.Address;
import com.crux.qxm.db.models.users.Institution;
import com.crux.qxm.db.models.users.Occupation;
import com.crux.qxm.db.models.users.UserInfoDetails;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.EditProfileFragmentFeature.DaggerEditProfileFragmentComponent;
import com.crux.qxm.di.EditProfileFragmentFeature.EditProfileFragmentComponent;
import com.crux.qxm.utils.Model;
import com.crux.qxm.adapter.MultipleSelectAdapter;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmCategoryHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmPreferableLanguageUpdateDialog;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserProfileFragment extends Fragment implements MultipleSelectAdapter.ChnageStatusListener {

    private static final String TAG = "EditProfileFragment";
    public static ArrayList<Object> mainList = new ArrayList<>();
    public static JSONObject mainJsonObj = new JSONObject();


    private RecyclerView rvQxmInterestCategory = null;
    private MultipleSelectAdapter MultipleSelectAdapter = null;
    private ArrayList<Model> models;
    private ArrayList<String> newInterestlist = new ArrayList<>();


    @Inject
    Retrofit retrofit;

    AppCompatImageView ivChangeUserName, ivChangeDesc, ivEditFullName, ivEditUsername, ivEditEmail,
            ivEditCountryName, ivEditLivingAddress, ivBackArrow, ivGPS, ivEditCityName, ivChangePreferableLang, ivEditGender, ivEditDob;
    AppCompatTextView tcFullName, tvUserName, tvEmail, tvCountryName, tvLivingAddress, tvCityAddress, descriptionTV, tvPreferableLanguage, tvDob, tvGender;
    AppCompatButton btAddSocial, btAddWebsite, btUseGps, btAddOccupation, btAddInterest, btAddEducation;
    RecyclerView rvEducation, rvSocial, rvWebsite, rvOccupation, rvInterest;
    List<SocialAddress> socialAddresses;

    NestedScrollView nvEditProfile;

    private QxmApiService apiService;
    private Context context;
    private Realm realm;
    private RealmService realmService;
    private QxmToken token;
    private UserInfoDetails user;
    private UserBasic userBasic;
    private List<String> websiteList;
    private List<Occupation> occupationList;
    private List<String> interestList = null;
    private List<Institution> institutionList;

    public EditUserProfileFragment() {
        // Required empty public constructor
    }


    // region Fragment-NewInstance

    public static EditUserProfileFragment newInstance() {

        Bundle args = new Bundle();
        EditUserProfileFragment fragment = new EditUserProfileFragment();
        fragment.setArguments(args);
        return fragment;

    }

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    // region SetUpDagger2

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buildResources();
        context = view.getContext();

        setUpDagger2(context);

        init(view);

        getUserDetails();

        setClickListeners();
    }

    // endregion

    // region GetUserDetails

    private void setUpDagger2(Context context) {

        EditProfileFragmentComponent editProfileFragmentComponent =
                DaggerEditProfileFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        editProfileFragmentComponent.injectEditProfileFragmentFeature(EditUserProfileFragment.this);

    }

    // endregion

    // region Init

    private void getUserDetails() {

        Log.d(TAG, "getUserDetails: token = " + token.getToken());

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(getContext());
        qxmProgressDialog.showProgressDialog("Loading Profile Details", "Profile is being loaded, please wait...", false);

        Call<UserInfoDetails> getUserProfileInfoDetails = apiService.getUserProfileInfoDetails(token.getToken(), token.getUserId());

        getUserProfileInfoDetails.enqueue(new Callback<UserInfoDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserInfoDetails> call, @NonNull Response<UserInfoDetails> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                qxmProgressDialog.closeProgressDialog();

                if (response.code() == 200 || response.code() == 201) {

                    user = response.body();

                    assert response.body() != null;
                    if (response.body().getInstitutionList() == null) {
                        List<Institution> institutionList = new ArrayList<>();
                        user.setInstitutionList(institutionList);
                    }

                    if (response.body().getSocialSite() == null) {

//                        List<SocialSite> socialSiteList = new ArrayList<>();
//                        user.setSocialSite(socialSiteList);
                    }

                    if (response.body().getOccupationList() == null) {
                        List<Occupation> occupationList = new ArrayList<>();
                        user.setOccupationList(occupationList);
                    }

                    if (response.body().getWebsiteList() == null) {
                        List<String> websiteList = new ArrayList<>();
                        user.setWebsiteList(websiteList);
                    }

                    if (response.body().getInterestList() == null) {
                        List<String> interestList = new ArrayList<>();
                        user.setInterestList(interestList);
                    }

                    setValueInViews();
                    nvEditProfile.setVisibility(View.VISIBLE);

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfoDetails> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: getUserProfileInfo");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                qxmProgressDialog.closeProgressDialog();
                Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    // endregion

    // region SetValuesInViews

    private void init(View view) {

        apiService = retrofit.create(QxmApiService.class);
        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        token = realmService.getApiToken();
        userBasic = realmService.getSavedUser();

        ivBackArrow = view.findViewById(R.id.ivBackArrow);
        ivChangeDesc = view.findViewById(R.id.ivChangeDesc);
        ivGPS = view.findViewById(R.id.ivGPS);
        tcFullName = view.findViewById(R.id.tvFullName);
        tvUserName = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.emailTV);
        descriptionTV = view.findViewById(R.id.tvDescription);
        tvCountryName = view.findViewById(R.id.tvCountryName);
        tvCityAddress = view.findViewById(R.id.tvCityAddress);
        tvLivingAddress = view.findViewById(R.id.tvLivingAddress);
        btAddSocial = view.findViewById(R.id.btAddSocial);
        btAddEducation = view.findViewById(R.id.btAddEducation);
        btAddInterest = view.findViewById(R.id.btAddInterest);
        btAddWebsite = view.findViewById(R.id.btAddWebsite);
        btUseGps = view.findViewById(R.id.btAddGPS);
        btAddOccupation = view.findViewById(R.id.btAddOccupation);
        rvEducation = view.findViewById(R.id.rvEducation);
        rvOccupation = view.findViewById(R.id.rvOccupation);
        rvInterest = view.findViewById(R.id.interestRV);
        ivEditFullName = view.findViewById(R.id.ivEditFullName);
        ivEditUsername = view.findViewById(R.id.ivEditUsername);
        ivEditEmail = view.findViewById(R.id.editEmailIV);
        ivEditCountryName = view.findViewById(R.id.ivEditCountryName);
        ivEditCityName = view.findViewById(R.id.ivEditCityName);
        ivEditLivingAddress = view.findViewById(R.id.ivEditLivingAddress);
        rvSocial = view.findViewById(R.id.rvSocial);
        rvWebsite = view.findViewById(R.id.rvWebsite);
        nvEditProfile = view.findViewById(R.id.nvEditProfile);
        ivChangePreferableLang = view.findViewById(R.id.ivChangePreferableLang);
        tvPreferableLanguage = view.findViewById(R.id.tvPreferableLanguage);
        tvGender = view.findViewById(R.id.genderTV);
        ivEditGender = view.findViewById(R.id.changeGenderIV);
        tvDob = view.findViewById(R.id.dobTV);
        ivEditDob = view.findViewById(R.id.changeDobIV);


    }

    // endregion

    // region SetClickListeners

    private void setValueInViews() {

        tcFullName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        tvEmail.setText(user.getEmail());
        descriptionTV.setText(user.getDescription());

        String userName = user.getUserName();
        // String userNameArray[] = userName.split("/");
        //        // tvUserName.setText(userNameArray[1]);
        tvUserName.setText(userName);

        if (user.getGender() != null) {
            tvGender.setText(user.getGender());
        }
        if (user.getDateOfBirth() != null) {
            String dob = TimeFormatString.getDate(Long.parseLong(user.getDateOfBirth()));
            tvDob.setText(dob);
        }


        // setting user address

        if (user.getAddressesList().size() != 0) {
            Address address = user.getAddressesList().get(0);
            tvLivingAddress.setText(address.getUserAddress());
            tvCountryName.setText(address.getUserCountry());
            tvCityAddress.setText(address.getUserCity());
        }

        if (user.getSocialSite() == null) {

            socialAddresses = new ArrayList<>();
            SocialAdapter socialAdapter = new SocialAdapter(getContext(), socialAddresses, apiService, token);
            rvSocial.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSocial.setAdapter(socialAdapter);
        }

        // getting all institution from user using gson
        institutionList = user.getInstitutionList();
        Log.d("InstitutionList", institutionList.toString());
        Collections.sort(institutionList);
        rvEducation.setLayoutManager(new LinearLayoutManager(getContext()));
        InstitutionAdapter institutionAdapter = new InstitutionAdapter(getContext(), institutionList, apiService, token);
        rvEducation.setAdapter(institutionAdapter);
        rvEducation.setNestedScrollingEnabled(false);


        if (user.getSocialSite() != null) {

            List<String> facebookList = user.getSocialSite().getFacebookList();
            List<String> instagramList = user.getSocialSite().getInstragramList();
            List<String> youtubeList = user.getSocialSite().getYoutubeList();
            List<String> linkedinList = user.getSocialSite().getLinkedinList();
            List<String> twitterList = user.getSocialSite().getTwitterList();

            Log.d("facebookList", facebookList.toString());
            Log.d("instagramList", instagramList.toString());
            Log.d("youtubeList", youtubeList.toString());
            Log.d("linkedinList", linkedinList.toString());
            Log.d("twitterList", twitterList.toString());

            // Log.d("SocialArray",socialArray.toString());
            // JSONObject socialObj = socialArray.getJSONObject(0);
            socialAddresses = new ArrayList<>();

            for (String facebookLink : facebookList) {
                SocialAddress fbAddress = new SocialAddress(facebookLink, SocialAddress.SocialType.FACEBOOK);
                socialAddresses.add(fbAddress);
            }
            for (String instaLink : instagramList) {
                SocialAddress instaAddress = new SocialAddress(instaLink, SocialAddress.SocialType.INSTAGRAM);
                socialAddresses.add(instaAddress);
            }
            for (String linkedinLink : linkedinList) {
                SocialAddress linkedinAddress = new SocialAddress(linkedinLink, SocialAddress.SocialType.LINKEDIN);
                socialAddresses.add(linkedinAddress);
            }
            for (String youtubeLink : youtubeList) {
                SocialAddress youtubeAddress = new SocialAddress(youtubeLink, SocialAddress.SocialType.YOUTUBE);
                socialAddresses.add(youtubeAddress);
            }
            for (String twitterLink : twitterList) {
                SocialAddress twitterAddress = new SocialAddress(twitterLink, SocialAddress.SocialType.TWITTER);
                socialAddresses.add(twitterAddress);
            }

            Log.d("SocialAddressList", socialAddresses.toString());

            SocialAdapter socialAdapter = new SocialAdapter(getContext(), socialAddresses, apiService, token);
            rvSocial.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSocial.setAdapter(socialAdapter);
            rvSocial.setNestedScrollingEnabled(false);


        }
        if (user.getSocialSite() == null) {

            socialAddresses = new ArrayList<>();
            SocialAdapter socialAdapter = new SocialAdapter(getContext(), socialAddresses, apiService, token);
            rvSocial.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSocial.setAdapter(socialAdapter);
        }

        //adding user interest
        interestList = user.getInterestList();
        interestList.removeAll(Arrays.asList("",null));
        InterestAdapter interestAdapter = new InterestAdapter(getContext(), interestList, apiService, token);
        rvInterest.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInterest.setAdapter(interestAdapter);
        rvInterest.setNestedScrollingEnabled(false);


        Log.d(TAG, "setValueInViews: interestList " + interestList.toString());

        // adding users occupation
        occupationList = user.getOccupationList();
        OccupationAdapter occupationAdapter = new OccupationAdapter(getContext(), occupationList, apiService, token);
        rvOccupation.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOccupation.setAdapter(occupationAdapter);
        rvOccupation.setNestedScrollingEnabled(false);

        // users website

        websiteList = user.getWebsiteList();
        rvWebsite.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("WebsiteList", user.getWebsiteList().toString());
        WebsiteAdapter websiteAdapter = new WebsiteAdapter(getContext(), websiteList, apiService, token);
        rvWebsite.setAdapter(websiteAdapter);
        rvWebsite.setNestedScrollingEnabled(false);


        tvPreferableLanguage.setText(QxmArrayListToStringProcessor.getStringFromArrayListWithNewLine(
                user.getLanguageList()
        ));


    }

    // endregion

    private void setClickListeners() {


        ivBackArrow.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });


        // region EditFulName

        ivEditFullName.setOnClickListener(v -> {

            Log.d(TAG, "editFullNameIV: clicked");

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_edit_fullname, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText firstNameET = bottomSheetView.findViewById(R.id.firstNameET);
            EditText middleNameET = bottomSheetView.findViewById(R.id.middleNameET);
            EditText lastNameET = bottomSheetView.findViewById(R.id.lastNameET);

            if (user.getFirstName() != null) {
                Log.d(TAG, "editFullNameIV:  " + user.getFirstName());
                firstNameET.setText(user.getFirstName());
            }
            if (user.getLastName() != null) {
                Log.d(TAG, "editFullNameIV:  " + user.getLastName());
                lastNameET.setText(user.getLastName());
            }

            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);

            doneIV.setOnClickListener(v2 -> {

                String firstName = "";
                if (!firstNameET.getText().toString().isEmpty())
                    firstName = firstNameET.getText().toString().trim();
                String middleName = "";
                if (!middleNameET.getText().toString().isEmpty())
                    middleName = middleNameET.getText().toString().trim();
                String lastName = "";
                if (!lastNameET.getText().toString().isEmpty())
                    lastName = lastNameET.getText().toString().trim();
                String fullName = firstName + " " + middleName + " " + lastName;

                if (!fullName.trim().isEmpty()) {
                    tcFullName.setText(fullName.trim());
                    mainList.add(fullName.trim());
                    user.setFirstName(firstName.trim() + " " + middleName.trim());
                    user.setLastName(lastName.trim());
                    user.setFullName(fullName.trim());

                    // updating fullName in database
                    updateFullName();
                    bottomSheetDialog.hide();
                } else
                    Toast.makeText(getContext(), "Full name can not be empty ", Toast.LENGTH_SHORT).show();


            });

        });

        // endregion

        // region EditUsername

        ivEditUsername.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            TextView usernameHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);
            usernameHintTV.setText("New username");
            EditText usernameET = bottomSheetView.findViewById(R.id.basicInfoET);
            usernameET.setText(tvUserName.getText());

            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);


            doneIV.setOnClickListener(v2 -> {

                if (!usernameET.getText().toString().isEmpty()) {

                    tvUserName.setText(usernameET.getText().toString().trim());
                    String username = tvUserName.getText().toString().trim();
                    user.setUserName(username);
                    // updating username in database
                    updateUsername();
                    bottomSheetDialog.hide();
                } else
                    Toast.makeText(getContext(), "username can not be empty", Toast.LENGTH_SHORT).show();


            });

        });

        // endregion

        // region EditGender

        ivEditGender.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_layout_user_gender, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            RadioGroup rgGender = bottomSheetView.findViewById(R.id.rgUserGender);
            AppCompatRadioButton rbGenderMale = bottomSheetView.findViewById(R.id.rbGenderMale);
            AppCompatRadioButton rbGenderFemale = bottomSheetView.findViewById(R.id.rbGenderFemale);
            AppCompatRadioButton rbGenderOther = bottomSheetView.findViewById(R.id.rbGenderOther);

            if (!TextUtils.isEmpty(tvGender.getText())) {

                String currentGender = tvGender.getText().toString();

                switch (currentGender) {
                    case "Male":
                        rbGenderMale.setChecked(true);
                        break;
                    case "Female":
                        rbGenderFemale.setChecked(true);
                        break;
                    case "Other":
                        rbGenderOther.setChecked(true);
                        break;
                }
            }


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            doneIV.setOnClickListener(v2 -> {

                String userGender = "";

                if (rbGenderMale.isChecked()) {
                    userGender = "Male";
                } else if (rbGenderFemale.isChecked()) {
                    userGender = "Female";
                } else if (rbGenderOther.isChecked()) {
                    userGender = "Other";
                }

                tvGender.setText(userGender);
                bottomSheetDialog.hide();

                // user's gender network call
                updateUserGender(userGender);


            });

        });

        // endregion

        //region edit dob
        ivEditDob.setOnClickListener(v -> {
            if (user.getDateOfBirth() != null)
                showDatePicker(user.getDateOfBirth());
            else showDatePicker(String.valueOf(System.currentTimeMillis()));
        });
        //endregion

        // region  EditEmail

        ivEditEmail.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, (ViewGroup) v.getRootView(), false);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            TextView usernameHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);
            usernameHintTV.setText("New Email");
            EditText emailET = bottomSheetView.findViewById(R.id.basicInfoET);
            emailET.setText(tvEmail.getText());

            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);

            doneIV.setOnClickListener(v2 -> {

                if (!emailET.getText().toString().isEmpty()) {

                    String previousEmail = tvEmail.getText().toString().trim();
                    String newEmail = emailET.getText().toString().trim();

                    Log.d(TAG, "setClickListeners previousEmail: " + previousEmail);
                    Log.d(TAG, "setClickListeners newEmail: " + newEmail);

                    tvEmail.setText(emailET.getText().toString().trim());

                    user.setEmail(newEmail);

                    // updating email in database
                    updateUserEmail(previousEmail, newEmail);
                    bottomSheetDialog.hide();
                } else
                    Toast.makeText(getContext(), "Email can not be empty", Toast.LENGTH_SHORT).show();
            });

        });

        // endregion


        // region EditUserDescription

        ivChangeDesc.setOnClickListener(v -> {


            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_description_edit, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();


            EditText descriptionET = bottomSheetView.findViewById(R.id.descriptionET);
            descriptionET.setText(descriptionTV.getText());

            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);


            doneIV.setOnClickListener(v2 -> {

                descriptionTV.setText(descriptionET.getText().toString().trim());
                String description = descriptionET.getText().toString().trim();
                //updating description in database
                user.setDescription(description);
                InfoUpdater.updateDescription(getContext(), apiService, token, description, "Description updated successfully!");
                bottomSheetDialog.hide();
            });
        });

        // endregion

        // region AddingUsersNewSocialSite

        btAddSocial.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_add_social_address, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            AppCompatSpinner selectSocialSP = bottomSheetView.findViewById(R.id.selectSocialSP);
            EditText socialLinkET = bottomSheetView.findViewById(R.id.socialLinkET);
            TextView tvSocialFixedPart = bottomSheetView.findViewById(R.id.tvSocialFixedPart);


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);


            String[] socialArr = {

                    "Facebook",
                    "Instagram",
                    "Linkedin",
                    "Youtube",
                    "Twitter"
            };

            ArrayAdapter<String> socialSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, socialArr);
            selectSocialSP.setAdapter(socialSpinnerAdapter);

            selectSocialSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (selectSocialSP.getSelectedItemPosition()) {

                        case 0:
                            Log.d("IsClicked", "true");
                            tvSocialFixedPart.setText("www.facebook.com/");
                            break;
                        case 1:
                            Log.d("IsClicked", "true");
                            tvSocialFixedPart.setText("www.instagram.com/");
                            break;
                        case 2:
                            Log.d("IsClicked", "true");
                            tvSocialFixedPart.setText("www.linkedin.com/");
                            break;
                        case 3:
                            Log.d("IsClicked", "true");
                            tvSocialFixedPart.setText("www.youtube.com/");
                            break;
                        case 4:
                            Log.d("IsClicked", "true");
                            tvSocialFixedPart.setText("www.twitter.com/");
                            break;

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            doneIV.setOnClickListener(vDone -> {


                String socialLink = tvSocialFixedPart.getText().toString().trim() + socialLinkET.getText().toString().trim();
                SocialAddress socialAddress;
                String socialSiteName;

                if (!socialLink.trim().isEmpty()) {

                    switch (selectSocialSP.getSelectedItem().toString()) {

                        case "Facebook":
                            socialAddress = new SocialAddress(socialLink, SocialAddress.SocialType.FACEBOOK);
                            socialAddresses.add(socialAddress);
                            socialSiteName = "facebook";
                            break;
                        case "Instagram":
                            socialAddress = new SocialAddress(socialLink, SocialAddress.SocialType.INSTAGRAM);
                            socialAddresses.add(socialAddress);
                            socialSiteName = "instagram";
                            break;
                        case "Linkedin":
                            socialAddress = new SocialAddress(socialLink, SocialAddress.SocialType.LINKEDIN);
                            socialAddresses.add(socialAddress);
                            socialSiteName = "linkedin";
                            break;
                        case "Youtube":
                            socialAddress = new SocialAddress(socialLink, SocialAddress.SocialType.YOUTUBE);
                            socialAddresses.add(socialAddress);
                            socialSiteName = "youtube";
                            break;
                        case "Twitter":
                            socialAddress = new SocialAddress(socialLink, SocialAddress.SocialType.TWITTER);
                            socialAddresses.add(socialAddress);
                            socialSiteName = "twitter";
                            break;
                        default:
                            socialSiteName = "";

                    }

                    InfoUpdater.updateSocialSite(context, apiService, token, socialSiteName, "", socialLink, "Successfully Added New Social Site!");
                    SocialAdapter socialAdapter1 = new SocialAdapter(getContext(), socialAddresses, apiService, token);
                    rvSocial.setAdapter(socialAdapter1);
                    socialAdapter1.notifyDataSetChanged();

                    bottomSheetDialog.hide();

                } else
                    Toast.makeText(getContext(), "Link can not be empty", Toast.LENGTH_SHORT).show();


            });

        });

        // endregion

        // region AddingUsersNewWebsite

        btAddWebsite.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_add_social, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText websiteLinkET = bottomSheetView.findViewById(R.id.websiteLinkET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);

            doneIV.setOnClickListener(view1 -> {

                if (!websiteLinkET.getText().toString().isEmpty()) {
                    websiteList.add(websiteLinkET.getText().toString());
                    WebsiteAdapter websiteAdapterNew = new WebsiteAdapter(getContext(), websiteList, apiService, token);
                    rvWebsite.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvWebsite.setAdapter(websiteAdapterNew);

                    // previousWebsite name will be left blank when add an item
                    String previousWebsite = "";
                    String newWebsite = websiteLinkET.getText().toString().trim();

                    Log.d(TAG, "onBindViewHolder previousWebsite" + previousWebsite);
                    Log.d(TAG, "onBindViewHolder newWebsite" + newWebsite);
                    Log.d(TAG, "onBindViewHolder linkType website");

                    // submitting website to database
                    InfoUpdater.editUpdateOrDeleteWebsite(getContext(), apiService, token, previousWebsite, newWebsite, "Website added successfully!");
                    bottomSheetDialog.hide();

                } else
                    Toast.makeText(getContext(), "Link can not be empty", Toast.LENGTH_SHORT).show();

            });

        });

        // endregion

        // region EditCountry

        ivEditCountryName.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText countryNameEt = bottomSheetView.findViewById(R.id.basicInfoET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            TextView countryHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);

            countryHintTV.setText("New Country");

            if (!tvCountryName.getText().toString().isEmpty()) {
                countryNameEt.setText(tvCountryName.getText().toString());
            }

            doneIV.setOnClickListener(vDone -> {

                if (!countryNameEt.getText().toString().isEmpty()) {

                    Address updatedAddress = new Address();

                    if (user.getAddressesList().size() == 0) {

                        updatedAddress.setId("blank_id");
                        updatedAddress.setUserCountry(countryNameEt.getText().toString().trim());
                        updatedAddress.setUserCity(tvCityAddress.getText().toString().trim());
                        updatedAddress.setUserAddress(tvLivingAddress.getText().toString().trim());

                    } else {

                        updatedAddress.setId(user.getAddressesList().get(0).getId());
                        updatedAddress.setUserCountry(countryNameEt.getText().toString().trim());
                        updatedAddress.setUserCity(tvCityAddress.getText().toString().trim());
                        updatedAddress.setUserAddress(tvLivingAddress.getText().toString().trim());

                    }
                    InfoUpdater.updateAddress(getContext(), apiService, token, updatedAddress, "Country updated successfully!");
                    tvCountryName.setText(countryNameEt.getText());
                    bottomSheetDialog.hide();

                } else {
                    Toast.makeText(context, "Country name can not be empty!", Toast.LENGTH_SHORT).show();
                }


            });

        });

        // endregion

        // region EditAddress

        ivEditLivingAddress.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText livingAddressEt = bottomSheetView.findViewById(R.id.basicInfoET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            TextView livingAddressHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);

            livingAddressHintTV.setText("New living address");

            if (!tvLivingAddress.getText().toString().isEmpty()) {
                livingAddressEt.setText(tvLivingAddress.getText().toString());
            }

            doneIV.setOnClickListener(vDone -> {

                if (!livingAddressEt.getText().toString().isEmpty()) {

                    Address updatedAddress = new Address();

                    if (user.getAddressesList().size() == 0) {

                        updatedAddress.setId("blank_id");
                        updatedAddress.setUserCountry(tvCountryName.getText().toString());
                        updatedAddress.setUserCity(tvCityAddress.getText().toString());
                        updatedAddress.setUserAddress(livingAddressEt.getText().toString().trim());

                    } else {

                        updatedAddress.setId(user.getAddressesList().get(0).getId());
                        updatedAddress.setUserCountry(tvCountryName.getText().toString());
                        updatedAddress.setUserCity(tvCityAddress.getText().toString());
                        updatedAddress.setUserAddress(livingAddressEt.getText().toString().trim());
                    }

                    InfoUpdater.updateAddress(getContext(), apiService, token, updatedAddress, "Address updated successfully!");
                    tvLivingAddress.setText(livingAddressEt.getText().toString().trim());
                    bottomSheetDialog.hide();
                } else {
                    Toast.makeText(context, "Living address can not be empty!", Toast.LENGTH_SHORT).show();
                }


            });
        });

        // endregion

        //region EditCityName
        ivEditCityName.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText cityNameEt = bottomSheetView.findViewById(R.id.basicInfoET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            TextView livingAddressHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);

            livingAddressHintTV.setText("New City");

            if (!tvCityAddress.getText().toString().isEmpty()) {
                cityNameEt.setText(tvCityAddress.getText().toString());
            }

            doneIV.setOnClickListener(vDone -> {

                if (!cityNameEt.getText().toString().isEmpty()) {

                    Address updatedAddress = new Address();


                    if (user.getAddressesList().size() == 0) {

                        updatedAddress.setId("blank_id");
                        updatedAddress.setUserCity(cityNameEt.getText().toString().trim());
                        updatedAddress.setUserCountry(tvCountryName.getText().toString());
                        updatedAddress.setUserAddress(tvLivingAddress.getText().toString());

                    } else {

                        updatedAddress.setId(user.getAddressesList().get(0).getId());
                        updatedAddress.setUserCity(cityNameEt.getText().toString().trim());
                        updatedAddress.setUserCountry(tvCountryName.getText().toString());
                        updatedAddress.setUserAddress(tvLivingAddress.getText().toString());

                    }


                    updatedAddress.setUserCity(cityNameEt.getText().toString().trim());
                    InfoUpdater.updateAddress(getContext(), apiService, token, updatedAddress, "City updated successfully!");
                    tvCityAddress.setText(cityNameEt.getText());
                    bottomSheetDialog.hide();
                } else {
                    Toast.makeText(context, "City name can not be empty!", Toast.LENGTH_SHORT).show();
                }


            });
        });

        // endregion

        // region AddingNewEducationalInstitution

        btAddEducation.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bottomSheetView = inflater.inflate(R.layout.instituition_edit_bottom_dialog, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            // bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            final EditText institutionET = bottomSheetView.findViewById(R.id.institutionET);
            final EditText degreeET = bottomSheetView.findViewById(R.id.degreeET);
            final EditText fieldOfStudyET = bottomSheetView.findViewById(R.id.fieldOfStudyET);
            final EditText gradeET = bottomSheetView.findViewById(R.id.gradeET);
            final EditText fromET = bottomSheetView.findViewById(R.id.fromET);
            final EditText toET = bottomSheetView.findViewById(R.id.toET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);


            fromET.setOnClickListener(view14 -> {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater12 = LayoutInflater.from(getContext());
                View timePickerView = inflater12.inflate(R.layout.time_picker, null);
                builder.setView(timePickerView);

                final NumberPicker studyDurationPicker = timePickerView.findViewById(R.id.studyDurationNP);
                studyDurationPicker.setMinValue(1970);
                studyDurationPicker.setMaxValue(2040);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Log.d("FROM", String.valueOf(studyDurationPicker.getValue()));
                        fromET.setText(String.valueOf(studyDurationPicker.getValue()));

                    }
                });

                dialog = builder.create();
                dialog.show();


            });

            toET.setOnClickListener(view12 -> {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                View timePickerView = inflater1.inflate(R.layout.time_picker, null);
                builder.setView(timePickerView);

                final NumberPicker studyDurationPicker = timePickerView.findViewById(R.id.studyDurationNP);
                studyDurationPicker.setMinValue(1970);
                studyDurationPicker.setMaxValue(2040);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.d("TO", String.valueOf(studyDurationPicker.getValue()));
                        toET.setText(String.valueOf(studyDurationPicker.getValue()));
                    }
                });
                dialog = builder.create();
                dialog.show();
            });


            doneIV.setOnClickListener(view13 -> {

                Institution newInstitution = new Institution();

                // passing no_id institution id when create a new institution
                // it is requirement of backend
                newInstitution.setId("no_id");

                if (!institutionET.getText().toString().isEmpty()) {

                    newInstitution.setInstituteName(institutionET.getText().toString());

                    if (!degreeET.getText().toString().isEmpty())
                        newInstitution.setDegree(degreeET.getText().toString());
                    else newInstitution.setDegree("");

                    if (!fieldOfStudyET.getText().toString().isEmpty())
                        newInstitution.setFieldOfStudy(fieldOfStudyET.getText().toString());
                    else newInstitution.setFieldOfStudy("");

                    if (!gradeET.getText().toString().isEmpty())
                        newInstitution.setGrade(gradeET.getText().toString());
                    else newInstitution.setGrade("");

                    String studyDuration = fromET.getText().toString().trim() + "-" + toET.getText().toString().trim();
                    newInstitution.setFromTo(studyDuration);

                    institutionList.add(newInstitution);
                    Collections.sort(institutionList);
                    InstitutionAdapter institutionAdapterNew = new InstitutionAdapter(getContext(), institutionList, apiService, token);
                    rvEducation.setAdapter(institutionAdapterNew);
                    // submitting new value to database

                    Log.d(TAG, "setClickListeners newInstitution : " + newInstitution);
                    InfoUpdater.addOrUpdateEducationalInstitute(getContext(), apiService, token, newInstitution, false, "Institute added successfully!");

                    bottomSheetDialog.hide();

                } else {
                    Toast.makeText(context, "You must provide institution name", Toast.LENGTH_SHORT).show();
                }


            });


        });

        // endregion

        // region AddingNewOccupation
        btAddOccupation.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.bottom_sheet_occupation_edit, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            AppCompatEditText etDesignation = bottomSheetView.findViewById(R.id.etDesignation);
            AppCompatEditText etOrganization = bottomSheetView.findViewById(R.id.etOrganization);
            AppCompatEditText etWorkDuration = bottomSheetView.findViewById(R.id.etWorkDuration);


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            doneIV.setOnClickListener(vDone -> {

                if (TextUtils.isEmpty(etDesignation.getText()) || TextUtils.isEmpty(etOrganization.getText())
//                        || TextUtils.isEmpty(etWorkDuration.getText())
                ) {


                    Toast.makeText(context, "Some fields my be empty", Toast.LENGTH_SHORT).show();

                } else {

                    Occupation newOccupation = new Occupation();
                    newOccupation.setId("no_id");
                    newOccupation.setDesignation(etDesignation.getText().toString().trim());
                    newOccupation.setOrganization(etOrganization.getText().toString().trim());
                    newOccupation.setWorkDuration(etWorkDuration.getText().toString().trim());

                    occupationList.add(newOccupation);
                    OccupationAdapter occupationAdapter = new OccupationAdapter(getContext(), occupationList, apiService, token);
                    rvOccupation.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvOccupation.setAdapter(occupationAdapter);
                    rvOccupation.setNestedScrollingEnabled(false);
                    occupationAdapter.notifyDataSetChanged();


                    InfoUpdater.addEditOrDeleteOccupation(getContext(), apiService, token, newOccupation, false, "Occupation added successfully!");
                    bottomSheetDialog.hide();

                }

            });


        });

        // endregion

        // region AddNewInterest

        btAddInterest.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.layout_interest, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);

            rvQxmInterestCategory = bottomSheetView.findViewById(R.id.rvQxmInterestCategory);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                    3, GridLayoutManager.HORIZONTAL, false);

            rvQxmInterestCategory.setLayoutManager(gridLayoutManager);
            rvQxmInterestCategory.setHasFixedSize(true);
            rvQxmInterestCategory.setNestedScrollingEnabled(false);
            MultipleSelectAdapter = new MultipleSelectAdapter(models, getActivity(), this);

//            QxmInterestAdapter qxmInterestAdapter = new QxmInterestAdapter(context, getAllInterests(), "", (interestMainContent, selectedInterest, position1) -> {
//
//                newInterest = selectedInterest;
//
//            });
            rvQxmInterestCategory.setAdapter(MultipleSelectAdapter);

            bottomSheetDialog.show();

            doneIV.setOnClickListener(vDone -> {

                updateList();


                int index = 0;
                for (String tempList : newInterestlist) {


                        if ((interestList.contains(tempList))) {
                            Toast.makeText(context, newInterestlist.get(index) + " is already in your interests!", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            interestList.add(newInterestlist.get(index));
                            String newintlist = newInterestlist.get(index);
                            InfoUpdater.addInterest(context, apiService, token, newintlist);
                            Log.d(TAG, "setClickListeners: Final int  " + interestList.toString());
                            Log.d(TAG, "setClickListeners: Final new  " + newInterestlist.toString());




                        }

                    index++;
                }


                InterestAdapter interestAdapterNew = new InterestAdapter(getContext(), interestList, apiService, token);
                rvInterest.setAdapter(interestAdapterNew);
                interestAdapterNew.notifyDataSetChanged();



                bottomSheetDialog.dismiss();
                newInterestlist.clear();

                Log.d(TAG, "setClickListeners: InterestAdding Called");

            });
//                            interestList.add(newInterestlist.get(index));
//
//








//            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_basic_profile, null);
//            bottomSheetDialog.setContentView(bottomSheetView);
//            //bottomSheetDialog.setCancelable(false);
//            bottomSheetDialog.show();
//
//            EditText interestET = bottomSheetView.findViewById(R.id.basicInfoET);
//            TextView interestHintTV = bottomSheetView.findViewById(R.id.basicInfoHintTV);
//            interestHintTV.setText("New interest");
//
//            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
//            doneIV.setOnClickListener(vDone -> {
//
//                if (!interestET.getText().toString().isEmpty()) {
//                    interestList.add(interestET.getText().toString());
//                    InterestAdapter interestAdapterNew = new InterestAdapter(getContext(), interestList,apiService,token);
//                    rvInterest.setAdapter(interestAdapterNew);
//                    InfoUpdater.addUpdateOrDeleteInterest(getContext(),apiService,token,interestList,false,"Interest added successfully!");
//                    bottomSheetDialog.hide();
//                } else
//                    Toast.makeText(getContext(), "Interest can not be empty", Toast.LENGTH_SHORT).show();
//
//            });

        });

        // endregion


        ivChangePreferableLang.setOnClickListener(v -> {

            // if no preferable language, provide an empty list
            if (user.getLanguageList() == null)
                user.setLanguageList(new ArrayList<>());

            QxmPreferableLanguageUpdateDialog qxmPreferableLanguageUpdateDialog =
                    new QxmPreferableLanguageUpdateDialog(v.getRootView(), getActivity(), context, user.getLanguageList(),
                            tvPreferableLanguage);
            qxmPreferableLanguageUpdateDialog.showPreferableLanguagePickerDialog(apiService, token);


        });

    }

    //region updateUserGender

    private void updateUserGender(String userGender) {

        Call<QxmApiResponse> updateGender = apiService.updateUserGender(token.getToken(), token.getUserId(), userGender);

        updateGender.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200 || response.code() == 201) {
                    Toast.makeText(context, "Gender updated.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();

            }
        });
    }

    //endregion

    // region updateUserDob
    private void updateUserDob(String usersBirthDayInMillis) {

        Call<QxmApiResponse> updateDob = apiService.updateUserDob(token.getToken(), token.getUserId(), usersBirthDayInMillis, user.getDateOfBirthPrivacy());

        updateDob.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200 || response.code() == 201) {
                    Toast.makeText(context, "Date of birth updated.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();

            }
        });

    }
    //endregion

    // region UpdateFullNameNetworkCall

    private void updateFullName() {

        Call<QxmApiResponse> updateFullName = apiService.updateFullName(token.getToken(), token.getUserId(), user.getFirstName(),
                user.getLastName(), user.getFullName());

        updateFullName.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 200 || response.code() == 201) {
                    Toast.makeText(context, "Name updated.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();

            }
        });

    }

    // endregion

    // region UpdateUsernameNetworkCall

    private void updateUsername() {
        Call<QxmApiResponse> updateUsername = apiService.updateUsername(token.getToken(), token.getUserId(), user.getUserName());
        updateUsername.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: success: getUserProfileInfo");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response);

                if (response.code() == 201) {

                    Log.d(TAG, "onResponse: update success");
                    Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 409) {


                    Toast.makeText(context, "This username is already taken!", Toast.LENGTH_SHORT).show();


                } else if (response.code() == 400) {

                    Toast.makeText(context, "More than one time username changing is not allowed", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d(TAG, "onResponse: update failed");
                    Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Toast.makeText(context, "Request timeout. Please try again.", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: unSuccessful");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

            }
        });
    }

    // endregion

    // region UpdateUserEmailNetworkCall

    private void updateUserEmail(String previousEmail, String newEmail) {

        Call<QxmApiResponse> updateUserEmailAddress = apiService.updateUserEmail(token.getToken(), token.getUserId(), previousEmail,
                newEmail);

        updateUserEmailAddress.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {


                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201 || response.code() == 200) {

                    if (response.body() != null) {

                        Toast.makeText(context, "A verification code is sent to your email, please verify your email to change the email!", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(context, "Response body is null!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Email is already taken", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(context, "Unexpected response code found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {


                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // endregion

    // region UpdateDescriptionNetworkCall

    private void updateUserProfileDescription() {

//        Call<UserInfoDetails> updateDescription = apiService.updateUserProfileDescription();

    }

    // endregion

    //region getAllInterests
    private ArrayList<String> getAllInterests() {

        return QxmCategoryHelper.getQxmCategoryList(context);
    }
    //endregion

    //region showDatePicker
    private void showDatePicker(String dobMilliString) {

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            String dateString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            String dob = "Your selected date is: " + dateString;
            Log.d(TAG, "onDateSet: " + dob);
            String usersBirthDayInMillis = String.valueOf(calendar.getTimeInMillis());
            Log.d(TAG, "showDatePicker selected Birth Days in millis: " + usersBirthDayInMillis);
            tvDob.setText(TimeFormatString.getDate(Long.parseLong(usersBirthDayInMillis)));

            // date of birth update network call
            updateUserDob(usersBirthDayInMillis);

        };

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(Long.parseLong(dobMilliString));

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle("Select Date of Birth");
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

        Log.d(TAG, "showDatePicker: called");

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


    private void buildResources() {
        Resources resources = getResources();
        if (models == null && resources != null) {
            String[] text = resources.getStringArray(R.array.text);
//            TypedArray image=resources.obtainTypedArray(R.array.image);
            models = new ArrayList<>();
            for (int i = 0; i < text.length; i++) {
                Model model = new Model();
                model.setText(text[i]);
//                model.setImage(image.getResourceId(i,R.mipmap.ic_launcher));
                model.setSelect(false);
                models.add(model);
            }
        }
    }

    @Override
    public void onItemChangeListener(int position, Model model) {
        try {
            models.set(position, model);

        } catch (Exception e) {

        }
    }


    private void updateList() {


        ArrayList<Model> temp = new ArrayList<>();

        try {
            for (int i = 0; i < models.size(); i++) {
                if (models.get(i).isSelect()) {

                    newInterestlist.add(models.get(i).getText());

//                    models.get(i).setSelect(false);
                    Log.d(TAG, "updateList l: " + models.get(i) + " ok " + newInterestlist.toString());
                } else {
                    temp.add(models.get(i));
                }
            }

        } catch (Exception e) {

        }
        models = temp;

//        interestList = newInterestlist;

        if (models.size() == 0) {
            rvQxmInterestCategory.setVisibility(View.GONE);
        }
        MultipleSelectAdapter.setModel(models);
        MultipleSelectAdapter.notifyDataSetChanged();
    }


    // endregion
}
