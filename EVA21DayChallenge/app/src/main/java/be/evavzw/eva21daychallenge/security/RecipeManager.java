package be.evavzw.eva21daychallenge.security;

import android.content.Context;

import java.util.List;

import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.rest.AddChallengeRestMethod;
import be.evavzw.eva21daychallenge.rest.GetAllRecipesRestMethod;

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
}