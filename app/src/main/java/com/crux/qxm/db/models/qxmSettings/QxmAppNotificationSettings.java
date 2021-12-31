package com.crux.qxm.db.models.qxmSettings;

import com.google.gson.annotations.SerializedName;

public class QxmAppNotificationSettings  {

	@SerializedName("pushNotification")
	private boolean pushNotification;

	@SerializedName("evaluationNotification")
	private boolean evaluationNotification;

	@SerializedName("followingNotification")
	private boolean followingNotification;

	@SerializedName("resultNotification")
	private boolean resultNotification;

	@SerializedName("playSound")
	private boolean playSound;

	@SerializedName("vibrate")
	private boolean vibrate;

	@SerializedName("pollNotification")
	private boolean pollNotification;

	@SerializedName("groupNotification")
	private boolean groupNotification;

	@SerializedName("participationNotification")
	private boolean participationNotification;

	public boolean isPushNotification() {
		return pushNotification;
	}

	public void setPushNotification(boolean pushNotification) {
		this.pushNotification = pushNotification;
	}

	public void setEvaluationNotification(boolean evaluationNotification){
		this.evaluationNotification = evaluationNotification;
	}

	public boolean isEvaluationNotification(){
		return evaluationNotification;
	}

	public void setFollowingNotification(boolean followingNotification){
		this.followingNotification = followingNotification;
	}

	public boolean isFollowingNotification(){
		return followingNotification;
	}

	public void setResultNotification(boolean resultNotification){
		this.resultNotification = resultNotification;
	}

	public boolean isResultNotification(){
		return resultNotification;
	}

	public void setPlaySound(boolean playSound){
		this.playSound = playSound;
	}

	public boolean isPlaySound(){
		return playSound;
	}

	public void setVibrate(boolean vibrate){
		this.vibrate = vibrate;
	}

	public boolean isVibrate(){
		return vibrate;
	}

	public void setPollNotification(boolean pollNotification){
		this.pollNotification = pollNotification;
	}

	public boolean isPollNotification(){
		return pollNotification;
	}

	public void setGroupNotification(boolean groupNotification){
		this.groupNotification = groupNotification;
	}

	public boolean isGroupNotification(){
		return groupNotification;
	}

	public void setParticipationNotification(boolean participationNotification){
		this.participationNotification = participationNotification;
	}

	public boolean isParticipationNotification(){
		return participationNotification;
	}

	@Override
 	public String toString(){
		return 
			"QxmAppNotificationSettings{" +
			"evaluationNotification = '" + evaluationNotification + '\'' + 
			",followingNotification = '" + followingNotification + '\'' + 
			",resultNotification = '" + resultNotification + '\'' + 
			",playSound = '" + playSound + '\'' + 
			",vibrate = '" + vibrate + '\'' + 
			",pollNotification = '" + pollNotification + '\'' + 
			",groupNotification = '" + groupNotification + '\'' + 
			",participationNotification = '" + participationNotification + '\'' + 
			"}";
		}
}