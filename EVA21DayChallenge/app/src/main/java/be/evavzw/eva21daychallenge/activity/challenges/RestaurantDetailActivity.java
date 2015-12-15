package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Comparator;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.MainMenu;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.services.ChallengeManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity which displays all information about a Restaurant.
 * TODO: Add actual map
 */
public class RestaurantDetailActivity extends AppCompatActivity {
    public static final String RESTAURANT = "restaurant";
    public static final String CURRENT = "current";

    private ChallengeManager challengeManager;

    Restaurant restaurant;

    @Bind(R.id.restaurantTitle)
    TextView restaurantTitle;

    @Bind(R.id.restaurantAddress)
    TextView restaurantAddress;

    @Bind(R.id.restaurantProperties)
    TextView restaurantProperties;

    @Bind(R.id.restaurantDescription)
    TextView restaurantDescription;

    @Bind(R.id.addChallenge)
    FloatingActionButton addChallenge;

    private GoogleMap googleMap;
    private boolean current = false;

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

        restaurant = (Restaurant) getIntent().getSerializableExtra(RESTAURANT);
        current = getIntent().getBooleanExtra(CURRENT, false);
        if (current)
        {
            addChallenge.setImageDrawable(getResources().getDrawable(R.drawable.apptheme_btn_check_on_focused_holo_light));
        }
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.restaurantMapFragment)).getMap();

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                return;
            }
        });

        new FetchRestaurantDetailsTask().execute();
        //updateChallenge(restaurant);
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
        address += "<a href=\"geo:0,0?q=" +
                restaurant.getStreet().trim() + " " +
                restaurant.getPostal().trim() + " " +
                restaurant.getCity().trim() + "\" >";

        address += restaurant.getStreet().trim() + "<br>";
        address += restaurant.getPostal().trim() + " " + restaurant.getCity().trim();
        address += "</a>";

        restaurantAddress.setText(Html.fromHtml(address));
        restaurantAddress.setMovementMethod(LinkMovementMethod.getInstance());

        String properties = "<b>" + getString(R.string.properties) + "</b><br>";
        properties += getString(R.string.phone) + ": ";
        properties += "<a href=\"tel:" + restaurant.getPhone() + "\">";
        properties += restaurant.getPhone() + "</a><br>";

        properties += getString(R.string.email) + ": ";
        properties += "<a href=\"mailto:" + restaurant.getEmail() + "\">";
        properties += restaurant.getEmail() + "</a><br>";

        properties += getString(R.string.website) + ": ";
        properties += "<a href=\"" + restaurant.getWebsite() + "\">";
        properties += restaurant.getWebsite() + "</a>";
        restaurantProperties.setText(Html.fromHtml(properties));
        restaurantProperties.setMovementMethod(LinkMovementMethod.getInstance());

        String description = "<b>" + getString(R.string.description) + "</b><br>";
        description += restaurant.getDescription();
        restaurantDescription.setText(Html.fromHtml(description));

        LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

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

    @OnClick(R.id.addChallenge)
    public void addChallenge(){
        if (!current) {
            new AddChallengeTask().execute();
        } else {
            new FinishChallengeTask().execute();
        }
    }

    private class AddChallengeTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                challengeManager.addChallenge("Restaurant", restaurant.getRestaurantId());
                return true;
            }catch(Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.error500, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                Intent intent = new Intent(RestaurantDetailActivity.this, MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    private class FinishChallengeTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                challengeManager.finishCurrentChallenge();
                return true;
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.error500, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                finish();
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
