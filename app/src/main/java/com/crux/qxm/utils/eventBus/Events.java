package com.crux.qxm.utils.eventBus;

public class Events {

    //region class OnPhotoUploadMessage

    // Event used to send message if photo uploaded or not
    public static class OnPhotoUploadMessage {
        private String localImageName;
        private String sentImageURL;
        private String message;
        private String photoUploadReason;
        private int position;

        public OnPhotoUploadMessage(String localImageName, String sentImageURL, int position,String message) {
            this.localImageName = localImageName;
            this.sentImageURL = sentImageURL;
            this.position = position;
            this.message = message;
        }

        public OnPhotoUploadMessage(String localImageName, String sentImageURL,String message){

            this.localImageName = localImageName;
            this.sentImageURL  = sentImageURL;
            this.message = message;
        }

        public OnPhotoUploadMessage(String localImageName, String sentImageURL,String message,String photoUploadReason){

            this.localImageName = localImageName;
            this.sentImageURL  = sentImageURL;
            this.message = message;
            this.photoUploadReason = photoUploadReason;
        }

        public OnPhotoUploadMessage(String message){
            this.message = message;
        }

        public OnPhotoUploadMessage(String message,String photoUploadReason){
            this.message = message;
            this.photoUploadReason = photoUploadReason;
        }

        public String getLocalImageName() {
            return localImageName;
        }

        public void setLocalImageName(String localImageName) {
            this.localImageName = localImageName;
        }

        public String getSentImageURL() {
            return sentImageURL;
        }

        public void setSentImageURL(String sentImageURL) {
            this.sentImageURL = sentImageURL;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "OnPhotoUploadMessage{" +
                    "localImageName='" + localImageName + '\'' +
                    ", sentImageURL='" + sentImageURL + '\'' +
                    ", message='" + message + '\'' +
                    ", position=" + position +
                    '}';
        }
    }
    //endregion

    //region class VariousMessages
    public static class VariousMessages {

        private String message;
        public VariousMessages(String message){
            this.message =  message;
        }
        public String getMessage() {
            return message;
        }
    }
    //endregion

    //region class KeyboardVisibilityEvent
    public static class KeyboardVisibilityEvent{

        boolean isKeyBoardOpen;

        public KeyboardVisibilityEvent(boolean isKeyBoardOpen){
            this.isKeyBoardOpen = isKeyBoardOpen;
        }

        public boolean isKeyBoardOpen() {
            return isKeyBoardOpen;
        }

        public void setKeyBoardOpen(boolean keyBoardOpen) {
            isKeyBoardOpen = keyBoardOpen;
        }
    }
    //endregion

    //region class OnPhotoUploadMessageQSetThumbnail

    public static class OnPhotoUploadMessageQSetThumbnail{

        private String localImageName;
        private String sentImageURL;
        private String message;
        private String photoUploadReason;

        public OnPhotoUploadMessageQSetThumbnail(String localImageName, String sentImageURL, String message, String photoUploadReason) {
            this.localImageName = localImageName;
            this.sentImageURL = sentImageURL;
            this.message = message;
            this.photoUploadReason = photoUploadReason;
        }

        public OnPhotoUploadMessageQSetThumbnail(String message) {
            this.message = message;
        }

        public String getLocalImageName() {
            return localImageName;
        }

        public void setLocalImageName(String localImageName) {
            this.localImageName = localImageName;
        }

        public String getSentImageURL() {
            return sentImageURL;
        }

        public void setSentImageURL(String sentImageURL) {
            this.sentImageURL = sentImageURL;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPhotoUploadReason() {
            return photoUploadReason;
        }

        public void setPhotoUploadReason(String photoUploadReason) {
            this.photoUploadReason = photoUploadReason;
        }


        @Override
        public String toString() {
            return "OnPhotoUploadMessageQSetThumbnail{" +
                    "localImageName='" + localImageName + '\'' +
                    ", sentImageURL='" + sentImageURL + '\'' +
                    ", message='" + message + '\'' +
                    ", photoUploadReason='" + photoUploadReason + '\'' +
                    '}';
        }
    }

    //endregion





}
