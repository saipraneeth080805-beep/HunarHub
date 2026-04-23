#!/bin/bash

# Check if git filter-repo is installed
if ! command -v git-filter-repo &> /dev/null; then
    echo "git-filter-repo could not be found. Please install it first."
    exit
fi

# Remove all commits by the specified contributor
git filter-repo --commit-callback '
if commit.author_email == "saipraneeth080805-beep@example.com":
    commit.drop()'

# Force push the changes to the remote repository
git push origin --force --all
