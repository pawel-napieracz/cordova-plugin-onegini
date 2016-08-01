# Getting started

The `cordova-plugin-onegini` is an Apache Cordova plugin build to allow the use of Onegini's native SDKs to create Cordova applications.

The plugin currently supports the following platforms:
  - Android
  - iOS

The Plugin allows to perform native code invocations on the Onegini SDK directly from the Javascript layer of a Cordova application. To read more about the 
plugin's public APIs documentation see the [Interface](interface.md) chapter.

The plugin uses a two hooks to download the iOS SDK and configure the SDK with the given Token Server configuration.
1. Downloading the iOS SDK - This hook is triggered after the plugin is installed (`after_plugin_install`). In order for this hook to complete successfully 
you must follow the [installation steps](installation.md).
2. Configuring the Onegini SDK - This hook is triggered after a platform is added (`after_platform_add`). The Onegini SDK requires some initial configuration 
for some of the security related features. To make this easy we have integrated the Cordova plugin with the 
[Onegini SDK configurator](https://github.com/Onegini/sdk-configurator). This hook will assume that you have installed the SDK configurator in your $PATH and 
have downloaded the Token Server configuration for your application to the project root folder. Please follow the [installation steps](installation.md) to let 
the hook complete successfully.

The next thing we recommend to do is read the installation and configuration instructions before you start 