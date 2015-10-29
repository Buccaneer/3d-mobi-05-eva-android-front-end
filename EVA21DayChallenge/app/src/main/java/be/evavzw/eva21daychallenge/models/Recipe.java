package be.evavzw.eva21daychallenge.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan on 15/10/2015.
 */
public class Recipe {
    private List<Ingredient> ingredients;
    private String name;
    private String description;
    private String image;
    private int recipeId;
    private List<RecipeProperty> properties;

    public Recipe(JSONObject jsonObject) throws Exception{
        ingredients = new ArrayList<>();
        properties = new ArrayList<>();
        parseJson(jsonObject);
    }

    private void parseJson(JSONObject jsonObject) throws Exception{
        name = jsonObject.getString("Name");
        description = jsonObject.getString("Description");
        image = jsonObject.getString("Image");
        recipeId = jsonObject.getInt("RecipeId");

        JSONArray ingredientsArray = jsonObject.getJSONArray("Ingredients");

        for(int i = 0; i < ingredientsArray.length(); i++){
            JSONObject jsonRow = ingredientsArray.getJSONObject(i);
            ingredients.add(new Ingredient(jsonRow));
        }

        JSONArray propertiesArray = jsonObject.getJSONArray("Properties");

        for(int i = 0; i < propertiesArray.length(); i++){
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
