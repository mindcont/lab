/***********************************************************************
 *@filename: SerialComm
 * @parameter:串口通信驱动函数
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"

u8 Buf[10]; //定义接收数据缓存
u8 SendCounter;//数组下标变量
u8 Flag;//接收完成标志位

/*
******************************************
** 函数名称 ：Serial_Reg(u8 BaudRate)
** 函数功能 ：串口初始化 中断优先级最高
** 入口参数 ：BaudRate
** 出口参数 ：无
*******************************************
*/
void Serial_Reg(u8 BaudRate)   //初始化UART
{	
	
	ES = 0;	 //关闭串口中断
	TR1 = 0;  //关闭定时器1
	TMOD |= 0x20; //定时器1工作在模式2
	TH1 = BaudRate;// 配置波特率
	TL1 = BaudRate;
	PCON =0x80;  //波特率倍增
	TR1 = 1;//开启T1定时器
	SCON = 0x50;// 串口工作在方式1  8位数据位  1位停止位  无校验位
	IP = 0x10;	// 串口优先中断
	ES = 1;	     //开启串口中断

}
/*
*********************************************
** 函数名称 ：HardWareInit(void)
** 函数功能 ：硬件初始化                           
** 入口参数 ：无
** 出口参数 ：无
*********************************************
*/
void HardWareInit(void)
{ 
	Lcd_Init();
	Init_24C02();   //IIC初始化
	EA = 0;
	Serial_Reg(BR_115K2);   // 初始化通讯, 波特率bps BR_9K6	  BR_115K2
	EA = 1;
}			 
/********************************************\
|* 功能： 延时5us 
\********************************************/
void  Delay_5us(void)
{
	u8 i;
	i--;
	i--;
	i--;
	i--;
	i--;
	i--;
}
/*************************************
** 函数名称: void Delay_500Us(u16 i)
** 功能描述: 演示500us
** 入口参数: i:延时时间
** 出口参数: 无
**************************************/
void Delay_500Us(u16 i)
{
  u16 j;
  	for(;i>0;i--)
  		for(j=110;j>0;j--);
}
/*************************************
** 函数名称: void UART_SendH(u8 data_buf)
** 功能描述: 串口发送数据
** 入口参数: data_buf:发送数据
** 出口参数: 无
*************************************/

void UART_SendH(u8 data_buf)
{
		SBUF = data_buf;	
		while(!TI);	//等待发送完成
            TI=0;//清除发送中断标志
 
}
/********************************************
** 函数名称: void UART_SendData(u8 *data_buf)
** 功能描述: 串口发送数据
** 入口参数: data_buf:发送数据缓冲区首地址
** 出口参数: 无
*********************************************/
void UART_SendData(u8 *data_buf)
{
	u8 iSendCounter = 0;
	
	while(data_buf[iSendCounter] != '\0')
	{
		SBUF = data_buf[iSendCounter];	
		while(TI==0);
            TI=0;
		    iSendCounter++;
	}  
}
/********************************************
** 函数名称: void UART_ClearData(void)
** 功能描述: 清空串口数据
** 入口参数: data_buf:发送数据缓冲区首地址
** 出口参数: 无
*********************************************/
void UART_ClearData(void)
{
	uchar i;
	for(i=0;i<strlen(Buf);i++)
	{
		Buf[i]='0';
	}
	SendCounter=0;
}

/******************************************
*功能:       UART接收中断程序
*输入参数:   无
*输出参数:   无
*返回值:     无
*************************************************/

void Serial_Interrupt() interrupt 4 
{

		if(RI==1)// 接收数据
		{	    
         		 
			RI=0; //软件清除接收中断
			if(Buf[SendCounter]!='0') //判断结束
			{
				  Buf[SendCounter]=SBUF; //取数据
				  SendCounter++;//数组下标自增，使下一个接收数据存到数组的下一位置
				  if( (SendCounter==4)&&(Buf[0]==0x04) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }

				  if( (SendCounter==2)&&(Buf[0]==0x05) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }

				  if( (SendCounter==2)&&(Buf[0]==0x02) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }

				  if( (SendCounter==2)&&(Buf[0]==0x01) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }

				  if( (SendCounter==2)&&(Buf[0]==0x03) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }

				  if( (SendCounter==2)&&(Buf[0]==0x06) )//接收两位数据完成
				  {
				  	Flag=1;
					SendCounter=0; //数组下标置第一
				  }
			}		
		 	
		 }

}