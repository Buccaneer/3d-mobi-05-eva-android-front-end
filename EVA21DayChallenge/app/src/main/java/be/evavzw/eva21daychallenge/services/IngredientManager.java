package be.evavzw.eva21daychallenge.services;

import android.content.Context;

import java.util.List;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.rest.GetIngredientsByNameRestMethod;

/**
 * Created by Jan on 15/11/2015.
 */
public class IngredientManager {

    private static IngredientManager ingredientManager;
    private Context context;

    /**
     * Singleton class
     *
     * @param context
     * @return instance of itself
     */
    public static IngredientManager getInstance(Context context) {
        if (ingredientManager == null) {
            ingredientManager = new IngredientManager(context);
        }

        return ingredientManager;
    }

    private IngredientManager(Context context) {
        this.context = context;
    }

    public List<Ingredient> getIngredientsByName(String name){
        GetIngredientsByNameRestMethod getIngredientsByNameRestMethod = new GetIngredientsByNameRestMethod(context);
        getIngredientsByNameRestMethod.setName(name);
        return getIngredientsByNameRestMethod.execute().getResource();
    }
}
