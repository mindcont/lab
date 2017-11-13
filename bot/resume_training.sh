#!/usr/bin/env sh
# 本脚本实现 当训练中断后恢复训练直到达到slover.prototxt 设定的迭代次数
# 
# 更多访问  http://blog.mindcont.com

set -e
LOG=/home/pi/data/bot/models/bvlc_alexnet/log/bvlc_alexnet_train_`date +%Y-%m-%d-%H-%M-%S`.log

/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/bot/models/bvlc_alexnet/solver.prototxt \
    --snapshot=/home/pi/data/bot/models/bvlc_alexnet/caffe_alexnet_train_iter_2400.solverstate 2>&1 | tee -a $LOG
    $@
