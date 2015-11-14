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
    private Dao<Challenge, Integer> challengeDao;

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

    public Dao<Challenge, Integer> getChallenges() throws SQLException {
        if (challengeDao == null) {
            challengeDao = getDao(Challenge.class);
        }
        return challengeDao;
    }

    public Dao<Recipe, Integer> getRecipes() throws SQLException {
        if (recipeDao == null) {
            recipeDao = getDao(Recipe.class);
        }
        return recipeDao;
    }

    public Dao<Restaurant, Integer> getRestaurants() throws SQLException {
        if (restaurantDao == null) {
            restaurantDao = getDao(Restaurant.class);
        }
        return restaurantDao;
    }

}
