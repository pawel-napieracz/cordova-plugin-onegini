const fs = require("fs");
const platform = require('platform');
const spawn = require('child_process').spawn;

module.exports = function (context) {
  const pluginId = 'cordova-plugin-onegini';
  const deferral = context.requireCordovaModule('q').defer();

  console.log(`${pluginId}: Resolving gradle dependencies...`);

  const options = {
    cwd: context.opts.plugin.pluginInfo.dir,
    shell: true
  };
  executable = getGradleExecutableForPlatform()
  const gradle = spawn(executable, ['clean', 'resolveDependencies'], options);

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
      deferral.reject(`${pluginId}: Error: cannot resolve dependencies! Make sure you have access to the Onegini repository. See the installation documentation for more info.`);
    }
  });

  return deferral.promise;
};

function getGradleExecutableForPlatform() {
  const osFamily = platform.os.family;

  var executable = './gradlew';
  if (osFamily.toLowerCase().startsWith('win')) {
    executable = 'gradlew.bat';
  }

  return executable;
}