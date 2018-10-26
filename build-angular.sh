#!/bin/bash
cd client
npm install
node_modules/.bin/ng build
cp -r dist/client ../src/main/resources/public/