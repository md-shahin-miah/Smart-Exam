package com.crux.qxm.adapter.participationQxm;

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

import java.util.ArrayList;

import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;

public class ParticipateMultipleChoiceAdapter extends RecyclerView.Adapter<ParticipateMultipleChoiceAdapter.ViewHolder> {

    private static final String TAG = "MultipleChoiceAdapter";
    private ArrayList<String> options;
    private ArrayList<String> participantsAnswers;
    private Context context;
    boolean isClicked = false;
    private String viewFor;


    public ParticipateMultipleChoiceAdapter(Context context, ArrayList<String> options, ArrayList<String> participantsAnswers, String viewFor) {
        this.context = context;
        this.options = options;
        this.participantsAnswers = participantsAnswers;
        this.viewFor = viewFor;

    }

    @NonNull
    @Override
    public ParticipateMultipleChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_participate_multiple_choice_option, parent, false);
        return new ParticipateMultipleChoiceAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipateMultipleChoiceAdapter.ViewHolder holder, int position) {

        if(viewFor.equals(VIEW_FOR_SEE_QUESTION)){
            holder.rbMultipleChoiceOption.setClickable(false);
        }

        // region setting options
        if (options.size() > 0) {

            holder.tvMultipleChoiceOption.setText(options.get(position));

        }
        //endregion

        // region multiple choice correct answer updating
        holder.rbMultipleChoiceOption.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                participantsAnswers.add(options.get(holder.getAdapterPosition()));
                Log.d(TAG, "MultipleChoice CorrectAnswer: " + participantsAnswers);
                isClicked = true;

            } else {
                participantsAnswers.remove(options.get(holder.getAdapterPosition()));
                Log.d(TAG, "MultipleChoice CorrectAnswer: " + participantsAnswers);
            }
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
        AppCompatTextView tvMultipleChoiceOption;

        ViewHolder(View itemView) {
            super(itemView);

            rbMultipleChoiceOption = itemView.findViewById(R.id.rbMultipleChoiceOption);
            tvMultipleChoiceOption = itemView.findViewById(R.id.tvMultipleChoiceOption);
        }

    }

    //endregion
}
