package be.evavzw.eva21daychallenge.activity.challenges;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    private ChallengeManager challengeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_history);

        challengeManager = ChallengeManager.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        historyView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.challengeHistory));

        new FetchChallengeHistoryTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupRecyclerView(List<Challenge> challenges){
        historyView.setAdapter(new CardViewRecyclerViewAdapter(getApplicationContext(), challenges));
    }

    private static class CardViewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Challenge> mChallenges;

        private static class ViewHolder extends RecyclerView.ViewHolder{
            public Challenge mChallenge;
            public final View mView;
            public final TextView mTextView;

            public ViewHolder(View view){
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(R.id.challengeCardText);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public Challenge getValueAt(int position){
            return mChallenges.get(position);
        }

        public CardViewRecyclerViewAdapter(Context context, List<Challenge> challenges){
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.mChallenges = challenges;

            mChallenges.add(0, mChallenges.get(0));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderr, int position) {
            final ViewHolder holder= (ViewHolder) holderr;
            holder.mChallenge = mChallenges.get(position);
            holder.mTextView.setText(mChallenges.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mChallenges.size();
        }
    }

    private class FetchChallengeHistoryTask extends AsyncTask<Void, Void, Boolean>{

        private List<Challenge> challenges = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                challenges = challengeManager.getChallengesFromUser();
                return true;
            }catch (Exception e){
                throw e;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                if(!challenges.isEmpty())
                    setupRecyclerView(challenges);
            }
        }
    }
}
