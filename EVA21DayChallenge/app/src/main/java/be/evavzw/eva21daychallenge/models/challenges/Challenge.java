package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import be.evavzw.eva21daychallenge.models.Category;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class Challenge
{
    public static final String ID_FIELD = "challenge_id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField
    private Date date;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Category.ID_FIELD_NAME)
    private Category category;

    Challenge() //for ormlite
    {
    }

    public Challenge(Category category)
    {
        this.category = category;
    }

    public Category getCategory()
    {
        return category;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
