package evavzw.be.eva21daychallenge.activity.challenges;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.Ingredient;
import evavzw.be.eva21daychallenge.models.Recipe;
import evavzw.be.eva21daychallenge.models.RecipeProperty;

/**
 * Created by Pieter-Jan on 2/11/2015.
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
        ((NestedScrollView)findViewById(R.id.nestedScrollView)).addView(inflater.inflate(R.layout.recipe_challenge, null));
        ButterKnife.bind(this);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra(RECIPE);
        updateChallenge(recipe);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addOffsetListener();

        subtitle.setText(recipe.getName());

        loadBackdrop();
    }

    private void addOffsetListener()
    {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0)
                {
                    collapsingToolbar.setTitle(recipe.getName());
                    isShow = true;
                }
                else if (isShow)
                {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(recipe.getImage()/*Images.getRandomCheeseDrawable()*/).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }

    private String toUpperCase(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String toLowerCase(String s)
    {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private void updateChallenge(Recipe recipe) {
        //INGREDIENTS
        /*String[] ingredients = new String[]{
                "1 ui fijngesnipperd", "1 teentje knoflook fijngehakt",
                "2 rode paprika's in stukjes", "2 el olie", "2 el tomatenpuree", "chilipoeder", "1 blik kidneybonen",
                "1 dl groentebouillon", "peper en zout", "harissa", "suiker"
        };*/

        String[] ingredients = new String[recipe.getIngredients().size()];
        int counter = 0;
        for (Ingredient i : recipe.getIngredients())
        {
            ingredients[counter++] = (i.getQuantity() > 0 ? i.getQuantity() + " " + (i.getUnit().trim().equals("") ? "" : i.getUnit().trim() + " "): "") + toLowerCase(i.getName().trim());
        }
        Arrays.sort(ingredients, new Vergelijker());
        String ingredients1 = "";
        String ingredients2 = "";
        for (int i = 0; i < ingredients.length; i++)
        {
            if (i % 2 == 0)
                ingredients1 += "• " + ingredients[i] + (i < ingredients.length - 2 ? "\n" : "");
            else
                ingredients2 += "• " + ingredients[i] + (i < ingredients.length - 2 ? "\n" : "");
        }
        //ingredientsTitle.setText("Ingredients");
        ingredientsLeft.setText(ingredients1);
        ingredientsRight.setText(ingredients2);

        //INFO
        /*String[] info = new String[]{
                "Aantal personen: 4",
                "Moeilijkheidsgraad: Voor starters",
                "Type: Hoofdgerecht",
                "Kooktijd: Snel",
                "Allergie: Glutenvrij"
        };*/
        String[] info = new String[recipe.getProperties().size()];
        counter = 0;
        for (RecipeProperty p : recipe.getProperties())
        {
            info[counter++] = toUpperCase(p.getType().trim()) + ": " + toUpperCase(p.getValue().trim());
        }
        Arrays.sort(info, new Vergelijker());
        String info1 = "";
        String info2 = "";
        for (int i = 0; i < info.length; i++)
        {
            if (i % 2 == 0)
                info1 += info[i] + (i < info.length - 2 ? "\n" : "");
            else
                info2 += info[i] + (i < info.length - 2 ? "\n" : "");
        }
        //extraTitle.setText("Extra information");
        extraLeft.setText(info1);
        extraRight.setText(info2);

        //DESCRIPTION
        /*String desc = "Verhit de olie in een braadpan en fruit daarin de ui glazig." +
                "Voeg de knoflkook en paprika's toe en bak enkele minuten mee." +
                "Voeg de tomatenpuree en het chilipoeder toe en bak al omscheppend 2 minuten." +
                "Spoel de bonen onder de kraan en voeg ze bij de tomatenpuree en de groenten." +
                "Voeg nu de bouillon toe en laat pruttelen tot de groenten gaar zijn." +
                "Dat duurt ongeveer een kwartier. Voeg naar smaak harissa en suiker toe." +
                "Dien het gerecht op met wraps, stokbrood of rijst en eventueel wat peterselie of koriander.";*/
        String desc = recipe.getDescription().replaceAll("<[^>]*>", "");
        desc = desc.replaceAll("\\n", "");
        String[] descArray = desc.split("\\.");
        String formatted = "";
        for (int i = 0; i < descArray.length; i++)
        {
            formatted += i + 1 + ". " + toUpperCase(descArray[i].trim()) + ".\n";
        }
        //descriptionTitle.setText("Preparation");
        description.setText(formatted);
    }

    private class Vergelijker implements Comparator<String>
    {
        public int compare(String o1, String o2)
        {
            if (o1.length() > o2.length())
            {
                return 1;
            }
            else if (o1.length() < o2.length())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}

