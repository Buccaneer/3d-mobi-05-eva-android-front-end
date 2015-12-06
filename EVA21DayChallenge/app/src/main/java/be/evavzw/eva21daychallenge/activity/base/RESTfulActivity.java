package be.evavzw.eva21daychallenge.activity.base;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import be.evavzw.eva21daychallenge.activity.About;
import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.Login;
import be.evavzw.eva21daychallenge.activity.MainMenu;
import be.evavzw.eva21daychallenge.activity.profile_setup.ProfileSetup;
import be.evavzw.eva21daychallenge.services.UserManager;

/**
 * Superclass for all of our activities that need the {@link DrawerLayout} or/and {@link ProgressBar}
 */
public abstract class RESTfulActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int mContentResId;
    private UserManager userManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentResId);

        // Retrieve an instance of the UserManager
        userManager = UserManager.getInstance(getApplicationContext());

        // Instantiate the NavigationView and set the listener to this class if present
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        // Instantiate the Toolbar and DrawerLayout and configure them if they're present
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (toolbar != null && drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        // Set the ProgressBar properties, this is shown right under the Action Bar if an AsyncMethod is in progress
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
                progressBar.setY(contentView.getY() + 55);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            userManager.invalidateToken();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else if(id == R.id.nav_challenges){
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        }
        else if(id == R.id.nav_settings){
            Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("CALLED_FROM", "navigation");
            finish();
            startActivity(intent);
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Makes the ProgressBar visible or invisible, this is called by sub classes
     *
     * @param toggle <code>if(true) show progressbar, else hide progressbar</code>
     */
    public void toggleProgressBar(boolean toggle) {
        progressBar.setVisibility(toggle ? View.VISIBLE : View.INVISIBLE);
    }
}