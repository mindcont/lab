/***********************************************************************
 * @filename:LED.C
 * @parameter LED 驱动函数
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"

/*************************************
** 函数名称: void Delay_500Us(u16 i)
** 功能描述: 演示500us
** 入口参数: i:延时时间
** 出口参数: 无
**************************************/
void Led_Blink(u8 Led_Status)
{
	u8 Temp = 0x20;	  
	u8 i=0;
	if(Led_Status==1)
	{
		for(i=0;i<2;i++)
		{
			P0=~Temp;
			Delay_500Us(1000);	 //延时500MS
			Temp= Temp<< 1;    //循环移1位，点亮下一个LED "<<"为左移位
	 	}

		for(i=0;i<2;i++)
		{
			P0=~Temp;
			Delay_500Us(1000);	 //延时500MS
			Temp= Temp >> 1;    //循环移1位，点亮下一个LED "<<"为左移位
	 	}
	}	
	else
	GPIO_LED = 0xFF;
}