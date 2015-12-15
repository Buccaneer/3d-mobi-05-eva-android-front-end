package be.evavzw.eva21daychallenge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import be.evavzw.eva21daychallenge.models.*;
import be.evavzw.eva21daychallenge.models.categories.*;
import be.evavzw.eva21daychallenge.models.challenges.*;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "evavzw.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Recipe, Integer> recipeDao;
    private Dao<Restaurant, Integer> restaurantDao;
    private Dao<RecipeCategory, String> recipeCategoryDao;
    private Dao<RestaurantCategory, String> restaurantCategoryDao;
    private Dao<TextCategory, String> textCategoryDao;
    private Dao<RecipeChallenge, Integer> recipeChallengeDao;
    private Dao<RestaurantChallenge, Integer> restaurantChallengeDao;
    private Dao<TextChallenge, Integer> textChallengeDao;
    private Dao<Ingredient, Integer> ingredientDao;
    private Dao<RecipeProperty, Integer> recipePropertyDao;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Ingredient.class);
            TableUtils.createTable(connectionSource, Recipe.class);
            TableUtils.createTable(connectionSource, RecipeProperty.class);
            TableUtils.createTable(connectionSource, Restaurant.class);
            TableUtils.createTable(connectionSource, RecipeChallenge.class);
            TableUtils.createTable(connectionSource, RestaurantChallenge.class);
            TableUtils.createTable(connectionSource, TextChallenge.class);
            TableUtils.createTable(connectionSource, RecipeCategory.class);
            TableUtils.createTable(connectionSource, RestaurantCategory.class);
            TableUtils.createTable(connectionSource, TextCategory.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Ingredient.class, true);
            TableUtils.dropTable(connectionSource, Recipe.class, true);
            TableUtils.dropTable(connectionSource, RecipeProperty.class, true);
            TableUtils.dropTable(connectionSource, Restaurant.class, true);
            TableUtils.dropTable(connectionSource, RecipeChallenge.class, true);
            TableUtils.dropTable(connectionSource, RestaurantChallenge.class, true);
            TableUtils.dropTable(connectionSource, TextChallenge.class, true);
            TableUtils.dropTable(connectionSource, RecipeCategory.class, true);
            TableUtils.dropTable(connectionSource, RestaurantCategory.class, true);
            TableUtils.dropTable(connectionSource, TextCategory.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<RecipeCategory, String> getRecipeCategories() throws SQLException {
        if (recipeCategoryDao == null) {
            recipeCategoryDao = getDao(RecipeCategory.class);
        }
        return recipeCategoryDao;
    }

    public Dao<RecipeChallenge, Integer> getRecipeChallenges() throws SQLException {
        if (recipeChallengeDao == null) {
            recipeChallengeDao = getDao(RecipeChallenge.class);
        }
        return recipeChallengeDao;
    }

    public Dao<RestaurantCategory, String> getRestaurantCategories() throws SQLException {
        if (restaurantCategoryDao == null) {
            restaurantCategoryDao = getDao(RestaurantCategory.class);
        }
        return restaurantCategoryDao;
    }

    public Dao<RestaurantChallenge, Integer> getRestaurantChallenges() throws SQLException {
        if (restaurantChallengeDao == null) {
            restaurantChallengeDao = getDao(RestaurantChallenge.class);
        }
        return restaurantChallengeDao;
    }

    public Dao<TextCategory, String> getTextCategories() throws SQLException {
        if (textCategoryDao == null) {
            textCategoryDao = getDao(TextCategory.class);
        }
        return textCategoryDao;
    }

    public Dao<TextChallenge, Integer> getTextChallenges() throws SQLException {
        if (textChallengeDao == null) {
            textChallengeDao = getDao(TextChallenge.class);
        }
        return textChallengeDao;
    }

    public Dao<Recipe, Integer> getRecipes() throws SQLException {
        if (recipeDao == null) {
            recipeDao = getDao(Recipe.class);
        }
        return recipeDao;
    }

    public Dao<Ingredient, Integer> getIngredients() throws SQLException {
        if (ingredientDao == null) {
            ingredientDao = getDao(Ingredient.class);
        }
        return ingredientDao;
    }

    public Dao<RecipeProperty, Integer> getRecipeProperties() throws SQLException {
        if (recipePropertyDao == null) {
            recipePropertyDao = getDao(RecipeProperty.class);
        }
        return recipePropertyDao;
    }

    public Dao<Restaurant, Integer> getRestaurants() throws SQLException {
        if (restaurantDao == null) {
            restaurantDao = getDao(Restaurant.class);
        }
        return restaurantDao;
    }
}
