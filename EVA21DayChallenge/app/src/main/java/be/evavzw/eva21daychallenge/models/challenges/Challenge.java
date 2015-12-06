package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.evavzw.eva21daychallenge.models.categories.Category;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class Challenge implements Serializable, Comparable<Challenge>
{
    public static final String ID_FIELD = "challenge_id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField
    private Date date;

    @DatabaseField
    private String type, name;

    @DatabaseField
    private int earnings, serverId;

    Challenge() //for ormlite
    {
    }

    public Challenge(JSONObject jsonObject) throws Exception
    {
        serverId = jsonObject.getInt("ChallengeId");
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("Date"));
        type = jsonObject.getString("Type");
        earnings = jsonObject.getInt("Earnings");
        name = jsonObject.getString("Name");
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getServerId()
    {
        return serverId;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public int getEarnings()
    {
        return earnings;
    }

    @Override
    public int compareTo(Challenge another)
    {
        return date.compareTo(another.date);
    }
}