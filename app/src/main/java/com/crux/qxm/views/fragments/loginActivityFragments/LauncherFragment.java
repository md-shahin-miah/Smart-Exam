package com.crux.qxm.views.fragments.loginActivityFragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.R;
import com.crux.qxm.utils.NetworkState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LauncherFragment extends Fragment {


    private static final String TAG = "LauncherFragment";
    AlertDialog alertDialog;
    @BindView(R.id.llcUpdateLoaderContainer)
    LinearLayoutCompat llcUpdateLoaderContainer;
    @BindView(R.id.noInternetView)
    View noInternetView;
    private Context context;

    public LauncherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        checkIfkUpdatedApkAvailable();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        //showing noInternetView
        if (!NetworkState.haveNetworkConnection(context)){
            llcUpdateLoaderContainer.setVisibility(GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }
        else{

            checkIfkUpdatedApkAvailable();
        }

        AppCompatTextView tvRetry = noInternetView.findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(v -> {
            noInternetView.setVisibility(GONE);
            checkIfkUpdatedApkAvailable();
        });


    }

    //region init
    private void init(View view) {
        context = view.getContext();
    }
    //endregion

    private void checkIfkUpdatedApkAvailable() {


        if (!NetworkState.haveNetworkConnection(context)){
            llcUpdateLoaderContainer.setVisibility(GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }else {
            llcUpdateLoaderContainer.setVisibility(View.VISIBLE);
            noInternetView.setVisibility(View.GONE);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("version");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isAdded() && isVisible() && getUserVisibleHint()) {

                    llcUpdateLoaderContainer.setVisibility(View.GONE);
                    Log.d(TAG, "onDataChange dataSnapshot: " + dataSnapshot.toString());
                    String latestVersion = (String) dataSnapshot.getValue();
                    String installedVersion = context.getResources().getString(R.string.version_code);

                    if (latestVersion != null) {

                        if (!latestVersion.equals(installedVersion)) {

                            generateUpdateDialog(context, installedVersion, latestVersion);

                        } else {

                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                            loadLoginFragment(Objects.requireNonNull(getActivity()));
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                llcUpdateLoaderContainer.setVisibility(View.GONE);
                Log.d(TAG, "onCancelled details: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled message: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled code: "+databaseError.getCode());
            }
        });
    }

    //region generateUpdateDialog
    private void generateUpdateDialog(Context context, String currentApkVersion, String latestApkVersion) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_dialog_message_updated_apk_available));

        //alertDialogBuilder.setMessage(context.getResources().getString(R.string.your_current_apk_version) +" "+ currentApkVersion+" " +"You must update to the latest version "+ latestApkVersion + " to continue the app.");
        alertDialogBuilder.setMessage(String.format(Locale.ENGLISH, "%s %s, %s", context.getResources().getString(R.string.your_current_apk_version), currentApkVersion, "you must update to the latest version " + latestApkVersion + " to continue the app."));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Update", (dialogInterface, i) -> {

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context.getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }

        });

        alertDialog = alertDialogBuilder.create();

        if (!((AppCompatActivity) context).isFinishing()) {
            alertDialog.show();
        }


    }
    //endregion

    // region LoadLoginFragment
    private void loadLoginFragment(FragmentActivity fragmentActivity) {
        // initializing with login fragment
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // endregion


}
