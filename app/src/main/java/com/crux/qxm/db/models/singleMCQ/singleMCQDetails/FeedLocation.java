package com.crux.qxm.db.models.singleMCQ.singleMCQDetails;

public class FeedLocation{
	private String type;

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"FeedLocation{" + 
			"type = '" + type + '\'' + 
			"}";
		}
}
