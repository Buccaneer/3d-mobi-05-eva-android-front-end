package be.evavzw.eva21daychallenge.activity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.User;
import be.evavzw.eva21daychallenge.services.UserManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BadgesActivity extends AppCompatActivity {

    @Bind(R.id.badgeStarter)
    ImageView badgeStarter;
    @Bind(R.id.badgeCreatieveling)
    ImageView badgeCreatieveling;
    @Bind(R.id.badgeDoorzetter)
    ImageView badgeDoorzetter;
    @Bind(R.id.badgeExplorer)
    ImageView badgeExplorer;
    @Bind(R.id.badgeGastronoom)
    ImageView badgeGastronoom;
    @Bind(R.id.badgeTrotsegebruiker)
    ImageView badgeTrotsegebruiker;
    @Bind(R.id.badgeSugarrush)
    ImageView badgeSugarrush;
    @Bind(R.id.badgeGenieter)
    ImageView badgeGenieter;
    @Bind(R.id.badgeName)
    TextView badgeName;
    @Bind(R.id.badgeDescription)
    TextView badgeDescription;

    private UserManager userManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        ButterKnife.bind(this);

        userManager = UserManager.getInstance(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.badges));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("user")) {
                user = (User) savedInstanceState.getSerializable("user");
            }
        }

        if (user == null)
            new FetchUserTask().execute();
        else
            setupBadges(user);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupBadges(User user) {
        this.user = user;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        List<String> badges = user.getBadges();
        Glide.with(getApplicationContext())
                .load(R.drawable.creatieveling)
                .fitCenter()
                .into(badgeCreatieveling);
        Glide.with(getApplicationContext())
                .load(R.drawable.explorer)
                .fitCenter()
                .into(badgeExplorer);
        Glide.with(getApplicationContext())
                .load(R.drawable.sugarrush)
                .fitCenter()
                .into(badgeSugarrush);
        Glide.with(getApplicationContext())
                .load(R.drawable.gastronoom)
                .fitCenter()
                .into(badgeGastronoom);
        Glide.with(getApplicationContext())
                .load(R.drawable.genieter)
                .fitCenter()
                .into(badgeGenieter);
        Glide.with(getApplicationContext())
                .load(R.drawable.trotsegebruiker)
                .fitCenter()
                .into(badgeTrotsegebruiker);
        Glide.with(getApplicationContext())
                .load(R.drawable.starter)
                .fitCenter()
                .into(badgeStarter);
        Glide.with(getApplicationContext())
                .load(R.drawable.doorzetter)
                .fitCenter()
                .into(badgeDoorzetter);

        if (!badges.contains("creatieveling"))
            badgeCreatieveling.setColorFilter(filter);
        if (!badges.contains("explorer"))
            badgeExplorer.setColorFilter(filter);
        if (!badges.contains("sugarrush"))
            badgeSugarrush.setColorFilter(filter);
        if (!badges.contains("gastronoom"))
            badgeGastronoom.setColorFilter(filter);
        if (!badges.contains("genieter"))
            badgeGenieter.setColorFilter(filter);
        if (!badges.contains("trotsegebruiker"))
            badgeTrotsegebruiker.setColorFilter(filter);
        if (!badges.contains("starter"))
            badgeStarter.setColorFilter(filter);
        if (!badges.contains("doorzetter"))
            badgeDoorzetter.setColorFilter(filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("user", user);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.badgeStarter)
    public void badgeStarterOnClick() {
        badgeName.setText(R.string.badgeStarter);
        badgeDescription.setText(R.string.badgeStarterDescription);
    }

    @OnClick(R.id.badgeCreatieveling)
    public void badgeCreatievelingOnclick() {
        badgeName.setText(R.string.badgeCreatieveling);
        badgeDescription.setText(R.string.badgeCreatievelingDescription);
    }

    @OnClick(R.id.badgeDoorzetter)
    public void badgeDoorzetterOnClick() {
        badgeName.setText(R.string.badgeDoorzetter);
        badgeDescription.setText(R.string.badgeDoorzetterDescription);
    }

    @OnClick(R.id.badgeExplorer)
    public void badgeExplorerOnClick() {
        badgeName.setText(R.string.badgeExplorer);
        badgeDescription.setText(R.string.badgeExplorerDescription);
    }

    @OnClick(R.id.badgeGastronoom)
    public void badgeGastronoomOnClick() {
        badgeName.setText(R.string.badgeGastronoom);
        badgeDescription.setText(R.string.badgeGastronoomDescription);
    }

    @OnClick(R.id.badgeTrotsegebruiker)
    public void badgeTrotseGebruikerOnClick() {
        badgeName.setText(R.string.badgeTrotseGebruiker);
        badgeDescription.setText(R.string.badgeTrotseGebruikerDescription);
    }

    @OnClick(R.id.badgeSugarrush)
    public void badgeSugarrushOnClick() {
        badgeName.setText(R.string.badgeSugarrush);
        badgeDescription.setText(R.string.badgeSugarrushDescription);
    }

    @OnClick(R.id.badgeGenieter)
    public void badgeGenieterOnClick() {
        badgeName.setText(R.string.badgeGenieter);
        badgeDescription.setText(R.string.badgeGenieterDescription);
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
            if (success) {
                setupBadges(user);
            }
        }
    }
}
