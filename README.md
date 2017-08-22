# Hyperbox
[![Build Status](https://travis-ci.org/hyperbox/hyperbox.svg?branch=master)](https://travis-ci.org/hyperbox/hyperbox)

## Quick Start
Requirements:
- Git
- Java JDK >= 1.8

On Debian/Ubuntu:
```
$ sudo apt-get install git default-jdk
```

To build Hyperbox binaries with client, server and all supported VirtualBox modules:
```
git clone https://github.com/hyperbox/hyperbox.git
cd hyperbox
./init
./gradlew build
```
	
You will then find the Client and Server binaries for Linux/Windows ready to be run as-is in the `./build/{client|server}/bin` directory.

To start the server in foreground interactive mode, log to console (replace `<OS>`):
```
cd build/server/bin/<OS>
./hyperbox
```

To start the GUI client:
```
cd build/client/bin/<OS>
./hyperbox
```

## Installation
You will find distribution packages in the [Releases section](https://github.com/hyperbox/hyperbox/releases)

To build from source:
```
# For Linux
./gradlew distLinux

# For Windows
./gradlew distWin

# For all platforms
./gradlew distAll
```

You can then follow the regular instructions in the [user manual](https://kamax.io/hbox/manual/#il-fl).
