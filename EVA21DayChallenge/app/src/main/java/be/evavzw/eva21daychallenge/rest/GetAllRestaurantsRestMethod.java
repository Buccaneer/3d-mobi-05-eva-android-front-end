package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Gebruiker on 09/11/2015.
 */
public class GetAllRestaurantsRestMethod extends AbstractRestMethod<List<Restaurant>> {
    private static final URI RESTAURANTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Restaurants/Find");
    private Context context;
    private double longitude, latitude, distance = 0.0;

    public GetAllRestaurantsRestMethod(Context context) {
        this.context = context;
    }

    public void setCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setCoordinatesAndDistance(double longitude, double latitude, double distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            String body = "Longitude=" + longitude + "&Latitude=" + latitude;
            body += distance == 0 ? "" : "&Distance=" + distance;
            Request r = new Request(RestMethodFactory.Method.POST, RESTAURANTURI, null, body.getBytes());
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    @Override
    protected List<Restaurant> parseResponseBody(String responseBody) throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        JSONArray jsonRestos = new JSONArray(responseBody);

        for (int i = 0; i < jsonRestos.length(); i++){
            Restaurant resto = new Restaurant(jsonRestos.getJSONObject(i));
            restaurants.add(resto);
        }

        return restaurants;
    }
}
