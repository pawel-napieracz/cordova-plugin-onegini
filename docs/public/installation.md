# Installing the plugin

Before you install the plugin make sure that you have access to the Onegini Artifactory repository. If you don't have access please contact Onegini Support. 
Access to Artifactory is required to download the Onegini iOS SDK library.

Also make sure that your Artifactory username and password are set in the `gradle.properties` file in your Gradle user home (e.g. ~/.gradle):
e.g.
```
artifactory_user=<username>
artifactory_password=<password>
```

See the documentation below for instructions on setting Gradle properties:
[https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties)

To install the plugin execute the following command from your Cordova application project directory.

```sh
cordova plugin add https://github.com/Onegini/onegini-cordova-plugin
```

After you have installed the plugin you can add platforms to your project. The Onegini Cordova plugin uses hooks to configure the SDK. There are a few 
prerequisites to this:

1. SDK Configurator 
You must have the [Onegini SDK Configurator](https://github.com/Onegini/sdk-configurator) installed and it must be accessible from the $PATH in your command line
application.

2. Node module
The node 'platform' module must be installed. execute:
```sh
npm install platform
```

3. Token Server configuration zip
In order to successfully add a platform you must also have a zip file containing the Token Server configuration for your application. This zip file can be 
downloaded from the Token Server administration console.

For every platform that you add you must provide one zip file.

The plugin will look for the following files:
- Android: onegini-config-android.zip
- iOS: onegini-config-ios.zip

>**Note:** You must remove and add your platforms (iOS & Android) again if you change the Token Server configuration zip. Configuration changes to your Cordova 
apps will only be applied after the SDK configurator has run again.

Make sure that these files are placed in the root folder of your Cordova application. Now you can add a platform:

```sh
cordova platform add android
```

## Troubleshooting

### Node module 'platform' not installed

If you see the error below it means that you haven't installed the node platform module. Make sure that you follow the installation instructions to install this 
module.

```
Failed to install 'cordova-plugin-onegini':Error: Cannot find module 'platform'
    at Function.Module._resolveFilename (module.js:339:15)
    at Function.Module._load (module.js:290:25)
    at Module.require (module.js:367:17)
    at require (internal/module.js:16:19)
    at Object.<anonymous> (/Volumes/code/mobile-platform/cordova-app/plugins/cordova-plugin-onegini/hooks/resolve_dependencies.js:2:18)
    at Module._compile (module.js:413:34)
    at Object.Module._extensions..js (module.js:422:10)
    at Module.load (module.js:357:32)
    at Function.Module._load (module.js:314:12)
    at Module.require (module.js:367:17)
Error: Cannot find module 'platform'
```

### Configuration zip not found

If you see the error below it means that the configurator could not find the Token Server configuration zip. Please make sure that you have placed the 
zip in the location that is specified in the error.

```
Configuring the Onegini SDK
===========================


Configuring the android platform
--------------------------------

Running command: 
onegini-sdk-configurator android --cordova --app-dir /Volumes/code/mobile-platform/cordova-app --config /Volumes/code/mobile-platform/cordova-app/onegini-config-android.zip

ERROR: could not read Token Server configuration zip: open /Volumes/code/mobile-platform/cordova-app/onegini-config-android.zip: no such file or directory
Error: Could not configure the Onegini SDK with your configuration
```

### Onegini SDK Configurator not found

If you see the error below it means that you do not have the `onegini-sdk-configurator` installed or it cannot be found in the $PATH.

```
Configuring the Onegini SDK
===========================

Configuring the android platform
--------------------------------

Running command:
onegini-sdk-configurator android --cordova --app-dir /Volumes/code/mobile-platform/cordova-app --config /Volumes/code/mobile-platform/cordova-app/onegini-config-android.zip

Error: spawn onegini-sdk-configurator ENOENT
```

The links below give you hints on how to add a folder to the $PATH. Make sure that you add the folder where you have placed the Onegini SDK Configurator to your $PATH
- Windows:  http://www.howtogeek.com/118594/how-to-edit-your-system-path-for-easy-command-line-access/
- Linux: http://stackoverflow.com/questions/14637979/how-to-permanently-set-path-on-linux/14638025#14638025
- Mac: http://www.cyberciti.biz/faq/appleosx-bash-unix-change-set-path-environment-variable/

# Updating the plugin

To update the plugin you must remove the plugin and add it again. To make sure that the plugin is configured again make sure to remove and add the platforms 
you are working on.

```sh
cordova platform remove ios android
cordova plugin remove cordova-plugin-onegini
cordova plugin add https://github.com/Onegini/onegini-cordova-plugin
cordova platform add ios android
```

>**NB.** make sure that you only add / remove the platforms that you are working on. The example above uses both Android and iOS.