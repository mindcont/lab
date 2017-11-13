# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测集正确类别结果 提取相应图片后缀，合成适合caffe 的train.txt 文件
# 
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':
    #compute_score('BOT_Image_Testset_1.txt','ADTC_DLU_results.txt')

    #caffenet = open("ADTC_DLU_results_caffenet_123_4_1.txt",'r')
    #res =codecs.open("ADTC_DLU_results_4.txt","w","utf-8-sig")
    caffenet = open("/home/pi/data/bot/code/results/20160924/ADTC_DLU_results_caffenet_6.txt",'r')
    res =codecs.open("ADTC_DLU_results_6.txt","w","utf-8")

    for caffenet_line in caffenet.readlines():
        caffenet_answer_name = caffenet_line.split()[0]
        caffenet_top1_label = caffenet_line.split()[1]
        caffenet_top1_prob = caffenet_line.split()[2]
        caffenet_top2_label = caffenet_line.split()[3]
        caffenet_top2_prob = caffenet_line.split()[4]
        #print caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,caffenet_top2_label,caffenet_top2_prob

        #vgg = open("./ADTC_DLU_results_vgg_123_4_1.txt",'r')
        vgg = open("/home/pi/data/bot/code/results/20160924/ADTC_DLU_results_vgg_6.txt",'r')
        for vgg_line in vgg.readlines():
            vgg_answer_name = vgg_line.split()[0]
            vgg_top1_label = vgg_line.split()[1]
            vgg_top1_prob = vgg_line.split()[2]
            vgg_top2_label = vgg_line.split()[3]
            vgg_top2_prob = vgg_line.split()[4]
            #print vgg_answer_name,vgg_top1_label,vgg_top1_prob,vgg_top2_label,vgg_top2_prob

            try:
                if caffenet_answer_name == vgg_answer_name:
                   if (caffenet_top1_label == vgg_top1_label)&(caffenet_top2_label == vgg_top2_label):
                        print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,caffenet_top2_label,caffenet_top2_prob

                   elif (caffenet_top1_label == vgg_top1_label)&(caffenet_top2_label != vgg_top2_label):
                        if caffenet_top2_prob > vgg_top2_prob:
                            print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,caffenet_top2_label,caffenet_top2_prob
                        else:
                            print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,vgg_top2_label,vgg_top2_prob

                   elif (caffenet_top1_label != vgg_top1_label)&(caffenet_top2_label == vgg_top2_label):
                         if caffenet_top1_prob > vgg_top1_prob:
                             print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,caffenet_top2_label,caffenet_top2_prob
                         else:
                             print >>res, caffenet_answer_name,vgg_top1_label,vgg_top1_prob,vgg_top2_label,vgg_top2_prob

                   elif (caffenet_top1_label != vgg_top1_label)&(caffenet_top2_label != vgg_top2_label):
                      if (caffenet_top1_prob >= vgg_top1_prob)&(caffenet_top2_prob >= vgg_top2_prob):
                          print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,caffenet_top2_label,caffenet_top2_prob

                      elif (caffenet_top1_prob >= vgg_top1_prob)&(caffenet_top2_prob < vgg_top2_prob):
                          print >>res, caffenet_answer_name,caffenet_top1_label,caffenet_top1_prob,vgg_top2_label,vgg_top2_prob

                      elif (caffenet_top1_prob < vgg_top1_prob)&(caffenet_top2_prob >= vgg_top2_prob):
                          print >>res,caffenet_answer_name,vgg_top1_label,vgg_top1_prob,caffenet_top2_label,caffenet_top2_prob

                      elif (caffenet_top1_prob < vgg_top1_prob)&(caffenet_top2_prob < vgg_top2_prob):
                          print >>res,caffenet_answer_name,vgg_top1_label,vgg_top1_prob,vgg_top2_label,vgg_top2_prob

            except:
                continue

    res.close()
    print 'done!'
