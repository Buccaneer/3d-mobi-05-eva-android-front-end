package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.User;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 17/11/2015.
 */
public class UpdateUserInfoRestMethod extends AbstractRestMethod{
    private URI USERINFOURI = URI.create("http://evavzwrest.azurewebsites.net/api/Account/UserInfo");
    private Context contex;
    private User user;

    public UpdateUserInfoRestMethod(Context context) {
        this.contex = context;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected Context getContext() {
        return contex;
    }

    @Override
    protected Request buildRequest() {
        if (user == null) {
            throw new IllegalArgumentException("User is empty");
        }

        try {
            String requestBody = buildRequestBody();
            Request r = new Request(RestMethodFactory.Method.POST, USERINFOURI, null, requestBody.getBytes());
            r.addHeader("Content-Type", new ArrayList<>(Arrays.asList("application/json")));
            return r;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot build request see nested exception", e);
        }
    }

    @Override
    protected Object parseResponseBody(String responseBody) throws Exception {
        return null;
    }

    private String buildRequestBody() throws JSONException {
        JSONObject body = new JSONObject();

        body.put("FirstName", user.getFirstName());
        body.put("LastName", user.getLastName());
        Date birthDay = user.getBirthDay();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        body.put("BirthDay", dateFormat.format(birthDay));
        body.put("Budget", user.getBudget());
        body.put("TypeOfVegan", user.getTypeOfVegan());
        JSONArray allergies = new JSONArray();
        ArrayList<Ingredient> all = user.getAllergies();
        if(all != null)
        for(Ingredient i: all){
            allergies.put(i.getIngredientId());
        }
        body.put("Allergies", allergies);
        body.put("PeopleInFamily", user.getPeopleInFamily());
        body.put("DoneSetup", true);

        return body.toString();
    }
}
