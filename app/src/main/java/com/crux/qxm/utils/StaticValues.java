package com.crux.qxm.utils;

public class StaticValues {
    // region Login-SignUp
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL = "email";
    public static final String GENDER = "user_gender";
    public static final String PROFILE_PIC_URL = "profile_pic_url";
    public static final String AUTHORIZATION = "Authorization";

    public static final String LOGGED_IN_WITH = "logged_in_with";
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String LOCAL = "local";

    public static final String USER_NOT_VERIFIED = "User Not Verified";

    // endregion

    public static final int CREATE_QXM_REQUEST = 100;
    public static final int EDIT_QXM_REQUEST = 101;
    public static boolean isHomeFeedRefreshingNeeded = false;
    public static boolean isMyQxmFeedRefreshingNeeded = false;
    public static boolean isMyQxmSingleQxmDetailsRefreshingNeeded = false;
    public static boolean isUserProfileFragmentRefreshNeeded = false;
    public static boolean isViewQxmGroupFragmentRefreshNeeded = false;
    public static boolean isMyQxmGroupFragmentRefreshNeeded = false;

    public static boolean isFeedSingleMCQItemClicked = false;
    public static boolean isMyQxmFeedSingleMCQItemClicked = false;


    //region Feed
    public static final String FEED_DATA_PASS_TO_QUESTION_OVERVIEW = "feed_data_pass";
    public static final String FEED_DATA_PASS_TO_MY_QXM_DETAILS = "feed_data_pass_my_qxm_details";
    public static final String FEED_DATA_PASS_TO_MY_SINGLE_MCQ_DETAILS = "feed_data_pass_my_single_mcq_details";
    public static final String FEED_VIEW_TYPE_POLL = "Poll";
    public static final String FEED_VIEW_TYPE_SINGLE_MCQ = "SingleMCQ";
    public static final String FEED_VIEW_TYPE_QXM = "Qxm";

    // endregion

    // region CreateQxm
    public static final String QUESTION_TYPE_MULTIPLE_CHOICE = "question_multiple_choice";
    public static final String QUESTION_TYPE_FILL_IN_THE_BLANKS = "question_fill_in_the_blank";
    public static final String QUESTION_TYPE_SHORT_ANSWER = "question_short_answer";

    public static final String PARCELABLE_QUESTION_SET = "parcelable_question_set";
    public static final String PARCELABLE_QUESTION_SETTINGS = "question_settings";
    // endregion

    // region QxmAppNotificationSettings
    public static final String QXM_TIME_TYPE_NO_LIMIT = "no_limit";
    public static final String QXM_TIME_TYPE_LIMITED = "limited";

    public static final String QXM_PRIVACY_PUBLIC = "public";
    public static final String QXM_PRIVACY_PRIVATE = "private";

    public static final String QUESTION_STATUS_PUBLISHED = "published";
    public static final String QUESTION_STATUS_DRAFTED = "drafted";

    public static final String QXM_START_SCHEDULE_CHOOSE_DATE_TIME = "qxm_start_schedule";
    public static final String QXM_EXPIRATION_DATE_TIME = "qxm_expiration_date";
    public static final String CONTEST_START_CHOOSE_DATE_TIME = "contest_start_time";
    public static final String CONTEST_END_CHOOSE_DATE_TIME = "contest_end_time";
    public static final String CORRECT_ANSWER_VISIBILITY_DATE_TIME = "correct_answer_visibility_time";
    public static final String LEADERBOARD_PUBLISH_CHOOSE_DATE_TIME = "leaderboard_publish_time";

    public static final String QXM_START_INSTANTLY = "instantly";
    public static final String QXM_START_BY_DATE = "by_date";
    public static final String QXM_START_MANUAL = "manual";

    public static final String QXM_CURRENT_STATUS_NOT_STARTED = "not_started";
    public static final String QXM_CURRENT_STATUS_RUNNING = "running";
    public static final String QXM_CURRENT_STATUS_ENDED = "ended";
    public static final String QXM_CURRENT_STATUS_STOPPED = "stopped";

    public static final String CONTEST_START_INSTANTLY = "instantly";
    public static final String CONTEST_START_BY_DATE = "by_date";
    public static final String CONTEST_START_MANUAL = "manual";

