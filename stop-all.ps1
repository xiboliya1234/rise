param(
    [int]$BackendPort = 12345,
    [int]$FrontendPort = 8080
)

$ErrorActionPreference = "Stop"

$stateDir = Join-Path $env:TEMP "rise-codex-launcher"
$backendPidFile = Join-Path $stateDir "backend-shell.pid"
$frontendPidFile = Join-Path $stateDir "frontend-shell.pid"

function Get-ListeningPid([int]$port) {
    $line = netstat -ano -p tcp | Select-String -Pattern "^\s*TCP\s+\S+:$port\s+\S+\s+LISTENING\s+(\d+)\s*$" | Select-Object -First 1
    if (-not $line) { return $null }
    $m = [regex]::Match($line.ToString(), "LISTENING\s+(\d+)\s*$")
    if ($m.Success) { return [int]$m.Groups[1].Value }
    return $null
}

function Stop-TreeByPid([int]$procId) {
    if ($procId -gt 0) {
        taskkill /PID $procId /T /F | Out-Null
    }
}

function Stop-FromPidFile([string]$pidFile, [string]$name) {
    if (-not (Test-Path -LiteralPath $pidFile)) { return }
    $raw = (Get-Content -LiteralPath $pidFile -ErrorAction SilentlyContinue | Select-Object -First 1).Trim()
    if ($raw -match "^\d+$") {
        $procId = [int]$raw
        Write-Host "Stopping $name (PID=$procId)"
        Stop-TreeByPid -procId $procId
    }
    Remove-Item -LiteralPath $pidFile -Force -ErrorAction SilentlyContinue
}

try {
    Stop-FromPidFile -pidFile $frontendPidFile -name "frontend shell"
    Stop-FromPidFile -pidFile $backendPidFile -name "backend shell"

    $backendPid = Get-ListeningPid -port $BackendPort
    if ($backendPid) {
        Write-Host "Stopping backend listener on port $BackendPort (PID=$backendPid)"
        Stop-TreeByPid -procId $backendPid
    }

    $frontendPid = Get-ListeningPid -port $FrontendPort
    if ($frontendPid) {
        Write-Host "Stopping frontend listener on port $FrontendPort (PID=$frontendPid)"
        Stop-TreeByPid -procId $frontendPid
    }

    $vueNode = Get-CimInstance Win32_Process -Filter "Name='node.exe'" -ErrorAction SilentlyContinue |
        Where-Object { $_.CommandLine -match "vue-cli-service(\.js)?\s+serve" }
    foreach ($p in $vueNode) {
        Write-Host "Stopping vue-cli-service process PID=$($p.ProcessId)"
        Stop-TreeByPid -procId $p.ProcessId
    }

    Write-Host "Stop completed."
}
catch {
    Write-Error $_
    throw
}
