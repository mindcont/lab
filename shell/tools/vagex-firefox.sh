#!/bin/bash
## vagex-firefox  2017-06-27
## email: bond@mindcont.com
## centos 7

echo " ";
echo "        .__            .___                   __   ";
echo "  _____ |__| ____    __| _/____  ____   _____/  |_ ";
echo " /     \|  |/    \  / __ |/ ___\/  _ \ /    \   __|";
echo "|  Y Y  \  |   |  \/ /_/ \  \__(  <_> )   |  \  |  ";
echo "|__|_|  /__|___|  /\____ |\___  >____/|___|  /__|  ";
echo "      \/        \/      \/    \/           \/      ";
echo "";
echo " script name:      vagex-firefox.sh  ";
echo " info: install gnome desktop and configure vncserver on centos7 ";
echo " how to run:                                       ";
echo "           chmod +x vagex-firefox.sh                ";
echo "           ./vagex-firefox.sh 2>&1 | tee vagex-firefox.log";
echo "---------------------------------------------------";
echo "";
echo -e "\033[31m the script only Support CentOS_7 x86_64 \033[0m"
echo -e "\033[31m Please Seriously.Press ctrl+C to cancel \033[0m"
sleep 10
echo "";

cat << EOF
+-------------------------------------------------+
|    Make sure your system is CentOS 7 x86_64     |
|     you have 10 seconds to stop by ctrl+C       |
+-------------------------------------------------+
EOF
sleep 10

#512M的小鸡增加1G的Swap分区
add_swap_zone(){
  echo "===add swap zone start==="
  dd if=/dev/zero of=/var/swap bs=1024 count=1048576
  mkswap /var/swap
  chmod 0600 /var/swap
  swapon /var/swap
  echo '/var/swap   swap   swap   default 0 0' >> /etc/fstab
  echo -e "\033[32m===swap zone added successful===\033[0m"
}

Xfce_desktop_install(){
echo "===start install gnome gui===";
#安装 Xfce desktop
yum install epel-release -y
yum groupinstall "X Window System" -y
yum groupinstall "Xfce" -y
#yum -y groupinstall "Gnome" "Desktop" (optional,but more bigger)
#获取当前运行等级
systemctl get-default
#设启动运行等级5，即图形模式
systemctl set-default graphical.target
#验证是否启用成功
systemctl get-default
#启动图像工作方式
systemctl isolate graphical.target

#chkconfig NetworkManager off
#service NetworkManager stop
echo -e "\033[32m===gnome installed successful===\033[0m"
}
#配置vnc远程桌面 not tested!!!
vncserver_config(){

  yum -y install tigervnc-server
  vncserver
  #查询包括关键字xterm所在的行，并替换为#xterm
  sed -i '/xterm/s/^/#/' ~/.vnc/xstartup
  sed -i '/twm/s/^/#/' ~/.vnc/xstartup
  #在最后一行新增gnome-session &
  sed -i '$a gnome-session &' ~/.vnc/xstartup
  sed -i '$a VNCSERVERS="1:root"' /etc/sysconfig/vncservers
  sed -i '$a VNCSERVERARGS[1]="-geometry 1024x768 -alwaysshared -depth 24"' /etc/sysconfig/vncservers
  service vncserver restart
  chmod +x ~/.vnc/xstartup
  chkconfig vncserver on
  service vncserver restart
  #打开5901端口
  iptables -I INPUT -p tcp --dport 5901 -j ACCEPT
  service iptables reload

}

#优化firefox,清除日志 not tested!!!
firefox_config(){
  yum -y install firefox fonts-chinese

  #安装cron
  chkconfig crond on
  service crond start

  #火狐优化
  echo  "15 * * * * rm -rf /root/.vnc/*.log > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "16 * * * * killall -9 firefox > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "17 * * * * export DISPLAY=:1;firefox > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "30 * * * * rm -rf /root/.vnc/*.log > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "31 * * * * killall -9 firefox > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "32 * * * * export DISPLAY=:1;firefox > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "45 * * * * rm -rf /root/.vnc/*.log > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "46 * * * * killall -9 firefox > /dev/null 2>&1"  >> /var/spool/cron/root
  echo  "47 * * * * export DISPLAY=:1;firefox > /dev/null 2>&1"  >> /var/spool/cron/root
}

#done
done_ok(){
cat << EOF
+-------------------------------------------------+
|               Install is done                   |
|   it's recommond to restart this server !       |
|             Please Reboot system                |
+-------------------------------------------------+
EOF
sleep 2
}


main(){
  #add_swap_zone
  Xfce_desktop_install
  #vncserver_config
  #firefox_config
  done_ok
}

main 2>&1 | tee vagex-firefox.log
