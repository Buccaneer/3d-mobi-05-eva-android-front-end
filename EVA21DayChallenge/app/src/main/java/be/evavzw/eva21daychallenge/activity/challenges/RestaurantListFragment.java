package be.evavzw.eva21daychallenge.activity.challenges;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.bumptech.glide.Glide;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.rest.ChallengeManager;

/**
 * Created by Pieter-Jan on 4/11/2015.
 * TODO: Add actual map
 */
public class RestaurantListFragment extends ChallengeFragment implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>
{

    private static final String TAG = "RestaurantListFragment";

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS;

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    private ChallengeManager challengeManager;
    private List<Restaurant> restaurants;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.restaurant_challenge, container, false);
        //setupTitle(layout);
        challengeManager = ChallengeManager.getInstance(getContext());
        rv = (RecyclerView) layout.findViewById(R.id.restaurantList);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        //setupRecyclerView(rv);

        mRequestingLocationUpdates = false;

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        if (Build.VERSION.SDK_INT >= 23)
        {
            askLocationPermission();
        }
        else
        {
            checkLocationSettings();
        }
        return layout;
    }

    private void askLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        else
        {
            checkLocationSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        Log.i(TAG, "PERMISSION CALLBACK");
        if (requestCode == 200)
        {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                checkLocationSettings();
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient()
    {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    private void checkLocationSettings()
    {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult)
    {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode())
        {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to " +
                        "upgrade location settings ");

                try
                {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                }
                catch (IntentSender.SendIntentException e)
                {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>()
        {
            @Override
            public void onResult(Status status)
            {
                mRequestingLocationUpdates = true;
            }
        });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates()
    {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>()
        {
            @Override
            public void onResult(Status status)
            {
                mRequestingLocationUpdates = false;
            }
        });
    }

    @Override
    public void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause()
    {
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
        }
        super.onPause();
    }

    @Override
    public void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint)
    {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null)
        {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation = location;
        new FetchRestaurantsTask(rv).execute(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /*private void setupTitle(LinearLayout layout)
    {
        ImageView iv = (ImageView) layout.findViewById(R.id.titleAvatar);
        Glide.with(iv.getContext())
                .load(Images.getRandomCheeseDrawable())
                .fitCenter()
                .into(iv);
        TextView tv = (TextView) layout.findViewById(R.id.titleText);
        tv.setText("Title");
    }*/

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), restaurants, getString(R.string.category_restaurant_descr)));
    }

    private static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Restaurant> mValues;
        private String description;

        public static class Description extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public Description(View view)
            {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.categoryAvatar);
                mTextView = (TextView) view.findViewById(R.id.categoryDescription);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public Restaurant mRestaurant;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            return position == 0 ? 0 : 1;
        }

        public Restaurant getValueAt(int position)
        {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Restaurant> restaurants, String description)
        {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = restaurants;
            /*mValues.add(0, mValues.get(0));
            mValues.add(3, mValues.get(0));
            mValues.add(3, mValues.get(0));
            mValues.add(3, mValues.get(0));
            mValues.add(3, mValues.get(0));
            mValues.add(3, mValues.get(0));*/
            this.description = description;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if (viewType == 0)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_description, parent, false);
                return new Description(view);
            }
            else
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                view.setBackgroundResource(mBackground);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, final int position)
        {
            if (holderr instanceof ViewHolder)
            {
                final ViewHolder holder = (ViewHolder) holderr;
                holder.mRestaurant = mValues.get(position);
                holder.mTextView.setText(mValues.get(position).getName());

                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RestaurantDetailActivity.class);
                        intent.putExtra(RestaurantDetailActivity.RESTAURANT, holder.mRestaurant);
                        context.startActivity(intent);
                    }
                });

                // TODO : Fix performantie / Out of memory
                Glide.with(holder.mImageView.getContext())
                        .load("" /*holder.mRestaurant.getImage()*/)
                        .placeholder(R.drawable.cutlery_green)
                        .thumbnail(0.2f)
                        .into(holder.mImageView);

                    /*Glide.with(holder.mImageView.getContext())
                            .load(Images.getRandomCheeseDrawable())
                            .fitCenter()
                            .into(holder.mImageView);*/
            }
            else if (holderr instanceof Description)
            {
                final Description holder = (Description) holderr;
                holder.mTextView.setText(description);
                Glide.with(holder.mImageView.getContext())
                        .load("" /*holder.mRestaurant.getImage()*/)
                        .placeholder(R.drawable.cutlery)
                        .thumbnail(0.2f)
                        .into(holder.mImageView);
            }

        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }
    }

    private void fetchChallenges()
    {
        //FetchRestaurantsTask fetch = new FetchRestaurantsTask(rv);
        //fetch.execute();

        /** TIJDELIJK **/
        /*JSONArray obj = null;
        try {
            obj = new JSONArray(Mock.restaurants);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //voor elk object in de recipes (dus elk recept) de constructor van recept aanroepen met recipes stukje
        for (int i = 0; i < obj.length(); i++) {
            JSONObject jsonRow = null;
            try {
                jsonRow = obj.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                restaurants.add(new Restaurant(jsonRow));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.restaurants = restaurants;*/
        /** EINDE TIJDELIJK **/
    }

    private class FetchRestaurantsTask extends AsyncTask<Double, String, Boolean>
    {
        List<Restaurant> list;
        RecyclerView recyclerView;

        public FetchRestaurantsTask(RecyclerView recyclerView)
        {
            super();
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Boolean doInBackground(Double... objects)
        {
            Log.e("Longitude", String.valueOf(objects[0]));
            Log.e("Latitude", String.valueOf(objects[1]));
            try
            {
                list = challengeManager.getRestaurantsByLocation(objects[0], objects[1]);
                Log.e("RestaurantListFragment", "Got restaurants");
                return true;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed)
        {
            //setRefresh(false);
            if (succeed)
            {
                Log.e("RecipeListFragment", "Post Execute called");
                restaurants = list;
                setupRecyclerView(recyclerView);
            }
            else
            {
                Log.e("qmlskdjf", "Post Execute Failed");
            }
        }
    }
}
