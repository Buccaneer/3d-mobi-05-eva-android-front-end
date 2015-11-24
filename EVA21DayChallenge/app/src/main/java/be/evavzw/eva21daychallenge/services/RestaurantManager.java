package be.evavzw.eva21daychallenge.services;

import android.content.Context;

import be.evavzw.eva21daychallenge.rest.AddChallengeRestMethod;

/**
 * Created by Jan on 22/11/2015.
 */
public class RestaurantManager {
    private static RestaurantManager restaurantManager;
    private Context context;

    /**
     * Singleton class
     * @param context
     * @return instance of itself
     */
    public static RestaurantManager getInstance(Context context){
        if(restaurantManager == null){
            restaurantManager = new RestaurantManager(context);
        }

        return restaurantManager;
    }

    private RestaurantManager(Context context){
        this.context=context;
    }

    public void addChallenge(String type, int id) {
        AddChallengeRestMethod addChallengeRestMethod = new AddChallengeRestMethod(context);
        addChallengeRestMethod.setType(type);
        addChallengeRestMethod.setId(id);
        addChallengeRestMethod.execute();
    }
}
