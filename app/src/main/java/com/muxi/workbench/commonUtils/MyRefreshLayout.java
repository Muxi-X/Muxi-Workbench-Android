package com.muxi.workbench.commonUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muxi.workbench.R;

public class MyRefreshLayout extends SwipeRefreshLayout {

    private OnLoadMoreListener mLoadMoreListener;
    private RecyclerView mListView;
    private float mDownY, mUpY;
    private boolean isLoading;
    private View mFooter;
    private int mScaledTouchSlop;
    private int mItemCount;

    private FooterCallBack mFooterCallback;

    public MyRefreshLayout(@NonNull Context context) {
        super(context);
        mFooter = View.inflate(getContext(), R.layout.item_home_footer, null);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mListView == null) {

            if (getChildCount() > 0) {

                mListView = (RecyclerView) getChildAt(1);

                mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (canLoadMore() && mLoadMoreListener != null) {
                            loadData();
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }


                });
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mDownY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:

                if (canLoadMore()) {
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                mUpY = getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void loadData() {
        if (mLoadMoreListener != null) {
            setLoading(true);
            mLoadMoreListener.onLoadMore();
        }
    }

    private boolean canLoadMore() {

        //1. 上拉
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1) {
            Log.e("SwipeRefreshLayout--->", "上拉.....");
        }

        //2. 最后一条
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {
            LinearLayoutManager manager = (LinearLayoutManager) mListView.getLayoutManager();
            if (mItemCount > 0) {
                if (mListView.getAdapter().getItemCount() < mItemCount) {
                    condition2 = false;
                } else {
                    condition2 = manager.findLastVisibleItemPosition() == (mListView.getAdapter().getItemCount() - 1);
                }
            } else {
                condition2 = manager.findLastVisibleItemPosition() == (mListView.getAdapter().getItemCount() - 1);
            }
        }
        if (condition2) {
            Log.e("SwipeRefreshLayout--->", "最后一条可见");
        }


        //3. 不是正在刷新
        boolean condition3 = !isLoading;
        if (condition3) {
            Log.e("SwipeRefreshLayout--->", "不是正在刷新");
        }

        return condition1 & condition2 & condition3;
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    public void setFooterCallback(FooterCallBack footerCallback) {
        this.mFooterCallback = footerCallback;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (mFooterCallback != null)
            if (isLoading) {
                mFooterCallback.addFooterView(mFooter);
            } else {
                mFooterCallback.removeFooterView(mFooter);
            }

        mDownY = 0;
        mUpY = 0;
    }

    public interface FooterCallBack {
        void addFooterView(View footerView);

        void removeFooterView(View footerView);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
