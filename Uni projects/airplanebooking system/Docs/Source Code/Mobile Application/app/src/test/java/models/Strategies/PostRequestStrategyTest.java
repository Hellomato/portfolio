package models.Strategies;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.Response;

public class PostRequestStrategyTest {
    PostRequestStrategy postRequestStrategy = new PostRequestStrategy();

    @Test
    public void testRequest() throws Exception {
        Response result = postRequestStrategy.Request(null, null);
        Assert.assertEquals(null, result);
    }
}
