#include  "include.h"	

sbit LED0=P0^5;
sbit LED1=P0^6;
sbit LED2=P0^7;

uchar TIP0[] = "   Init GMS     ";
uchar TIP1[] = "sending messages";
uchar TIP2[] = "messages sended ";
uchar TIP3[] = "have phone call ";
uchar TIP4[] = "    Alert !!!   ";
uchar TIP5[] = "                ";

uchar code message[]="0011000D91688143803657F60008A0044F60597D\r\n";
uchar code PhoneCall[]="ATD18340856948;\r\n";

uchar hand(uchar *ptr)
{
	if(strstr(Buf,ptr)!=NULL)
		return 1;
	else
		return 0;
}

void gms(void)
{
	uchar i = 0,j;
	for(j=0;j<3;j++)
	{
		LED0=0;LED1=0;LED2=0;	Delay_500Us(2000);
		LED0=1;LED1=1;LED2=1;	Delay_500Us(2000);
	}
	
	Wr_Cmd(1,0x80);
	writeString(TIP0,16);
	Wr_Cmd(1,0x80+0x40);
	writeString(TIP5,16);
	Delay_500Us(5000);

	while(!hand("OK")) //用来同步模块与单片机的波特率
	{
    UART_ClearData();
		UART_SendData("ATI\r\n"); //设备初始化
		Delay_500Us(5000);
		i++;
		if(i>=5)
		{
			break;
			//return;
		}
		else
			;
	}	
	UART_SendData("AT+CPIN?\r\n");
	UART_SendData("ATE0\r\n");//关闭模块指令回显
	UART_SendData("AT+CPIN?\r\n");//查看是否读到手机卡
	Wr_Cmd(1,0x80);
	writeString(TIP1,16);
	Delay_500Us(5000);
	
//	UART_SendData("AT+CMMI=2,1\r\n");//开回显
//	Delay_500Us(5000);
//	UART_SendData("AT+CMGF=0\r\n"); //0 PDU 模式 1文本模式
//	Delay_500Us(5000);
//	UART_SendData("AT+CMGS=19\r\n");
//	Delay_500Us(5000);
//	UART_SendData(message);//中文短信内容  
//	Delay_500Us(5000);
//  UART_SendData(0x1A);//16进制发送
//  Wr_Cmd(1,0x80);
//	writeString(TIP2,16);
//	Delay_500Us(5000);
//	
	Wr_Cmd(1,0x80);
	writeString(TIP3,16);
	Delay_500Us(5000);
	
	UART_SendData(PhoneCall); //此处拨打电话
	Delay_500Us(5000);
}