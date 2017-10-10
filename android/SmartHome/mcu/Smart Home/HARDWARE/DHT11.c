/***********************************************************************
 * @filename: DHT11.C
 * @parameter ��ʪ�ȴ�������������
 * @author :mindcont
 * @date  :2016.03.17
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"

//----���±�����Ϊȫ�ֱ���--------
//----�¶ȸ�8λ== U8T_data_H------
//----�¶ȵ�8λ== U8T_data_L------
//----ʪ�ȸ�8λ== U8RH_data_H-----
//----ʪ�ȵ�8λ== U8RH_data_L-----
//----У�� 8λ == U8checkdata-----
//--------------------------------
	
u8  Count;//�ȴ���������
u8  Checkdata;//У�����ݺ�
u8  T_data_H,RH_data_H;//��ʪ����Ч���ݸ�λ
u8  T_data_H_Rev,T_data_L_Rev,RH_data_H_Rev,RH_data_L_Rev,Checkdata_Rev;//������ʪ�����ֽ�����
u8  ByteData;

/***********************************
** ��������: void ReadOneByte(void)
** ��������: ��ȡһ���ֽ�����
** ��ڲ���: ��
** ���ڲ���: ��
************************************/

void  ReadOneByte(void)
{
	u8 i;	
	for(i=0;i<8;i++)	   
	{	
		Count=2;	
		while((!DataPin)&&Count++);
		Delay_5us();
		Delay_5us();
		Delay_5us();
		
		Delay_5us();
		Delay_5us();
		Delay_5us();

		Checkdata=0;

		if(DataPin)
			Checkdata=1;
		Count=2;
		while((DataPin)&&Count++);
		//��ʱ������forѭ��		  
		if(Count==1)
			break;
		//�ж�����λ��0����1	 		
		// ����ߵ�ƽ�߹�Ԥ��0�ߵ�ƽֵ������λΪ 1 		
		ByteData<<=1;
		ByteData|=Checkdata;        
	}//for

}

/*************************************
** ��������: void DHT11ReadData(void)
** ��������: ��ʪ�ȶ�ȡ�ӳ���
** ��ڲ���: ��
** ���ڲ���: ��
***************************************/

void DHT11ReadData(void)
{
	//��������18ms 
	DataPin=0;
	Delay_500Us(36);
	DataPin=1;
	//������������������ ������ʱ20us
	Delay_5us();
	Delay_5us();
	Delay_5us();
	Delay_5us();
	//������Ϊ���� �жϴӻ���Ӧ�ź� 
	DataPin=1;
	//�жϴӻ��Ƿ��е͵�ƽ��Ӧ�ź� �粻��Ӧ����������Ӧ����������	  
	if(!DataPin)		  
	{
		Count=2;
		//�жϴӻ��Ƿ񷢳� 80us �ĵ͵�ƽ��Ӧ�ź��Ƿ����	 
		while((!DataPin)&&Count++);
		Count=2;
		//�жϴӻ��Ƿ񷢳� 80us �ĸߵ�ƽ���緢����������ݽ���״̬
		while((DataPin)&&Count++);
		//���ݽ���״̬		 
		ReadOneByte();
		RH_data_H_Rev=ByteData;
		ReadOneByte();
		RH_data_L_Rev=ByteData;
		ReadOneByte();
		T_data_H_Rev=ByteData;
		ReadOneByte();
		T_data_L_Rev=ByteData;
		ReadOneByte();
		Checkdata_Rev=ByteData;
		DataPin=1;
		//����У�� 
		
		Checkdata=(T_data_H_Rev+T_data_L_Rev+RH_data_H_Rev+RH_data_L_Rev);
		if(Checkdata==Checkdata_Rev)
		{
			RH_data_H=RH_data_H_Rev;
			T_data_H=T_data_H_Rev;
		}
	}

}