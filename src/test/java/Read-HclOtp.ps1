Add-Type -AssemblyName UIAutomationClient
Add-Type -AssemblyName UIAutomationTypes

$root = [System.Windows.Automation.AutomationElement]::RootElement

$condition = New-Object System.Windows.Automation.PropertyCondition(
    [System.Windows.Automation.AutomationElement]::NameProperty,
    "Phone Link"
)

$app = $root.FindFirst(
    [System.Windows.Automation.TreeScope]::Descendants,
    $condition
)

if ($null -eq $app) {
    Write-Output "PHONE_LINK_NOT_FOUND"
    exit
}

$elements = $app.FindAll(
    [System.Windows.Automation.TreeScope]::Descendants,
    [System.Windows.Automation.Condition]::TrueCondition
)

$seen = New-Object 'System.Collections.Generic.HashSet[string]'

foreach ($element in $elements) {
    $message = $element.Current.Name

    if ([string]::IsNullOrWhiteSpace($message)) {
        continue
    }

    if ($message -match "HCLTech" -and
        $message -match "(?i)One Time Password") {

        $match = [regex]::Match(
            $message,
            "(?i)\b(\d{4})\s+is your One Time Password"
        )

        if ($match.Success) {
            $otp = $match.Groups[1].Value

            if ($seen.Add($otp)) {
                Write-Output "OTP=$otp"
            }
        }
    }
}

if ($seen.Count -eq 0) {
    Write-Output "HCLTECH_OTP_NOT_FOUND"
}