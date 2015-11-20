package be.evavzw.eva21daychallenge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import be.evavzw.eva21daychallenge.models.Category;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.RecipeProperty;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.models.challenges.RecipeChallenge;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "evavzw.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Recipe, Integer> recipeDao;
    private Dao<Restaurant, Integer> restaurantDao;
    private Dao<Category, String> categoryDao;
    private Dao<RecipeChallenge, Integer> recipeChallengeDao;
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
            TableUtils.createTable(connectionSource, Challenge.class);
            TableUtils.createTable(connectionSource, Category.class);
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
            TableUtils.dropTable(connectionSource, Ingredient.class, true);
            TableUtils.dropTable(connectionSource, Recipe.class, true);
            TableUtils.dropTable(connectionSource, RecipeProperty.class, true);
            TableUtils.dropTable(connectionSource, Challenge.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Category, String> getCategories() throws SQLException {
        if (categoryDao == null) {
            categoryDao = getDao(Category.class);
        }
        return categoryDao;
    }

    public Dao<RecipeChallenge, Integer> getRecipeChallenges() throws SQLException {
        if (recipeChallengeDao == null) {
            recipeChallengeDao = getDao(RecipeChallenge.class);
        }
        return recipeChallengeDao;
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
