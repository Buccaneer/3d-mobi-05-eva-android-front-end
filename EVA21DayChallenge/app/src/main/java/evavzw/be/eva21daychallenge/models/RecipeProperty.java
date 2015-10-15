package evavzw.be.eva21daychallenge.models;

import org.json.JSONObject;

/**
 * Created by Jan on 15/10/2015.
 */
public class RecipeProperty {

    private String value, type;
    private int propertyId;

    public RecipeProperty(JSONObject jsonObject) throws Exception{
        parseJson(jsonObject);
    }

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
