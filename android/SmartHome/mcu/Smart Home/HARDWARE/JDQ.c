/***********************************************************************
 *@filename:JDQ.C
 * @parameter �̵������ƺ���
 * @author :mindcont
 * @date  :2015.5.124
 * @version 1.0 
 * @return  NUll
************************************************************************/

#include "include.h"



/*************************************
** ��������: void JDQ_ON(u8 JDQ_Status)
** ��������: �̵��� ����
** ��ڲ���: JDQ_Status
** ���ڲ���: ��
**************************************/

void JDQ_ON(u8 JDQ_Status)
{
	if(JDQ_Status==1)
	{
		GPIO_JDQ=0;
		Delay_500Us(1000);	 //��ʱ500MS
		GPIO_JDQ=1;
		Delay_500Us(1000);	 //��ʱ500MS
	}	
	else
	GPIO_JDQ = 0;
}