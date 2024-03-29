package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import be.evavzw.eva21daychallenge.models.categories.*;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
@DatabaseTable(tableName = "text_challenges")
public class TextChallenge extends Challenge
{
    @DatabaseField
    private String text;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Category.ID_FIELD_NAME)
    private TextCategory category;

    TextChallenge() //for ormlite
    {
    }

    TextChallenge(TextCategory category, String text)
    {
        this.category = category;
        this.text = text;
    }

    public TextChallenge(JSONObject jsonObject) throws Exception
    {
        super(jsonObject);
    }

    public String getText()
    {
        return text;
    }

    public TextCategory getCategory()
    {
        return category;
    }

}
