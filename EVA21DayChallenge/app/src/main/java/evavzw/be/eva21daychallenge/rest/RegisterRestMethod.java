package evavzw.be.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evavzw.be.eva21daychallenge.rest.framework.AbstractRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jasper De Vrient on 11/10/2015.
 */
public class RegisterRestMethod extends AbstractRestMethod<Void> {
    private Context context;
    private String email, password;
    private static final URI REQURL = URI.create("http://evavzwrest.azurewebsites.net/api/Account/Register");
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try {
            JSONObject json = new JSONObject();

            json.put("Email", email);
            json.put("Password", password);
            json.put("ConfirmPassword", password);

            Request r = new Request(RestMethodFactory.Method.POST,REQURL,json.toString().getBytes());
            r.addHeader("Content-Type", Arrays.asList("application/json"));
            r.addHeader("Accept-Language", Arrays.asList(locale));
            return r;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not build request");
        }

    }

    @Override
    protected Void parseResponseBody(String responseBody) throws Exception {
        return null;
    }

    @Override
    protected void handleHttpStatus(int status, String responseBody) {
        if (status == 400) {
            try {
                JSONObject json = new JSONObject(responseBody);
                List<String> messages = new ArrayList<>();

                JSONObject modelState = json.getJSONObject("ModelState");
                JSONArray errs = modelState.getJSONArray("");

                for (int i = 0; i < errs.length(); ++i)
                    messages.add(errs.getString(i));

                throw new RegisterFailedException("Register failed", messages);
            } catch (Exception ex) {
                throw new RegisterFailedException("Register failed, failed to load error messages");
            }
        }
    }

    @Override
    protected boolean requiresAuthorization() {
        return false;
    }

    private RegisterRestMethod(Context context) {
        this.context = context;
    }

    public static class Builder {
        private Context context;
        private String s_email;
        private String p1,p2;
        public Builder(Context context) {
            this.context = context;
        }

        public Builder email(String email) {
            s_email = email;
            return this;
        }

        public Builder password(String password) {
            p1 = password;
            return this;
        }

        public Builder repeatPassword(String password) {
            p2 = password;
            return this;
        }

        public RegisterRestMethod build() {
            if (!p1.equals(p2))
                throw new IllegalArgumentException("Passwords are not equal.");

            RegisterRestMethod rrm = new RegisterRestMethod(context);
            rrm.email = s_email;
            rrm.password = p1;
            return rrm;
        }
    }
}
