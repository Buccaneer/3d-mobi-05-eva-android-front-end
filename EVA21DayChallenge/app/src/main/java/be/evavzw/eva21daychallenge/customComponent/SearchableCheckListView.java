package be.evavzw.eva21daychallenge.customComponent;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.models.profile_setup.AllergiesPage;
import be.evavzw.eva21daychallenge.rest.InternalLoginRestMethod;
import be.evavzw.eva21daychallenge.security.IngredientManager;

/**
 * Created by Jan on 15/11/2015.
 */
public class SearchableCheckListView extends LinearLayout implements TextWatcher {

    private IngredientManager ingredientManager;
    private CustomAdapter ingredientAdapter;
    private EditText ingredientText;
    private ListView ingredientList;
    private HashMap<Integer, Ingredient> checkedIngredients;
    private int ingredientTextLength = 0;
    private OnIngredientCheckedListener onIngredientCheckedListener;

    public SearchableCheckListView(Context context) {
        super(context);
        setupView();
    }

    public SearchableCheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public SearchableCheckListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    private void setupView() {
        ingredientManager = IngredientManager.getInstance(getContext());
        checkedIngredients = new HashMap<>();

        setOrientation(VERTICAL);

        ingredientAdapter = new CustomAdapter(getContext(), R.layout.ingredient_info, new ArrayList<Ingredient>());

        ingredientText = new EditText(getContext());
        addView(ingredientText);

        ingredientText.addTextChangedListener(this);

        ingredientList = new ListView(getContext());
        ingredientList.setAdapter(ingredientAdapter);

        addView(ingredientList);
    }

    public List<Ingredient> getCheckedIngredients() {
        return new ArrayList<>(checkedIngredients.values());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        ingredientTextLength = s.toString().length();
        if (ingredientTextLength != 0) {
            new GetIngredientsByNameTask().execute(s.toString());
        } else {
            ingredientAdapter.clear();
            ingredientAdapter.addAll(checkedIngredients.values());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putSerializable(AllergiesPage.INGREDIENT_DATA_KEY, checkedIngredients);
        bundle.putString("editTextValue", ingredientText.getText().toString());
        bundle.putInt("textLength", ingredientTextLength);
        bundle.putSerializable("currentItems", ingredientAdapter.getIngredientList());
        return bundle;
    }

    public void setCheckedIngredients(List<Ingredient> ingredients) {
        HashMap<Integer, Ingredient> ingredientmap = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            ingredientmap.put(ingredient.getIngredientId(), ingredient);
        }

        checkedIngredients = ingredientmap;

        ingredientAdapter.clear();
        ingredientAdapter.addAll(checkedIngredients.values());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Parcelable savedState = null;
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            checkedIngredients = (HashMap<Integer, Ingredient>) bundle.getSerializable(AllergiesPage.INGREDIENT_DATA_KEY);
            ingredientText.setText(bundle.getString("editTextValue"));
            ingredientTextLength = bundle.getInt("textLength");
            ingredientText.setSelection(ingredientTextLength);
            ingredientAdapter.clear();
            ingredientAdapter.addAll((ArrayList<Ingredient>) bundle.getSerializable("currentItems"));
            savedState = bundle.getParcelable("instanceState");
        }

        Editable editable = new SpannableStringBuilder(ingredientText.getText().toString());

        afterTextChanged(editable);

        super.onRestoreInstanceState(savedState);
    }

    private class CustomAdapter extends ArrayAdapter<Ingredient> {
        private ArrayList<Ingredient> ingredientList;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Ingredient> ingredientList) {
            super(context, textViewResourceId, ingredientList);
            this.ingredientList = new ArrayList<>();
            this.ingredientList.addAll(ingredientList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.ingredient_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Ingredient ingredient = (Ingredient) cb.getTag();
                        if (cb.isChecked()) {
                            checkedIngredients.put(ingredient.getIngredientId(), ingredient);
                            onIngredientCheckedListener.onChecked(new ArrayList<>(checkedIngredients.values()));
                        } else {
                            checkedIngredients.remove(ingredient.getIngredientId());
                            if(ingredientText.length() == 0){
                                ingredientAdapter.clear();
                                ingredientAdapter.addAll(checkedIngredients.values());
                                ingredientAdapter.notifyDataSetChanged();
                            }
                            onIngredientCheckedListener.onChecked(new ArrayList<>(checkedIngredients.values()));
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Ingredient ingredient = ingredientList.get(position);

            holder.name.setText(ingredient.getName());
            holder.name.setChecked(checkedIngredients.containsKey(ingredient.getIngredientId()));
            holder.name.setTag(ingredient);

            return convertView;
        }

        @Override
        public void addAll(Collection<? extends Ingredient> collection) {
            this.ingredientList.clear();
            this.ingredientList.addAll(collection);
            ingredientAdapter.notifyDataSetChanged();
            super.addAll(ingredientList);
        }

        @Override
        public void addAll(Ingredient... items) {
            this.ingredientList.clear();
            this.ingredientList.addAll(Arrays.asList(items));
            super.addAll(ingredientList);
        }

        public ArrayList<Ingredient> getIngredientList() {
            return ingredientList;
        }
    }

    public interface OnIngredientCheckedListener {
        void onChecked(ArrayList<Ingredient> chosenIngredients);
    }

    public void setOnIngredientCheckedListener(OnIngredientCheckedListener eventListener) {
        this.onIngredientCheckedListener = eventListener;
    }

    private class GetIngredientsByNameTask extends AsyncTask<String, Void, Boolean> {

        private List<Ingredient> ingredients = new ArrayList<>();


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                ingredients = ingredientManager.getIngredientsByName(params[0]);
                return true;
            } catch (Exception e) {
                throw e;
                //return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                ingredientAdapter.clear();
                ingredientAdapter.addAll(ingredients);
            }
        }
    }
}
