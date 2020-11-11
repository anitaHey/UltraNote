package API.communication;

import API.utility.NetworkUtils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private final Status status;
    private Map<String, Object> content;

    public Response(JsonObject json) {
        status = Status.values()[json.get("StatusCode").getAsInt()];

        if(status == Status.SUCCESS) {
            JsonObject rawContentJson = json.getAsJsonObject("content");
            content = NetworkUtils.getGson().fromJson(rawContentJson, HashMap.class);
        }
    }

    public Response(String rawJson) {
        this(NetworkUtils.getGson().fromJson(rawJson, JsonObject.class));
    }
}
