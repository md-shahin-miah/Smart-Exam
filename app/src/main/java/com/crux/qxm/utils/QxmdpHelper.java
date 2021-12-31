package com.crux.qxm.utils;

import android.content.Context;

public class QxmdpHelper {
    public static int getDp(Context context, int dpValue){
        float d = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * d);
    }
}
