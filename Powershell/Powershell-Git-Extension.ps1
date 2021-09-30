
# only execute scripts from local origin
# Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned


# Install git extension for powershell
Install-Module PowerShellForGitHub -Scope CurrentUser

# Add to $profile
# Note that there are multiple profiles e.g. executing this within VS Code will only add it to 
# the vsCode_profile, not the global one. PowerShell ISE also has its own profile
Write-Output "Import-Module PowerShellForGitHub" >> $profile