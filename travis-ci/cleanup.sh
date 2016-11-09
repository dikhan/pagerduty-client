#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    # Clean up gpg content
    shred --remove travis-ci/signingkey.asc
#    gpg --yes --batch --delete-key $GPG_KEY_NAME
#    gpg --yes --batch --delete-secret-key $GPG_KEY_NAME

    # Clean up GitHub bits and bobs
    ssh-add -D
    shred --remove ~/.ssh/github.com_rsa.pem
    git config --global --unset user.email
    git config --global --unset user.name
    git remote rm origin
fi