package be.evavzw.eva21daychallenge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import android.widget.Toast;

import java.util.Map;

import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import be.evavzw.eva21daychallenge.security.UserManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import be.evavzw.eva21daychallenge.R;

public class Login extends RESTfulActivity {

  @Bind(R.id.createAccount)
  TextView createAccount;
    @Bind(R.id.signIn)
    Button signIn;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    private WebView webView;
    private UserManager userManager;
    private AlertDialog alertDialog;
    @Bind(R.id.loginScreen)
    LinearLayout login;




    @Override
    protected void onStart() {
        super.onStart();
        new CheckIfCurrentTokenIsValidTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        userManager = UserManager.getInstance(getApplicationContext());

        signIn.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);
/*
        createAccount.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);
*/

        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);
     //   loadBackground();
    }

   @OnClick(R.id.createAccount)
    public void createAccountOnClick(View v){
        Intent intent = new Intent(v.getContext(), Register.class);
        startActivity(intent);
    }

    @OnClick(R.id.signIn)
    public void signInOnClick(View v){
        Intent intent = new Intent(v.getContext(), SignIn.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginFacebookButton)
    public void loginFacebookOnClick(){
        handleExternalLogin("Facebook");
    }

    @OnClick(R.id.loginGoogleButton)
    public void loginGoogleOnClick(){
        handleExternalLogin("Google");
    }

    @OnClick(R.id.loginTwitterButton)
    public void loginTwitterOnClick(){
        handleExternalLogin("Twitter");
    }

    private void handleExternalLogin(String service) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        webView = new WebView(this);
        alert.setView(webView);
        String close = getApplicationContext().getResources().getString(R.string.close);
        alert.setNegativeButton(close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = alert.create();

        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.startsWith("http://evavzwrest.azurewebsites.net")){
                    if (url.contains("#access_token=")) {
                        String cookies = CookieManager.getInstance().getCookie(url);
                        String token = getToken(url);
                        if (token != null) {
                            alertDialog.dismiss();
                            RegisterExternalLoginTask rel = new RegisterExternalLoginTask();
                            rel.execute(token, cookies);
                        }
                    } if (url.contains("#error=")) {
                        alertDialog.dismiss();
                    }
                }
            }
        });
        alertDialog.show();
        startLoginService(service);
    }

    private String getToken(String url) {
        //GetEncodedFragment shows us everything after the first # sign, this is where the access token starts
        //We then split on = and & and take the second element, this is the access token
        String[] access_token = Uri.parse(url).getEncodedFragment().split("\\&|=");
        if (access_token.length <2)
            return null;

        return access_token[1];
    }

    private void startLoginService(String loginProvider) {
        NavigateToExternalLoginProviderTask ntelp = new NavigateToExternalLoginProviderTask();
        ntelp.execute(loginProvider);
    }

    private class NavigateToExternalLoginProviderTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            setRefresh(true);
        }

        @Override
        protected String doInBackground(String... params) {
             try {
            Map<String, String> mdata = userManager.getExternalLoginProviders();
            if (mdata.containsKey(params[0]))
                return mdata.get(params[0]);
               } catch (final Exception ex) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
             }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setRefresh(false);
            if (s == null) {
                finish();
            } else {
                webView.loadUrl(s);
            }
        }
    }

    private class RegisterExternalLoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            setRefresh(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                userManager.registerExternal(params[0], params[1]);
                return true;
            } catch (final Exception ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeeded) {
            setRefresh(false);
            if (succeeded) {
                alertDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Login.this.finish();
                startActivity(intent);
            }
        }
    }

    private class CheckIfCurrentTokenIsValidTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                return userManager.isTokenPresent() && userManager.isTokenValid();
            }catch(final Exception ex){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeeded) {
            if(succeeded){
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Login.this.finish();
                startActivity(intent);
            }
        }
    }

    private void setRefresh(final boolean refresh){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleProgressBar(refresh);
            }
        });
    }


    //Nodig voor Picasso

/*
    private void loadBackground() {

        Picasso.with(this).load(R.drawable.achtergrond_login).into(loginScreen);
    }
*/


}