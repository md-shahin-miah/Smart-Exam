package com.crux.qxm.adapter.fullResultQxm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.ShortAnswer;
import com.crux.qxm.utils.FullResultCorrectAnswerFindListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;

public class FullResultQxmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "FullResultQxmAdapter";
    private Context context;
    private Picasso picasso;
    private List<Question> questionList;
    private double negativePoint;
    private boolean amICreator;


    public FullResultQxmAdapter(Context context, Picasso picasso, List<Question> questionList, double negativePoint, boolean amICreator) {
        this.context = context;
        this.picasso = picasso;
        this.questionList = questionList;
        this.negativePoint = negativePoint;
        this.amICreator = amICreator;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 1) { //QUESTION_TYPE_MULTIPLE_CHOICE
            View view = inflater.inflate(R.layout.rec_full_result_multiple_choice_layout, parent, false);
            return new MultipleChoiceQuestionViewHolder(view);

        } else if (viewType == 2) { //QUESTION_TYPE_FILL_IN_THE_BLANKS
            View view = inflater.inflate(R.layout.rec_full_result_qxm_fill_in_the_blanks_layout, parent, false);
            return new FillInTheBlanksViewHolder(view);

        } else { //QUESTION_TYPE_SHORT_ANSWER
            View view = inflater.inflate(R.layout.rec_full_result_short_answer_layout, parent, false);
            return new ShortAnswerViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1: { //QUESTION_TYPE_MULTIPLE_CHOICE
                MultipleChoiceQuestionViewHolder viewHolder = (MultipleChoiceQuestionViewHolder) holder;
                MultipleChoice multipleChoice = questionList.get(position).getMultipleChoice();
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(multipleChoice.getQuestionTitle());
                if (!TextUtils.isEmpty(multipleChoice.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(multipleChoice.getImage()).into(viewHolder.ivQuestionImage);
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


                        if (amICreator) {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.answer_is_correct));
                            viewHolder.tvAnswerAchievePoints.setText(String.format("Got %s out of %s", points, points));
                        } else {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_correct));
                            viewHolder.tvAnswerAchievePoints.setText(String.format("You have got %s out of %s", points, points));
                        }

                        viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                        viewHolder.llCorrectAnswer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onWrongAnswerFound(String correctAnswer, String points) {
                        Log.d(TAG, String.format("onWrongAnswerFound: correctAnswer: %s, points: %s", correctAnswer, points));

                        viewHolder.llQuestionEvaluationResult.setBackground(context.getResources().getDrawable(R.drawable.background_wrong_answer_full_result_preview));
                        viewHolder.tvAnswerResult.setTextColor(context.getResources().getColor(R.color.wrong_answer));


                        Log.d(TAG, "onWrongAnswerFound: negative point: " + negativePoint);
                        Log.d(TAG, "onWrongAnswerFound: question point: " + points);

                        if (amICreator) {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.answer_is_wrong));

                            if (negativePoint > 0)
                                viewHolder.tvAnswerAchievePoints.setText(String.format("Got -%s out of %s", negativePoint, points));
                            else
                                viewHolder.tvAnswerAchievePoints.setText(String.format("Got 0 out of %s", points));
                        } else {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_wrong));

                            if (negativePoint > 0)
                                viewHolder.tvAnswerAchievePoints.setText(String.format("You have got -%s out of %s", negativePoint, points));
                            else
                                viewHolder.tvAnswerAchievePoints.setText(String.format("You have got 0 out of %s", points));
                        }


                        if (TextUtils.isEmpty(correctAnswer))
                            viewHolder.rlAnswerIsNotFound.setVisibility(View.VISIBLE);
                        else
                            viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);

                        viewHolder.tvCorrectAnswer.setText(correctAnswer);

                    }
                };


                FullResultMultipleChoiceAdapter multipleChoiceAdapter = new FullResultMultipleChoiceAdapter(context, multipleChoice, multipleChoiceCorrectAnswerFindListener);
                viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
                viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

            }
            break;

            case 2: { //QUESTION_TYPE_FILL_IN_THE_BLANKS
                FillInTheBlanksViewHolder viewHolder = (FillInTheBlanksViewHolder) holder;
                FillInTheBlanks fillInTheBlanks = questionList.get(position).getFillInTheBlanks();

                Gson gson = new Gson();
                Log.d(TAG, "onBindViewHolder: FillInTheBlanks JSON: " + gson.toJson(fillInTheBlanks));

                viewHolder.tvPoint.setText(fillInTheBlanks.getPoints());
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(fillInTheBlanks.getQuestionTitle());
                if (!TextUtils.isEmpty(fillInTheBlanks.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(fillInTheBlanks.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }

                FullResultCorrectAnswerFindListener fillInTheBlanksCorrectAnswerFindListener = new FullResultCorrectAnswerFindListener() {

                    @Override
                    public void onPartialCorrectAnswerFound(String points, String achievePoints) {
                        Log.d(TAG, "onPartialCorrect answer found: ");
                        viewHolder.tvAnswerResult.setTextColor(context.getResources().getColor(R.color.partially_correct_answer));

                        if (amICreator) {
                            viewHolder.tvAnswerAchievePoints.setText(String.format("Got %s out of %s", achievePoints, points));
                        } else {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_partially_correct));
                            viewHolder.tvAnswerAchievePoints.setText(String.format("You have got %s out of %s", achievePoints, points));
                        }

                        viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                        viewHolder.llCorrectAnswer.setVisibility(View.GONE);

                    }

                    public void onCorrectAnswerFound(String points) {
                        Log.d(TAG, "onCorrectAnswerFound: ");

                        if (amICreator) {
                            viewHolder.tvAnswerAchievePoints.setText(String.format("Got %s out of %s", points, points));
                        } else {
                            viewHolder.tvAnswerAchievePoints.setText(String.format("You have got %s out of %s", points, points));
                        }

                        viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                        viewHolder.llCorrectAnswer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onWrongAnswerFound(String correctAnswer, String points) {
                        Log.d(TAG, String.format("onWrongAnswerFound: correctAnswer: %s, points: %s", correctAnswer, points));

                        viewHolder.llQuestionEvaluationResult.setBackground(context.getResources().getDrawable(R.drawable.background_wrong_answer_full_result_preview));
                        viewHolder.tvAnswerResult.setTextColor(context.getResources().getColor(R.color.wrong_answer));

                        if (amICreator) {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.answer_is_wrong));

                            if (negativePoint > 0)
                                viewHolder.tvAnswerAchievePoints.setText(String.format("Got -%s out of %s", negativePoint, points));
                            else
                                viewHolder.tvAnswerAchievePoints.setText(String.format("Got 0 out of %s", points));

                        } else {
                            viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_wrong));

                            if (negativePoint > 0)
                                viewHolder.tvAnswerAchievePoints.setText(String.format("You have got -%s out of %s", negativePoint, points));
                            else
                                viewHolder.tvAnswerAchievePoints.setText(String.format("You have got 0 out of %s", points));
                        }


                        if (TextUtils.isEmpty(correctAnswer))
                            viewHolder.rlAnswerIsNotFound.setVisibility(View.VISIBLE);
                        else {
                            viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                            viewHolder.tvCorrectAnswer.setText(correctAnswer);
                        }


                    }
                };

                FullResultFillInTheBlanksAdapter fillInTheBlanksAdapter = new FullResultFillInTheBlanksAdapter(context, fillInTheBlanks, fillInTheBlanksCorrectAnswerFindListener);
                viewHolder.rvFillBlankAnswers.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvFillBlankAnswers.setAdapter(fillInTheBlanksAdapter);
                viewHolder.rvFillBlankAnswers.setNestedScrollingEnabled(false);
            }
            break;

            case 3: { //QUESTION_TYPE_SHORT_ANSWER
                ShortAnswerViewHolder viewHolder = (ShortAnswerViewHolder) holder;
                ShortAnswer shortAnswer = questionList.get(position).getShortAnswer();
                Gson gson = new Gson();
                Log.d(TAG, "onBindViewHolder: shortAnswer" + gson.toJson(shortAnswer));
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(shortAnswer.getQuestionTitle());
                if (!TextUtils.isEmpty(shortAnswer.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(shortAnswer.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }

                // set ParticipantsAnswer
                Log.d(TAG, "onBindViewHolder: Participants answer: " + shortAnswer.getParticipantsAnswer());
                viewHolder.tvAnswer.setText(shortAnswer.getParticipantsAnswer());

                if (shortAnswer.isCorrect()) {

                    if (amICreator) {
                        viewHolder.tvAnswerAchievePoints.setText(String.format("Got %s out of %s", shortAnswer.getGivenPoints(), shortAnswer.getPoints()));
                    } else {

                        viewHolder.tvAnswerAchievePoints.setText(String.format("You have got %s out of %s", shortAnswer.getGivenPoints(), shortAnswer.getPoints()));
                    }

                    viewHolder.rlAnswerIsNotFound.setVisibility(View.GONE);
                    viewHolder.llCorrectAnswer.setVisibility(View.GONE);
                } else {
                    viewHolder.tvAnswerResult.setText(context.getString(R.string.your_answer_is_wrong));
                    viewHolder.tvAnswerResult.setTextColor(context.getResources().getColor(R.color.wrong_answer));
                    viewHolder.llQuestionEvaluationResult.setBackground(context.getResources().getDrawable(R.drawable.background_wrong_answer_full_result_preview));

                    if (amICreator) {
                        viewHolder.tvAnswerAchievePoints.setText(String.format("Got %s out of %s", shortAnswer.getGivenPoints(), shortAnswer.getPoints()));
                    } else {
                        viewHolder.tvAnswerAchievePoints.setText(String.format("You have got %s out of %s", shortAnswer.getGivenPoints(), shortAnswer.getPoints()));
                    }

                    viewHolder.rlAnswerIsNotFound.setVisibility(View.VISIBLE);
                    viewHolder.llCorrectAnswer.setVisibility(View.GONE);

                    viewHolder.ivInfo.setOnClickListener(v ->
                            Toast.makeText(context, "Correct answer is not given by the Qxm creator.", Toast.LENGTH_SHORT).show());
                }

            }
            break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

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
}

class MultipleChoiceQuestionViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvMultipleOptions;
    public LinearLayoutCompat llQuestionEvaluationResult;
    public AppCompatTextView tvAnswerResult;
    public AppCompatTextView tvAnswerAchievePoints;
    public RelativeLayout rlAnswerIsNotFound;
    public AppCompatImageView ivInfo;
    public LinearLayoutCompat llCorrectAnswer;
    public AppCompatTextView tvCorrectAnswer;

    MultipleChoiceQuestionViewHolder(View itemView) {
        super(itemView);

        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
        llQuestionEvaluationResult = itemView.findViewById(R.id.llQuestionEvaluationResult);
        tvAnswerResult = itemView.findViewById(R.id.tvAnswerResult);
        tvAnswerAchievePoints = itemView.findViewById(R.id.tvAnswerAchievePoints);
        rlAnswerIsNotFound = itemView.findViewById(R.id.rlAnswerIsNotFound);
        ivInfo = itemView.findViewById(R.id.ivInfo);
        llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
    }
}

class FillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvFillBlankAnswers;
    public LinearLayoutCompat llQuestionEvaluationResult;
    public AppCompatTextView tvAnswerResult;
    public AppCompatTextView tvAnswerAchievePoints;
    public RelativeLayout rlAnswerIsNotFound;
    public AppCompatImageView ivInfo;
    public LinearLayoutCompat llCorrectAnswer;
    public AppCompatTextView tvCorrectAnswer;


    FillInTheBlanksViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvFillBlankAnswers = itemView.findViewById(R.id.rvFillBlankAnswers);
        llQuestionEvaluationResult = itemView.findViewById(R.id.llQuestionEvaluationResult);
        tvAnswerResult = itemView.findViewById(R.id.tvAnswerResult);
        tvAnswerAchievePoints = itemView.findViewById(R.id.tvAnswerAchievePoints);
        rlAnswerIsNotFound = itemView.findViewById(R.id.rlAnswerIsNotFound);
        ivInfo = itemView.findViewById(R.id.ivInfo);
        llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);

    }
}

class ShortAnswerViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public AppCompatTextView tvAnswer;
    public LinearLayoutCompat llQuestionEvaluationResult;
    public AppCompatTextView tvAnswerResult;
    public AppCompatTextView tvAnswerAchievePoints;
    public RelativeLayout rlAnswerIsNotFound;
    public AppCompatImageView ivInfo;
    public LinearLayoutCompat llCorrectAnswer;
    public AppCompatTextView tvCorrectAnswer;

    ShortAnswerViewHolder(View itemView) {
        super(itemView);

        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        tvAnswer = itemView.findViewById(R.id.tvAnswer);
        llQuestionEvaluationResult = itemView.findViewById(R.id.llQuestionEvaluationResult);
        tvAnswerResult = itemView.findViewById(R.id.tvAnswerResult);
        tvAnswerAchievePoints = itemView.findViewById(R.id.tvAnswerAchievePoints);
        rlAnswerIsNotFound = itemView.findViewById(R.id.rlAnswerIsNotFound);
        ivInfo = itemView.findViewById(R.id.ivInfo);
        llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
    }
}