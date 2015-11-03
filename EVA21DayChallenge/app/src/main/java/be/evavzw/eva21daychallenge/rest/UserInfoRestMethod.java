package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONObject;

import java.net.URI;

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
            user.setHasRegistered(json.getBoolean("HasRegistered"));

            return user;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not retrieve the user data");
        }
    }
}
