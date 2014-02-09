/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

!define VERSION "@SERVER_VERSION@"
Name "Hyperbox Server"
OutFile "@SERVER_INSTALLER_OUTPUT@"
InstallDir "@SERVER_INSTALL_DIR@"
RequestExecutionLevel admin

Page directory
Page components
Page instfiles

Section "Core files"
SetOutPath $INSTDIR
File /r "@SERVER_OUT_BIN_DIR@\*.*"
WriteUninstaller $INSTDIR\uninstaller.exe
SectionEnd

Section "Start Menu Shortcuts"
CreateDirectory "$SMPROGRAMS\Hyperbox\Server"
CreateShortCut "$SMPROGRAMS\Hyperbox\Server\Uninstall.lnk" "$INSTDIR\uninstaller.exe"
SectionEnd

Section "Install Service"
ExecWait '$INSTDIR\bin\hboxd.exe install hboxd --DisplayName="Hyperbox" --Install="$INSTDIR\bin\hboxd.exe" --Jvm=auto --StartMode=jvm --StopMode=jvm --StartClass="org.altherian.hboxd.HyperboxService" --StartMethod=start --StopClass="org.altherian.hboxd.HyperboxService" --StopMethod=stop --Startup=auto --Classpath="$INSTDIR\bin\*;$INSTDIR\lib\*;$INSTDIR\modules\*"'
SectionEnd

Section "Start Service"
ExecWait '$INSTDIR\bin\hboxd.exe start hboxd'
SectionEnd

Section "Uninstall"
ExecWait '$INSTDIR\bin\hboxd.exe stop hboxd'
ExecWait '$INSTDIR\bin\hboxd.exe delete hboxd'
RMDir /r "$INSTDIR"
RMDir /r "$SMPROGRAMS\Hyperbox\Server"
RMDir "$SMPROGRAMS\Hyperbox"
SectionEnd