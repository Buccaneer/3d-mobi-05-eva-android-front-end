package be.evavzw.eva21daychallenge.rest.framework;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
	
	private URI requestUri;
	private Map<String, List<String>> headers = new HashMap<>() ;
	private byte[] body;
	private RestMethodFactory.Method method;
	
	public Request(RestMethodFactory.Method method, URI requestUri, Map<String, List<String>> headers,
			byte[] body) {
		super();
		this.method = method;
		this.requestUri = requestUri;
		this.headers = headers;
		this.body = body;
	}

	public Request(RestMethodFactory.Method method, URI requestUri, byte[] body) {
		this.method = method;
		this.requestUri = requestUri;
		this.body = body;
	}

	public RestMethodFactory.Method getMethod() {
		return method;
	}

	public URI getRequestUri() {
		return requestUri;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void addHeader(String key, List<String> value) {
		
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put(key, value);
	}

	
	
	
	
	

}
