@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
echo [rise] Stopping frontend and backend...
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%stop-all.ps1"
if errorlevel 1 (
  echo.
  echo [rise] Stop failed. See output above.
  pause
  exit /b 1
)

echo.
echo [rise] Stop script completed.
pause
