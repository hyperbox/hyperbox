# Hyperbox

### Release files

Packages for various platforms can be found on:

- [Github](https://github.com/hyperbox/hyperbox/releases)

### From Source

Requirements:

- Git
- Java JDK 8+
- Make

Extra build requirements for Linux packages:

- deb-dpkg
- makeself

Extra build requirements for Windows packages:
- nsis

On Debian/Ubuntu, you may install all requirements except with:
```
$ sudo apt-get install git default-jdk-headless make makeself nsis
```
To build Hyperbox binaries with client, server and all supported VirtualBox modules:
```
make all
```

Output files can be found in `./build/distribution/`

## Getting Started

You can then follow the regular instructions in the [user manual](https://apps.kamax.lu/hyperbox/manual/#il-fl).
