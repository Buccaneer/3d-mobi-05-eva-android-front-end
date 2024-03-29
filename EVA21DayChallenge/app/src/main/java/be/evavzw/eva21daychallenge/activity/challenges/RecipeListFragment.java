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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.categories.Category;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.services.ChallengeManager;
import be.evavzw.eva21daychallenge.services.RecipeManager;

/**
 * This Fragment is used to display a list of recipes
 */
public class RecipeListFragment extends ChallengeFragment {

    private ChallengeManager challengeManager;
    private RecipeManager recipeManager;
    private List<Recipe> recipes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.list, container, false);
        challengeManager = ChallengeManager.getInstance(getContext());
        recipeManager = RecipeManager.getInstance(getContext());
        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.challengeList);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        FrameLayout spinnerContainer = (FrameLayout) layout.findViewById(R.id.list_spinner_container);
        ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.list_spinner);
        TextView notFound = (TextView) layout.findViewById(R.id.not_found);
        fetchChallenges(rv, spinnerContainer, spinner, notFound);
        return layout;
    }

    //Add adapter to the RecyclerView
    private void setupRecyclerView(RecyclerView recyclerView, List<Recipe> recipes) {
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), recipes, getString(R.string.category_cooking_descr)));
    }

    /**
     * A custom Adapter with two types of ViewHolders
     */
    private static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Recipe> mValues;
        private String description;

        /**
         * A different ViewHolder, used to display the category description as the first item in the RecyclerView
         */
        public static class Description extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public Description(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.categoryAvatar);
                mTextView = (TextView) view.findViewById(R.id.categoryDescription);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        /**
         * The Viewholder used for actual items (recipes) in the RecyclerView
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Recipe mRecipe;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        public Recipe getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Recipe> recipes, String description) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = recipes;

            //Add the first item (recipe) twice because the Description ViewHolder overrides the first
            mValues.add(0, mValues.get(0));

            //For testing purposes only (testing scrolling up and down)
            /*mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));
            mValues.add(6, mValues.get(0));*/

            this.description = description;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_description, parent, false);
                return new Description(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                view.setBackgroundResource(mBackground);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, final int position) {
            if (holderr instanceof ViewHolder) {
                final ViewHolder holder = (ViewHolder) holderr;
                holder.mRecipe = mValues.get(position);
                holder.mTextView.setText(mValues.get(position).getName());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailActivity.RECIPE, holder.mRecipe);
                        context.startActivity(intent);
                    }
                });

                // TODO : Fix performantie / Out of memory
                Glide.with(holder.mImageView.getContext())
                        .load(holder.mRecipe.getImage())
//                        .placeholder(R.drawable.cook_green)
//                        .thumbnail(0.2f)
                        .into(holder.mImageView);

            } else if (holderr instanceof Description) {
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
        public int getItemCount() {
            return mValues.size();
        }
    }

    //Method that fetches the Challenges then calls setupRecyclerView
    private void fetchChallenges(RecyclerView rv, FrameLayout spinnerContainer, ProgressBar spinner, TextView notFound) {
        FetchChallengesTask fetch = new FetchChallengesTask(rv, spinnerContainer, spinner, notFound);
        fetch.execute();

        /** TIJDELIJK **/
        /*JSONArray obj = null;
        try {
            obj = new JSONArray(Mock.recipes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Recipe> recipes = new ArrayList<>();

        //voor elk object in de recipes (dus elk recept) de constructor van recept aanroepen met recipes stukje
        for (int i = 0; i < obj.length(); i++) {
            JSONObject jsonRow = null;
            try {
                jsonRow = obj.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                recipes.add(new Recipe(jsonRow));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.recipes = recipes;
        setupRecyclerView(rv);*/
        /** EINDE TIJDELIJK **/
    }

    /**
     * An Asynctask that uses the Rest Framework to fetch recipes
     */
    private class FetchChallengesTask extends AsyncTask<String, String, Boolean> {
        RecyclerView recyclerView;
        FrameLayout spinnerContainer;
        List<Recipe> recipes;
        ProgressBar spinner;
        TextView notFound;

        public FetchChallengesTask(RecyclerView recyclerView, FrameLayout spinnerContainer, ProgressBar spinner, TextView notFound) {
            super();
            this.recyclerView = recyclerView;
            this.spinnerContainer = spinnerContainer;
            this.spinner = spinner;
            this.notFound = notFound;
        }

        @Override
        protected void onPreExecute() {
            spinnerContainer.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            notFound.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                recipes = challengeManager.getRecipesForCategory(Category.COOKING);
                Log.e("RecipeListFragment", "Got recipes");
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if (isAdded()) //no callback if fragment is no longer added
            {
                Log.e("RecipeListFragment", "Post Execute called");
                if (succeed) { //TODO: Remove negation after testing
                    Log.e("RecipeListFragment", "Post Execute called and succeeded");
                    setupRecyclerView(recyclerView, recipes);
                    spinnerContainer.setVisibility(View.GONE);
                }
                else
                {
                    spinner.setVisibility(View.INVISIBLE);
                    notFound.setVisibility(View.VISIBLE);
                    notFound.setText(R.string.no_recipes_found);
                }
            }
        }
    }
}