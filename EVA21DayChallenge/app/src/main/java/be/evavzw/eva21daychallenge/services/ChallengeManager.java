package be.evavzw.eva21daychallenge.services;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.database.DatabaseHelper;
import be.evavzw.eva21daychallenge.models.*;
import be.evavzw.eva21daychallenge.models.categories.*;
import be.evavzw.eva21daychallenge.models.challenges.*;
import be.evavzw.eva21daychallenge.rest.GetAllRecipesRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRestaurantsRestMethod;
import be.evavzw.eva21daychallenge.rest.GetRestaurantDetailsRestMethod;

/**
 * Handles communication to retrieve {@link Recipe}s from the server
 */
public class ChallengeManager
{
    private static ChallengeManager challengeManager;
    private DatabaseHelper helper;
    private Context context;
    Dao<RecipeCategory, String> categoryDao;
    Dao<RecipeChallenge, Integer> recipeChallengeDao;
    Dao<Recipe, Integer> recipeDao;
    Dao<Ingredient, Integer> ingredientDao;
    Dao<RecipeProperty, Integer> recipePropertyDao;

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
        try {
            categoryDao = helper.getRecipeCategories();
            recipeChallengeDao = helper.getRecipeChallenges();
            recipeDao = helper.getRecipes();
            ingredientDao = helper.getIngredients();
            recipePropertyDao = helper.getRecipeProperties();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void refresh(Recipe recipe)
    {
        try
        {
            recipeDao.refresh(recipe);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Recipe> getRecipesForCategory(String categoryName)
    {
        try
        {
            /*Dao<RecipeCategory, String> categoryDao = helper.getRecipeCategories();
            Dao<RecipeChallenge, Integer> recipeChallengeDao = helper.getRecipeChallenges();
            Dao<Recipe, Integer> recipeDao = helper.getRecipes();
            Dao<Ingredient, Integer> ingredientDao = helper.getIngredients();
            Dao<RecipeProperty, Integer> recipePropertyDao = helper.getRecipeProperties();*/
            RecipeCategory category = categoryDao.queryForId(categoryName);
            // Category exists and has challenges -> return recipes from existing challenges
            if (category == null)
            {
                Log.e("ChallengeManager", "Category not found, getting recipes from server");
                category = new RecipeCategory(categoryName);
                categoryDao.create(category);
                List<RecipeChallenge> challenges = new ArrayList<>();
                List<Recipe> recipes = new GetAllRecipesRestMethod(context).execute().getResource();
                for (Recipe recipe : recipes)
                {
                    RecipeChallenge challenge = new RecipeChallenge(category, recipe);
                    recipeChallengeDao.create(challenge);

                    challenges.add(challenge);
                    recipe.setChallenge(challenge);
                    recipeDao.createIfNotExists(recipe);
                    for (Ingredient i : recipe.getIngredients())
                        ingredientDao.createIfNotExists(i);
                    for (RecipeProperty p : recipe.getProperties())
                        recipePropertyDao.createIfNotExists(p);
                }
                category.setChallenges(challenges);
                return recipes;
            }
            else if (category.getChallenges() == null || category.getChallenges().size() == 0)
            {
                Log.e("ChallengeManager", "Category found but no challenges, getting recipes from server");
                List<RecipeChallenge> challenges = new ArrayList<>();
                List<Recipe> recipes = new GetAllRecipesRestMethod(context).execute().getResource();
                for (Recipe recipe : recipes)
                {
                    RecipeChallenge challenge = new RecipeChallenge(category, recipe);
                    recipeChallengeDao.create(challenge);

                    challenges.add(challenge);
                    recipe.setChallenge(challenge);
                    recipeDao.createIfNotExists(recipe);
                    for (Ingredient i : recipe.getIngredients())
                        ingredientDao.createIfNotExists(i);
                    for (RecipeProperty p : recipe.getProperties())
                        recipePropertyDao.createIfNotExists(p);
                }
                category.setChallenges(challenges);
                return recipes;
            }
            else
            {
                Log.e("ChallengeManager", "Category found with challenges, getting recipes from DB");
                List<Recipe> recipes = new ArrayList<>();
                for (Challenge c : category.getChallenges())
                {
                    RecipeChallenge r = (RecipeChallenge) c;
                    recipes.add(r.getRecipe());
                }
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
            Dao<TextCategory, String> dao = helper.getTextCategories();
            TextCategory category = dao.queryForId(categoryName);
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
        return new GetRestaurantDetailsRestMethod(context, restaurantId).execute().getResource();
        /*try
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
        }*/
    }
}
