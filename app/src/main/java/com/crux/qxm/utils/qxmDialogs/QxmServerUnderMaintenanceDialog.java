package com.crux.qxm.utils.qxmDialogs;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class QxmServerUnderMaintenanceDialog {


    public static void showServerMaintenanceDialog(Context context){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Server is under maintenance !");
        alertDialogBuilder.setMessage("Our server is currently under maintenance. We are on it, stay tune.");
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private static void getDataFromFirebase(){



    }

}
