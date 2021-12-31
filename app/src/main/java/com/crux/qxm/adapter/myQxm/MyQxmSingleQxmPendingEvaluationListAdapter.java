package com.crux.qxm.adapter.myQxm;

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
import com.crux.qxm.db.models.myQxm.ParticipatorContainer;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyQxmSingleQxmPendingEvaluationListAdapter extends RecyclerView.Adapter<MyQxmSingleQxmPendingEvaluationListAdapter.ViewHolder> {


    private static final String TAG = "MyQxmSingleQxmPendingEv";

    private ArrayList<ParticipatorContainer> participatorContainerArrayList;
    private Picasso picasso;
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public MyQxmSingleQxmPendingEvaluationListAdapter(ArrayList<ParticipatorContainer> participatorContainerArrayList, Picasso picasso, FragmentActivity fragmentActivity, Context context) {

        this.participatorContainerArrayList = participatorContainerArrayList;
        this.picasso = picasso;
        this.context = context;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public MyQxmSingleQxmPendingEvaluationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myQxmSingleQxmPendingParticipantsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_single_qxm_pending_participants_list_item, parent, false);
        return new ViewHolder(myQxmSingleQxmPendingParticipantsView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyQxmSingleQxmPendingEvaluationListAdapter.ViewHolder holder, int position) {

        ParticipatorContainer participatorContainer = participatorContainerArrayList.get(position);
        Log.d(TAG, "participatorContainer: " + participatorContainer);

        if (!TextUtils.isEmpty(participatorContainer.getParticipator().getModifiedProfileImage()))
            picasso.load(participatorContainer.getParticipator().getModifiedProfileImage()).into(holder.ivUserImage);
        holder.tvUsername.setText(participatorContainer.getParticipator().getFullName());


    }

    @Override
    public int getItemCount() {
        return participatorContainerArrayList.size();
    }

    // region loadEvaluateQxmFragment
    private void loadEvaluateQxmFragment(String participationId, String participatorName) {

        qxmFragmentTransactionHelper.loadEvaluateQxmFragment(participationId, participatorName);

    }

    // endregion

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivUserImage;
        AppCompatTextView tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);

            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);

            itemView.setOnClickListener(v -> {
                ParticipatorContainer container = participatorContainerArrayList.get(getAdapterPosition());
                String participationId = container.getParticipationId();
                String participatorName = container.getParticipator().getFullName();
                qxmFragmentTransactionHelper.loadEvaluateQxmFragment(participationId, participatorName);
            });
        }
    }

}
