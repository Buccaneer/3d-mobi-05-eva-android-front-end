package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.models.challenges.RecipeChallenge;
import be.evavzw.eva21daychallenge.models.challenges.RestaurantChallenge;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.Response;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodResult;

/**
 * Rest method that returns all recipes from the server
 */
public class GetCurrentChallengeRestMethod extends AbstractRestMethod<Challenge> {
    private static final URI CHALLENGEURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenges/ForToday");
    private Context context;

    public GetCurrentChallengeRestMethod(Context context) {
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
            Request r = new Request(RestMethodFactory.Method.GET, CHALLENGEURI, null, null);
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    @Override
    protected RestMethodResult<Challenge> buildResult(Response response) {
        int status = response.status;
        String statusMsg = "";
        String responseBody = null;
        Challenge resource = null;

        try {
            // Parses the bytes to a string containing JSON
            responseBody = new String(response.body, getCharacterEncoding(response.headers));
            // If the reponse status starts with a 2, eg 200, 201, ... the request succeeded, else it failed
            if (response.status / 100 == 2 || response.status == 400)
                resource = parseResponseBody(responseBody);
            else
                handleHttpStatus(response.status, responseBody);
        } catch (RegisterFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return new RestMethodResult<>(status, statusMsg, resource);
    }

    /**
     * Parses the http response body into their respective objects (recipes)
     *
     * @param responseBody JSON string returned by the server
     * @return returns a list of available {@link Recipe}s
     * @throws Exception
     */
    @Override
    protected Challenge parseResponseBody(String responseBody) throws Exception
    {
        JSONObject obj = new JSONObject(responseBody);
        if (obj.optString("Message", null) != null) return null;
        String type = obj.getString("Type");
        if (type.equals("Recipe")) {
            return new RecipeChallenge(obj);
        }
        if (type.equals("Restaurant")) {
            return new RestaurantChallenge(obj);
        }
        // TODO: ensure challenges other than Recipe work as well
        if (type.equals("Suikervrij")) {
            throw new UnsupportedOperationException("TODO");
        }
        if (type.equals("CreativeCooking")) {
            throw new UnsupportedOperationException("TODO");
        }
        return null;
    }
}
