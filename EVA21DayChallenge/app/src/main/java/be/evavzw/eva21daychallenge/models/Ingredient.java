package be.evavzw.eva21daychallenge.models;

import org.json.JSONObject;

/**
 * Ingredients for a {@link Recipe}
 */
public class Ingredient {

    private String name, unit;
    private int ingredientId, quantity;

    public Ingredient(){}

    public Ingredient(int ingredientId, String name, String unit, int quantity) {
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
    public Ingredient(JSONObject jsonObject) throws Exception {
        parseJson(jsonObject);
    }

    /**
     * Sets the content for this class
     *
     * @param jsonObject JSON passed by {@link Recipe}
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception {
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

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
