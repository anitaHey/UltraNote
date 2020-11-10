package API.utility;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class NetworkUtils {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static String get(String uri) {
        String url = String.format("%s/%s", "http://127.0.0.1:8080", uri);
        Request request = new Request.Builder().url(url).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
