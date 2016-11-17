#!/bin/bash

set -ev

# This script will be used by Travis CI, thus it's able to decrypt the cert and sign the artifacts appropriately

echo $TRAVIS_BRANCH;
echo $TRAVIS_COMMIT_DESCRIPTION;
echo $TRAVIS_EVENT_TYPE;

if [[ "$TRAVIS_COMMIT_DESCRIPTION" != *"maven-release-plugin"* ]];then

    if [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_EVENT_TYPE" == "pull_request" ];then
        openssl aes-256-cbc -K $encrypted_363c33d4ecac_key -iv $encrypted_363c33d4ecac_iv -in travis-ci/codesigning.asc.enc -out travis-ci/signingkey.asc -d
        gpg --yes --batch --fast-import travis-ci/signingkey.asc  || { echo $0: mvn failed; exit 1; }
    fi

else
  echo "before-deploy: Not running gpg commands due to maven-release-plugin auto commit - this is just for preparation for dev/releases";
fi

