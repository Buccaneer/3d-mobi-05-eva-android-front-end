package evavzw.be.eva21daychallenge.activity.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import evavzw.be.eva21daychallenge.R;

/**
 * Created by Jan on 17/10/2015.
 */
public abstract class RESTfulActivity extends AppCompatActivity {
    private int mContentResId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentResId);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 34));
        progressBar.setProgress(65);
        final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(progressBar);

        ViewTreeObserver observer = progressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View contentView = decorView.findViewById(android.R.id.content);
                progressBar.setY(contentView.getY() +55);

                ViewTreeObserver observer = progressBar.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(this);
            }
        });
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    protected void setContentResId(int id) {
        mContentResId = id;
    }

    public void toggleProgressBar(boolean toggle){
        progressBar.setVisibility(toggle ? View.VISIBLE : View.INVISIBLE);
    }
}