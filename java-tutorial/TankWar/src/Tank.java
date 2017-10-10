/** 
 * @Title: Tank.java 
 * @Package  
 * @Description: 坦克对象，一系列的控制方法
 * @author mindcont
 * @date 2015年10月10日 下午1:57:45 
 * @version V1.0 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * @ClassName: Tank
 * @Description: 坦克的类 ，用于坦克对象一些列的控制方法
 * @author mindcont
 * @date 2015年10月10日 下午1:57:45
 * 
 */
public class Tank {
	int tankLocation_x, tankLocation_y; // 坦克显示位置 变量定义
	private int oldtankLocation_x, oldtankLocation_y;// 记录上一步坦克 位置
	public static final int Width = 35;
	public static final int Hight = 35;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	private boolean bL = false, bU = false, bR = false, bD = false; // 四个方向的布尔类型
	private boolean live = true;// 区分坦克是否被击中，消亡

	// 坦克生命 life 的get 方法
	public boolean isLive() {
		return live;
	}

	// 坦克生命life 的set 方法
	public void setLive(boolean live) {
		this.live = live;
	}

	private BloodBar bloodBar = new BloodBar();
	private boolean good;// 定义区分 敌我坦克，我方为true,敌方为false

	// 定义布尔类型的变量 good , good 的get 方法
	public boolean isGood() {
		return good;
	}

	// good 的 set方法
	public void setGood(boolean good) {
		this.good = good;
	}

	private int Blood = 100;// 定义 坦克生命值

	// 获取生命值 血量
	public int getBlood() {
		return Blood;
	}

	// 设置生命值 血量
	public void setBlood(int Blood) {
		this.Blood = Blood;
	}

	/**
	 * 
	 * @Title: eatBlood
	 * @Description: 用于定义当我方塔克，吃掉随机血块的方法
	 * @param @param blood
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean eatBlood(Blood blood) {
		if (this.live && blood.isLife()
				&& this.getRec().intersects(blood.getRect())) {
			this.Blood = 100; // 每当吃掉，瞬时满血
			blood.setLife(false);
			return true;
		}
		return false;
	}

	TankClient tankClient; // 持有引用变量的引用，才可以访问其内部变量

	// 定义坦克的方向
	public enum Direction {
		L, LU, U, RU, R, RD, D, LD, STOP
	};

	private static Random random = new Random(); // 用来随机敌方塔克方向
	private int randomStep = random.nextInt(12) + 3;// 随机移动的步数 3~14

	/**
	 * 
	 * @ClassName: BloodBar
	 * @Description: 图形化显示 坦克的生命值 的 血量条
	 * @author mindcont
	 * @date 2015年10月13日 下午1:12:39
	 *
	 */

	private class BloodBar {
		// 血量条的 draw 方法
		public void draw(Graphics g) {
			Color color = g.getColor();
			g.setColor(color.red);
			g.drawRect(tankLocation_x, tankLocation_y - 10, Width, 10);
			int w = Width * Blood / 100;
			g.fillRect(tankLocation_x, tankLocation_y - 10, w, 10);
		}
	}

	private Direction dir = Direction.STOP;

	// 定义坦克炮筒的方向 ，用于坦克静止时 也可以打出炮弹
	private Direction barrelDir = Direction.L;

	/**
	 * 
	 * Title: Tank Description: 坦克对象的构造方法
	 * 
	 * @param tankLocation_x
	 *            坦克的位置 x 坐标
	 * @param tankLocation_y
	 *            坦克的位置 y 坐标
	 * @param dir
	 *            坦克的 方向
	 * @param good
	 *            用来区分 敌我坦克的变量 ture 为我方坦克， 否则为敌方坦克
	 * @param tankClient
	 *            坦克游戏 大管家，用来访问其他相关资源的变量
	 */
	public Tank(int tankLocation_x, int tankLocation_y, Direction dir,
			Boolean good, TankClient tankClient) {
		this.tankLocation_x = tankLocation_x;
		this.tankLocation_y = tankLocation_y;
		this.dir = dir;
		this.good = good; // 区分敌我坦克
		this.tankClient = tankClient;
	}

