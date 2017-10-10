#ifndef __EEPROM_H
#define	__EEPROM_H

void Delay_24C02(void);
extern void Init_24C02(void);
void Start(void);
void Stop(void);
void Respons(void);
extern void WR_Byte(u8 Data);
extern u8 Read_Byte(void);
extern void WR_AddData(u8 Address,u8 Data);
extern void Delay_500Us(u16 i);
extern u8 Read_Data(u8 Address);

#define WriteData 1
#define ReadData  2

#endif