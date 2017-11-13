# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测集正确类别结果 提取相应图片后缀，合成适合caffe 的train.txt 文件
# 
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':
    #compute_score('BOT_Image_Testset_1.txt','ADTC_DLU_results.txt')

    answer = open("/home/pi/data/bot/code/tools/find_ext/Testset_6/BOT_Image_Testset_6.txt",'r')
    res =codecs.open("test_6_label.txt","w","utf-8-sig")

    for answer_line in answer.readlines():
        answer_name = answer_line.split()[0]
        top1_label = answer_line.split()[1]
        #answer_line = answer.readline()
        #print >>res, 'Testset2/'+answer_name+'.'+'jpg',top1_label
        #print answer_name,top1_label

        files = open("/home/pi/data/bot/code/tools/find_ext/Testset_6/Testset_6_file.txt",'r')
        for file_line in files.readlines():
            file_line_list = file_line.split()
            file_name = file_line_list[0].split(".")[0]
            ext_name = file_line_list[0].split(".")[1]
            #print file_name
            try:
                if answer_name == file_name:
                    #print answer_name+'.'+ext_name,top1_label
                    print >>res, 'Testset6/'+answer_name+'.'+ext_name,top1_label
            except:
                continue

    res.close()
    print 'done!'
