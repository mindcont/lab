/** 
 * @Title: TankClient.java 
 * @Package  
 * @Description: 用于运行 坦克游戏的主文件，包含main方法，和一些 例如坦克，子弹，障碍物的生成
 * @author mindcont
 * @date 2015年10月10日 上午9:29:01 
 * @version V1.0 
 */

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TankClient
 * @Description: 坦克游戏的运行的客户端，用于配置界面，paint 进程控制，坦克，子弹，障碍物生成，控制
 * @author mindcont
 * @date 2015年10月10日 上午9:29:01
 * 
 */

public class TankClient extends Frame {

	public static final int GAME_WIDTH = 800; // 定义游戏界面窗口的宽度
	public static final int GAME_HIGHT = 600; // 定义游戏界面窗口的长度
	int x = 50, y = 50; // 定义x , y 坐标

	Image offScreenImage = null; // 双缓冲，消除屏幕闪烁
	Explode explode = new Explode(70, 70, this); // 爆炸效果
	Tank mytank = new Tank(400, 400, Tank.Direction.STOP, true, this); // 我方坦克
	Wall wall1 = new Wall(450, 200, 20, 250, this); // 画出障碍物1
	Wall wall2 = new Wall(450, 200, 250, 20, this); // 画出障碍物2

	Blood blood = new Blood(); // 生成 固定轨迹的随机血块
	List<Missile> missiles = new ArrayList<Missile>(); // 生成 子弹的集合，通过调用 辨别好坏坦克
														// good 变量，来统一对子弹进行管理
	List<Explode> explodes = new ArrayList<Explode>(); // 生成爆炸效果的集合
	List<Tank> tanks = new ArrayList<Tank>(); // 生成坦克的集合

	/**
	 * @Title: main
	 * @Description: 函数运行主方法
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {

		TankClient tankClient = new TankClient();
		tankClient.launchFrame();
	}

	/**
	 * 
	 * @Title: launchFrame
	 * @Description: 游戏的主界面
	 * @param 无
	 * @return void 无
	 * @throws
	 */
	public void launchFrame() {
		for (int i = 0; i < 6; i++) {
			tanks.add(new Tank(50 + 40 * (i + 1), 100, Tank.Direction.D, false,
					this));
		}
		this.setTitle("tankWar");
		this.setLocation(300, 120);
		this.setSize(GAME_WIDTH, GAME_HIGHT);
		this.setResizable(false); // 不可改变大小
		this.addWindowListener(new WindowAdapter() { // 监听关闭事件
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setBackground(Color.orange);
		this.addKeyListener(new keyMonitor()); // 添加键盘监听
		this.setVisible(true);
		new Thread(new paintThread()).start(); // 开启每30毫秒进行重画进程
	}

	/**
	 * 
	 * Title: update 双缓冲，用于消除屏幕闪烁 Description: repaint 方法调用updata 方法，update方法
	 * 再调用paint 方法。在updata 中我们采用 offscreenimage
	 * 
	 * @param g
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */

	@Override
	public void update(Graphics g) {

		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HIGHT);
		}

		Graphics goffScreen = offScreenImage.getGraphics(); // 获得图片的像素点
		Color backgroundColor = goffScreen.getColor();// 获取像素点的原始颜色
		goffScreen.setColor(Color.orange);// 设置 背景
		goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HIGHT);// 填充前景色
		goffScreen.setColor(backgroundColor);// 还原为 原始像素点
		paint(goffScreen);
		g.drawImage(offScreenImage, 0, 0, null);// 调用paint方法 实现缓冲
	}

	/**
	 * 
	 * Title: paint Description: 作用 绘画 坦克 为红色小圆圈
	 * 
	 * @param g
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {

		g.drawString("***********************************", 15, 50);
		g.drawString("坦	克大战", 30, 70);
		g.drawString("已发炮弹数 :" + missiles.size(), 30, 90);
		g.drawString("敌方坦克数 :" + tanks.size(), 30, 110);
		g.drawString("当前血量 :" + mytank.getBlood(), 30, 140);
		g.drawString("得分 :", 30, 170);
		g.drawString("***********************************", 15, 200);
		if (tanks.size() <= 0) { // 敌方坦克死掉后，重新生成
			for (int i = 0; i < 6; i++) {
				tanks.add(new Tank(50 + 40 * (i + 1), 100, Tank.Direction.D,
						false, this));
			}
		}
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);// 检测每一发子弹是否打中了 坦克
			m.hitTank(mytank);// 检测是否打中我方坦克
			m.hitWall(wall1);// 限制子弹穿墙
			m.hitWall(wall2);
			if (!m.isLive())
				missiles.remove(m); // 如果子弹 超过屏幕范围，越界死亡后，从集合中删除一个
			else
				m.draw(g); // 通过集合存放子弹，每打一发从集合中取出
		}
		for (int i = 0; i < explodes.size(); i++) { // 从爆炸效果中取出 爆炸圆 并画出 多个爆炸效果
			Explode explode = explodes.get(i);
			explode.draw(g);
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			tank.collideWithWall(wall1);// 坦克撞墙方法，如果敌方坦克撞到墙 2，重新返回上一步的位置，开始随机
			tank.collideWithWall(wall2);
			tank.collideWithWalls(tanks);
			tank.draw(g);
		}
		mytank.draw(g); // 画出坦克
		mytank.eatBlood(blood);
		explode.draw(g);// 爆炸效果
		wall1.draw(g); // 障碍物1
		wall2.draw(g); // 障碍物2
		blood.draw(g); // 规定路线的血块，吃掉后以增加我方坦克的血量
	}

	/**
	 * 
	 * @ClassName: paintThread （重画进程）
	 * @Description: 新建一个线程，不断重刷 坦克 repaint 方法 ，通过paint
	 *               中显示位置变量的每次不断增加变量，达到坦卡运动的目的
	 * @author mindcont
	 * @date 2015年10月13日 上午9:12:31
	 *
	 */
	private class paintThread implements Runnable {
		@Override
		public void run() {

			while (true) {
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 
	 * @ClassName: keyMonitor
	 * @Description: 监听 键盘按键动作，用来控制坦克运动方向，子弹如何发射，特殊技能的释放，我方坦克的复活
	 * @author mindcont
	 * @date 2015年10月13日 上午9:13:06
	 *
	 */
	private class keyMonitor extends KeyAdapter {

		// 重新按键按下动作，在tank.java 中调用相 关的keypressed 方法
		@Override
		public void keyPressed(KeyEvent e) {
			mytank.KeyPressed(e);
		}

		// 重新按键松开动作，在tank.java 中调用相关的KeyReleased 方法
		@Override
		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}
	}
}
