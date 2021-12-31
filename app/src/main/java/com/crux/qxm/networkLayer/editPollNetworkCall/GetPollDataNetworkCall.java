package com.crux.qxm.networkLayer.editPollNetworkCall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.poll.pollEdit.PollData;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPollDataNetworkCall {

    private static final String TAG = "GetPollDataNetworkCall";

    public static void getPollDataNetworkCall(QxmApiService apiService, Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper,
                                              String token, String pollId) {
        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);
        qxmProgressDialog.showProgressDialog(context.getString(R.string.progress_dialog_title_fetch_poll_data), context.getString(R.string.progress_dialog_message_fetch_poll_data), false);

        Call<PollData> getFullPollData = apiService.getFullPollData(token, pollId);

        getFullPollData.enqueue(new Callback<PollData>() {
            @Override
            public void onResponse(@NonNull Call<PollData> call, @NonNull Response<PollData> response) {

                Log.d(TAG, "onResponse: getPollDataNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                qxmProgressDialog.closeProgressDialog();

                if (response.code() == 200) {

                    qxmFragmentTransactionHelper.loadEditPollFragment(response.body());

                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {

                    Log.d(TAG, "onResponse: getPollDataNetworkCall failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PollData> call, @NonNull Throwable t) {

                qxmProgressDialog.closeProgressDialog();
                Log.d(TAG, "onFailure: getPollDataNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
