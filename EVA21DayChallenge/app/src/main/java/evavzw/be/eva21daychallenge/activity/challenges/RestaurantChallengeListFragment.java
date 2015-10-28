package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.activity.base.RESTfulFragment;

/**
 * Created by Pieter-Jan on 26/10/2015.
 */
public class RestaurantChallengeListFragment extends RESTfulFragment
{
    //RestaurantManager restaurantManager; DOES NOT EXIST YET

    final static String ARG_RESTAURANT = "restaurant";
    int currentCategory = -1;

    Activity activity;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] items;
    //Restaurant[] restaurants;

    OnRestaurantSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnRestaurantSelectedListener
    {
        void onRestaurantSelected(/*Restaurant restaurant*/);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRestaurantSelectedListener) activity;
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
            currentCategory = savedInstanceState.getInt(ARG_RESTAURANT);
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
            updateChallengeListView(args.getInt(ARG_RESTAURANT));
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
        //fetchChallenges();
        items = Mock.Restaurants;
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Resources resources = getResources();
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(position % 2 == 0 ? resources.getColor(R.color.eva_wit) : resources.getColor(R.color.eva_lichtgroen));
                return view;
            }
        };
        listView.setAdapter(adapter);
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
        listView = new ListView(activity);
        listView.setId(View.generateViewId());
        ListView.OnItemClickListener mMessageClickedHandler = new ListView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int challenge, long id)
            {
                Log.e("ITEM", "ID=" + id + "\tVIEWID=" + v.getId());
                //mCallback.onRecipeSelected(currentCategory, challenge);
                mCallback.onRestaurantSelected(/*restaurants[challenge]*/);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        listView.setBackgroundColor(Color.WHITE);
        //restaurantManager = RestaurantManager.getInstance(activity.getApplicationContext());
    }

    /*private void fetchChallenges() {
        FetchChallengesTask fetch = new FetchChallengesTask();
        fetch.execute();
    }

    class FetchChallengesTask extends AsyncTask<String, String, Boolean>
    {
        List<Restaurant> list;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                list = restaurantManager.getAllRestaurants();
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
                restaurants = new Recipe[list.size()];
                restaurants = list.toArray(restaurants);
                items = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                    items[i] = list.get(i).getName();
                adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(adapter);
            }
        }
    }*/

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
