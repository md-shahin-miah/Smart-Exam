package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class ParticipatedQxm{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("participationNum")
	private int participationNum;

	@SerializedName("qxmCreatorId")
	private QxmCreatorId qxmCreatorId;

	@SerializedName("creator")
	private QxmCreator creator;

	@SerializedName("questionSet")
	private QuestionSet questionSet;

	@SerializedName("singleMCQ")
	private SingleMCQ singleMCQ;


	@SerializedName("qxmSettings")
	private QxmSettings qxmSettings;

	@SerializedName("singleMCQSettings")
    private SingleMCQSettings singleMCQSettings;

	@SerializedName("_id")
	private String id;

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getParticipationNum() {
		return participationNum;
	}

	public void setParticipationNum(int participationNum) {
		this.participationNum = participationNum;
	}

	public QxmCreatorId getQxmCreatorId() {
		return qxmCreatorId;
	}

	public void setQxmCreatorId(QxmCreatorId qxmCreatorId) {
		this.qxmCreatorId = qxmCreatorId;
	}

	public QxmCreator getCreator() {
		return creator;
	}

	public void setCreator(QxmCreator creator) {
		this.creator = creator;
	}

	public QuestionSet getQuestionSet() {
		return questionSet;
	}

	public void setQuestionSet(QuestionSet questionSet) {
		this.questionSet = questionSet;
	}

	public SingleMCQ getSingleMCQ() {
		return singleMCQ;
	}

	public void setSingleMCQ(SingleMCQ singleMCQ) {
		this.singleMCQ = singleMCQ;
	}

	public QxmSettings getQxmSettings() {
		return qxmSettings;
	}

	public void setQxmSettings(QxmSettings qxmSettings) {
		this.qxmSettings = qxmSettings;
	}

    public SingleMCQSettings getSingleMCQSettings() {
        return singleMCQSettings;
    }

    public void setSingleMCQSettings(SingleMCQSettings singleMCQSettings) {
        this.singleMCQSettings = singleMCQSettings;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @Override
    public String toString() {
        return "ParticipatedQxm{" +
                "createdAt='" + createdAt + '\'' +
                ", participationNum=" + participationNum +
                ", qxmCreatorId=" + qxmCreatorId +
                ", creator=" + creator +
                ", questionSet=" + questionSet +
                ", singleMCQ=" + singleMCQ +
                ", qxmSettings=" + qxmSettings +
                ", singleMCQSettings=" + singleMCQSettings +
                ", id='" + id + '\'' +
                '}';
    }
}