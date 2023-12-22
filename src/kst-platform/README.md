# KickStartTrustee

This is the repository of the KickStartTrustee data trustee. KickStartTrustee is a blueprint project that you can adjust and extend to create your own customized data trustee.

## Getting started

You want to setup your own instance? Have a look at the [Setup Guide](docs/SETUP.md).

## Build

You need JDK 17 and Node.JS 18 (including npm) to build this project.

### Backend

Unix/Linux:
```
./gradlew build
```

Windows:
```
gradlew.bat build
```

### Owner User Interface

1. Go to the ui/owner directory
2. Execute `npm install`
3. Execute `npm run build`
