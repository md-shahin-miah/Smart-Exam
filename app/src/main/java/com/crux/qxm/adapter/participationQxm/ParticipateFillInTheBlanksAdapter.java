package com.crux.qxm.adapter.participationQxm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;

import java.util.ArrayList;

import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;

public class ParticipateFillInTheBlanksAdapter extends RecyclerView.Adapter<ParticipateFillInTheBlanksAdapter.ViewHolder> {

    private static final String TAG = "FillInTheBlanksAdapter";

    private Context context;
    private ArrayList<String> answers;
    private ArrayList<String> participantsAnswers;
    private String viewFor;

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

    public ParticipateFillInTheBlanksAdapter(Context context, ArrayList<String> answers, ArrayList<String> participantsAnswers, String viewFor) {
        this.context = context;
        this.answers = answers;
        this.participantsAnswers = participantsAnswers;
        this.viewFor = viewFor;
    }

    @NonNull
    @Override
    public ParticipateFillInTheBlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View fillInTheBlanksView = LayoutInflater.from(context).inflate(R.layout.create_qxm_single_fill_in_the_blank_option,parent,false);
        return new ParticipateFillInTheBlanksAdapter.ViewHolder(fillInTheBlanksView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipateFillInTheBlanksAdapter.ViewHolder holder, int position) {

        if(viewFor.equals(VIEW_FOR_SEE_QUESTION)){
            holder.etFillInTheBlanksAnswer.setEnabled(false);
        }

        String answerSerial = answerSerialArray[position];
        holder.tvFillInTheBlanksOptionSerial.setText(answerSerial);
        String dummyAnswer ="answer to the question";
        holder.etFillInTheBlanksAnswer.setHint(dummyAnswer);

        //region fill in the blank answer updating
        holder.etFillInTheBlanksAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d(TAG, "beforeTextChanged: "+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d(TAG, "onTextChanged: "+charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: "+editable);
                participantsAnswers.set(holder.getAdapterPosition(),editable.toString());
                Log.d(TAG, "participantsAnswers: " + participantsAnswers.get(holder.getAdapterPosition()));
            }
        });

        //endregion

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    //region view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvFillInTheBlanksOptionSerial;
        AppCompatEditText etFillInTheBlanksAnswer;

        ViewHolder(View itemView) {
            super(itemView);

            tvFillInTheBlanksOptionSerial = itemView.findViewById(R.id.tvFillInTheBlanksOptionSerial);
            etFillInTheBlanksAnswer = itemView.findViewById(R.id.etFillInTheBlanksAnswer);
        }

    }
    //endregion
}
