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

package org.altherian.hboxd;

import org.altherian.hboxd.controller.Controller;

import java.util.Arrays;
import java.util.HashSet;

public class HyperboxService {
   
   private static Controller c;
   
   public static void main(String[] args) throws Exception {
      start(args);
   }
   
   public static void start(String[] args) throws Exception {
      Hyperbox.processArgs(new HashSet<String>(Arrays.asList(args)));

      c = new Controller();
      c.start();
   }
   
   public static void stop(String[] args) throws Exception {
      c.stop();
   }
   
}
