# Chatter

Simple messaging service written with spring reactive.

⚠️ Disclaimer: This project is not meant to be used in production. Use at your own risk.

Live demo: http://nyon.de:3000

## Architecture overview

![](./overview.drawio.png)

## Create docker image

Run `./build.sh`.

## Run locally

Run `docker-compose up`.
If you want to use a web client open `http://localhost:3000` in your browser.
If you want to use the CLI client run `client/run.sh` in your terminal.

### Connect your local CLI client to the live demo

Run `client/run.sh ws://nyon.de:8080/chat`