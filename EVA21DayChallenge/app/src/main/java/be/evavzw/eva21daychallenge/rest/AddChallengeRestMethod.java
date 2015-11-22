package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 22/11/2015.
 */
public class AddChallengeRestMethod extends AbstractRestMethod {

    private Context context;
    private String type;
    private int id;

    public AddChallengeRestMethod(Context context) {
        this.context = context;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
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
                case "Suikervrij":
                    return buildSuikerVrijRequest();
                case "":
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to build request");
        }
    }

    private Request buildSuikerVrijRequest() throws JSONException {
        URI requestURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenge");
        JSONObject body = new JSONObject();
        body.put("Type", type);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, requestURI, header, body.toString().getBytes());
    }

    private Request buildNewRestaurantRequest() throws JSONException {
        URI requestURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenge");
        JSONObject body = new JSONObject();
        body.put("Type", type);
        body.put("RestaurantId", id);
        Map<String, List<String>> header = new HashMap<>();
        header.put("Content-Type", Arrays.asList("application/json"));
        return new Request(RestMethodFactory.Method.PUT, requestURI, header, body.toString().getBytes());
    }

    @Override
    protected Object parseResponseBody(String responseBody) throws Exception {
        return null;
    }
}
