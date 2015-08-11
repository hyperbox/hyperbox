# Hyperbox

## Quick Start
Requirements:
- Git
- Ant
- Java JDK >= 1.6

On Debian/Ubuntu:

	$ sudo apt-get install git default-jdk ant

To build Hyperbox with client, server and all VirtualBox modules:

	git clone https://github.com/hyperbox/hyperbox.git
	cd hyperbox
	./init
	./configure
	ant deploy
	
You will then find the Client and Server binaries for Linux ready to be run as-is in the `./out/bin` directory.

To start the server in foreground interactive mode, log to console:

	cd out/bin/linux_amd64/server
	./hyperbox

To start the GUI client:

	cd out/bin/linux_amd64/client
	./hyperbox

Replace `linux_amd64` by `linux_x86` is you are running a 32 bits OS
