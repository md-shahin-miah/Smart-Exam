package com.crux.qxm.networkLayer.editSingleMCQNetworkCall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSingleMCQNetworkCall {

    private static final String TAG = "EditSingleMCQNetworkCal";

    public static void getSingleMCQForEditNetworkCall(QxmApiService apiService, Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper,
                                                      String token, String singleMCQId) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(context.getString(R.string.alert_dialog_title_edit_single_mcq),
                context.getString(R.string.alert_dialog_message_edit_single_mcq),
                false);

        Call<SingleMCQModel> getSingleMCQModel = apiService.getSingleMCQModel(token, singleMCQId);

        getSingleMCQModel.enqueue(new Callback<SingleMCQModel>() {
            @Override
            public void onResponse(@NonNull Call<SingleMCQModel> call, @NonNull Response<SingleMCQModel> response) {
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());
                progressDialog.closeProgressDialog();
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: getSingleMCQModel for edit success");
                    qxmFragmentTransactionHelper.loadEditSingleMultipleChoiceFragment(response.body());


                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: getSingleMCQ Network Call failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SingleMCQModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getSingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                progressDialog.closeProgressDialog();

                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
