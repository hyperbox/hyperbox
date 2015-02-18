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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hbox.kryonet;

import com.esotericsoftware.minlog.Log;

public class KryonetDefaultSettings {
   
   static {
      Log.ERROR();
   }
   
   public static final String CFGKEY_KRYO_NET_TCP_PORT = "kryonet.net.tcp.port";
   public static final String CFGVAL_KRYO_NET_TCP_PORT = "45612";
   
   public static final String CFGKEY_KRYO_NET_WRITE_BUFFER_SIZE = "kryonet.net.buffer.write";
   public static final String CFGVAL_KRYO_NET_WRITE_BUFFER_SIZE = "1838400";
   
   public static final String CFGKEY_KRYO_NET_OBJECT_BUFFER_SIZE = "kryonet.net.buffer.object";
   public static final String CFGVAL_KRYO_NET_OBJECT_BUFFER_SIZE = "1638400";
   
}
