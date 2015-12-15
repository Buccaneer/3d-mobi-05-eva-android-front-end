package be.evavzw.eva21daychallenge.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Ingredients for a {@link Recipe}
 */
@DatabaseTable(tableName = "ingredients")
public class Ingredient implements Serializable {
    @DatabaseField(id = true)
    private int ingredientId;

    @DatabaseField
    private String name = "", prefix = "", postfix = "";

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Recipe.ID_FIELD)
    private Recipe recipe;

    Ingredient() //for ormlite
    {
    }

    public Ingredient(Recipe recipe, int ingredientId, String name, String prefix, String postfix) {
        this.recipe = recipe;
        this.ingredientId = ingredientId;
        this.name = name;
        this.prefix = prefix;
        this.postfix = postfix;
    }

    /**
     * Let the class build itself with a {@link JSONObject}
     *
     * @param jsonObject content for this class
     * @throws Exception
     */
    public Ingredient(Recipe recipe, JSONObject jsonObject) throws Exception {
        this.recipe = recipe;
        parseJson(jsonObject);
    }

    /**
     * Sets the content for this class
     *
     * @param jsonObject JSON passed by {@link Recipe}
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception {
        if (jsonObject.has("Ingredient")) {
            ingredientId = jsonObject.getJSONObject("Ingredient").getInt("IngredientId");
            name = jsonObject.getJSONObject("Ingredient").getString("Name");
        }
        if (jsonObject.has("Prefix"))
            prefix = jsonObject.getString("Prefix");
        if (jsonObject.has("Postfix"))
            postfix = jsonObject.getString("Postfix");
    }

    public String getName() {
        return name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setName(String name) {
        this.name = name;
    }

}
