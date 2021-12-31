package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class SingleMCQ {

	@SerializedName("questionTitle")
	private String questionTitle;

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	@Override
	public String toString() {
		return "SingleMCQ{" +
				"questionTitle='" + questionTitle + '\'' +
				'}';
	}
}