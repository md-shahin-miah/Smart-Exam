package com.crux.qxm.views.fragments.performance;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.networkLayer.QxmApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Retrofit;

import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.USER_ID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPerformance extends Fragment {
    // region Fragment-Properties
    @Inject
    Picasso picasso;
    @Inject
    Retrofit retrofit;

    private static final String TAG = "MyPerformance";
    private QxmApiService apiService;
    private Context context;
    private RealmService realmService;
    private QxmToken token;
    //    private MyPerformanceAdapter performanceAdapter;
    private List<MyPerformance> myPerformanceList;


    // endregion


    // region Fragment-Constructor
    public MyPerformance() {
        // Required empty public constructor
    }
    // endregion

    // region MyPerformance-newInstance
    public static MyPerformance newInstance(String qxmId, String userId) {

        Bundle args = new Bundle();
        args.putString(QXM_ID_KEY, qxmId);
        args.putString(USER_ID_KEY, userId);
        MyPerformance fragment = new MyPerformance();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Override-onCreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_performance, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // endregion

    // region Override-OnViewCreated

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
