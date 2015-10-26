package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import evavzw.be.eva21daychallenge.activity.base.RESTfulFragment;
import evavzw.be.eva21daychallenge.models.Recipe;
import evavzw.be.eva21daychallenge.security.RecipeManager;

/**
 * Created by Pieter-Jan on 24/10/2015.
 */
public class ChallengeListFragment extends RESTfulFragment
{
    RecipeManager recipeManager;

    final static String ARG_CATEGORY = "category";
    int currentCategory = -1;

    Activity activity;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] items;
    Recipe[] recipes;

    OnChallengeSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnChallengeSelectedListener {
        //void onChallengeSelected(int category, int challenge);
        void onChallengeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnChallengeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            currentCategory = savedInstanceState.getInt(ARG_CATEGORY);
        }
        return listView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateChallengeListView(args.getInt(ARG_CATEGORY));
        } else if (currentCategory != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateChallengeListView(currentCategory);
        }
    }

    @Override
    public void onPause ()
    {
        super.onPause();
    }

    @Override
    public void onStop ()
    {
        super.onStop();
    }

    private void updateChallengeListView(int position) {
        // TODO API
        fetchChallenges();
        //items = Mock.Challenges[position];
        //adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items);
        //listView.setAdapter(adapter);
        currentCategory = position;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
        {
            //Restore the fragment's state here
            items = savedInstanceState.getStringArray("items");
            currentCategory = savedInstanceState.getInt("category");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putStringArray("items", items);
        outState.putInt("category", currentCategory);
    }

    private void init()
    {
        activity = getActivity();
        recipeManager = RecipeManager.getInstance(activity.getApplicationContext());
        listView = new ListView(activity);
        listView.setId(View.generateViewId());
        ListView.OnItemClickListener mMessageClickedHandler = new ListView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int challenge, long id)
            {
                Log.e("ITEM", "ID=" + id + "\tVIEWID=" + v.getId());
                //mCallback.onChallengeSelected(currentCategory, challenge);
                mCallback.onChallengeSelected(recipes[challenge]);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setBackgroundColor(Color.CYAN);
    }

    private void fetchChallenges() {
        FetchChallengesTask fetch = new FetchChallengesTask();
        fetch.execute();
    }

    class FetchChallengesTask extends AsyncTask<String, String, Boolean>
    {
        List<Recipe> list;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                list = recipeManager.getAllRecipes();
                return true;
            } catch  (Exception ex) {
                // connectie mislukt
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            setRefresh(false);
            if(succeed){
                recipes = new Recipe[list.size()];
                recipes = list.toArray(recipes);
                items = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                    items[i] = list.get(i).getName();
                adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(adapter);
            }
        }
    }

    private void setRefresh(final boolean toggle){
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                toggleProgressBar(toggle);
            }
        });
    }

}