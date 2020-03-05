package com.muxi.workbench.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.muxi.workbench.R;
import com.muxi.workbench.commonUtils.SPUtils;
import com.muxi.workbench.ui.login.LoginActivity;
import com.muxi.workbench.ui.login.model.UserWrapper;

public class MineFragment extends Fragment {

    private Button mLogoutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        mLogoutBtn = view.findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(v -> {
            SPUtils.getInstance(SPUtils.SP_CONFIG).clear();
            UserWrapper.getInstance().setUser(null);
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return view;
    }
}
