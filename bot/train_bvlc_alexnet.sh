#!/usr/bin/env sh
set -e

LOG=/home/pi/data/bot/models/bvlc_alexnet/log/bvlc_alexnet_train_`date +%Y-%m-%d-%H-%M-%S`.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/bvlc_alexnet/solver.prototxt \
    --weights=/home/pi/data/bot/models/bvlc_alexnet/caffe_alexnet_train_iter_2400.caffemodel 2>&1 | tee -a $LOG
