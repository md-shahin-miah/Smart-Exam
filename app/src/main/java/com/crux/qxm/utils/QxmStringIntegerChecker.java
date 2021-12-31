package com.crux.qxm.utils;

import android.text.TextUtils;

public class QxmStringIntegerChecker {

    public static boolean isLongInt(String stringLong){

        String regex = "[0-9]+";

        if(!TextUtils.isEmpty(stringLong)) return stringLong.matches(regex);
        else return false;
    }

    public static boolean isInt(String stringInt){

        String regex = "[0-9]+";

        if(!TextUtils.isEmpty(stringInt)) return stringInt.matches(regex);
        else return false;
    }

    public static boolean isNumber(String string){

        String regex = "[0-9]+";

        if(!string.isEmpty()) return string.matches(regex);
        else return false;
    }
}
