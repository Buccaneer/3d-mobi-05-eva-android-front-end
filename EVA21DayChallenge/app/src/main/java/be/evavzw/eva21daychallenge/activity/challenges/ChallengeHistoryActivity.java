package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.services.ChallengeManager;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ChallengeHistoryActivity extends AppCompatActivity {

    @Bind(R.id.historyList)
    RecyclerView historyView;

    @Bind(R.id.historyProgress)
    ProgressBar historyProgress;

    @Bind(R.id.historyNothingFound)
    TextView historyNothingFound;

    private ChallengeManager challengeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_history);

        challengeManager = ChallengeManager.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        historyView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new FetchChallengeHistoryTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupRecyclerView(List<Challenge> challenges) {
        historyView.setAdapter(new CardViewRecyclerViewAdapter(getApplicationContext(), challenges));
        historyView.setVisibility(View.VISIBLE);
        historyProgress.setVisibility(View.GONE);
    }

    private static class CardViewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Challenge> mChallenges;
        private Context context;

        private static class ViewHolder extends RecyclerView.ViewHolder {
            public Challenge mChallenge;
            public final View mView;
            public final TextView mChallengeCardName;
            public final ImageView mChallengeCardImage;
            public final TextView mChallengeCardDetails;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mChallengeCardName = (TextView) view.findViewById(R.id.challengeCardName);
                mChallengeCardImage = (ImageView) view.findViewById(R.id.challengeCardImage);
                mChallengeCardDetails = (TextView) view.findViewById(R.id.challengeCardDetails);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mChallengeCardName.getText();
            }
        }

        public Challenge getValueAt(int position) {
            return mChallenges.get(position);
        }

        public CardViewRecyclerViewAdapter(Context context, List<Challenge> challenges) {
            this.mChallenges = challenges;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, int position) {
            final ViewHolder holder = (ViewHolder) holderr;
            holder.mChallenge = mChallenges.get(position);
            holder.mChallengeCardName.setText(splitstring(holder.mChallenge.getType()) + " " + context.getString(R.string.challenge));

            StringBuilder builder = new StringBuilder();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date = format.format(holder.mChallenge.getDate());
            builder.append(context.getString(R.string.date)).append(" ").append(date);
            builder.append("\n").append(context.getString(R.string.challengeDone)).append(" ").append(holder.mChallenge.isDone() ? context.getString(R.string.yes) : context.getString(R.string.no));
            if (holder.mChallenge.isDone()) {
                builder.append("\n").append(context.getString(R.string.earnedPoints)).append(" ").append(holder.mChallenge.getEarnings());
            } else {
                builder.append("\n").append(context.getString(R.string.earnedPoints)).append(" ").append(0);
            }

            holder.mChallengeCardDetails.setText(builder.toString());

            switch (holder.mChallenge.getType()) {
                case "Restaurant":
                    Glide.with(context)
                            .load(R.drawable.restaurant)
                            .centerCrop()
                            .into(holder.mChallengeCardImage);
                    break;
                case "Recipe":
                case "CreativeCooking":
                    Glide.with(context)
                            .load(Uri.parse(holder.mChallenge.getThumbnail()))
                            .centerCrop()
                            .into(holder.mChallengeCardImage);
                    break;
                case "Suikervrij":
                    Glide.with(context)
                            .load(R.drawable.nosugar)
                            .fitCenter()
                            .into(holder.mChallengeCardImage);
                    break;
            }

//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO Eventueel onclick voor details?
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mChallenges.size();
        }
    }

    private static String splitstring(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }


    private class FetchChallengeHistoryTask extends AsyncTask<Void, Void, Boolean> {

        private List<Challenge> challenges = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                challenges = challengeManager.getChallengesFromUser();
                return true;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (!challenges.isEmpty())
                    setupRecyclerView(challenges);
                else{
                    historyNothingFound.setVisibility(View.VISIBLE);
                    historyProgress.setVisibility(View.GONE);
                }
            }
        }
    }
}
