% 本脚本实现 train_val.txt中按比例自动生存 train.txt /var.txt 文件
% 
% 更多访问  http://blog.mindcont.com
%
  clc;
  clear;
  close all;


  % 指定数据集label标签存放位置
  % 1
  labeldir='/home/pi/data/bot/label/train_123456/'
  % 行数即图片数目
  % Testset_1 8513
  % Testset_2 10052
  % Testset_3 10235
  % Testset_4 10475
  % Testset_5 9841
  % Testset_6 10592
  % test_error 16501
  % 2
  numoffile=16501;% Testset_6
  %先设置train占数据集的百分比，余下部分为val
  train_percent=0.9;%val_percent=1-train_percent

  %%
  %下面将trainval的顺序打乱
  file=cell(1,numoffile);
  % 3
  fin=fopen(fullfile(labeldir,'trainval_test123456.txt'),'r');
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
  % 4
  fout=fopen(fullfile(labeldir,'trainval_test123456.txt'),'w');
  for i=1:numoffile
      fprintf(fout,'%s\n',file{rep(i)});
  end
  fprintf('生成的trainval.txt已打乱顺序.\n');
  fclose(fout);

  %%
  %下面根据打乱顺序的trainval.txt生成train.txt和val.txt
  fprintf('开始生成train.txt和val.txt...\n');
  pause(1);
  % 5
  train_file=fopen(fullfile(labeldir,'train_test123456.txt'),'w');
  text_file=fopen(fullfile(labeldir,'val_test123456.txt'),'w');
  trainvalfile=fopen(fullfile(labeldir,'trainval_test123456.txt'),'r');

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
