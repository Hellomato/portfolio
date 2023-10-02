package models.Strategies;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetRequestStrategy implements RequestStrategy {

    /**
     * This method sends the POST request and returns the response
     * @param url The URL to send a request to. Should hold the URL of the API itself.
     * @param data Holds a JSON object to use as the request body for POST requests. Redundant for GetRequestStrategy, so should be null.
     * @return Response object holding the returned data from the request.
     */
    @Override
    public Response Request( HttpUrl url, JSONObject data ) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/json")
                .build();

        Response response = null;
        try
        {
            response = client.newCall(request).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return response;

    }
}
