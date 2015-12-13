package be.evavzw.eva21daychallenge.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import be.evavzw.eva21daychallenge.activity.challenges.ChallengeActivity;
import be.evavzw.eva21daychallenge.activity.challenges.ChallengeHistoryActivity;
import be.evavzw.eva21daychallenge.models.User;
import be.evavzw.eva21daychallenge.services.UserManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenu extends RESTfulActivity
{

    @Bind(R.id.textViewProgress)
    TextView textViewProgress;

    @Bind(R.id.textViewDagen)
    TextView textViewDagen;

    private UserManager userManager;

    @Bind(R.id.testView)
    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.setContentResId(R.layout.activity_main_menu);
        super.onCreate(savedInstanceState);

        userManager = UserManager.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_main_menu));

        toolbar.inflateMenu(R.menu.menu_with_actions);
        toolbar.getMenu().findItem(R.id.nav_badges).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), BadgesActivity.class);
                startActivity(intent);
                return true;
            }
        });

        new FetchUserTask().execute();
    }

    @OnClick(R.id.button_challenge)
    public void pickChallenge() {
        Intent intent = new Intent(MainMenu.this, ChallengeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_challengeHistory)
    public void challengeHistory(){
        Intent intent = new Intent(this, ChallengeHistoryActivity.class);
        startActivity(intent);
    }

    private void setupMenu(User user) {
        textViewProgress.setText(user.getChallengesDone() + " ");

        if(user.getChallengesDone() >= 0 && user.getChallengesDone() <= 20){
            if(user.getChallengesDone() == 1){
                textViewDagen.setText(getString(R.string.viewDag));
            }
            String tekst = "boom" + (user.getChallengesDone()+1);
            int id = getResources().getIdentifier(tekst, "drawable", getPackageName());

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
        }else if(user.getChallengesDone() > 20){
            String tekst = "boom"+21;
            int id = getResources().getIdentifier(tekst, "drawable", getPackageName());

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
    }

    private class FetchUserTask extends AsyncTask<Void, Void, Boolean> {

        private User user;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                user = userManager.getUser();
                return true;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                setupMenu(user);
            }
        }
    }

}
