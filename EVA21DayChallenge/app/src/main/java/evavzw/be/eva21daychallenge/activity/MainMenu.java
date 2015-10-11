package evavzw.be.eva21daychallenge.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.User;
import evavzw.be.eva21daychallenge.security.UserManager;

public class MainMenu extends AppCompatActivity {
    @Bind(R.id.lblUserGreet)
    TextView lblUserGreet;
    private UserManager mOAuthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mOAuthManager = UserManager.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        UserInfoTask uit = new UserInfoTask();
        uit.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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

        if(id == R.id.logOut){
            mOAuthManager.invalidateToken();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class UserInfoTask extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            try {
                return mOAuthManager.getUser();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            if (user!=null) {
                lblUserGreet.setText("Hello " + user.getEmail() + "\nRegistred: " + (user.isRegistered() ? "Yes" : "No"));
            }
        }
    }
}
