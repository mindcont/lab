## 用自己的数据训练和测试“CaffeNet”

  
更多访问  http://blog.mindcont.com
## 解释


## 预处理
### 制作标签
首先在自己的数据集上构建 train.txt val.txt ,图像路径之后一个空格之后跟着类别的ID。值得注意的是ID 必须从0开始，要连续。否则会出错，loss 不下降。
如下是train.txt 的样例，val.txt类似。
```
/home/pi/data/Bot-2016/animals/dog/1c2a1f5661444eaeb77da8ccfdd919ec.jpg 3
/home/pi/data/Bot-2016/animals/deer/04bf24951dae4ce7923369433d3bbb61.jpg 2
/home/pi/data/Bot-2016/animals/weasel/7f6d59b568b743bcbe831554f56f17ea.jpg 10
/home/pi/data/Bot-2016/animals/squirrel/d7784d42ca0d40d98085807664984bb7.jpg 9
/home/pi/data/Bot-2016/animals/giraffe/3c39e04202cc4503b580c3f397e33b2c.jpg 5
/home/pi/data/Bot-2016/animals/wolf/0fd6c2f5d1894296adfb54eeab456757.jpg 11
/home/pi/data/Bot-2016/animals/weasel/304c0e827c2b490cb560e6032eb4e277.jpg 10
/home/pi/data/Bot-2016/animals/hyenas/1ad41c0d6da648b8b651f09057b3ced5.jpg 7
/home/pi/data/Bot-2016/animals/weasel/d07e12038f18434aa581e68649119e15.jpg 10
```
其中类别 如同 examples/finetune_flickr_style/style_names.txt
```
0 Detailed
1 Pastel
2 Melancholy
3 Noir
4 HDR
5 Vintage
6 Long Exposure
```
你可以通过下面的命令 手动的构建labels标签。
* 查找当前路径下的jpg文件 并将其写入 txt

```
find -name *.jpg | cut -d '/' -f2-3 > ../train_jpg.txt

find -name *.gif | cut -d '/' -f2-3 > ../train_gif.txt
```

* 然后通过python 在train_jpg.txt 每行末尾添加相应的类别ID的方式手动构建。

