package com.crux.qxm.views.fragments.settings;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.qxmSettings.QxmAppNotificationSettings;
import com.crux.qxm.db.models.qxmSettings.SettingsDataContainer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.qxmAppSettingsFragmentFeatures.DaggerQxmAppSettingsFragmentComponent;
import com.crux.qxm.di.qxmAppSettingsFragmentFeatures.QxmAppSettingsFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.google.gson.Gson;
import com.onesignal.OSPermissionObserver;
import com.onesignal.OSPermissionStateChanges;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class QxmAppSettingsFragment extends Fragment implements OSPermissionObserver, OSSubscriptionObserver {

    // region Class-Properties
    private static final String TAG = "QxmAppSettingsFragment";
    @Inject
    Retrofit retrofit;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.nsvSettings)
    NestedScrollView nsvSettings;
    @BindView(R.id.switchPushNotification)
    SwitchCompat switchPushNotification;
    @BindView(R.id.checkBoxPlaySound)
    AppCompatCheckBox checkBoxPlaySound;
    @BindView(R.id.checkBoxVibrate)
    AppCompatCheckBox checkBoxVibrate;
    @BindView(R.id.switchFollowingNotifications)
    SwitchCompat switchFollowingNotifications;

    // endregion

    // region BindViewsWithButterKnife
    @BindView(R.id.switchGroupNotifications)
    SwitchCompat switchGroupNotifications;
    @BindView(R.id.switchParticipationNotification)
    SwitchCompat switchParticipationNotification;
    @BindView(R.id.switchEvaluationNotifications)
    SwitchCompat switchEvaluationNotifications;
    @BindView(R.id.switchResultNotifications)
    SwitchCompat switchResultNotifications;
    @BindView(R.id.switchPollNotifications)
    SwitchCompat switchPollNotifications;
    @BindView(R.id.tvThirdPartyNotices)
    AppCompatTextView tvThirdPartyNotices;
    @BindView(R.id.tvQxmVersion)
    AppCompatTextView tvQxmVersion;
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private QxmAppNotificationSettings appNotificationSettings;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Fragment-Constructor

    public QxmAppSettingsFragment() {
        // Required empty public constructor
    }

    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        OneSignal.addPermissionObserver(this);
        OneSignal.addSubscriptionObserver(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qxm_app_settings, container, false);

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

        if (appNotificationSettings == null)
            getAppNotificationSettingsNetworkCall();
    }

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        QxmAppSettingsFragmentComponent qxmAppSettingsFragmentComponent

                = DaggerQxmAppSettingsFragmentComponent.builder()
                .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                .build();

        qxmAppSettingsFragmentComponent.injectQxmAppSettingsFragmentFeature(QxmAppSettingsFragment.this);
    }

    // endregion

    // endregion

    // region Init

    private void init(View view) {
        context = view.getContext();
        apiService = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());


    }

    // endregion

    // region InitializeClickListeners
    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        switchPushNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {

            appNotificationSettings.setPushNotification(isChecked);
            OneSignal.setSubscription(isChecked);

            switchFollowingNotifications.setChecked(false);
            switchGroupNotifications.setChecked(false);
            switchParticipationNotification.setChecked(false);
            switchEvaluationNotifications.setChecked(false);
            switchResultNotifications.setChecked(false);
            switchPollNotifications.setChecked(false);
            checkBoxPlaySound.setChecked(false);
            checkBoxVibrate.setChecked(false);


        });

        checkBoxPlaySound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "checkBoxPlaySound: " + isChecked);
            OneSignal.enableSound(isChecked);
            appNotificationSettings.setPlaySound(isChecked);
        });

        checkBoxVibrate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "checkBoxVibrate: " + isChecked);
            OneSignal.enableVibrate(isChecked);
            appNotificationSettings.setVibrate(isChecked);
        });

        switchFollowingNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setFollowingNotification(isChecked)
        );

        switchGroupNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setGroupNotification(isChecked)
        );

        switchParticipationNotification.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setParticipationNotification(isChecked)
        );

        switchEvaluationNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setEvaluationNotification(isChecked)
        );

        switchResultNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setResultNotification(isChecked)
        );

        switchPollNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                appNotificationSettings.setPollNotification(isChecked)
        );

        tvThirdPartyNotices.setOnClickListener(v -> {

            //Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
            showThirdPartyNoticeDialog(v.getRootView());

        });
    }

    // endregion

    // region GetAppNotificationSettingsNetworkCall

    private void getAppNotificationSettingsNetworkCall() {
        Call<QxmAppNotificationSettings> appNotificationSettingsNetworkCall =
                apiService.getAppNotificationSettings(token.getToken(), token.getUserId());

        appNotificationSettingsNetworkCall.enqueue(new Callback<QxmAppNotificationSettings>() {
            @Override
            public void onResponse(@NonNull Call<QxmAppNotificationSettings> call, @NonNull Response<QxmAppNotificationSettings> response) {

                Log.d(TAG, "onResponse: getAppNotificationSettingsNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());


                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: success");
                        nsvSettings.setVisibility(View.VISIBLE);
                        appNotificationSettings = response.body();
                        loadAppNotificationSettingsDataIntoViews();
                    } else {
                        Log.d(TAG, "onResponse: failed. Response body null");
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: failed");
                    Toast.makeText(context, "Network request time out. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmAppNotificationSettings> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getAppNotificationSettingsNetworkCall");
                Log.d(TAG, "onFailure: error localized message: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: error stack trace: " + Arrays.toString(t.getStackTrace()));

                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    // region LoadAppNotificationSettingsDataIntoViews

    private void loadAppNotificationSettingsDataIntoViews() {

        switchPushNotification.setChecked(appNotificationSettings.isPushNotification());

        switchFollowingNotifications.setChecked(appNotificationSettings.isFollowingNotification());

        switchGroupNotifications.setChecked(appNotificationSettings.isGroupNotification());

        switchParticipationNotification.setChecked(appNotificationSettings.isParticipationNotification());

        switchEvaluationNotifications.setChecked(appNotificationSettings.isEvaluationNotification());

        switchResultNotifications.setChecked(appNotificationSettings.isResultNotification());

        switchPollNotifications.setChecked(appNotificationSettings.isPollNotification());

        checkBoxPlaySound.setChecked(appNotificationSettings.isPlaySound());

        checkBoxVibrate.setChecked(appNotificationSettings.isVibrate());

        tvQxmVersion.setText(tvQxmVersion.getContext().getResources().getString(R.string.version_code)+" "+tvQxmVersion.getContext().getResources().getString(R.string.version_variant));


    }

    // endregion

    // region UpdateQxmNotificationSettings

    private void updateQxmNotificationSettings() {
        SettingsDataContainer settingsDataContainer = new SettingsDataContainer();
        settingsDataContainer.setSettingsData(appNotificationSettings);

        Gson gson = new Gson();

        Log.d(TAG, "updateQxmNotificationSettings: " + gson.toJson(settingsDataContainer));

        Call<QxmAppNotificationSettings> updateQxmAppNotificationSettings = apiService.updateAppNotificationSettings(token.getToken(), token.getUserId(), settingsDataContainer);

        updateQxmAppNotificationSettings.enqueue(new Callback<QxmAppNotificationSettings>() {
            @Override
            public void onResponse(@NonNull Call<QxmAppNotificationSettings> call, @NonNull Response<QxmAppNotificationSettings> response) {
                Log.d(TAG, "onResponse: updateQxmAppNotificationSettings");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());


                if (response.code() == 201) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: success");
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    } else {
                        Log.d(TAG, "onResponse: failed. Response body null");
                        Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: failed");
                    Toast.makeText(context, "Network request time out. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmAppNotificationSettings> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getAppNotificationSettingsNetworkCall");
                Log.d(TAG, "onFailure: error localized message: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: error stack trace: " + Arrays.toString(t.getStackTrace()));

                Toast.makeText(context, "Network error occur on saving network settings.", Toast.LENGTH_SHORT).show();
            }
        });


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

    // region Override-OnPause

    @Override
    public void onPause() {
        if (appNotificationSettings != null)
            updateQxmNotificationSettings();
        super.onPause();

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
    public void onOSPermissionChanged(OSPermissionStateChanges stateChanges) {
        if (stateChanges.getFrom().getEnabled() &&
                !stateChanges.getTo().getEnabled()) {
            new AlertDialog.Builder(context)
                    .setMessage("Notifications Disabled!")
                    .show();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            Toast.makeText(context, "You've successfully subscribed to push notifications!", Toast.LENGTH_SHORT).show();
            // get player ID
            stateChanges.getTo().getUserId();

            Log.d(TAG, "onOSSubscriptionChanged: player id: " + stateChanges.getTo().getUserId());

        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);

    }


    //region show third party notice dialog

    private void showThirdPartyNoticeDialog(View rootView) {

        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.third_party_notice_layout, (ViewGroup) rootView, false);

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    //endregion

}
