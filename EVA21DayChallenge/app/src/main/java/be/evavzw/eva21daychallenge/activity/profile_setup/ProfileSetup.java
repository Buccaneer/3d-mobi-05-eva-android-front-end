package be.evavzw.eva21daychallenge.activity.profile_setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.MainMenu;
import be.evavzw.eva21daychallenge.models.profile_setup.AbstractWizardModel;
import be.evavzw.eva21daychallenge.models.profile_setup.ModelCallbacks;
import be.evavzw.eva21daychallenge.models.profile_setup.Page;
import be.evavzw.eva21daychallenge.models.profile_setup.SingleFixedChoicePage;
import be.evavzw.eva21daychallenge.models.profile_setup.UserInfoPage;

public class ProfileSetup extends android.support.v4.app.FragmentActivity implements PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel;

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    private String calledFrom = "";

    public void onCreate(Bundle savedInstanceState) {
        mWizardModel = new ProfileWizardModel(getApplicationContext());

        super.onCreate(savedInstanceState);

        calledFrom = getIntent().getExtras().getString("CALLED_FROM");

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
            calledFrom = savedInstanceState.getString("CALLED_FROM");
        }

        setContentView(R.layout.activity_profile_setup);

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    DialogFragment dg = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            return new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.submit_confirm_message)
                                    .setPositiveButton(R.string.submit_confirm_button, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            reviewPositiveClick();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .create();
                        }
                    };
                    dg.show(getSupportFragmentManager(), "submit_settings_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();

        Bundle bundle = new Bundle();
        Bundle bundle1 = new Bundle();
        String personalInfo = getApplicationContext().getResources().getString(R.string.personalInfo);
        bundle1.putString(UserInfoPage.AGE_DATA_KEY, "27-05-1992");
        bundle1.putString(UserInfoPage.SURNAME_DATA_KEY, "Louwagie");
        bundle1.putString(UserInfoPage.GIVEN_NAME_DATA_KEY, "Fien");
        bundle.putBundle(personalInfo, bundle1);

        Bundle bundle2 = new Bundle();
        String budgetAmount = getApplicationContext().getResources().getString(R.string.budgetAmount);
        String notShared = getApplicationContext().getResources().getString(R.string.notShared);
        bundle2.putString(SingleFixedChoicePage.SIMPLE_DATA_KEY, notShared);
        bundle.putBundle(budgetAmount, bundle2);

        Bundle bundle3 = new Bundle();
        String typeOfVegetarian = getApplicationContext().getResources().getString(R.string.typeOfVegetarian);
        String vegan = getApplicationContext().getResources().getString(R.string.vegan);
        bundle3.putString(SingleFixedChoicePage.SIMPLE_DATA_KEY, vegan);
        bundle.putBundle(typeOfVegetarian, bundle3);

        Bundle bundle4 = new Bundle();
        String numberHousehold = getApplicationContext().getResources().getString(R.string.numberHousehold);
        bundle4.putString(SingleFixedChoicePage.SIMPLE_DATA_KEY, String.valueOf(2));
        bundle.putBundle(numberHousehold, bundle4);

        mWizardModel.load(bundle);
    }

    private void reviewPositiveClick() {
        //TODO: retrieve all items filled in by the user and send it to the server + set it in sharedpreferences maybe?
        Intent intent = new Intent(this, MainMenu.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
        outState.putString("CALLED_FROM", calledFrom);
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
}
