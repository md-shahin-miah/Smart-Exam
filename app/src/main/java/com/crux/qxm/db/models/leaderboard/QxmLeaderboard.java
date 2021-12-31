package com.crux.qxm.db.models.leaderboard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QxmLeaderboard{

	@SerializedName("leaderBoard")
	private List<LeaderBoardItem> leaderBoard;

	@SerializedName("myInfo")
	private MyInfo myInfo;

	@SerializedName("myPosition")
	private int myPosition;

	public void setLeaderBoard(List<LeaderBoardItem> leaderBoard){
		this.leaderBoard = leaderBoard;
	}

	public List<LeaderBoardItem> getLeaderBoard(){
		return leaderBoard;
	}

	public void setMyInfo(MyInfo myInfo){
		this.myInfo = myInfo;
	}

	public MyInfo getMyInfo(){
		return myInfo;
	}

	public void setMyPosition(int myPosition){
		this.myPosition = myPosition;
	}

	public int getMyPosition(){
		return myPosition;
	}

	@Override
 	public String toString(){
		return 
			"QxmLeaderboard{" + 
			"leaderBoard = '" + leaderBoard + '\'' + 
			",myInfo = '" + myInfo + '\'' + 
			",myPosition = '" + myPosition + '\'' + 
			"}";
		}
}