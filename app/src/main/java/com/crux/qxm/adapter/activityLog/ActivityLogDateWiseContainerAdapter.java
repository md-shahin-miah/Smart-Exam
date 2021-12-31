package com.crux.qxm.adapter.activityLog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.activityLog.ActivityLogDateContainer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLogDateWiseContainerAdapter extends RecyclerView.Adapter<ActivityLogDateWiseContainerAdapter.ViewHolder> {

    // region Adapter-Properties

    private static final String TAG = "ActivityLogContainerAda";
    private Context context;
    private List<ActivityLogDateContainer> activityLogContainerList;
    private Picasso picasso;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Adapter-Constructor

    public ActivityLogDateWiseContainerAdapter(Context context, QxmFragmentTransactionHelper qxmFragmentTransactionHelper, List<ActivityLogDateContainer> activityLogList, Picasso picasso, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.activityLogContainerList = activityLogList;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
    }

    // endregion

    // region Override-OnCreateViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View activityLogContainerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_activity_log_single_item_container, parent, false);
        return new ActivityLogDateWiseContainerAdapter.ViewHolder(activityLogContainerView);
    }

    // endregion

    // region Override-OnBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvLogDate.setText(activityLogContainerList.get(position).getDate());

        ActivityLogAdapter activityLogAdapter = new ActivityLogAdapter(context, qxmFragmentTransactionHelper, activityLogContainerList.get(position).getActivitiesItemList(), picasso, apiService, token);
        holder.rvActivityLogContainer.setAdapter(activityLogAdapter);
        holder.rvActivityLogContainer.setLayoutManager(new LinearLayoutManager(context));
        //holder.rvActivityLogContainer.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        holder.rvActivityLogContainer.setNestedScrollingEnabled(false);

    }

    // endregion

    // region Override-GetItemCount

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + activityLogContainerList.size());
        return activityLogContainerList.size();
    }

    // endregion

    @Override
    public int getItemViewType(int position) {
        if (activityLogContainerList.get(position).getActivitiesItemList().size() == 0) return  0;
        return super.getItemViewType(position);
    }


    // region Clear

    public void clear() {
        activityLogContainerList.clear();
        notifyDataSetChanged();
    }

    // endregion

    // region FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvLogDate)
        AppCompatTextView tvLogDate;
        @BindView(R.id.rvActivityLogContainer)
        RecyclerView rvActivityLogContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    // endregion
}
