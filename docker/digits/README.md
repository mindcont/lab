[![Docker Pulls](https://img.shields.io/docker/pulls/mindcont/digits.svg)](https://hub.docker.com/r/mindcont/digits/)
[![Docker Stars](https://img.shields.io/docker/stars/mindcont/digits.svg)](https://hub.docker.com/r/mindcont/digits/)

digits
===========
Ubuntu Core 14.04 + [CUDA 7.5](http://www.nvidia.com/object/cuda_home_new.html) + [cuDNN v5](https://developer.nvidia.com/cuDNN) + [Caffe](http://caffe.berkeleyvision.org/) (NVIDIA fork) + [DIGITS](https://developer.nvidia.com/digits).

Requirements
------------

- [NVIDIA Docker](https://github.com/NVIDIA/nvidia-docker) - see [requirements](https://github.com/NVIDIA/nvidia-docker/wiki/CUDA#requirements) for more details.

Usage
-----
Just type the following command
```
# Install nvidia-docker and nvidia-docker-plugin
wget -P /tmp https://github.com/NVIDIA/nvidia-docker/releases/download/v1.0.0-rc.3/nvidia-docker_1.0.0.rc.3-1_amd64.deb
sudo dpkg -i /tmp/nvidia-docker*.deb && rm /tmp/nvidia-docker*.deb

# use nvidia-docker to run a container
nvidia-docker run -d -p 5000:34448 --name digits mindcont/digits 
```

Then open your browser on localhost:5000, you we see the following picture

![](http://blog.mindcont.com/images/docker/docker_digits_web.png)

Demo
-----
please visit at http://digits.daoapp.io

Citation
--------
### BibTeX

```
@misc{dockerfiles,
  author = {NVIDIA CORPORATION },
  title = {NVIDIA/nvidia-docker},
  url = {https://github.com/NVIDIA/nvidia-docker},
  year = {2016}
}
```
