#!/usr/bin/env sh
# Create the imagenet lmdb inputs
# N.B. set the path to the imagenet train + val data dirs
set -e

SAVE_LMDB_LOCATION=/home/pi/data/bot/lmdb/test_error_123456
DATA_LABEL=/home/pi/data/bot/label/train_123456
TOOLS=/home/pi/caffe/build/tools

# 此处的 TRAIN_DATA_ROOT 代表你训练图片数据集的上一层目录。例如 我的animals 图片集放在/home/pi/data/bot/animals/train
TRAIN_DATA_ROOT=/home/pi/data/bot/animals/
VAL_DATA_ROOT=/home/pi/data/bot/animals/

# Set RESIZE=true to resize the images to 256x256. Leave as false if images have
# already been resized using another tool.
# 归一化图片大小 256*256
RESIZE=true
if $RESIZE; then
  RESIZE_HEIGHT=256
  RESIZE_WIDTH=256
else
  RESIZE_HEIGHT=0
  RESIZE_WIDTH=0
fi

if [ ! -d "$TRAIN_DATA_ROOT" ]; then
  echo "Error: TRAIN_DATA_ROOT is not a path to a directory: $TRAIN_DATA_ROOT"
  echo "Set the TRAIN_DATA_ROOT variable in create_imagenet.sh to the path" \
       "where the ImageNet training data is stored."
  exit 1
fi

if [ ! -d "$VAL_DATA_ROOT" ]; then
  echo "Error: VAL_DATA_ROOT is not a path to a directory: $VAL_DATA_ROOT"
  echo "Set the VAL_DATA_ROOT variable in create_imagenet.sh to the path" \
       "where the ImageNet validation data is stored."
  exit 1
fi

echo "Creating train lmdb..."

GLOG_logtostderr=1 $TOOLS/convert_imageset \
    --resize_height=$RESIZE_HEIGHT \
    --resize_width=$RESIZE_WIDTH \
    --shuffle \
    $TRAIN_DATA_ROOT \
    $DATA_LABEL/train_test123456.txt \
    $SAVE_LMDB_LOCATION/animals_train_lmdb

echo "Creating val lmdb..."

GLOG_logtostderr=1 $TOOLS/convert_imageset \
    --resize_height=$RESIZE_HEIGHT \
    --resize_width=$RESIZE_WIDTH \
    --shuffle \
    $VAL_DATA_ROOT \
    $DATA_LABEL/val_test123456.txt \
    $SAVE_LMDB_LOCATION/animals_val_lmdb

echo "Done."
