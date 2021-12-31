package com.crux.qxm.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    private static Toast toast;

    public static void displayToast(Context context,String message) {

        if(toast!=null)
            toast.cancel();

        toast = Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT);
        toast.show();
    }
}
