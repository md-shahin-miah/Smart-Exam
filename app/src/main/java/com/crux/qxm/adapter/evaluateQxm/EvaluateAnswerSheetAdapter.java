package com.crux.qxm.adapter.evaluateQxm;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
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
import com.crux.qxm.utils.EvaluationFillInTheBlanksClickListener;
import com.crux.qxm.utils.EvaluationMultipleChoiceListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;

public class EvaluateAnswerSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "EvaluateAnswerSheetAdap";
    private Context context;
    private Picasso picasso;
    private List<Question> questionList;
    private EvaluationFillInTheBlanksClickListener evaluationFillInTheBlanksClickListener;
    private EvaluationMultipleChoiceListener evaluationMultipleChoiceClickListener;


    public EvaluateAnswerSheetAdapter(Context context, Picasso picasso, List<Question> questionList) {
        this.context = context;
        this.picasso = picasso;
        this.questionList = questionList;

        evaluationFillInTheBlanksClickListener = new EvaluationFillInTheBlanksClickListener() {
            @Override
            public void onCorrectAnswerClicked(View itemView) {
                Log.d(TAG, "onCorrectAnswerClicked: in EvaluateAnswerSheetAdapter");
                View rootView = itemView.getRootView();
                rootView.findViewById(R.id.viewEvaluationIndicatorFillInTheBlanks)
                        .setBackground(rootView.getContext().getResources()
                                .getDrawable(R.drawable.background_correct_answer_highlight_bar_active));

            }

            @Override
            public void onWrongAnswerClicked(View itemView) {
                Log.d(TAG, "onWrongAnswerClicked: in EvaluateAnswerSheetAdapter");
                View rootView = itemView.getRootView();
                rootView.findViewById(R.id.viewEvaluationIndicatorFillInTheBlanks)
                        .setBackground(rootView.getContext().getResources()
                                .getDrawable(R.drawable.background_wrong_answer_highlight_bar_active));

            }
        };


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 1) { //QUESTION_TYPE_MULTIPLE_CHOICE
            View view = inflater.inflate(R.layout.rec_evaluation_qxm_multiple_choice_layout, parent, false);
            return new MultipleChoiceQuestionViewHolder(view);

        } else if (viewType == 2) { //QUESTION_TYPE_FILL_IN_THE_BLANKS
            View view = inflater.inflate(R.layout.rec_evaluation_qxm_fill_in_the_blanks_layout, parent, false);
            return new FillInTheBlanksViewHolder(view);

        } else { //QUESTION_TYPE_SHORT_ANSWER
            View view = inflater.inflate(R.layout.rec_evaluation_qxm_short_answer_layout, parent, false);
            return new ShortAnswerViewHolder(view);
        }

    }

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

                evaluationMultipleChoiceClickListener = new EvaluationMultipleChoiceListener() {
                    @Override
                    public void onCorrectAnswerClicked() {
                        Log.d(TAG, "onCorrectAnswerClicked: MultipleChoice");

                        viewHolder.viewIndicatorMultipleChoice.setBackground(context.getResources()
                                .getDrawable(R.drawable.background_correct_answer_highlight_bar_active));
                    }

                    @Override
                    public void onWrongAnswerClicked() {
                        Log.d(TAG, "onWrongAnswerClicked: MultipleChoice");

                        viewHolder.viewIndicatorMultipleChoice.setBackground(context.getResources()
                                .getDrawable(R.drawable.background_wrong_answer_highlight_bar_active));

                    }
                };

                EvaluateMultipleChoiceAdapter multipleChoiceAdapter = new EvaluateMultipleChoiceAdapter(context, multipleChoice, evaluationMultipleChoiceClickListener);
                viewHolder.rvMultipleOptions.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvMultipleOptions.setAdapter(multipleChoiceAdapter);
                viewHolder.rvMultipleOptions.setNestedScrollingEnabled(false);

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

                String[] answerSerialArray = {"(a)", "(b)", "(c)", "(d)", "(e)", "(f)", "(g)", "(h)", "(i)", "(j)"};
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < fillInTheBlanks.getCorrectAnswers().size(); i++) {

                    if (i < fillInTheBlanks.getCorrectAnswers().size() - 1)
                        stringBuilder.append(answerSerialArray[i]).append(" ").append(fillInTheBlanks.getCorrectAnswers().get(i)).append(", ");
                    else
                        stringBuilder.append(answerSerialArray[i]).append(" ").append(fillInTheBlanks.getCorrectAnswers().get(i));
                }
                viewHolder.tvFillBlankReferenceAnswer.setText(stringBuilder);

                EvaluateFillInTheBlanksAdapter fillInTheBlanksAdapter = new EvaluateFillInTheBlanksAdapter(context, fillInTheBlanks, evaluationFillInTheBlanksClickListener);
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
                viewHolder.tvPoint.setText(String.format("Point: %s", shortAnswer.getPoints()));
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

                viewHolder.etGivenPoints.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {

                            double givenPoints = Double.parseDouble(s.toString());
                            double questionPoints = 0.0;

                            if (!TextUtils.isEmpty(shortAnswer.getPoints())) {
                                Double.parseDouble(shortAnswer.getPoints());
                            }

                            if (givenPoints < -questionPoints && givenPoints > questionPoints) {
                                //show warning, given points is greater than question points
                                Log.d(TAG, "Invalid given points");
                                Toast.makeText(context, "Invalid given points. Given points must less or equal to Question point.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "GivenPoints set : " + givenPoints);
                                if (givenPoints > 0.0)
                                    shortAnswer.setCorrect(true);
                                else
                                    shortAnswer.setCorrect(false);

                                shortAnswer.setGivenPoints(String.valueOf(givenPoints));
                            }
                        }
                    }
                });

                viewHolder.llCorrectAnswer.setOnClickListener(v -> {

                    if (!TextUtils.isEmpty(viewHolder.etGivenPoints.getText())) {
                        int actualPoint = Integer.parseInt(shortAnswer.getPoints());
                        int givenPoint = Integer.parseInt(Objects.requireNonNull(viewHolder.etGivenPoints.getText()).toString());

                        if (givenPoint > actualPoint) {
                            Toast.makeText(context, context.getString(R.string.toast_given_point_exceeded),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Correct Answer");
                            shortAnswer.setCorrect(true);
                            shortAnswer.setGivenPoints(String.valueOf(givenPoint));
                            // viewHolder.etGivenPoints.setText(shortAnswer.getPoints());
                            viewHolder.tvAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
                            viewHolder.viewEvaluationIndicator.setBackground(context.getResources().getDrawable(R.drawable.background_correct_answer_highlight_bar_active));
                        }
                    } else {
                        shortAnswer.setCorrect(true);
                        shortAnswer.setGivenPoints(shortAnswer.getPoints());
                        viewHolder.etGivenPoints.setText(shortAnswer.getPoints());
                        viewHolder.tvAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_correct_answer));
                        viewHolder.viewEvaluationIndicator.setBackground(context.getResources().getDrawable(R.drawable.background_correct_answer_highlight_bar_active));
                        Toast.makeText(context, context.getString(R.string.actual_point_is_given), Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.llWrongAnswer.setOnClickListener(v -> {
                    shortAnswer.setCorrect(true);
                    shortAnswer.setGivenPoints("0");
                    viewHolder.etGivenPoints.setText("0");
                    viewHolder.tvAnswer.setTextColor(context.getResources().getColor(R.color.btn_background_color_wrong_answer));
                    viewHolder.viewEvaluationIndicator.setBackground(context.getResources().getDrawable(R.drawable.background_wrong_answer_highlight_bar_active));
                });
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
        if (questionList.get(position).getQuestionType().equals(QUESTION_TYPE_MULTIPLE_CHOICE)) {
            return 1;
        } else if (questionList.get(position).getQuestionType().equals(QUESTION_TYPE_FILL_IN_THE_BLANKS)) {
            return 2;
        } else if (questionList.get(position).getQuestionType().equals(QUESTION_TYPE_SHORT_ANSWER)) {
            return 3;
        }
        return 0;
    }
}

class MultipleChoiceQuestionViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvMultipleOptions;
    public View viewIndicatorMultipleChoice;


    public MultipleChoiceQuestionViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
        viewIndicatorMultipleChoice = itemView.findViewById(R.id.viewEvaluationIndicatorMultipleChoice);
    }
}

class FillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvFillBlankAnswers;
    public View viewIndicatorFillInTheBlanks;
    public AppCompatTextView tvFillBlankReferenceAnswer;


    public FillInTheBlanksViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvFillBlankAnswers = itemView.findViewById(R.id.rvFillBlankAnswers);
        viewIndicatorFillInTheBlanks = itemView.findViewById(R.id.viewEvaluationIndicatorFillInTheBlanks);
        tvFillBlankReferenceAnswer = itemView.findViewById(R.id.tvFillBlankReferenceAnswer);

    }
}

class ShortAnswerViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public AppCompatTextView tvAnswer;
    public AppCompatEditText etGivenPoints;
    public View viewEvaluationIndicator;
    public LinearLayoutCompat llCorrectAnswer;
    public LinearLayoutCompat llWrongAnswer;

    public ShortAnswerViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        tvAnswer = itemView.findViewById(R.id.tvAnswer);
        etGivenPoints = itemView.findViewById(R.id.etGivenPoints);
        viewEvaluationIndicator = itemView.findViewById(R.id.viewEvaluationIndicator);
        llCorrectAnswer = itemView.findViewById(R.id.llCorrectAnswer);
        llWrongAnswer = itemView.findViewById(R.id.llWrongAnswer);
    }
}