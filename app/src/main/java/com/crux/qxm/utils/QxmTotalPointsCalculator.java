package com.crux.qxm.utils;

import android.text.TextUtils;

import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QxmModel;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_FILL_IN_THE_BLANKS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_SHORT_ANSWER;

public class QxmTotalPointsCalculator {

    public static String getTotalPoints(List<Question> questionList){

        double totalPoints = 0;

        for (Question question : questionList) {
            switch (question.getQuestionType()) {
                case QUESTION_TYPE_MULTIPLE_CHOICE:
                    if (!TextUtils.isEmpty(question.getMultipleChoice().getPoints()))
                        totalPoints += Double.parseDouble(question.getMultipleChoice().getPoints());
                    break;
                case QUESTION_TYPE_FILL_IN_THE_BLANKS:
                    if (!TextUtils.isEmpty(question.getFillInTheBlanks().getPoints()))
                        totalPoints += Double.parseDouble(question.getFillInTheBlanks().getPoints());
                    break;

                case QUESTION_TYPE_SHORT_ANSWER:
                    if (!TextUtils.isEmpty(question.getShortAnswer().getPoints()))
                        totalPoints += Double.parseDouble(question.getShortAnswer().getPoints());
                    break;
            }
        }

        return String.valueOf(totalPoints);
    }
}
