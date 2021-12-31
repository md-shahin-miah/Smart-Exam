package com.crux.qxm.utils;

public interface FullResultCorrectAnswerFindListener {

    void  onPartialCorrectAnswerFound(String points,String achievePoints);
    void onCorrectAnswerFound(String points);
    void onWrongAnswerFound(String correctAnswer, String points);
}
