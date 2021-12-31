package com.crux.qxm.views.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnrollFragment extends Fragment {

    private static final String TAG = "EnrollFragment";

    public EnrollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enroll, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity() != null){
            Log.d(TAG, "onStart: ");
            ((BottomNavigationViewEx)getActivity().findViewById(R.id.bottom_navigation_view)).getMenu().getItem(1).setChecked(true);
        }
    }
}
