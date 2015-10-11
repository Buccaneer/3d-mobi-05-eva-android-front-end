package evavzw.be.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import evavzw.be.eva21daychallenge.rest.framework.AbstractRestMethod;
import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Jasper De Vrient on 11/10/2015.
 */
public class ExternalLoginsRestMethod extends AbstractRestMethod<Map<String,String>> {

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

    @Override
    protected Request buildRequest() {
        return new Request(RestMethodFactory.Method.GET, REQURI, new byte[] {});
    }

    @Override
    protected Map<String, String> parseResponseBody(String responseBody) throws Exception {
        Map<String,String> outputMap = new HashMap<>();
        try {
            JSONArray arr = new JSONArray(responseBody);

            for (int i = 0; i < arr.length(); ++i) {
                try {
                    JSONObject provider = arr.getJSONObject(i);

                    outputMap.put(provider.getString("Name"),PRE +  provider.getString("Url"));
                } catch (Exception ex) { // Genest voor te zorgen dat niet alles faalt.

                }
            }
        } catch (Exception ex) {

        }

        return outputMap;
    }

    @Override
    protected boolean requiresAuthorization() {
        return false;
    }
}
