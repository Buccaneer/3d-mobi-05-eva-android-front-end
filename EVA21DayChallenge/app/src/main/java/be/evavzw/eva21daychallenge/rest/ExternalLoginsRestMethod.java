package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Rest Method which returns all links for available external login providers
 */
public class ExternalLoginsRestMethod extends AbstractRestMethod<Map<String, String>> {

    private static final URI REQURI = URI.create("http://evavzwrest.azurewebsites.net/api/Account/ExternalLogins?returnUrl=%2F&generateState=true");
    private static final String PRE = "http://evavzwrest.azurewebsites.net";
    private Context context;

    public ExternalLoginsRestMethod(Context context) {
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
     * @return returns all available login provider URLs
     * @throws Exception
     */
    @Override
    protected Map<String, String> parseResponseBody(String responseBody) throws Exception {
        Map<String, String> outputMap = new HashMap<>();
        try {
            JSONArray arr = new JSONArray(responseBody);

            for (int i = 0; i < arr.length(); ++i) {
                try {
                    JSONObject provider = arr.getJSONObject(i);

                    outputMap.put(provider.getString("Name"), PRE + provider.getString("Url"));
                } catch (Exception ex) {
                    throw ex;
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return outputMap;
    }

    /**
     * Overwrite hook {@link AbstractRestMethod#requiresAuthorization()}, this request doesn't require authorization
     *
     * @return
     */
    @Override
    protected boolean requiresAuthorization() {
        return false;
    }
}
