package be.evavzw.eva21daychallenge.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Pieter-Jan on 4/11/2015.
 */
public class Restaurant implements Serializable
{

    private String name, description, website, email, phone, street, postal, city;
    private int restaurantId;
    private double latitude, longitude;

    public Restaurant(JSONObject jsonObject) throws Exception{
        parseJson(jsonObject);
    }

    private void parseJson(JSONObject jsonObject) throws Exception{
        name = jsonObject.getString("Name");
        description = jsonObject.getString("Description");
        website = jsonObject.getString("Website");
        email = jsonObject.getString("Email");
        phone = jsonObject.getString("Phone");
        street = jsonObject.getString("Street");
        postal = jsonObject.getString("Postal");
        city = jsonObject.getString("City");

        restaurantId = jsonObject.getInt("RestaurantId");
        latitude = jsonObject.getDouble("Latitude");
        longitude = jsonObject.getDouble("Longitude");
    }

    public String getName() {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getWebsite()
    {
        return website;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getStreet()
    {
        return street;
    }

    public String getPostal()
    {
        return postal;
    }

    public String getCity()
    {
        return city;
    }

    public int getRestaurantId()
    {
        return restaurantId;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
}
