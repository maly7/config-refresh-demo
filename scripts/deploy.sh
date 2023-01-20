#!/bin/sh

az spring app deploy -n config-refresh-demo --source-path . --build-env BP_JVM_VERSION=17 --config-file-patterns "config-refresh-demo"