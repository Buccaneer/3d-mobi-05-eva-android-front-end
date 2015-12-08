package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.evavzw.eva21daychallenge.activity.challenges.ChallengeFragment;
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

    CreativeCookingChallenge(){
        ingredientList = new ArrayList<>();
    }

    public CreativeCookingChallenge(JSONObject object) throws Exception{
        super(object);
        ingredientList = new ArrayList<>();
        if(object.has("Recipe")){
            recipe = new Recipe(object.getJSONObject("Recipe"));
        }
        if(object.has("Ingredients")){
            JSONArray array = object.getJSONArray("Ingredients");
            for(int i = 0; i < array.length(); i++){
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
