$currentDateTime = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$hostname = $env:COMPUTERNAME
$currentUser = $env:USERNAME
$diskInfo = Get-Volume -DriveLetter C | Select-Object -First 1
if ($diskInfo) {
    $totalSpaceGB = ($diskInfo.Size / 1GB).ToString("F2")
    $freeSpaceGB = ($diskInfo.SizeRemaining / 1GB).ToString("F2")
    $diskUsage = "Free Space: $($freeSpaceGB) GB / Total Space: $($totalSpaceGB) GB"
} else {
    $diskUsage = "Could not retrieve disk information for C: drive."
}

$report = @"
System Report
-----------------
Current Date and Time: $currentDateTime
Hostname:              $hostname
Current User:          $currentUser
Disk Usage (C: Drive): $diskUsage
"@

Write-Output $report