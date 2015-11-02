package be.evavzw.eva21daychallenge.activity.profile_setup;

import android.content.Context;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.profile_setup.AbstractWizardModel;
import be.evavzw.eva21daychallenge.models.profile_setup.BranchPage;
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
        String pescetarian =mContext.getResources().getString(R.string.pescetarian);
        String parttimeVegetarian =mContext.getResources().getString(R.string.parttimeVegetarian);
        String vegetarian =mContext.getResources().getString(R.string.vegetarian);
        String vegan =mContext.getResources().getString(R.string.vegan);
        String other = mContext.getResources().getString(R.string.other);
        String numberHousehold = mContext.getResources().getString(R.string.numberHousehold);

        return new PageList(
                new UserInfoPage(this, personalInfo, mContext)
                        .setRequired(true),
                new SingleFixedChoicePage(this, typeOfVegetarian)
                        .setChoices(omnivore, pescetarian, parttimeVegetarian, vegetarian, vegan, other)
                        .setRequired(true),
                new SingleFixedChoicePage(this, numberHousehold)
                        .setChoices(String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), "5+")
        );
    }
}
