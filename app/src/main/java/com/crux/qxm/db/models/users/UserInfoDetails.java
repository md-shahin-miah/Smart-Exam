package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoDetails implements Parcelable {

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
    @SerializedName("gender")
    private String gender;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("dateOfBirthPrivacy")
    String dateOfBirthPrivacy;
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
    private SocialSite socialSite;
    @SerializedName("occupation")
    private List<Occupation> occupationList;
    @SerializedName("interest")
    private List<String> interestList;
    @SerializedName("language")
    private List<String> languageList;

    public UserInfoDetails() {
    }

    public UserInfoDetails(String firstName, String lastName, String fullName, String userName, String email, String fbImage, String bio, String description, List<Address> addressesList, List<Institution> institutionList, List<String> websiteList, SocialSite socialSite, List<Occupation> occupationList, List<String> interestList, List<String> languageList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.fbImage = fbImage;
        this.bio = bio;
        this.description = description;
        this.addressesList = addressesList;
        this.institutionList = institutionList;
        this.websiteList = websiteList;
        this.socialSite = socialSite;
        this.occupationList = occupationList;
        this.interestList = interestList;
        this.languageList = languageList;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirthPrivacy() {
        return dateOfBirthPrivacy;
    }

    public void setDateOfBirthPrivacy(String dateOfBirthPrivacy) {
        this.dateOfBirthPrivacy = dateOfBirthPrivacy;
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

    public List<Address> getAddressesList() {
        return addressesList;
    }

    public void setAddressesList(List<Address> addressesList) {
        this.addressesList = addressesList;
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

    public SocialSite getSocialSite() {
        return socialSite;
    }

    public void setSocialSite(SocialSite socialSite) {
        this.socialSite = socialSite;
    }

    public List<Occupation> getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(List<Occupation> occupationList) {
        this.occupationList = occupationList;
    }

    public List<String> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<String> interestList) {
        this.interestList = interestList;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    @Override
    public String toString() {
        return "UserInfoDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", dateOfBirthPrivacy='" + dateOfBirthPrivacy + '\'' +
                ", fbImage='" + fbImage + '\'' +
                ", bio='" + bio + '\'' +
                ", description='" + description + '\'' +
                ", addressesList=" + addressesList +
                ", institutionList=" + institutionList +
                ", websiteList=" + websiteList +
                ", socialSite=" + socialSite +
                ", occupationList=" + occupationList +
                ", interestList=" + interestList +
                ", languageList=" + languageList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.fullName);
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.dateOfBirthPrivacy);
        dest.writeString(this.fbImage);
        dest.writeString(this.bio);
        dest.writeString(this.description);
        dest.writeTypedList(this.addressesList);
        dest.writeTypedList(this.institutionList);
        dest.writeStringList(this.websiteList);
        dest.writeParcelable(this.socialSite, flags);
        dest.writeTypedList(this.occupationList);
        dest.writeStringList(this.interestList);
        dest.writeStringList(this.languageList);
    }

    protected UserInfoDetails(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.fullName = in.readString();
        this.userName = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.dateOfBirth = in.readString();
        this.dateOfBirthPrivacy = in.readString();
        this.fbImage = in.readString();
        this.bio = in.readString();
        this.description = in.readString();
        this.addressesList = in.createTypedArrayList(Address.CREATOR);
        this.institutionList = in.createTypedArrayList(Institution.CREATOR);
        this.websiteList = in.createStringArrayList();
        this.socialSite = in.readParcelable(SocialSite.class.getClassLoader());
        this.occupationList = in.createTypedArrayList(Occupation.CREATOR);
        this.interestList = in.createStringArrayList();
        this.languageList = in.createStringArrayList();
    }

    public static final Creator<UserInfoDetails> CREATOR = new Creator<UserInfoDetails>() {
        @Override
        public UserInfoDetails createFromParcel(Parcel source) {
            return new UserInfoDetails(source);
        }

        @Override
        public UserInfoDetails[] newArray(int size) {
            return new UserInfoDetails[size];
        }
    };
}
