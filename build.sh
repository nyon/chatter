#!/usr/bin/env bash
mvn package &&
cd service &&
docker-compose build