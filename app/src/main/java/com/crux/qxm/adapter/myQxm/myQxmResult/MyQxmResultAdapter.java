package com.crux.qxm.adapter.myQxm.myQxmResult;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.myQxmResult.MyQxmResult;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.TimeFormatString;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;

public class MyQxmResultAdapter extends RecyclerView.Adapter<MyQxmResultAdapter.ViewHolder> {
    // Todo:: MyPerformance: 90%  <- This value is missing from api response. Fix it and add it to View
    // region Fields-MyQxmResultAdapter
    private static final String TAG = "MyQxmResultAdapter";
    private final int NORMAL_FEED = 1;
    private Context context;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private MyQxmResultAdapterListener myQxmResultAdapterListener;
    private List<MyQxmResult> items;

    // endregion

    // region Constructor-MyQxmResultAdapter

    public MyQxmResultAdapter(Context context, Picasso picasso, FragmentActivity fragmentActivity, List<MyQxmResult> items, MyQxmResultAdapterListener myQxmResultAdapterListener) {
        this.context = context;
        this.picasso = picasso;
        this.items = items;
        this.myQxmResultAdapterListener = myQxmResultAdapterListener;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-OnCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NORMAL_FEED) {
            View performanceSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_performance_single_recyclerview_item, parent, false);
            return new ViewHolder(performanceSingleItemView);
        } else {
            View performanceSingleItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_performance_single_recyclerview_item_for_long_name, parent, false);
            return new ViewHolder(performanceSingleItemView);
        }
    }

    // endregion

    // region Override-OnBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: RsultItem = " + items.get(position).toString());

        MyQxmResult qxmResult = items.get(position);
        Log.d(TAG, "onBindViewHolder: qxmResult: " + qxmResult);

        // region SetDataIntoViews

        if (!TextUtils.isEmpty(qxmResult.getModifiedQxmCreatorProfileImage())) {
            picasso.load(qxmResult.getModifiedQxmCreatorProfileImage()).error(context.getResources().getDrawable(
                    R.drawable.ic_user_default)).into(holder.ivQxmCreatorImage
            );
        } else {
            holder.ivQxmCreatorImage.setBackgroundDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        }

        holder.tvQxmTitle.setText(qxmResult.getQxmName());

        holder.tvQxmCreatorName.setText(qxmResult.getQxmCreatorfullName());


        if (QxmStringIntegerChecker.isLongInt(qxmResult.getQxmCreationTime())) {

            holder.tvQxmCreationTime.setText(TimeFormatString.getTimeAgo(Long.parseLong(
                    qxmResult.getQxmCreationTime()
            )));
        }

        if (qxmResult.getQxmPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
            holder.ivQxmPrivacy.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
        } else {
            holder.ivQxmPrivacy.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));
        }

        if (qxmResult.getFeedViewType().equals(StaticValues.FEED_VIEW_TYPE_QXM)) {
            holder.tvPerformance.setText(String.format("My Performance: %s",
                    qxmResult.getUserPerformance().getPerformance().getPercentage()));
            holder.tvPosition.setText(qxmResult.getUserPerformance().getPerformance().getPercentage());
        } else if (qxmResult.getFeedViewType().equals(StaticValues.FEED_VIEW_TYPE_SINGLE_MCQ)) {
            holder.tvPerformance.setVisibility(View.GONE);
        }

        if (qxmResult.getQxmCategory() != null) {

            StringBuilder sbCategory;
            sbCategory = new StringBuilder();
            sbCategory.append("Category: ");

            for (int i = 0; i < qxmResult.getQxmCategory().size(); i++) {
                sbCategory.append(qxmResult.getQxmCategory().get(i));

                if (i != qxmResult.getQxmCategory().size() - 1)
                    sbCategory.append(", ");
            }

            holder.tvQxmCategory.setText(sbCategory.toString());
        } else {
            holder.tvQxmCategory.setText(context.getResources().getString(R.string.category));
        }

        if (qxmResult.getUserPerformance() != null) {
            holder.tvPosition.setText(String.format("My Position: %s", qxmResult.getUserPerformance().getMyPosition()));
        } else {
            holder.tvPosition.setVisibility(View.GONE);
        }

        // endregion

        // region ClickListeners
        holder.ivQxmCreatorImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        qxmResult.getQxmCreatorId(), qxmResult.getQxmCreatorfullName()
                )
        );


        // endregion

    }

    // endregion

    // region Override-GetItemCount

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + items.size());
        return items.size();
    }

    // endregion

    //region Override-getItemViewType
    @Override
    public int getItemViewType(int position) {

        int NORMAL_FEED_WITH_LONG_NAME = 2;

        if (items.get(position).getQxmCreatorfullName().length() < 22) return NORMAL_FEED;
        else return NORMAL_FEED_WITH_LONG_NAME;

    }
    //endregion

    // region FollowingViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivQxmCreatorImage)
        AppCompatImageView ivQxmCreatorImage;
        @BindView(R.id.tvQxmTitle)
        AppCompatTextView tvQxmTitle;
        @BindView(R.id.tvQxmCreatorName)
        AppCompatTextView tvQxmCreatorName;
        @BindView(R.id.tvQxmCreationTime)
        AppCompatTextView tvQxmCreationTime;
        @BindView(R.id.ivQxmPrivacy)
        AppCompatImageView ivQxmPrivacy;
        @BindView(R.id.tvPerformance)
        AppCompatTextView tvPerformance;
        @BindView(R.id.tvQxmCategory)
        AppCompatTextView tvQxmCategory;
        @BindView(R.id.tvPosition)
        AppCompatTextView tvPosition;
        @BindView(R.id.ivPerformanceOptions)
        AppCompatImageView ivPerformanceOptions;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> myQxmResultAdapterListener.onItemSelected(items.get(getAdapterPosition())));

        }
    }

    // endregion

    // region interface-MyQxmResultAdapterListener

    public interface MyQxmResultAdapterListener {
        void onItemSelected(MyQxmResult qxmResult);
    }

    //endregion

    //region clear

    public void clear() {

        // Clean all elements of the recycler
        items.clear();
        notifyDataSetChanged();
    }

    // endregion
}
