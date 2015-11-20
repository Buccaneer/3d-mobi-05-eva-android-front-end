package be.evavzw.eva21daychallenge.models.categories;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

import be.evavzw.eva21daychallenge.models.challenges.*;

/**
 * Created by Pieter-Jan on 20/11/2015.
 */
@DatabaseTable(tableName = "recipe_categories")
public class RecipeCategory extends Category implements Serializable
{
    @ForeignCollectionField(columnName = FIELD_CHALLENGES)
    private Collection<RecipeChallenge> challenges;

    RecipeCategory() //for ormlite
    {
    }

    public RecipeCategory(String name)
    {
        super(name);
    }

    public Collection<RecipeChallenge> getChallenges()
    {
        return challenges;
    }

    public void setChallenges(Collection<RecipeChallenge> challenges)
    {
        this.challenges = challenges;
    }
}
