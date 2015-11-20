package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

import be.evavzw.eva21daychallenge.models.categories.Category;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class Challenge implements Serializable
{
    public static final String ID_FIELD = "challenge_id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField
    private Date date;

    Challenge() //for ormlite
    {
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
