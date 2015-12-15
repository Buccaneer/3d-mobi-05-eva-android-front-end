package be.evavzw.eva21daychallenge.customComponent;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

import be.evavzw.eva21daychallenge.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jan on 6/12/2015.
 */
public class SplashDialog extends Dialog {

    @Bind(R.id.dialogLayout)
    LinearLayout linearLayout;
    @Bind(R.id.logoView)
    ImageView imageView;

    public SplashDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_splash);

        ButterKnife.bind(this);

        int logo = getContext().getResources().getIdentifier("eva_logo", "drawable", getContext().getPackageName());
        Glide.with(getContext())
                .load(logo)
                .into(imageView);
    }
}
