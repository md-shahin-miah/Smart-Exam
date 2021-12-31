package com.crux.qxm.db.models.enroll;

/**
 * Created by frshafi on 2/7/18.
 */

public class SocialAddress {

    String socialLink;
    SocialType socialType;

    public enum SocialType{

        FACEBOOK,INSTAGRAM,LINKEDIN,YOUTUBE,TWITTER
    }

    public SocialAddress(String socialLink, SocialType socialType){

        this.socialLink = socialLink;
        this.socialType = socialType;
    }

    public SocialAddress(){

    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;

    }

    public SocialType getSocialType() {
        return socialType;
    }

    public void setSocialType(SocialType socialType) {
        this.socialType = socialType;
    }


    @Override
    public String toString() {
        return socialLink;
    }
}
