/***********************************************************************
 *@filename: LCD1602.C
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @parameter GSM ��������
 * @since  2015.5.8
 * @return  NUll
************************************************************************/

#include "include.h"


u8 num=0;
u8 code DATE[]="  2016-03-18   5";
u8 code TIME[]="   16:16:21     ";

void delayus(u16 k)
{
   for(;k>0;k--);
}
void CheckBusy(void) //���Һ��æ״̬����
{
  RS_LCD=0;
  RW_LCD=1;
  EN_LCD=0;
  delayus(8);
  EN_LCD=1;
  while(P1&0x80);
  
}
/*************************************
** ��������: void Wr_Cmd(u8 Busy,u8 Cmd)
** ��������: 1602д����
** ��ڲ���: unsigned char comm
** ���ڲ���: ��
**************************************/
void Wr_Cmd(u8 Busy,u8 Cmd)//дָ���Ӻ���
{
   if(Busy)
   CheckBusy();
   RS_LCD=0;
   RW_LCD=0;
   P1=Cmd;
   delayus(6);
   EN_LCD=0;
   Delay_500Us(8);
   EN_LCD=1;
}
/*************************************
** ��������: void Wr_Data(u8 Date)
** ��������: 1602 д�Ĵ���
** ��ڲ���: unsigned char date
** ���ڲ���: ��
**************************************/
void Wr_Data(u8 Date)//д�����Ӻ���
{
   RS_LCD=1;
   RW_LCD=0;
   P1=Date;
   delayus(6);
   EN_LCD=0;
   Delay_500Us(8);
   EN_LCD=1;
}
/*************************************
** ��������: void Lcd_Init(void)
** ��������: 1602Һ����ʼ��
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Lcd_Init(void)
{
	u8 i;	
	RW_LCD=1;
	EN_LCD=1;
	Delay_500Us(30);
	Wr_Cmd(0,0x38);Delay_500Us(30);
	Wr_Cmd(0,0x38);Delay_500Us(30);
	Wr_Cmd(0,0x38);Delay_500Us(30);
	Wr_Cmd(1,0x38);
	Wr_Cmd(1,0x08);Delay_500Us(30);
		 
	Wr_Cmd(1,0x0c);Delay_500Us(30);  //c
	Wr_Cmd(1,0x06);Delay_500Us(30);
	Wr_Cmd(1,0x01);Delay_500Us(30);

	    Wr_Cmd(1,0x80);
			for(i=0;i<16;i++)
			{
				Wr_Data(DATE[i]);
				Delay_500Us(4);
			}
     	Wr_Cmd(1,0x80+0x40);
			for(i=0;i<16;i++)
			{
				Wr_Data(TIME[i]);
				Delay_500Us(4);
			}
}	
/*************************************
** ��������: void Display(u8 num)
** ��������: ��ʾ����
** ��ڲ���: u8 num
** ���ڲ���: ��
**************************************/
void Display(u8 num)
{
   Wr_Data(num/16+0x30);
   Wr_Data(num%16+0x30);  
}
/*************************************
** ��������: void writeString(unsigned char * str,  unsigned char length)
** ��������: �ַ�����ʾ
** ��ڲ���: unsigned char * str,  unsigned char length
** ���ڲ���: ��
**************************************/
void writeString(unsigned char * str,  unsigned char length)
{
     unsigned char i;
    for(i = 0; i < length; i++)
     {
         Wr_Data(str[i]);
     }
 }
