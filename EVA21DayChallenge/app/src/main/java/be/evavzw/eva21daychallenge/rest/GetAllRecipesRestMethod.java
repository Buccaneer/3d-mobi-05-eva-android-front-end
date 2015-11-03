package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Rest method that returns all recipes from the server
 */
public class GetAllRecipesRestMethod extends AbstractRestMethod<List<Recipe>> {
    private static final URI RECIPEURI = URI.create("http://evavzwrest.azurewebsites.net/api/Recipes");
    private Context context;

    public GetAllRecipesRestMethod(Context context) {
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    /**
     * Builds the {@link Request}
     *
     * @return returns the {@link Request}
     */
    @Override
    protected Request buildRequest() {
        try {
            Request r = new Request(RestMethodFactory.Method.GET, RECIPEURI, null, null);
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    /**
     * Parses the http response body into their respective objects (recipes)
     *
     * @param responseBody JSON string returned by the server
     * @return returns a list of available {@link Recipe}s
     * @throws Exception
     */
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
}
