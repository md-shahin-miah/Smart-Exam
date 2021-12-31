package com.crux.qxm.db.sampleModels;

import com.crux.qxm.db.models.singleMCQ.SingleMCQSettings;

import java.util.ArrayList;

import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_PUBLISHED;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_TIME_TYPE_NO_LIMIT;

public class SingleMCQSettingsModel {

    public static SingleMCQSettings getDefaultQuestionSettings() {

        ArrayList<String> category = new ArrayList<>();

        return new SingleMCQSettings(QUESTION_STATUS_PUBLISHED, category, SINGLE_MCQ_TIME_TYPE_NO_LIMIT, CORRECT_ANSWER_VISIBILITY_IMMEDIATELY,
                false, 0, true, false,
                LEADERBOARD_PUBLISH_TYPE_CONTINUE, LEADERBOARD_PRIVACY_PUBLIC);


    }

}
