package com.crux.qxm.adapter.poll;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {


    private static final String TAG = "PollAdapter";
    private Context context;
    private ArrayList<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptionList;
    private OnPollOptionDeletedListener onPollOptionDeletedListener;
    private boolean pollEdit;


    public PollAdapter(Context context, ArrayList<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptionList, OnPollOptionDeletedListener onPollOptionDeletedListener) {

        this.context = context;
        this.pollOptionList = pollOptionList;
        this.onPollOptionDeletedListener = onPollOptionDeletedListener;

    }

    public PollAdapter(Context context, ArrayList<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptionList, OnPollOptionDeletedListener onPollOptionDeletedListener, boolean pollEdit) {

        this.context = context;
        this.pollOptionList = pollOptionList;
        this.onPollOptionDeletedListener = onPollOptionDeletedListener;
        this.pollEdit = pollEdit;

    }


    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View pollAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_single_option_item, parent, false);
        return new PollViewHolder(pollAdapterView);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {


        com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem pollOptionsItem = pollOptionList.get(position);

        if(pollEdit) {
            holder.etPollOption.setEnabled(false);
            holder.etPollOption.setAlpha(0.4f);
            holder.ivDeletePollOption.setVisibility(View.GONE);
        }
        else{
            holder.etPollOption.setEnabled(true);
            holder.etPollOption.setAlpha(1.0f);
            holder.ivDeletePollOption.setVisibility(View.VISIBLE);
        }


        if (pollOptionsItem.getTitle().trim().isEmpty()) {

            holder.etPollOption.setHint(context.getResources().getString(R.string.poll_option) + " " + String.valueOf(holder.getAdapterPosition() + 1));
        }
        else {
            holder.etPollOption.setText(pollOptionsItem.getTitle());
        }

        holder.etPollOption.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                // adding answer in same arrayList position using set
                pollOptionsItem.setTitle(editable.toString());
                pollOptionList.set(holder.getAdapterPosition(), pollOptionsItem);
                Log.d(TAG, "afterTextChanged pollOptionList: " + pollOptionList);
            }

        });


        // delete a single poll option
        holder.ivDeletePollOption.setOnClickListener(v -> {

//            if (pollOptionList.size() > 2) {
//
//                Log.d(TAG, "onBindViewHolder pollOptionList : " + pollOptionList);
//                Log.d(TAG, "onBindViewHolder item position :"+ String.valueOf(holder.getAdapterPosition()));
//                holder.ivDeletePollOption.setVisibility(View.VISIBLE);
//
//            }

            holder.etPollOption.setText("");
            pollOptionList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());

            onPollOptionDeletedListener.onPollOptionDeleted();

        });


    }

    @Override
    public int getItemCount() {
        return pollOptionList.size();
    }


    // region interface OnPollOptionDeletedListener
    public interface OnPollOptionDeletedListener {
        void onPollOptionDeleted();
    }
    //endregion

    //region viewHolder
    class PollViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etPollOption)
        AppCompatEditText etPollOption;
        @BindView(R.id.ivDeletePollOption)
        AppCompatImageView ivDeletePollOption;

        PollViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //endregion
}
