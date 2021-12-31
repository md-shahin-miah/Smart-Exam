package com.crux.qxm.networkLayer.editQxmNetworkCall;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.CreateQxmActivity;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.EDIT_QXM_REQUEST;
import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_EDIT_KEY;

public class EditQxmNetworkCall {

    private static final String TAG = "EditQxmNetworkCall";

    public static void editQxmNetworkCall(QxmApiService apiService, Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper,
                                          FragmentActivity activity, String token, String userId, String qxmId) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(context.getString(R.string.alert_dialog_title_edit_qxm),
                context.getString(R.string.alert_dialog_message_edit_qxm),
                false);

        Call<QxmModel> editQxm = apiService.editQxm(token, userId, qxmId);

        editQxm.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                progressDialog.closeProgressDialog();
                Log.d(TAG, "onResponse: response.code = " + response.code());

                if (response.code() == 200) {

                    if (response.body() != null) {

                        QxmDraft qxmDraft = makeQxmDraftFromQxmModel(response.body());
                        Intent intent = new Intent(activity, CreateQxmActivity.class);
                        intent.putExtra(QXM_DRAFT_KEY, qxmDraft);
                        intent.putExtra(QXM_EDIT_KEY, true);
                        // Objects.requireNonNull(getActivity()).startActivity(intent);

                        // Todo:: startActivityForResult should be removed when fix Qxm Duplicates issue when edit qxm
                        Objects.requireNonNull(activity).startActivityForResult(intent, EDIT_QXM_REQUEST);
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();

                } else {
                    Log.d(TAG, "onResponse: editQxmNetworkCall failed");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: editQxmNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

                progressDialog.closeProgressDialog();

                /*//hiding noInternetView
                if (!NetworkState.haveNetworkConnection(context))
                    noInternetView.setVisibility(View.VISIBLE);
                else
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();*/
            }
        });


    }

    //region QxmDraft from QxmModel
    private static QxmDraft makeQxmDraftFromQxmModel(QxmModel qxmModel) {

        QxmDraft qxmDraft = new QxmDraft();

        qxmDraft.setId(qxmModel.getId());
        qxmDraft.setQuestionSetTitle(qxmModel.getQuestionSet().getQuestionSetTitle());
        qxmDraft.setQuestionSetDescription(qxmModel.getQuestionSet().getQuestionSetDescription());
        qxmDraft.setQuestionSetThumbnail(qxmModel.getQuestionSet().getQuestionSetThumbnail());
        qxmDraft.setYoutubeLink(qxmModel.getQuestionSet().getQuestionSetYoutubeLink());
        qxmDraft.setQuestionStatus(qxmModel.getQuestionSettings().getQuestionStatus());
        qxmDraft.setQuestionCategory(qxmModel.getQuestionSettings().getQuestionCategory());

        Gson gson = new Gson();
        String questionSetJson = gson.toJson(qxmModel);
        qxmDraft.setQuestionSetJson(questionSetJson);

        qxmDraft.setLastEditedAt(qxmModel.getQuestionSettings().getLastEditedAt());
        qxmDraft.setCreatedAt(qxmModel.getQuestionSettings().getCreatedAt());

        return qxmDraft;
    }
    //endregion
}
