package be.evavzw.eva21daychallenge.rest.framework;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.security.RequestSigner;
import be.evavzw.eva21daychallenge.security.UserManager;

/**
 * Abstract class which has a template method for executing a request.
 * Also sets the Locale for every request, builds the Request result and handles any http errors which weren't fetched in sub classes.
 *
 * @param <T> Type of object the Rest Method should return
 */
public abstract class AbstractRestMethod<T> implements RestMethod<T> {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private String locale;

    /**
     * Template method for executing a Rest Method
     *
     * @return returns a {@link RestMethodResult} of a certain type
     */
    public RestMethodResult<T> execute() {
        // Fetch the current locale of the system
        setLocale();

        // Let our sub class build a request
        Request request = buildRequest();

        // Checks if the request needs authorization, this is true by standard but it's a hook subclasses can overwrite
        if (requiresAuthorization()) {
            // Get an instance of the UserManager and let it sign our request
            RequestSigner signer = UserManager.getInstance(getContext());
            signer.authorize(request);
        }

        // Add the Accept Language header
        request.addHeader("Accept-Language", Arrays.asList(locale));

        // Gets the response after executing the request
        Response response = doRequest(request);

        // Build the result and return the RestMethodResult<T>
        return buildResult(response);
    }

    /**
     * Fetches the current locale of our system.
     * This is called for every request since the system language could be changed at runtime.
     */
    private void setLocale() {
        // Get the locale
        Locale locale = getContext().getResources().getConfiguration().locale;

        // Get the abbreviation of the language
        String lang = locale.getLanguage();
        switch (lang) {
            case "nl":
            default:
                this.locale = "nl-BE";
                return;
            case "en":
                this.locale = "en-GB";
                return;
            case "fr":
                this.locale = "fr-FR";
                return;
        }
    }

    protected abstract Context getContext();

    /**
     * Subclasses can overwrite for full control, eg. need to do special
     * inspection of response headers, etc.
     *
     * @param response response from the server
     * @return returns {@link RestMethodResult} (parsed server response)
     */
    protected RestMethodResult<T> buildResult(Response response) {
        int status = response.status;
        String statusMsg = "";
        String responseBody = null;
        T resource = null;

        try {
            // Parses the bytes to a string containing JSON
            responseBody = new String(response.body, getCharacterEncoding(response.headers));
            // If the reponse status starts with a 2, eg 200, 201, ... the request succeeded, else it failed
            if (response.status / 100 == 2)
                resource = parseResponseBody(responseBody);
            else
                handleHttpStatus(response.status, responseBody);
        } catch (RegisterFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return new RestMethodResult<T>(status, statusMsg, resource);
    }

    /**
     * Handles generic errors if not handled by the sub classes
     * @param status server response status
     * @param responseBody response message
     * @throws Exception
     */
    protected void handleHttpStatus(int status, String responseBody) throws Exception {
        if (status == 401) {
            String message = getContext().getResources().getString(R.string.notAuthorized);
            throw new Exception(message);
        } else {
            String message = getContext().getResources().getString(R.string.error500);
            throw new Exception(message);
        }
    }

    /**
     * Lets the subclass build a {@link Request}, eg REST url, headers, set body etc.
     * @return returns a {@link Request} which will be executed in {@link AbstractRestMethod#execute()}
     */
    protected abstract Request buildRequest();

    /**
     * Hook which subclasses can overwrite to disable authorization for a request.
     * Settings this to false will not add an authorization header
     * @return boolean to indicate whether to add authorization header
     */
    protected boolean requiresAuthorization() {
        return true;
    }

    /**
     * Parses the response body returned by the server
     * @param responseBody JSON string returned by the server
     * @return Returns a generic type which usually contains objects
     * @throws Exception
     */
    protected abstract T parseResponseBody(String responseBody) throws Exception;

    /**
     * Makes a new RestClient, this handles all our server connections, then lets the client execute a given request
     * @param request request to be executed
     * @return returns {@link Response} containing status, body and headers
     */
    private Response doRequest(Request request) {
        RestClient client = new RestClient();
        return client.execute(request);
    }

    /**
     * Gets the character encoding, can be changed if necessary
     * @param headers http response headers
     * @return encoding type (UTF-8) in our case
     */
    private String getCharacterEncoding(Map<String, List<String>> headers) {
        return DEFAULT_ENCODING;
    }
}
