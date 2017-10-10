# Start with Ubuntu base image
FROM ubuntu:14.04
MAINTAINER MINDCONT,INC <bond@mindcont.com>


# Install NVIDIA_CUDA_7_5_Runtime
LABEL com.nvidia.volumes.needed="nvidia_driver"

ENV NVIDIA_GPGKEY_SUM bd841d59a27a406e513db7d405550894188a4c1cd96bf8aa4f82f1b39e0b5c1c
ENV NVIDIA_GPGKEY_FPR 889bee522da690103c4b085ed88c3d385c37d3be

RUN apt-key adv --fetch-keys http://developer.download.nvidia.com/compute/cuda/repos/GPGKEY && \
    apt-key adv --export --no-emit-version -a $NVIDIA_GPGKEY_FPR | tail -n +2 > cudasign.pub && \
    echo "$NVIDIA_GPGKEY_SUM  cudasign.pub" | sha256sum -c --strict - && rm cudasign.pub && \
    echo "deb http://developer.download.nvidia.com/compute/cuda/repos/ubuntu1404/x86_64 /" > /etc/apt/sources.list.d/cuda.list

ENV CUDA_VERSION 7.5
LABEL com.nvidia.cuda.version="7.5"

ENV CUDA_PKG_VERSION 7-5=7.5-18
RUN apt-get update && apt-get install -y --no-install-recommends --force-yes \
        cuda-nvrtc-$CUDA_PKG_VERSION \
        cuda-cusolver-$CUDA_PKG_VERSION \
        cuda-cublas-$CUDA_PKG_VERSION \
        cuda-cufft-$CUDA_PKG_VERSION \
        cuda-curand-$CUDA_PKG_VERSION \
        cuda-cusparse-$CUDA_PKG_VERSION \
        cuda-npp-$CUDA_PKG_VERSION \
        cuda-cudart-$CUDA_PKG_VERSION && \
    ln -s cuda-$CUDA_VERSION /usr/local/cuda && \
    rm -rf /var/lib/apt/lists/*

RUN echo "/usr/local/cuda/lib" >> /etc/ld.so.conf.d/cuda.conf && \
    echo "/usr/local/cuda/lib64" >> /etc/ld.so.conf.d/cuda.conf && \
    ldconfig

RUN echo "/usr/local/nvidia/lib" >> /etc/ld.so.conf.d/nvidia.conf && \
    echo "/usr/local/nvidia/lib64" >> /etc/ld.so.conf.d/nvidia.conf

ENV PATH /usr/local/nvidia/bin:/usr/local/cuda/bin:${PATH}
ENV LD_LIBRARY_PATH /usr/local/nvidia/lib:/usr/local/nvidia/lib64:${LD_LIBRARY_PATH}


# Install NVIDIA_cuDNN_5
RUN echo "deb http://developer.download.nvidia.com/compute/machine-learning/repos/ubuntu1404/x86_64 /" > /etc/apt/sources.list.d/nvidia-ml.list

ENV CUDNN_VERSION 5
LABEL com.nvidia.cudnn.version="5"

RUN apt-get update && apt-get install -y --no-install-recommends --force-yes \
            libcudnn5=5.0.5-1+cuda7.5 && \
    rm -rf /var/lib/apt/lists/*
    
    
# Install NVIDIA_Caffe_0_14
ENV CAFFE_VERSION 0.14
LABEL com.nvidia.caffe.version="0.14"

ENV CAFFE_PKG_VERSION 0.14.5-2+cuda7.5
RUN apt-get update && apt-get install -y --no-install-recommends --force-yes \
            caffe-nv=$CAFFE_PKG_VERSION \
            caffe-nv-tools=$CAFFE_PKG_VERSION \
            python-caffe-nv=$CAFFE_PKG_VERSION && \
    rm -rf /var/lib/apt/lists/*
   
   
# Install NVIDIA_Digits_4_0
# workaround: gcc and libhdf5-dev are dependencies that are currently missing from the torch package
RUN apt-get update && apt-get install -y --no-install-recommends --force-yes \
        gcc \
        git \
        graphviz \
        gunicorn \
        libhdf5-dev \
        nginx \
        python-caffe-nv \
        python-flask \
        python-flaskext.socketio \
        python-flaskext.wtf \
        python-gevent \
        python-lmdb \
        python-pil \
        python-pip \
        python-pydot \
        python-requests \
        python-six \
        python-skimage \
        python-wtforms \
        torch7-nv=0.9.98-1+cuda7.5 && \
    rm -rf /var/lib/apt/lists/*

ENV DIGITS_VERSION 4.0
LABEL com.nvidia.digits.version="4.0"

WORKDIR /opt/digits

ENV CLONE_TAG=v4.0.0
RUN git clone -b ${CLONE_TAG} --depth 1 https://github.com/NVIDIA/DIGITS.git . && \
    for req in $(cat requirements.txt); do pip install $req; done

VOLUME /data
VOLUME /jobs

COPY digits.cfg digits/digits.cfg

EXPOSE 34448
ENTRYPOINT ["/opt/digits/digits-server"]