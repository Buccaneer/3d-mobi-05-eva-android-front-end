package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 26/11/2015.
 */
public class GetRecipesByIngredients extends AbstractRestMethod<List<Recipe>> {

    private final URI REQUESTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Recipes/ByIngredient");
    private Context context;
    private List<Ingredient> ingredients;

    public GetRecipesByIngredients(Context context) {
        this.context = context;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try{
            String requestBody = buildRequestBody();

            Request request = new Request(RestMethodFactory.Method.POST, REQUESTURI, requestBody.getBytes());
            request.addHeader("Content-Type", Arrays.asList("application/json"));

            return request;
        }catch(Exception e){
            throw new IllegalArgumentException("Cannot build request see nested exception: ", e);
        }
    }

    @Override
    protected List<Recipe> parseResponseBody(String responseBody) throws Exception {
        JSONArray obj = new JSONArray(responseBody);
        List<Recipe> recipes = new ArrayList<>();

        //Let the objects themselves handle the JSON parsing
        for (int i = 0; i < obj.length(); i++) {
            JSONObject jsonRow = obj.getJSONObject(i);
            recipes.add(new Recipe(jsonRow));
        }
        return recipes;
    }

    private String buildRequestBody() throws JSONException {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(Ingredient ingredient : ingredients){
            jsonArray.put(ingredient.getName());
        }

        object.put("Values", jsonArray);

        return object.toString();
    }
}
