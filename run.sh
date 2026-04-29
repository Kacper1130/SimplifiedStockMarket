#!/bin/bash

export APP_PORT=${1:-8080}

docker-compose down
docker-compose up --build -d