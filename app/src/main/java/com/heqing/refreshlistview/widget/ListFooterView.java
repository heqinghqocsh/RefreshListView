package com.heqing.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heqing.refreshlistview.R;


/**
 * Created by 何清 on 2016/7/20.
 *
 * @description
 */
public class ListFooterView extends LinearLayout{
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_LOADING = 2;

    private LinearLayout mRootView;
    private RelativeLayout mFooterContainer;
    private ProgressBar mProgress;
    private TextView mFooterTip;

    private int mState = STATE_NORMAL;

    public ListFooterView(Context context) {
        super(context);
        init(context);
    }

    public ListFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mRootView = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.list_footer_view_layout, null);
        mFooterContainer = (RelativeLayout)mRootView.findViewById(R.id.footer_content);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , 0);
        addView(mRootView,layoutParams);

        mProgress = (ProgressBar)mRootView.findViewById(R.id.footer_progress);
        mFooterTip = (TextView)mRootView.findViewById(R.id.footer_tip);
    }

    public void setState(int state){
        if(mState == state){
            return;
        }
        if (state == STATE_LOADING){
            mFooterTip.setText(R.string.refresh_listview_footer_loading);
            mProgress.setVisibility(VISIBLE);
        }else{
            mProgress.setVisibility(GONE);
            switch (state){
                case STATE_NORMAL:
                    mFooterTip.setText(R.string.refresh_listview_footer_normal);
                    break;
                case STATE_READY:
                    mFooterTip.setText(R.string.refresh_listview_footer_ready);
                    break;
            }
        }
        mState = state;
    }

    public void setBottomMargin(float margin){
        if(margin < 0){
            margin = 0;
        }
        LayoutParams layoutParams =
                (LayoutParams)mFooterContainer.getLayoutParams();
        layoutParams.bottomMargin = (int)margin;
        mFooterContainer.setLayoutParams(layoutParams);
    }

    public int getBottomMargin(){
        LayoutParams layoutParams =
                (LayoutParams)mFooterContainer.getLayoutParams();
        return layoutParams.bottomMargin;
    }

    public void setVisibleHeight(float height){
        if (height < 0){
            return;
        }
        LayoutParams layoutParams = (LayoutParams)mRootView.getLayoutParams();
        layoutParams.height = (int)height;
        mRootView.setLayoutParams(layoutParams);
    }

    public int getRealFooterContentHeight(){
        return mFooterContainer.getHeight();
    }

    public int getVisibleHeight(){
        return getHeight();
    }



}
