package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

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
        if (obj.getString("Message") != null) return null;
        return new Challenge(obj);
    }
}
