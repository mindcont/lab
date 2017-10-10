/***********************************************************************
 *@filename:JDQ.C
 * @parameter 继电器控制函数
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"



/*************************************
** 函数名称: void JDQ_ON(u8 JDQ_Status)
** 功能描述: 继电器 开关
** 入口参数: JDQ_Status
** 出口参数: 无
**************************************/

void JDQ_ON(u8 JDQ_Status)
{
	if(JDQ_Status==1)
	{
		GPIO_JDQ=0;
		Delay_500Us(1000);	 //延时500MS
		GPIO_JDQ=1;
		Delay_500Us(1000);	 //延时500MS
	}	
	else
	GPIO_JDQ = 0;
}