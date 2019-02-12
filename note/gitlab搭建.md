### 安装流程
1.  卸载本机gitliab以及相关文件
```
sudo gitlab-ctl uninstall
sudo rpm -e gitlab-ce
find / -name gitlab|xargs rm -rf
```
2.  安装相关依赖
```
yum install curl openssh-server openssh-clients postfix cronie policycoreutils-python –y
```
3.  启动postfix，并设置为开机启动
```
systemctl start postfix
systemctl enable postfix
```
4.  防火墙设置
```
#此命令需在防火墙开启后使用
firewall-cmd --add-service=http --permanent
firewall-cmd --reload
```
5.  获取安装包
```
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7/gitlab-ce-8.6.7-ce.0.el7.x86_64.rpm
```
6.  安装rpm包
```
rpm -ivh gitlab-ce-8.6.7-ce.0.el7.x86_64.rpm
```
7.  修改配置文件gitlab.rb
```
vim /etc/gitlab/gitlab.rb
查找到
external_url 这个属性，将其改为ip+地址
```
8.  加载配置文件并启动
```
gitlab-ctl reconfigure
gitlab-ctl restart
```
9.  访问gitlab
10.  更改密码
11. gitlab创建一个项目
12. 导入客户端公钥
13. 查找客户端公钥
14. 将客户端公钥添加进gitlab
15. 提交本地项目到gitlab
16. 报错处理
17. 查看gitlab日志修正报错
