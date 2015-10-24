package evavzw.be.eva21daychallenge.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.activity.base.RESTfulActivity;
import evavzw.be.eva21daychallenge.security.UserManager;

public class MainMenu extends RESTfulActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.left_drawer)
    ListView left;
    private String[] menu;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_main_menu);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        menu = new String[]{"EVA website, Recipes, About, FAQ"};


        left.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, menu));

        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.drawer_open, R.string.drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("title");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawer.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //txvChallengeCountdown.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ArtistampMedium.ttf"));
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
  */

    }

/*    @OnClick(R.id.tileFAQ)
    public void tileFAQClicked(){
        //Intent intent = new Intent(this, FrequentlyAskedQuestions.class);
        //startActivity(intent);
    }

    @OnClick(R.id.tileTipsTricks)
    public void tileTipsTricksClicked(){
        //Intent intent = new Intent(this, TipsTricks.class);
        //startActivity(intent);
    }

    @OnClick(R.id.tileRecipes)
    public void tileRecipesclicked(){
        //Intent intent = new Intent(this, Recipes.class);
        //startActivity(intent);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
