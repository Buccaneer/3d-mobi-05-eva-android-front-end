package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.security.RecipeManager;

/**
 * Created by Pieter-Jan on 2/11/2015.
 */
public class RecipeListFragment extends ChallengeFragment
{

    RecipeManager recipeManager;

    List<Recipe> recipes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.list, container, false);
        //setupTitle(layout);
        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.challengeList);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        //setupRecyclerView(rv);
        fetchChallenges(rv);
        return layout;
    }

    /*private void setupTitle(LinearLayout layout)
    {
        ImageView iv = (ImageView) layout.findViewById(R.id.titleAvatar);
        Glide.with(iv.getContext())
                .load(Images.getRandomCheeseDrawable())
                .fitCenter()
                .into(iv);
        TextView tv = (TextView) layout.findViewById(R.id.titleText);
        tv.setText(getResources().getText(R.string.category_cooking));
    }*/

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), recipes, getString(R.string.category_cooking_descr)));
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Recipe> mValues;
        private String description;

        public static class Description extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public Description(View view)
            {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.categoryAvatar);
                mTextView = (TextView) view.findViewById(R.id.categoryDescription);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            public Recipe mRecipe;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            return position == 0 ? 0 : 1;
        }

        public Recipe getValueAt(int position)
        {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Recipe> recipes, String description)
        {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = recipes;
            mValues.add(0, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            this.description = description;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if (viewType == 0)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_description, parent, false);
                return new Description(view);
            }
            else
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                view.setBackgroundResource(mBackground);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, final int position)
        {
            if (holderr instanceof ViewHolder)
            {
                final ViewHolder holder = (ViewHolder) holderr;
                holder.mRecipe = mValues.get(position);
                holder.mTextView.setText(mValues.get(position).getName());

                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailActivity.RECIPE, holder.mRecipe);
                        context.startActivity(intent);
                    }
                });

                // TODO : Fix performantie / Out of memory
                Glide.with(holder.mImageView.getContext())
                        .load("" /*holder.mRestaurant.getImage()*/)
                        .placeholder(R.drawable.cook_green)
                        .thumbnail(0.2f)
                        .into(holder.mImageView);

                    /*Glide.with(holder.mImageView.getContext())
                            .load(Images.getRandomCheeseDrawable())
                            .fitCenter()
                            .into(holder.mImageView);*/
            } else if (holderr instanceof  Description){
                final Description holder = (Description) holderr;
                holder.mTextView.setText(description);
                Glide.with(holder.mImageView.getContext())
                        .load("" /*holder.mRestaurant.getImage()*/)
                        .placeholder(R.drawable.cook)
                        .thumbnail(0.2f)
                        .into(holder.mImageView);
            }

        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }
    }

    private void fetchChallenges(RecyclerView rv)
    {
        //FetchChallengesTask fetch = new FetchChallengesTask(rv);
        //fetch.execute();

        /** TIJDELIJK **/
        JSONArray obj = null;
        try
        {
            obj = new JSONArray(Mock.recipes);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        List<Recipe> recipes = new ArrayList<>();

        //voor elk object in de recipes (dus elk recept) de constructor van recept aanroepen met recipes stukje
        for (int i = 0; i < obj.length(); i++)
        {
            JSONObject jsonRow = null;
            try
            {
                jsonRow = obj.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                recipes.add(new Recipe(jsonRow));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        this.recipes = recipes;
        /** EINDE TIJDELIJK **/

        setupRecyclerView(rv);
    }

    class FetchChallengesTask extends AsyncTask<String, String, Boolean>
    {
        List<Recipe> list;
        RecyclerView recyclerView;

        public FetchChallengesTask(RecyclerView recyclerView)
        {
            super();
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Boolean doInBackground(String... objects)
        {
            try
            {
                list = recipeManager.getAllRecipes();
                Log.e("RecipeListFragment", "Got recipes");
                return true;
            }
            catch (Exception ex)
            {
                // connectie mislukt
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed)
        {
            //setRefresh(false);
            Log.e("RecipeListFragment", "Post Execute called");
            if (succeed)
            {
                recipes = list;
                setupRecyclerView(recyclerView);
            }
        }
    }
}
