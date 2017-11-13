# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测集正确类别结果 置信度低的预测结果提取，进行二次学习，并生成适合caffe 的train.txt 文件
# 
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':

    # # 我们的预测结果
    results = open("/home/pi/data/bot/code/ADTC_DLU_results_6.txt",'r')
    comp_num = 0.5 # 此为固定数
    tmp =codecs.open("testset6_low.txt","w","utf-8-sig")

    for results_line in results.readlines():
        result_name = results_line.split()[0]
        #top1_label = results_line.split()[1]
        top1_acc = results_line.split()[2]
        # print result_name, top1_acc

        #寻找预测结果置信度 小于0.5 置信度的文件名
        if float(top1_acc) < comp_num:
            #print result_name

            # 寻找相应的文件后缀
            files = open("/home/pi/data/bot/code/tools/find_ext/Testset_6/Testset_6_file.txt",'r') #包含文件后缀
            for file_line in files.readlines():
                file_line_list = file_line.split()
                file_name = file_line_list[0].split(".")[0]
                ext_name = file_line_list[0].split(".")[1]
                # print file_name

                try:
                    if result_name == file_name:
                        #print answer_name+'.'+ext_name,top1_label
                        print >> tmp, result_name+'.'+ext_name
                except:
                    continue

        else:
            continue

    # 保存
    tmp.close()
    print 'done!'
