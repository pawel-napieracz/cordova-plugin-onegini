/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

const fs = require('fs');
const spawn = require('child_process').spawn;
const supportedPlatforms = ["ios", "android"];

module.exports = function (context) {
  const deferral = context.requireCordovaModule('q').defer();
  const args = [
    '--cordova',
    '--app-dir', context.opts.projectRoot
  ];
  console.log('Configuring the Onegini SDK');
  console.log('===========================\n\n');

  context.opts.platforms.forEach((platform) => {
    if (arrayContains(platform, supportedPlatforms)) {
      let platformArgs = args;
      platformArgs.unshift(platform);
      platformArgs.push('--config',`${context.opts.projectRoot}/onegini-config-${platform}.zip`)

      console.log(`Configuring the ${platform} platform`)
      console.log('--------------------------' + Array(platform.length).join("-") + '\n')
      execConfigurator(platformArgs, deferral);
    } else {
      console.log(`Skipping unsupported platform: ${platform}`)
    }
  });

  return deferral.promise;
};

function execConfigurator(args, deferral) {
  console.log('Running command: ')
  console.log('onegini-sdk-configurator ' + args.join(' ') + '\n')
  const configurator = spawn('onegini-sdk-configurator', args);

  configurator.stdout.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.stderr.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.on('close', (code) => {
    if (code !== 0) {
      deferral.reject('Could not configure the Onegini SDK with your configuration');
    }

    deferral.resolve();
  });
}

function arrayContains(needle, arrhaystack) {
    return (arrhaystack.indexOf(needle) > -1);
}