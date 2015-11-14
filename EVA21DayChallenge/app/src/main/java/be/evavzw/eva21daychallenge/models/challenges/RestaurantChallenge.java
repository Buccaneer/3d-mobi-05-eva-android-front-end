package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import be.evavzw.eva21daychallenge.models.Category;
import be.evavzw.eva21daychallenge.models.Restaurant;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class RestaurantChallenge extends Challenge
{
    @DatabaseField
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
