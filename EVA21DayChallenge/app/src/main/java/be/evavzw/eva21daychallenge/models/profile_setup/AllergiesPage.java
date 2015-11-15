package be.evavzw.eva21daychallenge.models.profile_setup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.profile_setup.AllergiesFragment;
import be.evavzw.eva21daychallenge.activity.profile_setup.UserInfoFragment;

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

        ArrayList<String> selections = mData.getStringArrayList(Page.SIMPLE_DATA_KEY);
        if (selections != null && selections.size() > 0) {
            for (String selection : selections) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(selection);
            }
        }

        dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
    }

    @Override
    public boolean isCompleted() {
        ArrayList<String> selections = mData.getStringArrayList(Page.SIMPLE_DATA_KEY);
        return selections != null && selections.size() > 0;
    }
}
