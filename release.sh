#!/bin/bash
git checkout -b release
git merge master
git push
git checkout master