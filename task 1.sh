#!/bin/bash

if [ "$#" -ne 1]; then
    echo "error: enter file name as argument"
    exit 1
fi

filename="$1"

if [ -f "$filename"]; then
    fileSize=$(stat -c%s "$filename" 2>/dev/null)

    if [ "$fileSize" -gt 1048576]; then
        echo "Warning: File is too large"
    else
        echo "File size is within limits."
    fi