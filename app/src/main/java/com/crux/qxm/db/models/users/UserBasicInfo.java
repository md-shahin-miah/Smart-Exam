package com.crux.qxm.db.models.users;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by frshafi on 2/16/18.
 */

public class UserBasicInfo {

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("userName")
    private String userName;
    @SerializedName("email")
    private String email;
    @SerializedName("fbImage")
    private String fbImage;
    @SerializedName("bio")
    private String bio;
    @SerializedName("description")
    private String description;
    @SerializedName("addresses")
    private List<Address> addressList;

    // constructor with params
    public UserBasicInfo(String firstName, String lastName, String fullName, String userName, String email, String fbImage, String bio, String description,List<Address> addressList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.fbImage = fbImage;
        this.bio = bio;
        this.description = description;
        this.addressList = addressList;
    }

    // empty constructor
    public UserBasicInfo(){

        firstName=" ";
        lastName=" ";
        fullName=" ";
        userName=" ";
        email=" ";
        fbImage=" ";
        bio=" ";
        description=" ";


    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbImage() {
        return fbImage;
    }

    public void setFbImage(String fbImage) {
        this.fbImage = fbImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @Override
    public String toString() {
        return "firstName: "+firstName+" lastName: "+lastName+" fullName: "+fullName+" userName: "+userName+" fbImage: "+fbImage+
                " bio: "+bio+" description: "+description +"addresses: "+addressList;
    }
}
