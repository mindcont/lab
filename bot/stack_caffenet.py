#! /usr/bin/env python

from __future__ import print_function

try:
    import caffe
    from caffe import layers as L, params as P, to_proto
except ImportError as e:
    print('You must set PYTHONPATH to the latest caffe-python.')
    raise e

import os
import time
import argparse

from six import iteritems
import numpy as np


def conv_relu(bottom, ks, nout, stride=1, pad=0, group=1):
    conv = L.Convolution(bottom, kernel_size=ks, stride=stride,
                                num_output=nout, pad=pad, group=group)
    return conv, L.ReLU(conv, in_place=True)

def fc_relu(bottom, nout):
    fc = L.InnerProduct(bottom, num_output=nout)
    return fc, L.ReLU(fc, in_place=True)

def max_pool(bottom, ks, stride=1):
    return L.Pooling(bottom, pool=P.Pooling.MAX, kernel_size=ks, stride=stride)

def caffenet(batch_size=32, stack=1):
    n = caffe.NetSpec()
    n.data, n.label = L.MemoryData(batch_size=batch_size, channels=3 * stack, height=227, width=227, ntop=2)
    # the net itself
    n.conv1, n.relu1 = conv_relu(n.data, 11, 96, stride=4)
    n.pool1 = max_pool(n.relu1, 3, stride=2)
    n.norm1 = L.LRN(n.pool1, local_size=5, alpha=1e-4, beta=0.75)
    n.conv2, n.relu2 = conv_relu(n.norm1, 5, 256, pad=2, group=2)
    n.pool2 = max_pool(n.relu2, 3, stride=2)
    n.norm2 = L.LRN(n.pool2, local_size=5, alpha=1e-4, beta=0.75)
    n.conv3, n.relu3 = conv_relu(n.norm2, 3, 384, pad=1)
    n.conv4, n.relu4 = conv_relu(n.relu3, 3, 384, pad=1, group=2)
    n.conv5, n.relu5 = conv_relu(n.relu4, 3, 256, pad=1, group=2)
    n.pool5 = max_pool(n.relu5, 3, stride=2)
    n.fc6, n.relu6 = fc_relu(n.pool5, 4096)
    n.drop6 = L.Dropout(n.relu6, in_place=True)
    n.fc7, n.relu7 = fc_relu(n.drop6, 4096)
    n.drop7 = L.Dropout(n.relu7, in_place=True)
    n.fc8 = L.InnerProduct(n.drop7, num_output=1000)
    return n

def parse_args():
    description = "Generate a pretrained caffemodel from that of bvlc_reference_caffenet in modelzoo by repeating in channels direction by specified times. You have to set a correct PYTHONPATH to run Caffe code."
    parser = argparse.ArgumentParser(description)
    parser.add_argument('caffemodel', help='Pretrained caffemodel of bvlc_reference_caffenet')
    parser.add_argument(
        'stack', type=int,
        help='Number of repeat of channels. If 9 is given,'
             'the channels of first convolutional kernel ends up with 27 (=3 * 9)')
    parser.add_argument(
        '--out_prefix',
        help='Prefix of output prototxt and caffemodel, i.e. {prefix}.prototxt, {prefix}.caffemodel')
    parser.add_argument('--keep_orig_proto', action='store_true',
                        help='Not delete temporary prototxt of original caffenet')

    args = parser.parse_args()
    if args.out_prefix is None:
        args.out_prefix = 'caffenet{}'.format(args.stack)
    return args

# Main
args = parse_args()
proto_caffenet = 'tmp{}.caffenet.prototxt'.format(int(abs(time.time())))
proto_caffenet_dst = args.out_prefix + '.prototxt'
caffemodel_dst = args.out_prefix + '.caffemodel'

# Create prototxt's
with open(proto_caffenet, 'w') as fd:
    fd.write(str(caffenet().to_proto()))
with open(proto_caffenet_dst, 'w') as fd:
    fd.write(str(caffenet(stack=args.stack).to_proto()))

# Create Caffe Net instances
net = caffe.Net(proto_caffenet, args.caffemodel, caffe.TEST)
net_dst = caffe.Net(proto_caffenet_dst, caffe.TEST)
if not args.keep_orig_proto:
    os.remove(proto_caffenet)

# Copy params while repeating stacks of conv1
for k, v in iteritems(net.params):
    if k == 'conv1':
        w, b = net.params[k]
        ww, bb = net_dst.params[k]
        ww.data[...] = np.repeat(w.data, args.stack, 1)
        bb.data[...] = b.data[...]
        continue
    for p, pp in zip(net.params[k], net_dst.params[k]):
        pp.data[...] = p.data[...]

# Save
net_dst.save(caffemodel_dst)
