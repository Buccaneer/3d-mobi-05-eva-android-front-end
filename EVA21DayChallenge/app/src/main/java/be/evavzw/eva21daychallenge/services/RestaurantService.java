package be.evavzw.eva21daychallenge.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Annemie on 11/11/2015.
 */
public class RestaurantService implements LocationListener {
    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private static RestaurantService instance = null;

    private LocationManager locationManager;
    public Location location;
    public double longitude;
    public double latitude;
    private boolean isGPSEnabled, isForceNetwork, isNetworkEnabled, locationServiceAvailable;


    /**
     * Singleton implementation
     * @return
     */
    public static RestaurantService getLocationManager(Context context)     {
        if (instance == null) {
            instance = new RestaurantService(context);
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private RestaurantService( Context context )     {

        initLocationService(context);
        Log.i("info", "LocationService created");
    }



    /**
     * Sets up location service after permissions is granted
     */
    @TargetApi(23)
    private void initLocationService(Context context) {


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try   {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                    if (locationManager != null)   {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateCoordinates();
                    }
                }//end if

                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);

                    if (locationManager != null)  {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateCoordinates();
                    }
                }
            }
        } catch (Exception ex)  {
            Log.i("info", "Error creating location service: " + ex.getMessage() );

        }
    }

    private void updateCoordinates() {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
    @Override
    public void onLocationChanged(Location location)     {
        this.location = location;
        updateCoordinates();
    }
}
