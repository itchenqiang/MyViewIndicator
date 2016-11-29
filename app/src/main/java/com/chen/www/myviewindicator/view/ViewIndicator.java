package com.chen.www.myviewindicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.www.myviewindicator.R;

import java.util.List;

/**
 * ViewIndicator.class
 * Created by ChenQiang on 2016/4/14.
 */
public class ViewIndicator extends LinearLayout {
    /**
     * 三角形宽
     */
    private int mTrangleWideth;
    /**
     * 三角形宽
     */
    private int mTrangleHeight;
    /**
     * 初始化偏移X
     */
    private int mInitTranslateX;
    /**
     * 偏移量
     */
    private int mTranslateX = 0;
    private static final float RATIO_WIDETH = 1 / 6f;
    private Paint mPaint;
    private Path mPath;
    /**
     * 可见tab
     */
    private int mVisibleTab;
    /**
     * 默认可见tab
     */
    private static final int DEFAULT_VISIBLE_TAB = 3;
    private static final int COLOR_TEXT_NORMAL = 0x77000000;
    private static final int COLOR_TEXT_HIGHTLIGHT = 0xffffffff;

    public ViewIndicator(Context context) {
        super(context);
    }

    public ViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.ViewIndicator);
        mVisibleTab = td.getInt(R.styleable.ViewIndicator_visible_tab_count, DEFAULT_VISIBLE_TAB);
        if (mVisibleTab < 0) {
            mVisibleTab = DEFAULT_VISIBLE_TAB;
        }
        td.recycle();
        mPaint = new Paint();
        //防止锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        //让三角形不要太尖锐
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    public ViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslateX + mTranslateX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
        Log.i("cq", "111111111111111111111111");

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("cq", "11111111" + " " + w + " " + h + " " + oldw + " " + oldh);
        mPath = new Path();
        mTrangleWideth = (int) (w / mVisibleTab * RATIO_WIDETH);
        mTrangleHeight = mTrangleWideth / 2;
        mInitTranslateX = w / mVisibleTab / 2 - mTrangleWideth / 2;
        initTrangle();
    }

    /**
     * 画三角形
     */
    private void initTrangle() {
        mPath.moveTo(0, 0);
        mPath.lineTo(mTrangleWideth, 0);
        mPath.lineTo(mTrangleWideth / 2, -mTrangleHeight);
        mPath.close();
    }

    /**
     * 滚动三角形
     *
     * @param position 当前tab得位置
     * @param Offset   偏移量
     */
    public void scroll(int position, float Offset) {
        int tabWidth = getWidth() / mVisibleTab;
//        mTranslateX = (int) (tabWidth * position + tabWidth * Offset);
        mTranslateX = (int) (tabWidth * (position + Offset));
        //当为最后一个tab
        if (position >= (mVisibleTab - 1) && Offset > 0 && getChildCount() > mVisibleTab) {
            this.scrollTo((position - (mVisibleTab - 1)) * tabWidth + (int) (tabWidth * Offset), 0);
        }

        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.weight = 0;
            layoutParams.width = getScreenWidth() / mVisibleTab;
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 获的屏幕的宽
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 设置可见tab数量
     *
     * @param count 可见的数量
     */
    public void setTabVisibleCount(int count) {
        mVisibleTab = count;
    }

    /**
     * 设置tab的标题
     *
     * @param titles 标题集合
     */
    public void setTabTitle(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            for (String title : titles) {
                addView(generalView(title));
            }
        }
    }

    /**
     * 根据title创建tab
     *
     * @param title 标题
     * @return view
     */
    private View generalView(String title) {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.width = getScreenWidth() / mVisibleTab;
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(COLOR_TEXT_NORMAL);
        return textView;
    }


    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    private OnPageChangeListener mListener;

    /**
     * 给viewpager设置addOnPageChangeListener
     *
     * @param listener listener
     */
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置关联的viewpager
     *
     * @param viewpager 关联的viewpager
     * @param position  当前viewpager的位置
     */
    public void setViewPager(ViewPager viewpager, int position) {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mListener != null)
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null)
                    mListener.onPageSelected(position);
                hightLightTextView(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null)
                    mListener.onPageScrollStateChanged(state);

            }
        });
        viewpager.setCurrentItem(position);
        hightLightTextView(position);
        setTabClickListener(viewpager);
    }

    /**
     * 设置tab点击事件
     *
     * @param viewpager viewpager
     */
    private void setTabClickListener(final ViewPager viewpager) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                final int j = i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewpager.setCurrentItem(j);
                        hightLightTextView(j);
                    }
                });
            }
        }
    }

    /**
     * 设置选中tab高亮
     *
     * @param position 设置高亮的位置
     */
    public void hightLightTextView(int position) {
        resetTextColor();
        setViewColor(position, COLOR_TEXT_HIGHTLIGHT);
    }

    /**
     * 设置textView颜色
     *
     * @param position tab的位置
     * @param color    设置的颜色
     */
    private void setViewColor(int position, int color) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    /**
     * 重置tab颜色
     */
    public void resetTextColor() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            setViewColor(i, COLOR_TEXT_NORMAL);
        }
    }
}
