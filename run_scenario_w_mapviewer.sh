#!/bin/bash

# killall java # to think about the possibility of processes from old executions!

# test Java 8
if ! $(java -version 2>&1 | head -1 | grep '\"1\.8' > /dev/null); then
    echo "Not a JAVA 8 version: '$(java -version 2>&1 | head -1)'"
    exit 1
fi

# create directory to save process numbers (in order to kill them at the end)
if [ -d ~/.vlibtour ]; then
    rm -f ~/.vlibtour/*
else
    mkdir -p ~/.vlibtour
fi

./Scripts/utils.sh

echo "\n\nSTEP 1: deploy Tour Management module\n\n"
# clean up the domain and the database, start the domain and the database, and deploy the tour management bean
if [ -n "$GLASSFISH_HOME" ]; then
    asadmin undeploy vlibtour-tour-management-bean
    asadmin stop-database
    asadmin stop-domain domain1
    asadmin start-domain domain1
    asadmin start-database
    asadmin deploy vlibtour-tour-management/vlibtour-tour-management-bean/target/vlibtour-tour-management-bean.jar
fi

echo "\nHit return to continue"
read x

echo "\n\nSTEP 2: populate Tour and POI database\n\n"
# populate the database with the POIs and the tours
if [ -z "$GLASSFISH_HOME" ]; then
    echo "Glassfish is not installed: the client to populate the database with the POIs and the tours cannot be executed"
else
    ./Scripts/admin_client_tour_management.sh populate toursAndPOIs
fi

echo "\nHit return to continue"
read x

echo "\n\nSTEP 3: start Rabbit-MQ\n\n"
# clean up the rabbitmq server, and start the rabbitmq server
if [ -n "$RABBITMQ_MNESIA_BASE" ]; then
    rabbitmqctl stop
    rabbitmq-server -detached
    rabbitmqctl stop_app
    rabbitmqctl reset
    rabbitmqctl start_app
fi

echo "\nHit return to continue"
read x

echo "\n\nSTEP 4: start Lobby Room server\n\n"
# start the lobby room server
if [ -z "$RABBITMQ_MNESIA_BASE" ]; then
    echo "RabbitMQ is not installed: the lobby room server cannot be executed"
else
    ./Scripts/start_lobby_room_server.sh

    sleep 3
fi

echo "\nHit return to continue"
read x

echo "\n\nSTEP 5: star Visit Emulation server and Bike Station server\n\n"
# start the visit emulation server
if [ -z "$GLASSFISH_HOME" ]; then
    echo "Glassfish is not installed: the visit emulation server cannot be executed"
else
    procNumber="$(netstat -nlp | grep 127.0.0.1:8888 | cut -d"N" -f2 | cut -d"/" -f1)"
    if [ -n "$procNumber" ]; then
        echo "There is an old visit emulation server running; remove proc $procNumber"
        kill -9 "$procNumber"
    fi
    ./Scripts/start_visit_emulation_server.sh
    sleep 3
fi

echo "\nHit return to continue"
read x

echo "\n\nSTEP 6: start Joe and Avrel Tourist Application\n\n"
# start the tourist applications
if [ -z "$GLASSFISH_HOME" ] || [ -z "$RABBITMQ_MNESIA_BASE" ]; then
    echo "Glassfish or RabbitMQ are not installed: the tourist application cannot be executed"
else
    ./Scripts/start_tourist_application_w_emulated_location.sh Joe
    # ./vlibtour-scenario/ mvn exec:java@touristapplijoe
    #JOE=$TOURISTAPPLICATION
    sleep 1
    ./Scripts/start_tourist_application_w_emulated_location.sh Avrel
    # ./vlibtour-scenario/ mvn exec:java@touristappliavrel
    #AVREL=$TOURISTAPPLICATION
    sleep 1
fi

# echo "\n\nSTEP 7\n\n"
echo "Hit return to end the demonstration"
read x

# echo "\n\nSTEP 8\n\n"
# stop the tourist applications, just in case
while read pid; do
    kill -9 $pid
done < ~/.vlibtour/tourist_applications
# kill the lobby room server
kill -9 $(cat ~/.vlibtour/lobby_room_server)
# kill the visit emulation server
kill -9 $(cat ~/.vlibtour/visit_emulation_server)
# kill the bike stations server
kill -9 $(cat ~/.vlibtour/visit_bike_stations_server)


# echo "\n\nSTEP 9\n\n"
# stop the rabbitmq server
if [ -n "$RABBITMQ_MNESIA_BASE" ]; then
    rabbitmqctl stop_app
    rabbitmqctl stop
fi

# echo "\n\nSTEP 10\n\n"
# empty the database, undeploy the bean, and stop the database and the domain
if [ -n "$GLASSFISH_HOME" ]; then
    ./Scripts/admin_client_tour_management.sh empty toursAndPOIs
    asadmin undeploy vlibtour-tour-management-bean
    asadmin stop-database
    asadmin stop-domain domain1
fi
