package be.evavzw.eva21daychallenge.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import android.widget.Toast;

import org.scribe.model.Token;

import java.util.Map;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import be.evavzw.eva21daychallenge.activity.profile_setup.ProfileSetup;
import be.evavzw.eva21daychallenge.customComponent.SplashDialog;
import be.evavzw.eva21daychallenge.services.UserManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity which the user sees upon login
 */
public class Login extends RESTfulActivity {
    @Bind(R.id.signIn)
    Button signIn;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    private WebView webView;
    private UserManager userManager;
    private AlertDialog alertDialog;
    @Bind(R.id.loginScreen)
    LinearLayout login;
    @Bind(R.id.blaadjes_achtergrond)
    ImageView img;
    private Dialog splashDialog;

    //For custom progress circle
    private AnimationDrawable frameAnimation;


    /**
     * Check if there's a token present and validate it, if it's valid this will redirect the user to the {@link MainMenu}
     */
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

        // Get instance of the UserManager
        userManager = UserManager.getInstance(getApplicationContext());

        // Set background color for our buttons
        signIn.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);

        // Set the size for the EVA logo which is displayed
        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

//This is done to get the background loaded

        loadBackground();


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signIn(username, password);
                img.setVisibility(View.VISIBLE);
                signIn.setText("");
                img.setBackgroundResource(R.drawable.blaadjes_progress);
                frameAnimation = (AnimationDrawable) img.getBackground();
                frameAnimation.start();

            }
        });
    }

    //Needed for portrait/landscape background
    private void loadBackground() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.achtergrond_login)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(this.getResources().getDisplayMetrics().widthPixels, this.getResources().getDisplayMetrics().heightPixels) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            BitmapDrawable background = new BitmapDrawable(bitmap);
                            login.setBackgroundDrawable(background);
                        }
                    });
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.landscapeachtergrond).asBitmap().into(new SimpleTarget<Bitmap>(this.getResources().getDisplayMetrics().widthPixels, this.getResources().getDisplayMetrics().heightPixels) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                    BitmapDrawable background = new BitmapDrawable(bitmap);
                    login.setBackgroundDrawable(background);
                }
            });
        }
    }

    @OnClick(R.id.createAccount)
    public void createAccountOnClick(View v) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginFacebookButton)
    public void loginFacebookOnClick() {
        handleExternalLogin("Facebook");
    }

    @OnClick(R.id.loginGoogleButton)
    public void loginGoogleOnClick() {
        handleExternalLogin("Google");
    }

    @OnClick(R.id.loginTwitterButton)
    public void loginTwitterOnClick() {
        handleExternalLogin("Twitter");
    }

    /**
     * Handles login for external services, eg Facebook, Twitter, Google
     *
     * @param service the service to be used for login
     */
    private void handleExternalLogin(String service) {
        // Make an AlertDialog which will contain a WebView
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        // Make the Web View, pressing the "Close" button will dismiss the dialog
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

        // Enable cookies and javascript, this seems to be needed for our third party register
        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setJavaScriptEnabled(true);

        // Add listeners for when the page is started
        webView.setWebViewClient(new WebViewClient() {
            private boolean doneYet = false;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // When the page is started, check the URL and act accordingly
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.startsWith("http://evavzwrest.azurewebsites.net")) {
                    if (url.contains("#access_token=") && !doneYet) {
                        String cookies = CookieManager.getInstance().getCookie(url);
                        String token = getToken(url);
                        if (token != null) {
                            // Once the user entered his credentials and is redirected back to our site, close the alert and start the Register service
                            //  alertDialog.dismiss();
                            RegisterExternalLoginTask rel = new RegisterExternalLoginTask();
                            rel.execute(token, cookies);
                        }
                        view.loadUrl(usedLastTime);
                        doneYet = true;
                        Log.i("FALSE", usedLastTime);
                    } else if (url.contains("#access_token=")) {
                        Log.i("TRUE", url);
                        String token = getToken(url);
                        if (token != null) {
                            // Once the user entered his credentials and is redirected back to our site, close the alert and start the Register service
                            alertDialog.dismiss();
                            userManager.login(new Token(token, ""));
                            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                            Login.this.finish();
                            startActivity(intent);
                        }
                    }
                    if (url.contains("#error=")) {
                        alertDialog.dismiss();
                    }
                }
            }
        });
        alertDialog.show();

        // Start the login service
        startLoginService(service);
    }

    /**
     * Retrieves the token out of the WebView URL
     *
     * @param url webview URL
     * @return Access token
     */
    private String getToken(String url) {
        //GetEncodedFragment shows us everything after the first # sign, this is where the access token starts
        //We then split on = and & and take the second element, this is the access token
        String[] access_token = Uri.parse(url).getEncodedFragment().split("\\&|=");
        if (access_token.length < 2)
            return null;

        return access_token[1];
    }

    private void startLoginService(String loginProvider) {
        NavigateToExternalLoginProviderTask ntelp = new NavigateToExternalLoginProviderTask();
        ntelp.execute(loginProvider);
    }


    private void signIn(String email, String password) {
        AuthorizeTask authorizeTask = new AuthorizeTask();
        authorizeTask.execute(email, password);
    }

    private void setEmailError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emailEditText.setError(error);
            }
        });

    }

    private void setPasswordError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                passwordEditText.setError(error);
            }
        });
    }

    private class AuthorizeTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            setRefresh(true);
        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                userManager.login(objects[0], objects[1]);
                return true;
            } catch (final IllegalArgumentException aex) {
                setEmailError(aex.getMessage());
                setPasswordError(aex.getMessage());

                return false;
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
        protected void onPostExecute(Boolean succeed) {
            setRefresh(false);
            stopAnimation();
            if (succeed) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Login.this.finish();
                startActivity(intent);
            }
        }
    }

    public static String usedLastTime = "";

    private class NavigateToExternalLoginProviderTask extends AsyncTask<String, Void, String> {

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
                usedLastTime = s;
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

    private class CheckIfCurrentTokenIsValidTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            if (userManager.isTokenPresent()) {
                splashDialog = new SplashDialog(Login.this, R.style.AppTheme_NoOverlay);
                splashDialog.setCancelable(false);
                splashDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return userManager.isTokenPresent() && userManager.isTokenValid();
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
            if (succeeded) {
                new CheckUserSetup().execute();
//                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
//                Login.this.finish();
//                startActivity(intent);
            } else {
                if (splashDialog != null)
                    splashDialog.dismiss();
            }
        }
    }

    private class CheckUserSetup extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return userManager.getUser().hasDoneSetup();
            } catch (Exception e) {
                if (splashDialog != null)
                    splashDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.error500, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean doneSetup) {
            if (doneSetup) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Login.this.finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
                Login.this.finish();
                startActivity(intent);
            }
        }
    }

    /**
     * Sets the ProgressBar visible or invisible, is called in the AsyncTasks at <code>onPreExecute()</code> or <code>onPostExecute()</code>
     *
     * @param refresh
     */
    private void setRefresh(final boolean refresh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleProgressBar(refresh);
            }
        });
    }

    //nodig om de personal progress circle te stoppen
    private void stopAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frameAnimation.stop();
                img.setVisibility(View.INVISIBLE);
                signIn.setText(R.string.signIn);
            }
        });


    }


}