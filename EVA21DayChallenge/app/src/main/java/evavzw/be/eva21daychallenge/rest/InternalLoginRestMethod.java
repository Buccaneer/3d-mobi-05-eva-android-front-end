package evavzw.be.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONObject;
import org.scribe.model.Token;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import evavzw.be.eva21daychallenge.rest.framework.AbstractRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jan on 10/10/2015.
 */
public class InternalLoginRestMethod extends AbstractRestMethod<Token> {
    private static final URI TOKENURI = URI.create("http://evavzwrest.azurewebsites.net/Token");
    private Context applicatonContext;
    private String email;
    private String password;


    @Override
    protected boolean requiresAuthorization() {
        return false;
    }

    public InternalLoginRestMethod(Context applicatonContext) {
        this.applicatonContext = applicatonContext.getApplicationContext();
    }

    public InternalLoginRestMethod(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    protected Context getContext() {
        return applicatonContext;
    }

    public void setCredentails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    protected Request buildRequest() {
        try {
            if (email == null || password == null)
                throw new IllegalArgumentException("WEll this didn't work...");

            Request r = new Request(RestMethodFactory.Method.POST, TOKENURI, null, ("grant_type=password&Username=" +URLEncoder.encode(email, "UTF-8") + "&Password=" + URLEncoder.encode(password, "UTF-8")).getBytes());
            r.addHeader("Content-Type", new ArrayList<>(Arrays.asList("application/x-www-form-urlencoded")));
            r.addHeader("Accept-Language", Arrays.asList(locale));
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot build request see nested exception.", ex);
        }
    }

    @Override
    protected Token parseResponseBody(String responseBody) throws Exception {
        JSONObject obj = new JSONObject(responseBody);

        if (obj.has("access_token"))
            return new Token(obj.getString("access_token"), "");
        else {
            throw new IllegalArgumentException("Authorization not complete!");
        }

    }

    @Override
    protected void handleHttpStatus(int status, String responseBody) {
        if (status == 400) {
            throw new IllegalArgumentException("Wrong credentials.");
        }
    }
}
