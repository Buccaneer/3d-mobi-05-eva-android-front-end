package be.evavzw.eva21daychallenge.models.challenges;

import com.j256.ormlite.field.DatabaseField;

import be.evavzw.eva21daychallenge.models.Category;

/**
 * Created by Pieter-Jan on 14/11/2015.
 */
public class TextChallenge extends Challenge
{
    @DatabaseField
    private String text;

    TextChallenge() //for ormlite
    {
    }

    TextChallenge(Category category, String text)
    {
        super(category);
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
