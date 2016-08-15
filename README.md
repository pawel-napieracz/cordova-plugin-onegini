# Install the plugin

## Resolve dependencies
The dependencies of the plugin need to be resolved before you install the plugin in your Cordova application.

    gradle clean resolveDependencies

## Add the plugin 

To install this plugin execute the following command from your Cordova application project directory.

    cordova plugin add <path_to_this_plugin>

## Native screen support

For added security this plugin has support for some native screens. These screens are used to let the user enter sensitive data, such as a PIN.
The native screens plugin provides an example implementation of these native screens.

The native screens are only available when the `cordova-onegini-native-screens` plugin is installed along with `cordova-onegini-plugin`.

The `cordova-onegini-native-screens` plugin is available on [GitHub](https://github.com/Onegini/cordova-plugin-onegini-native-screens):
