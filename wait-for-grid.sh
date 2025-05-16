#!/bin/bash

HUB_HOST=${HUB_HOST:-selenium-hub}
PORT=4444

echo "Waiting for Selenium Grid at $HUB_HOST:$PORT..."

while ! nc -z $HUB_HOST $PORT; do
  sleep 1
done

echo "Selenium Grid is ready. Starting tests..."

exec java -cp CarCheckingTest.jar:CarCheckingTest-tests.jar:libs/* \
     org.testng.TestNG -verbose 10 testng.xml
