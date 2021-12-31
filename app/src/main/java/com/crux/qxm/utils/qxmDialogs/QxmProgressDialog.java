package com.crux.qxm.utils.qxmDialogs;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class QxmProgressDialog {

    private ProgressDialog progressDialog;

    private Context context;

    public QxmProgressDialog(Context context) {

        this.context = context;
        this.progressDialog = new ProgressDialog(context);
    }

    // region ShowProgressDialog


    public void showProgressDialog(String title, String message, boolean cancelable) {

        if(!((AppCompatActivity) context).isFinishing())
        {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(cancelable);
            progressDialog.show();
        }


    }

    // endregion

    // region CloseProgressDialog

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }

    // endregion
}
