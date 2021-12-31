package com.crux.qxm.networkLayer;

import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.QxmApkUpdateResponse;
import com.crux.qxm.db.models.SignUp.UserSignUp;
import com.crux.qxm.db.models.activityLog.ActivityLog;
import com.crux.qxm.db.models.createQxm.YoutubeModel;
import com.crux.qxm.db.models.draft.DraftedQxm;
import com.crux.qxm.db.models.enroll.peopleEnrolled.PeopleEnrolledData;
import com.crux.qxm.db.models.enroll.receivedEnrollRequest.ReceivedEnrollRequestData;
import com.crux.qxm.db.models.enroll.sentEnrollRequest.SentEnrollRequestData;
import com.crux.qxm.db.models.enroll.youEnrolled.MyEnrolledData;
import com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew.YouParticipatedData;
import com.crux.qxm.db.models.evaluation.EvaluationDetails;
import com.crux.qxm.db.models.evaluation.EvaluationPendingQxm;
import com.crux.qxm.db.models.evaluation.Participator;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.following.FollowingUser;
import com.crux.qxm.db.models.group.Group;
import com.crux.qxm.db.models.group.addMemberToGroup.AddMemberList;
import com.crux.qxm.db.models.group.groupFeed.MyGroupFeed;
import com.crux.qxm.db.models.group.groupInvitations.GroupInvitations;
import com.crux.qxm.db.models.group.groupInvitationsOfUser.GroupInvitationsOfUser;
import com.crux.qxm.db.models.group.groupMemberList.GroupMembers;
import com.crux.qxm.db.models.group.groupPendingRequest.GroupPendingRequestListItem;
import com.crux.qxm.db.models.group.groupSettings.GroupSettingsContainer;
import com.crux.qxm.db.models.leaderboard.QxmLeaderboard;
import com.crux.qxm.db.models.leaderboardSingleMCQ.SingleMCQLeaderboardItem;
import com.crux.qxm.db.models.login.UserLogin;
import com.crux.qxm.db.models.menu.group.MyGroupDataInMenuContainer;
import com.crux.qxm.db.models.myQxm.MyQxmDetails;
import com.crux.qxm.db.models.myQxm.ParticipatorContainer;
import com.crux.qxm.db.models.myQxm.list.ListSettings;
import com.crux.qxm.db.models.myQxmResult.MyQxmResult;
import com.crux.qxm.db.models.notification.EvaluationNotification;
import com.crux.qxm.db.models.notification.ResultNotification;
import com.crux.qxm.db.models.notification.allNotification.AllNotification;
import com.crux.qxm.db.models.notification.groupNotification.GroupNotificationItem;
import com.crux.qxm.db.models.notification.qxmNotification.QxmNotification;
import com.crux.qxm.db.models.poll.PollPostingModel;
import com.crux.qxm.db.models.poll.pollEdit.PollData;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.models.qxmSettings.QxmAppNotificationSettings;
import com.crux.qxm.db.models.qxmSettings.SettingsDataContainer;
import com.crux.qxm.db.models.report.ReportModel;
import com.crux.qxm.db.models.response.UploadedImageLink;
import com.crux.qxm.db.models.response.UserAuthenticationResponse;
import com.crux.qxm.db.models.response.UserProfileImageResponse;
import com.crux.qxm.db.models.result.FullResult;
import com.crux.qxm.db.models.search.group.SearchedGroup;
import com.crux.qxm.db.models.search.group.SearchedGroupContainer;
import com.crux.qxm.db.models.search.list.SearchedList;
import com.crux.qxm.db.models.search.poll.SearchedPollContainer;
import com.crux.qxm.db.models.search.qxm.SearchedQxmContainer;
import com.crux.qxm.db.models.search.singleMCQ.SearchedSingleMcqContainer;
import com.crux.qxm.db.models.search.user.SearchedUserContainer;
import com.crux.qxm.db.models.singleMCQ.FullResultSingleMCQ;
import com.crux.qxm.db.models.singleMCQ.ParticipateSingleMCQModel;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.models.singleMCQ.singleMCQDetails.SingleMCQDetails;
import com.crux.qxm.db.models.userFeedback.UserFeedback;
import com.crux.qxm.db.models.users.LoggedInDevice;
import com.crux.qxm.db.models.users.UserInfo;
import com.crux.qxm.db.models.users.UserInfoDetails;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmModels.user.User;
import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.crux.qxm.utils.StaticValues.AUTHORIZATION;

public interface QxmApiService {


    // region FacebookLogin

    //qxm token generate
    @POST("/users/token/gen/facebookToken")
    @FormUrlEncoded
    Call<UserAuthenticationResponse> genTokenForFacebookLogin(@Field("fbToken") String fbToken);

    //Facebook login
    @POST("/users/auth/facebook")
    Call<UserAuthenticationResponse> facebookLogin(@Header(AUTHORIZATION) String token,
                                                   @Body UserLogin userLogin);

    // endregion

    // region GoogleLogin

    //qxm token generate
    @POST("/users/token/gen/googleToken")
    @FormUrlEncoded
    Call<UserAuthenticationResponse> genTokenForGoogleLogin(@Field("googleToken") String googleToken);

    //Google login
    @POST("/users/google/auth")
    Call<UserAuthenticationResponse> googleLogin(@Header(AUTHORIZATION) String token,
                                                 @Body UserLogin userLogin);

    // endregion

    // region EmailLogin

    // token generate for Login
    @POST("/users/{email}/login")
    Call<UserAuthenticationResponse> genTokenForEmailLogin(@Path("email") String email);

    //Email Login
    @POST("/users/login")
    Call<UserAuthenticationResponse> emailLogin(@Header(AUTHORIZATION) String token,
                                                @Body UserLogin userLogin);

    // endregion

    // region EmailSignUp

    // token generate for SignUp
    @POST("/users/{email}/signUp")
    Call<UserAuthenticationResponse> genTokenForEmailSignUp(@Path("email") String email);

    //Email SignUp
    @POST("/users")
    Call<UserAuthenticationResponse> emailSignUp(@Header(AUTHORIZATION) String token,
                                                 @Body UserSignUp userSignUp);

    // endregion

    // region ForgetPassword-sendForgetPasswordRequest

    @POST("/users/forget/password/req")
    @FormUrlEncoded
    Call<QxmApiResponse> sendForgetPasswordRequest(@Field("email") String email);

    // endregion

    // region UserInterestAndPreferableLanguageSend

