package be.evavzw.eva21daychallenge.activity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.customComponent.MagicTextView;
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
    @Bind(R.id.badgeScrollview)
    ScrollView badgeScrollview;
    @Bind(R.id.badgeProgress)
    ProgressBar badgeProgress;
    @Bind(R.id.userPoints)
    MagicTextView userPoints;
    @Bind(R.id.userProgress)
    ProgressBar userProgress;
    @Bind(R.id.userLevel)
    TextView userLevel;

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
            if (savedInstanceState.containsKey("name")) {
                badgeName.setText(savedInstanceState.getString("name"));
            }
            if (savedInstanceState.containsKey("description")) {
                badgeDescription.setText(savedInstanceState.getString("description"));
            }
        }

        if (user == null)
            new FetchUserTask().execute();
        else {
            setupScreen(user);
            badgeProgress.setVisibility(View.GONE);
            badgeScrollview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupScreen(User user) {
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

        int max = 0;
        int currentPoints = 0;
        if (badges.contains("level1")) {
            userLevel.setText(R.string.level1);
            max = 5;
            currentPoints = user.getPoints();
        }
        if (badges.contains("level2")) {
            userLevel.setText(R.string.level2);
            max = 10;
            currentPoints = user.getPoints() - 5;
        }
        if (badges.contains("level3")) {
            userLevel.setText(R.string.level3);
            max = 20;
            currentPoints = user.getPoints() - 10;
        }
        if (badges.contains("level4")) {
            userLevel.setText(R.string.level4);
            max = 32;
            currentPoints = user.getPoints() - 20;
        }
        if (badges.contains("level5")) {
            userLevel.setText(R.string.level5);
            max = 50;
            currentPoints = user.getPoints() - 32;
        }
        if (badges.contains("level6")) {
            userLevel.setText(R.string.level6);
            max = 50;
            currentPoints = 50;
        }

        userProgress.setMax(max);
        userProgress.setProgress(currentPoints);
        userPoints.setText(currentPoints + "/" + max + " " + getString(R.string.points));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("user", user);
        if (!badgeName.getText().toString().isEmpty()) {
            outState.putString("name", badgeName.getText().toString());
        }
        if (!badgeDescription.getText().equals(getString(R.string.badgeDescriptionPlaceholder))) {
            outState.putString("description", badgeDescription.getText().toString());
        }
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
            badgeProgress.setVisibility(View.GONE);
            badgeScrollview.setVisibility(View.VISIBLE);
            if (success) {
                setupScreen(user);
            }
        }
    }
}
