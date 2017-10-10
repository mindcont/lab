#ifndef __INCLUDE_H
#define	__INCLUDE_H

#include <reg52.h>
#include <string.h>
#include <intrins.h>
#include <stdio.h>

#define s8  signed  char
#define u8  unsigned  char
#define u16 unsigned  int
#define u32 unsigned  long
#define uint unsigned int
#define uchar unsigned char

//#include "wifi.h"
#include "SerialComm.h"
#include "main.h" 
#include "DS1302.h"
#include "JDQ.h"
#include "LCD1602.h"
#include "ADDA.h"
#include "EEPROM.h"
#include "LED.h"
#include "Motor.h"
#include "DHT11.h"


sbit RS_LCD=P0^0;
sbit RW_LCD=P0^1;
sbit EN_LCD=P0^2;
sbit SDA_EE=P0^3;//数据引脚定义
sbit CLK_EE=P0^4;//时钟引脚定义


sbit smoke=P2^3; //烟雾传感器

sbit GPIO_JDQ=P3^2;
sbit Data_DS1302= P3^4;	//数据
sbit CLK_DS1302 = P3^6;	//时钟		
sbit RST_DS1302 = P3^5;// DS1302复位

sbit  DataPin  = P3^7 ;//数据引脚定义

#endif
