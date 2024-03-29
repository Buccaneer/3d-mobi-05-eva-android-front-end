package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;

/**
 * Created by Annemie on 6/12/2015.
 */
public class CreativeCookingChallenge extends Challenge implements Serializable {

    @ForeignCollectionField(eager = true)
    private Collection<Ingredient> ingredientList;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
    private Recipe recipe;

    CreativeCookingChallenge() {
        ingredientList = new ArrayList<>();
    }

    public CreativeCookingChallenge(JSONObject jsonObject) throws Exception {
        super(jsonObject);
        ingredientList = new ArrayList<>();
        if (jsonObject.has("Recipe")) {
            recipe = new Recipe(jsonObject.getJSONObject("Recipe"));
        }
        if (jsonObject.has("Ingredients")) {
            JSONArray array = jsonObject.getJSONArray("Ingredients");
            for (int i = 0; i < array.length(); i++) {
                ingredientList.add(new Ingredient(recipe, array.getJSONObject(i)));
            }
        }
    }

    public Collection<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setIngredientList(Collection<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
