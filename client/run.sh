#!/usr/bin/env bash
cd "$(dirname "$0")"
java -jar "$(find target/client*.jar)" "${@: 1}"