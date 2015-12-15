package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.MainMenu;
import be.evavzw.eva21daychallenge.services.ChallengeManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Annemie on 19/11/2015.
 */
public class TextDetailFragment extends ChallengeFragment
{

    private ChallengeManager challengeManager;

    private Context c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_sugarfree_challenge, container, false);
        ImageView img = (ImageView) layout.findViewById(R.id.sugar_img);
        c = container.getContext().getApplicationContext();
        challengeManager = ChallengeManager.getInstance(c);
        Glide.with(c)
                .load(R.drawable.koe).into(img);

        ButterKnife.bind(this, layout);
        return layout;
    }


    @OnClick(R.id.addChallenge)
    public void addChallenge()
    {
        Log.i("addchallenge", "click");
        new AddChallengeTask().execute();
    }

    private class AddChallengeTask extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                challengeManager.addChallenge("Suikervrij", 0);
                return true;
            }
            catch (Exception e)
            {
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
        protected void onPostExecute(Boolean success)
        {
            if (success)
            {
                Intent intent = new Intent(getActivity(), MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
