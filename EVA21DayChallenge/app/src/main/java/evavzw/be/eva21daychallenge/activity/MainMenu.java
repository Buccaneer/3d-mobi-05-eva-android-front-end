package evavzw.be.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.activity.base.RESTfulActivity;
import evavzw.be.eva21daychallenge.security.UserManager;

public class MainMenu extends RESTfulActivity {
    /*@Bind(R.id.btnChallenges)
    Button btnChallenges;
    @Bind(R.id.btnEvaSite)
    Button btnEvaSite;
    @Bind(R.id.btnRecipes)
    Button btnRecipes;
    @Bind(R.id.btnPH1)
    Button btnPH1;
    @Bind(R.id.btnPH2)
    Button btnPH2;*/
    @Bind(R.id.challengeCountdown)
    TextView txvChallengeCountdown;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }
}
