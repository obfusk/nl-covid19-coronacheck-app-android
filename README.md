# FOSS version

NB: it's not really a "fork", just a few patches for F-Droid, no other
divergence from upstream.

## CoronaCheck

<a href="https://f-droid.org/packages/nl.rijksoverheid.ctr.holder/"><img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="62" /></a>

## Scanner for CoronaCheck

<a href="https://f-droid.org/packages/nl.rijksoverheid.ctr.verifier/"><img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="62" /></a>

## Upstream repo & issue tracker

https://github.com/minvws/nl-covid19-coronacheck-app-android

## Changes

* rm .aar (see below for building instructions)
* remove proprietary dependencies
* use zxing to scan barcodes
* hardcode version (instead of using `$GITHUB_RUN_NUMBER`)
* CI: build mobilecore.aar, only build prod APKs
* remove non-free images (icon is replaced, rest is made blank for now)
* disable update check
* provide triple-t metadata for F-Droid
* use old `DIGI_D_REDIRECT_URI` (new one doesn't work w/ some browsers)

## TODO

* get permission to use upstream's non-free images

## Branches & Updates

The FOSS version adds the `foss` branch: upstream's `main` + the
changes mentioned above, as well as the `holder-foss` and
`verifier-foss` branches for the 2 respective apps.

When upstream releases a new version, we merge the specific version
tag into `foss` and tag the new patched version as
`holder-foss-$VERSION` and `verifier-foss-$VERSION` respectively.

## How to build mobilecore.aar

NB: this requires the git submodule.

```sh
mkdir -p ../tmp-go
pushd ../tmp-go
if [ ! -e golang ]; then
  wget -O go.tar.gz -- https://dl.google.com/go/go1.16.7.linux-amd64.tar.gz
  printf '349f846599ca816f95f57adc41f789fdd6ade0ffcd325076de4fc3dcf06c72ae1474170ed5760e505a54a3ab10b1aa65d127f14a63cba27dec6672a1bcd2fbc6  go.tar.gz\n' | sha512sum -c
  tar xzf go.tar.gz
  mv go golang
fi
export GOPATH="$PWD"
export GO_LANG="$PWD/golang/bin"
export GO_COMPILED="$PWD/bin"
export PATH="$GO_LANG:$GO_COMPILED:$PATH"
popd
cd mobilecore-src
go get golang.org/x/mobile/cmd/gomobile
go get golang.org/x/mobile/cmd/gobind@latest
gomobile init
gomobile bind -target android -o mobilecore.aar github.com/minvws/nl-covid19-coronacheck-mobile-core
cd ..
cp mobilecore-src/mobilecore.aar mobilecore/
```

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

