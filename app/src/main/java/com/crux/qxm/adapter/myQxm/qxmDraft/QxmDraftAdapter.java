package com.crux.qxm.adapter.myQxm.qxmDraft;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.draft.DraftedQxm;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

public class QxmDraftAdapter extends RecyclerView.Adapter<QxmDraftAdapter.ViewHolder> {
    private static final String TAG = "QxmDraftAdapter";
    private Context context;
    private List<Object> qxmDraftList;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private RealmService realmService;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmDraftListener qxmDraftAdapterListener;

    public QxmDraftAdapter(Context context, List<Object> qxmDraftList, RealmService realmService, Picasso picasso, FragmentActivity fragmentActivity, QxmApiService apiService, QxmToken token, QxmDraftListener qxmDraftAdapterListener) {
        this.context = context;
        this.qxmDraftList = qxmDraftList;
        this.realmService = realmService;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        this.qxmDraftAdapterListener = qxmDraftAdapterListener;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View qBankSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_single_qxm_draft_recyclerview_item, parent, false);
        return new ViewHolder(qBankSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (qxmDraftList.get(position) instanceof QxmDraft) {
            //region Object from Realm DB
            QxmDraft qxm = (QxmDraft) qxmDraftList.get(position);
            Log.d(TAG, "onBindViewHolder: position: " + position);
            Log.d(TAG, "onBindViewHolder: Qxm = " + qxm.toString());

            if (!TextUtils.isEmpty(qxm.getQuestionSetThumbnail())) {
                picasso.load(qxm.getQuestionSetThumbnail()).error(context.getResources().getDrawable(R.drawable.ic_qxm_big)).into(holder.ivQxmThumbnail);
            }

            holder.tvQxmTitle.setText(qxm.getQuestionSetTitle());

            //setting category
            if (!TextUtils.isEmpty(qxm.getQuestionCategories()))
                holder.tvQxmCategory.setText(qxm.getQuestionCategories());
            else
                holder.tvQxmCategory.setVisibility(View.GONE);

            holder.tvQxmDescription.setText(qxm.getQuestionSetDescription());
            SetlinkClickable.setLinkclickEvent(holder.tvQxmDescription, new HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                    intent.putExtra(YOUTUBE_LINK_KEY, url);
                    context.startActivity(intent);
                }
            });

