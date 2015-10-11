package evavzw.be.eva21daychallenge.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.scribe.model.Token;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.security.UserManager;

public class WebviewLogin extends AppCompatActivity {


    @Bind(R.id.webviewLogin)
    WebView webviewLogin;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_login);
        ButterKnife.bind(this);

        userManager = UserManager.getInstance(this);

        CookieManager.getInstance().setAcceptCookie(true);
        webviewLogin.getSettings().setJavaScriptEnabled(true);
        webviewLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.startsWith("http://evavzwrest.azurewebsites.net/#access_token") ){
                    String cookies = CookieManager.getInstance().getCookie(url);
                    String token = getToken(url);
                    if (token != null) {
                        RegisterExternalLoginTask rel = new RegisterExternalLoginTask();
                        rel.execute(token, cookies);

                    }
                }
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(Login.EXTRA_MESSAGE);
       startLoginService(message);
    }

    private String getToken(String url) {
        //GetEncodedFragment shows us everything after the first # sign, this is where the access token starts
        //We then split on = and & and take the second element, this is the access token
        String[] access_token = Uri.parse(url).getEncodedFragment().split("\\&|=");
        if (access_token.length <2)
            return null;


        return access_token[1];
    }

    private void startLoginService( String loginProvider) {
        NavigateToExternalLoginProviderTask ntelp = new NavigateToExternalLoginProviderTask();
        ntelp.execute(loginProvider);
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

    private class NavigateToExternalLoginProviderTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
          //  try {
                Map<String, String> mdata = userManager.getExternalLoginProviders();
                if (mdata.containsKey(params[0]))
                    return mdata.get(params[0]);
        //   } catch (Exception ex) {}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                finish();
            } else {
                webviewLogin.loadUrl(s);
            }
        }
    }

    private class RegisterExternalLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                userManager.registerExternal(params[0], params[1]);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeded) {
            if (succeded) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                WebviewLogin.this.finish();
                startActivity(intent);
            }
        }
    }
}
