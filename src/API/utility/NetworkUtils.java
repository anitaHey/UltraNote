package API.utility;

import com.google.gson.Gson;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

public class NetworkUtils {
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    public static String get(String uri) {
        String url = String.format("%s/%s", "http://127.0.0.1:8000", uri);
        Request request = new Request.Builder().url(url).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String post(String uri, String json) {
        String url = String.format("%s/%s", "http://127.0.0.1:8000", uri);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Gson getGson(){
        return GSON;
    }
}
