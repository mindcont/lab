#!/usr/bin/python
# -*- coding:utf-8 -*-

'''
@description B站视频信息爬虫, 视频信息保存到csv文件中
@autor chenjiandongx
@url https://github.com/chenjiandongx/bili-spider
     http://python.jobbole.com/86532/

     视频的链接为 https://www.bilibili.com/video/av + aid
'''

import threading
from collections import namedtuple
from concurrent import futures
import time
import csv
import codecs
import requests


header = ["aid", "view", "danmaku", "reply", "favorite", "coin", "share","now_rank","his_rank","like","no_reprint","copyright"]
Video = namedtuple('Video', header)
headers = {
    'X-Requested-With': 'XMLHttpRequest',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) '
                  'Chrome/56.0.2924.87 Safari/537.36'
}
total = 1
result = []
lock = threading.Lock()


def run(url):
    """ 启动爬虫
    """
    global total
    req = requests.get(url, headers=headers, timeout=6).json()
    time.sleep(0.5)     # 延迟，避免太快 ip 被封
    try:
        data = req['data']
        video = Video(
            data['aid'],        # 视频编号
            data['view'],       # 播放量
            data['danmaku'],    # 弹幕数
            data['reply'],      # 评论数
            data['favorite'],   # 收藏数
            data['coin'],       # 硬币数
            data['share'],      # 分享数
            data['now_rank'],
            data['his_rank'],
            data['like'],
            data['no_reprint'],
            data['copyright'],
        )
        with lock:
            result.append(video)
            print total, video
            # print(total)
            total += 1
    except:
        pass


def save():
    """ 将数据保存至本地
    """
    with codecs.open("bilibili-video.csv", "w", encoding="utf-8") as f:
        f_csv = csv.writer(f)
        f_csv.writerow(header)
        f_csv.writerows(result)


if __name__ == "__main__":
    urls = ["http://api.bilibili.com/archive_stat/stat?aid={}".format(i)
            for i in range(1000000)]
    with futures.ThreadPoolExecutor(32) as executor:
        executor.map(run, urls)
    save()
