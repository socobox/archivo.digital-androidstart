package archivo.digital.androidstart.services;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class HttpService {

    private static final String BASE_URL_SERVICE_TWO = "https://archivo.digital";
    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(60000);
        client.setResponseTimeout(90000);
        Log.d("CALL->GET", url);
        client.addHeader("App-Key", DataService.APP_KEY);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postJSON(Context ctx, String url, String token, JSONObject obj, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException, JSONException {
        client.setTimeout(60000);
        client.setResponseTimeout(90000);
        Log.d("CALL->POST", getAbsoluteUrl(url) + "==> Authorization, Bearer " + token);
        client.addHeader("Authorization", "Bearer " + token);
        client.addHeader("App-Key", DataService.APP_KEY);
        client.post(ctx, getAbsoluteUrl(url), new StringEntity(obj.toString()), "application/json", responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL_SERVICE_TWO + relativeUrl;
    }


}
