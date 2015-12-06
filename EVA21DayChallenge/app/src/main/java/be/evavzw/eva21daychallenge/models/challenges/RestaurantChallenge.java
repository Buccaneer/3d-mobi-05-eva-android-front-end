package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import be.evavzw.eva21daychallenge.models.categories.Category;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.models.categories.RecipeCategory;
import be.evavzw.eva21daychallenge.models.categories.RestaurantCategory;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
@DatabaseTable(tableName = "restaurant_challenges")
public class RestaurantChallenge extends Challenge {
    @DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
    private Restaurant restaurant;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Category.ID_FIELD_NAME)
    private RestaurantCategory category;

    RestaurantChallenge() //for ormlite
    {
    }

    public RestaurantChallenge(RestaurantCategory category, Restaurant restaurant) {
        this.category = category;
        this.restaurant = restaurant;
    }

    public RestaurantChallenge(JSONObject object) throws JSONException {
        if (object.has("ChallengeId")) {

        }
        if (object.has("Date")) {
            //setDate(object.);
        }
        if (object.has("Done")) {
            setIsDone(object.getBoolean("Done"));
        }
        if (object.has("Name")) {
            setName(object.getString("Name"));
        }
        if (object.has("Earnings")) {
            setEarnings(object.getInt("Earnings"));
        }
        if (object.has("Type")) {
            this.category = new RestaurantCategory(object.getString("Type"));
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public RestaurantCategory getCategory() {
        return category;
    }

}
