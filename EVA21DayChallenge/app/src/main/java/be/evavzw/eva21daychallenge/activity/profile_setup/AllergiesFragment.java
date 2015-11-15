package be.evavzw.eva21daychallenge.activity.profile_setup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.profile_setup.AllergiesPage;
import be.evavzw.eva21daychallenge.models.profile_setup.UserInfoPage;

/**
 * Created by Jan on 15/11/2015.
 */
public class AllergiesFragment extends Fragment {
    private static final String ARG_KEY = "key";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AllergiesPage mPage;
    private EditText ingredientName;
    private ListView ingredientList;

    public AllergiesFragment(){}

    public static AllergiesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AllergiesFragment fragment = new AllergiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AllergiesPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_allergies, container, false);
        ((TextView) rootView.findViewById(R.id.pageTitle)).setText(mPage.getTitle());

        //ingredientName = ((EditText) rootView.findViewById(R.id.ingredientName));
        //ingredientName.setText(mPage.getData().getString(AllergiesPage.SURNAME_DATA_KEY));

        //ingredientList = ((ListView) rootView.findViewById(R.id.ingredientList));
        //ingredientList.setite.setText(mPage.getData().getString(UserInfoPage.SURNAME_DATA_KEY));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (ingredientName != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
