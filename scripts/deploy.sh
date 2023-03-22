#!/bin/sh

az spring app deploy -n test-app --source-path . --build-env BP_JVM_VERSION=17
