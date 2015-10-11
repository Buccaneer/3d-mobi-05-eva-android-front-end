package evavzw.be.eva21daychallenge.customComponent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import evavzw.be.eva21daychallenge.R;


/**
 * A Log In/Log Out button that maintains login state and logs in/out for the app.
 * <p/>
 * This control requires the app ID to be specified in the AndroidManifest.xml.
 */
public class LoginButton extends FacebookButton {

    // ***
    // Keep all the enum values in sync with attrs.xml
    // ***

    /**
     * The display modes for the login button tool tip.
     */
    public enum ToolTipMode {
        /**
         * Default display mode. A server query will determine if the tool tip should be displayed
         * and, if so, what the string shown to the user should be.
         */
        AUTOMATIC("automatic", 0),

        /**
         * Display the tool tip with a local string--regardless of what the server returns
         */
        DISPLAY_ALWAYS("display_always", 1),

        /**
         * Never display the tool tip--regardless of what the server says
         */
        NEVER_DISPLAY("never_display", 2);

        public static ToolTipMode DEFAULT = AUTOMATIC;

        public static ToolTipMode fromInt(int enumValue) {
            for (ToolTipMode mode : values()) {
                if (mode.getValue() == enumValue) {
                    return mode;
                }
            }

            return null;
        }

        private String stringValue;
        private int intValue;

        ToolTipMode(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int getValue() {
            return intValue;
        }
    }

    private static final String TAG = LoginButton.class.getName();
    private boolean confirmLogout;
    private String loginText;
    private String logoutText;


    public LoginButton(Context context) {
        super(
                context,
                null,
                0,
                0);
    }

    /**
     * Create the LoginButton by inflating from XML
     *
     * @see View#View(Context, AttributeSet)
     */
    public LoginButton(Context context, AttributeSet attrs) {
        super(
                context,
                attrs,
                0,
                0);
    }

    /**
     * Create the LoginButton by inflating from XML and applying a style.
     *
     * @see View#View(Context, AttributeSet, int)
     */
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(
                context,
                attrs,
                defStyle,
                0);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setButtonText();
    }


    @Override
    protected void configureButton(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        super.configureButton(context, attrs, defStyleAttr, defStyleRes);

        parseLoginButtonAttributes(context, attrs, defStyleAttr, defStyleRes);


        // cannot use a drawable in edit mode, so setting the background color instead
        // of a background resource.
        setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
        // hardcoding in edit mode as getResources().getString() doesn't seem to work in
        // IntelliJ
        loginText = "Log in with Facebook";


        setButtonText();
    }

    @Override
    protected int getDefaultRequestCode() {
        return 0;
    }

    @Override
    protected int getDefaultStyleResource() {
        return R.style.com_facebook_loginview_default_style;
    }

    private void parseLoginButtonAttributes(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.com_facebook_login_view,
                defStyleAttr,
                defStyleRes);
        try {
            confirmLogout = a.getBoolean(R.styleable.com_facebook_login_view_com_facebook_confirm_logout, true);
            loginText = a.getString(R.styleable.com_facebook_login_view_com_facebook_login_text);
            logoutText = a.getString(R.styleable.com_facebook_login_view_com_facebook_logout_text);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        int height = (getCompoundPaddingTop() +
                (int) Math.ceil(Math.abs(fontMetrics.top) + Math.abs(fontMetrics.bottom)) +
                getCompoundPaddingBottom());

        final Resources resources = getResources();
        String text = loginText;
        int logInWidth;
        int width;
        if (text == null) {
            text = resources.getString(R.string.com_facebook_loginview_log_in_button_long);
            logInWidth = measureButtonWidth(text);
            width = resolveSize(logInWidth, widthMeasureSpec);
            if (width < logInWidth) {
                text = resources.getString(R.string.com_facebook_loginview_log_in_button);
            }
        }
        logInWidth = measureButtonWidth(text);

        int logOutWidth = measureButtonWidth(text);

        width = resolveSize(Math.max(logInWidth, logOutWidth), widthMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureButtonWidth(final String text) {
        int textWidth = measureTextWidth(text);
        int width = (getCompoundPaddingLeft() +
                getCompoundDrawablePadding() +
                textWidth +
                getCompoundPaddingRight());
        return width;
    }

    private void setButtonText() {
        final Resources resources = getResources();
        if (loginText != null) {
            setText(loginText);
        } else {
            String text = resources.getString(
                    R.string.com_facebook_loginview_log_in_button_long);
            int width = getWidth();
            // if the width is 0, we are going to measure size, so use the long text
            if (width != 0) {
                // we have a specific width, check if the long text fits
                int measuredWidth = measureButtonWidth(text);
                if (measuredWidth > width) {
                    // it doesn't fit, use the shorter text
                    text = resources.getString(R.string.com_facebook_loginview_log_in_button);
                }
            }
            setText(text);
        }
    }
}