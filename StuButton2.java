
package com.esafirm.stubutton;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

public class StuButton2 extends RelativeLayout {

    private static final String TAG = "StuButton";
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_SIZE = 48;
    private static final float ALPHA_MODIFIER = 0.02f;
    private static final int SLIDER_IMAGE_WIDTH = 520;

    private TextView stuTxtLabel;
    private ImageView stuImgThumb;
    private ImageView stuBackground;

    private OnUnlockListener listener;

    private int thumbWidth = 0; // 518
    private boolean sliding = false; // true
    private int sliderPosition = 0; // 922
    private int initialSliderPosition = 0; // 0
    private float initialSlidingX = 0; // 394.987
    private boolean unLock;
    private int direction = 0;

    public StuButton2(Context context) {
        this(context, null);
    }

    public StuButton2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StuButton2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StuButton2,
                0, 0);

        init(context);
        setLabel(typedArray.getString(R.styleable.StuButton2_stu_label));
        setStuBackground(typedArray.getResourceId(R.styleable.StuButton2_stu_background, R.drawable.stu_default_bg));
        setThumb(typedArray.getResourceId(R.styleable.StuButton2_stu_thumbDrawable, R.drawable.stu_circle_material));

        typedArray.recycle();
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stu_main_layout, this, true);

        stuTxtLabel = (TextView) findViewById(R.id.stu_text_label);
        stuImgThumb = (ImageView) findViewById(R.id.stu_img_thumb);
        stuBackground = (ImageView) findViewById(R.id.stu_background);

        thumbWidth = dpToPx(DEFAULT_SIZE);

        stuImgThumb.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                stuImgThumb.getViewTreeObserver().removeOnPreDrawListener(this);
                thumbWidth = stuImgThumb.getWidth();
                return false;
            }
        });

    }

    public void setOnUnlockListener(OnUnlockListener listener) {
        this.listener = listener;
    }


    public void resetLeft() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stuImgThumb.getLayoutParams();
        Log.d(TAG,"reset:"+params.leftMargin);
        ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (stuImgThumb != null) {
                    int progress = (Integer) valueAnimator.getAnimatedValue();
                    params.leftMargin = progress;
                    stuImgThumb.requestLayout();

                    if(listener!=null && unLock && progress==0){
                        listener.onlock();
                        unLock = false;
                        Log.d(TAG,"resetLeft: "+(Integer) valueAnimator.getAnimatedValue()+" "+progress+"  "+unLock);
                    }

                }
            }
        });
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
        stuTxtLabel.setAlpha(1f);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        Log.d(TAG+"_onTouchEvent__", ""+event.getAction());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG+"_MotionEvent.ACTION_DOWN", ""+event.getAction());
            if (event.getX() > sliderPosition && event.getX() < (sliderPosition + thumbWidth)) {
                sliding = true;
                initialSlidingX = event.getX();
                initialSliderPosition = sliderPosition;
            }else if (event.getX() >= sliderPosition && event.getX() > (sliderPosition - thumbWidth) && unLock) {
                sliding = true;
                initialSlidingX = event.getX();
                initialSliderPosition = sliderPosition;
            }else {
                sliding = false;
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE && sliding) {
            sliderPosition = (int) (initialSliderPosition + (event.getX() - initialSlidingX));
            Log.d(TAG,"sliderPosition_"+sliderPosition+"  initialSliderPosition_"+initialSliderPosition+" event.getX()_"+event.getX()+" initialSlidingX_"+initialSlidingX);

            if (sliderPosition <= 0) {
                sliderPosition = 0;
            }
            if (sliderPosition >= (getMeasuredWidth() - thumbWidth)) {
                sliderPosition = getMeasuredWidth() - thumbWidth;
            } else {
                int max = getMeasuredWidth() - thumbWidth;
                int progress = (int) (sliderPosition * 100 / (max * 1.0f));
                stuTxtLabel.setAlpha(1f - progress * ALPHA_MODIFIER);
            }
            setMarginLeft(sliderPosition);

        }else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (sliderPosition >= (getMeasuredWidth() - thumbWidth)) {
                Log.d(TAG, "_MotionEvent.ACTION_UP "+event.getAction()+" "+sliderPosition);
                if (listener != null) {
                    listener.onUnlock();
                    unLock = true;
                    sliding = false;
                    Log.d(TAG,"SliderPosition: "+sliderPosition);
                }
            }else if (sliderPosition <=0) {
                Log.d(TAG, "_MotionEvent.ACTION_UP "+event.getAction()+" "+sliderPosition);
                if (listener != null ) {
                    listener.onlock();
                    unLock = false;
                    sliding = false;
                    Log.d(TAG,"SliderPosition: "+sliderPosition);
                }
            } else {
                if(!unLock) {
                    resetLeft();
                    sliding = false;
                    sliderPosition = 0;
                }
                if(unLock) {
                    resetRight();
                    sliding = false;
                    sliderPosition = getMeasuredWidth()-thumbWidth;
                }
            }
        }

        return true;
    }

    public void resetRight() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stuImgThumb.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(((getWidth()-thumbWidth)-params.leftMargin), 0);
        Log.d(TAG+"_resetLeft ",params.leftMargin+"::"+((getMeasuredWidth()-params.leftMargin)));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d(TAG,"_onAnimationUpdate: "+(Integer) valueAnimator.getAnimatedValue());
                if (stuImgThumb != null) {
                    int margin = (getWidth()-thumbWidth)-((Integer) valueAnimator.getAnimatedValue());
                    Log.d(TAG,"_onAnimationUpdate: "+margin+"  "+(Integer) valueAnimator.getAnimatedValue());
                    params.leftMargin = margin;
                    stuImgThumb.requestLayout();
                    //direction = 0;
                }
            }
        });
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
        stuTxtLabel.setAlpha(1f);
    }


    public void setViewReverse(){
        setLayoutParamMargin(getMeasuredWidth() - thumbWidth);
        sliderPosition = getMeasuredWidth()-thumbWidth;
        unLock = true;
        sliding = false;
        Log.d(TAG,"SliderPosition: "+sliderPosition);

    }

    public void setViewForward(){
        setLayoutParamMargin(0);
        unLock = false;
        sliding = false;
        sliderPosition = 0;
    }

    private void setMarginLeft(int margin) {
        if (stuImgThumb == null) return;
       setLayoutParamMargin(margin);
    }

    private void setLayoutParamMargin(int margin) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stuImgThumb.getLayoutParams();
        params.setMargins(margin, params.topMargin, params.rightMargin, params.bottomMargin);
        stuImgThumb.setLayoutParams(params);
        Log.d(TAG,"margin:: "+margin+"  "+params.leftMargin+" "+params.topMargin+" "+params.rightMargin+" "+params.bottomMargin);
    }
    private void setLayoutParamMarginRight(int margin) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stuImgThumb.getLayoutParams();
        params.setMargins(300, params.topMargin, params.rightMargin, params.bottomMargin);
        stuImgThumb.setLayoutParams(params);
        Log.d(TAG,"marginRight:: "+margin+" "+(getMeasuredWidth()-margin)+" ");
    }

    @Override
    protected void onDetachedFromWindow() {
        cleanUp();
        super.onDetachedFromWindow();
    }

    private void cleanUp() {
        stuTxtLabel = null;
        stuImgThumb = null;
        stuBackground = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateGestureExclusion();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateGestureExclusion();
    }

    /* --------------------------------------------------- */
    /* > Handle Gesture Navigation */
    /* --------------------------------------------------- */

    private List<Rect> gestureExclusionRects = new ArrayList<>();
    private Rect excludeRect = new Rect();

    private void updateGestureExclusion() {
        // Skip this call if we're not running on Android 10+
        if (Build.VERSION.SDK_INT < 29) {
            return;
        }

        // First, lets clear out any existing rectangles
        gestureExclusionRects.clear();

        // Now lets work out which areas should be excluded. For a SeekBar this will
        // be the bounds of the thumb drawable.

        getDrawingRect(excludeRect);
        gestureExclusionRects.add(excludeRect);

        // If we had other elements in this view near the edges, we could exclude them
        // here too, by adding their bounds to the list

        // Finally pass our updated list of rectangles to the system
        setSystemGestureExclusionRects(gestureExclusionRects);
    }

    /* --------------------------------------------------- */
    /* > Public Methods */
    /* --------------------------------------------------- */

    public void setLabel(@StringRes int label) {
        setLabel(getContext().getString(label));
    }

    public void setLabel(String label) {
        if (stuTxtLabel != null) {
            stuTxtLabel.setText(label);
        }
    }

    public void setStuBackground(@DrawableRes int resId) {
        if (stuBackground != null) {
            stuBackground.setImageResource(resId);
        }
    }

    public void setThumb(@DrawableRes int resId) {
        if (stuImgThumb != null) {
            stuImgThumb.setImageResource(resId);
        }
    }

    /* --------------------------------------------------- */
    /* > Helper */
    /* --------------------------------------------------- */

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /* --------------------------------------------------- */
    /* > Interfaces */
    /* --------------------------------------------------- */

    public interface OnUnlockListener {
        void onUnlock();
        void onlock();
    }



   /* public void moveToRightExpand() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 130f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                buttonContainer.setTranslationX(progress);
            }
        });

        valueAnimator.start();
        isPressed = true;
    }*/


}
