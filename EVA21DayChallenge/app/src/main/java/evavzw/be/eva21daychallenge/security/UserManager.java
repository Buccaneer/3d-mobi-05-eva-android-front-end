package evavzw.be.eva21daychallenge.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import evavzw.be.eva21daychallenge.rest.ExternalLoginsRestMethod;
import evavzw.be.eva21daychallenge.rest.InternalLoginRestMethod;
import evavzw.be.eva21daychallenge.rest.RegisterExternalRestMethod;
import evavzw.be.eva21daychallenge.rest.RegisterRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodResult;
import evavzw.be.eva21daychallenge.rest.UserInfoRestMethod;
import evavzw.be.eva21daychallenge.models.User;

import org.scribe.model.Token;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * OAuthManager handles OAuth authentication with our REST API.
 *
 */
public class UserManager implements RequestSigner {

    private static UserManager instance;
    private Context context;
    private final SharedPreferences prefs;
    private Token token;

    // Singleton van maken
    public static UserManager getInstance(Context context) {
        if (instance ==null) {
            instance = new UserManager(context);
            if (instance.checkToken())
                instance.loadToken();
        }

        return instance;
    }

    private UserManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.context = context;
    }

    /**
     * Generates a token and sets it to be used in authorisation.
     * @param email
     * @param password
     */
    public void login(String email, String password) {

        InternalLoginRestMethod ilrm = new InternalLoginRestMethod(context);
        ilrm.setCredentails(email, password);

        RestMethodResult<Token> result =  ilrm.execute();

        if (result.getStatusCode() != 200)
            throw new IllegalArgumentException(String.valueOf(result.getStatusCode()));


        login(result.getResource());
    }

    /**
     * Authorises with a token.
     * @param token
     */
    public void login(Token token) {
        this.token  = token;
        saveToken();
    }

    /**
     * Register and authorizes an user.
     * @param email
     * @param password
     * @param confirmPassword
     */
    public void register(String email, String password, String confirmPassword) {
         new RegisterRestMethod.Builder(context)
                .email(email)
                .password(password)
                .repeatPassword(confirmPassword)
                .build()
                .execute();

        login(email, password);
    }


    private void saveToken() {
        SharedPreferences.Editor editor = prefs.edit();

        if (token == null) {
            editor.remove("loginAccessToken");
        } else {
            editor.putString("loginAccessToken", token.getToken());
        }

        editor.commit();
    }

    private boolean checkToken() {


        return prefs.contains("loginAccessToken");

    }

    private void loadToken() {
        String t = prefs.getString("loginAccessToken","");
        if (!t.equals(""))
            token = new Token(t,"");
    }

    public void invalidateToken() {
        login(null);
    }

    public boolean isTokenPresent() {
        return token != null;
    }

    public boolean isTokenValid() {
        // Check if the present token is valid by requesting the userinfo if it fails then it is not valid otherwise valid.
       return getUser() != null;
    }

    @Override
    public void authorize(Request request) {
        if (token == null)
            throw new IllegalArgumentException("Not yet signed in.");

        request.addHeader("Authorization", new ArrayList<String>(Arrays.asList(new String[]{"bearer " + token.getToken()})));
    }

    public User getUser() {
        return new UserInfoRestMethod(context).execute().getResource();
    }

    public Map<String, String> getExternalLoginProviders() {
        return new ExternalLoginsRestMethod(context).execute().getResource();
    }

    public void registerExternal(String token, String cookie) {
        login(new Token(token,""));

        User  u = getUser();
        new RegisterExternalRestMethod(context).setEmail(u.getEmail()).setCookie(cookie).execute();
        u = getUser();
        if (!u.isRegistered()) {
            new RegisterExternalRestMethod(context).setEmail(u.getEmail()).setCookie(cookie).execute();
        }
    }
}