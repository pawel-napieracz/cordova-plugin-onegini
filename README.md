# Onegini Cordova Example app

This app serves as a simple example implementation for the [Onegini Cordova Plugin](https://github.com/onegini/cordova-plugin-onegini).

## Source structure
This app is build with the [Vue.js](https://vuejs.org) framework.
Every view has it's own component in `src/views`, containing all it's HTML, JS and CSS.
Components (like buttons or lists for specific items) are located in `src/components`.

## Running the Example app

### Perquisites
To run the Example App, you need to be able to build Cordova apps for Android and iOS on your machine.
Refer to the [Apache Cordova getting started guide](https://cordova.apache.org/#getstarted) if you need help setting up your Cordova environment.

Make sure you have the [SDK Configurator](https://github.com/Onegini/onegini-sdk-configurator) installed in your `$PATH`.
(See the [getting started guide](https://docs.onegini.com/msp/latest/cordova-plugin/topics/getting-started.html#install-the-sdk-configurator) of the Onegini Cordova Plugin)

### Building
Clone this repository
```sh
git clone https://github.com/onegini/cordova-example-app.git
cd cordova-example-app
```

Build www assets
```
npm install
npm run build
```

Add the Android and/or iOS platform
```sh
cordova platform add (android|ios)...
```

Run the example app
```sh
cordova run (android|ios)...
```
