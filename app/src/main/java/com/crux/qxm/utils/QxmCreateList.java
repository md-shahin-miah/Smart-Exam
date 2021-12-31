package com.crux.qxm.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;
import com.crux.qxm.adapter.myQxm.list.ListPrivacyAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.myQxm.list.ListSettings;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PUBLIC;

public class QxmCreateList {

    private static final String TAG = "QxmCreateList";
    private Context context;
    private QxmApiService apiService;
    private String userId;
    private String token;
    private QxmCreateListListeners qxmCreateListListeners;

    public QxmCreateList(Context context, QxmApiService apiService, String userId, String token, QxmCreateListListeners qxmCreateListListeners) {
        this.context = context;
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
        this.qxmCreateListListeners = qxmCreateListListeners;
    }

    public QxmCreateList(Context context, QxmApiService apiService, String userId, String token) {
        this.context = context;
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
    }

    // call from MyQxmListFragment
    public void createListDialogShow() {
        AppCompatDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater groupInflater = LayoutInflater.from(context);
        View createListGroupView = groupInflater.inflate(R.layout.dialog_create_new_list, null);

        AppCompatEditText etListTitle = createListGroupView.findViewById(R.id.etListTitle);
        AppCompatSpinner spinnerListPrivacy = createListGroupView.findViewById(R.id.spinnerListPrivacy);

        List<String> privacyList = new ArrayList<>();
        privacyList.add("Public");
        privacyList.add("Private");

        ListPrivacyAdapter listPrivacyAdapter = new ListPrivacyAdapter(createListGroupView.getContext(), R.layout.single_list_privacy, privacyList);
        spinnerListPrivacy.setAdapter(listPrivacyAdapter);

        builder.setView(createListGroupView);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {


            if (!TextUtils.isEmpty(etListTitle.getText().toString().trim())) {

                ListSettings listSettings = new ListSettings();

                listSettings.setListName(etListTitle.getText().toString().trim());

                if (spinnerListPrivacy.getSelectedItemPosition() == 0) {
                    listSettings.setListPrivacy(LIST_PRIVACY_PUBLIC);
                } else {
                    listSettings.setListPrivacy(LIST_PRIVACY_PRIVATE);
                }


                Log.d(TAG, "onViewCreated ListSetting :" + listSettings);
                Call<QxmApiResponse> createNewList = apiService.createNewList(token, userId, listSettings);


                QxmProgressDialog createProgressDialog = new QxmProgressDialog(context);
                createProgressDialog.showProgressDialog("Creating List", String.format("List named %s is being created. Please wait... ", listSettings.getListName()), false);


                createNewList.enqueue(new Callback<QxmApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                        Log.d(TAG, "onResponse createNewList body: " + response.body());
                        Log.d(TAG, "onResponse createNewList code: " + response.code());

                        if (response.code() == 201) {

                            createProgressDialog.closeProgressDialog();
                            dialogInterface.dismiss();
                            qxmCreateListListeners.onListCreatedListener();
                            Toast.makeText(context, "List created successfully!", Toast.LENGTH_SHORT).show();

                        } else {

                            createProgressDialog.closeProgressDialog();
                            Toast.makeText(context, "Something went wrong during creating list!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                        Log.d(TAG, "onFailure createNewList: " + t.getMessage());
                        Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        createProgressDialog.closeProgressDialog();
                        dialogInterface.dismiss();
                    }
                });

            } else {

                Toast.makeText(context, "List title can not be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());

        dialog = builder.create();
        dialog.show();
    }

    // Call from feed
    void createListDialogShow(FragmentActivity fragmentActivity, String qxmId, String qxmQuestionSetTitle) {
        AppCompatDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater groupInflater = LayoutInflater.from(context);
        View createListGroupView = groupInflater.inflate(R.layout.dialog_create_new_list, null);

        AppCompatEditText etListTitle = createListGroupView.findViewById(R.id.etListTitle);
        AppCompatSpinner spinnerListPrivacy = createListGroupView.findViewById(R.id.spinnerListPrivacy);

        List<String> privacyList = new ArrayList<>();
        privacyList.add("Public");
        privacyList.add("Private");

        ListPrivacyAdapter listPrivacyAdapter = new ListPrivacyAdapter(createListGroupView.getContext(), R.layout.single_list_privacy, privacyList);
        spinnerListPrivacy.setAdapter(listPrivacyAdapter);

        builder.setView(createListGroupView);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {

            if (!TextUtils.isEmpty(etListTitle.getText().toString().trim())) {

                ListSettings listSettings = new ListSettings();

                listSettings.setListName(etListTitle.getText().toString().trim());

                if (spinnerListPrivacy.getSelectedItemPosition() == 0) {
                    listSettings.setListPrivacy(LIST_PRIVACY_PUBLIC);
                } else {
                    listSettings.setListPrivacy(LIST_PRIVACY_PRIVATE);
                }


                Log.d(TAG, "onViewCreated ListSetting :" + listSettings);
                Call<QxmApiResponse> createNewList = apiService.createNewList(token, userId, listSettings);


                QxmProgressDialog createProgressDialog = new QxmProgressDialog(context);
                createProgressDialog.showProgressDialog("Creating List", String.format("List named %s is being created. Please wait... ", listSettings.getListName()), false);


                createNewList.enqueue(new Callback<QxmApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                        Log.d(TAG, "onResponse createNewList body: " + response.body());
                        Log.d(TAG, "onResponse createNewList code: " + response.code());

                        if (response.code() == 201) {

                            createProgressDialog.closeProgressDialog();
                            dialogInterface.dismiss();

                            QxmAddQxmToList addQxmToList = new QxmAddQxmToList( fragmentActivity
                            , context, apiService, userId, token);
                            addQxmToList.addQxmToList(qxmId, qxmQuestionSetTitle);

                        } else {

                            createProgressDialog.closeProgressDialog();
                            Toast.makeText(context, "Something went wrong during creating list!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                        Log.d(TAG, "onFailure createNewList: " + t.getMessage());
                        Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        createProgressDialog.closeProgressDialog();
                        dialogInterface.dismiss();
                    }
                });

            } else {

                Toast.makeText(context, "List title can not be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());

        dialog = builder.create();
        dialog.show();
    }

    public interface QxmCreateListListeners{

        void onListCreatedListener();
    }

}