    @POST("/profileEdit/{userId}/userInterestAndLanguage")
    @FormUrlEncoded
    Call<QxmApiResponse> postUserInterestAndPreferredLanguages(@Header(AUTHORIZATION) String token,
                                                               @Path("userId") String userId,
                                                               @Field("interest") ArrayList<String> interests,
                                                               @Field("language") ArrayList<String> preferredLanguage,
                                                               @Field("gender") String gender,
                                                               @Field("dob") String dateOfBirth,
                                                               @Field("dobPrivacy") String dateOfBirthPrivacy);

    // endregion

    // region UserProfileImageUpload
    @Multipart
    @POST("/users/{userId}/upload")
    Call<UserProfileImageResponse> uploadProfileImage(@Header(AUTHORIZATION) String token,
                                                      @Path("userId") String userId,
                                                      @Part MultipartBody.Part image);
    // endregion


    //get specific user
    @GET("/users/{userId}")
    Call<User> getUser(@Header(AUTHORIZATION) String token,
                       @Path("userId") String userId);


    // region Feed

    @GET("/feed/{userId}/{reloadIndex}")
    Call<List<FeedData>> getFeedItemList(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId,
                                         @Path("reloadIndex") int reloadIndex);

    @POST("/ignite/{userId}/{qxmId}")
    Call<QxmApiResponse> igniteQxm(@Header(AUTHORIZATION) String token,
                                   @Path("userId") String userId,
                                   @Path("qxmId") String qxmId);

    @POST("/qxm/{qxmId}/qxmHide/{userId}")
    Call<QxmApiResponse> notInterested(@Header(AUTHORIZATION) String token,
                                       @Path("qxmId") String qxmId,
                                       @Path("userId") String userId);

    // endregion

    // region UserProfile


    // GET_UserProfileInfo
    @GET("/profileEdit/{userId}/profileData/{reloadIndex}")
    Call<UserInfo> getUserProfileInfo(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("reloadIndex") int reloadIndex);

    // endregion

    // region accountStatus
    @GET("/users/{userId}/accountStatus")
    Call<QxmApiResponse> getAccountStatus(@Header(AUTHORIZATION) String token,
                                          @Path("userId") String userId);

    // endregion

    // region UpdatePassword

    @POST("/users/{userId}/passwordChange")
    @FormUrlEncoded
    Call<QxmApiResponse> updatePassword(@Header(AUTHORIZATION) String token,
                                        @Path("userId") String userId,
                                        @Field("previousPass") String currentPassword,
                                        @Field("newPass") String newPassword,
                                        @Field("confirmNewPass") String confirmPassword);

    // endregion

    // region SendVerificationEmail

    @POST("/users/{userId}/resendConfirmation")
    Call<QxmApiResponse> sendVerificationEmail(@Header(AUTHORIZATION) String token,
                                               @Path("userId") String userId);

    // endregion

    // GET_OtherUserProfileInfo
    @GET("/profileEdit/{userId}/othersProfileData/{otherUserId}")
    Call<UserInfo> getOtherUserProfileInfo(@Header(AUTHORIZATION) String token,
                                           @Path("userId") String userId,
                                           @Path("otherUserId") String otherUserId);

    // endregion

    // GET_UserProfileInfoDetails
    @GET("/users/{userId}/profile")
    Call<UserInfoDetails> getUserProfileInfoDetails(@Header(AUTHORIZATION) String token,
                                                    @Path("userId") String userId);

    // GET User following list
    @GET("/follow/{userId}/followingList")
    Call<List<com.crux.qxm.db.models.profile.User>> getFollowingList(@Header(AUTHORIZATION) String token,
                                                                     @Path("userId") String userId);

    // GET User following list
    @GET("/follow/{userId}/followingListSimple")
    Call<List<com.crux.qxm.db.models.profile.User>> getFollowingListSimple(@Header(AUTHORIZATION) String token,
                                                                           @Path("userId") String userId);

    //GET user follower list
    @GET("/follow/{userId}/followerList")
    Call<List<com.crux.qxm.db.models.profile.User>> getFollowerList(@Header(AUTHORIZATION) String token,
                                                                    @Path("userId") String userId);

    //GET user public group
    @GET("/group/{userId}/getUserPublicGroupList")
    Call<List<SearchedGroup>> getUserPublicGroupList(@Header(AUTHORIZATION) String token,
                                                     @Path("userId") String userId);


    // endregion

    // region EditUserInfoDetails

    // Update FirstName, LastName, FullName
    @PATCH("/profileEdit/{userId}/name/edit")
    @FormUrlEncoded
    Call<QxmApiResponse> updateFullName(@Header(AUTHORIZATION) String token,
                                        @Path("userId") String userId,
                                        @Field("firstName") String firstName,
                                        @Field("lastName") String lastName,
                                        @Field("fullName") String fullName);

    // Update gender
    @PATCH("/profileEdit/{userId}/genderEdit")
    @FormUrlEncoded
    Call<QxmApiResponse> updateUserGender(@Header(AUTHORIZATION) String token,
                                          @Path("userId") String userId,
                                          @Field("gender") String gender);

    // update dob
    @PATCH("/profileEdit/{userId}/dobEdit")
    @FormUrlEncoded
    Call<QxmApiResponse> updateUserDob(@Header(AUTHORIZATION) String token,
                                       @Path("userId") String userId,
                                       @Field("dateOfBirth") String gender,
                                       @Field("dateOfBirthPrivacy") String dateOfBirthPrivacy);


    // Update UserName
    @PATCH("/profileEdit/{userId}/username")
    @FormUrlEncoded
    Call<QxmApiResponse> updateUsername(@Header(AUTHORIZATION) String token,
                                        @Path("userId") String userId,
                                        @Field("userName") String userName);


    // update email
    @PATCH("/profileEdit/{userId}/email/edit/")
    @FormUrlEncoded
    Call<QxmApiResponse> updateUserEmail(@Header(AUTHORIZATION) String token,
                                       @Path("userId") String userId,
                                       @Field("previousEmail") String previousEmail,
                                       @Field("newEmail") String newEmail);

    // add or edit facebook address

    @PATCH("/profileEdit/{userId}/{socialSiteName}/edit")
    @FormUrlEncoded
    Call<QxmApiResponse> addOrUpdateSocialAddress(@Header(AUTHORIZATION) String token,
                                                  @Path("userId") String userId,
                                                  @Path("socialSiteName") String socialSiteName,
                                                  @Field("previousId") String previousId,
                                                  @Field("newId") String newId);


    // Update Interest
    @PATCH("/profileEdit/{userId}/interestEdit")
    @FormUrlEncoded
    Call<QxmApiResponse> addUpdateOrDeleteInterest(@Header(AUTHORIZATION) String token,
                                                   @Path("userId") String userId,
                                                   @Field("interest") List<String> interest,
                                                   @Field("deleteInterest") List<String> deletedItemList);

