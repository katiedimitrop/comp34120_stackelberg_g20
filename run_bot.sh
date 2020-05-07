#!/bin/env bash

echo "STARTING PLATFORM...."
cd g20Stackelberg/
java -classpath poi-3.7-20101029.jar: -Djava.rmi.server.hostname=127.0.0.1 comp34120.ex2.Main &
sleep 6
echo "PLATFORM STARTED, STARTING LEADER TO CONNECT"
cd ../target/
java -Djava.rmi.server.hostname=127.0.0.1 -cp g20Bot-1.0-shaded.jar comp34120.ex2.Leader -M Standard
echo "LEADER CONNECTED. CTRL-C TO EXIT"
