package evavzw.be.eva21daychallenge.activity;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;

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
