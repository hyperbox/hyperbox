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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.altherian.hboxc.updater;

import java.net.URL;
import java.util.Date;

public class Release implements _Release {
   
   private String channel;
   private String version;
   private String revision;
   private Date date;
   private URL downloadUrl;
   private URL changelogUrl;
   
   public Release(String channel, String version, String revision, Date releaseDate, URL downloadUrl, URL changelogUrl) {
      this.channel = channel;
      this.version = version;
      this.revision = revision;
      this.downloadUrl = downloadUrl;
      this.changelogUrl = changelogUrl;
   }
   
   @Override
   public String getChannel() {
      return channel;
   }
   
   @Override
   public String getVersion() {
      return version;
   }
   
   @Override
   public String getRevision() {
      return revision;
   }
   
   @Override
   public Date getDate() {
      return date;
   }
   
   @Override
   public URL getChangeLogURL() {
      return changelogUrl;
   }
   
   @Override
   public URL getDownloadURL() {
      return downloadUrl;
   }
   
}
