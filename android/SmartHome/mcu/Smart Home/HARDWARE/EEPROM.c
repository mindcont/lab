/***********************************************************************
 * @filename: EEPROM.C
 * @parameter 可编程逻辑存储器c2402驱动函数
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"
/*
发送器件地址部分，器件地址的最低位为读写控制位
0为写，1为读
所以写数据时为器件地址加上0
读数据时为器件地址加上1


/*************************************
** 函数名称: void Delay_24C02(void)
** 功能描述: eeprom24C02 延时    
** 入口参数: 无
** 出口参数: 无
**************************************/
void Delay_24C02(void)
{ ; ; }

/*************************************
** 函数名称: void Init_24C02(void)
** 功能描述: eeprom24C02 初始化  
** 入口参数: 无
** 出口参数: 无
**************************************/
void Init_24C02(void)//24C02初始化
{
	SDA_EE=1;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
}

/*************************************
** 函数名称: void Start(void)
** 功能描述:eeprom24C02 启动信号  
** 入口参数: 无
** 出口参数: 无
**************************************/
void Start(void)//24C02启动信号
{
	SDA_EE=1;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
	SDA_EE=0;
	Delay_24C02();
}

/*************************************
** 函数名称: void Stop(void)
** 功能描述: eeprom24C02 终止信号 
** 入口参数: 无
** 出口参数: 无
**************************************/
void Stop(void)//24C02终止信号
{
	SDA_EE=0;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
	SDA_EE=1;
	Delay_24C02();
}

/*************************************
** 函数名称:void Respons(void)
** 功能描述:eeprom24C02 应答信号  
** 入口参数: 无
** 出口参数: 无
**************************************/
void Respons(void)//应答信号
{
	u8 i=0;
	CLK_EE=1;
	Delay_24C02();
	while((SDA_EE==1)&&(i<255)) 
	i++;
	CLK_EE=0;
	Delay_24C02();
}

/*************************************
** 函数名称: void WR_Byte(u8 Data)
** 功能描述: eeprom24C02 写一个字节,共8位 
** 入口参数: u8 Data
** 出口参数: 无
**************************************/
void WR_Byte(u8 Data)//写一个字节,共8位
{
	u8 i,WData;
	WData=Data;
	for(i=0;i<8;i++)
		{
			WData=WData<<1;
			CLK_EE=0;
			Delay_24C02();
			SDA_EE=CY;
			Delay_24C02();
			CLK_EE=1;
			Delay_24C02();
		}
		CLK_EE=0;
		Delay_24C02();
		SDA_EE=1;
		Delay_24C02();
}

/*************************************
** 函数名称: u8 Read_Byte(void)
** 功能描述: eeprom24C02 读一个字节 
** 入口参数: 无
** 出口参数: 无
**************************************/
u8 Read_Byte(void)//读一个字节
{
	u8 i,RData;
	CLK_EE=0;
	Delay_24C02();
	SDA_EE=1;
	Delay_24C02();
	for(i=0;i<8;i++)
	{
		CLK_EE=1;
		Delay_24C02();	
		RData=(RData<<1)|SDA_EE;
		CLK_EE=0;
		Delay_24C02();	
	}
	return RData;
}

/*************************************
** 函数名称: void WR_AddData(u8 Address,u8 Data)
** 功能描述: eeprom24C02 指定地址写一个数据 
** 入口参数: u8 Address,u8 Data
** 出口参数: 无
**************************************/
void WR_AddData(u8 Address,u8 Data)//指定地址写一个数据
{
	Start();
	WR_Byte(0xa2);
	Respons();
	WR_Byte(Address);
	Respons();
	WR_Byte(Data);
	Respons();
	Stop();
}

/*************************************
** 函数名称: u8 Read_Data(u8 Address)
** 功能描述: eeprom24C02 读出指定地址的数据
** 入口参数: u8 Address
** 出口参数: 无
**************************************/
u8 Read_Data(u8 Address)//读出指定地址的数据
{
	u8 Data;
	Start();
	WR_Byte(0xa2);
	Respons();
	WR_Byte(Address);
	Respons();
	Start();
	WR_Byte(0xa3);
	Respons();
	Data=Read_Byte();
	Stop();
	return Data;
}


