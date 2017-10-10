/** 
 * @Title: Missile.java 
 * @Package  
 * @Description: 坦克游戏中子弹对象的一系列控制
 * @author mindcont
 * @date 2015年10月10日 下午2:57:55 
 * @version V1.0 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.annotation.Retention;
import java.util.List;

/**
 * @ClassName: Missile
 * @Description: 子弹的控制
 * @author mindcont
 * @date 2015年10月10日 下午2:57:55
 * 
 */
public class Missile {
	int missileLocation_x, missileLocation_y; // 定义子弹显示位置
	public static final int Width = 10;
	public static final int Hight = 10;

	public static final int mSpeed_x = 15;
	public static final int mSpeed_y = 15;// 定义子弹的速度

	private TankClient tankClient;// 声明一个tankClient对象
	Tank.Direction dir;// 引用tank.java中方向枚举类型

	private boolean Live = true;// 定子弹是否爆炸
	private boolean partnerMissile;// 同伙的子弹，不会打伤同伙 区分敌我子弹

	// 子弹是否爆炸过 的get方法
	public boolean isLive() {
		return Live;
	}

	/**
	 * 
	 * Title: Missile(int missileLocation_x, int missileLocation_y,
	 * Tank.Direction dir, Boolean partnerMissile, TankClient tankClient)
	 * Description: 子弹的相关控制
	 * 
	 * @param missileLocation_x
	 *            子弹的显示 位置 X 坐标
	 * @param missileLocation_y
	 *            子弹的显示位置 Y坐标
	 * @param dir
	 *            子弹的方向 （8个方向可选）
	 * @param partnerMissile
	 *            用于区分敌我子弹的 布尔类型变量， ture 为我方坦克的子弹， 否则为敌方
	 * @param tankClient
	 *            用于生成坦克的管家，方便调用其他文件中的相关资源
	 */
	public Missile(int missileLocation_x, int missileLocation_y,
			Tank.Direction dir, Boolean partnerMissile, TankClient tankClient) {

		this.missileLocation_x = missileLocation_x; // 子弹的位置
		this.missileLocation_y = missileLocation_y;
		this.dir = dir;
		this.partnerMissile = partnerMissile;
		this.tankClient = tankClient;
	}

	/**
	 * 
	 * @Title: draw
	 * @Description: 子弹的draw 方法用于显示子弹对象
	 * @param @param g 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void draw(Graphics g) {
		if (!Live) // 每次绘画前，先判断子弹是否爆炸过
			tankClient.missiles.remove(this); // 是，则去除，不会显示
		Color missileColor = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(missileLocation_x, missileLocation_y, 10, 10); // 画出一个小黑点
																	// 代表子弹
		g.setColor(missileColor);

		move(); // 调用子弹的移动方法
	}

	/**
	 * 
	 * @Title: move
	 * @Description: 子弹的move 方法
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void move() {
		switch (dir) { // 选择相应的方向，进行位置的移动
		case L:
			missileLocation_x -= mSpeed_x;
			break;
		case LU:
			missileLocation_x -= mSpeed_x;
			missileLocation_y -= mSpeed_y;
			break;
		case U:
			missileLocation_y -= mSpeed_y;
			break;
		case RU:
			missileLocation_x += mSpeed_x;
			missileLocation_y -= mSpeed_y;
			break;
		case R:
			missileLocation_x += mSpeed_x;
			break;
		case RD:
			missileLocation_x += mSpeed_x;
			missileLocation_y += mSpeed_y;
			break;
		case D:
			missileLocation_y += mSpeed_y;
			break;
		case LD:
			missileLocation_x -= mSpeed_x;
			missileLocation_y += mSpeed_y;
			break;

		}
		// 如果子弹越界
		if (missileLocation_x < 0 || missileLocation_x > TankClient.GAME_WIDTH
				|| missileLocation_y < 0
				|| missileLocation_y > TankClient.GAME_HIGHT) {
			Live = false;// 子弹越界 死亡
		}
	}

	/**
	 * 
	 * @Title: getRec
	 * @Description: 获得子弹的矩形方块，以便判断子弹是否击中坦克
	 * @param @return 设定文件
	 * @return Rectangle 返回类型
	 * @throws
	 */
	public Rectangle getRec() {
		return new Rectangle(missileLocation_x, missileLocation_y, Width, Hight);
	}

	public boolean hitTank(Tank tank) {// 如果我方坦克 未被击中 //子弹的矩形和坦克矩形碰撞到，且坦克未被击中
										// //当不是同伙，子弹产出威力
		if (this.Live && this.getRec().intersects(tank.getRec())
				&& tank.isLive() && this.partnerMissile != tank.isGood()) {
			if (tank.isGood()) { // 如果是我方坦克，每次击中血量减少20
				tank.setBlood(tank.getBlood() - 20);
				if (tank.getBlood() <= 0) {
					tank.setLive(false);// 坦克生命值为 0 ，死掉
				}
			} else { // 如果是敌方坦克，直接死掉
				tank.setLive(false);
			}
			this.Live = false; // 子弹同时消亡
			Explode explode = new Explode(missileLocation_x, missileLocation_y,
					tankClient);
			tankClient.explodes.add(explode);
			return true;// 击中目标
		}
		return false;// 否则没有打中
	}

	/**
	 * 
	 * @Title: hitTanks
	 * @Description: 以此检测 坦克集合中的每个坦克是否被打中，
	 * @param @param tanks
	 * @param @return 如果打中返回真
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: hitWall
	 * @Description: 检测子弹是否撞墙
	 * @param @param wall
	 * @param @return 撞墙 返回为真，否则返回 假
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean hitWall(Wall wall) {
		if (this.Live && this.getRec().intersects(wall.getRectangle())) {
			this.Live = false;
			return true;
		}
		return false;
	}
}
