package com.xp.wavesidebar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * class for the sidebar
 */
public class SideBar extends View {

    private static final String TAG = "WaveSideBar";

    // Bezier curve
    private static final double ANGLE = Math.PI * 45 / 180;
    private static final double ANGLE_R = Math.PI * 90 / 180;
    private OnTouchLetterChangeListener mListener;

    // list letters
    private List<String> mLetters;

    // choose the current position
    private int mChoosePosition = -1;

    private int mOldPosition;

    private int mNewPosition;

    private Paint mLettersPaint = new Paint();

    private Paint mTextPaint = new Paint();
    private Paint mWavePaint = new Paint();

    private int mTextSize;
    private int mHintTextSize;
    private int mTextColor;
    private int mWaveColor;
    private int mTextColorChoose;
    private int mWidth;
    private int mHeight;
    private int mItemHeight;
    private int mPadding;

    // wave path
    private Path mWavePath = new Path();

    // circle path
    private Path mCirclePath = new Path();

    // center point
    private int mCenterY;


    private int mRadius;


    private int mCircleRadius;

    private ValueAnimator mRatioAnimator;

    // Bezier curve ratio
    private float mRatio;

    // set x,y
    private float mPointX, mPointY;

    // circle center x
    private float mCircleCenterX;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.waveSideBarLetters));

        mTextColor = Color.parseColor("#969696");
        mWaveColor = Color.parseColor("#bef9b81b");
        mTextColorChoose = ContextCompat.getColor(context, android.R.color.white);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize);
        mHintTextSize = context.getResources().getDimensionPixelSize(R.dimen.hintTextSize);
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.padding);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.waveSideBar);
            mTextColor = a.getColor(R.styleable.waveSideBar_textColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.waveSideBar_chooseTextColor, mTextColorChoose);
            mTextSize = a.getDimensionPixelSize(R.styleable.waveSideBar_textSize, mTextSize);
            mHintTextSize = a.getDimensionPixelSize(R.styleable.waveSideBar_hintTextSize, mHintTextSize);
            mWaveColor = a.getColor(R.styleable.waveSideBar_backgroundColor, mWaveColor);
            mRadius = a.getDimensionPixelSize(R.styleable.waveSideBar_radius, context.getResources().getDimensionPixelSize(R.dimen.radius));
            mCircleRadius = a.getDimensionPixelSize(R.styleable.waveSideBar_circleRadius, context.getResources().getDimensionPixelSize(R.dimen.circleRadius));
            a.recycle();
        }

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColorChoose);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mHintTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final float x = event.getX();
        mOldPosition = mChoosePosition;
        mNewPosition = (int) (y / mHeight * mLetters.size());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //set the range
                if (x < mWidth - 1.5 * mRadius) {
                    return false;
                }
                mCenterY = (int) y;
                startAnimator(1.0f);

                break;
            case MotionEvent.ACTION_MOVE:

                mCenterY = (int) y;
                if (mOldPosition != mNewPosition) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.size()) {
                        mChoosePosition = mNewPosition;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewPosition));
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                startAnimator(0f);
                mChoosePosition = -1;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mItemHeight = (mHeight - mPadding) / mLetters.size();
        mPointX = mWidth - 1.6f * mTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLetters(canvas);
        drawWavePath(canvas);
        drawCirclePath(canvas);
        drawChooseText(canvas);

    }

    /**
     * draw the letters on the side
     * @param canvas that draws on it.
     */
    private void drawLetters(Canvas canvas) {

        RectF rectF = new RectF();
        rectF.left = mPointX - mTextSize;
        rectF.right = mPointX + mTextSize;
        rectF.top = mTextSize / 2;
        rectF.bottom = mHeight - mTextSize / 2;

        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.FILL);
        mLettersPaint.setColor(Color.parseColor("#F9F9F9"));
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mTextSize, mTextSize, mLettersPaint);

        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.STROKE);
        mLettersPaint.setColor(mTextColor);
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mTextSize, mTextSize, mLettersPaint);

        for (int i = 0; i < mLetters.size(); i++) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColor);
            mLettersPaint.setAntiAlias(true);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mLettersPaint.getFontMetrics();
            float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);

            float pointY = mItemHeight * i + baseline / 2 + mPadding;

            if (i == mChoosePosition) {
                mPointY = pointY;
            } else {
                canvas.drawText(mLetters.get(i), mPointX, pointY, mLettersPaint);
            }
        }

    }

    /**
     * draw the chosen texts
     * @param canvas that draws on it.
     */
    private void drawChooseText(Canvas canvas) {
        if (mChoosePosition != -1) {
            // paint the chosen text
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColorChoose);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mLetters.get(mChoosePosition), mPointX, mPointY, mLettersPaint);


            if (mRatio >= 0.9f) {
                String target = mLetters.get(mChoosePosition);
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
                float x = mCircleCenterX;
                float y = mCenterY + baseline / 2;
                canvas.drawText(target, x, y, mTextPaint);
            }
        }
    }

    /**
     * draw the waves
     * @param canvas that draws on it
     */
    private void drawWavePath(Canvas canvas) {
        mWavePath.reset();
        // move to the starting point
        mWavePath.moveTo(mWidth, mCenterY - 3 * mRadius);
        //calculate the top y axis location
        int controlTopY = mCenterY - 2 * mRadius;

        //calculate the end point on the top
        int endTopX = (int) (mWidth - mRadius * Math.cos(ANGLE) * mRatio);
        int endTopY = (int) (controlTopY + mRadius * Math.sin(ANGLE));
        mWavePath.quadTo(mWidth, controlTopY, endTopX, endTopY);

        //calculate the center (x,y)
        int controlCenterX = (int) (mWidth - 1.8f * mRadius * Math.sin(ANGLE_R) * mRatio);
        int controlCenterY = mCenterY;
        //calculate the bottom (x,y)
        int controlBottomY = mCenterY + 2 * mRadius;
        int endBottomX = endTopX;
        int endBottomY = (int) (controlBottomY - mRadius * Math.cos(ANGLE));
        mWavePath.quadTo(controlCenterX, controlCenterY, endBottomX, endBottomY);

        mWavePath.quadTo(mWidth, controlBottomY, mWidth, controlBottomY + mRadius);

        mWavePath.close();
        canvas.drawPath(mWavePath, mWavePaint);
    }

    /**
     * draw the circle path
     * @param canvas that draws on it
     */
    private void drawCirclePath(Canvas canvas) {
        //x axis path
        mCircleCenterX = (mWidth + mCircleRadius) - (2.0f * mRadius + 2.0f * mCircleRadius) * mRatio;

        mCirclePath.reset();
        mCirclePath.addCircle(mCircleCenterX, mCenterY, mCircleRadius, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mCirclePath.op(mWavePath, Path.Op.DIFFERENCE);
        }

        mCirclePath.close();
        canvas.drawPath(mCirclePath, mWavePaint);

    }


    private void startAnimator(float value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {

                mRatio = (float) value.getAnimatedValue();
                if (mRatio == 1f && mOldPosition != mNewPosition) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.size()) {
                        mChoosePosition = mNewPosition;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewPosition));
                        }
                    }
                }
                invalidate();
            }
        });
        mRatioAnimator.start();
    }


    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener listener) {
        this.mListener = listener;
    }

    public List<String> getLetters() {
        return mLetters;
    }

    public void setLetters(List<String> letters) {
        this.mLetters = letters;
        invalidate();
    }

    public interface OnTouchLetterChangeListener {
        void onLetterChange(String letter);
    }
}