package evavzw.be.eva21daychallenge.activity.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.*;

/**
 * Created by Pieter-Jan on 4/11/2015.
 */
public class RestaurantDetailActivity extends AppCompatActivity
{
    public static final String RESTAURANT = "restaurant";

    RestaurantTemp restaurant;

    @Bind(R.id.restaurantMap)
    ImageView restaurantMap;

    @Bind(R.id.restaurantTitle)
    TextView restaurantTitle;

    @Bind(R.id.restaurantAddress)
    TextView restaurantAddress;

    @Bind(R.id.restaurantProperties)
    TextView restaurantProperties;

    @Bind(R.id.restaurantDescription)
    TextView restaurantDescription;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.category_restaurant));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        restaurant = (RestaurantTemp) intent.getSerializableExtra(RESTAURANT);
        updateChallenge(restaurant);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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

    private void updateChallenge(RestaurantTemp restaurant)
    {
        restaurantTitle.setText(restaurant.getName());

        String address = "<b>" + getString(R.string.address) + "</b><br>";
        address += restaurant.getStreet().trim() + "<br>";
        address += restaurant.getPostal().trim() + " " + restaurant.getCity().trim();

        restaurantAddress.setText(Html.fromHtml(address));

        String properties = "<b>" + getString(R.string.properties) + "</b><br>";
        properties += getString(R.string.phone) + " " + restaurant.getPhone() + "<br>";
        properties += getString(R.string.email) + ": " + restaurant.getEmail() + "<br>";
        properties += getString(R.string.website) + ": " + restaurant.getWebsite();

        restaurantProperties.setText(Html.fromHtml(properties));

        String description = "<b>" + getString(R.string.description) + "</b><br>";
        description += restaurant.getDescription();
        restaurantDescription.setText(Html.fromHtml(description));
        /*String[] ingredients = new String[restaurant..getIngredients().size()];
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

        String desc = recipe.getDescription().replaceAll("<[^>]*>", "");
        desc = desc.replaceAll("\\n", "");
        String[] descArray = desc.split("\\.");
        String formatted = "";
        for (int i = 0; i < descArray.length; i++)
        {
            formatted += i + 1 + ". " + toUpperCase(descArray[i].trim()) + ".\n";
        }*/
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
