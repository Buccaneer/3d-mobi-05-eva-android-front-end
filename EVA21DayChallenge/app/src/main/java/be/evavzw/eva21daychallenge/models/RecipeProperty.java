package be.evavzw.eva21daychallenge.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Properties that belong to a {@link Recipe}
 */
public class RecipeProperty implements Serializable
{

    private String value, type;
    private int propertyId;

    /**
     * Let the class build itself with a given {@link JSONObject}
     * @param jsonObject content for this class
     * @throws Exception
     */
    public RecipeProperty(JSONObject jsonObject) throws Exception{
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
