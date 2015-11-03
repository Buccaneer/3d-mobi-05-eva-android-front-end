package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Handles external registration on our service eg Facebook
 */
public class RegisterExternalRestMethod extends AbstractRestMethod<Void> {
    private static final URI REQURI = URI.create("http://evavzwrest.azurewebsites.net/api/Account/RegisterExternal");
    private String cookie;
    private Context context;
    private String email;

    public RegisterExternalRestMethod setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterExternalRestMethod setCookie(String cookie) {
        this.cookie = cookie;
        return this;
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
        try {
            JSONObject json = new JSONObject();

            json.put("Email", email);
            Request r = new Request(RestMethodFactory.Method.POST, REQURI, json.toString().getBytes());
            r.addHeader("Content-Type", Arrays.asList("application/json"));
            r.addHeader("Cookie", Arrays.asList(cookie));
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not make Request.");
        }
    }

    /**
     * There's no return for this request
     *
     * @param responseBody JSON string returned by the server
     * @return <code>Void</code>
     * @throws Exception
     */
    @Override
    protected Void parseResponseBody(String responseBody) throws Exception {
        return null;
    }

    public RegisterExternalRestMethod(Context context) {
        this.context = context;
    }
}
