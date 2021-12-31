package com.crux.qxm.views.fragments.loginActivityFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.SignUp.SignUpStatus;
import com.crux.qxm.db.models.login.UserLogin;
import com.crux.qxm.db.models.response.UserAuthenticationResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.loginFragmentFeature.DaggerLoginFragmentComponent;
import com.crux.qxm.di.loginFragmentFeature.LoginFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.DeviceInfoHelper;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmDateHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.appUpdateAndMaintenanceHelper.AppUpdateDialog;
import com.crux.qxm.utils.appUpdateAndMaintenanceHelper.ServerMaintenanceDialog;
import com.crux.qxm.utils.qxmDialogs.ForgetPasswordDialog;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.utils.qxmLoginHelper.QxmLoginHelper;
import com.crux.qxm.views.activities.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.EMAIL;
import static com.crux.qxm.utils.StaticValues.FACEBOOK;
import static com.crux.qxm.utils.StaticValues.GOOGLE;
import static com.crux.qxm.utils.StaticValues.LOCAL;
import static com.crux.qxm.utils.StaticValues.SHARED_PREF_CHECK_STATUS;
import static com.crux.qxm.utils.StaticValues.SHARED_PREF_USER_EMAIL_KEY;
import static com.crux.qxm.utils.StaticValues.SHARED_PREF_USER_PASS_KEY;

public class LoginFragment extends Fragment {

    private static final String TAG = "MyLoginFragment";
    private static final int RC_GOOGLE_SIGN_IN = 120;


