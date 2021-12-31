package com.crux.qxm.views.fragments.createQxmActivityFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.TemplateQuestionAdapter;
import com.crux.qxm.db.models.createQxm.YoutubeModel;
import com.crux.qxm.db.models.questions.FillInTheBlanks;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.Question;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.crux.qxm.db.models.questions.QuestionSettings;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.models.questions.ShortAnswer;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.db.sampleModels.QxmSettingsModel;
import com.crux.qxm.di.createQxmFragmentFeature.CreateQxmFragmentComponent;
import com.crux.qxm.di.createQxmFragmentFeature.DaggerCreateQxmFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.postQxmNetworkCall.PostQxmToBackEndNetworkCall;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.KeyboardChecker;
import com.crux.qxm.utils.OnBackPressed;
import com.crux.qxm.utils.QxmArrayListToStringProcessor;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.inputValidations.InputValidationHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.crux.qxm.utils.StaticValues.ADD_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.ADD_YOUTUBE_VIDEO;
import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_Q_SET;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SET;
import static com.crux.qxm.utils.StaticValues.PARCELABLE_QUESTION_SETTINGS;
import static com.crux.qxm.utils.StaticValues.QUESTION_TYPE_MULTIPLE_CHOICE;
import static com.crux.qxm.utils.StaticValues.QXM_DRAFT_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_MODEL_KEY;
import static com.crux.qxm.utils.StaticValues.REMOVE_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.REMOVE_YOUTUBE_VIDEO;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateQxmFragment extends Fragment implements OnBackPressed {

    private static final String TAG = "CreateQxmFragment";
    public static String questionThumbnailImage;
    public static String youtubeThumbnailURL;
    public static boolean isYoutubeURLValid = false;

    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    @BindView(R.id.rvTemplateQuestion)
    RecyclerView rvTemplateQuestion;
    @BindView(R.id.llcAdQuestion)
    LinearLayoutCompat llcAdQuestion;
    @BindView(R.id.ivDoneCreatingQuestion)
    AppCompatImageView ivDoneCreatingQuestion;
    @BindView(R.id.etQuestionSetTitle)
    AppCompatEditText etQuestionSetTitle;
    @BindView(R.id.etQuestionSetDescription)
    AppCompatEditText etQuestionSetDescription;
    @BindView(R.id.llAddThumbnail)
    LinearLayoutCompat llAddThumbnail;
    @BindView(R.id.llAddYouTubeVideo)
    LinearLayoutCompat llAddYouTubeVideo;
    @BindView(R.id.ivQuestionSetOptions)
    AppCompatImageView ivQuestionSetOptions;
    @BindView(R.id.ivQuestionSetThumbnail)
    AppCompatImageView ivQuestionSetThumbnail;
    TemplateQuestionAdapter templateQuestionAdapter;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    //endregion
    @BindView(R.id.youTubeThumbnailView)
    YouTubeThumbnailView youTubeThumbnailView;
    @BindView(R.id.etYouTubeLink)
    AppCompatEditText etYouTubeLink;
    @BindView(R.id.tvYouTubeTitle)
    AppCompatTextView tvYouTubeTitle;
    @BindView(R.id.llcYouTubeThumbnailAndTitleContainer)
    LinearLayoutCompat llcYouTubeThumbnailAndTitleContainer;
    @BindView(R.id.tvAddDeleteThumbnail)
    AppCompatTextView tvAddDeleteThumbnail;
    @BindView(R.id.tvAddDeleteYouTube)
    AppCompatTextView tvAddDeleteYouTube;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;

    String youTubeLink;
    String youTubeVideoID;
    private UserBasic user;
    private ImageProcessor imageProcessorForQSetThumbnail;
    private ImageProcessor imageProcessorForSingleQuestion;
    private Context context;
    private ArrayList<Question> questionArrayList;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmDraft qxmDraft;
    private QxmModel qxmModel;
    private QuestionSettings questionSettings;
    //region fields for receiving photo upload click listener data
    private ArrayList<Question> handledQuestionArrayList;
    private int handledQuestionPosition;
    private int handledQuestionTypeIndex;

    //    private YouTubeThumbnailView.OnInitializedListener initializedListener;
    private int maxLengthQuestionSetTitle;
    private int maxLengthQuestionSetDescription;
    private RealmService realmService;

    private boolean readyForLoadingYoutubeThumbnail = true;


    // region Fragment-Constructor
    public CreateQxmFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Fragment-newInstance
    public static CreateQxmFragment newInstance(QxmDraft qxmDraft) {

        Bundle args = new Bundle();
        args.putParcelable(QXM_DRAFT_KEY, qxmDraft);
        CreateQxmFragment fragment = new CreateQxmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CreateQxmFragment newInstance(QxmModel qxmModel) {

        Bundle args = new Bundle();
        args.putParcelable(QXM_MODEL_KEY, qxmModel);
        CreateQxmFragment fragment = new CreateQxmFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Override-OnStart
    @Override
    public void onStart() {
        super.onStart();

        // registering event bus
        if (!EventBus.getDefault().isRegistered(this)) {
            // registering event bus
            EventBus.getDefault().register(this);
        }

    }
    // endregion

    // region Override-OnCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_qxm, container, false);

        // binding butterKnife with this fragment
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // region init

        setUpDagger2(getActivity());
        context = view.getContext();
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_Q_SET;

        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);

        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        apiService = retrofit.create(QxmApiService.class);
        user = realmService.getSavedUser();
        token = realmService.getApiToken();

        maxLengthQuestionSetTitle = context.getResources().getInteger(R.integer.maxLengthQxmTitle);
        maxLengthQuestionSetDescription = context.getResources().getInteger(R.integer.maxLengthQxmDescription);

        InputValidationHelper inputValidationHelperTitle = new InputValidationHelper(context);

        inputValidationHelperTitle.showEditTextMaximumCharacterReachedMessage(
                etQuestionSetTitle,
                maxLengthQuestionSetTitle,
                String.format(context.getString(R.string.toast_message_qxm_title_reached_maximum_length) + " %s.", maxLengthQuestionSetTitle)
        );

        InputValidationHelper inputValidationHelperDescription = new InputValidationHelper(context);
        inputValidationHelperDescription.showEditTextMaximumCharacterReachedMessage(
                etQuestionSetDescription,
                maxLengthQuestionSetDescription,
                String.format(context.getString(R.string.toast_message_qxm_description_reached_maximum_length) + " %s.", maxLengthQuestionSetDescription)
        );

        // region QxmDraft

        qxmDraft = getArguments() != null ? getArguments().getParcelable(QXM_DRAFT_KEY) : null;
        if (qxmDraft != null) {
            Log.d(TAG, "onViewCreated: qxmDraft: " + qxmDraft.toString());
            if (qxmDraft.getQuestionSetJson() != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<QxmModel>() {
                }.getType();
                QxmModel qxmModel = gson.fromJson(qxmDraft.getQuestionSetJson(), type);

                setParcelableDataIntoModel(qxmModel);
            }
        } else {
            Log.d(TAG, "onViewCreated: qxmDraft is null");

            qxmModel = getArguments() != null ? getArguments().getParcelable(QXM_MODEL_KEY) : null;
            if (qxmModel != null) {
                Log.d(TAG, "onViewCreated: QXM_MODEL_KEY not null");
                setParcelableDataIntoModel(qxmModel);
            }
        }

        // endregion

        // when we are coming to this fragment for the first time, question array list is null
        // So, we are initializing adapter with new array list
        if (questionArrayList == null)
            initAdapter();

            //if we are moving forward from this fragment and back back to this fragment,
            //it means previous question array list is available till now
            // so we are loading previous list
        else loadFromPreviousData();
        // endregion

        //checking if keyboard is open or not
        KeyboardChecker.isKeyboardOpen(view);

        // region AddThumbnail

        llAddThumbnail.setOnClickListener(v -> {
            //let user adding new image
            if (tvAddDeleteThumbnail.getText().toString().equals(ADD_THUMBNAIL)) {

                youtubeThumbnailURL = "";
                StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_Q_SET;
                imageProcessorForQSetThumbnail = new ImageProcessor(context, CreateQxmFragment.this, TAG, apiService, token, ivQuestionSetThumbnail);
                imageProcessorForQSetThumbnail.pickImageFromGallery();
                tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);
                tvAddDeleteYouTube.setText(ADD_YOUTUBE_VIDEO);

                if (etYouTubeLink.getVisibility() == View.VISIBLE)
                    etYouTubeLink.setText("");

                etYouTubeLink.setVisibility(GONE);
                if (llcYouTubeThumbnailAndTitleContainer.getVisibility() == VISIBLE) {
                    llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                    tvAddDeleteThumbnail.setText(ADD_YOUTUBE_VIDEO);
                }


            } // image is uploaded and user wants to remove it
            else if (tvAddDeleteThumbnail.getText().toString().equals(REMOVE_THUMBNAIL)) {

                questionThumbnailImage = "";
                ivQuestionSetThumbnail.setVisibility(GONE);
                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);

            }

        });

        // endregion

        // region AddYouTubeVideo

        llAddYouTubeVideo.setOnClickListener(v -> {


            if (tvAddDeleteYouTube.getText().toString().equals(ADD_YOUTUBE_VIDEO)) {

                questionThumbnailImage = "";
                etYouTubeLink.setVisibility(View.VISIBLE);
                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);
                tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                ivQuestionSetThumbnail.setVisibility(GONE);


            } else if (tvAddDeleteYouTube.getText().toString().equals(REMOVE_YOUTUBE_VIDEO)) {

                youtubeThumbnailURL = "";
                etYouTubeLink.setText("");
                etYouTubeLink.setVisibility(GONE);
                llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                tvAddDeleteYouTube.setText(ADD_YOUTUBE_VIDEO);

            }


        });


        etYouTubeLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                youTubeLink = etYouTubeLink.getText().toString().trim();
                getYoutubeVideo(youTubeLink);
            }
        });


        // endregion

        //region question set option menu
