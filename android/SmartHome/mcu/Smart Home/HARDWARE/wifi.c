//#include   "include.h"
//u8 RecvLen;
//char idata RecvBUF[MAX_RECV_LEN];
////���յ���AT��Ӧ����RecvBUF[]�У��յ�0x0d,0x0a,0x0d,0x0a������ʱ��RecvLen���λ��1�����ж��е��øú���
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
////������յ���RecvBUF���ж��Ƿ�ɹ�,�ɹ�����0�����򷵻�-1;ע���жϺ󲢲���RecvLen��0
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
////�������ܣ������յ��ַ����ָ�����ɲ���������������
////�����б�
////buf:�������ַ���
////len:�������ַ�������
////argv: ���ص��ַ�������ָ��
////argc�����ص��ַ������鳤��ָ��
////���磺   	char myargv[10][32];int myargc;
////			procBUFF(RecvBUF+4,(RecvLen&0x7ffff)-4,myargv,&myargc); //RecvBUF = "+OK=xxx" xxx���ǽ�Ҫ������ַ���
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
////�������ܣ� ������������ģʽ
////ָ�+++
////����ֵ���ɹ�����0��ʧ�ܷ���-1��
//s8 EnterCMDState()
//{
//	u8 i=0;
//	UART_SendData("+++");
//
//	RecvLen = 0;	//��0�����ַ������峤��
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
//				RecvLen = 0;	//��0�����ַ������峤��
//				Delay_500Us(2000);
//			}	
//		}
//	}
//	return -1;	//Cannot receive "+OK"
//}
//
//
////���ݲ�������wifiģ��
//u16 WIFI_Config(Wifi_InitTypeDef* Wifi_InitStruct)
//{
//	u16 state = 0;
//	char *arrtmp;
//	if(EnterCMDState()==0)		  //��������ģʽ
//	{
//		RecvLen = 0;	//��0�����ַ������峤��
//		arrtmp = "AT+WPRT=0\r\n";		  //infra
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= WPRT_STATE;
//		
//		RecvLen = 0;	//��0�����ַ������峤��
//		arrtmp = "AT+NIP=0\r\n";		 //DHCP��̬�ֲ�
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= NIP_STATE;
//		
//		RecvLen = 0;	//��0�����ַ������峤��
//		arrtmp = "AT+ATM=0\r\n";		 //�Զ�����ģʽ
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ATM_STATE;
//		
//		RecvLen = 0;	//��0�����ַ������峤��
//		sprintf(arrtmp,"AT+ATRM=%d,0,\"%s\",%s,6000\r\n",Wifi_InitStruct->TorU,Wifi_InitStruct->ObjIP,Wifi_InitStruct->ObjPort); //����Ŀ��IP���˿ڣ�TCP/UDP��
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ATRM_STATE;
//		
//		RecvLen = 0;	//��0�����ַ������峤��
//		sprintf(arrtmp,"AT+SSID=\"%s\"\r\n", Wifi_InitStruct->SSID);	//SSID
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= SSID_STATE;	
//
//		RecvLen = 0;	//��0�����ַ������峤��
//		arrtmp = "AT+ENCRY=7\r\n";		 //���ܷ�ʽ
//		UART_SendData(arrtmp);
//		Delay_500Us(200);
//		if(RecvState()!=0)
//			state|= ENCRY_STATE;
//
//		RecvLen = 0;	//��0�����ַ������峤��
//		sprintf(arrtmp,"AT+KEY=1,0,\"%s\"\r\n", Wifi_InitStruct->KEY);	//ascii����Կ����0��Key
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
//	Wifi_InitStruct.ObjIP ="192.168.0.144" ;	//Ŀ��IP
//
//	Wifi_InitStruct.ObjPort = "6000"; //Ŀ��˿�
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
