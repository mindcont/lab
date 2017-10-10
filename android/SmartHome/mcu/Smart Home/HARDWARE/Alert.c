/***********************************************************************
 * @filename: Alert.C
 * @parameter 烟雾报警函数
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/ 

#include   "include.h"	    


void alert(void)
{
		GPIO_JDQ=1;
		UART_SendData("ATI\r\n");
		Delay_500Us(4000);
		GPIO_JDQ=0;
		UART_SendData("AT+CPIN?\r\n");//检查SIM 卡
		Delay_500Us(4000);
		GPIO_JDQ=1;
		UART_SendData("ATE0\r\n");//关闭回显
		Delay_500Us(4000);
		GPIO_JDQ=0;
		UART_SendData("ATD18340863756;\r\n");//拨打电话
		Delay_500Us(4000);
	
}
