package be.evavzw.eva21daychallenge.activity.challenges;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Ingredient;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity which allows the user to pick challenges
 * Contains a TabLayout with a Tab for every category of Challenges
 * Each Tab contains a list with the available challenges in that category
 */
public class ChallengeActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;

    /*
     *  Config is used to keep track of the screen and layout used
     *  e.g. Portait and Land for smartphones, Large for tablets
     */
    private String config = "undefined";
    private final String PORTRAIT = "portrait";
    private final String LAND = "land";
    private final String LARGE = "large";


    //Position is used to keep track of the current Tab position
    public static String POSITION = "POSITION";
    private int pos = -1;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    TabLayout tabs;

    private SharedPreferences mPrefs;
    private RestaurantListFragment restaurantListFragment;
    private CreativeCookingFragment creativeCookingFragment;

    /**
     * Saves the current Tab position on pause
     */
    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt(POSITION, pos);
        ed.commit();
    }

    /**
     * Loads the last known Tab position if any
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        if (pos != -1)
            viewPager.setCurrentItem(pos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ButterKnife.bind(this);

        // Load last Tab position from preferences
        mPrefs = getSharedPreferences("tab", MODE_PRIVATE);
        pos = mPrefs.getInt(POSITION, -1);

        // Set the config based on XML used
        config = getString(R.string.selected_config);
        fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null)
        {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        restaurantListFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_with_actions, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    /**
     * Sets the ViewPager up by adding Fragments for every category of challenges
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager)
    {
        this.restaurantListFragment = new RestaurantListFragment();
        this.creativeCookingFragment = new CreativeCookingFragment();

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new RecipeListFragment(), getString(R.string.category_cooking));
        adapter.addFragment(restaurantListFragment, getString(R.string.category_restaurant));
        adapter.addFragment(new TextDetailFragment(), getString(R.string.category_sugarfree));
        adapter.addFragment(creativeCookingFragment, getString(R.string.CCC));


        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                // Update position when a new Tab / Page is selected
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    /**
     * An adapter.
     */
    private class Adapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private FragmentManager fm;

        public Adapter(FragmentManager fm)
        {
            super(fm);
            this.fm = fm;
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitles.get(position);
        }
    }

}