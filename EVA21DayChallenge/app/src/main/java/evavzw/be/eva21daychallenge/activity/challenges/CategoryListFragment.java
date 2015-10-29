package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ResourceBundle;

import evavzw.be.eva21daychallenge.R;

/**
 * Created by Pieter-Jan on 24/10/2015.
 */
public class CategoryListFragment extends Fragment
{
    AppCompatActivity activity;
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] items;

    OnCategorySelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnCategorySelectedListener
    {
        void onCategorySelected(int category);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            mCallback = (OnCategorySelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        activity.getSupportActionBar().setTitle(R.string.title_categoryList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return listView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
        {
            //Restore the fragment's state here
            items = savedInstanceState.getStringArray("items");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        //outState.putStringArray("items", items);
    }

    private void init()
    {
        activity = (AppCompatActivity) getActivity();
        items = Mock.Categories;
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Resources resources = getResources();
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(position % 2 == 0 ? resources.getColor(R.color.eva_lichtgrijs) : resources.getColor(R.color.eva_lichtgroen));
                return view;
            }
        };
        listView = new ListView(activity);
        listView.setId(View.generateViewId());
        //listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        listView.setAdapter(adapter);
        listView.setBackgroundColor(getResources().getColor(R.color.eva_lichtgrijs));
        // Create a message handling object as an anonymous class.
        ListView.OnItemClickListener mMessageClickedHandler = new ListView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                Log.e("ITEM", "ID=" + id + "\tPOSITION=" + position);
                mCallback.onCategorySelected(position);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }
}
