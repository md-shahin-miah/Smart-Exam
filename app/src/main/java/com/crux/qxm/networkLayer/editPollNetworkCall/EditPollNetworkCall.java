package com.crux.qxm.networkLayer.editPollNetworkCall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPollNetworkCall {
    private static final String TAG = "EditPollNetworkCall";

    public static void editPollNetworkCall(QxmApiService apiService, Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper,
                                           String token, String userId, String pollId, String pollTitle) {
        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);


        qxmProgressDialog.showProgressDialog(context.getString(R.string.progress_dialog_title_edit_poll), context.getString(R.string.progress_dialog_message_edit_poll), false);

        Call<QxmApiResponse> createPollNetworkCall = apiService.editPoll(token, userId, pollId, pollTitle);

        createPollNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse: editPollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                qxmProgressDialog.closeProgressDialog();

                if (response.code() == 201) {

                    Toast.makeText(context, R.string.toast_message_on_success_poll_update, Toast.LENGTH_SHORT).show();

                    StaticValues.isHomeFeedRefreshingNeeded = true;

                    qxmFragmentTransactionHelper.loadHomeFragment();

                } else if (response.code() == 400) {
                    Toast.makeText(context, response.body() != null ? response.body().getMessage() : context.getString(R.string.toast_something_went_wrong),
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {

                    Log.d(TAG, "onResponse: editPollNetworkCall failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "onFailure: editPollNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