	/**
	 * 
	 * @Title: draw
	 * @Description: 坦克对象的 绘画 方法，用来显示坦克
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void draw(Graphics g) {

		if (!live) { // 检测坦克打中
			if (!good) { // 检测是敌方坦克
				tankClient.tanks.remove(this); // 敌方坦克打中，从敌方坦克集合中去除
			}
			return;
		}
		Color color = g.getColor(); // 获得原始位置的前景色
		if (good)
			g.setColor(Color.white); // 根据敌我标志位，设定不同的颜色
		else
			g.setColor(Color.red); // 我方为白色，敌我为黑色
		g.fillOval(tankLocation_x, tankLocation_y, Width, Hight);
		g.setColor(color); // 还原 前景色

		if (isGood()) { // 由于敌方坦克，击中死亡，这里我们只显示 我方坦克的血量
			bloodBar.draw(g); // 显示当前血量
		}
		// 根据坦克炮筒的方向 来 绘画出 相应方向的炮筒
		switch (barrelDir) {
		case L: // 左
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x, tankLocation_y
					+ Tank.Hight / 2);
			break;
		case LU:// 左上
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x, tankLocation_y);
			break;
		case U: // 上
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x + Tank.Width / 2,
					tankLocation_y);
			break;
		case RU: // 右上
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x + Tank.Width,
					tankLocation_y);
			break;
		case R: // 右
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x + Tank.Width,
					tankLocation_y + Tank.Hight / 2);
			break;
		case RD: // 右下
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x + Tank.Width,
					tankLocation_y + Tank.Hight);
			break;
		case D: // 下
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x + Tank.Width / 2,
					tankLocation_y + Tank.Hight);
			break;
		case LD: // 左下
			g.drawLine(tankLocation_x + Tank.Width / 2, tankLocation_y
					+ Tank.Hight / 2, tankLocation_x, tankLocation_y
					+ Tank.Hight);
			break;
		}
		move(); // 坦克的移动方法
	}

	// 定义 八个方向的tank move 方法
	void move() {
		this.oldtankLocation_x = tankLocation_x; // 记录坦克移动之前 位置
		this.oldtankLocation_y = tankLocation_y;

		// 根据 键盘的方向，来移动坦克
		switch (dir) {
		case L:
			tankLocation_x -= XSPEED;
			break;
		case LU:
			tankLocation_x -= XSPEED;
			tankLocation_y -= YSPEED;
			break;
		case U:
			tankLocation_y -= YSPEED;
			break;
		case RU:
			tankLocation_x += XSPEED;
			tankLocation_y -= YSPEED;
			break;
		case R:
			tankLocation_x += XSPEED;
			break;
		case RD:
			tankLocation_x += XSPEED;
			tankLocation_y += YSPEED;
			break;
		case D:
			tankLocation_y += YSPEED;
			break;
		case LD:
			tankLocation_x -= XSPEED;
			tankLocation_y += YSPEED;
			break;
		case STOP:
			break;

		}
		// 当坦克的方向 不为 静止时，炮筒方向 和坦克方向一致
		if (this.dir != Direction.STOP) {
			this.barrelDir = this.dir;
		}
		// 用来的坦克越界 处理
		if (tankLocation_x < 0)
			tankLocation_x = 0;
		if (tankLocation_y < 30)
			tankLocation_y = 30;
		if (tankLocation_x + Tank.Width > tankClient.GAME_WIDTH)
			tankLocation_x = tankClient.GAME_WIDTH - Tank.Width;
		if (tankLocation_y + Tank.Hight > tankClient.GAME_HIGHT)
			tankLocation_y = tankClient.GAME_HIGHT - Tank.Hight;

		if (!good) { // 如果是敌方坦克，每次调用move方法后，随机改变方向
			Direction[] dirs = Direction.values();
			if (randomStep == 0) { // 只有当随机步数为 0 的时候，我们 让敌方坦克 改变方向
				randomStep = random.nextInt(12) + 3;
				int randomNnbmer = random.nextInt(dirs.length);
				dir = dirs[randomNnbmer]; // 改变敌方坦克的方向
			}
			randomStep--;
			if (random.nextInt(40) > 38)
				this.fire();
		}

	}

	// 当坦克 位于越界边缘 会死亡，调用该方法 重新是坦克 返回到之前的位置，再进行随机化，避免 坦克越界 一直处于死亡 状态
	private void stay() {

		tankLocation_x = oldtankLocation_x;
		tankLocation_y = oldtankLocation_y;
	}

	/**
	 * 
	 * @Title: KeyPressed
	 * @Description: 监测 键盘按键 状态，执行不同功能
	 * @param @param e 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	// 检测键盘按键动作，重新对二进制 bL,bU,bR,bD 布尔类型 进行赋值，然后重新调用方向定义函数
	public void KeyPressed(KeyEvent e) {

		int keyValue = e.getKeyCode(); // 获得键盘按键值
		switch (keyValue) {
		case KeyEvent.VK_F2: // 按下F2 键，我方坦克再死亡后 复活
			if (!this.live) {
				this.live = true;
				this.setBlood(100);

			}
		case KeyEvent.VK_LEFT: // 左方向键
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locationDirection();
	}

	/**
	 * 
	 * @Title: keyReleased
	 * @Description: 监测键盘按键松开，执行对应按键所对应的功能
	 * @param @param e 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void keyReleased(KeyEvent e) {
		int keyValue = e.getKeyCode(); // 获得键盘按键值
		switch (keyValue) {
		case KeyEvent.VK_A: // 定义 松开 A键 为 超级炮弹，即同时向八个方向发射炮弹
			superFire();
			break;
		case KeyEvent.VK_CONTROL: // 定义 松开 ctrl 键 为发射子弹
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locationDirection();

	}

	// 八个方向的设定
	void locationDirection() {
		// 如果 向左的按键按下，且同时其它三个键未被按下
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;

	}

	/**
	 * 
	 * @Title: fire
	 * @Description: 子弹攻击方法
	 * @param @return 设定文件
	 * @return Missile 返回类型
	 * @throws
	 */
	public Missile fire() {
		if (!live)
			return null;// 如果坦克被击中，子弹失效
		int x = tankLocation_x + Tank.Width / 2 - Missile.Width / 2;
		int y = tankLocation_y + Tank.Hight / 2 - Missile.Hight / 2;
		Missile missile = new Missile(x, y, barrelDir, good, this.tankClient);// 子弹方向和炮筒方向一致
																				// ，同时把坦克敌我变量传递给
																				// 子弹
																				// 同伙属性，以区别是否产生威力
		tankClient.missiles.add(missile); // 添加子弹
		return missile; // 返回 攻击 子弹
	}

