package be.evavzw.eva21daychallenge.rest.framework;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the actual communication with the server, needs a valid {@link Request} to execute
 */
public class RestClient {

    /**
     * Reads the body returned by the server
     *
     * @param in InputStream for the response body
     * @return byte array of the response body
     * @throws IOException
     */
    private static byte[] readStream(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int count = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        while ((count = in.read(buf)) != -1)
            out.write(buf, 0, count);
        return out.toByteArray();
    }

    /**
     * Executes a given {@link Request}
     *
     * @param request {@link Request} to be executed
     * @return returns {@link Response}
     */
    public Response execute(Request request) {
        HttpURLConnection conn = null;
        Response response = null;
        int status = -1;
        try {
            // Parse the URI to an URL
            URL url = request.getRequestUri().toURL();

            // Open the URL into an HTTP connection
            conn = (HttpURLConnection) url.openConnection();

            // Add the headers to the connection object
            if (request.getHeaders() != null) {
                for (String header : request.getHeaders().keySet()) {
                    for (String value : request.getHeaders().get(header)) {
                        conn.addRequestProperty(header, value);
                    }
                }
            }

            //TODO: add PUT and DELETE support
            switch (request.getMethod()) {
                case GET:
                    conn.setDoOutput(false);
                    break;
                case POST:
                    byte[] payload = request.getBody();
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(payload.length);
                    conn.getOutputStream().write(payload);
                    status = conn.getResponseCode();
                default:
                    break;
            }

            status = conn.getResponseCode();

            //Get the input stream for the body or error stream if there's an error
            BufferedInputStream in = new BufferedInputStream(status / 100 == 2 ? conn.getInputStream() : conn.getErrorStream());
            byte[] body = readStream(in);
            response = new Response(conn.getResponseCode(), conn.getHeaderFields(), body);
            response.status = status;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        if (response == null) {
            response = new Response(status, new HashMap<String, List<String>>(), new byte[]{});
        }

        return response;
    }
}
