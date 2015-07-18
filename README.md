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
	make
	
You will then find the Client and Server binaries for Linux ready to be run as-is in the `./out/bin` directory.
