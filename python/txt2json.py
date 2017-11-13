# -*- coding: utf-8 -*-
# 本脚本实现 对 Bot-2016大赛 预测集正确类别结果 置信度低的预测结果提取，进行二次学习，并生成适合caffe 的train.txt 文件
# 張正軒 (bond@mindcont.com)
# 更多访问  http://blog.mindcont.com

import os
import sys
import codecs


if __name__ == '__main__':

    # # 我们的预测结果
    file = open("sinaimg.txt",'r')

    json =codecs.open("sinaimg.json","w","utf-8-sig")

    for line in file.readlines():
        url = line.split()[0]
        number = line.split()[1]
        #print url
        print >>json,url

    # 保存
    json.close()
    print 'done!'