    // region Fragment-Properties
    @Inject
    Retrofit retrofit;
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.tvForgetPassword)
    AppCompatTextView tvForgetPassword;
    @BindView(R.id.checkRememberMe)
    AppCompatCheckBox checkRememberMe;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btSignUp)
    AppCompatButton btnSignUp;
    @BindView(R.id.btnFacebookLogin)
    AppCompatImageView btnFacebookLogin;
    @BindView(R.id.btnGoogleLogin)
    AppCompatImageView btnGoogleLogin;
    @BindView(R.id.login_button)
    LoginButton facebookLoginButton;
    @BindView(R.id.tvTermsAndPrivacyPolicy)
    AppCompatTextView tvTermsAndPrivacyPolicy;
    @BindView(R.id.checkAgreeTermAndPolicy)
    AppCompatCheckBox checkAgreeTermAndPolicy;

    private AlertDialog serverStatusAlertDialog;
    private AlertDialog appUpdateDialog;
    private boolean isUserAlreadyLoggedIn;
    private QxmApiService apiService;
    private Realm realm;
    private RealmService realmService;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // endregion

    // region ButterKnifeBindViews
    private Context context;
    private CallbackManager facebookCallbackManager;
    private GoogleSignInClient googleSignInClient;
    private QxmProgressDialog dialog;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private FacebookCallback<LoginResult> facebookCallback;
    private SignUpStatus signUpStatus;
    private QxmLoginHelper qxmLoginHelper;
    private boolean showPassword = false;
    private QxmToken token;

    private AlertDialog apkUpdateAlertDialog;


    // endregion

    // region Fragment-Constructor
    public LoginFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreate

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting keyhash for facebook app verification
        /*try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.crux.qxm",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);


        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        setUpDagger2(context);
        init();
        isUserAlreadyLoggedIn = false;

        //DebugKeyHashGenerator.printHashKey(getContext());

        //region sign-up,login related functionality

        Log.d(TAG, "onViewCreated signUpStatus: " + signUpStatus);
        if (isSignUpTriggered()) {

            if (!signUpStatus.isUserProfilePicUploadSkipped()) {
                qxmFragmentTransactionHelper.loadProfilePicUploadFragment();
            } else if (!signUpStatus.isUserInterestAndPreferredLanguageInputDone()) {
                // loadUserInterestAndPreferredLanguageInputFragment
                qxmFragmentTransactionHelper.loadUserInterestAndPreferableLanguageInputFragment();
            } else {
                // load SignUpFragment
                qxmFragmentTransactionHelper.loadSignUpFragment();
            }

        } else {

            if (checkIsUserLoggedIn()) {
                sendUserActivitySessionTimeToBackend();
                isUserAlreadyLoggedIn = true;
                gotoHomeActivity(context);
            } else {
                processFacebookLogin();
                btnLogin.setOnClickListener(v -> loginWithEmail());

                btnFacebookLogin.setOnClickListener(v -> {
                    String token = checkFacebookLoginStatus();
                    if (!token.isEmpty()) {
                        OneSignal.idsAvailable((playersId, registrationId) -> {
                            Log.d(TAG, "playerId: " + playersId);
                            if (NetworkState.haveNetworkConnection(context))
                                facebookLoginNetworkCall(token, playersId);
                            else
                                Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                        });
                    } else {

                        facebookLoginButton.performClick();
                    }
                });


                // sign in with google

                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.google_login_client_id))
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                googleSignInClient = GoogleSignIn.getClient(context, gso);


                // performing google login button click
                btnGoogleLogin.setOnClickListener(v -> {

                    if (NetworkState.haveNetworkConnection(context))
                        signIn();
                    else
                        Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(), HomeActivity.class));
                    //getActivity().finish();

                });

                //togglePasswordShowIcon();

                btnSignUp.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSignUpFragment());

                tvForgetPassword.setOnClickListener(v -> {
                    ForgetPasswordDialog forgetPasswordDialog = new ForgetPasswordDialog(getActivity(), context, v.getRootView(), apiService);

                    forgetPasswordDialog.showDialog();

                });
            }
        }

        //endregion


    }

    @Override
    public void onResume() {
        super.onResume();
        new AppUpdateDialog(apkUpdateAlertDialog).checkIfApkUpdatedApkAvailable(this);
        new ServerMaintenanceDialog(serverStatusAlertDialog).checkIfServerUnderMaintenance(this);
    }

    private void sendUserActivitySessionTimeToBackend() {
        UserActivitySessionTracker activitySessionTracker = realmService.getUserActivitySessionTrackerData();

        // Current Date
        Calendar calendar = Calendar.getInstance();
        long currentDateLong = QxmDateHelper.getDateOnly(Calendar.getInstance().getTime()); //17 Jan 2019 at 0:0:0 milliseconds

        calendar.setTimeInMillis(currentDateLong); // 17 Jan 2019 at 0:0:0 date set to calender instance

        Date currentDate = calendar.getTime(); // 17 Jan 2019 at 0:0:0 store the calender date into a Date object

        if (activitySessionTracker != null) {
            Log.d(TAG, "sendUserActivitySessionTimeToBackend: activitySessionTracker: " + activitySessionTracker.toString());
            if (QxmStringIntegerChecker.isLongInt(activitySessionTracker.getDate())) {

                long sessionDateLong = Long.parseLong(activitySessionTracker.getDate());
                calendar.setTimeInMillis(sessionDateLong);
                Date sessionDate = calendar.getTime();

                Log.d(TAG, "sendUserActivitySessionTimeToBackend: sessionDate: " + sessionDateLong);
                Log.d(TAG, "sendUserActivitySessionTimeToBackend: currentDate: " + currentDateLong);

                if (currentDate.after(sessionDate)) {
                    Log.d(TAG, "sendUserActivitySessionTimeToBackend: networkCall called");
                    sendUserActivitySessionTimeNetworkCall(activitySessionTracker);
                } else {
                    Log.d(TAG, "sendUserActivitySessionTimeToBackend: session date is equal or before current date");
                    Log.d(TAG, String.format("sendUserActivitySessionTimeToBackend: sessionDate: %s, currentDate: %s",
                            currentDate, sessionDate));
                    Log.d(TAG, "sendUserActivitySessionTimeToBackend: activitySessionTracker:" + activitySessionTracker.toString());
                }
            } else {
                Log.d(TAG, "sendUserActivitySessionTimeToBackend: sessionDate is not long");
            }
        }


    }

    private void sendUserActivitySessionTimeNetworkCall(UserActivitySessionTracker activitySessionTracker) {

        Log.d(TAG, "sendUserActivitySessionTimeNetworkCall: activitySessionTracker: " + activitySessionTracker);

        UserActivitySessionTracker sessionTracker = new UserActivitySessionTracker();
        sessionTracker.setDate(activitySessionTracker.getDate());
        sessionTracker.setLoginActivitySession(activitySessionTracker.getLoginActivitySession());
        sessionTracker.setHomeActivitySession(activitySessionTracker.getHomeActivitySession());
        sessionTracker.setCreateQxmActivitySession(activitySessionTracker.getCreateQxmActivitySession());
        sessionTracker.setYouTubePlaybackActivitySession(activitySessionTracker.getYouTubePlaybackActivitySession());
        sessionTracker.setTotalActivitySession(
                activitySessionTracker.getLoginActivitySession()
                        + activitySessionTracker.getHomeActivitySession()
                        + activitySessionTracker.getCreateQxmActivitySession()
                        + activitySessionTracker.getYouTubePlaybackActivitySession());

        Log.d(TAG, "sendUserActivitySessionTimeNetworkCall: sessionTracker: " + sessionTracker);


        Call<QxmApiResponse> sendUserActivitySessionTime = apiService.sendUserActivitySessionTime(token.getToken(),
                token.getUserId(), sessionTracker);

        sendUserActivitySessionTime.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201 || response.code() == 200) {

                    Log.d(TAG, "onResponse: Feed network call success");

                    realmService.resetUserActivitySessionTrackerData();


                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: sendUserActivitySessionTimeNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
            }
        });
    }

    private void togglePasswordShowIcon() {


        View togglePasswordButton = findTogglePasswordButton(tilPassword);
        if (togglePasswordButton != null) {
            togglePasswordButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // implementation
                    if (!showPassword) {
                        showPassword = true;
                        tilPassword.setPasswordVisibilityToggleDrawable(
                                getResources().getDrawable(R.drawable.ic_eye_grey)
                        );
                    } else {
                        showPassword = false;
                        tilPassword.setPasswordVisibilityToggleDrawable(
                                getResources().getDrawable(R.drawable.ic_eye_off_grey)
                        );
                    }

                    return false;
                }
            });
        }
    }

    private View findTogglePasswordButton(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int ind = 0; ind < childCount; ind++) {
            View child = viewGroup.getChildAt(ind);
            if (child instanceof ViewGroup) {
                View togglePasswordButton = findTogglePasswordButton((ViewGroup) child);
                if (togglePasswordButton != null) {
                    return togglePasswordButton;
                }
            } else if (child instanceof CheckableImageButton) {
                return child;
            }
        }
        return null;
    }

    // endregion

    // region isSignUpTriggered
    private boolean isSignUpTriggered() {

        return signUpStatus.isSignUpTriggered();

    }
    // endregion

    // region Init

    private void init() {
        apiService = retrofit.create(QxmApiService.class);
        realm = Realm.getDefaultInstance();
        realmService = new RealmService(realm);
        signUpStatus = realmService.getSignUpStatus();
        dialog = new QxmProgressDialog(context);
        qxmLoginHelper = new QxmLoginHelper(realmService);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        setRememberDataInViewsIfAvailable();

        tvTermsAndPrivacyPolicy.setOnClickListener(v -> {
            Uri uri = Uri.parse(v.getContext().getResources().getString(R.string.qxm_terms_and_privacy_url)); // missing 'http://' will cause
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        checkAgreeTermAndPolicy.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (!isChecked) {
                btnLogin.setEnabled(false);
                btnFacebookLogin.setEnabled(false);
                btnGoogleLogin.setEnabled(false);
                btnSignUp.setEnabled(false);
                btnLogin.setAlpha(0.5f);
                btnFacebookLogin.setAlpha(0.5f);
                btnGoogleLogin.setAlpha(0.5f);
                btnSignUp.setAlpha(0.5f);
            } else {
                btnLogin.setEnabled(true);
                btnFacebookLogin.setEnabled(true);
                btnGoogleLogin.setEnabled(true);
                btnSignUp.setEnabled(true);
                btnLogin.setAlpha(1f);
                btnFacebookLogin.setAlpha(1f);
                btnGoogleLogin.setAlpha(1f);
                btnSignUp.setAlpha(1f);
            }

        });
    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        LoginFragmentComponent loginFragmentComponent =
                DaggerLoginFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        loginFragmentComponent.injectLoginFragment(LoginFragment.this);
    }

    // endregion

    // region CheckUserLoggedInStatus

    private boolean checkIsUserLoggedIn() {
        token = realmService.getApiToken();
        UserBasic user = realmService.getSavedUser();

        if (token != null && user != null) {
            return !TextUtils.isEmpty(token.getToken())
                    && !TextUtils.isEmpty(user.getUserId());
        }

        return false;
    }

    private String checkFacebookLoginStatus() {
        Log.d(TAG, "checkFacebookLoginStatus: ");
        // is user logged in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Log.d(TAG, "user already logged in.");
            Log.d(TAG, String.format(accessToken.getToken(), "fb token: %s"));
            Log.d(TAG, "checkIsUserLoggedIn: " + accessToken.getToken());
            return accessToken.getToken();
        } else {
            Log.d(TAG, "No user is logged in with facebook");
            return "";
        }
    }

    // endregion

    // region ProcessLoginWithEmail

    private void loginWithEmail() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "The Email is not valid", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(context, "The password must be 6 digits long", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "User Login request");

                if (NetworkState.haveNetworkConnection(context)) {

                    OneSignal.idsAvailable((playersId, registrationId) -> {
                        Log.d(TAG, "playersId: " + playersId);
                        if (NetworkState.haveNetworkConnection(context))
                            localUserLoginNetworkCall(email, password, playersId);
                        else
                            Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                    });

                } else
                    Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Please input both field", Toast.LENGTH_SHORT).show();
        }

    }

    // endregion

    // region EmailAuthenticationFromBackend

    private void localUserLoginNetworkCall(String email, String password, String playersId) {
        dialog.showProgressDialog("Login", "Please wait, your credentials are verifying.", false);
        String tokenBearer = "qxm ";
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(email);
        userLogin.setPassword(password);
        userLogin.setPlayersID(playersId);
        userLogin.setDeviceInfo(DeviceInfoHelper.getDeviceInfo(context));
        Call<UserAuthenticationResponse> emailLogin = apiService.emailLogin(tokenBearer, userLogin);

        emailLogin.enqueue(new Callback<UserAuthenticationResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserAuthenticationResponse> call, @NonNull Response<UserAuthenticationResponse> response) {
                Log.d(TAG, "onResponse: email login network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                dialog.closeProgressDialog();

                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "onResponse: email login success");
                        if (response.body() != null) {
                            UserAuthenticationResponse user = response.body();
                            Log.d(TAG, "onResponse user: " + user);

                            if (user.getUser() != null) {

                                if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                                    realmService.deleteAll();

                                user.getUser().setAccountProvider(LOCAL);

                                qxmLoginHelper.qxmLoginSuccess(user);

                                // remember me
                                if (checkRememberMe.isChecked()) {
                                    saveLoginCredentialInPref(email, password, checkRememberMe.isChecked());
                                } else clearRememberMeSharedPreference();

                                gotoHomeActivity(context);
                            } else {
                                Toast.makeText(getContext(), "Timeout error. Please try again.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "Response body is null");
                            Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 401:
                        Log.d(TAG, "onResponse: email login failed");
                        Toast.makeText(context, "Email or password not matched", Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        Log.d(TAG, "onResponse: email login failed");
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Log.d(TAG, "onResponse: email login failed");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                dialog.closeProgressDialog();
                Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // endregion

    // region ProcessFacebookLogin

    private void processFacebookLogin() {

        /*facebookCallbackManager = CallbackManager.Factory.create();

        facebookLoginButton.setReadPermissions(Arrays.asList(EMAIL));

        facebookLoginButton.setFragment(this);*/

        // Callback registration
        /*facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook login callback: onSuccess");
                Log.d(TAG, String.format("token: %s", loginResult.getAccessToken().getToken()));
                Log.d(TAG, String.format("userId: %s", loginResult.getAccessToken().getUserId()));

                String fbToken = loginResult.getAccessToken().getToken();
                facebookLoginNetworkCall(fbToken);

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook login callback: onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "facebook login callback: onError");
            }
        });*/

        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook login callback: onSuccess");
                Log.d(TAG, String.format("token: %s", loginResult.getAccessToken().getToken()));
                Log.d(TAG, String.format("userId: %s", loginResult.getAccessToken().getUserId()));

                final String fbToken = loginResult.getAccessToken().getToken();

                // logout from facebook
                LoginManager.getInstance().logOut();

                OneSignal.idsAvailable((playersId, registrationId) -> {
                    Log.d(TAG, "playersId: " + playersId);
                    if (NetworkState.haveNetworkConnection(context))
                        facebookLoginNetworkCall(fbToken, playersId);
                    else
                        Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                });

