package be.evavzw.eva21daychallenge.customComponent;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import be.evavzw.eva21daychallenge.R;


/**
 * A Log In/Log Out button that maintains login state and logs in/out for the app.
 * <p/>
 * This control requires the app ID to be specified in the AndroidManifest.xml.
 */
public class LoginButton extends FacebookButton {

    private String loginText;

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
        final Resources resources = getResources();

        super.configureButton(context, attrs, defStyleAttr, defStyleRes);

        parseLoginButtonAttributes(context, attrs, defStyleAttr, defStyleRes);


        // cannot use a drawable in edit mode, so setting the background color instead
        // of a background resource.
        setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
        // hardcoding in edit mode as getResources().getString() doesn't seem to work in
        // IntelliJ
        loginText = resources.getString(
                R.string.com_facebook_loginview_log_in_button_long);


        setButtonText();
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
            loginText = a.getString(R.styleable.com_facebook_login_view_com_facebook_login_text);
        } finally {
            a.recycle();
        }
    }

    private void setButtonText() {
        final Resources resources = getResources();
        if (loginText != null) {
            setText(loginText);
        } else {
            String text = resources.getString(
                    R.string.com_facebook_loginview_log_in_button_long);

            setText(text);
        }
    }
}