package com.crux.qxm.views.fragments.singleMCQ;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.questions.SingleMultipleChoiceAdapter;
import com.crux.qxm.db.models.createQxm.YoutubeModel;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.db.sampleModels.SingleMCQSettingsModel;
import com.crux.qxm.di.createSingleMultipleChoiceFeature.CreateSingleMultipleChoiceComponent;
import com.crux.qxm.di.createSingleMultipleChoiceFeature.DaggerCreateSingleMultipleChoiceComponent;
import com.crux.qxm.linkclickable.HandleLinkClickInsideTextView;
import com.crux.qxm.linkclickable.SetlinkClickable;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.views.activities.YouTubePlayBackActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.realm.Realm;
import mabbas007.tagsedittext.TagsEditText;
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
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_LINK_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateSingleMultipleChoiceFragment extends Fragment {

    private static final String TAG = "CreateSingleMultipleCho";
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    SingleMCQModel singleMCQModel;
    MultipleChoice multipleChoice;
    SingleMultipleChoiceAdapter singleMultipleChoiceAdapter;
    ArrayList<String> optionArrayList;
    ArrayList<String> correctAnswerList;
    @BindView(R.id.tvNext)
    AppCompatTextView tvNext;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
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
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;
    private ImageProcessor imageProcessorForSingleMultipleChoice;

    private boolean readyForLoadingYoutubeThumbnail = true;


    public CreateSingleMultipleChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_single_multiple_choice, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        setUpDagger2(context);
        init();

        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);

    }

    private void setUpDagger2(Context context) {
        CreateSingleMultipleChoiceComponent createSingleMultipleChoiceComponent =
                DaggerCreateSingleMultipleChoiceComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        createSingleMultipleChoiceComponent.injectCreateSingleMultipleChoiceFeature(CreateSingleMultipleChoiceFragment.this);
    }

    private void init() {

        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        apiService = retrofit.create(QxmApiService.class);
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());


        if (multipleChoice == null)
            initMultipleChoiceQuestion();

        initMultipleChoiceAdapter();
        setViewOnClickListener();
        setThumbnailOrYoutubeVideoIfAvailable();


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

                tvAddDeleteThumbnail.setText(ADD_THUMBNAIL);
                tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                ivQuestionImage.setVisibility(GONE);


            } else if (tvAddDeleteYouTube.getText().toString().equals(REMOVE_YOUTUBE_VIDEO)) {

                multipleChoice.setYoutubeVideoLink("");
                multipleChoice.setImage("");
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

                if (!TextUtils.isEmpty(Objects.requireNonNull(etYouTubeLink.getText()).toString())) {
                    String youTubeLink = etYouTubeLink.getText().toString().trim();
                    getYoutubeVideo(youTubeLink);
                }

            }
        });

        ivQuestionOptions.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_signle_multiple_question_options, popup.getMenu());

            if (!multipleChoice.getHiddenDescription().trim().isEmpty())
                popup.getMenu().getItem(0).setTitle("Hidden Description Added");

            else popup.getMenu().getItem(0).setTitle("Add Hidden Description");

            if (multipleChoice.getTags() != null) {

                if (multipleChoice.getTags().size() != 0)
                    popup.getMenu().getItem(1).setTitle("Tags Added");

                else popup.getMenu().getItem(1).setTitle("Add Tags");
            }


            popup.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {

                    case R.id.action_add_hidden_description:

                        //region action add hidden description

                        AppCompatDialog dialogHiddenDescription;
                        AlertDialog.Builder builderHiddenDescription = new AlertDialog.Builder(v.getContext());
                        LayoutInflater hiddenDescriptionLayoutInflater = LayoutInflater.from(v.getContext());
                        View hiddenDescriptionDialogView = hiddenDescriptionLayoutInflater.inflate(R.layout.layout_edit_text_dialog, null);
                        builderHiddenDescription.setView(hiddenDescriptionDialogView);
                        AppCompatEditText hiddenDescriptionEditText = hiddenDescriptionDialogView.findViewById(R.id.etDialog);
                        hiddenDescriptionEditText.setTextColor(Color.parseColor("#000000"));
                        hiddenDescriptionEditText.setHint("Add hidden description");
                        builderHiddenDescription.setTitle("Hidden Description");
                        builderHiddenDescription.setCancelable(false);


                        // if already user put hidden description, show it

                        if (!multipleChoice.getHiddenDescription().trim().isEmpty()) {

                            hiddenDescriptionEditText.setText(multipleChoice.getHiddenDescription());
                            SetlinkClickable.setLinkclickEvent(hiddenDescriptionEditText, new HandleLinkClickInsideTextView() {
                                public void onLinkClicked(String url) {
                                    Intent intent = new Intent(context, YouTubePlayBackActivity.class);
                                    intent.putExtra(YOUTUBE_LINK_KEY, url);
                                    context.startActivity(intent);
                                }
                            });
                        }

                        builderHiddenDescription.setPositiveButton("OK", (dialogInterface, i) -> {

                            // checking user if user left the edit text blank or not
                            if (!hiddenDescriptionEditText.getText().toString().isEmpty()) {

                                multipleChoice.setHiddenDescription(hiddenDescriptionEditText.getText().toString());

                            } else {

                                multipleChoice.setHiddenDescription(" ");
                            }

                            // hiding dialog after pressing ok
                            dialogInterface.dismiss();

                        });

                        dialogHiddenDescription = builderHiddenDescription.create();
                        dialogHiddenDescription.show();
                        break;

                    //endregion

                    case R.id.action_add_tags:

                        //region add tags

                        AppCompatDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Add tag and press enter");
                        LayoutInflater tagLayoutInflater = LayoutInflater.from(v.getContext());
                        View tagDialogView = tagLayoutInflater.inflate(R.layout.layout_tag_edit_text_dialog, null);
                        builder.setView(tagDialogView);
                        TagsEditText tagsEditText = tagDialogView.findViewById(R.id.etTag);
                        tagsEditText.setSeparator(" ");
                        tagsEditText.setTagsTextColor(R.color.white);
                        tagsEditText.setTagsBackground(R.drawable.tag_background);
                        tagsEditText.setTextColor(Color.parseColor("#000000"));
                        builder.setCancelable(false);

                        // if already user put hidden description, show it
                        if (multipleChoice.getTags() != null) {

                            if (multipleChoice.getTags().size() != 0) {

//                                // appending all tags
//                                StringBuilder allTags = new StringBuilder();
//                                for (String tag : multipleChoice.getTags()) {
//                                    allTags.append(tag).append(" ");
//                                }
                                // setting in tagEditText
                                String[] separateTags = multipleChoice.getTags().toArray(new String[multipleChoice.getTags().size()]);
                                tagsEditText.setTags(separateTags);
                            }

                        } else tagsEditText.setText("");

                        builder.setPositiveButton("OK", (dialogInterface, i) -> {

                            if (!tagsEditText.getTags().isEmpty()) {
                                multipleChoice.setTags(tagsEditText.getTags());

                            } else {

                                multipleChoice.setTags(null);
                            }


                            Log.d("TagEditText", tagsEditText.getTags().toString());
                            dialogInterface.dismiss();

                        });

                        dialog = builder.create();
                        dialog.show();
                        break;

                    // endregion
                }

                return false;

            });

            popup.show();
        });

        llcAddMultipleChoiceOption.setOnClickListener(v -> {


            // maximum 6 options are allowed in single
            if (optionArrayList.size() >= 6) {

                Toast.makeText(getContext(), "Maximum 6 options are allowed", Toast.LENGTH_SHORT).show();
            } else {

                optionArrayList.add("");
                singleMultipleChoiceAdapter.notifyItemInserted(optionArrayList.size() - 1);
            }

        });


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

                    tvNext.setAlpha(1.0f);
                    tvNext.setClickable(true);

                } else {

                    tvNext.setAlpha(0.5f);
                    tvNext.setClickable(false);
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


        tvNext.setOnClickListener(v -> {

            if (isQuestionValid()) {

                singleMCQModel.setMultipleChoice(multipleChoice);
                singleMCQModel.setSingleMCQSettings(SingleMCQSettingsModel.getDefaultQuestionSettings());

                Toast.makeText(getContext(), "Multiple Question is ready to post", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "MultipleChoice: " + multipleChoice);

                Gson gson = new Gson();
                String multipleChoiceJson = gson.toJson(multipleChoice);
                Log.d(TAG, "MultipleChoice Json: " + multipleChoiceJson);

                //startSingleMultipleChoicePostNetworkCall();
                qxmFragmentTransactionHelper.loadSingleMultipleChoiceSettingsFragment(singleMCQModel);
            }

        });
    }
    //endregion

    //region initial multiple choice question

    private void initMultipleChoiceQuestion() {

        singleMCQModel = new SingleMCQModel();


        multipleChoice = new MultipleChoice();

        // initial empty value for multiple choice
        multipleChoice.setQuestionTitle("");
        multipleChoice.setImage("");
        multipleChoice.setLocalImage("");
        //default point is one
        multipleChoice.setPoints("5");
        multipleChoice.setRequired(false);
        multipleChoice.setDescription("");
        multipleChoice.setHiddenDescription("");
        multipleChoice.setYoutubeVideoLink("");
        ArrayList<String> multipleChoiceTagsList = new ArrayList<>();
        multipleChoice.setTags(multipleChoiceTagsList);
        optionArrayList = new ArrayList<>();
        optionArrayList.add("");
        multipleChoice.setOptions(optionArrayList);
        correctAnswerList = new ArrayList<>();
        multipleChoice.setCorrectAnswers(correctAnswerList);
    }

    //endregion

    // region init multiple choice adapter

    private void initMultipleChoiceAdapter() {

        singleMultipleChoiceAdapter = new SingleMultipleChoiceAdapter(getContext(), multipleChoice.getOptions(), multipleChoice.getCorrectAnswers());
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
                tvNext.setClickable(false);
                tvNext.setFocusableInTouchMode(false);
                tvNext.setAlpha(0.5f);
                tvNext.setVisibility(GONE);
                tvUploadingImage.setVisibility(View.VISIBLE);

                llcAddThumbnail.setClickable(false);
                llcAddThumbnail.setAlpha(0.5f);

                break;

            // if photo uploaded successfully, then let user change the photo or remove
            case IMAGE_UPLOAD_SUCCESS:

                Log.d(TAG, "onPhotoUploadEvent IMAGE_UPLOAD_SUCCESS: Called");
                tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);

                tvNext.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(etMultipleChoiceQuestionName.getText().toString())) {
                    tvNext.setClickable(true);
                    tvNext.setAlpha(1.0f);
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
                tvNext.setClickable(true);
                tvNext.setAlpha(1.0f);
                tvUploadingImage.setVisibility(View.GONE);
                tvNext.setVisibility(View.VISIBLE);

                llcAddThumbnail.setClickable(true);
                llcAddThumbnail.setAlpha(1.0f);

                break;

        }

    }

    //endregion

    //region initializePhotoUploadProcess

    private void initializePhotoUploadProcess() {

        tvNext.setClickable(false);
        tvNext.setFocusableInTouchMode(false);
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_SINGLE_MULTIPLE_CHOICE_THUMBNAIL;
        imageProcessorForSingleMultipleChoice = new ImageProcessor(getContext(), CreateSingleMultipleChoiceFragment.this, TAG, apiService, token, ivQuestionImage, tvNext);
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

        if (URLUtil.isValidUrl(youTubeLink)) {
            if (!TextUtils.isEmpty(youTubeLink) && youTubeLink.length() > 11) {

                String youTubeVideoID = youTubeLink.substring(youTubeLink.length() - 11);

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
                                }

                                @Override
                                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                                    llcYouTubeThumbnailAndTitleContainer.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), getString(R.string.invalid_youtube_url),
                                            Toast.LENGTH_SHORT).show();
                                    removeYoutubeDataFromModel();
                                    youTubeThumbnailLoader.release();

                                }
                            });
                            readyForLoadingYoutubeThumbnail = true;
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            readyForLoadingYoutubeThumbnail = true;
                            llcYouTubeThumbnailAndTitleContainer.setVisibility(View.GONE);
                            removeYoutubeDataFromModel();
                        }
                    });
                }
            } else
                Toast.makeText(context, getString(R.string.invalid_youtube_url), Toast.LENGTH_SHORT).show();
        } else {

            llcYouTubeThumbnailAndTitleContainer.setVisibility(View.GONE);
            removeYoutubeDataFromModel();
            Toast.makeText(context, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
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
                        //CreateQxmFragment.questionThumbnailImage = youtubeModel.getThumbnailURL();
                        multipleChoice.setImage(youtubeModel.getThumbnailURL());
                        multipleChoice.setYoutubeVideoLink(youTubeURL);
                        tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                        Log.d(TAG, "onResponse: youtubeThumbnailURL" + youtubeModel.getThumbnailURL());
                    }
                } else {
                    llcYouTubeThumbnailAndTitleContainer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getString(R.string.invalid_youtube_url),
                            Toast.LENGTH_SHORT).show();
                    removeYoutubeDataFromModel();
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeModel> call, @NonNull Throwable t) {

                Log.d(TAG, "onFailure getYouTubeDataAsJsonNetworkCall: " + t.getMessage());
                llcYouTubeThumbnailAndTitleContainer.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.no_internet_found),
                        Toast.LENGTH_SHORT).show();
                removeYoutubeDataFromModel();
            }
        });
    }

    //endregion

    //region setThumbnailOrYoutubeVideoIfAvailable
    private void setThumbnailOrYoutubeVideoIfAvailable() {

        if (multipleChoice != null) {

            if (!multipleChoice.getYoutubeVideoLink().trim().isEmpty() && multipleChoice.getYoutubeVideoLink() != null) {

                Log.d(TAG, "setThumbnailOrYoutubeVideoIfAvailable: youtubeLinkAvailable");
                ivQuestionImage.setVisibility(GONE);
                tvAddDeleteYouTube.setText(REMOVE_YOUTUBE_VIDEO);
                llcYouTubeThumbnailAndTitleContainer.setVisibility(VISIBLE);

            } else if (!multipleChoice.getImage().trim().isEmpty() && multipleChoice.getImage() != null) {

                Log.d(TAG, "setThumbnailOrYoutubeVideoIfAvailable: thumbnailAvailable");
                llcYouTubeThumbnailAndTitleContainer.setVisibility(GONE);
                tvAddDeleteThumbnail.setText(REMOVE_THUMBNAIL);
                ivQuestionImage.setVisibility(VISIBLE);
                picasso.load(multipleChoice.getImage()).into(ivQuestionImage);

            }

        }
    }
    //endregion

    //region removeYoutubeDataFromModel
    private void removeYoutubeDataFromModel() {

        multipleChoice.setYoutubeVideoLink("");
        multipleChoice.setImage("");
        multipleChoice.setLocalImage("");

    }
    //endregion


}