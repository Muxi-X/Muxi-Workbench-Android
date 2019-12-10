package com.muxi.workbench.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.model.FeedBean;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private FeedBean mBean = new FeedBean();
    private List<FeedBean.DataListBean> mDataList;
    private ItemListener mListener;
    private HomeContract.Presenter mPresenter;

    public FeedAdapter(FeedBean feedBean, HomeContract.Presenter presenter) {
        // TODO: 11/15/19 初始化mBean

        mPresenter = presenter;
        mBean = feedBean;
        mDataList = mBean.getDataList();


    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new VH(mLayoutInflater.inflate(R.layout.item_home_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        FeedBean.DataListBean mData = mDataList.get(position);
        FeedBean.DataListBean.UserBean mUser = mData.getUser();
        FeedBean.DataListBean.SourceBean mSource = mData.getSource();


        VH vh = (VH) holder;
        vh.mTime.setText(mData.getTimehm());

        vh.mHeadShot.setImageURI(mUser.getAvatar_url());

        vh.mContent.setText(mSource.getObject_id());

        vh.mTime.setText(mData.getTimehm());

        if (mData.isIfsplit()) vh.mSplitView.setVisibility(View.VISIBLE);


        vh.mStatus.setText("创建了进度");


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //普通的显示状态的item
    public class VH extends RecyclerView.ViewHolder {

        SimpleDraweeView mHeadShot;
        TextView mId, mStatus, mContent, mTime;
        SplitView mSplitView;

        public VH(@NonNull View itemView) {
            super(itemView);

            mSplitView = itemView.findViewById(R.id.split_bar);
            mHeadShot = itemView.findViewById(R.id.head_shot);
            mId = itemView.findViewById(R.id.item_id);
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
            mId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onNameClick();
                }
            });


        }
    }


    public void replaceData(FeedBean feedBean) {
        mBean = feedBean;
        notifyDataSetChanged();
    }

    interface ItemListener {
        void onNameClick();

        void onFileClick();
    }

}
