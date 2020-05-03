#!/bin/sh

# 安装环境必要的依赖
yum install java java-1.8.0-openjdk-devel git wget -y
# 切换到root目录
cd ~
# 下载tomcat
if [ -e apache-tomcat-9.0.34.tar.gz ]; then
  rm -rf apache-tomcat-9.0.34.tar.gz
fi
if [ -d /opt/apache-tomcat-9.0.34 ]; then
  rm -rf /opt/apache-tomcat-9.0.34
fi
wget https://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.34/bin/apache-tomcat-9.0.34.tar.gz
tar zxvf apache-tomcat-9.0.34.tar.gz -C /opt/
# 下载项目
if [ -d webCrawler ]; then
  rm -rf ~/webCrawler
fi
git clone https://github.com/junyalu/webCrawler.git
# 编译打包
cd ~/webCrawler
chmod +x ./mvnw
./mvnw clean package
# 清空webapps目录
rm -rf /opt/apache-tomcat-9.0.34/webapps/*
# 将war包拷贝到tomcat应用程序目录下
cp target/webCrawler.war /opt/apache-tomcat-9.0.34/webapps/
# 修改tomcat配置文件
(
cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>

<Server port="8005" shutdown="SHUTDOWN">
  <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
  <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />

  <GlobalNamingResources>

    <Resource name="UserDatabase" auth="Container"
              type="org.apache.catalina.UserDatabase"
              description="User database that can be updated and saved"
              factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
              pathname="conf/tomcat-users.xml" />
  </GlobalNamingResources>

  <Service name="Catalina">

    <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />

    <Engine name="Catalina" defaultHost="localhost">

      <Realm className="org.apache.catalina.realm.LockOutRealm">

        <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
               resourceName="UserDatabase"/>
      </Realm>

      <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">

                <Context path="/" docBase="webCrawler" debug="0" reloadable="true"/>

        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />

      </Host>
    </Engine>
  </Service>
</Server>
EOF
) > /opt/apache-tomcat-9.0.34/conf/server.xml
# 启动tomcat
/opt/apache-tomcat-9.0.34/bin/shutdown.sh
/opt/apache-tomcat-9.0.34/bin/startup.sh
