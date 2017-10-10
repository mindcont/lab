/** 
 * @Title: Explode.java 
 * @Package  
 * @Description: 用来做子弹爆炸效果
 * @author mindcont
 * @date 2015年10月11日 下午7:35:10 
 * @version V1.0 
 */

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @ClassName: Explode
 * @Description: 用于爆炸效果的控制 ，本游戏中用一系列 大小渐变的圆来代替子弹爆炸
 * @author mindcont
 * @date 2015年10月13日 上午9:26:16
 *
 */
public class Explode {
	int explode_x, explode_y; // 爆炸的位置
	private boolean live = true;// 爆炸是否成功
	int[] diameter = { 4, 7, 12, 18, 20, 24, 26, 28, 32, 35, 25, 18, 14, 10, 6 };// 表示爆炸圆的半径
	int step = 0;// 爆炸的运行步骤
	private TankClient tankClient; // 生成一个坦克客户端，大管家，用于方便的调用其他文件中相关方法

	/**
	 * 
	 * Title: Explode(int explode_x,int explode_y,TankClient tankClient)
	 * Description: 用于爆炸效果的控制
	 * 
	 * @param explode_x
	 *            爆炸位置的X坐标
	 * @param explode_y
	 *            爆炸位置的Y坐标
	 * @param tankClient
	 *            爆炸 的对象
	 */
	public Explode(int explode_x, int explode_y, TankClient tankClient) {

		this.explode_x = explode_x;
		this.explode_y = explode_y;
		this.tankClient = tankClient;
	}

	/**
	 * 
	 * @Title: draw
	 * @Description: 爆炸效果的draw 方法，用于显示爆炸效果
	 * @param @param g 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void draw(Graphics g) {
		if (!live) {
			tankClient.explodes.remove(this);// 爆炸完成后，从爆炸集合中除去
			return;
		}
		if (step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color color = g.getColor();
		g.setColor(Color.white);
		g.fillOval(explode_x, explode_y, diameter[step], diameter[step]);
		g.setColor(color);
		step++;
	}

}
