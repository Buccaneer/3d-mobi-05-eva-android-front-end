package be.evavzw.eva21daychallenge.activity.challenges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import be.evavzw.eva21daychallenge.R;

/**
 * Created by Annemie on 19/11/2015.
 */
public class SugarfreeChallenge extends ChallengeFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_sugarfree_challenge, container, false);
        return layout;
    }
}
