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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altherian.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StreamDumper extends Thread {
   
   private InputStream s;
   
   public StreamDumper(InputStream s) {
      this.s = s;
   }

   @Override
   public void run() {
      BufferedReader r = new BufferedReader(new InputStreamReader(s));
      try {
         String line;
         do {
            line = r.readLine();
         } while (line != null);
      } catch (Throwable t) {
         t.printStackTrace();
      } finally {
         try {
            r.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   
}
