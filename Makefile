.PHONY: all clean
default: all ;

clean:
	rm -rf build

modules:
	git submodule update --recursive --init
build/progress/api/installed: modules
	cd modules/api; ./gradlew clean build install
	mkdir -p build/progress/api
	touch $@
build/.client: build/progress/api/installed
	cd modules/client; ./gradlew build distLinuxZip distLinuxInstaller distWinZip distWinInst;  mv build/distributions/* ../../build/
	touch build/.client
modules/server/lib/vbox: modules
	mkdir -p modules/server/lib/vbox
modules/server/lib/vbox/vboxjws-6.0.jar: modules/server/lib/vbox
	mkdir -p build/tmp/vbox/6_0
	wget -P build/tmp/vbox/6_0 -N https://download.virtualbox.org/virtualbox/6.0.24/VirtualBoxSDK-6.0.24-139119.zip
	unzip -o build/tmp/vbox/6_0/VirtualBoxSDK-6.0.24-139119.zip sdk/bindings/webservice/java/jax-ws/vboxjws.jar -d build/tmp/vbox/6_0/
	cp build/tmp/vbox/6_0/sdk/bindings/webservice/java/jax-ws/vboxjws.jar $@
modules/server/lib/vbox/vboxjws-6.1.jar: modules/server/lib/vbox
	mkdir -p build/tmp/vbox/6_1
	wget -P build/tmp/vbox/6_1 -N https://download.virtualbox.org/virtualbox/6.1.30/VirtualBoxSDK-6.1.30-148432.zip
	unzip -o build/tmp/vbox/6_1/VirtualBoxSDK-6.1.30-148432.zip sdk/bindings/webservice/java/jax-ws/vboxjws.jar -d build/tmp/vbox/6_1/
	cp build/tmp/vbox/6_1/sdk/bindings/webservice/java/jax-ws/vboxjws.jar $@
build/progress/server/built: build/progress/api/installed \
  modules/server/lib/vbox/vboxjws-6.0.jar modules/server/lib/vbox/vboxjws-6.1.jar
	cd modules/server; ./gradlew build buildDist
	mkdir -p build/distributions/server
	mv modules/server/build/distributions/* build/distributions/server/
	mkdir -p build/progress/server; touch $@

build/progress/client/built: build/progress/api/installed
	cd modules/client; ./gradlew build distLinuxZip distLinuxInstaller distWinZip distWinInst
	mkdir -p build/distributions/client
	mv modules/client/build/distributions/* build/distributions/client/
	mkdir -p build/progress/client; touch $@
all: build/progress/server/built build/progress/client/built
