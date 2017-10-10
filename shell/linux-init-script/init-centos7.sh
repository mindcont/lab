#!/bin/bash
## centos7-init-server  2017-06-27
## email: bond@mindcont.com
## platform: centos 7 x86_64

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo " ";
echo "        .__            .___                   __   ";
echo "  _____ |__| ____    __| _/____  ____   _____/  |_ ";
echo " /     \|  |/    \  / __ |/ ___\/  _ \ /    \   __|";
echo "|  Y Y  \  |   |  \/ /_/ \  \__(  <_> )   |  \  |  ";
echo "|__|_|  /__|___|  /\____ |\___  >____/|___|  /__|  ";
echo "      \/        \/      \/    \/           \/      ";
echo "";
echo " script name:      init-centos7.sh  ";
echo " info: install gnome desktop and configure vncserver on centos7 ";
echo " how to run:                                       ";
echo "           chmod +x init-centos7.sh                ";
echo "           ./init-centos7.sh 2>&1 | tee server_init.log";
echo "---------------------------------------------------";
echo "";
echo -e "\033[31m the script only Support CentOS_7 x86_64 \033[0m"
echo -e "\033[31m system initialization script, Please Seriously. press ctrl+C to cancel \033[0m"
sleep 10
echo "";

# 检查是否为64位系统，这个脚本只支持64位脚本
platform=`uname -i`
if [ $platform != "x86_64" ];then
    echo "this script is only for 64bit Operating System !"
    exit 1
fi

#判断是不是root用户
if [[ "$(whoami)" != "root" ]]; then

    echo "please run this script as root ."
    exit 1
fi

cat << EOF
+-------------------------------------------------+
|       your system is CentOS 7 x86_64            |
|              start optimizing                   |
+-------------------------------------------------+
EOF
sleep 1

#更新和升级
yum_update(){
  echo "===update and upgrade vps==="
  yum update -y
  yum install -y unzip wget vim nmap net-tools git ntpdate
  echo -e "\033[32m===update and upgrade done!===\033[0m"
}

#新建普通用户，赋予sudo权限
add_sudo_user(){
  echo "===add a new user pi and join in sudo group==="
  adduser pi
  passwd pi
  ##在CentOS上，wheel组里的成员有执行sudo的权限
  usermod -aG wheel pi
  echo -e "\033[32m===add user done!===\033[0m"
}

