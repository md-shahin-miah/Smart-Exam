package com.crux.qxm.networkLayer.forgetPasswordNetworkCall;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassWordNetworkCall {
    private static final String TAG = "ForgetPassWordNetworkCa";
    private QxmApiService apiService;
    private Context context;

    public ForgetPassWordNetworkCall(QxmApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public void sendForgetPasswordEmail(String email, Dialog dialog) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog("Sending Email", "A password reset email is being sent to your email address.", false);

        Call<QxmApiResponse> sendForgetPasswordRequest = apiService.sendForgetPasswordRequest(email);

        sendForgetPasswordRequest.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: sendForgetPasswordRequest");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                if (response.code() == 200) {

                    Toast.makeText(context, "Please check your email, an email has been sent to your email.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                } else if (response.code() == 404)
                    Toast.makeText(context, "No account found with this email.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, context.getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

                progressDialog.closeProgressDialog();
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: sendForgetPasswordRequest");
                Log.d(TAG, "onFailure: error message: " + t.getMessage());
                Log.d(TAG, "onFailure: error stackTrace: " + Arrays.toString(t.getStackTrace()));
                progressDialog.closeProgressDialog();
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
