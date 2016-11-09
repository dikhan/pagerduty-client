#!/usr/bin/env bash

# This script will be used by Travis CI, thus it's able to decrypt the cert and sign the artifacts appropriately

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_363c33d4ecac_key -iv $encrypted_363c33d4ecac_iv -in travis-ci/codesigning.asc.enc -out travis-ci/signingkey.asc -d
    gpg --yes --fast-import travis-ci/signingkey.asc
fi