/***********************************************************************
 * @filename: DS1302.C
 * @parameter：实时时钟驱动函数
 * @author :mindcont
 * @date   :2016.03.17
 * @version 1.0 
 * @return  NUll
************************************************************************/
#include "include.h"

											//秒、分、小时、日、月、星期、年
const u8 WR_Buf[7]={0x21,0x00,0x16,0x20,0x03,0x07,0x16};        
code u8 WR_Add[7]={0x80,0x82,0x84,0x86,0x88,0x8a,0x8c}; 
code u8 RD_Add[7]={0x81,0x83,0x85,0x87,0x89,0x8b,0x8d};
u8 RD_Buf[7];

/*************************************
** 函数名称: void WR_1302_Byte(u8 temp) 
** 功能描述: 向DS1302 写入一个字节 
** 入口参数: u8 temp
** 出口参数: 无
**************************************/
void WR_1302_Byte(u8 temp) 
{
 u8 i;
 for (i=0;i<8;i++)     	//循环8次 写入数据
  { 
     CLK_DS1302=0;
     Data_DS1302=temp&0x01;     	//每次传输低字节 
     temp>>=1;  		//右移一位
     CLK_DS1302=1;
   }
}

/*************************************
** 函数名称: void WR_1302( u8 add,u8 date ) 
** 功能描述: 向DS1302 写入数据  
** 入口参数: u8 add,u8 date
** 出口参数: 无
**************************************/
void WR_1302( u8 add,u8 date )     
{
 	RST_DS1302=0;
	_nop_();
 	CLK_DS1302=0;
	_nop_();
 	RST_DS1302=1;	
   	_nop_();   //启动
 	WR_1302_Byte(add);	//发送地址
 	WR_1302_Byte(date);		//发送数据
 	RST_DS1302=0;  		//恢复
}	

/*************************************
** 函数名称: u8 Read_1302(u8 addr )
** 功能描述: 从DS1302 读取数据 
** 入口参数: u8 addr 
** 出口参数: 无
**************************************/
u8 Read_1302(u8 addr )
{
 	u8 i,temp=0x00;
 	RST_DS1302=0;
	_nop_();
 	CLK_DS1302=0;
	_nop_();
 	RST_DS1302=1;
	_nop_();
 	WR_1302_Byte(addr);
 	for (i=0;i<8;i++) 		//循环8次 读取数据
 	{
		CLK_DS1302=1;	
		_nop_();	
 		if(Data_DS1302)
 		temp|=0x80;			//每次传输低字节
		CLK_DS1302=0;
		temp>>=1;			//右移一位
	} 
 	RST_DS1302=0;
	_nop_();		
	RST_DS1302=1;
	Data_DS1302=0;

	return (temp);			//返回
}

/*************************************
** 函数名称: void Read_RTC(void)
** 功能描述: 读取 日历
** 入口参数: 无
** 出口参数: 无
**************************************/
void Read_RTC(void)		//读取 日历
{
 u8 i,*p;
 p=RD_Add; 	//地址传递
 for(i=0;i<7;i++)		//分7次读取 年月日时分秒星期
 {
  RD_Buf[i]=Read_1302(*p);
  p++;
 }
}

/*************************************
** 函数名称: void Set_RTC(void)
** 功能描述: 设定 日历
** 入口参数:  无
** 出口参数: 无
**************************************/
void Set_RTC(void)		//设定 日历
{
	u8 i,*p;
 	WR_1302(0x8E,0X00);
	
 	p=WR_Add;	//传地址	
 	for(i=0;i<7;i++)		//7次写入 年月日时分秒星期
 	{
		  WR_1302(*p,WR_Buf[i]);
 		 p++;  
	 }
	 WR_1302(0x8E,0x80);
}

/*************************************
** 函数名称: void DisTime(void)
** 功能描述: 1602液晶显示日期和时间 
** 入口参数: 无
** 出口参数: 无
**************************************/
void DisTime(void)
{
		Wr_Cmd(1,0x80+0x40+9);Display(RD_Buf[0]);
		
		Wr_Cmd(1,0x80+0x40+6);	Display(RD_Buf[1]);
	
		Wr_Cmd(1,0x80+0x40+3);Display(RD_Buf[2]);
		
		Wr_Cmd(1,0x80+10);	Display(RD_Buf[3]);
	
		Wr_Cmd(1,0x80+7);Display(RD_Buf[4]);
		
		Wr_Cmd(1,0x80+15);Wr_Data(0x30+RD_Buf[5]);
		
		Wr_Cmd(1,0x80+4);	Display(RD_Buf[6]);
	
}


