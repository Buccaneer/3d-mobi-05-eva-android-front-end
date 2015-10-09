package evavzw.be.eva21daychallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebviewLogin extends AppCompatActivity {

    private final String API_EXTERNALLOGINS = "http://evavzwrest.azurewebsites.net/api/Account/ExternalLogins?returnUrl=%2F&generateState=true";
    private final String API_URL = "http://evavzwrest.azurewebsites.net";
    @Bind(R.id.webviewLogin)
    WebView webviewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_login);
        ButterKnife.bind(this);

        webviewLogin.getSettings().setJavaScriptEnabled(true);
        webviewLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.startsWith("http://evavzwrest.azurewebsites.net/#access_token")){
                    if(checkIfUserIsRegistered(url)){

                    };
                }
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(Login.EXTRA_MESSAGE);
        switch (message) {
            case "facebook":
            case "twitter":
            case "microsoft":
            case "google":
                startLoginService(message);
                return;
        }
    }

    private boolean checkIfUserIsRegistered(String url) {
        //GetEncodedFragment shows us everything after the first # sign, this is where the access token starts
        //We then split on = and & and take the second element, this is the access token
        String access_token = Uri.parse(url).getEncodedFragment().split("\\&|=")[1];


        //Save access token in sharedPreferences to make future authorized API calls
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("evavzw.be.eva21daychallenge", Context.MODE_PRIVATE);
        preferences.edit().putString("access_token", access_token).apply();
        return true;
    }

    private void startLoginService(final String loginProvider) {
        Ion.with(getApplicationContext())
                .load(API_EXTERNALLOGINS)
                .asJsonArray()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonArray>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonArray> result) {
                        if (result.getHeaders().code() == 200) {
                            //The Json Array we get back is a collection of Json objects for our login providers,
                            //these Json objects contain the provider name, login url and state, we need to filter out the url and send our user to it
                            String loginUrl = getLoginUrl(result.getResult(), loginProvider);
                            //Todo: probably should throw an exception if it's not found
                            if (!loginUrl.equals("NotFound")) {
                                webviewLogin.loadUrl(API_URL + loginUrl);
                            }
                        } else {
                            //TODO: make header error logging system which displays the error as a label
                            //Something went wrong
                            //If error code is 400, the same request should not be sent to the server again before making adjustments
                            Log.e("headerError", String.valueOf(result.getHeaders().code()));
                        }
                    }
                });
    }

    private String getLoginUrl(JsonArray result, String loginProvider) {
        Iterator<JsonElement> iterator = result.iterator();
        while (iterator.hasNext()) {
            JsonElement jsonElement = iterator.next();
            if (jsonElement.getAsJsonObject().get("Name").getAsString().equalsIgnoreCase(loginProvider)) {
                return jsonElement.getAsJsonObject().get("Url").getAsString();
            }
        }
        return "NotFound";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
