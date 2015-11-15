package be.evavzw.eva21daychallenge.activity.profile_setup;

import android.content.Context;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.profile_setup.AbstractWizardModel;
import be.evavzw.eva21daychallenge.models.profile_setup.AllergiesPage;
import be.evavzw.eva21daychallenge.models.profile_setup.MultipleFixedChoicePage;
import be.evavzw.eva21daychallenge.models.profile_setup.PageList;
import be.evavzw.eva21daychallenge.models.profile_setup.SingleFixedChoicePage;
import be.evavzw.eva21daychallenge.models.profile_setup.UserInfoPage;

public class ProfileWizardModel extends AbstractWizardModel {
    public ProfileWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        String personalInfo = mContext.getResources().getString(R.string.personalInfo);
        String typeOfVegetarian = mContext.getResources().getString(R.string.typeOfVegetarian);
        String omnivore = mContext.getResources().getString(R.string.omnivore);
        String pescetarian = mContext.getResources().getString(R.string.pescetarian);
        String parttimeVegetarian = mContext.getResources().getString(R.string.parttimeVegetarian);
        String vegetarian = mContext.getResources().getString(R.string.vegetarian);
        String vegan = mContext.getResources().getString(R.string.vegan);
        String other = mContext.getResources().getString(R.string.other);
        String numberHousehold = mContext.getResources().getString(R.string.numberHousehold);
        String budgetAmount = mContext.getResources().getString(R.string.budgetAmount);
        String low = mContext.getResources().getString(R.string.low);
        String medium = mContext.getResources().getString(R.string.medium);
        String large = mContext.getResources().getString(R.string.large);
        String notShared = mContext.getResources().getString(R.string.notShared);
        String allergicTo = mContext.getResources().getString(R.string.allergicTo);
        String glutenFree = mContext.getResources().getString(R.string.glutenFree);
        String sugarFree = mContext.getResources().getString(R.string.sugarFree);
        String peanuts = mContext.getResources().getString(R.string.peanuts);
        String nuts = mContext.getResources().getString(R.string.nuts);

        return new PageList(
                new UserInfoPage(this, personalInfo, mContext)
                        .setRequired(true),
                new AllergiesPage(this, allergicTo, mContext),
                new SingleFixedChoicePage(this, budgetAmount)
                        .setChoices(low, medium, large, notShared)
                        .setRequired(true),
                new SingleFixedChoicePage(this, typeOfVegetarian)
                        .setChoices(omnivore, pescetarian, parttimeVegetarian, vegetarian, vegan, other)
                        .setRequired(true),
                new SingleFixedChoicePage(this, numberHousehold)
                        .setChoices(String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), "5+")
        );
    }
}
