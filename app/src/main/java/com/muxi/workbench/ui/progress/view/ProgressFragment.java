package com.muxi.workbench.ui.progress.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;

public class ProgressFragment extends Fragment {

    private ProgressTitleBar mProgressTitleBar;

    private RecyclerView mProgressListRv;

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);

        mProgressTitleBar = root.findViewById(R.id.ptb_progress);
        mProgressListRv = root.findViewById(R.id.rv_progress);


        return root;
    }

}