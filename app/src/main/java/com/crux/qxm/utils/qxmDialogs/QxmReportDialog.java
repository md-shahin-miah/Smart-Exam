package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.report.ReportModel;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.REPORT_GROUP;
import static com.crux.qxm.utils.StaticValues.REPORT_POLL;
import static com.crux.qxm.utils.StaticValues.REPORT_QXM;
import static com.crux.qxm.utils.StaticValues.REPORT_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.REPORT_USER;

public class QxmReportDialog {
    private static final String TAG = "QxmReportDialog";
    private FragmentActivity fragmentActivity;
    private Context context;
    private QxmApiService apiService;
    private String token;
    private String userId;
    private String reportingId;

    public QxmReportDialog(FragmentActivity fragmentActivity, Context context, QxmApiService apiService, String token, String userId, String reportingId) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.apiService = apiService;
        this.token = token;
        this.userId = userId;
        this.reportingId = reportingId;
    }

    // region ShowReportDialog

    public void showReportDialog(String reportFor) {

        LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_report_group, null);

        AppCompatTextView tvReportTitle = view.findViewById(R.id.tvReportTitle);
        RadioGroup rgReport = view.findViewById(R.id.rgReportGroup);
        RadioButton rbImageViolation = view.findViewById(R.id.rbImageViolation);
        RadioButton rbContentViolation = view.findViewById(R.id.rbContentViolation);
        RadioButton rbSpamming = view.findViewById(R.id.rbSpamming);
        AppCompatEditText etUserComment = view.findViewById(R.id.etUserComment);

        AppCompatTextView tvCancel = view.findViewById(R.id.tvCancel);
        AppCompatTextView tvSubmitReport = view.findViewById(R.id.tvSubmitReport);


        Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        ReportModel reportModel = new ReportModel();

        switch (reportFor) {

            case REPORT_QXM:
                tvReportTitle.setText(context.getString(R.string.report_this_qxm));
                break;

            case REPORT_SINGLE_MCQ:
                tvReportTitle.setText(context.getString(R.string.report_this_single_mcq));
                break;

            case REPORT_POLL:
                tvReportTitle.setText(context.getString(R.string.report_this_poll));
                break;

            case REPORT_GROUP:
                tvReportTitle.setText(context.getString(R.string.report_this_group));
                break;

            case REPORT_USER:
                tvReportTitle.setText(context.getString(R.string.report_this_user));
                break;


        }

        tvCancel.setOnClickListener(v -> dialog.dismiss());

        tvSubmitReport.setOnClickListener(v -> {

            if (rgReport.getCheckedRadioButtonId() == rbImageViolation.getId()) {
                reportModel.setImageViolation(true);
                reportModel.setReportedCategory(StaticValues.REPORT_CATEGORY_ADULT_CONTENT);

            } else if (rgReport.getCheckedRadioButtonId() == rbContentViolation.getId()) {
                reportModel.setContentViolation(true);
                reportModel.setReportedCategory(StaticValues.REPORT_CATEGORY_OFFENSIVE_CONTENT);

            } else if (rgReport.getCheckedRadioButtonId() == rbSpamming.getId()) {
                reportModel.setSpamming(true);
                reportModel.setReportedCategory(StaticValues.REPORT_CATEGORY_SPAM_CONTENT);

            }

            if (!TextUtils.isEmpty(etUserComment.getText().toString().trim())) {
                reportModel.setUserComment(etUserComment.getText().toString().trim());

            }

            Log.d(TAG, "showReportDialog: " + reportModel.toString());

            submitReport(reportFor, reportModel);

            dialog.dismiss();

        });

        dialog.show();

    }

    private void submitReport(String reportFor, ReportModel reportModel) {
        switch (reportFor) {
            case REPORT_QXM:
                reportAQxmNetworkCall(reportModel);
                break;
            case REPORT_SINGLE_MCQ:
                reportASingleMCQNetworkCall(reportModel);
                break;
            case REPORT_POLL:
                reportAPollNetworkCall(reportModel);
                break;
            case REPORT_GROUP:
                reportAGroupNetworkCall(reportModel);
                break;
            case REPORT_USER:
                reportAUserNetworkCall(reportModel);
                break;

        }

    }


    private void reportAQxmNetworkCall(ReportModel reportModel) {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(context.getString(R.string.dialog_title_report_submit), context.getString(R.string.dialog_message_report_submit), false);
        Call<QxmApiResponse> reportAQxmNetworkCall = apiService.reportAQxm(token, userId, reportingId, reportModel);

        reportAQxmNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: reportAQxmNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                dialog.closeProgressDialog();

                if (response.code() == 201) {
                    Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(context, context.getResources().getString(R.string.you_have_already_reported_this_content),
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Log.d(TAG, "onResponse: Feed network call failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: reportAQxmNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportASingleMCQNetworkCall(ReportModel reportModel) {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(context.getString(R.string.dialog_title_report_submit), context.getString(R.string.dialog_message_report_submit), false);
        Call<QxmApiResponse> reportAUserNetworkCall = apiService.reportASingleMCQ(token, userId, reportingId, reportModel);

        reportAUserNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: reportASingleMCQNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    dialog.closeProgressDialog();
                    Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 409) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
                    qxmFragmentTransactionHelper.logout();
                    dialog.closeProgressDialog();
                } else {
                    dialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: reportASingleMCQNetworkCall failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: reportASingleMCQNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportAPollNetworkCall(ReportModel reportModel) {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(context.getString(R.string.dialog_title_report_submit), context.getString(R.string.dialog_message_report_submit), false);
        Call<QxmApiResponse> reportAUserNetworkCall = apiService.reportAPoll(token, userId, reportingId, reportModel);

        reportAUserNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: reportAPollNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    dialog.closeProgressDialog();
                    Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 409) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
                    qxmFragmentTransactionHelper.logout();
                    dialog.closeProgressDialog();
                } else {
                    dialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: reportASingleMCQNetworkCall failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: reportAPollNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportAGroupNetworkCall(ReportModel reportModel) {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(context.getString(R.string.dialog_title_report_submit), context.getString(R.string.dialog_message_report_submit), false);
        Call<QxmApiResponse> reportAGroupNetworkCall = apiService.reportAGroup(token, userId, reportingId, reportModel);

        reportAGroupNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: reportAGroupNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    dialog.closeProgressDialog();
                    Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 409) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
                    qxmFragmentTransactionHelper.logout();
                    dialog.closeProgressDialog();
                } else {
                    dialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: reportAGroupNetworkCall failed");
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: reportAGroupNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
            }
        });
    }

    private void reportAUserNetworkCall(ReportModel reportModel) {
        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog(context.getString(R.string.dialog_title_report_submit), context.getString(R.string.dialog_message_report_submit), false);
        Call<QxmApiResponse> reportAUserNetworkCall = apiService.reportAUser(token, userId, reportingId, reportModel);

        reportAUserNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: reportAUserNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                if (response.code() == 201) {
                    dialog.closeProgressDialog();
                    Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 409) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    QxmFragmentTransactionHelper qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
                    qxmFragmentTransactionHelper.logout();
                    dialog.closeProgressDialog();
                } else {
                    dialog.closeProgressDialog();
                    Log.d(TAG, "onResponse: reportAUserNetworkCall failed");
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: reportAUserNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion


}
