/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxc.front.gui.notification;

import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.updater._Release;
import org.altherian.tool.logging.Logger;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@SuppressWarnings("serial")
public class UpdateAvailableNotification extends NotificationInfo {
   
   private URL downloadUrl;
   
   public UpdateAvailableNotification(_Release release) {
      downloadUrl = release.getDownloadURL();
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      setText("New update available: " + release.getVersion() + "." + release.getRevision() + " - " + downloadUrl);
      addMouseListener(new MouseListener());
   }
   
   private class MouseListener extends MouseAdapter {
      
      @Override
      public void mouseClicked(MouseEvent ev) {
         if ((ev.getButton() == MouseEvent.BUTTON1) && (ev.getClickCount() == 1)) {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
               Gui.showCopyPasteHelper("Browsing is not supported, please copy and paste the download URL from bellow", downloadUrl.toExternalForm());
            }
            
            try {
               Desktop.getDesktop().browse(downloadUrl.toURI());
            } catch (IOException e) {
               Gui.showError("Unable to browse to download location: " + e.getMessage());
               Logger.exception(e);
            } catch (URISyntaxException e) {
               Gui.showError("Unable to browse to download location: " + e.getMessage());
               Logger.exception(e);
            }
         }
      }
   }
   
}
