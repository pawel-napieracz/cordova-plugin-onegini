'use strict';

const fs = require('fs');
const spawn = require('child_process').spawn;

module.exports = function (context) {
  const args = [
    '--cordova',
    '--app-dir', context.opts.projectRoot
  ];
  console.log('Configuring the Onegini SDK');
  console.log('===========================');
  console.log('')
  console.log('')

  context.opts.platforms.forEach((platform) => {
    let platformArgs = args;
    platformArgs.unshift(platform);
    platformArgs.push('--config',`${context.opts.projectRoot}/onegini-config-${platform}.zip`)

    console.log(`Configuring the ${platform} platform`)
    console.log('--------------------------' + Array(platform.length).join("-"))
    console.log('')
    execConfigurator(platformArgs);
  });
};

function execConfigurator(args) {
  console.log('Running command: ')
  console.log('onegini-sdk-configurator ' + args.join(' '))
  console.log('')
  const configurator = spawn('onegini-sdk-configurator', args);

  configurator.stdout.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.stderr.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.on('close', (code) => {
    if (code !== 0) {
      console.log('onegini-cordova-plugin: Could not configure Onegini SDK with your configuration');
      process.exit(code);
    }
  });
}