package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class ProfileData implements Parcelable {
    @SerializedName("userId")
    private String userId;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("status")
    private String userStatus;
    @SerializedName("profileImage")
    private String userProfileUrl;
    @SerializedName("followerCount")
    private String followerCount;
    @SerializedName("followingCount")
    private String followingCount;
    @SerializedName("groupCount")
    private String groupsCount;
    @SerializedName("qxmCount")
    private String qxmCount;
    @SerializedName("amIFollowingThisUser")
    private boolean amIFollowingThisUser;
    @SerializedName("address")
    private Address address;
    @SerializedName("occupation")
    private Occupation occupation;
    @SerializedName("education")
    private Institution institution;

    public ProfileData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserProfileUrl() {

        if (userProfileUrl != null) {
            // as image might be from facebook or google as through social login
            if (userProfileUrl.contains("facebook") || userProfileUrl.contains("googleusercontent"))
                return userProfileUrl;

            return IMAGE_SERVER_ROOT + userProfileUrl;
        }
        return "";
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getGroupsCount() {
        return groupsCount;
    }

    public void setGroupsCount(String groupsCount) {
        this.groupsCount = groupsCount;
    }

    public String getQxmCount() {
        return qxmCount;
    }

    public void setQxmCount(String qxmCount) {
        this.qxmCount = qxmCount;
    }

    public boolean getAmIFollowingThisUser() {
        return amIFollowingThisUser;
    }

    public void setAmIFollowingThisUser(boolean amIFollowingThisUser) {
        this.amIFollowingThisUser = amIFollowingThisUser;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userProfileUrl='" + userProfileUrl + '\'' +
                ", followerCount='" + followerCount + '\'' +
                ", followingCount='" + followingCount + '\'' +
                ", groupsCount='" + groupsCount + '\'' +
                ", qxmCount='" + qxmCount + '\'' +
                ", amIFollowingThisUser='" + amIFollowingThisUser + '\'' +
                ", address=" + address +
                ", occupation=" + occupation +
                ", institution=" + institution +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.fullName);
        dest.writeString(this.userStatus);
        dest.writeString(this.userProfileUrl);
        dest.writeString(this.followerCount);
        dest.writeString(this.followingCount);
        dest.writeString(this.groupsCount);
        dest.writeString(this.qxmCount);
        dest.writeByte(this.amIFollowingThisUser ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.address, flags);
        dest.writeParcelable(this.occupation, flags);
        dest.writeParcelable(this.institution, flags);
    }

    protected ProfileData(Parcel in) {
        this.userId = in.readString();
        this.fullName = in.readString();
        this.userStatus = in.readString();
        this.userProfileUrl = in.readString();
        this.followerCount = in.readString();
        this.followingCount = in.readString();
        this.groupsCount = in.readString();
        this.qxmCount = in.readString();
        this.amIFollowingThisUser = in.readByte() != 0;
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.occupation = in.readParcelable(Occupation.class.getClassLoader());
        this.institution = in.readParcelable(Institution.class.getClassLoader());
    }

    public static final Creator<ProfileData> CREATOR = new Creator<ProfileData>() {
        @Override
        public ProfileData createFromParcel(Parcel source) {
            return new ProfileData(source);
        }

        @Override
        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };
}
