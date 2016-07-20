const fs = require('fs');
const spawn = require('child_process').spawn;

module.exports = function (context) {
  const configPath = context.opts.projectRoot + "/onegini-config.zip";
  const args = [
    '--cordova',
    '--config', configPath,
    '--app-dir', context.opts.projectRoot
  ];
  console.log(`Configuring Onegini SDK`);

  context.opts.platforms.forEach((platform) => {
    let platformArgs = args;
    platformArgs.unshift(platform);
    execConfigurator(platformArgs);
  });
};

function execConfigurator(args) {
  const configurator = spawn('onegini-sdk-configurator', args);

  configurator.stdout.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.stderr.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.on('close', (code) => {
    if(code !== 0) {
      console.log(`onegini-cordova-plugin: Could not configure Onegini SDK with your configuration`);
      process.exit(code);
    }
  });
}