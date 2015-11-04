package be.evavzw.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.challenges.ChallengeActivity;

public class MainMenu extends RESTfulActivity {

    private int mProgressStatus = 0;
    private final double CUTOFF = 100.0/21.0;
    private Handler mHandler = new Handler();
    @Bind(R.id.progressDaysRemaining)
    ProgressBar progressBar;
    @Bind(R.id.textViewProgress)
    TextView textViewProgress;
    @Bind(R.id.drawer_layout)
    DrawerLayout main;



    @OnClick(R.id.button_challenge)
    public void pickChallenge()
    {
        Intent intent = new Intent(MainMenu.this, ChallengeActivity.class);
        startActivity(intent);
    }

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

        progressBar.setProgress(100);
        setProgress();

        Glide.with(getApplicationContext())
                .load(R.drawable.csillagoszold)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(this.getResources().getDisplayMetrics().widthPixels, this.getResources().getDisplayMetrics().heightPixels) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        BitmapDrawable background = new BitmapDrawable(bitmap);
                        main.setBackgroundDrawable(background);
                    }
                });


    }


    public void setProgress() {

        if(progressBar != null){
            new Thread(new Runnable() {
                public void run() {
                    while (mProgressStatus <= 60) {
                        mProgressStatus += 1;
                        // Update the progress bar
                        mHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(mProgressStatus);
                                int progress = (int) (mProgressStatus/CUTOFF);
                                textViewProgress.setText(String.valueOf(21 - progress));
                            }
                        });
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
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