    @PATCH("/profileEdit/{userId}/addInterest")
    @FormUrlEncoded
    Call<QxmApiResponse> addInterest(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId,
                                     @Field("interest") String interest);

    @PATCH("/profileEdit/{userId}/deleteInterest")
    @FormUrlEncoded
    Call<QxmApiResponse> deleteInterest(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId,
                                     @Field("interest") String interest);

    @PATCH("/profileEdit/{userId}/updateInterest")
    @FormUrlEncoded
    Call<QxmApiResponse> updateInterest(@Header(AUTHORIZATION) String token,
                                        @Path("userId") String userId,
                                        @Field("oldInterest") String oldInterest,
                                        @Field("newInterest") String newInterest);


    // Update Education
    @PATCH("/profileEdit/{userId}/educationEdit/{id}")
    @FormUrlEncoded
    Call<QxmApiResponse> updateEducation(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId,
                                         @Path("id") String institutionId,
                                         @Field("institute") String name,
                                         @Field("fieldOfStudy") String fieldOfStudy,
                                         @Field("grade") String grade,
                                         @Field("degree") String degree,
                                         @Field("fromTo") String toFrom,
                                         @Field("deleteRequest") boolean deleteRequest);

    // Update AddressEdit
    @PATCH("/profileEdit/{userId}/description/edit")
    @FormUrlEncoded
    Call<QxmApiResponse> updateDescription(@Header(AUTHORIZATION) String token,
                                           @Path("userId") String userId,
                                           @Field("description") String description);


    // update user description
    @PATCH("/profileEdit/{userId}/addressEdit/{addressId}")
    @FormUrlEncoded
    Call<QxmApiResponse> updateAddress(@Header(AUTHORIZATION) String token,
                                       @Path("userId") String userId,
                                       @Field("country") String country,
                                       @Field("city") String city,
                                       @Field("address") String address,
                                       @Path("addressId") String addressId);


    // Update Occupation
    @PATCH("/profileEdit/{userId}/occupationEdit/{occupationId}")
    @FormUrlEncoded
    Call<QxmApiResponse> addEditOrDeleteOccupation(@Header(AUTHORIZATION) String token,
                                                   @Path("userId") String userId,
                                                   @Path("occupationId") String occupationId,
                                                   @Field("designation") String designation,
                                                   @Field("organization") String organization,
                                                   @Field("workDuration") String workDuration,
                                                   @Field("deleteRequest") Boolean deleteRequest);

    // Update Website
    @PATCH("/profileEdit/{userId}/website/edit")
    @FormUrlEncoded
    Call<QxmApiResponse> editUpdateOrDeleteWebsite(@Header(AUTHORIZATION) String token,
                                                   @Path("userId") String userId,
                                                   @Field("previousWebsite") String previousWebsite,
                                                   @Field("website") String website);

    // Update Website
    @PATCH("/profileEdit/{userId}/languageEdit")
    @FormUrlEncoded
    Call<QxmApiResponse> editUpdateOrDeletePreferableLanguage(@Header(AUTHORIZATION) String token,
                                                              @Path("userId") String userId,
                                                              @Field("language") List<String> languageList);


    // endregion

    // region CreateQxm

    @POST("/qxm/{userId}/newqxm")
    Call<QxmModel> postQxm(@Header(AUTHORIZATION) String token,
                           @Path("userId") String userId,
                           @Body QxmModel qxmModel);

    @Multipart
    @POST("/qxm/qxmimageQuestion")
    Call<UploadedImageLink> uploadThumbnailImage(@Header(AUTHORIZATION) String token,
                                                 @Part MultipartBody.Part image);

    @Multipart
    @POST("/qxm/qxmimageQuestion")
    Call<UploadedImageLink> uploadQuestionImage(@Header(AUTHORIZATION) String token,
                                                @Path("userId") String userId,
                                                @Part MultipartBody.Part image);


    // endregion

    //region DeleteQxm
    @GET("/qxm/{userId}/editQxm/{qxmId}")
    Call<QxmModel> editQxm(@Header(AUTHORIZATION) String token,
                           @Path("userId") String userId,
                           @Path("qxmId") String qxmId);

    @POST("/qxm/{userId}/editQxm/{qxmId}")
    Call<QxmModel> postEditedQxm(@Header(AUTHORIZATION) String token,
                                 @Path("userId") String userId,
                                 @Path("qxmId") String qxmId,
                                 @Body QxmModel qxmModel);
    //endregion

    //region DeleteQxm
    @POST("/qxm/{userId}/deleteQxm/{qxmId}")
    Call<QxmApiResponse> deleteQxm(@Header(AUTHORIZATION) String token,
                                   @Path("userId") String userId,
                                   @Path("qxmId") String qxmId);
    //endregion

    // region QxmManualStart

    @POST("/qxm/{qxmId}/manualStart")
    Call<QxmApiResponse> startQxmNow(@Header(AUTHORIZATION) String token,
                                     @Path("qxmId") String qxmId);

    // endregion

    // region ParticipateQxm
    @GET("/qxm/{qxmId}/examquestionsheet/")
    Call<QxmModel> getExamQuestionSheet(@Header(AUTHORIZATION) String token,
                                        @Path("qxmId") String qxmId);

    @POST("/qxm/{userId}/{qxmId}/initialQxmParticipation/")
    Call<QxmApiResponse> initialQxmParticipation(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("qxmId") String qxmId,
                                      @Body QxmModel qxmModel);

    @POST("/qxm/{userId}/{qxmId}/participation/")
    Call<QxmApiResponse> submitAnswer(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("qxmId") String qxmId,
                                      @Body QxmModel qxmModel);

    // endregion

    // region MyQxm Feed

    @GET("/qxm/{userId}/myQxmData/{reloadIndex}")
    Call<List<FeedData>> getMyQxmFeed(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("reloadIndex") int reloadIndex);
    // endregion


    // region Enroll

    @POST("/enroll/{userId}/enrollSend/{qxmId}")
    Call<QxmApiResponse> sendEnrollRequest(@Header(AUTHORIZATION) String token,
                                           @Path("userId") String userId,
                                           @Path("qxmId") String qxmId);


    // endregion

    // region SubmitEvaluation
    @POST("/qxm/{participationId}/evaluationConfirm/")
    Call<QxmApiResponse> submitEvaluatedAnswer(@Header(AUTHORIZATION) String token,
                                               @Path("participationId") String participationId,
                                               @Body EvaluationDetails evaluationDetails);
    // endregion

    // region getEvaluationDetails
    @GET("/qxm/{participationId}/evaluationDetails")
    Call<EvaluationDetails> getEvaluationDetails(@Header(AUTHORIZATION) String token,
                                                 @Path("participationId") String userId);
    // endregion

    // region getPendingEvaluationList

