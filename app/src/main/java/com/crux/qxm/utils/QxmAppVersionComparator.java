package com.crux.qxm.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crux.qxm.R;

import java.util.Arrays;

public class QxmAppVersionComparator {

    private static final String TAG = "VersionComparator";

    public static boolean isInstalledVersionBehind(Context context,String installedVersion, String updatedVersion){


        String splitedInstalledVersion[] = installedVersion.split("\\.");
        String splitedUpdatedVersion[] = updatedVersion.split("\\.");

        Log.d(TAG, "splittedInstalledVersion: "+ Arrays.toString(splitedInstalledVersion));
        Log.d(TAG, "splittedUpdatedVersion: "+ Arrays.toString(splitedUpdatedVersion));

        if(splitedInstalledVersion.length == splitedUpdatedVersion.length){


            for(int i=0;i<splitedInstalledVersion.length;i++){


                if(QxmStringIntegerChecker.isNumber(splitedInstalledVersion[i])
                    && QxmStringIntegerChecker.isNumber(splitedUpdatedVersion[i])){

                    String installedVersionSlice = splitedInstalledVersion[i].trim();
                    String updatedVersionSlice = splitedUpdatedVersion[i].trim();

                    float installedVersionSliceInFloat = Float.parseFloat(installedVersionSlice);
                    float updatedVersionSliceInFloat = Float.parseFloat(updatedVersionSlice);

                    Log.d(TAG, "installedVersionSliceInFloat: "+installedVersionSliceInFloat);
                    Log.d(TAG, "updatedVersionSliceInFloat: "+updatedVersionSliceInFloat);


                    if(installedVersionSliceInFloat<updatedVersionSliceInFloat)
                        return true;


                }else {

                    Toast.makeText(context, R.string.version_name_must_be_number, Toast.LENGTH_SHORT).show();
                    //System.out.println("Version name must contain number");
                }


            }

        }else {

            Toast.makeText(context, R.string.both_versions_must_contain_same_decimal_count, Toast.LENGTH_SHORT).show();
            //System.out.println("Both version must contain same decimal count");
        }


        return false;
    }
}
