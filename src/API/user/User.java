package API.user;

import API.communication.Response;
import API.utility.NetworkUtils;
import com.google.gson.JsonObject;

public class User {
    public static Response login(String email, String password) {
        String url = "user/login";

        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        return new Response(NetworkUtils.post(url, json.toString()));
    }
}