    @GET("/qxm/{qxmId}/userEvalutionPendingList")
    Call<ArrayList<ParticipatorContainer>> getPendingEvaluationList(@Header(AUTHORIZATION) String token,
                                                                    @Path("qxmId") String qxmId);
    // endregion

    // region getQxmFullResult
    @GET("/qxm/{userId}/seeResult/{qxmId}")
    Call<FullResult> getQxmFullResult(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("qxmId") String qxmId);

    // endregion

    // region getFollowingUserList
    // TODO:: add getFollowingUserList api


    // endregion

    //region QuestionOverview Fragment
    @GET("/qxm/{qxmId}/qxmOverView/{userId}")
    Call<FeedData> questionOverview(@Header(AUTHORIZATION) String token,
                                    @Path("qxmId") String qxmId,
                                    @Path("userId") String userId);


    @GET("/search/{qxmId}/participator/")
    Single<List<Participator>> getParticipatorListInASpecificQxm(@Header(AUTHORIZATION) String token,
                                                                 @Path("qxmId") String qxmId,
                                                                 @Query("searchParams") String searchParams);


    //endregion

    @GET("/qxm/{singleMCQId}/singleQxmOverview/{userId}")
    Call<FeedData> getSingleMCQOverview(@Header(AUTHORIZATION) String token,
                                        @Path("singleMCQId") String singleMCQId,
                                        @Path("userId") String userId);

    @GET("/qxm/{pollId}/singlePollOverview/{userId}")
    Call<FeedData> getSinglePollOverview(@Header(AUTHORIZATION) String token,
                                         @Path("pollId") String pollId,
                                         @Path("userId") String userId);


    // region Fragment-MyQxmFullResult

    @GET("/qxm/Performance/{userId}")
    Call<List<MyQxmResult>> getMyQxmResult(@Header(AUTHORIZATION) String token,
                                           @Path("userId") String userId);

    // endregion

    //region FRAGMENT-MyQxmFragment

    // region get specific qxm
    @GET("/qxm/{qxmId}/specificQxm")
    Call<MyQxmDetails> myQxmDetails(@Header(AUTHORIZATION) String token,
                                    @Path("qxmId") String qxmId);
    //endregion

    // region Fragment-MyQxmMyPoll

    @GET("/qxm/{userId}/getMyPoll/{reloadIndex}")
    Call<List<FeedData>> getMyPoll(@Header(AUTHORIZATION) String token,
                                   @Path("userId") String userId,
                                   @Path("reloadIndex") int reloadIndex);
    // endregion

    //region get accepted enrolled list

    @GET("/enroll/{qxmId}/acceptedEnrollQxm")
    Call<ArrayList<Participator>> getAcceptedEnrolledUserList(@Header(AUTHORIZATION) String token,
                                                              @Path("qxmId") String qxmId);
    //endregion

    // region get participator list

    @GET("/qxm/{qxmId}/participationinqxm")
    Call<ArrayList<Participator>> getParticipatorList(@Header(AUTHORIZATION) String token,
                                                      @Path("qxmId") String qxmId);


    // endregion

    //endregion

    // region FRAGMENT-MyQxmQxmDraftFragment

    @GET("qxm/{userId}/getQxmDrafts")
    Call<List<QxmDraft>> getMyQBank(@Header(AUTHORIZATION) String token,
                                    @Path("userId") String userId);

    @GET("qxm/{userId}/getDraftedQxm")
    Call<List<DraftedQxm>> getDraftedQxm(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId);

    @POST("/qxm/{draftedQxmId}/deleteDraftedQxm")
    Call<QxmApiResponse> deleteDraftedQxm(@Header(AUTHORIZATION) String token,
                                          @Path("draftedQxmId") String draftedQxmId);


    // endregion

    //region FRAGMENT-ENROLL FRAGMENT

    // region GetYouParticipatedList

    @GET("/enroll/{userId}/meParticipated")
    Call<List<YouParticipatedData>> getYouParticipatedList(@Header(AUTHORIZATION) String token,
                                                           @Path("userId") String userId);
    // endregion

    // region GetYouEnrolledList

    @GET("/enroll/{userId}/myAcceptedEnroll")
    Call<List<MyEnrolledData>> getYouEnrolledList(@Header(AUTHORIZATION) String token,
                                                  @Path("userId") String userId);
    // endregion

    // region GetPeopleEnrolledList

    @GET("/enroll/{userId}/acceptedUserEnrollOnMyQxm")
    Call<List<PeopleEnrolledData>> getPeopleEnrolledList(@Header(AUTHORIZATION) String token,
                                                         @Path("userId") String userId);
    // endregion

    // region GetEnrollSentRequestsList

    @GET("/enroll/{userId}/myPendingEnroll")
    Call<List<SentEnrollRequestData>> getEnrollSentRequestsList(@Header(AUTHORIZATION) String token,
                                                                @Path("userId") String userId);
    // endregion

    // region GetEnrollReceivedRequestsList

    @GET("/enroll/{userId}/pendingUserEnrollOnMyQxm")
    Call<List<ReceivedEnrollRequestData>> getEnrollReceivedRequestsList(@Header(AUTHORIZATION) String token,
                                                                        @Path("userId") String userId);
    // endregion

    // region AcceptEnrollRequest

    @POST("/enroll/{enrollId}/acceptedEnroll")
    Call<QxmApiResponse> acceptEnrollRequest(@Header(AUTHORIZATION) String token,
                                             @Path("enrollId") String enrollId);
    // endregion

    // region Reject/CancelEnrollRequest

    @POST("/enroll/{enrollId}/rejectedEnroll/{qxmId}")
    Call<QxmApiResponse> rejectEnrollRequest(@Header(AUTHORIZATION) String token,
                                             @Path("enrollId") String enrollId,
                                             @Path("qxmId") String qxmId);

    // endregion

    //region cancelMySentEnrollRequest

    @POST("/enroll/{enrollId}/cancelEnroll/{qxmId}")
    Call<QxmApiResponse> cancelMySentEnrollRequest(@Header(AUTHORIZATION) String token,
                                                   @Path("enrollId") String enrollId,
                                                   @Path("qxmId") String qxmId);
    //endregion

    // endregion

    // region Following

    // region Follow A User

    @PATCH("/follow/{followerId}/follow/{followingId}")
    Call<QxmApiResponse> followUser(@Header(AUTHORIZATION) String token,
                                    @Path("followerId") String followerId,
                                    @Path("followingId") String followingId);

    // endregion

    // region Following Feed Data

    @GET("/follow/{userId}/followingFeed/{reloadIndex}")
    Call<List<FeedData>> getFollowingFeedData(@Header(AUTHORIZATION) String token,
                                              @Path("userId") String userId,
                                              @Path("reloadIndex") int reloadIndex);

