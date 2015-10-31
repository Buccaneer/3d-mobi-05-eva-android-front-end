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
import evavzw.be.eva21daychallenge.R;

/**
 * Created by Jan on 15/10/2015.
 */
public class GetAllRecipesRestMethod extends AbstractRestMethod<List<Recipe>> {


    private static final URI RECIPEURI = URI.create("http://evavzwrest.azurewebsites.net/api/Recipes");
    private Context context;

    public GetAllRecipesRestMethod(Context context){
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            Request r = new Request(RestMethodFactory.Method.GET, RECIPEURI, null, null);
            r.addHeader("Accept-Language", Arrays.asList(locale));
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    @Override
    protected List<Recipe> parseResponseBody(String responseBody) throws Exception {
        JSONArray obj = new JSONArray(responseBody);
        List<Recipe> recipes = new ArrayList<>();

        //voor elk object in de json (dus elk recept) de constructor van recept aanroepen met json stukje
        for(int i = 0; i < obj.length(); i++){
            JSONObject jsonRow = obj.getJSONObject(i);
            recipes.add(new Recipe(jsonRow));
        }

        return recipes;
    }
}
