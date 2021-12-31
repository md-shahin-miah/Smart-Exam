package com.crux.qxm.views.fragments.create;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.crux.qxm.App;
import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.createFragmentFeature.CreateFragmentComponent;
import com.crux.qxm.di.createFragmentFeature.DaggerCreateFragmentComponent;
import com.crux.qxm.utils.FirebaseAnalyticsImpl;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.views.activities.CreateQxmActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static com.crux.qxm.utils.StaticValues.CREATE_QXM_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {

    private static final String TAG = "CreateFragment";
    @Inject
    Picasso picasso;
    @BindView(R.id.ivSearch)
    AppCompatImageView ivSearch;
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.rlCreateSetOfQuestions)
    FrameLayout rlCreateSetOfQuestions;
    @BindView(R.id.rlCrateSingleMultipleChoice)
    FrameLayout rlCrateSingleMultipleChoice;
    @BindView(R.id.rlCreatePoll)
    FrameLayout rlCreatePoll;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private Context context;


    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        setUpDagger2(context);

        init();

        initClickListeners(view.getContext());

        //firebase  analytics log
        FirebaseAnalyticsImpl.logEvent(context, TAG, TAG);
    }

    private void setUpDagger2(Context context) {
        CreateFragmentComponent createFragmentComponent = DaggerCreateFragmentComponent.builder()
                .appComponent(App.get((AppCompatActivity) context).getAppComponent())
                .build();
        createFragmentComponent.injectCreateFragmentFeature(CreateFragment.this);
    }

    private void init() {
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        UserBasic userBasic = realmService.getSavedUser();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(getActivity());

        if (!TextUtils.isEmpty(userBasic.getProfilePic()))
            picasso.load(userBasic.getModifiedURLProfileImage())
                    .error(getResources().getDrawable(R.drawable.ic_user_default))
                    .into(ivUserImage);
        else
            ivUserImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_default));


        ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(userBasic.getUserId(), userBasic.getFullName()));
        ivSearch.setOnClickListener(v -> qxmFragmentTransactionHelper.loadSearchFragmentFragment());
    }

    private void initClickListeners(Context context) {

        rlCreateSetOfQuestions.setOnClickListener(v ->
                Objects.requireNonNull(getActivity()).startActivityForResult(new Intent(getActivity(), CreateQxmActivity.class), CREATE_QXM_REQUEST)
        );

        rlCrateSingleMultipleChoice.setOnClickListener(v -> qxmFragmentTransactionHelper.loadCreateSingleMultipleChoiceFragment());

        rlCreatePoll.setOnClickListener(v -> qxmFragmentTransactionHelper.loadCreatePollFragment());
    }
}
