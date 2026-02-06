param (
    [string]$Filename
)
if (-not $Filename) {
    Write-Host "error: provide a filename as an argument"
    exit 1
}
if (test-path -Path $Filename -PathType Leaf) {
    $fileInfo = Get-Item -Path $Filename
    $fileSize = $fileInfo.Length
    $oneMB = 1048576

    if ($fileSize -gt $oneMB) {
        Write-Host "Warning: File is too large"
    } else {
        Write-Host "File size is within limits."
    }
} 