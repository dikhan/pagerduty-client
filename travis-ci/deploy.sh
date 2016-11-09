#!/usr/bin/env bash

# This script will be used by Travis CI and will deploy the project to maven, making sure to use the sign and
# build-extras profiles and any settings in our settings file.

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -P sign,build-extras --settings travis-ci/settings.xml
fi