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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import evavzw.be.eva21daychallenge.R;

/**
 * Created by Jan on 17/10/2015.
 */
public abstract class RESTfulActivity extends AppCompatActivity {

    private int mContentResId;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentResId);
	}

    protected void setContentResId(int id) {
        mContentResId = id;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.menu_progress);
        ProgressBar progressBar = (ProgressBar) MenuItemCompat.getActionView(menuItem);
        return super.onPrepareOptionsMenu(menu);
    }

    public void toggleProgressBar(boolean toggle){
        menuItem.setVisible(toggle);
    }
}