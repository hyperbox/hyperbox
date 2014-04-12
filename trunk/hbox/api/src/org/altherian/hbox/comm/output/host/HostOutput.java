package org.altherian.hbox.comm.output.host;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.HostAttributes;

import java.util.Collection;

public class HostOutput extends ObjectOutput {
   
   @SuppressWarnings("unused")
   private HostOutput() {
      // Used for serialization
   }
   
   public HostOutput(Collection<SettingIO> sIoList) {
      setSetting(sIoList);
   }
   
   public String getHostname() {
      return getSetting(HostAttributes.Hostname).getString();
   }
   
   public String getOSName() {
      return getSetting(HostAttributes.OperatingSystemName).getString();
   }
   
   public String getOSVersion() {
      return getSetting(HostAttributes.OperatingSystemVersion).getString();
   }
   
   public Long getMemorySize() {
      return getSetting(HostAttributes.MemoryTotal).getNumber();
   }
   
   public Long getMemoryAvailable() {
      return getSetting(HostAttributes.MemoryAvailable).getNumber();
   }
   
}
