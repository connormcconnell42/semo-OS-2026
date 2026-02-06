$files = Get-ChildItem -Path . -Filter *.txt -File

foreach ($file in $files) {
    $newName = "OLD_" + $file.Name
    
    Rename-Item -Path $file.FullName -NewName $newName
}