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
    private boolean isDone;

    @DatabaseField
    private int earnings;

    @DatabaseField
    private String name;

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

    public int getEarnings() {
        return earnings;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setEarnings(int earnings) {
        this.earnings = earnings;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setName(String name) {
        this.name = name;
    }
}
