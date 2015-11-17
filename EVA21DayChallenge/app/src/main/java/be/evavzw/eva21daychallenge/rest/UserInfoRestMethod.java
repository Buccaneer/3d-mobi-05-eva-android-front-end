package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import be.evavzw.eva21daychallenge.models.User;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Rest method to retrieve User information from the server
 */
public class UserInfoRestMethod extends AbstractRestMethod<User> {
    private static final URI REQURI = URI.create("http://evavzwrest.azurewebsites.net/api/Account/UserInfo");
    private Context context;

    public UserInfoRestMethod(Context context) {
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    /**
     * Builds the {@link Request}
     *
     * @return returns the built {@link Request}
     */
    @Override
    protected Request buildRequest() {
        Request r = new Request(RestMethodFactory.Method.GET, REQURI, new byte[]{});
        return r;
    }

    /**
     * Parses the returned string from the request into their respective objects
     *
     * @param responseBody JSON string returned by the server
     * @return returns a {@link User} object
     * @throws Exception
     */
    @Override
    protected User parseResponseBody(String responseBody) throws Exception {
        try {
            JSONObject json = new JSONObject(responseBody);
            User user = new User();
            if (json.has("Email"))
                user.setEmail(json.getString("Email"));
            if (json.has("FirstName"))
                user.setFirstName(json.getString("FirstName"));
            if (json.has("LastName"))
                user.setLastName(json.getString("LastName"));
            if (json.has("BirthDay")) {
                String birthDay = json.getString("BirthDay");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                user.setBirthDay(format.parse(birthDay));
            }
            if (json.has("Budget"))
                user.setBudget(json.getString("Budget"));
            if (json.has("TypeOfVegan"))
                user.setTypeOfVegan(json.getString("TypeOfVegan"));
            if (json.has("Allergies"))
                user.setAllergies(fillAllergiesArray(json));
            if (json.has("PeopleInFamily"))
                user.setPeopleInFamily(json.getInt("PeopleInFamily"));
            if (json.has("DoneSetup"))
                user.setDoneSetup(json.getBoolean("DoneSetup"));

            user.setHasRegistered(json.getBoolean("HasRegistered"));

            return user;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not retrieve the user data");
        }
    }

    private int[] fillAllergiesArray(JSONObject json) throws JSONException {
        try {
            JSONArray array = json.getJSONArray("Allergies");
            int[] numbers = new int[array.length()];
            for (int i = 0; i < array.length(); i++) {
                numbers[i] = array.getJSONObject(i).getInt("IngredientId");
            }
            return numbers;
        } catch (JSONException e) {
            throw e;
        }
    }
}
