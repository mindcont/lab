## 关闭内置防火墙，采用面板防火墙(centos7采用firewall替代iptables)
```java
systemctl start firewalld 启动
firewall-cmd --version 查看版本
firewall-cmd --help帮助信息
firewall-cmd --get-active-zones查看区域信息

firewall-cmd --zone=public --permanent --list-all 查看现有规则

firewall-cmd --zone=public --permanent --add-service=http 永久打开某项服务
firewall-cmd --zone=public --permanent --remove-service=http 永久关闭某项服务

firewall-cmd --zone=public --permanent --add-port=8080/tcp 永久打开一个端口
firewall-cmd --zone=public --permanent --remove-port=8080/tcp 永久关闭一个端口

重新加载规则
firewall-cmd --reload
firewall-cmd –complete-reload 断开再连接

检查是否生效
firewall-cmd --zone=public --query-port=80/tcp

firewall-cmd --zone=public --permanent --list-all 查看现有规则

列出所有的开放端口
firewall-cmd --list-all

systemctl stop firewalld 停止
sytemctl disable firewalld 禁开机启动
systemctl start firewalld 启动
systemctl enable firewalld 运行开机启动

其它
firewall-cmd –panic-on 拒绝所有包
firewall-cmd –panic-off取消拒绝状态
firewall-cmd –query-panic查看是否拒绝
```

配置面板防火墙规则 由主机提供商提供如阿里云安全组、vultr firewall

这里我们禁止linux 内置的防火墙规则，改用主机上面板防火墙规则
允许icmp 协议(用于ping命令)，http https 和修改后ssh 端口(tcp)
