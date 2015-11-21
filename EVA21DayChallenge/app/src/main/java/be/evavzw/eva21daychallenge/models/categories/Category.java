package be.evavzw.eva21daychallenge.models.categories;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import be.evavzw.eva21daychallenge.models.challenges.Challenge;

/**
 * Created by Pieter-Jan on 4/11/2015.
 */
public class Category
{
    public static final String COOKING = "Cooking";
    public static final String DINING_OUT = "Dining_Out";
    public static final String SUGARFREE = "Sugarfree";

    public static final String ID_FIELD_NAME = "name";
    public static final String FIELD_CHALLENGES = "challenges";

    @DatabaseField(id = true, columnName = ID_FIELD_NAME)
    private String name;

    Category()
    {
    }

    public Category(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
