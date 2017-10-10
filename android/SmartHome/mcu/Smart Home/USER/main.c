/************************************************************
*
*  �������ƣ� 						���ܼҾ�
*      ���ܣ�������׿APP ͸��Wifi �Ե�Ƭ��Ӳ�����м򵥿���
*    �����ˣ�	   mindcont
*  ����ʱ��:     2016-03-16			
*						
*
**************************************************************/

#include   "include.h"	

u8 ClearCounter=0;
u8 SetHour,SetMin;
u8 Clock_On;
u8 Status=0;

u16 ADC_Data;//ADC��ֵ
u16 DAC_Data;//DAC�趨ֵ
u16 Back_DAC_Data;//����DAC�趨ֵ

extern void alert(void);

void main()
{	
	HardWareInit();	// Ӳ����ʼ��
	Delay_500Us(10);
	DHT11ReadData();//Ԥ��ȡһ��
  RST_DS1302=0;
	Set_RTC();

	  while(1)   
		{

      Read_RTC();
			DisTime();

			if(Flag==1)//�������
			 {		
				 Flag=0;
				 EA=0;
				if( (Buf[0]==0x04) && (Buf[3]==0x01) )
				{
					 Status=1;
					 SetHour=Buf[1];
					 SetMin=Buf[2];
	
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 Buf[3]=0;
				}  
				
				else if( (Buf[0]==0x04) && (Buf[3]==0x00) )
				{
					 Clock_On=0;
					 Status=0;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 Buf[3]=0;
				}

				else if( Buf[0]==0x05) //�趨DACֵ ����ADC�ɼ�ֵ
				 {
				 	 Status=2;//״̬λ
					 EA=0;
					 DAC_Data=Buf[1]*255/33;
					 Back_DAC_Data = Buf[1];
				 	 DACconversion(PCF8591,0x40, DAC_Data);
					 EA=1;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
				 else if( (Buf[0]==0x02) && (Buf[1]==0xFF) ) //������ʪ�Ȳɼ�ֵ
				 {
				 	 Status=3;//״̬λ
					 for(ClearCounter=0;ClearCounter<10;ClearCounter++)
			  		 Buf[ClearCounter]=0;
					 SendCounter=0;
				 }
				 else if( (Buf[0]==0x01) && (Buf[1]==0x01))//APP����ָ��
				 {
					 Status=4;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
				 else if( (Buf[0]==0x01) && (Buf[1]==0x00))//APP�ص�ָ��
				 {
					 Status=0;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
				 else if( (Buf[0]==0x06) && (Buf[1]==0x01))//�̵�����ָ��
				 {
					 Status=5;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
				 else if( (Buf[0]==0x06) && (Buf[1]==0x00))//�̵�����ָ��
				 {
					 Status=0;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }

				 else if( (Buf[0]==0x03) && (Buf[1]==0x01))//�����תָ��
				 {
					 Status=5;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
  
				 else //����ָ��
				 {
					 for(ClearCounter=0;ClearCounter<10;ClearCounter++)
			  		 Buf[ClearCounter]=0;
					 SendCounter=0;
					 Status=0;
				 }
				 EA=1;
			}
	
	
			if(Status==1)
			{
				EA=0;
				Read_RTC();
				
				RD_Buf[2]=(RD_Buf[2]/16*10 )+(RD_Buf[2]%16 );
				RD_Buf[1]=(RD_Buf[1]/16*10 )+(RD_Buf[1]%16 ); 
				
				UART_SendH(SetHour);
				UART_SendH(SetMin);
				UART_SendH(RD_Buf[2]);
				UART_SendH(RD_Buf[1]);
			    EA=1;
				if( (SetMin==RD_Buf[1]) && (SetHour==RD_Buf[2]) )
				Clock_On=1;
					
			}
						
			else if(Status==2)
			 {
			 	 EA=0;
			 	 SendCMD(PCF8591,0x40);//���Ͷ�ȡADC��ֵ��������
		         ADC_Data=RecData(PCF8591); //ȡADCֵ 	
			   	 ADC_Data= ADC_Data* 33/255  ;//����Ϊ��ѹֵ��������ʮ��
				 EA=1;
				 UART_SendH(0x05);
				 UART_SendH(ADC_Data);
				 UART_SendH(Back_DAC_Data);
				 Delay_500Us(2000);	
			 }
			 else if(Status==3)
			 {
			 	   EA=0;
			 	   DHT11ReadData();//��ȡ��ʪ��ֵ
				   EA=1;
				   UART_SendH(0x02);
				   UART_SendH(T_data_H);
				   UART_SendH(RH_data_H);
				   T_data_H=0;//�������
				   RH_data_H=0;  
					Delay_500Us(4000);	 //��ȡģ���������� 2S 
			 }
			 else if(Status==4) {Led_Blink(1);	}
			 else if(Status==5) {JDQ_ON(1);}
			 else
			 {
			 	Led_Blink(0);P2=0xFF;
				Clock_On=0;JDQ_ON(0);
			 }
			 if(Clock_On==1) JDQ_ON(1);
			 else JDQ_ON(0);
			 
			 if(!smoke==1)
				{				
					Delay_500Us(4000);
					if(!smoke==1)
						{
								alert();//���ֱ���
						}
					}		
    }
}
