package com.crux.qxm.views.fragments.poll;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.adapter.poll.PollAdapter;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.poll.PollPostingModel;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.createPollFragmentFeature.CreatePollFragmentComponent;
import com.crux.qxm.di.createPollFragmentFeature.DaggerCreatePollFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.ImageProcessor;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.eventBus.Events;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
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
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static com.crux.qxm.utils.StaticValues.ERROR_OCCURRED_DURING_PHOTO_UPLOAD;
import static com.crux.qxm.utils.StaticValues.IMAGE_UPLOAD_SUCCESS;
import static com.crux.qxm.utils.StaticValues.ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL;
import static com.crux.qxm.utils.StaticValues.POLL_PRIVACY_PUBLIC;
import static com.crux.qxm.utils.StaticValues.POLL_SHARED_ON_FEED;
import static com.crux.qxm.utils.StaticValues.POLL_TYPE_PUBLISHED;
import static com.crux.qxm.utils.StaticValues.STARTING_PHOTO_UPLOAD;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePollFragment extends Fragment {


    private static final String TAG = "CreatePollFragment";


    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;
    private ArrayList<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptionList;
    private PollAdapter pollAdapter;
    private Context context;
    private QxmApiService apiService;
    private UserBasic user;
    private QxmToken token;
    private ImageProcessor imageProcessorForPollThumbnail;
    private PollPostingModel pollPostingModel;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;


    @BindView(R.id.tvPostPoll)
    AppCompatTextView tvPostPoll;
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.etPollTitle)
    AppCompatEditText etPollTitle;
    @BindView(R.id.rvPollOption)
    RecyclerView rvPollOption;
    @BindView(R.id.rlAddNewPollAnswerOption)
    RelativeLayout rlAddNewPollAnswerOption;
    @BindView(R.id.tvAddPollThumbnail)
    AppCompatTextView tvAddPollThumbnail;
    @BindView(R.id.ivPollThumbnail)
    AppCompatImageView ivPollThumbnail;
    @BindView(R.id.rlChangeOrDeletePollThumbnail)
    RelativeLayout rlChangeOrDeletePollThumbnail;
    @BindView(R.id.tvChangePollThumbnail)
    AppCompatTextView tvChangePollThumbnail;
    @BindView(R.id.tvRemovePollThumbnail)
    AppCompatTextView tvRemovePollThumbnail;
    @BindView(R.id.llcPollOptionOneAndTwo)
    LinearLayoutCompat llcPollOptionOneAndTwo;
    @BindView(R.id.etPollOption1)
    AppCompatEditText etPollOption1;
    @BindView(R.id.etPollOption2)
    AppCompatEditText etPollOption2;
    @BindView(R.id.tvUploadingImage)
    AppCompatTextView tvUploadingImage;


    // region newInstance
    public static CreatePollFragment newInstance() {
        Bundle args = new Bundle();
        CreatePollFragment fragment = new CreatePollFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    public CreatePollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_create_poll, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        setUpDagger2(getActivity());
        init();
        setupOnClickListener();

        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context,TAG,TAG);

    }


    // region SetUpDagger2
    private void setUpDagger2(Context context) {
        CreatePollFragmentComponent createPollFragmentComponent =
                DaggerCreatePollFragmentComponent
                        .builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();
        createPollFragmentComponent.injectCreatePollFragmentFeature(CreatePollFragment.this);
    }
    // endregion

    //region init
    private void init(){


        apiService = retrofit.create(QxmApiService.class);
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        user = realmService.getSavedUser();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
        pollOptionList = new ArrayList<>();
        pollOptionList.add(new com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem(""));
        pollOptionList.add(new com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem(""));
        pollAdapter = new PollAdapter(getContext(), pollOptionList, () -> {


            Log.d(TAG, "delete pollOptionList :"+pollOptionList);
            Log.d(TAG, "delete pollOptionList size :"+String.valueOf(pollOptionList.size()));

//            if (pollOptionList.size()==2){
//
//                rvPollOption.setVisibility(View.GONE);
//                llcPollOptionOneAndTwo.setVisibility(View.VISIBLE);
//                etPollOption1.setText(pollOptionList.get(0).getTitle());
//                etPollOption2.setText(pollOptionList.get(1).getTitle());
//
//            }else {
//
//                llcPollOptionOneAndTwo.setVisibility(View.GONE);
//                rvPollOption.setVisibility(View.VISIBLE);
//
//            }

        });
        rvPollOption.setAdapter(pollAdapter);
        rvPollOption.setLayoutManager(new LinearLayoutManager(getContext()));
        pollPostingModel = new PollPostingModel();


    }
    //endregion

    //region setupOnClickListener

    private void setupOnClickListener(){

        rlAddNewPollAnswerOption.setOnClickListener(v->{

            pollOptionList.add(new com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem(""));
            pollAdapter.notifyItemInserted(pollOptionList.size() - 1);



            Log.d(TAG, "setViewClick pollOptionList :"+pollOptionList);
            Log.d(TAG, "setViewClick pollOptionList size :"+String.valueOf(pollOptionList.size()));


        });

//        ivBackArrow.setOnClickListener(v -> {
//            if (getFragmentManager() != null) {
//                getFragmentManager().popBackStack();
//            }
//        });

        ivBackArrow.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        tvAddPollThumbnail.setOnClickListener(v-> initializePhotoUploadProcess());

        etPollTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                // if poll title is empty then stop user to post the poll
                if(!editable.toString().trim().isEmpty()){

                    tvPostPoll.setAlpha(1.0f);
                    tvPostPoll.setClickable(true);

                }else {

                    tvPostPoll.setAlpha(0.5f);
                    tvPostPoll.setClickable(false);
                }

                // updating poll title on every changes in title
                pollPostingModel.setPollName(editable.toString());
            }
        });


        // adding first two poll options in first two position of the pollOptionList
        etPollOption1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                pollOptionList.get(0).setTitle(editable.toString());
            }
        });

        etPollOption2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                pollOptionList.get(1).setTitle(editable.toString());

            }
        });

        tvPostPoll.setOnClickListener(v->{

            pollPostingModel.setPollOptions(pollOptionList);
            pollPostingModel.setPollPrivacy(POLL_PRIVACY_PUBLIC);
            pollPostingModel.setSharedOn(POLL_SHARED_ON_FEED);
            pollPostingModel.setPollType(POLL_TYPE_PUBLISHED);
            Log.d(TAG, "setupOnClickListener tvPostPoll: "+pollPostingModel);
            Gson gson = new Gson();
            String pollPostingModelJsonString = gson.toJson(pollPostingModel);
            Log.d(TAG, "setupOnClickListener pollPostingModel json: "+pollPostingModelJsonString);


            // region sending poll
            if(!TextUtils.isEmpty(etPollTitle.getText())){

                // checking if any poll option is empty or not
                boolean isAnyPollOptionEmpty = false;
                if(pollOptionList.size()>=2){

                    for(int i=0;i<pollOptionList.size();i++){

                        if(TextUtils.isEmpty(pollOptionList.get(i).getTitle())){

                            isAnyPollOptionEmpty = true;
                            break;
                        }
                    }

                    if(isAnyPollOptionEmpty){
                        Toast.makeText(context, "Poll option can not be empty!", Toast.LENGTH_SHORT).show();
                    }else {

                        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(getContext());
                        qxmProgressDialog.showProgressDialog("Creating Poll","Poll is being created, please wait...",false);

                        Call<QxmApiResponse> createPollNetworkCall = apiService.createNewPoll(token.getToken(),token.getUserId(),pollPostingModel);

                        createPollNetworkCall.enqueue(new Callback<QxmApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                                Log.d(TAG, "onResponse: createPollNetworkCall");
                                Log.d(TAG, "StatusCode: " + response.code());
                                Log.d(TAG, "response body: " + response.body());

                                qxmProgressDialog.closeProgressDialog();

                                if(response.code() == 201){

                                    Toast.makeText(context, "Poll is created successfully", Toast.LENGTH_SHORT).show();
                                    qxmFragmentTransactionHelper.loadHomeFragment();

                                }else if (response.code() == 403) {
                                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                                    qxmFragmentTransactionHelper.logout();
                                }else {

                                    Log.d(TAG, "onResponse: createPollNetworkCall failed");
                                    Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                                qxmProgressDialog.closeProgressDialog();
                                Log.d(TAG, "onFailure: getFeedItem");
                                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                                Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(context, "Poll is ready for posting!", Toast.LENGTH_SHORT).show();


                    }
                }else {

                    Toast.makeText(context, "Minimum 2 poll option is mandatory", Toast.LENGTH_SHORT).show();
                }

            }else {

                Toast.makeText(context, "Enter poll title!", Toast.LENGTH_SHORT).show();
            }
            //endregion


        });

        tvChangePollThumbnail.setOnClickListener(v-> initializePhotoUploadProcess());

        tvRemovePollThumbnail.setOnClickListener(v->{

            pollPostingModel.setThumbnailURL("");
            rlChangeOrDeletePollThumbnail.setVisibility(View.GONE);
            tvAddPollThumbnail.setVisibility(View.VISIBLE);
            ivPollThumbnail.setVisibility(View.GONE);
        });

    }
    //endregion

    //region receiving event after uploading question image

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUploadEvent(Events.OnPhotoUploadMessage onPhotoUploadMessage) {


        String sentImageURL = onPhotoUploadMessage.getSentImageURL();
        String localImageName = onPhotoUploadMessage.getLocalImageName();
        String message = onPhotoUploadMessage.getMessage();
        Log.d(TAG, "onPhotoUploadEvent Message: "+message);
        Log.d(TAG, "onPhotoUploadEvent: " + onPhotoUploadMessage);

        switch (message) {

            case STARTING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent STARTING_PHOTO_UPLOAD: Called");
                tvPostPoll.setClickable(false);
                tvPostPoll.setFocusableInTouchMode(false);
                tvPostPoll.setAlpha(0.5f);
                tvPostPoll.setVisibility(GONE);
                tvUploadingImage.setVisibility(View.VISIBLE);
                break;

            // if photo uploaded successfully, then let user change the photo or remove
            case IMAGE_UPLOAD_SUCCESS:

                Log.d(TAG, "onPhotoUploadEvent IMAGE_UPLOAD_SUCCESS: Called");
                tvAddPollThumbnail.setVisibility(GONE);
                rlChangeOrDeletePollThumbnail.setVisibility(View.VISIBLE);
                tvPostPoll.setClickable(true);
                tvPostPoll.setAlpha(1.0f);
                pollPostingModel.setThumbnailURL(sentImageURL);
                tvUploadingImage.setVisibility(View.GONE);
                tvPostPoll.setVisibility(View.VISIBLE);
                break;


            case ERROR_OCCURRED_DURING_PHOTO_UPLOAD:

                Log.d(TAG, "onPhotoUploadEvent ERROR_OCCURRED_DURING_PHOTO_UPLOAD: Called");
                pollPostingModel.setThumbnailURL("");
                ivPollThumbnail.setVisibility(GONE);
                tvAddPollThumbnail.setVisibility(View.VISIBLE);
                rlChangeOrDeletePollThumbnail.setVisibility(GONE);
                tvPostPoll.setClickable(true);
                tvPostPoll.setAlpha(1.0f);
                tvUploadingImage.setVisibility(View.GONE);
                tvPostPoll.setVisibility(View.VISIBLE);
                break;


        }


    }

    //endregion

    // region Override-OnActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // passing image process result for feedback

        imageProcessorForPollThumbnail.onActivityResult(requestCode,resultCode,data);

    }


    //endregion

    // region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();


        if(!EventBus.getDefault().isRegistered(this)){
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

    //region onStop

    @Override
    public void onStop() {
        super.onStop();
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

    //region initializePhotoUploadProcess
    private void initializePhotoUploadProcess(){

        tvPostPoll.setClickable(false);
        tvPostPoll.setFocusableInTouchMode(false);
        StaticValues.onActivityCall = ON_ACTIVITY_CALL_FOR_POLL_THUMBNAIL;
        imageProcessorForPollThumbnail = new ImageProcessor(context,CreatePollFragment.this,TAG,apiService,token,ivPollThumbnail,tvPostPoll);
        imageProcessorForPollThumbnail.pickImageFromGallery();


    }
    //endregion

}
