/***********************************************************************
 * @filename: DHT11.C
 * @parameter 温湿度传感器驱动函数
 * @author :mindcont
 * @date  :2016.03.17
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"

//----以下变量均为全局变量--------
//----温度高8位== U8T_data_H------
//----温度低8位== U8T_data_L------
//----湿度高8位== U8RH_data_H-----
//----湿度低8位== U8RH_data_L-----
//----校验 8位 == U8checkdata-----
//--------------------------------
	
u8  Count;//等待计数变量
u8  Checkdata;//校验数据和
u8  T_data_H,RH_data_H;//温湿度有效数据高位
u8  T_data_H_Rev,T_data_L_Rev,RH_data_H_Rev,RH_data_L_Rev,Checkdata_Rev;//接收温湿度五字节数据
u8  ByteData;

/***********************************
** 函数名称: void ReadOneByte(void)
** 功能描述: 读取一个字节数据
** 入口参数: 无
** 出口参数: 无
************************************/

void  ReadOneByte(void)
{
	u8 i;	
	for(i=0;i<8;i++)	   
	{	
		Count=2;	
		while((!DataPin)&&Count++);
		Delay_5us();
		Delay_5us();
		Delay_5us();
		
		Delay_5us();
		Delay_5us();
		Delay_5us();

		Checkdata=0;

		if(DataPin)
			Checkdata=1;
		Count=2;
		while((DataPin)&&Count++);
		//超时则跳出for循环		  
		if(Count==1)
			break;
		//判断数据位是0还是1	 		
		// 如果高电平高过预定0高电平值则数据位为 1 		
		ByteData<<=1;
		ByteData|=Checkdata;        
	}//for

}

/*************************************
** 函数名称: void DHT11ReadData(void)
** 功能描述: 温湿度读取子程序
** 入口参数: 无
** 出口参数: 无
***************************************/

void DHT11ReadData(void)
{
	//主机拉低18ms 
	DataPin=0;
	Delay_500Us(36);
	DataPin=1;
	//总线由上拉电阻拉高 主机延时20us
	Delay_5us();
	Delay_5us();
	Delay_5us();
	Delay_5us();
	//主机设为输入 判断从机响应信号 
	DataPin=1;
	//判断从机是否有低电平响应信号 如不响应则跳出，响应则向下运行	  
	if(!DataPin)		  
	{
		Count=2;
		//判断从机是否发出 80us 的低电平响应信号是否结束	 
		while((!DataPin)&&Count++);
		Count=2;
		//判断从机是否发出 80us 的高电平，如发出则进入数据接收状态
		while((DataPin)&&Count++);
		//数据接收状态		 
		ReadOneByte();
		RH_data_H_Rev=ByteData;
		ReadOneByte();
		RH_data_L_Rev=ByteData;
		ReadOneByte();
		T_data_H_Rev=ByteData;
		ReadOneByte();
		T_data_L_Rev=ByteData;
		ReadOneByte();
		Checkdata_Rev=ByteData;
		DataPin=1;
		//数据校验 
		
		Checkdata=(T_data_H_Rev+T_data_L_Rev+RH_data_H_Rev+RH_data_L_Rev);
		if(Checkdata==Checkdata_Rev)
		{
			RH_data_H=RH_data_H_Rev;
			T_data_H=T_data_H_Rev;
		}
	}

}