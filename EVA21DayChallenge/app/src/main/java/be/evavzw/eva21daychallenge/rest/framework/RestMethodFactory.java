package be.evavzw.eva21daychallenge.rest.framework;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.List;
import java.util.Map;

/**
 * Factory to help the Rest Methods, we don't really use any of this except the enum
 */
public class RestMethodFactory {

    private static RestMethodFactory instance;
    private static Object lock = new Object();
    private UriMatcher uriMatcher;
    private Context mContext;

    private static final int PROFILE = 1;
    private static final int TIMELINE = 2;

    private RestMethodFactory(Context context) {
        mContext = context.getApplicationContext();
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    }

    public static RestMethodFactory getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance = new RestMethodFactory(context);
            }
        }

        return instance;
    }

    public RestMethod getRestMethod(Uri resourceUri, Method method,
                                    Map<String, List<String>> headers, byte[] body) {

        switch (uriMatcher.match(resourceUri)) {

        }

        return null;
    }

    public static enum Method {
        GET, POST, PUT, DELETE
    }
}