#ssh 仅允许证书登录
sshd_config(){
  echo "=== ssh config start==="
  #修改/etc/sshd_config
  if [ ! -f "/etc/ssh/sshd_config.default" ]; then
      cp /etc/ssh/sshd_config /etc/ssh/sshd_config.default
  fi

  cat >/etc/ssh/sshd_config<<EOF
# 端口
Port 28166
AddressFamily inet
ListenAddress 0.0.0.0

# 协议
Protocol 2
HostKey /etc/ssh/ssh_host_rsa_key
HostKey /etc/ssh/ssh_host_dsa_key
HostKey /etc/ssh/ssh_host_ecdsa_key
HostKey /etc/ssh/ssh_host_ed25519_key

# 日志和连接限制
LogLevel INFO
LoginGraceTime 120
SyslogFacility AUTHPRIV
MaxAuthTries 3
UsePrivilegeSeparation no
KeyRegenerationInterval 3600
ServerKeyBits 1024
StrictModes yes
# 客户端60超时断开
ClientAliveInterval 30
ClientAliveCountMax 3

# 打印信息 /etc/motd
X11Forwarding no
PrintMotd yes
PrintLastLog yes
TCPKeepAlive yes
AcceptEnv LANG LC_*
Subsystem sftp /usr/lib/openssh/sftp-server
UsePAM yes
UseDNS no

# 证书登录认证
RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeysFile	%h/.ssh/authorized_keys
IgnoreRhosts yes
RhostsRSAAuthentication no
HostbasedAuthentication no
PermitEmptyPasswords no
ChallengeResponseAuthentication no

# 禁止密码和root远程登录
PermitRootLogin no
PasswordAuthentication no
EOF

  #登录信息通告
  sudo cat > /etc/motd <<EOF
         .__            .___                   __
   _____ |__| ____    __| _/____  ____   _____/  |_
  /     \|  |/    \  / __ |/ ___\/  _ \ /    \   __|
 |  Y Y  \  |   |  \/ /_/ \  \__(  <_> )   |  \  |
 |__|_|  /__|___|  /\____ |\___  >____/|___|  /__|
       \/        \/      \/    \/           \/

           welcome to login MindCont Inc

EOF

  #ssh公钥证书配置
  mkdir /home/pi/.ssh
  #追加到authorized_keys 文件后
  # wget xxx.com/bond_rsa.pub or sftp upload
  # cat  bond_rsa.pub >> ~/.ssh/authorized_keys
  cat >> /home/pi.ssh/authorized_keys <<EOF
  ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDHqN/Yh40UnzQi05GIKyRFAf85IO/JxM84VDBdWuBic3ijKgkFXyVWnlgSdZXty9RzBg9EofN0tVWkc22bUx/xVTaIEtIhbTMSKtNiHjDhkfKT904Wm/CMP1R68sPmGtQn/4qrezO08R6Zn+HwG1qaZHyP4Y2UOX3NR+DtVlQD9VLMWYGJ8WDv6CkSu9bysf7nVERYgsRk9n9DKwSR1t1FKm8UCGugbOW7miqkZ0MIYsULvQa13D0/ZUeu9UnZm+M4BCTwnMRCMjIQmqZm7bH1KVXk6kA49gTea5IwVZUsIIB6C90XjzYckEWIFHx6akYaypaTWlmbjemUCwyphjT7olhdo5Jg9jPn+BDbcmiAktl9PYlP5dxiIma4gVKf2lXQEwUL3WUSgZFfhTRS4bUB0ROFedVW7k3dVuBBL0q+J79BZ5sB7UfLfDAYo3vA2G0+Q4zcwMPC4+9lGLfkPWyudg/l1Tkp3kgsdqOeyov3FPjcvIMN6gcOT2W1GaIRx9f5yBQtxy4c2EeKurPAXqD+sl/U+TzrNldA4S9ORvauUPwDP998vpse/5KB+SdkMI3FE0aQCs3jBADw2Uv1PxodgUC7uTzxkmcHkRnz75XY1I4k8KuChYujDLsKClvs02L3L9Q0duS89X2PZBkr8vsuaxLxHvO7C1ln1Gf8wxinUQ== bond@MindCont Inc
EOF
  chown -R pi:pi /home/pi/.ssh
  chmod -R 0700  /home/pi/.ssh

  #内置防火墙允许28166端口
  systemctl status firewalld > /dev/null 2>&1
  if [ $? -eq 0 ]; then
      firewall-cmd --permanent --zone=public --add-port=28166/tcp
      firewall-cmd --permanent --zone=public --add-port=28166/udp
      firewall-cmd --reload
  else
      echo "Firewalld looks like not running, try to start..."
      systemctl start firewalld
      if [ $? -eq 0 ]; then
          firewall-cmd --permanent --zone=public --add-port=28166/tcp
          firewall-cmd --permanent --zone=public --add-port=28166/udp
          firewall-cmd --reload
      else
          echo "WARNING: Try to start firewalld failed. please enable port ${shadowsocksport} manually if necessary."
      fi
  fi
  firewall-cmd --zone=public --permanent --list-all

  sudo service sshd restart
  echo -e "\033[32m===ssh config successful===\033[0m"
}

#disabled firewalld,usr vultr panel config
disable_firewalld(){
echo "===disable firewalld begin==="
# 本地化时间设置
timedatectl set-timezone Asia/Shanghai
/usr/bin/systemctl start ntpd.service
/usr/bin/systemctl enable ntpd.service
# 关闭linux内置防火墙，采用面板防火墙
/usr/bin/systemctl stop  firewalld.service
/usr/bin/systemctl disable  firewalld.service
#/usr/bin/systemctl enable NetworkManager-wait-online.service
#/usr/bin/systemctl start NetworkManager-wait-online.service
/usr/bin/systemctl stop postfix.service
/usr/bin/systemctl disable postfix.service
echo -e "\033[32m===disable firewalld successful===\033[0m"
}

# Vultr Block storage(optional) only available NewJersey on Vultr.com
vultr_block_storage(){
   echo "===mount block_storage on Vultr==="
   # Create partitions:
   parted -s /dev/vdb mklabel gpt
   parted -s /dev/vdb unit mib mkpart primary 0% 100%
   # Create filesystem:
   mkfs.ext4 /dev/vdb1
   # Mount block storage:
   mkdir /mnt/blockstorage
   echo >> /etc/fstab
   echo /dev/vdb1               /mnt/blockstorage       ext4    defaults,noatime 0 0 >> /etc/fstab
   mount /mnt/blockstorage
   df -h
   echo -e "\033[32m===mount blockstorage /dev/blockstorage successful===\033[0m"
}

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

