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

#!/bin/bash

INSTALL_DIR="/opt/hboxc"

if ! [ -x $INSTALL_DIR ]; then
	mkdir $INSTALL_DIR
	if [ $? -eq 0 ]; then
		echo Created install dir
	else
		echo Fail Create install dir
		exit 1
	fi
fi

cp -r ./* $INSTALL_DIR
if [ $? -eq 0 ]; then
	echo Copy server file
else
	echo Fail copy server file
	exit 1
fi

chmod ugo+rx $INSTALL_DIR/hyperbox
if [ $? -eq 0 ]; then
        echo chmod hyperbox
else
        echo fail chmod hyperbox
        exit 1
fi

mv $INSTALL_DIR/hyperbox-client.desktop /usr/share/applications
if [ $? -eq 0 ]; then
        echo mv desktop shortcut
else
        echo fail mv desktop shortcut
        exit 1
fi

echo "Path=$INSTALL_DIR" >> /usr/share/applications/hyperbox-client.desktop
if [ $? -eq 0 ]; then
        echo add path info
else
        echo fail add path info
        exit 1
fi

echo "Exec=$INSTALL_DIR/hyperbox" >> /usr/share/applications/hyperbox-client.desktop
if [ $? -eq 0 ]; then
        echo add exec info
else
        echo fail add exec info
        exit 1
fi

echo "Icon=$INSTALL_DIR/icons/hyperbox.ico" >> /usr/share/applications/hyperbox-client.desktop
if [ $? -eq 0 ]; then
        echo add icon info
else
        echo fail add icon info
        exit 1
fi

rm $INSTALL_DIR/$0
if [ $? -eq 0 ]; then
        echo cleanup
else
        echo fail cleanup
        exit 1
fi

