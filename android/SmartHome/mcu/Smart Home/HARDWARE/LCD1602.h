#ifndef __LCD1602_H
#define	__LCD1602_H

extern void Delay_500Us(u16 i);
extern void Wr_Cmd(u8 Busy,u8 Cmd);
extern void Wr_Data(u8 Date);
extern void CheckBusy(void);
extern void Lcd_Init(void);
void Display(u8 num);
void writeString(unsigned char * str,  unsigned char length);
#endif