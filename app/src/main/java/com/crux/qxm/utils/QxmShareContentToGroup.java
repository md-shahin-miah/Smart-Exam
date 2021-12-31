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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.feedMenu.MyGroupAdapterInMenu;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.menu.group.MyGroupDataInMenu;
import com.crux.qxm.db.models.menu.group.MyGroupDataInMenuContainer;
import com.crux.qxm.networkLayer.QxmApiService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.SHARED_CATEGORY_QXM;

public class QxmShareContentToGroup {
    private static final String TAG = "QxmShareContentToGroup";
    private FragmentActivity fragmentActivity;
    private QxmApiService apiService;
    private Context context;
    private Picasso picasso;
    private String token;
    private String userId;
    public boolean sharedSuccess = false;


    public QxmShareContentToGroup(FragmentActivity fragmentActivity, QxmApiService apiService, Context context, Picasso picasso, String userId, String token) {
        this.fragmentActivity = fragmentActivity;
        this.apiService = apiService;
        this.context = context;
        this.picasso = picasso;
        this.token = token;
        this.userId = userId;
    }

    public void shareToGroup(String contentId, String sharedCategory) {
        //region share qxm to group

        // creating custom dialog for showing my group list
        AppCompatDialog dialogShareToGroup;
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        LayoutInflater groupInflater = LayoutInflater.from(fragmentActivity);
        View showGroupListView = groupInflater.inflate(R.layout.feed_menu_my_group_list_for_share, null);
        ProgressBar progressBar = showGroupListView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        AppCompatTextView tvDialogTitle = showGroupListView.findViewById(R.id.tvDialogTitle);
        AppCompatTextView tvNotFound = showGroupListView.findViewById(R.id.tvNotFound);
        RecyclerView rvMyGroupList = showGroupListView.findViewById(R.id.rvMyGroupList);
        builder.setView(showGroupListView);

        tvDialogTitle.setText(String.format("Share %s", sharedCategory));

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.setPositiveButton("OTHER SHARE OPTIONS", (dialogInterface, i) -> {

            QxmActionShareLink qxmActionShareLink = new QxmActionShareLink(fragmentActivity, context);
            QxmUrlHelper urlHelper = new QxmUrlHelper();
            if(sharedCategory.equals(SHARED_CATEGORY_QXM))
                qxmActionShareLink.shareQxmLink(urlHelper.getQxmUrl(contentId));
            else
                qxmActionShareLink.sharePollLink(urlHelper.getPollUrl(contentId));

        });

        dialogShareToGroup = builder.create();
        dialogShareToGroup.show();


        // region network call for fetching my group list and setting in recycler view
        Call<MyGroupDataInMenuContainer> getMyGroupDataInMenu = apiService.getMyGroupListInMenu(token, userId);

        getMyGroupDataInMenu.enqueue(new Callback<MyGroupDataInMenuContainer>() {
            @Override
            public void onResponse(@NonNull Call<MyGroupDataInMenuContainer> call, @NonNull Response<MyGroupDataInMenuContainer> response) {

                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "StatusCode getMyGroupDataInMenu : " + response.code());
                Log.d(TAG, "response body getMyGroupDataInMenu : " + response.body());

                if (response.code() == 200) {

                    List<MyGroupDataInMenu> myGroupDataInMenu = Objects.requireNonNull(response.body()).getMyGroupDataInMenuList();
                    rvMyGroupList.setItemAnimator(new DefaultItemAnimator());
                    rvMyGroupList.setLayoutManager(new LinearLayoutManager(showGroupListView.getContext()));

                    // sharing qxm to my group using interface call (last parameter of the below adapter is
                    // the interface call of group item selection

                    MyGroupAdapterInMenu myGroupAdapterInMenu = new MyGroupAdapterInMenu(dialogShareToGroup, showGroupListView.getContext(), myGroupDataInMenu, picasso, mySelectedGroupDataInMenu -> {


                        //QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);
                        //qxmProgressDialog.showProgressDialog("Sharing Qxm", String.format("%s is being shared. Please wait... ", qxmQuestionSetTitle), false);

                        Log.d(TAG, "onResponseGroupSelected UserID :" + userId);
                        Log.d(TAG, "onResponseGroupSelected QxmID :" + contentId);
                        Log.d(TAG, "onResponseGroupSelected GroupID :" + mySelectedGroupDataInMenu.getGroup().getId());


                        // sharing qxm to group network call
                        Call<QxmApiResponse> shareQxmToMyGroup = apiService.shareQxmToGroup(token, contentId, userId, mySelectedGroupDataInMenu.getGroup().getId());

                        shareQxmToMyGroup.enqueue(new Callback<QxmApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                                Log.d(TAG, "onResponse shareQxmToMyGroup:  " + response);

                                if (response.code() == 201) {

                                    //qxmProgressDialog.closeProgressDialog();
                                    //dialogShareToGroup.dismiss();
                                    sharedSuccess = true;
                                    Toast.makeText(showGroupListView.getContext(), "Qxm is shared successfully", Toast.LENGTH_SHORT).show();
                                } else {

                                    Log.d(TAG, "onResponse shareQxmToMyGroup: " + response.code());
                                    //qxmProgressDialog.closeProgressDialog();
                                    Toast.makeText(showGroupListView.getContext(), "Something went wrong during sharing the qxm!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                                Log.d(TAG, "onFailure shareQxmToMyGroup: " + t.getMessage());
                                //qxmProgressDialog.closeProgressDialog();
                                Toast.makeText(showGroupListView.getContext(), "Network error,connect with internet!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });
                    rvMyGroupList.setAdapter(myGroupAdapterInMenu);

                    if (myGroupDataInMenu.isEmpty()) {
                        rvMyGroupList.setVisibility(View.GONE);
                        tvNotFound.setVisibility(View.VISIBLE);
                        tvNotFound.setText(showGroupListView.getContext().getString(R.string.you_dont_have_any_group_yet));
                    }
                } else {

                    Toast.makeText(context, "Something went wrong while loading group, please try again !", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "getGroupListInMenu UserId :" + userId);
            }

            @Override
            public void onFailure(@NonNull Call<MyGroupDataInMenuContainer> call, @NonNull Throwable t) {

                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure getMyGroupDataInMenu" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
        // endregion

        //endregion
    }
}
