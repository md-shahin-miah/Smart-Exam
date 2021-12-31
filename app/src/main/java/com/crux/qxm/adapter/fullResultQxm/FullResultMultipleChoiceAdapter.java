package com.crux.qxm.adapter.fullResultQxm;

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
import com.crux.qxm.utils.FullResultCorrectAnswerFindListener;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;

public class FullResultMultipleChoiceAdapter extends RecyclerView.Adapter<FullResultMultipleChoiceAdapter.ViewHolder> {

    private static final String TAG = "MultipleChoiceAdapter";
    private Context context;
    private MultipleChoice multipleChoice;

    public FullResultMultipleChoiceAdapter(Context context, MultipleChoice multipleChoice, FullResultCorrectAnswerFindListener correctAnswerFindListener) {
        this.context = context;
        this.multipleChoice = multipleChoice;

        if (multipleChoice.getParticipantsAnswers() != null
                && multipleChoice.getParticipantsAnswers().containsAll(multipleChoice.getCorrectAnswers())
                && multipleChoice.getParticipantsAnswers().size() == multipleChoice.getCorrectAnswers().size())
            correctAnswerFindListener.onCorrectAnswerFound(multipleChoice.getPoints());
        else
            correctAnswerFindListener.onWrongAnswerFound(
                    QxmArrayListToStringProcessor.getStringFromArrayList(multipleChoice.getCorrectAnswers()),
                    multipleChoice.getPoints()
            );
    }

    @NonNull
    @Override
    public FullResultMultipleChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_evaluation_multiple_choice_option, parent, false);
        return new FullResultMultipleChoiceAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull FullResultMultipleChoiceAdapter.ViewHolder holder, int position) {

        if(multipleChoice.getParticipantsAnswers().contains(multipleChoice.getOptions().get(position)))
            holder.rbMultipleChoiceOption.setChecked(true);
        else
            holder.rbMultipleChoiceOption.setChecked(false);

        if(multipleChoice.getCorrectAnswers().contains(multipleChoice.getOptions().get(position)))
            holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.correct_answer));
        else if(multipleChoice.getParticipantsAnswers().contains(multipleChoice.getOptions().get(position)))
            holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.wrong_answer));


        if (position < multipleChoice.getParticipantsAnswers().size())
            Log.d(TAG, "onBindViewHolder: participantsAnswers: " + multipleChoice.getParticipantsAnswers().get(position));

        // region SettingOptions

        if (multipleChoice.getOptions().size() > 0)
            holder.tvMultipleChoiceOption.setText(multipleChoice.getOptions().get(position));

        //endregion

        if(multipleChoice.getParticipantsAnswers().contains(multipleChoice.getOptions().get(position))
                && multipleChoice.getCorrectAnswers().contains(multipleChoice.getOptions().get(position))){
            // this is participants answer, so checked the checkBox
            holder.rbMultipleChoiceOption.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return multipleChoice.getOptions().size();
    }

    // region FollowingViewHolder

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
