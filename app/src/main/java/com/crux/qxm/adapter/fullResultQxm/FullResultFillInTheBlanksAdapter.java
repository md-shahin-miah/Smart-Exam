package com.crux.qxm.adapter.fullResultQxm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.utils.FullResultCorrectAnswerFindListener;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;

public class FullResultFillInTheBlanksAdapter extends RecyclerView.Adapter<FullResultFillInTheBlanksAdapter.ViewHolder> {

    private static final String TAG = "FillInTheBlanksAdapter";

    private Context context;
    private FillInTheBlanks fillInTheBlanks;
    private FullResultCorrectAnswerFindListener correctAnswerFindListener;

    private String[] answerSerialArray = {"(a)", "(b)", "(c)", "(d)", "(e)", "(f)", "(g)", "(h)", "(i)", "(j)"
    };

    public FullResultFillInTheBlanksAdapter(Context context, FillInTheBlanks fillInTheBlanks, FullResultCorrectAnswerFindListener correctAnswerFindListener) {
        this.context = context;
        this.fillInTheBlanks = fillInTheBlanks;
        this.correctAnswerFindListener = correctAnswerFindListener;
    }

    @NonNull
    @Override
    public FullResultFillInTheBlanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View fillInTheBlanksView = LayoutInflater.from(context).inflate(R.layout.rec_full_result_single_fill_in_the_blank_option, parent, false);
        return new ViewHolder(fillInTheBlanksView);
    }

    @Override
    public void onBindViewHolder(@NonNull FullResultFillInTheBlanksAdapter.ViewHolder holder, int position) {

        String answerSerial = answerSerialArray[position];
        holder.tvFillInTheBlanksOptionSerial.setText(answerSerial);
        String dummyAnswer = "answer to the question";
        holder.tvFillInTheBlanksAnswer.setHint(dummyAnswer);

        // region SettingAnswers

        if (fillInTheBlanks.getParticipantsAnswers().size() > 0)
            holder.tvFillInTheBlanksAnswer.setText(fillInTheBlanks.getParticipantsAnswers().get(position));

        //endregion

        // region CorrectAnswerFindListener

        if (fillInTheBlanks.isAnswerPartiallyCorrect()) {
            correctAnswerFindListener.onPartialCorrectAnswerFound(fillInTheBlanks.getPoints(),fillInTheBlanks.getAchievedPoints());
        }
        else if (fillInTheBlanks.isCorrect())
            correctAnswerFindListener.onCorrectAnswerFound(fillInTheBlanks.getPoints());
        else
            correctAnswerFindListener.onWrongAnswerFound(QxmArrayListToStringProcessor.getStringFromArrayListAlphabetically(fillInTheBlanks.getCorrectAnswers()), fillInTheBlanks.getPoints());

        // endregion
    }

    @Override
    public int getItemCount() {
        return fillInTheBlanks.getCorrectAnswers().size();
    }

    //region FollowingViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
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
