/************************************************************
*
*  工程名称： 						智能家居
*      功能：借助安卓APP 透过Wifi 对单片机硬件进行简单控制
*    创建人：	   mindcont
*  创建时间:     2016-03-16			
*						
*
**************************************************************/

#include   "include.h"	

u8 ClearCounter=0;
u8 SetHour,SetMin;
u8 Clock_On;
u8 Status=0;

u16 ADC_Data;//ADC数值
u16 DAC_Data;//DAC设定值
u16 Back_DAC_Data;//返回DAC设定值

extern void alert(void);

void main()
{	
	HardWareInit();	// 硬件初始化
	Delay_500Us(10);
	DHT11ReadData();//预读取一次
  RST_DS1302=0;
	Set_RTC();

	  while(1)   
		{

      Read_RTC();
			DisTime();

			if(Flag==1)//接收完成
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

				else if( Buf[0]==0x05) //设定DAC值 返回ADC采集值
				 {
				 	 Status=2;//状态位
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
				 else if( (Buf[0]==0x02) && (Buf[1]==0xFF) ) //返回温湿度采集值
				 {
				 	 Status=3;//状态位
					 for(ClearCounter=0;ClearCounter<10;ClearCounter++)
			  		 Buf[ClearCounter]=0;
					 SendCounter=0;
				 }
				 else if( (Buf[0]==0x01) && (Buf[1]==0x01))//APP开灯指令
				 {
					 Status=4;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
				 else if( (Buf[0]==0x01) && (Buf[1]==0x00))//APP关灯指令
				 {
					 Status=0;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
				 else if( (Buf[0]==0x06) && (Buf[1]==0x01))//继电器开指令
				 {
					 Status=5;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
				 else if( (Buf[0]==0x06) && (Buf[1]==0x00))//继电器关指令
				 {
					 Status=0;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }

				 else if( (Buf[0]==0x03) && (Buf[1]==0x01))//电机正转指令
				 {
					 Status=5;
					 Buf[0]=0;
					 Buf[1]=0;
					 Buf[2]=0;
					 SendCounter=0;
				 }
		
  
				 else //错误指令
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
			 	 SendCMD(PCF8591,0x40);//发送读取ADC数值控制命令
		         ADC_Data=RecData(PCF8591); //取ADC值 	
			   	 ADC_Data= ADC_Data* 33/255  ;//换算为电压值，并扩大十倍
				 EA=1;
				 UART_SendH(0x05);
				 UART_SendH(ADC_Data);
				 UART_SendH(Back_DAC_Data);
				 Delay_500Us(2000);	
			 }
			 else if(Status==3)
			 {
			 	   EA=0;
			 	   DHT11ReadData();//读取温湿度值
				   EA=1;
				   UART_SendH(0x02);
				   UART_SendH(T_data_H);
				   UART_SendH(RH_data_H);
				   T_data_H=0;//清空数据
				   RH_data_H=0;  
					Delay_500Us(4000);	 //读取模块数据周期 2S 
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
								alert();//火灾报警
						}
					}		
    }
}
