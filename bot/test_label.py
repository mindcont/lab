# -*- coding: utf-8 -*-
# 本脚本实现 对自己训练模型进行预测图片分类结果并记录到 results.csv 文件中
# 
# 更多访问  http://blog.mindcont.com

import os
import codecs
import numpy as np
import matplotlib.pyplot as plt

caffe_root = '/home/pi/caffe/'
import sys
sys.path.insert(0,caffe_root+'python')

import caffe

# bvlc_reference_caffenet 网络
MODEL_FILE = '/home/pi/data/bot/models/bvlc_reference_caffenet/deploy.prototxt'
PRETRAINED = '/home/pi/data/bot/models/bvlc_reference_caffenet/log/20160927_test6/caffenet_train_iter_2500.caffemodel'
# bvlc_googlenet 网络
#MODEL_FILE = '/home/pi/data/bot/models/bvlc_googlenet/deploy.prototxt'
#PRETRAINED = '/home/pi/data/bot/models/bvlc_googlenet/bvlc_googlenet_quick_iter_2400.caffemodel'
# vgg_16 网络
#MODEL_FILE = '/home/pi/data/bot/models/vgg_16/vgg_16_deploy.prototxt'
#PRETRAINED = '/home/pi/data/bot/models/vgg_16/vgg_16_iter_2400.caffemodel'
# alexnet 网络
#MODEL_FILE = '/home/pi/data/bot/models/bvlc_alexnet/deploy.prototxt'
#PRETRAINED = '/home/pi/data/bot/models/bvlc_alexnet/caffe_alexnet_train_iter_2400.caffemodel'
# squeezenet 网络
#MODEL_FILE = '/home/pi/data/bot/models/squeezenet/deploy.prototxt'
#PRETRAINED = '/home/pi/data/bot/models/squeezenet/train_iter_2000.caffemodel'

#caffe.set_mode_cpu()
caffe.set_mode_gpu();
caffe.set_device(0);
net = caffe.Classifier(MODEL_FILE, PRETRAINED,
               mean=np.load(caffe_root + 'python/caffe/imagenet/ilsvrc_2012_mean.npy').mean(1).mean(1),
               channel_swap=(2,1,0),
               raw_scale=255,
               image_dims=(256, 256))
#imagenet_labels_filename = caffe_root + 'data/ilsvrc12/synset_words.txt'
imagenet_labels_filename = '/home/pi/data/bot/label/labels.txt'
labels = np.loadtxt(imagenet_labels_filename, str, delimiter='\t')

# 创建预测记录文本
#res =codecs.open("bvlc_googlenet_results.csv","w","utf-8-sig")
res =codecs.open("ADTC_DLU_results_train23456_7.txt","w","utf-8")


for root,dirs,files in os.walk("/home/pi/data/bot/animals/Testset7"):
    for file in files:
        IMAGE_FILE = os.path.join(root,file).decode('gbk').encode('utf-8');
        input_image = caffe.io.load_image(IMAGE_FILE)

        prediction = net.predict([input_image])
        #print 'predicted class:',prediction[0].argmax()

        prob= net.blobs['prob'].data[0].flatten() #取出最后一层（Softmax）属于某个类别的概率值，并打印
        #print prob

        #top_k = net.blobs['prob'].data[0].flatten().argsort()[-1:-6:-1]
        #top_k = prob.argsort()[-1:-3:-1]
        top_inds = prob.argsort()[::-1][:2]

        #print  >> res,file.split(".")[0],top_inds[0],labels[top_inds][0],prob[top_inds][0],top_inds[1],labels[top_inds][1],prob[top_inds][1]
        print >> res,file.split(".")[0]+'\t'+"%d\t%0.6f\t%d\t%0.6f" % (top_inds[0],prob[top_inds][0],top_inds[1],prob[top_inds][1])
 
        #print 'predicted class label:',labels[top_k[0]],labels[top_k[1]]
        print 'Prediction for----'+file+' ----'
        print 'probabilities and labels:',zip(labels[top_inds], prob[top_inds]),'\n';

        #for (x,y) in zip(labels[top_inds], prob[top_inds]:
        #	print  >> res ,'--',x,y

        #print labels[top_inds]
        #print prob[top_inds]

        #res.write(prob[top_inds])

res.close()
print 'done!'

"""
        #image = caffe.io.load_image('image.jpg')
        image = caffe.io.load_image(IMAGE_FILE)
        net.blobs['data'].data[...] = transformer.preprocess('data', image)

        # perform classification
        net.forward()

        # obtain the output probabilities
        output_prob = net.blobs['prob'].data[0]

        # sort top five predictions from softmax output
        top_inds = output_prob.argsort()[::-1][:5]

        plt.imshow(image)

        print 'probabilities and labels:'
        zip(output_prob[top_inds], labels[top_inds])
"""
