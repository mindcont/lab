%Convert image in gif format to jpg
%https://github.com/lllcho/bot
clc;
clear;
close all;

% 指定包含gif路径，程序会自动搜索并转为jpg图像
%CONVERT_PATH = '/home/pi/data/Bot-2016/matconvnet/train/';
CONVERT_PATH = '/home/pi/data/bot/animals/';

imdir=dir(CONVERT_PATH);
imdir=imdir(cellfun(@(x) x, {imdir.isdir}));
for i=3:length(imdir) % 当前目录下第一层循环
    disp(imdir(i).name)
    imgnames=dir([CONVERT_PATH, imdir(i).name '/*.gif']);%查找指定gif 后缀的文件
    for j=1:length(imgnames)% 二级目录下循环
        imgpath=[CONVERT_PATH, imdir(i).name '/' imgnames(j).name];%构建gif 路径
        [img,map]=imread(imgpath,'gif');%读入路径下 gif文件
        img = ind2rgb(img,map);%转换为rgb通道图像
        imwrite(img,[imgpath(1:end-3) 'jpg']);%写入图像并更改为.jpg文件
        delete(imgpath);
    end
end
fprintf('Convert complete！\n');
