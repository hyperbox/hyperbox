package org.altherian.hboxc.front.gui.network;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.hypervisor.GuestNetworkInterfaceOutput;
import org.altherian.hbox.comm.output.network.NetworkInterfaceOutput;
import org.altherian.helper.swing.JTextFieldUtils;
import org.altherian.tool.StringTools;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkInterfaceSummary {
   
   private NetworkInterfaceSummary() {
      
   }
   
   public static JPanel get(NetworkInterfaceOutput nicOut, GuestNetworkInterfaceOutput gNicOut) {
      JPanel panel = new JPanel(new MigLayout("ins 0"));
      
      JTextField nicValue = JTextFieldUtils.createAsLabel(nicOut.getAdapterType() + " using " + nicOut.getAttachMode()
            + (StringTools.isEmpty(nicOut.getAttachName()) ? "" : " on " + nicOut.getAttachName()));
      JTextField ipv4Value = JTextFieldUtils.createAsLabel(nicOut.getMacAddress());
      if (gNicOut != null) {
         ipv4Value.setText(ipv4Value.getText() + " / " + gNicOut.getIp4Address() + " / " + gNicOut.getIp4Subnet());
      } else {
         ipv4Value.setText(ipv4Value.getText() + " / IP information not available");
      }
      
      panel.add(new JLabel("Adapter " + (nicOut.getNicId() + 1)), "wrap");
      panel.add(new JLabel(""));
      panel.add(nicValue, "wrap");
      panel.add(new JLabel(""));
      panel.add(ipv4Value, "wrap");
      
      return panel;
   }
   
}
