#ifndef __SerialComm_H
#define	__SerialComm_H

//波特率发生器定时常数 22.1184MHZ
#define BR_0K6			0x40		// 600bps
#define BR_1K2			0xA0		// 1200bps
#define BR_2K4			0xD0		// 2400bps
#define BR_4K8			0xE8		// 4800bps
#define BR_9K6			0xF4		// 9600bps
#define BR_14K4			0xF8		// 14400bps
#define BR_19K2			0xFA		// 19200bps
#define BR_28K8			0xFC		// 28800bps
#define BR_38K4			0xFD		// 38400bps
#define BR_57K6			0xFE		// 57600bps
#define BR_115K2		0xFF		// 115200bps

void Serial_Init(void);
void UART_SendData(u8 *data_buf);
void UART_SendH(u8 data_buf);
void Serial_Reg(u8 BaudRate);
void HardWareInit(void);
void Delay_500Us(u16 i);
void Delay_5us(void);
void UART_ClearData(void);

#endif