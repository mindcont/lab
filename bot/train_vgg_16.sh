#!/usr/bin/env sh
set -e

LOG=/home/pi/data/bot/models/vgg_16/log/vgg_16_train_`date +%Y-%m-%d-%H-%M-%S`.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/vgg_16/vgg_16_solver.prototxt \
    --weights=/home/pi/data/bot/models/vgg_16/vgg_16_iter_2400.caffemodel 2>&1 | tee -a $LOG
