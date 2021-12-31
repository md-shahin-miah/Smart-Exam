package com.crux.qxm.adapter.myQxm.singleQxmDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.ShortAnswer;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;

public class MyQxmSingleQxmQuestionSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region MyQxmSingleQxmQuestionSheetAdapter-Properties
    private static final String TAG = "MyQxmSingleQxmQuestionS";
    private Context context;
    private Picasso picasso;
    private List<Question> questionList;
    //endregion

    //region MyQxmSingleQxmQuestionSheetAdapter-Constructor
    public MyQxmSingleQxmQuestionSheetAdapter(Context context, Picasso picasso, List<Question> questionList) {
        this.context = context;
        this.picasso = picasso;
        this.questionList = questionList;
    }
    //endregion

    //region Override-onCreateViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 1) { //QUESTION_TYPE_MULTIPLE_CHOICE
            View view = inflater.inflate(R.layout.rec_my_qxm_single_qxm_multiple_choice_layout, parent, false);
            return new MultipleChoiceQuestionViewHolder(view);

        } else if (viewType == 2) { //QUESTION_TYPE_FILL_IN_THE_BLANKS
            View view = inflater.inflate(R.layout.rec_my_qxm_single_qxm_fill_in_the_blanks_layout, parent, false);
            return new FillInTheBlanksViewHolder(view);

        } else { //QUESTION_TYPE_SHORT_ANSWER
            View view = inflater.inflate(R.layout.rec_my_qxm_single_qxm_short_answer_layout, parent, false);
            return new ShortAnswerViewHolder(view);
        }

    }
    //endregion

    //region Override-onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1: { //QUESTION_TYPE_MULTIPLE_CHOICE
                MultipleChoiceQuestionViewHolder viewHolder = (MultipleChoiceQuestionViewHolder) holder;
                MultipleChoice multipleChoice = questionList.get(position).getMultipleChoice();
                viewHolder.tvPoint.setText(String.format("Point: %s", multipleChoice.getPoints()));
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(multipleChoice.getQuestionTitle());
                if (!TextUtils.isEmpty(multipleChoice.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(multipleChoice.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }
                SingleQxmDetailsMultipleChoiceAdapter multipleChoiceAdapter = new SingleQxmDetailsMultipleChoiceAdapter(context, multipleChoice.getOptions(), multipleChoice.getCorrectAnswers());
                viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
                viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

                viewHolder.tvCorrectAnswer.setText(QxmArrayListToStringProcessor
                        .getStringFromArrayList(multipleChoice.getCorrectAnswers()));
            }
            break;

            case 2: { //QUESTION_TYPE_FILL_IN_THE_BLANKS
                FillInTheBlanksViewHolder viewHolder = (FillInTheBlanksViewHolder) holder;

                FillInTheBlanks fillInTheBlanks = questionList.get(position).getFillInTheBlanks();
                viewHolder.tvPoint.setText(String.format("Point: %s", fillInTheBlanks.getPoints()));
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(fillInTheBlanks.getQuestionTitle());

                if (!TextUtils.isEmpty(fillInTheBlanks.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(fillInTheBlanks.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }

                SingleQxmDetailsFillInTheBlanksAdapter fillInTheBlanksAdapter = new SingleQxmDetailsFillInTheBlanksAdapter(context, fillInTheBlanks.getCorrectAnswers());
                viewHolder.rvFillBlankAnswers.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvFillBlankAnswers.setAdapter(fillInTheBlanksAdapter);
                viewHolder.rvFillBlankAnswers.setNestedScrollingEnabled(false);
            }
            break;

            case 3: { //QUESTION_TYPE_SHORT_ANSWER
                ShortAnswerViewHolder viewHolder = (ShortAnswerViewHolder) holder;
                ShortAnswer shortAnswer = questionList.get(position).getShortAnswer();
                Log.d(TAG, "onBindViewHolder: ShortAnswer: " + shortAnswer.toString());

                viewHolder.tvPoint.setText(String.format("Point: %s", shortAnswer.getPoints()));
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(shortAnswer.getQuestionTitle());
                if (!TextUtils.isEmpty(shortAnswer.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(shortAnswer.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }

                viewHolder.tvWordLimit.setText(String.format(context.getString(R.string.character_limit_with_variable), shortAnswer.getWordLimits()));
            }
            break;

            default:
                break;
        }
    }
    //endregion

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        return questionList.size();
    }
    //endregion

    //region Override-getItemViewType
    @Override
    public int getItemViewType(int position) {
        switch (questionList.get(position).getQuestionType()) {
            case QUESTION_TYPE_MULTIPLE_CHOICE:
                return 1;
            case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                return 2;
            case QUESTION_TYPE_SHORT_ANSWER:
                return 3;
        }
        return 0;
    }
    //endregion
}

class MultipleChoiceQuestionViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvMultipleOptions;
    public AppCompatTextView tvCorrectAnswer;


    MultipleChoiceQuestionViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
    }
}

class ShortAnswerViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public AppCompatTextView tvWordLimit;

    ShortAnswerViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        tvWordLimit = itemView.findViewById(R.id.tvWordLimit);
    }
}

class FillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvFillBlankAnswers;

    FillInTheBlanksViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvFillBlankAnswers = itemView.findViewById(R.id.rvFillBlankAnswers);
    }
}