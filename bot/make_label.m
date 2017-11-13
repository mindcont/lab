% 本脚本实现 从分类图片文件夹 中自动构建 训练/验证 数据集的 train.txt /var.txt 文件，便于后续生成 各自对应lmdb文件
% 
% 更多访问  http://blog.mindcont.com
%
  clc;
  clear;
  close all;

  % maindir 为你的数据集存放的位置。例如我的是/home/pi/data/Bot-2016/animals/各个类别
  maindir='/home/pi/data/bot/animals/train/';
  % 指定数据集label标签存放位置
  labeldir='/home/pi/data/bot/label/'

  wf = fopen(fullfile(labeldir,'trainval.txt'),'w');
  lbf=fopen(fullfile(labeldir,'labels.txt'),'w');
  %先设置train占数据集的百分比，余下部分为val
  train_percent=0.8;%val_percent=1-train_percent

  %%
  %%下面生成顺序的trainval.txt和labels文件
  subdir = dir(maindir);
  % 按大赛要求重新构建类别号
  subdir_rename={'.','..','guinea_pig','squirrel','sikadeer','fox','dog','wolf','cat','chipmunk','giraffe','reindeer','hyena','weasel'};

  % 对subdir name 字段重排序
  for i = 1:length(subdir)
       subdir(i).name = subdir_rename(i);
  end

  ii=-1;
  numoffile=0;
  for i = 1:length(subdir)%第一层目录
    if ~strcmp(subdir(i).name ,'.') && ~strcmp(subdir(i).name,'..')
       ii=ii+1;
       %label = subdir(i).name;
       label = cell2mat(subdir(i).name);%cell 转 char/string
       fprintf(lbf,'%s\n',label);
       label=strcat(label,'/');
       subsubdir = dir(strcat(maindir,label));
      for j=1:length(subsubdir)
           if ~strcmp(subsubdir(j).name ,'.') && ~strcmp(subsubdir(j).name,'..')
             % 去掉该屏蔽代表生成全路径。例如/home/pi/data/Bot-2016/animals/hyenas/2a230a36a08c42a09699ff49619b8c3d.jpg 9
             %fprintf(wf,'%s%s%s %d\n',maindir,label,subsubdir(j).name,ii);
             % 该语句代表生成 animals/hyenas/2a230a36a08c42a09699ff49619b8c3d.jpg 9
             fprintf(wf,'%s%s%s %d\n','train/',label,subsubdir(j).name,ii);
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
