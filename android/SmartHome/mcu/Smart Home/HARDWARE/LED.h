#ifndef __LED_H
#define	__LED_H

#define GPIO_LED P0
extern void Delay_500Us(u16 i);
void Led_Blink(u8 Led_Status);
#endif