package evavzw.be.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

        //txvChallengeCountdown.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ArtistampMedium.ttf"));
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
  */

    }

    @OnClick(R.id.tileFAQ)
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }
}
