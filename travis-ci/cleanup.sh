#!/bin/bash

set -ev

echo $TRAVIS_BRANCH;
echo $TRAVIS_COMMIT_DESCRIPTION;
echo $TRAVIS_EVENT_TYPE;

if [[ "$TRAVIS_COMMIT_DESCRIPTION" != *"maven-release-plugin"* ]];then

    if [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_EVENT_TYPE" == "pull_request" ];then
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

else
  echo "cleanup: Not running cleanup mvn commands due to maven-release-plugin auto commit - this is just for preparation for dev/releases";
fi
