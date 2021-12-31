package com.crux.qxm.adapter.questions;

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

public class FillInTheBlanksAdapter extends RecyclerView.Adapter<FillInTheBlanksAdapter.ViewHolder> {

    private static final String TAG = "FillInTheBlanksAdapter";

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

    public FillInTheBlanksAdapter(Context context, ArrayList<String> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public FillInTheBlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View fillInTheBlanksView = LayoutInflater.from(context).inflate(R.layout.create_qxm_single_fill_in_the_blank_option,parent,false);
        return new FillInTheBlanksAdapter.ViewHolder(fillInTheBlanksView);
    }

    @Override
    public void onBindViewHolder(@NonNull FillInTheBlanksAdapter.ViewHolder holder, int position) {

        String answerSerial = answerSerialArray[position];
        holder.tvFillInTheBlanksOptionSerial.setText(answerSerial);
        String dummyAnswer ="answer to the question";
        holder.etFillInTheBlanksAnswer.setHint(dummyAnswer);

        // region setting answer during duplicate
        if (answers.size() > 0) {

            holder.etFillInTheBlanksAnswer.setText(answers.get(position));

        }
        //endregion

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
                answers.set(holder.getAdapterPosition(),editable.toString());
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
