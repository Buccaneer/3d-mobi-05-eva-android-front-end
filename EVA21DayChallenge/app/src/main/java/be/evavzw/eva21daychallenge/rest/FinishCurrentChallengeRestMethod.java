package be.evavzw.eva21daychallenge.rest;

import android.content.Context;

import java.net.URI;

import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.rest.framework.AbstractRestMethod;
import be.evavzw.eva21daychallenge.rest.framework.Request;
import be.evavzw.eva21daychallenge.rest.framework.Response;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodFactory;
import be.evavzw.eva21daychallenge.rest.framework.RestMethodResult;

/**
 * Created by Pieter-Jan on 10/12/2015.
 */
public class FinishCurrentChallengeRestMethod extends AbstractRestMethod<Integer>
{
    private final URI REQUESTURI;
    private Context context;

    public FinishCurrentChallengeRestMethod(Context context, int challengeId){
        this.context = context;
        REQUESTURI = URI.create("http://evavzwrest.azurewebsites.net/api/Challenge/" + challengeId);
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        try{
            return new Request(RestMethodFactory.Method.POST, REQUESTURI, new byte[]{});
        }catch(Exception e){
            throw new IllegalArgumentException("Cannot build request see nested exception.", e);
        }
    }

    @Override
    protected RestMethodResult<Integer> buildResult(Response response) {
        int status = response.status;
        String statusMsg = "";
        String responseBody = null;
        Integer resource = null;
        return new RestMethodResult<>(status, statusMsg, resource);
    }

    @Override
    protected Integer parseResponseBody(String responseBody) throws Exception
    {
        return null;
    }

}