    public static final String CORRECT_ANSWER_VISIBILITY_AFTER_CONTEST_ENDS = "contest_end";
    public static final String CORRECT_ANSWER_VISIBILITY_IMMEDIATELY = "instantly";
    public static final String CORRECT_ANSWER_VISIBILITY_AFTER_QXM_EXPIRES = "qxm_expires";
    public static final String CORRECT_ANSWER_VISIBILITY_BY_DATE = "by_date";
    public static final String QUESTION_VISIBILITY_AFTER_PARTICIPATION = "after_participation";
    public static final String QUESTION_VISIBILITY_BEFORE_PARTICIPATION = "before_participation";


    public static final String CONTEST_END_WITH_EXAM_DURATION = "exam_duration";
    public static final String CONTEST_END_BY_DATE = "by_date";

    public static final String EVALUATE_AUTOMATIC = "automatic";
    public static final String EVALUATE_MANUAL = "manual";
    public static final String EVALUATE_SEMI_AUTO = "semi_auto";

    public static final String LEADERBOARD_PUBLISH_TYPE_CONTINUE = "continue";
    public static final String LEADERBOARD_PUBLISH_TYPE_BY_DATE = "by_date";
    public static final String LEADERBOARD_PUBLISH_TYPE_QXM_EXPIRES = "qxm_expires";
    public static final String LEADERBOARD_PUBLISH_TYPE_CONTEST_END = "contest_ends";

    public static final String LEADERBOARD_PRIVACY_PUBLIC = "public";
    public static final String LEADERBOARD_PRIVACY_PARTICIPANTS = "participants";
    // endregion

    // region FragmentTag
    public static final String TAG_USER_PROFILE_FRAGMENT = "user_profile_fragment";
    public static final String TAG_EDIT_PROFILE_FRAGMENT = "edit_profile_fragment";
    public static final String TAG_CREATE_QXM_SETTINGS_FRAGMENT = "create_qxm_fragment";
    public static final String TAG_QXM_SETTINGS_FRAGMENT = "qxm_settings_fragment";
    public static final String TAG_PARTICIPATE_QUESTION_FRAGMENT = "participate_question_fragment";
    public static final String TAG_QUESTION_OVERVIEW = "questionOverViewFragment";
    public static final String TAG_EVALUATION_FRAGMENT = "evaluation_fragment";
    public static final String TAG_MY_QXM_SINGLE_QXM_PENDING_EVALUATION_FRAGMENT = "myQxm_single_qxm_pending_evaluation_evaluation_fragment";
    public static final String TAG_FULL_RESULT_FRAGMENT = "full_result_fragment";
    public static final String TAG_FOLLOWING_LIST_FRAGMENT = "following_list_fragment";
    public static final String TAG_CREATE_GROUP_FRAGMENT = "create_group_fragment";
    public static final String TAG_VIEW_QXM_GROUP_FRAGMENT = "view_qxm_group_fragment";

    public static final String TAG_ENROLL_FRAGMENT = "enroll_fragment";
    public static final String TAG_FOLLOWING_FRAGMENT = "following_fragment";
    public static final String TAG_MY_QXM_FRAGMENT = "my_qxm_fragment";
    public static final String TAG_CREATE_QXM_FRAGMENT = "create_qxm_fragment";
    public static final String TAG_HOME_FRAGMENT = "home_fragment";
    // endregion


    // region ImageUploadKeys
    public static final String ON_ACTIVITY_CALL_FOR_Q_SET = "q_set_on_activity_result";
    public static final String ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION = "single_question_on_activity_result";
    public static final String ON_ACTIVITY_CALL_FOR_GROUP_IMAGE = "group_image";
    public static final String ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL = "onActivity_call_for_poll_thumbnail";
    public static final String ON_ACTIVITY_CALL_FOR_PROFILE_PICTURE = "onActivity_call_for_profile_picture";
    public static final String ON_ACTIVITY_CALL_FOR_GROUP_IMAGE_CHANGE = "onActivity_call_for_group_image_change";
    public static final String ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL = "onActivity_call_for_single_multiple_choice_thumbnail";
    // endregion

    // region ParcelableKeys

