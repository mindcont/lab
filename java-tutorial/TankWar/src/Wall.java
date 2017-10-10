/** 
 * @Title: Wall.java 
 * @Package  
 * @Description: 障碍物构建
 * @author mindcont
 * @date 2015年10月12日 上午8:59:54 
 * @version V1.0 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @Name: Wall
 * @Description: 定义墙 障碍物
 * @author mindcont
 * @date 2015年10月12日 上午8:59:54
 * 
 */
public class Wall {
	int wallLocation_x, wallLocation_y, wallWidth, wallHight; // 墙的 显示位置和宽度、高度
	TankClient tankClient; // 坦克大管家，方便使用其他文件中的方法

	/**
	 * Title: Wall Description: 用来构造 障碍物墙的方法
	 * 
	 * @param wallLocation_x
	 *            障碍物位置 X 坐标
	 * @param wallLocation_y
	 *            障碍物位置 Y 坐标
	 * @param wallWidth
	 *            障碍物 宽度
	 * @param wallHight
	 *            障碍物长度
	 * @param tankClient
	 *            坦克大管家
	 */
	public Wall(int wallLocation_x, int wallLocation_y, int wallWidth,
			int wallHight, TankClient tankClient) {
		this.wallLocation_x = wallLocation_x;
		this.wallLocation_y = wallLocation_y;
		this.wallWidth = wallWidth;
		this.wallHight = wallHight;
		this.tankClient = tankClient;
	}

	/**
	 * 
	 * @Title: draw
	 * @Description: 障碍物墙 的绘画方法
	 * @param @param g 
	 * @throws
	 */
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(wallLocation_x, wallLocation_y, wallWidth, wallHight);
	}

	/**
	 * 
	 * @Title: getRectangle
	 * @Description: 获取 障碍物墙 的显示矩形块，用来判断相应的子弹和坦克是否撞到墙
	 * @param @return 设定文件
	 * @return Rectangle 返回类型
	 * @throws
	 */
	public Rectangle getRectangle() {
		return new Rectangle(wallLocation_x, wallLocation_y, wallWidth,
				wallHight);
	}

}
