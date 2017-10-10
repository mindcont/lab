/**   
* @Title: Egg.java
* @Package com.mindcont.games.snake
* @Description: 有关随机方块生成，绘制，重现，获取位置信息的文件
* @author mindcont 
* @date 2015年10月6日 下午2:10:11
* @version V1.0   
*/



package com.mindcont.games.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


/**
* @Title: Egg 随机方块
* @Description: 定义随机方块 行列数 和相应的get 、set 方法
* @param     
* @return void   
* @throws
*/
public class Egg {
	int row,col;
	int width = Yard.BLOCK_SIZE;
	int hight = Yard.BLOCK_SIZE;
	private Color color=Color.green;
	
	private static Random random = new Random();
	
	//定义egg： 随机块 出现的 行数，列数的构造方法
	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	// 构造对应两个参数对应的get 、set 方法
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	//egg 方法重写，随机出现块
	public Egg(){
		
		this(random.nextInt(Yard.ROWS-4)+3, random.nextInt(Yard.COLS));
	}
	
	//egg 复现
	public void reAppear(){
		this.row=random.nextInt(Yard.ROWS-4)+3;
		this.col=random.nextInt(Yard.COLS);
	}
	//获取egg 的位置
	public Rectangle getRect(){
		return new Rectangle(Yard.BLOCK_SIZE*col,Yard.BLOCK_SIZE*row,width,hight);
	}
	//绘制egg
	public void draw(Graphics g){
			Color color=g.getColor();
			g.setColor(Color.GREEN);
			g.fillRect(Yard.BLOCK_SIZE*col, Yard.BLOCK_SIZE*row, width, hight);
			g.setColor(color);
		}
	}
