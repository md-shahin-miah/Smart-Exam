package com.crux.qxm.adapter.singleMCQAdapter;

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

public class SingleMCQAdapter extends RecyclerView.Adapter<SingleMCQAdapter.ViewHolder> {

    private static final String TAG = "SingleMCQAdapter";
    private Context context;
    private MultipleChoice multipleChoice;

    public SingleMCQAdapter(Context context, MultipleChoice multipleChoice) {
        this.context = context;
        this.multipleChoice = multipleChoice;
    }

    @NonNull
    @Override
    public SingleMCQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View multipleChoiceOptionsView = LayoutInflater.from(context).inflate(R.layout.rec_single_mcq_option, parent, false);
        return new SingleMCQAdapter.ViewHolder(multipleChoiceOptionsView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMCQAdapter.ViewHolder holder, int position) {

        // region SettingOptions

        holder.tvMultipleChoiceOption.setText(multipleChoice.getOptions().get(position));

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
