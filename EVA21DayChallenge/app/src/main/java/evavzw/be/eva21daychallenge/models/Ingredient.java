package evavzw.be.eva21daychallenge.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Jan on 15/10/2015.
 */
public class Ingredient implements Serializable
{

    private String name, unit;
    private int ingredientId, quantity;

    public Ingredient(int ingredientId, String name, String unit, int quantity)
    {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public Ingredient(JSONObject jsonObject) throws Exception{
        parseJson(jsonObject);
    }

    private void parseJson(JSONObject jsonObject) throws Exception{
        name = jsonObject.getJSONObject("Ingredient").getString("Name");
        unit = jsonObject.getJSONObject("Ingredient").getString("Unit");
        quantity = jsonObject.getInt("Quantity");
        ingredientId = jsonObject.getJSONObject("Ingredient").getInt("IngredientId");
    }

    public String getName() {
        return name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }
}
