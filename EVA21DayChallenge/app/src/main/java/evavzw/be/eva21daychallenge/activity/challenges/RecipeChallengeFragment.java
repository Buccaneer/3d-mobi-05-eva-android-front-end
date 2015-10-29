package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.Ingredient;
import evavzw.be.eva21daychallenge.models.Recipe;
import evavzw.be.eva21daychallenge.models.RecipeProperty;

/**
 * Created by Pieter-Jan on 24/10/2015.
 */
public class RecipeChallengeFragment extends Fragment
{
    final static String ARG_RECIPE = "recipe";

    AppCompatActivity activity;

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

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.recipe_challenge, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            //updateChallenge(args.getInt(ARG_CATEGORY), args.getInt(ARG_CHALLENGE));
            // TODO
            updateChallenge(((Recipe) args.getSerializable(ARG_RECIPE)));
        } /*else if (currentCategory != -1) {
            // Set article based on saved instance state defined during onCreateView
            //updateChallenge(currentCategory, currentChallenge);
            // TODO
        }*/
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Bundle args = getArguments();
        activity.getSupportActionBar().setTitle(((Recipe) args.getSerializable(ARG_RECIPE)).getName());
    }

    @Override
    public void onPause ()
    {
        super.onPause();
        // TODO
    }

    @Override
    public void onStop ()
    {
        super.onStop();
        // TODO
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
            ingredients[counter++] = (i.getQuantity() == 0 ? "" : i.getQuantity() + " ") + (i.getUnit().trim().equals("") ? "" : i.getUnit().trim() + " ") + i.getName().trim();
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
            info[counter++] = p.getType().trim() + ": " + p.getValue().trim();
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
        String desc = recipe.getDescription();
        String[] descArray = desc.split("\\.");
        String formatted = "";
        for (int i = 0; i < descArray.length; i++)
        {
            formatted += i + 1 + ". " + descArray[i] + ".\n";
        }
        //descriptionTitle.setText("Preparation");
        description.setText(formatted);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        /*if (savedInstanceState != null)
        {
            //Restore the fragment's state here
            text = savedInstanceState.getString("text");
            currentCategory = savedInstanceState.getInt("category");
            currentChallenge = savedInstanceState.getInt("challenge");
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        /*outState.putString("text", text);
        outState.putInt("category", currentCategory);
        outState.putInt("challenge", currentChallenge);*/
    }

    private void init()
    {
        activity = (AppCompatActivity) getActivity();
    }

    class Vergelijker implements Comparator<String>
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