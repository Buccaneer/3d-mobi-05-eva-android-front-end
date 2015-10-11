package evavzw.be.eva21daychallenge.rest;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

import evavzw.be.eva21daychallenge.rest.framework.AbstractRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jasper De Vrient on 11/10/2015.
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

    @Override
    protected Request buildRequest() {
        try {
            JSONObject json = new JSONObject();

            json.put("Email", email);
         //   json.put("Token",token);
         //   json.put("Provider",provider);
            Request r= new Request(RestMethodFactory.Method.POST,REQURI, json.toString().getBytes());
            r.addHeader("Content-Type", Arrays.asList("application/json"));
            r.addHeader("Cookie",Arrays.asList(cookie));
            return r;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not make Request.");
        }
    }

    @Override
    protected Void parseResponseBody(String responseBody) throws Exception {
        return null;
    }

    @Override
    protected void handleHttpStatus(int status, String responseBody) {
        Log.wtf("Status",""+status);
    }

    public RegisterExternalRestMethod(Context context) {
        this.context = context;
    }
}
