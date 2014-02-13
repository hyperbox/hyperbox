#!/bin/bash
###########################################################################
#
# Hyperbox - Enterprise Virtualization Manager
# Copyright (C) 2013 Maxime Dor
# 
# http://hyperbox.altherian.org
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
###########################################################################

INSTALL_DIR="/opt/hboxd"
LOG_FILE="/var/log/install-hboxd.log"
RUNAS="hyperbox"
IS_DEBIAN_BASED=false
IS_REDHAT_BASED=false
RUNAS_DECIDED=false

function displayLogo {
	echo "#       # #       # ######### ######### #########  ########   ######  #       #"
	echo "#       #  #     #  #       # #         #        # #       # #      #  #     #"
	echo "#       #   #   #   #       # #         #        # #       # #      #   #   #"
	echo "#########    # #    ######### ######### #########  ########  #      #    # #"
	echo "#       #     #     #         #         #     #    #       # #      #   #   #"
	echo "#       #     #     #         #         #      #   #       # #      #  #     #"
	echo "#       #     #     #         ######### #       #  ########   ######  #       #"
	echo ""
	echo "http://hyperbox.altherian.org"
	echo ""
}

function log {
	echo "$@" >> $LOG_FILE
}

function logandout {
	log "$@"
	echo "$@"
}

function run {
	echo "Running: $@" >> $LOG_FILE
	$@
	LOCALRETVAL=$?
	echo "Return value: $LOCALRETVAL" >> $LOG_FILE
	return $LOCALRETVAL;
}

function abort {
	logandout "$@"
	log "Abording install" >> $LOG_FILE
	echo "An error occured and the installation will now be canceled. View $LOG_FILE for more details"
	cleanUp
	echo "Installation finish at "$(date "+%d-%I-%Y @ %H:%m") >> $LOG_FILE
	exit 1
}

function cleanUp {
	if [ -f $INSTALL_DIR/$0 ]; then
		log "Removing installer script"
		rm $INSTALL_DIR/$0 2>&1 >> $LOG_FILE
	fi
}

function checkRoot {
	if [ ! $(whoami) = "root" ]; then
		echo "This script must run as root, script will now abort"
		echo "Installation finish at "$(date "+%d-%I-%Y @ %H:%m") >> $LOG_FILE
		exit 1
	fi
}

# Return 0 if Java is at least the given version passed as parameter
# Return 1 if Java is not at least the version
# Return 2 if Java is not found
# Credit : Glenn Jackman @ http://stackoverflow.com/questions/7334754
function checkJavaVersion {
	log "Checking Java version"
	java -version >> $LOG_FILE 2>&1
	RETVAL=$?
	echo "" >> $LOG_FILE
	if [ $RETVAL -eq 0 ]
	then
		_java=java
	elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]
	then
		_java="$JAVA_HOME/bin/java"
	else
		return 2
	fi

	if [[ "$_java" ]]; then
		version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
		if [[ "$version" > "$1" ]]; then
			return 0
		else
			return 1
		fi
	fi
}

