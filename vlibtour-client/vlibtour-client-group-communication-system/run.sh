#!/bin/bash

MODULE_VERSION=0.1-SNAPSHOT
RABBITMQ_CLIENT_VERSION=5.4.1
PRODUCER=eu.telecomsudparis.rabbitmq.tutorial.EmitLogTopic
CONSUMER=eu.telecomsudparis.rabbitmq.tutorial.ReceiveLogsTopic

# if [[ -f ./target/step5-${MODULE_VERSION}.jar ]]
# then
#     export JARS=./target/step5-${MODULE_VERSION}.jar
# else
#     echo Archive file ./target/step5-${MODULE_VERSION}.jar missing
#     echo Run maven install to generate it
# fi

# if [[ -f ${HOME}/.m2/repository/com/rabbitmq/amqp-client/${RABBITMQ_CLIENT_VERSION}/amqp-client-${RABBITMQ_CLIENT_VERSION}.jar ]]
# then
#     export JARS=${HOME}/.m2/repository/com/rabbitmq/amqp-client/${RABBITMQ_CLIENT_VERSION}/amqp-client-${RABBITMQ_CLIENT_VERSION}.jar:${JARS}
# else
#     echo Archive file ${HOME}/.m2/repository/com/rabbitmq/amqp-client/${RABBITMQ_CLIENT_VERSION}/amqp-client-${RABBITMQ_CLIENT_VERSION}.jar missing
#     echo Run maven install to install it on your local maven repository
# fi

# if [[ -f ${HOME}/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar ]]
# then
#     export JARS=${HOME}/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:${JARS}
# else
#     echo Archive file ${HOME}/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar missing
#     echo Run maven install to install it on your local maven repository
# fi

# if [[ -f ${HOME}/.m2/repository/org/slf4j/slf4j-simple/1.7.25/slf4j-simple-1.7.25.jar ]]
# then
#     export JARS=${HOME}/.m2/repository/org/slf4j/slf4j-simple/1.7.25/slf4j-simple-1.7.25.jar:${JARS}
# else
#     echo Archive file ${HOME}/.m2/repository/org/slf4j/slf4j-simple/1.7.25/slf4j-simple-1.7.25.jar missing
#     echo Run maven install to install it on your local maven repository
# fi

rabbitmqctl stop
rabbitmq-server -detached
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app
java -cp ${JARS} ${CONSUMER} '#' &
TOREMOVE1=$!
java -cp ${JARS} ${CONSUMER} '*.orange.*' &
TOREMOVE2=$!
java -cp ${JARS} ${CONSUMER} '*.*.rabbit' 'lazy.#' &
TOREMOVE3=$!
sleep 2
rabbitmqctl list_queues name durable auto_delete
rabbitmqctl list_exchanges
rabbitmqctl list_bindings
sleep 2
java -cp ${JARS} ${PRODUCER} quick.orange.rabbit "message one"
sleep 1
java -cp ${JARS} ${PRODUCER} lazy.orange.elephant "message two"
sleep 1
java -cp ${JARS} ${PRODUCER} quick.orange.fox "message three"
sleep 1
java -cp ${JARS} ${PRODUCER} lazy.brown.fox "message four"
sleep 1
java -cp ${JARS} ${PRODUCER} lazy.pink.rabbit "message five"
sleep 1
java -cp ${JARS} ${PRODUCER} quick.brown.fox "message six"
sleep 1
java -cp ${JARS} ${PRODUCER} orange "message seven"
sleep 1
java -cp ${JARS} ${PRODUCER} quick.orange.male.rabbit "message eight"
sleep 1
java -cp ${JARS} ${PRODUCER} lazy.orange.male.rabbit "message nine"
sleep 5
kill -15 $TOREMOVE1 $TOREMOVE2 $TOREMOVE3
sleep 2
echo END OF TEST
echo FORCE CLOSE OF CONSUMER
rabbitmqctl stop_app
rabbitmqctl stop
