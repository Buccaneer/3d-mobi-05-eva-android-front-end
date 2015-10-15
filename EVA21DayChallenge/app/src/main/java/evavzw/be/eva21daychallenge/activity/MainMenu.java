package evavzw.be.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.Ingredient;
import evavzw.be.eva21daychallenge.models.Recipe;
import evavzw.be.eva21daychallenge.models.RecipeProperty;
import evavzw.be.eva21daychallenge.models.User;
import evavzw.be.eva21daychallenge.security.RecipeManager;
import evavzw.be.eva21daychallenge.security.UserManager;

public class MainMenu extends AppCompatActivity {
    @Bind(R.id.lblUserGreet)
    TextView lblUserGreet;
    private UserManager mOAuthManager;
    private RecipeManager mRecipeManager;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.recipeName)
    TextView recipeName;
    @Bind(R.id.recipeDescription)
    TextView recipeDescription;
    @Bind(R.id.recipeImage)
    ImageView recipeImage;
    @Bind(R.id.ingredients)
    TextView ingredients;
    @Bind(R.id.properties)
    TextView properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mOAuthManager = UserManager.getInstance(getApplicationContext());
        mRecipeManager = RecipeManager.getInstance(getApplicationContext());
        ButterKnife.bind(this);

        UserInfoTask uit = new UserInfoTask();
        uit.execute();
    }

    @OnClick(R.id.btnGetRecipes)
    public void getAllRecipes(View v){
        new GetAllRecipesTask().execute();
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
                lblUserGreet.setText("Hello " + user.getEmail() + "\nRegistered: " + (user.isRegistered() ? "Yes" : "No"));
            }
        }
    }

    private class GetAllRecipesTask extends AsyncTask<Void, Void, List<Recipe>>{

        @Override
        protected List<Recipe> doInBackground(Void... params) {
           try{
               return mRecipeManager.getAllRecipes();
           }catch (Exception e){
               Log.e("Error", e.getMessage());
               return null;
           }
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            final Recipe recipe = recipes.get(0);
            recipeName.setText(recipe.getName());
            recipeDescription.setText(recipe.getDescription());

            RunnableFuture f = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    Bitmap image = null;
                    try{
                        InputStream in = new URL(recipe.getImage()).openStream();
                        image = BitmapFactory.decodeStream(in);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return image;
                }
            });

            new Thread(f).start();

            Bitmap image = null;
            try {
                image = (Bitmap) f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            recipeImage.setImageBitmap(image);

            String ingredientsString = "Ingredienten:\n";
            for(Ingredient ingredient : recipe.getIngredients()){
                ingredientsString += String.format("%d %s %s\n", ingredient.getQuantity(), ingredient.getUnit(), ingredient.getName());
            }

            ingredients.setText(ingredientsString);

            String propertiesString = "";
            for(RecipeProperty property : recipe.getProperties()){
                ingredientsString += String.format("%s %s\n", property.getType(), property.getValue());
            }

            properties.setText(propertiesString);

            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
