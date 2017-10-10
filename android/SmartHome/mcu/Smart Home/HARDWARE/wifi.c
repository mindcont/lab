//#include   "include.h"
//u8 RecvLen;
//char idata RecvBUF[MAX_RECV_LEN];
////将收到的AT响应存在RecvBUF[]中，收到0x0d,0x0a,0x0d,0x0a结束符时将RecvLen最高位置1；在中断中调用该函数
//void Wifi_ProcessAtResp(u8 Res)
//{   
//	u16 len = (RecvLen&0x7fff);
//	RecvBUF[len] = Res;
//	RecvLen++;
//	if(len>=4&&len<MAX_RECV_LEN)
//	{
//		if((Res == 0x0a)&&(RecvBUF[len-1]==0x0d)&&(RecvBUF[len-2] == 0x0a)&&(RecvBUF[len-3]==0x0d))
//			RecvLen|=0x8000;
//	}
//	else if(len>= MAX_RECV_LEN)
//	{
//		RecvLen = 0;
//	}
//}
////处理接收到的RecvBUF；判断是否成功,成功返回0，否则返回-1;注意判断后并不把RecvLen清0
//s8 RecvState()
//{
//	if(strncmp(RecvBUF,"+OK",3) == 0)
//	{
//		return 0;
//	}
//	else
//	{
//		return -1;
//	}
//}
//
////函数功能：将接收到字符串分割成若干参数，放于数组中
////参数列表：
////buf:待处理字符串
////len:待处理字符串长度
////argv: 返回的字符串数组指针
////argc：返回的字符串数组长度指针
////例如：   	char myargv[10][32];int myargc;
////			procBUFF(RecvBUF+4,(RecvLen&0x7ffff)-4,myargv,&myargc); //RecvBUF = "+OK=xxx" xxx才是将要处理的字符串
////
//void procBUFF(char* buf, u8 len, char* argv[], u16 *argc)
//{
//	u16 tempLen,tempIndex=0;
//	*argc = 0;
//
//	for(tempLen=0;tempLen<len;tempLen++)
//	{
//		argv[*argc][tempIndex] = buf[tempLen];
//		if(buf[tempLen]==',')
//		{
//			argv[*argc][tempIndex] = '\0';
//			tempIndex = 0;
//			(*argc)++;
//		}
//		else if((buf[tempLen]==0x0a)&&(buf[tempLen-1]==0x0d))
//		{
//			if(tempIndex>1)
//			{
//				argv[*argc][tempIndex-1] = '\0';
//				tempIndex = 0;
//				(*argc)++;
//			}
//			else
//				tempIndex = 0;				
//		}
//		else
//			tempIndex++;			
//	}
//}
//
////函数功能： 进入命令配置模式
////指令：+++
////返回值：成功返回0；失败返回-1；
//s8 EnterCMDState()
//{
//	u8 i=0;
//	UART_SendData("+++");
//
//	RecvLen = 0;	//清0接收字符串缓冲长度
//
//	Delay_500Us(200);
//	for(i=0; i<3; i++)
//	{
//		UART_SendData("AT+\r\n");	 //Test wifi module state
//		Delay_500Us(200);
//		if((RecvLen&0x7fff)>0)	//Receive Data	RecvLen use bit:[0-14], bit15 is the state;
//		{
//			if(RecvState() == 0)		   //Receive "+OK",success.
//				return 0;
//			else
//			{
//				UART_SendData("+++");
//				RecvLen = 0;	//清0接收字符串缓冲长度
//				Delay_500Us(2000);
//			}	
//		}
//	}
//	return -1;	//Cannot receive "+OK"
//}
//
//
////根据参数配置wifi模块
//u16 WIFI_Config(Wifi_InitTypeDef* Wifi_InitStruct)
//{
//	u16 state = 0;
//	char *arrtmp;
//	if(EnterCMDState()==0)		  //进入命令模式
//	{
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		arrtmp = "AT+WPRT=0\r\n";		  //infra
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= WPRT_STATE;
//		
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		arrtmp = "AT+NIP=0\r\n";		 //DHCP动态分布
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= NIP_STATE;
//		
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		arrtmp = "AT+ATM=0\r\n";		 //自动工作模式
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ATM_STATE;
//		
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		sprintf(arrtmp,"AT+ATRM=%d,0,\"%s\",%s,6000\r\n",Wifi_InitStruct->TorU,Wifi_InitStruct->ObjIP,Wifi_InitStruct->ObjPort); //配置目标IP，端口，TCP/UDP等
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ATRM_STATE;
//		
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		sprintf(arrtmp,"AT+SSID=\"%s\"\r\n", Wifi_InitStruct->SSID);	//SSID
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= SSID_STATE;	
//
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		arrtmp = "AT+ENCRY=7\r\n";		 //加密方式
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ENCRY_STATE;
//
//		RecvLen = 0;	//清0接收字符串缓冲长度
//		sprintf(arrtmp,"AT+KEY=1,0,\"%s\"\r\n", Wifi_InitStruct->KEY);	//ascii，密钥索引0，Key
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= KEY_STATE;
//	}
//	return state;
//
//}
//
//s8 Testwifi(void)
//{
//	Wifi_InitTypeDef  Wifi_InitStruct;
//	Wifi_InitStruct.SSID ="TORE";
//
//	Wifi_InitStruct.KEY = "88888888";	//key
//
//	Wifi_InitStruct.ObjIP ="192.168.0.144" ;	//目标IP
//
//	Wifi_InitStruct.ObjPort = "6000"; //目标端口
//
//	Wifi_InitStruct.TorU = 1;
//
//	if(WIFI_Config(&Wifi_InitStruct)== 0)
//		return 0;
//	else
//		return -1;
//}
//
/////************************************end file************************************/
