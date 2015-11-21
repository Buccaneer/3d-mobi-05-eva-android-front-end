package be.evavzw.eva21daychallenge.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import be.evavzw.eva21daychallenge.models.*;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Pieter-Jan on 20/11/2015.
 */
public class ChallengeService
{
    public static final String BASE_URL = "http://evavzwrest.azurewebsites.net/api";
    static Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ssZ").create();
    static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    public static ChallengeApiInterface getApi()
    {
        return retrofit.create(ChallengeApiInterface.class);
    }

    public interface ChallengeApiInterface
    {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("/Recipes")
        Call<List<Recipe>> getRandomRecipes();

        @POST("/Restaurants/Find")
        Call<List<Restaurant>> getRestaurantsInRange(@Body RestaurantRequest request);

        @GET("/Restaurants/{id}/users")
        Call<Restaurant> getRestaurant(@Path("id") int id);

    }

    public class RestaurantRequest
    {
        @SerializedName("Longitude")
        final double longitude;
        @SerializedName("Latitude")
        final double latitude;
        @SerializedName("Distance")
        final double distance;

        RestaurantRequest(double longitude, double latitude, double distance)
        {
            this.longitude = longitude;
            this.latitude = latitude;
            this.distance = distance;
        }
    }

    private static void deserializeRecipes(final String json)
    {
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonObject ingredients = parser.parse(json).getAsJsonObject().getAsJsonObject("Ingredients");
        JsonObject name;
        //TODO : if we have spare time to port to Retrofit

    }

}
