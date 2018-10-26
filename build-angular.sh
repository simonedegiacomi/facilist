#!/bin/bash
cd client
npm install
ls
ls node_modules
ls node_modules/.bin
node_modules/.bin/ng build
cp -r dist/client ../src/main/resources/public/