    public static final String QXM_MODEL_KEY = "qxm_model";
    public static final String QUESTION_SET_KEY = "question_set_key";
    public static final String QUESTION_SETTINGS_KEY = "question_settings_key";
    public static final String QXM_DRAFT_KEY = "qxm_draft";
    public static final String QXM_EDIT_KEY = "qxm_edit";
    public static final String QXM_DRAFT_ID = "qxm_draft_id";
    public static final String EVALUATION_DETAILS_KEY = "evaluation_details";
    public static final String PARTICIPATION_ID_KEY = "participation_Id";
    public static final String USER_BASIC_KEY = "user_basic";
    public static final String USER_PERFORMANCE_KEY = "user_performance";
    public static final String FULL_NAME_KEY = "full_name";
    public static final String PROFILE_IMAGE_KEY = "profile_image";
    public static final String FULL_RESULT_KEY = "full_result";
    public static final String FEED_DATA_KEY = "feed_data";
    public static final String USER_ID_KEY = "user_id";
    public static final String USER_FULL_NAME = "user_full_name";

    public static final String QXM_QUESTION_SET_TITLE = "question_set_title";
    public static final String QXM_CREATOR_NAME_KEY = "qxm_creator_name";
    public static final String QXM_CREATION_TIME_KEY = "qxm_creation_time";
    public static final String QXM_PRIVACY_KEY = "qxm_privacy";
    public static final String QXM_LEADERBOARD_PRIVACY_KEY = "qxm_leader_board_privacy";
    public static final String SINGLE_MCQ_LEADER_BOARD_PRIVACY = "single_mcq_leader_board_privacy";
    public static final String SINGLE_MCQ_LEADER_BOARD_PRIVACY_PUBLIC = "public";
    public static final String SINGLE_MCQ_LEADER_BOARD_PRIVACY_PARTICIPANTS = "participants";
    public static final String SINGLE_MCQ_CREATOR_ID = "single_qxm_creator_id";

    public static final String SINGLE_MCQ_MODEL_KEY = "single_mcq_model";
    public static final String POLL_ID_KEY = "poll_id";

    //endregion

    // QuestionOverViewFragment
    public static final String VIEW_FOR_KEY = "view_for";
    public static final String VIEW_FOR_SEE_QUESTION = "see_question";
    public static final String VIEW_FOR_PARTICIPATE_QXM = "participate_qxm";
    public static final String VIEW_FOR_PARTICIPATE_SINGLE_MCQ = "participate_single_mcq";

    // Following
    public static final String FOLLOWING_NOTIFICATION_OCCASIONALLY = "Occasionally";
    public static final String FOLLOWING_NOTIFICATION_FREQUENTLY = "Frequently";
    public static final String FOLLOWING_NOTIFICATION_NONE = "None";
    public static final String FOLLOWING_NOTIFICATION_OFF = "Off";

    public static final String ENROLLED_LIST_FRAGMENT_BUNDLE_DATA = "enrolled_list_fragment_bundle_data";
    public static final String PARTICIPATED_LIST_FRAGMENT_BUNDLE_DATA = "participated_list_fragment_bundle_data";
    public static final String PENDING_EVALUATION_LIST_FRAGMENT_BUNDLE_DATA = "pending_evaluation_list_fragment_bundle_data";

    // Group
    public static final String GROUP_ID_KEY = "groupId";
    public static final String GROUP_NAME_KEY = "group_name";
    public static final String MEMBER_STATUS_ADMIN = "Admin";
    public static final String MEMBER_STATUS_MODERATOR = "Moderator";
    public static final String MEMBER_STATUS_MEMBER = "Member";
    public static final String MEMBER_STATUS_REQUEST_PENDING = "PendingUser";
    public static final String MEMBER_STATUS_NOT_MEMBER = "NotMember";
    public static final String MEMBER_STATUS = "member_status";
    public static final String MY_STATUS_KEY = "myStatus";

    public static final String FREQUENTLY = "frequently";
    public static final String OCCASIONALLY = "occasionally";
    public static final String OFF = "off";

    public static final String PRIVACY_PUBLIC = "public";
    public static final String PRIVACY_PRIVATE = "private";

    // region Notification

    public static final String CUSTOM_KEY = "custom_key";
    public static final String CHANNEL_KEY = "channel_key";
    public static final String NOTIFICATION_BUNDLE = "notification_bundle";
    public static final String NOTIFICATION_FOLLOWER_ID = "followerId";
    public static final String NOTIFICATION_FOLLOWER_FULL_NAME = "followerFullName";
    public static final String NOTIFICATION_GROUP_ID_KEY = "groupId";
    public static final String NOTIFICATION_GROUP_NAME_KEY = "group_name";
    public static final String NOTIFICATION_QXM_ID = "qxmId";
    public static final String QXM_ID_KEY = "qxmId";
    public static final String QXM_CREATOR_ID_KEY = "qxm_creator_id";
    public static final String SINGLE_MCQ_ID_KEY = "singleMCQId";
    public static final String NOTIFICATION_USER_ID = "userId";
    public static final String NOTIFICATION_PARTICIPATION_ID = "participationId";
    public static final String NOTIFICATION_PARTICIPATOR_NAME = "participatorName";
    public static final String PARTICIPATOR_NAME_KEY = "participatorName";
    public static final String PARTICIPATOR_LIST_FOR_KEY = "participatorListFor";
    public static final String QXM = "qxm";
    public static final String SINGLE_MCQ = "singleMcq";