    // endregion

    // region Following User List

    @GET("/follow/{userId}/followingList")
    Call<List<FollowingUser>> getFollowingUserList(@Header(AUTHORIZATION) String token,
                                                   @Path("userId") String userId);

    // endregion

    // region ChangeNotification-ChangeSpecificUserFollowingNotificationSettings

    @POST("/follow/{followingId}/changeNotification")
    @FormUrlEncoded
    Call<QxmApiResponse> changeSpecificUserFollowingNotificationSettings(@Header(AUTHORIZATION) String token,
                                                                         @Path("followingId") String followingId,
                                                                         @Field("notification") String notification);

    // endregion


    // region Un-follow A User

    @PATCH("/follow/{followerId}/unfollow/{followingId}")
    Call<QxmApiResponse> unfollowUser(@Header(AUTHORIZATION) String token,
                                      @Path("followerId") String followerId,
                                      @Path("followingId") String followingId);

    // endregion

    // endregion

    // region -------GROUP------

    // region CreateGroup

    @POST("/group/{userId}/createGroup")
    Call<QxmApiResponse> createGroup(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId,
                                     @Body Group group);


    // endregion

    // region GetGroupSettings

    @GET("/group/{groupId}/groupSettings")
    Call<GroupSettingsContainer> getGroupSettings(@Header(AUTHORIZATION) String token,
                                                  @Path("groupId") String groupId);


    // endreigon

    @GET("/group/{groupId}/joinReqList")
    Call<List<GroupPendingRequestListItem>> getGroupPendingRequestList(@Header(AUTHORIZATION) String token,
                                                                       @Path("groupId") String groupId);

    //
    @POST("/group/{groupId}/acceptRequest/{userId}/{adminId}")
    Call<QxmApiResponse> acceptGroupJoinRequest(@Header(AUTHORIZATION) String token,
                                                @Path("groupId") String groupId,
                                                @Path("userId") String userId,
                                                @Path("adminId") String adminId,
                                                @Field("null") String empty);

    // region UpdateGroupSettings

    @POST("/group/{groupId}/editGroupSettings/{userId}")
    Call<QxmApiResponse> updateGroupSettings(@Header(AUTHORIZATION) String token,
                                             @Path("groupId") String groupId,
                                             @Path("userId") String userId,
                                             @Body GroupSettingsContainer groupGroupSettingsContainer);

    // endregion

    // region GetSpecificGroupData

    @GET("/group/{groupId}/getGroupData/{userId}")
    Call<com.crux.qxm.db.models.group.groupFeed.Group> getSpecificGroupData(@Header(AUTHORIZATION) String token,
                                                                            @Path("groupId") String groupId,
                                                                            @Path("userId") String userId);

    // endregion

    // region getMyGroupList

    @GET("/group/{userId}/myGroup")
    Call<MyGroupFeed> getMyGroupList(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId);

    // endregion

    // region AddMemberToGroup
    @GET("/group/{userId}/suggestedList")
    Call<AddMemberList> getAddMemberList(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId);

    // endregion


    // region AddAdminToGroup

    @POST("/group/{userId}/addAdmin/{groupId}/{adminId}")
    Call<QxmApiResponse> addAdminToGroup(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId,
                                         @Path("groupId") String groupId,
                                         @Path("adminId") String adminId);

    // endregion

    // region AddMemberToGroup

    @POST("/group/{groupId}/addMember/{addedBy}")
    @FormUrlEncoded
    Call<QxmApiResponse> addMembersToGroup(@Header(AUTHORIZATION) String token,
                                           @Path("groupId") String groupId,
                                           @Path("addedBy") String addedBy,
                                           @Field("newMemberId") List<String> newMemberIds);

    // endregion

    // region SuggestedListForAddingMember

    @GET("/group/{userId}/suggestedList/{groupId}")
    Call<AddMemberList> getSuggestedAddMemberList(@Header(AUTHORIZATION) String token,
                                                  @Path("userId") String userId,
                                                  @Path("groupId") String groupId);

    // endregion


    // region SuggestedListForAddingMember

    @GET("/group/{userId}/memberSearch/{groupId}")
    Single<AddMemberList> getSearchedSuggestedAddMemberList(@Header(AUTHORIZATION) String token,
                                                            @Path("userId") String userId,
                                                            @Path("groupId") String groupId,
                                                            @Query("searchParams") String searchParams);

    // endregion

    // region GetGroupMemberList

    @GET("/group/{groupId}/memberList")
    Call<GroupMembers> getGroupMemberList(@Header(AUTHORIZATION) String token,
                                          @Path("groupId") String groupId);


    // endregion

    // region SendJoinRequestToGroup

    @POST("/group/{userId}/joinReq/{groupId}/")
    Call<QxmApiResponse> sendJoinRequestToGroup(@Header(AUTHORIZATION) String token,
                                                @Path("userId") String userId,
                                                @Path("groupId") String groupId);

    // endregion


    // region AddModeratorToGroup

    @POST("group/{moderatorId}/addModerator/{groupId}/{userId}")
    Call<QxmApiResponse> addModeratorToGroup(@Header(AUTHORIZATION) String token,
                                             @Path("moderatorId") String moderatorId,
                                             @Path("groupId") String groupId,
                                             @Path("userId") String addedById);

    // endregion

    // region AcceptGroupJoinRequest
    @POST("/group/{groupId}/acceptRequest/{userId}/{adminId}")
    Call<QxmApiResponse> acceptGroupJoinRequest(@Header(AUTHORIZATION) String token,
                                                @Path("groupId") String groupId,
                                                @Path("userId") String userId,
                                                @Path("adminId") String adminId);

    // endregion

    //region CancelGroupJoinRequest
    @POST("/group/{groupId}/cancelRequest/{userId}")
    Call<QxmApiResponse> cancelGroupJoinRequest(@Header(AUTHORIZATION) String token,
                                                @Path("groupId") String groupId,
                                                @Path("userId") String userId);
    //endregion

    //region RejectGroupJoinRequest
    @POST("/group/{groupId}/rejectRequest/{userId}/{adminId}")
    Call<QxmApiResponse> rejectGroupJoinRequest(@Header(AUTHORIZATION) String token,
                                                @Path("groupId") String groupId,
                                                @Path("userId") String userId,
                                                @Path("adminId") String adminId);
    //endregion

    // region DeleteAGroupMember

    @POST("/group/{groupId}/deleteMember/{memberId}")
    Call<QxmApiResponse> deleteAGroupMember(@Header(AUTHORIZATION) String token,
                                            @Path("groupId") String groupId,
                                            @Path("memberId") String memberId);

    // endregion

