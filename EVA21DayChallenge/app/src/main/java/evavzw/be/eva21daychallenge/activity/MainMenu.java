package evavzw.be.eva21daychallenge.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_main_menu);
        super.onCreate(savedInstanceState);


        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        toggleProgressBar(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


        //txvChallengeCountdown.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ArtistampMedium.ttf"));
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
  */



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


}
