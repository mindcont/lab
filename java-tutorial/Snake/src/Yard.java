/**   
* @Title: Yard.java
* @Package com.mindcont.games.snake
* @Description: 贪吃蛇面板文件，含主方法和控制部分
* @author mindcont 
* @date 2015年10月6日 下午2:10:11
* @version V1.0   
*/



package com.mindcont.games.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* @Title: Yard 面板
* @Description: 构造面板 布局，生成snake,egg 对象实例
* @param     
* @return void   
* @throws
*/
public class Yard extends Frame {
	PaintTread paintThread = new PaintTread();//新建一个回话进程
	private boolean gameOver=false;//游戏结束标志位
	
	//网格行列数 、 方块大小
	public static final int ROWS =15;
	public static final int COLS =15;
	public static final int BLOCK_SIZE =40;
	
	//设置游戏结束时 字体
	private Font fontGameOver = new Font("楷体",Font.ITALIC,20);
	private int score = 0; //初始得分为零

	//生成 贪吃蛇snake ,随机方块对象
	Snake snake =new Snake(this);//新建贪吃蛇
	Egg egg=new Egg();//新建物品块
	
	//修复刷屏时闪烁问题
	Image offScreenImage = null; 

	
	public void launch() {
		this.setLocation(180, 120);//显示窗口的位置
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor()); //监听键盘动作
		new Thread(paintThread).start();//进程启动
	}
	/**
	* @Title: main 方法，函数入口 
	* @Description: 调用面板launch 方法
	* @param     
	* @return void   
	* @throws
	*/
	public static void main(String[] args){
		new Yard().launch();
	}
	//游戏结束方法
	public void stop() {
		gameOver=true;
	}
	
	/**
	* @Title: paint
	* @Description: 绘制网格面板
	* @param     
	* @return void   
	* @throws
	*/
	@Override
	public void paint(Graphics g) {
		Color color=g.getColor();
		g.setColor(color.GRAY);
		g.fillRect(0, 0, COLS*BLOCK_SIZE, ROWS*BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		//画出横线
		for(int i =1;i<ROWS;i++){
	
			g.drawLine(0, BLOCK_SIZE*i, COLS*BLOCK_SIZE, BLOCK_SIZE*i);
		}
		//画出竖线
		for(int i=1;i<COLS;i++){
			g.drawLine(BLOCK_SIZE*i, 0, BLOCK_SIZE*i, BLOCK_SIZE*ROWS);
		}
		
		g.setColor(color.YELLOW);
		g.drawString("score ："+score, 40, 120);
		
		if(gameOver){
			g.setFont(fontGameOver);
			g.drawString("游戏结束", 40, 160);
	        
			// 游戏结束后，绘图进程重启
		    paintThread.restart();
		}
		
		g.setColor(color);
		egg.draw(g);
		snake.draw(g);
		snake.eat(egg);
	}
	
	/**
	* @Title: update
	* @Description: 更新显示面板
	* @param     
	* @return void   
	* @throws
	*/
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null){
			
			offScreenImage= this.createImage(COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		}
		Graphics goff =offScreenImage.getGraphics();
		paint(goff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	* @Title: PaintTread
	* @Description: 进程控制，调用Runnable 接口
	* @param     
	* @return void   
	* @throws
	*/
	private class PaintTread implements Runnable{
	 
	private boolean pause=false;
	private boolean running = true;

	public void run(){
		 while(running){
			 if(pause) continue;
			 else repaint();
			 try {
				Thread.sleep(350);//线程休眠100毫秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		 }
	 }

	/**
	* @Title: pause（暂停）
	* @Description: 按住f2 键，可以使游戏暂停
	* @param    
	* @return void    
	* @throws
	*/
	public  void pause() {
		this.pause = true;
	}
	/**
	* @Title: restart （重启）
	* @Description: 重新生成shake,开启游戏
	* @param    
	* @return void    
	* @throws
	*/
 	public void restart(){
 		this.pause =false;
 		snake = new Snake(Yard.this);
 		gameOver = false;
 	}
 
 }
	/**
	* @Title: KeyMonitor （监听键盘状态）
	* @Description: 由于控制部分，检测上下左右方向键，以便控制snake对象
	* @param    
	* @return void    
	* @throws
	*/
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F2) {
				paintThread.restart();
			}
			snake.keyPressed(e);
		}
	}
	
	/**
	* @Title: getScore （获得游戏分数）
	* @Description: 获得游戏分数
	* @param    
	* @return void    
	* @throws
	*/
	public int getScore() {
		return score;
	}
	/**
	* @Title: setScore（设置游戏分数）
	* @Description: 设置游戏分数
	* @param    
	* @return void    
	* @throws
	*/
	public void setScore(int score) {
		this.score = score;
	}
 
}