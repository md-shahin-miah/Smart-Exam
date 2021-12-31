package com.crux.qxm.views.fragments.group;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupReceivedJoinRequestFragment extends Fragment {

    private static final String TAG = "GroupReceivedJoinReques";

    @BindView(R.id.swipeRL)
    SwipeRefreshLayout swipeRL;
    @BindView(R.id.rvGroupReceivedJoinReq)
    RecyclerView rvGroupReceivedJoinReq;

    public GroupReceivedJoinRequestFragment() {
        // Required empty public constructor
    }

    public static GroupReceivedJoinRequestFragment newInstance() {

        Bundle args = new Bundle();

        GroupReceivedJoinRequestFragment fragment = new GroupReceivedJoinRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_received_join_request, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}
