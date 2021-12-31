package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.profileSettingsFragmentFeature.DaggerProfileSettingsFragmentComponent;
import com.crux.qxm.di.profileSettingsFragmentFeature.ProfileSettingsFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmAlertDialogBuilder;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.UpdatePasswordDialog;

import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.LOCAL;
import static com.crux.qxm.utils.StaticValues.USER_NOT_VERIFIED;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSettingsFragment extends Fragment {

    //region Fragment-Properties
    @Inject
    Retrofit retrofit;

    private static final String TAG = "ProfileSettingsFragment";
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    //endregion

    //region BindViewsWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.llUpdatePassword)
    LinearLayoutCompat llUpdatePassword;
    @BindView(R.id.llAccountStatus)
    LinearLayoutCompat llAccountStatus;
    @BindView(R.id.llLoginDevices)
    LinearLayoutCompat llLoginDevices;
    @BindView(R.id.llLogout)
    LinearLayoutCompat llLogout;
    //endregion

    //region Fragment-Constructor
    public ProfileSettingsFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Override-onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
    //endregion

    //region Override-onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();

    }
    //endregion

    //region SetUpDagger2
    private void setUpDagger2(Context context) {
        ProfileSettingsFragmentComponent profileSettingsFragmentComponent =
                DaggerProfileSettingsFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        profileSettingsFragmentComponent.injectProfileSettingsFragmentFeature(ProfileSettingsFragment.this);
    }
    //endregion

    //region Init
    private void init(View view) {
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if(realmService.getSavedUser().getAccountProvider().equals(LOCAL))
            llUpdatePassword.setVisibility(View.VISIBLE);
        else
            llUpdatePassword.setVisibility(View.GONE);
    }
    //endregion

    //region InitializeClickListeners
    private void initializeClickListeners() {

        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        llUpdatePassword.setOnClickListener(v -> showUpdatePasswordDialog(v.getRootView()));

        llAccountStatus.setOnClickListener(v -> getAccountStatusNetworkCall());

        llLoginDevices.setOnClickListener(v -> qxmFragmentTransactionHelper.loadLoggedInDevicesFragment());

        llLogout.setOnClickListener(v -> qxmFragmentTransactionHelper.logout(realmService));
    }

    private void showUpdatePasswordDialog(View rootView) {
        UpdatePasswordDialog updatePasswordDialog = new UpdatePasswordDialog(getActivity(), context,
                apiService, token, rootView, qxmFragmentTransactionHelper);
        updatePasswordDialog.showDialog();
    }
    //endregion

    private void getAccountStatusNetworkCall() {
        Call<QxmApiResponse> getAccountStatus = apiService.getAccountStatus(token.getToken(), token.getUserId());

        getAccountStatus.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getMessage().equals(USER_NOT_VERIFIED)) {
                            showResendVerificationEmailDialog();
                        } else {
                            Toast.makeText(context, R.string.account_is_verified, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getAccountStatusNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResendVerificationEmailDialog() {
        QxmAlertDialogBuilder qxmAlertDialogBuilder = new QxmAlertDialogBuilder(context);
        qxmAlertDialogBuilder.setTitle(getString(R.string.dialog_title_account_not_verified));
        qxmAlertDialogBuilder.setMessage(String.format(getString(R.string.dialog_description_account_not_verified_msg), token.getEmail()));
        qxmAlertDialogBuilder.getAlertDialogBuilder().setPositiveButton("SEND VERIFICATION EMAIL", (dialog, which) -> {
            sendAccountVerificationEmailNetworkCall();
        });
        qxmAlertDialogBuilder.getAlertDialogBuilder().setNegativeButton("CANCEL", null);

        qxmAlertDialogBuilder.show();

    }

    private void sendAccountVerificationEmailNetworkCall() {
        Call<QxmApiResponse> sendVerificationEmail = apiService.sendVerificationEmail(token.getToken(), token.getUserId());

        sendVerificationEmail.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                if (response.code() == 200) {
                    Toast.makeText(context, String.format(getString(R.string.verification_email_send_success_msg), token.getEmail()), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, R.string.network_call_something_went_wrong_message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: sendAccountVerificationEmailNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });
    }


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
}
