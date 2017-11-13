#!/usr/bin/env sh
# Compute the mean image from the imagenet training lmdb
# N.B. this is available in data/ilsvrc12

# 改成你自己数据集lmdb存放位置
EXAMPLE=/home/pi/data/bot/lmdb/test_error_123456

# 该成你希望数据均值文件存放位置 ，例如这里我将数据集 均值文件存放到我数据集的同级目录中
DATA=/home/pi/data/bot/lmdb/test_error_123456

# 编译好的caffe路径
TOOLS=/home/pi/caffe/build/tools

$TOOLS/compute_image_mean $EXAMPLE/animals_train_lmdb \
  $DATA/animals_mean.binaryproto

echo "Done."
