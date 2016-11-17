#!/bin/bash

set -ev

# This script will be used by Travis CI and will deploy the project to maven, making sure to use the sign and
# build-extras profiles and any settings in our settings file.

echo $TRAVIS_BRANCH;
echo $TRAVIS_COMMIT_DESCRIPTION;
echo $TRAVIS_EVENT_TYPE;
echo $TRAVIS_PULL_REQUEST_BRANCH;
echo $TRAVIS_PULL_REQUEST;

if [[ "$TRAVIS_COMMIT_DESCRIPTION" != *"maven-release-plugin"* ]];then

    openssl aes-256-cbc -pass pass:$PEM_ENCRYPTION_PASSWORD -in travis-ci/keys/github.com_rsa.pem.enc -out ~/.ssh/github.com_rsa.pem -d

    eval "$(ssh-agent -s)"
    chmod 600 ~/.ssh/github.com_rsa.pem
    ssh-add ~/.ssh/github.com_rsa.pem
    ssh-add -l
    git config --global user.email $GITHUB_EMAIL
    git config --global user.name $GITHUB_USERNAME

    git remote set-head origin $TRAVIS_BRANCH
    git show-ref --head
    git symbolic-ref HEAD refs/heads/$TRAVIS_BRANCH
    git symbolic-ref HEAD


    if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST_BRANCH" == "" ];then
        mvn --batch-mode release:clean release:prepare  || { echo $0: mvn failed; exit 1; }
        mvn release:perform --settings travis-ci/settings.xml  || { echo $0: mvn failed; exit 1; }
    else if [ "$TRAVIS_PULL_REQUEST" != "" ] && [ "$TRAVIS_EVENT_TYPE" == "pull_request" ];then
            mvn --batch-mode release:clean release:prepare release:stage --settings travis-ci/settings.xml || { echo $0: mvn failed; exit 1; }
        fi
    fi

else
  echo "deploy: Not running deploy mvn commands due to maven-release-plugin auto commit - this is just for preparation for dev/releases";
fi