    // Notification-ChannelIds
    public static final String CHANNEL_FOLLOWER_NAME = "Follow";
    public static final String CHANNEL_QXM_NAME = "Qxm";
    public static final String CHANNEL_PRIVATE_QXM_NAME = "Private Qxm";
    public static final String CHANNEL_ENROLL_NAME = "Enroll Request";
    public static final String CHANNEL_EVALUATION_NAME = "Evaluation";
    public static final String CHANNEL_RESULT_NAME = "Result";
    public static final String CHANNEL_GROUP_JOIN_REQUEST_NAME = "Group Join Request";
    public static final String CHANNEL_GROUP_INVITE_REQUEST_NAME = "Invite Request To User";
    public static final String CHANNEL_GROUP_NAME = "Group";


    public static final String SINGLE_QXM_DETAILS_TITLE_KEY = "single_qxm_details_title";
    public static final String SINGLE_SINGLE_MCQ_DETAILS_TITLE_KEY = "single_qxm_details_title";

    public static final String GROUP_NOTIFICATION_STATUS_INVITE_REQUEST = "Invite Request To User";
    public static final String GROUP_NOTIFICATION_STATUS_JOIN_REQUEST = "Join Request";
    public static final String GROUP_NOTIFICATION_STATUS_JOIN_REQUEST_ACCEPTED = "Join Request Accepted";

    // Notification-Ids
//    public static final int CHANNEL_FOLLOWER_NAME = 1000;
//    public static final int CHANNEL_QXM_NAME = 2000;
//    public static final int CHANNEL_PRIVATE_QXM_NAME = 3000;
//    public static final int CHANNEL_ENROLL_NAME = 4000;
//    public static final int CHANNEL_EVALUATION_NAME = 5000;
//    public static final int CHANNEL_RESULT_NAME = 6000;
//    public static final int CHANNEL_GROUP_NAME = 7000;
//    public static final int CHANNEL_JOIN_REQUEST_ID = 7100;

    // endregion

    // region AllNotificationStatus

    public static final String NOTIFICATION_STATUS_QXM_CREATED = "New Qxm Created";
    public static final String NOTIFICATION_STATUS_QXM_EDITED = "Qxm Edited";
    public static final String NOTIFICATION_STATUS_POLL_CREATED = "New Poll Created";
    public static final String NOTIFICATION_STATUS_SINGLE_MCQ_CREATED = "New SingleMCQ Created";
    public static final String NOTIFICATION_STATUS_SINGLE_MCQ_EDITED = "SingleQxm Edited";
    public static final String NOTIFICATION_STATUS_ENROLL_REQUEST = "Enroll";
    public static final String NOTIFICATION_STATUS_EVALUATION = "Evaluation";
    public static final String NOTIFICATION_STATUS_FOLLOW = "Follow";
    public static final String NOTIFICATION_STATUS_RESULT = "Result";
    public static final String NOTIFICATION_STATUS_GROUP_JOIN_REQUEST = "Group Join Request";
    public static final String NOTIFICATION_STATUS_GROUP_JOIN_REQUEST_ACCEPTED = "Group Join Accpted";
    public static final String NOTIFICATION_STATUS_GROUP_INVITE_REQUEST_TO_USER = "Invite Request To User";

    //region search

    //searchView query hint message
    public static final String SEARCH_USER = "Search user...";
    public static final String SEARCH_QXM = "Search qxm...";
    public static final String SEARCH_GROUP = "Search group...";
    public static final String SEARCH_SINGLE_MCQ = "Search single mcq...";
    public static final String SEARCH_POLL = "Search poll...";
    public static final String SEARCH_LIST = "Search list...";
    public static final String YOUTUBE_LINK_KEY = "youtube_link_key";

    //selected search item
    public enum SearchCategory {
        USER,
        QXM,
        GROUP,
        SingleMCQ,
        Poll,
        List
    }

