package com.amap.map3d.demo.location;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindcont.map.LoopjHttpClient;
import com.mindcont.map.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * AMapV2地图中介绍定位三种模式的使用，包括定位，追随，旋转
 */
public class LocationModeSourceActivity extends Activity implements LocationSource,
        AMapLocationListener, OnCheckedChangeListener {

    public final static String defaultUploadWebsite = " https://www.websmithing.com/gpstracker/updatelocation.php";
    private static final String TAG = "LocationService";
    public String sessionid;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;
    private TextView mLocationStatusText;

    private int upLoadTimes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.locationmodesource_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
//        findViewById(R.id.image_iat_set).setOnClickListener(IatDemo.this);

        mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        mGPSModeGroup.setOnCheckedChangeListener(this);
        mLocationStatusText = (TextView) findViewById(R.id.location_statusInfo_text);

//        sessionid=UUID.randomUUID().toString();
        sessionid = "d1ba8c55-9b46-4a5b-a7d3-76c8cbdf6691";

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.gps_locate_button:
                // 设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow_button:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate_button:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        AMapLocation location = mlocationClient.getLastKnownLocation();
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                String upLoads = String.valueOf(upLoadTimes);

                String okText = "经度 : " + location.getLongitude() + "\n" + " 纬度 : " + location.getLatitude() + "\n" + " 精度 : " + location.getAccuracy() + "米" + "\n" + " 上传次数: " + upLoads;
                Log.d("getLastKnownLocation", okText);

                mLocationStatusText.setVisibility(View.VISIBLE);
                mLocationStatusText.setBackgroundColor(getResources().getColor(R.color.black));
                mLocationStatusText.setTextColor(getResources().getColor(R.color.green));
                mLocationStatusText.setText(okText);
                sendLocationDataToWebsite(location);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);

                mLocationStatusText.setVisibility(View.VISIBLE);
                mLocationStatusText.setBackgroundColor(getResources().getColor(R.color.red));
                mLocationStatusText.setTextColor(getResources().getColor(R.color.white));
                mLocationStatusText.setText(errText);

            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(30 * 1000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public void sendLocationDataToWebsite(Location location) {
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
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - failure", defaultUploadWebsite, requestParams, errorResponse, headers, statusCode, e);
//				stopSelf();
            }
        });
    }
}