    // region DeleteQxmInGroup

    @POST("/group/{groupId}/deleteQxmInGroup/{feedId}")
    Call<QxmApiResponse> deleteAQxmInGroup(@Header(AUTHORIZATION) String token,
                                           @Path("groupId") String groupId,
                                           @Path("feedId") String feedId);

    // endregion

    // region DeleteAdmin
    @POST("/group/{groupId}/deleteAdmin/{adminId}")
    Call<QxmApiResponse> deleteAdmin(@Header(AUTHORIZATION) String token,
                                     @Path("groupId") String groupId,
                                     @Path("adminId") String adminToBeDeletedId);

    // endregion

    // region DeleteModerator

    @POST("/group/{groupId}/deleteMod/{moderatorId}")
    Call<QxmApiResponse> deleteModerator(@Header(AUTHORIZATION) String token,
                                         @Path("groupId") String groupId,
                                         @Path("moderatorId") String moderatorToBeDeletedId);


    // endregion

    // region LeaveGroup
    @POST("/group/{groupId}/leaveGroup/{userId}")
    @FormUrlEncoded
    Call<QxmApiResponse> leaveGroup(@Header(AUTHORIZATION) String token,
                                    @Path("groupId") String groupId,
                                    @Path("userId") String userId,
                                    @Field("leaveForever") boolean leaveForever);


    // endregion

    // region DeleteGroup
    @POST("/group/{groupId}/deleteGroup/{adminId}")
    Call<QxmApiResponse> deleteGroup(@Header(AUTHORIZATION) String token,
                                     @Path("groupId") String groupId,
                                     @Path("adminId") String adminId);
    // endregion


    // endregion

    // region GroupJoinRequest

    @POST("/group/{groupId}/inviteMember/{invitedById}/{invitedUserId}")
    Call<QxmApiResponse> inviteMember(@Header(AUTHORIZATION) String token,
                                      @Path("groupId") String groupId,
                                      @Path("invitedById") String inviteById,
                                      @Path("invitedUserId") String invitedUserId);

    // endregion


    // region GroupJoinRequest

    @POST("/group/{groupId}/addMember/{addedBy}")
    @FormUrlEncoded
    Call<QxmApiResponse> addMember(@Header(AUTHORIZATION) String token,
                                   @Path("groupId") String groupId,
                                   @Path("addedBy") String inviteById,
                                   @Field("newMemberId") String memberUserId);

    // endregion

    // region GetGroupInvitationList

    @GET("/group/{groupId}/invitationList")
    Call<GroupInvitations> getGroupInvitationList(@Header(AUTHORIZATION) String token,
                                                  @Path("groupId") String groupId);
    // endregion

    // region UN-USED :- GetInvitationListUser

    @POST("/group/{userId}/invitationListUser")
    Call<GroupInvitationsOfUser> getInvitationListForUser(@Header(AUTHORIZATION) String token,
                                                          @Path("userId") String userId);

    // endregion


    // region InviteAcceptFromAdmin

    @POST("/group/{groupId}/inviteAcceptFromAdmin/{invitedUserId}/{userId}")
    Call<QxmApiResponse> inviteAcceptFromAdmin(@Header(AUTHORIZATION) String token,
                                               @Path("groupId") String groupId,
                                               @Path("invitedUserId") String invitedUserId,
                                               @Path("userId") String userId);

    // endregion
    @Multipart
    @POST("/group/{groupId}/updateGroupImage")
    Call<QxmApiResponse> changeGroupImage(@Header(AUTHORIZATION) String token,
                                          @Path("groupId") String groupId,
                                          @Part MultipartBody.Part image);
    //region change group image


    //endregion

    // region inviteAcceptFromUser

    @POST("/group/{groupId}/inviteAcceptFromUser/{invitedUserId}")
    Call<QxmApiResponse> inviteAcceptFromUser(@Header(AUTHORIZATION) String token,
                                              @Path("groupId") String groupId,
                                              @Path("invitedUserId") String invitedUserId);

    // endregion

    // endregion

    // region ----REPORT---


    // region ReportAGroup

    @POST("/report/{userId}/reportOnGroup/{groupId}")
    Call<QxmApiResponse> reportAGroup(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Path("groupId") String groupId,
                                      @Body ReportModel reportModel);

    // endregion

    // region ReportAQxm

    @POST("/report/{userId}/reportOnQxm/{qxmId}")
    Call<QxmApiResponse> reportAQxm(@Header(AUTHORIZATION) String token,
                                    @Path("userId") String userId,
                                    @Path("qxmId") String qxmId,
                                    @Body ReportModel reportModel);

    // endregion

    // region ReportASingleMCQ
    @POST("/report/{userId}/reportOnSingleMcq/{singleMCQId}")
    Call<QxmApiResponse> reportASingleMCQ(@Header(AUTHORIZATION) String token,
                                          @Path("userId") String userId,
                                          @Path("singleMCQId") String singleMCQId,
                                          @Body ReportModel reportModel);
    // endregion

    // region ReportOnPoll

    @POST("/report/{userId}/reportOnPoll/{pollId}")
    Call<QxmApiResponse> reportAPoll(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId,
                                     @Path("pollId") String pollId,
                                     @Body ReportModel reportModel);

    // endregion

    // region ReportAUser

    @POST("/report/{userId}/reportAUser/{reportingUserId}")
    Call<QxmApiResponse> reportAUser(@Header(AUTHORIZATION) String token,
                                     @Path("userId") String userId,
                                     @Path("reportingUserId") String reportingUserId,
                                     @Body ReportModel reportModel);

    // endregion

    // endregion


    // region -----NOTIFICATION-----

    // region AllNotification
    @GET("/notification/{userId}/allNotification/")
    Call<AllNotification> getAllNotificationList(@Header(AUTHORIZATION) String token,
                                                 @Path("userId") String userId);

    // endregion

    // region QxmNotification

    @GET("/notification/{userId}/qxmNotification/")
    Call<List<QxmNotification>> getQxmNotificationList(@Header(AUTHORIZATION) String token,
                                                       @Path("userId") String userId);

    // endregion

    // region PrivateQxmNotification
    @GET("/notification/{userId}/privateNotification/")
    Call<List<EvaluationNotification>> getPrivateQxmNotificationList(@Header(AUTHORIZATION) String token,
                                                                     @Path("userId") String userId);


    // endregion

    // region EvaluationNotificationFragment
    @GET("/notification/{userId}/evaluationNotification/")
    Call<List<EvaluationNotification>> getEvaluationNotificationList(@Header(AUTHORIZATION) String token,
                                                                     @Path("userId") String userId);
    // endregion

    // region ResultNotificationFragment

