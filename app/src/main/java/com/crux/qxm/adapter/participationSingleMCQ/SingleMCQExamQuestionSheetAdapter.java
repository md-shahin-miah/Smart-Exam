package com.crux.qxm.adapter.participationSingleMCQ;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.adapter.participationQxm.ParticipateMultipleChoiceAdapter;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SingleMCQExamQuestionSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ExamQuestionSheetAdapte";
    private Context context;
    private Picasso picasso;
    private List<Question> questionList;
    private String viewFor;

    public SingleMCQExamQuestionSheetAdapter(Context context, Picasso picasso, List<Question> questionList, String viewFor) {
        this.context = context;
        this.picasso = picasso;
        this.questionList = questionList;
        this.viewFor = viewFor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rec_participate_single_multiple_choice_layout, parent, false);
        return new MultipleChoiceQuestionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MultipleChoiceQuestionViewHolder viewHolder = (MultipleChoiceQuestionViewHolder) holder;
        MultipleChoice multipleChoice = questionList.get(position).getMultipleChoice();
        viewHolder.tvPoint.setText(String.format("Point: %s", multipleChoice.getPoints()));
        viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
        ParticipateMultipleChoiceAdapter multipleChoiceAdapter = new ParticipateMultipleChoiceAdapter(context, multipleChoice.getOptions(), multipleChoice.getParticipantsAnswers(), viewFor);
        viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
        viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class MultipleChoiceQuestionViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvPoint;
        public AppCompatTextView tvQuestionNumber;
        public RecyclerView rvMultipleOptions;


        public MultipleChoiceQuestionViewHolder(View itemView) {
            super(itemView);

            tvPoint = itemView.findViewById(R.id.tvPoint);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
        }
    }


}

