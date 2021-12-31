package com.crux.qxm.adapter.fullResultSingleMCQ;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.adapter.fullResultQxm.FullResultMultipleChoiceAdapter;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.utils.FullResultCorrectAnswerFindListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullResultSingleMCQAdapter extends RecyclerView.Adapter<FullResultSingleMCQAdapter.ViewHolder> {
    private static final String TAG = "FullResultQxmAdapter";
    private Context context;
    private Picasso picasso;
    private List<MultipleChoice> multipleChoice;
    private boolean amICreator;


    public FullResultSingleMCQAdapter(Context context, Picasso picasso, List<MultipleChoice> multipleChoice, boolean amICreator) {
        this.context = context;
        this.picasso = picasso;
        this.multipleChoice = multipleChoice;
        this.amICreator = amICreator;
    }

    @NonNull
    @Override
    public FullResultSingleMCQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.rec_full_result_single_mcq_multiple_choice_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullResultSingleMCQAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
        viewHolder.tvQuestionTitle.setText(multipleChoice.get(position).getQuestionTitle());
        if (!TextUtils.isEmpty(multipleChoice.get(position).getImage())) {
            viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
            picasso.load(multipleChoice.get(position).getImage()).into(viewHolder.ivQuestionImage);
        } else {
            viewHolder.ivQuestionImage.setVisibility(View.GONE);
        }

        FullResultCorrectAnswerFindListener multipleChoiceCorrectAnswerFindListener = new FullResultCorrectAnswerFindListener() {
            @Override
            public void onPartialCorrectAnswerFound(String points, String achievePoints) {

            }

            @Override
            public void onCorrectAnswerFound(String points) {
                Log.d(TAG, "onCorrectAnswerFound: ");
                Log.d(TAG, "onCorrectAnswerFound: amICreator: " + amICreator);
                if (amICreator)
                    viewHolder.tvAnswerResult.setText(context.getString(R.string.answer_is_correct));
                else
                    viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_correct));

                viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                viewHolder.llCorrectAnswer.setVisibility(View.GONE);
            }

            @Override
            public void onWrongAnswerFound(String correctAnswer, String points) {
                Log.d(TAG, String.format("onWrongAnswerFound: correctAnswer: %s, points: %s", correctAnswer, points));

                viewHolder.llQuestionEvaluationResult.setBackground(context.getResources().getDrawable(R.drawable.background_wrong_answer_full_result_preview));
                viewHolder.tvAnswerResult.setTextColor(context.getResources().getColor(R.color.wrong_answer));

                Log.d(TAG, "onWrongAnswerFound: amICreator: " + amICreator);
                if (amICreator)
                    viewHolder.tvAnswerResult.setText(context.getString(R.string.answer_is_wrong));
                else
                    viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_wrong));

                if (TextUtils.isEmpty(correctAnswer))
                    viewHolder.rlAnswerIsNotFound.setVisibility(View.VISIBLE);
                else
                    viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);

                viewHolder.tvCorrectAnswer.setText(correctAnswer);

            }
        };


        FullResultMultipleChoiceAdapter multipleChoiceAdapter = new FullResultMultipleChoiceAdapter(context, multipleChoice.get(position), multipleChoiceCorrectAnswerFindListener);
        viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
        viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return multipleChoice.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvQuestionNumber;
        AppCompatTextView tvQuestionTitle;
        AppCompatImageView ivQuestionImage;
        RecyclerView rvMultipleOptions;
        LinearLayoutCompat llQuestionEvaluationResult;
        AppCompatTextView tvAnswerResult;
        RelativeLayout rlAnswerIsNotFound;
        AppCompatImageView ivInfo;
        LinearLayoutCompat llCorrectAnswer;
        AppCompatTextView tvCorrectAnswer;

        ViewHolder(View itemView) {
            super(itemView);

            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
            llQuestionEvaluationResult = itemView.findViewById(R.id.llQuestionEvaluationResult);
            tvAnswerResult = itemView.findViewById(R.id.tvAnswerResult);
            rlAnswerIsNotFound = itemView.findViewById(R.id.rlAnswerIsNotFound);
            ivInfo = itemView.findViewById(R.id.ivInfo);
            llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        }
    }
}



