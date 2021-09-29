
# only execute scripts from local origin
# Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned


# Install git extension for powershell
Install-Module posh-git -Scope CurrentUser

# Add to $profile
Add-PoshGitToProfile