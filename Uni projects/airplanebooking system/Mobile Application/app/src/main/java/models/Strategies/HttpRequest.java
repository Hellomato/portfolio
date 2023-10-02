package models.Strategies;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.Response;

public class HttpRequest {
    private HttpUrl url;
    private RequestStrategy strategy;
    private JSONObject data;

    public HttpRequest(RequestStrategy strategy, HttpUrl url, JSONObject data){
        this.strategy = strategy;
        this.url = url;
        this.data = data;
    }

    public Response Send(){

        Response response = strategy.Request(url, data);
        return response;

    }

}
