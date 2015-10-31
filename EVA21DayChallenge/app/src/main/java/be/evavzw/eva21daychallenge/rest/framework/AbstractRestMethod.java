package be.evavzw.eva21daychallenge.rest.framework;

import android.content.Context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.security.RequestSigner;
import be.evavzw.eva21daychallenge.security.UserManager;
import evavzw.be.eva21daychallenge.R;


public abstract class AbstractRestMethod<T> implements RestMethod<T> {

    private static final String DEFAULT_ENCODING = "UTF-8";
    protected static String locale;

    public RestMethodResult<T> execute() {
        setLocale();

        Request request = buildRequest();
        if (requiresAuthorization()) {
            RequestSigner signer = UserManager.getInstance(getContext());
            signer.authorize(request);
        }

        Response response = doRequest(request);
        return buildResult(response);
    }

    private void setLocale() {
        Locale locale = getContext().getResources().getConfiguration().locale;
        String lang = locale.getLanguage();
        switch (lang) {
            case "nl":
                this.locale = "nl-BE";
                return;
            case "en":
                this.locale = "en-GB";
                return;
            case "fr":
                this.locale = "fr-FR";
                return;
            default:
                this.locale = "nl-BE";
                return;
        }
    }

    protected abstract Context getContext();

    /**
     * Subclasses can overwrite for full control, eg. need to do special
     * inspection of response headers, etc.
     *
     * @param response
     * @return
     */
    protected RestMethodResult<T> buildResult(Response response) {

        int status = response.status;
        String statusMsg = "";
        String responseBody = null;
        T resource = null;

        try {
            responseBody = new String(response.body, getCharacterEncoding(response.headers));
            if (response.status == 200)
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

    protected void handleHttpStatus(int status, String responseBody) throws Exception {
        if (status == 401) {
            String message = getContext().getResources().getString(R.string.notAuthorized);
            throw new Exception(message);
        }else{
            String message = getContext().getResources().getString(R.string.error500);
            throw new Exception(message);
        }
    }

    protected abstract Request buildRequest();

    protected boolean requiresAuthorization() {
        return true;
    }

    protected abstract T parseResponseBody(String responseBody) throws Exception;

    private Response doRequest(Request request) {

        RestClient client = new RestClient();
        return client.execute(request);
    }

    private String getCharacterEncoding(Map<String, List<String>> headers) {
        // TODO get value from headers
        return DEFAULT_ENCODING;
    }

}
