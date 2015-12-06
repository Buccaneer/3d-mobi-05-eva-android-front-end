package be.evavzw.eva21daychallenge.services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.rest.AddChallengeRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRecipesRestMethod;
import be.evavzw.eva21daychallenge.rest.GetNumberOfRecipesByIngredients;
import be.evavzw.eva21daychallenge.rest.GetRecipesByIngredients;

/**
 * Handles communication to retrieve {@link Recipe}s from the server
 */
public class RecipeManager {

    private static RecipeManager recipeManager;
    private Context context;

    /**
     * Singleton class
     *
     * @param context
     * @return instance of itself
     */
    public static RecipeManager getInstance(Context context) {
        if (recipeManager == null) {
            recipeManager = new RecipeManager(context);
        }

        return recipeManager;
    }

    private RecipeManager(Context context) {
        this.context = context;
    }

    /**
     * Method to get all {@link Recipe}s from the server
     *
     * @return a list of available {@link Recipe}s
     */
    public List<Recipe> getAllRecipes() {
        return new GetAllRecipesRestMethod(context).execute().getResource();
    }

    public void addChallenge(String type, int id) {
        AddChallengeRestMethod addChallengeRestMethod = new AddChallengeRestMethod(context);
        addChallengeRestMethod.setType(type);
        addChallengeRestMethod.setId(id);
        addChallengeRestMethod.execute();
    }

    public int getNumberOfRecipes(ArrayList<Ingredient> ingredients) {
        GetNumberOfRecipesByIngredients getNumberOfRecipesByIngredients = new GetNumberOfRecipesByIngredients(context);
        getNumberOfRecipesByIngredients.setIngredients(ingredients);
        return getNumberOfRecipesByIngredients.execute().getResource();
    }

    public List<Recipe> getRecipesByIngredients(List<Ingredient> ingredients) {
        GetRecipesByIngredients getRecipesByIngredients = new GetRecipesByIngredients(context);
        getRecipesByIngredients.setIngredients(ingredients);
        return getRecipesByIngredients.execute().getResource();
    }
}