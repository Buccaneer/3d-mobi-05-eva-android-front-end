package be.evavzw.eva21daychallenge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Annemie on 16/11/2015.
 */


public class About extends RESTfulActivity {

    @Bind(R.id.about_img)
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_about);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        Glide.with(getApplicationContext())
                .load(R.drawable.varkentje).into(img);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_about));

        toolbar.inflateMenu(R.menu.menu_with_actions);
        toolbar.getMenu().findItem(R.id.nav_badges).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), BadgesActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @OnClick(R.id.button_website)
    public void clicked(){
        Uri uri = Uri.parse("http://www.evavzw.be");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
