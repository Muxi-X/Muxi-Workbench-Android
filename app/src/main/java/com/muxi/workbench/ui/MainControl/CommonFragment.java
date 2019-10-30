package com.muxi.workbench.ui.MainControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.muxi.workbench.R;

public class CommonFragment extends Fragment {



    public static CommonFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        CommonFragment fragment = new CommonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_common,container,false);
        TextView textView=view.findViewById(R.id.fragment_text);
        textView.setText(getArguments().getString("name"));
        return view;
    }
}