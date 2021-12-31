package com.crux.qxm.networkLayer.postQxmNetworkCall;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_DRAFTED;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_PUBLISHED;

public class PostQxmToBackEndNetworkCall {

    private static final String TAG = "PostQxmToBackEndNetwork";

    // region PostQxmTOBackend

    public void postQxmToBackEnd(Context context, FragmentActivity activity, QxmToken token, QxmApiService apiService, QxmModel qxmModel, QxmDraft qxmDraft, boolean isDraft) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        if (isDraft) {
            progressDialog.showProgressDialog("Draft QXM", "Qxm is being drafted...", false);
            qxmModel.getQuestionSettings().setQuestionStatus(QUESTION_STATUS_DRAFTED);
        } else {
            progressDialog.showProgressDialog("Publish QXM", "Qxm is being published...", false);
            qxmModel.getQuestionSettings().setQuestionStatus(QUESTION_STATUS_PUBLISHED);
        }


        Call<QxmModel> postQxm = apiService.postQxm(token.getToken(), token.getUserId(), qxmModel);

        postQxm.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                Log.d(TAG, "onResponse: postQxm network call");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());


                if (response.code() == 201) {
                    Log.d(TAG, "onResponse: postQxm success");
                    Log.d(TAG, "onResponse: new Qxm created successfully");

                    // if this qxm is previously drafted on device then delete it
                    if (qxmDraft != null) {
                        Log.d(TAG, "onResponse: qxmDraft: " + qxmDraft.toString());
                        RealmService realmService = new RealmService(Realm.getDefaultInstance());
                        realmService.deleteDraftedQxmById(qxmDraft.getId());
                        realmService.close();
                    }

                    if (!isDraft)
                        Toast.makeText(context, "Qxm posted successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Qxm drafted successfully", Toast.LENGTH_SHORT).show();

                    progressDialog.closeProgressDialog();

                    /*Intent intent = new Intent();

                    if (isDraft)
                        intent.putExtra(QXM_PRIVACY_KEY, QUESTION_STATUS_DRAFTED);
                    else
                        intent.putExtra(QXM_PRIVACY_KEY, QUESTION_STATUS_PUBLISHED);
                    activity.setResult(Activity.RESULT_OK, intent);*/

                    if (activity != null) {
                        activity.finish();
                    }


                } else if (response.code() == 400) {
                    progressDialog.closeProgressDialog();
                    Toast.makeText(context, R.string.the_minimum_interval_time_between_post_message, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(activity);
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    progressDialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: postQxm failed");

                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure postQxm: network call.");
                Log.d(TAG, "LocalizedMessage: " + t.getLocalizedMessage());
                Log.d(TAG, "Message: " + t.getMessage());
                progressDialog.closeProgressDialog();

                saveQxmAsDraftInDevice(activity, context, qxmDraft, qxmModel);

                Toast.makeText(context, "Failed to post Qxm but saved as draft on your device.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveQxmAsDraftInDevice(FragmentActivity activity, Context context, QxmDraft qxmDraft, QxmModel qxmModel) {

        qxmModel.getQuestionSettings().setQuestionStatus(QUESTION_STATUS_DRAFTED);

        Type type = new TypeToken<QxmModel>() {
        }.getType();
        Gson gson = new Gson();
        String qxmJson = gson.toJson(qxmModel, type);
        Log.d(TAG, "QXM JSON: " + qxmJson);

        if (qxmDraft == null)
            qxmDraft = new QxmDraft();


        qxmDraft.setQuestionSetTitle(qxmModel.getQuestionSet().getQuestionSetTitle());
        qxmDraft.setQuestionSetDescription(qxmModel.getQuestionSet().getQuestionSetDescription());
        qxmDraft.setQuestionCategory(qxmModel.getQuestionSettings().getQuestionCategory());
        qxmDraft.setQuestionStatus(qxmModel.getQuestionSettings().getQuestionStatus());

        qxmDraft.setQuestionCategories(QxmArrayListToStringProcessor.getStringFromArrayList(qxmModel.getQuestionSettings().getQuestionCategory()));

        qxmDraft.setLastEditedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        if (TextUtils.isEmpty(qxmDraft.getCreatedAt()))
            qxmDraft.setCreatedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        //QuestionSet Full JSON
        qxmDraft.setQuestionSetJson(qxmJson);

        // Save Qxm in Device as Draft
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        realmService.saveQxmAsDraft(qxmDraft);
        realmService.close();

        Toast.makeText(context, "This Exam has saved as a draft on your device.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "saveQxmAsDraft: This Exam has saved as a draft on your device is successful.");

        if (activity != null) {
            activity.finish();
            activity.getFragmentManager().popBackStack();
        }

    }

    // endregion
}
