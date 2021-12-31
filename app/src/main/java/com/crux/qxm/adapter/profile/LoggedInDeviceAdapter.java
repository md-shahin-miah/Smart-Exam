package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.users.LoggedInDevice;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoggedInDeviceAdapter extends RecyclerView.Adapter<LoggedInDeviceAdapter.ViewHolder> {
    private static final String TAG = "LoggedInDeviceAdapter";
    private Context context;
    private List<LoggedInDevice> items;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private String userId;
    private String token;

    public LoggedInDeviceAdapter(Context context, List<LoggedInDevice> items, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, QxmApiService apiService, String userId, String token) {
        this.context = context;
        this.items = items;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View loggedInDeviceSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_logged_in_devices_single_item, parent, false);

        return new LoggedInDeviceAdapter.ViewHolder(loggedInDeviceSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDeviceName.setText(String.format(
                "%s %s", items.get(position).getDeviceBrand(), items.get(position).getDeviceModel()
        ));

        if (!items.get(position).getCity().equals("Unknown"))
            holder.tvLocation.setText(String.format(
                    "%s %s", items.get(position).getCity(), items.get(position).getCountry()
            ));
        else
            holder.tvLocation.setText(items.get(position).getCity());

        holder.btnLogOut.setOnClickListener(v -> {
            logOutMeFromThisDevice(items.get(position).getMacAddress());
        });

    }

    private void logOutMeFromThisDevice(String macAddress) {
        Call<QxmApiResponse> logoutFromOneDevice = apiService.logoutFromOneDevice(token, userId, macAddress);

        logoutFromOneDevice.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: logOutMeFromThisDevice");
                Log.d(TAG, "onResponse: response.code " + response.code());
                Log.d(TAG, "onResponse: response.body " + response.body());
                if (response.code() == 201) {
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDeviceName)
        AppCompatTextView tvDeviceName;
        @BindView(R.id.tvLocation)
        AppCompatTextView tvLocation;
        @BindView(R.id.btnLogOut)
        AppCompatTextView btnLogOut;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
