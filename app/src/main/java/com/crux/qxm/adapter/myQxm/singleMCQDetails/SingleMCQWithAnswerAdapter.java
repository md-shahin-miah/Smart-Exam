package com.crux.qxm.adapter.myQxm.singleMCQDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.MultipleChoice;

public class SingleMCQWithAnswerAdapter extends RecyclerView.Adapter<SingleMCQWithAnswerAdapter.ViewHolder> {

    private static final String TAG = "SingleMCQAdapter";
    private Context context;
    private MultipleChoice multipleChoice;

    public SingleMCQWithAnswerAdapter(Context context, MultipleChoice multipleChoice) {
        this.context = context;
        this.multipleChoice = multipleChoice;
    }

    @NonNull
    @Override
    public SingleMCQWithAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_single_mcq_option, parent, false);
        return new SingleMCQWithAnswerAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMCQWithAnswerAdapter.ViewHolder holder, int position) {

        // region SettingOptions

        holder.tvMultipleChoiceOption.setText(multipleChoice.getOptions().get(position));

        if (multipleChoice.getCorrectAnswers().contains(multipleChoice.getOptions().get(position))) {
            holder.rbMultipleChoiceOption.setChecked(true);
            holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.correct_answer));
        } else{
            holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.primary_text_color));
            holder.rbMultipleChoiceOption.setChecked(false);
        }

        //endregion
    }

    @Override
    public int getItemCount() {

        // Log.d(TAG, "getItemCount: " + multipleChoice.getOptions().size());
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
