echo "Date/Time:    $(date)"
echo "Hostname:     $(hostname)"
echo "Current User: $(whoami)"
echo "DISK USAGE (Main Drive /):"
df -h / | awk 'NR==2 {printf "Total Space:  %s\nFree Space:   %s\nUsed Percent: %s\n", $2, $4, $5}'