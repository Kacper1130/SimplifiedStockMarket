@echo off
setlocal

set APP_PORT=%1
if "%APP_PORT%"=="" set APP_PORT=8080

docker-compose down
docker-compose up --build -d

endlocal