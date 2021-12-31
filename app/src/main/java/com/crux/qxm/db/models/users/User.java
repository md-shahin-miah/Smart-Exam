package com.crux.qxm.db.models.users;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by frshafi on 2/14/18.
 */

public class User {


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
    private List<Address> addressesList;
    @SerializedName("education")
    private List<Institution> institutionList;
    @SerializedName("website")
    private List<String> websiteList;
    @SerializedName("socialSite")
    private List<SocialSite> socialSiteList;
    @SerializedName("occupation")
    private List<String> occupationList;
    @SerializedName("interest")
    private List<String> interestList;

    public User(){

        firstName="";
        lastName="";
        fullName="";
        userName="";
        email="";
        fbImage="";
        bio="";
        description="";
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

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
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

    public List<Institution> getInstitutionList() {
        return institutionList;
    }

    public void setInstitutionList(List<Institution> institutionList) {
        this.institutionList = institutionList;
    }

    public List<String> getWebsiteList() {
        return websiteList;
    }

    public void setWebsiteList(List<String> websiteList) {
        this.websiteList = websiteList;
    }

    public List<SocialSite> getSocialSiteList() {
        return socialSiteList;
    }

    public void setSocialSiteList(List<SocialSite> socialSiteList) {
        this.socialSiteList = socialSiteList;
    }

    public List<String> getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(List<String> occupationList) {
        this.occupationList = occupationList;
    }

    public List<String> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<String> interestList) {
        this.interestList = interestList;
    }

    public List<Address> getAddressesList() {
        return addressesList;
    }

    public void setAddressesList(List<Address> addressesList) {
        this.addressesList = addressesList;
    }

    @Override
    public String toString() {
        return "firstName: "+firstName+" lastName: "+lastName+" fullName: "+fullName+" username: "+userName+" fbImage: "+fbImage+
                " bio: "+bio+" description: "+description+" addresses: "+addressesList+" education: "+institutionList+" website :"+websiteList+" socialSite: "+
                socialSiteList+" occupation: "+occupationList+" interest: "+interestList+" addresses "+addressesList;
    }
}
