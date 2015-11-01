package be.evavzw.eva21daychallenge.security;


import be.evavzw.eva21daychallenge.rest.framework.Request;

public interface RequestSigner {
    public void authorize(Request request);
}
