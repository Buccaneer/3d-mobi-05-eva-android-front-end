package be.evavzw.eva21daychallenge.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Pieter-Jan on 4/11/2015.
 */
@DatabaseTable
public class Restaurant implements Serializable {

    @DatabaseField(id = true)
    private int restaurantId;

    @DatabaseField
    private String name, description, website, email, phone, street, postal, city;

    @DatabaseField
    private double latitude, longitude;

    Restaurant() //for ormlite
    {
    }

    public Restaurant(JSONObject jsonObject) throws Exception {
        parseJson(jsonObject);
    }

    private void parseJson(JSONObject jsonObject) throws Exception {
        if (jsonObject.has("Name"))
            name = jsonObject.getString("Name");
        if (jsonObject.has("Description"))
            description = jsonObject.getString("Description");
        if (jsonObject.has("Website"))
            website = jsonObject.getString("Website");
        if (jsonObject.has("Email"))
            email = jsonObject.getString("Email");
        if (jsonObject.has("Phone"))
            phone = jsonObject.getString("Phone");
        if (jsonObject.has("Street"))
            street = jsonObject.getString("Street");
        if (jsonObject.has("Postal"))
            postal = jsonObject.getString("Postal");
        if (jsonObject.has("City"))
            city = jsonObject.getString("City");
        if (jsonObject.has("Id"))
            restaurantId = jsonObject.getInt("Id");
        if (jsonObject.has("Latitude"))
            latitude = jsonObject.getDouble("Latitude");
        if (jsonObject.has("Longitude"))
            longitude = jsonObject.getDouble("Longitude");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getPostal() {
        return postal;
    }

    public String getCity() {
        return city;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
