package com.mindcont.SmartHome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.iflytek.cloud.SpeechUtility;
import com.mindcont.SmartHome.TcpSocket;
import com.mindcont.SmartHome.R;

public class MainActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {
	private Button ledSwitch, alertSwitch,currentVB,eleSwitch;
	private Button upB, downB, leftB, rightB, stopB, hourB,minuteB, cancelB, setB;
	private TextView temp,hehum;
	private SeekBar setVBar;
	private TcpSocket tcpSocket;
	private TimePicker timePicker;
	private RelativeLayout tempRelative;
	private byte ledStatus;
	private byte alertStatus;
	private byte eleStatus;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				byte bytes[] = (byte[])msg.obj;
				refreshView(bytes); // 刷新界面
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		
		setContentView(R.layout.activity_main);
		findView();
		tcpSocket = new TcpSocket(handler);// 初始化tcp连接
	}

	private void findView() {
		ledSwitch = (Button) findViewById(R.id.ledSwitch);
		eleSwitch = (Button) findViewById(R.id.eleSwitch);
		alertSwitch = (Button) findViewById(R.id.alertSwitch);
		upB = (Button) findViewById(R.id.upButton);
		downB = (Button) findViewById(R.id.downButton);
		leftB = (Button) findViewById(R.id.leftButton);
		rightB = (Button) findViewById(R.id.rightButton);
		stopB = (Button) findViewById(R.id.stopButton);
		hourB = (Button) findViewById(R.id.hourButton);
		minuteB = (Button) findViewById(R.id.minuteButton);
		currentVB = (Button) findViewById(R.id.vTB);
		setVBar = (SeekBar) findViewById(R.id.svS);
		tempRelative = (RelativeLayout) findViewById(R.id.tempRelative);
		temp = (TextView) findViewById(R.id.temp);
		hehum = (TextView) findViewById(R.id.hehum);

		// 注册点击事件
		upB.setOnClickListener(this);
		ledSwitch.setOnClickListener(this);
		alertSwitch.setOnClickListener(this);
		downB.setOnClickListener(this);
		leftB.setOnClickListener(this);
		rightB.setOnClickListener(this);
		stopB.setOnClickListener(this);
		hourB.setOnClickListener(this);
		minuteB.setOnClickListener(this);
		eleSwitch.setOnClickListener(this);
		setVBar.setOnSeekBarChangeListener(this);
		tempRelative.setOnClickListener(this);
	}

	public void refreshView(byte bytes[]) {
		int choice = (bytes[0]&0xff);
		switch (choice) {
		case 0x01:

			break;
		case 0x02:
			int tem =  (bytes[1]&0xff);
			int hum =  (bytes[2]&0xff);
            temp.setText(""+ tem);
            hehum.setText(""+ hum);
			break;
		case 0x03:

			break;
		case 0x04:

			break;
		case 0x05:
			currentVB.setText("" +( bytes[1]&0xff)/10.0f);
			break;
		case 0x06:

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		byte bytes[] = null ;
		switch (arg0.getId()) {
		case R.id.leftButton:   // 电机左
			
			bytes = new byte[]{0x03,0x01};
			sendMessage(bytes);
			break;
		case R.id.rightButton:  // 电机右
			bytes = new byte[]{0x03,0x02};
			sendMessage(bytes);
			break; 
		case R.id.upButton:     // 点击上
			bytes = new byte[]{0x03,0x03};
			sendMessage(bytes);
			break;
		case R.id.downButton: //  点击下
			
			bytes = new byte[]{0x03,0x04};
			sendMessage(bytes);
			break;
		case R.id.stopButton:   //点击停止
			
			bytes = new byte[]{0x03,0x05};
			sendMessage(bytes);
			break;
		case R.id.hourButton:
			showDialog();
			break;
		case R.id.minuteButton:
			showDialog();
			break;
		case R.id.cancelButton:
			dialog.dismiss();
			break;
		case R.id.setButton:
			hour = timePicker.getCurrentHour();
			minute = timePicker.getCurrentMinute();
			hourB.setText(""+hour);
			minuteB.setText(""+minute);
			dialog.dismiss();
			break;
		case R.id.tempRelative:  //查询温湿度
			
			bytes = new byte[]{0x02,(byte) 0xff};
			sendMessage(bytes);
			break;
		case R.id.ledSwitch:  //控制led
			
			if (ledStatus == 0) {
				ledStatus = 0x01;
				ledSwitch.setBackgroundResource(R.drawable.on);
			}else {
				ledStatus = 0x00;
				ledSwitch.setBackgroundResource(R.drawable.off);
			}
			bytes = new byte[] { 0x01, ledStatus };
			sendMessage(bytes);
			break;
		case R.id.alertSwitch: //定时开关
			
			if (alertStatus == 0) {
				alertStatus = 0x01;
				alertSwitch.setBackgroundResource(R.drawable.on);
			}else {
				alertStatus = 0x00;
				alertSwitch.setBackgroundResource(R.drawable.off);
			}
			bytes = new byte[] { 0x04, (byte)hour,(byte)minute,alertStatus };
			sendMessage(bytes);
			break;
		case R.id.eleSwitch:
			if (eleStatus == 0) {
				eleStatus = 0x01;
				eleSwitch.setBackgroundResource(R.drawable.on);
			}else {
				eleStatus = 0x00;
				eleSwitch.setBackgroundResource(R.drawable.off);
			}
			bytes = new byte[] { 0x06, eleStatus };
			sendMessage(bytes);
			break;
		default:
			break;
		}
		
	}
	private Dialog dialog;
    private int hour;
    private int minute;
	private void showDialog() {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.dialog1);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.timedialog);
			setB = (Button)  dialog.findViewById(R.id.setButton);
			cancelB = (Button)  dialog.findViewById(R.id.cancelButton);
			setB.setOnClickListener(this);
			cancelB.setOnClickListener(this);
			timePicker = (TimePicker)  dialog.findViewById(R.id.timePicker);
			timePicker.setIs24HourView(true);

		}
		dialog.show();
	}
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		byte[] bytes = new byte[] { 0x05,(byte) arg1 };
		sendMessage(bytes);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {

	}

    public void sendMessage(final byte[] bytes) {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				tcpSocket.sendMessage(bytes);
				
			}
		}).start();
    }
	//重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//填充选项菜单（读取XML文件、解析、加载到Menu组件上）
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.voice:
				// Launch the DeviceListActivity to see devices and do scan
                startActivity(new Intent(this, com.iflytek.voicedemo.IatDemo.class));
				return true;

//            case R.id.menu_preview:
//                startActivity(new Intent(this, CubePreview.class));
//                return true;
//			case R.id.menu_about:
//				// Ensure this device is discoverable by others
//				//ensureDiscoverable();
//
//				//在此处编写about_activity
//				startActivity(new Intent(this, AboutActivity.class));
//				return true;
		}
		return false;
	}
}
