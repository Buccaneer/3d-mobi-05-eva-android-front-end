package evavzw.be.eva21daychallenge.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.google.android.gms.common.SignInButton;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.customComponent.LoginButton;
import evavzw.be.eva21daychallenge.customComponent.TwitterButton;
import evavzw.be.eva21daychallenge.security.UserManager;

public class Login extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "evavzw.be.eva21daychallenge.MESSAGE";
    @Bind(R.id.createAccount)
    Button createAccount;
    @Bind(R.id.signIn)
    Button signIn;
    @Bind(R.id.loginFacebookButton)
    LoginButton loginFacebook;
    @Bind(R.id.loginTwitterButton)
    TwitterButton loginTwitter;
    @Bind(R.id.loginGoogleButton)
    SignInButton loginGoogle;
    @Bind(R.id.progress_indicator)
    ProgressBar progressBar;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    private WebView webView;
    private UserManager mOAuthManager;


    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mOAuthManager.isTokenPresent() && mOAuthManager.isTokenValid()) {
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        mOAuthManager = UserManager.getInstance(this);

        signIn.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);
        createAccount.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);

        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();

        //double check my math, this should be right, though
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);

        //Use RelativeLayout.LayoutParams if your parent is a RelativeLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //Start a new activity when pressing Register
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Register.class);
                startActivity(intent);
            }
        });

        //Start a new activity when pressing Sign in
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignIn.class);
                startActivity(intent);
            }
        });

        loginFacebook.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               handleExternalLogin("Facebook");
                                           }
                                       }
        );

        loginGoogle.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              handleExternalLogin("Google");
                                          }
                }
        );

        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExternalLogin("Twitter");
            }
        });
    }

    private void handleExternalLogin(String service) {
        //Intent intent = new Intent(getApplicationContext(), WebviewLogin.class);
        //intent.putExtra(EXTRA_MESSAGE, service);
        //startActivity(intent);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        webView = new WebView(this);

        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.startsWith("http://evavzwrest.azurewebsites.net/#access_token")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    String token = getToken(url);
                    if (token != null) {
                        RegisterExternalLoginTask rel = new RegisterExternalLoginTask();
                        rel.execute(token, cookies);
                    }
                }
            }
        });

        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
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

    private void startLoginService( String loginProvider) {
        NavigateToExternalLoginProviderTask ntelp = new NavigateToExternalLoginProviderTask();
        ntelp.execute(loginProvider);
    }

    private class NavigateToExternalLoginProviderTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            //  try {
            Map<String, String> mdata = mOAuthManager.getExternalLoginProviders();
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
                webView.loadUrl(s);
            }
        }
    }

    private class RegisterExternalLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                mOAuthManager.registerExternal(params[0], params[1]);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeded) {
            if (succeded) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Login.this.finish();
                startActivity(intent);
            }
        }
    }
}
