package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class Challenge implements Serializable, Comparable<Challenge> {
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

    @DatabaseField
    private String type;

    @DatabaseField
    private int serverId;

    @DatabaseField
    private String thumbnail;

    @DatabaseField
    private boolean isCurrentChallenge;

    private int minutesLeft;

    Challenge() //for ormlite
    {
    }

    public Challenge(JSONObject jsonObject) throws Exception {
        serverId = jsonObject.getInt("ChallengeId");
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("Date"));
        type = jsonObject.getString("Type");
        earnings = jsonObject.getInt("Earnings");
        name = jsonObject.getString("Name");
        isDone = jsonObject.getBoolean("Done");
        if (jsonObject.has("Thumbnail"))
            thumbnail = jsonObject.getString("Thumbnail");
        minutesLeft = jsonObject.getInt("TimeToAccept");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public boolean isCurrentChallenge()
    {
        return isCurrentChallenge;
    }

    public void setIsCurrentChallenge(boolean isCurrentChallenge)
    {
        this.isCurrentChallenge = isCurrentChallenge;
    }

    public int getServerId()
    {
        return serverId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getMinutesLeft()
    {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft)
    {
        this.minutesLeft = minutesLeft;
    }

    public int getEarnings()
    {
        return earnings;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int compareTo(Challenge another)
    {
        if (date == null ^ another.date == null) {
            return (date == null) ? -1 : 1;
        }
        if (date == null) {
            return 0;
        }
        return date.compareTo(another.date);
    }
}
