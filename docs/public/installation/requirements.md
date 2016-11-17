# Requirements

To use the Onegini Cordova plugin you will need to be able to build Cordova projects and have the [Onegini SDK Configurator](https://github.com/Onegini/onegini-sdk-configurator) installed in your `$PATH`.

## Building Cordova applications
To be able to create an Apache Cordova project you'll need to install several tools. Everything is explained perfectly on the Cordova website, so please refer to the [Apache Cordova getting started guide](https://cordova.apache.org/#getstarted). This will guide you through setting up npm, Cordova, Xcode, and the Android SDK's (depending on the platforms you want to target).

> Note that if you want to create an iOS application you'll need a Mac to build and debug your application.

## Installing the SDK Configurator

For the Cordova plugin to be able to configure your cordova apps for use with your token server, you will need to install the configurator in your `$PATH`.

Download the latest 3.x release for your platform from [release page](https://github.com/Onegini/onegini-sdk-configurator/releases) and extract the zip file.

### Add the configurator to your $PATH

If you do not have directory to add executables, create one first:
```sh
mkdir ~/bin
```

Copy the extracted binary to your executables directory:
```sh
cp /path/to/my/download ~/bin
```

Make sure to add your executable directory to your `$PATH`. For bash shell, edit `~/.bashrc`
```sh
echo 'export PATH=$HOME/bin:$PATH' >> ~/.bashrc
```