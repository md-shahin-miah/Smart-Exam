package com.crux.qxm.adapter.questions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crux.qxm.R;

import java.util.ArrayList;

public class SingleMultipleChoiceAdapter extends RecyclerView.Adapter<SingleMultipleChoiceAdapter.ViewHolder> {

    private static final String TAG = "SingleMultipleChoiceAda";
    private ArrayList<String> options;
    private ArrayList<String> correctAnswer;
    private Context context;
    boolean isClicked = false;
    private boolean singleMCQEdit = false;


    public SingleMultipleChoiceAdapter(Context context, ArrayList<String> options, ArrayList<String> correctAnswer) {
        this.context = context;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public SingleMultipleChoiceAdapter(Context context, ArrayList<String> options, ArrayList<String> correctAnswer, boolean singleMCQEdit) {
        this.context = context;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.singleMCQEdit = singleMCQEdit;
    }


    @NonNull
    @Override
    public SingleMultipleChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.create_qxm_single_multiple_choice_option, parent, false);
        return new SingleMultipleChoiceAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMultipleChoiceAdapter.ViewHolder holder, int position) {

        String questionPositionNumber = String.valueOf(position + 1);
        String dummyOption = "Write Option" + " " + questionPositionNumber;
        holder.etMultipleChoiceOption.setHint(dummyOption);

        if(singleMCQEdit) holder.ivDeleteMcqOption.setVisibility(View.GONE);
        else holder.ivDeleteMcqOption.setVisibility(View.VISIBLE);


        // region setting correct radio button check during duplicate

        if (correctAnswer.size() > 0) {

            for (int i = 0; i < correctAnswer.size(); i++) {

                if (correctAnswer.get(i).equals(options.get(holder.getAdapterPosition()))) {
                    holder.rbMultipleChoiceOption.setChecked(true);
                }
            }
        }

        //endregion

        // region setting option during duplicate
        if (options.size() > 0) {

            holder.etMultipleChoiceOption.setText(options.get(position));

        }
        //endregion

        // region multiple choice option updating
        holder.etMultipleChoiceOption.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d(TAG, "beforeTextChanged: " + charSequence);

                if(correctAnswer.contains(charSequence.toString())){

                    holder.rbMultipleChoiceOption.setChecked(false);
                    correctAnswer.remove(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d(TAG, "onTextChanged: " + charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: " + editable);
                options.set(holder.getAdapterPosition(), editable.toString());

            }
        });
        //endregion

        // region multiple choice correct answer updating
        holder.rbMultipleChoiceOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(holder.etMultipleChoiceOption.getText().toString().trim().isEmpty()){

                    Toast.makeText(context, "Empty option can't be correct answer", Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                }else {

                    if(b){

                        correctAnswer.add(holder.etMultipleChoiceOption.getText().toString());
                        Log.d(TAG, "MultipleChoice CorrectAnswer: "+correctAnswer);
                        isClicked = true;

                    }else {

                        // we are using if and contains here,because if a user delete an option from click cross button then
                        // we are setting checkbox set checked false(See holder.ivDeleteMcqOption.setOnClickListener).
                        // As a result this method will be called and will not find this item,because it is already deleted

                        correctAnswer.remove(holder.etMultipleChoiceOption.getText().toString());
                        Log.d(TAG, "MultipleChoice CorrectAnswer: "+correctAnswer);
                    }
                }


            }
        });
        //endregion

        //region deleting mcq option

        holder.ivDeleteMcqOption.setOnClickListener(v->{

            options.remove(holder.getAdapterPosition());

            // if this option index is in correct answer list,also delete it
            if(correctAnswer != null){
                if(correctAnswer.size()!=0){
                    for(int i =0;i<correctAnswer.size();i++){

                        if(holder.etMultipleChoiceOption.getText().toString().equals(correctAnswer.get(i))){

                            correctAnswer.remove(i);
                            holder.rbMultipleChoiceOption.setChecked(false);
                            break;
                        }

                    }
                    Log.d(TAG, "MultipleChoice CorrectAnswer Holder Position: "+holder.getAdapterPosition());
                    Log.d(TAG, "MultipleChoice CorrectAnswer: "+correctAnswer);
                }
            }

            notifyItemRemoved(holder.getAdapterPosition());

        });

        //endregion

    }

    @Override
    public int getItemCount() {
        return options.size();
    }


    // region view holder
    class ViewHolder extends RecyclerView.ViewHolder {


        AppCompatCheckBox rbMultipleChoiceOption;
        AppCompatEditText etMultipleChoiceOption;
        RelativeLayout rlMultipleChoiceOptionHolder;
        AppCompatImageView ivDeleteMcqOption;

        ViewHolder(View itemView) {
            super(itemView);

            rbMultipleChoiceOption = itemView.findViewById(R.id.rbMultipleChoiceOption);
            etMultipleChoiceOption = itemView.findViewById(R.id.etMultipleChoiceOption);
            rlMultipleChoiceOptionHolder = itemView.findViewById(R.id.rlMultipleChoiceOptionHolder);
            ivDeleteMcqOption = itemView.findViewById(R.id.ivDeleteMcqOption);
        }


    }

    //endregion
}
