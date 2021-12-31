package com.crux.qxm.utils.appUpdateAndMaintenanceHelper;

import android.content.Context;
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

public class ServerMaintenanceDialog {

    private static final String TAG = "ServerMaintenanceDialog";
    private  AlertDialog serverStatusAlertDialog;

    public ServerMaintenanceDialog(AlertDialog alertDialog){
        this.serverStatusAlertDialog = alertDialog;
    }

    public  void checkIfServerUnderMaintenance(Fragment fragment) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("server_status");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (fragment.isAdded() && fragment.isVisible() && fragment.getUserVisibleHint()) {

                    if (serverStatusAlertDialog != null)
                        serverStatusAlertDialog.dismiss();

                    Log.d(TAG, "onDataChange dataSnapshot server_status : " + dataSnapshot.toString());

                    String serverStatus = (String) dataSnapshot.getValue();

                    if (serverStatus != null && !serverStatus.isEmpty()) {

                        generateServerStatusDialog(fragment.getContext(), serverStatus);

                    } else {

                        if (serverStatusAlertDialog != null) {
                            serverStatusAlertDialog.dismiss();
                        }
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
    private  void generateServerStatusDialog(Context context, String message) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_server_under_maintenance));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);

        if (!((AppCompatActivity) context).isFinishing()) {

            serverStatusAlertDialog = alertDialogBuilder.create();
            serverStatusAlertDialog.show();
        }


    }
    //endregion
}
