package com.crux.qxm.views.fragments.loginActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.SignUp.UserSignUp;
import com.crux.qxm.db.models.login.UserLogin;
import com.crux.qxm.db.models.response.UserAuthenticationResponse;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.signUpFragmentFeature.DaggerSignUpFragmentComponent;
import com.crux.qxm.di.signUpFragmentFeature.SignUpFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.DeviceInfoHelper;
import com.crux.qxm.utils.NetworkState;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.utils.qxmLoginHelper.QxmLoginHelper;
import com.crux.qxm.views.activities.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.onesignal.OneSignal;

import java.util.Collections;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.FACEBOOK;
import static com.crux.qxm.utils.StaticValues.GOOGLE;
import static com.crux.qxm.utils.StaticValues.LOCAL;

public class SignUpFragment extends Fragment {

    // region Fragment-Properties

    @Inject
    Retrofit retrofit;

    private static final String TAG = "MySignUpFragment";
    private static final String EMAIL = "email";
    private static final int RC_GOOGLE_SIGN_IN = 120;
    private Context context;
    private QxmApiService apiService;
    private CallbackManager callbackManager;
    //    private ProgressDialog progressDialog;
    private RealmService realmService;
    private QxmLoginHelper qxmLoginHelper;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private GoogleSignInClient googleSignInClient;
    private QxmProgressDialog qxmProgressDialog;

    // endregion

    // region ButterKnifeBindViews

    @BindView(R.id.btnLogin)
    AppCompatButton btnLogin;
    @BindView(R.id.btnFacebookLogin)
    AppCompatImageView btnFacebookLogin;
    @BindView(R.id.btnGoogleLogin)
    AppCompatImageView btnGoogleLogin;
    @BindView(R.id.login_button)
    LoginButton facebookLoginButton;
    @BindView(R.id.etFirstName)
    AppCompatEditText etFirstName;
    @BindView(R.id.etLastName)
    AppCompatEditText etLastName;
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;
    @BindView(R.id.btnSignUp)
    Button btnSignUpLocal;

    // endregion

