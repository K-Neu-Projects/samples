# Custom Commands
# 
# Depending on where the command should be available place a profile.ps1 file in one of the folders documented in the link below
# https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.2#the-profile-files
#
# Add custom commands 
# Start a new powershell session to load the new profile

# Custom Change directory command
function cdToYourLocation { Set-Location "your\location" } New-alias -Name gotoyourlocation cdToYourLocation
