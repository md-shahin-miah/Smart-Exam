package com.crux.qxm.views.fragments.singleMCQ;


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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.SingleMultipleChoiceAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.createQxm.YoutubeModel;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.db.sampleModels.SingleMCQSettingsModel;
import com.crux.qxm.di.editSingleMultipleChoiceFeature.createSingleMultipleChoiceFeature.DaggerEditSingleMultipleChoiceFragmentComponent;
import com.crux.qxm.di.editSingleMultipleChoiceFeature.createSingleMultipleChoiceFeature.EditSingleMultipleChoiceFragmentComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
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
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.REMOVE_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.REMOVE_YOUTUBE_VIDEO;
import static com.crux.qxm.utils.StaticValues.SINGLE_MCQ_MODEL_KEY;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditSingleMultipleChoiceFragment extends Fragment {

    private static final String TAG = "EditSingleMultipleChoic";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    SingleMCQModel singleMCQModel;
    MultipleChoice multipleChoice;
    SingleMultipleChoiceAdapter singleMultipleChoiceAdapter;
    ArrayList<String> optionArrayList;
    ArrayList<String> correctAnswerList;

    private Context context;

    @BindView(R.id.tvUpdate)
    AppCompatTextView tvUpdate;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.cvQuestionContainer)
    CardView cvQuestionContainer;
    @BindView(R.id.llcAddThumbnail)
    LinearLayoutCompat llcAddThumbnail;
    @BindView(R.id.tvAddDeleteThumbnail)
    AppCompatTextView tvAddDeleteThumbnail;
    @BindView(R.id.llcAddYouTubeVideo)
    LinearLayoutCompat llcAddYouTubeVideo;
    @BindView(R.id.tvAddDeleteYouTube)
    AppCompatTextView tvAddDeleteYouTube;
    @BindView(R.id.etYouTubeLink)
    AppCompatEditText etYouTubeLink;
    @BindView(R.id.llcYouTubeThumbnailAndTitleContainer)
    LinearLayoutCompat llcYouTubeThumbnailAndTitleContainer;
    @BindView(R.id.youTubeThumbnailView)
    YouTubeThumbnailView youTubeThumbnailView;
    @BindView(R.id.tvYouTubeTitle)
    AppCompatTextView tvYouTubeTitle;
    @BindView(R.id.ivQuestionImage)
    AppCompatImageView ivQuestionImage;
    @BindView(R.id.ivQuestionOptions)
    AppCompatImageView ivQuestionOptions;
    @BindView(R.id.etMultipleChoiceQuestionName)
    AppCompatEditText etMultipleChoiceQuestionName;
    @BindView(R.id.etMultipleChoiceDescription)
    AppCompatEditText etMultipleChoiceDescription;
    @BindView(R.id.llcAddMultipleChoiceOption)
    LinearLayoutCompat llcAddMultipleChoiceOption;
    @BindView(R.id.rvMultipleOptions)
    RecyclerView rvMultipleOptions;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    private RealmService realmService;
    private ImageProcessor imageProcessorForSingleMultipleChoice;

    private boolean readyForLoadingYoutubeThumbnail = true;

    public EditSingleMultipleChoiceFragment() {
        // Required empty public constructor
    }

    public static EditSingleMultipleChoiceFragment newInstance(SingleMCQModel singleMCQModel) {

        Bundle args = new Bundle();
        args.putParcelable(SINGLE_MCQ_MODEL_KEY, singleMCQModel);
        EditSingleMultipleChoiceFragment fragment = new EditSingleMultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_single_multiple_choice, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        singleMCQModel = getArguments() != null ? getArguments().getParcelable(SINGLE_MCQ_MODEL_KEY) : null;

        if (singleMCQModel != null) {

            context = view.getContext();
            setUpDagger2(context);
            init();
            setViewOnClickListener();
            setDataInViews();
            disableViewsForEditMode();
            cvQuestionContainer.setVisibility(VISIBLE);

        } else {
            Toast.makeText(context, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
            cvQuestionContainer.setVisibility(GONE);
        }


        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);


    }

    private void setDataInViews() {
        etMultipleChoiceQuestionName.setText(singleMCQModel.getMultipleChoice().getQuestionTitle());
        etMultipleChoiceDescription.setText(singleMCQModel.getMultipleChoice().getDescription());
        SetlinkClickable.setLinkclickEvent(etMultipleChoiceDescription, new HandleLinkClickInsideTextView() {
            public void onLinkClicked(String url) {
                Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                intent.putExtra(YOUTUBE_LINK_KEY, url);
                context.startActivity(intent);
            }
        });
    }

    private void setUpDagger2(Context context) {
        EditSingleMultipleChoiceFragmentComponent editSingleMultipleChoiceFragmentComponent =
                DaggerEditSingleMultipleChoiceFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        editSingleMultipleChoiceFragmentComponent.injectEditSingleMultipleChoiceFragmentFeature(EditSingleMultipleChoiceFragment.this);
    }

    private void init() {

        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        initMultipleChoiceQuestion();

        initMultipleChoiceAdapter();


    }

    private void disableViewsForEditMode() {
        ivQuestionOptions.setEnabled(false);
        ivQuestionOptions.setAlpha(0.4f);

        llcAddMultipleChoiceOption.setEnabled(false);
        llcAddMultipleChoiceOption.setAlpha(0.4f);
    }


    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            // registering event bus
            EventBus.getDefault().register(this);
        }

        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(GONE);
        }
    }

    // endregion

    // region Override-OnDestroyView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            Log.d(TAG, "onDestroyView: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        }
    }

    // endregion

    //region set viewOnClick Listener
    private void setViewOnClickListener() {

        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        llcAddThumbnail.setOnClickListener(v -> {

            if (tvAddDeleteThumbnail.getText().toString().equals(ADD_THUMBNAIL)) {

                initializePhotoUploadProcess();

                multipleChoice.setYoutubeVideoLink("");
                etYouTubeLink.setText("");
                etYouTubeLink.setVisibility(GONE);
                llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                tvAddDeleteYouTube.setText(ADD_YOUTUBE_VIDEO);

            } else if (tvAddDeleteThumbnail.getText().toString().equals(REMOVE_THUMBNAIL)) {

                multipleChoice.setImage("");
                ivQuestionImage.setVisibility(GONE);
                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);

            }

        });

        llcAddYouTubeVideo.setOnClickListener(v -> {


            if (tvAddDeleteYouTube.getText().toString().equals(ADD_YOUTUBE_VIDEO)) {

                multipleChoice.setImage("");
                etYouTubeLink.setVisibility(View.VISIBLE);
                llcYouTubeThumbnailAndTitleContainer.setVisibility(VISIBLE);

                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);
                tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                ivQuestionImage.setVisibility(GONE);


            } else if (tvAddDeleteYouTube.getText().toString().equals(REMOVE_YOUTUBE_VIDEO)) {

                multipleChoice.setYoutubeVideoLink("");
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

                String youTubeLink = etYouTubeLink.getText().toString().trim();

                getYoutubeVideo(youTubeLink);
            }
        });

        //region BlockedClickEvents - for Edit Mode
        ivQuestionOptions.setOnClickListener(v ->
                Toast.makeText(context, R.string.toast_single_mcq_options_menu_restriction_message, Toast.LENGTH_LONG).show());

        llcAddMultipleChoiceOption.setOnClickListener(v ->
                Toast.makeText(context, R.string.toast_edit_single_mcq_add_option_restriction_message, Toast.LENGTH_LONG).show());
        //endregion

        etMultipleChoiceQuestionName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                // if question title is empty then stop user to post the question
                if (!editable.toString().trim().isEmpty()) {

                    tvUpdate.setAlpha(1.0f);
                    tvUpdate.setClickable(true);

                } else {

                    tvUpdate.setAlpha(0.5f);
                    tvUpdate.setClickable(false);
                }

                multipleChoice.setQuestionTitle(editable.toString());
            }
        });

        etMultipleChoiceDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                multipleChoice.setDescription(editable.toString());

            }
        });


        tvUpdate.setOnClickListener(v -> {

            if (isQuestionValid()) {

                singleMCQModel.setMultipleChoice(multipleChoice);
                singleMCQModel.setSingleMCQSettings(SingleMCQSettingsModel.getDefaultQuestionSettings());

                //Toast.makeText(getContext(), "Multiple Question is ready to post", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "MultipleChoice: " + multipleChoice);

                Gson gson = new Gson();
                String multipleChoiceJson = gson.toJson(multipleChoice);
                Log.d(TAG, "MultipleChoice Json: " + multipleChoiceJson);

                editSingleMultipleChoiceNetworkCall(singleMCQModel);
            }

        });
    }

    private void editSingleMultipleChoiceNetworkCall(SingleMCQModel singleMCQModel) {

        QxmProgressDialog progressDialog = new QxmProgressDialog(context);

        progressDialog.showProgressDialog(context.getString(R.string.alert_dialog_title_updating_single_mcq),
                context.getString(R.string.alert_dialog_message_updating_single_mcq),
                false);

        Call<QxmApiResponse> updateSingleMultipleChoice = apiService.updateSingleMultipleChoice(
                token.getToken(), token.getUserId(), singleMCQModel.getId(), singleMCQModel
        );

        updateSingleMultipleChoice.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: editSingleMultipleChoiceNetworkCall");
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "response body: " + response.body());

                progressDialog.closeProgressDialog();

                if (response.code() == 201) {
                    StaticValues.isHomeFeedRefreshingNeeded = true;
                    StaticValues.isMyQxmFeedRefreshingNeeded = true;
                    if (getActivity() != null) getActivity().onBackPressed();

                } else if (response.code() == 403) {
                    Toast.makeText(context, getString(R.string.login_session_expired_message), Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Log.d(TAG, "onResponse: editSingleMultipleChoiceNetworkCall failed");
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: editSingleMultipleChoiceNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

                progressDialog.closeProgressDialog();

                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //endregion

    //region initial multiple choice question

    private void initMultipleChoiceQuestion() {
        multipleChoice = singleMCQModel.getMultipleChoice();
        optionArrayList = multipleChoice.getOptions();
        correctAnswerList = multipleChoice.getCorrectAnswers();
    }

    //endregion

    // region init multiple choice adapter

    private void initMultipleChoiceAdapter() {
        singleMultipleChoiceAdapter = new SingleMultipleChoiceAdapter(getContext(), optionArrayList, correctAnswerList, true);
        rvMultipleOptions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMultipleOptions.setNestedScrollingEnabled(false);
        rvMultipleOptions.setAdapter(singleMultipleChoiceAdapter);

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

    //region is question valid

    private boolean isQuestionValid() {

        // if question set is empty, it means nothing to check,
        // so let user add a question first (see else block)

        if (TextUtils.isEmpty(etMultipleChoiceQuestionName.getText().toString().trim())) {

            new SimpleTooltip.Builder(getContext())
                    .anchorView(etMultipleChoiceQuestionName)
                    .text("Write question title")
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();

            return false;

        } else if (multipleChoice.getOptions().isEmpty()) {

            new SimpleTooltip.Builder(getContext())
                    .anchorView(rvMultipleOptions)
                    .text("Give one or more option")
                    .gravity(Gravity.END)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();

            return false;

        } else if (multipleChoice.getOptions().size() < 2) {


            new SimpleTooltip.Builder(getContext())
                    .anchorView(rvMultipleOptions)
                    .text("Minimum 2 options are required")
                    .gravity(Gravity.END)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();

            return false;

        } else if (multipleChoice.getCorrectAnswers().isEmpty()) {

            new SimpleTooltip.Builder(getContext())
                    .anchorView(rvMultipleOptions)
                    .text("Select correct answer")
                    .gravity(Gravity.END)
                    .animated(true)
                    .transparentOverlay(true)
                    .build()
                    .show();

            return false;

        } else if (!multipleChoice.getOptions().isEmpty()) {

            for (String singleMultiOption : multipleChoice.getOptions()) {

                if (singleMultiOption.isEmpty()) {

                    new SimpleTooltip.Builder(getContext())
                            .anchorView(rvMultipleOptions)
                            .text("Option can not be empty")
                            .gravity(Gravity.END)
                            .animated(true)
                            .transparentOverlay(true)
                            .build()
                            .show();


                    return false;


                }
            }
        }
        return true;
    }

    //endregion

    //region receiving event after uploading question image

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEvent(Events.OnPhotoUploadMessage onPhotoUploadMessage) {

        String sentImageURL = onPhotoUploadMessage.getSentImageURL();
        String localImageName = onPhotoUploadMessage.getLocalImageName();
        String message = onPhotoUploadMessage.getMessage();
        Log.d(TAG, "onPhotoUploadEvent Message: " + message);
        Log.d(TAG, "onPhotoUploadEvent: " + onPhotoUploadMessage);

        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent STARTING_PHOTO_UPLOAD: Called");
                tvUpdate.setClickable(false);
                tvUpdate.setFocusableInTouchMode(false);
                tvUpdate.setAlpha(0.5f);
                tvUpdate.setVisibility(GONE);
                tvUploadingImage.setVisibility(View.VISIBLE);

                llcAddThumbnail.setClickable(false);
                llcAddThumbnail.setAlpha(0.5f);

                break;

            // if photo uploaded successfully, then let user change the photo or remove
            case IMAGE_UPLOAD_SUCCESS:

                Log.d(TAG, "onPhotoUploadEvent IMAGE_UPLOAD_SUCCESS: Called");
                tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);

                tvUpdate.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(etMultipleChoiceQuestionName.getText().toString())) {
                    tvUpdate.setClickable(true);
                    tvUpdate.setAlpha(1.0f);
                }

                multipleChoice.setImage(sentImageURL);
                multipleChoice.setLocalImage(localImageName);
                tvUploadingImage.setVisibility(View.GONE);


                llcAddThumbnail.setClickable(true);
                llcAddThumbnail.setAlpha(1.0f);

                Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                break;

            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent ERROR_OCCURRED_DURING_PHOTO_UPLOAD: Called");
                multipleChoice.setImage("");
                ivQuestionImage.setVisibility(GONE);
                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);
                tvUpdate.setClickable(true);
                tvUpdate.setAlpha(1.0f);
                tvUploadingImage.setVisibility(View.GONE);
                tvUpdate.setVisibility(View.VISIBLE);

                llcAddThumbnail.setClickable(true);
                llcAddThumbnail.setAlpha(1.0f);

                break;

        }

    }

    //endregion

    //region initializePhotoUploadProcess

    private void initializePhotoUploadProcess() {

        tvUpdate.setClickable(false);
        tvUpdate.setFocusableInTouchMode(false);
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL;
        imageProcessorForSingleMultipleChoice = new ImageProcessor(getContext(), EditSingleMultipleChoiceFragment.this, TAG, apiService, token, ivQuestionImage, tvUpdate);
        imageProcessorForSingleMultipleChoice.pickImageFromGallery();


    }

    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // passing image process result for feedback

        imageProcessorForSingleMultipleChoice.onActivityResult(requestCode, resultCode, data);

    }

    //endregion


    //region getYoutubeVideo

    private void getYoutubeVideo(String youTubeLink) {

        if (!TextUtils.isEmpty(youTubeLink) && youTubeLink.length() > 11) {
            String youTubeVideoID = youTubeLink.substring(youTubeLink.length() - 11);
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
                                    getYouTubeDataInJsonFormat(youTubeLink);
                                    youTubeThumbnailLoader.release();
                                    youTubeThumbnailView.destroyDrawingCache();
                                }

                                @Override
                                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                                    Toast.makeText(getContext(), "Wrong YouTube URL!", Toast.LENGTH_SHORT).show();
                                    youTubeThumbnailLoader.release();

                                    youTubeThumbnailView.destroyDrawingCache();
                                }
                            });
                            readyForLoadingYoutubeThumbnail = true;
                        }
                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            readyForLoadingYoutubeThumbnail = true;
                        }
                    });
                }


            } else {
                Toast.makeText(getContext(), "Invalid Youtube URL", Toast.LENGTH_SHORT).show();
            }


            Log.d(TAG, "afterTextChanged youTubeVideoID: " + youTubeVideoID);


        }


    }

    //endregion

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
                        multipleChoice.setImage(youtubeModel.getThumbnailURL());
                        multipleChoice.setYoutubeVideoLink(youTubeURL);
                        tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                        Log.d(TAG, "onResponse: youtubeThumbnailURL" + youtubeModel.getThumbnailURL());
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
}