//                facebookLoginNetworkCall(fbToken);

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook login callback: onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "facebook login callback: onError");
                Toast.makeText(context, getString(R.string.facebook_login_error_message), Toast.LENGTH_SHORT).show();
            }
        };

    }

    // endregion

    // region FacebookAuthenticationFromBackendStarts

    private void facebookLoginNetworkCall(String fbToken, String playersId) {

        dialog.showProgressDialog("Facebook Authentication", "Please wait facebook authentication verifying is in progress", false);

        UserLogin userLogin = new UserLogin();
        userLogin.setFbToken(fbToken);
        userLogin.setPlayersID(playersId);
        userLogin.setDeviceInfo(DeviceInfoHelper.getDeviceInfo(context));

        String tokenBearer = "qxm";

        Call<UserAuthenticationResponse> facebookLogin = apiService.facebookLogin(tokenBearer, userLogin);
        facebookLogin.enqueue(new Callback<UserAuthenticationResponse>() {

            @Override
            public void onResponse(@NonNull Call<UserAuthenticationResponse> call, @NonNull Response<UserAuthenticationResponse> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                dialog.closeProgressDialog();

                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: facebook network call success");
                    Log.d(TAG, "onResponse: existing user");
                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                            realmService.deleteAll();

                        user.getUser().setAccountProvider(FACEBOOK);
                        qxmLoginHelper.qxmLoginSuccess(user);

                        //clearing remember me
                        clearRememberMeSharedPreference();

                        gotoHomeActivity(context);

                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }


                } else if (response.code() == 201) {
                    Log.d(TAG, "onResponse: facebook network call success");
                    Log.d(TAG, "onResponse: new user");
                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                            realmService.deleteAll();

                        realmService.addUser(user.getUser());
                        realmService.saveToken(user.getToken());
                        realmService.resetUserActivitySessionTrackerData();

                        gotoProfileActivity(user.getUser());
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: 2nd network call");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                dialog.closeProgressDialog();
                Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // endregion

    // region GotoProfileActivity

    // when user is logged in with Google & Facebook
    public void gotoProfileActivity(UserBasic user) {

        if (user != null) {
//
//            Bundle bundle = new Bundle();
//            bundle.putString(FULL_NAME, user.getFullName());
//            bundle.putString(PROFILE_PIC_URL, user.getProfilePic());
            qxmFragmentTransactionHelper.loadProfilePicUploadFragment();
//            transactionToProfileFragment(bundle);
        }

    }

    // endregion

    // region TransactionToProfileActivity

//    private void transactionToProfileFragment(Bundle bundle) {
//
//        ProfilePicUploadFragment profilePicUploadFragment = new ProfilePicUploadFragment();
//        profilePicUploadFragment.setArguments(bundle);
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager != null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//            fragmentTransaction.replace(R.id.fragment_container, profilePicUploadFragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
//    }

    // endregion

    // region GotoHomeActivity

    public void gotoHomeActivity(Context context) {
        Log.d(TAG, "gotoHomeActivity: ");
        if (isAdded()) {
            startActivity(new Intent(context.getApplicationContext(), HomeActivity.class));
        }
        if (getActivity() != null)
            getActivity().finish();
    }

    // endregion

    // region GoogleSignIn

    private void signIn() {
        Log.d(TAG, "signIn: google signIn in progress...");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        dialog.showProgressDialog("Google Authentication", "Please wait google authentication verifying is in progress", false);
    }

    // endregion

    // region HandleGoogleSignInResult

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "googleSignInIDToken: " + account.getIdToken());
                // passing token to the backend for authentication
                String userTokenID = account.getIdToken();

                OneSignal.idsAvailable((playersId, registrationId) -> {
                    Log.d(TAG, "playersId: " + playersId);
                    if (NetworkState.haveNetworkConnection(context))
                        googleLoginNetworkCall(userTokenID, playersId);
                    else
                        Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                });

            } else {
                Log.d(TAG, "handleSignInResult: account is null");
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }


            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {

            dialog.closeProgressDialog();

            Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "handleSignInResult Error: " + e.getStatusCode());
            Log.d(TAG, "handleSignInResult: " + e.getMessage());
            //updateUI(null);
        }

    }

    // endregion

    // region GoogleAuthenticationFromBackendStarts

    private void googleLoginNetworkCall(String googleToken, String playersId) {
        dialog.showProgressDialog("Google Authentication", "Please wait google authentication verifying is in progress", false);

        String token = "qxm";
        UserLogin userLogin = new UserLogin();
        userLogin.setGoogleToken(googleToken);
        userLogin.setPlayersID(playersId);
        userLogin.setDeviceInfo(DeviceInfoHelper.getDeviceInfo(context));

        Call<UserAuthenticationResponse> googleLogin = apiService.googleLogin(token, userLogin);
        googleLogin.enqueue(new Callback<UserAuthenticationResponse>() {
            @Override

            public void onResponse(@NonNull Call<UserAuthenticationResponse> call, @NonNull Response<UserAuthenticationResponse> response) {

                Log.d(TAG, "onResponse: google login network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                dialog.closeProgressDialog();

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: google login success.");
                    Log.d(TAG, "onResponse: existing user");

                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                            realmService.deleteAll();

                        user.getUser().setAccountProvider(GOOGLE);
                        qxmLoginHelper.qxmLoginSuccess(user);
                        //clearing remember me
                        clearRememberMeSharedPreference();
                        gotoHomeActivity(context);
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Log.d(TAG, "onResponse: google login success.");
                    Log.d(TAG, "onResponse: new user");

                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {

                        if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                            realmService.deleteAll();

                        realmService.addUser(user.getUser());
                        realmService.saveToken(user.getToken());
                        realmService.resetUserActivitySessionTrackerData();


                        gotoProfileActivity(user.getUser());
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure googleLogin: network call.");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                Log.d(TAG, "googleLogin onFailure stack trace: " + Arrays.toString(t.getStackTrace()));


                dialog.closeProgressDialog();

                Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    // endregion

    //region checkIfServerUnderMaintenance
    private void checkIfServerUnderMaintenance() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("server_status");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    if (serverStatusAlertDialog != null)
                        serverStatusAlertDialog.dismiss();

                    Log.d(TAG, "onDataChange dataSnapshot server_status : " + dataSnapshot.toString());

                    String serverStatus = (String) dataSnapshot.getValue();

                    if (serverStatus != null && !serverStatus.isEmpty()) {

                        generateServerStatusDialog(context, serverStatus);

                    } else {

                        if (serverStatusAlertDialog != null) {
                            serverStatusAlertDialog.dismiss();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled details: " + databaseError.getDetails());
                Log.d(TAG, "onCancelled message: " + databaseError.getMessage());
                Log.d(TAG, "onCancelled code: " + databaseError.getCode());
            }
        });
    }
    //endregion

    //region generateServerStatusDialog
    private void generateServerStatusDialog(Context context, String message) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_server_under_maintenance));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);

        if (!((AppCompatActivity) context).isFinishing()) {

            serverStatusAlertDialog = alertDialogBuilder.create();
            serverStatusAlertDialog.show();
        }


    }
    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: ");

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {

            dialog.closeProgressDialog();
            Log.d(TAG, "Google signIn request callback.");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

        // Facebook login callback
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        //latest apk available dialog
        //QxmApkUpdateDialog.showApkUpdateDialogIfUpdateAvailable(context,apiService,token);
        //QxmServerUnderMaintenanceDialog.showServerMaintenanceDialog(context);
        // checkIfServerUnderMaintenance();
        // new ServerMaintenanceDialog(serverStatusAlertDialog).checkIfServerUnderMaintenance(this);
        // new AppUpdateDialog(appUpdateDialog).checkIfLatestVersionOfAppAvailable(this);

        if (!isUserAlreadyLoggedIn) {
            facebookCallbackManager = CallbackManager.Factory.create();

            facebookLoginButton.setReadPermissions(Collections.singletonList(EMAIL));
            //facebookLoginButton.setReadPermissions(Collections.singletonList(GENDER ));
            facebookLoginButton.registerCallback(facebookCallbackManager, facebookCallback);
            facebookLoginButton.setFragment(LoginFragment.this);
        }

        Log.d(TAG, "onStart: isUserAlreadyLoggedIn: " + isUserAlreadyLoggedIn);
    }

    // endregion

    // region Override-OnDestroy
    @Override
    public void onDestroy() {
        realm.close();
        // App.getRefWatcher(Objects.requireNonNull(getActivity())).watch(getContext());

        facebookCallback = null;

        facebookLoginButton.removeCallbacks(() -> Log.d(TAG, "onDestroy: facebookLoginButton remove callback called."));
        facebookLoginButton.invalidate();
        facebookCallbackManager = null;
        facebookLoginButton = null;
        super.onDestroy();

    }
    // endregion

    private void saveLoginCredentialInPref(String email, String pass, boolean isChecked) {

        editor.putString(SHARED_PREF_USER_EMAIL_KEY, email);
        editor.putString(SHARED_PREF_USER_PASS_KEY, pass);
        editor.putBoolean(SHARED_PREF_CHECK_STATUS, isChecked);
        editor.apply();
    }

    private void setRememberDataInViewsIfAvailable() {
        boolean isChecked = preferences.getBoolean(SHARED_PREF_CHECK_STATUS, false);
        if (isChecked) {
            String email = preferences.getString(SHARED_PREF_USER_EMAIL_KEY, "not_found");
            String password = preferences.getString(SHARED_PREF_USER_PASS_KEY, "not_found");
            etEmail.setText(email);
            etPassword.setText(password);
            checkRememberMe.setChecked(true);
        }

    }

    private void clearRememberMeSharedPreference() {
        preferences.edit().clear().apply();
    }

}
