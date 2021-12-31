package com.crux.qxm.db.models.myQxmResult;

import com.google.gson.annotations.SerializedName;

public class UserPerformance{

	@SerializedName("performance")
	private Performance performance;

	@SerializedName("myPosition")
	private int myPosition;

	public void setPerformance(Performance performance){
		this.performance = performance;
	}

	public Performance getPerformance(){
		return performance;
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
			"UserPerformance{" + 
			"performance = '" + performance + '\'' + 
			",myPosition = '" + myPosition + '\'' + 
			"}";
		}
}