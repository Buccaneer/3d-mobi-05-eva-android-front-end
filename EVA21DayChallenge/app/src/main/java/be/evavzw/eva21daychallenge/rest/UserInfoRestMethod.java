package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;
import be.evavzw.eva21daychallenge.models.User;
import evavzw.be.eva21daychallenge.R;

/**
 * Created by Jasper De Vrient on 11/10/2015.
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

    @Override
    protected Request buildRequest() {
        Request r = new Request(RestMethodFactory.Method.GET, REQURI, new byte[] {});
        r.addHeader("Accept-Language", Arrays.asList(locale));
        return r;
    }

    @Override
    protected User parseResponseBody(String responseBody) throws Exception {
        try {
            JSONObject json = new JSONObject(responseBody);
            User user = new User();
            if (json.has("Email"))
                user.setEmail(json.getString("Email"));
            user.setHasRegistered(json.getBoolean("HasRegistered"));

            return user;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not retrieve the user data");
        }
    }
}
