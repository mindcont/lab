/** 
 * @Title: ChatClient.java 
 * @Package  
 * @Description: 客户端聊天程序 
 * 
 * 大体思想：主函数调用 面板launchFrame 方法，
 * 开启 连接服务端connectServer 方法  连接到服务端
 * 和textFieldListener 处理服务器端回复的文本信息
 * 
 * @author mindcont
 * @date 2015年10月7日 下午2:46:10 
 * @version V1.0 
 */

 
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @ClassName: ChatClient
 * @Description: 客户端聊天面板
 * @author mindcont
 * @date 2015年10月7日 下午2:46:10
 * 
 */
public class ChatClient extends Frame {

	private boolean serverConnect = false;
	Socket socket = null;
	DataOutputStream dataOutputStream = null;
	DataInputStream dataInputStream = null;
	TextField textField = new TextField();
	TextArea textContent = new TextArea();

	Thread textThread = new Thread(new RevThread());

	/**
	 * @Title: main
	 * @Description: 聊天客户端程序入口
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		new ChatClient().launchFrame();

	}

	//
	public void launchFrame() {
		setLocation(120, 180);
		setSize(280, 540);
		add(textField, BorderLayout.SOUTH);
		add(textContent, BorderLayout.CENTER);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnectServer();
				System.exit(0);
			}
		});

		textField.addActionListener(new textFieldListener());
		setVisible(true);// 设为可见
		connectServer();// 连接到服务器端
		textThread.start();// 文本处理进程启动
	}

	// 通过socket 套接字 实现 连接到服务端程序
	public void connectServer() {
		try {
			socket = new Socket("127.0.0.1", 19820);
			dataOutputStream = new DataOutputStream(socket.getOutputStream()); // 获得客户端输出流
			dataInputStream = new DataInputStream(socket.getInputStream()); // 获得服务器端的输入流
			System.out.println("server has connected!");
			serverConnect = true;
		} catch (UnknownHostException e) { // catch 几个可能会出现的异常并打印
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 断开与服务器端的连接 ，释放相应的进程资源
	public void disconnectServer() {
		try {
			dataOutputStream.close();// 关闭socket 输出流
			dataInputStream.close();
			socket.close(); // 关闭 通信管道

		} catch (IOException e) { // catch IO 异常
			e.printStackTrace();
		}

	}

	// 文本信息 监听进程，用了处理向服务器端 发送信息
	private class textFieldListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = textField.getText().trim();
			textField.setText("");
			try {
				dataOutputStream.writeUTF(text);
				dataOutputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

	// 文本信息接收方法，用来处理服务器端返回的信息
	private class RevThread implements Runnable {

		@Override
		public void run() {

			try {
				while (serverConnect) {
					String string = dataInputStream.readUTF();
					textContent.setText(textContent.getText() + string);
				}
			} catch (SocketException e) {
				System.out.println("退出了，bye!");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}