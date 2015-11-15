package be.evavzw.eva21daychallenge.customComponent;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.Ingredient;
import be.evavzw.eva21daychallenge.security.IngredientManager;
import be.evavzw.eva21daychallenge.security.RecipeManager;

/**
 * Created by Jan on 15/11/2015.
 */
public class SearchableCheckListView extends LinearLayout implements TextWatcher {

    private IngredientManager ingredientManager;
    private CustomAdapter ingredientAdapter;
    private EditText ingredientText;
    private ListView ingredientList;
    private List<Ingredient> checkedIngredients;

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

    private void setupView(){
        ingredientManager = IngredientManager.getInstance(getContext());
        checkedIngredients = new ArrayList<>();

        setOrientation(VERTICAL);

        ingredientAdapter = new CustomAdapter(getContext(), R.layout.ingredient_info, new ArrayList<Ingredient>());

        ingredientText = new EditText(getContext());
        addView(ingredientText);

        ingredientText.addTextChangedListener(this);

        ingredientList = new ListView(getContext());
        ingredientList.setAdapter(ingredientAdapter);

        addView(ingredientList);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().length() != 0){
            new GetIngredientsByNameTask().execute(s.toString());
        }
        Log.e("TEXTCHANGED", s.toString());
    }

    private class CustomAdapter extends ArrayAdapter<Ingredient>{
        private List<Ingredient> ingredientList;

        public CustomAdapter(Context context, int textViewResourceId, List<Ingredient> ingredientList){
            super(context, textViewResourceId, ingredientList);
            this.ingredientList = new ArrayList<>();
            this.ingredientList.addAll(ingredientList);
        }

        private class ViewHolder{
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.ingredient_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Ingredient ingredient = (Ingredient) cb.getTag();
                        if(cb.isChecked()){
                            Log.i("add", ingredient.getName());
                            checkedIngredients.add(ingredient);
                        }else{
                            Log.i("remove", ingredient.getName());
                            checkedIngredients.remove(ingredient);
                        }
                        Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ingredient ingredient = ingredientList.get(position);
            holder.code.setText(" (" +  ingredient.getIngredientId() + ")");
            holder.name.setText(ingredient.getName());
            holder.name.setChecked(checkedIngredients.contains(ingredient));
            holder.name.setTag(ingredient);

            return convertView;
        }

        @Override
        public void addAll(Collection<? extends Ingredient> collection) {
            super.addAll(collection);
            this.ingredientList.clear();
            this.ingredientList.addAll(collection);
        }

//        private void checkButtonClick() {
//            Button myButton = (Button) findViewById(R.id.findSelected);
//            myButton.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                    StringBuffer responseText = new StringBuffer();
//                    responseText.append("The following were selected...\n");
//
//                    ArrayList<Ingredient> ingredientList = dataAdapter.ingre;
//                    for (int i = 0; i < countryList.size(); i++) {
//                        Ingredient country = countryList.get(i);
//                        if (country.isSelected()) {
//                            responseText.append("\n" + country.getName());
//                        }
//                    }
//
//                    Toast.makeText(getApplicationContext(),
//                            responseText, Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//        }
    }

    private class GetIngredientsByNameTask extends AsyncTask<String, Void, Boolean>{

        private List<Ingredient> ingredients = new ArrayList<>();


        @Override
        protected Boolean doInBackground(String... params) {
            try{
                ingredients = ingredientManager.getIngredientsByName(params[0]);
                return true;
            }catch(Exception e){
                throw e;
                //return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                ingredientAdapter.clear();
                ingredientAdapter.addAll(ingredients);
            }
        }
    }
}
