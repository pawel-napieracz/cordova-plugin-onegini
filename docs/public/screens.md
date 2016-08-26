# Native or HTML screens?

The Cordova plugin supports both HTML and Native screens for the authentication flows. The native screens are considered more secure and therefore recommended 
for apps where security really important.

## HTML screens

The HTML screens and resources are located in the example Cordova application. If you want access to the example Cordova application please request this at 
Onegini Support.

## Native screens

An example implementation of the native screens is located in a separate plugin: `cordova-onegini-native-screens`.

In order to change UI elements, like buttons, texts or background images you have to modify/override the drawables located in the `cordova-onegini-native-screens`. 
When customizing any drawables or views in this plugin we strongly recommend **forking** and **renaming** this plugin instead of editing the project directly.
