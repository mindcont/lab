#ifndef __ADDA_H
#define	__ADDA_H

extern void Delay_500Us(u16 i);
extern void SendCMD(u8 IP_Add,u8 Data);
extern u8 RecData(u8 IP_Add);
extern void DACconversion(u8 IP_Add, u8 CMD, u8 Val);
#define  PCF8591 0x90    //PCF8591 µÿ÷∑

#endif