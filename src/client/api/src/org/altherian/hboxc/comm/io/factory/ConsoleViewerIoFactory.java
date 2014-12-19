/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * hyperbox at altherian dot org
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

package org.altherian.hboxc.comm.io.factory;

import org.altherian.hboxc.comm.output.ConsoleViewerOutput;
import org.altherian.hboxc.core._ConsoleViewer;
import java.util.ArrayList;
import java.util.List;

public class ConsoleViewerIoFactory {
   
   private ConsoleViewerIoFactory() {
   }
   
   public static ConsoleViewerOutput getOut(_ConsoleViewer conView) {
      ConsoleViewerOutput out = new ConsoleViewerOutput(conView.getId(), conView.getHypervisorId(), conView.getModuleId(), conView.getViewerPath(),
            conView.getArgs());
      return out;
   }
   
   public static List<ConsoleViewerOutput> getOutList(List<_ConsoleViewer> connViews) {
      List<ConsoleViewerOutput> listOut = new ArrayList<ConsoleViewerOutput>();
      for (_ConsoleViewer conView : connViews) {
         listOut.add(getOut(conView));
      }
      return listOut;
   }
   
}