            holder.ivQxmStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cellphone_android));

            if (!TextUtils.isEmpty(qxm.getLastEditedAt())) {

                holder.tvQxmLastEditTime.setText(TimeFormatString.getTimeAndDate(
                        Long.parseLong(qxm.getLastEditedAt())
                ));
            } else {
                holder.tvQxmLastEditTime.setVisibility(View.GONE);
            }

        /*if (qxm.getQuestionStatus().equals(QUESTION_STATUS_PUBLISHED)) {
            Bundle args = new Bundle();
            args.putString(FEED_DATA_PASS_TO_MY_QXM_DETAILS, qxm.getId());

            holder.ivQxmThumbnail.setOnClickListener(v -> qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args));
            holder.tvQxmTitle.setOnClickListener(v -> qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args));
            holder.tvQxmDescription.setOnClickListener(v -> qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args));

        } else if (qxm.getQuestionStatus().equals(QUESTION_STATUS_DRAFTED)) {
            holder.ivQxmThumbnail.setOnClickListener(v -> {
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
            });
            holder.tvQxmTitle.setOnClickListener(v -> {
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
            });
            holder.tvQxmDescription.setOnClickListener(v -> {
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
            });
        }*/

            // holder.ivQxmThumbnail.setOnClickListener(v -> qxmDraftAdapterListener.onItemSelected(qxm));
            // holder.tvQxmTitle.setOnClickListener(v -> qxmDraftAdapterListener.onItemSelected(qxm));
            // holder.tvQxmDescription.setOnClickListener(v -> qxmDraftAdapterListener.onItemSelected(qxm));

            holder.ivQBankOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_qxm_drafts_list, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {

                    switch (item.getItemId()) {

                        case R.id.action_delete:

                            realmService.deleteDraftedQxm(qxm);
                            qxmDraftList.remove(qxm);

                            notifyItemRemoved(holder.getAdapterPosition());

                            notifyItemRangeChanged(holder.getAdapterPosition(), qxmDraftList.size() - 1);

                            if (qxmDraftList.size() == 0)
                                qxmDraftAdapterListener.onQxmDraftListEmptyListener();

                            break;
                    }


                    return false;
                });
                popup.show();
            });
            //endregion
        } else if (qxmDraftList.get(position) instanceof DraftedQxm) {
            //region Object from Server Response
            DraftedQxm qxm = (DraftedQxm) qxmDraftList.get(position);

            Log.d(TAG, "onBindViewHolder: position: " + position);
            Log.d(TAG, "onBindViewHolder: DraftedQxm = " + qxm.toString());


            holder.tvQxmTitle.setText(qxm.getFeedTitle());

            //setting category

            holder.tvQxmCategory.setVisibility(View.GONE);

            // Description
            holder.tvQxmDescription.setText(qxm.getFeedDescription());

            // Last Edited At
            holder.tvQxmLastEditTime.setText(TimeFormatString.getTimeAndDate(
                    Long.parseLong(qxm.getFeedCreationTime())
            ));

            // holder.ivQxmThumbnail.setOnClickListener(v -> getDraftedQxmNetworkCall(qxm.getFeedQxmId()));
            // holder.tvQxmTitle.setOnClickListener(v -> getDraftedQxmNetworkCall(qxm.getFeedQxmId()));
            // holder.tvQxmDescription.setOnClickListener(v -> getDraftedQxmNetworkCall(qxm.getFeedQxmId()));

            holder.ivQxmStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloud_check));

            holder.ivQBankOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.END);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_qxm_drafts_list, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {

                    switch (item.getItemId()) {

                        case R.id.action_delete:

                            deleteDraftedQxmNetworkCall(qxm.getFeedQxmId());
                            qxmDraftList.remove(qxm);
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), qxmDraftList.size() - 1);

                            if (qxmDraftList.size() == 0)
                                qxmDraftAdapterListener.onQxmDraftListEmptyListener();

                            break;
                    }


                    return false;
                });
                popup.show();
            });
            //endregion
        }
    }

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + qxmDraftList.size());
        return qxmDraftList.size();
    }
    //endregion

    //region Override-clear
    public void clear() {
        if (qxmDraftList.size() != 0)
            qxmDraftList.clear();
        notifyDataSetChanged();
    }
    //endregion

    //region Override-addAll
    public void addAll(List<QxmDraft> qxmDrafts) {
        if (qxmDrafts.size() > 0) qxmDraftList.addAll(qxmDrafts);
        //qxmDraftList.addAll(qxmDrafts);

        /*if(qxmDrafts.size()!=0){
            Realm realm = Realm.getDefaultInstance();
            qxmDraftList.addAll(realm.copyToRealmOrUpdate(realm.copyFromRealm(qxmDrafts, 2))); // maybe 3?
        }*/
    }
    //endregion

    private void getDraftedQxmNetworkCall(String draftedQxmId) {
        QxmProgressDialog progressDialog = new QxmProgressDialog(context);
        progressDialog.showProgressDialog(context.getString(R.string.progress_dialog_title_qxm_draft), context.getString(R.string.progress_dialog_message_qxm_draft), false);
        Call<QxmModel> editQxm = apiService.editQxm(token.getToken(), token.getUserId(), draftedQxmId);
        editQxm.enqueue(new Callback<QxmModel>() {
            @Override
            public void onResponse(@NonNull Call<QxmModel> call, @NonNull Response<QxmModel> response) {
                Log.d(TAG, "onResponse: getDraftedQxmNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                progressDialog.closeProgressDialog();

                if (response.code() == 200) {

                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: success");
                        qxmDraftAdapterListener.onItemSelected(response.body());
                    } else {
                        Log.d(TAG, "response body is null");
                        Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: getDraftedQxmNetworkCall");
                Log.d(TAG, "onFailure: stackTrace: " + Arrays.toString(t.getStackTrace()));
                progressDialog.closeProgressDialog();
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //region deleteDraftedQxmNetworkCall
    private void deleteDraftedQxmNetworkCall(String draftedQxmId) {
        Log.d(TAG, "deleteDraftedQxmNetworkCall: draftedQxmId: " + draftedQxmId);

        Call<QxmApiResponse> deleteDraftedQxm = apiService.deleteDraftedQxm(token.getToken(), draftedQxmId);

        deleteDraftedQxm.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: deleteDraftedQxmNetworkCall");
                Log.d(TAG, "onResponse: response.code = " + response.code());
                Log.d(TAG, "onResponse: response.body = " + response.body());

                if (response.code() == 201) {
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, context.getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: deleteDraftedQxmNetworkCall");
                Log.d(TAG, "onFailure: stackTrace: " + Arrays.toString(t.getStackTrace()));

                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();

            }
        });
    }
    //endregion

    //region Interface-QxmDraftListener
    public interface QxmDraftListener {
        void onItemSelected(QxmDraft qxmDraft);

        void onItemSelected(QxmModel qxmModel);

        void onQxmDraftListEmptyListener();
    }
    //endregion

    //region Class-ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivQxmThumbnail;
        AppCompatTextView tvQxmTitle;
        AppCompatTextView tvQxmCategory;
        AppCompatTextView tvQxmDescription;
        AppCompatImageView ivQxmStatus;
        AppCompatTextView tvQxmStatus;
        AppCompatTextView tvQxmLastEditTime;
        AppCompatImageView ivQBankOptions;


        public ViewHolder(View itemView) {
            super(itemView);
            ivQxmThumbnail = itemView.findViewById(R.id.ivQxmThumbnail);
            tvQxmTitle = itemView.findViewById(R.id.tvQxmTitle);
            tvQxmCategory = itemView.findViewById(R.id.tvQxmCategory);
            tvQxmDescription = itemView.findViewById(R.id.tvQxmDescription);
            ivQxmStatus = itemView.findViewById(R.id.ivQxmStatus);
            tvQxmStatus = itemView.findViewById(R.id.tvQxmStatus);
            tvQxmLastEditTime = itemView.findViewById(R.id.tvQxmLastEditTime);
            ivQBankOptions = itemView.findViewById(R.id.ivQBankOptions);

            itemView.setOnClickListener(v -> {
                if (qxmDraftList.get(getAdapterPosition()) instanceof QxmDraft) {
                    QxmDraft qxm = (QxmDraft) qxmDraftList.get(getAdapterPosition());
                    Log.d(TAG, "ViewHolder: instanceOf QxmDraft (Realm DB)");
                    qxmDraftAdapterListener.onItemSelected(qxm);
                } else {
                    Log.d(TAG, "ViewHolder: instanceOf drafted QXM (Server)");
                    DraftedQxm draftedQxm = (DraftedQxm) qxmDraftList.get(getAdapterPosition());
                    getDraftedQxmNetworkCall(draftedQxm.getFeedQxmId());

                }
            });
        }
    }
    //endregion
}
