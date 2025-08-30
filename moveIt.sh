#! /bin/sh
yarn prepare && rm -Rf ../social-app/node_modules/vync-video-player && cp -R . ../social-app/node_modules/vync-video-player
