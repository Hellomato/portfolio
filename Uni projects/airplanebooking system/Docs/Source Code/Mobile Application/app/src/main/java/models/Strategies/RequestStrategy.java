package models.Strategies;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.Response;

public interface RequestStrategy
{

    Response Request( HttpUrl url, JSONObject data);

}
