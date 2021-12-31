package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.crux.qxm.R;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.forgetPasswordNetworkCall.ForgetPassWordNetworkCall;
import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordDialog {

    private FragmentActivity fragmentActivity;
    private Context context;
    private QxmApiService apiService;
    private View rootView;

    public ForgetPasswordDialog(FragmentActivity fragmentActivity, Context context, View rootView, QxmApiService apiService) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.apiService = apiService;
        this.rootView = rootView;
    }

    public void showDialog() {
        LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_forget_password, (ViewGroup) rootView, false);

        TextInputEditText tiEtEmailAddress = view.findViewById(R.id.tiEtEmailAddress);
        AppCompatButton btnSend = view.findViewById(R.id.btnSend);

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnSend.setOnClickListener(v -> {

            String email = tiEtEmailAddress.getText().toString().trim();

            if (!TextUtils.isEmpty(email)) {

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context, "The Email is not valid", Toast.LENGTH_SHORT).show();
                } else {

                    ForgetPassWordNetworkCall forgetPassWordNetworkCall = new ForgetPassWordNetworkCall(apiService, context);

                    forgetPassWordNetworkCall.sendForgetPasswordEmail(email, dialog);

                }
            } else {
                Toast.makeText(fragmentActivity, "Please type your email address first.", Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();
    }
}
