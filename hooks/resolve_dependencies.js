const fs = require("fs");
const spawn = require('child_process').spawn;

module.exports = function (context) {
  const pluginId = 'cordova-plugin-onegini';
  const deferral = context.requireCordovaModule('q').defer();

  console.log(`${pluginId}: Resolving gradle dependencies...`);

  const options = {
    cwd: context.opts.plugin.pluginInfo.dir,
    shell: true
  };
  const gradle = spawn('gradle', ['clean', 'resolveDependencies'], options);

  gradle.stdout.on('data', (data) => {
    process.stdout.write(data);
  });

  gradle.stderr.on('data', (data) => {
    process.stdout.write(data);
  });

  gradle.on('close', (code) => {
    if (code === 0) {
      console.log(`${pluginId}: Resolved dependencies.`);
      deferral.resolve();
    } else {
      console.log(`${pluginId}: Error: cannot resolve dependencies! Make sure you have gradle installed and have access to the Onegini repository.`)
      deferral.reject();
    }
  });

  return deferral.promise;
};