/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2015 Maxime Dor
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

package org.altherian.tool;

import org.altherian.tool.logging.Logger;

public class Version {

   private String _rawVersion = "";
   private int _major = 0;
   private int _minor = 0;
   private int _patch = 0;
   private String _revision = "";

   private boolean isValid = false;
   private String error = "";

   public static final Version UNKNOWN = new Version("0.0.0");

   public Version(String rawVersion) {
      if (AxStrings.isEmpty(rawVersion)) {
         isValid = false;
         error = "Version cannot be null or empty";
         return;
      }
      _rawVersion = rawVersion;

      String[] mainSplit = rawVersion.split("-");
      if (mainSplit.length > 1) {
         _revision = mainSplit[1];
      }

      String[] versionSplit = mainSplit[0].split("\\.", 3);
      if (versionSplit.length != 3) {
         isValid = false;
         error = "Version is not in x.y.z format";
         return;
      }

      try {
         _major = Integer.parseInt(versionSplit[0]);
         _minor = Integer.parseInt(versionSplit[1]);
         _patch = Integer.parseInt(versionSplit[2]);
         isValid = true;
      } catch (Throwable e) {
         isValid = false;
         error = e.getMessage();
         Logger.exception(e);
      }
   }

   public boolean isValid() {
      return isValid;
   }

   public String getError() {
      return error;
   }

   public void validate() {
      if (!isValid()) {
         throw new RuntimeException("Invalid version: " + _rawVersion);
      }
   }

   public boolean isCompatible(String otherVersion) {
      return isCompatible(new Version(otherVersion));
   }

   public boolean isCompatible(Version otherVersion) {
      return (otherVersion != null) && otherVersion.isValid() && isValid() && (getMajor() == otherVersion.getMajor())
            && (getMinor() >= otherVersion.getMinor());
   }

   public int getMajor() {
      validate();
      return _major;
   }

   public int getMinor() {
      validate();
      return _minor;
   }

   public int getPatch() {
      validate();
      return _patch;
   }

   public String getRevision() {
      validate();
      return _revision;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((_rawVersion == null) ? 0 : _rawVersion.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof Version)) {
         return false;
      }
      Version other = (Version) obj;
      if (_rawVersion == null) {
         if (other._rawVersion != null) {
            return false;
         }
      } else if (!_rawVersion.equals(other._rawVersion)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return _rawVersion;
   }

   public String getFormated() {
      return _major + "." + _minor + "." + _patch + (AxStrings.isEmpty(_revision) ? "" : "-" + _revision);
   }

}