function askUserConf {
	USER_CONF_MSG="Do you want to continue?"
	if [ $# -gt 0 ]; then
		USER_CONF_MSG="$@"
	fi
	
	USER_INPUT="no"
	read -p "$USER_CONF_MSG [Y/n] " USER_INPUT
	if [[ $USER_INPUT =~ ^[Yy]$ ]]; then
		return 0
	else
		abort "User canceled"
	fi
}

function askUserConfOrContinue {
	USER_CONF_MSG="Do you want to continue?"
	if [ $# -gt 0 ]; then
		USER_CONF_MSG="$@"
	fi
	
	USER_INPUT="no"
	read -p "$USER_CONF_MSG [Y/n] " USER_INPUT
	if [[ $USER_INPUT =~ ^[Yy]$ ]]; then
		return 0
	else
		return 1
	fi
}

function getDedicatedUserInput {
	logandout "A dedicated user is required to run Hyperbox."
	logandout "If the user does not already exist, it will be created automatically"
	read -e -p "Please enter a dedicated username to run Hyperbox [$RUNAS]: " RUNAS_INPUT
	if [[ $RUNAS_INPUT = "" ]]; then
		log "User didn't enter any username, using default"
	else
		log "Got user input for user: $RUNAS_INPUT"
		RUNAS=$RUNAS_INPUT	
	fi
}

function checkRequirements {
	if [ -f /etc/debian-release ]; then
		logandout "Debian-based system detected"
		log $(cat /etc/debian-release)
		IS_DEBIAN_BASED=true
	elif [ -f /usr/bin/apt-get ]; then
		logandout "Debian-based system detected"
		IS_DEBIAN_BASED=true
	elif [ -f /etc/redhat-release ]; then
		logandout "Redhat-based system detected"
		log $(cat /etc/redhat-release)
		IS_REDHAT_BASED=true
	else
		abort "Unsupported system. You can manually install by using the ZIP package and following the Manual Install steps in the User Manual"
	fi
	
	if [ -x /etc/init.d/hboxd ]; then
		/etc/init.d/hboxd status >> $LOG_FILE 2>&1
		if [ $? -eq 0 ]; then
			logandout "hboxd must be stopped before continuing. The script will handle the shutdown."
			askUserConf
			logandout "Shutting down hboxd..."
			
			COULD_STOP_HBOXD=1
			if $IS_REDHAT_BASED; then
				service hboxd stop >> $LOG_FILE 2>&1
				COULD_STOP_HBOXD=$?
			else
				/etc/init.d/hboxd stop >> $LOG_FILE 2>&1
				COULD_STOP_HBOXD=$?
			fi
			
			if [ $? -ne 0 ]; then
				abort "Unable to stop hboxd, aborting...";
			fi
		fi
	fi

	if [ -x $INSTALL_DIR ]; then
		logandout "$INSTALL_DIR already exists, care should be taken in using this directory"
		askUserConf
	fi

	checkJavaVersion 1.6
	RETVAL=$?
	if [ $RETVAL -eq 0 ]
	then
		logandout "Found suitable java version (>= 1.6), continuing..."
	elif [ $RETVAL -eq 1 ]
	then
		abort "hboxd requires Java 1.6 or later."
	else
		abort "Java is reqired by hboxd but no Java was found."
	fi
	
	
	if ! $RUNAS_DECIDED && [ -f /etc/init.d/hboxd ]; then
		log "hbox init.d found, parsing for default value"
		RUNAS_HBOX=$(grep "^RUNAS" /etc/init.d/hboxd | cut -d"=" -f2 | tr -d "\"")
		log "Possible hbox user: $RUNAS_HBOX"
		if ! [ "$RUNAS_HBOX" == "" ]; then
			log "Dedicated user found in hbox init.d file: $RUNAS_HBOX"
			RUNAS=$RUNAS_HBOX
			RUNAS_DECIDED=true
		else
			log "No dedicated user was found in hbox init.d file"
		fi
	fi
	
	
	if ! $RUNAS_DECIDED && [ -f /etc/default/virtualbox ]; then
		log "vbox init.d config found, parsing for default value"
		RUNAS_VBOX=$(grep "VBOXWEB_USER" /etc/default/virtualbox | cut -d"=" -f2)
		if ! [ "$RUNAS_VBOX" == "" ]; then
			log "Dedicated user found in vbox init.d file: $RUNAS_VBOX"
			askUserConfOrContinue "$RUNAS_VBOX was detected as a dedicated user. Use it?"
			if [ $? -eq 0 ]; then
				log "User validated the usage of $RUNAS_VBOX as dedicated user"
				RUNAS=$RUNAS_VBOX
				RUNAS_DECIDED=true
			else
				log "User denied the usage of $RUNAS_VBOX as dedicated user"
			fi
		else
			log "No dedicated user was found in vbox init.d file"
		fi
	fi
	if ! $RUNAS_DECIDED; then
		getDedicatedUserInput
		grep $RUNAS /etc/passwd > /dev/null 2>&1
		if [ $? -ne 0 ]; then
			log "User $RUNAS does not exist, creating the user"
			useradd -m $RUNAS
		else
			log "User $RUNAS already exists"
		fi
	fi
	
	log "Hyperbox will run under dedicated user: $RUNAS"
}

function copyFiles {
	if ! [ -x $INSTALL_DIR ]; then
	        mkdir $INSTALL_DIR >> $LOG_FILE 2>&1
	        if [ $? -ne 0 ]; then
	        	abort "Failed to create install dir"
		fi
	fi
	
	echo Will install to $INSTALL_DIR
	cp -r ./* $INSTALL_DIR >> $LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
	        abort "Failed to copy hboxd files"
	fi
	
	chown -R $RUNAS:$RUNAS $INSTALL_DIR
	if [ $? -ne 0 ]; then
		abort "Failed to set permissions on install dir"
	fi

	chmod ugo+rx $INSTALL_DIR/bin/hboxd 2>&1 >> $LOG_FILE
	if [ $? -ne 0 ]; then
	        abort "Failed to set permission on hboxd files"
	fi
}

function installDaemon {
	logandout "Install Daemon..."
	
	INSTDIR_SED="s#.*#INSTALL_DIR=$INSTALL_DIR#"
	INSTDIR_LN=$(grep -m 1 -n "^INSTALL_DIR=" $INSTALL_DIR/hboxd | awk -F ":" '{print $1}')
	sed -i $INSTDIR_LN$INSTDIR_SED  $INSTALL_DIR/hboxd >> $LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
		abort "Failed to configure service init.d script"
    fi
	
	RUNAS_SED="s/.*/RUNAS=$RUNAS/"
	RUNAS_LN=$(grep -m 1 -n "^RUNAS=" $INSTALL_DIR/hboxd | awk -F ":" '{print $1}')
	sed -i $RUNAS_LN$RUNAS_SED $INSTALL_DIR/hboxd >> $LOG_FILE 2>&1
    if [ $? -ne 0 ]; then
		abort "Failed to configure service init.d script"
    fi

	mv $INSTALL_DIR/hboxd /etc/init.d/hboxd >> $LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
	        abort "Failed to relocated init.d script to /etc/init.d"
	fi

	log "Chmod ugo+rx /etc/init.d/hboxd"
	chmod ugo+rx /etc/init.d/hboxd >> $LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
	        abort "Failed to set execute permission on init.d script"
	fi
	
	log "chown root:root /etc/init.d/hboxd: $?"
	chown root:root /etc/init.d/hboxd >> $LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
	        abort "Failed to change owner to root on init.d script"
	fi

	if $IS_DEBIAN_BASED; then
		log "Registering init.d using update-rc.d"
		update-rc.d hboxd defaults 90 16 >> $LOG_FILE 2>&1
		INITD_REG=$?
	elif $IS_REDHAT_BASED; then
		log "Registering init.d using ckconfig"
		chkconfig --add hboxd
		INITD_REG=$?
	else
		logandout "Unsupported system, cannot register init.d script"
		INITD_REG=1
	fi
	if [ $INITD_REG -ne 0 ]; then
	        abort "Failed to register init.d script with the system"
	fi
	
	log "Starting hboxd daemon"
	if $IS_REDHAT_BASED; then
		service hboxd start
		log "Return code of init.d start: $?"
	else
		/etc/init.d/hboxd start
		log "Return code of init.d start: $?"
	fi
	
}

function parseParameters {
	if [ $# -gt 0 ]; then
		log "Parameters given: $@"
		INSTALL_DIR="$@"
	else
		log "No parameters were given to the script"
	fi
}

echo "Installation start at "$(date "+%d-%I-%Y @ %H:%m") > $LOG_FILE
parseParameters $@
displayLogo
checkRoot
checkRequirements
copyFiles
installDaemon
cleanUp
echo "Installation finish at "$(date "+%d-%I-%Y @ %H:%m") >> $LOG_FILE

echo "The Hyperbox Server is now installed and running."
echo "To get started, please read the User Manual located in the doc directory, or visit http://hyperbox.altherian.org"
echo
