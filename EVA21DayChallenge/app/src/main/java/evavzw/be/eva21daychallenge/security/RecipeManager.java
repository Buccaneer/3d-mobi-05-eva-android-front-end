package evavzw.be.eva21daychallenge.security;

import android.content.Context;

import java.util.List;

import evavzw.be.eva21daychallenge.models.Recipe;
import evavzw.be.eva21daychallenge.rest.GetAllRecipesRestMethod;

/**
 * Created by Jan on 15/10/2015.
 */
public class RecipeManager {

    private static RecipeManager recipeManager;
    private Context context;

    public static RecipeManager getInstance(Context context){
        if(recipeManager == null){
            recipeManager = new RecipeManager(context);
        }

        return recipeManager;
    }

    private RecipeManager(Context context){
        this.context=context;
    }

    public List<Recipe> getAllRecipes(){
        return new GetAllRecipesRestMethod(context).execute().getResource();
    }
}
