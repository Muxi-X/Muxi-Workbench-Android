package com.muxi.workbench.ui.home.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.HomeContract;
import com.muxi.workbench.ui.home.model.FeedBean;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private List<FeedBean.DataListBean> mDataList;
    private ItemListener mListener;
    private HomeContract.Presenter mPresenter;

    FeedAdapter(FeedBean feedBean, HomeContract.Presenter presenter, ItemListener listener) {
        mPresenter = presenter;
        mDataList = feedBean.getDataList();
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new VH(mLayoutInflater.inflate(R.layout.item_home_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FOOTER) return;

        FeedBean.DataListBean mData = mDataList.get(position);
        FeedBean.DataListBean.UserBean mUser = mData.getUser();
        FeedBean.DataListBean.SourceBean mSource = mData.getSource();
        VH vh = (VH) holder;

        //设置分割线
        if (mData.isIfsplit()) vh.mSplitView.setVisibility(View.VISIBLE);
        else vh.mSplitView.setVisibility(View.GONE);
        vh.mSplitView.setTextDate(mData.getTimeday());
        vh.mSplitView.setTextSign(mData.getTimehm());

        vh.mHeadShot.setImageURI(mUser.getAvatar_url());
        vh.mName.setText(mData.getUser().getName());
        vh.mProjectName.setText(mSource.getObject_name());
        vh.mTime.setText(mData.getTimehm());
        vh.mStatus.setText(getObjectNameFromId(mData.getAction(),
                mSource.getKind_id(), mSource.getProject_name()));


        vh.mHeadShot.setOnClickListener(view -> mListener.onNameClick());
        vh.mContent.setOnClickListener(view -> {
            mListener.onFileClick(mSource.getObject_id(), mUser.getName(),
                    mUser.getAvatar_url(), mSource.getObject_name());
        });
    }

    private String getObjectNameFromId(String action, int kind_id, String projectName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(action);
        switch (kind_id) {
            case 1:
                stringBuilder.append("了团队");
                break;
            case 2:
                stringBuilder.append("了项目");
                break;
            case 3:
                stringBuilder.append("了文档");
                break;
            case 4:
                stringBuilder.append("了文件");
                break;
            case 5:
            case 6:
                stringBuilder.append("了进度");
                break;
            default:
                break;
        }
        if (!projectName.contains("noname"))
            stringBuilder.append(projectName);

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //普通的显示状态的item
    private class VH extends RecyclerView.ViewHolder {

        SimpleDraweeView mHeadShot;
        TextView mName, mStatus, mProjectName, mTime;
        SplitView mSplitView;
        ConstraintLayout mContent;

        private VH(@NonNull View itemView) {
            super(itemView);
            mSplitView = itemView.findViewById(R.id.split_bar);
            mHeadShot = itemView.findViewById(R.id.head_shot);
            mContent = itemView.findViewById(R.id.item_content);
            mName = itemView.findViewById(R.id.item_name);
            mStatus = itemView.findViewById(R.id.item_status);
            mProjectName = itemView.findViewById(R.id.item_project_name);
            mTime = itemView.findViewById(R.id.item_time);
        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        TextView textView;

        private VHFooter(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.footer_text);
            textView.setOnClickListener(view -> mPresenter.loadMore());
        }
    }


    void replaceData(FeedBean feedBean) {
        Log.e("TAG", "feedAdapter replaceData");
        mDataList = feedBean.getDataList();
        notifyDataSetChanged();
    }

    void addData(FeedBean feedBean) {
        mDataList.addAll(feedBean.getDataList());
        notifyDataSetChanged();
    }


    interface ItemListener {

        void onNameClick();

        void onFileClick(int sid, String username, String avatar, String title);
    }

}
