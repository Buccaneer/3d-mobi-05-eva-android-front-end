package evavzw.be.eva21daychallenge.security;


import evavzw.be.eva21daychallenge.rest.Request;

public interface RequestSigner {
    public void authorize(Request request);
}
