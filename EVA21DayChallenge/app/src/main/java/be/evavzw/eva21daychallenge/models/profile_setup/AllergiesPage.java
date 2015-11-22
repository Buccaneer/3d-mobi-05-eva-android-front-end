package be.evavzw.eva21daychallenge.models.profile_setup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.profile_setup.AllergiesFragment;
import be.evavzw.eva21daychallenge.activity.profile_setup.UserInfoFragment;
import be.evavzw.eva21daychallenge.models.Ingredient;

/**
 * Created by Jan on 15/11/2015.
 */
public class AllergiesPage extends Page {

    public AllergiesPage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title, context);
    }

    @Override
    public Fragment createFragment() {
        return AllergiesFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        StringBuilder sb = new StringBuilder();

        ArrayList<Ingredient> selections = (ArrayList<Ingredient>) mData.getSerializable(Page.INGREDIENT_DATA_KEY);
        if (selections != null && selections.size() > 0) {
            for (Ingredient selection : selections) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(selection.getName());
            }
        }

        dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
    }
}
