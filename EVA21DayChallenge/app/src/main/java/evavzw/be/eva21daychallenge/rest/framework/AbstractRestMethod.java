package evavzw.be.eva21daychallenge.rest.framework;

import java.util.List;
import java.util.Map;

import android.content.Context;

import evavzw.be.eva21daychallenge.security.UserManager;
import evavzw.be.eva21daychallenge.security.RequestSigner;


public abstract class AbstractRestMethod<T> implements RestMethod<T> {

	private static final String DEFAULT_ENCODING = "UTF-8";

	public RestMethodResult<T> execute() {

		Request request = buildRequest();
		if (requiresAuthorization()) {
			RequestSigner signer = UserManager.getInstance(getContext());
			signer.authorize(request);
		}
		Response response = doRequest(request);
		return buildResult(response);
	}
	
	protected abstract Context getContext();

	/**
	 * Subclasses can overwrite for full control, eg. need to do special
	 * inspection of response headers, etc.
	 * 
	 * @param response
	 * @return
	 */
	protected RestMethodResult<T> buildResult(Response response) {

		int status = response.status;
		String statusMsg = "";
		String responseBody = null;
		T resource = null;

		try {
			responseBody = new String(response.body, getCharacterEncoding(response.headers));
			if (response.status == 200)
				resource = parseResponseBody(responseBody);
			else
				handleHttpStatus(response.status,responseBody);
		} catch (Exception ex) {
		throw new IllegalArgumentException(ex.getMessage());
		//	status = 506; // spec only defines up to 505
	//		statusMsg = ex.getMessage();
		}
		return new RestMethodResult<T>(status, statusMsg, resource);
	}

	protected void handleHttpStatus(int status, String responseBody) {

	}

	protected abstract Request buildRequest();
	
	protected boolean requiresAuthorization() {
		return true;
	}

	protected abstract T parseResponseBody(String responseBody) throws Exception;

	private Response doRequest(Request request) {

		RestClient client = new RestClient();
		return client.execute(request);
	}

	private String getCharacterEncoding(Map<String, List<String>> headers) {
		// TODO get value from headers
		return DEFAULT_ENCODING;
	}

}
