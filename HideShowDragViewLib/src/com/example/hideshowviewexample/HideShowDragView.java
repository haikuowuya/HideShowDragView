
package com.example.hideshowviewexample;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Superclass that all views that can be hidden / shown or dragged inherit from.
 * 
 * IMPORTANT: Parent layout (layout that contains the view) needs to be a
 * RelativeLayout, FrameLayout or LinearLayout
 * 
 * @author Philipp Jahoda
 */
public abstract class HideShowDragView extends FrameLayout {

    private static final String LOG_TAG = "HideShowView";

    /** interpolator for the showing animation */
    private TimeInterpolator mShowInterpolator;

    /** interpolator for the hiding animation */
    private TimeInterpolator mHideInterpolator;

    /** boolean flag indicating the visible state */ 
    private boolean mIsShown = true;

    /** indicator if the view is currently being animated */
    private boolean mIsAnimating = false;

    /** the duration of the hide and show process in millisecionds */
    private int mHideShowDuration = 500;

    /** listener for the hiding and showing process */
    private HideShowDragListener mListener;

    /**
     * if set to true, the view will acutally be made invisible once the hiding
     * animation has completed
     */
    private boolean mMakeInvisible = true;

    /**
     * flag that indicates of the onLayout() method has been called to get the
     * current position of the view
     */
    private boolean mNotMeasured = true;

    /** indicates if the view is draggable or not */
    private boolean mDragEnabled = true;

    /** x-coordinate destination of the show animation */
    private float mShowX = 0f;

    /** y-coordinate destination of the show animation */
    private float mShowY = 0f;

    /** x-coordinate destination of the hide animation */
    private float mHideX = 0f;

    /** y-coordinate destination of the hide animation */
    private float mHideY = 0f;

    /** the height of the statusbar in pixels */
    private int mStatusBarHeight = 0;

    /** default constructor */
    public HideShowDragView(Context context) {
        super(context);
        init();
    }

    /** XML constructor */
    public HideShowDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /** even more awesome constructor */
    public HideShowDragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * do all initialization stuff
     */
    private void init() {

        mShowInterpolator = new DecelerateInterpolator(0.5f);
        mHideInterpolator = new DecelerateInterpolator(0.5f);

        mStatusBarHeight = getStatusBarHeight();

        Log.i(LOG_TAG, "HideShowView init(), status-bar height: " + mStatusBarHeight);
    }

    /**
     * set an interpolator for the showing animation, default: decelerate 0.5f
     * 
     * @param i
     */
    public void setShowInterpolator(TimeInterpolator i) {
        this.mShowInterpolator = i;
    }

    /**
     * set an interpolator for the hiding animation, default: decelerate 0.5f
     * 
     * @param i
     */
    public void setHideInterpolator(TimeInterpolator i) {
        this.mHideInterpolator = i;
    }

    /**
     * add a hideshowlistener to the view
     * 
     * @param l
     */
    public void setHideShowListener(HideShowDragListener l) {
        this.mListener = l;
    }

    /**
     * set the positions between the view should animate when switching show and
     * hide state the starting position of the view is not ignored tho
     * 
     * @param xShow
     * @param yShow
     * @param xHide
     * @param yHide
     */
    public void setPositions(float xShow, float yShow, float xHide, float yHide) {

        this.mShowX = xShow;
        this.mShowY = yShow;
        this.mHideX = xHide;
        this.mHideY = yHide;

        mNotMeasured = false;
    }

    /**
     * set the position to which the view should animate from its current
     * position the starting position is the current position of the view, the
     * other position can be set via this method
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {

        int[] loc = new int[] {
                0, 0
        };

        getLocationOnScreen(loc);

        // take statusbar and view height into consideration
        loc[1] = loc[1] - getHeight() / 2 - mStatusBarHeight;

        if (isShown()) {

            mHideX = x;
            mHideY = y;

            mShowX = loc[0];
            mShowY = loc[1];

        } else {
            mShowX = x;
            mShowY = y;

            mHideX = loc[0];
            mHideY = loc[1];
        }

        Log.i(LOG_TAG, "set position: show: " + mShowX + " / " + mShowY + ", hide: " + mHideX
                + " / " + mHideY + ", shown: " + mIsShown);
    }

    /**
     * hide the view if its currently shown, show it if its currently hidden
     */
    public void toggle() {

        if (mIsShown)
            hide();
        else
            show();
    }

