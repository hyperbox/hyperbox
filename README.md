# Hyperbox
[![Build Status](https://travis-ci.org/hyperbox/hyperbox.svg?branch=master)](https://travis-ci.org/hyperbox/hyperbox)
## Quick Start
Requirements:
- Git
- Java JDK >= 1.7

On Debian/Ubuntu:

	$ sudo apt-get install git default-jdk

To build Hyperbox with client, server and all supported VirtualBox modules:

	git clone https://github.com/hyperbox/hyperbox.git
	cd hyperbox
	./init
	./gradlew build
	
You will then find the Client and Server binaries for Linux ready to be run as-is in the `./build/bin` directory.

To start the server in foreground interactive mode, log to console:

	cd build/bin/server
	./hyperbox

To start the GUI client:

	cd build/bin/client
	./hyperbox

You can then follow the regular instructions in the [user manual](https://kamax.io/hbox/manual/#il-fl).
