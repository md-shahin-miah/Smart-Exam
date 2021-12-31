package com.crux.qxm.db.sampleModels;

import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.ShortAnswer;

import java.util.ArrayList;

public class CreateQxmModel {



    public static ArrayList<Object> getDummyQuestion(){

        ArrayList<Object> questionArrayList = new ArrayList<>();

        // dummy multiple choice
        MultipleChoice multipleChoice = new MultipleChoice();
        multipleChoice.setQuestionTitle("This is a dummy multiple choice question");

        ArrayList<String> options = new ArrayList<>();
        options.add("dummy option 1");
        options.add("dummy option 2");
        options.add("dummy option 3");
        options.add("dummy option 4");

        multipleChoice.setOptions(options);
        questionArrayList.add(multipleChoice);

        // dummy fill in the blanks
        FillInTheBlanks fillInTheBlanks = new FillInTheBlanks();
        fillInTheBlanks.setQuestionTitle("This is a dummy fill in the blanks question");

        ArrayList<String> answers = new ArrayList<>();
        answers.add("dummy answer 1");
        answers.add("dummy answer 2");
        answers.add("dummy answer 3");
        answers.add("dummy answer 4");
        fillInTheBlanks.setCorrectAnswers(answers);

        questionArrayList.add(fillInTheBlanks);


        // dummy short answer
        ShortAnswer shortAnswer = new ShortAnswer();
        shortAnswer.setQuestionTitle("This is a dummy short answer question");
        questionArrayList.add(shortAnswer);



        return questionArrayList;
    }



}
