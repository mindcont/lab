package com.mindcont.SmartHome;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author yantianzhu tcp 
 */
public class TcpSocket {
	public static final String HOST = "192.168.2.1"; // tcp 连接ip
	public static final int PORT = 8000; // tcp连接端口
	
	private Socket socket;
	private BufferedOutputStream bufferedOutputStream;
	private BufferedInputStream bufferedInputStream;
	private byte[] receiveBytes; // 接收字节数组
	
	private Handler handler;
	private ReceiveThread receiveThread;

	public TcpSocket(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 
	 * @param bytes
	 * 
	 */
	public void sendMessage(byte[] bytes) {
		if (socket == null || socket.isClosed()) {
			try {
				socket = new Socket(HOST, PORT);//建立连接

			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		if (socket.isConnected()) {
			
			try {
				bufferedOutputStream = new BufferedOutputStream(
						socket.getOutputStream());

				if (receiveThread == null) {
					receiveThread = new ReceiveThread();
					receiveThread.start();
				}
				System.out.println("sendata:" + bytes[0] + ":" + bytes[1]);
				// 发送数据
				
				bufferedOutputStream.write(bytes);
				bufferedOutputStream.flush();

			} catch (IOException e) {
				e.printStackTrace();
				//关闭socket
				
				if (socket != null) {
					try {
						socket.close();
						if (bufferedInputStream != null) {
							bufferedInputStream = null;
						}
						if (bufferedOutputStream != null) {
							bufferedOutputStream.close();
						}
						if (receiveThread != null) {
							receiveThread.interrupt();
							receiveThread = null;
						}

					} catch (IOException ee) {
						ee.printStackTrace();
					}

				}
			} finally {

			}
		}

	}

	private class ReceiveThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				bufferedInputStream = new BufferedInputStream(
						socket.getInputStream());
				//receive data
				
				while (true) {
					receiveBytes = new byte[1024];
					int len = bufferedInputStream.read(receiveBytes);
					if (len == -1) {
						return;
					}
					System.out.println("receive" +( receiveBytes[0]&0xff )+ ":"
							+ (receiveBytes[1]&0xff) +":"+(receiveBytes[2]&0xff));
					Message message = new Message(); //通知界面刷新
					
					message.what = 1;
					message.obj = receiveBytes;
					handler.sendMessage(message);

				}
			} catch (IOException e) {
				e.printStackTrace();
				//关闭socket
				
				if (socket != null) {
					try {
						socket.close();
						if (bufferedInputStream != null) {
							bufferedInputStream = null;
						}
						if (bufferedOutputStream != null) {
							bufferedOutputStream.close();
						}
						if (receiveThread != null) {
							receiveThread.interrupt();
							receiveThread = null;
						}

					} catch (IOException ee) {
						ee.printStackTrace();
					}

				}
			}
			
		}
	}
}
