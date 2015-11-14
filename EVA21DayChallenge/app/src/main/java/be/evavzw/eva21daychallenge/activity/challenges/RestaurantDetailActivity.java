package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Comparator;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.rest.ChallengeManager;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity which displays all information about a Restaurant.
 * TODO: Add actual map
 */
public class RestaurantDetailActivity extends AppCompatActivity {
    public static final String RESTAURANT = "restaurant";

    private ChallengeManager challengeManager;
    Restaurant restaurant;

    @Bind(R.id.restaurantMap)
    ImageView restaurantMap;

    @Bind(R.id.restaurantTitle)
    TextView restaurantTitle;

    @Bind(R.id.restaurantAddress)
    TextView restaurantAddress;

    @Bind(R.id.restaurantProperties)
    TextView restaurantProperties;

    @Bind(R.id.restaurantDescription)
    TextView restaurantDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);
        ButterKnife.bind(this);

        challengeManager = ChallengeManager.getInstance(getApplicationContext());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.category_restaurant));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra(RESTAURANT);
        new FetchRestaurantDetailsTask().execute();
        //updateChallenge(restaurant);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }

    private String toUpperCase(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String toLowerCase(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Formats the Restaurant's details then puts them into their proper TextViews
     *
     * @param restaurant
     */
    private void updateChallenge(Restaurant restaurant) {
        restaurantTitle.setText(restaurant.getName());

        String address = "<b>" + getString(R.string.address) + "</b><br>";
        address += restaurant.getStreet().trim() + "<br>";
        address += restaurant.getPostal().trim() + " " + restaurant.getCity().trim();

        restaurantAddress.setText(Html.fromHtml(address));

        String properties = "<b>" + getString(R.string.properties) + "</b><br>";
        properties += getString(R.string.phone) + " " + restaurant.getPhone() + "<br>";
        properties += getString(R.string.email) + ": " + restaurant.getEmail() + "<br>";
        properties += getString(R.string.website) + ": " + restaurant.getWebsite();

        restaurantProperties.setText(Html.fromHtml(properties));

        String description = "<b>" + getString(R.string.description) + "</b><br>";
        description += restaurant.getDescription();
        restaurantDescription.setText(Html.fromHtml(description));
    }

    //A simple string comparator
    private class Vergelijker implements Comparator<String> {
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class FetchRestaurantDetailsTask extends AsyncTask<Void, Void, Restaurant>{

        @Override
        protected Restaurant doInBackground(Void... params) {
            return challengeManager.getRestaurantDetails(restaurant.getRestaurantId());
        }

        @Override
        protected void onPostExecute(Restaurant restaurant) {
            updateChallenge(restaurant);
        }
    }
}
