package be.evavzw.eva21daychallenge.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Properties that belong to a {@link Recipe}
 */
@DatabaseTable(tableName = "recipeproperties")
public class RecipeProperty implements Serializable
{
    @DatabaseField(id = true)
    private int propertyId;

    @DatabaseField
    private String value, type;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Recipe.ID_FIELD_NAME)
    private Recipe recipe;

    RecipeProperty() //for ormlite
    {
    }

    /**
     * Let the class build itself with a given {@link JSONObject}
     * @param jsonObject content for this class
     * @throws Exception
     */
    public RecipeProperty(Recipe recipe, JSONObject jsonObject) throws Exception{
        this.recipe = recipe;
        parseJson(jsonObject);
    }

    /**
     * Parses the JSON passed by {@link Recipe}
     * @param jsonObject JSON object with content for this class
     * @throws Exception
     */
    private void parseJson(JSONObject jsonObject) throws Exception{
        value = jsonObject.getString("Value");
        type = jsonObject.getString("Type");
        propertyId = jsonObject.getInt("PropertyId");
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
