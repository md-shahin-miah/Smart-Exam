package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class QuestionSet{

	@SerializedName("questionSetName")
	private String questionSetName;

	public void setQuestionSetName(String questionSetName){
		this.questionSetName = questionSetName;
	}

	public String getQuestionSetName(){
		return questionSetName;
	}

	@Override
 	public String toString(){
		return 
			"QuestionSet{" + 
			"questionSetName = '" + questionSetName + '\'' + 
			"}";
		}
}