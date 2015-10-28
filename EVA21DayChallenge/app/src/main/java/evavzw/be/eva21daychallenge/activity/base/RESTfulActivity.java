package evavzw.be.eva21daychallenge.activity.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.activity.Login;
import evavzw.be.eva21daychallenge.security.UserManager;

/**
 * Created by Jan on 17/10/2015.
 */
public abstract class RESTfulActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int mContentResId;
    private MenuItem menuItem;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentResId);
        userManager = UserManager.getInstance(getApplicationContext());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (toolbar != null && drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }
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
            startActivity(intent);
            this.finish();
        }/*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.menu_progress);
        return super.onPrepareOptionsMenu(menu);
    }

    public void toggleProgressBar(boolean toggle) {
        if (menuItem != null)
            menuItem.setVisible(toggle);
    }
}