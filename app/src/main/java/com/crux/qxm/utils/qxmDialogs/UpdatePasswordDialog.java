package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.updatePasswordNetworkCall.UpdatePasswordNetworkCall;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.google.android.material.textfield.TextInputEditText;

public class UpdatePasswordDialog {

    private FragmentActivity fragmentActivity;
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;
    private View rootView;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public UpdatePasswordDialog(FragmentActivity fragmentActivity, Context context, QxmApiService apiService, QxmToken token, View rootView, QxmFragmentTransactionHelper qxmFragmentTransactionHelper) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.apiService = apiService;
        this.token = token;
        this.rootView = rootView;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }

    public void showDialog() {
        LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_update_password, (ViewGroup) rootView, false);

        TextInputEditText tiEtCurrentPassword = view.findViewById(R.id.tiEtCurrentPassword);
        TextInputEditText tiEtNewPassword = view.findViewById(R.id.tiEtNewPassword);
        TextInputEditText tiEtConfirmPassword = view.findViewById(R.id.tiEtConfirmPassword);
        AppCompatTextView tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword);

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tvUpdatePassword.setOnClickListener(v -> {

            String currentPassword = tiEtCurrentPassword.getText().toString().trim();
            String newPassword = tiEtNewPassword.getText().toString().trim();
            String confirmPassword = tiEtConfirmPassword.getText().toString().trim();

            if (!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword)
                    && !TextUtils.isEmpty(confirmPassword)) {

                if (currentPassword.length() < 6) {
                    Toast.makeText(context, R.string.current_password_length_is_too_short_msg, Toast.LENGTH_SHORT).show();
                }else if(currentPassword.length() > 32){
                    Toast.makeText(context, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                } else if(newPassword.equals(confirmPassword)) {

                    if(newPassword.length() < 6){
                        Toast.makeText(fragmentActivity, R.string.new_password_length_is_too_short_msg, Toast.LENGTH_SHORT).show();
                    }else if(newPassword.length() > 32){
                        Toast.makeText(fragmentActivity, R.string.new_password_length_is_too_big_msg, Toast.LENGTH_SHORT).show();
                    }else{
                        UpdatePasswordNetworkCall updatePasswordNetworkCall = new UpdatePasswordNetworkCall(context, apiService, token,
                                qxmFragmentTransactionHelper);
                        updatePasswordNetworkCall.updatePassword(currentPassword, newPassword, confirmPassword, dialog);
                    }

                }else{
                    Toast.makeText(fragmentActivity, R.string.password_do_not_match_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(fragmentActivity, R.string.please_input_all_fields, Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();
    }
}
