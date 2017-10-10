# java-tutorial

本目录中的程序源码均来自于 北京尚学堂 马士兵老师教学视频中的例子程序， 用以记录了我的学习Java过程，文中我对源码进行了详细注释，旨在帮助和我 一样Java初级学习者！

## 程序目录

目录名           | 算法名
------------- | ------------
TankWar       | 简单坦克大战游戏
Snake         | 贪吃蛇游戏
Chat          | C/S模式简单的聊天程序
DataMining_EM | 最大期望算法Demo

- ### 坦克大战(Tank War)
实现简单的坦克大战效果，拥有敌方机器人坦克和我方坦克相互攻击的效果！

  * 功能
  - [x] 1、 简单八个方向的移动，每当松开CTRL 键进行攻击敌方坦克
  - [x] 2、 操作者坦克拥有一个超级技能 （每当按下 A 键可以向八 个方向同时开火）
  - [x] 3、 操作者坦克可以吃掉沿固定路线移动的血块，瞬间满血但只能使用一次
  - [x] 4、操作者坦克死亡后按F2键可以复活，敌方坦克死亡后自动生成

  * 如何体验

  ```java

  1、首先请确定你安装了Java 运行环境，你可以通过Win + R同时按住，在对话框中输入cmd ,并回车
     在弹出的DOS界面下输入 java -version 来查看本机java版本号
  2、在TankWar/jar 下有TankWar.jar 文件，你可以下载后双击运行
  3、如果双击后，系统弹出请选择运行程序，你需要 以管理员身份运行regedit(注册表管理器) 目的是告知系统运行.jar 文件的程序
  4、在注册表编辑器中，找到“HKEY_CLASSES_ROOT\Applications\javaw.exe\shell\open\command”，在其中文件打开命令中加入参数“-jar”（无引号），
     修改后的数值类似：“"C:\Program Files\Java\jre7\bin\javaw.exe" -jar "%1"”（只需要添加-jar参数，无需修改其他信息），
     保存并退出注册表编辑器。
  ```

- ### 贪吃蛇(Snake)

  实现简单的贪吃蛇吃点随机方块后自加1，其中贪吃蛇拐弯方面存在bugs

- ### C/S模式简单的聊天程序(Chat)

  实现简单的客户端/服务器聊天程序

- ### 最大期望算法Demo

  通过从文件中读取坐标点，递归寻找簇点并进行图形化展示，达到阐明最大期望算法思想的目的