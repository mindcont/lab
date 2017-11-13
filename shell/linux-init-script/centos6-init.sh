#!/bin/bash
#
#by:wanglei
#date:20160823
#version:1.0

source /etc/profile
cat < < EOF
+++++++++++++++++++++++++++++++++++++++++++++++
+                                             +
+          Start system initialization        +
+                                             +
+++++++++++++++++++++++++++++++++++++++++++++++
EOF

###修改主机名###
echo "===开始修改主机名.===" |tee -a init.log
echo -e "\033[31m请选择是否修改主机名.\033[0m"
echo "  1: change hostname"
echo "  2: not change"
echo "  Any other character (except 1,2) exits"

read -p "Select Options (1 or 2): " option
if [[ "$option" == "1" ]]; then
    read -p "Please input a new hostname: " hostName
    sed -i 's/^HOSTNAME=.*/HOSTNAME='$hostName'/g' /etc/sysconfig/network
    hostname $hostName
    echo -e "new hostname is \033[31m$hostName\033[0m "
elif [[ "$option" == "2" ]]; then
    exit
else
    echo -e "\033[31mUnknown Options.\033[0m"
    exit
fi

echo "===主机名修改完成===" | tee -a init.log

###修改DNS服务器###
echo "===开始修改DNS服务器.===" |tee -a init.log
echo -e "nameserver 223.5.5.5\nnameserver 8.8.8.8" > /etc/resolv.conf
echo "===DNS服务器修改完成.===" | tee -a init.log

###安装wget工具###
echo "===开始安装wget工具.===" |tee -a init.log
yum install -y wget && echo -e "\033[31mwget安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mwget安装失败.\033[0m" |tee -a init.log
echo "===wget工具安装完成.===" |tee -a init.log

###安装YUM源###
echo "===开始安装epel源.===" |tee -a init.log
osVersion=$(cat /etc/redhat-release | awk -F. '{print $1}' | awk '{print $NF}')
if [[ "$osVersion" -lt 7 ]]; then
    wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-6.repo && echo -e "\033[31mepel源安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mepel源安装失败.\033[0m" |tee -a init.log
else
    wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo && echo -e "\033[31mepel源安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mepel源安装失败.\033[0m" |tee -a init.log
fi
yum makecache
echo "===epel源安装完成.===" | tee -a init.log

###升级系统yum update###
echo "===开始升级系统.===" |tee -a init.log
yum -y update && echo -e "\033[31m系统升级成功.\033[0m" |tee -a init.log || echo -e "\033[32m系统升级失败.\033[0m" |tee -a init.log
echo "===升级系统完成.===" |tee -a init.log

###安装常用工具软件###
echo "===开始安装常用软件.===" |tee -a init.log
yum install -y glances && echo -e "\033[31mglances安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mglances安装失败.\033[0m" |tee -a init.log
yum install -y vim && echo -e "\033[31mvim安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mvim安装失败.\033[0m" |tee -a init.log
yum install -y iftop && echo -e "\033[31miftop安装成功.\033[0m" |tee -a init.log || echo -e "\033[32miftop安装失败.\033[0m" |tee -a init.log
yum install -y ntp && echo -e "\033[31mntp安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mntp安装失败.\033[0m" |tee -a init.log
yum install -y openssh* && echo -e "\033[31mopenssh安装成功.\033[0m" |tee -a init.log || echo -e "\033[32mopenssh安装失败.\033[0m" |tee -a init.log
echo "===常用软件安装完成.===" |tee -a init.log

###关闭selinux###
echo "===开始关闭SELINUX.===" |tee -a init.log
sed -i "s/SELINUX=enforcing/#SELINUX=enforcing\nSELINUX=disabled/g" /etc/selinux/config
setenforce 0
echo "===SELINUX已关闭完成.===" |tee -a init.log

###修改SSH配置###
echo "===开始修改SSH配置.===" |tee -a init.log
cp /etc/ssh/sshd_config /etc/ssh/sshd_config.bak-$(date +%Y%m%d%H%M)
read -p "Please input a new portnumber: " portNumber
sed -i "s/#Port 22/#Port 22\nPort $portNumber/g" /etc/ssh/sshd_config
sed -i 's/#UseDNS yes/#UseDNS yes\nUseDNS no/g' /etc/ssh/sshd_config
echo -e "new portnumber is \033[31m$portNumber\033[0m ." | tee -a init.log
iptables -I INPUT -p tcp --dport $portNumber -j ACCEPT
service iptables save
echo "===SSH配置修改完成.===" |tee -a init.log

###修改root密码###
echo "===开始修改root用户密码.===" |tee -a init.log
read -p "Please input new password: " passWord
echo $passWord | passwd --stdin root
echo "===root密码修改完成.===" |tee -a init.log

###修改文件句柄限制###
echo "===开始修改文件句柄限制.===" |tee -a init.log
cat >> /etc/security/limits.conf < <EOF
* soft nproc 327675
* hard nproc 327675
* soft nofile 327675
* hard nofile 327675
EOF

echo "ulimit -SH 327675" >> /etc/rc.local
echo "===文件句柄限制修改完成.===" |tee -a init.log

###内核参数优化###
echo "===开始内核参数优化.===" |tee -a init.log
cat >> /etc/sysctl.conf < <EOF
fs.file-max = 327675
net.ipv4.tcp_fin_timeout = 30
net.ipv4.tcp_rmem = 4096 87380 8388608
net.ipv4.tcp_wmem = 4096 87380 8388608
net.core.rmem_max = 16777216
net.core.wmem_max = 16777216
net.ipv4.tcp_window_scaling = 1
net.ipv4.tcp_max_tw_buckets = 5000
net.ipv4.ip_local_port_range = 1024 65000
net.ipv4.tcp_tw_recycle = 1
net.core.netdev_max_backlog = 262144
net.ipv4.tcp_max_orphans = 262144
net.ipv4.tcp_max_syn_backlog = 4096
net.ipv4.tcp_timestamps = 0
net.ipv4.tcp_synack_retries = 2
net.ipv4.tcp_syn_retries = 2
net.ipv4.tcp_keepalive_time = 30
net.ipv4.tcp_mem = 94500000 915000000 927000000
EOF

sysctl -p
echo "===内核参数优化完成.===" |tee -a init.log

###同步系统时间###
echo "===开始同步系统时间.===" |tee -a init.log
rm -rf /etc/localtime
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ntpdate 2.cn.pool.ntp.org && echo -e "\033[31m同步系统时间成功.\033[0m" |tee -a init.log || echo -e "\033[32m同步系统时间失败.\033[0m" |tee -a init.log
hwclock --systohc
echo "===系统时间同步完成.===" |tee -a init.log

###重启SSH服务###
echo "===开始重启SSH服务.===" |tee -a init.log
/etc/init.d/sshd restart && echo -e "\033[31mSSH服务重启成功.\033[0m" |tee -a init.log || echo -e "\033[32mSSH服务重启失败.\033[0m" |tee -a init.log
echo "===SSH服务重启完成.===" |tee -a init.log

###输出初始化日志###
echo -e "===\033[31m系统初始化完成，日志如下:\033[0m \n==="
cat init.log