    @GET("/notification/{userId}/resultNotification/")
    Call<List<ResultNotification>> getResultNotificationList(@Header(AUTHORIZATION) String token,
                                                             @Path("userId") String userId);

    // endregion

    // region GroupNotificationFragment

    @GET("/notification/{userId}/groupNotification/")
    Call<List<GroupNotificationItem>> getGroupNotificationList(@Header(AUTHORIZATION) String token,
                                                               @Path("userId") String userId);


    // endregion

    // endregion

    // region Menu

    // region QxmAddToGroup
    @POST("/group/{qxmId}/{userId}/shareQxmToGroup/{groupId}/")
    Call<QxmApiResponse> shareQxmToGroup(@Header(AUTHORIZATION) String token,
                                         @Path("qxmId") String qxmId,
                                         @Path("userId") String userId,
                                         @Path("groupId") String groupId);

    // endregion

    // endregion

    //region  search

    @GET("/search/{userId}/users/")
    Single<SearchedUserContainer> getSearchedUser(@Header(AUTHORIZATION) String token,
                                                  @Path("userId") String userId,
                                                  @Query("searchParams") String searchParams);


    @GET("/search/{userId}/qxm/")
    Single<SearchedQxmContainer> getSearchedQxm(@Header(AUTHORIZATION) String token,
                                                @Path("userId") String userId,
                                                @Query("searchParams") String searchParams);

    @GET("/search/{userId}/group/")
    Single<SearchedGroupContainer> getSearchedGroup(@Header(AUTHORIZATION) String token,
                                                    @Path("userId") String userId,
                                                    @Query("searchParams") String searchParams);

    @GET("/search/{userId}/singleMcq/")
    Single<SearchedSingleMcqContainer> getSearchedSingleMCQ(@Header(AUTHORIZATION) String token,
                                                            @Path("userId") String userId,
                                                            @Query("searchParams") String searchParams);

    @GET("/search/{userId}/poll/")
    Single<SearchedPollContainer> getSearchedPoll(@Header(AUTHORIZATION) String token,
                                                  @Path("userId") String userId,
                                                  @Query("searchParams") String searchParams);

    @GET("/search/{userId}/list/")
    Single<List<SearchedList>> getSearchedList(@Header(AUTHORIZATION) String token,
                                               @Path("userId") String userId,
                                               @Query("searchParams") String searchParams);

    //endregion

    //region feed menu

    // group list in menu
    @GET("/group/{userId}/myGroup")
    Call<MyGroupDataInMenuContainer> getMyGroupListInMenu(@Header(AUTHORIZATION) String token,
                                                          @Path("userId") String userId);

    //share qxm in group
//    @POST("/group/{qxmId}/{userId}/shareToGroup/{groupId}" )
//    Call<QxmApiResponse> shareToGroup(@Header(AUTHORIZATION) String token,
//                                         @Path("qxmId" ) String qxmId,
//                                         @Path("userId" ) String userId,
//                                         @Path("groupId" ) String groupId);

    //add qxm to list
    @POST("list/{listId}/addQxmToList/{qxmId}")
    Call<QxmApiResponse> addQxmToList(@Header(AUTHORIZATION) String token,
                                      @Path("listId") String listId,
                                      @Path("qxmId") String qxmId);


    //endregion

    //region list

    //create list
    @POST("/list/{userId}/newList")
    Call<QxmApiResponse> createNewList(@Header(AUTHORIZATION) String token,
                                       @Path("userId") String userId,
                                       @Body ListSettings listSettings);

    //get my all list
    @GET("/list/{userId}/myList")
    Call<List<com.crux.qxm.db.models.myQxm.list.List>> getAllList(@Header(AUTHORIZATION) String token,
                                                                  @Path("userId") String userId);

    //get my all list
    @POST("/list/{listId}/deleteList/{userId}")
    Call<QxmApiResponse> deleteSingleList(@Header(AUTHORIZATION) String token,
                                          @Path("listId") String listId,
                                          @Path("userId") String userId);

    //get qxm of a list
    @GET("/list/{listId}/getFeed/{userId}")
    Call<List<FeedData>> getListFeed(@Header(AUTHORIZATION) String token,
                                     @Path("listId") String listId,
                                     @Path("userId") String userId);

    // delete Qxm in list
    @POST("/list/{listId}/deleteQxmToList/{qxmId}")
    Call<QxmApiResponse> deleteQxmInList(@Header(AUTHORIZATION) String token,
                                         @Path("listId") String listId,
                                         @Path("qxmId") String qxmId);


    //endregion


    //region poll

    //region CreatePoll
    @POST("/qxm/{userId}/newPoll")
    Call<QxmApiResponse> createNewPoll(@Header(AUTHORIZATION) String token,
                                       @Path("userId") String userId,
                                       @Body PollPostingModel pollPostingModel);
    //endregion

    @GET("/qxm/{pollId}/getFullPollData")
    Call<PollData> getFullPollData(@Header(AUTHORIZATION) String token,
                                   @Path("pollId") String userId);

    //region EditPoll
    @POST("/qxm/{userId}/editPoll/{pollId}")
    @FormUrlEncoded
    Call<QxmApiResponse> editPoll(@Header(AUTHORIZATION) String token,
                                  @Path("userId") String userId,
                                  @Path("pollId") String pollId,
                                  @Field("pollTitle") String pollPostingModel);
    // endregion

    //region DeletePoll
    @POST("/qxm/{userId}/deletePoll/{pollId}")
    Call<QxmApiResponse> deletePoll(@Header(AUTHORIZATION) String token,
                                    @Path("userId") String userId,
                                    @Path("pollId") String pollId);
    //endregion


    //region PollParticipation
    @POST("qxm/{userId}/pollParticipation/{pollId}")
    @FormUrlEncoded
    Call<QxmApiResponse> pollParticipation(@Header(AUTHORIZATION) String token,
                                           @Path("userId") String userId,
                                           @Path("pollId") String pollId,
                                           @Field("optionName") String option,
                                           @Field("olderOption") String olderOption);
    //endregion

    //region UndoPollParticipation
    @POST("qxm/{userId}/undoPollParticipation/{pollId}")
    @FormUrlEncoded
    Call<QxmApiResponse> undoPollParticipation(@Header(AUTHORIZATION) String token,
                                               @Path("userId") String userId,
                                               @Path("pollId") String pollId,
                                               @Field("optionName") String option);
    //endregion

    //region PollIgnite
    @POST("ignite/{userId}/pollIgnite/{pollId}")
    Call<QxmApiResponse> pollIgnite(@Header(AUTHORIZATION) String token,
                                    @Path("userId") String userId,
                                    @Path("pollId") String pollId);
    //endregion

