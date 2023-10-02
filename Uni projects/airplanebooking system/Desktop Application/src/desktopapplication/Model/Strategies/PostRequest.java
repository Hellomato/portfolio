/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Strategies;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Reecus
 */
public class PostRequest implements RequestStrategy
{
    /**
     * This method sends the POST request and returns the response
     * @param url The URL to send a request to. Should hold the URL of the API itself.
     * @param data Holds a JSON object to use as the request body for POST requests.
     * @return Response object holding the returned data from the request.
     */
    @Override
    public Response Request(HttpUrl url, String data) {

        Response response = null;
        OkHttpClient client = new OkHttpClient();

        // Create the request header and body.
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "b4da9d43-f382-7764-72b1-055808ea360b")
                .build();
        
        try 
        {
            // Make the post request
            response = client.newCall(request).execute();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return response;
    }
}
