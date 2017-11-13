#!/usr/bin/env sh
set -e

LOG=/home/pi/data/bot/models/bvlc_googlenet/log/bvlc_googlenet_train_`date +%Y-%m-%d-%H-%M-%S`.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/bvlc_googlenet/quick_solver.prototxt \
    --weights=/home/pi/data/bot/models/bvlc_googlenet/bvlc_googlenet.caffemodel 2>&1 | tee -a $LOG
