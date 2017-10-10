/** 
 * @Title: ChatServer.java 
 * @Package  
 * @Description: 聊天程序服务器端程序
 * 
 * 大体思想： 
 * 建立一个集合容器，用来存放每个客户端消息进程，
 * 针对每个客户端消息，开启相应新的 进程来管理 
 * 在 Client run 方法中 对容器中的所有客户端 广播 服务器收到的所有消息
 * 
 * @author mindcont
 * @date 2015年10月7日 下午9:26:48 
 * @version V1.0 
 */

 
 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ChatServer
 * @Description: 聊天程序服务端程序
 * @author mindcont
 * @date 2015年10月7日 下午9:26:48
 * 
 */
public class ChatServer {
	int number = 1; // 客户端连接序号
	ServerSocket serverSocket = null;
	boolean started = false; // 服务器端是否启动

	List<Client> clients = new ArrayList<ChatServer.Client>();

	/**
	 * @Title: main
	 * @Description: 聊天服务器端程序入口
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		new ChatServer().Start();
	}
    //聊天服务端 开启方法，用来启动服务端相应的 监听端口
	public void Start() {

		try {
			serverSocket = new ServerSocket(19820); //本地 127.0.0.1 ：19820 端口通信
			started = true; // 服务端 监听通信端口  启动标志位
		} catch (BindException e) { // 在开启服务器监听端口启动时，捕获可能会出现的异常并作相应的处理
			System.out.println("端口使用中....,尝试改变服务器和客户端通信端口");
			System.out.println("请关掉相关程序后重新启动服务端程序！ ");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			while (started) {// 启动后，针对相应的客户端连接，新建相应的线程进行相应
				Socket socket = serverSocket.accept();
				Client client = new Client(socket);
				new Thread(client).start(); //针对每个客户端 新建相应的进程 
				clients.add(client); //将新建的客户端进程 放到clients 集合容器中 进行管理
				System.out.println("Client  " + (number++)
						+ "  has connected successfully");  //打印 当前服务端 所接受到的 客户端数目 
			}
		} catch (IOException e) {  //捕获这样做，可能会出现的异常，并给出提示
			e.printStackTrace();
		} finally { // 最后收尾工作，不管是否发生异常，判断管道和输入流是否为空，关闭

			try {
				serverSocket.close(); // 读写过程中报错，首先把服务器通信管道关闭，可能由于客户端通信过程中突然关闭
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
   
	//针对每个客户端 处理的 client 方法 继承 runnable 接口
	class Client implements Runnable {

		private Socket clientSocket;
		private boolean ClientHasConnected = false; // 客户端是否连接
		private DataInputStream dataInputStream = null; //声明 客户端 消息输入流
		private DataOutputStream dataOutputStream = null; //声明服务器端消息 输出流

		public Client(Socket clientSocket) {
			this.clientSocket = clientSocket;
			try {
				dataInputStream = new DataInputStream(  //对相应的输入 / 输出流做相应的 声明变量
						clientSocket.getInputStream());
				dataOutputStream = new DataOutputStream(
						clientSocket.getOutputStream());
				ClientHasConnected = true;// 客户端连接成功
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        //服务器端 发送 文本的方法
		public void Send(String string) {
			try {
				dataOutputStream.writeUTF(string);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		@Override
		public void run() {
			try {
				while (ClientHasConnected) { //当客户端 连接成功标志位   为真，在while 循环中 一直处理相应的文本信息
					String string = dataInputStream.readUTF();
					System.out.println(string);  
					for (int i = 0; i < clients.size(); i++) {     //对集合容器中的所有 客户端进程 ，进行广播所有收到的消息
 						Client client = clients.get(i);
						client.Send(string);
					}
				}
			} catch (EOFException e) {  //捕获处理可能会出现的异常 并作相应的提示
				System.out.println("Client " + "has closed !");
			} catch (IOException e) {
				e.printStackTrace();
			} finally { // 收尾工作，无论是否出现异常，服务器端最后 关闭相应的进程和资源
				try {
					if (dataInputStream != null)
						dataInputStream.close();
					if (dataOutputStream != null)
						dataOutputStream.close();
					if (clientSocket != null)
						clientSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
