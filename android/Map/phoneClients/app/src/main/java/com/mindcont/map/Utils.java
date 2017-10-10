package com.mindcont.map;

import android.location.Location;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fenxi on 2016/4/4.
 */
public class Utils {

    private static final String TAG = "LocationService";

    public final static String defaultUploadWebsite = "https://www.websmithing.com/gpstracker/updatelocation.php";
    public static int upLoadTimes = 1;
    public static  byte[] serverResponse;
    public synchronized static void sendLocationDataToWebsite(Location location) {

        String sessionid = "d1ba8c55-9b46-4a5b-a7d3-76c8cbdf6681";

        // formatted for mysql datetime format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = new Date(location.getTime());

//		SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = sharedPreferences.edit();

//		float totalDistanceInMeters = sharedPreferences.getFloat("totalDistanceInMeters", 0f);
//
//		boolean firstTimeGettingPosition = sharedPreferences.getBoolean("firstTimeGettingPosition", true);
//
//		if (firstTimeGettingPosition) {
//			editor.putBoolean("firstTimeGettingPosition", false);
//		} else {
//			Location previousLocation = new Location("");
//			previousLocation.setLatitude(sharedPreferences.getFloat("previousLatitude", 0f));
//			previousLocation.setLongitude(sharedPreferences.getFloat("previousLongitude", 0f));
//
//			float distance = location.distanceTo(previousLocation);
//			totalDistanceInMeters += distance;
//			editor.putFloat("totalDistanceInMeters", totalDistanceInMeters);
//		}
//
//		editor.putFloat("previousLatitude", (float)location.getLatitude());
//		editor.putFloat("previousLongitude", (float)location.getLongitude());
//		editor.apply();

        final RequestParams requestParams = new RequestParams();
        requestParams.put("latitude", Double.toString(location.getLatitude()));
        requestParams.put("longitude", Double.toString(location.getLongitude()));

        Double speedInMilesPerHour = location.getSpeed() * 2.2369;
        requestParams.put("speed", Integer.toString(speedInMilesPerHour.intValue()));

        try {
            requestParams.put("date", URLEncoder.encode(dateFormat.format(date), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        requestParams.put("locationmethod", location.getProvider());

//		if (totalDistanceInMeters > 0) {
//			requestParams.put("distance", String.format("%.1f", totalDistanceInMeters / 1609)); // in miles,
//		} else {
//			requestParams.put("distance", "0.0"); // in miles
//		}

//		requestParams.put("username", sharedPreferences.getString("userName", ""));
        requestParams.put("username", "mindcont");
//		requestParams.put("phonenumber", sharedPreferences.getString("appID", "")); // uuid
//		requestParams.put("sessionid", sharedPreferences.getString("sessionID", "")); // uuid
        requestParams.put("sessionid", sessionid);

        Double accuracyInFeet = location.getAccuracy() * 3.28;
        requestParams.put("accuracy", Integer.toString(accuracyInFeet.intValue()));

//        Double altitudeInFeet = location.getAltitude() * 3.28;
//        requestParams.put("extrainfo", Integer.toString(altitudeInFeet.intValue()));

//        requestParams.put("eventtype", "android");

        Float direction = location.getBearing();
        requestParams.put("direction", Integer.toString(direction.intValue()));

//		final String uploadWebsite = sharedPreferences.getString("defaultUploadWebsite", defaultUploadWebsite);

        LoopjHttpClient.get(defaultUploadWebsite, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - success", defaultUploadWebsite, requestParams, responseBody, headers, statusCode, null);
//				stopSelf();
                upLoadTimes++;
                serverResponse = responseBody;
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - failure", defaultUploadWebsite, requestParams, errorResponse, headers, statusCode, e);
//				stopSelf();
            }
        });
    }
}
