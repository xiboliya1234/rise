param(
    [int]$BackendPort = 12345,
    [int]$FrontendPort = 8080,
    [string]$MavenCmd = "C:\Users\13798\Desktop\apache-maven-3.8.1\bin\mvn.cmd"
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Join-Path $repoRoot "new"
$frontendDir = Join-Path $repoRoot "riseout\riseour"
$stateDir = Join-Path $env:TEMP "rise-codex-launcher"
$backendPidFile = Join-Path $stateDir "backend-shell.pid"
$frontendPidFile = Join-Path $stateDir "frontend-shell.pid"

function Assert-Path([string]$path, [string]$name) {
    if (-not (Test-Path -LiteralPath $path)) {
        throw "$name not found: $path"
    }
}

function Get-ListeningPid([int]$port) {
    $line = netstat -ano -p tcp | Select-String -Pattern "^\s*TCP\s+\S+:$port\s+\S+\s+LISTENING\s+(\d+)\s*$" | Select-Object -First 1
    if (-not $line) { return $null }
    $m = [regex]::Match($line.ToString(), "LISTENING\s+(\d+)\s*$")
    if ($m.Success) { return [int]$m.Groups[1].Value }
    return $null
}

function Wait-Port([int]$port, [int]$timeoutSec) {
    $deadline = (Get-Date).AddSeconds($timeoutSec)
    while ((Get-Date) -lt $deadline) {
        if (Get-ListeningPid -port $port) { return $true }
        Start-Sleep -Seconds 1
    }
    return $false
}

function Stop-TreeByPid([int]$procId) {
    if ($procId -gt 0) {
        taskkill /PID $procId /T /F | Out-Null
    }
}

$backendProc = $null
$frontendProc = $null

try {
    Assert-Path -path $backendDir -name "Backend directory"
    Assert-Path -path $frontendDir -name "Frontend directory"
    Assert-Path -path $MavenCmd -name "Maven executable"

    if (-not (Get-Command npm.cmd -ErrorAction SilentlyContinue)) {
        throw "npm.cmd was not found. Install Node.js and ensure npm is in PATH."
    }

    if (Get-ListeningPid -port $BackendPort) {
        throw "Backend port $BackendPort is already in use. Run stop-all.ps1 first."
    }

    if (Get-ListeningPid -port $FrontendPort) {
        throw "Frontend port $FrontendPort is already in use. Run stop-all.ps1 first."
    }

    $mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
    if ($mysqlService) {
        if ($mysqlService.Status -ne "Running") {
            Write-Host "Starting MySQL80 service..."
            Start-Service -Name "MySQL80"
            Start-Sleep -Seconds 2
        }
    } else {
        Write-Warning "MySQL80 service not found. If you use another DB service name, ignore this warning."
    }

    New-Item -ItemType Directory -Path $stateDir -Force | Out-Null

    $backendCommand = "Set-Location -LiteralPath '$backendDir'; & '$MavenCmd' spring-boot:run -DskipTests '-Dspring-boot.run.arguments=--server.port=$BackendPort'"
    $backendProc = Start-Process -FilePath "powershell.exe" -ArgumentList @("-NoExit", "-NoProfile", "-Command", $backendCommand) -PassThru
    Set-Content -LiteralPath $backendPidFile -Value $backendProc.Id -Encoding ASCII
    Write-Host "Backend window started. PID: $($backendProc.Id)"

    if (-not (Wait-Port -port $BackendPort -timeoutSec 150)) {
        throw "Backend did not listen on port $BackendPort within 150 seconds."
    }

    $frontendCommand = "Set-Location -LiteralPath '$frontendDir'; `$env:PORT='$FrontendPort'; `$env:BROWSER='none'; npm.cmd run serve"
    $frontendProc = Start-Process -FilePath "powershell.exe" -ArgumentList @("-NoExit", "-NoProfile", "-Command", $frontendCommand) -PassThru
    Set-Content -LiteralPath $frontendPidFile -Value $frontendProc.Id -Encoding ASCII
    Write-Host "Frontend window started. PID: $($frontendProc.Id)"

    if (-not (Wait-Port -port $FrontendPort -timeoutSec 120)) {
        Write-Warning "Frontend is not listening on port $FrontendPort yet. Check frontend window logs."
    }

    Write-Host ""
    Write-Host "All start steps completed."
    Write-Host "Backend URL : http://localhost:$BackendPort"
    Write-Host "Frontend URL: http://localhost:$FrontendPort"
    Write-Host "Stop command: .\stop-all.ps1 or stop-all.bat"
}
catch {
    Write-Error $_
    if ($frontendProc) { Stop-TreeByPid -procId $frontendProc.Id }
    if ($backendProc) { Stop-TreeByPid -procId $backendProc.Id }
    throw
}
