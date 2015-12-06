package be.evavzw.eva21daychallenge.activity.challenges;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.services.ChallengeManager;

/**
 * Created by Pieter-Jan on 27/11/2015.
 */
public class TextDetailActivity extends AppCompatActivity
{
    private ChallengeManager challengeManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugarfree_challenge);
        challengeManager = ChallengeManager.getInstance(this);
    }

}
