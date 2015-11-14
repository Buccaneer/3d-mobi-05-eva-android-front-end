package be.evavzw.eva21daychallenge.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Ingredients for a {@link Recipe}
 */
@DatabaseTable(tableName = "ingredients")
public class Ingredient implements Serializable
{
    @DatabaseField(id = true)
    private int ingredientId;

    @DatabaseField
    private String name, unit;

    @DatabaseField
    private int quantity;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Recipe.ID_FIELD_NAME)
    private Recipe recipe;

    Ingredient() //for ormlite
    {
    }

    public Ingredient(Recipe recipe, int ingredientId, String name, String unit, int quantity)
    {
        this.recipe = recipe;
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    /**
     * Let the class build itself with a {@link JSONObject}
     *
     * @param jsonObject content for this class
     * @throws Exception
     */
    public Ingredient(Recipe recipe, JSONObject jsonObject) throws Exception
    {
        this.recipe = recipe;
        parseJson(jsonObject);
    }

    /**
     * Sets the content for this class
     *
     * @param jsonObject JSON passed by {@link Recipe}
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception
    {
        name = jsonObject.getJSONObject("Ingredient").getString("Name");
        unit = jsonObject.getJSONObject("Ingredient").getString("Unit");
        quantity = jsonObject.getInt("Quantity");
        ingredientId = jsonObject.getJSONObject("Ingredient").getInt("IngredientId");
    }

    public String getName()
    {
        return name;
    }

    public int getIngredientId()
    {
        return ingredientId;
    }

    public String getUnit()
    {
        return unit;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
