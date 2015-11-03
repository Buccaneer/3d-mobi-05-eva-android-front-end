package be.evavzw.eva21daychallenge.models.profile_setup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.profile_setup.UserInfoFragment;

/**
 * A page asking for a name and an email.
 */
public class UserInfoPage extends Page {
    public static final String GIVEN_NAME_DATA_KEY = "givenname";
    public static final String SURNAME_DATA_KEY = "surname";
    public static final String AGE_DATA_KEY = "age";

    public UserInfoPage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title, context);
    }

    @Override
    public Fragment createFragment() {
        return UserInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        String givenname = mContext.getResources().getString(R.string.givenname);
        String surname = mContext.getResources().getString(R.string.surname);
        String age = mContext.getResources().getString(R.string.age);
        dest.add(new ReviewItem(givenname, mData.getString(GIVEN_NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem(surname, mData.getString(SURNAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem(age, mData.getString(AGE_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(GIVEN_NAME_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(SURNAME_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(AGE_DATA_KEY));
    }
}
