package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Token;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;
import be.evavzw.eva21daychallenge.R;

/**
 * Rest method which handles login requests on our service (not third party)
 */
public class InternalLoginRestMethod extends AbstractRestMethod<Token> {
    private static final URI TOKENURI = URI.create("http://evavzwrest.azurewebsites.net/Token");
    private Context context;
    private String email;
    private String password;

    public InternalLoginRestMethod(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Overwrite the hook {@link AbstractRestMethod#requiresAuthorization()} as logging in obviously doesn't require authorization
     * @return indicator whether to authenticate the request
     */
    @Override
    protected boolean requiresAuthorization() {
        return false;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    public void setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Builds the {@link Request}
     * @return returns the built {@link Request}
     */
    @Override
    protected Request buildRequest() {
        try {
            if (email == null || password == null)
                throw new IllegalArgumentException("This shouldn't happen");

            //Make the Request and encode the headers
            Request r = new Request(RestMethodFactory.Method.POST, TOKENURI, null, ("grant_type=password&Username=" +URLEncoder.encode(email, "UTF-8") + "&Password=" + URLEncoder.encode(password, "UTF-8")).getBytes());
            r.addHeader("Content-Type", new ArrayList<>(Arrays.asList("application/x-www-form-urlencoded")));
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    /**
     * Parses the response body
     * @param responseBody JSON string returned by the server
     * @return {@link Token} obtained by server
     * @throws Exception
     */
    @Override
    protected Token parseResponseBody(String responseBody) throws Exception {
        JSONObject obj = new JSONObject(responseBody);
        if (obj.has("access_token"))
            return new Token(obj.getString("access_token"), "");
        else {
            throw new IllegalArgumentException("Authorization not complete!");
        }

    }

    /**
     * Handles any errors the server might throw for this request
     * @param status server response status
     * @param responseBody response message
     * @throws Exception
     */
    @Override
    protected void handleHttpStatus(int status, String responseBody) throws Exception {
        if(status == 400){
            try {
                JSONObject jsonObject = new JSONObject(responseBody);
                if(jsonObject.getString("error").equals("invalid_grant")){
                    String message = context.getResources().getString(R.string.UNamePwdIncorrect);
                    throw new IllegalArgumentException(message);
                }else if(jsonObject.get("error").equals("unsupported_grant_type")){
                    throw new Exception("Unsupported grant type, this shouldn't happen");
                }
            } catch (JSONException e) {
                throw new Exception("Couldn't convert JSON, this shouldn't happen either");
            }
        }
        if(status == 500){
            String message = context.getResources().getString(R.string.error500);
            throw new Exception(message);
        }
    }
}
