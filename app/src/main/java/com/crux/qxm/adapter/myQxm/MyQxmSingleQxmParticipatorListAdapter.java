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
import com.crux.qxm.db.models.evaluation.EvaluationDetails;
import com.crux.qxm.db.models.evaluation.Participator;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyQxmSingleQxmParticipatorListAdapter extends RecyclerView.Adapter<MyQxmSingleQxmParticipatorListAdapter.ViewHolder> {


    private static final String TAG = "MyQxmSingleQxmParticipa";

    private ArrayList<Participator> participatorArrayList;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private Context context;
    private EvaluationDetails evaluationDetailhs;
    private QxmApiService apiService;
    private String token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public MyQxmSingleQxmParticipatorListAdapter(ArrayList<Participator> participatorArrayList, Picasso picasso, FragmentActivity fragmentActivity, Context context, QxmApiService apiService, String token) {

        this.participatorArrayList = participatorArrayList;
        this.picasso = picasso;
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.apiService = apiService;
        this.token = token;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    @NonNull
    @Override
    public MyQxmSingleQxmParticipatorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myQxmSingleQxmPendingParticipantsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myqxm_single_qxm_participator_list_item, parent, false);
        return new ViewHolder(myQxmSingleQxmPendingParticipantsView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyQxmSingleQxmParticipatorListAdapter.ViewHolder holder, int position) {

        Participator participator = participatorArrayList.get(position);
        Log.d(TAG, "participator: " + participator);

        if (!TextUtils.isEmpty(participator.getModifiedProfileImage()))
            picasso.load(participator.getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        else
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));
        holder.tvUsername.setText(participator.getFullName());

    }

    @Override
    public int getItemCount() {
        return participatorArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivUserImage;
        AppCompatTextView tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(participatorArrayList.get(getAdapterPosition()).getId(), participatorArrayList.get(getAdapterPosition()).getFullName()));

            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }


    }


}
