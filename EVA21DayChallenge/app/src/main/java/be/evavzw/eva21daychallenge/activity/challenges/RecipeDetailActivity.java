package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.Comparator;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.RecipeProperty;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity which displays all information about a Recipe.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE = "recipe";

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.subtitle)
    TextView subtitle;

    @Bind(R.id.nestedScrollView)
    NestedScrollView scrollView;

    @Bind(R.id.ingredientsTitle)
    TextView ingredientsTitle;

    @Bind(R.id.ingredientsLeft)
    TextView ingredientsLeft;

    @Bind(R.id.ingredientsRight)
    TextView ingredientsRight;

    @Bind(R.id.extraTitle)
    TextView extraTitle;

    @Bind(R.id.extraLeft)
    TextView extraLeft;

    @Bind(R.id.extraRight)
    TextView extraRight;

    @Bind(R.id.descriptionTitle)
    TextView descriptionTitle;

    @Bind(R.id.description)
    TextView description;

    Recipe recipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ((NestedScrollView) findViewById(R.id.nestedScrollView)).addView(inflater.inflate(R.layout.recipe_challenge, null));
        ButterKnife.bind(this);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra(RECIPE);
        updateChallenge(recipe);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addOffsetListener();

        //Sets the text on top of the CollapsingToolbar's image
        subtitle.setText(recipe.getName());

        loadBackdrop();
    }

    /**
     * This listener ensures the toolbar only shows the Title when collapsed
     */
    private void addOffsetListener() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(recipe.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    //Loads the image in the CollapsingToolbar
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(recipe.getImage()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }

    private String toUpperCase(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String toLowerCase(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Formats the Recipe's details then puts them into their proper TextViews
     *
     * @param recipe
     */
    private void updateChallenge(Recipe recipe) {
        //INGREDIENTS
        String[] ingredients = new String[recipe.getIngredients().size()];
        int counter = 0;
        for (Ingredient i : recipe.getIngredients()) {
            ingredients[counter++] = (i.getQuantity() > 0 ? i.getQuantity() + " " + (i.getUnit().trim().equals("") ? "" : i.getUnit().trim() + " ") : "") + toLowerCase(i.getName().trim());
        }
        Arrays.sort(ingredients, new Vergelijker());
        String ingredients1 = "";
        String ingredients2 = "";
        for (int i = 0; i < ingredients.length; i++) {
            if (i % 2 == 0)
                ingredients1 += "• " + ingredients[i] + (i < ingredients.length - 2 ? "\n" : "");
            else
                ingredients2 += "• " + ingredients[i] + (i < ingredients.length - 2 ? "\n" : "");
        }
        ingredientsLeft.setText(ingredients1);
        ingredientsRight.setText(ingredients2);

        //INFO
        String[] info = new String[recipe.getProperties().size()];
        counter = 0;
        for (RecipeProperty p : recipe.getProperties()) {
            info[counter++] = toUpperCase(p.getType().trim()) + ": " + toUpperCase(p.getValue().trim());
        }
        Arrays.sort(info, new Vergelijker());
        String info1 = "";
        String info2 = "";
        for (int i = 0; i < info.length; i++) {
            if (i % 2 == 0)
                info1 += info[i] + (i < info.length - 2 ? "\n" : "");
            else
                info2 += info[i] + (i < info.length - 2 ? "\n" : "");
        }
        extraLeft.setText(info1);
        extraRight.setText(info2);

        //DESCRIPTION
        String desc = recipe.getDescription().replaceAll("<[^>]*>", "");
        desc = desc.replaceAll("\\n", "");
        String[] descArray = desc.split("\\.");
        String formatted = "";
        for (int i = 0; i < descArray.length; i++) {
            formatted += i + 1 + ". " + toUpperCase(descArray[i].trim()) + ".\n";
        }
        description.setText(formatted);
    }

    //A simple String comparator
    private class Vergelijker implements Comparator<String> {
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

