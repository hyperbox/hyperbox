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

package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.out.security.PermissionOut;
import org.altherian.hboxd.security._ActionPermission;
import org.altherian.hboxd.security._ItemPermission;
import org.altherian.hboxd.security._User;

public class PermissionIoFactory {

   private PermissionIoFactory() {
      // static only
   }

   public static PermissionOut get(_User usr, _ActionPermission perm) {
      return new PermissionOut(usr.getId(), perm.getItemType().toString(), perm.getAction().toString(), perm.isAllowed());
   }

   public static PermissionOut get(_User usr, _ItemPermission perm) {
      return new PermissionOut(usr.getId(), perm.getItemType().toString(), perm.getAction().toString(), perm.getItemId(), perm.isAllowed());
   }

}
