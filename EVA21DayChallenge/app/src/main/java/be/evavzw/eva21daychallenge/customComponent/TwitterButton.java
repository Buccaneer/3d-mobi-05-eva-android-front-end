package be.evavzw.eva21daychallenge.customComponent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.twitter.sdk.android.core.R.color;
import com.twitter.sdk.android.core.R.dimen;
import com.twitter.sdk.android.core.R.drawable;
import com.twitter.sdk.android.core.R.string;

public class TwitterButton extends AppCompatButton {

    public TwitterButton(Context context) {
        this(context, (AttributeSet)null);
    }

    public TwitterButton(Context context, AttributeSet attrs) {
        this(context, attrs, 16842824);
    }

    TwitterButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setupButton();
    }

    private void setupButton() {
        Resources res = this.getResources();
        super.setCompoundDrawablesWithIntrinsicBounds(res.getDrawable(drawable.tw__ic_logo_default), (Drawable)null, (Drawable)null, (Drawable)null);
        super.setCompoundDrawablePadding(res.getDimensionPixelSize(dimen.tw__login_btn_drawable_padding));
        super.setText(string.tw__login_btn_txt);
        super.setTextColor(res.getColor(color.tw__solid_white));
        super.setTypeface(Typeface.DEFAULT_BOLD);
        super.setPadding(res.getDimensionPixelSize(dimen.tw__login_btn_left_padding), 0, res.getDimensionPixelSize(dimen.tw__login_btn_right_padding), 0);
        super.setBackgroundResource(drawable.tw__login_btn);
    }
}
