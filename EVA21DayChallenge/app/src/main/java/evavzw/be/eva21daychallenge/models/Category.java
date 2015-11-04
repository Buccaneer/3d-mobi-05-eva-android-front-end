package evavzw.be.eva21daychallenge.models;

import android.content.Context;

import evavzw.be.eva21daychallenge.R;

/**
 * Created by Pieter-Jan on 4/11/2015.
 */
public enum Category
{
    KOKEN("TITILE", "Bij deze challenge is het de bedoeling dat je iets plantaardig kookt. Dit is een saaie placeholder tekst.");

    private final String title;
    private final String description;

    Category(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

}
