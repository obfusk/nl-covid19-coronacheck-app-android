# FOSS version

NB: it's not really a "fork", just a few patches for F-Droid, no other
divergence from upstream.

## Upstream repo & issue tracker

https://github.com/minvws/nl-covid19-coronacheck-app-android

## Changes

* rm .aar & add build-mobilecore.sh
* remove proprietary dependencies
* use zxing to scan barcodes
* hardcode version (instead of using $GITHUB_RUN_NUMBER)
* CI: build mobilecore.aar, only build prod APKs
* remove non-free images (icon is replaced, rest is made blank for now)
* disable update check

## TODO

* update mobilecore
* provide triple-t metadata for F-Droid

## Branches & Updates

The FOSS version adds the `foss` branch: upstream's `main` + the
changes mentioned above, as well as the `holder-foss` and
`verifier-foss` branches for the 2 respective apps.

When upstream releases a new version, we merge the specific version
tag into `foss` and tag the new patched version as
`holder-foss-$VERSION` and `verifier-foss-$VERSION` respectively.

# COVID-19 CoronaCheck Prototype - Android

## Introduction
This repository contains the Android prototype of the Dutch COVID-19 CoronaCheck project.

* The Android app is located in the repository you are currently viewing.
* The iOS app can be found here: https://github.com/minvws/nl-covid19-coronacheck-app-ios

The project is currently an experimental prototype to explore technical possibilities.

## Development & Contribution process

The development team works on the repository in a private fork (for reasons of compliance with existing processes) and shares its work as often as possible.

If you plan to make non-trivial changes, we recommend to open an issue beforehand where we can discuss your planned changes.
This increases the chance that we might be able to use your contribution (or it avoids doing work if there are reasons why we wouldn't be able to use it).

Note that all commits should be signed using a gpg key.

