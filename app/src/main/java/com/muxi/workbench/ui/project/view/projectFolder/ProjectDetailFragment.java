package com.muxi.workbench.ui.project.view.projectFolder;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.muxi.workbench.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailFragment extends Fragment {


    private List<Fragment>mFragments;
    private ProjectFolderFragment mdocFragment;
    private ProjectFolderFragment mFileFragment;
    private Toolbar mToobar;
    private static final String PID="pid";
    private int pid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments=new ArrayList<>();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pid=((ProjectDetailActivity)context).getPid();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView=inflater.inflate(R.layout.fragment_project_detail,container,false);
        TabLayout tabLayout = fragmentView.findViewById(R.id.project_table_layout);
        ViewPager mViewPager = fragmentView.findViewById(R.id.project_viewPager);
        mToobar=fragmentView.findViewById(R.id.project_title_bar);
        List<String> titles = new ArrayList<>();
        titles.add("文档");
        titles.add("文件");
        tabLayout.addTab(tabLayout.newTab().setText("文档"));
        tabLayout.addTab(tabLayout.newTab().setText("文件"));
        tabLayout.setupWithViewPager(mViewPager);

        if (mdocFragment==null){
            mdocFragment=ProjectFolderFragment.newInstance(pid,0);
            mFragments.add(mdocFragment);
        }
        if (mFileFragment==null){
            mFileFragment=ProjectFolderFragment.newInstance(pid,1);
            mFragments.add(mFileFragment);
        }

        FolderViewPagerAdapter viewPagerAdapter=new FolderViewPagerAdapter(getChildFragmentManager(),mFragments,titles);
        mViewPager.setAdapter(viewPagerAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToobar.setNavigationOnClickListener(v -> {
                viewPagerAdapter.getmCurrentFragment().backToPreFolder();
            });
        }


        return fragmentView;

    }
    public static ProjectDetailFragment newInstance(int pid){

        Bundle args = new Bundle();
        args.putInt(PID,pid);
        ProjectDetailFragment fragment=new ProjectDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }




}
