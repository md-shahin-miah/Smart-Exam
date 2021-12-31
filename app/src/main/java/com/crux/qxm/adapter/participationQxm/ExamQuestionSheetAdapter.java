package com.crux.qxm.adapter.participationQxm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.ShortAnswer;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;

public class ExamQuestionSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ExamQuestionSheetAdapte";
    private Context context;
    private Picasso picasso;
    private List<Question> questionList;
    private String viewFor;

    public ExamQuestionSheetAdapter(Context context, Picasso picasso, List<Question> questionList, String viewFor) {
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

        if (viewType == 1) { //QUESTION_TYPE_MULTIPLE_CHOICE
            View view = inflater.inflate(R.layout.rec_participate_qxm_multiple_choice_layout, parent, false);
            return new MultipleChoiceQuestionViewHolder(view);

        } else if (viewType == 2) { //QUESTION_TYPE_FILL_IN_THE_BLANKS
            View view = inflater.inflate(R.layout.rec_participate_qxm_fill_in_the_blanks_layout, parent, false);
            return new FillInTheBlanksViewHolder(view);

        } else { //QUESTION_TYPE_SHORT_ANSWER
            View view = inflater.inflate(R.layout.rec_participate_qxm_short_answer_layout, parent, false);
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
                ParticipateMultipleChoiceAdapter multipleChoiceAdapter = new ParticipateMultipleChoiceAdapter(context, multipleChoice.getOptions(), multipleChoice.getParticipantsAnswers(), viewFor);
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
                // fill in the blanks ... put empty blanks

//                ArrayList<String> hideCorrectAns = new ArrayList<>(fillInTheBlanks.getCorrectAnswers());
//                Collections.fill(hideCorrectAns.getCorrectAnswers(), "");

                fillInTheBlanks.setParticipantsAnswers(new ArrayList<>());

                while (fillInTheBlanks.getParticipantsAnswers().size() < fillInTheBlanks.getCorrectAnswers().size()) {
                    fillInTheBlanks.getParticipantsAnswers().add("");
                }

                ParticipateFillInTheBlanksAdapter fillInTheBlanksAdapter = new ParticipateFillInTheBlanksAdapter(context, fillInTheBlanks.getCorrectAnswers(), fillInTheBlanks.getParticipantsAnswers(), viewFor);
                viewHolder.rvFillBlankAnswers.setLayoutManager(new LinearLayoutManager(context));
                viewHolder.rvFillBlankAnswers.setAdapter(fillInTheBlanksAdapter);
                viewHolder.rvFillBlankAnswers.setNestedScrollingEnabled(false);
            }
            break;

            case 3: { //QUESTION_TYPE_SHORT_ANSWER
                ShortAnswerViewHolder viewHolder = (ShortAnswerViewHolder) holder;
                ShortAnswer shortAnswer = questionList.get(position).getShortAnswer();
                Log.d(TAG, "onBindViewHolder: ShortAnswer: " + shortAnswer.toString());
                if (viewFor.equals(VIEW_FOR_SEE_QUESTION)) {
                    viewHolder.etAnswer.setEnabled(false);
                }
                viewHolder.tvPoint.setText(String.format("Point: %s", shortAnswer.getPoints()));
                viewHolder.tvQuestionNumber.setText(String.format("Q%s", position + 1));
                viewHolder.tvQuestionTitle.setText(shortAnswer.getQuestionTitle());
                if (!TextUtils.isEmpty(shortAnswer.getImage())) {
                    viewHolder.ivQuestionImage.setVisibility(View.VISIBLE);
                    picasso.load(shortAnswer.getImage()).into(viewHolder.ivQuestionImage);
                } else {
                    viewHolder.ivQuestionImage.setVisibility(View.GONE);
                }
                InputFilter inputFilter;
                if (QxmStringIntegerChecker.isInt(shortAnswer.getWordLimits())) {
                    inputFilter = new InputFilter.LengthFilter(Integer.parseInt(shortAnswer.getWordLimits()));
                }else{
                    inputFilter = new InputFilter.LengthFilter(50);
                }
                viewHolder.etAnswer.setFilters(new InputFilter[]{inputFilter});

                viewHolder.etAnswer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d(TAG, "afterTextChanged: FillInTheBlanks ParticipantsAnswer: " + s.toString());
                        String answer = s.toString();
                        if (QxmStringIntegerChecker.isInt(shortAnswer.getWordLimits())) {
                            if(!TextUtils.isEmpty(answer)){
                                int wordLimit = Integer.parseInt(shortAnswer.getWordLimits());
                                if(answer.length() > wordLimit - 10){
                                    shortAnswer.setParticipantsAnswer(s.toString());
                                    viewHolder.tvRemainingCharacter.setText(String.format("Character Remain: %s", wordLimit - answer.length()));
                                    viewHolder.tvRemainingCharacter.setVisibility(View.VISIBLE);
                                }else if(answer.length() <= wordLimit){
                                    shortAnswer.setParticipantsAnswer(s.toString());
                                    viewHolder.tvRemainingCharacter.setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                });

                viewHolder.tvWordLimit.setText(String.format(context.getString(R.string.character_limit_with_variable), shortAnswer.getWordLimits()));
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


    public MultipleChoiceQuestionViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvMultipleOptions = itemView.findViewById(R.id.rvMultipleOptions);
    }
}

class ShortAnswerViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public AppCompatEditText etAnswer;
    public AppCompatTextView tvWordLimit;
    public AppCompatTextView tvRemainingCharacter;

    public ShortAnswerViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        etAnswer = itemView.findViewById(R.id.etAnswer);
        tvWordLimit = itemView.findViewById(R.id.tvWordLimit);
        tvRemainingCharacter = itemView.findViewById(R.id.tvRemainingCharacter);
    }
}

class FillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvPoint;
    public AppCompatTextView tvQuestionNumber;
    public AppCompatTextView tvQuestionTitle;
    public AppCompatImageView ivQuestionImage;
    public RecyclerView rvFillBlankAnswers;

    public FillInTheBlanksViewHolder(View itemView) {
        super(itemView);

        tvPoint = itemView.findViewById(R.id.tvPoint);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        rvFillBlankAnswers = itemView.findViewById(R.id.rvFillBlankAnswers);
    }
}