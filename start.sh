#!/bin/bash

if [ "$1" = "socket" ]; then
  # Command to start the server in the background
  mvn clean compile exec:java -Dexec.mainClass="com.example.Main" -Dexec.args="server" &

  # Wait for a short period to allow the server to start
  sleep 5

  # Command to start the client
  mvn clean compile exec:java -Dexec.mainClass="com.example.Main" -Dexec.args="client"
else
  # Command for Communication with Single Instance (Same Java Process)
  mvn clean compile exec:java -Dexec.mainClass="com.example.Main" -Dexec.args="normal"
fi

# After the client finishes, kill the server process
kill $(pgrep -f "com.example.Main")

# Clean up Maven artifacts
mvn clean
