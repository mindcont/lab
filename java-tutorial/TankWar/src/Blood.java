import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** 
 * @Title: Blood.java 
 * @Package  
 * @Description: 用来产生随机血量，我方坦克吃掉后，瞬时满血，只能使用一次
 * @author mindcont
 * @date 2015年10月12日 下午4:19:42 
 * @version V1.0 
 */

/**
 * @ClassName: Blood
 * @Description:固定路线的血块，以增加我方坦克的血量
 * @author mindcont
 * @date 2015年10月12日 下午4:19:42
 */
public class Blood {
	int step = 0;
	int bloodLocation_x, bloodLocation_y, bloodWidth, bloodHight;
	TankClient tankClient; // 生成 坦克客户端，游戏大管家对象，可以调用相关的资源
	private boolean life = true; // 确定血块是否吃掉

	// life 的get方法
	public boolean isLife() {
		return life;
	}

	// life 的set方法
	public void setLife(boolean life) {
		this.life = life;
	}

	/**
	 * 用于定义血块的规定轨迹控制
	 */
	private int[][] Position = { { 300, 300 }, { 330, 340 }, { 355, 275 },
			{ 370, 270 }, { 390, 400 }, { 400, 100 }, { 380, 280 },
			{ 300, 240 } };

	/**
	 *
	 * Title: public Blood 方法 Description: 用于定义随机血块的x ,y 坐标和 长宽
	 */
	public Blood() {
		bloodLocation_x = Position[0][0];
		bloodLocation_y = Position[0][1];
		bloodWidth = bloodHight = 10;
	}

	/**
	 * 
	 * @Title: draw
	 * @Description: 随机血块的draw 方法，用于显示随机血块
	 * @param @param g 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void draw(Graphics g) {
		if (!life)
			return;
		Color color = g.getColor();
		g.setColor(color.magenta); // 品红
		g.fillRect(bloodLocation_x, bloodLocation_y, bloodWidth, bloodHight);
		g.setColor(color);
		move();
	}

	/**
	 * 
	 * @Title: move
	 * @Description: 随机血块的move 方法，用于从位置表中 读取坐标，并移动
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void move() {
		step++;
		if (step == Position.length) {
			step = 0;
		}
		bloodLocation_x = Position[step][0];
		bloodLocation_y = Position[step][1];
	}

	/**
	 * 
	 * @Title: getRect
	 * @Description: 获得随机血块的 显示位置矩形，以便 判断我方塔克 是否相撞而 进行满血操作
	 * @param @return 设定文件
	 * @return Rectangle 返回随机血块的显示位置的矩形
	 * @throws
	 */
	public Rectangle getRect() {
		return new Rectangle(bloodLocation_x, bloodLocation_y, bloodWidth,
				bloodHight);
	}

}
