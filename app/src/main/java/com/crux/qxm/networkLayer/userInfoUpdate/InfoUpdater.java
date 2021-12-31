package com.crux.qxm.networkLayer.userInfoUpdate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.users.Address;
import com.crux.qxm.db.models.users.Institution;
import com.crux.qxm.db.models.users.Occupation;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by frshafi on 2/16/18.
 */

public class InfoUpdater {

    private static final String TAG = "InfoUpdater";
    private Context context;

    // default constructor
    public InfoUpdater() {
    }


    // Constructor with context parameter
    InfoUpdater(Context context) {

        this.context = context.getApplicationContext();
    }

    // region updateSocialSite
    public static void updateSocialSite(Context context, QxmApiService apiService, QxmToken token, String socialSiteName, String previousId, String newId, String successMessage) {


        Call<QxmApiResponse> addOrUpdateSocialAddressNetworkCall = apiService.addOrUpdateSocialAddress(token.getToken(), token.getUserId(), socialSiteName, previousId, newId);
        addOrUpdateSocialAddressNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());


                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();
            }
        });


    }
    //endregion

    //region updateDescription
    public static void updateDescription(Context context, QxmApiService apiService, QxmToken token, String description, String successMessage) {


        Call<QxmApiResponse> updateDescriptionNetworkCall = apiService.updateDescription(token.getToken(), token.getUserId(), description);

        updateDescriptionNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());


                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });

    }
    //endregion

    public static void addOrUpdateEducationalInstitute(Context context, QxmApiService apiService, QxmToken token, Institution institution, boolean deleteRequest, String successMessage) {


        Call<QxmApiResponse> addOrUpdateEducationalInstituteNetworkCall =
                apiService.updateEducation(
                        token.getToken(),
                        token.getUserId(),
                        institution.getId(),
                        institution.getInstituteName(),
                        institution.getFieldOfStudy(),
                        institution.getGrade(),
                        institution.getDegree(),
                        institution.getFromTo(),
                        deleteRequest
                );


        addOrUpdateEducationalInstituteNetworkCall.enqueue(new Callback<QxmApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());


                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void addEditOrDeleteOccupation(Context context, QxmApiService apiService, QxmToken token, Occupation occupation, boolean deleteRequest, String successMessage) {


        Call<QxmApiResponse> addEditOrDeleteOccupationNetworkCall = apiService.addEditOrDeleteOccupation(
                token.getToken(),
                token.getUserId(),
                occupation.getId(),
                occupation.getDesignation(),
                occupation.getOrganization(),
                occupation.getWorkDuration(),
                deleteRequest);


        addEditOrDeleteOccupationNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void updateInterest(Context context, QxmApiService apiService, QxmToken token, String oldInterest,
                         String newInterest) {

        Call<QxmApiResponse> updateInterestCall = apiService.updateInterest(token.getToken(), token.getUserId(),
                oldInterest, newInterest);

        updateInterestCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, context.getResources().getString(R.string.updated_successfully),
                                Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void addInterest(Context context,QxmApiService apiService,QxmToken token,String interest){

        Call<QxmApiResponse> addInterestCall = apiService.addInterest(token.getToken(), token.getUserId(),
                interest);

        addInterestCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, context.getResources().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void deleteInterest(Context context,QxmApiService apiService,QxmToken token,String interest){

        Call<QxmApiResponse> deleteInterestCall = apiService.deleteInterest(token.getToken(), token.getUserId(),
                interest);

        deleteInterestCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, context.getResources().getString(R.string.deleted_successfully),
                                Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });

    }

    //region editUpdateOrDeleteWebsite
    public static void editUpdateOrDeleteWebsite(Context context, QxmApiService apiService, QxmToken token, String previousWebsite, String newWebsite, String successMessage) {


        Call<QxmApiResponse> editUpdateOrDeleteWebsiteNetworkCall = apiService.editUpdateOrDeleteWebsite(token.getToken(), token.getUserId(), previousWebsite, newWebsite);

        editUpdateOrDeleteWebsiteNetworkCall.enqueue(new Callback<QxmApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());


                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });


    }
    //endregion

    // region updateAddress
    public static void updateAddress(Context context, QxmApiService apiService, QxmToken token, Address address, String successMessage) {


        Call<QxmApiResponse> updateAddressNetworkCall = apiService.updateAddress(token.getToken(), token.getUserId(), address.getUserCountry(), address.getUserCity(), address.getUserAddress(), address.getId());

        updateAddressNetworkCall.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void addUpdateOrDeletePreferableLanguage(Context context, QxmApiService apiService, QxmToken token, List<String> usersPreferableLanguages, String successMessage) {


        Call<QxmApiResponse> addUpdateOrDeletePreferableLanguage  =  apiService.editUpdateOrDeletePreferableLanguage(token.getToken(),token.getUserId(),usersPreferableLanguages);

        addUpdateOrDeletePreferableLanguage.enqueue(new Callback<QxmApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {


                Log.d(TAG, "onResponse body: " + response);
                Log.d(TAG, "onResponse code " + response.code());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();

                    }

                } else
                    Toast.makeText(context, context.getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {


                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.no_internet_found), Toast.LENGTH_SHORT).show();

            }
        });



    }
    //endregion


}
