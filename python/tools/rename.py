#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import hashlib


def md5(fname):
    hash_md5 = hashlib.md5()
    with open(fname, "rb") as f:
        for chunk in iter(lambda: f.read(4096), b""):
            hash_md5.update(chunk)
    return hash_md5.hexdigest()

def main():

    name_number = 0
    with open('rename-log.txt','a+') as log:

        #os.listdir('.')遍历文件夹内的每个文件名，并返回一个包含文件名的list
        for file in os.listdir('./images'):
            if file[-2: ] == 'py':
                continue   #过滤掉改名的.py文件

            name = file.replace(' ', '')   #去掉空格

            # 旧的命名格式 4位累加的数字 + 文件原始后缀
            name_number+=1 #累加
            
    	    # 选择名字中需要保留的部分
            #new_name = '%0*d' % (4, name_number)+name[-4:]+".jpg"

            # 新的命名格式 md5sum 文件值 + 文件原始后缀
            #print hashlib.md5(open('./images/'+file, 'rb').read()).hexdigest()

            #选择名字中需要保留的部分
            new_name = md5('./images/'+file) +name[-4:] 
	    print name_number,"\t" + name,new_name
	   
	    #保留变更记录
            print >>log, name_number, "\t" + name + "\t" +new_name

            # 执行变更
            os.rename('./images/'+file, './images/'+new_name)

if __name__ == "__main__":
    main()
