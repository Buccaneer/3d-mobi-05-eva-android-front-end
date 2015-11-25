package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import com.bumptech.glide.util.ExceptionCatchingInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 25/11/2015.
 */
public class GetNumberOfRecipesByIngredients extends AbstractRestMethod<Integer> {

    private final URI REQUESTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Recipes/ByIngredient");
    private Context context;
    private ArrayList<Ingredient> ingredients;

    public GetNumberOfRecipesByIngredients(Context context){
        this.context = context;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try{
            String body = buildRequestBody();
            Request request = new Request(RestMethodFactory.Method.POST, REQUESTURI, body.getBytes());
            request.addHeader("Content-Type", Arrays.asList("application/json"));
            return request;
        }catch(Exception e){
            throw new IllegalArgumentException("Cannot build request see nested exception.", e);
        }
    }

    @Override
    protected Integer parseResponseBody(String responseBody) throws Exception {
        JSONArray jsonArray = new JSONArray(responseBody);

        return jsonArray.length();
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