    //region PollIgnite
    @POST("ignite/{userId}/singleQxmIgnite/{singleMCQId}")
    Call<QxmApiResponse> singleMCQIgnite(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId,
                                         @Path("singleMCQId") String singleMCQId);
    //endregion
    //endregion


    // region leaderboard
    @GET("/qxm/{qxmId}/leaderboard/{userId}")
    Call<QxmLeaderboard> getLeaderboardOfSpecificQxm(@Header(AUTHORIZATION) String token,
                                                     @Path("qxmId") String qxmId,
                                                     @Path("userId") String userId);
    // endregion

    // region ActivityLog

    @GET("/activity/{userId}/getActivity")
    Call<ActivityLog> getUserActivityLog(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId);

    // endregion


    // region QxmAppSettings-----ALL-OK

    // region getAppNotificationSettings---OK

    @GET("/users/{userId}/getSettings")
    Call<QxmAppNotificationSettings> getAppNotificationSettings(@Header(AUTHORIZATION) String token,
                                                                @Path("userId") String userId);

    // endregion

    // region updateAppNotificationSettings---OK

    @POST("/users/{userId}/changeSetting/")
    Call<QxmAppNotificationSettings> updateAppNotificationSettings(@Header(AUTHORIZATION) String token,
                                                                   @Path("userId") String userId,
                                                                   @Body SettingsDataContainer settingsDataContainer);

    // endregion

    // endregion

    // region MyQxmPendingEvaluatedList

    @GET("/qxm/{userId}/myQxmEvaluatedList")
    Call<List<EvaluationPendingQxm>> getMyPendingEvaluatedList(@Header(AUTHORIZATION) String token,
                                                               @Path("userId") String userId);

    // endregion

    // region SendFeedBack

    @POST("users/{userId}/feedBack")
    Call<QxmApiResponse> sendFeedback(@Header(AUTHORIZATION) String token,
                                      @Path("userId") String userId,
                                      @Body UserFeedback userFeedback);

    // endregion


    //region getYoutubeVideoDataInJsonFormat

    @GET("https://www.youtube.com/oembed/")
    Call<YoutubeModel> getYoutubeVideoDataInJsonFormat(@Query("url") String youtubeURL,
                                                       @Query("format") String format);

    //endregion

    // region----Analytics-APIs----//

    @POST("/users/{userId}/sessions")
    Call<QxmApiResponse> sendUserActivitySessionTime(@Header(AUTHORIZATION) String token,
                                                     @Path("userId") String userId,
                                                     @Body UserActivitySessionTracker activitySessionTracker);

    // endregion


    //region single multiple choice

    // SINGLE MCQ - CREATE
    @POST("/qxm/{userId}/newSinglemcq")
    Call<QxmApiResponse> createSingleMultipleChoice(@Header(AUTHORIZATION) String token,
                                                    @Path("userId") String userId,
                                                    @Body SingleMCQModel singleMCQModel);

    // SINGLE MCQ - PARTICIPATE
    @GET("/qxm/{singleMCQId}/singleMcqData")
    Call<SingleMCQModel> getSingleMCQModel(@Header(AUTHORIZATION) String token,
                                           @Path("singleMCQId") String singleMCQId);


    // SINGLE MCQ - SUBMIT ANSWER
    @POST("/qxm/{userId}/participationInSingleMcq/{singleMCQId}")
    Call<QxmApiResponse> submitSingleMCQAnswer(@Header(AUTHORIZATION) String token,
                                               @Path("userId") String userId,
                                               @Path("singleMCQId") String singleMCQId,
                                               @Body ParticipateSingleMCQModel participateSingleMCQModel);

    // SINGLE MCQ - FULL RESULT
    @GET("/qxm/{userId}/seeResultSingleMcq/{singleMCQId}")
    Call<FullResultSingleMCQ> getSingleMCQFullResult(@Header(AUTHORIZATION) String token,
                                                     @Path("userId") String userId,
                                                     @Path("singleMCQId") String singleMCQId);

    // SINGLE MCQ - LEADERBOARD
    @GET("/qxm/{singleQxmId}/singleQxmLeaderBoard/{userId}")
    Call<List<SingleMCQLeaderboardItem>> getLeaderboardOfSingleMCQ(@Header(AUTHORIZATION) String token,
                                                                   @Path("singleQxmId") String singleQxmId,
                                                                   @Path("userId") String userId);

    // SINGLE MCQ - EDIT
    @POST("/qxm/{userId}/editSingleQxm/{singleQxmId}")
    Call<QxmApiResponse> updateSingleMultipleChoice(@Header(AUTHORIZATION) String token,
                                                    @Path("userId") String userId,
                                                    @Path("singleQxmId") String singleMCQId,
                                                    @Body SingleMCQModel singleMCQModel);

    // SINGLE MCQ - DELETE
    @POST("/qxm/{userId}/deleteSingleQxm/{singleQxmId}")
    Call<QxmApiResponse> deleteSingleMCQ(@Header(AUTHORIZATION) String token,
                                         @Path("userId") String userId,
                                         @Path("singleQxmId") String singleMCQId);


    //endregion

    //region apk update

    @GET("/qxm/{userId}/checkIfUpdatedApkAvailable")
    Call<QxmApkUpdateResponse> getUpdatedApkStatus(@Header(AUTHORIZATION) String token,
                                                   @Path("userId") String userId,
                                                   @Field("currentApkVersion") String currentApkVersion);
    //endregion


    //region Get Single MCQ - for editing
    @GET("/qxm/{singleQxmId}/specificSingleQxm")
    Call<SingleMCQDetails> getSpecificSingleMCQ(@Header(AUTHORIZATION) String token,
                                                @Path("singleQxmId") String singleMCQId);
    //endregion

    // region PollOverviewFragment- getSinglePoll
    @GET("/qxm/{pollId}/singlePollOverview/{userId}")
    Call<FeedData> getSinglePollOverviewData(@Header(AUTHORIZATION) String token,
                                             @Path("pollId") String pollId,
                                             @Path("userId") String userId);
    // endregion

    // region LoggedInDevice
    @GET("/users/{userId}/allDevices")
    Call<List<LoggedInDevice>> getMyLoggedInDevices(@Header(AUTHORIZATION) String token,
                                                    @Path("userId") String userId);

    @POST("users/{userId}/logoutFromOneDevice")
    @FormUrlEncoded
    Call<QxmApiResponse> logoutFromOneDevice(@Header(AUTHORIZATION) String token,
                                             @Path("userId") String userId,
                                             @Field("macAddress") String macAddress);

    @POST("users/{userId}/logoutFromAllDevice")
    Call<QxmApiResponse> logoutFromAllDevice(@Header(AUTHORIZATION) String token,
                                             @Path("userId") String userId);

    // endregion

}