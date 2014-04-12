package org.altherian.hboxd.comm.io.factory;

import org.altherian.hbox.comm.io.SettingIO;
import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hbox.constant.HostAttributes;
import org.altherian.hboxd.host._Host;

import java.util.ArrayList;
import java.util.List;

public class HostIoFactory {
   
   private HostIoFactory() {
      // static class, cannot be instantiated
   }
   
   public static HostOutput get(_Host host) {
      List<SettingIO> sIoList = new ArrayList<SettingIO>();
      sIoList.add(new SettingIO(HostAttributes.Hostname, host.getHostname()));
      sIoList.add(new SettingIO(HostAttributes.OperatingSystemName, host.getOSName()));
      sIoList.add(new SettingIO(HostAttributes.OperatingSystemVersion, host.getOSVersion()));
      sIoList.add(new SettingIO(HostAttributes.MemoryTotal, host.getMemorySize()));
      sIoList.add(new SettingIO(HostAttributes.MemoryAvailable, host.getMemoryAvailable()));
      return new HostOutput(sIoList);
   }
   
}
