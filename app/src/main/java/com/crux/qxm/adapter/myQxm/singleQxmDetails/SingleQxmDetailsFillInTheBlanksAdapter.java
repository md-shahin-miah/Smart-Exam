package com.crux.qxm.adapter.myQxm.singleQxmDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;

import java.util.ArrayList;

public class SingleQxmDetailsFillInTheBlanksAdapter extends RecyclerView.Adapter<SingleQxmDetailsFillInTheBlanksAdapter.ViewHolder> {

    private static final String TAG = "SingleQxmDetailsFillInT";
    private Context context;
    private ArrayList<String> answers;

    private String answerSerialArray[] = {

            "(a)",
            "(b)",
            "(c)",
            "(d)",
            "(e)",
            "(f)",
            "(g)",
            "(h)",
            "(i)",
            "(j)"
    };

    SingleQxmDetailsFillInTheBlanksAdapter(Context context, ArrayList<String> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public SingleQxmDetailsFillInTheBlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fillInTheBlanksView = LayoutInflater.from(context).inflate(R.layout.rec_my_qxm_single_qxm_details_single_fill_in_the_blank_option, parent, false);
        return new SingleQxmDetailsFillInTheBlanksAdapter.ViewHolder(fillInTheBlanksView);
    }

    //region Override-onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull SingleQxmDetailsFillInTheBlanksAdapter.ViewHolder holder, int position) {
        String answerSerial = answerSerialArray[position];
        holder.tvFillInTheBlanksOptionSerial.setText(answerSerial);
        holder.tvFillInTheBlanksAnswer.setText(answers.get(position));
    }
    //endregion

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: answers: " + answers.size());
        return answers.size();
    }
    //endregion

    //region Class-ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvFillInTheBlanksOptionSerial;
        AppCompatTextView tvFillInTheBlanksAnswer;

        ViewHolder(View itemView) {
            super(itemView);

            tvFillInTheBlanksOptionSerial = itemView.findViewById(R.id.tvFillInTheBlanksOptionSerial);
            tvFillInTheBlanksAnswer = itemView.findViewById(R.id.tvFillInTheBlanksAnswer);
        }

    }
    //endregion
}
