package com.crux.qxm.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.feedMenu.MyListAdapterInMenu;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.myQxm.list.List;
import com.crux.qxm.networkLayer.QxmApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QxmAddQxmToList {

    private static final String TAG = "QxmAddQxmToList";
    private FragmentActivity fragmentActivity;
    private Context context;
    private QxmApiService apiService;
    private String userId;
    private String token;

    public QxmAddQxmToList(FragmentActivity fragmentActivity, Context context, QxmApiService apiService, String userId, String token) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
    }

    public void addQxmToList(String qxmId, String qxmQuestionSetTitle) {
        //region Add Qxm to List

        // creating custom dialog for showing my QxmList
        AppCompatDialog dialogAddToList;
        AlertDialog.Builder builderAddToList = new AlertDialog.Builder(fragmentActivity);
        LayoutInflater listInflater = LayoutInflater.from(fragmentActivity);
        View showListListView = listInflater.inflate(R.layout.feed_menu_add_to_my_list, null);

        ProgressBar listLoadingProgressingBar = showListListView.findViewById(R.id.progressBar);
        listLoadingProgressingBar.setVisibility(View.VISIBLE);

        AppCompatTextView tvNotFoundList = showListListView.findViewById(R.id.tvNotFound);
        RecyclerView rvMyList = showListListView.findViewById(R.id.rvMyList);
        builderAddToList.setView(showListListView);

        builderAddToList.setPositiveButton("CREATE NEW LIST", (dialogInterface, i) -> {
            QxmCreateList createList = new QxmCreateList(context, apiService, userId, token);
            createList.createListDialogShow(fragmentActivity, qxmId, qxmQuestionSetTitle);
        });
        builderAddToList.setNegativeButton("CANCEL ADDING", (dialogInterface, i) -> dialogInterface.dismiss());

        dialogAddToList = builderAddToList.create();
        dialogAddToList.show();


        //network call for fetching my group list and setting in recycler view
        Call<java.util.List<List>> getMyListInMenu = apiService.getAllList(token, userId);

        getMyListInMenu.enqueue(new Callback<java.util.List<List>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<List>> call, @NonNull Response<java.util.List<List>> response) {

                listLoadingProgressingBar.setVisibility(View.GONE);

                Log.d(TAG, "StatusCode getMyListInMenu : " + response.code());
                Log.d(TAG, "response body getMyListInMenu : " + response.body());

                if (response.code() == 200) {

                    java.util.List<List> myListInMenu = response.body();
                    rvMyList.setItemAnimator(new DefaultItemAnimator());
                    rvMyList.setItemAnimator(new DefaultItemAnimator());
                    rvMyList.addItemDecoration(new DividerItemDecoration(showListListView.getContext(), DividerItemDecoration.VERTICAL));
                    rvMyList.setNestedScrollingEnabled(false);
                    rvMyList.setLayoutManager(new LinearLayoutManager(showListListView.getContext()));

                    // adding qxm to my list using interface call (last parameter of the below adapter is
                    // the interface call of list item selection

                    MyListAdapterInMenu myListAdapterInMenu = new MyListAdapterInMenu(dialogAddToList, showListListView.getContext(), myListInMenu, list -> {

                        //QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);
                        //qxmProgressDialog.showProgressDialog("Adding to list", String.format("%s is being added to list. Please wait... ", qxmQuestionSetTitle), false);

                        Log.d(TAG, "onResponseListSelected UserID :" + userId);
                        Log.d(TAG, "onResponseListSelected QxmID :" + qxmId);
                        Log.d(TAG, "onResponseListSelected GroupID :" + list.getId());


                        // sharing qxm to group network call
                        Call<QxmApiResponse> addQxmToMyList = apiService.addQxmToList(token, list.getId(), qxmId);

                        addQxmToMyList.enqueue(new Callback<QxmApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                                Log.d(TAG, "onResponse addQxmToMyList:  " + response);

                                if (response.code() == 201) {

                                    //qxmProgressDialog.closeProgressDialog();
                                    //dialogAddToList.hide();
                                    Toast.makeText(showListListView.getContext(), R.string.added_successfully, Toast.LENGTH_SHORT).show();

                                } else {

                                    Log.d(TAG, "onResponse addQxmToMyList: " + response.code());
                                    //qxmProgressDialog.closeProgressDialog();
                                    Toast.makeText(showListListView.getContext(), "Something went wrong during adding to list!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                                Log.d(TAG, "onFailure addQxmToMyList: " + t.getMessage());
                                //qxmProgressDialog.closeProgressDialog();
                                Toast.makeText(showListListView.getContext(), "Network error,connect with internet!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });

                    rvMyList.setAdapter(myListAdapterInMenu);

                    assert myListInMenu != null;
                    if (myListInMenu.isEmpty()) {
                        rvMyList.setVisibility(View.GONE);
                        tvNotFoundList.setVisibility(View.VISIBLE);
                        tvNotFoundList.setText(showListListView.getContext().getString(R.string.you_dont_have_any_list_yet));
                    }
                } else {

                    Toast.makeText(context, "Something went wrong while loading list, please try again! ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<List>> call, @NonNull Throwable t) {

                listLoadingProgressingBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure getMyListInMenu" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }
        });

        //endregion
    }
}
