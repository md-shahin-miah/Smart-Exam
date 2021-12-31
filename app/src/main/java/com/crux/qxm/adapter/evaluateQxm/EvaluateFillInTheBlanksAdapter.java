package com.crux.qxm.adapter.evaluateQxm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.utils.EvaluationFillInTheBlanksClickListener;

public class EvaluateFillInTheBlanksAdapter extends RecyclerView.Adapter<EvaluateFillInTheBlanksAdapter.ViewHolder> {

    private static final String TAG = "FillInTheBlanksAdapter";

    private Context context;
    private FillInTheBlanks fillInTheBlanks;
    private EvaluationFillInTheBlanksClickListener evaluationMultipleChoiceClickListener;
    private int achievePoint = 0;

    private String[] answerSerialArray = {"(a)", "(b)", "(c)", "(d)", "(e)", "(f)", "(g)", "(h)", "(i)", "(j)"};

    public EvaluateFillInTheBlanksAdapter(Context context, FillInTheBlanks fillInTheBlanks, EvaluationFillInTheBlanksClickListener evaluationMultipleChoiceClickListener) {
        this.context = context;
        this.fillInTheBlanks = fillInTheBlanks;
        this.evaluationMultipleChoiceClickListener = evaluationMultipleChoiceClickListener;
    }

    @NonNull
    @Override
    public EvaluateFillInTheBlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View fillInTheBlanksView = LayoutInflater.from(context).inflate(R.layout.rec_evaluation_single_fill_in_the_blank_option, parent, false);
        return new EvaluateFillInTheBlanksAdapter.ViewHolder(fillInTheBlanksView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluateFillInTheBlanksAdapter.ViewHolder holder, int position) {

        String answerSerial = answerSerialArray[position];
        holder.tvFillInTheBlanksOptionSerial.setText(answerSerial);
        String dummyAnswer = "answer to the question";
        holder.tvFillInTheBlanksAnswer.setHint(dummyAnswer);

        // region setting answer
        if (fillInTheBlanks.getCorrectAnswers().size() > 0) {
            holder.tvFillInTheBlanksAnswer.setText(fillInTheBlanks.getParticipantsAnswers().get(position));
        }
        //endregion

        // for (String participantsAnswer : fillInTheBlanks.getParticipantsAnswers()) {
        //     if (fillInTheBlanks.getCorrectAnswers().get(position).equals(participantsAnswer)) {
        //         Log.d(TAG, "FillInTheBlanks-> Answer is correct. " + fillInTheBlanks.getCorrectAnswers().get(position) + " = " + participantsAnswer);
        //         holder.tvFillInTheBlanksAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
        //     } else {
        //         Log.d(TAG, "FillInTheBlanks-> Answer is Wrong. " + fillInTheBlanks.getCorrectAnswers().get(position) + " != " + participantsAnswer);
        //         holder.tvFillInTheBlanksAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_wrong_answer));
        //     }
        // }

        holder.btnCorrectAnswer.setOnClickListener(v -> {
            // fillInTheBlanks.setCorrect(true);
            // evaluationMultipleChoiceClickListener.onCorrectAnswerClicked(holder.itemView);
            holder.tvFillInTheBlanksAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
            holder.btnCorrectAnswer.setAlpha(1.0f);
            holder.btnCorrectAnswer.setClickable(false);
            holder.btnWrongAnswer.setAlpha(0.5f);
            holder.btnWrongAnswer.setClickable(true);
            achievePoint++;
            int totalAnswerCount = fillInTheBlanks.getCorrectAnswers().size();
            double calculateTotalPoint = (achievePoint * Double.parseDouble(fillInTheBlanks.getPoints())) / totalAnswerCount;
            fillInTheBlanks.setAchievedPoints(String.valueOf(calculateTotalPoint));
            if (achievePoint == fillInTheBlanks.getCorrectAnswers().size()) {
                fillInTheBlanks.setCorrect(true);
                fillInTheBlanks.setAnswerPartiallyCorrect(false);
            } else if (achievePoint > 0 && achievePoint < Double.parseDouble(fillInTheBlanks.getPoints())) {
                fillInTheBlanks.setAnswerPartiallyCorrect(true);
            } else fillInTheBlanks.setAnswerPartiallyCorrect(false);

        });

        holder.btnWrongAnswer.setOnClickListener(v -> {

            // evaluationMultipleChoiceClickListener.onWrongAnswerClicked(holder.itemView);
            holder.tvFillInTheBlanksAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_wrong_answer));
            holder.btnCorrectAnswer.setAlpha(0.5f);
            holder.btnCorrectAnswer.setClickable(true);
            holder.btnWrongAnswer.setAlpha(1.0f);
            holder.btnWrongAnswer.setClickable(false);
            int totalAnswerCount = fillInTheBlanks.getCorrectAnswers().size();
            double calculateTotalPoint = (achievePoint * Double.parseDouble(fillInTheBlanks.getPoints())) / totalAnswerCount;
            fillInTheBlanks.setAchievedPoints(String.valueOf(calculateTotalPoint));
            if (achievePoint == 0) {
                fillInTheBlanks.setCorrect(false);
                fillInTheBlanks.setAnswerPartiallyCorrect(false);
            }
            else if (achievePoint > 0 && achievePoint < Double.parseDouble(fillInTheBlanks.getPoints())) {
                fillInTheBlanks.setAnswerPartiallyCorrect(true);
            } else {
                fillInTheBlanks.setAnswerPartiallyCorrect(false);
            }

        });
    }

    @Override
    public int getItemCount() {
        return fillInTheBlanks.getCorrectAnswers().size();
    }

    //region view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvFillInTheBlanksOptionSerial;
        AppCompatTextView tvFillInTheBlanksAnswer;
        AppCompatImageButton btnWrongAnswer;
        AppCompatImageButton btnCorrectAnswer;

        ViewHolder(View itemView) {
            super(itemView);

            tvFillInTheBlanksOptionSerial = itemView.findViewById(R.id.tvFillInTheBlanksOptionSerial);
            tvFillInTheBlanksAnswer = itemView.findViewById(R.id.tvFillInTheBlanksAnswer);
            btnWrongAnswer = itemView.findViewById(R.id.btnWrongAnswer);
            btnCorrectAnswer = itemView.findViewById(R.id.btnCorrectAnswer);

        }

    }
    //endregion
}