    //endregion

    public static final String LINK_CATEGORY_QXM = "Qxm";
    public static final String LINK_CATEGORY_USER = "User";
    public static final String LINK_CATEGORY_GROUP = "Group";
    public static final String LINK_CATEGORY_POLL = "Poll";
    public static final String LINK_CATEGORY_SINGLE_MCQ = "Single MCQ";

    public static final String SHARED_CATEGORY_QXM = "Qxm";
    public static final String SHARED_CATEGORY_POLL = "Poll";
    public static final String SHARED_CATEGORY_SINGLE_MCQ = "Single MCQ";

    //region list

    public static final String LIST_PRIVACY_PUBLIC = "Public";
    public static final String LIST_PRIVACY_PRIVATE = "Private";
    public static final String LIST_ID = "list_id";
    public static final String LIST_TITLE = "list_title";

    //endregion

    //region Toast
    public static final String TOAST_MSG_NO_INTERNET = "No internet connection, please connect with internet!";
    //endregion

    // region ActivityLogOld

    public static final String ACTIVITY_FOLLOW = "Follow";

    public static final String ACTIVITY_QXM_CREATION = "QxmCreation";
    public static final String ACTIVITY_SINGLE_MCQ_CREATED = "Single MCQ Created";

    public static final String ACTIVITY_QXM_EDITED = "QxmEdited";
    public static final String ACTIVITY_SINGLE_MCQ_EDITED = "SingleQxmEdited";

    public static final String ACTIVITY_QXM_PARTICIPATION = "QxmParticipation";
    public static final String ACTIVITY_PARTICIPATION = "Participation";
    public static final String ACTIVITY_SINGLE_MCQ_PARTICIPATION = "Participation In Single Mcq";

    public static final String ACTIVITY_ENROLL_REQUEST_SENT = "EnrollRequestSent";
    public static final String ACTIVITY_ENROLL_REQUEST_ACCEPTED = "enrollAccepted";
    public static final String ACTIVITY_EVALUATION_CONFIRM = "EvaluationConfirm";

    public static final String ACTIVITY_IGNITE = "Ignite";
    public static final String ACTIVITY_IGNITE_SINGLE_MCQ = "IgniteSingleQxm";


    public static final String ACTIVITY_CREATE_LIST = "ListCreate";

    public static final String ACTIVITY_GROUP_CREATE = "GroupCreate";
    public static final String ACTIVITY_GROUP_JOIN_REQUEST = "GroupJoinReq";
    public static final String ACTIVITY_GROUP_ACCEPT_JOIN_REQUEST = "AcceptGroupJoinReq";
    public static final String ACTIVITY_GROUP_ADD_MEMBER = "AddMember";
    public static final String ACTIVITY_SHARE_QXM_TO_GROUP = "ShareQxmToGroup";


    // endregion

    public static final String VIEW_PAGER_POSITION = "view_pager_position";

    // region QR Code

    public static final String QR_CODE_FOR_GROUP = "Group";
    public static final String QR_CODE_FOR_QXM = "Qxm";
    public static final String QR_CODE_FOR_USER = "User";

    public static final int QR_CODE_WIDTH = 500;
    public static final int QR_CODE_HEIGHT = 500;

    // endregion

    // region Report

    public static final String REPORT_QXM = "report_qxm";
    public static final String REPORT_GROUP = "report_group";
    public static final String REPORT_USER = "report_user";
    public static final String REPORT_POLL = "report_poll";
    public static final String REPORT_SINGLE_MCQ = "report_single_mcq";

    // Report Category
    public static final String REPORT_CATEGORY_ADULT_CONTENT = "adult_content";
    public static final String REPORT_CATEGORY_OFFENSIVE_CONTENT = "offensive_content";
    public static final String REPORT_CATEGORY_SPAM_CONTENT = "spam_content";

    // endregion


    //region poll
    public static final String POLL_PRIVACY_PUBLIC = "public";
    public static final String POLL_PRIVACY_PRIVATE = "private";
    public static final String POLL_SHARED_ON_FEED = "Feed";
    public static final String POLL_TYPE_PUBLISHED = "published";
    public static final String POLL_DATA_KEY = "poll_data";
    //endregion

    //region event bus message
    public static final String ERROR_OCCURRED_DURING_PHOTO_UPLOAD = "error_occurred_during_photo_upload";
    public static final String STARTING_PHOTO_UPLOAD = "starting_photo_upload";
    public static final String IMAGE_UPLOAD_SUCCESS = "image_upload_success";

