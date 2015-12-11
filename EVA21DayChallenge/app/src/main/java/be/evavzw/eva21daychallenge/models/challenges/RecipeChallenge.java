package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.categories.Category;
import be.evavzw.eva21daychallenge.models.categories.RecipeCategory;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
@DatabaseTable(tableName = "recipe_challenges")
public class RecipeChallenge extends Challenge implements Serializable {
    @DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
    private Recipe recipe;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Category.ID_FIELD_NAME)
    private RecipeCategory category;

    @DatabaseField
    private int prepareFor;

    RecipeChallenge() //for ormlite
    {
    }

    public RecipeChallenge(RecipeCategory category, Recipe recipe) {
        this.category = category;
        this.recipe = recipe;
    }

    public RecipeChallenge(JSONObject jsonObject) throws Exception {
        super(jsonObject);
        if (jsonObject.has("Recipe"))
            recipe = new Recipe(jsonObject.getJSONObject("Recipe"));
        if (jsonObject.has("PrepareFor"))
            prepareFor = jsonObject.getInt("PrepareFor");
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeCategory getCategory() {
        return category;
    }
}
