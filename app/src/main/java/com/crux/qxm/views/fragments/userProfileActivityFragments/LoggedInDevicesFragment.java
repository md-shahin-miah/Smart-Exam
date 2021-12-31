package com.crux.qxm.views.fragments.userProfileActivityFragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.profile.LoggedInDeviceAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.users.LoggedInDevice;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.loggedInDevicesFragmentFeature.DaggerLoggedInDevicesFragmentComponent;
import com.crux.qxm.di.loggedInDevicesFragmentFeature.LoggedInDevicesFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.DeviceInfoHelper;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class LoggedInDevicesFragment extends Fragment {

    //region Fragment-properties
    @Inject
    Retrofit retrofit;

    private static final String TAG = "LoggedInDevicesFragment";
    private Context context;
    private QxmApiService apiService;
    private RealmService realmService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private LoggedInDeviceAdapter loggedInDeviceAdapter;
    private List<LoggedInDevice> loggedInDeviceList;
    //endregion

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.llLoggedInDevicesFragmentContainer)
    LinearLayoutCompat llLoggedInDevicesFragmentContainer;
    @BindView(R.id.tvDeviceName)
    AppCompatTextView tvDeviceName;
    @BindView(R.id.tvLocation)
    AppCompatTextView tvLocation;
    @BindView(R.id.btnSignOutFromEverywhere)
    AppCompatButton btnSignOutFromEverywhere;
    @BindView(R.id.rvLoggedInDevices)
    RecyclerView rvLoggedInDevices;


    public LoggedInDevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logged_in_devices, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();

        setUpDagger2(context);

        init();

        initializeClickListeners();

        getMyLoggedInDevicesNetworkCall();


    }

    private void setUpDagger2(Context context) {
        LoggedInDevicesFragmentComponent loggedInDevicesFragmentComponent
                = DaggerLoggedInDevicesFragmentComponent.builder()
                .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                .build();

        loggedInDevicesFragmentComponent.injectLoggedInDevicesFragmentFeature(LoggedInDevicesFragment.this);
    }

    private void init() {

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();

        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (loggedInDeviceList == null) loggedInDeviceList = new ArrayList<>();

        loggedInDeviceAdapter = new LoggedInDeviceAdapter(context, loggedInDeviceList, qxmFragmentTransactionHelper, apiService, token.getUserId(), token.getToken());

        rvLoggedInDevices.setAdapter(loggedInDeviceAdapter);
        rvLoggedInDevices.setLayoutManager(new LinearLayoutManager(context));

    }

    private void initializeClickListeners() {
        ivBackArrow.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
        btnSignOutFromEverywhere.setOnClickListener(v -> logOutMeFromEverywhere());
    }

    private void getMyLoggedInDevicesNetworkCall() {

        Call<List<LoggedInDevice>> getMyLoggedInDevices = apiService.getMyLoggedInDevices(token.getToken(), token.getUserId());

        getMyLoggedInDevices.enqueue(new Callback<List<LoggedInDevice>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoggedInDevice>> call, @NonNull Response<List<LoggedInDevice>> response) {
                Log.d(TAG, "onResponse: getMyLoggedInDevicesNetworkCall");
                Log.d(TAG, "onResponse: response.code " + response.code());
                Log.d(TAG, "onResponse: response.body " + response.body());

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: success");
                    if (response.body() != null) {


                        processLoggedInDevicesData(response.body());
                    }
                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: getMyLoggedInDevicesNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LoggedInDevice>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getMyLoggedInDevicesNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void processLoggedInDevicesData(List<LoggedInDevice> devices) {
        String thisDeviceMac = DeviceInfoHelper.getDeviceInfo(context).getMacAddress();
        LoggedInDevice loggedInDevice = null;
        for (LoggedInDevice device : devices) {
            if (device.getMacAddress().equals(thisDeviceMac)) {
                loggedInDevice = device;
                break;
            }
        }

        if (loggedInDevice != null) {
            llLoggedInDevicesFragmentContainer.setVisibility(View.VISIBLE);
            tvDeviceName.setText(String.format("%s %s", loggedInDevice.getDeviceBrand(), loggedInDevice.getDeviceModel()));
            if (!loggedInDevice.getCity().equals(getString(R.string.unknown)))
                tvLocation.setText(String.format("%s %s", loggedInDevice.getCity(), loggedInDevice.getCountry()));
            else
                tvLocation.setText(getString(R.string.unknown));

            devices.remove(loggedInDevice);
            if (devices.size() == 0) {
                rvLoggedInDevices.setVisibility(View.GONE);
            } else {
                loggedInDeviceList.addAll(devices);
                loggedInDeviceAdapter.notifyDataSetChanged();
            }
        } else {
            Log.d(TAG, "processLoggedInDevicesData: logged in device is not matched ");
            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

        }
    }

    private void logOutMeFromEverywhere() {
        Call<QxmApiResponse> logoutFromOneDevice = apiService.logoutFromAllDevice(token.getToken(), token.getUserId());

        logoutFromOneDevice.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: logOutMeFromThisDevice");
                Log.d(TAG, "onResponse: response.code " + response.code());
                Log.d(TAG, "onResponse: response.body " + response.body());
                if (response.code() == 201) {

                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: logOutMeFromThisDevice failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: logOutMeFromThisDevice");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
