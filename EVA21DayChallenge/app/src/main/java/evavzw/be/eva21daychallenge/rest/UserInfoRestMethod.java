package evavzw.be.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONObject;

import java.net.URI;

import evavzw.be.eva21daychallenge.rest.framework.AbstractRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodFactory;
import evavzw.be.eva21daychallenge.models.User;

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
        return new Request(RestMethodFactory.Method.GET, REQURI, new byte[] {});
    }

    @Override
    protected void handleHttpStatus(int status, String responseBody) {
        if (status == 401) {
            throw new IllegalArgumentException("Not authorized");
        }
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
