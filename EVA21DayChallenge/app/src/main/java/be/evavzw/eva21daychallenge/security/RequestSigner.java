package be.evavzw.eva21daychallenge.security;


import be.evavzw.eva21daychallenge.rest.framework.Request;

/**
 * RequestSigner, all of our Managers can implement this, but generally only the UserManager does this
 */
public interface RequestSigner {
    void authorize(Request request);
}
