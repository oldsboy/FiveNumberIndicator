package com.oldsboy.fivenumberindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @ProjectName: Monitor5.0
 * @Package: com.medicine.monitor.view
 * @ClassName: FiveNumberIndicator
 * @Description: java类作用描述
 * @Author: 作者名 oldsboy
 * @CreateDate: 2020/6/17 16:02
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/6/17 16:02
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FiveNumberIndicator extends LinearLayout {
    private ImageView imgFirst;
    private ImageView imgLast;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private LinearLayout containerItem;

    /**
     * attrs
     */
    private int lastPageIndex = -1;
    private int itemSize = px2dp(24);
    private int max = -1;

    private OnIndicatorSelectListener onIndicatorSelectListener;

    /**
     * function
     */

    public ImageView getFirstPageImageView() {
        return imgFirst;
    }

    public ImageView getLastPageImageView() {
        return imgLast;
    }

    public void setIndicatorTextSize(int spSize){
        if (spSize > 0) {
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }else {
            int textSize = (int) (itemSize * (2f/3f));

            tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    public void setIndicatorTextSelectBackgroundResource(@DrawableRes int resId){
        tv3.setBackgroundResource(resId);
    }

    public void setIndicatorTextUnSelectBackgroundResource(@DrawableRes int resId){
        tv1.setBackgroundResource(resId);
        tv2.setBackgroundResource(resId);
        tv4.setBackgroundResource(resId);
        tv5.setBackgroundResource(resId);
    }

    public void setIndicatorTextColor(@ColorInt int color){
        tv1.setTextColor(color);
        tv2.setTextColor(color);
        tv3.setTextColor(color);
        tv4.setTextColor(color);
        tv5.setTextColor(color);
    }

    /**
     * 设置首尾按钮颜色
     */
    public void setPageButtonColor(@ColorInt int color){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_first_page_black_24dp);
        if (drawable != null) {
            Drawable mutate = DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTint(mutate, color);
            imgFirst.setImageDrawable(drawable);
        }
        Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.ic_last_page_black_24dp);
        if (drawable2 != null) {
            Drawable mutate = DrawableCompat.wrap(drawable2).mutate();
            DrawableCompat.setTint(mutate, color);
            imgLast.setImageDrawable(drawable2);
        }
    }

    public void setOnIndicatorSelectListener(OnIndicatorSelectListener onIndicatorSelectListener) {
        this.onIndicatorSelectListener = onIndicatorSelectListener;
    }

    public void setMax(int max){
        this.max = max;
    }

    public void setItemSize(int itemSize){
        this.itemSize = itemSize;
    }

    public void select(int index) {
        if (index > (max-1)){
            throw new IndexOutOfBoundsException("选择的索引超过了最大值");
        }
        index += 1;     //  让index为1开头的页数

        final int onePageDistance = px2dp(2*3) + itemSize;
        TranslateAnimation translateAnimation = null;
        if (lastPageIndex > index){          //  整体向右移动
            translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, -onePageDistance, Animation.ABSOLUTE, 0
                    , Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        }else if (lastPageIndex < index){    //  整体向左移动
            translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, onePageDistance, Animation.ABSOLUTE, 0
                    , Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        }
        if (translateAnimation != null) {
            translateAnimation.setDuration(600);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            translateAnimation.setFillAfter(false);
            containerItem.startAnimation(translateAnimation);
        }
        lastPageIndex = index;

        int endNumber = 0;
        int startNumber = 0;
        if ((index+1) < (max+1)){   //  当前选择的item还能有1个末尾
            setViewShowHide(VISIBLE, tv3, tv4);

            tv3.setText(String.valueOf(index));
            tv4.setText(String.valueOf(index+1));
            endNumber = 1;
        }

        if ((index+2) < (max+1)){   //  当前选择的item还能有2个末尾
            setViewShowHide(VISIBLE, tv5);

            tv5.setText(String.valueOf(index+2));
            endNumber = 2;
        }

        if ((index-1) > 0){   //  当前选择的item还能有1个前缀
            setViewShowHide(VISIBLE, tv2, tv3);

            tv2.setText(String.valueOf(index-1));
            tv3.setText(String.valueOf(index));
            startNumber = 1;
        }

        if ((index-2) > 0){   //  当前选择的item还能有2个前缀
            setViewShowHide(VISIBLE, tv1);

            tv1.setText(String.valueOf(index-2));
            startNumber = 2;
        }

        if (index == 1 && max == 1){
            setViewShowHide(VISIBLE, tv3);

            tv3.setText(String.valueOf(index));
        }

        //  回收情况
        if (startNumber == 0){
            setViewShowHide(GONE, tv1, tv2);
        }else if (startNumber == 1){
            setViewShowHide(GONE, tv1);
        }

        if (endNumber == 0){
            setViewShowHide(GONE, tv4, tv5);
        }else if (endNumber == 1){
            setViewShowHide(GONE, tv5);
        }

        //  首尾按钮情况
        if (index == 1){
            setViewShowHide(GONE, imgFirst);
        }else {
            setViewShowHide(VISIBLE, imgFirst);
        }

        if (index == max){
            setViewShowHide(GONE, imgLast);
        }else {
            setViewShowHide(VISIBLE, imgLast);
        }
    }

    /**
     * constructor
     */
    public FiveNumberIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View root = LayoutInflater.from(context).inflate(R.layout.indicator_base_layout, this, false);
        this.bindViews(root);
        this.initItemSize(context, attrs);
        this.initItemAttr(context, attrs);
        this.setIndicatorTextSize(-1);
        this.addView(root);
    }

    private void initItemAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FiveNumberIndicator);
        Drawable selectDrawable = typedArray.getDrawable(R.styleable.FiveNumberIndicator_item_select);
        Drawable unSelectDrawable = typedArray.getDrawable(R.styleable.FiveNumberIndicator_item_unselect);
        typedArray.recycle();

        if (selectDrawable != null) {
            tv3.setBackground(selectDrawable);
        }else {
            setIndicatorTextSelectBackgroundResource(R.drawable.indicator_item_bg);
        }
        if (unSelectDrawable != null) {
            tv1.setBackground(unSelectDrawable);
            tv2.setBackground(unSelectDrawable);
            tv4.setBackground(unSelectDrawable);
            tv5.setBackground(unSelectDrawable);
        }else {
            setIndicatorTextUnSelectBackgroundResource(R.drawable.indicator_item_un_bg);
        }
    }

    private void initItemSize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FiveNumberIndicator);
        itemSize = (int) typedArray.getDimension(R.styleable.FiveNumberIndicator_item_size, itemSize);
        typedArray.recycle();

        ViewGroup.LayoutParams layoutParams = tv1.getLayoutParams();
        layoutParams.width = itemSize;
        layoutParams.height = itemSize;
        tv1.setLayoutParams(layoutParams);
        tv2.setLayoutParams(layoutParams);
        tv3.setLayoutParams(layoutParams);
        tv4.setLayoutParams(layoutParams);
        tv5.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams firstParams = imgFirst.getLayoutParams();
        firstParams.width = itemSize;
        firstParams.height = itemSize;
        imgFirst.setLayoutParams(firstParams);
        ViewGroup.LayoutParams lastParams = imgLast.getLayoutParams();
        lastParams.width = itemSize;
        lastParams.height = itemSize;
        imgLast.setLayoutParams(lastParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
        } else {
            int showPageNumber = 0;
            int showButtonNumber = 0;
            if (tv1.getVisibility() == View.VISIBLE) showPageNumber ++;
            if (tv2.getVisibility() == View.VISIBLE) showPageNumber ++;
            if (tv3.getVisibility() == View.VISIBLE) showPageNumber ++;
            if (tv4.getVisibility() == View.VISIBLE) showPageNumber ++;
            if (tv5.getVisibility() == View.VISIBLE) showPageNumber ++;
            if (imgFirst.getVisibility() == View.VISIBLE) showButtonNumber ++;
            if (imgLast.getVisibility() == View.VISIBLE) showButtonNumber ++;
            // 2-img, x-itemSize, 3*2*x-margin
            width = (int)((itemSize * showButtonNumber) + (itemSize * showPageNumber) + px2dp(3*2*showPageNumber));
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (itemSize);
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    protected void bindViews(View root) {
        imgFirst = (ImageView) root.findViewById(R.id.img_first);
        imgLast = (ImageView) root.findViewById(R.id.img_last);
        tv1 = (TextView) root.findViewById(R.id.tv1);
        tv2 = (TextView) root.findViewById(R.id.tv2);
        tv3 = (TextView) root.findViewById(R.id.tv3);
        tv4 = (TextView) root.findViewById(R.id.tv4);
        tv5 = (TextView) root.findViewById(R.id.tv5);
        containerItem = (LinearLayout) root.findViewById(R.id.container_item);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selectPage = Integer.valueOf(((TextView) v).getText().toString());
                    int selectIndex = selectPage - 1;
                    select(selectIndex);
                    if (onIndicatorSelectListener != null) {
                        onIndicatorSelectListener.onItemSelect(selectIndex);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };

        tv1.setOnClickListener(onClickListener);
        tv2.setOnClickListener(onClickListener);
        tv3.setOnClickListener(onClickListener);
        tv4.setOnClickListener(onClickListener);
        tv5.setOnClickListener(onClickListener);

        imgFirst.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select(0);
                if (onIndicatorSelectListener != null) {
                    onIndicatorSelectListener.onItemSelect(0);
                }
            }
        });
        imgLast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectIndex = max - 1;
                select(selectIndex);
                if (onIndicatorSelectListener != null) {
                    onIndicatorSelectListener.onItemSelect(selectIndex);
                }
            }
        });
    }

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Visibility {}
    private void setViewShowHide(@Visibility int visibility, View... views){
        for (View view : views) {
            if (view.getVisibility() != visibility) {
                view.setVisibility(visibility);
            }
        }
    }

    protected int px2dp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getContext().getResources().getDisplayMetrics());
    }

    public interface OnIndicatorSelectListener {
        void onItemSelect(int index);
    }
}