    /**
     * return true if the view is currently in show state, false if not
     * 
     * @return
     */
    public boolean isShown() {
        return mIsShown;
    }

    /**
     * hand over true to set the views state to shown, false to set it hidden
     * 
     * @param shown
     */
    public void setIsShown(boolean shown) {
        this.mIsShown = shown;
    }

    /**
     * returns true if the view is currently being animated, false if not
     * 
     * @return
     */
    public boolean isAnimating() {
        return mIsAnimating;
    }

    /**
     * returns the duration the view needs for hiding / showing in milliseconds
     * 
     * @return
     */
    public int getHideShowDuration() {
        return mHideShowDuration;
    }

    /**
     * sets the duration the view needs for hiding / showing, default 500ms
     * 
     * @param duration in milliseconds
     */
    public void setHideShowDuration(int duration) {
        this.mHideShowDuration = duration;
    }

    /**
     * set this to true if you want the view to be set invisible once the hide
     * animation is finished, default: true
     * 
     * @param enabled
     */
    public void makeInvisibleOnHide(boolean enabled) {
        this.mMakeInvisible = enabled;
    }

    /**
     * set this to true if you want the view to be draggable on the screen,
     * default: true
     * 
     * @param enabled
     */
    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    /**
     * returns true if drag is enabled for the view, false if not, default: true
     * 
     * @return
     */
    public boolean isDraggable() {
        return mDragEnabled;
    }

