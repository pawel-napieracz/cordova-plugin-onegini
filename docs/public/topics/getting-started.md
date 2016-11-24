# Getting started

<!-- toc -->

## Building Cordova Applications

To be able to create an Apache Cordova project you'll need to install several tools. Refer to the [Apache Cordova getting started guide](https://cordova.apache.org/#getstarted) if you need help setting up your Cordova environment.

> Note that if you want to create an iOS application you'll need a Mac to build and debug your application.

You may add the Onegini Cordova Plugin to your existing app, but if you're starting from scratch, you will need to create a Cordova Project first:

```sh
cordova create <PATH> [ID [NAME [CONFIG]]] [options]
```    

Example:
```sh
cordova create ./myapp com.mycompany.myapp "My App"
```

## Access the Onegini Repository

The Onegini Cordova Plugin is a wrapper for the Onegini SDK. For your Cordova app to access the Onegini SDK you will need access to the [Onegini Repository](https://repo.onegini.com/). If you do not have login credentials for the Onegini Repository, you will need to [obtain those first](https://docs.onegini.com/app-developer-quickstart.html#step1). 
After obtaining credentials, store them in your local `gradle.properties` file.

In `~/.gradle/gradle.properties`:
```
artifactory_user=<username>
artifactory_password=<password>
```

## Install the SDK Configurator

For the Cordova plugin to be able to configure your Cordova app for use with your token server, you will need to install the configurator in your `$PATH`.

Download the latest 3.x release for your platform from [release page](https://github.com/Onegini/onegini-sdk-configurator/releases) and extract the zip file.

### Add the configurator to your `$PATH`

If you do not have directory to add executables, create one first:

```sh
mkdir ~/bin
```

Copy the extracted binary to your executables directory:

```sh
cp /path/to/my/download ~/bin
```

Make sure to add your executable directory to your `$PATH`. For bash shell, edit `~/.bashrc`.

```sh
echo 'export PATH=$HOME/bin:$PATH' >> ~/.bashrc && . ~/.bashrc
```

## Add the plugin to your project

```sh
cordova plugin add https://github.com/Onegini/cordova-plugin-onegini
```

### Configure your preferences

The following preferences can be set to configure the Onegini Cordova plugin:

| Preference | Default value | Description |
| --- | --- | --- |
| OneginiRootDetectionEnabled | true | Enable Root detection in your application. This disable your application on rooted Android devices. |
| OneginiDebugDetectionEnabled | true | Enable Debug detection. This will disable attempts to attach a debugger to your application on Android and iOS. |
| OneginiGcmSenderId | None | The GCM Sender ID to receive push notifications on Android |

You can set these preferences in your Cordova app's `config.xml`.

```xml
<preference name="Preference" value="Value"/>
```

Example:
```xml
<preference name="OneginiRootDetectionEnabled" value="false"/>
<preference name="OneginiDebugDetectionEnabled" value="false"/>
<preference name="OneginiGcmSenderId" value="586427927998"/>
```

For more information refer to the [Cordova Documentation](https://cordova.apache.org/docs/en/latest/config_ref/#preference).


### Configure your app for your Token Server

For the Onegini SDK to connect to your Token Server, you will need to add the configuration zip file for your platform version to your project. You can download this zip from your Token Server admin console under `Configuration > Applications > My App`. Under _Platform versions_ click `Actions > Export Platform version config`.

Once you have obtained the zip file for your platform place it in your project root as `onegini-config-android.zip` or `ongegini-config-ios.zip` depending on your platform. The SDK Configurator will pick up these files when you add a platform.

Alternatively, you can [run the configurator from the command line](https://github.com/Onegini/onegini-sdk-configurator#usage).

### Add a Cordova platform

When you add a platform to your Cordova app, the plugin will automatically resolve the SDK dependencies.

```sh
cordova platform add android ios
```

### Run your app

You are now ready to run your application!

```sh
cordova run android ios
```
