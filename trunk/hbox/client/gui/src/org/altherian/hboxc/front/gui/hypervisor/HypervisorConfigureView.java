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

package org.altherian.hboxc.front.gui.hypervisor;

import org.altherian.hbox.comm.in.HypervisorIn;
import org.altherian.hboxc.HyperboxClient;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.vbox.VBox4_2ConfigureView;
import org.altherian.hboxc.front.gui.vbox.VBox4_3ConfigureView;

public class HypervisorConfigureView {
   
   public static HypervisorIn getInput(String srvId) {
      String hypId = Gui.getServer(srvId).getHypervisor().getInfo().getId();
      // TODO make it generic
      if (hypId.toLowerCase().contains("vbox") && hypId.toLowerCase().contains("4.3")) {
         return VBox4_3ConfigureView.getInput(srvId);
      }
      else if (hypId.toLowerCase().contains("vbox") && hypId.toLowerCase().contains("4.2")) {
         return VBox4_2ConfigureView.getInput(srvId);
      }
      else {
         HyperboxClient.getView().postError("No Configuration GUI module for this hypervisor: " + hypId);
         return null;
      }
      
   }
   
}
