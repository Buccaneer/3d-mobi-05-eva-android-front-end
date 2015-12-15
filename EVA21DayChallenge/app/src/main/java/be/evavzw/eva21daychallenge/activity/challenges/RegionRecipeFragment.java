package be.evavzw.eva21daychallenge.activity.challenges;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.models.Restaurant;
import be.evavzw.eva21daychallenge.services.ChallengeManager;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RegionRecipeFragment extends ChallengeFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.regionRecipeSpinner)
    Spinner regionRecipeSpinner;
    @Bind(R.id.recipeList)
    RecyclerView recipeList;
    @Bind(R.id.list_spinner)
    ProgressBar spinner;

    private List<Recipe> recipes;
    private ChallengeManager challengeManager;
    private int currentSpinnerPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_region_recipe, container, false);

        ButterKnife.bind(this, layout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.recipe_regions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionRecipeSpinner.setAdapter(adapter);
        regionRecipeSpinner.setOnItemSelectedListener(this);
        regionRecipeSpinner.setSelection(currentSpinnerPosition);

        recipeList.setLayoutManager(new LinearLayoutManager(getContext()));

        challengeManager = ChallengeManager.getInstance(getContext());

        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("recipes")){
                recipes = (List<Recipe>) savedInstanceState.getSerializable("recipes");
            }
            if (savedInstanceState.containsKey("currentSpinnerPosition")) {
                currentSpinnerPosition = savedInstanceState.getInt("currentSpinnerPosition");
                if(recipes == null || recipes.isEmpty()){
                    switch (currentSpinnerPosition) {
                        case 0:
                            new FetchRecipesTask().execute("afrikaans");
                            break;
                        case 1:
                            new FetchRecipesTask().execute("oosters");
                            break;
                        case 2:
                            new FetchRecipesTask().execute("westers");
                            break;
                        case 3:
                            new FetchRecipesTask().execute("zuid-amerikaans");
                            break;
                    }
                }else{
                    setupRecyclerView(recipes);
                }
            }
        }else{
            new FetchRecipesTask().execute("afrikaans");
        }

        return layout;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = parent.getItemAtPosition(position).toString();
        String afrikaans = getString(R.string.afrikaans);
        String oosters = getString(R.string.oosters);
        String westers = getString(R.string.westers);
        String zuidAmerikaans = getString(R.string.zuidAmerikaans);

        if (currentSpinnerPosition != position) {
            spinner.setVisibility(View.VISIBLE);
            recipeList.setVisibility(View.GONE);
        }

        if (selected.equals(afrikaans)) {
            if (currentSpinnerPosition != position) {
                new FetchRecipesTask().execute("afrikaans");
            }
        }
        if (selected.equals(oosters)) {
            if (currentSpinnerPosition != position) {
                new FetchRecipesTask().execute("oosters");
            }
        }
        if (selected.equals(westers)) {
            if (currentSpinnerPosition != position) {
                new FetchRecipesTask().execute("westers");
            }
        }
        if (selected.equals(zuidAmerikaans)) {
            if (currentSpinnerPosition != position) {
                new FetchRecipesTask().execute("zuid-amerikaans");
            }
        }

        currentSpinnerPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentSpinnerPosition", currentSpinnerPosition);
        if(!recipes.isEmpty())
            outState.putSerializable("recipes", (ArrayList<Recipe>) recipes);
        super.onSaveInstanceState(outState);
    }

    private void setupRecyclerView(List<Recipe> recipes) {
        recipeList.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        this.recipes = recipes;
        recipeList.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), recipes));
    }

    /**
     * A custom Adapter with two types of ViewHolders
     */
    private static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Recipe> mValues;

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

        public Recipe getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Recipe> recipes) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = recipes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, final int position) {
            final ViewHolder holder = (ViewHolder) holderr;
            holder.mRecipe = mValues.get(position);
            holder.mTextView.setText(mValues.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailActivity.RECIPE, holder.mRecipe);
                    intent.putExtra("CALLED_FROM", "RegionRecipe");
                    context.startActivity(intent);
                }
            });

            // TODO : Fix performantie / Out of memory
            Glide.with(holder.mImageView.getContext())
                    .load(holder.mRecipe.getImage())
//                        .placeholder(R.drawable.cook_green)
//                        .thumbnail(0.2f)
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    private class FetchRecipesTask extends AsyncTask<String, Void, Boolean> {

        List<Recipe> recipes = new ArrayList<>();

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                recipes = challengeManager.getRecipesByRegion(params[0]);
                return true;
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), R.string.error500, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            recipeList.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            if (success && !recipes.isEmpty() && isAdded()) {
                setupRecyclerView(recipes);
            }
        }
    }
}
