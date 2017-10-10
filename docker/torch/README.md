[![Docker Pulls](https://img.shields.io/docker/pulls/mindcont/torch.svg)](https://hub.docker.com/r/mindcont/torch/) [![Docker Stars](https://img.shields.io/docker/stars/mindcont/torch.svg)](https://hub.docker.com/r/mindcont/torch/)

## torch
Ubuntu Core 14.04 + [Torch7](http://torch.ch/) (including iTorch).

### Usage
Just type the following command

```java
docker run -it -p 8888:8888 --name torch mindcont/torch

jupyter notebook --ip="0.0.0.0" --no-browser

```

Then open your browser on `localhost:8888`,you we see the following picture

![](http://blog.mindcont.com/images/tools/docker/docker_torch.png)

### Citation
#### BibTeX

```
@misc{dockerfiles,
  author = {Kai Arulkumaran},
  title = {Kaixhin/dockerfiles},
  url = {https://github.com/Kaixhin/dockerfiles},
  year = {2015}
}
```