    // region Fragment-Constructor
    public SignUpFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);

        setUpDagger2(view.getContext());

        init(view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        processFacebookLogin();

        btnFacebookLogin.setOnClickListener(v -> {
            String token = checkFacebookLoginStatus();
            if (!token.isEmpty()) {
                OneSignal.idsAvailable((playersId, registrationId) -> {
                    Log.d(TAG, "playersId: " + playersId);
                    facebookLoginNetworkCall(token, playersId);
                });
            } else {
                facebookLoginButton.performClick();
            }
        });

        btnLogin.setOnClickListener(v -> gotoLoginFragment());


        // sign in with google android

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_login_client_id))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);

        // performing google login button click
        btnGoogleLogin.setOnClickListener(v -> signIn());
    }

    // endregion

    // region Init

    private void init(View view) {
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        qxmProgressDialog = new QxmProgressDialog(context);
        qxmLoginHelper = new QxmLoginHelper(realmService);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        btnSignUpLocal.setOnClickListener(v -> {

            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email)
                    && !TextUtils.isEmpty(password)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context, "The Email is not valid", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(context, "The password must be 6 digits long", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "New User SignUp request");

//                    showProgressDialog("SignUp", "Please wait, your account is being created.",
//                            false);

                    if (NetworkState.haveNetworkConnection(context)) {
                        qxmProgressDialog.showProgressDialog("SignUp", "Please wait, your account is being created.",
                                false);
                        OneSignal.idsAvailable((playersId, registrationId) -> {
                            Log.d(TAG, "playersId: " + playersId);
                            localUserSignUpNetworkCall(firstName, lastName, firstName + " " + lastName, email, password, playersId);
                        });
                    } else {
                        Toast.makeText(context, R.string.no_internet_found, Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                Toast.makeText(context, "Please input all mandatory fields and tap SingUp", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void localUserSignUpNetworkCall(String firstName, String lastName, String fullName, String email, String password, String playersId) {
        String tokenBearer = "qxm";
        UserSignUp userSignUp = new UserSignUp();
        userSignUp.setFirstName(firstName);
        userSignUp.setLastName(lastName);
        userSignUp.setFullName(fullName);
        userSignUp.setEmail(email);
        userSignUp.setPassword(password);
        userSignUp.setPlayersID(playersId);
        userSignUp.setDeviceInfo(DeviceInfoHelper.getDeviceInfo(context));

        Call<UserAuthenticationResponse> signUpUser = apiService.emailSignUp(tokenBearer, userSignUp);

        signUpUser.enqueue(new Callback<UserAuthenticationResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserAuthenticationResponse> call, @NonNull Response<UserAuthenticationResponse> response) {

                Log.d(TAG, "onResponse: localUserSignUpNetworkCall success network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {

                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        if (user.getUser() != null && user.getToken() != null) {

                            //if (!realmService.isUserIdMatched(user.getUser().getUserId()))
                                realmService.deleteAll();

                            // save user to LocalDB to ensure session
                            user.getUser().setAccountProvider(LOCAL);

                            qxmLoginHelper.qxmSignUpSuccess(user);

                            qxmFragmentTransactionHelper.loadProfilePicUploadFragment();

//                            gotoProfileActivity(user.getUser());
                        } else {
                            Toast.makeText(getContext(), "Timeout error. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Timeout error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    qxmProgressDialog.closeProgressDialog();
                } else if (response.code() == 409) {
                    Log.d(TAG, "onResponse: email conflict");
                    qxmProgressDialog.closeProgressDialog();
                    Toast.makeText(context, "Email already exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Bad Status Code");
                    qxmProgressDialog.closeProgressDialog();
                    Toast.makeText(context, "Timeout error. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: emailLogin network call");
                Log.d(TAG, "onFailure: unSuccessful");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                qxmProgressDialog.closeProgressDialog();

                Toast.makeText(getContext(), "Timeout error. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    // endregion

    // region SetUpDagger2

    public void setUpDagger2(Context context) {
        SignUpFragmentComponent signUpFragmentComponent =
                DaggerSignUpFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        signUpFragmentComponent.injectSignUpFragment(SignUpFragment.this);
    }

    // endregion

    // region ShowProgressDialog

//    private void showProgressDialog(String title, String message, boolean cancelable) {
//        progressDialog.setTitle(title);
//        progressDialog.setMessage(message);
//        progressDialog.setCanceledOnTouchOutside(cancelable);
//        progressDialog.show();
//    }

    // endregion

    // region CloseProgressDialog

//    private void closeProgressDialog() {
//        progressDialog.dismiss();
//    }

    // endregion

    // region CheckFacebookLoginStatus

    private String
    checkFacebookLoginStatus() {
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

    // region ProcessFacebookLogin

    private void processFacebookLogin() {

        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton.setReadPermissions(Collections.singletonList(EMAIL));

        facebookLoginButton.setFragment(SignUpFragment.this);

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: facebook login callback: onSuccess");
                Log.d(TAG, String.format("token: %s", loginResult.getAccessToken().getToken()));
                Log.d(TAG, String.format("userId: %s", loginResult.getAccessToken().getUserId()));

                String fbToken = loginResult.getAccessToken().getToken();
                OneSignal.idsAvailable((playersId, registrationId) -> {
                    Log.d(TAG, "playersId: " + playersId);
                    facebookLoginNetworkCall(fbToken, playersId);
                });
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
        });
    }

    // endregion

    // region PerformGoogleSignIn

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);

        qxmProgressDialog.showProgressDialog("Google Authentication", "Please wait google authentication verifying is in progress",
                false);

//        showProgressDialog("Google Authentication", "Please wait google authentication verifying is in progress",
//                false);

    }


    private void gotoLoginFragment() {
        // transaction to login fragment for login
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragment_container, loginFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    // endregion

    // region CheckUserLoggedInStatus

    /*private void checkIsUserLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Log.d(TAG, "user already logged in.");
        } else {
            Log.d(TAG, "user is not logged in.");
        }
    }*/

    // endregion

    // region GotoProfileActivity

//    public void gotoProfileActivity(UserBasic user) {
//        if (user != null) {
//
//            Bundle bundle = new Bundle();
//            bundle.putString(FULL_NAME, user.getFullName());
//            bundle.putString(PROFILE_PIC_URL, user.getProfilePic());
//
//            // transaction to login fragment for login
//            ProfilePicUploadFragment profilePicUploadFragment = new ProfilePicUploadFragment();
//            profilePicUploadFragment.setArguments(bundle);
//            FragmentManager fragmentManager = getFragmentManager();
//            if (fragmentManager != null) {
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//                fragmentTransaction.replace(R.id.fragment_container, profilePicUploadFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//
//        }
//    }

    // endregion

    // region GotoHomeActivity

    public void gotoHomeActivity() {
        Log.d(TAG, "gotoHomeActivity: ");
        startActivity(new Intent(getActivity(), HomeActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }

    // endregion

    // region GoogleAuthenticationFromBackendStarts

    private void googleLoginNetworkCall(String googleToken, String playersId) {

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

                        /*realmService.addUser(user.getUser());
                        realmService.saveToken(user.getToken());*/

                        gotoHomeActivity();
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 201) {
                    Log.d(TAG, "onResponse: google login success.");
                    Log.d(TAG, "onResponse: new user");

                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        qxmLoginHelper.qxmSignUpSuccess(user);
                        qxmFragmentTransactionHelper.loadProfilePicUploadFragment();
//                        gotoProfileActivity(user.getUser());
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }

                qxmProgressDialog.closeProgressDialog();
            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure googleLogin: network call.");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                qxmProgressDialog.closeProgressDialog();

                Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    // endregion

    // region HandleGoogleSignInResult

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "handleSignInResult: " + account.getIdToken());
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


        } catch (ApiException e) {

            qxmProgressDialog.closeProgressDialog();

            Toast.makeText(getContext(), "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "handleSignInResult: " + e.getStatusCode());
            //updateUI(null);
        }

    }

    // endregion

    // region FacebookAuthenticationFromBackendStarts

    private void facebookLoginNetworkCall(String fbToken, String playersId) {

        qxmProgressDialog.showProgressDialog("Facebook Authentication", "Please wait facebook authentication verifying is in progress",
                false);

//        showProgressDialog("Facebook Authentication", "Please wait facebook authentication verifying is in progress",
//                false);

        // 2nd network call starts here
        UserLogin userLogin = new UserLogin();
        userLogin.setFbToken(fbToken);
        userLogin.setPlayersID(playersId);
        userLogin.setDeviceInfo(DeviceInfoHelper.getDeviceInfo(context));

        String tokenBearer = "qxm";
        Call<UserAuthenticationResponse> facebookLogin = apiService.facebookLogin(tokenBearer,userLogin);
        facebookLogin.enqueue(new Callback<UserAuthenticationResponse>() {
            @Override

            public void onResponse(@NonNull Call<UserAuthenticationResponse> call, @NonNull Response<UserAuthenticationResponse> response) {

                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

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

                        /*realmService.addUser(user.getUser());
                        realmService.saveToken(user.getToken());*/

                        gotoHomeActivity();
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    qxmProgressDialog.closeProgressDialog();

                } else if (response.code() == 201) {
                    Log.d(TAG, "onResponse: facebook network call success");
                    Log.d(TAG, "onResponse: new user");
                    UserAuthenticationResponse user = response.body();

                    // Inserting User to Local DB
                    if (user != null) {
                        qxmLoginHelper.qxmSignUpSuccess(user);
                        qxmFragmentTransactionHelper.loadProfilePicUploadFragment();

//                        gotoProfileActivity(user.getUser());
                    } else {
                        Log.d(TAG, "User is null");
                        Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    qxmProgressDialog.closeProgressDialog();

                } else {
                    qxmProgressDialog.closeProgressDialog();
                    Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserAuthenticationResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: 2nd network call");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());

                qxmProgressDialog.closeProgressDialog();
                Toast.makeText(context, "Authentication timeout. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // endregion

    // region Override-OnActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

        // Facebook login callback
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // endregion

    // region Override-OnStart
    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getContext()));
        //updateUI(account);

    }
    // endregion

    // region Override-OnDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        //  App.getRefWatcher(getActivity()).watch(this);
    }
    // endregion

}
