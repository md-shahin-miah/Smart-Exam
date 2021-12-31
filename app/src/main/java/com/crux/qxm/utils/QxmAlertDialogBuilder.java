package com.crux.qxm.utils;

import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;

public class QxmAlertDialogBuilder {

    private AlertDialog.Builder builder;

    public QxmAlertDialogBuilder(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);
    }

    public void setTitle(String title) {
        builder.setTitle(title);
    }

    public void setMessage(String message) {
        builder.setMessage(message);
    }

    public AlertDialog.Builder getAlertDialogBuilder() {
        return builder;
    }

    public void setIcon(int iconId){
        builder.setIcon(iconId);
    };

    public void show(){
        builder.show();
    }

}
