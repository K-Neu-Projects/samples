###################################################################################################################
#   $Key -> Checks if a config key contains this value, leave empty to delete all keys with a given label instead #
#   $Label -> The label to fetch keys for                                                                         #
#   $Subscription -> Subscription Id of the subscription the config is located in                                 #
#   $AppConfigName -> Name of the app config to delete keys in                                                    #
###################################################################################################################
param (
    $Key = '',
    $Label = 'testing',
    $Subscription = '',
    $AppConfigName = 'MyOfficeConfig'
)

az account set -s $Subscription
Write-Host "Fetching all key-values..."
$configs = (az appconfig kv list -n $AppConfigName --all --label $Label | ConvertFrom-Json)

Write-Host "Found $($configs.Length) configs, going to delete the following:"
$configsToDelete = New-Object System.Collections.Generic.List[System.Object]
foreach ($config in $configs) {
    if(!$Key -or $config.key.contains($Key)){
        Write-Host "[$($config.label)] $($config.key)"
        $configsToDelete.add($config)
    }
}

Write-Host
Write-Host 'Press any key to delete or press Ctrl + C to cancel...'
Read-Host

foreach ($config in $configsToDelete) {
    Write-Host "[$($config.label)] $($config.key)"
    az appconfig kv delete -n $AppConfigName --key $config.key --label $config.label --yes
        
}