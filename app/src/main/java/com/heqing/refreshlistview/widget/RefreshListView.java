package com.heqing.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

import com.heqing.refreshlistview.listener.RefreshLoadMoreListener;

/**
 * Created by 何清 on 2016/7/20.
 *
 * @description  自定义的上拉加载，下拉刷新listview
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    private final static int SCROLL_DURATION = 400;
    private final static float OFFSET_RADIO = 1.8f;

    private Scroller mScroller;

    private ListHeaderView mHeaderView;
    private ListFooterView mFooterView;
    private int mRealHeaderHeight = -1;
    private int mRealFooterHeight;
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private boolean mEnablePullLoadmore = true;
    private boolean mPullLoading = false;

    private int mScrollBack;
    private static final int SCROLLBACK_HEADER = 1;
    private static final int SCROLLBACK_FOOTER = 2;

    private float mLastY = 0;
    private float mTotalItemCount;
    private RefreshLoadMoreListener mRefreshLoadMoreListener;
    private boolean mAddFooter = false;
    private boolean mNeedRefresh = false;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mScroller = new Scroller(context,new DecelerateInterpolator());
        setOnScrollListener(this);
        mHeaderView = new ListHeaderView(context);
        addHeaderView(mHeaderView);
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRealHeaderHeight = mHeaderView.getRealHeaderHeight();
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (mNeedRefresh){
                            mNeedRefresh = false;
                            startRefresh();
                        }
                    }
                });
        mFooterView = new ListFooterView(context);
        addFooterView(mFooterView);
        mFooterView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRealFooterHeight = mFooterView.getRealFooterContentHeight();
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

    }

    public void setRefreshLoadMoreListener(RefreshLoadMoreListener listener){
        mRefreshLoadMoreListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        if (visibleItemCount >= totalItemCount){
            removeFooterView(mFooterView);
            mAddFooter = false;
        }else{
            if (!mAddFooter){
                addFooterView(mFooterView);
                mAddFooter = true;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && mEnablePullRefresh){
                    if (!mPullLoading){
                        if (mHeaderView.getVisibleHeight() > 0 || deltaY > 0){
                            updateHeaderHeight(deltaY / OFFSET_RADIO);
                        }
                    }
                } else if(getLastVisiblePosition() == mTotalItemCount - 1 && mEnablePullLoadmore){
                    if (!mPullRefreshing){
                        if (mFooterView.getVisibleHeight() > 0 || deltaY < 0){
                            updateFooterHeight(-deltaY / OFFSET_RADIO);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getFirstVisiblePosition() == 0){
                    if (mEnablePullRefresh && !mPullRefreshing
                            && mHeaderView.getVisibleHeight() > mRealHeaderHeight){
                        mPullRefreshing = true;
                        if (mRefreshLoadMoreListener != null){
                            mRefreshLoadMoreListener.refresh();
                        }
                        mHeaderView.setState(ListHeaderView.STATE_REFRESHING);
                    }
                    resetHeaderHeight();
                }else if(getLastVisiblePosition() == mTotalItemCount - 1){
                    if (mEnablePullLoadmore && !mPullLoading
                            && mFooterView.getVisibleHeight() > mRealFooterHeight){
                        mPullLoading = true;
                        if (mRefreshLoadMoreListener != null){
                            mRefreshLoadMoreListener.loadMore();
                        }
                        mFooterView.setState(ListFooterView.STATE_LOADING);
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void completeRefresh(){
        if (mPullRefreshing){
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    public void completeLoadMore(){
        if (mPullLoading){
            mPullLoading = false;
            resetFooterHeight();
        }
    }

    public void startRefresh(){
        if (mRealHeaderHeight <= 0){
            mNeedRefresh = true;
            return;
        }
        mPullRefreshing = true;
        mHeaderView.setVisibleHeight(mRealHeaderHeight);
        mHeaderView.setState(ListHeaderView.STATE_REFRESHING);
        if (mRefreshLoadMoreListener != null){
            mRefreshLoadMoreListener.refresh();
        }
    }

    public void startLoadmore(){
        mPullLoading = true;
        mFooterView.setVisibleHeight(mRealHeaderHeight);
        mFooterView.setState(ListFooterView.STATE_LOADING);
        if (mRefreshLoadMoreListener != null){
            mRefreshLoadMoreListener.loadMore();
        }
    }

    private void resetHeaderHeight(){
        int height = mHeaderView.getVisibleHeight();
        if (height == 0){
            return;
        }
        if (mPullRefreshing && height <= mRealHeaderHeight){
            return;
        }
        int finalHeight = 0;
        if (mPullRefreshing && height > mRealHeaderHeight){
            finalHeight = mRealHeaderHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private void resetFooterHeight(){
        int height = mFooterView.getVisibleHeight();
        if (height == 0){
            return;
        }
        if (mPullLoading && height <= mRealFooterHeight){
            return;
        }
        int finalHeight = 0;
        if (mPullLoading && height > mRealFooterHeight){
            finalHeight = mRealFooterHeight;
        }
        mScrollBack = SCROLLBACK_FOOTER;
        mScroller.startScroll(0,height,0,-height+finalHeight,SCROLL_DURATION);
        invalidate();
    }

    private void updateFooterHeight(float delta){
        final float height = delta + mFooterView.getVisibleHeight();
        mFooterView.setVisibleHeight(height);
        if (!mPullLoading){
            if (height > mRealFooterHeight){
                mFooterView.setState(ListFooterView.STATE_READY);
            }else{
                mFooterView.setState(ListFooterView.STATE_NORMAL);
            }
        }
    }

    private void updateHeaderHeight(float delta){
        final int height = (int)delta+mHeaderView.getVisibleHeight();
        mHeaderView.setVisibleHeight(height);
        if (!mPullRefreshing){
            if (height > mRealHeaderHeight){
                mHeaderView.setState(ListHeaderView.STATE_READY);//更新箭头状态为ready
            }else{
                mHeaderView.setState(ListHeaderView.STATE_NORMAL);//更新箭头状态为normal
            }
        }
        setSelection(0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            switch (mScrollBack){
                case SCROLLBACK_HEADER:
                    mHeaderView.setVisibleHeight(mScroller.getCurrY());
                    break;
                case SCROLLBACK_FOOTER:
                    mFooterView.setVisibleHeight(mScroller.getCurrY());
                    break;
            }
        }
        super.computeScroll();
    }

}
