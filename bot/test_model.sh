#!/usr/bin/env sh
# 本脚本实现 对同一图片经过自己模型预测结果和 bvlc_reference_caffenet 预测结果的比对
# 
# 更多访问  http://blog.mindcont.com

set -e

/home/pi/caffe//build/examples/cpp_classification/classification.bin \
/home/pi/caffe/models/bvlc_reference_caffenet/deploy.prototxt \
/home/pi/caffe/models/bvlc_reference_caffenet/bvlc_reference_caffenet.caffemodel \
/home/pi/caffe/data/ilsvrc12/imagenet_mean.binaryproto \
/home/pi/caffe/data/ilsvrc12/synset_words.txt \
/home/pi/caffe/examples/images/cat.jpg

/home/pi/caffe//build/examples/cpp_classification/classification.bin \
/home/pi/data/bot/models/bvlc_reference_caffenet/deploy.prototxt \
/home/pi/data/bot/models/bvlc_reference_caffenet/caffenet_train_iter_6400.caffemodel \
/home/pi/data/bot/lmdb/animals_mean.binaryproto \
/home/pi/data/bot/label/labels.txt \
/home/pi/caffe/examples/images/cat.jpg
