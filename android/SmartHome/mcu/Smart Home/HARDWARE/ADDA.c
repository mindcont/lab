/***********************************************************************
 * @filename: ADDA.c
 * @parameter 模数-数模转换
 * @author :mindcont
 * @date  :2016.03.17
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"
/*
发送器件地址部分，器件地址的最低位为读写控制位
0为写，1为读
所以写数据时为器件地址加上0
读数据时为器件地址加上1
*/
u16  D[1];//定义DAC设定数据

/*************************************
** 函数名称: void DACconversion(u8 IP_Add, u8 CMD, u8 Val)
** 功能描述: DAC 变换, 转化函数 
** 入口参数: u8 IP_Add, u8 CMD, u8 Val
** 出口参数: 无
**************************************/
void DACconversion(u8 IP_Add, u8 CMD, u8 Val)
{
   Start();              //启动总线
   WR_Byte(IP_Add);            //发送器件地址
   Respons();
   WR_Byte(CMD);              //发送控制字节
   Respons();
   WR_Byte(Val);            //发送DAC的数值  
   Respons();
   Stop();               //结束总线
}

/*************************************
** 函数名称: void SendCMD(u8 IP_Add,u8 Data)
** 功能描述: ADC发送字节[命令]数据函数 
** 入口参数: u8 IP_Add,u8 Data
** 出口参数: 无
**************************************/
void SendCMD(u8 IP_Add,u8 Data)
{
   Start();              //启动总线
   WR_Byte(IP_Add);      //发送器件地址
   Respons();			 //应答
   WR_Byte(Data);        //发送数据
   Respons();
   Stop();               //结束总线
}

/*************************************
** 函数名称: u8 RecData(u8 IP_Add)
** 功能描述: ADC读字节数据函数 
** 入口参数: u8 IP_Add
** 出口参数: 无
**************************************/
u8 RecData(u8 IP_Add)
{  
	u8 Data;

   Start();          //启动总线
   WR_Byte(IP_Add+1);  //发送器件地址
   Respons();      //应答
   Data=Read_Byte(); //读取数据
   Respons();
   Stop();           //结束总线
   return(Data);
}