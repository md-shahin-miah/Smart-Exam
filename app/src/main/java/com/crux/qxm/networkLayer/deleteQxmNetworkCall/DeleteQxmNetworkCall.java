package com.crux.qxm.networkLayer.deleteQxmNetworkCall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteQxmNetworkCall {
    private static final String TAG = "DeleteQxmNetworkCall";

    public OnDeleteComplete onDeleteComplete;

    public static void deleteQxmNetworkCall(QxmApiService apiService, Context context,
                                            QxmFragmentTransactionHelper qxmFragmentTransactionHelper,
                                            String token, String userId, String qxmId,OnDeleteComplete onDeleteComplete) {


        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(context.getString(R.string.alert_dialog_title_delete_qxm),
                context.getString(R.string.progress_dialog_message_delete_qxm),
                false);

        Call<QxmApiResponse> deleteQxm = apiService.deleteQxm(token, userId, qxmId);

        deleteQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                progressDialog.closeProgressDialog();
                Log.d(TAG, "onResponse: response.code = " + response.code());

                if (response.code() == 201) {
                    onDeleteComplete.onDeleteComplete();
                    Toast.makeText(context, "Qxm Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: deleteQxmNetworkCall failed");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteQxmNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

                progressDialog.closeProgressDialog();

                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setOnDeleteComplete(OnDeleteComplete onDeleteComplete){
        this.onDeleteComplete = onDeleteComplete;
    }

    public interface OnDeleteComplete{
        void onDeleteComplete();
    }

}
