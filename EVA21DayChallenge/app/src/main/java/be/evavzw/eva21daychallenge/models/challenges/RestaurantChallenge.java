package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;
import be.evavzw.eva21daychallenge.models.categories.Category;
import be.evavzw.eva21daychallenge.models.Restaurant;
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

    public RestaurantChallenge(JSONObject jsonObject) throws Exception
    {
        super(jsonObject);
        if(jsonObject.has("Restaurant"))
            restaurant = new Restaurant(jsonObject.getJSONObject("Restaurant"));
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public RestaurantCategory getCategory() {
        return category;
    }

}
