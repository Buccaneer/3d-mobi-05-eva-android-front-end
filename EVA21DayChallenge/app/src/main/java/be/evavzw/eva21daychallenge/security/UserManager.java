package be.evavzw.eva21daychallenge.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.scribe.model.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import be.evavzw.eva21daychallenge.models.User;
import be.evavzw.eva21daychallenge.rest.ExternalLoginsRestMethod;
import be.evavzw.eva21daychallenge.rest.InternalLoginRestMethod;
import be.evavzw.eva21daychallenge.rest.RegisterExternalRestMethod;
import be.evavzw.eva21daychallenge.rest.RegisterRestMethod;
import be.evavzw.eva21daychallenge.rest.UserInfoRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodResult;


/**
 * UserManager handles OAuth authentication with our REST API.
 */
public class UserManager implements RequestSigner {

    private static UserManager instance;
    private final SharedPreferences prefs;
    private Context context;
    private Token token;

    /**
     * Private constructor for the singleton class {@link be.evavzw.eva21daychallenge.security.UserManager}.
     *
     * @param context the applicationcontext which the {@link be.evavzw.eva21daychallenge.security.UserManager#getInstance(Context)} receives
     */
    private UserManager(Context context) {
        // Fetches the shared preferences and assigns it to a variable
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.context = context;
    }

    /**
     * This class is a singleton.
     * It will create an instance of itself if one doesn't exist yet.
     * Also checks if our applicationcontext contains an access token and loads it in if it exists.
     *
     * @param context our applicationcontext which we use to determine the Accept-Language for requests and check for presence of existing access tokens.
     * @return returns an instance of itself
     */
    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
            if (instance.checkToken())
                instance.loadToken();
        }

        return instance;
    }

    /**
     * Generates a token and sets it to be used in authorization.
     *
     * @param email    email of the user logging in
     * @param password password of the user logging in
     */
    public void login(String email, String password) {
        // Make a new Rest Method which handles the login in our system (not third party)
        InternalLoginRestMethod ilrm = new InternalLoginRestMethod(context);
        ilrm.setCredentials(email, password);

        // Execute the method and fetch the result
        RestMethodResult<Token> result = ilrm.execute();

        // Save the token in our preferences
        login(result.getResource());
    }

    /**
     * Setter for our token and also calls {@link UserManager#saveToken()} to save or replace the token in preferences.
     *
     * @param token accesstoken retrieved from {@link UserManager#login(String email, String password)}
     */
    public void login(Token token) {
        this.token = token;
        saveToken();
    }

    /**
     * Register and authorizes a user.
     *
     * @param email           email of the user registering
     * @param password        password of the user registering
     * @param confirmPassword confirmation of <code>password</code>
     */
    public void register(String email, String password, String confirmPassword) {
        // Make a new Rest Method which handles the registering in our system (not third party)
        new RegisterRestMethod.Builder(context)
                .email(email)
                .password(password)
                .repeatPassword(confirmPassword)
                .build()
                .execute();

        // Log the user in automatically when the Register succeeded
        login(email, password);
    }

    /**
     * Saves the access token fetched by {@link UserManager#login(Token)}
     */
    private void saveToken() {
        // Instantiate the preferences editor
        SharedPreferences.Editor editor = prefs.edit();

        // Removes a token if we're logging a user out or put a token in the preferences if he's logging in
        if (token == null) {
            editor.remove("loginAccessToken");
        } else {
            editor.putString("loginAccessToken", token.getToken());
        }

        // Save our changes
        editor.commit();
    }

    /**
     * Checks if a token is present
     * @return boolean to indicate whether we already have a token saved for the user
     */
    private boolean checkToken() {
        return prefs.contains("loginAccessToken");
    }

    /**
     * Fetches the access token from the preferences if present and loads it into our class
     */
    private void loadToken() {
        String t = prefs.getString("loginAccessToken", "");
        if (!t.equals(""))
            token = new Token(t, "");
    }

    /**
     * Invalidates the token on the device, used for logging the user out
     */
    public void invalidateToken() {
        login(null);
    }

    /**
     * Checks if we have set the token in our class
     * @return boolean to indicate whether we have a token loaded in our class
     */
    public boolean isTokenPresent() {
        return token != null;
    }

    /**
     * Check if the present token is valid by requesting the User Info, if it fails then it is not valid otherwise valid
     * @return boolean which indicates if the token is valid or not
     */
    public boolean isTokenValid() {
        return getUser() != null;
    }

    /**
     * Authorizes a request to the server
     * @param request Request going to the server
     */
    @Override
    public void authorize(Request request) {
        // This shouldn't happen, a user can't log in without a token, but check for it anyway
        if (token == null)
            throw new IllegalArgumentException("Not yet signed in.");

        // Add a bearer token to the Authorization header
        request.addHeader("Authorization", new ArrayList<String>(Arrays.asList(new String[]{"bearer " + token.getToken()})));
    }

    /**
     * Gets the User Info of the user currently logged in in the system
     * @return returns a {@link User}
     */
    public User getUser() {
        // Make a new Rest Method which gets the User Info from the server, converted into a User object
        return new UserInfoRestMethod(context).execute().getResource();
    }

    /**
     * Gets links for the external login providers (Facebook, Twitter, Google)
     * @return returns a Map for all of the external providers registered on our server
     */
    public Map<String, String> getExternalLoginProviders() {
        // Make a new Rest Method which gets the external login provider links from the server
        return new ExternalLoginsRestMethod(context).execute().getResource();
    }

    /**
     * Method for login through Facebook, Twitter, Google, ...
     * @param token access token retrieved from the URL callback of the third party services
     * @param cookie cookie generated by the redirect request
     */
    public void registerExternal(String token, String cookie) {
        // Save the token in our system
        login(new Token(token, ""));

        // Get a User object, we don't need to add an authorization header because this will be called automatically by the AbstractRestMethod template
        User u = getUser();
        if (!u.isRegistered()) {
            // Make a new Rest Method which handles the registering of external users
            new RegisterExternalRestMethod(context).setEmail(u.getEmail()).setCookie(cookie).execute();
            u = getUser();

            // For some reason the account doesn't get confirmed if it's the first time registering, so call it again if it's not confirmed
            if (!u.isRegistered()) {
                new RegisterExternalRestMethod(context).setEmail(u.getEmail()).setCookie(cookie).execute();
            }
        }
    }
}