package be.evavzw.eva21daychallenge.models.categories;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import be.evavzw.eva21daychallenge.models.challenges.*;

/**
 * Created by Pieter-Jan on 20/11/2015.
 */
@DatabaseTable(tableName = "restaurant_categories")
public class RestaurantCategory extends Category
{
    @ForeignCollectionField(columnName = FIELD_CHALLENGES, eager = true)
    private Collection<RestaurantChallenge> challenges;

    RestaurantCategory() //for ormlite
    {
    }

    public RestaurantCategory(String name)
    {
        super(name);
    }

    public Collection<RestaurantChallenge> getChallenges()
    {
        return challenges;
    }

    public void setChallenges(Collection<RestaurantChallenge> challenges)
    {
        this.challenges = challenges;
    }
}