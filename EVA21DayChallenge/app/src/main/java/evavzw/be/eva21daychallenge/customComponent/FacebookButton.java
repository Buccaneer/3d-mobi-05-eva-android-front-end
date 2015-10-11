package evavzw.be.eva21daychallenge.customComponent;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import evavzw.be.eva21daychallenge.R;

/**
 * A base class for a facebook button.
 */
public abstract class FacebookButton extends Button {
    private boolean overrideCompoundPadding;
    private int overrideCompoundPaddingLeft;
    private int overrideCompoundPaddingRight;
    private Fragment parentFragment;

    protected FacebookButton(
            final Context context,
            final AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, 0);
        defStyleRes = (defStyleRes == 0 ? this.getDefaultStyleResource() : defStyleRes);
        defStyleRes = (defStyleRes == 0 ? R.style.com_facebook_button : defStyleRes);
        configureButton(context, attrs, defStyleAttr, defStyleRes);
    }

    protected abstract int getDefaultRequestCode();

    /**
     * Sets the fragment that contains this control. This allows the button to be embedded inside a
     * Fragment, and will allow the fragment to receive the
     * {@link Fragment#onActivityResult(int, int, android.content.Intent) onActivityResult}
     * call rather than the Activity.
     *
     * @param fragment the fragment that contains this control
     */
    public void setFragment(final Fragment fragment) {
        parentFragment = fragment;
    }

    /**
     * Gets the fragment that contains this control.
     * @return The fragment that contains this control.
     */
    public Fragment getFragment() {
        return parentFragment;
    }

    /**
     * Returns the request code used for this Button.
     *
     * @return the request code.
     */
    public int getRequestCode() {
        return getDefaultRequestCode();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean centered = (this.getGravity() & Gravity.CENTER_HORIZONTAL) != 0;
        if (centered) {
            // if the text is centered, we need to adjust the frame for the titleLabel based on the
            // size of the text in order to keep the text centered in the button without adding
            // extra blank space to the right when unnecessary
            // 1. the text fits centered within the button without colliding with the image
            //    (imagePaddingWidth)
            // 2. the text would run into the image, so adjust the insets to effectively left align
            //    it (textPaddingWidth)
            final int compoundPaddingLeft = getCompoundPaddingLeft();
            final int compoundPaddingRight = getCompoundPaddingRight();
            final int compoundDrawablePadding = getCompoundDrawablePadding();
            final int textX = compoundPaddingLeft + compoundDrawablePadding;
            final int textContentWidth = getWidth() - textX - compoundPaddingRight;
            final int textWidth = measureTextWidth(getText().toString());
            final int textPaddingWidth = (textContentWidth - textWidth) / 2;
            final int imagePaddingWidth = (compoundPaddingLeft - getPaddingLeft()) / 2;
            final int inset = Math.min(textPaddingWidth, imagePaddingWidth);
            this.overrideCompoundPaddingLeft = compoundPaddingLeft - inset;
            this.overrideCompoundPaddingRight = compoundPaddingRight + inset;
            this.overrideCompoundPadding = true;
        }
        super.onDraw(canvas);
        this.overrideCompoundPadding = false;
    }

    @Override
    public int getCompoundPaddingLeft() {
        return (this.overrideCompoundPadding ?
                this.overrideCompoundPaddingLeft :
                super.getCompoundPaddingLeft());
    }

    @Override
    public int getCompoundPaddingRight() {
        return (this.overrideCompoundPadding ?
                this.overrideCompoundPaddingRight :
                super.getCompoundPaddingRight());
    }
/*
    protected Activity getActivity() {
        Context context = getContext();
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }
        throw new FacebookException("Unable to get Activity.");
    }
*/
    protected int getDefaultStyleResource() {
        return 0;
    }

    protected int measureTextWidth(final String text) {
        return (int)Math.ceil(getPaint().measureText(text));
    }

    protected void configureButton(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        parseBackgroundAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseCompoundDrawableAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseContentAttributes(context, attrs, defStyleAttr, defStyleRes);
        parseTextAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void parseBackgroundAttributes(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        // TODO, figure out why com_facebook_button_like_background.xml doesn't work in designers
        if (isInEditMode()) {
            return;
        }

        final int attrsResources[] = {
                android.R.attr.background,
        };
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                attrsResources,
                defStyleAttr,
                defStyleRes);
        try {
            if (a.hasValue(0)) {
                int backgroundResource = a.getResourceId(0, 0);
                if (backgroundResource != 0) {
                    setBackgroundResource(backgroundResource);
                } else {
                    setBackgroundColor(a.getColor(0, 0));
                }
            } else {
                // fallback, if no background specified, fill with Facebook blue
                setBackgroundColor(a.getColor(0, R.color.com_facebook_blue));
            }
        } finally {
            a.recycle();
        }
    }

    private void parseCompoundDrawableAttributes(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        final int attrsResources[] = {
                android.R.attr.drawableLeft,
                android.R.attr.drawableTop,
                android.R.attr.drawableRight,
                android.R.attr.drawableBottom,
                android.R.attr.drawablePadding,
        };
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                attrsResources,
                defStyleAttr,
                defStyleRes);
        try {
            setCompoundDrawablesWithIntrinsicBounds(
                    a.getResourceId(0, 0),
                    a.getResourceId(1, 0),
                    a.getResourceId(2, 0),
                    a.getResourceId(3, 0));
            setCompoundDrawablePadding(a.getDimensionPixelSize(4, 0));

        } finally {
            a.recycle();
        }
    }

    private void parseContentAttributes(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        final int attrsResources[] = {
                android.R.attr.paddingLeft,
                android.R.attr.paddingTop,
                android.R.attr.paddingRight,
                android.R.attr.paddingBottom,
        };
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                attrsResources,
                defStyleAttr,
                defStyleRes);
        try {
            setPadding(
                    a.getDimensionPixelSize(0, 0),
                    a.getDimensionPixelSize(1, 0),
                    a.getDimensionPixelSize(2, 0),
                    a.getDimensionPixelSize(3, 0));
        } finally {
            a.recycle();
        }
    }

    private void parseTextAttributes(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        final int colorResources[] = {
                android.R.attr.textColor,
        };
        final TypedArray colorAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                colorResources,
                defStyleAttr,
                defStyleRes);
        try {
            setTextColor(colorAttrs.getColor(0, Color.WHITE));
        } finally {
            colorAttrs.recycle();
        }
        final int gravityResources[] = {
                android.R.attr.gravity,
        };
        final TypedArray gravityAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                gravityResources,
                defStyleAttr,
                defStyleRes);
        try {
            setGravity(gravityAttrs.getInt(0, Gravity.CENTER));
        } finally {
            gravityAttrs.recycle();
        }
        final int attrsResources[] = {
                android.R.attr.textSize,
                android.R.attr.textStyle,
                android.R.attr.text,
        };
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                attrsResources,
                defStyleAttr,
                defStyleRes);
        try {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(0, 0));
            setTypeface(Typeface.defaultFromStyle(a.getInt(1, Typeface.BOLD)));
            setText(a.getString(2));
        } finally {
            a.recycle();
        }
    }
}
