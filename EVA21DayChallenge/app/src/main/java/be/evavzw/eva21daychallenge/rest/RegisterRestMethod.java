package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;
import evavzw.be.eva21daychallenge.R;

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
    protected void handleHttpStatus(int status, String responseBody) throws Exception{
        if (status == 400) {
            try {
                JSONObject json = new JSONObject(responseBody);
                Map<String, List<String>> errors = new HashMap<>();
                if(json.has("ModelState")){
                    JSONObject modelState = json.getJSONObject("ModelState");
                    if(modelState.has("")){
                        String modelStateErrors = modelState.getJSONArray("").toString();
                        if(modelStateErrors.contains("is already taken")){
                            List<String> errorDetails = new ArrayList<>();
                            String emailTaken = context.getResources().getString(R.string.emailTaken);
                            errorDetails.add(emailTaken);
                            errors.put("email", errorDetails);
                        }if(modelStateErrors.contains("is invalid")){
                            List<String> errorDetails = new ArrayList<>();
                            String emailTaken = context.getResources().getString(R.string.emailInvalid);
                            errorDetails.add(emailTaken);
                            errors.put("email", errorDetails);
                        }
                    }
                    if(modelState.has("model.Password")){
                        List<String> errorDetails = new ArrayList<>();
                        errorDetails.add(modelState.getJSONArray("model.Password").get(0).toString());
                        errors.put("password", errorDetails);
                    }
                    if(modelState.has("model.ConfirmPassword")){
                        List<String> errorDetails = new ArrayList<>();
                        errorDetails.add(modelState.getString("model.ConfirmPassword"));
                        errors.put("confirmPassword", errorDetails);
                    }
                }
                if(errors.size() != 0){
                    throw new RegisterFailedException(errors);
                }
            } catch (JsonParseException ex) {
                String message = context.getResources().getString(R.string.couldntRetrieveErrors);
                throw new RegisterFailedException(message);
            }
        }
        if(status == 500){
            String message = context.getResources().getString(R.string.error500);
            throw new Exception(message);
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
            if (!p1.equals(p2)){
                Map<String, List<String>> errors = new HashMap<>();
                List<String> errorDetails = new ArrayList<>();
                String message = context.getResources().getString(R.string.passwordsNotEqual);
                errorDetails.add(message);
                errors.put("confirmPassword", errorDetails);
                throw new RegisterFailedException(errors);
            }

            RegisterRestMethod rrm = new RegisterRestMethod(context);
            rrm.email = s_email;
            rrm.password = p1;
            return rrm;
        }
    }
}