    //endregion


    //region onActivityResult
    public static String onActivityCall = "";
    //endregion

    // region filter
    public static final String FILTER_ALL_QXMS = "all_qxms";
    public static final String FILTER_PUBLIC_QXMS = "public_qxms";
    public static final String FILTER_PRIVATE_QXMS = "private_qxms";
    public static String CURRENT_FILTER = FILTER_ALL_QXMS;

    // endregion

    // region Feedback

    public static final int UGLY = 1;
    public static final int BAD = 2;
    public static final int AVERAGE = 3;
    public static final int GOOD = 4;
    public static final int AWESOME = 5;
    public static final String COMMENT = "Comment";
    public static final String BUG = "Bug";
    public static final String OTHERS = "Others";

    // endregion


    //region youtube api key

    public static final String YOUTUBE_API_KEY = "AIzaSyBV5AjYPRQI-J1CdkXvKi_YUi3xDOjmh4g";

    //endregion


    //region fragment name (declaring for event bus)

    public static final String PHOTO_UPLOAD_REASON_QSET_THUMBNAIL = "photo_upload_reason_thumbnail";
    public static final String PHOTO_UPLOAD_REASON_SINGLE_QUESTION_THUMBNAIL = "photo_upload_reason_single_question_thumbnail";

    //endregion

    //region view Text

    public static final String REMOVE_THUMBNAIL = "Remove Thumbnail";
    public static final String ADD_THUMBNAIL = "Add Thumbnail";
    public static final String ADD_YOUTUBE_VIDEO = "Add YouTube Video";
    public static final String REMOVE_YOUTUBE_VIDEO = "Remove YouTube Video";

    //endregion

    public static final String SINGLE_MCQ_TIME_TYPE_NO_LIMIT = "no_limit";
    public static final String MCQ_EXPIRATION_DATE_TIME = "mcq_expiration_date";


    //region DeepLinkingURI

    public static final String URI_QXM = "qxm";
    public static final String URI_USER = "user";
    public static final String URI_GROUP = "group";
    public static final String URI_POLL = "poll";
    public static final String URI_SINGLE_MCQ = "singleMCQ";
    //endregion


    //region INPUT FIELD LENGTH (also exist in integer.xml,
    // if you change the following value, please update in integer.xml file.)
    // create qxm
    public static final int MAX_LENGTH_OF_QXM_TITLE = 200;
    public static final int MAX_LENGTH_OF_QXM_DESCRIPTION = 500;
    public static final int MAX_LENGTH_OF_QUESTION_TITLE = 300;
    public static final int MAX_LENGTH_OF_FILL_IN_THE_BLANKS_QUESTION_TITLE = 500;
    public static final int MAX_LENGTH_OF_SHORT_ANSWER_QUESTION_TITLE = 200;
    public static final int MAX_LENGTH_OF_TAG = 200;
    public static final int MAX_LENGTH_OF_QUESTION_DESCRIPTION = 200;
    public static final int MAX_LENGTH_OF_QUESTION_HIDDEN_DESCRIPTION = 200;

    public static final int MAX_LENGTH_OF_POINT_IN_DIGIT = 2;
    public static final int MAX_LENGTH_OF_CHARACTER_LIMIT_OF_SHORT_ANSWER_IN_DIGIT = 3;
    //endregion

    //region qxm image server root URL
    public static final String IMAGE_SERVER_ROOT = "https://qxmspace.nyc3.cdn.digitaloceanspaces.com/qxm/image/image/";
    public static final String YOUTUBE_IMAGE_ROOT = "i.ytimg.com";
    public static final String FACEBOOK_IMAGE_ROOT = "facebook.com";
    public static final String GOOGLE_IMAGE_ROOT = "googleusercontent";
    //endregion

    //region group privacy
    public static final String GROUP_PRIVACY_PUBLIC = "Public";
    public static final String GROUP_PRIVACY_PRIVATE = "Private";
    //endregion

    //region shared preference
    public static final String SHARED_PREF_NAME = "qxm_shared_pref";
    public static final String SHARED_PREF_USER_EMAIL_KEY = "user_email";
    public static final String SHARED_PREF_USER_PASS_KEY = "user_pass";
    public static final String SHARED_PREF_CHECK_STATUS = "check_box_status";
    //endregion

    public  static  int isfirsttimeentered=1;

}

