package com.crux.qxm.networkLayer.updatePasswordNetworkCall;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordNetworkCall {
    private static final String TAG = "UpdatePasswordNetworkCa";
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public UpdatePasswordNetworkCall(Context context, QxmApiService apiService, QxmToken token, QxmFragmentTransactionHelper qxmFragmentTransactionHelper) {
        this.context = context;
        this.apiService = apiService;
        this.token = token;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }

    public void updatePassword(String currentPassword, String newPassword, String confirmPassword, Dialog dialog) {

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);
        qxmProgressDialog.showProgressDialog(context.getString(R.string.progress_dialog_title_update_password),
                context.getString(R.string.progress_dialog_message_update_password), false);

        Call<QxmApiResponse> updatePassword = apiService.updatePassword(token.getToken(), token.getUserId(), currentPassword, newPassword, confirmPassword);

        updatePassword.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: updatePassword");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                qxmProgressDialog.closeProgressDialog();

                if (response.code() == 201) {
                    Toast.makeText(context, R.string.password_updated_successfully, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else if (response.code() == 403) {
                    Toast.makeText(context, R.string.login_session_expired_message, Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } if (response.code() == 400)
                    Toast.makeText(context, context.getString(R.string.current_password_not_matched), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "onFailure: updatePassword");
                Log.d(TAG, "onFailure: error message: " + t.getMessage());
                Log.d(TAG, "onFailure: error stackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, R.string.network_call_failure_message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
