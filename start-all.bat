@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
echo [rise] Running one-click startup...
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%start-all.ps1"
if errorlevel 1 (
  echo.
  echo [rise] Startup failed. See output above.
  pause
  exit /b 1
)

echo.
echo [rise] Startup script completed.
pause
