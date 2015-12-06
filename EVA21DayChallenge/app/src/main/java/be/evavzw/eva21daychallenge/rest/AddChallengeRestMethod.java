package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 22/11/2015.
 */
public class AddChallengeRestMethod extends AbstractRestMethod {

    private final URI REQURESTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenge");
    private Context context;
    private String type;
    private int id;
    private List<Ingredient> ingredientsForCreativeCookingChallenge;

    public AddChallengeRestMethod(Context context) {
        this.context = context;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIngredientsForCreativeCookingChallenge(List<Ingredient> ingredientsForCreativeCookingChallenge) {
        this.ingredientsForCreativeCookingChallenge = ingredientsForCreativeCookingChallenge;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            switch (type) {
                case "Restaurant":
                    return buildNewRestaurantRequest();
                case "Recipe":
                    return buildNewRecipeRequest();
                case "Suikervrij":
                    return buildNewSuikervrijRequest();
                case "CreativeCooking":
                    return buildNewCreativeCookingRequest();
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to build request");
        }
    }

    private Request buildNewSuikervrijRequest() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("Type", type);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, REQURESTURI, header, body.toString().getBytes());
    }

    private Request buildNewRecipeRequest() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("Type", type);
        body.put("RecipeId", id);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, REQURESTURI, header, body.toString().getBytes());
    }


    private Request buildNewRestaurantRequest() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("Type", type);
        body.put("RestaurantId", id);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, REQURESTURI, header, body.toString().getBytes());
    }

    private Request buildNewCreativeCookingRequest() throws JSONException {
        JSONObject body = new JSONObject();
        JSONArray array = new JSONArray();
        body.put("Type", type);
        for(Ingredient ingredient : ingredientsForCreativeCookingChallenge){
            array.put(ingredient.getIngredientId());
        }
        body.put("IngredientsId", array);
        body.put("RecipeId", id);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, REQURESTURI, header, body.toString().getBytes());
    }

    @Override
    protected Object parseResponseBody(String responseBody) throws Exception {
        return null;
    }
}
