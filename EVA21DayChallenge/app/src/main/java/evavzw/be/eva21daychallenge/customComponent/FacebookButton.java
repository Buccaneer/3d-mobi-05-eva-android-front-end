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

    protected int getDefaultStyleResource() {
        return 0;
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
