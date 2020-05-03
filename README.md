OpenConnect for Android
=======================

This is a VPN client for Android, based on the Linux build of
[OpenConnect](http://www.infradead.org/openconnect/).

Much of the Java code was derived from [OpenVPN for Android](https://play.google.com/store/apps/details?id=de.blinkt.openvpn&hl=en) by Arne Schwabe.  
Original OpenConnect for android can be find via [OpenConnect](https://github.com/cernekee/ics-openconnect) by Kevin Cernekee

Official releases are posted in the [Google Play](https://play.google.com/store/apps/details?id=com.github.digitalsoftwaresolutions.openconnect)

## Building from source

### Prerequisites

On the host side you'll need to install:

* Android SDK in your $PATH (both platform-tools/ and tools/ directories)
* $ANDROID\_HOME pointed at the Android SDK directory
* javac 1.8 and a recent version of Apache ant in your $PATH
* Use the Android SDK Manager to install API 19
* NDK r16b, nominally unzipped under /opt/android-ndk-r16b
* Host-side gcc, make, etc. (Red Hat "Development Tools" group or Debian build-essential)
* git, autoconf, automake, and libtool

### Compiling the external dependencies

Building OpenConnect from source requires compiling several .jar files and
native binaries from external packages.  These commands will build the binary
components and copy them into the appropriate library and asset directories:

    git clone https://github.com/cernekee/ics-openconnect
    cd ics-openconnect
    git submodule init
    git submodule update
    git submodule foreach git pull
    make -C external

This procedure only runs on a Linux PC.