# 优化内核参数 tune kernel parametres
sysctl_config(){
echo "===tune kernel parametres begin==="

if [ ! -f "/etc/sysctl.conf.default" ]; then
    cp /etc/sysctl.conf /etc/sysctl.conf.default
fi

#add
cat > /etc/sysctl.conf << EOF
net.ipv6.conf.all.disable_ipv6 = 1
net.ipv6.conf.default.disable_ipv6 = 1
net.ipv4.tcp_syn_retries = 1
net.ipv4.tcp_synack_retries = 1
net.ipv4.tcp_keepalive_time = 600
net.ipv4.tcp_keepalive_probes = 3
net.ipv4.tcp_keepalive_intvl =15
net.ipv4.tcp_retries1 = 3
net.ipv4.tcp_retries2 = 5
net.ipv4.tcp_fin_timeout = 10
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_window_scaling = 1
net.ipv4.tcp_max_tw_buckets = 60000
net.ipv4.tcp_max_orphans = 32768
net.ipv4.tcp_max_syn_backlog = 16384
net.ipv4.tcp_mem = 94500000 915000000 927000000
net.ipv4.tcp_wmem = 4096 16384 13107200
net.ipv4.tcp_rmem = 4096 87380 17476000
net.ipv4.ip_local_port_range = 1024 65000
net.ipv4.route.gc_timeout = 100
net.core.somaxconn = 32768
net.core.netdev_max_backlog = 32768
vm.overcommit_memory = 1
vm.swappiness = 1
fs.file-max = 1024000
EOF

#reload sysctl
/sbin/sysctl -p
sleep 1
echo -e "\033[32m===tune kernel parametres successful===\033[0m"
}

# enable_bbr,not tested
enable_bbr(){
echo "===install BBR begin==="
uname -r
#Upgrade the kernel using the ELRepo RPM repository
sudo rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
sudo rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm
sudo yum --enablerepo=elrepo-kernel install kernel-ml -y

echo -e "\033[32m===BBR enabled successful===\033[0m"
echo "for more visit https://www.vultr.com/docs/how-to-deploy-google-bbr-on-centos-7";
}

#change server hostname
change_hostname(){
  echo "change hostname";


  echo -e "\033[32m===change hostname and timezone successful===\033[0m"
  echo "for more visit https://www.vultr.com/docs/how-to-change-your-hostname-on-centos";
}

#关闭SELINUX
disable_selinux(){
echo "===disable selinux===";
sed -i 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/selinux/config
setenforce 0
echo -e "\033[32m===selinux disabled successful===\033[0m"
}

#设置历史命令记录格式 history
history_config(){
echo "===format history command log===";
export HISTFILESIZE=10000000
export HISTSIZE=1000000
export PROMPT_COMMAND="history -a"
export HISTTIMEFORMAT="%Y-%m-%d_%H:%M:%S "
##export HISTTIMEFORMAT="{\"TIME\":\"%F %T\",\"HOSTNAME\":\"\$HOSTNAME\",\"LI\":\"\$(who -u am i 2>/dev/null| awk '{print \$NF}'|sed -e 's/[()]//g')\",\"LU\":\"\$(who am i|awk '{print \$1}')\",\"NU\":\"\${USER}\",\"CMD\":\""
cat >>/etc/bashrc<<EOF
alias vi='vim'
HISTDIR='/var/log/command.log'
if [ ! -f \$HISTDIR ];then
touch \$HISTDIR
chmod 666 \$HISTDIR
fi
export HISTTIMEFORMAT="{\"TIME\":\"%F %T\",\"IP\":\"\$(ip a | grep -E '192.168|172' | head -1 | awk '{print \$2}' | cut -d/ -f1)\",\"LI\":\"\$(who -u am i 2>/dev/null| awk '{print \$NF}'|sed -e 's/[()]//g')\",\"LU\":\"\$(who am i|awk '{print \$1}')\",\"NU\":\"\${USER}\",\"CMD\":\""
export PROMPT_COMMAND='history 1|tail -1|sed "s/^[ ]\+[0-9]\+  //"|sed "s/$/\"}/">> /var/log/command.log'
EOF
source /etc/bashrc
echo -e "\033[32m===history command log format successful===\033[0m"
}

#安装shadowsocks
shadowsocks_install(){
  echo "===install shadowsocksR==="
  wget --no-check-certificate https://raw.githubusercontent.com/mindcont/shell/master/shadowsocks/shadowsocksR.sh
  chmod +x shadowsocksR.sh
  ./shadowsocksR.sh 2>&1 | tee shadowsocksR.log
  echo -e "\033[32m===shadowsocksR installed successful===\033[0m"
}

#done
done_ok(){
cat << EOF
+-------------------------------------------------+
|               optimizer is done                 |
|   it's recommond to restart this server !       |
|             Please Reboot system                |
+-------------------------------------------------+
EOF
sleep 1
}


main(){
  ##硬件(hardware)
  #vultr_block_storage
  add_swap_zone

  ##系统(os)
  yum_update
  add_sudo_user
  sshd_config
  disable_firewalld
  sysctl_config

  #history_config
  #change_hostname
  #enable_bbr

  ##软件(application)
  shadowsocks_install

  done_ok
}

main
