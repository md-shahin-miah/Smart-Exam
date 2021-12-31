package com.crux.qxm.db.models.profile;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class EmailUpdateApiResponse implements Parcelable {

	@SerializedName("data")
	private Data data;

	@SerializedName("token")
	private String token;

	@SerializedName("refreshToken")
	private String refreshToken;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken(){
		return refreshToken;
	}

	@Override
 	public String toString(){
		return 
			"EmailUpdateApiResponse{" + 
			"data = '" + data + '\'' + 
			",token = '" + token + '\'' + 
			",refreshToken = '" + refreshToken + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.data, flags);
		dest.writeString(this.token);
		dest.writeString(this.refreshToken);
	}

	public EmailUpdateApiResponse() {
	}

	protected EmailUpdateApiResponse(Parcel in) {
		this.data = in.readParcelable(Data.class.getClassLoader());
		this.token = in.readString();
		this.refreshToken = in.readString();
	}

	public static final Parcelable.Creator<EmailUpdateApiResponse> CREATOR = new Parcelable.Creator<EmailUpdateApiResponse>() {
		@Override
		public EmailUpdateApiResponse createFromParcel(Parcel source) {
			return new EmailUpdateApiResponse(source);
		}

		@Override
		public EmailUpdateApiResponse[] newArray(int size) {
			return new EmailUpdateApiResponse[size];
		}
	};
}