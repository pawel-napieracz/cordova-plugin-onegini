# App secret generation

In order to allow the application to perform successfully communicate with the Token Server a signature of the app needs to be calculated and provided to the 
application configuration in the Token Server administration console.

**Development mode**
In order to easily develop your application without having to calculate the signature and update the application configuration after every build you can enable 
[development mode](https://docs.onegini.com/public/token-server/topics/mobile-apps/app-configuration/app-configuration.html#development-mode).

> **NB**: Please note that each platform uses its own dedicated BinaryHashCalculator instance.

## Android

Since version 3.02.01 of the Android SDK it uses a calculated binary signature. This provides additional security protecting an application against tampering/modification.

#### Calculating the value
Navigate to the 'binaryhashcalculator' folder and from the command line execute:
```bash
java com.onegini.mobile.hashCalc.HashCalculator PATH_TO_BINARY_FILE
```

If the provided path is valid the program will print the calculated hash value. 
```bash
Calculated hash - a491d0374840ac684d6bcb4bf9fc93ee4d9731dbe2996b5a1db2313efb42b7e
```

## iOS

Since version 3.02.00 the iOS SDK calculates a binary signature. This provides additional security protecting an application against tampering/modification.

#### Calculating the value
Navigate to 'binaryhashcalculator' folder and from the command line execute:
```bash
java -cp . com/onegini/mobile/hashCalc/iOS/HashCalculator {PATH_TO_APPLICATION_BINARY}

```

PATH_TO_APPLICATION_BINARY - path pointing to application binary equal to one ${BUILT_PRODUCTS_DIR}/${PROJECT_NAME}.app/${PROJECT_NAME} accessed from Xcode.


If provided path is valid the program will print calculated hash value. 
```bash
Calculated hash - c96de7fe7fe80a5e73b9a5af90afcc8a639506688914f1ffeb00d62f47315ea2