/*
        ivQuestionSetOptions.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(Objects.requireNonNull(getActivity()), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_question_set_options, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.action_add_thumbnail:


                            StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_Q_SET;
                            Log.d(TAG, "onMenuItemClick: " + getActivity().getClass().getSimpleName());
                            imageProcessorForQSetThumbnail = new ImageProcessor(context, CreateQxmFragment.this, TAG, apiService, user, ivQuestionSetThumbnail);
                            imageProcessorForQSetThumbnail.pickImageFromGallery();
                            break;

                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }

                    return false;
                }
            });
            popup.show();
        });*/

        //endregion

        // region after clicking done
        ivDoneCreatingQuestion.setOnClickListener(v -> {

            Log.d(TAG, "onViewCreated ivDoneCreatingQuestion: clicked");

            // region if everything is ok,go to next fragment
            if (isQuestionSetValid()) {
                Log.d(TAG, "QuestionSet validation: everything is ok.. ");
                Bundle args = new Bundle();
                args.putParcelable(PARCELABLE_QUESTION_SET, getQuestionSet());
                if (questionSettings != null) {
                    //Log.d(TAG, "PARCELABLE_QUESTION_SETTINGS: " + questionSettings.toString());
                    args.putParcelable(PARCELABLE_QUESTION_SETTINGS, questionSettings);
                    //args.putString(QXM_DRAFT_ID, qxmDraft.getId());
                    args.putParcelable(QXM_DRAFT_KEY, qxmDraft);
                } else {
                    Log.d(TAG, "QuestionSettings is empty");
                }

                QxmSettingsFragment qxmSettingsFragment = new QxmSettingsFragment();
                qxmSettingsFragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, qxmSettingsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                Log.d(TAG, "onViewCreated: ivDoneClick: ");
            } else {
                Log.d(TAG, "onViewCreated ivDoneCreateQuestion : Question is not valid");
            }

            // endregion


        });

        //endregion

        // region handling single question setOnPhotoUploadCLickListener
        // In this listener we are passing three parameters

        templateQuestionAdapter.setOnPhotoUploadCLickListener((questionArrayList, position, questionTypeIndex) -> {

            handledQuestionArrayList = questionArrayList;
            handledQuestionPosition = position;
            handledQuestionTypeIndex = questionTypeIndex;


            Log.d(TAG, "onViewCreated: onPhotoUploadListenerCalled");

        });
        //endregion

        // pop this fragment when press back arrow
        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

    private void setParcelableDataIntoModel(QxmModel qxmModel) {

        Log.d(TAG, "setParcelableDataIntoModel: called");

        questionArrayList = (ArrayList<Question>) qxmModel.getQuestionSet().getQuestions();
        etQuestionSetTitle.setText(qxmModel.getQuestionSet().getQuestionSetTitle());
        etQuestionSetDescription.setText(qxmModel.getQuestionSet().getQuestionSetDescription());

        if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetYoutubeLink())) {

            tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
            etYouTubeLink.setText(qxmModel.getQuestionSet().getQuestionSetYoutubeLink());
            etYouTubeLink.setVisibility(VISIBLE);
            getYoutubeVideo(qxmModel.getQuestionSet().getQuestionSetYoutubeLink());

        } else if (!TextUtils.isEmpty(qxmModel.getQuestionSet().getQuestionSetThumbnail())) {

            tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);
            ivQuestionSetThumbnail.setVisibility(View.VISIBLE);

            if (!qxmModel.getQuestionSet().getQuestionSetThumbnail().contains(YOUTUBE_IMAGE_ROOT)) {
                String imageUrl = IMAGE_SERVER_ROOT.concat(qxmModel.getQuestionSet().getQuestionSetThumbnail());
                picasso.load(imageUrl).into(ivQuestionSetThumbnail);
            }
        }

        questionSettings = qxmModel.getQuestionSettings();

        Log.d(TAG, "onViewCreated: ==============================\n\n" +
                questionSettings.toString() +
                "\n\n==============================");
    }

    private void getYoutubeVideo(String youTubeLink) {

        if (!TextUtils.isEmpty(youTubeLink) && youTubeLink.length() > 11) {

            youTubeVideoID = youTubeLink.substring(youTubeLink.length() - 11);

            if (youTubeVideoID.length() == 11) {

                if (readyForLoadingYoutubeThumbnail) {

                    readyForLoadingYoutubeThumbnail = false;

                    youTubeThumbnailView.initialize(StaticValues.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                            youTubeThumbnailLoader.setVideo(youTubeVideoID);

                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                                @Override
                                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                                    Log.d(TAG, "onThumbnailLoaded: s: " + s);
                                    llcYouTubeThumbnailAndTitleContainer.setVisibility(View.VISIBLE);
                                    isYoutubeURLValid = true;
                                    getYouTubeDataInJsonFormat(youTubeLink);
                                    youTubeThumbnailLoader.release();
                                }

                                @Override
                                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                                    Toast.makeText(getContext(), "Wrong YouTube URL!", Toast.LENGTH_SHORT).show();
                                    removeYoutubeDataFromModel();
                                    isYoutubeURLValid = false;
                                    llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                                    youTubeThumbnailLoader.release();

                                }
                            });

                            readyForLoadingYoutubeThumbnail = true;
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            readyForLoadingYoutubeThumbnail = true;
                            isYoutubeURLValid = false;
                            removeYoutubeDataFromModel();
                            llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                        }
                    });
                }


            } else {

                Toast.makeText(context, "Invalid Youtube URL", Toast.LENGTH_SHORT).show();
                removeYoutubeDataFromModel();
                llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
            }


            Log.d(TAG, "afterTextChanged youTubeVideoID: " + youTubeVideoID);


        }


    }

    // region PrepareQuestionSet

    private QuestionSet getQuestionSet() {
        QuestionSet questionSet = new QuestionSet();

        Toast.makeText(context, "I am called", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "getQuestionSet: called");
        questionSet.setQuestionSetTitle(etQuestionSetTitle.getText().toString().trim());
        questionSet.setQuestionSetDescription(etQuestionSetDescription.getText().toString().trim());

        if (!TextUtils.isEmpty(etYouTubeLink.getText().toString().trim())) {

            Log.d(TAG, "getQuestionSet: called inside");
            if (isYoutubeURLValid) {
                questionSet.setQuestionSetYoutubeLink(etYouTubeLink.getText().toString().trim());
                Log.d(TAG, "getQuestionSet: called inside true youtube");
            } else {

                questionSet.setQuestionSetYoutubeLink("");
                Log.d(TAG, "getQuestionSet: called inside false youtube");
            }
        }
        if (!TextUtils.isEmpty(questionThumbnailImage))
            questionSet.setQuestionSetThumbnail(questionThumbnailImage);
        Log.d(TAG, "getQuestionSet thumbnail: " + questionThumbnailImage);

        // if (!TextUtils.isEmpty(youtubeThumbnailURL))
        questionSet.setQuestions(templateQuestionAdapter.getQuestionArrayList());

        return questionSet;
    }

    // endregion

    // endregion

    // region isQuestionSetValid -> Validate Question Set

    private boolean isQuestionSetValid() {

        String questionSetTitle = etQuestionSetTitle.getText().toString().trim();
        String questionSetDescription = etQuestionSetDescription.getText().toString().trim();

        Log.d(TAG, "isQuestionSetValid: Called");
        Log.d(TAG, "isQuestionSetValid: youtubeLink: " + etYouTubeLink.getText().toString());

        // checking if user gives question set title or not
        if (questionSetTitle.isEmpty()) {

            new SimpleTooltip.Builder(context)
                    .anchorView(etQuestionSetTitle)
                    .text("Write question set title")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();
            return false;
        }
        // checking if user gives question set description or not
        /*else if (questionSetDescription.isEmpty()) {

            new SimpleTooltip.Builder(context)
                    .anchorView(etQuestionSetDescription)
                    .text("Write question set description")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();
            return false;
        }*/
        // if youtube link editText field is visible user must give link

        else if (etYouTubeLink.getVisibility() == VISIBLE) {

            if (etYouTubeLink.getText().toString().trim().isEmpty()) {

                new SimpleTooltip.Builder(context)
                        .anchorView(etYouTubeLink)
                        .text("Give Youtube link")
                        .gravity(Gravity.BOTTOM)
                        .animated(true)
                        .transparentOverlay(true)
                        .build()
                        .show();
                return false;
            }
        }
        // checking if question set has any question or not
        else if (templateQuestionAdapter.getQuestionArrayList().isEmpty()) {

            Toast.makeText(context, "Empty Question Body :(", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    // endregion

    // region initAdapter
    private void initAdapter() {

        // getting initial question with default value
        Question question = initQuestionSetWithDefaultParams();

        // inserting and passing question in recycler view during initialization
        questionArrayList = new ArrayList<>();
        questionArrayList.add(question);
        imageProcessorForSingleQuestion = new ImageProcessor(context, CreateQxmFragment.this, TAG, apiService, token);
        templateQuestionAdapter = new TemplateQuestionAdapter(context, CreateQxmFragment.this, questionArrayList,
                questionSettings, rvTemplateQuestion, imageProcessorForSingleQuestion, llcAdQuestion, ivDoneCreatingQuestion, etQuestionSetTitle, etQuestionSetDescription, etYouTubeLink, tvYouTubeTitle, qxmDraft);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTemplateQuestion.setLayoutManager(linearLayoutManager);
        rvTemplateQuestion.setAdapter(templateQuestionAdapter);
        // rvTemplateQuestion.setNestedScrollingEnabled(false);
        rvTemplateQuestion.smoothScrollToPosition(rvTemplateQuestion.getAdapter().getItemCount() - 1);

    }
    //endregion

    //region load previous data
    public void loadFromPreviousData() {


        Log.d(TAG, "loadFromPreviousData question arrayList : " + questionArrayList);
        templateQuestionAdapter = new TemplateQuestionAdapter(context, CreateQxmFragment.this, questionArrayList,
                questionSettings, rvTemplateQuestion, imageProcessorForSingleQuestion, llcAdQuestion, ivDoneCreatingQuestion
                , etQuestionSetTitle, etQuestionSetDescription, etYouTubeLink, tvYouTubeTitle, qxmDraft);
        rvTemplateQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTemplateQuestion.setAdapter(templateQuestionAdapter);
        // rvTemplateQuestion.setNestedScrollingEnabled(false);
        rvTemplateQuestion.smoothScrollToPosition(rvTemplateQuestion.getAdapter().getItemCount() - 1);

    }
    //endregion

    // region SetUpDagger2
    private void setUpDagger2(Context context) {
        CreateQxmFragmentComponent createQxmFragmentComponent =
                DaggerCreateQxmFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        createQxmFragmentComponent.injectCreateQxmFragment(CreateQxmFragment.this);

    }
    // endregion

    // region method for making initial question
    // it will be passed in adapter
    public Question initQuestionSetWithDefaultParams() {

        Question question = new Question();
        question.setQuestionType(QUESTION_TYPE_MULTIPLE_CHOICE);

        // passing three types of question in question object
        question.setMultipleChoice(new MultipleChoice());
        question.setFillInTheBlanks(new FillInTheBlanks());
        question.setShortAnswer(new ShortAnswer());


        // initial empty value for multiple choice
        question.getMultipleChoice().setQuestionTitle("");
        question.getMultipleChoice().setImage("");
        question.getMultipleChoice().setLocalImage("");
        question.getMultipleChoice().setPoints("");
        question.getMultipleChoice().setRequired(false);
        question.getMultipleChoice().setDescription("");
        question.getMultipleChoice().setHiddenDescription("");
        question.getMultipleChoice().setHiddenDescription("");
        ArrayList<String> multipleChoiceTagsList = new ArrayList<>();
        question.getMultipleChoice().setTags(multipleChoiceTagsList);
        ArrayList<String> optionArrayList = new ArrayList<>();
        optionArrayList.add("");
        question.getMultipleChoice().setOptions(optionArrayList);
        ArrayList<String> correctAnswerList = new ArrayList<>();
        question.getMultipleChoice().setCorrectAnswers(correctAnswerList);


        // initial empty value for fill in the blanks
        question.getFillInTheBlanks().setQuestionTitle("");
        question.getFillInTheBlanks().setImage("");
        question.getFillInTheBlanks().setLocalImage("");
        question.getFillInTheBlanks().setPoints("");
        question.getFillInTheBlanks().setAchievedPoints("");
        question.getFillInTheBlanks().setRequired(false);
        question.getFillInTheBlanks().setDescription("");
        question.getFillInTheBlanks().setHiddenDescription("");
        question.getFillInTheBlanks().setHiddenDescription("");
        ArrayList<String> fillInTheTagsList = new ArrayList<>();
        question.getFillInTheBlanks().setTags(fillInTheTagsList);
        ArrayList<String> answerArrayList = new ArrayList<>();
        question.getFillInTheBlanks().setCorrectAnswers(answerArrayList);


        // initial empty value for short answer
        question.getShortAnswer().setQuestionTitle("");
        question.getShortAnswer().setImage("");
        question.getShortAnswer().setLocalImage("");
        question.getShortAnswer().setPoints("");
        question.getShortAnswer().setWordLimits("");
        question.getShortAnswer().setRequired(false);
        question.getShortAnswer().setDescription("");
        question.getShortAnswer().setHiddenDescription("");
        question.getShortAnswer().setHiddenDescription("");
        ArrayList<String> shortAnswerTagsList = new ArrayList<>();
        question.getShortAnswer().setTags(shortAnswerTagsList);

        return question;

    }
    //endregion

    //region receiving event after uploading single question image

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEvent(Events.OnPhotoUploadMessage onPhotoUploadMessage) {


        Log.d(TAG, "onPhotoUploadEvent: " + onPhotoUploadMessage);

        String sentImageURL = onPhotoUploadMessage.getSentImageURL();
        String localImageName = onPhotoUploadMessage.getLocalImageName();
        String message = onPhotoUploadMessage.getMessage();

        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                ivDoneCreatingQuestion.setVisibility(GONE);
                tvUploadingImage.setVisibility(VISIBLE);

                break;

            case IMAGE_UPLOAD_SUCCESS:


                /*
                 after image upload success response is received in this event,
                 based on question type, inserting received image url from
                 response and image name saved in local storage in specific
                 question object at specific position based on question type
                */

                switch (handledQuestionTypeIndex) {

                    case 0:
                        handledQuestionArrayList.get(handledQuestionPosition).getMultipleChoice().setImage(sentImageURL);
                        handledQuestionArrayList.get(handledQuestionPosition).getMultipleChoice().setLocalImage(localImageName);
                        Log.d(TAG, "SavedLocalImage: " + questionArrayList.get(handledQuestionPosition).getMultipleChoice().getLocalImage());
                        break;
                    case 1:
                        handledQuestionArrayList.get(handledQuestionPosition).getFillInTheBlanks().setImage(sentImageURL);
                        handledQuestionArrayList.get(handledQuestionPosition).getFillInTheBlanks().setLocalImage(localImageName);
                        break;
                    case 2:
                        handledQuestionArrayList.get(handledQuestionPosition).getShortAnswer().setImage(sentImageURL);
                        handledQuestionArrayList.get(handledQuestionPosition).getShortAnswer().setLocalImage(localImageName);
                        break;
                }

                tvUploadingImage.setVisibility(GONE);
                ivDoneCreatingQuestion.setVisibility(VISIBLE);
                imageProcessorForSingleQuestion.getRemovePhotoImageView().setVisibility(VISIBLE);
                Toast.makeText(context, "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                break;

            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                tvUploadingImage.setVisibility(GONE);
                ivDoneCreatingQuestion.setVisibility(VISIBLE);
                imageProcessorForSingleQuestion.getImageView().setVisibility(GONE);


                break;


        }

    }

    //endregion

    //region receiving event after uploading qSet thumbnail

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEventQSetThumbnail(Events.OnPhotoUploadMessageQSetThumbnail onPhotoUploadMessageQSetThumbnail) {

        Log.d(TAG, "onPhotoUploadEventQSetThumbnail: " + onPhotoUploadMessageQSetThumbnail);

        String message = onPhotoUploadMessageQSetThumbnail.getMessage();
        String qSetThumbnailURL = onPhotoUploadMessageQSetThumbnail.getSentImageURL();


        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                //changing upload button text
                tvAddDeleteThumbnail.setText(R.string.uploading_image);

                //block going to next fragment
                ivDoneCreatingQuestion.setClickable(false);
                ivDoneCreatingQuestion.setFocusableInTouchMode(false);
                ivDoneCreatingQuestion.setAlpha(0.5f);
                break;

            case IMAGE_UPLOAD_SUCCESS:

                // setting url in a variable
                CreateQxmFragment.questionThumbnailImage = qSetThumbnailURL;

                //changing upload button text
                tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);


                // release going to next fragment
                //block going to next fragment
                ivDoneCreatingQuestion.setClickable(true);
                ivDoneCreatingQuestion.setAlpha(1.0f);

                Toast.makeText(context, R.string.toast_message_image_uploaded_successfully, Toast.LENGTH_SHORT).show();
                break;

            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                //changing upload button text
                tvAddDeleteThumbnail.setText(R.string.add_thumbnail);

                // release going to next fragment
                //block going to next fragment
                ivDoneCreatingQuestion.setClickable(true);
                ivDoneCreatingQuestion.setAlpha(1.0f);


                //hiding thumbnail view
                ivQuestionSetThumbnail.setVisibility(GONE);

                Toast.makeText(context, R.string.toast_message_could_not_upload_image_try_again_later, Toast.LENGTH_SHORT).show();
                break;


        }

    }

    //endregion

    // region receiving keyboard visibility event message
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onKeyboardShowingEvent(Events.KeyboardVisibilityEvent keyboardVisibilityEvent) {

        Log.d(TAG, "onKeyboardShowingEvent: " + keyboardVisibilityEvent.isKeyBoardOpen());

        //hiding add question button when keyboard is open
        if (keyboardVisibilityEvent.isKeyBoardOpen()) {
            // llcAdQuestion.setVisibility(GONE);
        } else {
            // llcAdQuestion.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // we are receiving onActivityResult based on from where we are calling it

        Log.d(TAG, "onActivityResult: Called");
        if (StaticValues.onActivityCall.equals(ON_ACTIVITY_CALL_FOR_Q_SET)) {
            // passing image process result for feedback
            if (imageProcessorForQSetThumbnail != null) {

                Log.d(TAG, "onActivityResult: imageProcessorForQSetThumbnail");
                imageProcessorForQSetThumbnail.onActivityResult(requestCode, resultCode, data);

            }
        } else if (StaticValues.onActivityCall.equals(ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION)) {
            if (imageProcessorForSingleQuestion != null) {

                Log.d(TAG, "onActivityResult:imageProcessorForSingleQuestion ");
                imageProcessorForSingleQuestion.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    // endregion

    // region Override-onRequestPermissionsResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // waiting for permission
        Log.d(TAG, "onRequestPermissionsResult: Called");


        if (StaticValues.onActivityCall.equals(ON_ACTIVITY_CALL_FOR_Q_SET)) {

            if (imageProcessorForQSetThumbnail != null) {

                imageProcessorForQSetThumbnail.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else if (StaticValues.onActivityCall.equals(ON_ACTIVITY_CALL_FOR_SINGLE_QUESTION)) {
            if (imageProcessorForSingleQuestion != null) {

                imageProcessorForSingleQuestion.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    // endregion

    // region Override-OnStop

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Called");

        if (imageProcessorForQSetThumbnail != null) {
            imageProcessorForQSetThumbnail.onStop();
        }
        if (imageProcessorForSingleQuestion != null) {
            imageProcessorForSingleQuestion.onStop();
        }

    }

    // endregion

    // region Override-onBackPressed

    @Override
    public void onBackPressed() {
        //Toast.makeText(context, "this is from Create Qxm Fragment", Toast.LENGTH_SHORT).show();
        if (isQuestionSetValid()) {
            saveQxmAsDraftNetworkCall();
        }
    }

    private void saveQxmAsDraftNetworkCall() {
        PostQxmToBackEndNetworkCall postQxmToBackEndNetworkCall = new PostQxmToBackEndNetworkCall();

        if (questionSettings == null) {
            questionSettings = QxmSettingsModel.getDefaultQuestionSettings();
        } else if (!questionSettings.isContestModeEnabled()) {
            questionSettings.setNumberOfWinners(0);
            questionSettings.setRaffleDrawEnabled(false);
            questionSettings.setParticipateAfterContestEnd(false);
        }


        QxmModel qxmModel = new QxmModel(getQuestionSet(), questionSettings);

        if (this.qxmModel != null) {
            qxmModel.setDraftedId(this.qxmModel.getId());
        }

        postQxmToBackEndNetworkCall.postQxmToBackEnd(context, getActivity(), token, apiService,
                qxmModel, qxmDraft, true);
    }

    // endregion

    // region saveQxmAsDraft

    private void saveQxmAsDraft() {
        QxmModel qxmModel = new QxmModel();
        if (questionSettings != null) {
            qxmModel.setQuestionSet(getQuestionSet());
            qxmModel.setQuestionSettings(questionSettings);
        } else {
            qxmModel.setQuestionSet(getQuestionSet());
            qxmModel.setQuestionSettings(QxmSettingsModel.getDefaultQuestionSettings());
        }

        Type type = new TypeToken<QxmModel>() {
        }.getType();
        Gson gson = new Gson();
        String qxmJson = gson.toJson(qxmModel, type);
        Log.d(TAG, "QXM JSON: " + qxmJson);

        if (qxmDraft == null)
            qxmDraft = new QxmDraft();

        qxmDraft.setQuestionSetTitle(qxmModel.getQuestionSet().getQuestionSetTitle());
        qxmDraft.setQuestionSetDescription(qxmModel.getQuestionSet().getQuestionSetDescription());
        qxmDraft.setQuestionCategory(qxmModel.getQuestionSettings().getQuestionCategory());
        qxmDraft.setQuestionStatus(qxmModel.getQuestionSettings().getQuestionStatus());

        qxmDraft.setQuestionCategories(QxmArrayListToStringProcessor.getStringFromArrayList(qxmModel.getQuestionSettings().getQuestionCategory()));

        qxmDraft.setLastEditedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        if (TextUtils.isEmpty(qxmDraft.getCreatedAt()))
            qxmDraft.setCreatedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        //QuestionSet Full JSON
        qxmDraft.setQuestionSetJson(qxmJson);
        realmService = new RealmService(Realm.getDefaultInstance());
        realmService.saveQxmAsDraft(qxmDraft);

        Toast.makeText(context, "This Exam has saved as a draft on your device.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "saveQxmAsDraft: This Exam has saved as a draft on your device is successful.");

        if (getActivity() != null) {
            getActivity().finish();
            getActivity().getFragmentManager().popBackStack();
        }

    }

    // endregion

    //region get youtube data as in json

    private void getYouTubeDataInJsonFormat(String youTubeURL) {

        Call<YoutubeModel> getYouTubeDataAsJsonNetworkCall = apiService.getYoutubeVideoDataInJsonFormat(youTubeURL, "json");

        getYouTubeDataAsJsonNetworkCall.enqueue(new Callback<YoutubeModel>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeModel> call, @NonNull Response<YoutubeModel> response) {

                Log.d(TAG, "onResponse getYouTubeDataAsJsonNetworkCall: " + response);
                Log.d(TAG, "onResponse getYouTubeDataAsJsonNetworkCall response code: " + response.code());
                Log.d(TAG, "onResponse getYouTubeDataAsJsonNetworkCall: " + response.body());

                YoutubeModel youtubeModel = response.body();

                if (youtubeModel != null) {

                    if (youtubeModel.getYoutubeVideoTitle() != null) {

                        tvYouTubeTitle.setText(youtubeModel.getYoutubeVideoTitle());

                    }
                    if (youtubeModel.getThumbnailURL() != null) {

                        // setting url in a variable
                        CreateQxmFragment.questionThumbnailImage = youtubeModel.getThumbnailURL();
                        youtubeThumbnailURL = youtubeModel.getThumbnailURL();
                        Log.d(TAG, "onResponse: youtubeThumbnailURL" + youtubeThumbnailURL);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeModel> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure getYouTubeDataAsJsonNetworkCall: " + t.getMessage());
            }
        });
    }

    //endregion

    //region removeYoutubeDataFromModel
    private void removeYoutubeDataFromModel() {

        // setting url in a variable
        CreateQxmFragment.questionThumbnailImage = "";
        youtubeThumbnailURL = "";
    }
    //endregion

    //region onDestroy

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregistering event bus
        EventBus.getDefault().unregister(this);
    }

    //endregion

}