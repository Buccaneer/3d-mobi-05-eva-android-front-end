package evavzw.be.eva21daychallenge.activity.challenges;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;

/**
 * Created by Pieter-Jan on 26/10/2015.
 */
public class RestaurantChallengeFragment extends Fragment
{
    final static String ARG_RESTAURANT = "restaurant";

    @Bind(R.id.restaurantTitle)
    TextView restaurantTitle;

    @Bind(R.id.restaurantAddress)
    TextView restaurantAddress;

    @Bind(R.id.restaurantProperties)
    TextView restaurantProperties;

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.restaurant_challenge, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            //updateChallenge(args.getInt(ARG_CATEGORY), args.getInt(ARG_CHALLENGE));
            // TODO
            updateChallenge();
        } /*else if (currentCategory != -1) {
            // Set article based on saved instance state defined during onCreateView
            //updateChallenge(currentCategory, currentChallenge);
            // TODO
        }*/
    }

    @Override
    public void onPause ()
    {
        super.onPause();
        // TODO
    }

    @Override
    public void onStop ()
    {
        super.onStop();
        // TODO
    }

    private void updateChallenge(/*TODO fill in Restaurant object once we get these from API*/) {
        String name = "Qorn'r";
        String address = "Landjuweelstraat 23\n9050 Ledeberg";
        String properties = "Eethuis\n100% vegetarisch\nApproved by EVA\nEVA voordeel\nVeganvriendelijk";
        restaurantTitle.setText(name);
        restaurantAddress.setText(address);
        restaurantProperties.setText(properties);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    private void init()
    {

    }

}
