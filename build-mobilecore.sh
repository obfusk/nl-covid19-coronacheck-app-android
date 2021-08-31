#!/bin/bash
set -e
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
git clone https://github.com/minvws/nl-covid19-coronacheck-mobile-core tmp-mobilecore
cd tmp-mobilecore
git checkout d7731562f5c142bab624b7209f526e271c4c8c9d
go get golang.org/x/mobile/cmd/gomobile
go get golang.org/x/mobile/cmd/gobind@latest
gomobile init
gomobile bind -target android -o mobilecore.aar github.com/minvws/nl-covid19-coronacheck-mobile-core
cd ..
cp tmp-mobilecore/mobilecore.aar mobilecore
rm -fr tmp-mobilecore
