package com.crux.qxm.adapter.myQxm.singleQxmDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;

import java.util.ArrayList;

public class SingleQxmDetailsMultipleChoiceAdapter extends RecyclerView.Adapter<SingleQxmDetailsMultipleChoiceAdapter.ViewHolder> {

    private static final String TAG = "MultipleChoiceAdapter";
    private Context context;
    private ArrayList<String> options;
    private ArrayList<String> correctAnswers;

    SingleQxmDetailsMultipleChoiceAdapter(Context context, ArrayList<String> options, ArrayList<String> correctAnswers) {
        this.context = context;
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    @NonNull
    @Override
    public SingleQxmDetailsMultipleChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_evaluation_multiple_choice_option, parent, false);
        return new SingleQxmDetailsMultipleChoiceAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleQxmDetailsMultipleChoiceAdapter.ViewHolder holder, int position) {

        holder.tvMultipleChoiceOption.setText(options.get(position));

        for(String correct : correctAnswers){
            if(correct.equals(options.get(position))){
                holder.tvMultipleChoiceOption.setTextColor(context.getResources().getColor(R.color.correct_answer));
            }
        }

    }

    @Override
    public int getItemCount() {
        return options.size();
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
