//////////////////////////////////////
#ifndef __WIFI_H
#define	__WIFI_H

#define WPRT_STATE			(1<<0)
#define NIP_STATE			(1<<1)
#define ATM_STATE 			(1<<2)
#define ATRM_STATE 			(1<<3)
#define SSID_STATE 			(1<<4)
#define ENCRY_STATE 		(1<<5)
#define KEY_STATE 			(1<<6)

	
#define MAX_RECV_LEN 32

typedef struct 
{
	char* SSID; 	//网络名称

	char* KEY;	//key

	char* ObjIP;	//目标IP

	char* ObjPort; //目标端口

	char TorU;

}Wifi_InitTypeDef;

extern void UART_SendData(u8 *data_buf);
extern void Delay_500Us(u16 i);

void Wifi_ProcessAtResp(u8 Res);
void procBUFF(char* buf, u8 len, char* argv[], u16 *argc);

s8 EnterCMDState();

u16 WIFI_Config(Wifi_InitTypeDef* Wifi_InitStruct);


s8 Testwifi(void);

#endif
/////////////////////////////////////////////////////////
