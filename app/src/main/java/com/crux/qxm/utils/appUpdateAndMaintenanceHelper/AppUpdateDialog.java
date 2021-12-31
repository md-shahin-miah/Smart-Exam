package com.crux.qxm.utils.appUpdateAndMaintenanceHelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppUpdateDialog {

    private static final String TAG = "AppUpdateDialog";
    private AlertDialog appUpdateDialog;

    public AppUpdateDialog(AlertDialog alertDialog) {
        this.appUpdateDialog = alertDialog;
    }

    public void checkIfApkUpdatedApkAvailable(Fragment fragment) {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("app_version_code");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (fragment.isAdded() && fragment.isVisible() && fragment.getUserVisibleHint()) {

                    if (appUpdateDialog != null)
                        appUpdateDialog.dismiss();

                    Log.d(TAG, "onDataChange dataSnapshot: " + dataSnapshot.toString());


                    String appVersion = (String) dataSnapshot.getValue();

                    try {
                        PackageInfo pInfo =
                                fragment.getActivity().getPackageManager().getPackageInfo(fragment.getActivity().getPackageName(), 0);
                        String version = pInfo.versionName;
                        long versionCode;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            versionCode = pInfo.getLongVersionCode();
                        } else versionCode = pInfo.versionCode;
                        if (appVersion != null && !appVersion.isEmpty()) {
                            long updatedVersionCode = Long.parseLong(appVersion);
                            if (updatedVersionCode > versionCode) {
                                if (appUpdateDialog != null)
                                    appUpdateDialog.dismiss();
                                generateUpdateDialog(fragment.getContext());
                            } else {
                                if (appUpdateDialog != null) {
                                    appUpdateDialog.dismiss();
                                }
                            }

                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled details: " + databaseError.getDetails());
                Log.d(TAG, "onCancelled message: " + databaseError.getMessage());
                Log.d(TAG, "onCancelled code: " + databaseError.getCode());
            }
        });
    }

    //region generateServerStatusDialog
    private void generateUpdateDialog(Context context) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_updated_apk_available));

        //alertDialogBuilder.setMessage(context.getResources().getString(R.string.your_current_apk_version) +" "+ currentApkVersion+" " +"You must update to the latest version "+ latestApkVersion + " to continue the app.");
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.you_are_using_old_version));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Update", (dialogInterface, i) -> {

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context.getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }

            dialogInterface.dismiss();

        });


        if (!((AppCompatActivity) context).isFinishing()) {

            appUpdateDialog = alertDialogBuilder.create();
            appUpdateDialog.show();
        }


    }
    //endregion
}
