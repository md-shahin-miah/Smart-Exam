package com.crux.qxm.views.fragments.sendFeedback;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.userFeedback.UserFeedback;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.sendFeedbackFragmentFeature.DaggerSendFeedbackFragmentComponent;
import com.crux.qxm.di.sendFeedbackFragmentFeature.SendFeedbackFragmentComponent;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.AVERAGE;
import static com.crux.qxm.utils.StaticValues.AWESOME;
import static com.crux.qxm.utils.StaticValues.BAD;
import static com.crux.qxm.utils.StaticValues.BUG;
import static com.crux.qxm.utils.StaticValues.COMMENT;
import static com.crux.qxm.utils.StaticValues.GOOD;
import static com.crux.qxm.utils.StaticValues.OTHERS;
import static com.crux.qxm.utils.StaticValues.UGLY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendFeedbackFragment extends Fragment {

    // region Fragment-Properties


    @Inject
    Retrofit retrofit;

    private static final String TAG = "SendFeedbackFragment";
    private Context context;
    private QxmApiService apiServices;
    private RealmService realmService;
    private QxmToken token;
    private UserFeedback userFeedback;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region BindViewWithButterKnife
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.rgContent)
    RadioGroup rgContent;
    @BindView(R.id.rbContentUgly)
    AppCompatRadioButton rbContentUgly;
    @BindView(R.id.rbContentBad)
    AppCompatRadioButton rbContentBad;
    @BindView(R.id.rbContentNotBad)
    AppCompatRadioButton rbContentNotBad;
    @BindView(R.id.rbContentGood)
    AppCompatRadioButton rbContentGood;
    @BindView(R.id.rbContentAwesome)
    AppCompatRadioButton rbContentAwesome;

    @BindView(R.id.rgDesign)
    RadioGroup rgDesign;
    @BindView(R.id.rbDesignUgly)
    AppCompatRadioButton rbDesignUgly;
    @BindView(R.id.rbDesignBad)
    AppCompatRadioButton rbDesignBad;
    @BindView(R.id.rbDesignNotBad)
    AppCompatRadioButton rbDesignNotBad;
    @BindView(R.id.rbDesignGood)
    AppCompatRadioButton rbDesignGood;
    @BindView(R.id.rbDesignAwesome)
    AppCompatRadioButton rbDesignAwesome;

    @BindView(R.id.rgEasyOfUse)
    RadioGroup rgEasyOfUse;
    @BindView(R.id.rbEasyOfUseUgly)
    AppCompatRadioButton rbEasyOfUseUgly;
    @BindView(R.id.rbEasyOfUseBad)
    AppCompatRadioButton rbEasyOfUseBad;
    @BindView(R.id.rbEasyOfUseNotBad)
    AppCompatRadioButton rbEasyOfUseNotBad;
    @BindView(R.id.rbEasyOfUseGood)
    AppCompatRadioButton rbEasyOfUseGood;
    @BindView(R.id.rbEasyOfUseAwesome)
    AppCompatRadioButton rbEasyOfUseAwesome;

    @BindView(R.id.rgOverall)
    RadioGroup rgOverall;
    @BindView(R.id.rbOverallUgly)
    AppCompatRadioButton rbOverallUgly;
    @BindView(R.id.rbOverallBad)
    AppCompatRadioButton rbOverallBad;
    @BindView(R.id.rbOverallNotBad)
    AppCompatRadioButton rbOverallNotBad;
    @BindView(R.id.rbOverallGood)
    AppCompatRadioButton rbOverallGood;
    @BindView(R.id.rbOverallAwesome)
    AppCompatRadioButton rbOverallAwesome;

    @BindView(R.id.rgFeedbackType)
    RadioGroup rgFeedbackType;
    @BindView(R.id.rbComment)
    AppCompatRadioButton rbComment;
    @BindView(R.id.rbBug)
    AppCompatRadioButton rbBug;
    @BindView(R.id.rbOthers)
    AppCompatRadioButton rbOthers;
    @BindView(R.id.etFeedbackMessage)
    AppCompatEditText etFeedbackMessage;

    @BindView(R.id.btnSendFeedback)
    AppCompatButton btnSendFeedback;

    // endregion

    // region Fragment-Constructor
    public SendFeedbackFragment() {
        // Required empty public constructor
    }
    // endregion

    // region Override-OnCreateView

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_feedback, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpDagger2(view.getContext());

        init(view);

        initializeClickListeners();
    }

    // region Init

    private void init(View view) {
        context = view.getContext();
        apiServices = retrofit.create(QxmApiService.class);
        realmService = new RealmService(Realm.getDefaultInstance());
        token = realmService.getApiToken();
        userFeedback = new UserFeedback();
        userFeedback.setAppVersion(getString(R.string.version_code)+" "+getString(R.string.version_variant));
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());
    }

    // endregion

    // region InitializeClickListeners

    private void initializeClickListeners() {

        ivBackArrow.setOnClickListener(v ->
                Objects.requireNonNull(getActivity()).onBackPressed()
        );

        rgContent.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "RadioGroup: Content");
            switch (checkedId) {
                case R.id.rbContentUgly:
                    Log.d(TAG, "Ugly");
                    userFeedback.setContentScore(UGLY);
                    break;

                case R.id.rbContentBad:
                    Log.d(TAG, "Bad");
                    userFeedback.setContentScore(BAD);
                    break;

                case R.id.rbContentNotBad:
                    Log.d(TAG, "Not Bad");
                    userFeedback.setContentScore(AVERAGE);
                    break;

                case R.id.rbContentGood:
                    Log.d(TAG, "Good");
                    userFeedback.setContentScore(GOOD);
                    break;

                case R.id.rbContentAwesome:
                    Log.d(TAG, "Awesome");
                    userFeedback.setContentScore(AWESOME);
                    break;
            }
        });

        rgDesign.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "RadioGroup: Design");
            switch (checkedId) {
                case R.id.rbDesignUgly:
                    Log.d(TAG, "Ugly");
                    userFeedback.setDesignScore(UGLY);
                    break;

                case R.id.rbDesignBad:
                    Log.d(TAG, "Bad");
                    userFeedback.setDesignScore(BAD);
                    break;

                case R.id.rbDesignNotBad:
                    Log.d(TAG, "Not Bad");
                    userFeedback.setDesignScore(AVERAGE);
                    break;

                case R.id.rbDesignGood:
                    Log.d(TAG, "Good");
                    userFeedback.setDesignScore(GOOD);
                    break;

                case R.id.rbDesignAwesome:
                    Log.d(TAG, "Awesome");
                    userFeedback.setDesignScore(AWESOME);
                    break;
            }
        });

        rgEasyOfUse.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "RadioGroup: Easy Of Use");
            switch (checkedId) {
                case R.id.rbEasyOfUseUgly:
                    Log.d(TAG, "Ugly");
                    userFeedback.setEasyOfUseScore(UGLY);
                    break;

                case R.id.rbEasyOfUseBad:
                    Log.d(TAG, "Bad");
                    userFeedback.setEasyOfUseScore(BAD);
                    break;

                case R.id.rbEasyOfUseNotBad:
                    Log.d(TAG, "Not Bad");
                    userFeedback.setEasyOfUseScore(AVERAGE);
                    break;

                case R.id.rbEasyOfUseGood:
                    Log.d(TAG, "Good");
                    userFeedback.setEasyOfUseScore(GOOD);
                    break;

                case R.id.rbEasyOfUseAwesome:
                    Log.d(TAG, "Awesome");
                    userFeedback.setEasyOfUseScore(AWESOME);
                    break;
            }
        });

        rgOverall.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "RadioGroup: Overall");
            switch (checkedId) {
                case R.id.rbOverallUgly:
                    Log.d(TAG, "Ugly");
                    userFeedback.setOverAllScore(UGLY);
                    break;

                case R.id.rbOverallBad:
                    Log.d(TAG, "Bad");
                    userFeedback.setOverAllScore(BAD);
                    break;

                case R.id.rbOverallNotBad:
                    Log.d(TAG, "Not Bad");
                    userFeedback.setOverAllScore(AVERAGE);
                    break;

                case R.id.rbOverallGood:
                    Log.d(TAG, "Good");
                    userFeedback.setOverAllScore(GOOD);
                    break;

                case R.id.rbOverallAwesome:
                    Log.d(TAG, "Awesome");
                    userFeedback.setOverAllScore(AWESOME);
                    break;
            }
        });

        rgFeedbackType.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "RadioGroup: Feedback type");
            switch (checkedId) {
                case R.id.rbComment:
                    Log.d(TAG, "Comment");
                    userFeedback.setFeedbackType(COMMENT);
                    break;

                case R.id.rbBug:
                    Log.d(TAG, "Bug");
                    userFeedback.setFeedbackType(BUG);
                    break;

                case R.id.rbOthers:
                    Log.d(TAG, "Others");
                    userFeedback.setFeedbackType(OTHERS);
                    break;
            }
        });

        btnSendFeedback.setOnClickListener(v -> {
            String feedbackMessage = etFeedbackMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(feedbackMessage)) {
                Log.d(TAG, "feedbackMessage: " + feedbackMessage);
                userFeedback.setFeedbackMessage(feedbackMessage);
                sendFeedbackNetworkCall(token.getToken(), token.getUserId(), userFeedback);

            } else {
                Toast.makeText(context, "Please write something first.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // endregion

    // region SetUpDagger2

    private void setUpDagger2(Context context) {
        SendFeedbackFragmentComponent sendFeedbackFragmentComponent =
                DaggerSendFeedbackFragmentComponent.builder()
                        .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                        .build();

        sendFeedbackFragmentComponent.injectSendFeedbackFragmentFeature(
                SendFeedbackFragment.this
        );
    }

    // endregion

    // region SendFeedbackNetworkCall

    private void sendFeedbackNetworkCall(String token, String userId, UserFeedback userFeedback) {

        Call<QxmApiResponse> sendFeedback = apiServices.sendFeedback(token, userId, userFeedback);

        sendFeedback.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                if (response.code() == 201) {
                    Toast.makeText(context, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout(realmService);
                } else {
                    Toast.makeText(context, "Feedback submission failed. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

            }
        });
    }

    // endregion

    //region Override-OnStart

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Log.d(TAG, "onStart: " + TAG);
            getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
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

}
