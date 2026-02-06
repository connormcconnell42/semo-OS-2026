$processes = Get-Process -ErrorAction SilentlyContinue

$topProcesses = $processes | Sort-Object -Property WS -Descending | Select-Object -First 5

foreach ($process in $topProcesses) {
    $memoryUsageMB = [math]::Round($process.WS / 1MB, 2)
    Write-Host "Process Name: $($process.ProcessName) | ID (PID): $($process.ID) | Memory Usage (MB): $($memoryUsageMB)"
}