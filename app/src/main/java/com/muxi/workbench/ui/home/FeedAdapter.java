package com.muxi.workbench.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.commonUtils.TransCodingUtil;
import com.muxi.workbench.ui.home.model.FeedBean;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FeedBean.DataListBean> mDataList;
    private ItemListener mListener;
    private HomeContract.Presenter mPresenter;
    private View mFooter;

    public FeedAdapter(FeedBean feedBean, HomeContract.Presenter presenter, ItemListener listener) {

        // TODO: 11/15/19 初始化mBean

        mPresenter = presenter;
        mDataList = feedBean.getDataList();
        mListener = listener;

    }

    public void setFooter(View FooterView) {
        mFooter = FooterView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1 && mFooter != null)
            return new VHFooter(mFooter);

        mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new VH(mLayoutInflater.inflate(R.layout.item_home_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FOOTER) return;

        FeedBean.DataListBean mData = mDataList.get(position);
        FeedBean.DataListBean.UserBean mUser = mData.getUser();
        FeedBean.DataListBean.SourceBean mSource = mData.getSource();
        StringBuilder stringBuilder = new StringBuilder();
        VH vh = (VH) holder;

        //设置分割线
        if (mData.isIfsplit()) vh.mSplitView.setVisibility(View.VISIBLE);
        vh.mSplitView.setTextDate(mData.getTimeday());
        vh.mSplitView.setTextSign(mData.getTimehm());

        vh.mTime.setText(mData.getTimehm());

        vh.mHeadShot.setImageURI(mUser.getAvatar_url());


        vh.mContent.setText(mSource.getObject_name());
        vh.mName.setText(mData.getUser().getName());


        vh.mTime.setText(mData.getTimehm());


        stringBuilder.append(mData.getAction());
        switch (mSource.getProject_id()) {
            case 1:
                stringBuilder.append("团队");
                break;

            case 2:
                stringBuilder.append("项目");
                break;

            case 3:
                stringBuilder.append("文档");
                break;

            case 4:
                stringBuilder.append("文件");
                break;

            case 5:
                stringBuilder.append("");
                break;

            case 6:
                stringBuilder.append("进度");
                break;

            default:
                break;
        }
        stringBuilder.append(mSource.getProject_name());
        vh.mStatus.setText(stringBuilder);


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //普通的显示状态的item
    private class VH extends RecyclerView.ViewHolder {

        SimpleDraweeView mHeadShot;
        TextView mName, mStatus, mContent, mTime;
        SplitView mSplitView;

        private VH(@NonNull View itemView) {
            super(itemView);

            mSplitView = itemView.findViewById(R.id.split_bar);
            mHeadShot = itemView.findViewById(R.id.head_shot);
            mName = itemView.findViewById(R.id.item_name);
            mStatus = itemView.findViewById(R.id.item_status);
            mContent = itemView.findViewById(R.id.item_content);
            mTime = itemView.findViewById(R.id.item_time);


            mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onFileClick();

                }
            });
            mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onNameClick();
                }
            });

            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onNameClick();
                }
            });


        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        TextView textView;

        private VHFooter(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.footer_text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.loadMore();
                }
            });
        }
    }


    public void replaceData(FeedBean feedBean) {
        Log.e("TAG", "feedAdapter replaceData");
        mDataList = feedBean.getDataList();
        notifyDataSetChanged();
    }

    public void addData(FeedBean feedBean) {
        mDataList.addAll(feedBean.getDataList());
        notifyDataSetChanged();
    }

    interface ItemListener {
        void onNameClick();

        void onFileClick();
    }

}
