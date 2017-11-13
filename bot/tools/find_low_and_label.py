# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测集正确类别结果 置信度低的预测结果提取，进行二次学习，并生成适合caffe 的train.txt 文件
# 
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':

    # 对于第一测试
    results = open("/home/pi/data/bot/code/results/20160917/ADTC_DLU_results_4.txt",'r') # 我们的预测结果
    comp_num = 0.5 # 此为固定数
    tmp =codecs.open("tmp.txt","w","utf-8-sig")
    res =codecs.open("/home/pi/data/bot/code/tools/low-acc/test_4_lowacc.txt","w","utf-8-sig")

    for results_line in results.readlines():
        result_name = results_line.split()[0]
        #top1_label = results_line.split()[1]
        top1_acc = results_line.split()[2]
        #print result_name, top1_acc

        #寻找预测结果置信度 小于0.5 置信度的文件名
        if float(top1_acc) < comp_num:
            #print answer_name

            # 寻找相应的文件后缀
            files = open("/home/pi/data/bot/code/tools/find_ext/Testset_4/Testset_4_file.txt",'r') #包含文件后缀
            for file_line in files.readlines():
                file_line_list = file_line.split()
                file_name = file_line_list[0].split(".")[0]
                ext_name = file_line_list[0].split(".")[1]
                #print file_name
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

    # 打开中间结果,二次寻找正确 类别 ID
    with_ext = open("tmp.txt",'r')

    for with_ext_line in with_ext.readlines():
        #print with_ext_line
        with_ext_file_name = with_ext_line.split(".")[0]
        with_ext_ext_name = with_ext_line.split(".")[1].strip('\n')
        # print with_ext_file_name,with_ext_ext_name
        #print with_ext_ext_name

        # 寻找正确类别ID
        answer = open("/home/pi/data/bot/code/results/20160917/BOT_Image_Testset_4.txt",'r') # 官方正确的结果
        for answer_line in answer.readlines():
            answer_name = answer_line.split()[0]
            top1_label = answer_line.split()[1]
            #print answer_name,top1_label
            try:
                if with_ext_file_name== answer_name:
                    #print answer_name+'.'+ext_name,top1_label
                    print >>res,'Testset4/'+with_ext_file_name+'.'+with_ext_ext_name,top1_label
            except:
                continue

    res.close()
    os.remove('tmp.txt')
    print 'done!'
    
    """
        file_a = open("a.txt")
        file_b = open("b.txt",'w')
        comp_num = 10 # 此为固定数
        num = file_a.readline()[:-1]
        while num:
            if float(num) > comp_num:
                file_b.write(num + ' -1\n')
            else:

    """
