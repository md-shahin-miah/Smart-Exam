package com.crux.qxm.adapter.evaluateQxm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.utils.EvaluationMultipleChoiceListener;

public class EvaluateMultipleChoiceAdapter extends RecyclerView.Adapter<EvaluateMultipleChoiceAdapter.ViewHolder> {

    private static final String TAG = "MultipleChoiceAdapter";
    private Context context;
    private MultipleChoice multipleChoice;
    private EvaluationMultipleChoiceListener evaluationMultipleChoiceClickListener;


    public EvaluateMultipleChoiceAdapter(Context context, MultipleChoice multipleChoice, EvaluationMultipleChoiceListener evaluationMultipleChoiceClickListener) {
        this.context = context;
        this.multipleChoice = multipleChoice;
        this.evaluationMultipleChoiceClickListener = evaluationMultipleChoiceClickListener;
    }

    @NonNull
    @Override
    public EvaluateMultipleChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_evaluation_multiple_choice_option, parent, false);
        return new EvaluateMultipleChoiceAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluateMultipleChoiceAdapter.ViewHolder holder, int position) {
        if (position < multipleChoice.getParticipantsAnswers().size())
            Log.d(TAG, "onBindViewHolder: participantsAnswers: " + multipleChoice.getParticipantsAnswers().get(position));

        // region setting options
        if (multipleChoice.getOptions().size() > 0) {

            holder.tvMultipleChoiceOption.setText(multipleChoice.getOptions().get(position));

        }
        //endregion



        for (String participantsAnswer : multipleChoice.getParticipantsAnswers()) {
            if (multipleChoice.getOptions().get(position).equals(participantsAnswer)) {
                // this is participants answer, so checked the checkBox
                holder.rbMultipleChoiceOption.setChecked(true);
            }

            for (String correctAnswer : multipleChoice.getCorrectAnswers()) {

                if (multipleChoice.getOptions().get(position).equals(participantsAnswer) && multipleChoice.getOptions().get(position).equals(correctAnswer)) {
                    multipleChoice.setCorrect(true);
                    holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
                    evaluationMultipleChoiceClickListener.onCorrectAnswerClicked();

                } else if(multipleChoice.getOptions().get(position).equals(participantsAnswer) && !multipleChoice.getOptions().get(position).equals(correctAnswer)) {
                    multipleChoice.setCorrect(false);
                    holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.btn_background_color_wrong_answer));
                    evaluationMultipleChoiceClickListener.onWrongAnswerClicked();
                }else if(multipleChoice.getOptions().get(position).equals(correctAnswer)){
                    holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return multipleChoice.getOptions().size();
    }


    // region view holder
    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox rbMultipleChoiceOption;
        AppCompatTextView tvMultipleChoiceOption;

        ViewHolder(View itemView) {
            super(itemView);

            rbMultipleChoiceOption = itemView.findViewById(R.id.rbMultipleChoiceOption);
            tvMultipleChoiceOption = itemView.findViewById(R.id.tvMultipleChoiceOption);
        }

    }

    //endregion
}
