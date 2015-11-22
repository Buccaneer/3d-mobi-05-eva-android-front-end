package be.evavzw.eva21daychallenge.rest.framework;

public interface RestMethod<T> {

    public RestMethodResult<T> execute();
}
