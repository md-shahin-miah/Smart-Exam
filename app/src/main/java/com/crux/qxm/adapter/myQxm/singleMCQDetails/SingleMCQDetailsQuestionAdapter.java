package com.crux.qxm.adapter.myQxm.singleMCQDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.adapter.singleMCQAdapter.SingleMCQAdapter;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SingleMCQDetailsQuestionAdapter extends RecyclerView.Adapter<SingleMCQDetailsQuestionAdapter.ViewHolder> {
    private Context context;
    private Picasso picasso;
    private List<MultipleChoice> multipleChoice;


    public SingleMCQDetailsQuestionAdapter(Context context, Picasso picasso, List<MultipleChoice> multipleChoice) {
        this.context = context;
        this.picasso = picasso;
        this.multipleChoice = multipleChoice;
    }

    @NonNull
    @Override
    public SingleMCQDetailsQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.rec_my_qxm_single_mcq_details_multiple_choice_question_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMCQDetailsQuestionAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
        viewHolder.tvQuestionTitle.setText(multipleChoice.get(position).getQuestionTitle());
        if (!TextUtils.isEmpty(multipleChoice.get(position).getImage())) {
            viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
            picasso.load(multipleChoice.get(position).getImage()).into(viewHolder.ivQuestionImage);
        } else {
            viewHolder.ivQuestionImage.setVisibility(View.GONE);
        }


        viewHolder.tvCorrectAnswer.setText(QxmArrayListToStringProcessor.getStringFromArrayList(multipleChoice.get(position).getCorrectAnswers()));

        SingleMCQWithAnswerAdapter singleMCQWithAnswerAdapter = new SingleMCQWithAnswerAdapter(context, multipleChoice.get(position));
        viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvMultipleOptions.setAdapter(singleMCQWithAnswerAdapter);
        viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return multipleChoice.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView tvQuestionNumber;
        public AppCompatTextView tvQuestionTitle;
        public AppCompatImageView ivQuestionImage;
        public RecyclerView rvMultipleOptions;
        public LinearLayoutCompat llCorrectAnswer;
        public AppCompatTextView tvCorrectAnswer;

        ViewHolder(View itemView) {
            super(itemView);

            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
            llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        }
    }
}



