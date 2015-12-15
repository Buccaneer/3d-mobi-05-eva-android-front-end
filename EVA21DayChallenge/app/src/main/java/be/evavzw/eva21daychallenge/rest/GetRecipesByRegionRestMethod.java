package be.evavzw.eva21daychallenge.rest;

import android.content.Context;
import android.util.Log;

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
 * Created by Jan on 13/12/2015.
 */
public class GetRecipesByRegionRestMethod extends AbstractRestMethod<List<Recipe>> {
    private final URI REQUESTURL = URI.create("http://evavzwrest.azurewebsites.net/api/Recipes/ByProperty");
    private Context context;
    private String region;

    public GetRecipesByRegionRestMethod(Context context) {
        this.context = context;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            JSONObject body = new JSONObject();
            JSONArray array = new JSONArray();
            array.put(region);
            body.put("Values", array);

            Request r = new Request(RestMethodFactory.Method.POST, REQUESTURL, body.toString().getBytes());
            r.addHeader("Content-Type", Arrays.asList("application/json"));

            return r;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to build request see nested exception.", e);
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
}
