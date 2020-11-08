Since Glassfish does not run under JAVA 9 or 10, but requires JAVA 1.8, we add
the shell script 'java8', which is acessible via the shell variable PATH:
$cat java8
#! /bin/bash
JAVA_HOME=...Jdk1.8.0... # Directory with JDK 1.8 - adapt the path to your configuration
CLASSPATH=$JAVA_HOME/lib:$CLASSPATH
PATH=$JAVA_HOME/bin:$PATH
PATH=$PATH:~/MWInstallation
export PATH CLASSPATH JAVA_HOME

Only one glassfish instance (standalone or embedded) should be running in the
same domain. So, if you have started a standalone glassfish server, stop it
first:
$ (. java8;asadmin stop-domain)

To compile and install, execute the following command:
$ (. java8;mvn clean install)




(. java8;asadmin start-domain domain1; asadmin start-database; asadmin deploy vlibtour-tour-management-bean/target/vlibtour-tour-management-bean.jar)

(. java8;asadmin undeploy vlibtour-tour-management-bean ; asadmin stop-database; asadmin stop-domain domain1)

