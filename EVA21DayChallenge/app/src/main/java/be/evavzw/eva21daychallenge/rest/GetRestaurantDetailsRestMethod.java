package be.evavzw.eva21daychallenge.rest;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 11/11/2015.
 */
public class GetRestaurantDetailsRestMethod extends AbstractRestMethod<Restaurant> {

    private Context context;
    private int restaurantId;

    public GetRestaurantDetailsRestMethod(Context context, int restaurantId) {
        this.context = context;
        this.restaurantId = restaurantId;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            final URI RESTAURANTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Restaurant/" + restaurantId);
            Request r = new Request(RestMethodFactory.Method.GET, RESTAURANTURI, null, null);
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    @Override
    protected Restaurant parseResponseBody(String responseBody) throws Exception {
        return new Restaurant(new JSONObject(responseBody));
    }
}
