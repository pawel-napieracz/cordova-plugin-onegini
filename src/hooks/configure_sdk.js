const fs = require('fs');
const spawn = require('child_process').spawn;

module.exports = function (context) {
  const configPath = context.opts.projectRoot + "/onegini-config.zip";
  const env = process.env.NODE_ENV || "production";
  const args = [
    '--cordova',
    '--env', env,
    '--config', configPath,
    '--app-dir', context.opts.projectRoot
  ];

  context.opts.platforms.forEach((platform) => {
    let platformArgs = args;
    platformArgs.unshift(platform);
    execConfigurator(platformArgs);
  });
};

function execConfigurator(args) {
  const configurator = spawn('sdk-configurator', args);

  configurator.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });

  configurator.stderr.on('data', (data) => {
    console.log(`stderr: ${data}`)
  });
}