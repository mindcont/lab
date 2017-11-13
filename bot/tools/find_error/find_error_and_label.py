# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测结果错误的条目同正确答案得到比较后，输出caffe 的train.txt 文件进行二次训练
# 
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':

    # 对于提交结果
    ## 1
    results = open("/home/pi/data/bot/code/results/20160924/ADTC_DLU_results_6.txt",'r') # 我们的预测结果
    tmp =codecs.open("tmp.txt","w","utf-8")
    ## 2
    res =codecs.open("test_6_error.txt","w","utf-8")

    for results_line in results.readlines():
        result_name = results_line.split()[0]
        result_top1_label = results_line.split()[1]
        #print result_name, result_top1_label

        # 正确类别ID
        ## 3
        answer = open("/home/pi/data/bot/code/results/20160924/BOT_Image_Testset_6.txt",'r') # 官方正确的结果
        for answer_line in answer.readlines():
            answer_name = answer_line.split()[0]
            answer_top1_label = answer_line.split()[1]
            #print answer_name,answer_top1_label

            try:
                if ((result_name== answer_name)&(result_top1_label!=answer_top1_label)):
                    print>>tmp, answer_name,answer_top1_label

            except:
                continue

    # 保存
    tmp.close()


    # 打开中间结果,二次寻找正确 类别 ID
    error = open("tmp.txt",'r')

    for error_line in error.readlines():
        #print with_ext_line
        test_error_name = error_line.split()[0]
        test_error_label = error_line.split()[1]
        #print test_error_name,test_error_label

        # 寻找相应的文件后缀
        ## 4
        files = open("/home/pi/data/bot/code/tools/find_ext/Testset_6/Testset_6_file.txt",'r') #包含文件后缀
        for file_line in files.readlines():
            file_line_list = file_line.split()
            file_name = file_line_list[0].split(".")[0]
            ext_name = file_line_list[0].split(".")[1]
            #print file_name,ext_name

            try:
                if test_error_name == file_name:
                    #print test_error_name+'.'+ext_name,test_error_label
                    ## 5
                    print >>res,'Testset6/'+test_error_name+'.'+ext_name,test_error_label
            except:
                continue

    res.close()
    os.remove('tmp.txt')
    print 'done!'
