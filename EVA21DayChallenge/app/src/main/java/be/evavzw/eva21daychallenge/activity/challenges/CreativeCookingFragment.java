package be.evavzw.eva21daychallenge.activity.challenges;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.customComponent.SearchableCheckListView;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.rest.GetNumberOfRecipesByIngredients;
import be.evavzw.eva21daychallenge.services.RecipeManager;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CreativeCookingFragment extends ChallengeFragment implements SearchableCheckListView.OnIngredientCheckedListener {

    private RecipeManager recipeManager;
    @Bind(R.id.numberOfRecipesFound)
    TextView recipesFound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_creative_cooking, container, false);

        ButterKnife.bind(this, layout);

        recipeManager = RecipeManager.getInstance(getContext());
        ((SearchableCheckListView) layout.findViewById(R.id.checkListView)).setOnIngredientCheckedListener(this);

        return layout;
    }

    @Override
    public void onChecked(ArrayList<Ingredient> chosenIngredients) {
        GetNumberOfIngredientsTask task = new GetNumberOfIngredientsTask();
        if (chosenIngredients.size() == 0) {
            recipesFound.setText(" 0");
        } else {
            task.execute(chosenIngredients);
        }
    }

    private void updateNumberOfRecipes(final int number) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recipesFound.setText(" " + number);
            }
        });
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
}
