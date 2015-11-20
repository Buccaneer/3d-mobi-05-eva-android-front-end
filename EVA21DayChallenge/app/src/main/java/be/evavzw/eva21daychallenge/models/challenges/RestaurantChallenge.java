package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import be.evavzw.eva21daychallenge.models.Category;
import be.evavzw.eva21daychallenge.models.Restaurant;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
@DatabaseTable(tableName = "restaurantchallenges")
public class RestaurantChallenge extends Challenge
{
    @DatabaseField(foreign = true)
    private Restaurant restaurant;

    RestaurantChallenge() //for ormlite
    {
    }

    public RestaurantChallenge(Category category, Restaurant restaurant)
    {
        super(category);
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }
}
