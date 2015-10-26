package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.Recipe;


public class ChallengeActivity extends AppCompatActivity implements CategoryListFragment.OnCategorySelectedListener, ChallengeListFragment.OnChallengeSelectedListener
{
    FragmentManager fragmentManager;
    CategoryListFragment categoryListFragment;
    ChallengeListFragment challengeListFragment;
    ChallengeFragment challengeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        //fragmentManager.addOnBackStackChangedListener(this);

        setContentView(R.layout.activity_challenge);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        /*if (findViewById(R.id.fragment_container) != null)
        {
            if (savedInstanceState != null)
            {
                categoryListFragment = (CategoryListFragment) fragmentManager.getFragment(savedInstanceState, "categoryListFragment");
                challengeListFragment = (ChallengeListFragment) fragmentManager.getFragment(savedInstanceState, "challengeListFragment");
                challengeFragment = (ChallengeFragment) fragmentManager.getFragment(savedInstanceState, "challengeFragment");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (categoryListFragment != null)
                    fragmentTransaction.add(R.id.fragment_container, categoryListFragment);
                if (challengeListFragment != null)
                    fragmentTransaction.add(R.id.fragment_container, challengeListFragment);
                if (challengeFragment != null)
                    fragmentTransaction.add(R.id.fragment_container, challengeFragment);
                fragmentTransaction.commit();
            }
            else
            {
                categoryListFragment = new CategoryListFragment();
                categoryListFragment.setArguments(getIntent().getExtras());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, categoryListFragment);
                fragmentTransaction.commit();
            }
        }*/

        if (savedInstanceState == null)
        {
            Log.e("ACTIVITY", "SAVED INSTANCE IS NULL");
            categoryListFragment = new CategoryListFragment();
            categoryListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, categoryListFragment, "categoryListFragment");
            fragmentTransaction.commit();

        }
        /*else
        {
            categoryListFragment = (CategoryListFragment) fragmentManager.getFragment(savedInstanceState, "categoryListFragment");
            challengeListFragment = (ChallengeListFragment) fragmentManager.getFragment(savedInstanceState, "challengeListFragment");
            challengeFragment = (ChallengeFragment) fragmentManager.getFragment(savedInstanceState, "challengeFragment");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (categoryListFragment != null)
                fragmentTransaction.add(R.id.fragment_container, categoryListFragment);
            if (challengeListFragment != null)
                fragmentTransaction.add(R.id.fragment_container, challengeListFragment);
            if (challengeFragment != null)
                fragmentTransaction.add(R.id.fragment_container, challengeFragment);
            fragmentTransaction.commit();
        }*/
    }

    @Override
    public void onCategorySelected(int category)
    {
        //Create list with challenges
        challengeListFragment = new ChallengeListFragment();
        Bundle args = new Bundle();
        args.putInt(ChallengeListFragment.ARG_CATEGORY, category);
        challengeListFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, challengeListFragment, "challengeListFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*@Override
    public void onChallengeSelected(int category, int challenge)
    {
        //Create list with challenges
        challengeFragment = new ChallengeFragment();
        Bundle args = new Bundle();
        args.putInt(ChallengeFragment.ARG_CATEGORY, category);
        args.putInt(ChallengeFragment.ARG_CHALLENGE, challenge);
        challengeFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, challengeFragment, "challengeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }*/
    @Override
    public void onChallengeSelected(Recipe recipe)
    {
        //Create list with challenges
        challengeFragment = new ChallengeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ChallengeFragment.ARG_RECIPE, recipe);
        /*args.put(ChallengeFragment.ARG_INGREDIENTS, recipe.getIngredients().);
        args.putString(ChallengeFragment.ARG_PROPERTIES, recipe.getName());
        args.putString(ChallengeFragment.ARG_DESCRIPTION, recipe.getName());
        args.putString(ChallengeFragment.ARG_IMAGE, recipe.getImage());*/

        challengeFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, challengeFragment, "challengeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        //This method is called when the up button is pressed. Just the pop back stack.
        fragmentManager.popBackStack();
        return true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // TODO : Save here?
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.e("ACTIVITY", "SAVE INSTANCE STATE CALLED");
        super.onSaveInstanceState(outState);
        if (categoryListFragment != null && categoryListFragment.isAdded())
            fragmentManager.putFragment(outState, "categoryListFragment", categoryListFragment);
        if (challengeListFragment != null && challengeListFragment.isAdded())
            fragmentManager.putFragment(outState, "challengeListFragment", challengeListFragment);
        if (challengeFragment != null && challengeFragment.isAdded())
            fragmentManager.putFragment(outState, "challengeFragment", challengeFragment);
    }

}
