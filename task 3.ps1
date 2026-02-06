$logFilePath = "C:\server.log"

$errorLines = Get-Content -Path $logFilePath | Where-Object { $_ -match "Error" }
$count = $errorLines.Count

Write-Output "The file '$logFilePath' contains $count lines with the string 'Error'."