package com.crux.qxm.db.sampleModels;

import com.crux.qxm.db.models.questions.QuestionSettings;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.ContestMode;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.LeaderboardSettings;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmCategory;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmSettings;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.RealmList;

import static com.crux.qxm.utils.StaticValues.CONTEST_END_WITH_EXAM_DURATION;
import static com.crux.qxm.utils.StaticValues.CONTEST_START_INSTANTLY;
import static com.crux.qxm.utils.StaticValues.CORRECT_ANSWER_VISIBILITY_IMMEDIATELY;
import static com.crux.qxm.utils.StaticValues.EVALUATE_AUTOMATIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.LEADERBOARD_PUBLISH_TYPE_CONTINUE;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_DRAFTED;
import static com.crux.qxm.utils.StaticValues.QUESTION_VISIBILITY_AFTER_PARTICIPATION;
import static com.crux.qxm.utils.StaticValues.QXM_CURRENT_STATUS_RUNNING;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.QXM_START_INSTANTLY;
import static com.crux.qxm.utils.StaticValues.QXM_TIME_TYPE_NO_LIMIT;

public class QxmSettingsModel {

    public static QxmSettings getDummyQxmSettings() {

        ArrayList<String> winnerSelectionPriorityRemote = new ArrayList<>();
        winnerSelectionPriorityRemote.add("marks");
        winnerSelectionPriorityRemote.add("quick_submission");
        winnerSelectionPriorityRemote.add("raffle_draw");

        RealmList<String> winnerSelectionPriority = new RealmList<>();
        winnerSelectionPriority.add("marks");
        winnerSelectionPriority.add("quick_submission");
        winnerSelectionPriority.add("raffle_draw");


        ContestMode contestMode = new ContestMode(false, CONTEST_START_INSTANTLY, -1,
                CONTEST_END_WITH_EXAM_DURATION, -1, 5, false, winnerSelectionPriorityRemote, winnerSelectionPriority, false);

        LeaderboardSettings leaderboardSettings = new LeaderboardSettings("continue", -1, "public");

        ArrayList<String> category = new ArrayList<>();
        RealmList<QxmCategory> qxmCategory = new RealmList<>();


        return new QxmSettings("draft", category, qxmCategory, "no_time_limit",
                -1, "public", "after_participation", "instantly",
                -1, QXM_CURRENT_STATUS_RUNNING, "automatic", true, 0.5f,
                false, -1, "immediately", -1,
                -1, false, -1, -1, contestMode, leaderboardSettings);
    }


    public static QxmSettings getDefaultSettings() {
        ArrayList<String> winnerSelectionPriorityRemote = new ArrayList<>();
        winnerSelectionPriorityRemote.add("marks");
        winnerSelectionPriorityRemote.add("quick_submission");
        winnerSelectionPriorityRemote.add("raffle_draw");


        RealmList<String> winnerSelectionPriority = new RealmList<>();
        winnerSelectionPriority.add("marks");
        winnerSelectionPriority.add("quick_submission");
        winnerSelectionPriority.add("raffle_draw");


        ContestMode contestMode = new ContestMode(false, CONTEST_START_INSTANTLY, -1,
                CONTEST_END_WITH_EXAM_DURATION, -1, 5, false, winnerSelectionPriorityRemote, winnerSelectionPriority, false);

        LeaderboardSettings leaderboardSettings = new LeaderboardSettings("continue", -1, "public");

        ArrayList<String> category = new ArrayList<>();
        RealmList<QxmCategory> qxmCategory = new RealmList<>();


        return new QxmSettings("draft", category, qxmCategory, "no_time_limit",
                -1, "public", "after_participation", "instantly",
                -1, QXM_CURRENT_STATUS_RUNNING, "automatic", false, 1.0f,
                false, -1, "immediately", -1,
                Calendar.getInstance().getTimeInMillis(), false, -1, -1, contestMode, leaderboardSettings);
    }

    public static QuestionSettings getDefaultQuestionSettings() {
        ArrayList<String> winnerSelectionPriorityRules = new ArrayList<>();
        winnerSelectionPriorityRules.add("marks");
        winnerSelectionPriorityRules.add("quick_submission");
        winnerSelectionPriorityRules.add("raffle_draw");

        ArrayList<String> category = new ArrayList<>();

        return new QuestionSettings(QUESTION_STATUS_DRAFTED, category, QXM_TIME_TYPE_NO_LIMIT, QXM_PRIVACY_PUBLIC, false,
                QUESTION_VISIBILITY_AFTER_PARTICIPATION, QXM_START_INSTANTLY, QXM_CURRENT_STATUS_RUNNING, EVALUATE_AUTOMATIC, 0.0f, null
                , CORRECT_ANSWER_VISIBILITY_IMMEDIATELY, false, 5, false, winnerSelectionPriorityRules,
                false, LEADERBOARD_PUBLISH_TYPE_CONTINUE, LEADERBOARD_PRIVACY_PUBLIC);
    }

}
