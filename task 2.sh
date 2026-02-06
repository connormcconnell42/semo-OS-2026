#!/bin/bash

ps -eo cmd,pid,%mem --sort=-%mem | head -n 6 | tail -n 5 | awk '{printf "%-12s | %-5s | %-5s\n", $1, $2, $3}'