	/**
	 * 
	 * @Title: fire
	 * @Description: 重新定义 可以向指定方向发射子弹的方法
	 * @param @param dir 子弹攻击方向
	 * @param @return 返回攻击子弹
	 * @return Missile 返回类型
	 * @throws
	 */
	public Missile fire(Direction dir) {
		if (!live)
			return null;// 如果坦克被击中，子弹失效
		int x = tankLocation_x + Tank.Width / 2 - Missile.Width / 2;
		int y = tankLocation_y + Tank.Hight / 2 - Missile.Hight / 2;
		Missile missile = new Missile(x, y, dir, good, this.tankClient);// 子弹方向和炮筒方向一致
																		// ，同时把坦克敌我变量传递给
																		// 子弹
																		// 同伙属性，以区别是否产生威力
		tankClient.missiles.add(missile); // 添加子弹
		return missile; // 返回 攻击 子弹
	}

	/**
	 * 
	 * @Title: superFire
	 * @Description: 定义超级炮弹
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void superFire() {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < 8; i++) {
			tankClient.missiles.add(fire(dirs[i]));
		}
	}

	/**
	 * 
	 * @Title: getRec
	 * @Description: 获得坦克对象的矩形方块
	 * @param @return 设定文件
	 * @return Rectangle 返回类型
	 * @throws
	 */
	public Rectangle getRec() {
		return new Rectangle(tankLocation_x, tankLocation_y, Width, Hight);
	}

	/**
	 * 
	 * @Title: collideWithWall
	 * @Description: 获得坦克撞墙的方法
	 * @param @param wall
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean collideWithWall(Wall wall) {
		if (this.live && this.getRec().intersects(wall.getRectangle())) {
			this.stay();// 调用坦克与墙碰撞的 停止方法,即返回上一步的值
			return true;// 撞到墙
		}
		return false;// 没有撞到墙

	}

	/**
	 * 
	 * @Title: collideWithWalls
	 * @Description: 坦克撞 墙 的方法
	 * @param @param tanks 坦克集合中的坦克
	 * @param @return
	 * @return boolean 是否撞到墙，ture 代表撞到墙，否则则没有
	 * @throws
	 */
	public boolean collideWithWalls(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) { // 从坦克集合中拿出每一辆坦克进行 判断
													// ,避免坦克之间相互碾压的问题 ，坦克之间碰到会返回
			Tank tank = tanks.get(i);
			if (this != tank) { // 如果是两个不同的坦克相撞，判断
				if (this.live && tank.isLive()
						&& this.getRec().intersects(tank.getRec())) {
					this.stay();// 调用坦克与墙碰撞的 停止方法,即返回上一步的值
					tank.stay();
					return true;// 撞到墙
				}
			}
		}
		return false;// 没有撞到墙
	}

}
