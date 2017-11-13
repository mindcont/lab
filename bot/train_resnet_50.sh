#!/usr/bin/env sh
set -e

LOG=/home/pi/data/bot/models/resnet_50/log/resnet_50_train_`date +%Y-%m-%d-%H-%M-%S`.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/resnet_50/resnet_50_solver.prototxt 2>&1 | tee -a $LOG
