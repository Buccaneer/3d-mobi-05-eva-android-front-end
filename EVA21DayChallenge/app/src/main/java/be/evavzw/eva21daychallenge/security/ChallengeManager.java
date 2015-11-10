package be.evavzw.eva21daychallenge.security;

import android.content.Context;

import java.util.List;

import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.rest.GetAllRecipesRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRestaurantsRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodResult;

/**
 * Handles communication to retrieve {@link Recipe}s from the server
 */
public class ChallengeManager {

    private static ChallengeManager challengeManager;
    private Context context;

    /**
     * Singleton class
     * @param context
     * @return instance of itself
     */
    public static ChallengeManager getInstance(Context context){
        if(challengeManager == null){
            challengeManager = new ChallengeManager(context);
        }

        return challengeManager;
    }

    private ChallengeManager(Context context){
        this.context=context;
    }

    /**
     * Method to get all {@link Recipe}s from the server
     * @return a list of available {@link Recipe}s
     */
    public List<Recipe> getAllRecipes(){
        return new GetAllRecipesRestMethod(context).execute().getResource();
    }

    public List<Restaurant> getRestaurantsByLocation(double longitude, double latitude){
        GetAllRestaurantsRestMethod byLocationRestMethod =  new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinates(longitude, latitude);
        return byLocationRestMethod.execute().getResource();
    }

    public List<Restaurant> getRestaurantsByLocationAndRadius(double longitude, double latitude, int radius){
        GetAllRestaurantsRestMethod byLocationRestMethod =  new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinatesAndDistance(longitude, latitude, radius);
        return byLocationRestMethod.execute().getResource();
    }
}
