package evavzw.be.eva21daychallenge.rest.framework;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import evavzw.be.eva21daychallenge.rest.framework.Request;
import evavzw.be.eva21daychallenge.rest.framework.Response;

public class RestClient {

    private static byte[] readStream(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int count = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        while ((count = in.read(buf)) != -1)
            out.write(buf, 0, count);
        return out.toByteArray();
    }

    public Response execute(Request request) {
        HttpURLConnection conn = null;
        Response response = null;
        int status = -1;
        try {

            URL url = request.getRequestUri().toURL();
            conn = (HttpURLConnection) url.openConnection();
            if (request.getHeaders() != null) {
                for (String header : request.getHeaders().keySet()) {
                    for (String value : request.getHeaders().get(header)) {
                        conn.addRequestProperty(header, value);
                    }
                }
            }

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



            BufferedInputStream in = new BufferedInputStream(status == 200 ? conn.getInputStream() : conn.getErrorStream());
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