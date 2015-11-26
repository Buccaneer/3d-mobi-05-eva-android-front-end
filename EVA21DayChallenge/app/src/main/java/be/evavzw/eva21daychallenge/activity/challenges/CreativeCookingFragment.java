package be.evavzw.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.customComponent.SearchableCheckListView;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.Recipe;
import be.evavzw.eva21daychallenge.rest.GetNumberOfRecipesByIngredients;
import be.evavzw.eva21daychallenge.services.RecipeManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreativeCookingFragment extends ChallengeFragment implements SearchableCheckListView.OnIngredientCheckedListener {

    private RecipeManager recipeManager;
    @Bind(R.id.numberOfRecipesFound)
    TextView recipesFound;
    private GetNumberOfIngredientsTask task;
    private int numberFound;
    @Bind(R.id.checkListView)
    SearchableCheckListView searchableCheckListView;
    @Bind(R.id.selectListView)
    LinearLayout selectListView;
    @Bind(R.id.recipeListview)
    LinearLayout recipeListView;
    private CustomAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_creative_cooking, container, false);

        ButterKnife.bind(this, layout);

        recipeManager = RecipeManager.getInstance(getContext());
        ((SearchableCheckListView) layout.findViewById(R.id.checkListView)).setOnIngredientCheckedListener(this);

        ListView recipeList = new ListView(getContext());
        listAdapter = new CustomAdapter(getContext(), R.layout.list_description, new ArrayList<Recipe>());

        recipeListView.addView(recipeList);
        recipeList.setAdapter(listAdapter);

        recipeListView.setVisibility(View.GONE);
        return layout;
    }

    @Override
    public void onChecked(ArrayList<Ingredient> chosenIngredients) {
        if(task !=  null)
            task.cancel(false);

        task = new GetNumberOfIngredientsTask();
        if (chosenIngredients.size() == 0) {
            numberFound = 0;
            recipesFound.setText(" 0");
        } else {
            task.execute(chosenIngredients);
        }
    }

    private void updateNumberOfRecipes(final int number) {
        this.numberFound = number;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recipesFound.setText(" " + number);
            }
        });
    }

    @OnClick(R.id.btnContinue)
    public void btnContinueClicked(View v){
        if(numberFound != 0){
            selectListView.setVisibility(View.GONE);
            recipeListView.setVisibility(View.VISIBLE);
            new GetRecipeListTask().execute(searchableCheckListView.getCheckedIngredients());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey("numberFound")){
            recipesFound.setText(savedInstanceState.getString("numberFound"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberFound", recipesFound.getText().toString());
    }

    private class CustomAdapter extends ArrayAdapter<Recipe>{
        private ArrayList<Recipe> recipeList;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipeList) {
            super(context, textViewResourceId, recipeList);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.recipeList= new ArrayList<>();
            this.recipeList.addAll(recipeList);
        }

        private class ViewHolder{
            public View mView;
            public ImageView mImageView;
            public TextView mTextView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_description, null);

                holder = new ViewHolder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.categoryAvatar);
                convertView.setTag(holder);

                holder.mTextView = (TextView) convertView.findViewById(R.id.categoryDescription);

                convertView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        LinearLayout linearLayout = (LinearLayout)v;
                        Recipe recipe = (Recipe) linearLayout.getTag(R.id.recipeTag);
                        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailActivity.RECIPE, recipe);
                        intent.putExtra("CALLED_FROM", "CCC");
                        getContext().startActivity(intent);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setBackgroundResource(mBackground);
            holder.mTextView.setBackgroundResource(R.color.tw__transparent);
            holder.mTextView.setTextColor(Color.BLACK);


            Recipe recipe = recipeList.get(position);
            holder.mTextView.setText(recipe.getName());

            convertView.setTag(R.id.recipeTag, recipe);

            Glide.with(holder.mImageView.getContext())
                    .load("" /*holder.mRestaurant.getImage()*/)
                    .placeholder(R.drawable.cook_green)
                    .thumbnail(0.2f)
                    .into(holder.mImageView);

            return convertView;
        }

        @Override
        public void addAll(Collection<? extends Recipe> collection) {
            this.recipeList.clear();
            this.recipeList.addAll(collection);
            listAdapter.notifyDataSetChanged();
            super.addAll(recipeList);
        }

        @Override
        public void addAll(Recipe... items) {
            this.recipeList.clear();
            this.recipeList.addAll(Arrays.asList(items));
            super.addAll(recipeList);
        }
    }

    private class GetNumberOfIngredientsTask extends AsyncTask<ArrayList<Ingredient>, Void, Boolean> {

        private int nrOfRecipes = 0;

        @Override
        protected Boolean doInBackground(ArrayList<Ingredient>... params) {
            try {
                nrOfRecipes = recipeManager.getNumberOfRecipes(params[0]);
                return true;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (recipesFound.getText().length() != 0)
                    updateNumberOfRecipes(nrOfRecipes);
            }
        }
    }

    private class GetRecipeListTask extends AsyncTask<List<Ingredient>, Void, Boolean>{

        private List<Recipe> foundRecipes;

        @Override
        protected Boolean doInBackground(List<Ingredient>... params) {
            try{
                foundRecipes = recipeManager.getRecipesByIngredients(params[0]);
                return true;
            }catch (Exception e){
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success && foundRecipes != null){
                listAdapter.clear();
                listAdapter.addAll(foundRecipes);
            }
        }
    }
}
