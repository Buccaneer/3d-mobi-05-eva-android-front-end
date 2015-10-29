package evavzw.be.eva21daychallenge.activity.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import evavzw.be.eva21daychallenge.R;

/**
 * Created by Pieter-Jan on 25/10/2015.
 */
public abstract class RESTfulFragment extends Fragment
{

    /*//private int mContentResId;
    private MenuItem menuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(mContentResId);
    }

    //protected void setContentResId(int id) {
    //    mContentResId = id;
    //}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.menu_progress);
        ProgressBar progressBar = (ProgressBar) MenuItemCompat.getActionView(menuItem);
    }

    public void toggleProgressBar(boolean toggle){
        //menuItem.setVisible(toggle);
    }*/
}