package be.evavzw.eva21daychallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import be.evavzw.eva21daychallenge.activity.challenges.ChallengeActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenu extends RESTfulActivity {

    private int mProgressStatus = 0;
    private final double CUTOFF = 100.0 / 21.0;
    private Handler mHandler = new Handler();
/*    @Bind(R.id.progressDaysRemaining)
    ProgressBar progressBar;*/
    @Bind(R.id.textViewProgress)
    TextView textViewProgress;
    @Bind(R.id.drawer_layout)
    DrawerLayout main;
@Bind(R.id.testView)
ImageView view;


    @OnClick(R.id.button_challenge)
    public void pickChallenge() {
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

      /*  progressBar.setProgress(100);
        setProgress();*/



        //TODO: hier moet getal komen per dag
        String tekst = "boom" + 12;
        int id = getResources().getIdentifier(tekst,"drawable", getPackageName());


        Glide.with(getApplicationContext())
                .load(id)
                .centerCrop()
               .into(new SimpleTarget<GlideDrawable>() {
                         @Override
                         public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                             view.setImageDrawable(resource);
                             resource.start();
                             resource.setLoopCount(1);


                         }
                     }
               );
    }


/*    public void setProgress() {

        if (progressBar != null) {
            new Thread(new Runnable() {
                public void run() {
                    while (mProgressStatus <= 60) {
                        mProgressStatus += 1;
                        // Update the progress bar
                        mHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(mProgressStatus);
                                int progress = (int) (mProgressStatus/CUTOFF);
                                textViewProgress.setText(String.valueOf(progress));
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
    }*/

    //txvChallengeCountdown.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ArtistampMedium.ttf"));
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
  */



}
