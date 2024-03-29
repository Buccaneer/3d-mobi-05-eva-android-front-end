package be.evavzw.eva21daychallenge.services;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.evavzw.eva21daychallenge.database.DatabaseHelper;
import be.evavzw.eva21daychallenge.models.*;
import be.evavzw.eva21daychallenge.models.categories.*;
import be.evavzw.eva21daychallenge.models.challenges.*;
import be.evavzw.eva21daychallenge.rest.AddChallengeRestMethod;
import be.evavzw.eva21daychallenge.rest.FinishCurrentChallengeRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRecipesRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRestaurantsRestMethod;
import be.evavzw.eva21daychallenge.rest.GetChallengesFromUser;
import be.evavzw.eva21daychallenge.rest.GetCurrentChallengeRestMethod;
import be.evavzw.eva21daychallenge.rest.GetRecipesByRegionRestMethod;
import be.evavzw.eva21daychallenge.rest.GetRestaurantDetailsRestMethod;

/**
 * Handles communication to retrieve {@link Recipe}s from the server
 */
public class ChallengeManager {
    private static ChallengeManager challengeManager;
    private DatabaseHelper helper;
    private Context context;
    Dao<RecipeCategory, String> categoryDao;
    Dao<RecipeChallenge, Integer> recipeChallengeDao;
    Dao<RestaurantChallenge, Integer> restaurantChallengeDao;
    Dao<TextChallenge, Integer> textChallengeDao;
    Dao<Recipe, Integer> recipeDao;
    Dao<Ingredient, Integer> ingredientDao;
    Dao<RecipeProperty, Integer> recipePropertyDao;

    /**
     * Singleton class
     *
     * @param context
     * @return instance of itself
     */
    public static ChallengeManager getInstance(Context context) {
        if (challengeManager == null) {
            challengeManager = new ChallengeManager(context);
        }
        return challengeManager;
    }

