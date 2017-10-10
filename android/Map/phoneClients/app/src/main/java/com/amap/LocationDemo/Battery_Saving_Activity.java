package com.amap.LocationDemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.mindcont.map.R;

/**
 * 低功耗定位模式功能演示
 *
 * @author hongming.wang
 * @创建时间： 2015年11月24日 下午4:24:07
 * @项目名称： AMapLocationDemo2.x
 * @文件名称: Battery_Saving_Activity.java
 * @类型名称: Battery_Saving_Activity
 */
public class Battery_Saving_Activity extends Activity implements
        OnCheckedChangeListener, OnClickListener, AMapLocationListener {
    private RadioGroup rgLocation;
    private RadioButton rbLocationContinue;
    private RadioButton rbLocationOnce;
    private View layoutInterval;
    private EditText etInterval;
    private CheckBox cbAddress;
    private TextView tvReult;
    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case Utils.MSG_LOCATION_START:
                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = Utils.getLocationStr(loc);
                    tvReult.setText(result);
                    break;
                case Utils.MSG_LOCATION_STOP:
                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private Button btLocation;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_saving);
        setTitle(R.string.title_battery_saving);

        rgLocation = (RadioGroup) findViewById(R.id.rg_location);
        rbLocationContinue = (RadioButton) findViewById(R.id.rb_continueLocation);
        rbLocationOnce = (RadioButton) findViewById(R.id.rb_onceLocation);
        layoutInterval = findViewById(R.id.layout_interval);
        etInterval = (EditText) findViewById(R.id.et_interval);
        cbAddress = (CheckBox) findViewById(R.id.cb_needAddress);
        tvReult = (TextView) findViewById(R.id.tv_result);
        btLocation = (Button) findViewById(R.id.bt_location);

        rgLocation.setOnCheckedChangeListener(this);
        btLocation.setOnClickListener(this);

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_continueLocation:
                //只有持续定位设置定位间隔才有效，单次定位无效
                layoutInterval.setVisibility(View.VISIBLE);
                //设置为不是单次定位
                locationOption.setOnceLocation(false);
                break;
            case R.id.rb_onceLocation:
                //只有持续定位设置定位间隔才有效，单次定位无效
                layoutInterval.setVisibility(View.GONE);
                //设置为单次定位
                locationOption.setOnceLocation(true);
                break;
        }
    }

    /**
     * 设置控件的可用状态
     */
    private void setViewEnable(boolean isEnable) {
        rbLocationContinue.setEnabled(isEnable);
        rbLocationOnce.setEnabled(isEnable);
        etInterval.setEnabled(isEnable);
        cbAddress.setEnabled(isEnable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_location) {
            if (btLocation.getText().equals(
                    getResources().getString(R.string.startLocation))) {
                setViewEnable(false);
                initOption();
                btLocation.setText(getResources().getString(
                        R.string.stopLocation));
                // 设置定位参数
                locationClient.setLocationOption(locationOption);
                // 启动定位
                locationClient.startLocation();
                mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
            } else {
                setViewEnable(true);
                btLocation.setText(getResources().getString(
                        R.string.startLocation));
                // 停止定位
                locationClient.stopLocation();
                mHandler.sendEmptyMessage(Utils.MSG_LOCATION_STOP);
            }
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(cbAddress.isChecked());

        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            /**
             *  设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
             *  只有持续定位设置定位间隔才有效，单次定位无效
             */
            locationOption.setInterval(Long.valueOf(strInterval));
        }

    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }
}
