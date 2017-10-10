/**   
* @Title: Snake.java
* @Package com.mindcont.games.snake
* @Description: 有关贪吃蛇，生成节点，移动，绘制，检测死亡的方法
* @author mindcont 
* @date 2015年10月6日 下午2:10:11
* @version V1.0   
*/



package com.mindcont.games.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
* @Title: Snake 贪吃蛇
* @Description: 有关贪吃蛇 生成，添加头/尾节点，从尾部删除，移动，检测死亡
* @param     
* @return void   
* @throws
*/

public class Snake {
	
	//定义蛇节点 头，尾，初始节点个数
	private Node head = null;
	private Node tail = null;
	private int size = 1;
	//初始节点 位置 和方法 ，默认值向左
	private Node node= new Node(12, 15, Dir.left);
    private Yard yard;//选择哪个区域下
    
    // 在yard 面板中生成一个蛇节点
	public Snake(Yard yard){
		head=node;
		tail=node;
		size=1;
		this.yard=yard;
		
	}
	/**
	* @Title:addToHead
	* @Description: 向贪吃蛇 蛇身节点中添加到头部节点
	* @param     
	* @return void   
	* @throws
	*/
	public void addToHead(){
		Node node =null;
		switch (head.dir) {
		case left:
			node = new Node(head.row, head.col-1, head.dir);
			break;
		case up:
			node = new Node(head.row-1,head.col, head.dir);
			break;
		case right:
			node = new Node(head.row, head.col+1, head.dir);
			break;
		case down:
			node = new Node(head.row+1, head.col, head.dir);
			break;
		}
		node.next=head;
		head.previous=node;
		head =node;
		size++;
	}
	
	/**
	* @Title:addToTail
	* @Description: 向贪吃蛇 蛇身节点中添加到尾部节点
	* @param     
	* @return void   
	* @throws
	*/
	public void addToTail(){
		Node node =null;
		switch (tail.dir) {
		case left:
			node = new Node(tail.row, tail.col+1, tail.dir);
			break;
		case up:
			node = new Node(tail.row+1,tail.col, tail.dir);
			break;
		case right:
			node = new Node(tail.row, tail.col-1, tail.dir);
			break;
		case down:
			node = new Node(tail.row-1, tail.col, tail.dir);
			break;
		}
		
		tail.next=node;
		node.previous=tail;
		tail =node;
		size++;
	}
	
	/**
	* @Title: deleteFromTail
	* @Description: 从贪吃蛇蛇身节点中删除尾部节点，用于move 方法的调用
	* @param    
	* @return void    
	* @throws
	*/
	private void deleteFromTail() {
		if(size==0) return;
		tail = tail.previous;
		tail.next=null;
	}
	/**
	* @Title: draw
	* @Description: 绘制贪吃蛇
	* @param    
	* @return void    
	* @throws
	*/
	public void draw(Graphics g){
		if(size<= 0) return;
		move();//先move 再画出，可以提高键盘操作的灵敏度
		for(Node n = head;n!=null;n=n.next){
			n.draw(g);
		}
	}
	
	/**
	* @Title: move
	* @Description: 贪吃蛇的移动方法，实质就是从蛇头添加一个节点并相应的从蛇尾删除一个节点
	* @param    
	* @return void    
	* @throws
	*/
	private void move(){
		addToHead();
		deleteFromTail();
		checkDead();
	}
	
	/**
	* @Title: checkDead
	* @Description: 每次move 后都要检查是否满足贪吃蛇死亡条件
	* @param    
	* @return void    
	* @throws
	*/
	private void checkDead() {
		if(head.row < 2 || head.col < 0 || head.row > Yard.ROWS || head.col > Yard.COLS)  {
			yard.stop();
		}
		for(Node n = head.next; n != null; n = n.next) {
			if(head.row == n.row && head.col == n.col) {
				yard.stop();
			}
		}
	}
	
	/**
	* @Title: Node
	* @Description: 蛇身节点 构造
	* @param    
	* @return void    
	* @throws
	*/
	private class Node {
		
		int row,col;
		int width = Yard.BLOCK_SIZE;
		int height = Yard.BLOCK_SIZE;
		Dir dir= Dir.left;//默认node 节点的方向
		Node next= null; //初始化蛇身下一节节点为空
		Node previous = null;//初始化蛇身上一节点为空
		
		//蛇身节点的构造方法，带有横纵坐标和方向三个参数
		Node(int row, int col,Dir dir) {
			this.row = row;
			this.col = col;
			this.dir= Dir.left;
		}
		//设定节点的颜色背景
		void draw(Graphics g){
			Color color=g.getColor();
			g.setColor(Color.BLACK);
			g.fillRect(Yard.BLOCK_SIZE*col, Yard.BLOCK_SIZE*row, width, height);
			g.setColor(color);
		}
	}
	
	/**
	* @Title: getRect()
	* @Description: 获得蛇头位置
	* @param    
	* @return void    
	* @throws
	*/
	public Rectangle getRect() {
		
		return new Rectangle(Yard.BLOCK_SIZE*head.col,Yard.BLOCK_SIZE*head.row,head.width,head.height);
	}
	/**
	* @Title: eat(Egg egg)
	* @Description:贪吃蛇 eat 方法和游戏分数
	* @param    
	* @return void    
	* @throws
	*/
	public void eat(Egg egg) {
		if(this.getRect().intersects(egg.getRect())) {
			egg.reAppear();
			this.addToHead();
			yard.setScore(yard.getScore() + 5);
		}
	}
	
	/**
	* @Title: keyPressed
	* @Description: 检测键盘按下，相应的控制贪吃蛇移动位置
	* @param     
	* @return void   
	* @throws
	*/
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:   
			if(head.dir!=Dir.right) //防止贪吃蛇直线倒行
			head.dir=Dir.left;
			break;
		case KeyEvent.VK_UP:
			if(head.dir!=Dir.down)
			head.dir=Dir.up;
			break;
		case KeyEvent.VK_RIGHT:
			if(head.dir!=Dir.left)
			head.dir=Dir.right;
			break;
		case KeyEvent.VK_DOWN:
			if(head.dir!=Dir.up)
			head.dir=Dir.down;
			break;

		}
    }
}
