package com.crux.qxm.db.models.users;

import com.google.gson.annotations.SerializedName;

public class LoggedInDevice{

	@SerializedName("country")
	private String country;

	@SerializedName("macAddress")
	private String macAddress;

	@SerializedName("city")
	private String city;

	@SerializedName("ip")
	private String ip;

	@SerializedName("deviceModel")
	private String deviceModel;

	@SerializedName("_id")
	private String id;

	@SerializedName("deviceBrand")
	private String deviceBrand;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setMacAddress(String macAddress){
		this.macAddress = macAddress;
	}

	public String getMacAddress(){
		return macAddress;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return ip;
	}

	public void setDeviceModel(String deviceModel){
		this.deviceModel = deviceModel;
	}

	public String getDeviceModel(){
		return deviceModel;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDeviceBrand(String deviceBrand){
		this.deviceBrand = deviceBrand;
	}

	public String getDeviceBrand(){
		return deviceBrand;
	}

	@Override
 	public String toString(){
		return 
			"LoggedInDevice{" + 
			"country = '" + country + '\'' + 
			",macAddress = '" + macAddress + '\'' + 
			",city = '" + city + '\'' + 
			",ip = '" + ip + '\'' + 
			",deviceModel = '" + deviceModel + '\'' + 
			",_id = '" + id + '\'' + 
			",deviceBrand = '" + deviceBrand + '\'' + 
			"}";
		}
}