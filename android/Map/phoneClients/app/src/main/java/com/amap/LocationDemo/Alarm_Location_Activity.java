package com.amap.LocationDemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.mindcont.map.R;
import com.mindcont.map.Utils;

/**
 * 后台唤醒定位
 *
 * @author hongming.wang
 * @创建时间：2016年1月8日 上午11:25:01
 * @项目名称： AMapLocationDemo2.x
 * @文件名称：Alarm_Location_Activity.java
 * @类型名称：Alarm_Location_Activity
 * @since 2.3.0
 */
public class Alarm_Location_Activity extends Activity implements LocationSource,
        OnClickListener, AMapLocationListener, OnCheckedChangeListener {

    private AMap aMap;
    private MapView mapView;

    private EditText etInterval;
    private EditText etAlarm;
    private CheckBox cbAddress;
    private CheckBox cbGpsFirst;
    private Button btLocation;
    private TextView mLocationStatusText;
    private RadioGroup mGPSModeGroup;

    private OnLocationChangedListener mLocationListener;
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;


    //    private TextView tvReult;
//    Handler mHandler = new Handler() {
//        public void dispatchMessage(Message msg) {
//            switch (msg.what) {
//                //开始定位
//                case Utils.MSG_LOCATION_START:
////                    tvReult.setText("正在定位...");
//                    break;
//                // 定位完成
//                case Utils.MSG_LOCATION_FINISH:
//                    AMapLocation loc = (AMapLocation) msg.obj;
//                    String result = Utils.getLocationStr(loc);
////                    tvReult.setText(result);
//                    break;
//                //停止定位
//                case Utils.MSG_LOCATION_STOP:
////                    tvReult.setText("定位停止");
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION")) {
                if (null != mlocationClient) {
                    mlocationClient.startLocation();
                }
            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_location);
        setTitle(R.string.title_alarmCPU);

        etInterval = (EditText) findViewById(R.id.et_interval);
        etAlarm = (EditText) findViewById(R.id.et_alarm);
        cbAddress = (CheckBox) findViewById(R.id.cb_needAddress);
        cbGpsFirst = (CheckBox) findViewById(R.id.cb_gpsFirst);
//        tvReult = (TextView) findViewById(R.id.tv_result);
        btLocation = (Button) findViewById(R.id.bt_location);
        mLocationStatusText = (TextView) findViewById(R.id.location_statusInfo_text);
        btLocation.setOnClickListener(this);

        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        mlocationClient.setLocationListener(this);

        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();

        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        //alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);

        mapView = (MapView) findViewById(R.id.map);
        mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        mGPSModeGroup.setOnCheckedChangeListener(this);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }

        if (null != alarmReceiver) {
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationListener = listener;
//        if (mlocationClient == null) {
//            mlocationClient = new AMapLocationClient(this);
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//            mLocationOption.setInterval(30 * 1000);//1分钟
//            //设置定位参数
//            mlocationClient.setLocationOption(mLocationOption);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mlocationClient.startLocation();
//        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
//        mLocationListener = null;
//        if (mlocationClient != null) {
//            mlocationClient.stopLocation();
//            mlocationClient.onDestroy();
//        }
//        mlocationClient = null;
    }

    /**
     * 设置控件的可用状态
     */
    private void setViewEnable(boolean isEnable) {
        etInterval.setEnabled(isEnable);
        etAlarm.setEnabled(isEnable);
        cbAddress.setEnabled(isEnable);
        cbGpsFirst.setEnabled(isEnable);
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        mLocationOption.setNeedAddress(cbAddress.isChecked());
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        mLocationOption.setGpsFirst(cbGpsFirst.isChecked());

        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            mLocationOption.setInterval(Long.valueOf(strInterval) * 1000);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_location) {
            if (btLocation.getText().equals(
                    getResources().getString(R.string.startLocation))) {
                setViewEnable(false);
                initOption();
                int alarmInterval = 10;
                String str = etAlarm.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    alarmInterval = Integer.parseInt(str);
                }
                btLocation.setText(getResources().getString(
                        R.string.stopLocation));
                // 设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 启动定位
                mlocationClient.startLocation();
//                mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);

                if (null != alarm) {
                    //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
                    alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2 * 1000,
                            alarmInterval * 1000, alarmPi);
                }
            } else {
                setViewEnable(true);
                btLocation.setText(getResources().getString(
                        R.string.startLocation));
                // 停止定位
                mlocationClient.stopLocation();
//                mHandler.sendEmptyMessage(Utils.MSG_LOCATION_STOP);

                //停止定位的时候取消闹钟
                if (null != alarm) {
                    alarm.cancel(alarmPi);
                }
            }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        AMapLocation location = mlocationClient.getLastKnownLocation();
        if (mLocationListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {

                Utils.sendLocationDataToWebsite(location);//发送定位数据到默认网址

                String upLoads = String.valueOf(Utils.upLoadTimes);
                String mServerResponse = new String(Utils.serverResponse);
                String okText = "经度 : " + location.getLongitude() + " 纬度 : " + location.getLatitude() + " 精度 : " + location.getAccuracy() + "米" + " 上传次数: " + upLoads + "\n" + "服务器应答 : " +mServerResponse+ "\n"+"地址"+location.getAddress();

                mLocationStatusText.setVisibility(View.VISIBLE);
                mLocationStatusText.setBackgroundColor(getResources().getColor(R.color.black));
                mLocationStatusText.setTextColor(getResources().getColor(R.color.green));
                mLocationStatusText.setText(okText);
                mLocationListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                Log.d("getLastKnownLocation", okText);


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

}
