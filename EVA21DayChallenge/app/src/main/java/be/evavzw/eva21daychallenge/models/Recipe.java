package be.evavzw.eva21daychallenge.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import be.evavzw.eva21daychallenge.models.challenges.*;

/**
 * Class that represents a Recipe
 */
@DatabaseTable(tableName = "recipes")
public class Recipe implements Serializable
{
    public static final String ID_FIELD = "recipeId";

    @DatabaseField(id = true, columnName = ID_FIELD)
    private int recipeId;

    @DatabaseField
    private String name, description, image;

    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = Challenge.ID_FIELD)
    private RecipeChallenge challenge;

    @ForeignCollectionField
    private Collection<Ingredient> ingredients;

    @ForeignCollectionField
    private Collection<RecipeProperty> properties;

    Recipe() //for ormlite
    {
    }

    /**
     * Let the class build itself with a given {@link JSONObject}
     * @param jsonObject content for the class and for {@link Ingredient} and {@link RecipeProperty}
     * @throws Exception
     */
    public Recipe(JSONObject jsonObject) throws Exception{
        ingredients = new ArrayList<>();
        properties = new ArrayList<>();
        parseJson(jsonObject);
    }

    /**
     * Parses the {@link JSONObject} into objects and properties
     * @param jsonObject content for this class and sub classes
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception{
        name = jsonObject.getString("Name");
        description = jsonObject.getString("Description");
        image = jsonObject.getString("Image");
        recipeId = jsonObject.getInt("RecipeId");

        JSONArray ingredientsArray = jsonObject.getJSONArray("Ingredients");

        for(int i = 0; i < ingredientsArray.length(); i++){
            JSONObject jsonRow = ingredientsArray.getJSONObject(i);
            ingredients.add(new Ingredient(this, jsonRow));
        }

        JSONArray propertiesArray = jsonObject.getJSONArray("Properties");

        for(int i = 0; i < propertiesArray.length(); i++){
            JSONObject jsonRow = propertiesArray.getJSONObject(i);
            properties.add(new RecipeProperty(this, jsonRow));
        }
    }

    public int getRecipeId() {
        return recipeId;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public Collection<RecipeProperty> getProperties() {
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

    public RecipeChallenge getChallenge()
    {
        return challenge;
    }

    public void setChallenge(RecipeChallenge challenge)
    {
        this.challenge = challenge;
    }
}
