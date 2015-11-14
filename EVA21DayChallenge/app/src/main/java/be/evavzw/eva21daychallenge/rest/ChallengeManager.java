package be.evavzw.eva21daychallenge.rest;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.database.DatabaseHelper;
import be.evavzw.eva21daychallenge.models.*;
import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.models.challenges.RecipeChallenge;
import be.evavzw.eva21daychallenge.models.challenges.TextChallenge;

/**
 * Handles communication to retrieve {@link Recipe}s from the server
 */
public class ChallengeManager
{
    private static ChallengeManager challengeManager;
    private DatabaseHelper helper;
    private Context context;

    /**
     * Singleton class
     *
     * @param context
     * @return instance of itself
     */
    public static ChallengeManager getInstance(Context context)
    {
        if (challengeManager == null)
        {
            challengeManager = new ChallengeManager(context);
        }
        return challengeManager;
    }

    private ChallengeManager(Context context)
    {
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    public List<Recipe> getRecipesForCategory(String categoryName)
    {
        try
        {
            Dao<Category, String> dao = helper.getCategories();
            Category category = dao.queryForId(categoryName);
            // Category exists and has challenges -> return recipes from existing challenges
            if (category != null && category.getChallenges() != null && category.getChallenges().size() > 0)
            {
                Log.e("ChallengeManager", "Category found, getting recipes from DB");
                List<Recipe> recipes = new ArrayList<>();
                for (Challenge c : category.getChallenges())
                {
                    RecipeChallenge r = (RecipeChallenge) c;
                    recipes.add(r.getRecipe());
                }
                return recipes;
            }
            else
            {
                Log.e("ChallengeManager", "Category not found, getting recipes from server");
                category = new Category(categoryName);
                List<Challenge> challenges = new ArrayList<>();
                List<Recipe> recipes = new GetAllRecipesRestMethod(context).execute().getResource();
                for (Recipe recipe : recipes)
                {
                    challenges.add(new RecipeChallenge(category, recipe));
                }
                category.setChallenges(challenges);
                dao.create(category);
                return recipes;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getTextForCategory(String categoryName)
    {
        try
        {
            Dao<Category, String> dao = helper.getCategories();
            Category category = dao.queryForId(categoryName);
            // Category exists and has challenges -> return recipes from existing challenges
            if (category != null && category.getChallenges() != null && category.getChallenges().size() > 0)
            {
                TextChallenge challenge = (TextChallenge) category.getChallenges().toArray()[0];
                return challenge.getText();
            }
            else
            {
                //category = new Category(new GetAllRecipesRestMethod())
                return null; // TODO AFTER RETROFIT
            }
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<Restaurant> getRestaurantsByLocation(double longitude, double latitude)
    {
        GetAllRestaurantsRestMethod byLocationRestMethod = new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinates(longitude, latitude);
        return byLocationRestMethod.execute().getResource();
    }

    public List<Restaurant> getRestaurantsByLocationAndRadius(double longitude, double latitude, int radius)
    {
        GetAllRestaurantsRestMethod byLocationRestMethod = new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinatesAndDistance(longitude, latitude, radius);
        return byLocationRestMethod.execute().getResource();
    }

    public Restaurant getRestaurantDetails(int restaurantId)
    {
        try
        {
            Dao<Restaurant, Integer> dao = helper.getRestaurants();
            Restaurant restaurant = dao.queryForId(restaurantId);
            if (restaurant != null)
                return restaurant;
            restaurant = new GetRestaurantDetailsRestMethod(context, restaurantId).execute().getResource();
            dao.create(restaurant);
            return restaurant;
        }
        catch (SQLException e)
        {
            return null;
        }
    }
}
