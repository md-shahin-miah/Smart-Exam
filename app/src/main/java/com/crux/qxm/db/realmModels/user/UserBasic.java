package com.crux.qxm.db.realmModels.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.crux.qxm.utils.StaticValues.FACEBOOK_IMAGE_ROOT;
import static com.crux.qxm.utils.StaticValues.GOOGLE_IMAGE_ROOT;
import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;

public class UserBasic extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("_id")
    private String userId;
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
    @SerializedName("profileImage")
    private String profilePic;
    @SerializedName("isVerified")
    private boolean isVerified;
    @SerializedName("accountProvider")
    private String accountProvider;

    public UserBasic() {
    }

    public UserBasic(String userId, String fullName, String profilePic) {
        this.userId = userId;
        this.fullName = fullName;
        this.profilePic = profilePic;
    }

    public UserBasic(String userId, String firstName, String lastName, String fullName, String userName, String email, String profilePic, boolean isVerified) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.profilePic = profilePic;
        this.isVerified = isVerified;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProfilePic() {

        return profilePic;
    }

    public String getModifiedProfileImage(){
        if(profilePic!=null){
            // as image might be from facebook or google as through social login
            if (profilePic.contains("facebook") || profilePic.contains("googleusercontent"))
                return profilePic;

            return IMAGE_SERVER_ROOT + profilePic;
        }
        return "";
    }

    public void setProfilePic(String profilePic) {


        this.profilePic = profilePic;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getAccountProvider() {
        return accountProvider;
    }

    public void setAccountProvider(String accountProvider) {
        this.accountProvider = accountProvider;
    }

    public String getModifiedURLProfileImage() {

        if (profilePic != null && !profilePic.isEmpty() && !profilePic.contains(YOUTUBE_IMAGE_ROOT)
                && !profilePic.contains(FACEBOOK_IMAGE_ROOT) && !profilePic.contains(GOOGLE_IMAGE_ROOT))
            return IMAGE_SERVER_ROOT + profilePic;
        return profilePic;
    }

    @Override
    public String toString() {
        return "UserBasic{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", isVerified=" + isVerified +
                ", accountProvider='" + accountProvider + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.fullName);
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.profilePic);
        dest.writeByte(this.isVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.accountProvider);
    }

    protected UserBasic(Parcel in) {
        this.userId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.fullName = in.readString();
        this.userName = in.readString();
        this.email = in.readString();
        this.profilePic = in.readString();
        this.isVerified = in.readByte() != 0;
        this.accountProvider = in.readString();
    }

    public static final Parcelable.Creator<UserBasic> CREATOR = new Parcelable.Creator<UserBasic>() {
        @Override
        public UserBasic createFromParcel(Parcel source) {
            return new UserBasic(source);
        }

        @Override
        public UserBasic[] newArray(int size) {
            return new UserBasic[size];
        }
    };
}
