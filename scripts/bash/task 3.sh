#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <logFilePath>"
    exit 1
fi

logFile="$1"

count=$(grep -c "Error" "$logFile")

echo "The number of lines containing \"Error\" in $logFile is: $count"
