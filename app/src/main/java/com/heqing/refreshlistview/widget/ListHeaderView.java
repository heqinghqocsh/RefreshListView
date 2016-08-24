package com.heqing.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heqing.refreshlistview.R;


/**
 * Created by 何清 on 2016/7/20.
 *
 * @description 下拉刷新的头
 */
public class ListHeaderView extends LinearLayout {
    private static final int ROTATE_DURATION = 180;
    public static final int STATE_REFRESHING = 0;
    public static final int STATE_NORMAL = 1;
    public static final int STATE_READY = 2;

    private LinearLayout rootView;
    private RelativeLayout mHeaderContainer;
    private TextView mHeaderTip;
    private ImageView mHeaderArrow;
    private ProgressBar mProgressBar;

    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;

    private int mState = STATE_NORMAL;

    public ListHeaderView(Context context) {
        super(context);
        init(context);
    }

    public ListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutParams layoutParams = new
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);

        rootView = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.list_header_view_layout, null);
        mHeaderContainer = (RelativeLayout)rootView.findViewById(R.id.real_header_container);
        rootView.setLayoutParams(layoutParams);

        addView(rootView);

        mHeaderTip = (TextView) findViewById(R.id.header_tip);
        mHeaderArrow = (ImageView) findViewById(R.id.header_arrow);
        mProgressBar = (ProgressBar) findViewById(R.id.header_progress);

        mRotateUpAnim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        if (state == STATE_REFRESHING) {
            mHeaderArrow.clearAnimation();
            mHeaderArrow.setVisibility(INVISIBLE);
            mProgressBar.setVisibility(VISIBLE);
            mHeaderTip.setText(R.string.refresh_listview_header_refreshing);
        } else {
            mHeaderArrow.setVisibility(VISIBLE);
            mProgressBar.setVisibility(INVISIBLE);
            switch (state) {
                case STATE_READY:
                    if (mState != STATE_READY) {
                        mHeaderArrow.clearAnimation();
                        mHeaderArrow.startAnimation(mRotateUpAnim);
                        mHeaderTip.setText(R.string.refresh_listview_header_ready);
                    }
                    break;
                case STATE_NORMAL:
                    if (mState != STATE_NORMAL) {
                        mHeaderArrow.clearAnimation();
                        mHeaderArrow.startAnimation(mRotateDownAnim);
                        mHeaderTip.setText(R.string.refresh_listview_header_normal);
                    }
                    break;
            }
        }
        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams layoutParams = (LayoutParams) rootView.getLayoutParams();
        layoutParams.height = height;
        rootView.setLayoutParams(layoutParams);
    }

    public int getVisibleHeight() {
        return getHeight();
    }

    public int getRealHeaderHeight() {
        return mHeaderContainer.getHeight();
    }

}
