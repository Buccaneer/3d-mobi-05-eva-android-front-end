package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 15/11/2015.
 */
public class GetIngredientsByNameRestMethod extends AbstractRestMethod<List<Ingredient>> {
    private Context context;
    private String name;

    public GetIngredientsByNameRestMethod(Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            URI ingredientURI = URI.create("http://evavzwrest.azurewebsites.net/api/Ingredient?name=" + name);
            Request r = new Request(RestMethodFactory.Method.GET, ingredientURI, null, null);
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }

    }

    @Override
    protected List<Ingredient> parseResponseBody(String responseBody) throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        JSONArray ingredientsArray = new JSONArray(responseBody);
        for(int i = 0; i < ingredientsArray.length(); i++){
            JSONObject ingredient = ingredientsArray.getJSONObject(i);
            int ingredientId = ingredient.getInt("IngredientId");
            String name = ingredient.getString("Name");
            Ingredient ingr = new Ingredient(null, ingredientId, name, "", "");
            ingredients.add(ingr);
        }
        return ingredients;
    }
}
