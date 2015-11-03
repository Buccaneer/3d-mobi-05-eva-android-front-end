package be.evavzw.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulFragment;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.security.RecipeManager;

/**
 * Created by Pieter-Jan on 24/10/2015.
 */
public class RecipeChallengeListFragment extends RESTfulFragment {
    RecipeManager recipeManager;

    final static String ARG_CATEGORY = "category";
    int currentCategory = -1;

    AppCompatActivity activity;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] items;
    Recipe[] recipes;

    OnRecipeSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnRecipeSelectedListener {
        //void onRecipeSelected(int category, int challenge);
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRecipeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onResume() {
        super.onResume();
        activity.getSupportActionBar().setTitle(R.string.title_recipeList);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            items = savedInstanceState.getStringArray("items");
            currentCategory = savedInstanceState.getInt("category");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putStringArray("items", items);
        outState.putInt("category", currentCategory);
    }

    private void init() {
        activity = (AppCompatActivity) getActivity();
        recipeManager = RecipeManager.getInstance(activity.getApplicationContext());
        listView = new ListView(activity);
        listView.setId(View.generateViewId());
        ListView.OnItemClickListener mMessageClickedHandler = new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int challenge, long id) {
                Log.e("ITEM", "ID=" + id + "\tVIEWID=" + v.getId());
                //mCallback.onRecipeSelected(currentCategory, challenge);
                mCallback.onRecipeSelected(recipes[challenge]);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setBackgroundColor(getResources().getColor(R.color.eva_lichtgrijs));
    }

    private void fetchChallenges() {
        FetchChallengesTask fetch = new FetchChallengesTask();
        fetch.execute();
    }

    class FetchChallengesTask extends AsyncTask<String, String, Boolean> {
        List<Recipe> list;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                list = recipeManager.getAllRecipes();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            //setRefresh(false);
            if (succeed) {
                recipes = new Recipe[list.size()];
                recipes = list.toArray(recipes);
                items = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                    items[i] = list.get(i).getName();
                adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        Resources resources = getResources();
                        View view = super.getView(position, convertView, parent);
                        view.setBackgroundColor(position % 2 == 0 ? resources.getColor(R.color.eva_lichtgrijs) : resources.getColor(R.color.eva_lichtgroen));
                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }
        }
    }

    /*private void setRefresh(final boolean toggle){
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                toggleProgressBar(toggle);
            }
        });
    }*/

}