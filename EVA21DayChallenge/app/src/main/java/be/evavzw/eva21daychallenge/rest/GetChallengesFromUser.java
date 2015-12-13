package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import be.evavzw.eva21daychallenge.models.challenges.Challenge;
import be.evavzw.eva21daychallenge.models.challenges.CreativeCookingChallenge;
import be.evavzw.eva21daychallenge.models.challenges.RecipeChallenge;
import be.evavzw.eva21daychallenge.models.challenges.RestaurantChallenge;
import be.evavzw.eva21daychallenge.models.challenges.TextChallenge;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;

/**
 * Created by Annemie on 6/12/2015.
 */
public class GetChallengesFromUser extends AbstractRestMethod<List<Challenge>> {
    private static final URI USERCHALLENGEURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenge");
    private Context context;

    public GetChallengesFromUser(Context context) {
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try{
            return new Request(RestMethodFactory.Method.GET, USERCHALLENGEURI, new byte[]{});
        }catch (Exception e){
            throw new IllegalArgumentException("Can't build request", e);
        }
    }

    @Override
    protected List<Challenge> parseResponseBody(String responseBody) throws Exception {
        List<Challenge> challenges = new ArrayList<>();
        JSONArray jsonChallenges = new JSONArray(responseBody);

        for (int i = 0; i < jsonChallenges.length(); i++) {

            JSONObject row = jsonChallenges.getJSONObject(i);
            Challenge challenge = null;
            switch (row.getString("Type")) {
                case "Restaurant":
                    challenge = new RestaurantChallenge(row);
                    break;
                case "Suikervrij":
                    challenge = new TextChallenge(row);
                    break;
                case "CreativeCooking":
                    challenge = new CreativeCookingChallenge(row);
                    break;
                case "Recipe":
                case "RegionRecipe":
                    challenge = new RecipeChallenge(row);
                    break;
                default:
                    break;
            }

            if (challenge != null)
                challenges.add(challenge);
        }

        return challenges;
    }
}
