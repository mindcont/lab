/***********************************************************************
 * @filename: EEPROM.C
 * @parameter �ɱ���߼��洢��c2402��������
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"
/*
����������ַ���֣�������ַ�����λΪ��д����λ
0Ϊд��1Ϊ��
����д����ʱΪ������ַ����0
������ʱΪ������ַ����1


/*************************************
** ��������: void Delay_24C02(void)
** ��������: eeprom24C02 ��ʱ    
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Delay_24C02(void)
{ ; ; }

/*************************************
** ��������: void Init_24C02(void)
** ��������: eeprom24C02 ��ʼ��  
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Init_24C02(void)//24C02��ʼ��
{
	SDA_EE=1;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
}

/*************************************
** ��������: void Start(void)
** ��������:eeprom24C02 �����ź�  
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Start(void)//24C02�����ź�
{
	SDA_EE=1;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
	SDA_EE=0;
	Delay_24C02();
}

/*************************************
** ��������: void Stop(void)
** ��������: eeprom24C02 ��ֹ�ź� 
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Stop(void)//24C02��ֹ�ź�
{
	SDA_EE=0;
	Delay_24C02();
	CLK_EE=1;
	Delay_24C02();
	SDA_EE=1;
	Delay_24C02();
}

/*************************************
** ��������:void Respons(void)
** ��������:eeprom24C02 Ӧ���ź�  
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
void Respons(void)//Ӧ���ź�
{
	u8 i=0;
	CLK_EE=1;
	Delay_24C02();
	while((SDA_EE==1)&&(i<255)) 
	i++;
	CLK_EE=0;
	Delay_24C02();
}

/*************************************
** ��������: void WR_Byte(u8 Data)
** ��������: eeprom24C02 дһ���ֽ�,��8λ 
** ��ڲ���: u8 Data
** ���ڲ���: ��
**************************************/
void WR_Byte(u8 Data)//дһ���ֽ�,��8λ
{
	u8 i,WData;
	WData=Data;
	for(i=0;i<8;i++)
		{
			WData=WData<<1;
			CLK_EE=0;
			Delay_24C02();
			SDA_EE=CY;
			Delay_24C02();
			CLK_EE=1;
			Delay_24C02();
		}
		CLK_EE=0;
		Delay_24C02();
		SDA_EE=1;
		Delay_24C02();
}

/*************************************
** ��������: u8 Read_Byte(void)
** ��������: eeprom24C02 ��һ���ֽ� 
** ��ڲ���: ��
** ���ڲ���: ��
**************************************/
u8 Read_Byte(void)//��һ���ֽ�
{
	u8 i,RData;
	CLK_EE=0;
	Delay_24C02();
	SDA_EE=1;
	Delay_24C02();
	for(i=0;i<8;i++)
	{
		CLK_EE=1;
		Delay_24C02();	
		RData=(RData<<1)|SDA_EE;
		CLK_EE=0;
		Delay_24C02();	
	}
	return RData;
}

/*************************************
** ��������: void WR_AddData(u8 Address,u8 Data)
** ��������: eeprom24C02 ָ����ַдһ������ 
** ��ڲ���: u8 Address,u8 Data
** ���ڲ���: ��
**************************************/
void WR_AddData(u8 Address,u8 Data)//ָ����ַдһ������
{
	Start();
	WR_Byte(0xa2);
	Respons();
	WR_Byte(Address);
	Respons();
	WR_Byte(Data);
	Respons();
	Stop();
}

/*************************************
** ��������: u8 Read_Data(u8 Address)
** ��������: eeprom24C02 ����ָ����ַ������
** ��ڲ���: u8 Address
** ���ڲ���: ��
**************************************/
u8 Read_Data(u8 Address)//����ָ����ַ������
{
	u8 Data;
	Start();
	WR_Byte(0xa2);
	Respons();
	WR_Byte(Address);
	Respons();
	Start();
	WR_Byte(0xa3);
	Respons();
	Data=Read_Byte();
	Stop();
	return Data;
}


