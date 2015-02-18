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

package org.altherian.hboxc.server;

import java.util.List;
import org.altherian.hbox.comm.io.NetServiceIO;
import org.altherian.hbox.comm.out.hypervisor.HypervisorOut;
import org.altherian.hbox.comm.out.network.NetAdaptorOut;
import org.altherian.hbox.comm.out.network.NetModeOut;
import org.altherian.hbox.comm.out.storage.MediumOut;
import org.altherian.hbox.hypervisor._MachineLogFile;
import org.altherian.hboxd.exception.net.InvalidNetworkModeException;
import org.altherian.hboxd.exception.net.NetworkAdaptorNotFoundException;

public interface _Hypervisor {
   
   public HypervisorOut getInfo();
   
   public String getServerId();
   
   public String getType();
   
   public String getVendor();
   
   public String getProduct();
   
   public String getVersion();
   
   public String getRevision();
   
   public boolean hasToolsMedium();
   
   public MediumOut getToolsMedium();
   
   /**
    * List all supported network modes for the adaptors
    *
    * @return List of network modes or empty list if none is found
    */
   public List<NetModeOut> listNetworkModes();
   
   public NetModeOut getNetworkMode(String id);
   
   /**
    * List Network adaptors accessible to the VMs
    *
    * @return List of network adaptors or empty list if none is found
    */
   public List<NetAdaptorOut> listAdaptors();
   
   /**
    * List all network adaptors for the given network mode
    *
    * @param modeId Network mode ID to match
    * @return List of network adaptor of the specified network mode, or empty list if none is found
    * @throws InvalidNetworkModeException If the netmork mode does not exist
    */
   public List<NetAdaptorOut> listAdaptors(String modeId) throws InvalidNetworkModeException;
   
   public NetAdaptorOut createAdaptor(String modeId, String name) throws InvalidNetworkModeException;
   
   public void removeAdaptor(String modeId, String adaptorId) throws InvalidNetworkModeException;
   
   public NetAdaptorOut getNetAdaptor(String modId, String adaptorId) throws NetworkAdaptorNotFoundException;
   
   public NetServiceIO getNetService(String modeId, String adaptorId, String svcTypeId) throws NetworkAdaptorNotFoundException;
   
   public List<String> getLogFileList(String vmId);
   
   public _MachineLogFile getLogFile(String vmId, long logid);
   
}