    private ChallengeManager(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        try {
            categoryDao = helper.getRecipeCategories();
            recipeChallengeDao = helper.getRecipeChallenges();
            restaurantChallengeDao = helper.getRestaurantChallenges();
            textChallengeDao = helper.getTextChallenges();
            recipeDao = helper.getRecipes();
            ingredientDao = helper.getIngredients();
            recipePropertyDao = helper.getRecipeProperties();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh(Recipe recipe) {
        try {
            recipeDao.refresh(recipe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CHALLENGE METHODS
    public void addChallenge(String type, int id) {
        AddChallengeRestMethod addChallengeRestMethod = new AddChallengeRestMethod(context);
        addChallengeRestMethod.setType(type);
        addChallengeRestMethod.setId(id);
        addChallengeRestMethod.execute();
    }

    public void addChallenge(String type, int id, List<Ingredient> ingredients) {
        AddChallengeRestMethod addChallengeRestMethod = new AddChallengeRestMethod(context);
        addChallengeRestMethod.setType(type);
        addChallengeRestMethod.setId(id);
        addChallengeRestMethod.setIngredientsForCreativeCookingChallenge(ingredients);
        addChallengeRestMethod.execute();
    }

    public void finishCurrentChallenge()
    {
        Challenge currentChallenge = getCurrentChallenge();
        int serverId = currentChallenge.getServerId();
        new FinishCurrentChallengeRestMethod(context, serverId).execute();
        try
        {
            DeleteBuilder<RecipeChallenge, Integer> deleteRecipeChallenges = recipeChallengeDao.deleteBuilder();
            deleteRecipeChallenges.where().eq("isCurrentChallenge", true);
            deleteRecipeChallenges.delete();

            DeleteBuilder<RestaurantChallenge, Integer> deleteRestaurantChallenges = restaurantChallengeDao.deleteBuilder();
            deleteRestaurantChallenges.where().eq("isCurrentChallenge", true);
            deleteRestaurantChallenges.delete();

            DeleteBuilder<TextChallenge, Integer> deleteTextChallenges = textChallengeDao.deleteBuilder();
            deleteTextChallenges.where().eq("isCurrentChallenge", true);
            deleteTextChallenges.delete();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Looks for current challenge in cache, if not found it requests the server.
     * Returns null if no current challenge is set.
     *
     * @return Challenge currentChallenge or null
     */
    public Challenge getCurrentChallenge()
    {
        try {
            // NOT EFFICIENT! Model is sort of horrible anyway due to lack of inheritance strategies in ORMLite
            List<Challenge> challenges = new ArrayList<>();
            challenges.addAll(recipeChallengeDao.queryForAll());
            //challenges.addAll(restaurantChallengeDao.queryForAll());
            challenges.addAll(textChallengeDao.queryForAll());
            for (Challenge c : challenges)
            {
                if (c.isCurrentChallenge())
                {
                    return c;
                }
            }
            Challenge c = new GetCurrentChallengeRestMethod(context).execute().getResource();
            if (c != null) setCurrentChallenge(c);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new GetCurrentChallengeRestMethod(context).execute().getResource();
    }

    private void setCurrentChallenge(Challenge challenge)
    {
        deleteRedundantChallenges();
        challenge.setIsCurrentChallenge(true);
        try
        {
            if (challenge instanceof RecipeChallenge)
            {
                RecipeChallenge newChallenge = (RecipeChallenge) challenge;
                recipeChallengeDao.create(newChallenge);
                Recipe recipe = newChallenge.getRecipe();
                recipeDao.createIfNotExists(recipe);
                for (Ingredient i : recipe.getIngredients())
                    ingredientDao.createIfNotExists(i);
                for (RecipeProperty p : recipe.getProperties())
                    recipePropertyDao.createIfNotExists(p);
            }
            else if (challenge instanceof RestaurantChallenge)
            {
                //RestaurantChallenge newChallenge = (RestaurantChallenge) challenge;
                //restaurantChallengeDao.create(newChallenge);
            }
            else if (challenge instanceof TextChallenge)
            {
                TextChallenge newChallenge = (TextChallenge) challenge;
                textChallengeDao.create(newChallenge);
            }else if(challenge instanceof RegionRecipeChallenge){

            }else if(challenge instanceof CreativeCookingChallenge){

            }
            else {
                throw new IllegalArgumentException("Could not recognize challenge type.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void deleteRedundantChallenges() {
        try {
            DeleteBuilder<RecipeChallenge, Integer> deleteRecipeChallenges = recipeChallengeDao.deleteBuilder();
            deleteRecipeChallenges.where().isNull("date");
            deleteRecipeChallenges.delete();

            DeleteBuilder<RestaurantChallenge, Integer> deleteRestaurantChallenges = restaurantChallengeDao.deleteBuilder();
            deleteRestaurantChallenges.where().isNull("date");
            deleteRestaurantChallenges.delete();

            DeleteBuilder<TextChallenge, Integer> deleteTextChallenges = textChallengeDao.deleteBuilder();
            deleteTextChallenges.where().isNull("date");
            deleteTextChallenges.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //RECIPE METHODS

    public List<Recipe> getRecipesForCategory(String categoryName) {
        try {
            /*Dao<RecipeCategory, String> categoryDao = helper.getRecipeCategories();
            Dao<RecipeChallenge, Integer> recipeChallengeDao = helper.getRecipeChallenges();
            Dao<Recipe, Integer> recipeDao = helper.getRecipes();
            Dao<Ingredient, Integer> ingredientDao = helper.getIngredients();
            Dao<RecipeProperty, Integer> recipePropertyDao = helper.getRecipeProperties();*/
            RecipeCategory category = categoryDao.queryForId(categoryName);
            // Category exists and has challenges -> return recipes from existing challenges
            if (category == null) {
                Log.e("ChallengeManager", "Category not found, getting recipes from server");
                category = new RecipeCategory(categoryName);
                categoryDao.create(category);
                List<RecipeChallenge> challenges = new ArrayList<>();
                List<Recipe> recipes = new GetAllRecipesRestMethod(context).execute().getResource();
                for (Recipe recipe : recipes) {
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
            } else if (category.getChallenges() == null || category.getChallenges().size() == 0) {
                Log.e("ChallengeManager", "Category found but no challenges, getting recipes from server");
                List<RecipeChallenge> challenges = new ArrayList<>();
                List<Recipe> recipes = new GetAllRecipesRestMethod(context).execute().getResource();
                for (Recipe recipe : recipes) {
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
            } else {
                Log.e("ChallengeManager", "Category found with challenges, getting recipes from DB");
                List<Recipe> recipes = new ArrayList<>();
                for (Challenge c : category.getChallenges()) {
                    RecipeChallenge r = (RecipeChallenge) c;
                    recipes.add(r.getRecipe());
                }
                return recipes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TEXT METHODS

    public String getTextForCategory(String categoryName) {
        try {
            Dao<TextCategory, String> dao = helper.getTextCategories();
            TextCategory category = dao.queryForId(categoryName);
            // Category exists and has challenges -> return recipes from existing challenges
            if (category != null && category.getChallenges() != null && category.getChallenges().size() > 0) {
                TextChallenge challenge = (TextChallenge) category.getChallenges().toArray()[0];
                return challenge.getText();
            } else {
                //category = new Category(new GetAllRecipesRestMethod())
                return null; // TODO AFTER RETROFIT
            }
        } catch (SQLException e) {
            return null;
        }
    }

    //RESTAURANT METHODS

    public List<Restaurant> getRestaurantsByLocation(double longitude, double latitude) {
        GetAllRestaurantsRestMethod byLocationRestMethod = new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinates(longitude, latitude);
        return byLocationRestMethod.execute().getResource();
    }

    public List<Restaurant> getRestaurantsByLocationAndRadius(double longitude, double latitude, int radius) {
        GetAllRestaurantsRestMethod byLocationRestMethod = new GetAllRestaurantsRestMethod(context);
        byLocationRestMethod.setCoordinatesAndDistance(longitude, latitude, radius);
        return byLocationRestMethod.execute().getResource();
    }

    public Restaurant getRestaurantDetails(int restaurantId) {
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

    public List<Challenge> getChallengesFromUser() {
        return new GetChallengesFromUser(context).execute().getResource();
    }

    public List<Recipe> getRecipesByRegion(String region) {
        GetRecipesByRegionRestMethod getRecipesByRegion = new GetRecipesByRegionRestMethod(context);
        getRecipesByRegion.setRegion(region);
        return getRecipesByRegion.execute().getResource();
    }
}
