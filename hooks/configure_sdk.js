/*
 * Copyright (c) 2017 Onegini B.V.
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
const path = require('path');
const spawn = require('child_process').spawn;

const supportedPlatforms = ['android', 'ios'];
const envVariables = {
  enableAutoconfigure: 'ONEGINI_AUTOCONFIGURE',
  configuratorName: 'ONEGINI_SDK_CONFIGURATOR_PATH',
  configFiles: {
    android: 'ONEGINI_CONFIG_ANDROID_PATH',
    ios: 'ONEGINI_CONFIG_IOS_PATH'
  }
};

module.exports = function (context) {
  if (process.env.ONEGINI_AUTOCONFIGURE === "false") {
    console.log('ONEGINI_AUTOCONFIGURE is set to false. Skipping Onegini SDK Configuration');
    return;
  }

  const deferral = context.requireCordovaModule('q').defer();
  const projectRoot = context.opts.projectRoot
  const args = [
    '--cordova',
    '--app-dir', projectRoot
  ];

  let platform = context.opts.plugin.platform;

  // Don't trigger the iOS SDK configurator during the 'after_plugin_install' lifecycle phase as it will be triggered again during the 'after_platform_add'
  // lifecycle phase.
  // There is a caveat if 'cordova platform add ios android' is triggered. In this the configurator for iOS can't be suppressed for the iOS platform only.
  // Hence, this command will execute the SDK configurator twice for the iOS platform. It does not break anything but is merely redundant.
  if (afterPluginInstallHookTriggeredDuringPlatformInstallOnlyForIos(context)) {
    return;
  }

  // Only trigger the SDK configurator during the 'after_platform_add' lifecycle phase for the iOS platform in case the
  // cordova command 'cordova platform add ios' is triggered.
  if (afterPlatformAddHookTriggeredDuringPlatformAddAndIosPlatformInstalled(context, projectRoot)) {
    // the platform is not provided in the context for the 'cordova platform add <platform>' command so it is set manually.
    platform = 'ios';
  }
  else if (platform === undefined || platform === 'undefined') {
    return;
  }

  console.log(`Configuring the ${platform} platform`);
  console.log('===========================\n\n');

  if (supportedPlatforms.indexOf(platform) < -1) {
    console.log(`${platform} is not supported`);
    return deferral.promise;
  }

  let platformArgs = args.slice();
  platformArgs.unshift(platform);
  platformArgs.push('--config', getConfigFileForPlatform(context.opts.projectRoot, platform));

  execConfigurator(platformArgs, deferral);

  return deferral.promise
};

function afterPluginInstallHookTriggeredDuringPlatformInstallOnlyForIos(context) {
  let afterPluginInstallHookTriggered = context.hook === 'after_plugin_install';
  const platformAddCommandTriggeredOnlyForIos =
    context.cmdLine.includes('platform add')
    && context.cmdLine.includes('ios')
    && !context.cmdLine.includes('android');

  return (afterPluginInstallHookTriggered && platformAddCommandTriggeredOnlyForIos)
}

function afterPlatformAddHookTriggeredDuringPlatformAddAndIosPlatformInstalled(context, projectRoot) {
  const afterPlatformAddHookTriggered = context.hook === 'after_platform_add';
  const iosPlatformInstalled = fs.existsSync(path.join(projectRoot, 'platforms', 'ios'));
  const platformAddCommandTriggeredForIos = context.cmdLine.includes('platform add') && context.cmdLine.includes('ios');

  return (afterPlatformAddHookTriggered && iosPlatformInstalled && platformAddCommandTriggeredForIos);
}

function execConfigurator(args, deferral) {
  const configuratorName = getConfiguratorName();

  console.log('\nRunning command:');
  console.log(`${configuratorName} ${args.join(' ')}\n`);

  const configurator = spawn(configuratorName, args);

  configurator.stdout.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.stderr.on('data', (data) => {
    process.stdout.write(data);
  });

  configurator.on('close', (code) => {
    if (code === 0) {
      deferral.resolve();
    } else {
      deferral.reject('Could not configure the Onegini SDK with your configuration');
    }
  });
}

function getConfigFileForPlatform(projectRoot, platform) {
  const environmentVar = envVariables.configFiles[platform];
  const environmentLocation = process.env[environmentVar];
  const defaultLocation = `${projectRoot}/onegini-config-${platform}.zip`;

  if (environmentLocation) {
    console.log(`Using Token Server config zip: '${environmentLocation}' set in '${environmentVar}'`);
    return environmentLocation;
  }

  console.log(`Using default Token Server config zip: '${defaultLocation}'`);
  return defaultLocation;
}

function getConfiguratorName() {
  const environmentVar = envVariables.configuratorName;
  const environmentName = process.env[environmentVar];
  const defaultName = 'onegini-sdk-configurator';

  if (environmentName) {
    console.log(`Using SDK Configurator executable in '${environmentName}' set in '${environmentVar}'`);
    return environmentName;
  }

  console.log('Using SDK Configurator from $PATH');
  return defaultName;
}
