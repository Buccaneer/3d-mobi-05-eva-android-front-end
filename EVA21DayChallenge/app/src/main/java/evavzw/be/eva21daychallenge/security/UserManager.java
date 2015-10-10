package evavzw.be.eva21daychallenge.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evavzw.be.eva21daychallenge.rest.InternalLoginRestMethod;
import evavzw.be.eva21daychallenge.rest.Request;
import evavzw.be.eva21daychallenge.rest.RestMethodResult;
import evavzw.be.eva21daychallenge.security.RequestSigner;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.MapUtils;
import org.scribe.utils.URLUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


/**
 * OAuthManager handles OAuth authentication with our REST API.
 *
 */
public class UserManager implements RequestSigner {
// Singleton van maken
    private static UserManager instance;
    private Context context;
    private final SharedPreferences prefs;


    private Token token;

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
        this.token = null;
    }

    public boolean isTokenPresent() {
        return token != null;
    }

    public boolean isTokenValid() {
        // Check if the present token is valid by requesting the userinfo if it fails then it is not valid otherwise valid.
        return true;
    }

    @Override
    public void authorize(Request request) {
        if (token == null)
            throw new IllegalArgumentException("Not yet signed in.");

        request.addHeader("Authorization", new ArrayList<String>(Arrays.asList(new String[]{"bearer " + token.getToken()})));
    }
}
