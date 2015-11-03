package be.evavzw.eva21daychallenge.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Recipe
 */
public class Recipe implements Serializable {
    private List<Ingredient> ingredients;
    private String name;
    private String description;
    private String image;
    private int recipeId;
    private List<RecipeProperty> properties;

    /**
     * Let the class build itself with a given {@link JSONObject}
     *
     * @param jsonObject content for the class and for {@link Ingredient} and {@link RecipeProperty}
     * @throws Exception
     */
    public Recipe(JSONObject jsonObject) throws Exception {
        ingredients = new ArrayList<>();
        properties = new ArrayList<>();
        parseJson(jsonObject);
    }

    /**
     * Parses the {@link JSONObject} into objects and properties
     *
     * @param jsonObject content for this class and sub classes
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception {
        name = jsonObject.getString("Name");
        description = jsonObject.getString("Description");
        image = jsonObject.getString("Image");
        recipeId = jsonObject.getInt("RecipeId");

        JSONArray ingredientsArray = jsonObject.getJSONArray("Ingredients");

        for (int i = 0; i < ingredientsArray.length(); i++) {
            JSONObject jsonRow = ingredientsArray.getJSONObject(i);
            ingredients.add(new Ingredient(jsonRow));
        }

        JSONArray propertiesArray = jsonObject.getJSONArray("Properties");

        for (int i = 0; i < propertiesArray.length(); i++) {
            JSONObject jsonRow = propertiesArray.getJSONObject(i);
            properties.add(new RecipeProperty(jsonRow));
        }
    }

    public int getRecipeId() {
        return recipeId;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeProperty> getProperties() {
        return properties;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
