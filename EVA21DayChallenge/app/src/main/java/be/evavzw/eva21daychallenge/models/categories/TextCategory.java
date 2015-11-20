package be.evavzw.eva21daychallenge.models.categories;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import be.evavzw.eva21daychallenge.models.challenges.*;

/**
 * Created by Pieter-Jan on 20/11/2015.
 */
@DatabaseTable(tableName = "text_categories")
public class TextCategory extends Category
{
    @ForeignCollectionField(columnName = FIELD_CHALLENGES)
    private Collection<TextChallenge> challenges;

    TextCategory() //for ormlite
    {
    }

    public TextCategory(String name)
    {
        super(name);
    }

    public Collection<TextChallenge> getChallenges()
    {
        return challenges;
    }

    public void setChallenges(Collection<TextChallenge> challenges)
    {
        this.challenges = challenges;
    }
}