    /**
     * show the view if its currently not visible it is advised to set the
     * isVisible variable to true within this method
     */
    public void show() {

        if (!mIsShown && !mIsAnimating) {

            invalidate();

            setVisibility(View.VISIBLE);
            mIsAnimating = true;

            Log.i(LOG_TAG, "Showing, to: " + mShowX + " / " + mShowY);

            if (Utils.supportsAPILevel(16)) {

                animate().y(mShowY).setDuration(mHideShowDuration)
                        .setInterpolator(mShowInterpolator).withLayer();
                animate().x(mShowX).setDuration(mHideShowDuration)
                        .setInterpolator(mShowInterpolator).withLayer();
            }
            else if (Utils.supportsAPILevel(12)) {

                animate().y(mShowY).setDuration(mHideShowDuration)
                        .setInterpolator(mShowInterpolator);
                animate().x(mShowX).setDuration(mHideShowDuration)
                        .setInterpolator(mShowInterpolator);
            }

            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override
                public void run() {

                    mIsShown = true;
                    mIsAnimating = false;
                    if (mListener != null)
                        mListener.onShow(HideShowDragView.this, mShowX, mShowY);
                }
            }, mHideShowDuration);
        }
    }

    /**
     * hide the view if its currently visible it is advised to set the isVisible
     * variable to false within this method
     */
    public void hide() {

        if (mIsShown && !mIsAnimating) {

            invalidate();

            mIsAnimating = true;
            Log.i(LOG_TAG, "Hiding, to: " + mHideX + " / " + mHideY);

            if (Utils.supportsAPILevel(16)) {

                animate().y(mHideY).setDuration(mHideShowDuration)
                        .setInterpolator(mHideInterpolator).withLayer();
                animate().x(mHideX).setDuration(mHideShowDuration)
                        .setInterpolator(mHideInterpolator).withLayer();
            }
            else if (Utils.supportsAPILevel(12)) {

                animate().y(mHideY).setDuration(mHideShowDuration)
                        .setInterpolator(mHideInterpolator);
                animate().x(mHideX).setDuration(mHideShowDuration)
                        .setInterpolator(mHideInterpolator);
            }

            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (mMakeInvisible)
                        setVisibility(View.INVISIBLE);
                    mIsShown = false;
                    mIsAnimating = false;

                    if (mListener != null)
                        mListener.onHide(HideShowDragView.this, mHideX, mHideY);
                }
            }, mHideShowDuration);
        }
    }

    /**
     * calculates the height of the statusbar
     * 
     * @return
     */
    private int getStatusBarHeight() {

        if (isFullScreen())
            return 0;

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * determine if the app is running in fullscreen mode
     * 
     * @return
     */
    public boolean isFullScreen() {

        int flg = ((Activity) getContext()).getWindow().getAttributes().flags;
        boolean flag = false;

        if ((flg & 1024) == 1024) {
            flag = true;
        }
        return flag;
    }

    /** distance between the dragging finger and the left edge of the view */
    private float mOffsetX = 0;

    /** distance between the dragging finger and the top edge of the view */
    private float mOffsetY = 0;
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!mDragEnabled)
            return false;

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                if (getLayoutParams() instanceof FrameLayout.LayoutParams) {

                    FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) getLayoutParams();

                    mOffsetX = ev.getRawX() - params.leftMargin;
                    mOffsetY = ev.getRawY() - params.topMargin;
                    
                    if(mListener != null) mListener.onDragStart(this, params.leftMargin, params.topMargin);

                } else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {

                    RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();

                    mOffsetX = ev.getRawX() - params.leftMargin;
                    mOffsetY = ev.getRawY() - params.topMargin;
                    
                    if(mListener != null) mListener.onDragStart(this, params.leftMargin, params.topMargin);
                    
                } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    
                    LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) getLayoutParams();

                    mOffsetX = ev.getRawX() - params.leftMargin;
                    mOffsetY = ev.getRawY() - params.topMargin;
                    
                    if(mListener != null) mListener.onDragStart(this, params.leftMargin, params.topMargin);
                }              

                break;
            }

            case MotionEvent.ACTION_MOVE: {

                if (getLayoutParams() instanceof FrameLayout.LayoutParams) {

                    FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) getLayoutParams();

                    params.setMargins((int) (ev.getRawX() - mOffsetX),
                            (int) (ev.getRawY() - mOffsetY), 0, 0);
                    setLayoutParams(params);

                } else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {

                    RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();

                    params.setMargins((int) (ev.getRawX() - mOffsetX),
                            (int) (ev.getRawY() - mOffsetY), 0, 0);
                    setLayoutParams(params);
                    
                } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    
                    LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) getLayoutParams();

                    params.setMargins((int) (ev.getRawX() - mOffsetX),
                            (int) (ev.getRawY() - mOffsetY), 0, 0);
                    setLayoutParams(params);
                }

                break;
            }

            case MotionEvent.ACTION_UP: {

                int[] loc = new int[] {
                        0, 0
                };

                getLocationOnScreen(loc);

                // take statusbar and view height into consideration
                loc[1] = loc[1] - getHeight() / 2 - mStatusBarHeight;

                if (isShown()) {
                    mShowX = loc[0];
                    mShowY = loc[1];
                } else {
                    mHideX = loc[0];
                    mHideY = loc[1];
                }
                
                if(mListener != null) mListener.onDragFinished(this, loc[0], loc[1]);

                break;
            }
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mNotMeasured) {

            mNotMeasured = false;
            Log.i(LOG_TAG, "onLayout: left: " + left + ", top: " + top + ", right: " + right
                    + ", bottom: " + bottom);

            if (isShown()) {
                mShowX = left;
                mShowY = top;
            } else {
                mHideX = left;
                mHideY = top;
            }
        }
    }

    /**
     * Utils class for measuring API levels
     * 
     * @author Philipp Jahoda
     */
    private static class Utils {

        /**
         * returns the devices API level as an integer, e.g. 16 for Android 4.1
         * 
         * @return
         */
        public static int getAPILevel() {
            return Build.VERSION.SDK_INT;
        }

        /**
         * checks if the api level of the device is higher than the level in the
         * parameter, if so return true
         * 
         * @param level
         * @return
         */
        public static boolean supportsAPILevel(int level) {

            if (getAPILevel() >= level)
                return true;
            else
                return false;
        }
    }

    /**
     * listener that can be notified when the hiding / showing process has
     * finished
     * 
     * @author Philipp Jahoda
     */
    public interface HideShowDragListener {

        /**
         * called when the hiding animation of the view has finished
         * 
         * @param v
         * @param curX
         * @param curY
         */
        public void onHide(HideShowDragView v, float curX, float curY);

        /**
         * called when the showing animation of the view has finished
         * 
         * @param v
         * @param curX
         * @param curY
         */
        public void onShow(HideShowDragView v, float curX, float curY);
        
        /**
         * called when the dragging of the view is started
         * @param v
         * @param startX
         * @param startY
         */
        public void onDragStart(HideShowDragView v, float startX, float startY);
        
        /**
         * called when the dragging of the view is stopped (when the view is dropped)
         * @param v
         * @param stopX
         * @param stopY
         */
        public void onDragFinished(HideShowDragView v, float stopX, float stopY);
    }
}
