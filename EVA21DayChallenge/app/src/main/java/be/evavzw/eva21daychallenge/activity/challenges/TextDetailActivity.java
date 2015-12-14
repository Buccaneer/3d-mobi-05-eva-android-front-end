package be.evavzw.eva21daychallenge.activity.challenges;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.services.ChallengeManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pieter-Jan on 27/11/2015.
 */
public class TextDetailActivity extends AppCompatActivity
{
    private ChallengeManager challengeManager;

    @Bind(R.id.addChallenge)
    Button addChallenge;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_details);
        ButterKnife.bind(this);
        challengeManager = ChallengeManager.getInstance(this);
        addChallenge.setText(R.string.finished_challenge);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.addChallenge)
    public void addChallenge() {
        new FinishChallengeTask().execute();
    }

    private class FinishChallengeTask extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                challengeManager.finishCurrentChallenge();
                return true;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                finish();
            }
        }
    }

}
