# Set the folder path where the files are located
$folderPath = "C:\folder\path"

# Set the prefix to add to the file names
$prefix = "myprefix_"

# Get all files in the specified folder
$files = Get-ChildItem $folderPath

# Loop through each file and add the prefix to the file name
foreach($file in $files) {
  $oldName = $file.Name
  $newName = $prefix + $oldName
  Rename-Item -Path "$folderPath\$oldName" -NewName $newName
}
