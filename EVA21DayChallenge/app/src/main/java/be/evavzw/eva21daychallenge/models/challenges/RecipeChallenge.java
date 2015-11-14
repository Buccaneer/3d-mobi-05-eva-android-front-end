package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import be.evavzw.eva21daychallenge.models.Category;
import be.evavzw.eva21daychallenge.models.Recipe;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class RecipeChallenge extends Challenge
{
    @DatabaseField
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
