package be.evavzw.eva21daychallenge.activity;

import android.app.Activity;
import android.os.Bundle;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import butterknife.ButterKnife;

/**
 * Created by Annemie on 16/11/2015.
 */


public class About extends RESTfulActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_about);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);


    }


}
