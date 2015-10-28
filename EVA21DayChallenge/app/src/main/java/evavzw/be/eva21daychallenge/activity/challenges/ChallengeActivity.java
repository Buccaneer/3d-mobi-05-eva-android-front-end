package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.models.Recipe;


public class ChallengeActivity extends AppCompatActivity implements CategoryListFragment.OnCategorySelectedListener, RecipeChallengeListFragment.OnRecipeSelectedListener, RestaurantChallengeListFragment.OnRestaurantSelectedListener
{
    FragmentManager fragmentManager;

    private boolean large = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        //fragmentManager.addOnBackStackChangedListener(this);

        setContentView(R.layout.activity_challenge);
        large = findViewById(R.id.challengeListFrame) != null;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        /*if (findViewById(R.id.fragment_container) != null)
        {
            if (savedInstanceState != null)
            {
                categoryListFragment = (CategoryListFragment) fragmentManager.getFragment(savedInstanceState, "categoryListFragment");
                challengeListFragment = (RecipeChallengeListFragment) fragmentManager.getFragment(savedInstanceState, "challengeListFragment");
                challengeFragment = (RecipeChallengeFragment) fragmentManager.getFragment(savedInstanceState, "challengeFragment");
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
            CategoryListFragment categoryListFragment = new CategoryListFragment();
            categoryListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, categoryListFragment, "categoryListFragment");
            fragmentTransaction.commit();

        }
        /*else
        {
            categoryListFragment = (CategoryListFragment) fragmentManager.getFragment(savedInstanceState, "categoryListFragment");
            challengeListFragment = (RecipeChallengeListFragment) fragmentManager.getFragment(savedInstanceState, "challengeListFragment");
            challengeFragment = (RecipeChallengeFragment) fragmentManager.getFragment(savedInstanceState, "challengeFragment");
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
        // TODO make these check for actual categories rather than position once API supports categories
        switch (category)
        {
            case 0:
                //Create list with challenges
                RecipeChallengeListFragment recipeListFragment = new RecipeChallengeListFragment();
                Bundle args0 = new Bundle();
                args0.putInt(RecipeChallengeListFragment.ARG_CATEGORY, category);
                recipeListFragment.setArguments(args0);

                //Fragment transaction to show list with challenges
                FragmentTransaction transaction0 = fragmentManager.beginTransaction();
                if (large)
                {
                    transaction0.replace(R.id.challengeListFrame, recipeListFragment, "challengeListFragment");
                } else {
                    transaction0.replace(R.id.fragment_container, recipeListFragment, "challengeListFragment");
                    transaction0.addToBackStack(null);
                }
                transaction0.commit();
                break;
            case 1:
                //Create list with challenges
                RestaurantChallengeListFragment restaurantListFragment = new RestaurantChallengeListFragment();
                Bundle args1 = new Bundle();
                args1.putInt(RecipeChallengeListFragment.ARG_CATEGORY, category);
                restaurantListFragment.setArguments(args1);

                //Fragment transaction to show list with challenges
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                if (large)
                {
                    Log.e("ACTIVITY", "ACTING LIKE A TABLET");
                    transaction1.replace(R.id.challengeListFrame, restaurantListFragment, "challengeListFragment");
                } else {
                    Log.e("ACTIVITY", "ACTING LIKE A PHONE");
                    transaction1.replace(R.id.fragment_container, restaurantListFragment, "challengeListFragment");
                    transaction1.addToBackStack(null);
                }
                transaction1.commit();
                break;
            default:
                break;
        }

    }

    /*@Override
    public void onRecipeSelected(int category, int challenge)
    {
        //Create list with challenges
        challengeFragment = new RecipeChallengeFragment();
        Bundle args = new Bundle();
        args.putInt(RecipeChallengeFragment.ARG_CATEGORY, category);
        args.putInt(RecipeChallengeFragment.ARG_CHALLENGE, challenge);
        challengeFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, challengeFragment, "challengeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    @Override
    public void onRecipeSelected(Recipe recipe)
    {
        //Create list with challenges
        RecipeChallengeFragment challengeFragment = new RecipeChallengeFragment();
        Bundle args = new Bundle();
        args.putSerializable(RecipeChallengeFragment.ARG_RECIPE, recipe);
        /*args.put(RecipeChallengeFragment.ARG_INGREDIENTS, recipe.getIngredients().);
        args.putString(RecipeChallengeFragment.ARG_PROPERTIES, recipe.getName());
        args.putString(RecipeChallengeFragment.ARG_DESCRIPTION, recipe.getName());
        args.putString(RecipeChallengeFragment.ARG_IMAGE, recipe.getImage());*/

        challengeFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (large)
        {
            transaction.replace(R.id.challengeDetailFrame, challengeFragment, "challengeFragment");
        } else {
            transaction.replace(R.id.fragment_container, challengeFragment, "challengeFragment");
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onRestaurantSelected()
    {
        //Create list with challenges
        RestaurantChallengeFragment challengeFragment = new RestaurantChallengeFragment();
        Bundle args = new Bundle();

        challengeFragment.setArguments(args);

        //Fragment transaction to show list with challenges
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (large)
        {
            transaction.replace(R.id.challengeDetailFrame, challengeFragment, "challengeFragment");
        } else {
            transaction.replace(R.id.fragment_container, challengeFragment, "challengeFragment");
            transaction.addToBackStack(null);
        }
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
        /*if (categoryListFragment != null && categoryListFragment.isAdded())
            fragmentManager.putFragment(outState, "categoryListFragment", categoryListFragment);
        if (challengeListFragment != null && challengeListFragment.isAdded())
            fragmentManager.putFragment(outState, "challengeListFragment", challengeListFragment);
        if (challengeFragment != null && challengeFragment.isAdded())
            fragmentManager.putFragment(outState, "challengeFragment", challengeFragment);*/
    }

}
