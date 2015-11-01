package be.evavzw.eva21daychallenge.rest.framework;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which contains a request, contains everything that's needed for the {@link RestClient} to do his work
 */
public class Request {

    private URI requestUri;
    private Map<String, List<String>> headers = new HashMap<>();
    private byte[] body;
    private RestMethodFactory.Method method;

    /**
     * Constructor for a Request object with http headers
     *
     * @param method     GET, PUT, POST or DELETE, use the existing {@link RestMethodFactory}
     * @param requestUri Server URI to direct the request to
     * @param headers    http headers to include in the request
     * @param body       http body to include in the request
     */
    public Request(RestMethodFactory.Method method, URI requestUri, Map<String, List<String>> headers,
                   byte[] body) {
        super();
        this.method = method;
        this.requestUri = requestUri;
        this.headers = headers;
        this.body = body;
    }

    /**
     * Constructor for a Request object without http headers
     *
     * @param method     GET, PUT, POST or DELETE, use the existing {@link RestMethodFactory}
     * @param requestUri Server URI to direct the request to
     * @param body       http body to include in the request
     */
    public Request(RestMethodFactory.Method method, URI requestUri, byte[] body) {
        this.method = method;
        this.requestUri = requestUri;
        this.body = body;
    }

    /**
     * Gets the REST method used for the request
     *
     * @return GET, PUT, POST or DELETE
     */
    public RestMethodFactory.Method getMethod() {
        return method;
    }

    /**
     * Gets the URI of the request
     *
     * @return REST URI
     */
    public URI getRequestUri() {
        return requestUri;
    }

    /**
     * Gets the http headers of the request
     *
     * @return http headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Gets the http body of the request
     *
     * @return return byte representation of http body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Adds a http header to the request
     *
     * @param key   key of the header, eg Authorization, Accept-Language
     * @param value values that belong with the header
     */
    public void addHeader(String key, List<String> value) {

        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }


}
