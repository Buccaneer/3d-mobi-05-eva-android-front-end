package evavzw.be.eva21daychallenge.activity.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import evavzw.be.eva21daychallenge.activity.Login;
import evavzw.be.eva21daychallenge.security.UserManager;

/**
 * Created by Jan on 17/10/2015.
 */
public abstract class RESTfulActivity extends AppCompatActivity {

    private int mContentResId;
    private MenuItem menuItem;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentResId);
        userManager = UserManager.getInstance(getApplicationContext());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.logOut){
            userManager.invalidateToken();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleProgressBar(boolean toggle){
        menuItem.setVisible(toggle);
    }
}