package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import be.evavzw.eva21daychallenge.models.Category;
import be.evavzw.eva21daychallenge.models.Recipe;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
@DatabaseTable(tableName = "recipechallenges")
public class RecipeChallenge extends Challenge
{
    @DatabaseField(foreign = true)
    private Recipe recipe;

    RecipeChallenge() //for ormlite
    {
    }

    public RecipeChallenge(Category category, Recipe recipe)
    {
        super(category);
        this.recipe = recipe;
    }

    public Recipe getRecipe()
    {
        return recipe;
    }
}