或者利用下面是简单的工具来自[小咸鱼_](http://blog.csdn.net/sinat_30071459/article/details/51613304)，指定图片集位置后自动的生成 train.txt 和 val.txt

```
clc;
clear;
close all;

% maindir 为你的数据集存放的位置。例如我的是/home/pi/data/Bot-2016/animals/各个类别
maindir='/home/pi/data/Bot-2016/animals/';
% 指定数据集label标签存放位置
labeldir='/home/pi/data/Bot-2016/label/'

wf = fopen(fullfile(labeldir,'trainval.txt'),'w');
lbf=fopen(fullfile(labeldir,'labels.txt'),'w');
%先设置train占数据集的百分比，余下部分为val
train_percent=0.9;%val_percent=1-train_percent

%%
%%下面生成顺序的trainval.txt和labels文件
subdir = dir(maindir);
ii=-1;
numoffile=0;
for i = 1:length(subdir)%第一层目录
  if ~strcmp(subdir(i).name ,'.') && ~strcmp(subdir(i).name,'..')
     ii=ii+1;
     label = subdir(i).name;
     fprintf(lbf,'%s\n',label);
     label=strcat(label,'/');
     subsubdir = dir(strcat(maindir,label));
    for j=1:length(subsubdir)
         if ~strcmp(subsubdir(j).name ,'.') && ~strcmp(subsubdir(j).name,'..')
           % 去掉该屏蔽代表生成全路径。例如/home/pi/data/Bot-2016/animals/hyenas/2a230a36a08c42a09699ff49619b8c3d.jpg 9
           %fprintf(wf,'%s%s%s %d\n',maindir,label,subsubdir(j).name,ii);
           % 该语句代表生成 animals/hyenas/2a230a36a08c42a09699ff49619b8c3d.jpg 9
           fprintf(wf,'%s%s%s %d\n','animals/',label,subsubdir(j).name,ii);
           numoffile=numoffile+1;
           fprintf('处理标签为%d的第%d张图片\n',ii,j-2);
         end
    end
  end
end
fclose(wf);
fclose(lbf);

%%
%下面将trainval的顺序打乱
file=cell(1,numoffile);
fin=fopen(fullfile(labeldir,'trainval.txt'),'r');
i=1;
while ~feof(fin)
    tline=fgetl(fin);
    file{i}=tline;
    i=i+1;
end
fclose(fin);

fprintf('\ntrainval.txt共%d行，开始打乱顺序....\n',numoffile);
pause(1);
rep=randperm(numoffile);
fout=fopen(fullfile(labeldir,'trainval.txt'),'w');
for i=1:numoffile
    fprintf(fout,'%s\n',file{rep(i)});
end
fprintf('生成的trainval.txt已打乱顺序.\n');
fclose(fout);

%%
%下面根据打乱顺序的trainval.txt生成train.txt和val.txt
fprintf('开始生成train.txt和val.txt...\n');
pause(1);
train_file=fopen(fullfile(labeldir,'train.txt'),'w');
text_file=fopen(fullfile(labeldir,'val.txt'),'w');
trainvalfile=fopen(fullfile(labeldir,'trainval.txt'),'r');

num_train=sort(randperm(numoffile,floor(numoffile*train_percent)));
num_test=setdiff(1:numoffile,num_train);
i=1;
while ~feof(trainvalfile)
    tline=fgetl(trainvalfile);
    if ismember(i,num_train)
        fprintf(train_file,'%s\n',tline);
    else
        fprintf(text_file,'%s\n',tline);
    end
    i=i+1;
end
fclose(train_file);
fclose(text_file);
fclose(trainvalfile);
fprintf('共有图片%d张!\n',numoffile);
fprintf('Done！\n');
```
生成的train.txt 形如
```
animals/squirrel/0e3b6c9a43f644f9989f2cf6969b02a8.jpg 9
animals/guineapig/0c3be3c3d1b841749634993562c5aac6.jpg 6
animals/chipmunk/41e7f07fc5044ffda6a0ae7dfb2ffc57.jpg 1
animals/reindeer/37dc77eaa14840f88df7dcab37207d2a.jpg 8
animals/weasel/5adf7e0e0c17442cbfc23fa7444c4d62.jpg 10
animals/squirrel/0c604a365a954e408eac80ff36d6f2a9.jpg 9
```
生成的var.txt 形如
```
animals/weasel/9e4d12a2c1874655ba182069a7bad7a3.jpg 10
animals/deer/4f3aa559020349a7ab235384e95bfddb.jpg 2
animals/hyenas/4b485770c98d406b9e23062601ab3924.jpg 7
animals/squirrel/0e77764f6caa40eda2eedb4936f94b80.jpg 9
animals/hyenas/3cfd6087e1ab4402830a8e3eed1fd402.jpg 7
animals/dog/2bea5a4fcaf34db986a43ef950302e77.jpg 3
```
生成的labels.txt
```
0 cat
1 chipmunk
2 deer
3 dog
4 fox
5 giraffe
```

### 生成lmdb或leveldb
```
#!/usr/bin/env sh
# Create the imagenet lmdb inputs
# N.B. set the path to the imagenet train + val data dirs
set -e

SAVE_LMDB_LOCATION=/home/pi/data/Bot-2016/lmdb #你自己数据集转换lmdb 存放位置
DATA_LABEL=/home/pi/data/Bot-2016/label #图片集label文件目录，用来搜索 train.txt val.txt 文件
TOOLS=/home/pi/caffe/build/tools # 编译好的caffe路径，用来调用caffe 提供的 convert_imageset工具实现制作lmdb 或 leveldb

# 此处的 TRAIN_DATA_ROOT 代表你训练图片数据集的上一层目录。例如 我的animals 图片集放在/home/pi/data/Bot-2016/animals
TRAIN_DATA_ROOT=/home/pi/data/Bot-2016/ # 训练数据集图片存放位置 ，由于我们没有分开 /animals/train/类别 ，所以这里采样同一目录
VAL_DATA_ROOT=/home/pi/data/Bot-2016/   # 验证数据集图片存放位置 ，由于我们没有分开 /animals/val/类别 ，所以这里采样同一目录

# Set RESIZE=true to resize the images to 256x256. Leave as false if images have
# already been resized using another tool.
RESIZE=true   #归一化图片大小 256*256
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
    $DATA_LABEL/train.txt \
    $SAVE_LMDB_LOCATION/animals_train_lmdb # 改存放 训练 lmdb的文件夹的名字

echo "Creating val lmdb..."

GLOG_logtostderr=1 $TOOLS/convert_imageset \
    --resize_height=$RESIZE_HEIGHT \
    --resize_width=$RESIZE_WIDTH \
    --shuffle \
    $VAL_DATA_ROOT \
    $DATA_LABEL/val.txt \
    $SAVE_LMDB_LOCATION/animals_val_lmdb #更改存放 验证 lmdb的文件夹的名字

echo "Done."
```

### 计算数据集均值
这里使用$Caffe_ROOT/build/tools/compute_image_mean 。修改$Caffe_ROOT/examples/imagenet/make_imagenet_mean.sh

```
#!/usr/bin/env sh
# Compute the mean image from the imagenet training lmdb
# N.B. this is available in data/ilsvrc12

# 改成你自己数据集lmdb存放位置
EXAMPLE=/home/pi/data/Bot-2016/lmdb
# 该成你希望数据均值文件存放位置 ，例如这里我将数据集 均值文件存放到我数据集的同级目录中
DATA=/home/pi/data/Bot-2016/lmdb
# 编译好的caffe路径
TOOLS=/home/pi/caffe/build/tools

$TOOLS/compute_image_mean $EXAMPLE/animals_train_lmdb \
  $DATA/animals_mean.binaryproto

echo "Done."
```
### 修改模型
拷贝Caffe_ROOT/models/bvlc_googlenet/  到 Bot-2016/

* 首先修改 网络模型配置 train_val.prototxt 的数据输入层
```
name: "GoogleNet"
layer {
  name: "data"
  type: "Data"
  top: "data"
  top: "label"
  include {
    phase: TRAIN
  }
  transform_param {
    mirror: true
    crop_size: 224
    #
    mean_file: "/home/pi/data/Bot-2016/lmdb/animals_mean.binaryproto" #均值文件  
    #mean_value: 104
    #mean_value: 117
    #mean_value: 123
  }
  data_param {
    #
    #source: "examples/imagenet/ilsvrc12_train_lmdb"
    source: "/home/pi/data/Bot-2016/lmdb/animals_train_lmdb" #训练集的lmdb  
    batch_size: 32 #根据GPU修改
    backend: LMDB
  }
}
layer {
  name: "data"
  type: "Data"
  top: "data"
  top: "label"
  include {
    phase: TEST
  }
  transform_param {
    mirror: false
    crop_size: 224
    mean_file: "/home/pi/data/Bot-2016/lmdb/animals_mean.binaryproto" #均值文件
    #mean_value: 104
    #mean_value: 117
    #mean_value: 123
  }
  data_param {
    #
    #source: "examples/imagenet/ilsvrc12_val_lmdb"
    source: "/home/pi/data/Bot-2016/lmdb/animals_val_lmdb" #验证集lmdb  
    batch_size: 50  #和solver中的test_iter相乘约等于验证集大小
    backend: LMDB
  }
}

```

* 修改网络输出
**由于Googlenet有三个输出，所以改三个地方**，其他网络一般只有一个输出，则改一个地方即可。
如果是微调，那么输出层的层名也要修改。（参数根据层名来初始化，由于输出改了，该层参数就不对应了，因此要改名）

* loss1/classifier 层
```
layer {
  name: "loss1/classifier"
  type: "InnerProduct"
  bottom: "loss1/fc"
  top: "loss1/classifier"
  param {
    lr_mult: 1
    decay_mult: 1
  }
  param {
    lr_mult: 2
    decay_mult: 0
  }
  inner_product_param {
    num_output: 1000  #改成你的数据集类别数  
    weight_filler {
      type: "xavier"
    }
    bias_filler {
      type: "constant"
      value: 0
    }
  }
}
```
* loss2/classifier 层
```
layer {
  name: "loss2/classifier"
  type: "InnerProduct"
  bottom: "loss2/fc"
  top: "loss2/classifier"
  param {
    lr_mult: 1
    decay_mult: 1
  }
  param {
    lr_mult: 2
    decay_mult: 0
  }
  inner_product_param {
    num_output: 1000 #改成你的数据集类别数  
    weight_filler {
      type: "xavier"
    }
    bias_filler {
      type: "constant"
      value: 0
    }
  }
}
```
* loss3/classifier 层
```
layer {
  name: "loss3/classifier"
  type: "InnerProduct"
  bottom: "pool5/7x7_s1"
  top: "loss3/classifier"
  param {
    lr_mult: 1
    decay_mult: 1
  }
  param {
    lr_mult: 2
    decay_mult: 0
  }
  inner_product_param {
    num_output: 1000 #改成你的数据集类别数
    weight_filler {
      type: "xavier"
    }
    bias_filler {
      type: "constant"
      value: 0
    }
  }
}
```

打开deploy.prototxt，修改：
```
    layer {  
      name: "loss3/classifier"  
      type: "InnerProduct"  
      bottom: "pool5/7x7_s1"  
      top: "loss3/classifier"  
      param {  
        lr_mult: 1  
        decay_mult: 1  
      }  
      param {  
        lr_mult: 2  
        decay_mult: 0  
      }  
      inner_product_param {  
        num_output: 1000 #改成你的数据集类别数  
        weight_filler {  
          type: "xavier"  
        }  
        bias_filler {  
          type: "constant"  
          value: 0  
        }  
      }  
    }  
```
接着，打开solver，修改
```
test_iter: 1000 是指测试的批次,我们就 10 张照片,设置 10 就可以了。
test_interval: 1000 是指每 1000 次迭代测试一次,我们改成 500 次测试一次。
base_lr: 0.01 是基础学习率,因为数据量小,0.01 就会下降太快了,因此改成 0.001
lr_policy: "step"学习率变化
gamma: 0.1 学习率变化的比率
stepsize: 100000 每 100000 次迭代减少学习率
display: 20 每 20 层显示一次
max_iter: 450000 最大迭代次数,
momentum: 0.9 学习的参数,不用变
weight_decay: 0.0005 学习的参数,不用变
snapshot: 10000 每迭代 10000 次显示状态,这里改为 2000 次
solver_mode: GPU 末尾加一行,代表用 GPU 进行
```

```
net: "examples/imagenet/bvlc_googlenet/train_val.prototxt" #路径不要错  
test_iter: 1000 #前面已说明该值  
test_interval: 4000 #迭代多少次测试一次  
test_initialization: false  
display: 40  
average_loss: 40  
base_lr: 0.01  
lr_policy: "step"  
stepsize: 320000 #迭代多少次改变一次学习率  
gamma: 0.96  
max_iter: 10000000 #迭代次数  
momentum: 0.9  
weight_decay: 0.0002  
snapshot: 40000  
snapshot_prefix: "examples/imagenet/bvlc_googlenet" #生成的caffemodel保存在imagenet下，形如bvlc_googlenet_iter_***.caffemodel  
solver_mode: GPU
```

## 训练

```
#!/usr/bin/env sh
set -e
LOG=/home/pi/data/Bot-2016/models/bvlc_googlenet/log/bvlc_googlenet_train.log
/home/pi/caffe/build/tools/caffe train \
    --solver=/home/pi/data/Bot-2016/models/bvlc_googlenet/solver.prototxt 2>&1 | tee -a $LOG
```
### 绘图
* 模型图

bvlc_reference_caffenet
```
python /home/pi/caffe/python/draw_net.py /home/pi/data/Bot-2016/models/bvlc_reference_caffenet/train_val.prototxt /home/pi/data/Bot-2016/models/bvlc_reference_caffenet/train_val.png

python /home/pi/caffe/python/draw_net.py \
/home/pi/data/Bot-2016/models/bvlc_reference_caffenet/deploy.prototxt \
/home/pi/data/Bot-2016/models/bvlc_reference_caffenet/deploy.png
```
bvlc_googlenet
```
python /home/pi/caffe/python/draw_net.py \
/home/pi/data/bot/models/bvlc_googlenet/train_val.prototxt \
/home/pi/data/bot/models/bvlc_googlenet/train_val.png

python /home/pi/caffe/python/draw_net.py \
/home/pi/data/bot/models/bvlc_googlenet/deploy.prototxt \
/home/pi/data/bot/models/bvlc_googlenet/deploy.png
```

* 准确率-迭代次数
bvlc_googlenet
```
python /home/pi/caffe/tools/extra/plot_training_log.py 0 /home/pi/data/Bot-2016/models/bvlc_googlenet/log/bvlc_googlenet_train_0.png /home/pi/data/Bot-2016/models/bvlc_googlenet/log/bvlc_googlenet_train.log
```
bvlc_reference_caffenet
```
python /home/pi/caffe/tools/extra/plot_training_log.py 0 /home/pi/data/Bot-2016/models/bvlc_reference_caffenet/log/bvlc_googlenet_train_0.png /home/pi/data/Bot-2016/models/bvlc_reference_caffenet/log/bvlc_reference_caffenet_train.log
```
## 预测

1.抽取关键帧的命令：
```
sudo add-apt-repository ppa:mc3man/trusty-media
sudo apt-get update
sudo apt-get install ffmpeg
cd tmp
mkdir dog
ffmpeg -i dog.mp4 -vf select='eq(pict_type\,I)',setpts='N/(25*TB)' ./dog/%09d.jpg
```
批量重命名
```
for var in `ls`;do mv -f "$var" `echo "deer_$var"`; done
```
删除文件名前三个字符
```
for var in `ls`; do mv -f "$var" `echo "$var" |sed 's/^...//'`; done
```
把文件名的前三个字母变为 cat
```
for var in `ls`; do mv -f "$var" `echo "$var" |sed 's/^.../cat/'`; done
```
利用python 进行批量测试
```
import numpy as np
import matplotlib.pyplot as plt
import os

caffe_root = '/home/pi/caffe/'
import sys
sys.path.insert(0,caffe_root+'python')

import caffe

MODEL_FILE = caffe_root+'models/bvlc_reference_caffenet/deploy.prototxt'
PRETRAINED = caffe_root+'models/bvlc_reference_caffenet/bvlc_reference_caffenet.caffemodel'

#cpu模式
caffe.set_mode_cpu()
#定义使用的神经网络模型
net = caffe.Classifier(MODEL_FILE, PRETRAINED,
               mean=np.load(caffe_root + 'python/caffe/imagenet/ilsvrc_2012_mean.npy').mean(1).mean(1),
               channel_swap=(2,1,0),
               raw_scale=255,
               image_dims=(256, 256))
imagenet_labels_filename = caffe_root + 'data/ilsvrc12/synset_words.txt'
labels = np.loadtxt(imagenet_labels_filename, str, delimiter='\t')

#对目标路径中的图像，遍历并分类
for root,dirs,files in os.walk("/home/pi/data/Bot-2016/matconvnet/val/"):
    for file in files:
        #加载要分类的图片
        IMAGE_FILE = os.path.join(root,file).decode('gbk').encode('utf-8');
        input_image = caffe.io.load_image(IMAGE_FILE)

        #预测图片类别
        prediction = net.predict([input_image])
        print 'predicted class:',prediction[0].argmax()

        # 输出概率最大的前5个预测结果
        top_k = net.blobs['prob'].data[0].flatten().argsort()[-1:-6:-1]
        print labels[top_k]
```


## 参考
* [薛开宇-caffe学习笔记3](ftp://mindcont.com/Book/caffe学习笔记.pdf)
* [基于caffe的图像分类(1)——制作train.txt和val.txt文件](http://blog.csdn.net/sinat_30071459/article/details/51613304)
* [基于caffe的图像分类(2)——制作lmdb和计算均值 ](http://blog.csdn.net/sinat_30071459/article/details/51679159)
* [基于caffe的图像分类(3)——修改网络并训练模型](http://blog.csdn.net/sinat_30071459/article/details/51679995)
* [Fine-tuning CaffeNet for Style Recognition on “Flickr Style” Data](http://caffe.berkeleyvision.org/gathered/examples/finetune_flickr_style.html)
