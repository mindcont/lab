#!/usr/bin/env sh
set -e

LOG=/home/pi/data/bot/models/bvlc_reference_caffenet/log/bvlc_reference_caffenet_train_`date +%Y-%m-%d-%H-%M-%S`.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/bvlc_reference_caffenet/solver.prototxt \
    --weights=/home/pi/data/bot/models/bvlc_reference_caffenet/caffenet_train_iter_2500.caffemodel 2>&1 | tee -a $LOG
